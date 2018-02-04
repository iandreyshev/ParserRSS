package ru.iandreyshev.parserrss.models.useCase

import android.util.Log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.repository.Rss

class LoadAllRssUseCase(
        private val mRepository: IRepository,
        private val mFilter: IArticlesFilter,
        private val mListener: IListener) : IUseCase {

    companion object {
        private val TAG = LoadAllRssUseCase::class.java.name
    }

    interface IListener {
        fun onLoadRss(rss: Rss)
    }

    override fun start() {
        doAsync {
            try {
                mRepository.getRssIdList().forEach { id ->
                    val rss = App.repository.getRssById(id)

                    if (rss != null) {
                        mFilter.sort(rss.articles)
                        uiThread {
                            mListener.onLoadRss(rss)
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.e(TAG, Log.getStackTraceString(ex))
            }
        }
    }
}
