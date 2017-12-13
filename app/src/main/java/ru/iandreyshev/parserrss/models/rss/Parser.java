package ru.iandreyshev.parserrss.models.rss;

import android.support.annotation.NonNull;

import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.models.article.Article;
import ru.iandreyshev.parserrss.models.feed.Feed;

abstract class Parser {
    private State mState = State.NotParse;
    private Feed mFeed;
    private List<Article> mArticles = new ArrayList<>();

    enum State {
        NotParse,
        Success,
        InvalidFormat,
    }

    public final void parse(final Document xml) {
        try {
            mState = State.InvalidFormat;
            final Element root = xml.getRootElement();

            if (root != null && root.getName().toLowerCase().equals(NodeName.ROOT)) {
                parseFromRoot(root);
            }
        } catch (Exception ex) {
        }
    }

    public final State getState() {
        return mState;
    }

    public List<Article> getArticles() {
        return mArticles;
    }

    public Feed getFeed() {
        return mFeed;
    }

    protected final void addArticles(final List<Article> article) {
        mArticles.addAll(article);
    }

    protected final void setFeed(final Feed feed) {
        mState = State.Success;
        mFeed = feed;
    }

    protected abstract void parseFromRoot(final Element root);

    protected Feed parseFeed(final Element channelNode) {
        final Element titleNode = channelNode.getChild(NodeName.FEED_TITLE);
        final Element urlNode = channelNode.getChild(NodeName.FEED_ORIGIN);

        if (titleNode == null || urlNode == null) {

            return null;
        }

        final Feed feed = new Feed(titleNode.getValue(), urlNode.getValue());
        final Element description = channelNode.getChild(NodeName.FEED_DESCRIPTION);

        if (description != null) {
            feed.setDescription(description.getValue());
        }

        return feed;
    }

    protected List<Article> parseArticles(@NonNull final Element items) {
        final List<Article> result = new ArrayList<>();
        final List<Element> itemsCollection = items.getChildren(NodeName.ARTICLE);

        if(itemsCollection == null) {
            return null;
        }

        for (final Element itemNode : itemsCollection) {
            final Article article = parseItem(itemNode);

            if (article != null) {
                result.add(article);
            }
        }

        return result;
    }

    protected Article parseItem(@NonNull final Element item) {
        final Element titleNode = item.getChild(NodeName.ARTICLE_TITLE);
        final Element originNode = item.getChild(NodeName.ARTICLE_ORIGIN);

        if (titleNode == null || originNode == null) {
            return null;
        }

        final Article article = new Article(titleNode.getValue(), originNode.getValue());
        final Element textNode = item.getChild(NodeName.ARTICLE_TEXT);

        if (textNode != null) {
            article.setText(textNode.getText());
        }

        return article;
    }
}
