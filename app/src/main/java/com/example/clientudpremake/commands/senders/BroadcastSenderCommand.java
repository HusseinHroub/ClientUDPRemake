package com.example.clientudpremake.commands.senders;

import com.example.clientudpremake.utilites.AddressesUtility;

import java.net.InetAddress;

public class BroadcastSenderCommand extends SenderCommand {

    public BroadcastSenderCommand(String message) {
        super(message);
    }

    @Override
    protected void preSend() {

    }

    @Override
    protected InetAddress getReceiverAddress() {
        return AddressesUtility.getBroadcastAddress();
    }
}
