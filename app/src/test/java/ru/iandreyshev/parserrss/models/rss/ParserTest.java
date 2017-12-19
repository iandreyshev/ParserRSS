package ru.iandreyshev.parserrss.models.rss;

import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import ru.iandreyshev.parserrss.TestUtils;

import static org.junit.Assert.*;

public class ParserTest {
    private Parser mFeedParseExceptionParser;
    private Parser mArticlesParseExceptionParser;

    @Before
    public void reset() {
        mFeedParseExceptionParser = new Parser() {
            @Override
            protected RssFeed parseFeed(Element root) throws Exception {
                throw new UnsupportedOperationException();
            }

            @Override
            protected List<RssArticle> parseArticles(Element root) throws Exception {
                fail();

                return null;
            }
        };
        mArticlesParseExceptionParser = new Parser() {
            @Override
            protected RssFeed parseFeed(Element root) throws Exception {
                return null;
            }

            @Override
            protected List<RssArticle> parseArticles(Element root) throws Exception {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    public void subclasses_can_throw_exception_in_feed_parsing_method() {
        final Rss rss = mFeedParseExceptionParser.parse(getXml("root_only"));

        assertNull(rss);
    }

    @Test
    public void subclasses_can_throw_exception_in_articles_parsing_method() {
        final Rss rss = mArticlesParseExceptionParser.parse(getXml("root_only"));

        assertNull(rss);
    }

    private String getXml(final String fileName) {
        return TestUtils.readFromFile("/xmlSamples/xml/" + fileName + ".xml");
    }
}