package com.pkgprateek.madplanner;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.Exams;
import com.pkgprateek.madplanner.db.Subjects;
import com.pkgprateek.madplanner.ex.Methods;
/**
 * Created by pkgprateek on 01/12/15.
 */
public class ExamAdapter extends ArrayAdapter<Exams> {

    Context context;
    int resource;
    private LayoutInflater inflater;

    public ExamAdapter(Context context, int resource, List<Exams> objects) {
        super(context, resource, objects);
        this.context = context;
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
            holder.subject = (TextView) view.findViewById(R.id.title);
            holder.details = (TextView) view.findViewById(R.id.contents);
            holder.time = (TextView) view.findViewById(R.id.time);
            holder.endtime = (TextView) view.findViewById(R.id.endtime);

            // Possibly import the background and set the light colour here.

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Exams item = getItem(position);
        DB db = new DB(getContext());
        db.open();
        Subjects s = db.getSubject(item.getSubject());
        String r = db.getRoom(item.getRoom());


        holder.time.setBackgroundColor(s.getColourInt());
        holder.time.setTextColor(context.getResources().getColor(R.color.white));
        holder.time.setText(s.getShortName());
        holder.subject.setText(item.getTitle());
        holder.details.setText(item.getContents());
        String days = Methods.getDaysBetween(item.getDate()).substring(0,1);
        if (days.equals("-")) {
            holder.endtime.setText(Html.fromHtml("<b>Passed</b><br/>" + r + ", " + getTime(item.getDate(), item.getTime())));
            holder.time.setTextColor(context.getResources().getColor(R.color.faded_text));
            holder.subject.setTextColor(context.getResources().getColor(R.color.faded_text));
            holder.details.setTextColor(context.getResources().getColor(R.color.faded_text));
            holder.endtime.setTextColor(context.getResources().getColor(R.color.faded_text));
        } else if (days.equals("0")) {
            holder.endtime.setText(Html.fromHtml("<b>Today</b><br/>" + r + ", " + getTime(item.getDate(), item.getTime())));
        } else {
            holder.endtime.setText(Html.fromHtml("<b>" + Methods.getDaysBetween(item.getDate()) + "</b><br/>" + r + ", " + getTime(item.getDate(), item.getTime())));
        }

        holder.colour = Color.parseColor(s.getLightColour());

        return view;
    }




    private static class ViewHolder {
        public TextView subject;
        public TextView details;
        public TextView time;
        public TextView endtime;
        public int colour;
    }




    public String getTime(String date, String time) {
        int days = Integer.parseInt(date.split("/")[2]);
        int months = Integer.parseInt(date.split("/")[1]);
        int years = Integer.parseInt(date.split("/")[0]);

        return time + "<br/>" + days + " " + Methods.getMonth(months) + ", " + years;
    }
}
