package ru.iandreyshev.parserrss.models.article;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

import okhttp3.HttpUrl;

public interface IArticleInfo extends Serializable {
    String getTitle();

    String getText();

    HttpUrl getUrl();

    Date getDate();

    Bitmap getImage();

    boolean isDateExist();

    boolean isImageExist();
}
