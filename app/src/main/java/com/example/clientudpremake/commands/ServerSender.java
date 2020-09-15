package com.example.clientudpremake.commands;

import com.example.clientudpremake.commands.SendCommand;
import com.example.clientudpremake.utilites.AddressesUtility;
import com.example.clientudpremake.utilites.ThreadsUtilty;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerSender implements SendCommand {
    private static final int PORT = 9999;
    protected final String message;

    public ServerSender(String message) {
        this.message = message;
    }

    @Override
    public void send() {
        ThreadsUtilty.getExecutorService().execute(this::doSend);
    }

    private void doSend() {
        try {
            byte[] buffer = message.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, AddressesUtility.getServerAddress(), PORT);
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(datagramPacket);
            datagramSocket.close();
        } catch (IOException e) {

        }
    }
}
