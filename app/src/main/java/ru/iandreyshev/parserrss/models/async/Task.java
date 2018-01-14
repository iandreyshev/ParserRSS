package ru.iandreyshev.parserrss.models.async;

import android.os.AsyncTask;
import android.support.annotation.CallSuper;

import ru.iandreyshev.parserrss.app.IEvent;

abstract class Task<T, U, V> extends AsyncTask<T, U, V> {
    private ITaskListener<V> mListener;
    private IEvent mResultEvent;

    Task(final ITaskListener<V> listener) {
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
        if (mResultEvent != null) {
            mResultEvent.doEvent();
        }
    }

    void setResultEvent(final IEvent resultEvent) {
        mResultEvent = resultEvent;
    }
}
