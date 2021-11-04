package ru.nsu.dyuganov.file_sender_server;

import lombok.SneakyThrows;
import ru.nsu.dyuganov.file_sender_server.Protocol.TCPReceiver;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Server.class);

    private final ExecutorService threadPool;
    private final ServerSocket serverSocket;

    @SneakyThrows
    Server(int port) {
        if (Constants.MIN_PORT > port || Constants.MAX_PORT < port) {
            logger.error("Wrong port");
            throw new IllegalArgumentException("Wrong port");
        }
        serverSocket = new ServerSocket(port);
        logger.info("Creates server socket. " + "Server ip is: " + InetAddress.getLocalHost().getHostAddress());
        System.out.println("Server ip is: " + InetAddress.getLocalHost().getHostAddress());

        threadPool = Executors.newFixedThreadPool(Constants.THREAD_POOL_SIZE);
        logger.debug("newFixedThreadPool with size " + Constants.THREAD_POOL_SIZE + " created");
    }

    @SneakyThrows
    public void run() {
        while (!serverSocket.isClosed()) {
            Socket newConnection = serverSocket.accept();
            threadPool.submit(new ConnectionHandler(Constants.UPLOADS_DIRECTORY, new TCPReceiver(newConnection)));
        }
        threadPool.shutdown();
    }
}