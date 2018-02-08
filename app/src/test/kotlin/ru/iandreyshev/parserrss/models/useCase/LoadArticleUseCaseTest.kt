package ru.iandreyshev.parserrss.models.useCase

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.iandreyshev.parserrss.MocksFactory
import ru.iandreyshev.parserrss.models.repository.Article
import ru.iandreyshev.parserrss.models.repository.Rss

@RunWith(RobolectricTestRunner::class)
@Config(manifest = "src/main/AndroidManifest.xml")
class LoadArticleUseCaseTest {

    companion object {
        private const val RSS_ID: Long = 12
        private const val RSS_TITLE = "Rss title"
        private const val RSS_DESCRIPTION = "Rss description"

        private const val ARTICLE_ID: Long = 15
        private const val ARTICLE_TITLE = "Article title"
        private const val ARTICLE_DESCRIPTION = "Article description"
    }

    private lateinit var mRss: Rss
    private lateinit var mArticle: Article
    private lateinit var mFactory: MocksFactory
    private lateinit var mListener: LoadArticleUseCase.IListener

    @Before
    fun setup() {
        mFactory = MocksFactory()
        mRss = Rss(
                id = RSS_ID,
                title = RSS_TITLE,
                description = RSS_DESCRIPTION
        )
        mArticle = Article(
                id = ARTICLE_ID,
                rssId = RSS_ID,
                title = ARTICLE_TITLE,
                description = ARTICLE_DESCRIPTION)
        mListener = mock()
    }

    @Test
    fun callLoadArticleMethod() {
        whenever(mFactory.repository.getArticleById(ARTICLE_ID)).thenReturn(mArticle)
        whenever(mFactory.repository.getRssById(RSS_ID)).thenReturn(mRss)

        LoadArticleUseCase(mFactory.repository, ARTICLE_ID, mListener).execute().get()

        verify(mListener).loadArticle(
                argThat { id == mRss.id || title == mRss.title || description == mRss.description },
                argThat { id == mArticle.id || title == mArticle.title || description == mArticle.description })
    }

    @Test
    fun callProcessMethods() {
        whenever(mFactory.repository.getArticleById(ARTICLE_ID)).thenReturn(mArticle)
        whenever(mFactory.repository.getRssById(RSS_ID)).thenReturn(mRss)

        LoadArticleUseCase(mFactory.repository, ARTICLE_ID, mListener).execute().get()

        verify(mListener).processStart()
        verify(mListener).processEnd()
    }
}
