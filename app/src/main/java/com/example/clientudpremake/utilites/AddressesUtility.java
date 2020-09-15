package com.example.clientudpremake.utilites;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AddressesUtility {

    private static InetAddress serverAddress;

    public void setServerAddress(InetAddress serverAddress) {
        AddressesUtility.serverAddress = serverAddress;
    }

    public static InetAddress getServerAddress() {
        if (serverAddress == null)
            throw new RuntimeException("Server address isn't set");
        return serverAddress;
    }

    public static InetAddress getBroadcastAddress() {
        try {
            return InetAddress.getByName("192.168.1.255");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
