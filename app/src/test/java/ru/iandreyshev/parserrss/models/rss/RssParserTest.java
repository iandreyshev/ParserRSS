package ru.iandreyshev.parserrss.models.rss;

import android.support.annotation.NonNull;

import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import ru.iandreyshev.parserrss.TestUtils;
import ru.iandreyshev.parserrss.models.repository.Rss;
import ru.iandreyshev.parserrss.models.repository.Article;

import static org.junit.Assert.*;

public class RssParserTest {
    private RssParseEngine mFeedParseExceptionParser;
    private RssParseEngine mArticlesParseExceptionParser;

    @Before
    public void setup() {
        mFeedParseExceptionParser = new RssParseEngine() {
            @Override
            protected Rss parseRss(Element root) throws Exception {
                throw new UnsupportedOperationException();
            }

            @NonNull
            @Override
            protected ArrayList<Article> parseArticles(Element root) throws Exception {
                fail();

                return null;
            }
        };
        mArticlesParseExceptionParser = new RssParseEngine() {
            @Override
            protected Rss parseRss(Element root) throws Exception {
                return null;
            }

            @NonNull
            @Override
            protected ArrayList<Article> parseArticles(Element root) throws Exception {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    public void subclassesCanThrowExceptionInFeedParsingMethod() {
        final Rss rss = mFeedParseExceptionParser.parse(getXml("root_only"));

        assertNull(rss);
    }

    @Test
    public void subclassesCanThrowExceptionInArticlesParsingMethod() {
        final Rss rss = mArticlesParseExceptionParser.parse(getXml("root_only"));

        assertNull(rss);
    }

    private String getXml(final String fileName) {
        return TestUtils.readFromFile("/xmlSamples/xml/" + fileName + ".xml");
    }
}