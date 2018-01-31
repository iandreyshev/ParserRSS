package ru.iandreyshev.parserrss.interactor

import android.net.Uri
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.rss.ViewRss

class RssInfoInteractor(private val outputPort: IOutputPort) : BaseInteractor(outputPort) {

    var rss: ViewRss? = null
        set(value) {
            field = value
            when (field) {
                null -> {
                    outputPort.showMessage(R.string.toast_invalid_rss_format)
                    outputPort.close()
                }
                else -> outputPort.setInfo(field?.title, field?.description)
            }
        }

    fun onOpenOriginal() {

    }

    interface IOutputPort : IInteractorOutputPort {
        fun openOriginal(uri: Uri)

        fun setInfo(title: String?, description: String?)

        fun close()
    }
}
