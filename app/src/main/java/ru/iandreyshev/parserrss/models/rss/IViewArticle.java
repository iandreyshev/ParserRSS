package ru.iandreyshev.parserrss.models.rss;

import android.support.annotation.NonNull;

public interface IViewArticle {
    long getId();

    @NonNull
    String getTitle();

    @NonNull
    String getOriginUrl();

    @NonNull
    String getDescription();

    Long getDate();

    byte[] getImage();

    String getImageUrl();

    void setImage(byte[] image);
}
