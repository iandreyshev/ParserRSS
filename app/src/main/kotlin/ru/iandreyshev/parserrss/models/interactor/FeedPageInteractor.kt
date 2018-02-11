package ru.iandreyshev.parserrss.models.interactor

import org.jetbrains.anko.doAsync
import ru.iandreyshev.parserrss.factory.useCase.IUseCaseFactory
import ru.iandreyshev.parserrss.factory.useCase.UseCaseType
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.ui.adapter.IItemIcon

class FeedPageInteractor(
        private val mUseCaseFactory: IUseCaseFactory,
        private val mListener: IUseCaseListener,
        private val mRssId: Long) {

    init {
        doAsync {
            mUseCaseFactory
                    .create(UseCaseType.RSS_PAGE_LOAD_ARTICLES_LIST, mRssId, mListener)
                    .start()
        }
    }

    fun updateImage(icon: IItemIcon) {
        doAsync {
            mUseCaseFactory
                    .create(UseCaseType.RSS_PAGE_LOAD_ICON, icon, mListener)
                    .start()
        }
    }

    fun onUpdate() {
        doAsync {
            mUseCaseFactory
                    .create(UseCaseType.FEED_UPDATE_RSS, mRssId, mListener)
                    .start()
        }
    }
}
