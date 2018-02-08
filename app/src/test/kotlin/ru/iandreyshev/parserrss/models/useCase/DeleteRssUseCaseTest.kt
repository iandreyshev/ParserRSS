package ru.iandreyshev.parserrss.models.useCase

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.iandreyshev.parserrss.models.repository.IRepository

@RunWith(RobolectricTestRunner::class)
@Config(manifest = "src/main/AndroidManifest.xml")
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
        whenever(mRepository.removeRssById(ID)).thenReturn(false)
        mUseCase.execute().get()
        verify(mListener).removingRssFailed()
    }

    @Test
    fun callRemoveRssIfRepositoryReturnTrue() {
        whenever(mRepository.removeRssById(ID)).thenReturn(true)
        mUseCase.execute().get()
        verify(mListener).removeRss(ID)
    }
}
