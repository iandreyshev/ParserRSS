package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.viewModels.ViewArticle
import ru.iandreyshev.parserrss.models.viewModels.ViewRss

class LoadArticleUseCase(
        private val mRepository: IRepository,
        private val mArticleId: Long,
        private val mListener: IListener) : BaseUseCase<Any?, Any?, Any?>(mListener) {

    interface IListener : IUseCaseListener {
        fun loadArticle(rss: ViewRss?, article: ViewArticle?)
    }

    private var mRss: ViewRss? = null
    private var mArticle: ViewArticle? = null

    override fun doInBackground(vararg params: Any?): Any? {
        val article = mRepository.getArticleById(mArticleId)
        val rss = article?.let {
            mArticle = ViewArticle(article)
            return@let mRepository.getRssById(article.rssId)
        }
        mRss = rss?.let { return@let ViewRss(rss) }

        return null
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        mListener.loadArticle(mRss, mArticle)
    }
}
