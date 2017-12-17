package ru.iandreyshev.parserrss.models.rss;

import android.support.annotation.NonNull;

import ru.iandreyshev.parserrss.models.web.Url;

class RssFeed implements IRssFeed {
    private Url mUrl;
    private String mOrigin;
    private String mTitle;
    private String mDescription;

    RssFeed(@NonNull final String title, @NonNull final String origin) throws NullPointerException {
        mTitle = title;
        mOrigin = origin;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public String getOrigin() {
        return mOrigin;
    }

    @Override
    public Url getUrl() {
        return mUrl;
    }

    public void setDescription(final String description) {
        mDescription = description;
    }

    public void setUrl(final Url url) throws NullPointerException {
        mUrl = url;
    }
}
