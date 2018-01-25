package ru.iandreyshev.parserrss.presentation.presenter

import android.net.Uri
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.async.GetArticleFromDbTask
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.view.IArticleView

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast

@InjectViewState
class ArticlePresenter (private val articleId: Long) : MvpPresenter<IArticleView>() {
    var articleUrl: String? = null

    override fun onFirstViewAttach() {
        GetArticleFromDbTask.execute(articleId, GetArticleFromDbListener())
    }

    fun onOpenOriginal() = viewState.openInBrowser(Uri.parse(articleUrl))

    private inner class GetArticleFromDbListener : GetArticleFromDbTask.IEventListener {
        override fun onSuccess(rss: ViewRss, article: ViewArticle) {
            this@ArticlePresenter.articleUrl = article.originUrl
            viewState.initArticle(rss, article)
        }

        override fun onFail() {
            toast(R.string.article_error_load)
            viewState.closeArticle()
        }
    }
}
