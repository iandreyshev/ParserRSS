package ru.iandreyshev.parserrss.models.async;

import android.os.AsyncTask;

import ru.iandreyshev.parserrss.models.async.listener.IOnCancelledListener;
import ru.iandreyshev.parserrss.models.async.listener.IOnErrorListener;
import ru.iandreyshev.parserrss.models.async.listener.IOnProgressListener;
import ru.iandreyshev.parserrss.models.async.listener.IOnSuccessListener;

public abstract class Task<TParams, TProgress, TResult, TError> extends AsyncTask<TParams, TProgress, TResult> {
    private IOnSuccessListener<TResult> mOnSuccessListener;
    private IOnProgressListener<TProgress> mOnProgressListener;
    private IOnErrorListener<TError> mOnErrorListener;
    private IOnCancelledListener mOnCancelled;
    private TError mError;

    public final Task<TParams, TProgress, TResult, TError> setSuccessListener(IOnSuccessListener<TResult> listener) {
        mOnSuccessListener = listener;

        return this;
    }

    public final Task<TParams, TProgress, TResult, TError> setProgressListener(IOnProgressListener<TProgress> listener) {
        mOnProgressListener = listener;

        return this;
    }

    public final Task<TParams, TProgress, TResult, TError> setErrorListener(IOnErrorListener<TError> listener) {
        mOnErrorListener = listener;

        return this;
    }

    public final Task<TParams, TProgress, TResult, TError> setOnCancelledListener(IOnCancelledListener listener) {
        mOnCancelled = listener;

        return this;
    }

    @Override
    protected void onProgressUpdate(TProgress[] progress) {
        if (mOnProgressListener != null) {
            mOnProgressListener.onProcessEvent(progress);
        }
    }

    @Override
    protected void onPostExecute(TResult result) {
        super.onPostExecute(result);

        if (mError == null) {
            successEvent(result);
        } else {
            errorEvent(mError);
        }
    }

    @Override
    protected void onCancelled() {
        cancelEvent();
    }

    @Override
    protected void onCancelled(TResult result) {
        cancelEvent();
    }

    protected final void setError(TError error) {
        mError = error;
    }

    private void cancelEvent() {
        if (mOnCancelled != null) {
            mOnCancelled.onCancel();
        }
    }

    private void errorEvent(TError error) {
        if (mOnErrorListener != null) {
            mOnErrorListener.onErrorEvent(error);
        }
    }

    private void successEvent(TResult result) {
        if (mOnSuccessListener != null) {
            mOnSuccessListener.onSuccessEvent(result);
        }
    }
}
