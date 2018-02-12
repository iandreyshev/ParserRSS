package ru.iandreyshev.parserrss.models.useCase.article

import android.graphics.Bitmap
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.iandreyshev.parserrss.MocksFactory
import ru.iandreyshev.parserrss.models.repository.Article
import ru.iandreyshev.parserrss.models.repository.ArticleImage
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class LoadArticleImageUseCaseTest {

    companion object {
        private const val ARTICLE_ID = 0L
        private val IMAGE_BITMAP = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8)
    }

    private lateinit var mListener: LoadArticleImageUseCase.IListener
    private lateinit var mFactory: MocksFactory
    private lateinit var mImage: ArticleImage
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
        mImage = ArticleImage(articleId = ARTICLE_ID, bitmap = IMAGE_BITMAP)
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
        whenever(mFactory.imageProps.configureToView(IMAGE_BITMAP)).thenReturn(IMAGE_BITMAP)

        mUseCase.start()

        verify(mListener).insertImage(IMAGE_BITMAP)
    }

    @Test
    fun callInsertImageIfTakeItFromNet() {
        val imageUrl = "Image.url"
        val article = Article(imageUrl = imageUrl)

        whenever(mFactory.repository.getArticleImageByArticleId(ARTICLE_ID)).thenReturn(null)
        whenever(mFactory.repository.getArticleById(ARTICLE_ID)).thenReturn(article)
        whenever(mFactory.requestHandler.send(imageUrl)).thenReturn(HttpRequestHandler.State.SUCCESS)
        whenever(mFactory.requestHandler.body).thenReturn(ByteArray(0))
        whenever(mFactory.imageProps.configureToMemory(any())).thenReturn(IMAGE_BITMAP)
        whenever(mFactory.imageProps.configureToView(any())).thenReturn(IMAGE_BITMAP)

        mUseCase.start()

        verify(mFactory.repository).putArticleImageIfArticleExist(ARTICLE_ID, IMAGE_BITMAP)
        verify(mFactory.imageProps).configureToMemory(argThat { true })
        verify(mFactory.imageProps).configureToView(IMAGE_BITMAP)
        verify(mListener).insertImage(IMAGE_BITMAP)
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