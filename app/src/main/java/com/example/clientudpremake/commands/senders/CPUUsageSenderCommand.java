package com.example.clientudpremake.commands.senders;

import android.app.Activity;

import com.example.clientudpremake.commands.Command;
import com.example.clientudpremake.models.CPUUsageModel;
import com.example.clientudpremake.utilites.ThreadsUtilty;
import com.example.clientudpremake.workers.websocket.WebSocketCommandsFactory;
import com.example.clientudpremake.workers.websocket.WebSocketManager;
import com.google.gson.Gson;

import static com.example.clientudpremake.workers.websocket.WebSocketCommandsFactory.GET_CPU_USAGE;

public class CPUUsageSenderCommand implements Command {
    @Override
    public void apply(Activity activity) {
        long currentNanoTime = System.nanoTime();
        WebSocketCommandsFactory.monitorCPUUsageCommand.addExpectedSequenceID(currentNanoTime + 1);
        ThreadsUtilty.getExecutorService().execute(() -> WebSocketManager.INSTANCE.sendText(getJSONMessage(currentNanoTime)));
    }

    private String getJSONMessage(long currentNanoTime) {
        return new Gson().toJson(CPUUsageModel.builder().type(GET_CPU_USAGE).sequenceId(currentNanoTime));
    }
}
