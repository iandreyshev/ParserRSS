package ru.iandreyshev.parserrss.models.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.exception.DbException;
import ru.iandreyshev.parserrss.app.App;

public class Database {
    private final BoxStore mBoxStore = App.getBoxStore();
    private Box<Rss> mRssBox;
    private Box<Article> mArticleBox;

    public Database() throws DbException {
        if (mBoxStore == null) {
            throw new DbException("Box-store is null in constructor");
        }

        mRssBox = mBoxStore.boxFor(Rss.class);
        mArticleBox = mBoxStore.boxFor(Article.class);
    }

    public long getRssCount(final String url) {
        return mRssBox.find(Rss_.mUrl, url).size();
    }

    @Nullable
    public Rss getRssById(long id) throws Exception {
        return mRssBox.get(id);
    }

    @Nullable
    public Article getArticleById(long id) throws Exception {
        return mArticleBox.get(id);
    }

    public void updateArticleImage(long id, byte[] bytes) throws Exception {
        final Article article = mArticleBox.get(id);
        article.setImage(bytes);
        mArticleBox.put(article);
    }

    @NonNull
    public List<Rss> getAllRss() throws Exception {
        return mBoxStore.callInTx(() -> {
            final ArrayList<Rss> result = new ArrayList<>();

            for (final Rss rss : mRssBox.getAll()) {
                final List<Article> articles = mArticleBox.query()
                        .equal(Article_.mRssId, rss.getId())
                        .build()
                        .find();
                rss.setArticles(articles);
                result.add(rss);
            }

            return result;
        });
    }

    public boolean putRssIfSameUrlNotExist(final Rss rss) throws Exception {
        return mBoxStore.callInTx(() -> {
            Rss rssWithSameUrl = mRssBox.query()
                    .equal(Rss_.mUrl, rss.getUrl())
                    .build()
                    .findFirst();

            if (rssWithSameUrl != null) {
                return false;
            }

            mArticleBox.query()
                    .equal(Article_.mRssId, rss.getId())
                    .build()
                    .remove();
            mRssBox.put(rss);
            bindArticles(rss);
            mArticleBox.put(rss.getArticles());

            return true;
        });
    }

    public boolean updateRssWithSameUrl(final Rss newRss) throws Exception {
        return mBoxStore.callInTx(() -> {
            Rss rssWithSameUrl = mRssBox.query()
                    .equal(Rss_.mUrl, newRss.getUrl())
                    .build()
                    .findFirst();

            if (rssWithSameUrl == null) {
                return false;
            }

            newRss.setId(rssWithSameUrl.getId());
            bindArticles(newRss);
            mRssBox.put(newRss);

            mArticleBox.query()
                    .equal(Article_.mRssId, newRss.getId())
                    .build()
                    .remove();
            mArticleBox.put(newRss.getArticles());

            return true;
        });
    }

    public void removeRss(long rssId) throws Exception {
        mBoxStore.runInTx(() -> {
            mRssBox.remove(rssId);
            mArticleBox.query()
                    .equal(Article_.mRssId, rssId)
                    .build()
                    .remove();
        });
    }

    private void bindArticles(final Rss rss) {
        for (final Article article : rss.getArticles()) {
            article.setRssId(rss.getId());
        }
    }
}
