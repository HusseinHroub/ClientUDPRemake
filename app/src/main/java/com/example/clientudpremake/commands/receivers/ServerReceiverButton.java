package com.example.clientudpremake.commands.receivers;

import android.view.View;

import com.example.clientudpremake.commands.Command;

public class ServerReceiverButton implements Command {
    private final View button;
    private final String message;

    public ServerReceiverButton(View button, String message) {
        this.button = button;
        this.message = message;
    }

    @Override
    public void apply() {
        button.setEnabled(true);
    }
}
