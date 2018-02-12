package ru.iandreyshev.parserrss.models.interactor

import org.jetbrains.anko.doAsync
import ru.iandreyshev.parserrss.factory.useCase.IUseCaseFactory
import ru.iandreyshev.parserrss.factory.useCase.UseCaseType
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.rss.Rss

class FeedInteractor(
        private val mUseCaseFactory: IUseCaseFactory,
        private val mListener: IUseCaseListener) {

    init {
        doAsync {
            mUseCaseFactory
                    .create(UseCaseType.FEED_LOAD_ALL_RSS, object {}, mListener)
                    .start()
        }
    }

    fun onAddNewRss(url: String) = doAsync {
        mUseCaseFactory
                .create(UseCaseType.FEED_INSERT_RSS, url, mListener)
                .start()
    }

    fun onDeleteRss(rssId: Long?) = doAsync {
        mUseCaseFactory
                .create(UseCaseType.FEED_DELETE_RSS, rssId, mListener)
                .start()
    }

    fun onOpenRssInfo(rss: Rss?) = doAsync {
        mUseCaseFactory
                .create(UseCaseType.FEED_OPEN_RSS_INFO, rss, mListener)
                .start()
    }

    fun onOpenArticle(articleId: Long) = doAsync {
        mUseCaseFactory
                .create(UseCaseType.RSS_PAGE_OPEN_ARTICLE, articleId, mListener)
                .start()
    }
}
