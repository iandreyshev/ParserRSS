package ru.iandreyshev.parserrss.models.web;

import okhttp3.HttpUrl;

public class Url {
    private HttpUrl mInstance;

    public static Url parse(final String url) {
        HttpUrl instance = HttpUrl.parse(url);

        if (instance == null) {
            return null;
        }

        return new Url(instance);
    }

    @Override
    public String toString() {
        return mInstance.toString();
    }

    HttpUrl getInstance() {
        return mInstance;
    }

    private Url(final HttpUrl url) {
        mInstance = url;
    }
}
