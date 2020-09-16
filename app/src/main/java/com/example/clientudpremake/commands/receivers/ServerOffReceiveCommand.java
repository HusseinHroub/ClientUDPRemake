package com.example.clientudpremake.commands.receivers;

import android.view.View;

import com.example.clientudpremake.commands.Command;

public class ServerOffReceiveCommand implements Command {
    private final View[] buttons;

    public ServerOffReceiveCommand(View[] buttons) {
        this.buttons = buttons;
    }

    @Override
    public void apply() {
        for (View button : buttons) {
            button.setEnabled(false);
        }
    }
}
