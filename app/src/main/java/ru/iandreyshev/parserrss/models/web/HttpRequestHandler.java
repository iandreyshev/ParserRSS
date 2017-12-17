package ru.iandreyshev.parserrss.models.web;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpRequestHandler {
    private static final String TAG = "NewWorker";

    private static final int GOOD_RESPONSE_CODE = 200;
    private static final int CONNECTION_TIMEOUT_SEC = 1;
    private static final int READ_TIMEOUT_SEC = 1;
    private static final int WRITE_TIMEOUT_SEC = 1;

    private OkHttpClient mClient = new OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SEC, TimeUnit.SECONDS)
            .build();
    private State mState;
    private byte[] mBody;
    private Url mUrl;

    public enum State {
        Success,
        InvalidUrl,
        BadConnection,
        PermissionDenied,
    }

    public void sendGet(final String url) {
        if (!prepareUrl(url)) {
            return;
        }

        sendGet(mUrl);
    }

    public void sendGet(final Url url) {
        if (url == null) {
            mState = State.InvalidUrl;

            return;
        }

        mUrl = url;

        send(new Request.Builder()
                .url(mUrl.getInstance())
                .build());
    }

    public State getState() {
        return mState;
    }

    public byte[] getResponseBody() {
        if (mState != State.Success || mBody == null) {
            return null;
        }

        return mBody;
    }

    public String getUrl() {
        return mUrl.toString();
    }

    private boolean prepareUrl(final String url) {
        if (url == null) {
            mState = State.InvalidUrl;

            return false;
        }

        mUrl = Url.parse(url);

        if (mUrl == null) {
            mState = State.InvalidUrl;

            return false;
        }

        return true;
    }

    private void send(Request request) {
        try (final Response response = mClient.newCall(request).execute()) {

            final boolean isResponseValid = response.code() == GOOD_RESPONSE_CODE;
            mState = isResponseValid ? State.Success : State.BadConnection;

            try (final ResponseBody body = response.body()) {
                mBody = body.bytes();
            } catch (Exception ex) {
            }

        } catch (SecurityException ex) {
            mState = State.PermissionDenied;
        } catch (Exception ex) {
            mState = State.BadConnection;
        }
    }
}
