package com.example.clientudpremake.workers.websocket;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.clientudpremake.R;
import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.commands.DummyCommand;
import com.example.clientudpremake.commands.ToastCommand;
import com.example.clientudpremake.commands.senders.ConfirmationDialogSendCommand;
import com.example.clientudpremake.commands.senders.LoadingDialogSendCommand;
import com.example.clientudpremake.commands.senders.WebSocketSenderButton;
import com.example.clientudpremake.models.StandardModel;
import com.example.clientudpremake.utilites.LogUtility;
import com.example.clientudpremake.workers.websocket.monitors.MonitorUsages;

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

    public static final MonitorUsages monitorUsages = new MonitorUsages();
    public static final Map<Integer, Command> sendCommands = new HashMap<>();

    static {
        sendCommands.put(R.id.turnOnMButton, new WebSocketSenderButton(StandardModel.builder().type(TURN_ON_MONITOR_MESSAGE).build()));
        sendCommands.put(R.id.turnOffMButton, new WebSocketSenderButton(StandardModel.builder().type(TURN_OFF_MONITOR_MESSAGE).build()));
        sendCommands.put(R.id.shutButton, new ConfirmationDialogSendCommand(new WebSocketSenderButton(StandardModel.builder().type(IS_SERVER_ON).build())));
        sendCommands.put(R.id.restartButton, new ConfirmationDialogSendCommand(new WebSocketSenderButton(StandardModel.builder().type(RESTART_MESSAGE).build())));
        sendCommands.put(R.id.imageButton, new LoadingDialogSendCommand(new WebSocketSenderButton(StandardModel.builder().type(CAPTURE_SCREEN_SHOT).build())));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Command getSenderCommand(int buttonId) {
        return sendCommands.getOrDefault(buttonId, new DummyCommand());
    }

    public static Command getReceiverCommand(JSONObject jsonObject) {
        LogUtility.log("received message commmand from websocket: " + jsonObject.toString());
        String messageType = getMessageType(jsonObject);
        switch (messageType) {
            case TURN_OFF_MONITOR_MESSAGE:
                return new ToastCommand("Monitor is off");
            case TURN_ON_MONITOR_MESSAGE:
                return new ToastCommand("Monitor is on");
            case MonitorUsages.GET_CPU_USAGE:
            case MonitorUsages.GET_MEMORY_USAGE:
                try {
                    return monitorUsages.getMonitorCommandFromReceiveEvent(messageType,
                            jsonObject.getString("value"),
                            jsonObject.getLong("sequenceId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            default:
                return new DummyCommand();
        }

    }

    private static String getMessageType(JSONObject jsonObject) {
        String message = null;
        try {
            message = jsonObject.getString(TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }


}
