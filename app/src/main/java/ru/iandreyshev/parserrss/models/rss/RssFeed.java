package ru.iandreyshev.parserrss.models.rss;

import android.os.Parcel;
import android.os.Parcelable;

public final class RssFeed implements Parcelable {
    private static final int DEFAULT_HASH = 11;

    private String mUrl;
    private String mOrigin;
    private String mTitle;
    private String mDescription;
    private int mHash;

    RssFeed(final String title, final String origin) throws NullPointerException {
        mTitle = title;
        mOrigin = origin;
    }

    protected RssFeed(Parcel in) {
        mUrl = in.readString();
        mOrigin = in.readString();
        mTitle = in.readString();
        mDescription = in.readString();
        mHash = in.readInt();
    }

    public static final Creator<RssFeed> CREATOR = new Creator<RssFeed>() {
        @Override
        public RssFeed createFromParcel(Parcel in) {
            return new RssFeed(in);
        }

        @Override
        public RssFeed[] newArray(int size) {
            return new RssFeed[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setDescription(final String description) {
        mDescription = description;
    }

    public void setUrl(final String url) {
        mUrl = url;
        initHash();
    }

    @Override
    public int hashCode() {
        return mHash;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof RssFeed) && (this.hashCode() == obj.hashCode());
    }

    private void initHash() {
        final String urlStr = mUrl == null ? "" : mUrl;
        mHash = DEFAULT_HASH;

        for (int i = 0; i < urlStr.length(); i++) {
            mHash = mHash * 31 + urlStr.charAt(i);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUrl);
        dest.writeString(mOrigin);
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeInt(mHash);
    }
}
