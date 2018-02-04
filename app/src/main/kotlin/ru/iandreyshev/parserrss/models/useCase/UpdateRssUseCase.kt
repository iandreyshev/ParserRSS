package ru.iandreyshev.parserrss.models.useCase

import android.util.Log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.web.IHttpRequestHandler

class UpdateRssUseCase(
        private val mRepository: IRepository,
        private val mRequestHandler: IHttpRequestHandler,
        filter: IArticlesFilter,
        private val mListener: IListener)
    : DownloadRssUseCase(
        mRequestHandler,
        filter,
        mListener), IUseCase {

    companion object {
        private val TAG = UpdateRssUseCase::class.java.name
    }

    interface IListener : DownloadRssUseCase.IListener {
        fun onRssNotExist()

        fun onDatabaseError()

        fun onUpdateSuccess(articles: MutableList<ViewArticle>)
    }

    override fun isUrlValidAsync(): Boolean {
        if (!super.isUrlValidAsync()) {

            return false

        } else if (!mRepository.isRssWithUrlExist(mRequestHandler.urlString)) {

            return false
        }

        return true
    }

    override fun onSuccessAsync(rss: Rss) {
        doAsync {
            try {
                if (mRepository.updateRssWithSameUrl(rss)) {
                    uiThread {
                        mListener.onUpdateSuccess(ViewRss(rss).articles)
                    }
                } else {
                    uiThread {
                        mListener.onRssNotExist()
                    }
                }
            } catch (ex: Exception) {
                Log.e(TAG, Log.getStackTraceString(ex))
                uiThread {
                    mListener.onDatabaseError()
                }
            }
        }
    }
}
