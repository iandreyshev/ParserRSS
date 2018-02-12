package ru.iandreyshev.parserrss.models.useCase.article

import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.useCase.UseCase
import ru.iandreyshev.parserrss.models.rss.Article
import ru.iandreyshev.parserrss.models.rss.Rss

class LoadArticleUseCase(
        private val mRepository: IRepository,
        private val mArticleId: Long,
        private val mListener: IListener) : UseCase(mListener) {

    interface IListener : IUseCaseListener {
        fun loadArticle(rss: Rss?, article: Article?)
    }

    override fun onProcess() {
        val article = mRepository.getArticleById(mArticleId)
        val rss = article?.let {
            mRepository.getRssById(article.rssId)
        }
        mListener.loadArticle(rss, article)
    }
}
