package ru.iandreyshev.parserrss.models.useCase.rssList

import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.useCase.UseCase
import ru.iandreyshev.parserrss.models.rss.Article

class LoadArticlesListUseCase(
        private val mRepository: IRepository,
        private val mRssId: Long,
        private val mFilter: IArticlesFilter,
        private val mListener: IListener) : UseCase(mListener) {

    interface IListener : IUseCaseListener {
        fun loadArticles(articles: MutableList<Article>)
    }

    override fun onProcess() = mRepository.runInTx {
        val articles = mRepository.getRssById(mRssId)?.articles
        mFilter.sort(articles ?: ArrayList())
        mListener.loadArticles(articles ?: ArrayList())
    }
}
