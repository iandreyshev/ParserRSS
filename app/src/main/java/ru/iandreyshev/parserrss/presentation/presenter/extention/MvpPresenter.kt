package ru.iandreyshev.parserrss.presentation.presenter.extention

import com.arellomobile.mvp.MvpPresenter
import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.presentation.view.IBaseView

internal fun<T: IBaseView> MvpPresenter<T>.toast(stringId: Int) {
    this.viewState.showToast(App.getStr(stringId))
}
