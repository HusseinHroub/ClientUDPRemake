package com.example.clientudpremake.commands;

import android.content.Context;

import com.example.clientudpremake.utilites.ToastUtility;

public class ToastCommand implements Command {
    private Context context;
    private String message;

    public ToastCommand(Context context, String message) {
        this.context = context;
    }

    @Override
    public void apply() {
        ToastUtility.showMessage(message, context);
    }
}
