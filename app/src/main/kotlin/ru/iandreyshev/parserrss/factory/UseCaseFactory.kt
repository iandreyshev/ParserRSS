package ru.iandreyshev.parserrss.factory

import ru.iandreyshev.parserrss.app.App
import ru.iandreyshev.parserrss.models.imageProps.ArticleImageProps
import ru.iandreyshev.parserrss.models.useCase.*
import ru.iandreyshev.parserrss.models.useCase.UseCaseType.*
import ru.iandreyshev.parserrss.models.web.HttpRequestHandler

object UseCaseFactory : IUseCaseFactory {
    override fun create(type: UseCaseType, presenter: Any, data: Any): IUseCase {
        return when (type) {
            GET_ARTICLE_IMAGE ->
                GetArticleImageUseCase(
                        App.repository,
                        HttpRequestHandler(),
                        data as Long,
                        ArticleImageProps,
                        presenter as GetArticleImageUseCase.IListener
                )
            LOAD_AND_OPEN_ARTICLE ->
                LoadArticleUseCase(
                        App.repository,
                        data as Long,
                        presenter as LoadArticleUseCase.IListener
                )
            OPEN_ARTICLE_ORIGINAL ->
                OpenOriginalUseCase(
                        App.repository,
                        data as Long,
                        presenter as OpenOriginalUseCase.IListener
                )
            else -> object : IUseCase {
                override fun start() {
                }
            }
        }
    }
}
