package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.rss.ViewRss

class LoadAllRssUseCase(
        private val mRepository: IRepository,
        private val mFilter: IArticlesFilter,
        private val mListener: IListener) : BaseUseCase<Any, ViewRss, Any?>(mListener) {

    interface IListener : IUseCaseListener {
        fun updateCapacityBeforeLoad(isFull: Boolean)

        fun loadRss(rss: ViewRss)
    }

    private var mIsFeedFull = false
    private var mIsCapacityUpdated = false

    override fun doInBackground(vararg args: Any): Any? {
        mIsFeedFull = mRepository.isFull
        mRepository.rssIdList.forEach { id ->
            val rss = mRepository.getRssById(id)

            if (rss != null) {
                mFilter.sort(rss.articles)
                onProgressUpdate(ViewRss(rss))
            }
        }

        return null
    }

    override fun onProgressUpdate(vararg values: ViewRss) {
        if (!mIsCapacityUpdated) {
            mListener.updateCapacityBeforeLoad(mIsFeedFull)
            mIsCapacityUpdated = true
        }

        mListener.loadRss(values[0])
    }
}
