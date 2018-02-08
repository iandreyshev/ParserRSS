package ru.iandreyshev.parserrss.presentation.presenter

import android.graphics.Bitmap
import android.net.Uri
import ru.iandreyshev.parserrss.presentation.view.IArticleView

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.factory.useCase.UseCaseFactory
import ru.iandreyshev.parserrss.models.interactor.ArticleInteractor
import ru.iandreyshev.parserrss.models.viewModels.ViewArticle
import ru.iandreyshev.parserrss.models.viewModels.ViewRss
import ru.iandreyshev.parserrss.models.useCase.LoadArticleUseCase
import ru.iandreyshev.parserrss.models.useCase.LoadArticleImageUseCase
import ru.iandreyshev.parserrss.models.useCase.OpenArticleOriginalUseCase
import ru.iandreyshev.parserrss.models.counter.ProcessCounter
import ru.iandreyshev.parserrss.presentation.presenter.extention.openInBrowser
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast

@InjectViewState
class ArticlePresenter(articleId: Long) : MvpPresenter<IArticleView>() {

    private val mProcessCounter = ProcessCounter(this::onProcessCountChange)
    val interactor = ArticleInteractor(UseCaseFactory, UseCasesListener(), articleId)

    private inner class UseCasesListener : LoadArticleImageUseCase.IListener,
            LoadArticleUseCase.IListener,
            OpenArticleOriginalUseCase.IListener {

        override fun loadArticle(rss: ViewRss?, article: ViewArticle?) {
            if (rss != null && article != null) {
                viewState.initArticle(rss, article)
            } else {
                toast(R.string.article_error_load)
                viewState.closeArticle()
            }
        }

        override fun openOriginal(path: Uri?) {
            if (path != null) {
                openInBrowser(path)
            } else {
                toast(R.string.toast_invalid_url)
            }
        }

        override fun insertImage(imageBitmap: Bitmap) {
            viewState.setImage(imageBitmap)
        }

        override fun processStart() = mProcessCounter.add()

        override fun processEnd() = mProcessCounter.remove()
    }

    private fun onProcessCountChange(newCount: Int) {
        viewState.startProgressBar(newCount > 0)
    }
}
