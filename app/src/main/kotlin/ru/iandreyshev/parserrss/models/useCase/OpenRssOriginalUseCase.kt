package ru.iandreyshev.parserrss.models.useCase

import android.net.Uri
import ru.iandreyshev.parserrss.models.extention.uri
import ru.iandreyshev.parserrss.models.viewModels.ViewRss

class OpenRssOriginalUseCase(
        private val mRss: ViewRss,
        private val mPresenter: IListener) : IUseCase {

    interface IListener : IUseCaseListener {
        fun openOriginal(path: Uri?)
    }

    override fun start() {
        mPresenter.openOriginal(mRss.origin?.uri)
    }
}
