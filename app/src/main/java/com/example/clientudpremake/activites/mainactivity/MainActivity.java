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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ToolbarHelper(this).init();
        new BroadcastRegisterManager(new WifiBroadcastReceiver(this), this);
        executorService = Executors.newFixedThreadPool(5);
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
