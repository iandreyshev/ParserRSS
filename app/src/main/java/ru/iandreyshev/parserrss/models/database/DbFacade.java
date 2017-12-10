package ru.iandreyshev.parserrss.models.database;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.exception.DbException;
import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.rss.Rss;
import ru.iandreyshev.parserrss.models.rss.RssArticle;
import ru.iandreyshev.parserrss.models.rss.RssArticle_;
import ru.iandreyshev.parserrss.models.rss.Rss_;

public class DbFacade {
    private static final String TAG = DbFacade.class.getName();
    private static final String BOX_NOT_FOUND_PATTERN = "Box for class %s not found";
    private static final int MIN_INDEX = 1;

    private Box<Rss> mRssBox;
    private Box<RssArticle> mArticleBox;

    public DbFacade() throws DbException {
        if (App.getBoxStore() == null) {
            throw new DbException("Box-store is null in constructor");
        }

        mRssBox = App.getBoxStore().boxFor(Rss.class);
        mArticleBox = App.getBoxStore().boxFor(RssArticle.class);
    }

    public boolean isRssExist(final String url) {
        return !mRssBox.find(Rss_.mUrl, url).isEmpty();
    }

    public boolean putRssIfNotExist(final Rss rss) throws Exception {
        final List<Rss> rssWithSameUrl = mRssBox.find(Rss_.mUrl, rss.getUrl());

        if (!rssWithSameUrl.isEmpty()) {
            return false;
        }

        mRssBox.put(rss);

        return true;
    }

    @NonNull
    public List<RssArticle> getArticles(final Rss rss) throws Exception {
        return mArticleBox.query()
                .equal(RssArticle_.mRssId, rss.getId())
                .build()
                .find();
    }

    @NonNull
    public List<Rss> getAllRss() throws Exception {
        final List<Rss> result = mRssBox.getAll();

        return result == null ? new ArrayList<>() : result;
    }

    public void saveArticles(final Rss rss, final List<RssArticle> articles) {
        mArticleBox.query()
                .equal(RssArticle_.mRssId, rss.getId())
                .build()
                .remove();

        for (final RssArticle article : articles) {
            article.setRssId(rss.getId());
            mArticleBox.put(article);
        }
    }
}
