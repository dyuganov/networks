package ru.nsu.dyuganov.file_sender_client;

import java.util.Properties;

public class Client implements Runnable{
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);

    final String filePath;
    final String fileName;
    final String serverAddress;
    final int serverPort;

    Client(final String filePath, final String serverAddress, final int serverPort){
        this.filePath = filePath;
        this.fileName = filePath.substring(filePath.lastIndexOf('/'));
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {

    }
}
