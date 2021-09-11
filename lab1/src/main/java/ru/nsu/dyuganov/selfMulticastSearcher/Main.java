package ru.nsu.dyuganov.selfMulticastSearcher;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.SortedMap;
import java.util.logging.SocketHandler;

public class Main {
    public static void main(String[] args) throws IOException {

        final MulticastSelfSearcher multicastSelfSearcher = new MulticastSelfSearcher(args[0]);

        while (true){
            multicastSelfSearcher.findProgCopies();
        }

/*
        var udpSocket = new DatagramSocket(8888);
        var content = "hello world";

        var datagram = new DatagramPacket(
                content.getBytes(),
                content.getBytes().length,
                InetAddress.getByName("127.0.0.1"),
                8888
        );
        udpSocket.send(datagram);

        // or

        udpSocket.send(new DatagramPacket(
                content.getBytes(),
                content.getBytes().length,
                InetAddress.getByName("127.0.0.1"),
                8888
        ));
        */

    }
}
