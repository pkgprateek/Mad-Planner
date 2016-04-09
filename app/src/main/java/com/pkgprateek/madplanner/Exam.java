package com.pkgprateek.madplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.Exams;

/**
 * Created by pkgprateek on 01/12/15.
 */
public class Exam extends Fragment {

    private ExamAdapter adapter;
    private ListView lV;
    private LinearLayout error;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exam, null);
        lV = (ListView) v.findViewById(R.id.listView);
        error = (LinearLayout) v.findViewById(R.id.error);
        error.setVisibility(View.GONE);



        // Add button
        ActionButton button = (ActionButton) v.findViewById(R.id.action_button);
        button.setButtonColor(getResources().getColor(R.color.accent));
        button.setButtonColorPressed(getResources().getColor(R.color.accent_light));
        button.setImageDrawable(getResources().getDrawable(R.drawable.fab_plus_icon));
        button.setImageResource(R.drawable.fab_plus_icon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("Schedule.ADDEXAM");
                startActivity(i);
            }
        });

        DB db = new DB(getContext());
        db.open();
        final List<Exams> exams = db.getAllExams();

        if (exams.size() == 0) {
            error.setVisibility(View.VISIBLE);
        }
        adapter = new ExamAdapter(getContext(), R.layout.fragment_homework_item, exams);

        lV.setAdapter(adapter);

        lV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent("Schedule.VIEWEXAM");
                i.putExtra("ID",exams.get(position).getId());
                startActivity(i);
            }
        });

        db.close();

        return v;
    }
}
