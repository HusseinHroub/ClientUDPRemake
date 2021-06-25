package com.example.clientudpremake.commands.receivers;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.example.clientudpremake.commands.UDPCommand;
import com.example.clientudpremake.utilites.AddressesUtility;
import com.example.clientudpremake.utilites.LogUtility;
import com.example.clientudpremake.workers.UDPMessage;
import com.example.clientudpremake.workers.websocket.MyWSClient;
import com.example.clientudpremake.workers.websocket.WebSocketManager;

import java.io.IOException;
import java.util.Locale;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ServerOnReceiveCommand implements UDPCommand {
    private final View[] buttons;

    @Override
    public void apply(Activity activity, UDPMessage udpMessage) {
        AddressesUtility.setServerAddress(udpMessage.getSenderAddress(), activity);
        connectToServer(activity);
    }

    public void connectToServer(Activity activity) {
        try {
            WebSocketManager.INSTANCE.connectToServer(getServerEndPoint(activity), new MyWSClient(buttons, activity));
        } catch (IOException e) {
            LogUtility.log("Couldn't connect to server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getServerEndPoint(Context context) {
        return String.format(Locale.ENGLISH,
                "ws://%s:%d/",
                AddressesUtility.getServerAddress(context).getHostAddress(), 9721);
    }
}
