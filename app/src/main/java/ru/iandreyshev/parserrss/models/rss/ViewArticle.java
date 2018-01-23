package ru.iandreyshev.parserrss.models.rss;

import android.support.annotation.NonNull;

import ru.iandreyshev.parserrss.models.repository.Article;

public class ViewArticle {
    private final long mId;
    private final String mTitle;
    private final String mOriginUrl;
    private final String mDescription;
    private final Long mDate;

    public ViewArticle(@NonNull final Article article) {
        mId = article.getId();
        mTitle = article.getTitle();
        mDescription = article.getDescription();
        mOriginUrl = article.getOriginUrl();
        mDate = article.getDate();
    }

    public long getId() {
        return mId;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    @NonNull
    public String getDescription() {
        return mDescription;
    }

    public String getOriginUrl() {
        return mOriginUrl;
    }

    public Long getDate() {
        return mDate;
    }

    @Override
    public final boolean equals(Object other) {
        return (other instanceof ViewArticle) && mOriginUrl.equals(((ViewArticle) other).mOriginUrl);
    }

    @Override
    public final int hashCode() {
        return mOriginUrl.hashCode();
    }
}
