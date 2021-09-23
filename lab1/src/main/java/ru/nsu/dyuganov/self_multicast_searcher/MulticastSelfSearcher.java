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
    private final InetAddress groupInetAddress;

    private final UUID thisUUID = UUID.randomUUID();
    private final byte[] sendBytes = thisUUID.toString().getBytes();
    private final byte[] receiveBytes = new byte[sendBytes.length];
    private final DatagramPacket sendMessagePacket;
    private DatagramPacket receiveMessagePacket;

    private final HashMap<UUID, String> uuidToIpCopies = new HashMap<>();
    private final HashMap<UUID, Long> uuidToLastMessageTimeoutMills = new HashMap<>();

    public MulticastSelfSearcher(String multicastGroupIp) throws IOException {
        groupInetAddress = InetAddress.getByName(multicastGroupIp);
        if (!groupInetAddress.isMulticastAddress()) {
            throw new IllegalArgumentException("ip " + multicastGroupIp + " is not multicast address");
        }
        sendMessagePacket = new DatagramPacket(sendBytes, sendBytes.length, groupInetAddress, Constants.port);
        receiveMessagePacket = new DatagramPacket(receiveBytes, receiveBytes.length, groupInetAddress, Constants.port);
        multicastSocket.joinGroup(groupInetAddress);
        multicastSocket.setSoTimeout(Constants.disconnectTimeoutMills);
    }

    void findProgCopies() throws IOException {
        while (true) {
            //uuidToIpCopies.clear();
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
        //addCopyId();
        addLazyCopyId();
    }

    private void addCopyId() {
        UUID otherUUID = UUID.fromString(new String(receiveMessagePacket.getData()));
        if (otherUUID.equals(thisUUID)) {
            return;
        }
        String messageSenderIp = receiveMessagePacket.getAddress().getHostAddress();
        //uuidToIpCopies.put(otherUUID, messageSenderIp);
        long currentTime = System.currentTimeMillis();
        if(!uuidToIpCopies.containsKey(otherUUID)){
            uuidToIpCopies.put(otherUUID, messageSenderIp);
            uuidToLastMessageTimeoutMills.put(otherUUID, currentTime);
            return;
        }

/*        long lastMsgTimeGapMills = currentTime - uuidToLastMessageTimeoutMills.get(otherUUID);
        if(Constants.clearUUIDListTimeoutMills < lastMsgTimeGapMills){
            uuidToLastMessageTimeoutMills.remove(otherUUID);
            uuidToIpCopies.remove(otherUUID);
            return;
        }*/
        uuidToLastMessageTimeoutMills.put(otherUUID, currentTime);
        checkForTimeout();
    }

    private void addLazyCopyId() {
        UUID otherUUID = UUID.fromString(new String(receiveMessagePacket.getData()));
        if (otherUUID.equals(thisUUID)) {
            return;
        }
        String messageSenderIp = receiveMessagePacket.getAddress().getHostAddress();

        uuidToIpCopies.put(otherUUID, messageSenderIp);
        checkForLazyTimeout();
    }

    private void checkForTimeout(){
        long currentTimeMills = System.currentTimeMillis();
        for(var item : uuidToLastMessageTimeoutMills.entrySet()){
            long lastMsgTimeGapMills = currentTimeMills - item.getValue();
            if(Constants.clearUUIDListTimeoutMills > lastMsgTimeGapMills){
                UUID keyToRemove = item.getKey();
                uuidToIpCopies.remove(keyToRemove);
                uuidToLastMessageTimeoutMills.remove(keyToRemove);
            }
        }
    }

    private long lastTimeCheck = System.currentTimeMillis();
    private void checkForLazyTimeout(){
        long currTimeMills = System.currentTimeMillis();
        long timeGap = currTimeMills - lastTimeCheck;
        if(Constants.clearUUIDListTimeoutMills > timeGap){
            uuidToIpCopies.clear();
            lastTimeCheck = currTimeMills;
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
