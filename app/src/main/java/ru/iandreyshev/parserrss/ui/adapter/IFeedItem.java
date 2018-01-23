package ru.iandreyshev.parserrss.ui.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

public interface IFeedItem {
    void updateImage(@NonNull Bitmap bitmap);

    boolean isImageLoaded();

    long getId();
}
