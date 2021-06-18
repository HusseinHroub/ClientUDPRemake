package com.example.clientudpremake.commands.receivers;

import android.app.Activity;
import android.view.View;

import com.example.clientudpremake.commands.Command;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServerOffReceiveCommand implements Command {
    private final View[] buttons;

    @Override
    public void apply(Activity activity) {
        for (View button : buttons) {
            button.setEnabled(false);
        }
    }
}
