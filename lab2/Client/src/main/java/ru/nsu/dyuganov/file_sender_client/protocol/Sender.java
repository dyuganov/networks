package ru.nsu.dyuganov.file_sender_client.protocol;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Sender implements SendProtocol {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Sender.class);

    private final static int FILE_DATA_BUF_SIZE = 2048;
    Socket socket;
    private final DataOutputStream outputStream;

    @SneakyThrows
    public Sender(@NonNull final InetAddress serverAddress, final int port) {
        logger.debug("Sender creation started");
        socket = new Socket(serverAddress, port);
        logger.debug(socket + " : socket created. Port: " + port + "; Address: " + serverAddress);
        outputStream = new DataOutputStream(socket.getOutputStream());
        logger.debug(socket + " : DataOutputStream created");
        logger.debug("Sender creation finished");
    }

    @SneakyThrows
    @Override
    public void sendFileNameSize(int size) {
        logger.debug(socket + " : sending file name size start");
        outputStream.writeInt(size);
        outputStream.flush();
        logger.debug(socket + " : sending file name size success");
    }

    @SneakyThrows
    @Override
    public void sendFileName(@NonNull String fileName) {
        logger.info(socket + " :sending file name start");
        outputStream.writeUTF(fileName);
        outputStream.flush();
        logger.info(socket + " :sending file name success");
    }

    @SneakyThrows
    @Override
    public void sendFileSize(long fileSize) {
        logger.info(socket + " : sending file size start");
        outputStream.writeLong(fileSize);
        outputStream.flush();
        logger.info(socket + " : sending file size success");
    }

    @SneakyThrows
    @Override
    public void sendFile(@NonNull Path filePath) {
        logger.debug(socket + " : sending file start");
        @Cleanup InputStream fileReader = Files.newInputStream(filePath);

        var buffer = new byte[FILE_DATA_BUF_SIZE];
        int bytesFromFile = 0;

        while (0 <= bytesFromFile) {
            bytesFromFile = fileReader.read(buffer, 0, FILE_DATA_BUF_SIZE);
            outputStream.write(buffer, 0, bytesFromFile);
            outputStream.flush();
        }
        logger.debug(socket + " : sending file success");
    }

    @SneakyThrows
    @Override
    public void closeConnections() {
        logger.debug(socket + " : closing connection started");
        outputStream.close();
        socket.close();
        logger.debug(socket + " : Socket and DataOutputStream closed");
    }
}
