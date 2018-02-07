package ru.iandreyshev.parserrss.interactor

import ru.iandreyshev.parserrss.factory.useCase.IUseCaseFactory
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.factory.useCase.UseCaseType
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener

class RssInfoInteractor(
        private val mUseCaseFactory: IUseCaseFactory,
        private val mRss: ViewRss,
        private val mListener: IUseCaseListener) {

    init {
        mUseCaseFactory
                .create(UseCaseType.LOAD_RSS_INFO, mListener, mRss)
                .start()
    }

    fun onOpenOriginal() {
        mUseCaseFactory
                .create(UseCaseType.OPEN_RSS_ORIGINAL, mListener, mRss)
                .start()
    }
}
