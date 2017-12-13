package ru.iandreyshev.parserrss.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.models.article.IArticleContent;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<IArticleContent> mList;
    private IOnItemClickListener<IArticleContent> mListener;

    public FeedAdapter(Context context) {
        mList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(IOnItemClickListener<IArticleContent> listener) {
        this.mListener = listener;
    }

    public void setItems(List<IArticleContent> newItems) {
        clear();
        for (IArticleContent item : newItems) {
            mList.add(item);
            notifyItemChanged(mList.size());
        }
    }

    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.feed_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(clickedView ->
            mListener.onItemClick(clickedView, holder.mContent)
        );
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < 0 || position >= mList.size()) {
            return;
        }

        holder.setContent(mList.get(position));
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
        IArticleContent mContent;
        TextView mTitle;
        TextView mText;
        ImageView mImage;

        ViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.item_title);
            mText = view.findViewById(R.id.item_text);
            mImage = view.findViewById(R.id.item_image);
        }

        void setContent(IArticleContent mContent) {
            this.mContent = mContent;
            mTitle.setText(mContent.getTitle());
            mText.setText(mContent.getText());

            if (mContent.isImageExist()) {
                mImage.setImageBitmap(mContent.getImage());
            } else {
                mImage.setVisibility(View.GONE);
            }
        }
    }
}
