package ru.iandreyshev.parserrss.presentation.presenter

import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.view.IFeedView

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.interactor.FeedInteractor
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast

@InjectViewState
class FeedPresenter : MvpPresenter<IFeedView>() {

    val interactor = FeedInteractor(FeedInteractorOutput())

    override fun onFirstViewAttach() = interactor.onLoadFromDatabase()

    private inner class FeedInteractorOutput : FeedInteractor.IOutputPort {
        override fun openArticle(articleId: Long) = viewState.openArticle(articleId)

        override fun insertRss(rss: ViewRss) = viewState.insertRss(rss)

        override fun removeRss(rss: ViewRss) = viewState.removeRss(rss)

        override fun showMessage(messageId: Int) = viewState.showToast(App.getStr(messageId))

        override fun openRssInfo(rss: ViewRss) = viewState.openRssInfo(rss)

        override fun onChangeRssCount(newCount: Int, isFull: Boolean) {
            viewState.setToolbarScrollable(newCount > 0)
            viewState.openEmptyContentMessage(newCount <= 0)
            viewState.openToolbarTitle(newCount <= 0)

            viewState.enableAddButton(!isFull)
            viewState.enableDeleteButton(newCount > 0)
            viewState.enableInfoButton(newCount > 0)

            if (isFull) {
                toast(R.string.feed_is_full)
            }
        }

        override fun onChangeProcessCount(newCount: Int) {
            viewState.startProgressBar(newCount > 0)
        }

        override fun openInternetPermissionDialog() = viewState.openInternetPermissionDialog()
    }
}
