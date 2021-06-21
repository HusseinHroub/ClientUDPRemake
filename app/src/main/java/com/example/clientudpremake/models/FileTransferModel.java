package com.example.clientudpremake.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FileTransferModel {
    final String type = "fileTransfer";
    final String fileName;
    final long frame;
    final long latestFrame;
    final long id;

}
