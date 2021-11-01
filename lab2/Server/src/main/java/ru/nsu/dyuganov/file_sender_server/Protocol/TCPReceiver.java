package ru.nsu.dyuganov.file_sender_server.Protocol;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TCPReceiver implements ReceiveProtocol {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TCPReceiver.class);
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;




    private final static int FILE_DATA_BUF_SIZE = 2048;
    private final static int TIME_INTERVAL_SEC = 3;

    @SneakyThrows
    public TCPReceiver(@NonNull Socket socket) {
        logger.debug("Receiver creation started");
        this.socket = socket;
        inputStream = new DataInputStream(socket.getInputStream()); // TODO : if falls in SneakyThrows, resources are free?
        logger.info("Got DataInputStream from client socket");
        outputStream = new DataOutputStream(socket.getOutputStream());
        logger.info("Got DataOutputStream from client socket");
        logger.debug("Receiver is ready");
    }

    /**
     * @return size in bytes
     */
    @SneakyThrows
    @Override
    public int getFileNameSize() {
        Instant start = Instant.now();
        final int result = inputStream.readInt();
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        logger.debug(socket.toString() + " Getting file name size done at " + timeElapsed.toMillis() + " millis");
        System.out.println("Getting file name size time taken: "+ timeElapsed.toMillis() +" milliseconds" +
                " at speed " + Integer.SIZE/timeElapsed.getSeconds() + "bytes/second");
        return result;
    }

    /**
     * @param nameSize in bytes
     * @return name without '/'
     */
    @SneakyThrows
    @Override
    public String getFileName(final int nameSize) {
        String fileName = inputStream.readUTF();
        if (nameSize != fileName.length()) {
            logger.error(socket.toString() + " Got wrong file name. Expected len = " + nameSize + ", got len = " + fileName.length());
            return null;
        }
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    /**
     * @return size in bytes
     */
    @SneakyThrows
    @Override
    public long getFileSize() {
        logger.debug(socket.toString() + " Getting file size");
        return inputStream.readInt();
    }

    /**
     * @param fileSize in bytes
     */
    @SneakyThrows
    @Override
    public void getFile(final long fileSize, @NonNull final Path filePath) {
        final ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleAtFixedRate(this::countSpeed, 2, TIME_INTERVAL_SEC, TimeUnit.SECONDS);
        long totalSpeed = 0;
        long currentSpeed = 0;
        long totalSessionTime = 0;

        long totalBytesRead = 0;
        long bytesReadForInterval = 0;

        var fileWriter = Files.newOutputStream(filePath);
        if((long)Integer.MAX_VALUE < fileSize){
            throw new RuntimeException("Can't get file. It is too big");
        }
        byte[] buf = new byte[FILE_DATA_BUF_SIZE];
        long totalBytes = 0;
        while(fileSize > totalBytes){
            int bytesFromStream = inputStream.read(buf, 0, FILE_DATA_BUF_SIZE);
            if (bytesFromStream < 0) {
                break;
            }
            fileWriter.write(buf, 0, bytesFromStream);
            totalBytes += bytesFromStream;
            totalBytesRead += bytesFromStream;
            bytesReadForInterval += bytesFromStream;
        }


        scheduledThreadPool.shutdown();
    }

    @SneakyThrows
    @Override
    public void closeConnections(){
        inputStream.close();
        outputStream.close();
    }

    private void countSpeed(){

    }
}
