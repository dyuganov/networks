package ru.nsu.dyuganov.self_multicast_searcher;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MulticastSelfSearcher {
    private final MulticastSocket multicastSocket = new MulticastSocket(Constants.port);
    private InetAddress groupInetAddress;

    private final UUID thisUUID = UUID.randomUUID();
    byte[] sendMessageBytes = thisUUID.toString().getBytes();
    private final byte[] receiveMessageBytes = new byte[sendMessageBytes.length];
    private final DatagramPacket sendMessagePacket = new DatagramPacket(sendMessageBytes, sendMessageBytes.length, groupInetAddress, Constants.port);
    private DatagramPacket receiveMessagePacket = new DatagramPacket(receiveMessageBytes, receiveMessageBytes.length, groupInetAddress, Constants.port);

    private final HashMap<UUID, String> uuidToIpCopies = new HashMap<>();

    public MulticastSelfSearcher(String multicastGroupIp) throws IOException {
        groupInetAddress = InetAddress.getByName(multicastGroupIp);
        if (!groupInetAddress.isMulticastAddress()) {
            throw new IllegalArgumentException("ip " + multicastGroupIp + " is not multicast address");
        }
        multicastSocket.joinGroup(groupInetAddress);
        multicastSocket.setSoTimeout(Constants.disconnectTimeoutMills);
    }

    void findProgCopies() throws IOException {
        if (!multicastSocket.isConnected()) { // ЗДЕСЬ НЕ ПРОХОДИТ ПРОВЕРКУ
            System.err.println("multicastSocket is not connected");
            return;
        }
        while (true) {
            multicastSocket.send(sendMessagePacket);
            receiveMessage();
            updateCopiesList();
            printCopies();
        }
    }

    void receiveMessage() {
        try {
            multicastSocket.receive(receiveMessagePacket);
            TimeUnit.SECONDS.sleep(Constants.sleepTimeSeconds);
        } catch (IOException | InterruptedException e) {
            System.out.println("time is out");
        }
    }

    void updateCopiesList() {
        if (null == receiveMessagePacket || null == receiveMessagePacket.getAddress()) {
            System.err.println("Cannot update copies list. Receive message packet (or its components) is null.");
            return;
        }
        addCopyId();
    }

    private void addCopyId() {
        UUID messageUUID = UUID.fromString(new String(receiveMessagePacket.getData()));
        if (!messageUUID.equals(thisUUID)) {
            String messageSenderIp = receiveMessagePacket.getAddress().getHostAddress();
            uuidToIpCopies.put(messageUUID, messageSenderIp);
        }
    }

    void printCopies() {
        if (uuidToIpCopies.isEmpty()) {
            System.out.println("No copies");
            return;
        }
        System.out.println("Found my copies:");
        for (String ip : uuidToIpCopies.values()) {
            System.out.println(ip);
        }
    }
}
