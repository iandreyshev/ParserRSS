package ru.iandreyshev.parserrss.presentation.presenter;

import android.support.annotation.NonNull;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.async.GetArticleFromDbTask;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;
import ru.iandreyshev.parserrss.models.rss.IViewRss;
import ru.iandreyshev.parserrss.presentation.view.IArticleView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class ArticlePresenter extends MvpPresenter<IArticleView> {
    private Long mArticleId;

    public void setArticleId(long articleId) {
        if (mArticleId != null) {
            return;
        }

        mArticleId = articleId;
        GetArticleFromDbTask.execute(mArticleId, new GetArticleFromDbListener());
    }

    private class GetArticleFromDbListener implements GetArticleFromDbTask.IEventListener {
        @Override
        public void onPreExecute() {
        }

        @Override
        public void onPostExecute(Void result) {
        }

        @Override
        public void onSuccess(final @NonNull IViewRss rss, final @NonNull IViewArticle article) {
            getViewState().initArticle(rss, article);
        }

        @Override
        public void onFail() {
            getViewState().showShortToast(App.getStr(R.string.article_error_load));
            getViewState().closeArticle();
        }
    }
}
