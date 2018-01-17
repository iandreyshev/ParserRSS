package ru.iandreyshev.parserrss.models.repository;

import android.support.annotation.NonNull;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;

@Entity
public final class Article implements IViewArticle {
    @Id
    long mId;
    long mRssId;

    String mTitle = "";
    String mOriginUrl = "";
    String mDescription = "";
    String mImageUrl;
    Long mPostDate;
    byte[] mImage;

    public Article(@NonNull String title, @NonNull String description, @NonNull String originUrl) {
        mTitle = title;
        mDescription = description;
        mOriginUrl = originUrl;
    }

    Article() {
    }

    @Override
    public long getId() {
        return mId;
    }

    @NonNull
    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    @NonNull
    public String getOriginUrl() {
        return mOriginUrl;
    }

    @Override
    @NonNull
    public String getDescription() {
        return mDescription;
    }

    @Override
    public Long getDate() {
        return mPostDate;
    }

    @Override
    public byte[] getImage() {
        return mImage;
    }

    @Override
    public String getImageUrl() {
        return mImageUrl;
    }

    public Long getRssId() {
        return mRssId;
    }

    void setRssId(long id) {
        mRssId = id;
    }

    @Override
    public void setImage(final byte[] image) {
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
