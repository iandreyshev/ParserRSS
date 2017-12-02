package ru.iandreyshev.parserrss.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.concurrent.Callable;

import ru.iandreyshev.parserrss.R;
import ru.iandreyshev.parserrss.models.feed.IFeedItem;

public class FeedListAdapter extends ArrayAdapter<IFeedItem> {
    private Callable<Integer> onItemClick;

    public FeedListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.feed_item, parent, false);
        }

        TextView itemTitle = convertView.findViewById(R.id.item_title);
        itemTitle.setText(getItem(position).getTitle());

        TextView itemText = convertView.findViewById(R.id.item_text);
        itemText.setText(getItem(position).getText());

        return convertView;
    }
}
