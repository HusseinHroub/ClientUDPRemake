package com.example.clientudpremake.utilites;

public class LogUtility {
    private static final boolean isLogEnabled = true;

    public static void log(String logMessage) {
        if (isLogEnabled) {
            System.out.println("[MY_LOG]: " + logMessage);
        }
    }
}
