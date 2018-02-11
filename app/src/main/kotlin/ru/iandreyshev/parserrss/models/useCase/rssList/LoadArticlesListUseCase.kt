package ru.iandreyshev.parserrss.models.useCase.rssList

import ru.iandreyshev.parserrss.models.extention.viewModel
import ru.iandreyshev.parserrss.models.filters.IArticlesFilter
import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.useCase.UseCase
import ru.iandreyshev.parserrss.models.viewModels.ViewArticle

class LoadArticlesListUseCase(
        private val mRepository: IRepository,
        private val mRssId: Long,
        private val mFilter: IArticlesFilter,
        private val mListener: IListener) : UseCase(mListener) {

    interface IListener : IUseCaseListener {
        fun loadArticles(articles: ArrayList<ViewArticle>)
    }

    override fun onProcess() {
        val articles = mRepository.getArticlesByRssId(mRssId)
        mFilter.sort(articles)
        mListener.loadArticles(ArrayList(articles.map { it.viewModel }))
    }
}
