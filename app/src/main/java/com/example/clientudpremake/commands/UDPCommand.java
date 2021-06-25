package com.example.clientudpremake.commands;

import android.app.Activity;

import com.example.clientudpremake.workers.UDPMessage;

public interface UDPCommand {
    void apply(Activity activity, UDPMessage udpMessage);
}
