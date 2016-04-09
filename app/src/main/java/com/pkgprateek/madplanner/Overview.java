package com.pkgprateek.madplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pkgprateek.madplanner.db.DB;

public class Overview extends AppCompatActivity {

    private CharSequence mTitle;
    private ListView overviewList;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private boolean canRefresh = false;
    private boolean showWeekSwitcher = false;
    private String weekIndicator = "Week ";

    private FrameLayout mainFragment;
    private int systemWeek;

    private boolean unlock = true;

    private int globalPosition = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        // Check if the activity has been initiated before.
        if (savedInstanceState != null) {
            return;
        }

        // Initialise all the navigation drawer stuff
        overviewList = (ListView) findViewById(R.id.overview_list);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mainFragment = (FrameLayout) findViewById(R.id.main_frag);


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.blank, R.string.blank) {
            /* Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /* Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };


        /* Get the current week from the Rotations database */
        DB db = new DB(getApplicationContext());
        db.open();

        systemWeek = db.getWeek(new Date());
        weekIndicator = "Week " + systemWeek;
        getSharedPreferences(AppSetting.sP, MODE_PRIVATE).edit().putInt("DISPLAY_WEEK", systemWeek).commit();

        db.close();

        /* Actionbar */
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /* Properties drawerLayout */
        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.setStatusBarBackground(R.color.primary_dark);


        /* Toggle onClickListener */
        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });


        /* Sync that shit */
        drawerToggle.syncState();


        /* Setup the current drawer */
        setupDrawer();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showWeekSwitcher) {
            getMenuInflater().inflate(R.menu.global, menu);

            MenuItem item = menu.findItem(R.id.action_week);
            if (getSharedPreferences(AppSetting.sP, MODE_PRIVATE).getInt("DB_WEEKS",1) != 1) {
                item.setTitle(weekIndicator);
                item.setVisible(true);
            } else {
                item.setVisible(false);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Handle action bar item clicks here. The action bar will
         * automatically handle clicks on the Home/Up button, so long
         * as you specify a parent activity in AndroidManifest.xml. */
        int id = item.getItemId();

        if (showWeekSwitcher) {
            switch (item.getItemId()) {
                case R.id.action_week:
                    int newWeek = incrementDisplayWeek();
                    weekIndicator = "Week " + newWeek;
                    Toast.makeText(getBaseContext(), weekIndicator, Toast.LENGTH_SHORT).show();
                    supportInvalidateOptionsMenu();

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frag, new Timetable()).commit();
                    showWeekSwitcher = true;

                    break;
                case R.id.action_edittimetable:
                    Intent i = new Intent("Schedule.EDITTIMETABLE");
                    startActivity(i);
                    break;
            }
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }


    public void setupDrawer() {
        /* Setting up the items list */
        final List<MenuAdapterItem> items = new ArrayList<MenuAdapterItem>();
        items.add(new MenuAdapterItem("<title>", "", false, 0, 0));
        items.add(new MenuAdapterItem("Now", "", false, 0, R.drawable.ic_flight_takeoff_black_24dp));
        items.add(new MenuAdapterItem("Timetables", "", false, 0, R.drawable.ic_dashboard_black_24dp));
        items.add(new MenuAdapterItem("Homeworks", "", true, 0, R.drawable.ic_edit_black_24dp));
        items.add(new MenuAdapterItem("Exams", "", true, 0, R.drawable.ic_school_black_24dp));
        items.add(new MenuAdapterItem("Reminders", "", true, 0, R.drawable.ic_access_alarms_black_24dp));
        items.add(new MenuAdapterItem("<div>", "", false, 0, 0));
        items.add(new MenuAdapterItem("Settings", "", false, 0, R.drawable.ic_settings_black_24dp));

        /* Apply the adapter */
        overviewList.setAdapter(new MenuAdapter(this, R.layout.menu_item_layout, items));

        /* OnClick for the list view in the drawer - Handle loading of fragments into the main view */
        overviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0 && position != 6) {
                    overviewList.setItemChecked(position, true);
                    overviewList.setSelection(position);
                    showWeekSwitcher = false;
                    if (position != 7)
                        toolbar.setTitle(items.get(position).getTitle());
                    /* Change the position */
                    setFragmentView(position);

                    /* Close the drawer once the Fragment has been selected */
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });

        /* Default screen when the app starts up. */
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frag, new Now()).commit();
    }

    @Override
    protected void onDestroy() {
        getSharedPreferences(AppSetting.sP, MODE_PRIVATE).edit().putInt("DISPLAY_WEEK", systemWeek).commit();
        super.onDestroy();
    }


    /* Increment the week which will be shown in the timetable
     * In Overview due to toolbar displaying */
    public int incrementDisplayWeek() {
        SharedPreferences sp = getSharedPreferences(AppSetting.sP, MODE_PRIVATE);
        int numberOfWeeks = sp.getInt("DB_WEEKS", 4);
        int displayWeeks = sp.getInt("DISPLAY_WEEK", 1);
        if (displayWeeks < numberOfWeeks)
            displayWeeks++;
        else
            displayWeeks = 1;

        sp.edit().putInt("DISPLAY_WEEK", displayWeeks).apply();
        return displayWeeks;
    }


    public void setFragmentView(int position) {
        if (unlock) {
            switch (position) {
                case 1:
                    globalPosition = position;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frag, new Now()).commit();
                    break;
                case 2:
                    globalPosition = position;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frag, new Timetable()).commit();
                    showWeekSwitcher = true;
                    break;
                case 3:
                    globalPosition = position;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frag, new Homework()).commit();
                    break;

                case 4:
                    globalPosition = position;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frag, new Exam()).commit();
                    break;
                case 5:
                    globalPosition = position;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_frag, new Reminder()).commit();
                    break;
                case 7:
                    Intent i = new Intent("Schedule.APPSETTINGS");
                    startActivity(i);
                    break;
            }
        }
    }


    @Override
    public void onResume() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager != null) {
            if (globalPosition == 1 || globalPosition == 3 || globalPosition == 4 || globalPosition == 5) {
                setFragmentView(globalPosition);
            }
        }
        super.onResume();
    }
}
