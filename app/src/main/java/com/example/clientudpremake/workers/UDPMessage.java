package com.example.clientudpremake.workers;

import java.net.InetAddress;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UDPMessage {
    private final String content;
    private final InetAddress senderAddress;
}
