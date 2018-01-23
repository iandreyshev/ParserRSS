package ru.iandreyshev.parserrss.models.repository;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class ArticleTest {
    private static final String TITLE = "TITLE";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String ORIGIN = "ORIGIN";
    private static final Long RSS_ID = 1L;
    private static final Bitmap IMAGE = Bitmap.createBitmap(100, 100, Bitmap.Config.ALPHA_8);
    private static final String IMAGE_URL = "IMAGE_URL";
    private static final Date POST_DATE = new Date();

    private Article mArticle;
    private Article mDefaultArticle;

    @Before
    public void setup() {
        mArticle = new Article(TITLE, DESCRIPTION, ORIGIN);
        mDefaultArticle = new Article();
    }

    @Test
    public void haveEmptyTitleByDefault() {
        assertEquals(mDefaultArticle.getTitle(), "");
    }

    @Test
    public void haveEmptyDescriptionByDefault() {
        assertEquals(mDefaultArticle.getDescription(), "");
    }

    @Test
    public void haveEmptyOriginByDefault() {
        assertEquals(mDefaultArticle.getOriginUrl(), "");
    }

    @Test
    public void haveConstructorWithZeroArgs() {
        final Article article = new Article();

        assertNotNull(article);
    }

    @Test
    public void haveIdGetter() {
        assertEquals(mArticle.getId(), 0);
    }

    @Test
    public void haveRssIdGetterAndSetter() {
        mArticle.setRssId(RSS_ID);

        assertEquals(mArticle.getRssId(), RSS_ID);
    }

    @Test
    public void haveImageGetterAndSetter() {
        mArticle.setImage(IMAGE);

        assertEquals(mArticle.getImage(), IMAGE);
    }

    @Test
    public void haveImageUrlGetterAndSetter() {
        mArticle.setImageUrl(IMAGE_URL);

        assertEquals(mArticle.getImageUrl(), IMAGE_URL);
    }

    @Test
    public void haveDateGetterAndSetter() {
        mArticle.setDate(POST_DATE);

        assertEquals(mArticle.getDate(), new Long(POST_DATE.getTime()));
    }

    @Test
    public void equalWithObjectIfEqualsItsOrigins() {
        Article other = new Article();

        assertFalse(other.equals(mArticle));

        other = new Article("", "", mArticle.getOriginUrl());

        assertTrue(mArticle.equals(other));
    }

    @Test
    public void hashcodeEqualOriginHashcode() {
        assertEquals(mArticle.hashCode(), mArticle.getOriginUrl().hashCode());
    }
}