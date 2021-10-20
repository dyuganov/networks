package ru.nsu.dyuganov.file_sender_server;

import java.io.IOException;
import java.util.Properties;

public class Main {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);
    private static final Properties properties = new Properties();

    public static void main(String[] args) throws InterruptedException, IOException {
        //PropertyConfigurator.configure("Server/src/main/resources/log4j.properties"); // not needed?
        final String propertiesFileName = "config.properties";
        properties.load(Main.class.getClassLoader().getResourceAsStream(propertiesFileName));
        logger.debug("Properties file \"" + propertiesFileName + "\" loaded");
        final int ARGS_NUM = Integer.parseInt(properties.getProperty("ARGS_NUM"));
        final int SERVER_PORT_ARGS_IDX = Integer.parseInt(properties.getProperty("SERVER_PORT_ARGS_IDX"));
        logger.debug("Constants parsed from properties");
        if (ARGS_NUM != args.length) {
            logger.error("Wrong args num");
            return;
        }

        int port = Integer.parseInt(args[SERVER_PORT_ARGS_IDX]);
        Server server = new Server(port);
        Thread serverThread = new Thread(server);
        logger.debug("Server thread created");
        serverThread.start();
        logger.debug("Server thread started");
        serverThread.join();
        logger.debug("Server thread joined main");
    }
}
