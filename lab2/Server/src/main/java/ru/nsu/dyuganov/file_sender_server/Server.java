package ru.nsu.dyuganov.file_sender_server;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Server.class);

    private final ExecutorService threadPool;
    private final String UPLOADS_DIRECTORY;
    private final ServerSocket serverSocket;

    Server(int port) throws IOException {
        final Properties properties = new Properties();
        final String propertiesFileName = "config.properties";
        properties.load(Main.class.getClassLoader().getResourceAsStream(propertiesFileName));
        logger.debug("Properties file \"" + propertiesFileName + "\" loaded");

        final int MIN_PORT = Integer.parseInt(properties.getProperty("MIN_PORT"));
        final int MAX_PORT = Integer.parseInt(properties.getProperty("MAX_PORT"));
        if (MIN_PORT > port || MAX_PORT < port) {
            throw new IllegalArgumentException("Port is not correct");
        }
        serverSocket = new ServerSocket(port);

        final int THREAD_POOL_SIZE = Integer.parseInt(properties.getProperty("THREAD_POOL_SIZE"));
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        UPLOADS_DIRECTORY = properties.getProperty("UPLOADS_DIRECTORY");
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            Socket newConnection = serverSocket.accept();
            threadPool.submit(new ConnectionHandler(newConnection, UPLOADS_DIRECTORY));
        }
        threadPool.shutdown();
    }
}