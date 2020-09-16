package com.example.clientudpremake.commands;

import android.view.View;

import com.example.clientudpremake.utilites.AddressesUtility;
import com.example.clientudpremake.utilites.ThreadsUtilty;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SenderReceiverButton implements Sender {
    private static final int PORT = 9999;
    private final View button;
    private final String message;

    public SenderReceiverButton(View button, String message) {
        this.button = button;
        this.message = message;
    }

    @Override
    public void send() {
        button.setEnabled(false);
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
