package com.example.clientudpremake.commands.receivers;

import android.view.View;

import com.example.clientudpremake.commands.Command;

public class ServerOnReceiveCommand implements Command {
    private final View[] buttons;

    public ServerOnReceiveCommand(View[] buttons) {
        this.buttons = buttons;
    }

    @Override
    public void apply() {
        for (View button : buttons) {
            button.setEnabled(true);
        }
    }
}
