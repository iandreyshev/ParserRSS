package ru.iandreyshev.parserrss.models.useCase

import android.net.Uri
import ru.iandreyshev.parserrss.models.extention.uri
import ru.iandreyshev.parserrss.models.repository.IRepository

class OpenArticleOriginalUseCase(
        private val mRepository: IRepository,
        private val mArticleId: Long,
        private val mPresenter: IListener) : BaseUseCase<Any?, Any?, Uri?>(mPresenter) {

    interface IListener : IUseCaseListener {
        fun openOriginal(path: Uri?)
    }

    override fun doInBackground(vararg params: Any?): Uri? {
        val article = mRepository.getArticleById(mArticleId)

        return article?.originUrl?.uri
    }

    override fun onPostExecute(result: Uri?) {
        super.onPostExecute(result)
        mPresenter.openOriginal(result)
    }
}
