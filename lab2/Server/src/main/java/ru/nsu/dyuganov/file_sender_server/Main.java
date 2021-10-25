package ru.nsu.dyuganov.file_sender_server;

import lombok.SneakyThrows;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.File;

public class Main {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);
    public static final String PROPERTIES_FILE_NAME = "config.properties";

    @SneakyThrows
    public static void main(String[] args) {
        final Configuration config = new Configurations().properties(new File("config.properties"));
        logger.debug("Properties file \"" + PROPERTIES_FILE_NAME + "\" loaded");
        final int ARGS_NUM = config.getInt("ARGS_NUM");
        final int SERVER_PORT_ARGS_IDX = config.getInt("SERVER_PORT_ARGS_IDX");
        logger.debug("Constants parsed from properties");

        if (ARGS_NUM != args.length) {
            logger.error("Wrong args num");
            return;
        }

        int port = Integer.parseInt(args[SERVER_PORT_ARGS_IDX]);
        Server server = new Server(port);
        server.run();
    }
}
