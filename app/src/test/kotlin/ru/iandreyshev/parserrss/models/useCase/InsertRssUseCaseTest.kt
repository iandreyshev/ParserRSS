package ru.iandreyshev.parserrss.models.useCase

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import ru.iandreyshev.parserrss.MocksFactory

@RunWith(RobolectricTestRunner::class)
class InsertRssUseCaseTest {

    companion object {
        private const val INVALID_URL = ""
    }

    private lateinit var mFactory: MocksFactory
    private lateinit var mListener: InsertRssUseCase.IListener
    private lateinit var mUseCase: InsertRssUseCase

    @Before
    fun setup() {
        mFactory = MocksFactory()
        mListener = mock(InsertRssUseCase.IListener::class.java)
        mUseCase = InsertRssUseCase(
                mFactory.repository,
                mFactory.requestHandler,
                INVALID_URL,
                mFactory.articleFilter,
                mListener)
    }

    @Test
    fun callCountIsMaxIfRepositoryIsFull() {
        `when`(mFactory.repository.isFull).thenReturn(true)
        startUseCase()

        verifyStartEndProcess()
        verify(mListener).rssCountIsMax()
        verifyNoMoreInteractions(mListener)
        verifyZeroInteractions(mFactory.requestHandler)
    }

    @Test
    fun callRssAlreadyExistBeforeLoad() {
        `when`(mFactory.repository.isRssWithUrlExist(INVALID_URL)).thenReturn(true)
        startUseCase()

        verifyStartEndProcess()
        verify(mListener).rssAlreadyExist()
        verifyNoMoreInteractions(mListener)
        verifyZeroInteractions(mFactory.requestHandler)
    }

    private fun startUseCase() {
        mUseCase.execute().get()
    }

    private fun verifyStartEndProcess() {
        verify(mListener).processStart()
        verify(mListener).processEnd()
    }
}
