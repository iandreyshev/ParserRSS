package ru.iandreyshev.parserrss.presentation.presenter

import android.net.Uri
import ru.iandreyshev.parserrss.presentation.view.IArticleView

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.interactor.ArticleInteractor
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast

@InjectViewState
class ArticlePresenter(private val articleId: Long) : MvpPresenter<IArticleView>() {

    val interactor = ArticleInteractor(ArticleInteractorOutput())

    override fun onFirstViewAttach() = interactor.initArticle(articleId)

    private inner class ArticleInteractorOutput : ArticleInteractor.IOutputPort {
        override fun initArticle(rss: ViewRss, article: ViewArticle) {
            viewState.initArticle(rss, article)
        }

        override fun openOriginal(url: Uri) = viewState.openInBrowser(url)

        override fun showMessage(messageId: Int) = toast(messageId)

        override fun close() = viewState.closeArticle()
    }
}
