package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.repository.IRepository

class DeleteRssUseCase(
        private val mRepository: IRepository,
        private val mRssId: Long,
        private val mPresenter: IListener) : BaseUseCase<Any, Any, Any?>(mPresenter) {

    interface IListener : IUseCaseListener {
        fun removeRss(id: Long)
    }

    override fun doInBackground(vararg rssToDelete: Any): Long? {
        mRepository.removeRssById(mRssId)

        return null
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        mPresenter.removeRss(mRssId)
    }
}
