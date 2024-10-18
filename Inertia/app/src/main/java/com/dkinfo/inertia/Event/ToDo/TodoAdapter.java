package com.dkinfo.inertia.Event.ToDo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import com.dkinfo.inertia.R;

import java.util.List;

public class TodoAdapter extends ArrayAdapter<TodoItem> {

    private LayoutInflater inflater;

    public TodoAdapter(Context context, List<TodoItem> itemList) {
        super(context, 0, itemList);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        ViewHolder viewHolder;

        if (itemView == null) {
            itemView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = itemView.findViewById(R.id.eventName);
            viewHolder.textViewDis = itemView.findViewById(R.id.text_view_dis);
            itemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }

        TodoItem item = getItem(position);

        viewHolder.textViewName.setText(item.getTitle());
        viewHolder.textViewDis.setText(item.getDescription());

        return itemView;
    }

    private static class ViewHolder {
        CheckBox checkbox;
        TextView textViewName;
        TextView textViewDis;
    }
}

