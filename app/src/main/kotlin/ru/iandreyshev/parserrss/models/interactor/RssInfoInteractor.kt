package ru.iandreyshev.parserrss.models.interactor

import org.jetbrains.anko.doAsync
import ru.iandreyshev.parserrss.factory.useCase.IUseCaseFactory
import ru.iandreyshev.parserrss.factory.useCase.UseCaseType
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.rss.Rss

class RssInfoInteractor(
        private val mUseCaseFactory: IUseCaseFactory,
        private val mRss: Rss?,
        private val mListener: IUseCaseListener) {

    init {
        doAsync {
            mUseCaseFactory
                    .create(UseCaseType.RSS_INFO_LOAD_DATA, mRss, mListener)
                    .start()
        }
    }

    fun onOpenOriginal() = doAsync {
        mUseCaseFactory
                .create(UseCaseType.RSS_INFO_OPEN_ORIGINAL, mRss?.originUrl, mListener)
                .start()
    }
}
