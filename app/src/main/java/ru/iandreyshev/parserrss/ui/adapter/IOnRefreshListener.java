package ru.iandreyshev.parserrss.ui.adapter;

import ru.iandreyshev.parserrss.models.rss.RssFeed;

public interface IOnRefreshListener {
    void onRefresh(RssFeed feed);
}
