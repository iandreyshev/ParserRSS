package ru.iandreyshev.parserrss.presentation.presenter.feed;

import ru.iandreyshev.parserrss.models.feed.Article;
import ru.iandreyshev.parserrss.presentation.view.feed.FeedView;
import ru.iandreyshev.parserrss.util.FeedItemPref;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class FeedPresenter extends MvpPresenter<FeedView> {
    public void onSettingsButtonClick() {
        getViewState().openSettings();
    }

    public void onRefresh() {
        for (int i = 0; i < 10; ++i) {
            addItem();
        }
    }

    public void onItemClick(int id) {
        FeedItemPref.save(getNewArticle());
        getViewState().openArticle();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        onRefresh();
    }

    private void addItem() {
        getViewState().addItem(getNewArticle());
    }

    private Article getNewArticle() {
        return new Article(
                1,
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
    }
}
