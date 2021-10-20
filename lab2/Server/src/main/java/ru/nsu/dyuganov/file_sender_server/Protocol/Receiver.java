package ru.nsu.dyuganov.file_sender_server.Protocol;

public class Receiver implements ReceiveProtocol {

    Receiver(){

    }

    /**
     * @param dataSize
     * @return size in bytes
     * */
    @Override
    public int getNameSize(int dataSize) {
        return 0;
    }

    @Override
    public String getFilename(int nameSize) {
        // check name don't contain '/'
        return null;
    }

    @Override
    public int getFileSize(int dataSize) {
        return 0;
    }

    @Override
    public void getFile(int fileSize) {

    }
}
