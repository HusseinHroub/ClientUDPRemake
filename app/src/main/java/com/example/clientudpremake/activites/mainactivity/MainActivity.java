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
import com.example.clientudpremake.commands.SendCommand;
import com.example.clientudpremake.commands.ServerSender;
import com.example.clientudpremake.utilites.ThreadsUtilty;
import com.example.clientudpremake.utilites.ToastUtility;
import com.example.clientudpremake.workers.ReceiveWorker;
import com.google.android.material.navigation.NavigationView;

import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActivityStateObservable implements NavigationView.OnNavigationItemSelectedListener, WifiStateObserver {

    private Map<Integer, SendCommand> sendCommands;
    private static final String TURN_OFF_MONITOR_MESSAGE = "turnOffMonitor";
    private static final String TURN_ON_MONITOR_MESSAGE = "turnOnMonitor";
    private static final String SHUT_DOWN_MESSAGE = "shutDown";
    private static final String RESTART_MESSAGE = "restart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ToolbarHelper(this).init();
        new BroadcastRegisterManager(new WifiBroadcastReceiver(this), this);
        initSendCommands();
        try {
            ReceiveWorker receiveCommand = new ReceiveWorker((m) -> System.out.println(m));
            ThreadsUtilty.getExecutorService().execute(receiveCommand);
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    private void initSendCommands() {
        sendCommands = new HashMap<>();
        sendCommands.put(R.id.turnOffMButton, new ServerSender(TURN_ON_MONITOR_MESSAGE));
        sendCommands.put(R.id.turnOnMButton, new ServerSender(TURN_OFF_MONITOR_MESSAGE));
        sendCommands.put(R.id.shutButton, new ServerSender(SHUT_DOWN_MESSAGE));
        sendCommands.put(R.id.restartButton, new ServerSender(RESTART_MESSAGE));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onWifiStatusChange(boolean enabled) {
        //do whatever u want to do!
    }

    public void sendMessage(View button) {
        if (!sendCommands.containsKey(button.getId())) {
            ToastUtility.showMessageAtTop("Not implemented yet..", this);
        }
        button.setEnabled(false);
        sendCommands.get(button.getId()).send();
    }

}
