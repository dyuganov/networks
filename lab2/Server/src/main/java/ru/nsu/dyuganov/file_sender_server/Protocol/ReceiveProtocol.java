package ru.nsu.dyuganov.file_sender_server.Protocol;

public interface ReceiveProtocol {
    int getNameSize(int dataSize);
    String getFilename(int nameSize);
    int getFileSize(int dataSize);
    void getFile(int fileSize, String uploadsPath);
}
