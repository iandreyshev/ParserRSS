package ru.iandreyshev.parserrss.presentation.presenter

import android.net.Uri
import ru.iandreyshev.parserrss.presentation.view.IArticleView

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.interactor.ArticleInteractor
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast

@InjectViewState
class ArticlePresenter(private val articleId: Long) : MvpPresenter<IArticleView>() {

    private val interactor = ArticleInteractor(ArticleInteractorOutput())

    fun onOpenOriginal() = interactor.openOriginal()

    override fun onFirstViewAttach() = interactor.initArticle(articleId)

    private inner class ArticleInteractorOutput : ArticleInteractor.IOutput {
        override fun initArticle(rss: ViewRss, article: ViewArticle) = viewState.initArticle(rss, article)

        override fun openOriginal(url: String?) {
            try {
                Uri.parse(url)?.let {
                    viewState.openInBrowser(it)
                    return
                }
                toast(R.string.toast_invalid_url)
            } catch (ex: Exception) {
                toast(R.string.toast_invalid_url)
            }
        }

        override fun showMessage(messageId: Int) = toast(messageId)

        override fun close() = viewState.closeArticle()
    }
}
