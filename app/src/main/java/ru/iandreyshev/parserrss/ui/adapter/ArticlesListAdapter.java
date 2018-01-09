package ru.iandreyshev.parserrss.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.app.Utils;
import ru.iandreyshev.parserrss.models.rss.IViewArticle;
import ru.iandreyshev.parserrss.presentation.view.IFeedItemView;
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener;
import ru.iandreyshev.parserrss.ui.listeners.IOnImageInsertListener;
import ru.iandreyshev.parserrss.ui.listeners.IOnImageRequestListener;

public class ArticlesListAdapter extends RecyclerView.Adapter<ArticlesListAdapter.ListItem> {
    private static final int MIN_SCROLL_SPEED_TO_LOAD_IMAGE = 20;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

    private final LayoutInflater mInflater;
    private final List<IViewArticle> mArticles = new ArrayList<>();
    private IOnArticleClickListener mArticleClickListener;
    private IOnImageRequestListener mOnImageRequestListener;
    private int mScrollVelocity;

    public ArticlesListAdapter(Context context, final RecyclerView listView) {
        mInflater = LayoutInflater.from(context);
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollVelocity = Math.abs(dy);
            }
        });
    }

    public void setImageRequestListener(final IOnImageRequestListener listener) {
        mOnImageRequestListener = listener;
    }

    public void setArticleClickListener(final IOnArticleClickListener listener) {
        mArticleClickListener = listener;
    }

    public void setArticles(final List<IViewArticle> newItems) {
        mArticles.clear();
        mArticles.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public ListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.feed_item, parent, false);

        return new ListItem(view, mArticleClickListener);
    }

    @Override
    public void onBindViewHolder(ListItem holder, int position) {
        holder.setContent(mArticles.get(position));

        if (mOnImageRequestListener != null && mScrollVelocity < MIN_SCROLL_SPEED_TO_LOAD_IMAGE) {
            mOnImageRequestListener.loadImage(holder.mContent, holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(ListItem holder){
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    class ListItem extends RecyclerView.ViewHolder implements IOnImageInsertListener {
        private IViewArticle mContent;
        private final TextView mTitle;
        private final TextView mDescription;
        private final TextView mDate;
        private final ImageView mImage;
        private final IOnArticleClickListener mClickListener;

        ListItem(View view, IOnArticleClickListener clickListener) {
            super(view);

            mTitle = view.findViewById(R.id.item_title);
            mDescription = view.findViewById(R.id.item_text);
            mImage = view.findViewById(R.id.item_image);
            mDate = view.findViewById(R.id.item_date);
            mClickListener = clickListener;

            view.setOnClickListener(v -> {
                if (mClickListener != null) {
                    mClickListener.onArticleClick(mContent);
                }
            });
        }

        void setContent(IViewArticle content) {
            this.mContent = content;

            mTitle.setText(Html.fromHtml(mContent.getTitle()));
            mDescription.setText(Html.fromHtml(mContent.getDescription()));

            loadImage(Utils.toBitmap(mContent.getImage()));
            loadDate();
        }

        void loadImage(final Bitmap image) {
            if (image != null) {
                mImage.setImageBitmap(image);
            }
        }

        void loadDate() {
            if (mContent.getPostDate() != null) {
                mDate.setText(DATE_FORMAT.format(mContent.getPostDate()));
            }
        }

        @Override
        public void insert(@NonNull final Bitmap image) {
            loadImage(image);
        }
    }
}
