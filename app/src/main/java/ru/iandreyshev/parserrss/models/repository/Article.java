package ru.iandreyshev.parserrss.models.repository;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.Date;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public final class Article {
    @Id
    long mId;
    long mRssId;

    String mTitle = "";
    String mOriginUrl = "";
    String mDescription = "";
    String mImageUrl;
    Long mPostDate;
    @Convert(dbType = byte[].class, converter = BitmapConverter.class)
    Bitmap mImage;

    public Article(@NonNull String title, @NonNull String description, @NonNull String originUrl) {
        mTitle = title;
        mDescription = description;
        mOriginUrl = originUrl;
    }

    Article() {
    }

    public long getId() {
        return mId;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    @NonNull
    public String getOriginUrl() {
        return mOriginUrl;
    }

    @NonNull
    public String getDescription() {
        return mDescription;
    }

    public Long getDate() {
        return mPostDate;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public Long getRssId() {
        return mRssId;
    }

    void setRssId(long id) {
        mRssId = id;
    }

    public void setImage(final Bitmap image) {
        mImage = image;
    }

    public void setDate(final Date date) {
        mPostDate = date.getTime();
    }

    public void setImageUrl(final String url) {
        mImageUrl = url;
    }

    @Override
    public final boolean equals(Object other) {
        return (other instanceof Article) && mOriginUrl.equals(((Article) other).mOriginUrl);
    }

    @Override
    public final int hashCode() {
        return mOriginUrl.hashCode();
    }
}
