package ru.nsu.dyuganov.file_sender_server.Protocol;

import java.nio.file.Path;

public interface ReceiveProtocol {
    int getFileNameSize();
    String getFileName(int nameSize);
    long getFileSize();
    void getFile(long fileSize, Path uploadsPath);
    void closeConnections();
}
