package ru.iandreyshev.parserrss.models.rss;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public final class RssArticle implements Serializable {
    private String mTitle;
    private String mOrigin;
    private String mDescription;
    private Bitmap mImage;
    private String mImageUrl;
    private Date mDate;

    RssArticle(final String title, final String origin) {
        mTitle = title;
        mOrigin = origin;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public String getDescription() {
        return mDescription;
    }

    public Date getDate() {
        return mDate;
    }

    public Bitmap getImage() {
        return mImage;
    }

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
