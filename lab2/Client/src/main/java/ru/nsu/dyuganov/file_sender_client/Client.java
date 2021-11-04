package ru.nsu.dyuganov.file_sender_client;

import lombok.NonNull;
import lombok.SneakyThrows;
import ru.nsu.dyuganov.file_sender_client.Protocol.SendProtocol;
import ru.nsu.dyuganov.file_sender_client.Protocol.Sender;

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
        this.filePath = filePath;
        this.serverAddress = serverAddress;
        this.serverPort = port;
        this.fileName = filePath.getFileName().toString();
    }

    /**
     * Order of sending info: file name size, file name, file size, file
     * */
    @SneakyThrows
    public void run() {
        SendProtocol sender = new Sender(serverAddress, serverPort);

        sender.sendFileNameSize(fileName.length());
        sender.sendFileName(fileName);
        sender.sendFileSize(Files.size(filePath));
        sender.sendFile(filePath);
        sender.closeConnections();
    }
}
