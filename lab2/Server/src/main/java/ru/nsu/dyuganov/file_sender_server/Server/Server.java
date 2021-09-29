package ru.nsu.dyuganov.file_sender_server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;


public class Server implements Runnable {

    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    private final int port;
    Server(int port) {
        // Registered ports are in the range 1024 to 49151
        final int MIN_PORT = 1023;
        final int MAX_PORT = 65535;
        if(MIN_PORT > port || MAX_PORT < port){
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
