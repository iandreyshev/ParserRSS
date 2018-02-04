package ru.iandreyshev.parserrss.interactor

import android.net.Uri
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.extention.uri
import ru.iandreyshev.parserrss.models.rss.ViewRss

class RssInfoInteractor(
        private val mOutputPort: IOutputPort,
        private val mRss: ViewRss) : BaseInteractor(mOutputPort) {

    init {
        mOutputPort.setInfo(mRss.title, mRss.description)
        mOutputPort.setOpenOriginalEnabled(mRss.origin != null)
    }

    fun onOpenOriginal() {
        mRss.origin?.uri.let {
            when (it) {
                null -> mOutputPort.showMessage(R.string.toast_invalid_url)
                else -> mOutputPort.openOriginal(it)
            }
        }
    }

    interface IOutputPort : IInteractorOutputPort {
        fun openOriginal(uri: Uri)

        fun setInfo(title: String?, description: String?)

        fun setOpenOriginalEnabled(idEnabled: Boolean)

        fun close()
    }
}
