package ru.iandreyshev.parserrss.factory

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.filters.ArticlesFilterByDate
import ru.iandreyshev.parserrss.models.imageProps.ArticleImageProps
import ru.iandreyshev.parserrss.models.imageProps.FeedListIconProps
import ru.iandreyshev.parserrss.models.rss.ViewRss
import ru.iandreyshev.parserrss.models.useCase.*
import ru.iandreyshev.parserrss.models.useCase.UseCaseType.*
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler
import ru.iandreyshev.parserrss.presentation.presenter.IPresenter
import ru.iandreyshev.parserrss.ui.adapter.IItemIcon

object UseCaseFactory : IUseCaseFactory {
    override fun create(type: UseCaseType, presenter: IPresenter, data: Any): IUseCase {
        return when (type) {
            LOAD_IMAGE_TO_ACTIVITY -> LoadImageToActivityUseCase(
                    App.repository,
                    HttpRequestHandler(),
                    ArticleImageProps,
                    data as Long,
                    presenter as LoadImageToActivityUseCase.IListener
            )
            LOAD_IMAGE_TO_FEED_ITEM -> LoadImageToFeedItemUseCase(
                    App.repository,
                    HttpRequestHandler(),
                    FeedListIconProps,
                    data as IItemIcon
            )
            LOAD_ARTICLE_DATA -> LoadArticleUseCase(
                    App.repository,
                    presenter as LoadArticleUseCase.IListener,
                    data as Long
            )
            OPEN_ARTICLE_ORIGINAL -> OpenOriginalUseCase(
                    App.repository,
                    data as Long,
                    presenter as OpenOriginalUseCase.IListener
            )
            LOAD_ARTICLES_FIRST_TIME -> LoadArticlesFirstTimeUseCase(
                    data as ViewRss,
                    presenter as LoadArticlesFirstTimeUseCase.IListener
            )
            UPDATE_RSS -> UpdateRssUseCase(
                    App.repository,
                    HttpRequestHandler(),
                    ArticlesFilterByDate,
                    presenter as UpdateRssUseCase.IListener
            )
            else -> object : IUseCase {
                override fun start() {
                }
            }
        }
    }
}
