package ru.nsu.dyuganov.file_sender_server;

import java.io.IOException;
import java.util.Properties;

public class Main {

    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);
    private static final Properties properties = new Properties();

    public static void main(String[] args) throws InterruptedException, IOException {
        //PropertyConfigurator.configure("Server/src/main/resources/log4j.properties");

        properties.load(Main.class.getClassLoader().getResourceAsStream("config.properties"));
        final int SERVER_PORT_ARGS_IDX = Integer.parseInt(properties.getProperty("SERVER_PORT_ARGS_IDX"));
        final int ARGS_NUM = Integer.parseInt(properties.getProperty("ARGS_NUM"));

        if(ARGS_NUM != args.length){
            System.err.println("Wrong args num");
            logger.error("error!");
            return;
        }
        int port = Integer.parseInt(args[SERVER_PORT_ARGS_IDX]);
        Server server = new Server(port);
        Thread serverThread = new Thread(server);
        serverThread.start();
        serverThread.join();
    }
}
