package ru.iandreyshev.parserrss.app.parserRss;

import android.support.annotation.NonNull;

import org.jdom2.Element;

import java.util.List;

import ru.iandreyshev.parserrss.models.article.Article;
import ru.iandreyshev.parserrss.models.feed.Feed;

final class Parser_2_0 extends Parser {
    @Override
    protected void parseFromRoot(@NonNull final Element root) {
        if (!initFeed(root)) {
            return;
        }

        initArticles(root.getChild(FEED_NODE_NAME));
    }

    @Override
    protected Feed parseChannel(@NonNull final Element channel) {
        Feed feed = super.parseChannel(channel);

        if (feed == null || feed.getOriginUrl() == null) {
            return null;
        }

        return feed;
    }

    @Override
    protected Article parseItem(@NonNull final Element item) {
        Article article = super.parseItem(item);

        if (article == null || article.getOriginUrl() == null) {
            return null;
        }

        return article;
    }

    private boolean initFeed(@NonNull final Element root) {
        final Element channel = root.getChild(FEED_NODE_NAME);

        if (channel == null) {
            return false;
        }

        final Feed feed = parseChannel(channel);

        if (feed == null) {
            return false;
        }

        setFeed(feed);

        return true;
    }

    private void initArticles(@NonNull final Element channel) {
        List<Article> articles = parseItems(channel);

        if (articles == null) {
            return;
        }

        addArticles(articles);
    }
}
