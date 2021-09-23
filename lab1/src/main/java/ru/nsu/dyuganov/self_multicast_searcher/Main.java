package ru.nsu.dyuganov.self_multicast_searcher;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String multicastServerIp = args[0];
        if (!idCorrectInput(args)) {
            System.err.println("Wrong args number");
            return;
        }
        final MulticastSelfSearcher multicastSelfSearcher = new MulticastSelfSearcher(multicastServerIp);
        while(true){
            try {
                multicastSelfSearcher.findProgCopies();
            } catch (IOException e) {
                System.out.println("time is out");
            }
        }

    }

    private static boolean idCorrectInput(String[] args) {
        return Constants.expectedArgsNum == args.length;
    }
}
