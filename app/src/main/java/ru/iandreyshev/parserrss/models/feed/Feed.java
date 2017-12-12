package ru.iandreyshev.parserrss.models.feed;

import android.support.annotation.NonNull;

import okhttp3.HttpUrl;

public class Feed implements IFeedInfo {
    private int mId;
    private String mTitle;
    private String mDescription;
    private HttpUrl mUrl;
    private HttpUrl mOriginUrl;

    public Feed(@NonNull final String title, @NonNull final String description) {
        setTitle(title);
        setDescription(description);
    }

    @Override
    public int getId() {
        return mId;
    }

    public void setId(int value) {
        mId = value;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(@NonNull final String value) {
        mTitle = value;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(@NonNull final String description) {
        mDescription = description;
    }

    @Override
    public HttpUrl getOriginUrl() {
        return mOriginUrl;
    }

    public void setOriginUrl(@NonNull final HttpUrl originUrl) {
        mOriginUrl = originUrl;
    }

    public HttpUrl getUrl() {
        return mUrl;
    }

    public void setUrl(@NonNull final HttpUrl value) {
        mUrl = value;
    }
}
