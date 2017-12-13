package ru.iandreyshev.parserrss.app.parserRss;

import org.jdom2.Document;
import org.junit.Before;
import org.junit.Test;

import ru.iandreyshev.parserrss.TestUtils;
import ru.iandreyshev.parserrss.models.article.Article;
import ru.iandreyshev.parserrss.models.feed.Feed;

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
        parseFileAndCheck("valid_without_xml_format");

        final Feed feed = mParser.getFeed();

        assertEquals(feed.getTitle(), RSS_TITLE);
        assertEquals(feed.getDescription(), RSS_DESCRIPTION);
        assertNotNull(feed.getOriginUrl());
    }

    @Test
    public void parseRssWithFeedTitleAndDescriptionOnly() {
        parseFileAndCheck("valid_minimal");

        final Feed feed = mParser.getFeed();

        assertEquals(feed.getTitle(), RSS_TITLE);
        assertEquals(feed.getDescription(), RSS_DESCRIPTION);
    }

    @Test
    public void notParseFeedOriginUrlIfTheyNotValidForOkHttp3() {
        parseFile("invalid_with_invalid_feed_origin_url");

        assertEquals(mParser.getResult(), ParserRssResult.InvalidRssFormat);
    }

    @Test
    public void notParseIfTitleIsAbsent() {
        parseFile("invalid_without_title");

        assertEquals(mParser.getResult(), ParserRssResult.InvalidRssFormat);
    }

    @Test
    public void returnNotParseStatusIfNotParseAnythingYet() {
        assertEquals(mParser.getResult(), ParserRssResult.NotParse);
    }

    @Test
    public void returnEmptyArticlesListIfArticlesIsAbsent() {
        parseFileAndCheck("valid_minimal");

        assertEquals(0, mParser.getArticles().size());
    }

    @Test
    public void notParseArticlesInItemsNode() {
        parseFileAndCheck("invalid_with_articles_in_items_node");

        assertEquals(0, mParser.getArticles().size());
    }

    @Test
    public void parseArticlesInChannelNode() {
        parseFileAndCheck("valid_with_articles");

        assertEquals(2, mParser.getArticles().size());

        for (Article article : mParser.getArticles()) {

            assertEquals(article.getTitle(), ARTICLE_TITLE);
            assertEquals(article.getText(), ARTICLE_TEXT);
            assertNotNull(article.getUrl());

        }
    }

    private void parseFile(final String fileName) {
        final Document result = TestUtils.readXmlFromFile(toPath(fileName));

        assertNotNull(result);

        mParser.parse(result);
    }

    private void parseFileAndCheck(final String fileName) {
        parseFile(fileName);

        assertEquals(mParser.getResult(), ParserRssResult.Success);
    }

    private String toPath(final String fileName) {
        return "/xmlSamples/v2_0/" + fileName + ".xml";
    }
}