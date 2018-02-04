package ru.iandreyshev.parserrss.presentation.presenter

import android.graphics.Bitmap
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

import ru.iandreyshev.parserrss.interactor.FeedPageInteractor
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast
import ru.iandreyshev.parserrss.presentation.view.IFeedPageView
import ru.iandreyshev.parserrss.ui.adapter.IItemIcon

@InjectViewState
class FeedPagePresenter(rss: ViewRss) : MvpPresenter<IFeedPageView>() {

    val interactor = FeedPageInteractor(FeedPageInteractorOutput(), rss)

    private inner class FeedPageInteractorOutput : FeedPageInteractor.IOutputPort {
        override fun openInternetPermissionDialog() {
            viewState.openInternetPermissionDialog()
        }

        override fun insertImage(item: IItemIcon, imageBitmap: Bitmap) {
            item.updateImage(imageBitmap)
        }

        override fun showMessage(messageId: Int) = toast(messageId)

        override fun onChangeProcessCount(newCount: Int) {
            viewState.startUpdate(newCount > 0)
        }

        override fun setArticles(articles: List<ViewArticle>) {
            viewState.setArticles(articles)
            viewState.openEmptyContentMessage(articles.isEmpty())
            viewState.updateImages()
        }

        override fun updateArticles(articles: List<ViewArticle>) {
            viewState.setArticles(articles)
            viewState.openEmptyContentMessage(articles.isEmpty())
            viewState.updateImages()
        }
    }
}
