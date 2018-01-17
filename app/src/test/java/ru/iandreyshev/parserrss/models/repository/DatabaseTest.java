package ru.iandreyshev.parserrss.models.repository;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DatabaseTest {
    private static final String RSS_TITLE = "TITLE";
    private static final String RSS_ORIGIN = "ORIGIN";
    private static final String RSS_URL = "URL";
    private static final String NOT_USE_RSS_URL = "NOT_USE_URL";

    private static final int ARTICLES_COUNT = 25;
    private static final String ARTICLE_TITLE = "TITLE %s";
    private static final String ARTICLE_DESCRIPTION = "DESCRIPTION %s";
    private static final String ARTICLE_ORIGIN = "ORIGIN %s";

    private static final byte[] IMAGE = {0, 1, 2, 3};

    private Database mDatabase;
    private Rss mRss;

    @Before
    public void setup() throws Exception {
        File tempFile = File.createTempFile("object-store-test", "");
        tempFile.delete();
        mDatabase = new Database(MyObjectBox.builder().directory(tempFile).build());

        mRss = new Rss(RSS_TITLE, RSS_ORIGIN);
        mRss.setUrl(RSS_URL);
        final List<Article> articleList = new ArrayList<>();

        for (int i = 0; i < ARTICLES_COUNT; ++i) {
            articleList.add(createArticle(i));
        }

        mRss.setArticles(articleList);
    }

    @Test
    public void noThrowExceptionIfTryToGetItemByInvalidId() {
        try {
            for (Long id : Database.INVALID_IDS) {
                mDatabase.getArticleById(id);
                mDatabase.getRssById(id);
                mDatabase.updateArticleImage(id, null);
                mDatabase.removeRssById(id);
            }
        } catch (Exception ex) {
            fail();
        }
    }

    @Test
    public void returnNullIfGetRssByInvalidId() {
        assertNull(mDatabase.getRssById(-2));
    }

    @Test
    public void returnTrueIfRssWithSameUrlExist() throws Exception {
        mDatabase.putRssIfSameUrlNotExist(mRss);

        assertTrue(mDatabase.isRssWithUrlExist(RSS_URL));
    }

    @Test
    public void returnFalseIfRssWithSameUrlNotExist() throws Exception {
        mDatabase.putRssIfSameUrlNotExist(mRss);

        assertFalse(mDatabase.isRssWithUrlExist(NOT_USE_RSS_URL));
    }

    @Test
    public void returnArticlesById() throws Exception {
        mDatabase.putRssIfSameUrlNotExist(mRss);

        for (final Article article : mRss.getArticles()) {
            assertNotNull(mDatabase.getArticleById(article.getId()));
        }
    }

    @Test
    public void returnEmptyListAfterGetAllIfStoreEmpty() throws Exception {
        assertTrue(mDatabase.getAllRss().isEmpty());
    }

    @Test
    public void updateRssIdAfterPut() throws Exception {
        mDatabase.putRssIfSameUrlNotExist(mRss);

        assertEquals(mDatabase.getRssById(mRss.getId()), mRss);
    }

    @Test
    public void returnTrueWhenUpdatingRssExist() throws Exception {
        final Rss newRss = new Rss("Title", "Origin");
        newRss.setDescription("Description");
        newRss.setUrl(mRss.getUrl());

        assertNotEquals(newRss.getTitle(), mRss.getTitle());
        assertNotEquals(newRss.getDescription(), mRss.getDescription());
        assertNotEquals(newRss.getArticles().size(), mRss.getArticles().size());

        assertEquals(newRss.getUrl(), mRss.getUrl());

        assertTrue(mDatabase.putRssIfSameUrlNotExist(newRss));
        assertTrue(mDatabase.updateRssWithSameUrl(mRss));

        assertNotEquals(newRss.getTitle(), mRss.getTitle());
        assertNotEquals(newRss.getDescription(), mRss.getDescription());
    }

    @Test
    public void returnFalseWhenUpdatingRssNotExist() throws Exception {
        final Rss newRss = new Rss();
        newRss.setUrl(NOT_USE_RSS_URL);

        assertTrue(mDatabase.putRssIfSameUrlNotExist(newRss));
        assertFalse(mDatabase.updateRssWithSameUrl(mRss));
    }

    @Test
    public void removeRss() throws Exception {
        assertTrue(mDatabase.putRssIfSameUrlNotExist(mRss));

        mDatabase.removeRssById(mRss.getId());

        assertTrue(mDatabase.getAllRss().isEmpty());
    }

    @Test
    public void returnFalseAfterPutRssWithSameUrl() throws Exception {
        mDatabase.putRssIfSameUrlNotExist(mRss);

        final Rss rssWithSameUrl = new Rss();
        rssWithSameUrl.setUrl(mRss.getUrl());

        assertFalse(mDatabase.putRssIfSameUrlNotExist(rssWithSameUrl));
    }

    @Test
    public void updateRssArticlesIdAfterPut() throws Exception {
        mDatabase.putRssIfSameUrlNotExist(mRss);

        for (final Article article : mRss.getArticles()) {
            assertTrue(article.getRssId() == mRss.getId());
        }
    }

    @Test
    public void updateImageBytesIfArticleExist() throws Exception {
        final byte[] newImage = {1};

        assertNotEquals(newImage.length, IMAGE.length);

        mDatabase.putRssIfSameUrlNotExist(mRss);

        for (final Article article : mRss.getArticles()) {
            article.setImage(IMAGE);

            Long id = article.getId();
            mDatabase.updateArticleImage(id, newImage);

            final Article articleFromDatabase = mDatabase.getArticleById(id);

            assertNotNull(articleFromDatabase);
            assertEquals(articleFromDatabase.getImage().length, newImage.length);
        }
    }

    @Test
    public void returnRssWithSameArticles() throws Exception {
        final HashSet<Article> articles = new HashSet<>(mRss.getArticles());

        mDatabase.putRssIfSameUrlNotExist(mRss);
        final Rss rssFromDatabase = mDatabase.getRssById(mRss.getId());

        assertNotNull(rssFromDatabase);

        for (final Article articleFromDatabase : rssFromDatabase.getArticles()) {
            assertTrue(articles.contains(articleFromDatabase));
        }
    }

    @Test
    public void canSaveImageBytesArray() throws Exception {
        for (final Article article : mRss.getArticles()) {
            article.setImage(IMAGE);
        }

        mDatabase.putRssIfSameUrlNotExist(mRss);
        final Rss rssFromDb = mDatabase.getRssById(mRss.getId());

        assertNotNull(rssFromDb);

        for (final Article article : rssFromDb.getArticles()) {
            assertNotNull(article.getImage());
        }
    }

    @Test
    public void returnAllRss() throws Exception {
        mDatabase.putRssIfSameUrlNotExist(mRss);

        assertEquals(mDatabase.getAllRss().size(), 1);
    }

    @NonNull
    private Article createArticle(int number) throws Exception {
        return new Article(
                String.format(ARTICLE_TITLE, number),
                String.format(ARTICLE_DESCRIPTION, number),
                String.format(ARTICLE_ORIGIN, number));
    }
}