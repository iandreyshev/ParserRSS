package ru.iandreyshev.parserrss.models.feed;

import ru.iandreyshev.parserrss.models.web.Url;

public class Feed implements IFeedContent {
    private String mTitle;
    private String mOrigin;
    private String mDescription;
    private String mUrl;

    public Feed(final String title, final String origin) throws NullPointerException {
        mTitle = title;
        mOrigin = origin;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(final String description) {
        mDescription = description;
    }

    @Override
    public String getOrigin() {
        return mOrigin;
    }

    public Url getUrl() {
        return Url.parse(mUrl);
    }

    public void setUrl(final Url url) throws NullPointerException {
        mUrl = url.toString();
    }
}
