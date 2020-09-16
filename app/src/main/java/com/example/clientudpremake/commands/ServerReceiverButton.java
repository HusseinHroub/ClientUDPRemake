package com.example.clientudpremake.commands;

import android.view.View;

public class ServerReceiverButton implements Sender {
    private final View button;
    private final String message;

    public ServerReceiverButton(View button, String message) {
        this.button = button;
        this.message = message;
    }

    @Override
    public void send() {
        button.setEnabled(true);
    }
}
