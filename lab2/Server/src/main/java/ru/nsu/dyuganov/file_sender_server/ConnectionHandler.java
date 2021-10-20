package ru.nsu.dyuganov.file_sender_server;

import lombok.NonNull;

import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private final String uploadsDirectory;
    private final Socket connection;

    public ConnectionHandler(@NonNull Socket connection, @NonNull String uploadsDirectory) {
        this.connection = connection;
        this.uploadsDirectory = uploadsDirectory;
    }

    @Override
    public void run() {
        // ...
    }
}
