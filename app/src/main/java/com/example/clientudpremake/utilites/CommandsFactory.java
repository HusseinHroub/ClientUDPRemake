package com.example.clientudpremake.utilites;

import android.app.Activity;
import android.view.View;

import com.example.clientudpremake.R;
import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.commands.ServerOnReceiveCommand;
import com.example.clientudpremake.commands.ServerReceiverButton;
import com.example.clientudpremake.commands.ServerSenderButton;

public class CommandsFactory {

    private static final String TURN_OFF_MONITOR_SEND_MESSAGE = "turnOffMonitor";
    private static final String TURN_ON_MONITOR_SEND_MESSAGE = "turnOnMonitor";
    private static final String SHUT_DOWN_SEND_MESSAGE = "shutDown";
    private static final String RESTART_SEND_MESSAGE = "restart";

    private static final String TURN_OFF_MONITOR_RECEIVE_MESSAGE = "monitorIsOff";
    private static final String TURN_ON_MONITOR_RECEIVE_MESSAGE = "monitorIsOn";
    private static final String SERVER_ON_RECEIVE_MESSAGE = "serverIsOn";

    public static Command getSenderCommand(View button) {
        switch (button.getId()) {
            case R.id.turnOffMButton:
                return new ServerSenderButton(button, TURN_ON_MONITOR_SEND_MESSAGE);
            case R.id.turnOnMButton:
                return new ServerSenderButton(button, TURN_OFF_MONITOR_SEND_MESSAGE);
            case R.id.shutButton:
                return new ServerSenderButton(button, SHUT_DOWN_SEND_MESSAGE);
            case R.id.restartButton:
                return new ServerSenderButton(button, RESTART_SEND_MESSAGE);
            default:
                throw new RuntimeException("No command found for button: " + button.getId());
        }
    }

    public static Command getReceiverCommand(String message, Activity activity) {
        switch (message) {
            case TURN_OFF_MONITOR_RECEIVE_MESSAGE:
                return new ServerReceiverButton(activity.findViewById(R.id.turnOffMButton), message);
            case TURN_ON_MONITOR_RECEIVE_MESSAGE:
                return new ServerReceiverButton(activity.findViewById(R.id.turnOnMButton), message);
            case SERVER_ON_RECEIVE_MESSAGE:
                return new ServerOnReceiveCommand(getActivityButtons(activity));
            default:
                throw new RuntimeException("No command found for receiving");
        }
    }

    private static View[] getActivityButtons(Activity activity) {
        return new View[]{activity.findViewById(R.id.turnOnMButton), activity.findViewById(R.id.turnOffMButton), activity.findViewById(R.id.shutButton), activity.findViewById(R.id.restartButton)};
    }
}