package ru.iandreyshev.parserrss.models.rss;

import java.util.ArrayList;
import java.util.List;

public final class Rss {
    private static final List<Parser> mParsers = new ArrayList<>();

    private RssFeed mFeed;
    private ArrayList<RssArticle> mArticles;

    static {
        mParsers.add(new Parser_2_0());
    }

    public static Rss parse(final byte[] rssBytes) {
        if (rssBytes == null) {
            return null;
        }

        return parse(new String(rssBytes));
    }

    public static Rss parse(final String rssText) {
        for (final Parser parser : mParsers) {
            final Rss rss = parser.parse(rssText);

            if (rss != null) {
                return rss;
            }
        }

        return null;
    }

    public RssFeed getFeed() {
        return mFeed;
    }

    public ArrayList<RssArticle> getArticles() {
        return mArticles;
    }

    public void setUrl(final String url) {
        mFeed.setUrl(url);
    }

    @Override
    public int hashCode() {
        return mFeed.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Rss) {
            final Rss other = (Rss) object;

            return mFeed.equals(other.mFeed);
        }

        return false;
    }

    Rss(final RssFeed feed, final ArrayList<RssArticle> articles) throws NullPointerException {
        if (feed == null) {
            throw new NullPointerException("Try to create rss with null feed");
        } else if (articles == null) {
            throw new NullPointerException("Try to create rss with null articles");
        }

        mFeed = feed;
        mArticles = articles;
    }
}
