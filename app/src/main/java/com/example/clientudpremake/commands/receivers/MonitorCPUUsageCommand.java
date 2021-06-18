package com.example.clientudpremake.commands.receivers;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.clientudpremake.R;
import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.utilites.AnimationUtils;
import com.example.clientudpremake.utilites.LogUtility;
import com.example.clientudpremake.workers.websocket.WebSocketCommandsFactory;

import java.util.HashSet;
import java.util.Set;

import lombok.Setter;


public class MonitorCPUUsageCommand implements Command {
    private static final long DELAY_AMOUNT = 1000;

    @Setter
    private String cpuValue;
    @Setter
    private long currentSequenceId;

    private final Set<Long> expectedSequenceIds;
    private final Handler handler;

    public MonitorCPUUsageCommand() {
        handler = new Handler();
        expectedSequenceIds = new HashSet<>();
    }

    @Override
    public void apply(Activity activity) {
        if (isValidSequenceId()) {
            LogUtility.log("Received valid sequence id, clearing set and starting actual process");
            expectedSequenceIds.clear();
            processCPUValueShowing(activity);
        } else {
            LogUtility.log("Received in-valid sequence id: " + currentSequenceId);
        }
    }

    private void processCPUValueShowing(Activity activity) {
        View cpuUsageContainer = activity.findViewById(R.id.cpu_usage_container);
        if (cpuUsageContainer.getVisibility() == View.GONE) {
            AnimationUtils.fadeIn(cpuUsageContainer);
            startMonitoringJob(activity);
            LogUtility.log("CPU container is not visible, fading it in, and started monitor job.");
        }
    }

    private boolean isValidSequenceId() {
        return expectedSequenceIds.contains(currentSequenceId);
    }

    private void startMonitoringJob(Activity activity) {
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                TextView cpuTextView = activity.findViewById(R.id.cpu_text_view);
                cpuTextView.setText("CPU Usage: " + cpuValue);
                WebSocketCommandsFactory.getSenderCommand(R.id.monitor_cpu_usage_button).apply(activity);
                LogUtility.log("Displayed new CPU usage value and asking next value from server in: " + DELAY_AMOUNT + "ms");
                handler.postDelayed(this, DELAY_AMOUNT);
            }
        }, DELAY_AMOUNT);
    }

    public void stopMonitoring() {
        LogUtility.log("stopping handler of MonitorCpuUsage");
        handler.removeCallbacksAndMessages(null);
        expectedSequenceIds.clear();
    }

    public void addExpectedSequenceID(long expectedSequenceId) {
        LogUtility.log("added " + expectedSequenceId + " as expected sequence value in set");
        expectedSequenceIds.add(expectedSequenceId);
    }
}
