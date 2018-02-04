package ru.iandreyshev.parserrss.presentation.presenter

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import ru.iandreyshev.parserrss.presentation.view.IArticleView

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.factory.UseCaseFactory
import ru.iandreyshev.parserrss.interactor.ArticleInteractor
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.useCase.LoadArticleUseCase
import ru.iandreyshev.parserrss.models.useCase.LoadImageToActivityUseCase
import ru.iandreyshev.parserrss.models.useCase.OpenOriginalUseCase
import ru.iandreyshev.parserrss.models.util.ProcessCounter
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast

@InjectViewState
class ArticlePresenter(articleId: Long) : MvpPresenter<IArticleView>() {

    private val mProcessCounter = ProcessCounter(this::onProcessCountChange)
    val interactor = ArticleInteractor(UseCaseFactory, UseCasesListener(), articleId)

    private inner class UseCasesListener : LoadImageToActivityUseCase.IListener,
            LoadArticleUseCase.IListener,
            OpenOriginalUseCase.IListener,
            IPresenter {

        override fun openOriginal(path: Uri) {
            viewState.startActivity(Intent(Intent.ACTION_VIEW, path))
        }

        override fun openOriginalFail() {
            toast(R.string.toast_invalid_url)
        }

        override fun initArticle(rss: ViewRss, article: ViewArticle) {
            viewState.initArticle(rss, article)
        }

        override fun initArticleFail() {
            viewState.closeArticle()
        }

        override fun insertImage(imageBitmap: Bitmap) {
            viewState.setImage(imageBitmap)
        }

        override fun processStart() = mProcessCounter.startProcess()

        override fun processEnd() = mProcessCounter.endProcess()
    }

    private fun onProcessCountChange(newCount: Int) {
        viewState.startProgressBar(newCount > 0)
    }
}
