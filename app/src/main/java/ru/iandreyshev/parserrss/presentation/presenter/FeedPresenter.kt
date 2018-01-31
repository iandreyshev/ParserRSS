package ru.iandreyshev.parserrss.presentation.presenter

import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.view.IFeedView

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.interactor.FeedInteractor

@InjectViewState
class FeedPresenter : MvpPresenter<IFeedView>() {

    private val interactor = FeedInteractor(FeedInteractorOutput())

    fun onInsertRss(url: String) = interactor.insertRss(url)

    fun onDeleteRss(rss: ViewRss?) = interactor.deleteRss(rss)

    fun onOpenArticle(articleId: Long) = viewState.openArticle(articleId)

    fun onOpenRssInfo(rss: ViewRss?) = interactor.openRssInfo(rss)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setToolbarScrollable(false)
        viewState.startProgressBar(false)
        interactor.loadFromDatabase()
    }

    private inner class FeedInteractorOutput : FeedInteractor.IOutput {
        override fun insertRss(rss: ViewRss) = viewState.insertRss(rss)

        override fun removeRss(rss: ViewRss) = viewState.removeRss(rss)

        override fun showMessage(messageId: Int) = viewState.showShortToast(App.getStr(messageId))

        override fun openRssInfo(rss: ViewRss) = viewState.openRssInfo(rss)

        override fun onChangeRssCount(newCount: Int) {
            viewState.setToolbarScrollable(newCount > 0)
            viewState.openContentMessage(newCount <= 0, App.getStr(R.string.feed_list_empty))
        }

        override fun onChangeProcessCount(newCount: Int) = viewState.startProgressBar(newCount > 0)
    }
}
