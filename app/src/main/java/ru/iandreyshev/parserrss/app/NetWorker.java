package ru.iandreyshev.parserrss.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    public void send(final String url) {
        mUrl = HttpUrl.parse(url);

        if (mUrl == null) {
            mStatus = Status.BadUrl;

            return;
        }

        send(new Request.Builder()
                .url(url)
                .build());
    }

    public Status getStatus() {
        return mStatus;
    }

    public String getResponseAsText() {
        try {
            return mResponse.body().string();
        } catch (Exception ex) {
            return null;
        }
    }

    public Bitmap getResponseAsBitmap() {
        Bitmap result = null;

        try {
            InputStream stream = mResponse.body().byteStream();
            result = BitmapFactory.decodeStream(stream);

            return result;

        } catch (Exception ex) {
            return null;
        }
    }

    public HttpUrl getUrl() {
        return mUrl;
    }

    private void send(Request request) {
        try (Response response = mClient.newCall(request).execute()) {

            final boolean isResponseValid = response.code() == GOOD_RESPONSE_CODE;
            mStatus = isResponseValid ? Status.Success : Status.BadConnection;
            mResponse = response;

        } catch (Exception ex) {

            mStatus = Status.BadConnection;
            // TODO: create error log

        }
    }
}
