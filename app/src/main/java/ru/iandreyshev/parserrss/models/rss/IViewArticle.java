package ru.iandreyshev.parserrss.models.rss;

import android.support.annotation.NonNull;

public interface IViewArticle {
    long getId();

    @NonNull
    String getTitle();

    String getOriginUrl();

    String getDescription();

    Long getPostDate();

    byte[] getImage();

    String getImageUrl();

    void setImage(byte[] image);
}
