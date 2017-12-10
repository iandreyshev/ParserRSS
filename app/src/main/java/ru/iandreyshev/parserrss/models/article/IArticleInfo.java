package ru.iandreyshev.parserrss.models.article;

import android.graphics.Bitmap;

import java.io.Closeable;
import java.io.Serializable;
import java.util.Date;

public interface IArticleInfo extends Serializable {
    int getId();

    Date getDate();

    String getTitle();

    String getText();

    Bitmap getImage();

    boolean isDateExist();

    boolean isImageExist();
}
