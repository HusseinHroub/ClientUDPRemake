package com.example.clientudpremake.models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CPUUsageModel {
    private final String type;
    private final long sequenceId;
}
