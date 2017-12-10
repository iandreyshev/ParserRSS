package ru.iandreyshev.parserrss.presentation.presenter.task;

import android.os.AsyncTask;

import ru.iandreyshev.parserrss.presentation.presenter.task.listeners.IOnErrorListener;
import ru.iandreyshev.parserrss.presentation.presenter.task.listeners.IOnProcessListener;
import ru.iandreyshev.parserrss.presentation.presenter.task.listeners.IOnSuccessListener;

public abstract class Task<TParams, TProcess, TResult, TError> extends AsyncTask<TParams, TProcess, TResult> {
    private IOnSuccessListener<TResult> mOnSuccessListener;
    private IOnProcessListener<TProcess> mOnProcessListener;
    private IOnErrorListener<TError> mOnErrorListener;
    private TError mError;

    public final Task<TParams, TProcess, TResult, TError> setSuccessListener(IOnSuccessListener<TResult> listener) {
        mOnSuccessListener = listener;

        return this;
    }

    public final Task<TParams, TProcess, TResult, TError> setProcessListener(IOnProcessListener<TProcess> listener) {
        mOnProcessListener = listener;

        return this;
    }

    public final Task<TParams, TProcess, TResult, TError> setErrorListener(IOnErrorListener<TError> listener) {
        mOnErrorListener = listener;

        return this;
    }

    protected final void processEvent(TProcess process) {
        mOnProcessListener.onProcessEvent(process);
    }

    protected final void cancel(TError error) {
        mError = error;
        cancel(false);
    }

    @Override
    protected final void onPostExecute(TResult result) {
        super.onPostExecute(result);
        successEvent(result);
    }

    @Override
    protected final void onCancelled() {
        errorEvent(mError);
    }

    private void errorEvent(TError error) {
        mOnErrorListener.onErrorEvent(error);
    }

    private void successEvent(TResult result) {
        mOnSuccessListener.onSuccessEvent(result);
    }
}
