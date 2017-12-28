package ru.iandreyshev.parserrss.app;

import android.support.annotation.NonNull;

public interface IBuilder<T> {
    @NonNull
    T build();
}
