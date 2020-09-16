package com.example.clientudpremake.commands;

import android.view.View;

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
