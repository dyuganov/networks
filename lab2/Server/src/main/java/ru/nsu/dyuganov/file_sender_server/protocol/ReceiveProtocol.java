package ru.nsu.dyuganov.file_sender_server.protocol;

import java.nio.file.Path;

public interface ReceiveProtocol {
    int getFileNameSize();
    String getFileName();
    long getFileSize();
    void getFile(long fileSize, Path uploadsPath);
    void closeConnections();
}
