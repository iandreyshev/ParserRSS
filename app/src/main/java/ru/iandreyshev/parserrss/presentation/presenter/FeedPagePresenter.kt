package ru.iandreyshev.parserrss.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

import ru.iandreyshev.parserrss.interactor.FeedPageInteractor
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast
import ru.iandreyshev.parserrss.presentation.view.IFeedPageView

@InjectViewState
class FeedPagePresenter(private val rss: ViewRss) : MvpPresenter<IFeedPageView>() {

    val interactor = FeedPageInteractor(FeedPageInteractorOutput())

    override fun onFirstViewAttach() {
        interactor.rss = rss
    }

    private inner class FeedPageInteractorOutput : FeedPageInteractor.IOutputPort {
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
