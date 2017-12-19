package ru.iandreyshev.parserrss.models.rss;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.models.web.Url;

public final class Rss {
    private static final List<Parser> PARSERS = new ArrayList<>();

    private RssFeed mFeed;
    private List<RssArticle> mArticles;

    static {
        PARSERS.add(new Parser_2_0());
    }

    public static Rss parse(final byte[] rssBytes) {
        return parse(new String(rssBytes));
    }

    public static Rss parse(final String rssText) {
        for (final Parser parser : PARSERS) {
            final Rss rss = parser.parse(rssText);

            if (rss != null) {
                return rss;
            }
        }

        return null;
    }

    public IRssFeed getFeed() {
        return mFeed;
    }

    public List<IRssArticle> getArticles() {
        return new ArrayList<>(mArticles);
    }

    public void setUrl(final Url url) {
        if (mFeed != null) {
            mFeed.setUrl(url);
        }
    }

    Rss(final RssFeed feed, final List<RssArticle> articles) throws NullPointerException {
        if (feed == null) {
            throw new NullPointerException("Try to create rss with null feed");
        } else if (articles == null) {
            throw new NullPointerException("Try to create rss with null articles");
        }

        mFeed = feed;
        mArticles = articles;
    }
}
