package com.example.clientudpremake.workers.websocket;

import android.app.Activity;

import com.example.clientudpremake.commands.receivers.LoadingDialogReceiveCommand;
import com.example.clientudpremake.commands.receivers.ScreenShotCommand;

public class BinaryCommands {
    public static void apply(Activity activity, byte[] binary) {
        new LoadingDialogReceiveCommand(new ScreenShotCommand(binary)).apply(activity);
    }
}
