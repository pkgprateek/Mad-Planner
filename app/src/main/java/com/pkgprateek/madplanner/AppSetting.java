package com.pkgprateek.madplanner;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;

import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by pkgprateek on 11/11/15.
 */
public class AppSetting extends PreferenceActivity {

    public static final String sP = "schedule-sp";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);


        /**
         * Setting up the toolbar for the activity
         */
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbars, root, false);
        bar.setTitleTextColor(getResources().getColor(R.color.white));
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bar.setTitle("Settings");
        root.addView(bar, 0); // insert at top


        /**
         * Developers Database launcher - Used to populate the database with test data
         */
        Preference developerOptions;
        developerOptions = (Preference) findPreference("settings_pref_key_database");
        developerOptions.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent("Schedule.DEVDATABASE");
                startActivity(i);
                return true;
            }
        });


        /**
         * Launch the setup activity
         */
        Preference setup;
        setup = (Preference) findPreference("settings_pref_key_setup");
        setup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent("Schedule.SETUP");
                startActivity(i);
                return true;
            }
        });


        /**
         * About dialog - About me!
         */
        Preference about;
        about = (Preference) findPreference("settings_pref_key_about");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AppSetting.this);
                builder.setTitle("About");
                builder.setMessage(Html.fromHtml("<b>Mad Group 10</b><br/><br/>Email me if you have any issues: <i>madgroup10@bml.edu.in</i>"));

                AlertDialog dialog = builder.create();
                dialog.show();


                return true;
            }
        });


        /**
         * Changing the current week with a selector?
         */
        Preference currentWeek;
        currentWeek = (Preference) findPreference("settings_pref_key_currentweek");
        currentWeek.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // Create the selector dialog

                return false;
            }
        });
    }
}
