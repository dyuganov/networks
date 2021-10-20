package ru.nsu.dyuganov.file_sender_server.Protocol;

public class Receiver implements ReceiveProtocol {
    Receiver() {
        // ...
    }

    /**
     * @param dataSize normally is 32 bytes (size of name size, which type is int)
     * @return size in bytes
     */
    @Override
    public int getNameSize(int dataSize) {
        // ...
        return 0;
    }

    /**
     * @param nameSize in bytes
     * @return name without '/'
     */
    @Override
    public String getFilename(int nameSize) {
        // check name don't contain '/'
        return null;
    }

    /**
     * @param dataSize normally is 32 bytes (size of name size, which type is int)
     * @return size in bytes
     */
    @Override
    public int getFileSize(int dataSize) {
        // ...
        return 0;
    }

    /**
     * @param fileSize in bytes
     */
    @Override
    public void getFile(final int fileSize, final String uploadsPath) {
        // ...
    }
}
