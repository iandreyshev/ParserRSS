package ru.iandreyshev.parserrss.models.useCase

import android.graphics.Bitmap
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.iandreyshev.parserrss.MocksFactory

@RunWith(RobolectricTestRunner::class)
@Config(manifest = "src/main/AndroidManifest.xml")
class LoadArticleImageToFeedItemUseCaseTest {

    companion object {
        private const val ARTICLE_ID = 0L
        private val IMAGE = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8)
    }

    private lateinit var mFactory: MocksFactory
    private lateinit var mListener: IUseCaseListener

    @Before
    fun setup() {
        mFactory = MocksFactory()
        mListener = mock()
    }

    @Test
    fun notCallProcessMethods() {
        whenever(mFactory.itemIcon.isUpdateStart).thenReturn(true)

        createUseCase().execute().get()

        verifyZeroInteractions(mListener)
    }

    @Test
    fun configureToViewAndReturnIfFindInRepository() {
        whenever(mFactory.itemIcon.id).thenReturn(ARTICLE_ID)
        whenever(mFactory.itemIcon.isUpdateStart).thenReturn(false)
        whenever(mFactory.repository.getArticleImageBitmapByArticleId(ARTICLE_ID)).thenReturn(IMAGE)
        whenever(mFactory.imageProps.configureToView(IMAGE)).thenReturn(IMAGE)

        createUseCase().execute().get()

        verify(mFactory.itemIcon).updateImage(IMAGE)
        verify(mFactory.imageProps).configureToView(IMAGE)
    }

    private fun createUseCase(): LoadArticleImageToFeedItemUseCase {
        return LoadArticleImageToFeedItemUseCase(
                mFactory.repository,
                mFactory.requestHandler,
                mFactory.imageProps,
                mFactory.itemIcon,
                mListener
        )
    }
}