package com.example.clientudpremake.commands.receivers;

import android.view.View;

import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.utilites.AddressesUtility;
import com.example.clientudpremake.workers.websocket.MyWSClient;
import com.example.clientudpremake.workers.websocket.WebSocketManager;
import com.neovisionaries.ws.client.WebSocketException;

import java.io.IOException;
import java.util.Locale;

public class ServerOnReceiveCommand implements Command {
    private final View[] buttons;

    public ServerOnReceiveCommand(View[] buttons) {
        this.buttons = buttons;
    }

    @Override
    public void apply() {
        try {
            WebSocketManager.INSTANCE.connectToServer(getServerEndPoint(), new MyWSClient());
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
