package ru.iandreyshev.parserrss.presentation.presenter.extention

import android.content.Intent
import android.net.Uri
import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.presentation.view.IBaseView

internal fun <T : IBaseView> MvpPresenter<T>.toast(stringId: Int) {
    viewState.showToast(App.getStr(stringId))
}

internal fun <T : IBaseView> MvpPresenter<T>.openInBrowser(path: Uri) {
    viewState.startActivity(Intent(Intent.ACTION_VIEW, path))
}
