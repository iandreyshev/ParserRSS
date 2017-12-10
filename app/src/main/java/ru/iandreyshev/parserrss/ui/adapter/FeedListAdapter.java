package ru.iandreyshev.parserrss.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.models.article.IArticleInfo;

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<IArticleInfo> mList;
    private IOnItemClickListener<IArticleInfo> mListener;

    public FeedListAdapter(Context context) {
        mList = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(IOnItemClickListener<IArticleInfo> listener) {
        this.mListener = listener;
    }

    public void setItems(List<IArticleInfo> newItems) {
        clear();
        for (IArticleInfo item : newItems) {
            mList.add(item);
            notifyItemChanged(mList.size());
        }
    }

    @Override
    public FeedListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.feed_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(clickedView -> {
            mListener.onItemClick(clickedView, holder.content);
        });
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
        IArticleInfo content;
        TextView title;
        TextView text;

        ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.item_title);
            text = view.findViewById(R.id.item_text);
        }

        void setContent(IArticleInfo content) {
            this.content = content;
            title.setText(content.getTitle());
            text.setText(content.getText());
        }
    }
}
