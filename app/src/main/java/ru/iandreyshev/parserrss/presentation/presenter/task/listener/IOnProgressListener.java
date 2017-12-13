package ru.iandreyshev.parserrss.presentation.presenter.task.listener;

public interface IOnProgressListener<TProcess> {
    void onProcessEvent(TProcess[] process);
}
