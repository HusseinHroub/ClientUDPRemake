package com.example.clientudpremake.workers.websocket;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.example.clientudpremake.utilites.LogUtility;
import com.example.clientudpremake.utilites.ToastUtility;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class MyWSClient extends WebSocketAdapter {
    private Handler handler;
    private Activity activity;
    private View[] buttons;

    public MyWSClient(View[] buttons, Activity activity) {
        this.activity = activity;
        this.buttons = buttons;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) {
        try {
            JSONObject jsonObject = new JSONObject(text);
            handler.post(() -> WebSocketCommandsFactory.getReceiverCommand(jsonObject).apply(activity));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] binary) {
        LogUtility.log("Received images bytes");
        handler.post(() -> BinaryCommands.apply(activity, binary));//currently any binary message means its a screenshot.
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) {
        LogUtility.log("onError websocket, cause is: " + cause.getMessage());
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) {
        LogUtility.log("On connect error: " + exception.getMessage());
    }

    @Override
    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
//        LogUtility.log("Sent frame: " + frame.getPayloadText());
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        LogUtility.log("Disconnected from webscoket");
        handler.post(() -> {
            for (View button : buttons) {
                button.setEnabled(false);
            }
            if (closedByServer) {
                ToastUtility.showMessage("Server got closed", activity);
            } else {
                ToastUtility.showMessage("Lost connection with server", activity);
            }

        });
        super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
        LogUtility.log("Connected to websocket!");
        handler.post(() -> {
            for (View button : buttons) {
                button.setEnabled(true);
            }
        });
    }

}
