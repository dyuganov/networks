package ru.nsu.dyuganov.file_sender_client;

import lombok.NonNull;
import ru.nsu.dyuganov.file_sender_client.protocol.SendProtocol;
import ru.nsu.dyuganov.file_sender_client.protocol.Sender;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class Client implements Runnable{
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);

    private final Path filePath;
    private final String fileName;
    private final InetAddress serverAddress;
    private final int serverPort;

    /**
     * @param filePath filepath or filename. It will be converted to name only.
     * @param serverAddress server ip or name
     * */
    Client(@NonNull final InetAddress serverAddress, final int port, @NonNull final Path filePath){
        logger.debug("Client constructor started");
        this.filePath = filePath;
        this.serverAddress = serverAddress;
        this.serverPort = port;
        this.fileName = filePath.getFileName().toString();
        logger.debug("Client constructor finished");
    }

    /**
     * Order of sending info: file name size, file name, file size, file
     * */
    public void run() {
        logger.debug("Client runs");
        SendProtocol sender = null;
        try {
            sender = new Sender(serverAddress, serverPort);
        } catch (IOException e) {
            logger.error("Error while creating sender: " + e.getMessage());
            throw new RuntimeException("Error while creating sender");
        }
        logger.debug("Sender created");
        try {
            sender.sendFileNameSize(fileName.length());
        } catch (IOException e) {
            logger.error("Error while sending file name size: " + e.getMessage());
            throw new RuntimeException("Error while sending name name size");
        }
        logger.info("File name size sent");
        try {
            sender.sendFileName(fileName);
        } catch (IOException e) {
            logger.error("Error while sending file name: " + e.getMessage());
            throw new RuntimeException("Error while sending file name");
        }
        logger.info("File name sent");
        try {
            sender.sendFileSize(Files.size(filePath));
        } catch (IOException e) {
            logger.error("Error while sending file size: " + e.getMessage());
            throw new RuntimeException("Error while sending file size");
        }
        logger.info("File size sent");
        try {
            sender.sendFile(filePath);
        } catch (IOException e) {
            logger.error("Error while sending file: " + e.getMessage());
            throw new RuntimeException("Error while sending file");
        }
        logger.info("File size sent");
        try {
            sender.closeConnections();
        } catch (IOException e) {
            logger.error("Error while closing connection: " + e.getMessage());
            throw new RuntimeException("Error while closing connection");
        }
        logger.info("Connection closed");
    }
}
