package com.pkgprateek.madplanner;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.TimetableSlot;

/**
 * Created by pkgprateek on 18/11/15.
 */
public class TimetableDayAdapter extends ArrayAdapter<TimetableSlot> {

    private LayoutInflater inflater;
    private int resource;
    private Context c;

    public TimetableDayAdapter(Context context, int resource, List<TimetableSlot> objects) {
        super(context, resource, objects);
        this.c = context;
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(resource, null);

            holder = new ViewHolder();
            holder.subject = (TextView) view.findViewById(R.id.subject);
            holder.details = (TextView) view.findViewById(R.id.contents);
            holder.time = (TextView) view.findViewById(R.id.time);
            holder.endtime = (TextView) view.findViewById(R.id.endtime);
            holder.colour = (ImageView) view.findViewById(R.id.colour);

            // Possibly import the background and set the light colour here.

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        TimetableSlot item = getItem(position);

        holder.subject.setText(item.getName());
        holder.details.setText(item.getTeacher() + "\n" + item.getRoom());
        holder.time.setText(item.getStartTime());

        if (item.getLength() >= 1) {
            DB db = new DB(getContext());
            db.open();
            String endTime = db.getEndTime(item.getPeriod() + item.getLength());
            db.close();
            holder.endtime.setText(endTime);
        } else {
            holder.endtime.setText(item.getEndTime());
        }

        GradientDrawable bg = (GradientDrawable) holder.colour.getBackground();
        int color = Color.parseColor(item.getColour());
        bg.setColor(color);

        return view;
    }


    private static class ViewHolder {
        public TextView subject;
        public TextView details;
        public TextView time;
        public TextView endtime;
        public ImageView colour;
    }
}
