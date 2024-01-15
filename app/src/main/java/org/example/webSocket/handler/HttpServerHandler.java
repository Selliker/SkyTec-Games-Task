package org.example.webSocket.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Message;
import org.example.webSocket.MessageProcessor;
import org.example.webSocket.NettyWsServerHandshakeFactory;


public class HttpServerHandler extends ChannelInboundHandlerAdapter {
    private final String CONNECTION_SWITCH_PROTOCOL_VALUE = "upgrade";
    private final String UPGRADE_TO_WEBSOCKET_HEADER_VALUE = "WebSocket";

    private final Logger logger = LogManager.getLogger(getClass().getName());
    private final static NettyWsServerHandshakeFactory nettyWebSocketFactory = new NettyWsServerHandshakeFactory();
    MessageProcessor messageProcessor = new MessageProcessor();
    private WebSocketServerHandshaker webSocketServerHandshaker;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) msg;
            HttpHeaders headers = httpRequest.headers();
            if (
                headers.get(HttpHeaderNames.CONNECTION).equalsIgnoreCase(CONNECTION_SWITCH_PROTOCOL_VALUE)
                &&
                headers.get(HttpHeaderNames.UPGRADE).equalsIgnoreCase(UPGRADE_TO_WEBSOCKET_HEADER_VALUE)
            ) {
                logger.debug("Try to start new http request");
                handleHandshake(ctx, httpRequest);
                logger.debug("Handshake was started");
            } else {
                String tmp = httpRequest.toString();
                logger.warn("CHECK httpRequest: " + tmp);
            }
        }
        logger.info("It's ws request");
        if (msg instanceof BinaryWebSocketFrame) {
            onMessage(ctx, (BinaryWebSocketFrame) msg);
        } else if (msg instanceof PingWebSocketFrame) {
            onPing(ctx, (PingWebSocketFrame) msg);
        } else if (msg instanceof CloseWebSocketFrame) {
            onClose(ctx, (CloseWebSocketFrame) msg);
        } else {
            logger.error("Wrong packet size");
        }
    }

    private void handleHandshake(ChannelHandlerContext ctx, HttpRequest req) {
        if (webSocketServerHandshaker == null) {
            webSocketServerHandshaker = nettyWebSocketFactory.newHandshaker(req);
        }
        webSocketServerHandshaker.handshake(ctx.channel(), req);
    }

    private void onPing(ChannelHandlerContext ctx, PingWebSocketFrame pingWebSocketFrame) {
        ctx.channel().writeAndFlush(new PongWebSocketFrame(pingWebSocketFrame.content()));
    }

    private void onMessage(ChannelHandlerContext channelHandlerContext, BinaryWebSocketFrame binaryWebSocketFrame) {
        try {
            Message message = Message.parseFrom(ByteBufUtil.getBytes(binaryWebSocketFrame.content()));
            Message responceMessage = messageProcessor.process(message);
            channelHandlerContext.writeAndFlush(
                    new BinaryWebSocketFrame(
                            Unpooled.copiedBuffer(
                                    responceMessage.toByteArray()
                            )
                    )
            );
        } catch (InvalidProtocolBufferException e) {
            logger.error("Message parse error ", e);

        }
    }

    public void onClose(ChannelHandlerContext channelHandlerContext, CloseWebSocketFrame closeWebSocketFrame) {
        try {
                CloseWebSocketFrame closeFrame = closeWebSocketFrame.retain();
                channelHandlerContext.channel()
                        .writeAndFlush(closeFrame, channelHandlerContext.newPromise())
                        .addListener(ChannelFutureListener.CLOSE);
        } catch (Exception ex) {
            logger.error("Failed on close ", ex);
        }
    }

}
