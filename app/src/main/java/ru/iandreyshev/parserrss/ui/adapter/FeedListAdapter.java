package ru.iandreyshev.parserrss.ui.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.app.Utils;
import ru.iandreyshev.parserrss.models.rss.ViewArticle;
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener;

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ListItem> {
    private static final String DEFAULT_DATE_TEXT = "";

    private final List<ViewArticle> mArticles = new ArrayList<>();
    private final HashSet<ListItem> mItemsOnWindow = new HashSet<>();
    private WeakReference<IOnArticleClickListener> mArticleClickListener;

    public void setArticles(final List<ViewArticle> newItems) {
        mArticles.clear();
        mArticles.addAll(newItems);
        notifyDataSetChanged();
    }

    public void setArticleClickListener(@Nullable final IOnArticleClickListener listener) {
        mArticleClickListener = new WeakReference<>(listener);
    }

    @Override
    public ListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.feed_item, parent, false);

        return new ListItem(view);
    }

    @Override
    public void onBindViewHolder(ListItem item, int position) {
        item.setContent(mArticles.get(position));
        item.setClickListener(mArticleClickListener);
    }

    @Override
    public void onViewAttachedToWindow(ListItem item) {
        mItemsOnWindow.add(item);
    }

    @Override
    public void onViewDetachedFromWindow(ListItem item) {
        mItemsOnWindow.remove(item);
    }

    public Collection<ListItem> getItemsOnWindow() {
        return mItemsOnWindow;
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public static class ListItem extends RecyclerView.ViewHolder implements IFeedItem, View.OnClickListener {
        private long mId;
        private boolean mIsImageLoaded;
        private WeakReference<IOnArticleClickListener> mClickListener;

        @BindView(R.id.item_title)
        TextView mTitle;
        @BindView(R.id.item_text)
        TextView mDescription;
        @BindView(R.id.item_image)
        ImageView mImage;
        @BindView(R.id.item_date)
        TextView mDate;

        @Override
        public long getId() {
            return mId;
        }

        @Override
        public void updateImage(@NonNull Bitmap image) {
            recycleImage();
            mIsImageLoaded = true;
            mImage.setImageBitmap(image);
        }

        @Override
        public boolean isImageLoaded() {
            return mIsImageLoaded;
        }

        public void recycleImage() {
            if (mImage.getDrawable() instanceof BitmapDrawable) {
                final Bitmap bitmap = ((BitmapDrawable) mImage.getDrawable()).getBitmap();

                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
        }

        private ListItem(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void setContent(ViewArticle content) {
            mId = content.getId();
            mTitle.setText(Html.fromHtml(content.getTitle()));
            mDescription.setText(Html.fromHtml(content.getDescription()));

            updateImage(BitmapFactory.decodeResource(itemView.getContext().getResources(), R.drawable.ic_image_black_24dp));
            mDate.setText(Utils.toDateString(content.getDate()));

            itemView.setOnClickListener(this);
            mIsImageLoaded = false;
        }

        private void setClickListener(WeakReference<IOnArticleClickListener> listener) {
            mClickListener = listener;
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null && mClickListener.get() != null) {
                mClickListener.get().onArticleClick(mId);
            }
        }
    }
}
