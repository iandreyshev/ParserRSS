package ru.iandreyshev.parserrss.models.rss;

import android.graphics.Bitmap;

import java.io.Serializable;

public interface IRssArticle extends Serializable {
    String getTitle();

    String getOrigin();

    String getDescription();

    Bitmap getImage();
}
