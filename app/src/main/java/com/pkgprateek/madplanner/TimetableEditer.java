package com.pkgprateek.madplanner;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.Subject;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.Periods;
import com.pkgprateek.madplanner.db.Subjects;
import com.pkgprateek.madplanner.db.TimetableSlot;
import com.pkgprateek.madplanner.db.Timetables;
import com.pkgprateek.madplanner.ex.CustomViewPager;

/**
 * Created by pkgprateek on 04/12/15.
 */
public class TimetableEditer extends AppCompatActivity {

    // Global variables
    private int subjectInQuestion;
    private int currentWeek;
    private String[] weekDays;
    private TimetableDrawer drawer;
    private CustomViewPager pager;
    private Toolbar t;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_editor);

        // Toolbar
        t = (Toolbar) findViewById(R.id.toolbar);
        t.setTitle("Edit timetable");
        t.setTitleTextColor(getResources().getColor(R.color.white));
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Database variables
        DB db = new DB(getApplicationContext());
        db.open();
        currentWeek = 1;
        List<Periods> periods = db.getAllPeriods();
        weekDays = getResources().getStringArray(R.array.weekdays);

        LinearLayout timetable = (LinearLayout) findViewById(R.id.timetableDrawer);
        drawer = new TimetableDrawer(getApplicationContext(), 8, periods.size(), currentWeek);
        timetable.addView(drawer);

        final ViewTreeObserver observer = drawer.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                drawer.reloadDisplay();
            }
        });

        // Set currentWeek and subjectInQuestion here
        List<Subjects> subjects = db.getAllSubjects();
        pager = (CustomViewPager) findViewById(R.id.pager);
        pager.setAdapter(new TimetableDrawerAdapter(getSupportFragmentManager(), subjects));
        pager.setPagingEnabled(true);

        // Close the DB connection
        db.close();
    }


    /**
     * On resume method - Reload the display
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (drawer.getFinished())
            drawer.reloadDisplay();
    }


    /**
     * Update the colour of the title bar according to which subject
     */
    public void updateColour(int colour) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(colour);
        }
        t.setBackgroundColor(colour);
    }


    /**
     * Class to manage the adapter for the Timetable Drawer
     */
    public class TimetableDrawerAdapter extends FragmentPagerAdapter {
        FragmentManager fm;
        List<Subjects> subjects;
        public TimetableDrawerAdapter(FragmentManager fm, List<Subjects> subjects) {
            super(fm);
            this.fm = fm;
            this.subjects = subjects;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putInt("ID", subjects.get(position).getId());
            bundle.putInt("POSITION", position);
            SubjectFragment frag = new SubjectFragment();
            frag.setArguments(bundle);
            return frag;

        }

        @Override
        public int getCount() {
            return subjects.size();
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }


    }

    /**
     * Fragment for the Subjects breakdown
     */
    public class SubjectFragment extends Fragment {

        private Subjects s = new Subjects(DB.OP.NOTEXIST);
        private boolean loaded;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View v;
            Bundle args = getArguments();

            // Inflate the view
            v = inflater.inflate(R.layout.timetable_editor_subjects, null);
            int id = 0;
            id = args.getInt("ID");

            // Get the subject from the Database
            DB db = new DB(getContext());
            db.open();
            s = db.getSubject(id);
            db.close();

            // If the thing exists
            if (s.getOp() == DB.OP.NOTEXIST) {
                return inflater.inflate(R.layout.timetable_editor_subjects_error, null);
            } else {
                TextView subject = (TextView) v.findViewById(R.id.subject);
                subject.setText(s.getName());
            }

            // If it's the first subject, then change the colour properly
            if (!loaded) {
                int position = args.getInt("POSITION");
                if (position == 0) {
                    updateColour(s.getColourInt());
                }
            }
            loaded = true;
            return v;
        }

        @Override
        public void setMenuVisibility(boolean menuVisible) {
            super.setMenuVisibility(menuVisible);
            if (loaded) {
                if (menuVisible && s.getOp() != DB.OP.NOTEXIST) {
                    updateColour(s.getColourInt());
                }
            }
        }
    }
}
