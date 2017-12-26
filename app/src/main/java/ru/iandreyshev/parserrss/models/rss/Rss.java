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
public class Rss implements IViewRss {
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
    public ArrayList<IViewRssArticle> getArticles() {
        return new ArrayList<>(mArticles);
    }

    public List<RssArticle> getRssArticles() {
        return mArticles;
    }

    public void setUrl(final String url) {
        mUrl = url;
    }

    public void setId(long id) {
        mId = id;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Rss) && ((Rss) other).getUrl().equals(mUrl);
    }

    void setArticles(final List<RssArticle> newArticles) {
        mArticles.clear();
        mArticles.addAll(newArticles);
        Log.e(TAG, String.format("Add %s new articles", newArticles.size()));
    }

    public static class Parser {
        private static final List<RssParseEngine> mParsers = new ArrayList<>();

        static {
            mParsers.add(new RssParserV2());
        }

        public static Rss parse(final byte[] rssBytes) {
            if (rssBytes == null) {
                return null;
            }

            return parse(new String(rssBytes));
        }

        public static Rss parse(final String rssText) {
            for (final RssParseEngine parser : mParsers) {
                final Rss rss = parser.parse(rssText);

                if (rss != null) {
                    return rss;
                }
            }

            return null;
        }
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
