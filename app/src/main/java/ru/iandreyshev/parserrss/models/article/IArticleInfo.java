package ru.iandreyshev.parserrss.models.article;

import android.graphics.Bitmap;

import java.util.Date;

public interface IArticleInfo {
    int getId();
    Date getDate();
    String getTitle();
    String getText();
    Bitmap getImage();
    boolean isDateExist();
    boolean isImageExist();
}
