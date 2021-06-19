package com.example.clientudpremake.monitors;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
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

    private String value;

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
        this.value = value;
        View container = activity.findViewById(R.id.monitor_usage_container);
        if (container.getVisibility() == View.GONE) {
            setValueView(activity);
            activity.findViewById(R.id.loading_dialog).setVisibility(View.GONE);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            AnimationUtils.fadeIn(container);
            startMonitoringJob(activity);
            LogUtility.log("Container is not visible, fading it in, and started monitor job.");
        }
    }

    private boolean isValidSequenceId(long currentSequenceId) {
        return expectedSequenceIds.contains(currentSequenceId);
    }

    private void startMonitoringJob(Activity activity) {
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                setValueView(activity);
                sendNewReadingRequest();
                LogUtility.log("Displayed new usage value and asking next value from server in: " + DELAY_AMOUNT + "ms");
                handler.postDelayed(this, DELAY_AMOUNT);
            }
        }, DELAY_AMOUNT);
    }

    private void setValueView(Activity activity) {
        TextView textView = activity.findViewById(R.id.usage_text_view);
        textView.setText(getUsageLabel() + ": " + value);
    }

    protected abstract String getUsageLabel();

    public void stopMonitoring(Activity activity) {
        LogUtility.log("stopping handler of monitoring [" + getClass().getSimpleName() + "]");
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        handler.removeCallbacksAndMessages(null);
        expectedSequenceIds.clear();
    }

    protected abstract String getMessageType();
}
