package ru.iandreyshev.parserrss.models.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import okhttp3.HttpUrl;

public class Url {
    private final HttpUrl mInstance;

    @Nullable
    public static Url parse(final String url) {
        if (url == null) {
            return null;
        }

        final HttpUrl instance = HttpUrl.parse(url);

        if (instance == null) {
            return null;
        }

        return new Url(instance);
    }

    @Override
    public String toString() {
        return mInstance.toString();
    }

    private Url(final HttpUrl url) {
        mInstance = url;
    }

    @NonNull
    HttpUrl getInstance() {
        return mInstance;
    }
}
