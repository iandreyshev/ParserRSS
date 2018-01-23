package ru.iandreyshev.parserrss.models.repository;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RssTest {
    private static final String TITLE = "Title";
    private static final String ORIGIN = "Origin";
    private static final String DESCRIPTION = "Description";
    private static final String URL = "URL";
    private static final Article ARTICLE = new Article();
    private static final long ID = 12;
    private Rss mRss;
    private Rss mDefaultRss;

    @Before
    public void setup() {
        mRss = new Rss(TITLE, ORIGIN);
        mDefaultRss = new Rss();
    }

    @Test
    public void haveConstructorWithZeroArgs() {
        final Rss rss = new Rss();

        assertNotNull(rss);
    }

    @Test
    public void haveIdGetterAndSetter() {
        mRss.setId(ID);

        assertEquals(mRss.getId(), ID);
    }

    @Test
    public void haveTitleGetter() {
        assertEquals(mRss.getTitle(), TITLE);
    }

    @Test
    public void haveDescriptionGetterAndSetter() {
        mRss.setDescription(DESCRIPTION);

        assertEquals(mRss.getDescription(), DESCRIPTION);
    }

    @Test
    public void haveEmptyTitleByDefault() {
        assertEquals(mDefaultRss.getTitle(), "");
    }

    @Test
    public void haveEmptyUrlByDefault() {
        assertEquals(mDefaultRss.getUrl(), "");
    }

    @Test
    public void haveEmptyOriginByDefault() {
        assertEquals(mDefaultRss.getOrigin(), "");
    }

    @Test
    public void haveEmptyListOfArticlesByDefault() {
        assertEquals(mDefaultRss.getArticles().size(), 0);
    }

    @Test
    public void haveEmptyListOfArticlesViewsByDefault() {
        assertTrue(mDefaultRss.getArticles().isEmpty());
    }

    @Test
    public void haveArticlesGetterAndSetter() {
        final List<Article> articles = new ArrayList<>();
        articles.add(ARTICLE);
        mRss.setArticles(articles);

        assertEquals(mRss.getArticles().get(0), ARTICLE);
    }

    @Test
    public void haveUrlGetterAndSetter() {
        mRss.setUrl(URL);

        assertEquals(mRss.getUrl(), URL);
    }

    @Test
    public void hashcodeEqualsUrlHashcode() {
        mRss.setUrl(URL);

        assertEquals(mRss.hashCode(), URL.hashCode());
    }

    @Test
    public void equalsWithObjectIfEqualsItsUrls() {
        mRss.setUrl(URL);
        final Rss otherRss = new Rss();

        assertFalse(mRss.equals(otherRss));

        otherRss.setUrl(URL);

        assertTrue(mRss.equals(otherRss));
    }
}