package ru.iandreyshev.parserrss.models.async.listener;

public interface IOnProgressListener<TProcess> {
    void onProcessEvent(TProcess[] process);
}
