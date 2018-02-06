package ru.iandreyshev.parserrss.presentation.presenter

import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.view.IFeedView

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.factory.useCase.IUseCaseFactory
import ru.iandreyshev.parserrss.interactor.FeedInteractor
import ru.iandreyshev.parserrss.models.useCase.*
import ru.iandreyshev.parserrss.models.counter.ProcessCounter
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast

@InjectViewState
class FeedPresenter(useCaseFactory: IUseCaseFactory) : MvpPresenter<IFeedView>() {

    private val mProcessCounter = ProcessCounter(this::onChangeProcessCount)
    private val mRssCounter = ProcessCounter(this::onChangeRssCount)

    val interactor = FeedInteractor(useCaseFactory, UseCaseListener())

    private inner class UseCaseListener : LoadAllRssUseCase.IListener,
            DeleteRssUseCase.IListener,
            OpenArticleUseCase.IListener,
            InsertRssUseCase.IListener,
            OpenRssInfoUseCase.IListener {

        override fun loadRss(rss: ViewRss) {
            viewState.insertRss(rss)
            mRssCounter.add()
        }

        override fun insertNewRss(rss: ViewRss, isFull: Boolean) {
            viewState.insertRss(rss)
            viewState.openRssPage(rss.id)
            mRssCounter.add()
            onChangeCapacityStatus(isFull)
        }

        override fun rssCountIsMax() {
            onChangeCapacityStatus(true)
        }

        override fun removeRss(id: Long) {
            viewState.removeRss(id)
            mRssCounter.remove()
            onChangeCapacityStatus(false)
        }

        override fun openArticle(articleId: Long) {
            viewState.openArticle(articleId)
        }

        override fun rssAlreadyExist() {
            toast(R.string.toast_rss_already_exist)
        }

        override fun invalidRssUrl() {
            toast(R.string.toast_invalid_url)
            viewState.openAddingRssDialog()
        }

        override fun connectionError(requestResult: IHttpRequestResult) {
            when (requestResult.state) {
                HttpRequestHandler.State.PERMISSION_DENIED -> viewState.openInternetPermissionDialog()
                else -> toast(R.string.toast_bad_connection)
            }
        }

        override fun invalidRssFormat() {
            toast(R.string.toast_invalid_rss_format)
            viewState.openAddingRssDialog()
        }

        override fun updateCapacityBeforeLoad(isFull: Boolean) {
            onChangeCapacityStatus(isFull)
        }

        override fun openRssInfo(rss: ViewRss) = viewState.openRssInfo(rss)

        override fun processStart() = mProcessCounter.add()

        override fun processEnd() = mProcessCounter.remove()
    }

    private fun onChangeProcessCount(newCount: Int) {
        viewState.startProgressBar(newCount > 0)
    }

    private fun onChangeRssCount(newCount: Int) {
        viewState.setToolbarScrollable(newCount > 0)
        viewState.openEmptyContentMessage(newCount <= 0)
        viewState.openToolbarTitle(newCount <= 0)

        viewState.enableDeleteButton(newCount > 0)
        viewState.enableInfoButton(newCount > 0)
    }

    private fun onChangeCapacityStatus(isFull: Boolean) {
        viewState.enableAddButton(!isFull)
        if (isFull) {
            toast(R.string.feed_is_full)
        }
    }
}
