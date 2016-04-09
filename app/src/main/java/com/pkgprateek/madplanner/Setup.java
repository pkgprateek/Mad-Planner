package com.pkgprateek.madplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.Homeworks;
import com.pkgprateek.madplanner.db.PeriodSetup;
import com.pkgprateek.madplanner.db.Periods;
import com.pkgprateek.madplanner.db.Rotations;
import com.pkgprateek.madplanner.ex.CustomViewPager;
import com.pkgprateek.madplanner.ex.Methods;
import com.pkgprateek.madplanner.ex.SlidingTabLayout;
import com.pkgprateek.madplanner.ex.SwipeDismissListViewTouchListener;

/**
 * Created by pkgprateek on 18/11/15.
 */
public class Setup extends FragmentActivity {

    private SlidingTabLayout slidingTabLayout;
    private CustomViewPager pager;
    Toolbar t;
    int whichOne = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup);

        t = (Toolbar) findViewById(R.id.toolbar);
        t.setTitle("Setup");

        /**
         * Initialise the viewpager and the tab bar
         */
        pager = (CustomViewPager) findViewById(R.id.pager);
        pager.setAdapter(new SlidingAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(4);
        pager.setPagingEnabled(false);

    }



    // Adapter to set the display
    public class SlidingAdapter extends FragmentPagerAdapter {

        // Constructor
        public SlidingAdapter(FragmentManager fm) {
            super(fm);
        }

        // Setting the tabview title
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Welcome";
                case 1:
                    return "Weeks";
                case 2:
                    return "Lessons";
                default:
                    return "Error.";
            }
        }

        // Return the fragment to the display
        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new SetupOne();
                case 1:
                    return new SetupTwo();
                case 2:
                    return new SetupThree();
                default:
                    return new BlankFrag();

            }
        }

        // Get the total amount of questions
        @Override
        public int getCount() {
            return 3;
        }
    }

    /**
     * Setup One fragment!
     */
    public class SetupOne extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.setup_one, null);
            return v;
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                removeMenu();
                addMenuNext();
            }
        }
    }


    /**
     * Setup two fragment!
     */
    public class SetupTwo extends Fragment implements View.OnClickListener{

        SharedPreferences.Editor prefs;
        Button rot1;
        Button rot2;
        Button rot3;
        Button rot4;
        Button rot5;
        Button rot6;
        int currWeek = 0;


        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && currWeek == 0) {
                removeMenu();
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.setup_two, null);

            prefs = getActivity().getSharedPreferences(AppSetting.sP, Context.MODE_PRIVATE).edit();

            rot1 = (Button) v.findViewById(R.id.rot1);
            rot2 = (Button) v.findViewById(R.id.rot2);
            rot3 = (Button) v.findViewById(R.id.rot3);
            rot4 = (Button) v.findViewById(R.id.rot4);
            rot5 = (Button) v.findViewById(R.id.rot5);
            rot6 = (Button) v.findViewById(R.id.rot6);

            rot1.setOnClickListener(this);
            rot2.setOnClickListener(this);
            rot3.setOnClickListener(this);
            rot4.setOnClickListener(this);
            rot5.setOnClickListener(this);
            rot6.setOnClickListener(this);

            return v;
        }


        @Override
        public void onPause() {
            super.onPause();
            prefs.commit();
        }

        @Override
        public void onClick(View v) {
            disableAll();
            boolean nothing = false;
            switch (v.getId()) {
                case R.id.rot1: currWeek = 1; rot1.setBackgroundDrawable(getResources().getDrawable(R.drawable.current_week_pressed)); break;
                case R.id.rot2: currWeek = 2; rot2.setBackgroundDrawable(getResources().getDrawable(R.drawable.current_week_pressed)); break;
                case R.id.rot3: currWeek = 3; rot3.setBackgroundDrawable(getResources().getDrawable(R.drawable.current_week_pressed)); break;
                case R.id.rot4: currWeek = 4; rot4.setBackgroundDrawable(getResources().getDrawable(R.drawable.current_week_pressed)); break;
                case R.id.rot5: currWeek = 5; rot5.setBackgroundDrawable(getResources().getDrawable(R.drawable.current_week_pressed)); break;
                case R.id.rot6: currWeek = 6; rot6.setBackgroundDrawable(getResources().getDrawable(R.drawable.current_week_pressed)); break;
                default: currWeek = 0; nothing = true; break;
            }
            removeMenu();
            if (!nothing) {
                // Enable the scrolling > Go to the left.
                addMenuNext();
            }
            prefs.putInt("DB_WEEKS", currWeek).commit();
        }

        public void disableAll() {
            Drawable d = getResources().getDrawable(R.drawable.current_week_normal);
            rot1.setBackgroundDrawable(d);
            rot2.setBackgroundDrawable(d);
            rot3.setBackgroundDrawable(d);
            rot4.setBackgroundDrawable(d);
            rot5.setBackgroundDrawable(d);
            rot6.setBackgroundDrawable(d);
        }
    }


    /**
     * Setup three fragment
     */
    public class SetupThree extends Fragment {

        private PeriodAdapter periodsList;
        private List<PeriodSetup> periods;
        private ListView list;

        private int DEFAULT_NUMBER_OF_PERIODS = 5;
        private int DEFAULT_DURATION = 60;
        private int DEFAULT_DIFFERENCE_PERIODS = 0;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.setup_three, null);
            list = (ListView) v.findViewById(R.id.listView);

            /**
             * Setup the model period display - 5 periods of which
             */

            periods = new ArrayList<PeriodSetup>();
            int minutes = 540;
            for (int i = 1; i <= DEFAULT_NUMBER_OF_PERIODS; i++) {
                periods.add(new PeriodSetup(i, minutes + ((i-1) * DEFAULT_DURATION), minutes + (i * DEFAULT_DURATION)));
            }

            periodsList = new PeriodAdapter(getContext(),
                    R.layout.setup_three_periods,
                    periods);

            list.setAdapter(periodsList);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Set start time
                    setTime("What time does the lesson start?", position,
                            periods.get(position).getStartTimeInt(), false);
                }
            });

            SwipeDismissListViewTouchListener touchListener =
                    new SwipeDismissListViewTouchListener(
                            list, new SwipeDismissListViewTouchListener.DismissCallbacks() {
                        @Override
                        public boolean canDismiss(int position) {
                            return true;
                        }
                        @Override
                        public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                            for (int position : reverseSortedPositions) {
                                periodsList.remove(periodsList.getItem(position));
                            }
                            refactorPeriods();
                            periodsList.notifyDataSetChanged();
                        }
                    });
            list.setOnTouchListener(touchListener);
            // Setting this scroll listener is required to ensure that during ListView scrolling,
            // we don't look for swipes.
            list.setOnScrollListener(touchListener.makeScrollListener());

            return v;
        }

        /* Method to update the period ID numbers */
        public void refactorPeriods() {
            for (int i = 0; i < periods.size(); i++) {
                periods.get(i).setPeriod(i + 1);
            }
        }

        /* Set the time for the periods  */
        public void setTime(String message, final int id, final int startMinutes, final boolean last) {
            TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (last) {
                        if (hourOfDay * 60 + minute <= periods.get(id).getStartTimeInt()) {
                            periods.get(id).setEndTime(periods.get(id).getStartTimeInt() + 30);
                            Toast.makeText(getApplicationContext(), "The time you selected is before the start of the period", Toast.LENGTH_LONG).show();
                        } else if (hourOfDay*60 + minute > 60*24) {
                            periods.get(id).setEndTime(60*24);
                            Toast.makeText(getApplicationContext(), "Time exceeded the end of the day", Toast.LENGTH_LONG).show();
                        }
                        else
                            periods.get(id).setEndTime(hourOfDay*60 + minute);
                        /**
                         * Update the list view
                         */
                        // Get difference between end And start
                        DEFAULT_DURATION = periods.get(id).getEndTimeInt() - periods.get(id).getStartTimeInt();
                        if (id != periods.size() -1 ) {
                            for (int i = id + 1; i < periods.size(); i++) {
                                periods.get(i).setStartTime(periods.get(i-1).getEndTimeInt());
                                periods.get(i).setEndTime(periods.get(i).getStartTimeInt() + DEFAULT_DURATION);
                                if (periods.get(i).getStartTimeInt() >= (60*24) || periods.get(i).getEndTimeInt() > (60*24))
                                    periods.get(i).mark();
                            }
                            for (int i = 0; i < periods.size(); i++) {
                                if (periods.get(i).isMarked()) {
                                    periods.remove(i);
                                    i--;
                                }
                            }
                        }

                        periodsList.notifyDataSetChanged();
                    } else {
                        int DEFAULT_START = (hourOfDay*60 + minute) - periods.get(id).getStartTimeInt();

                        PeriodSetup p = periods.get(id);

                        if (id != 0) {
                            if ((hourOfDay*60+minute) < periods.get(id-1).getEndTimeInt()) {
                                p.setStartTime(periods.get(id - 1).getEndTimeInt());
                                Toast.makeText(getApplicationContext(), "Time is before the end of the previous period", Toast.LENGTH_LONG).show();
                            } else {
                                p.setStartTime(hourOfDay * 60 + minute);
                            }
                        } else {
                            p.setStartTime(hourOfDay * 60 + minute);
                        }


                        setTime("Starting at " + Methods.getTime(p.getStartTimeInt()) + "\nWhat time will the lesson end?", id, periods.get(id).getEndTimeInt() + DEFAULT_START, true);
                    }
                }
            }, startMinutes / 60, startMinutes % 60, true);
            dialog.setTitle(message);
            dialog.show();
        }

        /* Determine which screen is on */
        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                removeMenu();
                addMenu();
            }
        }


        /* Add the menu items to the display */
        public void addMenu() {
            t.inflateMenu(R.menu.setup_list);
            t.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_add) {
                        PeriodSetup lastItem = periods.get(periods.size() - 1);
                        if (lastItem.getEndTimeInt() >= 24 * 60)
                            Toast.makeText(getApplicationContext(), "Over 24 hours!", Toast.LENGTH_LONG).show();
                        else {
                            periods.add(new PeriodSetup(periods.size(), lastItem.getEndTimeInt(), lastItem.getEndTimeInt() + lastItem.getDuration()));
                        }
                        refactorPeriods();
                        periodsList.notifyDataSetChanged();
                    }
                    if (item.getItemId() == R.id.action_done) {

                        // Do some checks
                        for (int i = 0; i < periods.size(); i++) {

                        }

                        /**
                         * Store everything here!
                         *  SP: DB_WEEKS holds the number of weeks
                         *  DB: DB_CURRENT holds the current week
                         *  periodsList: holds a list of periods
                         */
                        DB db = new DB(getContext());
                        db.open();
                        SharedPreferences prefs = getSharedPreferences(AppSetting.sP, MODE_PRIVATE);
                        Date firstDayOfWeek = Methods.getFirstDayOfWeekDate(new Date());
                        // Clear any existing records
                        db.clearAllRotations();
                        db.clearAllPeriods();
                        int temporaryWeek = prefs.getInt("DB_CURRENT", 1);
                        int weekCycle = prefs.getInt("DB_WEEKS", 1);
                        // Apply the rotations thing for the next year
                        // Will be made editable in the future
                        for (int i = 0; i < 52; i++) {
                            Rotations r = new Rotations(DB.OP.CREATE, -1, Methods.getDate(firstDayOfWeek), temporaryWeek);
                            db.runRotation(r);

                            // Go to the start of the next week
                            Methods.goToNextWeek(firstDayOfWeek);

                            if (temporaryWeek >= weekCycle)
                                temporaryWeek = 1;
                            else
                                temporaryWeek++;
                        }
                        // Setup finished of the rotations database

                        // Setup the periods rotation system
                        for (int i = 1; i <= periods.size(); i++) {
                            Periods p = new Periods(DB.OP.CREATE, i, periods.get(i-1).getStartTime(), periods.get(i-1).getEndTime());
                            db.runPeriod(p);
                        }
                        // Setup finished for the periods

                        // Close the db
                        db.close();

                        // Show the timetable editor
                        Intent i = new Intent("Schedule.EDITTIMETABLE");
                        startActivity(i);
                        finish();
                    }
                    return true;
                }
            });
        }
    }


    /* Add the menu for the week rotation */
    public void addMenuNext() {
        t.inflateMenu(R.menu.setup);

        t.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_done) {
                    if (pager.getCurrentItem() == 1) {
                        int weeks = getSharedPreferences(AppSetting.sP, MODE_PRIVATE).getInt("DB_WEEKS", -1);
                        if (weeks == -1) {
                            Toast.makeText(getApplicationContext(), "You've broken it, somehow. You need to pick a week cycle", Toast.LENGTH_LONG).show();
                        }
                        if (weeks != 1) {
                            // Need to set the week!
                            // Construct a dialog to ask for the option!

                            AlertDialog.Builder builder = new AlertDialog.Builder(Setup.this);
                            builder.setTitle("Which week is this week?");
                            String[] objects = new String[weeks];
                            for (int i = 0; i < weeks; i++) {
                                objects[i] = "Week " + (i+1);
                            }
                            builder.setSingleChoiceItems(objects, whichOne, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    whichOne = which;
                                }
                            });
                            builder.setNegativeButton("Cancel", null);
                            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (whichOne != -1) {
                                        getSharedPreferences(AppSetting.sP, MODE_PRIVATE).edit().putInt("DB_CURRENT", whichOne+1).apply();
                                        pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please select which week it is", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            builder.create().show();
                        } else {
                            getSharedPreferences(AppSetting.sP, MODE_PRIVATE).edit().putInt("DB_CURRENT", 1).apply();
                            pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                        }
                    } else {
                        pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                        getSharedPreferences(AppSetting.sP, MODE_PRIVATE).edit().putInt("DB_CURRENT", 1);
                    }
                }
                return true;
            }
        });
    }


    /* Remove the menu from the toolbar */
    public void removeMenu() {
        t.getMenu().clear();
    }


    /* Small array class for PeriodSetup */
    public class PeriodAdapter extends ArrayAdapter<PeriodSetup> {

        Context context;
        int resource;
        private LayoutInflater inflater;

        public PeriodAdapter(Context context, int resource, List<PeriodSetup> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(resource, null);

            PeriodSetup item = getItem(position);

            TextView period = (TextView) view.findViewById(R.id.period);
            TextView time = (TextView) view.findViewById(R.id.time);
            period.setText("Period " + item.getPeriod());
            time.setText(item.getStartTime() + " > " + item.getEndTime());

            return view;
        }
    }



    public void setSliding(boolean sliding) {
        pager.setPagingEnabled(sliding);
    }
}
