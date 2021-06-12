package com.example.clientudpremake.utilites;

import android.app.Activity;
import android.view.View;

import com.example.clientudpremake.R;
import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.commands.receivers.ServerOffReceiveCommand;
import com.example.clientudpremake.commands.receivers.ServerOnReceiveCommand;
import com.example.clientudpremake.commands.receivers.ServerReceiverButton;
import com.example.clientudpremake.commands.senders.ServerSenderButton;

public class CommandsFactory {

    private static final String TURN_OFF_MONITOR_MESSAGE = "turnOffMonitor";
    private static final String TURN_ON_MONITOR_MESSAGE = "turnOnMonitor";
    private static final String IS_SERVER_ON = "isServerOn";
    private static final String RESTART_MESSAGE = "restart";


    public static Command getSenderCommand(View button) {
        switch (button.getId()) {
            case R.id.turnOnMButton:
                return new ServerSenderButton(button, TURN_ON_MONITOR_MESSAGE);
            case R.id.turnOffMButton:
                return new ServerSenderButton(button, TURN_OFF_MONITOR_MESSAGE);
            case R.id.shutButton:
                return new ServerSenderButton(button, IS_SERVER_ON);
            case R.id.restartButton:
                return new ServerSenderButton(button, RESTART_MESSAGE);
            default:
                throw new RuntimeException("No command found for button: " + button.getId());
        }
    }

    public static Command getReceiverCommand(String message, Activity activity) {
        switch (message) {
            case TURN_OFF_MONITOR_MESSAGE:
                return new ServerReceiverButton(activity.findViewById(R.id.turnOffMButton), message);
            case TURN_ON_MONITOR_MESSAGE:
                return new ServerReceiverButton(activity.findViewById(R.id.turnOnMButton), message);
            case IS_SERVER_ON:
                return new ServerOnReceiveCommand(getActivityButtons(activity));
            case RESTART_MESSAGE:
                return new ServerOffReceiveCommand(getActivityButtons(activity));
            default:
                throw new RuntimeException("No command found for receiving");
        }
    }

    private static View[] getActivityButtons(Activity activity) {
        return new View[]{activity.findViewById(R.id.turnOnMButton),
                activity.findViewById(R.id.turnOffMButton),
                activity.findViewById(R.id.shutButton),
                activity.findViewById(R.id.restartButton)};
    }
}
