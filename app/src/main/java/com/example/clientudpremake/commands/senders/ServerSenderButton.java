package com.example.clientudpremake.commands.senders;

import android.view.View;

import com.example.clientudpremake.utilites.AddressesUtility;

import java.net.InetAddress;

public class ServerSenderButton extends SenderCommand {
    private final View button;

    public ServerSenderButton(View button, String message) {
        super(message);
        this.button = button;
    }

    @Override
    protected void preSend() {
        button.setEnabled(false);
    }

    @Override
    protected InetAddress getReceiverAddress() {
        return AddressesUtility.getServerAddress();
    }
}
