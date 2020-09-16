package com.example.clientudpremake.commands.senders;

import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.utilites.ThreadsUtilty;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public abstract class SenderCommand implements Command {

    private static final int PORT = 9999;
    private final String message;

    public SenderCommand(String message) {
        this.message = message;
    }

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
        } catch (IOException e) {

        }
    }

    protected abstract InetAddress getReceiverAddress();
}
