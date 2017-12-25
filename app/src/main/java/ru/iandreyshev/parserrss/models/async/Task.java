package ru.iandreyshev.parserrss.models.async;

import android.os.AsyncTask;
import android.support.annotation.CallSuper;

abstract class Task<T, U, V> extends AsyncTask<T, U, V> {
    private ITaskListener<V> mListener;

    @CallSuper
    void setTaskListener(final ITaskListener<V> listener) {
        mListener = listener;
    }

    @CallSuper
    @Override
    protected void onPreExecute() {
        if (mListener != null) {
            mListener.onPreExecute();
        }
    }

    @CallSuper
    @Override
    protected void onPostExecute(V result) {
        if (mListener != null) {
            mListener.onPostExecute(result);
        }
    }
}
