package ru.iandreyshev.parserrss.models.async;

public interface ITaskListener<TParams, TProgress, TResult, TError> {
    void onCancel(TParams[] params);

    void onErrorEvent(TParams[] params, TError error);

    void onProgress(TProgress[] process);

    void onSuccessEvent(TResult result);
}
