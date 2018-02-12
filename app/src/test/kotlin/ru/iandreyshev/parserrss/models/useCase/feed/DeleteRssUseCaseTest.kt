package ru.iandreyshev.parserrss.models.useCase.feed

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import ru.iandreyshev.parserrss.firstArgAsFun
import ru.iandreyshev.parserrss.models.repository.IRepository

class DeleteRssUseCaseTest {

    companion object {
        private const val ID: Long = 0
    }

    private lateinit var mUseCase: DeleteRssUseCase
    private lateinit var mRepository: IRepository
    private lateinit var mListener: DeleteRssUseCase.IListener

    @Before
    fun setup() {
        mRepository = mock()
        mListener = mock()
        mUseCase = DeleteRssUseCase(mRepository, ID, mListener)
    }

    @Test
    fun callFailedIfRepositoryReturnFalse() {
        whenever(mRepository.runInTx(any())).then { it.firstArgAsFun.invoke() }
        whenever(mRepository.removeRssById(ID)).thenReturn(false)

        mUseCase.start()

        verify(mListener).removingRssFailed()
    }

    @Test
    fun callRemoveRssIfRepositoryReturnTrue() {
        whenever(mRepository.runInTx(any())).then { it.firstArgAsFun.invoke() }
        whenever(mRepository.removeRssById(ID)).thenReturn(true)

        mUseCase.start()

        verify(mListener).removeRss(ID)
    }
}