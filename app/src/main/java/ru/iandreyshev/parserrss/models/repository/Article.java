package ru.iandreyshev.parserrss.models.repository;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.support.annotation.NonNull;

import java.util.Date;

import javax.annotation.Nullable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;

@Entity
public final class Article implements IViewArticle {
    private static final Long NULL_DATE = -1L;

    @Id
    long mId;
    long mRssId;

    String mTitle;
    @Nullable
    String mOriginUrl;
    @Nullable
    String mDescription;
    @Nullable
    String mImageUrl;
    @Nullable
    Long mPostDate;

    @Transient
    private Bitmap mImage;

    public Article(@NonNull final String title) {
        mTitle = title;
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
    public String getOriginUrl() {
        return mOriginUrl;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public Long getPostDate() {
        return mPostDate;
    }

    @Override
    public Bitmap getImage() {
        return mImage;
    }

    @Override
    public String getImageUrl() {
        return mImageUrl;
    }

    public void setTitle(final String title) {
        mTitle = title;
    }

    void setRssId(long id) {
        mRssId = id;
    }

    public void setDescription(final String text) {
        mDescription = text;
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

    public void setOrigin(final String origin) {
        mOriginUrl = origin;
    }
}
