package ru.iandreyshev.parserrss.models.rss;

import org.junit.Before;
import org.junit.Test;

import ru.iandreyshev.parserrss.TestUtils;
import ru.iandreyshev.parserrss.models.repository.Article;
import ru.iandreyshev.parserrss.models.repository.Rss;

import static org.junit.Assert.*;

public class RssParserV2Test {
    private static final String RSS_TITLE = "Feed title";
    private static final String RSS_DESCRIPTION = "Feed description";
    private static final String ARTICLE_TITLE = "Article title";
    private static final String ARTICLE_TEXT = "Article text";
    private static final String ARTICLE_IMG_URL = "Article_image_url";

    private RssParserV2 mParser;

    @Before
    public void setup() {
        mParser = new RssParserV2();
    }

    @Test
    public void parseRssWithoutXmlFormat() {
        final Rss rss = parseFileAndCheck("valid_without_xml_format");

        assertEquals(RSS_TITLE, rss.getTitle());
        assertEquals(RSS_DESCRIPTION, rss.getDescription());
        assertNotNull(rss.getOrigin());
    }

    @Test
    public void parseRssWithFeedTitleAndDescriptionOnly() {
        final Rss rss = parseFileAndCheck("valid_minimal");

        assertEquals(RSS_TITLE, rss.getTitle());
        assertEquals(RSS_DESCRIPTION, rss.getDescription());
    }

    @Test
    public void notParseIfTitleIsAbsent() {
        final Rss rss = parseFile("invalid_without_title");

        assertNull(rss);
    }

    @Test
    public void returnEmptyArticlesListIfArticlesIsAbsent() {
        final Rss rss = parseFileAndCheck("valid_minimal");

        assertTrue(rss.getArticles().isEmpty());
    }

    @Test
    public void notParseArticlesInItemsNode() {
        final Rss rss = parseFileAndCheck("invalid_with_articles_in_items_node");

        assertTrue(rss.getArticles().isEmpty());
    }

    @Test
    public void parseArticlesInChannelNodeIfTheyHaveTitleLinkDescription() {
        final Rss rss = parseFileAndCheck("valid_with_articles");

        assertEquals(2, rss.getArticles().size());

        for (final Article article : rss.getArticles()) {

            assertEquals(ARTICLE_TITLE, article.getTitle());
            assertEquals(ARTICLE_TEXT, article.getDescription());
            assertNotNull(article.getOriginUrl());

        }
    }

    @Test
    public void parseArticlesWithImageEnclosure() {
        final Rss rss = parseFile("valid_with_article_image");

        assertEquals(2, rss.getArticles().size());

        for (final Article article : rss.getArticles()) {

            assertEquals(ARTICLE_IMG_URL, article.getImageUrl());

        }
    }

    @Test
    public void returnNullIfParseInvalidXml() {
        final Rss rss = parseFile("invalid_with_bad_xml");

        assertNull(rss);
    }

    @Test
    public void returnNullIfParseNullString() {
        final Rss rss = mParser.parse(null);

        assertNull(rss);
    }

    @Test
    public void returnEmptyArticleListIfTheyDoNotHaveTitleOrDescriptionOrLink() {
        final Rss rss = parseFile("valid_without_one_of_required_article_element");

        assertTrue(rss.getArticles().isEmpty());
    }

    @Test
    public void notParseArticlesImageIfTheyDoNotHaveUrlOrType() {
        final Rss rss = parseFile("valid_with_articles_enclosure_without_required_element");

        for (final Article article : rss.getArticles()) {

            assertNull(article.getImageUrl());

        }
    }

    @Test
    public void parseArticleDate() {
        final Rss rss = parseFile("valid_with_pub_date");

        assertNotNull(rss.getArticles().get(0).getDate());
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