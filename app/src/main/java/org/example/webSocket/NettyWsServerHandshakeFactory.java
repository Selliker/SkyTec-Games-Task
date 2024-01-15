package org.example.webSocket;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Config;

import static java.util.Objects.isNull;

public class NettyWsServerHandshakeFactory {

    private final static Logger logger = LogManager.getLogger(NettyWsServerHandshakeFactory.class.getName());
    private static WebSocketServerHandshakerFactory wsFactory = initWsFactory();

    private static WebSocketServerHandshakerFactory initWsFactory(){
        logger.debug("Init new WebSocketServerHandshakerFactory");
        String host = Config.getInstance().getHost();
        String webSocketURL = getWebSocketURL(host);
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(webSocketURL, null, true);
        return wsFactory;
    }

    public WebSocketServerHandshaker newHandshaker(HttpRequest req){
        logger.debug("Init new WebSocketServerHandshaker for request with headers: " + req.headers().toString());
        if(isNull(wsFactory)){
            wsFactory = initWsFactory();
        }
        return wsFactory.newHandshaker(req);
    }

    private static String getWebSocketURL(String host) {
        String url = "ws://" + host + "/chat";
        logger.debug("Constructed URL : " + url);
        return url;
    }
}
