package com.example.medicinealarm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class myAdapter extends ArrayAdapter<Model> {
    private Context context;
    private List<Model> list;


    public myAdapter(Context context, List<Model> list) {
        super(context, 0, list);

    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent) {
        Model current = getItem(position);
        ViewHolder viewHolder = new ViewHolder();
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.listviewactivty, parent, false);
        viewHolder.text = convertView.findViewById(R.id.listview_item_title);
        viewHolder.image = convertView.findViewById(R.id.listview_image);
        convertView.setTag(viewHolder);
        viewHolder.text.setText(current.getName());
        viewHolder.text.setTag(current.getTag());
        viewHolder.image.setImageBitmap(current.getImage());
        return convertView;
    }

    public static class ViewHolder {
        TextView text;
        ImageView image;
    }
}
