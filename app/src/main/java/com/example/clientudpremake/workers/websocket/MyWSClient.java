package com.example.clientudpremake.workers.websocket;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;

import java.util.List;
import java.util.Map;

public class MyWSClient extends WebSocketAdapter {

    @Override
    public void onTextMessage(WebSocket websocket, String text) {
        System.out.println("received message len: " + text.length());
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] binary) {
        System.out.println("received binary len: " + binary.length);
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
        System.out.println("CONNECTED!");
    }

}
