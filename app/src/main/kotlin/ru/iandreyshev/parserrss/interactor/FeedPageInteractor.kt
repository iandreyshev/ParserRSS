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
        private val mOutputPort: IOutputPort,
        private val mRss: ViewRss) : BaseInteractor(mOutputPort) {

    init {
        mOutputPort.setArticles(mRss.articles)
    }

    interface IOutputPort : IInteractorOutputPort {
        fun setArticles(articles: List<ViewArticle>)

        fun updateArticles(articles: List<ViewArticle>)

        fun insertImage(item: IItemIcon, imageBitmap: Bitmap)

        fun openInternetPermissionDialog()
    }

    fun load(icon: IItemIcon) {
        if (icon.isLoaded) {
            return
        }

        GetArticleImageTask.execute(icon.id, InsertImageListener(icon, icon.id), FeedListIconProps.newInstance)
    }

    fun onUpdate() {
        val url = mRss.url

        when (url) {
            null -> mOutputPort.showMessage(R.string.toast_invalid_url)
            else -> {
                updateProcessCount()
                UpdateRssTask.execute(UpdateFromNetListener(), url, FilterByDate.newInstance)
            }
        }
    }

    private inner class UpdateFromNetListener : UpdateRssTask.IEventListener {
        override fun onPostExecute(result: Rss?) = updateProcessCount(false)

        override fun onRssNotExist() = mOutputPort.showMessage(R.string.toast_rss_not_exist)

        override fun onDatabaseError() = mOutputPort.showMessage(R.string.toast_error_saving_to_db)

        override fun onInvalidUrl() = mOutputPort.showMessage(R.string.toast_invalid_url)

        override fun onParserError() = mOutputPort.showMessage(R.string.toast_invalid_rss_format)

        override fun onNetError(requestResult: IHttpRequestResult) {
            mOutputPort.showMessage(when (requestResult.state) {
                HttpRequestHandler.State.PermissionDenied -> {
                    mOutputPort.openInternetPermissionDialog()
                    R.string.toast_internet_permission_denied
                }
                else -> R.string.toast_bad_connection
            })
        }

        override fun onSuccess(articles: List<ViewArticle>) = mOutputPort.updateArticles(articles)
    }

    private inner class InsertImageListener constructor(
            private val mIcon: IItemIcon,
            private val mIdBeforeLoad: Long) : GetArticleImageTask.IEventListener {

        override fun onPostExecute(result: Bitmap?) {
            if (mIcon.id == mIdBeforeLoad) {
                mOutputPort.insertImage(mIcon, result ?: return)
            }
        }
    }
}