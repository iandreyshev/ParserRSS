package ru.iandreyshev.parserrss.models.rss;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

final class Parser_2_0 extends Parser {
    private static final String FEED_NAME = "channel";
    private static final String FEED_TITLE_NAME = "title";
    private static final String FEED_ORIGIN_NAME = "link";
    private static final String FEED_DESCRIPTION_NAME = "description";

    private static final String ARTICLE_NAME = "item";
    private static final String ARTICLE_TITLE_NAME = "title";
    private static final String ARTICLE_ORIGIN_NAME = "link";
    private static final String ARTICLE_DESCRIPTION_NAME = "description";

    @Override
    protected RssFeed parseFeed(final Element root) {
        final Element channel = root.getChild(FEED_NAME);

        if (channel == null) {
            return null;
        }

        return parseChannel(channel);
    }

    @Override
    protected List<RssArticle> parseArticles(final Element root) {
        final Element channel = root.getChild(FEED_NAME);
        final List<Element> items = channel.getChildren(ARTICLE_NAME);

        if (items == null) {
            return null;
        }

        final List<RssArticle> result = new ArrayList<>();

        for (final Element article : items) {
            result.add(parseArticle(article));
        }

        return result;
    }

    private RssFeed parseChannel(final Element channel) {
        final String title = channel.getChildText(FEED_TITLE_NAME);
        final String link = channel.getChildText(FEED_ORIGIN_NAME);
        final String description = channel.getChildText(FEED_DESCRIPTION_NAME);

        if (title == null || link == null || description == null) {
            return null;
        }

        final RssFeed result = new RssFeed(title, link);
        result.setDescription(description);

        return result;
    }

    private RssArticle parseArticle(final Element item) {
        final String title = item.getChildText(ARTICLE_TITLE_NAME);
        final String link = item.getChildText(ARTICLE_ORIGIN_NAME);
        final String description = item.getChildText(ARTICLE_DESCRIPTION_NAME);

        if (title == null || link == null || description == null) {
            return null;
        }

        final RssArticle result = new RssArticle(title, link);
        result.setDescription(description);

        return result;
    }
}
