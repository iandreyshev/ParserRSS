package ru.iandreyshev.parserrss.models.useCase

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import ru.iandreyshev.parserrss.MocksFactory
import ru.iandreyshev.parserrss.models.repository.Rss

@RunWith(RobolectricTestRunner::class)
class LoadAllRssUseCaseTest {

    private lateinit var mFactory: MocksFactory
    private lateinit var mListener: LoadAllRssUseCase.IListener
    private lateinit var mUseCase: LoadAllRssUseCase

    @Before
    fun setup() {
        mFactory = MocksFactory()
        mListener = mock()
        mUseCase = createUseCase()
    }

    @Test
    fun callProcessMethods() {
        fun verify(rssCount: Int) {
            whenever(mFactory.repository.rssIdList).thenReturn(LongArray(rssCount))
            whenever(mFactory.repository.getRssById(any())).thenReturn(Rss())

            createUseCase().execute().get()

            verify(mListener).processStart()
            verify(mListener).processEnd()
            clearInvocations(mListener)
        }

        verify(0)
        verify(10)
    }

    @Test
    fun callUpdateCapacityMethod() {
        fun verify(rssCount: Int, isFull: Boolean) {
            whenever(mFactory.repository.isFull).thenReturn(isFull)
            whenever(mFactory.repository.rssIdList).thenReturn(LongArray(rssCount))
            whenever(mFactory.repository.getRssById(any())).thenReturn(null)

            createUseCase().execute().get()

            verify(mListener).updateCapacityAfterLoad(isFull)
        }

        verify(0, false)
        verify(1, false)
    }

    @Test
    fun callLoadMethodIfRssExist() {
        val rssCount = 10
        val rss = Rss(id = 10)

        whenever(mFactory.repository.isFull).thenReturn(false)
        whenever(mFactory.repository.rssIdList).thenReturn(LongArray(rssCount))
        whenever(mFactory.repository.getRssById(any())).thenReturn(rss)

        mUseCase.execute().get()

        verify(mListener, times(rssCount)).loadRss(argThat { id == rss.id })
    }

    private fun createUseCase(): LoadAllRssUseCase {
        return LoadAllRssUseCase(mFactory.repository, mFactory.articleFilter, mListener)
    }
}
