package com.example.clientudpremake.monitors;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.clientudpremake.R;
import com.example.clientudpremake.models.UsageModel;
import com.example.clientudpremake.utilites.AnimationUtils;
import com.example.clientudpremake.utilites.LogUtility;
import com.example.clientudpremake.utilites.ThreadsUtilty;
import com.example.clientudpremake.workers.websocket.WebSocketManager;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;


abstract public class AbstractMonitorUsage {
    private static final long DELAY_AMOUNT = 1000;

    private final Set<Long> expectedSequenceIds;
    private final Handler handler;

    public AbstractMonitorUsage() {
        handler = new Handler();
        expectedSequenceIds = new HashSet<>();
    }

    public void monitorNewRecord(Activity activity, long currentSequenceId, String value) {
        if (isValidSequenceId(currentSequenceId)) {
            LogUtility.log("Received valid sequence id, clearing set and starting actual process");
            expectedSequenceIds.clear();
            processValueView(activity, value);
        } else {
            LogUtility.log("Received in-valid sequence id: " + currentSequenceId);
        }
    }

    public void sendNewReadingRequest() {
        long currentNanoTime = System.nanoTime();
        addExpectedSequenceID(currentNanoTime + 1);
        ThreadsUtilty.getExecutorService().execute(() -> WebSocketManager.INSTANCE.sendText(getJSONMessage(currentNanoTime)));
    }

    public void addExpectedSequenceID(long expectedSequenceId) {
        LogUtility.log("added " + expectedSequenceId + " as expected sequence value in set");
        expectedSequenceIds.add(expectedSequenceId);
    }

    private String getJSONMessage(long currentNanoTime) {
        return new Gson().toJson(UsageModel.builder().type(getMessageType()).sequenceId(currentNanoTime));
    }

    private void processValueView(Activity activity, String value) {
        View container = activity.findViewById(R.id.monitor_usage_container);
        if (container.getVisibility() == View.GONE) {
            AnimationUtils.fadeIn(container);
            startMonitoringJob(activity, value);
            LogUtility.log("Container is not visible, fading it in, and started monitor job.");
        }
    }

    private boolean isValidSequenceId(long currentSequenceId) {
        return expectedSequenceIds.contains(currentSequenceId);
    }

    private void startMonitoringJob(Activity activity, String value) {
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                TextView textView = activity.findViewById(R.id.usage_text_view);
                textView.setText(getUsageLabel() + ": " + value);
                sendNewReadingRequest();
                LogUtility.log("Displayed new usage value and asking next value from server in: " + DELAY_AMOUNT + "ms");
                handler.postDelayed(this, DELAY_AMOUNT);
            }
        }, DELAY_AMOUNT);
    }

    protected abstract String getUsageLabel();

    public void stopMonitoring() {
        LogUtility.log("stopping handler of monitoring");
        handler.removeCallbacksAndMessages(null);
        expectedSequenceIds.clear();
    }

    protected abstract String getMessageType();
}
