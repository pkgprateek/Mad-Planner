package com.pkgprateek.madplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.software.shell.fab.ActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.Homeworks;
import com.pkgprateek.madplanner.db.Periods;
import com.pkgprateek.madplanner.db.Subjects;
import com.pkgprateek.madplanner.ex.Methods;


/**
 * Created by pkgprateek on 11/11/15.
 */
public class AddHomework extends AppCompatActivity {

    TextView result;
    private int day;
    private int month;
    private int year;
    private int hours = 12;
    private int minutes = 0;

    private String[] subjectArray;
    private String subjectChosen;
    private String dateChosen;
    private int indexForArray = -1;
    private int indexForPeriods = -1;
    private int period = -1;
    private int keepingIndexForArray = -1;

    private EditText title;
    private EditText content;
    private ActionButton submit;
    private Button subject;
    private Button pickDate;
    private Button chooseDate;
    private Button chooseTime;
    private LinearLayout body;
    private Toolbar t;

    private boolean subjectChecker = false;
    private boolean dateChecker = false;
    private boolean timeChecker = false;

    private Homeworks homeworkItem;

    private List<Subjects> subjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_homework2);

        /**
         * Menial tasks, setting up the layout of the UI and the toolbar
         */
        t = (Toolbar) findViewById(R.id.toolbar);
        t.setTitle("Add Homework");
        t.setTitleTextColor(getResources().getColor(R.color.white));
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * Input control from the views
         */
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        submit = (ActionButton) findViewById(R.id.action_button);
        result = (TextView) findViewById(R.id.result);
        subject = (Button) findViewById(R.id.subject);
        pickDate = (Button) findViewById(R.id.pickdate);
        body = (LinearLayout) findViewById(R.id.editer);
        body.setVisibility(View.GONE);
        chooseDate = (Button) findViewById(R.id.changedate);
        chooseTime = (Button) findViewById(R.id.changetime);

        // Monitor the changing of the text when typing
        TextWatcher tW = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) { showSubmit();}
        };
        title.addTextChangedListener(tW);
        content.addTextChangedListener(tW);

        // Setup the submit button to save it
        submit.setButtonColor(getResources().getColor(R.color.accent));
        submit.setButtonColorPressed(getResources().getColor(R.color.accent_light));
        submit.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_white_24dp));
        submit.setImageResource(R.drawable.ic_done_white_24dp);
        submit.hide();

        // submit.setHideAnimation(ActionButton.Animations.JUMP_TO_DOWN);
        // submit.setShowAnimation(ActionButton.Animations.JUMP_FROM_DOWN);

        /**
         *  Initialise the SimpleDateFormatters and setup the date
         */
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);


        /**
         * Subject code - Select the subject it's due for.
         *  - Get the subjects from the database
         *  - Check if there's a subject to set a homework for.
         */
        DB db = new DB(this);
        db.open();
        subjectList = db.getAllSubjects();

        // Convert them into a string array of only the names - For the selector
        subjectArray = new String[subjectList.size()];
        for (int i = 0; i < subjectList.size(); i++) {
            subjectArray[i] = subjectList.get(i).getName();
        }



        /**
         * Everything has been set up - Check if we're editing something now
         *  Check if we're editing a current Homework item.
         * Do this through bundles
         */
        Bundle b = getIntent().getExtras();
        homeworkItem = new Homeworks(DB.OP.CREATE);
        try {
            if (b.getBoolean("EDIT")) {

                // If it's set to edit, get the homework item and set it to an update
                homeworkItem = db.getHomework(b.getInt("ID"));
                homeworkItem.setOp(DB.OP.UPDATE);

                Subjects sub = db.getSubject(homeworkItem.getSubject());
                subject.setText(sub.getName());
                updateColour(sub.getColourInt());

                title.setText(homeworkItem.getTitle());
                content.setText(homeworkItem.getContents());


                // Correct the year, month, day stuff
                String[] dat = homeworkItem.getDueDate().split("/");
                year = Integer.parseInt(dat[0]);
                month = Integer.parseInt(dat[1]);
                day = Integer.parseInt(dat[2]);


                /**
                 * Need to update the display with the information here
                 */

                // need to set keepingIndexForArray;

                for (int i = 0; i < subjectList.size(); i++) {
                    if (subjectList.get(i).getId() == homeworkItem.getSubject()) {
                        keepingIndexForArray = i;
                    }
                }

                subjectChecker = true;
                dateChecker = true;
                timeChecker = true;

                updateSwitcher(dateChecker, timeChecker);
                showUpdate();
                showSubmit();
            }
        } catch (NullPointerException e) {
            /*
                Adding a homework item, as cannot find any EDIT settings.
             */
            pickDate.setVisibility(View.GONE);
            homeworkItem = new Homeworks(DB.OP.CREATE);
        }

        db.close();


        /**
         * Get all the subjects
         *  subjectArrayForSubjects holds the values
         *  OnClick for the pickDate
         *                  chooseDate
         *                  chooseTime
         *                  Submit
         *
         */
        subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickSubject();
            }
        });
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPeriod(homeworkItem.getSubject());
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                homeworkItem.setTitle(title.getText().toString());
                homeworkItem.setContents(content.getText().toString());

                DB db = new DB(getApplicationContext());
                db.open();
                db.runHomework(homeworkItem);
                db.close();
                finish();
            }
        });

    }


    /**
     * Method to pick which subject the homework will be due in for
     */

    public void pickSubject() {
        if (subjectList.size() != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Which subject is this due in for?");
            builder.setSingleChoiceItems(subjectArray, keepingIndexForArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (keepingIndexForArray != which) {
                        indexForArray = which;
                    }
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (keepingIndexForArray != indexForArray) {
                        homeworkItem.setSubject(subjectList.get(indexForArray).getId());

                        homeworkItem.setPeriod(-1);
                        homeworkItem.setTime("");
                        homeworkItem.setDueDate("");

                        subject.setText(subjectList.get(indexForArray).getName());
                        subjectChecker = true;
                        pickDate.setVisibility(View.VISIBLE);
                        dateChecker = false;
                        timeChecker = false;
                        updateSwitcher(dateChecker, timeChecker);
                        showSubmit();
                        result.setText("");
                        keepingIndexForArray = indexForArray;
                        updateColour(subjectList.get(indexForArray).getColourInt());
                    }
                }
            });
            builder.create().show();
        }
    }

    /**
     * Method to pick the date that it's due in for
     *
     */
    public void pickDate() {
        DatePickerDialog dialog = new DatePickerDialog(AddHomework.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int yearAfterBC, int monthOfYear, int dayOfMonth) {
                year = yearAfterBC;
                month = monthOfYear + 1;
                day = dayOfMonth;

                dateChecker = true;
                homeworkItem.setDueDate(yearAfterBC + "/" + Methods.convert(month) + "/" + Methods.convert(day));

                showUpdate();

                pickPeriod(homeworkItem.getSubject());
            }
        }, year, month - 1, day);
        dialog.show();
    }


    /**
     * Method to pick the time that it's due in for, maybe the period thats
     * selected
     */
    public void pickPeriod(int subject) {

        // Get the day it will be
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        Date d = cal.getTime();
        String dateString = Methods.dayOfWeekFormatter.format(d);
        Calendar first = (Calendar) cal.clone();
        first.add(Calendar.DAY_OF_WEEK,
                first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));
        String startString = Methods.dateFormatter.format(first.getTime());


        // Database opening
        DB db = new DB(getApplicationContext());
        db.open();


        /** Get the list of periods available on that day - And setup a String[] array in the form of
         *  [0] - Period 1, 12:00 to 13:00
         *  [1] - Period 3, 14:00 to 15:00
         *  .....
         *  [n-1] - Period m, x to y
         *  [n] - Custom (will launch the timepicker)
         */

        final List<Periods> periods = db.getPeriodsAvailableToHomeworks(dateString, startString, subject);


        Log.i("Schedule", "Date string : " + dateString);
        Log.i("Schedule", "Start of the week date: " + startString);
        Log.i("Schedule", "Subject     : " + subject);


        if (periods.size() != 0) {
            final String[] periodsStringArrayForSelector = new String[periods.size() + 1];
            for (int i = 0; i < periods.size(); i++) {
                periodsStringArrayForSelector[i] = "Period " + periods.get(i).getId() + ", starting " + periods.get(i).getStartTime();
            }
            periodsStringArrayForSelector[periods.size()] = "Custom time...";

            /**
             * Construct the Alert Dialog for picking which period it's due in for
             */
            AlertDialog.Builder builder = new AlertDialog.Builder(AddHomework.this);
            builder.setTitle("Would you like it due in for a lesson that day?");
            builder.setSingleChoiceItems(periodsStringArrayForSelector, indexForPeriods, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    indexForPeriods = which;
                }
            });
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (indexForPeriods != periods.size()) {
                        homeworkItem.setPeriod(periods.get(indexForPeriods).getId());
                        hours = Integer.parseInt(periods.get(indexForPeriods).getStartTime().split(":")[0]);
                        minutes = Integer.parseInt(periods.get(indexForPeriods).getStartTime().split(":")[1]);
                        homeworkItem.setTime(periods.get(indexForPeriods).getStartTime());

                        timeChecker = true;

                        showUpdate();
                        showSubmit();
                        updateSwitcher(dateChecker, timeChecker);
                    } else {
                        pickTime();
                    }
                }
            });
            builder.create().show();

        } else {
            pickTime();
        }
    }


    /**
     * Pick the time that the homework item is due in for.
     */
    public void pickTime() {
        TimePickerDialog dialog = new TimePickerDialog(AddHomework.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hours = hourOfDay;
                minutes = minute;

                homeworkItem.setPeriod(-1);
                homeworkItem.setTime(Methods.convert(hourOfDay) + ":" + Methods.convert(minute));
                timeChecker = true;

                showUpdate();
                showSubmit();
                updateSwitcher(dateChecker, timeChecker);
            }
        }, hours, minutes, true);
        dialog.show();
    }


    /**
     * Show the update
     */
    public void showUpdate() {
        if (homeworkItem.getPeriod() != -1)
            result.setText(Methods.convert(day) + " " + Methods.getMonth(month) + " for Period " + homeworkItem.getPeriod()
                    + ",\nstarting at " + Methods.convert(hours) + ":" + Methods.convert(minutes));
        else
            result.setText(Methods.convert(day) + " " + Methods.getMonth(month) + " for " + Methods.convert(hours) + ":" + Methods.convert(minutes));
    }



    /**
     * Determine if the submit button will be shown
     */
    public void showSubmit() {
        if (dateChecker && timeChecker && subjectChecker && homeworkItem.validate() && !title.getText().toString().equals("")) {
            submit.show();
        } else {
            submit.hide();
        }
    }

    /**
     * Method to display the colour when the subject is picked
     *
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
     * Method to decide which buttons show based on dateChecker
     */
    public void updateSwitcher(boolean selected, boolean selected2) {
        if (selected && selected2) {
            body.setVisibility(View.VISIBLE);
            pickDate.setVisibility(View.GONE);
        } else {
            body.setVisibility(View.GONE);
            pickDate.setVisibility(View.VISIBLE);
        }
    }
}
