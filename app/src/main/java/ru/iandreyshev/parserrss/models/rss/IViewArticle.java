package ru.iandreyshev.parserrss.models.rss;

import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

public interface IViewArticle {
    long getId();

    @NonNull
    String getTitle();

    String getOriginUrl();

    String getDescription();

    Long getPostDate();

    Bitmap getImage();

    String getImageUrl();
}
