package ru.iandreyshev.parserrss.presentation.presenter

import ru.iandreyshev.parserrss.presentation.view.IArticleView

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.factory.useCase.UseCaseFactory
import ru.iandreyshev.parserrss.models.interactor.ArticleInteractor
import ru.iandreyshev.parserrss.models.rss.Article
import ru.iandreyshev.parserrss.models.rss.Rss
import ru.iandreyshev.parserrss.models.useCase.article.LoadArticleUseCase
import ru.iandreyshev.parserrss.models.useCase.article.LoadArticleImageUseCase
import ru.iandreyshev.parserrss.models.useCase.article.OpenArticleOriginalUseCase
import ru.iandreyshev.parserrss.models.counter.ProcessCounter
import ru.iandreyshev.parserrss.models.extention.bitmap
import ru.iandreyshev.parserrss.models.extention.uri
import ru.iandreyshev.parserrss.presentation.presenter.extention.openInBrowser
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast
import ru.iandreyshev.parserrss.presentation.presenter.extention.uiThread

@InjectViewState
class ArticlePresenter(articleId: Long) : MvpPresenter<IArticleView>() {

    private val mProcessCounter = ProcessCounter(this::onProcessCountChange)
    val interactor = ArticleInteractor(UseCaseFactory, UseCasesListener(), articleId)

    private inner class UseCasesListener : LoadArticleImageUseCase.IListener,
            LoadArticleUseCase.IListener,
            OpenArticleOriginalUseCase.IListener {

        override fun loadArticle(rss: Rss?, article: Article?) = uiThread {
            if (rss != null && article != null) {
                viewState.initArticle(rss, article)
            } else {
                toast(R.string.article_error_load)
                viewState.closeArticle()
            }
        }

        override fun openOriginal(addressString: String?) {
            val address = addressString?.uri
            uiThread {
                if (address != null) {
                    openInBrowser(address)
                } else {
                    toast(R.string.toast_invalid_url)
                }
            }
        }

        override fun insertImage(imageBytes: ByteArray) {
            val imageBitmap = imageBytes.bitmap
            uiThread {
                viewState.setImage(imageBitmap)
            }
        }

        override fun processStart() = uiThread {
            mProcessCounter.add()
        }

        override fun processEnd() = uiThread {
            mProcessCounter.remove()
        }
    }

    private fun onProcessCountChange(newCount: Int) {
        viewState.startProgressBar(newCount > 0)
    }
}
