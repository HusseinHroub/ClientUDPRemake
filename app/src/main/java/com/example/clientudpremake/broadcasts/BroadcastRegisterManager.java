package com.example.clientudpremake.broadcasts;

import android.content.Context;
import android.content.IntentFilter;

import com.example.clientudpremake.activites.ActivityStateObservable;
import com.example.clientudpremake.activites.ActivityStateObserver;

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
    public void onActivityStateChange(boolean isActivityResumed) {
        if (isActivityResumed) {
            registerReceiver();
        } else {
            unRegisterReceiver();
        }
    }

    private void registerReceiver() {
        context.registerReceiver(broadcastReceiver, new IntentFilter(broadcastReceiver.getIntentAction()));
        isBroadCastRegistered = true;
    }

    private void unRegisterReceiver() {
        if (!isBroadCastRegistered) {
            context.unregisterReceiver(broadcastReceiver);
            isBroadCastRegistered = false;
        }
    }
}
