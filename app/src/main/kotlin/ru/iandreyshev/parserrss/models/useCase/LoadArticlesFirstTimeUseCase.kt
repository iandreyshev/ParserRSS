package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.rss.ViewArticle
import ru.iandreyshev.parserrss.models.rss.ViewRss

class LoadArticlesFirstTimeUseCase(
        private val mRss: ViewRss,
        private val mPresenter: IListener) : IUseCase {

    interface IListener : IUseCaseListener {
        fun insertArticlesFirstTime(articles: MutableList<ViewArticle>)
    }

    override fun start() {
        mPresenter.insertArticlesFirstTime(mRss.articles)
    }
}
