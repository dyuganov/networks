package ru.nsu.dyuganov.file_sender_server.Protocol;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.file.Path;

public class TCPReceiver implements ReceiveProtocol {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TCPReceiver.class);
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    @SneakyThrows
    public TCPReceiver(@NonNull Socket socket) {
        logger.debug("Receiver creation started");
        this.socket = socket;
        inputStream = new DataInputStream(socket.getInputStream()); // TODO : if falls in SneakyThrows, resources are free?
        logger.info("Got DataInputStream from client socket");
        outputStream = new DataOutputStream(socket.getOutputStream());
        logger.info("Got DataOutputStream from client socket");
        logger.debug("Receiver is ready");
    }

    /**
     * @return size in bytes
     */
    @SneakyThrows
    @Override
    public int getFileNameSize() {
        logger.debug(socket.toString() + " Getting file name size");
        return inputStream.readInt();
    }

    /**
     * @param nameSize in bytes
     * @return name without '/'
     */
    @SneakyThrows
    @Override
    public String getFileName(final int nameSize) {
        String fileName = inputStream.readUTF();
        if (nameSize != fileName.length()) {
            logger.error(socket.toString() + " Got wrong file name. Expected len = " + nameSize + ", got len = " + fileName.length());
            return null;
        }
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    /**
     * @return size in bytes
     */
    @SneakyThrows
    @Override
    public long getFileSize() {
        logger.debug(socket.toString() + " Getting file size");
        return inputStream.readInt();
    }

    /**
     * @param fileSize in bytes
     */
    @Override
    public void getFile(final long fileSize, @NonNull final Path filePath) {
        //var fileWriter = Files.newOutputStream(uploadsPath);
    }
}
