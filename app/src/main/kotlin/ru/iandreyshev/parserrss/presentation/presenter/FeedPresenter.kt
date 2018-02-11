package ru.iandreyshev.parserrss.presentation.presenter

import ru.iandreyshev.parserrss.models.viewModels.ViewRss
import ru.iandreyshev.parserrss.presentation.view.IFeedView

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.factory.useCase.UseCaseFactory
import ru.iandreyshev.parserrss.models.interactor.FeedInteractor
import ru.iandreyshev.parserrss.models.counter.ProcessCounter
import ru.iandreyshev.parserrss.models.useCase.feed.OpenArticleUseCase
import ru.iandreyshev.parserrss.models.useCase.feed.DeleteRssUseCase
import ru.iandreyshev.parserrss.models.useCase.feed.InsertRssUseCase
import ru.iandreyshev.parserrss.models.useCase.feed.LoadAllRssUseCase
import ru.iandreyshev.parserrss.models.useCase.feed.OpenRssInfoUseCase
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult
import ru.iandreyshev.parserrss.presentation.presenter.extention.addRssDialog
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast
import ru.iandreyshev.parserrss.presentation.presenter.extention.uiThread
import ru.iandreyshev.parserrss.ui.fragment.InternetPermissionDialog
import ru.iandreyshev.parserrss.ui.fragment.RssInfoDialog

@InjectViewState
class FeedPresenter : MvpPresenter<IFeedView>() {

    private val mProcessCounter = ProcessCounter(this::onChangeProcessCount)
    private val mRssCounter = ProcessCounter(this::onChangeRssCount)

    val interactor = FeedInteractor(UseCaseFactory(), UseCaseListener())

    private inner class UseCaseListener : LoadAllRssUseCase.IListener,
            DeleteRssUseCase.IListener,
            OpenArticleUseCase.IListener,
            InsertRssUseCase.IListener,
            OpenRssInfoUseCase.IListener {

        override fun loadRss(rss: ViewRss) = uiThread {
            viewState.insertRss(rss)
            mRssCounter.add()
        }

        override fun insertNewRss(rss: ViewRss, isFull: Boolean) = uiThread {
            viewState.insertRss(rss)
            viewState.openRssPage(rss.id)
            mRssCounter.add()
            onChangeCapacityStatus(isFull)
        }

        override fun rssCountIsMax() = uiThread {
            onChangeCapacityStatus(true)
        }

        override fun removeRss(id: Long) = uiThread {
            viewState.removeRss(id)
            mRssCounter.remove()
            onChangeCapacityStatus(false)
        }

        override fun openArticle(articleId: Long) = uiThread {
            viewState.openArticle(articleId)
        }

        override fun rssAlreadyExist() = uiThread {
            toast(R.string.toast_rss_already_exist)
        }

        override fun connectionError(requestResult: IHttpRequestResult) = uiThread {
            when (requestResult.state) {
                HttpRequestHandler.State.BAD_URL -> {
                    toast(R.string.toast_invalid_url)
                    viewState.openDialog(addRssDialog(requestResult.urlString))
                }
                HttpRequestHandler.State.PERMISSION_DENIED -> {
                    toast(R.string.toast_internet_permission_denied)
                    viewState.openDialog(InternetPermissionDialog())
                }
                else -> {
                    toast(R.string.toast_bad_connection)
                    viewState.openDialog(addRssDialog(requestResult.urlString))
                }
            }
        }

        override fun invalidRssFormat(url: String) = uiThread {
            toast(R.string.toast_invalid_rss_format)
            viewState.openDialog(addRssDialog(url))
        }

        override fun updateCapacityAfterLoad(isFull: Boolean) = uiThread {
            onChangeCapacityStatus(isFull)
        }

        override fun removingRssFailed() = uiThread {
            toast(R.string.feed_deleting_error)
        }

        override fun urlToAddRssIsEmpty() = uiThread {
            toast(R.string.feed_url_to_add_is_empty)
        }

        override fun openRssInfo(rss: ViewRss?) = uiThread {
            if (rss != null) {
                viewState.openDialog(RssInfoDialog.newInstance(rss))
            } else {
                toast(R.string.toast_open_info_fail)
            }
        }

        override fun processStart() = uiThread {
            mProcessCounter.add()
        }

        override fun processEnd() = uiThread {
            mProcessCounter.remove()
        }
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
