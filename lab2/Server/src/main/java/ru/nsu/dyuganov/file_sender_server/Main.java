package ru.nsu.dyuganov.file_sender_server;

import java.io.IOException;

public class Main {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);
    public static final String PROPERTIES_FILE_NAME = "config.properties";

    public static void main(String[] args) {
        if (Constants.ARGS_NUM != args.length) {
            logger.error("Wrong args num");
            return;
        }

        int port = Integer.parseInt(args[Constants.SERVER_PORT_ARGS_IDX]);
        Server server = null;
        try {
            server = new Server(port);
        } catch (IOException e) {
            logger.error("Error while creating server: " + e.getMessage());
            throw new RuntimeException("Error while creating server");
        }
        server.run();
    }
}
