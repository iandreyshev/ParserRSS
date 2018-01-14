package ru.iandreyshev.parserrss.models.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface IHttpRequestResult {
    enum State {
        NotSend,
        Success,
        BadUrl,
        BadConnection,
        PermissionDenied,
    }

    @NonNull
    HttpRequestHandler.State getState();

    @Nullable
    String getResponseBodyAsString();

    @Nullable
    byte[] getResponseBody();

    @Nullable
    String getUrlStr();
}
