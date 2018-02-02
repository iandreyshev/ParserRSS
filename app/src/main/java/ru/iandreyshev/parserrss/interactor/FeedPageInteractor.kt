package ru.iandreyshev.parserrss.interactor

import android.graphics.Bitmap
import ru.iandreyshev.parserrss.R
import ru.iandreyshev.parserrss.models.async.GetArticleImageTask
import ru.iandreyshev.parserrss.models.async.UpdateRssTask
import ru.iandreyshev.parserrss.models.filters.FilterByDate
import ru.iandreyshev.parserrss.models.imageProps.FeedListIconProps
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.models.web.IHttpRequestResult
import ru.iandreyshev.parserrss.ui.adapter.IItemIcon

class FeedPageInteractor(
        private val _outputPort: IOutputPort,
        private val _rss: ViewRss) : BaseInteractor(_outputPort) {

    init {
        _outputPort.setArticles(_rss.articles)
    }

    interface IOutputPort : IInteractorOutputPort {
        fun setArticles(articles: List<ViewArticle>)

        fun updateArticles(articles: List<ViewArticle>)

        fun insertImage(item: IItemIcon, imageBitmap: Bitmap)
    }

    fun load(icon: IItemIcon) {
        if (icon.isLoaded) {
            return
        }

        GetArticleImageTask.execute(icon.id, InsertImageListener(icon, icon.id), FeedListIconProps.newInstance)
    }

    fun onUpdate() {
        val url = _rss.url

        when (url) {
            null -> _outputPort.showMessage(R.string.toast_invalid_url)
            else -> {
                updateProcessCount()
                UpdateRssTask.execute(UpdateFromNetListener(), url, FilterByDate.newInstance)
            }
        }
    }

    private inner class UpdateFromNetListener : UpdateRssTask.IEventListener {
        override fun onPostExecute(result: Rss?) = updateProcessCount(false)

        override fun onRssNotExist() = _outputPort.showMessage(R.string.toast_rss_not_exist)

        override fun onDatabaseError() = _outputPort.showMessage(R.string.toast_error_saving_to_db)

        override fun onInvalidUrl() = _outputPort.showMessage(R.string.toast_invalid_url)

        override fun onParserError() = _outputPort.showMessage(R.string.toast_invalid_rss_format)

        override fun onNetError(requestResult: IHttpRequestResult) {
            _outputPort.showMessage(when (requestResult.state) {
                HttpRequestHandler.State.PermissionDenied -> R.string.toast_internet_permission_denied
                else -> R.string.toast_bad_connection
            })
        }

        override fun onSuccess(articles: List<ViewArticle>) = _outputPort.updateArticles(articles)
    }

    private inner class InsertImageListener constructor(
            private val _icon: IItemIcon,
            private val _idBeforeLoad: Long) : GetArticleImageTask.IEventListener {

        override fun onPostExecute(result: Bitmap?) {
            if (_icon.id == _idBeforeLoad) {
                _outputPort.insertImage(_icon, result ?: return)
            }
        }
    }
}