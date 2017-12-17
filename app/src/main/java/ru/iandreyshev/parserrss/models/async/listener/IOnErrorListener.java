package ru.iandreyshev.parserrss.models.async.listener;

public interface IOnErrorListener<TError> {
    void onErrorEvent(TError error);
}
