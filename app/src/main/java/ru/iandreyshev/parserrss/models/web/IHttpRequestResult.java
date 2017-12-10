package ru.iandreyshev.parserrss.models.web;

public interface IHttpRequestResult {
    enum State {
        NotSend,
        Success,
        BadUrl,
        BadConnection,
        PermissionDenied,
    }

    HttpRequestHandler.State getState();

    String getResponseBody();

    String getUrl();
}
