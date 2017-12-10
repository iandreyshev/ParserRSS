package ru.iandreyshev.parserrss.models.feed;

import android.support.annotation.NonNull;

import okhttp3.HttpUrl;

public class Feed implements IFeedInfo {
    private int mId;
    private String mName;
    private HttpUrl mUrl;

    public Feed(int id, @NonNull String name, @NonNull HttpUrl url) {
        setId(id);
        setName(name);
        setUrl(url);
    }

    @Override
    public int getId() {
        return mId;
    }

    public void setId(int value) {
        mId = value;
    }

    @Override
    public String getName() {
        return mName;
    }

    public void setName(@NonNull String value) {
        mName = value;
    }

    public HttpUrl getUrl() {
        return mUrl;
    }

    public void setUrl(@NonNull HttpUrl value) {
        mUrl = value;
    }
}
