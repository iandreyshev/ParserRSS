package ru.iandreyshev.parserrss.presentation.presenter

import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.factory.useCase.UseCaseFactory
import ru.iandreyshev.parserrss.models.interactor.RssInfoInteractor
import ru.iandreyshev.parserrss.models.viewModels.ViewRss
import ru.iandreyshev.parserrss.models.useCase.LoadRssInfoUseCase
import ru.iandreyshev.parserrss.models.useCase.OpenRssOriginalUseCase
import ru.iandreyshev.parserrss.presentation.presenter.extention.openInBrowser
import ru.iandreyshev.parserrss.presentation.presenter.extention.toast
import ru.iandreyshev.parserrss.presentation.view.IRssInfoView

@InjectViewState
class RssInfoPresenter(rss: ViewRss) : MvpPresenter<IRssInfoView>() {

    val interactor = RssInfoInteractor(UseCaseFactory, rss, UseCaseListener())

    private inner class UseCaseListener : LoadRssInfoUseCase.IListener,
            OpenRssOriginalUseCase.IListener {

        override fun enableOpenOriginalButton(isEnabled: Boolean) {
            viewState.setOpenOriginalEnabled(isEnabled)
        }

        override fun openOriginal(path: Uri?) {
            if (path != null) {
                openInBrowser(path)
            } else {
                toast(R.string.toast_invalid_url)
            }
        }

        override fun setInfo(title: String?, description: String?) {
            viewState.setInfo(title, description)
        }
    }
}
