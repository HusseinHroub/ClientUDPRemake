package com.example.clientudpremake.popups;

import android.view.View;

import com.example.clientudpremake.commands.receivers.MonitorCPUUsageCommand;
import com.example.clientudpremake.utilites.AnimationUtils;
import com.example.clientudpremake.utilites.LogUtility;

public class CPUMonitorPopup extends PopUps {
    private final MonitorCPUUsageCommand cpuUsageCommand;

    public CPUMonitorPopup(View view, MonitorCPUUsageCommand cpuUsageCommand) {
        super(view);
        this.cpuUsageCommand = cpuUsageCommand;
    }

    @Override
    public void removeVisibility() {
        cpuUsageCommand.stopMonitoring();
        LogUtility.log("A popup is visible, fading it out!");
        AnimationUtils.fadeOut(view);
    }
}
