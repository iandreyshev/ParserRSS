package ru.iandreyshev.parserrss.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.interactor.FeedPageInteractor
import ru.iandreyshev.parserrss.models.async.UpdateRssFromNetTask
import ru.iandreyshev.parserrss.models.filters.FilterByDate
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast
import ru.iandreyshev.parserrss.presentation.view.IFeedPageView

@InjectViewState
class FeedPagePresenter(private val rss: ViewRss) : MvpPresenter<IFeedPageView>() {

    private val interactor = FeedPageInteractor(FeedPageInteractorOutput())

    fun onUpdate() = interactor.update(rss, FilterByDate.newInstance())

    override fun onFirstViewAttach() = viewState.setArticles(rss.articles)

    private inner class FeedPageInteractorOutput : FeedPageInteractor.IOutput {
        override fun showMessage(messageId: Int) = toast(messageId)

        override fun onChangeProcessCount(newCount: Int) {
            viewState.startUpdate(newCount > 0)
        }

        override fun setArticles(articles: List<ViewArticle>) {
            viewState.setArticles(articles)
            viewState.updateImages(true)
        }
    }
}
