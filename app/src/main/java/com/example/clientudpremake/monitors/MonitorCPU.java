package com.example.clientudpremake.monitors;

import static com.example.clientudpremake.workers.websocket.monitors.MonitorUsages.GET_CPU_USAGE;

public class MonitorCPU extends AbstractMonitorUsage {
    @Override
    protected String getUsageLabel() {
        return "CPU Usage";
    }

    @Override
    protected String getMessageType() {
        return GET_CPU_USAGE;
    }
}
