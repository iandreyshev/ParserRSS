package ru.iandreyshev.parserrss.factory.useCase

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.factory.httpRequestHandler.HttpRequestHandlerType
import ru.iandreyshev.parserrss.factory.httpRequestHandler.HttpRequestHandlerFactory
import ru.iandreyshev.parserrss.models.filters.ArticlesFilterByDate
import ru.iandreyshev.parserrss.models.imageProps.ArticleImageProps
import ru.iandreyshev.parserrss.models.imageProps.FeedListIconProps
import ru.iandreyshev.parserrss.models.viewModels.ViewRss
import ru.iandreyshev.parserrss.models.useCase.*
import ru.iandreyshev.parserrss.factory.useCase.UseCaseType.*
import ru.iandreyshev.parserrss.models.parser.ParserV2
import ru.iandreyshev.parserrss.models.viewModels.ViewArticle
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.ui.adapter.IItemIcon

object UseCaseFactory : IUseCaseFactory {
    override fun create(type: UseCaseType, mListener: IUseCaseListener, data: Any): IUseCase {
        return when (type) {
            LOAD_ARTICLE_IMAGE -> LoadArticleImageUseCase(
                    App.repository,
                    HttpRequestHandlerFactory.create(HttpRequestHandlerType.ARTICLE_IMAGE),
                    ArticleImageProps,
                    data as Long,
                    mListener as LoadArticleImageUseCase.IListener
            )
            LOAD_ARTICLE_IMAGE_TO_FEED_ITEM -> LoadArticleImageToFeedItemUseCase(
                    App.repository,
                    HttpRequestHandlerFactory.create(HttpRequestHandlerType.ARTICLE_FEED_ITEM_IMAGE),
                    FeedListIconProps,
                    data as IItemIcon,
                    mListener
            )
            LOAD_ARTICLE -> LoadArticleUseCase(
                    App.repository,
                    data as Long,
                    mListener as LoadArticleUseCase.IListener
            )
            OPEN_ARTICLE_ORIGINAL -> OpenArticleOriginalUseCase(
                    App.repository,
                    data as Long,
                    mListener as OpenArticleOriginalUseCase.IListener
            )
            LOAD_ARTICLES_FIRST_TIME -> LoadArticlesFirstTimeUseCase(
                    data as MutableList<ViewArticle>,
                    mListener as LoadArticlesFirstTimeUseCase.IListener
            )
            UPDATE_RSS -> UpdateRssUseCase(
                    App.repository,
                    HttpRequestHandlerFactory.create(HttpRequestHandlerType.UPDATE_RSS),
                    ParserV2(),
                    data as String,
                    ArticlesFilterByDate,
                    mListener as UpdateRssUseCase.IListener
            )
            INSERT_RSS -> InsertRssUseCase(
                    App.repository,
                    HttpRequestHandlerFactory.create(HttpRequestHandlerType.NEW_RSS),
                    ParserV2(),
                    data as String,
                    ArticlesFilterByDate,
                    mListener as InsertRssUseCase.IListener
            )
            LOAD_RSS_INFO -> LoadRssInfoUseCase(
                    data as ViewRss,
                    mListener as LoadRssInfoUseCase.IListener
            )
            OPEN_RSS_ORIGINAL -> OpenRssOriginalUseCase(
                    data as ViewRss,
                    mListener as OpenRssOriginalUseCase.IListener
            )
            LOAD_ALL_RSS -> LoadAllRssUseCase(
                    App.repository,
                    ArticlesFilterByDate,
                    mListener as LoadAllRssUseCase.IListener
            )
            DELETE_RSS -> DeleteRssUseCase(
                    App.repository,
                    data as Long,
                    mListener as DeleteRssUseCase.IListener
            )
            OPEN_ARTICLE -> OpenArticleUseCase(
                    data as Long,
                    mListener as OpenArticleUseCase.IListener
            )
            OPEN_RSS_INFO -> OpenRssInfoUseCase(
                    data as ViewRss,
                    mListener as OpenRssInfoUseCase.IListener
            )
            MESSAGE -> MessageUseCase(
                    data as String,
                    mListener
            )
        }
    }
}
