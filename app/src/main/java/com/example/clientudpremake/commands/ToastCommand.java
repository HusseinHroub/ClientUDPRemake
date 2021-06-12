package com.example.clientudpremake.commands;

import android.content.Context;

import com.example.clientudpremake.utilites.ToastUtility;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ToastCommand implements Command {
    private final Context context;
    private final String message;

    @Override
    public void apply() {
        ToastUtility.showMessage(message, context);
    }
}
