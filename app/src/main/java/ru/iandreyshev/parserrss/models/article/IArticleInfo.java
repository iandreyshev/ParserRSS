package ru.iandreyshev.parserrss.models.article;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

import okhttp3.HttpUrl;

public interface IArticleInfo extends Serializable {
    int getId();

    String getTitle();

    String getText();

    HttpUrl getOriginUrl();

    Date getDate();

    Bitmap getImage();

    boolean isDateExist();

    boolean isImageExist();
}
