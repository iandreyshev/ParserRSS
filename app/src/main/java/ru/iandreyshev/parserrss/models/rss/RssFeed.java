package ru.iandreyshev.parserrss.models.rss;

import android.support.annotation.NonNull;

import ru.iandreyshev.parserrss.models.web.Url;

class RssFeed implements IRssFeed {
    private static int DEFAULT_HASH = 11;

    private Url mUrl;
    private String mOrigin;
    private String mTitle;
    private String mDescription;
    private int mHash;

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
        initHash();
    }

    @Override
    public int hashCode() {
        return mHash;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof RssFeed) && (this.hashCode() == obj.hashCode());
    }

    private void initHash() {
        final String urlStr = mUrl == null ? "" : mUrl.toString();
        mHash = DEFAULT_HASH;

        for (int i = 0; i < urlStr.length(); i++) {
            mHash = mHash * 31 + urlStr.charAt(i);
        }
    }
}
