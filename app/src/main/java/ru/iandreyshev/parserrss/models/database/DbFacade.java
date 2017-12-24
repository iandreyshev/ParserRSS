package ru.iandreyshev.parserrss.models.database;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.rss.RssArticle;
import ru.iandreyshev.parserrss.models.rss.Rss_;

public class DbFacade {
    private static final int NOT_PUT = -1;

    private BoxStore mBoxStore = App.getBoxStore();
    private Box<Rss> mRssBox = mBoxStore.boxFor(Rss.class);
    private Box<RssArticle> mArticleBox = mBoxStore.boxFor(RssArticle.class);

    public Long getRssId(final String url) {
        final List<Rss> rssWithSameUrl = mRssBox.find(Rss_.mUrl, url);

        if (rssWithSameUrl.isEmpty()) {
            return null;
        }

        return rssWithSameUrl.get(0).getId();
    }

    public long putRss(final Rss rss) {
        final List<Rss> rssWithSameUrl = mRssBox.find(Rss_.mUrl, rss.getUrl());

        if (!rssWithSameUrl.isEmpty()) {
            return NOT_PUT;
        }

        long id = mRssBox.put(rss);

        return id > 0 ? id : NOT_PUT;
    }

    public List<Rss> getAllRss() {
        return mRssBox.getAll();
    }
}
