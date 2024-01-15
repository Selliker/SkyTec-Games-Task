package org.example.webSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Config;

public class WsServer implements Runnable {
    private final Logger logger = LogManager.getLogger(getClass().getName());

    @Override
    public void run() {
        try {
            logger.error("WS Server starting");
            Config config = Config.getInstance();
            configureAndStartServer(config);
        } catch (Exception ex) {
            logger.error("Error while starting WS server", ex);
            System.exit(-1);
        }
    }

    private void configureAndStartServer(Config config) {

        String host = config.getHost();
        int port = config.getPort();
        logger.info("WS Server configuring on " + host + ":" + port);

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(config.getBossThreadCount());
        EventLoopGroup workerGroup = new NioEventLoopGroup(config.getWorkerThreadCount());
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebSocketServerInitializer());

            ChannelFuture bind = serverBootstrap.bind(host, port);
            logger.info("WS Server started on " + host + ":" + port);
            Channel ch = bind.sync().channel();

            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
