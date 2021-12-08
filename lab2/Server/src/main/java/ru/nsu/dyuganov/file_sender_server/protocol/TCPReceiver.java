package ru.nsu.dyuganov.file_sender_server.protocol;

import lombok.NonNull;

import java.io.DataInputStream;
import java.io.IOException;
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
    private final DataInputStream dataInputStream;

    private volatile long totalSessionTime = 0;
    private volatile long totalBytesRead = 0;
    private volatile long bytesReadForInterval = 0;

    private final static int FILE_DATA_BUF_SIZE = 2048;
    private final static int TIME_INTERVAL_SEC = 3;
    private final static int TIMER_INIT_DELAY = 3;

    public TCPReceiver(@NonNull Socket socket) throws IOException {
        logger.debug("Receiver creation started");
        this.socket = socket;
        dataInputStream = new DataInputStream(socket.getInputStream());
        logger.info("Got DataInputStream from client socket");
        logger.debug("Receiver is ready");
    }

    /**
     * @return size in bytes
     */
    @Override
    public int getFileNameSize() throws IOException {
        logger.debug("Getting file name size started");
        Instant start = Instant.now();
        final int result = dataInputStream.readInt();
        logger.debug("Got int: " + result);
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        logger.debug(socket.toString() + " Getting file name size done at " + timeElapsed.toMillis() + " millis");
        logger.debug("Getting file name size time taken: "+ timeElapsed.toMillis() +" milliseconds" +
                " at speed " + Integer.SIZE/timeElapsed.getSeconds() + "bytes/second");
        System.out.println("Getting file name size time taken: "+ timeElapsed.toMillis() +" milliseconds" +
                " at speed " + Integer.SIZE/timeElapsed.getSeconds() + "bytes/second");
        System.out.flush();
        return result;
    }

    /**
     * @return name without '/'
     */
    @Override
    public String getFileName() throws IOException {
        logger.debug(socket.toString() + " :getting file name");
        return dataInputStream.readUTF();
    }

    /**
     * @return size in bytes
     */
    @Override
    public long getFileSize() throws IOException {
        logger.debug(socket.toString() + " :getting file size");
        return dataInputStream.readInt();
    }

    /**
     * @param fileSize in bytes
     */
    @Override
    public void getFile(final long fileSize, @NonNull final Path filePath) throws IOException {
        logger.debug("Getting file started");
        final ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleAtFixedRate(this::countSpeed, TIMER_INIT_DELAY, TIME_INTERVAL_SEC, TimeUnit.SECONDS);
        logger.debug("scheduledThreadPool started with TIMER_INIT_DELAY: " + TIMER_INIT_DELAY + ", TIME_INTERVAL_SEC: " + TIME_INTERVAL_SEC + " seconds");
        var fileWriter = Files.newOutputStream(filePath);
        if((long)Integer.MAX_VALUE < fileSize){
            logger.error("Can't get file. It is too big");
            throw new RuntimeException("Can't get file. It is too big");
        }
        byte[] buf = new byte[FILE_DATA_BUF_SIZE];
        long totalBytes = 0;
        while(fileSize > totalBytes){
            int bytesFromStream = dataInputStream.read(buf, 0, FILE_DATA_BUF_SIZE);
            if (bytesFromStream < 0) {
                break;
            }
            fileWriter.write(buf, 0, bytesFromStream);

            totalBytes += bytesFromStream;
            totalBytesRead += bytesFromStream;
            bytesReadForInterval += bytesFromStream;
        }
        scheduledThreadPool.shutdown();
        fileWriter.close();
        logger.debug("Getting file finished");
    }

    @Override
    public void closeConnections() throws IOException {
        dataInputStream.close();
    }

    private void countSpeed() {
        totalSessionTime += TIME_INTERVAL_SEC;
        long currentSpeed = (bytesReadForInterval / 1024) / TIME_INTERVAL_SEC;
        bytesReadForInterval = 0;
        long totalSpeed = (totalBytesRead / 1024) / totalSessionTime;
        System.out.println("Total speed: " + totalSpeed + " Kb/second");
        System.out.println("Current transfer speed: " + currentSpeed + " Kb/second");
        logger.info(this + ": Current transfer speed: " + currentSpeed + " Kb/second");
        logger.info(this + ": Total speed: " + totalSpeed + " Kb/second");
    }
}
