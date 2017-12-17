package ru.iandreyshev.parserrss.ui.adapter;

import ru.iandreyshev.parserrss.models.rss.IRssFeed;

public interface IOnRefreshListener {
    void onRefresh(IRssFeed feed);
}
