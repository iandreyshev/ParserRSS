package ru.iandreyshev.parserrss.app.parserRss;

import android.support.annotation.NonNull;

import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import ru.iandreyshev.parserrss.models.article.Article;
import ru.iandreyshev.parserrss.models.feed.Feed;

abstract class Parser implements IParserRss {
    static final String ROOT_NODE_NAME = "rss";

    static final String FEED_NODE_NAME = "channel";
    static final String FEED_TITLE_NODE_NAME = "title";
    static final String FEED_DESCRIPTION_NODE_NAME = "description";
    static final String FEED_ORIGIN_NODE_NAME = "link";

    static final String ARTICLE_NODE_NAME = "item";
    static final String ARTICLE_TITLE_NODE_NAME = "title";
    static final String ARTICLE_DESCRIPTION_NODE_NAME = "description";
    static final String ARTICLE_URL_NODE_NAME = "link";

    private ParserRssResult mResult = ParserRssResult.NotParse;
    private Feed mFeed;
    private List<Article> mArticles = new ArrayList<>();

    @Override
    public final void parse(@NonNull Document xml) {
        mResult = ParserRssResult.InvalidRssFormat;

        final Element root = xml.getRootElement();

        if (root != null && root.getName().toLowerCase().equals(ROOT_NODE_NAME)) {
            parseFromRoot(root);
        }
    }

    @Override
    public void parseFeed(@NonNull Document xml) {
        mResult = ParserRssResult.InvalidRssFormat;

        final Element root = xml.getRootElement();

        if (root != null && root.getName().toLowerCase().equals(ROOT_NODE_NAME)) {
        }
    }

    @Override
    public final ParserRssResult getResult() {
        return mResult;
    }

    @Override
    public final List<Article> getArticles() {
        return mArticles;
    }

    @Override
    public final Feed getFeed() {
        return mFeed;
    }

    protected abstract void parseFromRoot(@NonNull final Element root);

    protected final void setFeed(@NonNull final Feed feed) {
        mResult = ParserRssResult.Success;
        mFeed = feed;
    }

    protected final void addArticles(@NonNull final List<Article> article) {
        mArticles.addAll(article);
    }

    protected Feed parseFeed(@NonNull final Element channelNode) {
        final Element titleNode = channelNode.getChild(FEED_TITLE_NODE_NAME);
        final Element urlNode = channelNode.getChild(FEED_ORIGIN_NODE_NAME);

        if (titleNode == null || urlNode == null) {

            return null;
        }

        final HttpUrl url = HttpUrl.parse(urlNode.getValue());

        if (url == null) {
            return null;
        }

        final Feed feed = new Feed(titleNode.getValue(), url);
        final Element description = channelNode.getChild(FEED_DESCRIPTION_NODE_NAME);

        if (description != null) {
            feed.setDescription(description.getValue());
        }

        return feed;
    }

    protected List<Article> parseArticles(@NonNull final Element items) {
        final List<Article> result = new ArrayList<>();
        final List<Element> itemsCollection = items.getChildren(ARTICLE_NODE_NAME);

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
        final Element titleNode = item.getChild(ARTICLE_TITLE_NODE_NAME);
        final Element urlNode = item.getChild(ARTICLE_URL_NODE_NAME);

        if (titleNode == null || urlNode == null) {
            return null;
        }

        HttpUrl url = HttpUrl.parse(urlNode.getValue());

        if (url == null) {
            return null;
        }

        final Article article = new Article(titleNode.getValue(), url);
        final Element textNode = item.getChild(ARTICLE_DESCRIPTION_NODE_NAME);

        if (textNode != null) {
            article.setText(textNode.getText());
        }

        return article;
    }
}
