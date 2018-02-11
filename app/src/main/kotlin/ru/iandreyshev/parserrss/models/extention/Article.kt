package ru.iandreyshev.parserrss.models.extention

import ru.iandreyshev.parserrss.models.repository.Article
import ru.iandreyshev.parserrss.models.viewModels.ViewArticle

val Article.viewModel: ViewArticle
    get() = ViewArticle(this)
