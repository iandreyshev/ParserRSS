package ru.iandreyshev.parserrss.models.rss;

import android.support.annotation.NonNull;

import org.jdom2.Element;

import java.util.List;

import ru.iandreyshev.parserrss.models.article.Article;
import ru.iandreyshev.parserrss.models.feed.Feed;

final class Parser_2_0 extends Parser {
    @Override
    protected void parseFromRoot(final Element root) {
        final Element feed = root.getChild(NodeName.FEED);

        if (!initFeed(feed)) {
            return;
        }

        initArticles(feed);
    }

    @Override
    protected Feed parseFeed(@NonNull final Element channel) {
        Feed feed = super.parseFeed(channel);

        if (feed == null || feed.getDescription() == null) {
            return null;
        }

        return feed;
    }

    @Override
    protected Article parseItem(@NonNull final Element item) {
        Article article = super.parseItem(item);

        if (article == null || article.getText() == null) {
            return null;
        }

        return article;
    }

    private boolean initFeed(final Element feedNode) {
        final Feed feed = parseFeed(feedNode);

        if (feed == null) {
            return false;
        }

        setFeed(feed);

        return true;
    }

    private void initArticles(@NonNull final Element channel) {
        List<Article> articles = parseArticles(channel);

        if (articles == null) {
            return;
        }

        addArticles(articles);
    }
}
