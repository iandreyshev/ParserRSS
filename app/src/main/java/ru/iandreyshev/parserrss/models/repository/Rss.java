package ru.iandreyshev.parserrss.models.repository;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.Transient;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;
import ru.iandreyshev.parserrss.models.rss.IViewRss;

@Entity
public final class Rss implements IViewRss {
    @Id
    long mId;

    String mTitle = "";
    @Index
    String mUrl = "";
    String mOrigin = "";
    String mDescription;

    @Transient
    private List<Article> mArticles = new ArrayList<>();

    public Rss(@NonNull final String title, @NonNull final String origin) {
        mTitle = title;
        mOrigin = origin;
    }

    Rss() {
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
    public String getDescription() {
        return mDescription;
    }

    @Override
    @NonNull
    public String getUrl() {
        return mUrl;
    }

    @Override
    @NonNull
    public String getOrigin() {
        return mOrigin;
    }

    @NonNull
    @Override
    public List<IViewArticle> getViewArticles() {
        return new ArrayList<>(mArticles);
    }

    @NonNull
    public List<Article> getArticles() {
        return mArticles;
    }

    public void setId(long newId) {
        mId = newId;
    }

    public void setUrl(final String url) {
        mUrl = url;
    }

    public void setArticles(@NonNull final List<Article> newArticles) {
        mArticles = new ArrayList<>(newArticles);
    }

    public void setDescription(final String description) {
        mDescription = description;
    }

    @Override
    public final boolean equals(Object other) {
        return (other instanceof Rss) && mUrl.equals(((Rss) other).mUrl);
    }

    @Override
    public final int hashCode() {
        return mUrl.hashCode();
    }
}
