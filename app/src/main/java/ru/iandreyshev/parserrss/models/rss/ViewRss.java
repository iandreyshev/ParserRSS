package ru.iandreyshev.parserrss.models.rss;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class ViewRss implements Serializable {
    public abstract long getId();

    public abstract String getTitle();

    public abstract String getDescription();

    public abstract String getUrl();

    public abstract String getOrigin();

    public abstract ArrayList<ViewRssArticle> getArticlesViewInfo();

    @Override
    public final boolean equals(Object other) {
        return (other instanceof ViewRss) && getUrl().equals(((ViewRss) other).getUrl());
    }
}
