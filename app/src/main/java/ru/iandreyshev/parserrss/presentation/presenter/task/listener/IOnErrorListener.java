package ru.iandreyshev.parserrss.presentation.presenter.task.listener;

public interface IOnErrorListener<TError> {
    void onErrorEvent(TError error);
}
