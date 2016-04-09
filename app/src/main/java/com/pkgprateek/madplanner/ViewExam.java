package com.pkgprateek.madplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.Exams;
import com.pkgprateek.madplanner.db.Rooms;
import com.pkgprateek.madplanner.db.Subjects;
import com.pkgprateek.madplanner.ex.Methods;

/**
 * Created by pkgprateek on 04/12/15.
 */
public class ViewExam extends AppCompatActivity {

    private Toolbar toolbar;
    private int examID;
    private Exams exam;

    private TextView subject;
    private TextView duein;
    private TextView room;
    private TextView seat;
    private TextView duration;

    private TextView title;
    private TextView contents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_exam);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            examID = extras.getInt("ID");
        }

        // Setup the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Exam");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        toolbar.inflateMenu(R.menu.viewhomework);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewExam.this);
                        builder.setTitle("Are you sure?");
                        builder.setMessage("Are you sure you want to delete this homework event?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                exam.setOp(DB.OP.DESTROY);
                                DB newDB = new DB(ViewExam.this);
                                newDB.open();
                                newDB.runExam(exam);
                                newDB.close();
                                finish();
                            }
                        });
                        builder.setNegativeButton("No", null);
                        builder.create().show();
                        break;
                    case R.id.action_edit:
                        Intent i = new Intent("Schedule.ADDEXAM");
                        i.putExtra("EDIT", true);
                        i.putExtra("ID", exam.getId());

                        startActivity(i);
                        break;
                }
                return true;
            }
        });


        /**
         * Set up the views on the display
         */
        subject = (TextView) findViewById(R.id.subject);
        duein = (TextView) findViewById(R.id.duein);
        room = (TextView) findViewById(R.id.room);
        seat = (TextView) findViewById(R.id.seat);
        title = (TextView) findViewById(R.id.title);
        contents = (TextView) findViewById(R.id.content);
        duration = (TextView) findViewById(R.id.duration);
    }




    @Override
    protected void onResume() {

        DB newDB = new DB(getApplicationContext());
        newDB.open();
        exam = newDB.getExam(examID);

        Subjects subjectDB = newDB.getSubject(exam.getSubject());
        String roomDB = newDB.getRoom(exam.getRoom());
        String dueinDB = Methods.reverseDate(exam.getDate()) + " at " + exam.getTime() + "\n" + Methods.getBreakdown(exam.getDate(), exam.getTime(), false);
        subject.setText(subjectDB.getName());
        duein.setText(dueinDB);
        room.setText(roomDB);

        newDB.close();

        seat.setText(exam.getSeatnumber());
        title.setText(exam.getTitle());
        contents.setText(exam.getContents());
        updateColour(subjectDB.getColourInt());
        duration.setText(exam.getDuration() + " minutes");

        super.onResume();
    }



    public void updateColour(int colour) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(colour);
        }
        toolbar.setBackgroundColor(colour);
    }

}
