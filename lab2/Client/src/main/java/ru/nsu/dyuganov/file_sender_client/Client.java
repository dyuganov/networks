package ru.nsu.dyuganov.file_sender_client;

public class Client implements Runnable{
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);

    private final String filePath;
    private final String fileName;
    private final String serverAddress;
    private final int serverPort;

    /**
     * @param filePath filepath or filename. It will be converted to name only.
     * @param serverAddress server ip or name
     * */
    Client(final String filePath, final String serverAddress, final int serverPort){
        this.filePath = filePath;
        this.fileName = filePath.substring(filePath.lastIndexOf('/'));
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    /**
     * Order of sending info: file name size, file name, file size, file
     * */
    public void run() {

    }
}
