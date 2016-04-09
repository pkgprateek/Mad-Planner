package com.pkgprateek.madplanner;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pkgprateek on 15/11/15.
 */
public class MenuAdapter extends ArrayAdapter<MenuAdapterItem>{

    Context context;
    int resource;
    private LayoutInflater inflater;

    public MenuAdapter(Context context, int resource, List<MenuAdapterItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        MenuAdapterItem item = getItem(position);

        if (item.getTitle().equals("<title>"))
            return inflater.inflate(R.layout.main_title, null);
        if (item.getTitle().equals("<div>"))
            return inflater.inflate(R.layout.main_div, null);

        if (convertView == null) {
            convertView = inflater.inflate(resource, null);

            holder = new ViewHolder();

            holder.image = (ImageView) convertView.findViewById(R.id.rowicon);
            holder.notifier = (ImageView) convertView.findViewById(R.id.rownotify);
            holder.image.setBackgroundResource(item.getDrawable());
            holder.text = (TextView) convertView.findViewById(R.id.rowtext);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(item.getTitle());

        if (item.isNotifier() && item.getNotifications() > 0) {
            int notifications = item.getNotifications();
            switch (notifications) {
                case 1:
                    holder.notifier.setBackgroundColor(Color.RED);
                    holder.notifier.setBackgroundResource(R.drawable.ic_filter_2_black_24dp);
                    break;
            }
        }

        return convertView;
    }


    private static class ViewHolder {
        ImageView image;
        ImageView notifier;
        TextView text;
    }
}
