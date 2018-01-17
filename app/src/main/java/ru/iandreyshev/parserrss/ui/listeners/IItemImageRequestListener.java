package ru.iandreyshev.parserrss.ui.listeners;

import ru.iandreyshev.parserrss.ui.adapter.IFeedItem;

public interface IItemImageRequestListener {
    void getImageFor(final IFeedItem item, boolean isForce);
}
