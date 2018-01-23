package ru.iandreyshev.parserrss.models.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpRequestHandler implements IHttpRequestResult {
    private static final int MAX_CONTENT_BYTES = 5242880; // 5MB

    private static final int GOOD_RESPONSE_CODE = 200;
    private static final String DEFAULT_PROTOCOL = "http://";

    private static final int READ_TIMEOUT_SEC = 2;
    private static final int CONNECTION_TIMEOUT_SEC = 2;
    private static final int WRITE_TIMEOUT_SEC = 2;

    private final OkHttpClient mClient = new OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SEC, TimeUnit.SECONDS)
            .build();
    private State mState = State.NotSend;
    private byte[] mBody;
    private Url mUrl;
    private long mMaxContentBytes = MAX_CONTENT_BYTES;

    public HttpRequestHandler(final String urlString) {
        setUrl(urlString);

        if (mState == State.BadUrl) {
            setUrl(DEFAULT_PROTOCOL + urlString);
        }
    }

    public State sendGet() {
        if (mState != State.BadUrl) {
            send(new Request.Builder()
                    .url(mUrl.getInstance())
                    .build());
        }

        return mState;
    }

    public void setMaxContentBytes(long maxBytes) {
        mMaxContentBytes = maxBytes;
    }

    @NonNull
    @Override
    public State getState() {
        return mState;
    }

    @Nullable
    @Override
    public String getResponseBodyAsString() {
        return mBody == null ? null : new String(mBody);
    }

    @Nullable
    @Override
    public byte[] getResponseBody() {
        return mBody;
    }

    @Override
    public String getUrlStr() throws NullPointerException {
        return mUrl.toString();
    }

    private void setUrl(final String url) {
        mUrl = Url.parse(url == null ? "" : url);
        mState = mUrl == null ? State.BadUrl : State.NotSend;
    }

    private void send(Request request) {
        try (final Response response = mClient.newCall(request).execute()) {
            try (final ResponseBody body = response.body()) {
                if (response.code() != GOOD_RESPONSE_CODE || body == null || body.contentLength() > mMaxContentBytes) {
                    mState = State.BadConnection;

                    return;
                }
                mBody = body.bytes();
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
