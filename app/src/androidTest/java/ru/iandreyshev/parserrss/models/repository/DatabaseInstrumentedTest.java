package ru.iandreyshev.parserrss.models.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DatabaseInstrumentedTest {
    private static final String RSS_TITLE = "TITLE";
    private static final String RSS_ORIGIN = "ORIGIN";

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
        final List<Article> articleList = new ArrayList<>();

        for (int i = 0; i < ARTICLES_COUNT; ++i) {
            articleList.add(createArticle(i));
        }

        mRss.setArticles(articleList);
    }

    @Test
    public void returnEmptyListAfterGetAllIfStoreEmpty() throws Exception {
        assertEquals(mDatabase.getAllRss().size(), 0);
    }

    @Test
    public void updateRssIdAfterPut() throws Exception {
        mDatabase.putRssIfSameUrlNotExist(mRss);

        assertEquals(mDatabase.getRssById(mRss.getId()), mRss);
    }

    @Test
    public void updateArticlesRssIdAfterPut() throws Exception {
        mDatabase.putRssIfSameUrlNotExist(mRss);

        for (final Article article : mRss.getArticles()) {
            assertTrue(article.getRssId() == mRss.getId());
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
    public void canSaveBitmap() throws Exception {
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

    @NonNull
    private Article createArticle(int number) throws Exception {
        return new Article(
                String.format(ARTICLE_TITLE, number),
                String.format(ARTICLE_DESCRIPTION, number),
                String.format(ARTICLE_ORIGIN, number));
    }
}