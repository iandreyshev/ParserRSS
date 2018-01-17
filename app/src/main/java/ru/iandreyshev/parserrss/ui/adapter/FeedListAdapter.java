package ru.iandreyshev.parserrss.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.app.Utils;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;
import ru.iandreyshev.parserrss.ui.listeners.IItemImageRequestListener;
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener;

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ListItem> {
    private static final int MAX_SCROLL_SPEED_TO_UPDATE_IMAGES = 15;

    private final LayoutInflater mInflater;
    private final List<IViewArticle> mArticles = new ArrayList<>();
    private final RecyclerView mListView;
    private IOnArticleClickListener mArticleClickListener;
    private IItemImageRequestListener mItemImageRequestListener;

    public FeedListAdapter(Context context, final RecyclerView listView) {
        mInflater = LayoutInflater.from(context);
        mListView = listView;
        mListView.setHasFixedSize(true);
        mListView.addOnScrollListener(new ScrollListener());
    }

    public void setArticleClickListener(@Nullable final IOnArticleClickListener listener) {
        mArticleClickListener = listener;
    }

    public void setImageRequestListener(@Nullable final IItemImageRequestListener listener) {
        mItemImageRequestListener = listener;
    }

    public void setArticles(final List<IViewArticle> newItems) {
        mArticles.clear();
        mArticles.addAll(newItems);
        notifyDataSetChanged();
        updateImages(true);
    }

    @Override
    public ListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.feed_item, parent, false);

        return new ListItem(view);
    }

    @Override
    public void onBindViewHolder(ListItem item, int position) {
        item.setContent(mArticles.get(position));
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public void updateImages(boolean isForce) {
        for (int i = 0; i < mListView.getChildCount(); ++i) {
            final ListItem item = (ListItem) mListView.getChildViewHolder(mListView.getChildAt(i));

            if (mItemImageRequestListener != null) {
                mItemImageRequestListener.getImageFor(item, isForce);
            }
        }
    }

    class ListItem extends RecyclerView.ViewHolder implements IFeedItem, View.OnClickListener {
        private IViewArticle mContent;
        private final TextView mTitle;
        private final TextView mDescription;
        private TextView mDate;
        private ImageView mImage;
        private boolean mIsImageLoaded;

        @Override
        public IViewArticle getArticle() {
            return mContent;
        }

        @Override
        public void updateImage(@NonNull Bitmap image) {
            if (mImage.getDrawable() instanceof BitmapDrawable) {
                ((BitmapDrawable) mImage.getDrawable()).getBitmap().recycle();
            }

            mIsImageLoaded = true;
            mImage.setImageBitmap(image);
        }

        @Override
        public boolean isImageLoaded() {
            return mIsImageLoaded;
        }

        private ListItem(View view) {
            super(view);
            mTitle = view.findViewById(R.id.item_title);
            mDescription = view.findViewById(R.id.item_text);
            mImage = view.findViewById(R.id.item_image);
            mDate = view.findViewById(R.id.item_date);
        }

        private void setContent(IViewArticle content) {
            mContent = content;
            mImage.setImageResource(R.drawable.ic_image_black_24dp);
            mTitle.setText(Html.fromHtml(mContent.getTitle()));
            mDescription.setText(Html.fromHtml(mContent.getDescription()));
            mDate.setText(mContent.getDate() == null ? "" : Utils.toDateStr(mContent.getDate()));
            itemView.setOnClickListener(this);
            mIsImageLoaded = false;
        }

        @Override
        public void onClick(View view) {
            if (mArticleClickListener != null) {
                mArticleClickListener.onArticleClick(mContent);
            }
        }
    }

    class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (Math.abs(dy) <= MAX_SCROLL_SPEED_TO_UPDATE_IMAGES) {
                updateImages(false);
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            updateImages(false);
        }
    }
}
