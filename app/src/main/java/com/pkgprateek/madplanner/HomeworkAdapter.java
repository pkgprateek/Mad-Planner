package com.pkgprateek.madplanner;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.Homeworks;
import com.pkgprateek.madplanner.db.Subjects;
import com.pkgprateek.madplanner.ex.Methods;

/**
 * Created by pkgprateek on 15/11/15.
 */
public class HomeworkAdapter extends ArrayAdapter<Homeworks> {

    Context context;
    int resource;
    private LayoutInflater inflater;


    public HomeworkAdapter(Context context, int resource, List<Homeworks> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;

        Homeworks item = getItem(position);
        DB db = new DB(getContext());
        db.open();
        Subjects s = db.getSubject(item.getSubject());
        db.close();

        if (view == null) {
            view = inflater.inflate(resource, null);

            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.details = (TextView) view.findViewById(R.id.contents);
            holder.time = (TextView) view.findViewById(R.id.time);
            holder.endTime = (TextView) view.findViewById(R.id.endtime);
            holder.highlighter = (RelativeLayout) view.findViewById(R.id.highlighter);

            holder.time.setBackgroundColor(s.getColourInt());
            holder.time.setTextColor(context.getResources().getColor(R.color.white));
            holder.title.setText(item.getTitle());
            holder.details.setText(item.getContents());
            holder.time.setText(Html.fromHtml("<font color='#FFFFFF'>" + s.getShortName() + "</font>"));
            holder.endTime.setText(Html.fromHtml("Due in <b>" + Methods.getShortTime(item) + "</b><br/>" + getTime(item.getDueDate(), item.getTime())));

            holder.color = Color.parseColor(s.getLightColour());

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        if (item.getDone() == 1) {
            holder.title.setTextColor(context.getResources().getColor(R.color.fab_material_grey_500));
            holder.details.setTextColor(context.getResources().getColor(R.color.fab_material_grey_500));
            holder.highlighter.setBackgroundColor(context.getResources().getColor(R.color.faded));
            holder.time.setTextColor(context.getResources().getColor(R.color.fab_material_grey_500));
            holder.endTime.setTextColor(context.getResources().getColor(R.color.fab_material_grey_500));
            holder.endTime.setText(Html.fromHtml("Due in " + getTime(item.getDueDate(), item.getTime())));
            // Log.i("Schedule", "Drawing item " + item.getId() + " in DONE");
        } else {
            holder.title.setTextColor(context.getResources().getColor(R.color.black));
            holder.details.setTextColor(context.getResources().getColor(R.color.fab_material_grey_900));
            // holder.highlighter.setBackgroundColor(context.getResources().getColor(R.color.background_colour));
            holder.time.setTextColor(context.getResources().getColor(R.color.fab_material_grey_900));
            holder.endTime.setTextColor(context.getResources().getColor(R.color.fab_material_grey_900));
            holder.endTime.setText(Html.fromHtml("Due in <b>" + Methods.getShortTime(item) + "</b><br/>" + getTime(item.getDueDate(), item.getTime())));

            // Log.i("Schedule", "Drawing item " + item.getId() + " in NOT DONE");
        }

        return view;
    }

    private static class ViewHolder {
        TextView title;
        TextView details;
        TextView time;
        TextView endTime;
        RelativeLayout highlighter;
        int color;
    }


    public String getTime(String date, String time) {
        int days = Integer.parseInt(date.split("/")[2]);
        int months = Integer.parseInt(date.split("/")[1]);
        int years = Integer.parseInt(date.split("/")[0]);

        return time + "<br/>" + days + " " + Methods.getMonth(months) + ", " + years;
    }
}
