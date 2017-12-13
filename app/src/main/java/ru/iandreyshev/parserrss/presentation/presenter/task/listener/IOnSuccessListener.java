package ru.iandreyshev.parserrss.presentation.presenter.task.listener;

public interface IOnSuccessListener<TResult> {
    void onSuccessEvent(TResult result);
}
