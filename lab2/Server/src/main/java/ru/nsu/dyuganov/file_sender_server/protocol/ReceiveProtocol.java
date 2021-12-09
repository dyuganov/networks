package ru.nsu.dyuganov.file_sender_server.protocol;

import java.io.IOException;
import java.nio.file.Path;

public interface ReceiveProtocol {
    int getFileNameSize() throws IOException;
    String getFileName() throws IOException;
    long getFileSize() throws IOException;
    void getFile(long fileSize, Path uploadsPath) throws IOException;
    void closeConnections() throws IOException;
}
