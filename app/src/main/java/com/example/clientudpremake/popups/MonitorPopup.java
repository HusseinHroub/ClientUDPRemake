package com.example.clientudpremake.popups;

import android.view.View;

import com.example.clientudpremake.utilites.AnimationUtils;
import com.example.clientudpremake.utilites.LogUtility;

public class MonitorPopup extends PopUps {
    private final Runnable stopMonitorRunnable;

    public MonitorPopup(View view, Runnable stopMonitorRunnable) {
        super(view);
        this.stopMonitorRunnable = stopMonitorRunnable;
    }

    @Override
    public void removeVisibility() {
        stopMonitorRunnable.run();
        LogUtility.log("A popup is visible, fading it out!");
        AnimationUtils.fadeOut(view);
    }
}