package com.example.clientudpremake.activites.mainactivity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.clientudpremake.R;
import com.example.clientudpremake.activites.ActivityStateObservable;
import com.example.clientudpremake.broadcasts.BroadcastRegisterManager;
import com.example.clientudpremake.broadcasts.wifi.WifiBroadcastReceiver;
import com.example.clientudpremake.broadcasts.wifi.WifiStateObserver;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends ActivityStateObservable implements NavigationView.OnNavigationItemSelectedListener, WifiStateObserver {

    private ExecutorService executorService;
    private BroadcastRegisterManager broadcastRegisterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ToolbarHelper(this).init();
        executorService = Executors.newFixedThreadPool(5);
        broadcastRegisterManager = new BroadcastRegisterManager(new WifiBroadcastReceiver(this), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        broadcastRegisterManager.isActivityResumed(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        broadcastRegisterManager.isActivityResumed(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        broadcastRegisterManager.isActivityResumed(false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onWifiStatusChange(boolean enabled) {
        //do whatever u want to do!
    }
}
