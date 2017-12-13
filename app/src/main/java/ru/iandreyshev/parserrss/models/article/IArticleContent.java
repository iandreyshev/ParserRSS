package ru.iandreyshev.parserrss.models.article;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

public interface IArticleContent extends Serializable {
    String getTitle();

    String getText();

    String getOrigin();

    Date getDate();

    Bitmap getImage();

    boolean isDateExist();

    boolean isImageExist();
}
