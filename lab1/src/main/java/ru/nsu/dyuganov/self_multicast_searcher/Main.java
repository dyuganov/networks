package ru.nsu.dyuganov.self_multicast_searcher;

public class Main {
    public static void main(String[] args) {
        String multicastServerIp = args[0];
        if (!idCorrectInput(args)) {
            System.err.println("Wrong args number");
            return;
        }


        try {
            final MulticastSelfSearcher multicastSelfSearcher = new MulticastSelfSearcher(multicastServerIp);
            multicastSelfSearcher.findProgCopies();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean idCorrectInput(String[] args) {
        return Constants.expectedArgsNum == args.length;
    }
}
