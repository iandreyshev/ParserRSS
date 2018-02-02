package ru.iandreyshev.parserrss.interactor

import android.net.Uri
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.extention.uri
import ru.iandreyshev.parserrss.models.rss.ViewRss

class RssInfoInteractor(
        private val _outputPort: IOutputPort,
        private val _rss: ViewRss) : BaseInteractor(_outputPort) {

    init {
        _outputPort.setInfo(_rss.title, _rss.description)
        _outputPort.setOpenOriginalEnabled(_rss.origin != null)
    }

    fun onOpenOriginal() {
        _rss.origin?.uri.let {
            when (it) {
                null -> _outputPort.showMessage(R.string.toast_invalid_url)
                else -> _outputPort.openOriginal(it)
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
