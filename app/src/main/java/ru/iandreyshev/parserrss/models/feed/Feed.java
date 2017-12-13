package ru.iandreyshev.parserrss.models.feed;

import okhttp3.HttpUrl;

public class Feed implements IFeedInfo {
    private String mTitle;
    private String mOriginUrl;
    private String mDescription;
    private String mUrl;

    public Feed(final String title, final HttpUrl originUrl) throws NullPointerException {
        setTitle(title);
        mOriginUrl = originUrl.toString();
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(final String value) {
        mTitle = value;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(final String description) {
        mDescription = description;
    }

    @Override
    public HttpUrl getOriginUrl() {
        return HttpUrl.parse(mOriginUrl);
    }

    public HttpUrl getUrl() {
        return HttpUrl.parse(mUrl);
    }

    public void setUrl(final HttpUrl url) throws NullPointerException {
        mUrl = url.toString();
    }
}
