package ru.nsu.dyuganov.file_sender_client.protocol;

import lombok.Cleanup;
import lombok.NonNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Sender implements SendProtocol {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Sender.class);

    private final static int FILE_DATA_BUF_SIZE = 2048;
    Socket socket;
    private final DataOutputStream dataOutputStream;

    public Sender(@NonNull final InetAddress serverAddress, final int port) throws IOException {
        logger.debug("Sender creation started");
        socket = new Socket(serverAddress, port);
        logger.debug(socket + " : socket created. Port: " + port + "; Address: " + serverAddress);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        logger.debug(socket + " : DataOutputStream created");
        logger.debug("Sender creation finished");
    }

    @Override
    public void sendFileNameSize(int size) throws IOException {
        logger.debug(socket + " : sending file name size start");
        dataOutputStream.writeInt(size);
        dataOutputStream.flush();
        logger.debug(socket + " : sending file name size success");
    }

    @Override
    public void sendFileName(@NonNull String fileName) throws IOException {
        logger.info(socket + " :sending file name start");
        dataOutputStream.writeUTF(fileName);
        dataOutputStream.flush();
        logger.info(socket + " :sending file name success");
    }

    @Override
    public void sendFileSize(long fileSize) throws IOException {
        logger.info(socket + " : sending file size start");
        dataOutputStream.writeLong(fileSize);
        dataOutputStream.flush();
        logger.info(socket + " : sending file size success");
    }

    @Override
    public void sendFile(@NonNull Path filePath) throws IOException {
        logger.debug(socket + " : sending file start");
        @Cleanup InputStream fileReader = Files.newInputStream(filePath);

        var buffer = new byte[FILE_DATA_BUF_SIZE];
        int bytesFromFile = 0;

        while (0 <= bytesFromFile) {
            bytesFromFile = fileReader.read(buffer, 0, FILE_DATA_BUF_SIZE);
            dataOutputStream.write(buffer, 0, bytesFromFile);
            dataOutputStream.flush();
        }
        logger.debug(socket + " : sending file success");
    }

    @Override
    public void closeConnections() throws IOException {
        logger.debug(socket + " : closing connection started");
        dataOutputStream.close();
        socket.close();
        logger.debug(socket + " : Socket and DataOutputStream closed");
    }
}
