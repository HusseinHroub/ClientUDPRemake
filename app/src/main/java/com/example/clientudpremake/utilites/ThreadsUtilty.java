package com.example.clientudpremake.utilites;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadsUtilty {
    private static ExecutorService executorService;
    private static final int NUMBER_OF_THREADS = 5;

    private ThreadsUtilty() {

    }

    synchronized public static ExecutorService getExecutorService() {
        if (executorService == null) {
            LogUtility.log("Executor service is null, initializing new one");
            executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        }
        return executorService;
    }
}
