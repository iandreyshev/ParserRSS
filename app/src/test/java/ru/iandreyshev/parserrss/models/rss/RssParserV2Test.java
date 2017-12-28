package ru.iandreyshev.parserrss.models.rss;

import org.junit.Before;
import org.junit.Test;

import ru.iandreyshev.parserrss.TestUtils;

import static org.junit.Assert.*;

public class RssParserV2Test {
    private static final String RSS_TITLE = "Feed title";
    private static final String RSS_DESCRIPTION = "Feed description";
    private static final String ARTICLE_TITLE = "Article title";
    private static final String ARTICLE_TEXT = "Article text";
    private static final String ARTICLE_IMG_URL = "Article_image_url";

    private RssParserV2 mParser;

    @Before
    public void resetParser() {
        mParser = new RssParserV2();
    }

    @Test
    public void parse_rss_without_xml_format() {
        final Rss rss = parseFileAndCheck("valid_without_xml_format");

        assertEquals(RSS_TITLE, rss.getTitle());
        assertEquals(RSS_DESCRIPTION, rss.getDescription());
        assertNotNull(rss.getOrigin());
    }

    @Test
    public void parse_rss_with_feed_title_and_description_only() {
        final Rss rss = parseFileAndCheck("valid_minimal");

        assertEquals(RSS_TITLE, rss.getTitle());
        assertEquals(RSS_DESCRIPTION, rss.getDescription());
    }

    @Test
    public void not_parse_if_title_is_absent() {
        final Rss rss = parseFile("invalid_without_title");

        assertNull(rss);
    }

    @Test
    public void return_empty_articles_list_if_articles_is_absent() {
        final Rss rss = parseFileAndCheck("valid_minimal");

        assertTrue(rss.getViewArticles().isEmpty());
    }

    @Test
    public void not_parse_articles_in_items_node() {
        final Rss rss = parseFileAndCheck("invalid_with_articles_in_items_node");

        assertTrue(rss.getViewArticles().isEmpty());
    }

    @Test
    public void parse_articles_in_channel_node_if_they_have_title_link_description() {
        final Rss rss = parseFileAndCheck("valid_with_articles");

        assertEquals(2, rss.getViewArticles().size());

        for (final ViewRssArticle article : rss.getViewArticles()) {

            assertEquals(ARTICLE_TITLE, article.getTitle());
            assertEquals(ARTICLE_TEXT, article.getDescription());
            assertNotNull(article.getOriginUrl());

        }
    }

    @Test
    public void parse_articles_with_image_enclosure() {
        final Rss rss = parseFile("valid_with_article_image");

        assertEquals(2, rss.getViewArticles().size());

        for (final ViewRssArticle article : rss.getViewArticles()) {

            assertEquals(ARTICLE_IMG_URL, article.getImageUrl());

        }
    }

    @Test
    public void return_null_if_parse_invalid_xml() {
        final Rss rss = parseFile("invalid_with_bad_xml");

        assertNull(rss);
    }

    @Test
    public void return_null_if_parse_null_string() {
        final Rss rss = mParser.parse(null);

        assertNull(rss);
    }

    @Test
    public void return_empty_article_list_if_they_do_not_have_title_or_description_or_link() {
        final Rss rss = parseFile("valid_without_one_of_required_article_element");

        assertTrue(rss.getViewArticles().isEmpty());
    }

    @Test
    public void not_parse_articles_image_if_they_do_not_have_url_or_type() {
        final Rss rss = parseFile("valid_with_articles_enclosure_without_required_element");

        for (final ViewRssArticle article : rss.getViewArticles()) {

            assertNull(article.getImageUrl());

        }
    }

    @Test
    public void parse_article_date() {
        final Rss rss = parseFile("valid_with_pub_date");

        assertNotNull(rss.getViewArticles().get(0).getPostDate());
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