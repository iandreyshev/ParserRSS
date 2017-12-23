package ru.iandreyshev.parserrss.ui.listeners;

import ru.iandreyshev.parserrss.models.rss.RssFeed;

public interface IOnUpdateRssListener {
    void onUpdateRss(final RssFeed feed);
}
