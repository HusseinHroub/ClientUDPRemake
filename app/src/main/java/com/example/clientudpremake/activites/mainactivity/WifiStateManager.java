package com.example.clientudpremake.activites.mainactivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

public class WifiStateManager implements ActivityStateObserver {
    private final BroadcastReceiver wifiBroadcastReceiver;
    private final Context context;
    private boolean isWifiBroadCastRegistered;

    public WifiStateManager(BroadcastReceiver wifiBroadcastReceiver, Context context) {
        this.wifiBroadcastReceiver = wifiBroadcastReceiver;
        this.context = context;
    }

    @Override
    public void isActivityResumed(boolean isActivityResume) {
        if (isActivityResume) {
            registerWifiReceiver();
        } else {
            unRegisterWifiReceiver();
        }
    }

    private void registerWifiReceiver() {
        context.registerReceiver(wifiBroadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        isWifiBroadCastRegistered = true;
    }

    private void unRegisterWifiReceiver() {
        if (!isWifiBroadCastRegistered) {
            context.unregisterReceiver(wifiBroadcastReceiver);
            isWifiBroadCastRegistered = false;
        }
    }
}
