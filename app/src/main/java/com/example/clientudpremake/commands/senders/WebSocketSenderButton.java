package com.example.clientudpremake.commands.senders;

import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.utilites.ThreadsUtilty;
import com.example.clientudpremake.workers.websocket.WebSocketManager;

public class WebSocketSenderButton implements Command {
    private final String message;

    public WebSocketSenderButton(String message) {
        this.message = message;
    }

    @Override
    public void apply() {
        ThreadsUtilty.getExecutorService().execute(() -> WebSocketManager.INSTANCE.sendText(message));
    }
}
