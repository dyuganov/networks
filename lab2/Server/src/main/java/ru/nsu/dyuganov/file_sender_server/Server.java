package ru.nsu.dyuganov.file_sender_server;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Server.class);

    private final ExecutorService threadPool;
    private final String UPLOADS_DIRECTORY;
    private final ServerSocket serverSocket;

    @SneakyThrows
    Server(int port) {
        @NonNull final Configuration config = new Configurations().properties(new File(Main.PROPERTIES_FILE_NAME));
        logger.debug("Properties file \"" + Main.PROPERTIES_FILE_NAME + "\" loaded");

        final int MIN_PORT = config.getInt("MIN_PORT");
        final int MAX_PORT = config.getInt("MAX_PORT");
        if (MIN_PORT > port || MAX_PORT < port) {
            throw new IllegalArgumentException("Port is not correct");
        }
        serverSocket = new ServerSocket(port);

        final int THREAD_POOL_SIZE = config.getInt("THREAD_POOL_SIZE", 1);
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        UPLOADS_DIRECTORY = config.getString("UPLOADS_DIRECTORY", "/uploads");
    }

    @SneakyThrows
    public void run() {
        while (!serverSocket.isClosed()) {
            Socket newConnection = serverSocket.accept();
            threadPool.submit(new ConnectionHandler(newConnection, UPLOADS_DIRECTORY));
        }
        threadPool.shutdown();
    }
}