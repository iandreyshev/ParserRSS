package ru.iandreyshev.parserrss.models.rss;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import ru.iandreyshev.parserrss.app.IBuilder;

@Entity
public final class Rss extends ViewRss {
    private static final String TAG = Rss.class.getName();

    @Id
    long mId;

    String mTitle;
    @Index
    @Nullable
    String mUrl;
    @Nullable
    String mOrigin;
    @Nullable
    String mDescription;
    List<RssArticle> mArticles = new ArrayList<>();

    @Override
    public long getId() {
        return mId;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public String getOrigin() {
        return mOrigin;
    }

    @NonNull
    @Override
    public List<ViewRssArticle> getViewArticles() {
        return new ArrayList<>(mArticles);
    }

    @NonNull
    public List<RssArticle> getArticles() {
        return mArticles;
    }

    public void setId(long newId) {
        mId = newId;
    }

    public void setUrl(final String url) {
        mUrl = url;
    }

    public void bindArticles() {
        for (final RssArticle article : mArticles) {
            article.bindRss(this);
        }
    }

    public void setArticles(final List<RssArticle> newArticles) {
        mArticles = newArticles == null ? new ArrayList<>() : new ArrayList<>(newArticles);
    }

    private Rss() {
    }

    static class Builder implements IBuilder<Rss> {
        private final Rss mRss = new Rss();

        Builder(final String rssTitle) {
            mRss.mTitle = rssTitle;
        }

        @NonNull
        @Override
        public Rss build() {
            return mRss;
        }

        public Builder setUrl(final String url) {
            mRss.setUrl(url);

            return this;
        }

        public Builder setArticles(final List<RssArticle> articles) {
            mRss.setArticles(articles);

            return this;
        }

        public Builder setDescription(final String description) {
            mRss.mDescription = description;

            return this;
        }

        public Builder setOrigin(final String origin) {
            mRss.mOrigin = origin;

            return this;
        }
    }
}
