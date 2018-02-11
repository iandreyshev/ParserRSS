package ru.iandreyshev.parserrss.presentation.presenter.extention

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.presentation.presenter.FeedPresenter
import ru.iandreyshev.parserrss.presentation.view.IBaseView
import ru.iandreyshev.parserrss.ui.fragment.AddRssDialog

internal fun <T : IBaseView> MvpPresenter<T>.toast(stringId: Int) {
    viewState.showToast(App.getStr(stringId))
}

internal fun <T : IBaseView> MvpPresenter<T>.openInBrowser(path: Uri) {
    viewState.startActivity(Intent(Intent.ACTION_VIEW, path))
}

internal fun uiThread(func: () -> Unit) {
    Handler(Looper.getMainLooper()).post(func)
}

internal fun FeedPresenter.addRssDialog(url: String = ""): AddRssDialog {
    return AddRssDialog.newInstance(url)
}
