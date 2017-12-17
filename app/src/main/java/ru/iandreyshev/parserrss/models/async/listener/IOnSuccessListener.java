package ru.iandreyshev.parserrss.models.async.listener;

public interface IOnSuccessListener<TResult> {
    void onSuccessEvent(TResult result);
}
