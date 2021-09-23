package ru.nsu.dyuganov.self_multicast_searcher;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MulticastSelfSearcher {
    private final MulticastSocket multicastSocket = new MulticastSocket();

    private final byte[] receiveMessageBytes = new byte[255];
    private DatagramPacket receiveMessagePacket = new DatagramPacket(receiveMessageBytes, receiveMessageBytes.length);
    private final DatagramPacket sendMessagePacket;

    private final UUID thisUUID = UUID.randomUUID();

    private final Map<String, String> uuidToIpCopies = new HashMap<>();

    public MulticastSelfSearcher(String multicastIp) throws IOException {
        InetAddress groupInetAddress = InetAddress.getByName(multicastIp);
        multicastSocket.joinGroup(groupInetAddress);
        byte[] sendMessageBytes = Constants.sendMessage.getBytes();
        sendMessagePacket = new DatagramPacket(sendMessageBytes, sendMessageBytes.length, groupInetAddress, Constants.port);
        multicastSocket.setSoTimeout(Constants.disconnectTimeoutMills);

    }

    void findProgCopies() throws IOException {
        multicastSocket.setSoTimeout(Constants.disconnectTimeoutMills);
        multicastSocket.send(sendMessagePacket);
        multicastSocket.receive(receiveMessagePacket);
            /*try {
            } catch (IOException e) {
                System.out.println("time is out!");
            }*/
        updateCopiesList(receiveMessagePacket);
        printCopies();
    }

    void receiveMessage() {
        try {
            multicastSocket.receive(receiveMessagePacket);
        } catch (IOException e) {
            System.out.println("time is out!");
        }
    }

    void updateCopiesList(DatagramPacket receiveMessagePacket) {
        uuidToIpCopies.put(Arrays.toString(receiveMessagePacket.getData()), receiveMessagePacket.getAddress().getHostName());
    }

    void printCopies() {
        if (uuidToIpCopies.isEmpty()) {
            System.out.println("No copies!");
            return;
        }
        System.out.println("Found my copies:");
        for (String ip : uuidToIpCopies.values()) {
            System.out.println(ip);
        }
    }
}
