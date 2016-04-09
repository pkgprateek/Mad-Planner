package com.pkgprateek.madplanner;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.TimetableSlot;

/**
 * Created by pkgprateek on 18/11/15.
 */
public class TimetableDay extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent;
        // GET INFORMATION FROM THE BUNDLE

        Bundle b = getArguments();

        String day = b.getString("DAY");
        String date = b.getString("DATE");

        DB db = new DB(getContext());
        db.open();

        int week = getContext().getSharedPreferences(AppSetting.sP, Context.MODE_PRIVATE).getInt("DISPLAY_WEEK", 1);
        List<TimetableSlot> lessons = db.getDaysLessons(week, day);


        if (lessons.size() > 0) {
            parent = inflater.inflate(R.layout.fragment_timetable_day, null);
            ListView main = (ListView) parent.findViewById(R.id.main_layout);

            main.setAdapter(new TimetableDayAdapter(getContext(), R.layout.fragment_timetable_day_item2, lessons));
            main.setDivider(getResources().getDrawable(R.color.transparent));

            main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
                    builder.setTitle("Your message here!");
                    builder.setMessage("I wonder what this will say?");
                    builder.create().show();
                }
            });
        } else {
            return inflater.inflate(R.layout.fragment_timetable_day_empty, null);
        }


        return parent;


    }
}
