package ru.iandreyshev.parserrss.models.rss;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

class RssArticle implements IRssArticle {
    private String mTitle;
    private String mOrigin;
    private String mDescription;
    private Bitmap mImage;

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
    public Bitmap getImage() {
        return mImage;
    }

    void setTitle(String title) {
        mTitle = title;
    }

    void setDescription(String text) {
        mDescription = text;
    }

    public void setImage(Bitmap image) {
        mImage = image;
    }
}
