package ru.iandreyshev.parserrss.models.rss;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public abstract class ViewRss implements Serializable {
    public abstract long getId();

    public abstract String getTitle();

    public abstract String getDescription();

    public abstract String getUrl();

    public abstract String getOrigin();

    @NonNull
    public abstract List<ViewRssArticle> getViewArticles();

    @Override
    public final boolean equals(Object other) {
        return (other instanceof ViewRss) && getUrl().equals(((ViewRss) other).getUrl());
    }

    @Override
    public final int hashCode() {
        return getUrl().hashCode();
    }
}
