package ru.nsu.dyuganov.file_sender_server;

import ru.nsu.dyuganov.file_sender_server.protocol.TCPReceiver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Server.class);

    private final ExecutorService threadPool;
    private final ServerSocket serverSocket;

    Server(int port) throws IOException {
        if (Constants.MIN_PORT > port || Constants.MAX_PORT < port) {
            logger.error("Wrong port");
            throw new IllegalArgumentException("Wrong port");
        }
        serverSocket = new ServerSocket(port);
        logger.info("Creating server socket. " + "Server ip is: " + InetAddress.getLocalHost().getHostAddress());
        System.out.println("Server ip is: " + InetAddress.getLocalHost().getHostAddress());

        threadPool = Executors.newFixedThreadPool(Constants.THREAD_POOL_SIZE);
        logger.debug("newFixedThreadPool with size " + Constants.THREAD_POOL_SIZE + " created");
    }

    public void run() {
        logger.debug("Server started");
        while (!serverSocket.isClosed()) {
            Socket newConnection = null;
            try {
                newConnection = serverSocket.accept();
            } catch (IOException e) {
                logger.error("Error while getting new connection: " + e.getMessage());
                throw new RuntimeException("Error while getting new connection");
            }
            logger.info("Got new connection: " + newConnection);
            try {
                threadPool.submit(new ConnectionHandler(Constants.UPLOADS_DIRECTORY, new TCPReceiver(newConnection)));
            } catch (IOException e) {
                logger.error("Error while creating TCPReceiver: " + e.getMessage());
                throw new RuntimeException("Error while creating TCPReceiver");
            }
        }
        threadPool.shutdown();
        logger.info("Server finished work");
    }
}