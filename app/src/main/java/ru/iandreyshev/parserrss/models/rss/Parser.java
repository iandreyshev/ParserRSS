package ru.iandreyshev.parserrss.models.rss;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

abstract class Parser implements IRssParser {
    private static final String DISABLE_DTD_FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

    private static List<SAXBuilder> mDocBuilders = new ArrayList<>();

    static {
        final SAXBuilder withoutDtdBuilder = new SAXBuilder();
        withoutDtdBuilder.setFeature(DISABLE_DTD_FEATURE, false);
        mDocBuilders.add(withoutDtdBuilder);
        mDocBuilders.add(new SAXBuilder());
    }

    @Override
    public final Rss parse(final String rss) {
        try {
            final Document doc = toDocument(rss);

            if (doc == null) {
                return null;
            }

            final Element root = doc.getRootElement();
            final RssFeed feed = parseFeed(root);
            final ArrayList<RssArticle> articles = parseArticles(root);

            if (feed == null || articles == null) {
                return null;
            }

            return new Rss(feed, articles);

        } catch (Exception ex) {
            return null;
        }
    }

    protected abstract RssFeed parseFeed(final Element root) throws Exception;

    protected abstract ArrayList<RssArticle> parseArticles(final Element root) throws Exception;

    private static Document toDocument(final String xmlText) {
        for (final SAXBuilder builder : mDocBuilders) {
            final Document result;

            try {
                result = builder.build(new StringReader(xmlText));
            } catch (Exception ex) {
                continue;
            }

            if (result != null) {
                return result;
            }
        }

        return null;
    }
}
