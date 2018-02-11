package ru.iandreyshev.parserrss.models.extention

import ru.iandreyshev.parserrss.models.repository.Rss
import ru.iandreyshev.parserrss.models.viewModels.ViewRss

val Rss.viewModel: ViewRss
    get() = ViewRss(this)
