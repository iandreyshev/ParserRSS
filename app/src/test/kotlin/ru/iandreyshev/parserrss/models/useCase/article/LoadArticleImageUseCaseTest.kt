package ru.iandreyshev.parserrss.models.useCase.article

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import ru.iandreyshev.parserrss.MocksFactory
import ru.iandreyshev.parserrss.models.rss.Article
import ru.iandreyshev.parserrss.models.rss.ArticleImage
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

class LoadArticleImageUseCaseTest {

    companion object {
        private const val ARTICLE_ID = 0L
    }

    private lateinit var mListener: LoadArticleImageUseCase.IListener
    private lateinit var mFactory: MocksFactory
    private lateinit var mImage: ArticleImage
    private lateinit var mImageBytes: ByteArray
    private val mUseCase
        get() = LoadArticleImageUseCase(
                mFactory.repository,
                mFactory.requestHandler,
                mFactory.imageProps,
                ARTICLE_ID,
                mListener
        )

    @Before
    fun setup() {
        mFactory = MocksFactory()
        mListener = mock()
        mImageBytes = byteArrayOf()
        mImage = ArticleImage(articleId = ARTICLE_ID, bytes = mImageBytes)
    }

    @Test
    fun callProcessMethods() {
        mUseCase.start()

        verify(mListener).processStart()
        verify(mListener).processEnd()
    }

    @Test
    fun callInsertImageIfFoundItInRepository() {
        whenever(mFactory.repository.getArticleImageByArticleId(ARTICLE_ID)).thenReturn(mImage)
        whenever(mFactory.imageProps.configureToView(mImageBytes)).thenReturn(mImageBytes)

        mUseCase.start()

        verify(mListener).insertImage(mImageBytes)
    }

    @Test
    fun callInsertImageIfTakeItFromNet() {
        val imageUrl = "Image.url"
        val article = Article(imageUrl = imageUrl)

        whenever(mFactory.repository.getArticleImageByArticleId(ARTICLE_ID)).thenReturn(null)
        whenever(mFactory.repository.getArticleById(ARTICLE_ID)).thenReturn(article)
        whenever(mFactory.requestHandler.send(imageUrl)).thenReturn(HttpRequestHandler.State.SUCCESS)
        whenever(mFactory.requestHandler.body).thenReturn(ByteArray(0))
        whenever(mFactory.imageProps.configureToMemory(any())).thenReturn(mImageBytes)
        whenever(mFactory.imageProps.configureToView(any())).thenReturn(mImageBytes)

        mUseCase.start()

        verify(mFactory.repository).putArticleImageIfArticleExist(argThat {
            articleId == ARTICLE_ID && bytes.contentEquals(mImageBytes)
        })
        verify(mFactory.imageProps).configureToMemory(argThat { true })
        verify(mFactory.imageProps).configureToView(mImageBytes)
        verify(mListener).insertImage(mImageBytes)
    }

    @Test
    fun notCallInsertImageIfItNotFound() {
        whenever(mFactory.repository.getArticleImageByArticleId(ARTICLE_ID)).thenReturn(null)
        whenever(mFactory.repository.getArticleById(ARTICLE_ID)).thenReturn(null)

        mUseCase.start()

        verify(mListener).processStart()
        verify(mListener).processEnd()
        verifyNoMoreInteractions(mListener)
    }
}