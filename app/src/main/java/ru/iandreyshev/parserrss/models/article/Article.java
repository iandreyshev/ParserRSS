package ru.iandreyshev.parserrss.models.article;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Date;

public class Article implements IArticleInfo {
    private int mId;
    private Date mDate;
    private String mTitle;
    private String mText;
    private Bitmap mImage;

    public Article(int id, @NonNull String title, @NonNull String text) {
        setId(id);
        setTitle(title);
        setText(text);
    }

    @Override
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
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
