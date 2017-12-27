package ru.iandreyshev.parserrss.models.rss;

import android.graphics.Bitmap;
import android.os.Parcelable;

import java.io.Serializable;

public abstract class ViewRssArticle implements Parcelable, Serializable {
    public abstract long getId();

    public abstract String getTitle();

    public abstract String getOriginUrl();

    public abstract String getDescription();

    public abstract Long getPostDate();

    public abstract Bitmap getImage();

    public abstract String getImageUrl();
}
