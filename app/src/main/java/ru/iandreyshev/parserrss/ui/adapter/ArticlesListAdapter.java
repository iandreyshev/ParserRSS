package ru.iandreyshev.parserrss.ui.adapter;

import android.content.Context;
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
import ru.iandreyshev.parserrss.models.rss.IRssArticle;

public class ArticlesListAdapter extends RecyclerView.Adapter<ArticlesListAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private IOnArticleClickListener mArticleClickListener;
    private List<IRssArticle> mList = new ArrayList<>();

    public ArticlesListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setItemClickListener(IOnArticleClickListener listener) {
        this.mArticleClickListener = listener;
    }

    public void setArticles(List<IRssArticle> newItems) {
        clear();
        mList.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public ArticlesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.feed_item, parent, false);

        return new ViewHolder(view, mArticleClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final IRssArticle article = mList.get(position);

        if (article != null) {
            holder.setContent(article);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private IRssArticle mContent;
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

        void setContent(IRssArticle content) {
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
