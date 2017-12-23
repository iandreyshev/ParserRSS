package ru.iandreyshev.parserrss.ui.adapter;

import ru.iandreyshev.parserrss.models.rss.RssFeed;

public interface IOnRefreshFeedListener {
    void onRefresh(RssFeed feed);
}
