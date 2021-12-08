package ru.nsu.dyuganov.file_sender_client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (Constants.ARGS_NUM != args.length) {
            logger.error("Wrong args num");
            return;
        }

        final String filePath = args[Constants.FILE_PATH_ARGS_IDX];
        if (filePath.lastIndexOf('/') == filePath.length()) {
            logger.error("Wrong file path. Last symbol is '/'");
            return;
        }
        Path path = Paths.get(filePath);

        final String serverAddress = args[Constants.SERVER_ADR_ARGS_IDX];
        InetAddress address = null;
        try {
            address = InetAddress.getByName(serverAddress);
        } catch (UnknownHostException e) {
            logger.error("Error while getting InetAddress by name: " + e.getMessage());
            throw new RuntimeException("Error while getting InetAddress by name");
        }
        final int port = Integer.parseInt(args[Constants.SERVER_PORT_ARGS_IDX]);

        Client client = new Client(address, port, path);
        client.run();
    }
}
