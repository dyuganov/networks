package ru.nsu.dyuganov.file_sender_client.protocol;

import java.io.IOException;
import java.nio.file.Path;

public interface SendProtocol {
    void sendFileNameSize(int size) throws IOException;
    void sendFileName(String fileName) throws IOException;
    void sendFileSize(long fileSize) throws IOException;
    void sendFile(Path filepath) throws IOException;
    void closeConnections() throws IOException;
}
