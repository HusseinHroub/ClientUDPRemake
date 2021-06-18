package com.example.clientudpremake.commands.senders;

import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.utilites.LogUtility;
import com.example.clientudpremake.utilites.ThreadsUtilty;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class SenderCommand implements Command {

    private static final int PORT = 9722;
    private final String message;


    @Override
    public void apply() {
        preSend();
        ThreadsUtilty.getExecutorService().execute(this::doSend);
    }

    protected abstract void preSend();

    private void doSend() {
        try {
            byte[] buffer = message.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, getReceiverAddress(), PORT);
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(datagramPacket);
            datagramSocket.close();
            LogUtility.log("Sent message of data: " + message + " to server using UDP protocol");
        } catch (IOException e) {
            LogUtility.log("An exception occurred while sending UDP message to server");
            e.printStackTrace();
        }
    }

    protected abstract InetAddress getReceiverAddress();
}
