package ru.nsu.dyuganov.file_sender_server;

import lombok.NonNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private final int port;
    private final int THREADPOOL_SIZE = 5;

    private final ExecutorService threadPool = Executors.newFixedThreadPool(THREADPOOL_SIZE);

    Server(int port) {
        // Registered ports are in the range 1024 to 49151
        final int MIN_PORT = 1023;
        final int MAX_PORT = 65535;
        if (MIN_PORT > port || MAX_PORT < port) {
            throw new IllegalArgumentException("Port is not correct");
        }
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket newConnection = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}