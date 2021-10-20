package ru.nsu.dyuganov.file_sender_client;

import java.io.IOException;
import java.util.Properties;

public class Main {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);
    private static final Properties properties = new Properties();

    public static void main(String[] args) throws IOException, InterruptedException {
        properties.load(Main.class.getClassLoader().getResourceAsStream("config.properties"));
        final int FILE_PATH_ARGS_IDX = Integer.parseInt(properties.getProperty("FILE_PATH_ARGS_IDX"));
        final int SERVER_ADR_ARGS_IDX = Integer.parseInt(properties.getProperty("SERVER_ADR_ARGS_IDX"));
        final int SERVER_PORT_ARGS_IDX = Integer.parseInt(properties.getProperty("SERVER_PORT_ARGS_IDX"));
        if(3 != args.length){
            System.err.println("Wrong args num");
            return;
        }
        final String filePath = args[FILE_PATH_ARGS_IDX];
        if (filePath.lastIndexOf('/') == filePath.length()){
            throw new IllegalArgumentException("Wrong file path. Last symbol is '/'");
        }
        final String serverAddress = args[SERVER_ADR_ARGS_IDX];
        final int serverPort = Integer.parseInt(args[SERVER_PORT_ARGS_IDX]);

        Client client = new Client(filePath, serverAddress, serverPort);
        Thread clientThread = new Thread(client);
        clientThread.start();
        clientThread.join();
    }
}
