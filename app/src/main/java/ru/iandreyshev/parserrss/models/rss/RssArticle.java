package ru.iandreyshev.parserrss.models.rss;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public final class RssArticle implements Parcelable {
    private String mTitle;
    private String mOrigin;
    private String mDescription;
    private Bitmap mImage;
    private String mImageUrl;
    private Long mDate;

    RssArticle(final String title, final String origin) {
        mTitle = title;
        mOrigin = origin;
    }

    protected RssArticle(Parcel in) {
        mTitle = in.readString();
        mOrigin = in.readString();
        mDescription = in.readString();
        mImage = in.readParcelable(Bitmap.class.getClassLoader());
        mImageUrl = in.readString();
        mDate = in.readLong();
    }

    public static final Creator<RssArticle> CREATOR = new Creator<RssArticle>() {
        @Override
        public RssArticle createFromParcel(Parcel in) {
            return new RssArticle(in);
        }

        @Override
        public RssArticle[] newArray(int size) {
            return new RssArticle[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public String getDescription() {
        return mDescription;
    }

    public Date getDate() {
        return mDate == null ? null : new Date(mDate);
    }

    public Bitmap getImage() {
        return mImage;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    void setTitle(String title) {
        mTitle = title;
    }

    void setDescription(String text) {
        mDescription = text;
    }

    void setImage(Bitmap image) {
        mImage = image;
    }

    void setDate(Date date) {
        mDate = date.getTime();
    }

    void setImageUrl(String url) {
        mImageUrl = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mOrigin);
        dest.writeString(mDescription);
        dest.writeParcelable(mImage, flags);
        dest.writeString(mImageUrl);
        dest.writeLong(mDate);
    }
}
