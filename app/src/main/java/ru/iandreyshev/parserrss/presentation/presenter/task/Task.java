package ru.iandreyshev.parserrss.presentation.presenter.task;

import android.os.AsyncTask;

import ru.iandreyshev.parserrss.presentation.presenter.task.listeners.IOnErrorListener;
import ru.iandreyshev.parserrss.presentation.presenter.task.listeners.IOnSuccessListener;

public abstract class Task<TParams, TProcess, TResult, TError> extends AsyncTask<TParams, TProcess, TResult> {
    private IOnSuccessListener<TResult> onSuccessListener;
    private IOnErrorListener<TError> onErrorListener;
    private TError errorKind;

    public final Task<TParams, TProcess, TResult, TError> setSuccessListener(IOnSuccessListener<TResult> listener) {
        onSuccessListener = listener;

        return this;
    }

    public final Task<TParams, TProcess, TResult, TError> setErrorListener(IOnErrorListener<TError> listener) {
        onErrorListener = listener;

        return this;
    }

    protected final void successEvent(TResult result) {
        onSuccessListener.onSuccessEvent(result);
    }

    protected final void errorEvent(TError error) {
        onErrorListener.onErrorEvent(error);
    }

    protected final void cancel(TError error) {
        errorKind = error;
        cancel(false);
    }

    @Override
    protected final void onPostExecute(TResult result) {
        super.onPostExecute(result);
        successEvent(result);
    }

    @Override
    protected final void onCancelled() {
        errorEvent(errorKind);
    }
}
