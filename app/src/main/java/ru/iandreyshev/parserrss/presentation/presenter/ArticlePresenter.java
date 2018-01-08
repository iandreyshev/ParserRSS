package ru.iandreyshev.parserrss.presentation.presenter;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.app.App;
import ru.iandreyshev.parserrss.models.async.GetArticleFromDbTask;
import ru.iandreyshev.parserrss.models.async.ITaskListener;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;
import ru.iandreyshev.parserrss.presentation.view.IArticleView;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class ArticlePresenter extends MvpPresenter<IArticleView> {
    public void onErrorLoadArticle() {
        getViewState().showShortToast(App.getStr(R.string.article_error_load));
        getViewState().openFeed();
    }

    public void onLoadArticle(long articleId) {
        GetArticleFromDbTask.execute(articleId, new GetArticleFromDbListener());
    }

    private class GetArticleFromDbListener implements ITaskListener<IViewArticle> {
        @Override
        public void onPreExecute() {
            getViewState().startProgressBar(true);
        }

        @Override
        public void onPostExecute(final IViewArticle result) {
            getViewState().startProgressBar(false);

            if (result == null) {
                onErrorLoadArticle();
            } else {
                getViewState().initArticle(result);
            }
        }
    }
}
