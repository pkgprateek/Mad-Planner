package com.pkgprateek.madplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.Homeworks;

/**
 * Created by pkgprateek on 15/11/15.
 */
public class Homework extends Fragment {

    private HomeworkAdapter adapter;
    private ListView main;
    private List<Homeworks> homeworks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DB db = new DB(getContext());
        db.open();
        if (db.getNumberOfSubjects() != 0) {

            /**
             * Display all the homeworks here in chronological order
             */
            homeworks = db.getAllHomeworks();

            if (homeworks.size() != 0) {
                View v = inflater.inflate(R.layout.fragment_homework, null);
                final ActionButton button = (ActionButton) v.findViewById(R.id.action_button);
                main = (ListView) v.findViewById(R.id.listView);

                /**
                 * Setting up stuff for the "Add Homework" button
                 */
                button.setButtonColor(getResources().getColor(R.color.accent));
                button.setButtonColorPressed(getResources().getColor(R.color.accent_light));
                button.setImageDrawable(getResources().getDrawable(R.drawable.fab_plus_icon));
                button.setImageResource(R.drawable.fab_plus_icon);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent("Schedule.ADDHOMEWORK");
                        startActivity(i);
                    }
                });

                final int[] ids = new int[homeworks.size()];
                for (int i = 0; i < homeworks.size(); i++) {
                    ids[i] = homeworks.get(i).getId();
                }

                /**
                 * Temporary code to just display all the homework items
                 *  - Through an adapter
                 */
                adapter = new HomeworkAdapter(getContext(), R.layout.fragment_homework_item, homeworks);
                main.setAdapter(adapter);
                main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent("Schedule.VIEWHOMEWORK");
                        i.putExtra("ID", homeworks.get(position).getId());
                        startActivity(i);
                    }
                });
                db.close();
                return v;
            } else {

                View v = inflater.inflate(R.layout.fragment_homework_blank, null);
                ActionButton button = (ActionButton) v.findViewById(R.id.action_button);

                /**
                 * Setting up stuff for the "Add Homework" button
                 */
                button.setButtonColor(getResources().getColor(R.color.accent));
                button.setButtonColorPressed(getResources().getColor(R.color.accent_light));
                button.setImageDrawable(getResources().getDrawable(R.drawable.fab_plus_icon));
                button.setImageResource(R.drawable.fab_plus_icon);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent("Schedule.ADDHOMEWORK");
                        startActivity(i);
                    }
                });
                return v;
            }


        } else {
            db.close();
            return inflater.inflate(R.layout.fragment_homework_error, null);
        }
    }
}
