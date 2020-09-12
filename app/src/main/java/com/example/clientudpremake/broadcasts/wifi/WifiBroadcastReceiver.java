package com.example.clientudpremake.broadcasts.wifi;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.example.clientudpremake.broadcasts.BroadcastReceiver;

public class WifiBroadcastReceiver extends BroadcastReceiver {
    private final WifiStateObserver wifiStateObserver;

    public WifiBroadcastReceiver(WifiStateObserver wifiStateObserver) {
        this.wifiStateObserver = wifiStateObserver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        wifiStateObserver.onWifiStatusChange(networkInfo != null && networkInfo.isConnected());

    }

    @Override
    public String getIntentAction() {
        return "android.net.conn.CONNECTIVITY_CHANGE";
    }
}
