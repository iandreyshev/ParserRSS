package ru.iandreyshev.parserrss.models.useCase.article

import ru.iandreyshev.parserrss.models.repository.IRepository
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.useCase.UseCase

class OpenArticleOriginalUseCase(
        private val mRepository: IRepository,
        private val mArticleId: Long,
        private val mListener: IListener) : UseCase(mListener) {

    interface IListener : IUseCaseListener {
        fun openOriginal(addressString: String?)
    }

    override fun onProcess() {
        val article = mRepository.getArticleById(mArticleId)
        mListener.openOriginal(article?.originUrl)
    }
}
