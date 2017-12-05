package ru.iandreyshev.parserrss.presentation.presenter.feed.refreshTask;

public interface IOnErrorListener {
    void onErrorEvent(RefreshTask.RefreshError error);
}
