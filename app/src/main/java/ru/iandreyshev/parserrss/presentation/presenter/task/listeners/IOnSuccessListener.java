package ru.iandreyshev.parserrss.presentation.presenter.task.listeners;

public interface IOnSuccessListener<TResult> {
    void onSuccessEvent(TResult result);
}
