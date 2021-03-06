package ru.iandreyshev.parserrss.models.rss;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.jdom2.Element;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.iandreyshev.parserrss.models.repository.Article;
import ru.iandreyshev.parserrss.models.repository.Rss;

final class RssParserV2 extends RssParseEngine {
    private static final String FEED_NAME = "channel";
    private static final String FEED_TITLE = "title";
    private static final String FEED_ORIGIN = "link";
    private static final String FEED_DESCRIPTION = "description";

    private static final String ARTICLE_NAME = "item";
    private static final String ARTICLE_TITLE = "title";
    private static final String ARTICLE_ORIGIN = "link";
    private static final String ARTICLE_DESCRIPTION = "description";

    private static final String DATE_NAME = "pubDate";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(("EEE, d MMM yyyy HH:mm:ss Z"), Locale.ENGLISH);

    private static final String ARTICLE_IMG_NODE = "enclosure";
    private static final String ARTICLE_IMG_URL = "url";
    private static final String ARTICLE_IMG_TYPE = "type";

    @Override
    @Nullable
    protected Rss parseRss(final Element root) {
        final Element channel = root.getChild(FEED_NAME);
        final String title = channel.getChildText(FEED_TITLE);
        final String origin = channel.getChildText(FEED_ORIGIN);

        if (title == null || origin == null) {
            return null;
        }

        final Rss rss = new Rss(title, origin);
        rss.setDescription(channel.getChildText(FEED_DESCRIPTION));

        return rss;
    }

    @NonNull
    @Override
    protected ArrayList<Article> parseArticles(final Element root) {
        final Element channel = root.getChild(FEED_NAME);
        final List<Element> items = channel.getChildren(ARTICLE_NAME);
        final ArrayList<Article> result = new ArrayList<>();

        for (final Element item : items) {
            final Article article = parseArticle(item);

            if (article != null) {
                result.add(article);
            }
        }

        return result;
    }

    private Article parseArticle(final Element item) {
        final String title = item.getChildText(ARTICLE_TITLE);
        final String origin = item.getChildText(ARTICLE_ORIGIN);
        final String description = item.getChildText(ARTICLE_DESCRIPTION);

        if (title == null || origin == null || description == null) {
            return null;
        }

        final Article article = new Article(title, description, origin);
        parseArticleDate(item, article);
        parseArticleImage(item, article);

        return article;
    }

    private void parseArticleDate(final Element item, final Article article) {
        final String dateStr = item.getChildText(DATE_NAME);
        final Date date;

        try {
            date = DATE_FORMAT.parse(dateStr);
        } catch (Exception ex) {
            return;
        }

        article.setDate(date);
    }

    private void parseArticleImage(final Element item, final Article article) {
        final Element resource = item.getChild(ARTICLE_IMG_NODE);

        if (resource == null) {
            return;
        }

        final String url = resource.getAttributeValue(ARTICLE_IMG_URL);
        final String type = resource.getAttributeValue(ARTICLE_IMG_TYPE);

        if (url == null || type == null || !isValidImageType(type)) {
            return;
        }

        article.setImageUrl(url);
    }

    private boolean isValidImageType(String type) {
        return type.equals(MimeType.JPEG) || type.equals(MimeType.PNG);
    }
}
