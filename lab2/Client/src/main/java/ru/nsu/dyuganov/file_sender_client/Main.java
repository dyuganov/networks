package ru.nsu.dyuganov.file_sender_client;

import java.util.Properties;

public class Main {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);
    private static final Properties properties = new Properties();

    public static void main(String[] args) {
        if(3 != args.length){
            System.err.println("Wrong args num");
            return;
        }
        String filePath = args[0];
        String serverAddress = args[1];
        String serverPort = args[2];
    }
}
