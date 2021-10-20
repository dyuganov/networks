package ru.nsu.dyuganov.file_sender_client;

import java.io.IOException;
import java.util.Properties;

public class Main {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);
    private static final Properties properties = new Properties();

    public static void main(String[] args) throws IOException, InterruptedException {
        final String propertiesFileName = "config.properties";
        properties.load(Main.class.getClassLoader().getResourceAsStream(propertiesFileName));
        logger.debug("Properties file \"" + propertiesFileName + "\" loaded");
        final int FILE_PATH_ARGS_IDX = Integer.parseInt(properties.getProperty("FILE_PATH_ARGS_IDX"));
        final int SERVER_ADR_ARGS_IDX = Integer.parseInt(properties.getProperty("SERVER_ADR_ARGS_IDX"));
        final int SERVER_PORT_ARGS_IDX = Integer.parseInt(properties.getProperty("SERVER_PORT_ARGS_IDX"));
        logger.debug("Constants parsed from properties");
        if (3 != args.length) {
            logger.error("Wrong args num");
            return;
        }
        final String filePath = args[FILE_PATH_ARGS_IDX];
        if (filePath.lastIndexOf('/') == filePath.length()) {
            logger.error("Wrong file path. Last symbol is '/'");
            return;
        }
        final String serverAddress = args[SERVER_ADR_ARGS_IDX];
        final int serverPort = Integer.parseInt(args[SERVER_PORT_ARGS_IDX]);

        Client client = new Client(filePath, serverAddress, serverPort);
        Thread clientThread = new Thread(client);
        logger.debug("Client thread created");
        clientThread.start();
        logger.debug("Client thread started");
        clientThread.join();
        logger.debug("Client thread joined main");
    }
}
