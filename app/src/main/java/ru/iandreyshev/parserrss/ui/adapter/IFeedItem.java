package ru.iandreyshev.parserrss.ui.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import ru.iandreyshev.parserrss.models.rss.IViewArticle;

public interface IFeedItem {
    void updateImage(@NonNull Bitmap bitmap);

    boolean isImageLoaded();

    IViewArticle getArticle();
}
