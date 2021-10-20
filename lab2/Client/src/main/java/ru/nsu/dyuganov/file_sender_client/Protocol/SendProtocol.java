package ru.nsu.dyuganov.file_sender_client.Protocol;

import java.io.File;

public interface SendProtocol {
    void sendFileNameSize(int dataSize);
    void sendFileName(String fileName);
    void sendFileSize(int fileSize);
    void sendFile(File file);
}
