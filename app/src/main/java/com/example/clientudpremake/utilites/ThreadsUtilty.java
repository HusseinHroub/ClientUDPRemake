package com.example.clientudpremake.utilites;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadsUtilty {
    private static ExecutorService executorService;
    private static final int NUMBER_OF_THREADS = 5;

    private ThreadsUtilty() {

    }

    public static ExecutorService getExecutorService() {
        if (executorService == null)
            executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        return executorService;
    }
}
