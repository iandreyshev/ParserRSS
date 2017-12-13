package ru.iandreyshev.parserrss.app;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
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

    public enum Status {
        NotSend,
        Success,
        BadUrl,
        BadConnection,
        PermissionDenied,
    }

    private OkHttpClient mClient = new OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SEC, TimeUnit.SECONDS)
            .build();
    private Status mStatus = Status.NotSend;
    private byte[] mBody;
    private HttpUrl mUrl;

    public void sendGet(final String url) {
        if (!prepareUrl(url)) {
            return;
        }

        sendGet(mUrl);
    }

    public void sendGet(final HttpUrl url) {
        mUrl = url;

        send(new Request.Builder()
                .url(mUrl)
                .addHeader("content-type", "application/json")
                .build());
    }

    public Status getStatus() {
        return mStatus;
    }

    public byte[] getResponseBody() {
        if (mStatus != Status.Success || mBody == null) {
            return null;
        }

        return mBody;
    }

    public String getUrl() {
        return mUrl.toString();
    }

    private boolean prepareUrl(final String url) {
        mUrl = HttpUrl.parse(url);

        if (mUrl == null) {
            mStatus = Status.BadUrl;

            return false;
        }

        return true;
    }

    private void send(Request request) {
        try (final Response response = mClient.newCall(request).execute()) {

            final boolean isResponseValid = response.code() == GOOD_RESPONSE_CODE;
            mStatus = isResponseValid ? Status.Success : Status.BadConnection;

            try (final ResponseBody body = response.body()) {
                mBody = body.bytes();
            } catch (Exception ex) {
            }

        } catch (SecurityException ex) {
            mStatus = Status.PermissionDenied;
        } catch (Exception ex) {
            mStatus = Status.BadConnection;
        }
    }
}
