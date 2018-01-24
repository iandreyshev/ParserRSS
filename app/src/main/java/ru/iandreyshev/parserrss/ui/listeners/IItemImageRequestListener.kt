package ru.iandreyshev.parserrss.ui.listeners

import ru.iandreyshev.parserrss.ui.adapter.IFeedItem

interface IItemImageRequestListener {
    fun getIconForItem(item: IFeedItem, isWithoutQueue: Boolean)
}
