package com.example.clientudpremake.commands.senders;

import android.app.Activity;

import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.utilites.DialogUtility;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConfirmationDialogSendCommand implements Command {
    private final Command command;

    @Override
    public void apply(Activity activity) {
        DialogUtility.showConfirmationDialog(activity, () -> command.apply(activity));
    }
}
