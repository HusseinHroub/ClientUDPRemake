package com.example.clientudpremake.activites.mainactivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.clientudpremake.R;
import com.example.clientudpremake.activites.ActivityStateObservable;
import com.example.clientudpremake.broadcasts.BroadcastRegisterManager;
import com.example.clientudpremake.broadcasts.wifi.WifiBroadcastReceiver;
import com.example.clientudpremake.broadcasts.wifi.WifiStateObserver;
import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.commands.receivers.ServerOnReceiveCommand;
import com.example.clientudpremake.commands.senders.BroadcastSenderCommand;
import com.example.clientudpremake.popups.FadeOutPopup;
import com.example.clientudpremake.popups.MonitorPopup;
import com.example.clientudpremake.popups.PopUps;
import com.example.clientudpremake.services.FileTransferManager;
import com.example.clientudpremake.utilites.AddressesUtility;
import com.example.clientudpremake.utilites.LogUtility;
import com.example.clientudpremake.utilites.ThreadsUtilty;
import com.example.clientudpremake.utilites.ToastUtility;
import com.example.clientudpremake.workers.ReceiveWorker;
import com.google.android.material.navigation.NavigationView;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import static com.example.clientudpremake.workers.websocket.WebSocketCommandsFactory.getSenderCommand;
import static com.example.clientudpremake.workers.websocket.WebSocketCommandsFactory.monitorUsages;

public class MainActivity extends ActivityStateObservable implements NavigationView.OnNavigationItemSelectedListener, WifiStateObserver {
    private static final String IS_SERVER_ON = "isServerOn";
    private static final int CHOOSE_FILE_REQUEST_CODE = 11;
    private List<PopUps> popUps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtility.log("Application started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        new ToolbarHelper(this).init();
        new BroadcastRegisterManager(new WifiBroadcastReceiver(this), this);
        initPopups();
        initReceiveWorker();

    }

    private void initPopups() {
        popUps = new ArrayList<>();
        popUps.add(new FadeOutPopup(findViewById(R.id.image_container)));
        popUps.add(new MonitorPopup(findViewById(R.id.monitor_usage_container), () -> monitorUsages.stopMonitors(this)));
    }

    private void initReceiveWorker() {
        try {
            ThreadsUtilty.getExecutorService().execute(new ReceiveWorker(this::receiveMessage));
            LogUtility.log("Receive worked initialized");
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
            LogUtility.log("Wifi is disabled");
            ToastUtility.showMessage("Wifi must be enabled in order to work.", this);
            setButtonsEnabled(false);
        } else {
            LogUtility.log("Wifi is enabled");
            AddressesUtility.initBroadcastAddress(this);
            new BroadcastSenderCommand("isServerOn").apply(this);
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        for (View view : getActivityButtons()) {
            view.setEnabled(enabled);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sendWebSocketMessage(View button) {
        getSenderCommand(button.getId()).apply(this);
    }

    public void sendWebSocketMessageForMonitor(View button) {
        findViewById(R.id.loading_dialog).setVisibility(View.VISIBLE);
        monitorUsages.sendMonitorRequest(button.getId());
    }

    public void receiveMessage(String message) {
        getReceiveCommand(message).apply(this);
    }

    public void viewChooseFileExplorer(View view) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, CHOOSE_FILE_REQUEST_CODE);
    }

    private Command getReceiveCommand(String message) {
        switch (message) {
            case IS_SERVER_ON:
                return new ServerOnReceiveCommand(getActivityButtons());
            default:
                throw new RuntimeException("No command found for receiving");
        }
    }

    private View[] getActivityButtons() {
        return new View[]{findViewById(R.id.turnOnMButton),
                findViewById(R.id.turnOffMButton),
                findViewById(R.id.shutButton),
                findViewById(R.id.restartButton),
                findViewById(R.id.imageButton),
                findViewById(R.id.monitor_cpu_usage_button),
                findViewById(R.id.monitor_memory_usage_button),
                findViewById(R.id.send_file_button)};
    }

    @Override
    public void onBackPressed() {
        boolean wasPopupRemoved = removePopup();
        if (!wasPopupRemoved) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            FileTransferManager fileTransferManager = new FileTransferManager(this, data.getData());
            fileTransferManager.sendFileToServer();
        }
    }

    private boolean removePopup() {
        boolean wasPopupRemoved = false;
        for (PopUps popUps : popUps) {
            if (popUps.isVisible()) {
                popUps.removeVisibility();
                wasPopupRemoved = true;
                break;
            }
        }
        return wasPopupRemoved;
    }
}