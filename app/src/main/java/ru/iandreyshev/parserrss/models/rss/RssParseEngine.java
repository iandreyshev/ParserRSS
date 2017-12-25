package ru.iandreyshev.parserrss.models.rss;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

abstract class RssParseEngine {
    private static final String DISABLE_DTD_FEATURE = "http://apache.org/xml/features/nonValidating/load-external-dtd";
    private static final List<SAXBuilder> DOC_BUILDERS = new ArrayList<>();

    static {
        final SAXBuilder withoutDtdBuilder = new SAXBuilder();
        withoutDtdBuilder.setFeature(DISABLE_DTD_FEATURE, false);
        DOC_BUILDERS.add(withoutDtdBuilder);
        DOC_BUILDERS.add(new SAXBuilder());
    }

    public final Rss parse(final String rssText) {
        try {
            final Document doc = toDocument(rssText);

            if (doc == null) {
                return null;
            }

            final Element root = doc.getRootElement();
            final Rss.Builder rssBuilder = parseRss(root);

            if (rssBuilder == null) {
                return null;
            }

            final ArrayList<RssArticle> articles = parseArticles(root);
            rssBuilder.setArticles(articles == null ? new ArrayList<>() : articles);

            return rssBuilder.build();

        } catch (Exception ex) {
            return null;
        }
    }

    protected abstract Rss.Builder parseRss(final Element root) throws Exception;

    protected abstract ArrayList<RssArticle> parseArticles(final Element root) throws Exception;

    private static Document toDocument(final String xmlText) {
        for (final SAXBuilder builder : DOC_BUILDERS) {
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
