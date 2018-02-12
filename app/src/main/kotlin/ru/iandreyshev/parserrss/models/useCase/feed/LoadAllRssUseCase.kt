package ru.iandreyshev.parserrss.models.useCase.feed

import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.useCase.UseCase
import ru.iandreyshev.parserrss.models.rss.Rss

class LoadAllRssUseCase(
        private val mRepository: IRepository,
        private val mFilter: IArticlesFilter,
        private val mListener: IListener) : UseCase(mListener) {

    interface IListener : IUseCaseListener {
        fun updateCapacity(isFull: Boolean)

        fun loadRss(rss: Rss)
    }

    override fun onProcess() = mRepository.runInTx {
        mRepository.rssIdList.forEach { id ->
            val rss = mRepository.getRssById(id)

            if (rss != null) {
                mFilter.sort(rss.articles)
                mListener.loadRss(rss)
            }
        }
        mListener.updateCapacity(mRepository.isFull)
    }
}