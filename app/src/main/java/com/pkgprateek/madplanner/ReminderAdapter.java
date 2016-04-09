package com.pkgprateek.madplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import com.pkgprateek.madplanner.db.Reminders;

/**
 * Created by pkgprateek on 18/11/15.
 */
public class ReminderAdapter extends ArrayAdapter<Reminders> {

    private LayoutInflater inflater;
    private int resource;
    private Context c;

    public ReminderAdapter(Context context, int resource, List<Reminders> objects) {
        super(context, resource, objects);
        this.c = context;
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        Reminders r = getItem(position);

        if (view == null) {
            view = inflater.inflate(resource, null);

            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.details = (TextView) view.findViewById(R.id.contents);
            holder.time = (TextView) view.findViewById(R.id.time);
            holder.endTime = (TextView) view.findViewById(R.id.endtime);
            holder.highlighter = (RelativeLayout) view.findViewById(R.id.highlighter);

            holder.title.setText(r.getTitle());
            holder.details.setText(r.getContents());

            if (r.getDueDate() != null && !r.getDueDate().equals("")) {
                String[] s = r.getDueDate().split("/");
                String ss = s[2] + "/" + s[1] + "/" + s[0];
                holder.time.setText(ss);
            } else {
                holder.time.setText("");
            }

            holder.endTime.setText(r.getTime());

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        return view;
    }


    private static class ViewHolder {
        TextView title;
        TextView details;
        TextView time;
        TextView endTime;
        RelativeLayout highlighter;
    }
}
