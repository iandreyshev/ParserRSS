package ru.iandreyshev.parserrss.models.async;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class TaskExecutor {
    private static final int THREADS_COUNT = 25;

    private static Executor mInstance = Executors.newFixedThreadPool(THREADS_COUNT);

    static Executor getInstance() {
        return mInstance;
    }

    private TaskExecutor() {
    }
}
