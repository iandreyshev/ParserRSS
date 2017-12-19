package ru.iandreyshev.parserrss.models.async;

import ru.iandreyshev.parserrss.models.rss.IRssFeed;

public class DeleteFeedTask extends Task<IRssFeed, Void, Void, Void> {
    @Override
    protected Void behaviourProcess(IRssFeed[] iRssFeeds) {
        return null;
    }
}
