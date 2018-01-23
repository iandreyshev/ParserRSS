package ru.iandreyshev.parserrss.ui.listeners;

import ru.iandreyshev.parserrss.ui.adapter.IFeedItem;

public interface IItemImageRequestListener {
    void getIconForItem(final IFeedItem item, boolean isWithoutQueue);
}
