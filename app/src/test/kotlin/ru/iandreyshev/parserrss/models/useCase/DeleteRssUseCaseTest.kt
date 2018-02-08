package ru.iandreyshev.parserrss.models.useCase

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import ru.iandreyshev.parserrss.models.repository.IRepository

@RunWith(RobolectricTestRunner::class)
class DeleteRssUseCaseTest {

    companion object {
        private const val ID: Long = 0
    }

    private lateinit var mUseCase: DeleteRssUseCase
    private lateinit var mRepository: IRepository
    private lateinit var mListener: DeleteRssUseCase.IListener

    @Before
    fun setup() {
        mRepository = mock(IRepository::class.java)
        mListener = mock(DeleteRssUseCase.IListener::class.java)
        mUseCase = DeleteRssUseCase(mRepository, ID, mListener)
    }

    @Test
    fun callFailedIfRepositoryReturnFalse() {
        `when`(mRepository.removeRssById(ID)).thenReturn(false)
        mUseCase.execute().get()
        verify(mListener).removingRssFailed()
    }

    @Test
    fun callRemoveRssIfRepositoryReturnTrue() {
        `when`(mRepository.removeRssById(ID)).thenReturn(true)
        mUseCase.execute().get()
        verify(mListener).removeRss(ID)
    }
}
