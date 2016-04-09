package com.pkgprateek.madplanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.ex.Methods;
import com.pkgprateek.madplanner.ex.SlidingTabLayout;

/**
 * Created by pkgprateek on 18/11/15.
 */
public class Timetable extends Fragment {

    private SlidingTabLayout slidingTab;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
    private List<String> days;
    private ViewPager pager;
    private SharedPreferences sP;
    private Date now;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /**
         * Setup the timetable layout
         */

        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        View v = inflater.inflate(R.layout.fragment_timetable, null);

        now = new Date();
        days = new ArrayList<>();
        DB db = new DB(getContext());
        db.open();
        sP = getContext().getSharedPreferences(AppSetting.sP, Context.MODE_PRIVATE);
        int displayingWeek = sP.getInt("DISPLAY_WEEK", 1);
        days = db.getDays(displayingWeek);
        db.close();
        if (days.size() == 0) {
            days.add("Monday");
            days.add("Tuesday");
            days.add("Wednesday");
            days.add("Thursday");
            days.add("Friday");
        }
        sP = getActivity().getSharedPreferences(AppSetting.sP, Context.MODE_PRIVATE);
        dayOfTheWeek = sP.getString("DISPLAY_DAY", dayOfTheWeek);

        pager = (ViewPager) v.findViewById(R.id.pager);
        pager.setAdapter(new SlidingAdapter(getFragmentManager()));
        pager.setOffscreenPageLimit(5);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                sP.edit().putString("DISPLAY_DAY", days.get(position)).apply();
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        slidingTab = (SlidingTabLayout) v.findViewById(R.id.slidingtab);
        slidingTab.setViewPager(pager);
        slidingTab.setDividerColors(getResources().getColor(R.color.transparent));
        slidingTab.setSelectedIndicatorColors(getResources().getColor(R.color.white));


        /* Navigate to the day of the week it is */
        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).equals(dayOfTheWeek)) {
                pager.setCurrentItem(i);
            }
        }

        return v;
    }


    // Adapter to set the display
    public class SlidingAdapter extends FragmentStatePagerAdapter {

        // Constructor
        public SlidingAdapter(FragmentManager fm) {
            super(fm);
        }

        // Setting the tabview title
        @Override
        public CharSequence getPageTitle(int position) {
            return days.get(position);
        }

        // Return the fragment to the display
        @Override
        public Fragment getItem(int i) {
            TimetableDay t = new TimetableDay();
            Bundle b = new Bundle();
            b.putString("DAY",days.get(i));
            t.setArguments(b);
            return t;
        }

        // Get the total amount of questions
        @Override
        public int getCount() {
            return days.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_UNCHANGED;
        }


    }




    @Override
    public void onDestroyView() {
        pager.removeAllViews();
        super.onDestroyView();
    }
}

