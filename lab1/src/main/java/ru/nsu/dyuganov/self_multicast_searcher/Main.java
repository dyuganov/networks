package ru.nsu.dyuganov.self_multicast_searcher;

public class Main {
    public static void main(String[] args) {
        if (Constants.expectedArgsNum != args.length) {
            System.err.println("Wrong args number");
            return;
        }
        String multicastServerIp = args[0];
        try {
            final MulticastSelfSearcher multicastSelfSearcher = new MulticastSelfSearcher(multicastServerIp);
            multicastSelfSearcher.findProgCopies();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
