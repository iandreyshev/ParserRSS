package ru.iandreyshev.parserrss.models.rss;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.Date;

class RssArticle implements IRssArticle {
    private String mTitle;
    private String mOrigin;
    private String mDescription;
    private Bitmap mImage;
    private String mImageUrl;
    private Date mDate;

    RssArticle(@NonNull final String title, @NonNull final String origin) {
        setTitle(title);
        mOrigin = origin;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getOrigin() {
        return mOrigin;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public Date getDate() {
        return mDate;
    }

    @Override
    public Bitmap getImage() {
        return mImage;
    }

    @Override
    public String getImageUrl() {
        return mImageUrl;
    }

    void setTitle(String title) {
        mTitle = title;
    }

    void setDescription(String text) {
        mDescription = text;
    }

    void setImage(Bitmap image) {
        mImage = image;
    }

    void setDate(Date date) {
        mDate = date;
    }

    void setImageUrl(String url) {
        mImageUrl = url;
    }
}
