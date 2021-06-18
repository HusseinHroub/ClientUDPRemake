package com.example.clientudpremake.commands.receivers;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.example.clientudpremake.R;
import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.utilites.AnimationUtils;
import com.example.clientudpremake.utilites.LogUtility;
import com.example.clientudpremake.utilites.ThreadsUtilty;
import com.example.clientudpremake.workers.websocket.WebSocketManager;

import lombok.Setter;

import static com.example.clientudpremake.workers.websocket.WebSocketCommandsFactory.GET_CPU_USAGE;


public class MonitorCPUUsageCommand implements Command {
    private static final long DELAY_AMOUNT = 1000;

    @Setter
    private String cpuValue;

    private final Handler handler;

    public MonitorCPUUsageCommand() {
        handler = new Handler();
    }

    @Override
    public void apply(Activity activity) {
        View cpuUsageContainer = activity.findViewById(R.id.cpu_usage_container);
        if (cpuUsageContainer.getVisibility() == View.GONE) {
            AnimationUtils.fadeIn(cpuUsageContainer);
            startMonitoringJob(activity);
            LogUtility.log("CPU container is not visible, fading it in, and started monitor job.");
        }
    }

    private void startMonitoringJob(Activity activity) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView cpuTextView = activity.findViewById(R.id.cpu_text_view);
                cpuTextView.setText("CPU Usage: " + cpuValue);
                ThreadsUtilty.getExecutorService().execute(() -> WebSocketManager.INSTANCE.sendText(GET_CPU_USAGE));
                LogUtility.log("Displayed new CPU usage value and asking next value from server in: " + DELAY_AMOUNT + "ms");
                handler.postDelayed(this, DELAY_AMOUNT);
            }
        }, DELAY_AMOUNT);
    }

    public void stopMonitoring() {
        LogUtility.log("stopping handler of MonitorCpuUsage");
        handler.removeCallbacksAndMessages(null);
    }
}
