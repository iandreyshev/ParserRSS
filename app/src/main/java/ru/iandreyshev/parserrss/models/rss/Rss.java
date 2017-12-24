package ru.iandreyshev.parserrss.models.rss;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.Transient;
import ru.iandreyshev.parserrss.app.IBuilder;

@Entity
public class Rss implements IViewRss {
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

    @Transient
    ArrayList<RssArticle> mRssArticles = new ArrayList<>();
    @Transient
    ArrayList<IViewRssArticle> mArticles = new ArrayList<>();

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
        return mArticles;
    }

    public ArrayList<RssArticle> getRssArticles() {
        return mRssArticles;
    }

    public void setArticles(final List<RssArticle> articles) {
        mArticles = new ArrayList<>(articles);
        mRssArticles = new ArrayList<>(articles);
    }

    public void setUrl(final String url) {
        mUrl = url;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof Rss) && ((Rss) other).getUrl().equals(mUrl);
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
        private Rss mRss = new Rss();

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
