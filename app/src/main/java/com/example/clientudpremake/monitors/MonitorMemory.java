package com.example.clientudpremake.monitors;

public class MonitorMemory extends AbstractMonitorUsage {
    @Override
    protected String getUsageLabel() {
        return "Memory Usage";
    }

    @Override
    protected String getMessageType() {
        return "getMemoryUsage";
    }
}
