package ru.nsu.dyuganov.file_sender_server.Server;

public class Main {
    private final static int ARGS_NUM = 1;
    private final static int PORT_ARG_IDX = 0;

    public static void main(String[] args) throws InterruptedException {
        if(ARGS_NUM != args.length){
            System.err.println("Wrong args num");
            return;
        }
        int port = Integer.parseInt(args[PORT_ARG_IDX]);
        Server server = new Server(port);
        Thread serverThread = new Thread(server);
        serverThread.start();
        serverThread.join();
    }
}
