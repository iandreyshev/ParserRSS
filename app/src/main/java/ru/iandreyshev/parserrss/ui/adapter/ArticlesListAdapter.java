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

import java.util.ArrayList;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.models.rss.RssArticle;

public class ArticlesListAdapter extends RecyclerView.Adapter<ArticlesListAdapter.ViewHolder> {
    private static final String TAG = ArticlesListAdapter.class.getName();

    private LayoutInflater mInflater;
    private IOnArticleClickListener mArticleClickListener;
    private ArrayList<RssArticle> mArticles = new ArrayList<>();

    public ArticlesListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setItemClickListener(IOnArticleClickListener listener) {
        this.mArticleClickListener = listener;
    }

    public void setArticles(final ArrayList<RssArticle> newItems) {
        mArticles = newItems;
        Log.e(TAG, Integer.toString(mArticles.size()));
        notifyDataSetChanged();
    }

    public ArrayList<RssArticle> getArticles() {
        return mArticles;
    }

    @Override
    public ArticlesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.feed_item, parent, false);

        return new ViewHolder(view, mArticleClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RssArticle article = mArticles.get(position);

        if (article != null) {
            holder.setContent(article);
        }
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RssArticle mContent;
        private TextView mTitle;
        private TextView mDescription;
        private ImageView mImage;
        private IOnArticleClickListener mClickListener;

        ViewHolder(View view, IOnArticleClickListener clickListener) {
            super(view);

            mTitle = view.findViewById(R.id.item_title);
            mDescription = view.findViewById(R.id.item_text);
            mImage = view.findViewById(R.id.item_image);
            mClickListener = clickListener;

            view.setOnClickListener(v -> {
                if (mClickListener != null) {
                    mClickListener.onItemClick(mContent);
                }
            });
        }

        void setContent(RssArticle content) {
            this.mContent = content;

            mTitle.setText(Html.fromHtml(mContent.getTitle()));
            mDescription.setText(Html.fromHtml(mContent.getDescription()));

            loadImage();
        }

        void loadImage() {
            if (mContent.getImage() == null) {
                mImage.setVisibility(View.GONE);
            } else {
                mImage.setVisibility(View.VISIBLE);
            }
        }
    }
}
