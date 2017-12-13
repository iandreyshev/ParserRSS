package ru.iandreyshev.parserrss.models.rss;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.models.article.Article;
import ru.iandreyshev.parserrss.models.feed.Feed;

public final class Rss {
    private static final String DISABLE_DTD_FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

    private static List<SAXBuilder> mDocBuilders = new ArrayList<>();

    static {
        mDocBuilders.add(new SAXBuilder());

        final SAXBuilder withoutDtdBuilder = new SAXBuilder();
        withoutDtdBuilder.setFeature(DISABLE_DTD_FEATURE, false);
    }

    private static List<Parser> mParsers = new ArrayList<>();

    static {
        mParsers.add(new Parser_0_91());
        mParsers.add(new Parser_1_0());
        mParsers.add(new Parser_2_0());
    }

    private Feed mFeed;
    private List<Article> mArticles;

    public static Rss parse(final String rssText) {
        try {
            Rss result = null;

            for (final Parser parser : mParsers) {
                final Document document = toDocument(rssText);
                parser.parse(document);

                if (parser.getState() == Parser.State.Success) {
                    result = new Rss(parser.getFeed(), parser.getArticles());

                    break;
                }
            }

            return result;

        } catch (Exception ex) {
            return null;
        }
    }

    private Rss(final Feed feed, final List<Article> articles) {
        mFeed = feed;
        mArticles = articles;
    }

    public Feed getFeed() {
        return mFeed;
    }

    public List<Article> getArticles() {
        return mArticles;
    }

    private static Document toDocument(final String xmlText) {
        try {
            Document result = null;

            for (final SAXBuilder builder : mDocBuilders) {
                result = builder.build(new StringReader(xmlText));

                if (result != null) {
                    break;
                }
            }

            return result;

        } catch (Exception ex) {
            return null;
        }
    }
}
