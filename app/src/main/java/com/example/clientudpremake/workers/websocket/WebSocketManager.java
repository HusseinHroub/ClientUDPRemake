package com.example.clientudpremake.workers.websocket;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketListener;

import java.io.IOException;

public enum WebSocketManager {
    INSTANCE;

    private WebSocket webSocket;

    //ws://localhost:9721/
    public void connectToServer(String serverURI, WebSocketListener webSocketListener) throws IOException, WebSocketException {
        webSocket.disconnect();
        webSocket = new WebSocketFactory()
                .createSocket(serverURI)
                .addListener(webSocketListener)
                .connect();
    }

    public void disconnect() {
        webSocket.disconnect();
    }

    public void sendText(String text) {
        webSocket.sendText(text);
    }
}
