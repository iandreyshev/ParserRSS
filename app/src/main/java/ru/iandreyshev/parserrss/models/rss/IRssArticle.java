package ru.iandreyshev.parserrss.models.rss;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

public interface IRssArticle extends Serializable {
    String getTitle();

    String getOrigin();

    String getDescription();

    Date getDate();

    Bitmap getImage();

    String getImageUrl();
}
