package com.example.clientudpremake.broadcasts.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class WifiBroadcastReceiver extends BroadcastReceiver {
    private final WifiStateObserver activity;

    public WifiBroadcastReceiver(WifiStateObserver activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        activity.onWifiStatusChange(networkInfo != null && networkInfo.isConnected());

    }
}
