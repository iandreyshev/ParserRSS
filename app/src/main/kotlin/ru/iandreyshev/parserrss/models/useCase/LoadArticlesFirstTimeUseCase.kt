package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.viewModels.ViewArticle

class LoadArticlesFirstTimeUseCase(
        private val mArticles: MutableList<ViewArticle>,
        private val mListener: IListener) : IUseCase {

    interface IListener : IUseCaseListener {
        fun insertArticlesFirstTime(articles: MutableList<ViewArticle>)
    }

    override fun start() {
        mListener.insertArticlesFirstTime(mArticles)
    }
}
