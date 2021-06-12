package com.example.clientudpremake.activites.mainactivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.clientudpremake.R;
import com.example.clientudpremake.activites.ActivityStateObservable;
import com.example.clientudpremake.broadcasts.BroadcastRegisterManager;
import com.example.clientudpremake.broadcasts.wifi.WifiBroadcastReceiver;
import com.example.clientudpremake.broadcasts.wifi.WifiStateObserver;
import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.commands.receivers.ServerOnReceiveCommand;
import com.example.clientudpremake.commands.senders.BroadcastSenderCommand;
import com.example.clientudpremake.utilites.AddressesUtility;
import com.example.clientudpremake.utilites.ThreadsUtilty;
import com.example.clientudpremake.utilites.ToastUtility;
import com.example.clientudpremake.workers.ReceiveWorker;
import com.example.clientudpremake.workers.websocket.WebSocketCommandsFactory;
import com.google.android.material.navigation.NavigationView;

import java.net.SocketException;

public class MainActivity extends ActivityStateObservable implements NavigationView.OnNavigationItemSelectedListener, WifiStateObserver {
    private static final String IS_SERVER_ON = "isServerOn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        new ToolbarHelper(this).init();
        new BroadcastRegisterManager(new WifiBroadcastReceiver(this), this);
        initReceiveWorker();

    }

    private void initReceiveWorker() {
        try {
            ThreadsUtilty.getExecutorService().execute(new ReceiveWorker(this::receiveMessage));
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onWifiStatusChange(boolean enabled) {
        if (!enabled) {
            ToastUtility.showMessage("Wifi must be enabled in order to work.", this);
            setButtonsEnabled(false);
        } else {
            AddressesUtility.initBroadcastAddress(this);
            new BroadcastSenderCommand("isServerOn").apply();
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        findViewById(R.id.turnOffMButton).setEnabled(enabled);
        findViewById(R.id.turnOnMButton).setEnabled(enabled);
        findViewById(R.id.shutButton).setEnabled(enabled);
        findViewById(R.id.restartButton).setEnabled(enabled);
    }

    public void sendMessage(View button) {
        WebSocketCommandsFactory.getSenderCommand(button).apply();
    }

    public void receiveMessage(String message) {
        getReceiveCommand(message).apply();
    }

    private Command getReceiveCommand(String message) {
        switch (message) {
            case IS_SERVER_ON:
                return new ServerOnReceiveCommand(getApplicationContext(), getActivityButtons());
            default:
                throw new RuntimeException("No command found for receiving");
        }
    }

    private View[] getActivityButtons() {
        return new View[]{findViewById(R.id.turnOnMButton),
                findViewById(R.id.turnOffMButton),
                findViewById(R.id.shutButton),
                findViewById(R.id.restartButton)};
    }
}
