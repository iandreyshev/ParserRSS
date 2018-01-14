package ru.iandreyshev.parserrss.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener;
import ru.iandreyshev.parserrss.ui.listeners.IOnImageInsertListener;

public class ArticlesListAdapter extends RecyclerView.Adapter<ArticlesListAdapter.ListItem> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

    private final LayoutInflater mInflater;
    private final List<IViewArticle> mArticles = new ArrayList<>();
    private IOnArticleClickListener mArticleClickListener;

    public ArticlesListAdapter(Context context, final RecyclerView listView) {
        mInflater = LayoutInflater.from(context);
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
    }

    @Override
    public void onViewDetachedFromWindow(ListItem item) {

    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    class ListItem extends RecyclerView.ViewHolder implements IOnImageInsertListener {
        private IViewArticle mContent;
        private final TextView mTitle;
        private final TextView mDescription;
        private TextView mDate;
        private ImageView mImage;
        private final IOnArticleClickListener mClickListener;

        ListItem(View view, IOnArticleClickListener clickListener) {
            super(view);
            mTitle = view.findViewById(R.id.item_title);
            mDescription = view.findViewById(R.id.item_text);
            mImage = view.findViewById(R.id.item_image);
            mDate = view.findViewById(R.id.item_date);
            mClickListener = clickListener;
        }

        void setContent(IViewArticle content) {
            mContent = content;
            mTitle.setText(Html.fromHtml(mContent.getTitle()));
            mDescription.setText(Html.fromHtml(mContent.getDescription()));

            itemView.setOnClickListener(v -> {
                if (mClickListener != null) {
                    mClickListener.onArticleClick(mContent);
                }
            });

            updateImage(Utils.toBitmap(mContent.getImage()));
            updateDate();
        }

        void updateImage(final Bitmap image) {
            if (image != null) {
                mImage.setImageBitmap(image);
            } else {
                mImage.setImageResource(R.drawable.ic_image_black_24dp);
            }
        }

        void updateDate() {
            if (mContent.getPostDate() != null) {
                mDate.setText(DATE_FORMAT.format(mContent.getPostDate()));
            } else {
                mDate.setText(R.string.feed_item_default_date);
            }
        }

        @Override
        public void onImageInsert(@NonNull final Bitmap image) {
            updateImage(image);
        }
    }
}
