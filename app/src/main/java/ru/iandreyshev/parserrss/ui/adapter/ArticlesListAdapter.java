package ru.iandreyshev.parserrss.ui.adapter;

import android.content.Context;
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
import java.util.Locale;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.models.rss.IViewRssArticle;
import ru.iandreyshev.parserrss.ui.listeners.IOnArticleClickListener;

public class ArticlesListAdapter extends RecyclerView.Adapter<ArticlesListAdapter.ViewHolder> {
    private static final String TAG = ArticlesListAdapter.class.getName();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

    private LayoutInflater mInflater;
    private IOnArticleClickListener mArticleClickListener;
    private ArrayList<IViewRssArticle> mArticles = new ArrayList<>();

    public ArticlesListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setArticleClickListener(final IOnArticleClickListener listener) {
        mArticleClickListener = listener;
    }

    public IOnArticleClickListener getArticleClickListener() {
        return mArticleClickListener;
    }

    public void setArticles(final ArrayList<IViewRssArticle> newItems) {
        mArticles = newItems;
        Log.e(TAG, Integer.toString(mArticles.size()));
        notifyDataSetChanged();
    }

    public ArrayList<IViewRssArticle> getArticles() {
        return mArticles;
    }

    @Override
    public ArticlesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.feed_item, parent, false);

        return new ViewHolder(view, mArticleClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final IViewRssArticle article = mArticles.get(position);

        if (article != null) {
            holder.setContent(article);
        }
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private IViewRssArticle mContent;
        private TextView mTitle;
        private TextView mDescription;
        private TextView mDate;
        private ImageView mImage;
        private IOnArticleClickListener mClickListener;

        ViewHolder(View view, IOnArticleClickListener clickListener) {
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

        void setContent(IViewRssArticle content) {
            this.mContent = content;

            mTitle.setText(Html.fromHtml(mContent.getTitle()));
            mDescription.setText(Html.fromHtml(mContent.getDescription()));

            loadImage();
            loadDate();
        }

        void loadImage() {
            if (mContent.getImage() != null) {
                mImage.setImageBitmap(mContent.getImage());
            }
        }

        void loadDate() {
            if (mContent.getPostDate() != null) {
                mDate.setText(DATE_FORMAT.format(mContent.getPostDate()));
            }
        }
    }
}
