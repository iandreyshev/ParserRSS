package ru.iandreyshev.parserrss.models.article;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.Date;

import okhttp3.HttpUrl;

public class Article implements IArticleInfo {
    private String mTitle;
    private String mUrl;
    private String mText;
    private Bitmap mImage;
    private Date mDate;

    public Article(String title, HttpUrl link) {
        setTitle(title);
        setUrl(link);
    }

    @Override
    public Date getDate() {
        return mDate;
    }

    public void setDate(@NonNull Date date) {
        mDate = date;
    }

    @Override
    public String getText() {
        return mText;
    }

    public void setText(@NonNull String text) {
        mText = text;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(@NonNull String title) {
        mTitle = title;
    }

    @Override
    public HttpUrl getUrl() {
        return HttpUrl.parse(mUrl);
    }

    public void setUrl(@NonNull final HttpUrl originUrl) {
        mUrl = originUrl.toString();
    }

    @Override
    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(@NonNull Bitmap image) {
        mImage = image;
    }

    @Override
    public boolean isDateExist() {
        return mDate != null;
    }

    @Override
    public boolean isImageExist() {
        return mImage != null;
    }
}
