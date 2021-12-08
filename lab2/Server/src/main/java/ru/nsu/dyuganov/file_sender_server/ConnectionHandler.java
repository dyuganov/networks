package ru.nsu.dyuganov.file_sender_server;

import lombok.NonNull;
import ru.nsu.dyuganov.file_sender_server.protocol.ReceiveProtocol;

import java.io.IOException;
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
    @Override
    public void run() {
        logger.debug("connection handler started: " + this);
        logger.debug("Getting file name size");
        int fileNameSize = 0;
        try {
            fileNameSize = receiveProtocol.getFileNameSize();
        } catch (IOException e) {
            logger.error("Error while getting file name size: " + e.getMessage());
            throw new RuntimeException("Error while getting file name size");
        }
        logger.debug("Got file name size: " + fileNameSize);
        logger.info("Getting file name");
        @NonNull String fileName = null;
        try {
            fileName = receiveProtocol.getFileName();
        } catch (IOException e) {
            logger.error("Error while getting file name: " + e.getMessage());
            throw new RuntimeException("Error while getting file name");
        }
        logger.info("Got file name: " + fileName);
        if (fileNameSize != fileName.length()) {
            logger.error("Got wrong file name. Expected len = " + fileNameSize + ", got len = " + fileName.length() + " name: " + fileName);
            try {
                receiveProtocol.closeConnections();
            } catch (IOException e) {
                logger.error("Error while closing connection: " + e.getMessage());
                throw new RuntimeException("Error while closing connection");
            }
            throw new RuntimeException("Wrong filename");
        }
        logger.debug("File name length is correct");
        logger.debug("Getting file size");
        long fileSize = 0;
        try {
            fileSize = receiveProtocol.getFileSize();
        } catch (IOException e) {
            logger.error("Error while getting file size: " + e.getMessage());
            throw new RuntimeException("Error while getting file size");
        }
        logger.info("Got file size: " + fileSize);
        logger.debug("Creating file");
        final var filePath = createFile(fileName);
        logger.info("File created. Path: " + filePath);
        logger.debug("Getting file");
        try {
            receiveProtocol.getFile(fileSize, filePath);
        } catch (IOException e) {
            logger.error("Error while getting file: " + e.getMessage());
            throw new RuntimeException("Error while getting file");
        }
        logger.info("Got file");
        try {
            receiveProtocol.closeConnections();
        } catch (IOException e) {
            logger.error("Error while closing connection: " + e.getMessage());
            throw new RuntimeException("Error while closing connection");
        }
        logger.info("Connection closed");
        try {
            if(fileSize != Files.size(filePath)){
                logger.error("Real != expected file size");
                throw new RuntimeException("Real != expected file size");
            }
        } catch (IOException e) {
            logger.error("Error while getting downloaded file size: " + e.getMessage());
            throw new RuntimeException("Error while getting downloaded file size");
        }
        logger.debug("File size is correct");
        logger.info("File receive finished");
    }

    private Path createFile(@NonNull String fileName) {
        final var directoryPath = Paths.get(uploadsDirectoryName);
        final String separator = System.getProperty("file.separator");
        final Path filePath = Paths.get(directoryPath + separator + fileName);
        try {
            Files.createFile(filePath);
        } catch (IOException e) {
            logger.error("Error while creating file: " + e.getMessage());
            throw new RuntimeException("Error while creating file");
        }
        logger.info("File created with name: " + fileName);
        return filePath;
    }
}


