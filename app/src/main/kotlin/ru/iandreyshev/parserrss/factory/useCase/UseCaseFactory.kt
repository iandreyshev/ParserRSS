package ru.iandreyshev.parserrss.factory.useCase

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.factory.httpRequestHandler.HttpRequestHandlerType
import ru.iandreyshev.parserrss.factory.httpRequestHandler.HttpRequestHandlerFactory
import ru.iandreyshev.parserrss.models.filters.ArticlesFilterByDate
import ru.iandreyshev.parserrss.models.imageProps.ArticleImageProps
import ru.iandreyshev.parserrss.models.imageProps.FeedListIconProps
import ru.iandreyshev.parserrss.models.useCase.*
import ru.iandreyshev.parserrss.factory.useCase.UseCaseType.*
import ru.iandreyshev.parserrss.models.parser.ParserV2
import ru.iandreyshev.parserrss.models.useCase.IUseCaseListener
import ru.iandreyshev.parserrss.models.useCase.article.LoadArticleImageUseCase
import ru.iandreyshev.parserrss.models.useCase.article.LoadArticleUseCase
import ru.iandreyshev.parserrss.models.useCase.article.OpenArticleOriginalUseCase
import ru.iandreyshev.parserrss.models.useCase.feed.OpenArticleUseCase
import ru.iandreyshev.parserrss.models.useCase.feed.*
import ru.iandreyshev.parserrss.models.useCase.rssInfo.LoadRssInfoUseCase
import ru.iandreyshev.parserrss.models.useCase.rssInfo.OpenRssOriginalUseCase
import ru.iandreyshev.parserrss.models.useCase.rssList.LoadArticleImageToFeedItemUseCase
import ru.iandreyshev.parserrss.models.useCase.rssList.LoadArticlesListUseCase
import ru.iandreyshev.parserrss.models.useCase.rssList.UpdateRssUseCase
import ru.iandreyshev.parserrss.models.viewModels.ViewRss
import ru.iandreyshev.parserrss.ui.adapter.IItemIcon

class UseCaseFactory : IUseCaseFactory {
    override fun create(type: UseCaseType, data: Any?, listener: IUseCaseListener): UseCase {
        return when (type) {
            ARTICLE_LOAD_IMAGE -> LoadArticleImageUseCase(
                    App.repository,
                    HttpRequestHandlerFactory.create(HttpRequestHandlerType.ARTICLE_IMAGE),
                    ArticleImageProps(),
                    data as Long,
                    listener as LoadArticleImageUseCase.IListener
            )
            RSS_PAGE_LOAD_ICON -> LoadArticleImageToFeedItemUseCase(
                    App.repository,
                    HttpRequestHandlerFactory.create(HttpRequestHandlerType.ARTICLE_FEED_ITEM_IMAGE),
                    FeedListIconProps(),
                    data as IItemIcon,
                    listener as LoadArticleImageToFeedItemUseCase.IListener
            )
            ARTICLE_LOAD_DATA -> LoadArticleUseCase(
                    App.repository,
                    data as Long,
                    listener as LoadArticleUseCase.IListener
            )
            ARTICLE_OPEN_ORIGINAL -> OpenArticleOriginalUseCase(
                    App.repository,
                    data as Long,
                    listener as OpenArticleOriginalUseCase.IListener
            )
            FEED_UPDATE_RSS -> UpdateRssUseCase(
                    App.repository,
                    HttpRequestHandlerFactory.create(HttpRequestHandlerType.UPDATE_RSS),
                    ParserV2(),
                    data as Long,
                    ArticlesFilterByDate(),
                    listener as UpdateRssUseCase.IListener
            )
            FEED_INSERT_RSS -> InsertRssUseCase(
                    App.repository,
                    HttpRequestHandlerFactory.create(HttpRequestHandlerType.NEW_RSS),
                    ParserV2(),
                    data as String,
                    listener as InsertRssUseCase.IListener
            )
            RSS_INFO_OPEN_ORIGINAL -> OpenRssOriginalUseCase(
                    data as String?,
                    listener as OpenRssOriginalUseCase.IListener
            )
            FEED_LOAD_ALL_RSS -> LoadAllRssUseCase(
                    App.repository,
                    ArticlesFilterByDate(),
                    listener as LoadAllRssUseCase.IListener
            )
            FEED_DELETE_RSS -> DeleteRssUseCase(
                    App.repository,
                    data as Long?,
                    listener as DeleteRssUseCase.IListener
            )
            RSS_PAGE_OPEN_ARTICLE -> OpenArticleUseCase(
                    data as Long,
                    listener as OpenArticleUseCase.IListener
            )
            FEED_OPEN_RSS_INFO -> OpenRssInfoUseCase(
                    data as ViewRss?,
                    listener as OpenRssInfoUseCase.IListener
            )
            RSS_PAGE_LOAD_ARTICLES_LIST -> LoadArticlesListUseCase(
                    App.repository,
                    data as Long,
                    ArticlesFilterByDate(),
                    listener as LoadArticlesListUseCase.IListener
            )
            RSS_INFO_LOAD_DATA -> LoadRssInfoUseCase(
                    data as ViewRss?,
                    listener as LoadRssInfoUseCase.IListener
            )
        }
    }
}
