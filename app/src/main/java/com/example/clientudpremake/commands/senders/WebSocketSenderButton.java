package com.example.clientudpremake.commands.senders;

import android.app.Activity;

import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.utilites.ThreadsUtilty;
import com.example.clientudpremake.workers.websocket.WebSocketManager;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;

public class WebSocketSenderButton implements Command {

    private final String message;
    public <T> WebSocketSenderButton(T model) {
        this.message = new Gson().toJson(model);
    }

    @Override
    public void apply(Activity activity) {
        ThreadsUtilty.getExecutorService().execute(() -> WebSocketManager.INSTANCE.sendText(message));
    }
}
