package ru.iandreyshev.parserrss.presentation.presenter

import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.async.GetArticleFromDbTask
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.view.IArticleView

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

@InjectViewState
class ArticlePresenter (private val articleId: Long) : MvpPresenter<IArticleView>() {
    override fun onFirstViewAttach() {
        GetArticleFromDbTask.execute(articleId, GetArticleFromDbListener())
    }

    private inner class GetArticleFromDbListener : GetArticleFromDbTask.IEventListener {
        override fun onPreExecute() {}

        override fun onPostExecute(result: Void?) {}

        override fun onSuccess(rss: ViewRss, article: ViewArticle) {
            viewState.initArticle(rss, article)
        }

        override fun onFail() {
            viewState.showShortToast(App.getStr(R.string.article_error_load))
            viewState.closeArticle()
        }
    }
}
