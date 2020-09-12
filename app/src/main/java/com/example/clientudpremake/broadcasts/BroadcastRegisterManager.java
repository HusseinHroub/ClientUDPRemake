package com.example.clientudpremake.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

import com.example.clientudpremake.activites.ActivityStateObservable;
import com.example.clientudpremake.activites.mainactivity.ActivityStateObserver;

public class BroadcastRegisterManager implements ActivityStateObserver {
    private final BroadcastReceiver broadcastReceiver;
    private final Context context;
    private boolean isBroadCastRegistered;

    public BroadcastRegisterManager(BroadcastReceiver broadcastReceiver, ActivityStateObservable activityStateObservable) {
        this.broadcastReceiver = broadcastReceiver;
        this.context = activityStateObservable;
        activityStateObservable.attachObserver(this);
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
        context.registerReceiver(broadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        isBroadCastRegistered = true;
    }

    private void unRegisterWifiReceiver() {
        if (!isBroadCastRegistered) {
            context.unregisterReceiver(broadcastReceiver);
            isBroadCastRegistered = false;
        }
    }
}
