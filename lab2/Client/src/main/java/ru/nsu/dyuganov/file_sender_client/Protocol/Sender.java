package ru.nsu.dyuganov.file_sender_client.Protocol;

import lombok.NonNull;

public class Sender implements SendProtocol {
    Sender() {

    }

    @Override
    public void sendFileNameSize(int dataSize) {

    }

    @Override
    public void sendFileName(@NonNull String fileName) {

    }

    @Override
    public void sendFileSize(int fileSize) {

    }

    @Override
    public void sendFile(@NonNull String fileName) {

    }
}