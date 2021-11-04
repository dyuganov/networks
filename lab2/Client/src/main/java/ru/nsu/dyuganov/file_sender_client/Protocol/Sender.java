package ru.nsu.dyuganov.file_sender_client.Protocol;

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
    private final DataOutputStream outputChannel;

    @SneakyThrows
    public Sender(@NonNull final InetAddress serverAddress, final int port) {
        socket = new Socket(serverAddress, port);
        logger.info("Socket created.");
        outputChannel = new DataOutputStream(socket.getOutputStream());
    }

    @SneakyThrows
    @Override
    public void sendFileNameSize(int size) {
        outputChannel.writeInt(size);
        outputChannel.flush();
    }

    @SneakyThrows
    @Override
    public void sendFileName(@NonNull String fileName) {
        outputChannel.writeUTF(fileName);
        outputChannel.flush();
    }

    @SneakyThrows
    @Override
    public void sendFileSize(long fileSize) {
        outputChannel.writeLong(fileSize);
        outputChannel.flush();
    }

    @SneakyThrows
    @Override
    public void sendFile(@NonNull Path filePath) {
        @Cleanup InputStream fileReader = Files.newInputStream(filePath);

        var buffer = new byte[FILE_DATA_BUF_SIZE];
        int bytesFromFile = 0;

        while (0 <= bytesFromFile) {
            bytesFromFile = fileReader.read(buffer, 0, FILE_DATA_BUF_SIZE);
            outputChannel.write(buffer, 0, bytesFromFile);
            outputChannel.flush();
        }
    }

    @SneakyThrows
    @Override
    public void closeConnections() {
        outputChannel.close();
        socket.close();
    }
}
