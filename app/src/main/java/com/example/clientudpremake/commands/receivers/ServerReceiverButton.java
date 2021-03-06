package com.example.clientudpremake.commands.receivers;

import android.app.Activity;
import android.view.View;

import com.example.clientudpremake.commands.Command;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServerReceiverButton implements Command {
    private final View button;
    private final String message;

    @Override
    public void apply(Activity activity) {
        button.setEnabled(true);
    }
}
