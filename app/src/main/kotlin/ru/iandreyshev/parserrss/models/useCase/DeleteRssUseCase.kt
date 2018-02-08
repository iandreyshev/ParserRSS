package ru.iandreyshev.parserrss.models.useCase

import ru.iandreyshev.parserrss.models.repository.IRepository

class DeleteRssUseCase(
        private val mRepository: IRepository,
        private val mRssId: Long,
        private val mListener: IListener) : BaseUseCase<Any, Any, Any?>(mListener) {

    interface IListener : IUseCaseListener {
        fun removeRss(id: Long)

        fun removingRssFailed()
    }

    private var mResultEvent: (() -> Unit)? = null

    override fun doInBackground(vararg rssToDelete: Any): Long? {
        mResultEvent = if (mRepository.removeRssById(mRssId)) {
            { mListener.removeRss(mRssId) }
        } else {
            mListener::removingRssFailed
        }

        return null
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        mResultEvent?.invoke()
    }
}
