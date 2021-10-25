package ru.nsu.dyuganov.file_sender_client;

import lombok.SneakyThrows;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.File;

public class Main {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);
    public static final String PROPERTIES_FILE_NAME = "config.properties";

    @SneakyThrows
    public static void main(String[] args) {
        final Configuration config = new Configurations().properties(new File(PROPERTIES_FILE_NAME));
        logger.debug("Properties file \"" + PROPERTIES_FILE_NAME + "\" loaded");
        final int FILE_PATH_ARGS_IDX = config.getInt("FILE_PATH_ARGS_IDX");
        final int SERVER_ADR_ARGS_IDX = config.getInt("SERVER_ADR_ARGS_IDX");
        final int SERVER_PORT_ARGS_IDX = config.getInt("SERVER_PORT_ARGS_IDX");
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
        client.run();
    }
}
