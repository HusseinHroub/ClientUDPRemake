package com.example.clientudpremake.workers.websocket;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.clientudpremake.R;
import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.commands.DummyCommand;
import com.example.clientudpremake.commands.ToastCommand;
import com.example.clientudpremake.commands.receivers.MonitorCPUUsageCommand;
import com.example.clientudpremake.commands.receivers.ScreenShotCommand;
import com.example.clientudpremake.commands.senders.WebSocketSenderButton;
import com.example.clientudpremake.utilites.LogUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WebSocketCommandsFactory {

    public static final String TURN_OFF_MONITOR_MESSAGE = "turnOffMonitor";
    public static final String TURN_ON_MONITOR_MESSAGE = "turnOnMonitor";
    public static final String IS_SERVER_ON = "isServerOn";
    public static final String CAPTURE_SCREEN_SHOT = "captureDesktopScreenshot";
    public static final String RESTART_MESSAGE = "restart";
    public static final String TYPE = "type";
    public static final String BINARY_IMAGE = "binaryImage";
    public static final String GET_CPU_USAGE = "getCpuUsage";

    public static final Map<Integer, Command> sendCommands = new HashMap<>();
    public static MonitorCPUUsageCommand monitorCPUUsageCommand = new MonitorCPUUsageCommand();

    static {
        sendCommands.put(R.id.turnOnMButton, new WebSocketSenderButton(TURN_ON_MONITOR_MESSAGE));
        sendCommands.put(R.id.turnOffMButton, new WebSocketSenderButton(TURN_OFF_MONITOR_MESSAGE));
        sendCommands.put(R.id.shutButton, new WebSocketSenderButton(IS_SERVER_ON));
        sendCommands.put(R.id.restartButton, new WebSocketSenderButton(RESTART_MESSAGE));
        sendCommands.put(R.id.imageButton, new WebSocketSenderButton(CAPTURE_SCREEN_SHOT));
        sendCommands.put(R.id.monitor_cpu_usage, new WebSocketSenderButton(GET_CPU_USAGE));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Command getSenderCommand(int buttonId) {
        return sendCommands.getOrDefault(buttonId, new DummyCommand());
    }

    public static Command getReceiverCommand(Activity activity, JSONObject jsonObject) {
        String message = getMessage(jsonObject);
        switch (message) {
            case TURN_OFF_MONITOR_MESSAGE:
                return new ToastCommand(activity, "Monitor is off");
            case TURN_ON_MONITOR_MESSAGE:
                return new ToastCommand(activity, "Monitor is on");
            case CAPTURE_SCREEN_SHOT:
                try {
                    return new ScreenShotCommand(activity, jsonObject.getString(BINARY_IMAGE));
                } catch (JSONException e) {
                    LogUtility.log("Exception occured while intlizing ScreenShotCOmmand");
                    e.printStackTrace();
                }
            case GET_CPU_USAGE: {
                try {
                    monitorCPUUsageCommand.setActivity(activity);
                    monitorCPUUsageCommand.setValue(jsonObject.getString("value"));
                    return monitorCPUUsageCommand;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            default:
                return new DummyCommand();
        }
    }

    private static String getMessage(JSONObject jsonObject) {
        String message = null;
        try {
            message = jsonObject.getString(TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }


}
