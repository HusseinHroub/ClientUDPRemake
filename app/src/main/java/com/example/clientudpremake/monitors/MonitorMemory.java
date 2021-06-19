package com.example.clientudpremake.monitors;

import static com.example.clientudpremake.workers.websocket.monitors.MonitorUsages.GET_MEMORY_USAGE;

public class MonitorMemory extends AbstractMonitorUsage {

    @Override
    protected String getUsageLabel() {
        return "Memory Usage";
    }

    @Override
    protected String getMessageType() {
        return GET_MEMORY_USAGE;
    }
}
