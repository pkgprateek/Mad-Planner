package com.pkgprateek.madplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.software.shell.fab.ActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.Exams;
import com.pkgprateek.madplanner.db.Homeworks;
import com.pkgprateek.madplanner.db.Reminders;
import com.pkgprateek.madplanner.db.Rooms;
import com.pkgprateek.madplanner.db.Rotations;
import com.pkgprateek.madplanner.db.Subjects;
import com.pkgprateek.madplanner.db.TimetableSlot;
import com.pkgprateek.madplanner.ex.Methods;

/**
 * Created by pkgprateek on 15/11/15.
 */
public class Now extends Fragment {

    private SharedPreferences prefs;
    private LayoutInflater inflater;
    private LinearLayout mainLayout;
    private FloatingActionButton addHomework, addExam, addReminder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /**
         * Input the layout, and the linearlayout we're adding everything too
         */
        View v = inflater.inflate(R.layout.fragment_now, null);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mainLayout = (LinearLayout) v.findViewById(R.id.now_feed);
        this.inflater = inflater;

        /**
         * Setup the Addition button for the reminders and the homeworks and the reminders...
         *  Using the Clan FAB library
         */
        addHomework = (FloatingActionButton) v.findViewById(R.id.button_homework);
        addExam = (FloatingActionButton) v.findViewById(R.id.button_exams);
        addReminder = (FloatingActionButton) v.findViewById(R.id.button_remind);
        addHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("Schedule.ADDHOMEWORK");
                startActivity(i);
            }
        });
        addExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("Schedule.ADDEXAM");
                startActivity(i);
            }
        });
        addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("Schedule.ADDREMINDER");
                startActivity(i);
            }
        });


        /**
         * Run the ASyncTask to show the display. All the while show a loading dialog
         */
        LoadDisplay display = new LoadDisplay();
        display.execute(mainLayout);

        return v;
    }


    /**
     * ASyncTask to load the main display. In order for the continuous feed,
     * there is quite a lot of heavy SQL querying
     */

    public class LoadDisplay extends AsyncTask<LinearLayout,Integer,Boolean> {
        // OnPreExecute method - Get the timetable slots and query stuff here
        List<List<TimetableSlot>> slotsList = new ArrayList<>();
        List<DateIdentifier> datesToShow = new ArrayList<>();
        private int NUMBER_OF_DAYS = 3;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /**
             * Get the available dates to Show here
             */

            int index = 0;
            int incremental = 0;
            Date currentDate = new Date();
            DB db = new DB(getContext());
            db.open();
            int week = db.getWeek(currentDate);
            List<String> days = db.getDays(week);



            while (index < NUMBER_OF_DAYS && incremental < 14) {
                if (days.contains(Methods.getDay(currentDate))) {

                    Date storeDate = new Date();
                    storeDate.setTime(currentDate.getTime());
                    DateIdentifier dateID = new DateIdentifier(storeDate, true);
                    dateID.addHomeworks(db.getHomeworks(Methods.getDate(storeDate), false));
                    dateID.addExams(db.getExams(Methods.getDate(storeDate), false));
                    dateID.addReminders(db.getReminders(Methods.getDate(storeDate)));
                    datesToShow.add(dateID);
                    index++;
                } else {
                    // Check if there's any homeworks or exams due in on that day
                    if (db.getNumberOfHomeworks(currentDate) != 0
                            || db.getNumberOfExams(currentDate) != 0
                            || db.getNumberOfReminders(currentDate) != 0) {
                        Date storeDate = new Date();
                        storeDate.setTime(currentDate.getTime());
                        DateIdentifier id = new DateIdentifier(storeDate, false);
                        id.addHomeworks(db.getHomeworks(Methods.getDate(storeDate), false));
                        id.addExams(db.getExams(Methods.getDate(storeDate), false));
                        id.addReminders(db.getReminders(Methods.getDate(storeDate)));
                        datesToShow.add(id);
                    }
                }
                // Increment to the next day
                currentDate.setTime(currentDate.getTime() + (1000 * 60 * 60 * 24));
                incremental++;
            }


            db.close();
        }

        @Override
        protected Boolean doInBackground(LinearLayout... params) {
            /**
             * Get the slots for the number of next dates
             */

            DB db = new DB(getContext());
            db.open();

            for (int i = 0; i < datesToShow.size(); i++) {
                DateIdentifier dd = datesToShow.get(i);
                if (dd.isTimetable()) {
                    if (slotsList.size() == 0 && Methods.getDay(dd.getDate()).equals(Methods.getDay(new Date())))
                        slotsList.add(db.getDaysLessons(dd.getDate(), Methods.getTime(dd.getDate())));
                    else
                        slotsList.add(db.getDaysLessons(dd.getDate()));
                }
            }

            db.close();

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            /**
             * Constructing the display one step at a time
             * First day
             */
            int index = 0;
            for (int i = 0; i < datesToShow.size(); i++) {
                DateIdentifier dd = datesToShow.get(i);
                // We need a header view
                mainLayout = addHeader(mainLayout, dd.getDate());
                if (dd.isTimetable()) {
                    // Construct the timetable breakdown view.
                    mainLayout = addViews(mainLayout, dd.getDate(), slotsList.get(index), i);
                    index++;
                } else {
                    for (int a = 0; a < dd.homeworks.size(); a++) {
                        mainLayout = addEvent(mainLayout, dd.homeworks.get(a));
                    }
                    for (int a = 0; a < dd.exams.size(); a++) {
                        mainLayout = addEvent(mainLayout, dd.exams.get(a));
                    }
                    for (int a = 0; a < dd.reminders.size(); a++) {
                        mainLayout = addEvent(mainLayout, dd.reminders.get(a));
                    }
                }
                mainLayout = addDayFinisher(mainLayout);
            }
        }


        /**
         * Method to check inline if a homework, exam
         */
        public LinearLayout checkInline(LinearLayout layout, int index) {
            return checkInline(layout,index,new TimetableSlot(getContext(), -1, "23:59", "24:00", -1, "null", "null", 0, "null", "null", 0));
        }
        public LinearLayout checkInline(LinearLayout layout, int index, TimetableSlot s) {
            if (datesToShow.get(index).getEvents() != 0) {
                for (int h = 0; h < datesToShow.get(index).homeworks.size(); h++) {
                    if (Methods.convert(s.getEndTime())
                            > Methods.convert(datesToShow.get(index).homeworks.get(h).getTime())) {
                        layout = addEvent(layout, datesToShow.get(index).homeworks.get(h));
                        datesToShow.get(index).homeworks.remove(h);
                    }
                }
                for (int h = 0; h < datesToShow.get(index).exams.size(); h++) {
                    if (Methods.convert(s.getEndTime())
                            > Methods.convert(datesToShow.get(index).exams.get(h).getTime())) {
                        layout = addEvent(layout, datesToShow.get(index).exams.get(h));
                        datesToShow.get(index).exams.remove(h);
                    }
                }
                for (int h = 0; h < datesToShow.get(index).reminders.size(); h++) {
                    if (Methods.convert(s.getEndTime())
                            > Methods.convert(datesToShow.get(index).reminders.get(h).getTime())) {
                        layout = addEvent(layout, datesToShow.get(index).reminders.get(h));
                        datesToShow.get(index).reminders.remove(h);

                    }
                }
            }
            return layout;
        }

        /**
         * Add the TimetableSlots to the main layout
         */
        public LinearLayout addViews(LinearLayout layout, Date day, List<TimetableSlot> slots, int index) {
            // Do the day one information
            if (slots.size() != 0) {
                for (int i = 0; i < slots.size(); i++) {

                    // Check if there's any other homework items
                    layout = checkInline(layout,index,slots.get(i));

                    // Get the slot in question and inflate the view
                    TimetableSlot slot = slots.get(i);
                    View item = inflater.inflate(R.layout.fragment_now_item, null);
                    TextView subject = (TextView) item.findViewById(R.id.lessontitle);
                    TextView time = (TextView) item.findViewById(R.id.time);
                    TextView details = (TextView) item.findViewById(R.id.room);
                    ImageView image = (ImageView) item.findViewById(R.id.colour);

                    // Apply the values to the slot
                    image.setBackgroundColor(Color.parseColor(slot.getColour()));
                    subject.setText(slot.getName());
                    details.setText(slot.getRoom() + " with " + slot.getTeacher());
                    if (slot.getLength() >= 1) {
                        DB db = new DB(getContext());
                        db.open();
                        slot.setEndTime(db.getEndTime(slot.getPeriod() + slot.getLength()));
                        db.close();
                    }

                    // If it's today, show the timer
                    if (Methods.getDay(day).equals(Methods.getDay(new Date()))) {
                        time.setText(Methods.getBreakdown(Methods.getDate(day), slot.getStartTime(), true, false) + "\n"
                        + slot.getStartTime() + " - " + slot.getEndTime());
                    } else {
                        time.setText(slot.getStartTime() + " - " + slot.getEndTime());
                    }

                    // Check if there's any homeworks or exams due in for this subject at this time
                    TextView homework = (TextView) item.findViewById(R.id.homework);
                    TextView exam = (TextView) item.findViewById(R.id.exam);
                    if (!(slot.getHomeworks().size() == 0 && slot.getExams().size() == 0)) {
                        LinearLayout dueStuff = (LinearLayout) item.findViewById(R.id.due_body);
                        for (int h = 0; h < slot.getHomeworks().size(); h++) {
                            homework.setVisibility(View.VISIBLE);
                            final Homeworks homeworkI = slot.getHomeworks().get(h);
                            View homeworkItem = inflater.inflate(R.layout.fragment_now_homework, null);
                            if (homeworkI.getDone() != 1) {
                                ((TextView) homeworkItem.findViewById(R.id.homeworkTitle)).setText(homeworkI.getTitle());
                                ((TextView) homeworkItem.findViewById(R.id.homeworkDesc)).setText(homeworkI.getContents());
                            } else {
                                TextView title = (TextView) homeworkItem.findViewById(R.id.homeworkTitle);
                                title.setTextColor(getResources().getColor(R.color.faded_text));
                                title.setText(homeworkI.getTitle());
                                TextView desc = (TextView) homeworkItem.findViewById(R.id.homeworkDesc);
                                desc.setTextColor(getResources().getColor(R.color.faded_text));
                                desc.setText(homeworkI.getContents());
                            }
                            homeworkItem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent("Schedule.VIEWHOMEWORK");
                                    i.putExtra("ID", homeworkI.getId());
                                    startActivity(i);
                                }
                            });
                            dueStuff.addView(homeworkItem);
                        }
                        for (int h = 0; h < slot.getExams().size(); h++) {

                            exam.setVisibility(View.VISIBLE);
                            // Import the exams with Exams exam = slot.getExams().get(h);
                            final Exams examI = slot.getExams().get(h);
                            View examItem = inflater.inflate(R.layout.fragment_now_exam, null);
                            ((TextView) examItem.findViewById(R.id.examTitle)).setText(examI.getTitle());
                            ((TextView) examItem.findViewById(R.id.examDesc)).setText(examI.getContents());
                            String summary = "";
                            if (!examI.getSeatnumber().equals("")) {
                                summary += "Seat: " + examI.getSeatnumber() + "\n";
                            }
                            DB db = new DB(getContext());
                            db.open();
                            String room = db.getRoom(examI.getRoom());
                            if (!room.equals(slot.getRoom())) {
                                room = room.substring(0, 8) + "...";
                                summary += "Room: " + room + "\n";
                            }
                            summary += examI.getDuration() + " minutes";
                            ((TextView) examItem.findViewById(R.id.examDets)).setText(summary);

                            examItem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent("Schedule.VIEWEXAM");
                                    i.putExtra("ID", examI.getId());
                                    startActivity(i);
                                }
                            });
                            dueStuff.addView(examItem);
                        }
                    }

                    layout.addView(item);

                }

                // Check if there's any other homework items
                layout = checkInline(layout,index);
            } else {
                layout.addView(inflater.inflate(R.layout.fragment_now_empty, null));
            }
            return layout;
        }

        public LinearLayout addDayFinisher(LinearLayout layout) {
            layout.addView(inflater.inflate(R.layout.fragment_now_bottom, null));
            return layout;
        }

        public LinearLayout addHeader(LinearLayout layout, Date day) {
            View v = inflater.inflate(R.layout.fragment_now_top, null);
            TextView header = (TextView) v.findViewById(R.id.title);
            switch (Methods.getDaysBetween(day,new Date())) {
                case 0:
                    header.setText("Today");
                    break;
                case 1:
                    header.setText("Tomorrow");
                    break;
                default:
                    header.setText(Methods.getDay(day));
                    break;
            }
            ((TextView) v.findViewById(R.id.info)).setText("Showing " + Methods.getDay(day) + ", " + Methods.getDate(day));
            layout.addView(v);
            return layout;
        }

        public LinearLayout addEvent(LinearLayout layout, Homeworks h) {
            View v = inflater.inflate(R.layout.fragment_now_event_single, null);
            TextView title, contents, time, details;
            ImageView border, colour;
            title = (TextView) v.findViewById(R.id.title);
            contents = (TextView) v.findViewById(R.id.contents);
            time = (TextView) v.findViewById(R.id.time);
            details = (TextView) v.findViewById(R.id.details);
            border = (ImageView) v.findViewById(R.id.border);
            colour = (ImageView) v.findViewById(R.id.colour);

            DB db = new DB(getContext());
            db.open();
            Subjects s = db.getSubject(h.getSubject());
            db.close();

            border.setBackgroundColor(getResources().getColor(R.color.homework_indicator));
            title.setText(s.getShortName() + ": " + h.getTitle());
            contents.setText(h.getContents());
            if (h.getDueDate().equals(Methods.getDate(new Date()))) {
                time.setText(Methods.getBreakdown(h.getDueDate(), h.getTime(), true));
            } else {
                time.setText(h.getTime());
            }
            if (h.getDone() == 1) {
                title.setTextColor(getResources().getColor(R.color.faded_text));
                contents.setTextColor(getResources().getColor(R.color.faded_text));
                time.setTextColor(getResources().getColor(R.color.faded_text));
                border.setBackgroundColor(getResources().getColor(R.color.homework_done_indicator));
                details.setBackgroundColor(getResources().getColor(R.color.homework_done_indicator));
                details.setTextColor(getResources().getColor(R.color.white));
                details.setText("HOMEWORK DONE");
            } else {
                details.setText("HOMEWORK");
                details.setTextColor(getResources().getColor(R.color.white));
                details.setBackgroundColor(getResources().getColor(R.color.homework_indicator));
            }
            colour.setBackgroundColor(s.getColourInt());
            layout.addView(v);
            return layout;
        }
        public LinearLayout addEvent(LinearLayout layout, Exams e) {
            View v = inflater.inflate(R.layout.fragment_now_event_single, null);
            TextView title, contents, time, details;
            ImageView border, colour;
            title = (TextView) v.findViewById(R.id.title);
            contents = (TextView) v.findViewById(R.id.contents);
            time = (TextView) v.findViewById(R.id.time);
            details = (TextView) v.findViewById(R.id.details);
            DB db = new DB(getContext());
            db.open();
            Subjects s = db.getSubject(e.getSubject());
            border = (ImageView) v.findViewById(R.id.border);
            colour = (ImageView) v.findViewById(R.id.colour);

            title.setText(s.getShortName() + ": " + e.getTitle());
            contents.setText(e.getContents());
            if (e.getDate().equals(Methods.getDate(new Date()))) {
                time.setText(Methods.getBreakdown(e.getDate(), e.getTime(), true));
            } else {
                time.setText(e.getTime());
            }
            String r = db.getRoom(e.getRoom());
            details.setText("Exam: " + r + ", " + e.getDuration() + " minutes");
            if (!e.getSeatnumber().equals(""))
                details.append(" in seat " + e.getSeatnumber());
            border.setBackgroundColor(getResources().getColor(R.color.homework_indicator));
            details.setTextColor(getResources().getColor(R.color.white));
            details.setBackgroundColor(getResources().getColor(R.color.homework_indicator));
            colour.setBackgroundColor(s.getColourInt());
            final int examID = e.getId();
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent("Schedule.VIEWEXAM");
                    i.putExtra("ID", examID);
                    startActivity(i);
                }
            });
            layout.addView(v);
            db.close();
            return layout;
        }
        public LinearLayout addEvent(LinearLayout layout, Reminders rem) {
            View v = inflater.inflate(R.layout.fragment_now_event_single, null);
            TextView title, contents, time, details;
            title = (TextView) v.findViewById(R.id.title);
            contents = (TextView) v.findViewById(R.id.contents);
            time = (TextView) v.findViewById(R.id.time);
            details = (TextView) v.findViewById(R.id.details);
            title.setText(rem.getTitle());
            contents.setText(rem.getContents());
            if (rem.getDueDate().equals(Methods.getDate(new Date()))) {
                time.setText(Methods.getBreakdown(rem.getDueDate(), rem.getTime(), true));
            } else {
                time.setText(rem.getTime());
            }
            details.setVisibility(View.GONE);
            layout.addView(v);
            return layout;
        }
    }

    private class DateIdentifier {
        private Date d;
        private boolean isTimetable;
        public List<Homeworks> homeworks;
        public List<Exams> exams;
        public List<Reminders> reminders;
        public DateIdentifier(Date d, boolean isTimetable) {
            this.d = d;
            this.isTimetable = isTimetable;
            homeworks = new ArrayList<>();
            exams = new ArrayList<>();
            reminders = new ArrayList<>();
        }

        public Date getDate() {
            return d;
        }

        public void setDate(Date d) {
            this.d = d;
        }

        public boolean isTimetable() {
            return isTimetable;
        }

        public void setIsTimetable(boolean isTimetable) {
            this.isTimetable = isTimetable;
        }

        public void addHomework(Homeworks h) {
            homeworks.add(h);
        }
        public void addHomeworks(List<Homeworks> h) {
            for (int i = 0; i < h.size(); i++) {
                homeworks.add(h.get(i));
            }
        }
        public void addExam(Exams e) {
            exams.add(e);
        }
        public void addExams(List<Exams> e) {
            for (int i = 0; i < e.size(); i++) {
                exams.add(e.get(i));
            }
        }
        public void addReminders(Reminders rem) {
            reminders.add(rem);
        }
        public void addReminders(List<Reminders> rem) {
            for (int i = 0; i < rem.size(); i++) {
                reminders.add(rem.get(i));
            }
        }

        public int getEvents() {
            return homeworks.size() + exams.size() + reminders.size();
        }
    }
}
