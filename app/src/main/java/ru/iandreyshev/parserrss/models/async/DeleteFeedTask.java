package ru.iandreyshev.parserrss.models.async;

import ru.iandreyshev.parserrss.models.rss.RssFeed;

public class DeleteFeedTask extends Task<RssFeed, Void, Void, Void> {
    @Override
    protected Void behaviourProcess(RssFeed[] iRssFeeds) {
        return null;
    }
}
