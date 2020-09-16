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
import com.example.clientudpremake.utilites.CommandsFactory;
import com.example.clientudpremake.utilites.ThreadsUtilty;
import com.example.clientudpremake.utilites.ToastUtility;
import com.example.clientudpremake.workers.ReceiveWorker;
import com.google.android.material.navigation.NavigationView;

import java.net.SocketException;

public class MainActivity extends ActivityStateObservable implements NavigationView.OnNavigationItemSelectedListener, WifiStateObserver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            setButtonsEnabled(true);
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        findViewById(R.id.turnOffMButton).setEnabled(enabled);
        findViewById(R.id.turnOnMButton).setEnabled(enabled);
        findViewById(R.id.shutButton).setEnabled(enabled);
        findViewById(R.id.restartButton).setEnabled(enabled);
    }

    public void sendMessage(View button) {
        CommandsFactory.getSenderCommand(button).apply();
    }

    public void receiveMessage(String message) {
        CommandsFactory.getReceiverCommand(message, this).apply();
    }
}
