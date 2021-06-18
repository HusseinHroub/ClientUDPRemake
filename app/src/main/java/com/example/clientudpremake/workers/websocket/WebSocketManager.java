package com.example.clientudpremake.workers.websocket;

import com.example.clientudpremake.utilites.LogUtility;
import com.example.clientudpremake.utilites.ThreadsUtilty;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;

import java.io.IOException;

public enum WebSocketManager {
    INSTANCE;

    private WebSocket webSocket;
    private String currentServerURI;

    public void connectToServer(String serverURI, WebSocketListener webSocketListener) throws IOException {
        if (isAlreadyConnectedToServer()) {
            LogUtility.log("Websocket state is OPEN and same server uri, hence no need to connect again.");
            return;
        }
        doConnectToServer(serverURI, webSocketListener);
    }

    private void doConnectToServer(String serverURI, WebSocketListener webSocketListener) throws IOException {
        LogUtility.log("Connecting to websocket with serverURI: " + serverURI);
        currentServerURI = serverURI;
        webSocket = new WebSocketFactory()
                .createSocket(serverURI)
                .addListener(webSocketListener);
        connect();
    }

    private boolean isAlreadyConnectedToServer() {
        return webSocket != null &&
                webSocket.getState() == WebSocketState.OPEN &&
                webSocket.getURI().toString().equals(currentServerURI);
    }

    private void connect() {
        ThreadsUtilty.getExecutorService().execute(() -> {
            try {
                webSocket.connect();
                LogUtility.log("Connected websocket in new thread");
            } catch (WebSocketException e) {
                e.printStackTrace();
                LogUtility.log("Failed to connect websocket");
            }
        });
    }

    public void disconnect() {
        webSocket.disconnect();
    }

    public void sendText(String text) {
        LogUtility.log("Sending: " + text + " to websocket");
        webSocket.sendText(text);
    }
}
