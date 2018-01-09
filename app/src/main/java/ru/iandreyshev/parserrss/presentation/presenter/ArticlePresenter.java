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
    private Long mArticleId;

    public void setArticleId(long articleId) {
        if (mArticleId != null) {
            return;
        }

        mArticleId = articleId;
        GetArticleFromDbTask.execute(mArticleId, new GetArticleFromDbListener());
    }

    private class GetArticleFromDbListener implements ITaskListener<IViewArticle> {
        @Override
        public void onPreExecute() {
        }

        @Override
        public void onPostExecute(final IViewArticle result) {
            if (result == null) {
                getViewState().showShortToast(App.getStr(R.string.article_error_load));
                getViewState().openFeed();
            } else {
                getViewState().initArticle(result);
            }
        }
    }
}
