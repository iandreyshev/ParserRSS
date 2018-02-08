package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.rss.ViewRss

class LoadAllRssUseCase(
        private val mRepository: IRepository,
        private val mFilter: IArticlesFilter,
        private val mListener: IListener) : BaseUseCase<Any, ViewRss, Any?>(mListener) {

    interface IListener : IUseCaseListener {
        fun updateCapacityAfterLoad(isFull: Boolean)

        fun loadRss(rss: ViewRss)
    }

    private var mIsFeedFull = false

    override fun doInBackground(vararg args: Any): Any? {
        mRepository.rssIdList.forEach { id ->
            val rss = mRepository.getRssById(id)

            if (rss != null) {
                mFilter.sort(rss.articles)
                onProgressUpdate(ViewRss(rss))
            }
        }
        mIsFeedFull = mRepository.isFull

        return null
    }

    override fun onProgressUpdate(vararg values: ViewRss) {
        mListener.loadRss(values[0])
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        mListener.updateCapacityAfterLoad(mIsFeedFull)
    }
}
