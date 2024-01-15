package org.example;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static volatile Config instance;
    private final static Logger logger = LogManager.getLogger(Config.class.getName());
    public static final String CONFIG_FILE = "websocket.properties";
    private static final String WS_HOST = "host";
    private static final String WS_PORT = "port";
    private static final String BOSS_GROUP_THREAD_COUNT = "netty.bossGroup.threadCount";
    private static final String WORKER_GROUP_THREAD_COUNT = "netty.workerGroup.threadCount";

    //Default values
    private static final Integer WS_PORT_DEFAULT = 8888;
    private static final Integer BOSS_GROUP_THREAD_COUNT_DEFAULT = 2;
    private static final Integer WORKER_GROUP_THREAD_COUNT_DEFAULT = 4;

    private static final Properties properties = new Properties();


    @SneakyThrows
    public static Config getInstance() {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();
                    try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
                        if (inputStream != null) {
                            properties.load(inputStream);
                        }
                    }
                        String wsPort = properties.getProperty(WS_PORT);
                    if (wsPort != null && !wsPort.isEmpty()) {
                        properties.setProperty(WS_PORT, wsPort);
                    } else {
                        properties.setProperty(WS_PORT, WS_PORT_DEFAULT.toString());
                    }
                }
            }
        }
        return instance;
    }


    public String getHost() {
        return Config.properties.getProperty(WS_HOST, "localhost");
    }

    public Integer getPort() {
        try {
            return Integer.parseInt(properties.getProperty(WS_PORT));
        } catch (NumberFormatException | NullPointerException e) {
            logger.warn("Port not specified", e);
            return WS_PORT_DEFAULT;
        }
    }

    public int getBossThreadCount() {
        try {
            return Integer.parseInt(properties.getProperty(BOSS_GROUP_THREAD_COUNT));
        } catch (NumberFormatException | NullPointerException e){
            logger.warn("The \"netty.bossGroup.threadCount\" not specified in \"websocket.properties\" file");
            properties.setProperty(BOSS_GROUP_THREAD_COUNT, BOSS_GROUP_THREAD_COUNT_DEFAULT.toString());
            return BOSS_GROUP_THREAD_COUNT_DEFAULT;
        }
    }

    public int getWorkerThreadCount() {
        try {
            return Integer.parseInt(properties.getProperty(WORKER_GROUP_THREAD_COUNT));
        } catch (NumberFormatException | NullPointerException e){
            logger.warn("The \"netty.workerGroup.threadCount\" not specified in \"websocket.properties\" file");
            properties.setProperty(WORKER_GROUP_THREAD_COUNT, WORKER_GROUP_THREAD_COUNT_DEFAULT.toString());
            return WORKER_GROUP_THREAD_COUNT_DEFAULT;
        }
    }

}
