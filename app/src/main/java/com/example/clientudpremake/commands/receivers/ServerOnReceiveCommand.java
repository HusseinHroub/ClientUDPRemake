package com.example.clientudpremake.commands.receivers;

import android.content.Context;
import android.view.View;

import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.utilites.AddressesUtility;
import com.example.clientudpremake.workers.websocket.MyWSClient;
import com.example.clientudpremake.workers.websocket.WebSocketManager;

import java.io.IOException;
import java.util.Locale;

public class ServerOnReceiveCommand implements Command {
    private final View[] buttons;
    private Context context;

    public ServerOnReceiveCommand(Context context, View[] buttons) {
        this.buttons = buttons;
        this.context = context;
    }

    @Override
    public void apply() {
        try {
            WebSocketManager.INSTANCE.connectToServer(getServerEndPoint(), new MyWSClient(context));
            for (View button : buttons) {
                button.setEnabled(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getServerEndPoint() {
        return String.format(Locale.ENGLISH,
                "ws://%s:%d/",
                AddressesUtility.getServerAddress().getHostAddress(), 9721);
    }
}
