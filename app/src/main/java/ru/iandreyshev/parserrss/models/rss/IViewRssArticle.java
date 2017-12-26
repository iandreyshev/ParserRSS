package ru.iandreyshev.parserrss.models.rss;

import android.graphics.Bitmap;
import android.os.Parcelable;

import java.io.Serializable;

public interface IViewRssArticle extends Parcelable, Serializable {
    long getId();

    String getTitle();

    String getOriginUrl();

    String getDescription();

    Long getPostDate();

    Bitmap getImage();

    String getImageUrl();
}
