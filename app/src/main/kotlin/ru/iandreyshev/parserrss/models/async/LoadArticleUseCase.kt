package ru.iandreyshev.parserrss.models.useCase

import android.util.Log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss

class LoadArticleUseCase(
        private val mRepository: IRepository,
        private val mArticleId: Long,
        private val mListener: IListener) : IUseCase {

    companion object {
        private val TAG = LoadArticleUseCase::class.java.name
    }

    interface IListener {
        fun initArticle(rss: ViewRss, article: ViewArticle)

        fun initArticleFail()
    }

    override fun start() {
        doAsync {
            try {
                val articleFromRepository = mRepository.getArticleById(mArticleId)

                if (articleFromRepository == null) {
                    uiThread {
                        mListener.initArticleFail()
                    }
                    return@doAsync
                }

                val resultArticle = ViewArticle(articleFromRepository)
                val resultRss = ViewRss(
                        id = articleFromRepository.rssId,
                        title = mRepository.getRssTitle(articleFromRepository.rssId)
                )

                uiThread {
                    mListener.initArticle(resultRss, resultArticle)
                }
            } catch (ex: Exception) {
                Log.e(TAG, Log.getStackTraceString(ex))
                uiThread {
                    mListener.initArticleFail()
                }
            }
        }
    }
}
