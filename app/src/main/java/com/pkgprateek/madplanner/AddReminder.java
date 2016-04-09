package com.pkgprateek.madplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.software.shell.fab.ActionButton;

import java.util.Calendar;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.Reminders;

/**
 * Created by pkgprateek on 11/11/15.
 */
public class AddReminder extends AppCompatActivity {

    private Reminders reminder;
    private EditText title;
    private EditText desc;
    private Button date;
    private Button remove;
    private Button changeTime;
    private Button changeDate;
    private TextView datelabel;
    private ActionButton submit;
    private String dateString = "";
    private String timeString = "";
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minutes;
    private LinearLayout edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reminder);



        /**
         * Menial tasks, setting up the layout of the UI and the toolbar
         */
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        t.setTitle("Add Reminder");
        t.setTitleTextColor(getResources().getColor(R.color.white));
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        /**
         * Components of the display
         */
        reminder = new Reminders(DB.OP.CREATE);
        title = (EditText) findViewById(R.id.title);
        desc = (EditText) findViewById(R.id.contents);
        date = (Button) findViewById(R.id.date);
        datelabel = (TextView) findViewById(R.id.datelabel);
        submit = (ActionButton) findViewById(R.id.action_button);
        edit = (LinearLayout) findViewById(R.id.editer);
        remove = (Button) findViewById(R.id.remove);
        changeTime = (Button) findViewById(R.id.changetime);
        changeDate = (Button) findViewById(R.id.changedate);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the date and the time
                getDate(true);
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setVisibility(View.GONE);
                date.setVisibility(View.VISIBLE);
                datelabel.setText("No date or time set.");
                reminder.setTime("");
                reminder.setDueDate("");
            }
        });
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate(false);
            }
        });
        changeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime();
            }
        });


        /**
         * Add listeners to the text fields, it will decide if the accept button is displayed
         * in order to add it to the database under the Reminder's table
         *
         */
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
        desc.addTextChangedListener(tW);




        /**
         * Submit button - To save the new/existing item to the database
         *
         */
        submit.setButtonColor(getResources().getColor(R.color.accent));
        submit.setButtonColorPressed(getResources().getColor(R.color.accent_light));
        submit.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_white_24dp));
        submit.hide();
        submit.setImageResource(R.drawable.ic_done_white_24dp);
        submit.setHideAnimation(ActionButton.Animations.JUMP_TO_DOWN);
        submit.setShowAnimation(ActionButton.Animations.JUMP_FROM_DOWN);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminder.setTitle(title.getText().toString());
                reminder.setContents(desc.getText().toString());
                DB db = new DB(getApplicationContext());
                db.open();
                db.runReminder(reminder);
                db.close();
                finish();
            }
        });


        /**
         * Setup the time variables
         * - Assign everything to here and now.
         */
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        hour = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);
        edit.setVisibility(View.GONE);


        /**
         * Checking if the edit fields are enabled.
         * If the edit flag is set, setup the reminder for edit
         */
        Bundle b = getIntent().getExtras();
        try {
            if (b.getBoolean("EDIT")) {
                int id = b.getInt("ID");

                DB db = new DB(getApplicationContext());
                db.open();
                reminder = db.getReminder(id);
                db.close();
                if (reminder.getTime() != null && reminder.getDueDate() != null) {
                    String[] dater = reminder.getDueDate().split("/");
                    year = Integer.parseInt(dater[0]);
                    month = Integer.parseInt(dater[1]);
                    day = Integer.parseInt(dater[2]);
                    String[] timer = reminder.getTime().split(":");
                    hour = Integer.parseInt(timer[0]);
                    minutes = Integer.parseInt(timer[1]);
                    datelabel.setText(getLabel());
                }
                reminder.setOp(DB.OP.UPDATE);

                title.setText(reminder.getTitle());
                desc.setText(reminder.getContents());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * ShowSubmit method. Will check if any combinations of input is valid, and
     * show the accept button dependent on that.
     */
    public void showSubmit() {
        if (!title.getText().toString().trim().equals("")) {
            submit.show();
        } else {
            submit.hide();
        }
    }

    public void getDate(boolean showTime) {
        final boolean showTime1 = showTime;
        DatePickerDialog dialog = new DatePickerDialog(AddReminder.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int yearInt, int monthOfYear, int dayOfMonth) {
                year = yearInt;
                month = monthOfYear + 1;
                day = dayOfMonth;
                if (showTime1) {
                    getTime();
                } else {
                    datelabel.setText(getLabel());
                    showSubmit();
                }
            }
        }, year, month - 1, day);
        dialog.show();
    }

    public void getTime() {
        TimePickerDialog dialog = new TimePickerDialog(AddReminder.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                minutes = minute;

                datelabel.setText(getLabel());
                showSubmit();
            }
        }, hour, minutes, true);
        dialog.show();
    }

    public String convert(int i) {
        if (i < 10)
            return "0" + i;
        else
            return i + "";
    }

    public String getLabel() {
        reminder.setDueDate(year + "/" + convert(month) + "/" + convert(day));
        reminder.setTime(convert(hour) + ":" + convert(minutes));
        edit.setVisibility(View.VISIBLE);
        date.setVisibility(View.GONE);
        return convert(day) + "/" + convert(month) + "/" + year + " at " + convert(hour) + ":" + convert(minutes);
    }
}
