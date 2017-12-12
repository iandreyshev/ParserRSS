package ru.iandreyshev.parserrss.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NetWorker {
    private static final int GOOD_RESPONSE_CODE = 200;
    private static final int CONNECTION_TIMEOUT_SEC = 1;
    private static final int READ_TIMEOUT_SEC = 1;
    private static final int WRITE_TIMEOUT_SEC = 1;

    public enum Status {
        NotSend,
        Success,
        BadUrl,
        BadConnection,
    }

    private OkHttpClient mClient = new OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SEC, TimeUnit.SECONDS)
            .build();
    private Status mStatus = Status.NotSend;
    private Response mResponse;
    private HttpUrl mUrl;

    public void sendGet(final String url) {
        if (!prepareUrl(url)) {
            return;
        }

        send(new Request.Builder()
                .url(mUrl)
                .build());
    }

    public Status getStatus() {
        return mStatus;
    }

    public String getResponseAsText() {
        try (final ResponseBody body = mResponse.body()) {

            return body == null ? null : body.string();

        } catch (Exception ex) {
            return null;
        }
    }

    public Bitmap getResponseAsBitmap() {
        try (final ResponseBody body = mResponse.body()) {
            if (body == null) {
                return null;
            }

            final InputStream stream = body.byteStream();
            final Bitmap result = BitmapFactory.decodeStream(stream);
            stream.close();

            return result;

        } catch (Exception ex) {
            return null;
        }
    }

    public HttpUrl getUrl() {
        return mUrl;
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
            mResponse = response;

        } catch (Exception ex) {

            mStatus = Status.BadConnection;
            // TODO: create error log

        }
    }
}
