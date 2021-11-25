package ru.nsu.dyuganov.file_sender_client.protocol;

import java.nio.file.Path;

public interface SendProtocol {
    void sendFileNameSize(int size);
    void sendFileName(String fileName);
    void sendFileSize(long fileSize);
    void sendFile(Path filepath);
    void closeConnections();
}
