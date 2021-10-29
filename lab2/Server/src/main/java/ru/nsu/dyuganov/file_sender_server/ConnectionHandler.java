package ru.nsu.dyuganov.file_sender_server;

import lombok.NonNull;
import lombok.SneakyThrows;
import ru.nsu.dyuganov.file_sender_server.Protocol.ReceiveProtocol;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConnectionHandler implements Runnable {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ConnectionHandler.class);

    private final String uploadsDirectoryName;
    private final ReceiveProtocol receiveProtocol;

    public ConnectionHandler(@NonNull String uploadsDirectory, @NonNull ReceiveProtocol protocol) {
        this.uploadsDirectoryName = uploadsDirectory;
        this.receiveProtocol = protocol;
    }

    @SneakyThrows
    @Override
    public void run() {
        //@NonNull final Configuration config = new Configurations().properties(new File(Main.PROPERTIES_FILE_NAME));
        final int fileNameSize = receiveProtocol.getFileNameSize();
        @NonNull String fileName = receiveProtocol.getFileName(fileNameSize);
        final long fileSize = receiveProtocol.getFileSize();
        var filePath = createFile(fileName);
        receiveProtocol.getFile(fileSize, filePath);
    }

    @SneakyThrows
    private Path createFile(@NonNull String fileName){
        final var directoryPath = Paths.get(uploadsDirectoryName);
        final String separator = System.getProperty("file.separator");
        final Path filePath = Paths.get(directoryPath + separator + fileName);
        Files.createFile(filePath);
        return filePath;
    }
}


