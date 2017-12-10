package ru.iandreyshev.parserrss.presentation.presenter.task.listeners;

public interface IOnProcessListener<TProcess> {
    void onProcessEvent(TProcess process);
}
