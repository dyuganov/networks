package ru.nsu.dyuganov.selfMulticastSearcher;

import java.io.IOException;
import java.net.*;

public class MulticastSelfSearcher {
    private final MulticastSocket udpMulticastSocket = new MulticastSocket();
    private String ip;
    //private final DatagramSocket udpMulticastSocket = new DatagramSocket(1488);

    public MulticastSelfSearcher(String ip) throws IOException {
        this.ip = ip;
    }

    void findProgCopies(){
        //udpMulticastSocket.joinGroup();
    }
}
