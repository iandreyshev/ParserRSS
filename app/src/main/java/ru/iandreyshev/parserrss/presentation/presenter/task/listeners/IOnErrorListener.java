package ru.iandreyshev.parserrss.presentation.presenter.task.listeners;

public interface IOnErrorListener<TError> {
    void onErrorEvent(TError error);
}
