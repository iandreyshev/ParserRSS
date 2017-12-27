package ru.iandreyshev.parserrss.models.rss;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToMany;
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

    @Backlink
    ToMany<RssArticle> mArticles;

    private Rss() {
    }

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

    @Override
    public ArrayList<ViewRssArticle> getArticlesViewInfo() {
        return new ArrayList<>(mArticles == null ? new ArrayList<>() : mArticles);
    }

    public List<RssArticle> getArticles() {
        return mArticles;
    }

    public void updateInfo(final Rss rssWithNewInfo) {
        mTitle = rssWithNewInfo.mTitle;
        mDescription = rssWithNewInfo.mDescription;

        setArticles(rssWithNewInfo.mArticles);
    }

    public void setUrl(final String url) {
        mUrl = url;
    }

    void setArticles(final List<RssArticle> newArticles) {
        mArticles.clear();
        mArticles.addAll(newArticles);
        Log.e(TAG, String.format("Add %s new articles", newArticles.size()));
    }

    static class Builder implements IBuilder<Rss> {
        private final Rss mRss = new Rss();

        Builder(final String rssTitle) {
            mRss.mTitle = rssTitle;
        }

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
