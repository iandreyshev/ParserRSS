package ru.iandreyshev.parserrss.models.web;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpRequestHandler implements IHttpRequestResult {
    private static final String TAG = HttpRequestHandler.class.getName();

    private static final int GOOD_RESPONSE_CODE = 200;
    private static final int READ_TIMEOUT_SEC = 1;
    private static final int CONNECTION_TIMEOUT_SEC = 1;
    private static final int WRITE_TIMEOUT_SEC = 1;

    private final OkHttpClient mClient = new OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SEC, TimeUnit.SECONDS)
            .build();
    private State mState = State.NotSend;
    private String mBody;
    private Url mUrl;

    public HttpRequestHandler(final String urlString) {
        setUrl(urlString);
    }

    public IHttpRequestResult sendGet() {
        send(new Request.Builder()
                .url(mUrl.getInstance())
                .build());

        return this;
    }

    @Override
    public State getState() {
        return mState;
    }

    @Override
    public String getResponseBody() {
        return mBody;
    }

    @Override
    public String getUrlStr() {
        return mUrl.toString();
    }

    public void setUrl(final String url) {
        mUrl = Url.parse(url);
        mState = mUrl == null ? State.BadUrl : State.NotSend;
    }

    private void send(Request request) {
        try (final Response response = mClient.newCall(request).execute()) {

            try (final ResponseBody body = response.body()) {

                if (response.code() != GOOD_RESPONSE_CODE || body == null) {
                    mState = State.BadConnection;

                    return;
                }
                mBody = body.string();
                mState = State.Success;

            } catch (Exception ex) {
                mState = State.BadConnection;
            }

        } catch (SecurityException ex) {
            mState = State.PermissionDenied;
        } catch (Exception ex) {
            mState = State.BadConnection;
        }
    }
}
