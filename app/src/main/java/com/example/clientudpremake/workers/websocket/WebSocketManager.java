package com.example.clientudpremake.workers.websocket;

import com.example.clientudpremake.utilites.ThreadsUtilty;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketListener;

import java.io.IOException;

public enum WebSocketManager {
    INSTANCE;

    private WebSocket webSocket;

    public void connectToServer(String serverURI, WebSocketListener webSocketListener) throws IOException {
        if (webSocket != null) {
            webSocket.disconnect();
        }

        webSocket = new WebSocketFactory()
                .createSocket(serverURI)
                .addListener(webSocketListener);
        connect();

    }

    private void connect() {
        ThreadsUtilty.getExecutorService().execute(() -> {
            try {
                webSocket.connect();
            } catch (WebSocketException e) {
                e.printStackTrace();
            }
        });
    }

    public void disconnect() {
        webSocket.disconnect();
    }

    public void sendText(String text) {
        webSocket.sendText(text);
    }
}
