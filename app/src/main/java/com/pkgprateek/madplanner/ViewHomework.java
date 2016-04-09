package com.pkgprateek.madplanner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.software.shell.fab.ActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.Homeworks;
import com.pkgprateek.madplanner.db.Subjects;
import com.pkgprateek.madplanner.ex.Methods;

/**
 * Created by pkgprateek on 04/12/15.
 */
public class ViewHomework extends AppCompatActivity {

    private int homeworkID = -1;
    private ActionButton done;
    private TextView subject, duein, title, content, subjectid, dueinid;
    private RelativeLayout layout;
    private Homeworks homework;
    private Toolbar t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DB db = new DB(getApplicationContext());
        db.open();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            homeworkID = extras.getInt("ID");
        }

        // Check if a homework ID has been passed through
        if (homeworkID != -1) {

            homework = db.getHomework(homeworkID);
            // If that homework item exists
            if (homework.getOp() != DB.OP.NOTEXIST) {

                setContentView(R.layout.view_homework);

                // Setup the toolbar
                t = (Toolbar) findViewById(R.id.toolbar);
                t.setTitle("Homework");
                t.setTitleTextColor(getResources().getColor(R.color.white));
                t.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                t.inflateMenu(R.menu.viewhomework);
                t.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_delete:
                                AlertDialog.Builder builder = new AlertDialog.Builder(ViewHomework.this);
                                builder.setTitle("Are you sure?");
                                builder.setMessage("Are you sure you want to delete this homework event?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        homework.setOp(DB.OP.DESTROY);
                                        DB newDB = new DB(ViewHomework.this);
                                        newDB.open();
                                        newDB.runHomework(homework);
                                        newDB.close();
                                        finish();
                                    }
                                });
                                builder.setNegativeButton("No", null);
                                builder.create().show();
                                break;
                            case R.id.action_edit:
                                Intent i = new Intent("Schedule.ADDHOMEWORK");
                                i.putExtra("EDIT", true);
                                i.putExtra("ID", homework.getId());

                                startActivity(i);
                                break;
                        }
                        return true;
                    }
                });

                // Import all the views
                subject = (TextView) findViewById(R.id.subject);
                duein = (TextView) findViewById(R.id.duein);
                title = (TextView) findViewById(R.id.title);
                content = (TextView) findViewById(R.id.content);
                done = (ActionButton) findViewById(R.id.action_button);
                subjectid = (TextView) findViewById(R.id.subjectid);
                dueinid = (TextView) findViewById(R.id.dueinid);
                layout = (RelativeLayout) findViewById(R.id.background);

                done.setButtonColor(getResources().getColor(R.color.accent));
                done.setButtonColorPressed(getResources().getColor(R.color.accent_light));

                updateView(homework.getDone());
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DB db = new DB(getApplicationContext());
                        db.open();
                        // Set the update for the database, with what's done and what isn't
                        homework.setOp(DB.OP.UPDATE);

                        // If it wasn't done before
                        if (homework.getDone() == 0) {
                            homework.setDone(1);
                            db.runHomework(homework);
                            updateView(1);
                        }
                        // If it was done before
                        else {
                            homework.setDone(0);
                            db.runHomework(homework);
                            updateView(0);
                        }
                        db.close();
                    }
                });

                title.setTextSize(35);

            } else {
                setContentView(R.layout.view_homework_error);
            }
        } else {
            setContentView(R.layout.view_homework_error);
        }

        db.close();
    }


    public void updateView(int doneStatus) {
        if (doneStatus == 0) {
            done.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_white_24dp));
            done.setImageResource(R.drawable.ic_done_white_24dp);

            title.setTextColor(getResources().getColor(R.color.fab_material_grey_900));
            duein.setTextColor(getResources().getColor(R.color.fab_material_grey_900));
            title.setTextColor(getResources().getColor(R.color.black));
            content.setTextColor(getResources().getColor(R.color.fab_material_grey_900));
            subject.setTextColor(getResources().getColor(R.color.fab_material_grey_900));
            subjectid.setTextColor(getResources().getColor(R.color.fab_material_grey_900));
            dueinid.setTextColor(getResources().getColor(R.color.fab_material_grey_900));
            layout.setBackgroundColor(getResources().getColor(R.color.background_colour));
        } else {
            done.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_all_white_24dp));
            done.setImageResource(R.drawable.ic_done_all_white_24dp);

            title.setTextColor(getResources().getColor(R.color.faded_text));
            duein.setTextColor(getResources().getColor(R.color.faded_text));
            title.setTextColor(getResources().getColor(R.color.faded_text));
            subject.setTextColor(getResources().getColor(R.color.faded_text));
            content.setTextColor(getResources().getColor(R.color.faded_text));
            subjectid.setTextColor(getResources().getColor(R.color.faded_text));
            dueinid.setTextColor(getResources().getColor(R.color.faded_text));
            layout.setBackgroundColor(getResources().getColor(R.color.faded));
        }
    }

    @Override
    protected void onResume() {
        // Database query - get updates
        DB db = new DB(getApplicationContext());
        db.open();
        homework = db.getHomework(homeworkID);
        Subjects sub = db.getSubject(homework.getSubject());
        db.close();

        // Check if everything is null and if not, update the view
        if (duein != null && title != null && content != null) {
            duein.setText(Html.fromHtml(Methods.reverseDate(homework.getDueDate()) + " at " + homework.getTime() + "<br/>" + Methods.getBreakdown(homework.getDueDate(), homework.getTime(), false)));
            title.setText(homework.getTitle());
            content.setText(homework.getContents());
            subject.setText(sub.getName());
            updateColour(Color.parseColor(sub.getColour()));

        }

        updateView(homework.getDone());
        super.onResume();
    }


    public void updateColour(int colour) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(colour);
        }
        t.setBackgroundColor(colour);
    }

}
