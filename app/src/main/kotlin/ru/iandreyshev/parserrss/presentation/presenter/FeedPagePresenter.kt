package ru.iandreyshev.parserrss.presentation.presenter

import android.graphics.Bitmap
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.factory.useCase.UseCaseFactory

import ru.iandreyshev.parserrss.models.interactor.FeedPageInteractor
import ru.iandreyshev.parserrss.models.viewModels.ViewArticle
import ru.iandreyshev.parserrss.models.useCase.rssList.UpdateRssUseCase
import ru.iandreyshev.parserrss.models.counter.ProcessCounter
import ru.iandreyshev.parserrss.models.useCase.rssList.LoadArticleImageToFeedItemUseCase
import ru.iandreyshev.parserrss.models.useCase.rssList.LoadArticlesListUseCase
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast
import ru.iandreyshev.parserrss.presentation.presenter.extention.uiThread
import ru.iandreyshev.parserrss.presentation.view.IFeedPageView
import ru.iandreyshev.parserrss.ui.adapter.IItemIcon

@InjectViewState
class FeedPagePresenter(rssId: Long) : MvpPresenter<IFeedPageView>() {

    private val mProcessCounter = ProcessCounter(this::onChangeProcessCount)
    val interactor = FeedPageInteractor(UseCaseFactory(), UseCaseListener(), rssId)

    private inner class UseCaseListener : UpdateRssUseCase.IListener,
            LoadArticleImageToFeedItemUseCase.IListener,
            LoadArticlesListUseCase.IListener {

        override fun connectionError(requestResult: IHttpRequestResult) = uiThread {
            when (requestResult.state) {
                HttpRequestHandler.State.BAD_URL -> {
                    toast(R.string.toast_invalid_url)
                }
                HttpRequestHandler.State.PERMISSION_DENIED -> {
                    viewState.openInternetPermissionDialog()
                }
                else -> toast(R.string.toast_bad_connection)
            }
        }

        override fun parseError() = uiThread {
            toast(R.string.toast_invalid_rss_format)
        }

        override fun rssNotExist() = uiThread {
            toast(R.string.toast_rss_not_exist)
        }

        override fun updateRss(articles: ArrayList<ViewArticle>) = uiThread {
            viewState.setArticles(articles)
            viewState.openEmptyContentMessage(articles.isEmpty())
            viewState.updateImages()
        }

        override fun loadArticles(articles: ArrayList<ViewArticle>) {
            updateRss(articles)
        }

        override fun insertImage(icon: IItemIcon, idOnStart: Long, imageBitmap: Bitmap) = uiThread {
            if (idOnStart == icon.id) {
                icon.updateImage(imageBitmap)
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
        viewState.startUpdate(newCount > 0)
    }
}
