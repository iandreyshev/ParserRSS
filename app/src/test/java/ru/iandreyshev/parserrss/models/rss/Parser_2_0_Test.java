package ru.iandreyshev.parserrss.models.rss;

import org.junit.Before;
import org.junit.Test;

import ru.iandreyshev.parserrss.TestUtils;

import static org.junit.Assert.*;

public class Parser_2_0_Test {
    private static final String RSS_TITLE = "Feed title";
    private static final String RSS_DESCRIPTION = "Feed description";
    private static final String ARTICLE_TITLE = "Article title";
    private static final String ARTICLE_TEXT = "Article text";

    private Parser_2_0 mParser;

    @Before
    public void resetParser() {
        mParser = new Parser_2_0();
    }

    @Test
    public void parseRssWithoutXmlFormat() {
        final Rss rss = parseFileAndCheck("valid_without_xml_format");
        final IRssFeed feed = rss.getFeed();

        assertEquals(feed.getTitle(), RSS_TITLE);
        assertEquals(feed.getDescription(), RSS_DESCRIPTION);
        assertNotNull(feed.getOrigin());
    }

    @Test
    public void parseRssWithFeedTitleAndDescriptionOnly() {
        final Rss rss = parseFileAndCheck("valid_minimal");
        final IRssFeed feed = rss.getFeed();

        assertEquals(feed.getTitle(), RSS_TITLE);
        assertEquals(feed.getDescription(), RSS_DESCRIPTION);
    }

    @Test
    public void notParseIfTitleIsAbsent() {
        final Rss rss = parseFile("invalid_without_title");

        assertNull(rss);
    }

    @Test
    public void returnEmptyArticlesListIfArticlesIsAbsent() {
        final Rss rss = parseFileAndCheck("valid_minimal");

        assertEquals(rss.getArticles().size(), 0);
    }

    @Test
    public void notParseArticlesInItemsNode() {
        final Rss rss = parseFileAndCheck("invalid_with_articles_in_items_node");

        assertEquals(rss.getArticles().size(), 0);
    }

    @Test
    public void parseArticlesInChannelNode() {
        final Rss rss = parseFileAndCheck("valid_with_articles");

        assertEquals(rss.getArticles().size(), 2);

        for (IRssArticle article : rss.getArticles()) {

            assertEquals(article.getTitle(), ARTICLE_TITLE);
            assertEquals(article.getDescription(), ARTICLE_TEXT);
            assertNotNull(article.getOrigin());

        }
    }

    private Rss parseFile(final String fileName) {
        final String result = TestUtils.readFromFile(toPath(fileName));

        assertNotNull(result);

        return mParser.parse(result);
    }

    private Rss parseFileAndCheck(final String fileName) {
        final Rss rss = parseFile(fileName);

        assertNotNull(rss);

        return rss;
    }

    private String toPath(final String fileName) {
        return "/xmlSamples/v2_0/" + fileName + ".xml";
    }
}