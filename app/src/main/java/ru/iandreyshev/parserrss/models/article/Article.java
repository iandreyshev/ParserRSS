package ru.iandreyshev.parserrss.models.article;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.Date;

public class Article implements IArticleContent {
    private String mTitle;
    private String mOrigin;
    private String mText;
    private Bitmap mImage;
    private Date mDate;

    public Article(String title, String origin) {
        setTitle(title);
        mOrigin = origin;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(@NonNull String title) {
        mTitle = title;
    }

    @Override
    public String getOrigin() {
        return mOrigin;
    }

    @Override
    public String getText() {
        return mText;
    }

    public void setText(@NonNull String text) {
        mText = text;
    }

    @Override
    public Date getDate() {
        return mDate;
    }

    public void setDate(@NonNull Date date) {
        mDate = date;
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
