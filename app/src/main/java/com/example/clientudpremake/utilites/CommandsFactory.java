package com.example.clientudpremake.utilites;

import android.view.View;

import com.example.clientudpremake.R;
import com.example.clientudpremake.commands.Sender;
import com.example.clientudpremake.commands.SenderReceiverButton;

public class CommandsFactory {

    private static final String TURN_OFF_MONITOR_MESSAGE = "turnOffMonitor";
    private static final String TURN_ON_MONITOR_MESSAGE = "turnOnMonitor";
    private static final String SHUT_DOWN_MESSAGE = "shutDown";
    private static final String RESTART_MESSAGE = "restart";

    public static Sender getSenderCommand(View button) {
        switch (button.getId()) {
            case R.id.turnOffMButton:
                return new SenderReceiverButton(button, TURN_ON_MONITOR_MESSAGE);
            case R.id.turnOnMButton:
                return new SenderReceiverButton(button, TURN_OFF_MONITOR_MESSAGE);
            case R.id.shutButton:
                return new SenderReceiverButton(button, SHUT_DOWN_MESSAGE);
            case R.id.restartButton:
                return new SenderReceiverButton(button, RESTART_MESSAGE);
            default:
                throw new RuntimeException("No command found for button: " + button.getId());
        }
    }
}
