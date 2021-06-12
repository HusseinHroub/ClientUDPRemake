package com.example.clientudpremake.workers.websocket;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class MyWSClient extends WebSocketAdapter {
    private Handler handler;
    private Context context;
    public MyWSClient(Context context) {
        this.context = context;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) {
        try {
            JSONObject jsonObject = new JSONObject(text);
            handler.post(() -> WebSocketCommandsFactory.getReceiverCommand(context, jsonObject).apply());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
