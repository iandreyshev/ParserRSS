package ru.iandreyshev.parserrss.presentation.presenter

import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.interactor.RssInfoInteractor
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast
import ru.iandreyshev.parserrss.presentation.view.IRssInfoView

@InjectViewState
class RssInfoPresenter(private val rss: ViewRss) : MvpPresenter<IRssInfoView>() {

    val interactor = RssInfoInteractor(RssInfoInteractorOutput())

    override fun onFirstViewAttach() {
        interactor.rss = rss
    }

    private inner class RssInfoInteractorOutput : RssInfoInteractor.IOutputPort {
        override fun setInfo(title: String?, description: String?) {
            viewState.setInfo(title, description)
        }

        override fun openOriginal(uri: Uri) = viewState.openInBrowser(uri)

        override fun showMessage(messageId: Int) = toast(messageId)

        override fun close() = viewState.close()
    }
}
