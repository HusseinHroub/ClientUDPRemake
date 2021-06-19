package com.example.clientudpremake.workers.websocket.monitors;

import android.app.Activity;

import com.example.clientudpremake.R;
import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.monitors.AbstractMonitorUsage;
import com.example.clientudpremake.monitors.MonitorCPU;
import com.example.clientudpremake.monitors.MonitorMemory;
import com.example.clientudpremake.utilites.LogUtility;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;

public class MonitorUsages {
    public static final String GET_CPU_USAGE = "getCpuUsage";
    public static final String GET_MEMORY_USAGE = "getMemoryUsage";

    private final AbstractMonitorUsage monitorCPUUsage;
    private final AbstractMonitorUsage monitorMemoryUsage;
    private final Map<String, AbstractMonitorUsage> monitors = new HashMap<>();

    public MonitorUsages() {
        monitorCPUUsage = new MonitorCPU();
        monitorMemoryUsage = new MonitorMemory();
        initMonitorsMap();
    }

    private void initMonitorsMap() {
        monitors.put("" + R.id.monitor_cpu_usage_button, monitorCPUUsage);
        monitors.put("" + R.id.monitor_memory_usage_button, monitorMemoryUsage);
        monitors.put(GET_CPU_USAGE, monitorCPUUsage);
        monitors.put(GET_MEMORY_USAGE, monitorMemoryUsage);
    }

    public void monitorNewData(Activity activity, MonitoringDetailsData monitoringDetailsData) {
        AbstractMonitorUsage abstractMonitorUsage = getMonitorUsage(monitoringDetailsData.type);
        abstractMonitorUsage.monitorNewRecord(activity, monitoringDetailsData.sequenceId, monitoringDetailsData.value);
    }

    public Command getMonitorCommandFromReceiveEvent(String messageType, String value, long sequenceId) {
        return new MonitorReceiveCommand(messageType, value, sequenceId);
    }

    public void sendMonitorRequest(int id) {
        getMonitorUsage(id).sendNewReadingRequest();
    }

    public void stopMonitors(Activity activity) {
        monitorCPUUsage.stopMonitoring(activity);
        monitorMemoryUsage.stopMonitoring(activity);
    }

    private AbstractMonitorUsage getMonitorUsage(int buttonId) {
        return getMonitorUsage("" + buttonId);
    }

    private AbstractMonitorUsage getMonitorUsage(String type) {
        AbstractMonitorUsage abstractMonitorUsage = monitors.get(type);
        if (abstractMonitorUsage == null) {
            LogUtility.log("Got null monitor usage with type: " + type);
            throw new IllegalArgumentException("Got null monitor usage with type: " + type);
        }
        return abstractMonitorUsage;
    }

    private class MonitorReceiveCommand extends MonitoringDetailsData implements Command {

        public MonitorReceiveCommand(String type, String value, long sequenceId) {
            super(type, value, sequenceId);
        }

        @Override
        public void apply(Activity activity) {
            monitorNewData(activity, this);
        }
    }

    @RequiredArgsConstructor
    private static class MonitoringDetailsData {
        private final String type;
        private final String value;
        private final long sequenceId;
    }
}
