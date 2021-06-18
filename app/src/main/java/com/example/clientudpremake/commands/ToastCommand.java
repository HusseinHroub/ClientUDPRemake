package com.example.clientudpremake.commands;

import android.app.Activity;
import android.content.Context;

import com.example.clientudpremake.utilites.ToastUtility;

import lombok.RequiredArgsConstructor;
import lombok.Setter;


@RequiredArgsConstructor
public class ToastCommand implements Command {
    private final String message;

    @Override
    public void apply(Activity activity) {
        ToastUtility.showMessage(message, activity);
    }
}
