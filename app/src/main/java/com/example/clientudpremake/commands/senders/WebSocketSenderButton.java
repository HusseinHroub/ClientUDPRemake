package com.example.clientudpremake.commands.senders;

import android.app.Activity;

import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.utilites.ThreadsUtilty;
import com.example.clientudpremake.workers.websocket.WebSocketManager;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WebSocketSenderButton implements Command {

    private final String message;

    @Override
    public void apply(Activity activity) {
        ThreadsUtilty.getExecutorService().execute(() -> WebSocketManager.INSTANCE.sendText(message));
    }
}
