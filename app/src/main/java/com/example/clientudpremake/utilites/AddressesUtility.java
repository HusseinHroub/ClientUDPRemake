package com.example.clientudpremake.utilites;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AddressesUtility {

    private static InetAddress serverAddress;
    private static InetAddress broadcastAddress;

    synchronized public static InetAddress getServerAddress(Context context) {
        if (serverAddress == null) {
            String serverAddressFromShared = MySharedPrefrences.getServerAddress(context);
            LogUtility.log("SharedServerAddress value is: " + serverAddressFromShared);
            if (serverAddressFromShared != null) {
                try {
                    serverAddress = InetAddress.getByName(serverAddressFromShared);
                } catch (UnknownHostException e) {
                    LogUtility.log("Couldn't parse server address: " + serverAddressFromShared);
                    e.printStackTrace();
                }
            }
        }
        return serverAddress;
    }

    public static void setServerAddress(InetAddress serverAddress, Context context) {
        AddressesUtility.serverAddress = serverAddress;
        MySharedPrefrences.setServerAddress(context, serverAddress.getHostAddress());
        LogUtility.log("Initialized server address to the value: " + serverAddress.toString());
    }

    public static InetAddress getBroadcastAddress() {
        if (broadcastAddress == null)
            throw new RuntimeException("BroadCast address isn't set");
        return broadcastAddress;
    }

    public static void initBroadcastAddress(Context context) {
        int broadcast = getBroadcastFromSubnet(context);
        byte[] broadcastBytes = convertAddressIntegerToBytes(broadcast);
        setBroadcastAddressFromBytes(broadcastBytes);
    }

    private static void setBroadcastAddressFromBytes(byte[] broadcastBytes) {
        try {
            AddressesUtility.broadcastAddress = InetAddress.getByAddress(broadcastBytes);
            LogUtility.log("Broadcast address is set to the value: " + AddressesUtility.broadcastAddress.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            LogUtility.log("Exception occurred while trying to set broadcast address");
            throw new RuntimeException("Error occurred while setting broadcast address");
        }
    }

    private static byte[] convertAddressIntegerToBytes(int broadcast) {
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) (broadcast >> (k * 8));
        return quads;
    }

    private static int getBroadcastFromSubnet(Context context) {
        WifiManager wifi = (WifiManager)
                context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        return (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
    }
}
