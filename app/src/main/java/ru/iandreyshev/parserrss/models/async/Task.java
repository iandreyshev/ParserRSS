package ru.iandreyshev.parserrss.models.async;

import android.os.AsyncTask;

public abstract class Task<TParams, TProgress, TResult, TError> extends AsyncTask<TParams, TProgress, TResult> {
    private ITaskListener<TParams, TProgress, TResult, TError> mListener;
    private TParams[] mParams;
    private TError mError;

    public final Task<TParams, TProgress, TResult, TError> setListener(ITaskListener<TParams, TProgress, TResult, TError> listener) {
        mListener = listener;

        return this;
    }

    protected abstract TResult behaviourProcess(TParams[] params);

    @Override
    protected final TResult doInBackground(TParams[] params) {
        mParams = params;

        return behaviourProcess(mParams);
    }

    @Override
    protected final void onProgressUpdate(TProgress[] progress) {
        if (mListener != null) {
            mListener.onProgress(progress);
        }
    }

    @Override
    protected final void onPostExecute(TResult result) {
        super.onPostExecute(result);

        if (mError == null) {
            successEvent(result);
        } else {
            errorEvent(mError);
        }
    }

    @Override
    protected final void onCancelled() {
        cancelEvent();
    }

    @Override
    protected final void onCancelled(TResult result) {
        cancelEvent();
    }

    protected final void setError(TError error) {
        mError = error;
    }

    private void cancelEvent() {
        if (mListener != null) {
            mListener.onCancel(mParams);
        }
    }

    private void errorEvent(TError error) {
        if (mListener != null) {
            mListener.onErrorEvent(mParams, error);
        }
    }

    private void successEvent(TResult result) {
        if (mListener != null) {
            mListener.onSuccessEvent(result);
        }
    }
}
