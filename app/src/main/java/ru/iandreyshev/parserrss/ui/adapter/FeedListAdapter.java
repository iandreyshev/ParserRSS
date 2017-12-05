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
    private LayoutInflater inflater;
    private List<IArticleInfo> list;
    private IOnItemClickListener listener;

    public FeedListAdapter(Context context) {
        list = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(IOnItemClickListener<IArticleInfo> listener) {
        this.listener = listener;
    }

    public void setItems(List<IArticleInfo> newItems) {
        clear();
        for (IArticleInfo item : newItems) {
            list.add(item);
            notifyItemChanged(list.size());
        }
    }

    @Override
    public FeedListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.feed_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(clickedView -> {
            listener.onItemClick(clickedView, holder.content);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < 0 || position >= list.size()) {
            return;
        }

        holder.setContent(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void clear() {
        list.clear();
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
