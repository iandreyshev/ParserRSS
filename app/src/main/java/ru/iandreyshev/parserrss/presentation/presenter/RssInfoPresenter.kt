package ru.iandreyshev.parserrss.presentation.presenter

import android.net.Uri
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.presentation.view.IRssInfoView

@InjectViewState
class RssInfoPresenter(private val rss: ViewRss) : MvpPresenter<IRssInfoView>() {
    override fun onFirstViewAttach() = viewState.setInfo(rss)

    fun onOpenOriginal() = viewState.openInBrowser(Uri.parse(rss.origin))
}
