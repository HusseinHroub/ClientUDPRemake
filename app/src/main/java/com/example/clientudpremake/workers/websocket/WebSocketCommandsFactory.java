package com.example.clientudpremake.workers.websocket;

import android.content.Context;
import android.view.View;

import com.example.clientudpremake.R;
import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.commands.DummyCommand;
import com.example.clientudpremake.commands.ToastCommand;
import com.example.clientudpremake.commands.senders.WebSocketSenderButton;

import org.json.JSONException;
import org.json.JSONObject;

public class WebSocketCommandsFactory {

    private static final String TURN_OFF_MONITOR_MESSAGE = "turnOffMonitor";
    private static final String TURN_ON_MONITOR_MESSAGE = "turnOnMonitor";
    private static final String IS_SERVER_ON = "isServerOn";
    private static final String RESTART_MESSAGE = "restart";
    private static final String TYPE = "type";


    public static Command getSenderCommand(View button) {
        switch (button.getId()) {
            case R.id.turnOnMButton:
                return new WebSocketSenderButton(TURN_ON_MONITOR_MESSAGE);
            case R.id.turnOffMButton:
                return new WebSocketSenderButton(TURN_OFF_MONITOR_MESSAGE);
            case R.id.shutButton:
                return new WebSocketSenderButton(IS_SERVER_ON);
            case R.id.restartButton:
                return new WebSocketSenderButton(RESTART_MESSAGE);
            default:
                return new DummyCommand();
        }
    }

    public static Command getReceiverCommand(Context context, JSONObject jsonObject) {
        String message = getMessage(jsonObject);
        switch (message) {
            case TURN_OFF_MONITOR_MESSAGE:
                return new ToastCommand(context, "Monitor is off");
            case TURN_ON_MONITOR_MESSAGE:
                return new ToastCommand(context, "Monitor is on");
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
