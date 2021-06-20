package com.example.clientudpremake.models;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileTransferModel {
    final String type = "fileTransfer";
    final String data;
    final String fileName;
    final long frame;
    final long latestFrame;
    final long id;

}
