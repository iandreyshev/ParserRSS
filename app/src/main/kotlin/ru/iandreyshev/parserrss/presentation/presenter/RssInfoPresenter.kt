package ru.iandreyshev.parserrss.presentation.presenter

import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.factory.useCase.UseCaseFactory
import ru.iandreyshev.parserrss.models.interactor.RssInfoInteractor
import ru.iandreyshev.parserrss.models.viewModels.ViewRss
import ru.iandreyshev.parserrss.models.useCase.rssInfo.OpenRssOriginalUseCase
import ru.iandreyshev.parserrss.models.useCase.rssInfo.LoadRssInfoUseCase
import ru.iandreyshev.parserrss.presentation.presenter.extention.openInBrowser
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast
import ru.iandreyshev.parserrss.presentation.presenter.extention.uiThread
import ru.iandreyshev.parserrss.presentation.view.IRssInfoView

@InjectViewState
class RssInfoPresenter(rss: ViewRss) : MvpPresenter<IRssInfoView>() {

    val interactor = RssInfoInteractor(UseCaseFactory, rss, UseCaseListener())

    private inner class UseCaseListener : OpenRssOriginalUseCase.IListener,
            LoadRssInfoUseCase.IListener {

        override fun loadData(title: String?, description: String?) {
            if (title != null) {
                viewState.loadData(title, description)
                return
            }

            toast(R.string.feed_open_info_error)
            viewState.close()
        }

        override fun enableOpenOriginalButton(isEnabled: Boolean) {
            viewState.setOpenOriginalEnabled(isEnabled)
        }

        override fun openOriginal(path: Uri?) = uiThread {
            if (path != null) {
                openInBrowser(path)
            } else {
                toast(R.string.toast_invalid_url)
            }
        }
    }
}
