package ru.nsu.dyuganov.file_sender_server;

import lombok.NonNull;
import lombok.SneakyThrows;
import ru.nsu.dyuganov.file_sender_server.protocol.ReceiveProtocol;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConnectionHandler implements Runnable {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ConnectionHandler.class);

    private final String uploadsDirectoryName;
    private final ReceiveProtocol receiveProtocol;

    public ConnectionHandler(@NonNull String uploadsDirectory, @NonNull ReceiveProtocol protocol) {
        logger.debug("Connection handler creation started");
        this.uploadsDirectoryName = uploadsDirectory;
        logger.debug("uploadsDirectory name: " + uploadsDirectory);
        this.receiveProtocol = protocol;
        logger.debug("ReceiveProtocol: " + protocol);
        logger.debug("Connection handler creation finished");
    }

    /**
     * Order of getting info: file name size, file name, file size, file
     * */
    @SneakyThrows
    @Override
    public void run() {
        logger.debug("connection handler started: " + this);
        logger.debug("Getting file name size");
        final int fileNameSize = receiveProtocol.getFileNameSize();
        logger.debug("Got file name size: " + fileNameSize);
        logger.debug("Getting file name");
        @NonNull String fileName = receiveProtocol.getFileName();
        logger.debug("Got file name: " + fileName);
        if (fileNameSize != fileName.length()) {
            logger.error("Got wrong file name. Expected len = " + fileNameSize + ", got len = " + fileName.length() + " name: " + fileName);
            receiveProtocol.closeConnections();
            throw new RuntimeException("Wrong filename");
        }
        logger.debug("File name length is correct");
        logger.debug("Getting file size");
        final long fileSize = receiveProtocol.getFileSize();
        logger.debug("Got file size: " + fileSize);
        logger.debug("Creating file");
        final var filePath = createFile(fileName);
        logger.debug("File created. Path: " + filePath);
        logger.debug("Getting file");
        receiveProtocol.getFile(fileSize, filePath);
        logger.debug("Got file");
        receiveProtocol.closeConnections();
        logger.debug("Connection closed");
        if(fileSize != Files.size(filePath)){
            logger.error("Real != expected file size");
            throw new RuntimeException("Real != expected file size");
        }
        logger.debug("File size is correct");
        logger.debug("File receive finished");
    }

    @SneakyThrows
    private Path createFile(@NonNull String fileName){
        final var directoryPath = Paths.get(uploadsDirectoryName);
        final String separator = System.getProperty("file.separator");
        final Path filePath = Paths.get(directoryPath + separator + fileName);
        Files.createFile(filePath);
        logger.info("File created with name: " + fileName);
        return filePath;
    }
}


