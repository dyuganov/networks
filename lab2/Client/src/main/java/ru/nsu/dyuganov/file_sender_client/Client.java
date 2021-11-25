package ru.nsu.dyuganov.file_sender_client;

import lombok.NonNull;
import lombok.SneakyThrows;
import ru.nsu.dyuganov.file_sender_client.protocol.SendProtocol;
import ru.nsu.dyuganov.file_sender_client.protocol.Sender;

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
    @SneakyThrows
    public void run() {
        logger.debug("Client runs");
        SendProtocol sender = new Sender(serverAddress, serverPort);
        logger.debug("Sender created");
        sender.sendFileNameSize(fileName.length());
        logger.info("File name size sent");
        sender.sendFileName(fileName);
        logger.info("File name sent");
        sender.sendFileSize(Files.size(filePath));
        logger.info("File size sent");
        sender.sendFile(filePath);
        logger.info("File size sent");
        sender.closeConnections();
        logger.info("Connection closed");
    }
}
