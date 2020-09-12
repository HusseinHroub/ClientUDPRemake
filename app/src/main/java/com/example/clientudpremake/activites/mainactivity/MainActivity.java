package com.example.clientudpremake.activites.mainactivity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clientudpremake.R;
import com.example.clientudpremake.broadcasts.wifi.WifiBroadcastReceiver;
import com.example.clientudpremake.broadcasts.wifi.WifiStateObserver;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, WifiStateObserver {

    private ExecutorService executorService;
    private WifiStateManager wifiStateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ToolbarHelper(this).init();
        executorService = Executors.newFixedThreadPool(5);
        wifiStateManager = new WifiStateManager(new WifiBroadcastReceiver(this), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wifiStateManager.isActivityResumed(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wifiStateManager.isActivityResumed(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        wifiStateManager.isActivityResumed(false);
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
