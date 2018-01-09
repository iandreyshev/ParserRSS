package ru.iandreyshev.parserrss.models.async;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

final class TaskExecutor {
    private static final int THREADS_COUNT = 25;

    private static Executor mMultiThreadInstance = Executors.newFixedThreadPool(THREADS_COUNT);

    static Executor getMultiThreadPool() {
        return mMultiThreadInstance;
    }

    private TaskExecutor() {
    }
}
