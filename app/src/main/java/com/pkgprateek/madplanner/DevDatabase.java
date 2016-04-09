package com.pkgprateek.madplanner;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.Periods;
import com.pkgprateek.madplanner.db.Rooms;
import com.pkgprateek.madplanner.db.Rotations;
import com.pkgprateek.madplanner.db.Subjects;
import com.pkgprateek.madplanner.db.Teachers;
import com.pkgprateek.madplanner.db.Timetables;

import java.util.List;

/**
 * Created by pkgprateek on 01/12/15.
 */
public class DevDatabase extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dev_database);

        DB db = new DB(getApplicationContext());
        db.open();

        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        t.setTitle("Dev Database");
        t.setTitleTextColor(getResources().getColor(R.color.white));
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /**
         * Print out all the information from all the tables in the database for the timetable feature
         * Exams and Homeworks and Reminders can be tested in their respective fragments.
         */


        // Print out all records for Room
        List<Rooms> roomsList = db.getAllRooms();
        String printoutRoom = "";
        for (int i = 0; i < roomsList.size(); i++) {
            printoutRoom += roomsList.get(i).toString() + "\n";
        }
        if (printoutRoom.equals(""))
            printoutRoom = "No records available";
        ((TextView) findViewById(R.id.roomLayout)).setText(printoutRoom);



        // Print out all records for Teacher
        List<Teachers> teachersList = db.getAllTeachers();
        String printoutTeacher = "";
        for (int i = 0; i < teachersList.size(); i++) {
            printoutTeacher += teachersList.get(i).toString() + "\n";
        }
        if (printoutTeacher.equals(""))
            printoutTeacher = "No records available";
        ((TextView) findViewById(R.id.teacherLayout)).setText(printoutTeacher);



        // Print out all records for Periods
        List<Periods> periodsList = db.getAllPeriods();
        String printoutPeriods = "";
        for (int i = 0; i < periodsList .size(); i++) {
            printoutPeriods += periodsList .get(i).toString() + "\n";
        }
        if (printoutPeriods.equals(""))
            printoutPeriods = "No records available";
        ((TextView) findViewById(R.id.periodLayout)).setText(printoutPeriods);



        // Print out all records for Subjects
        List<Subjects> subjectList = db.getAllSubjects();
        String printoutSubject = "";
        for (int i = 0; i < subjectList.size(); i++) {
            printoutSubject += subjectList.get(i).toString() + "\n";
        }
        if (printoutSubject.equals(""))
            printoutSubject = "No records available";
        ((TextView) findViewById(R.id.subjectLayout)).setText(printoutSubject);


        // Print out all records for Timetables
        List<Timetables> timetableList = db.getAllTimetables();
        String printoutTimetable = "";
        for (int i = 0; i < timetableList.size(); i++) {
            printoutTimetable += timetableList.get(i).toString() + "\n";
        }
        if (printoutTimetable.equals(""))
            printoutTimetable = "No records available";
        ((TextView) findViewById(R.id.timetableLayout)).setText(printoutTimetable);

        db.close();

        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /**
                 * Test scenario for the database
                 */


                DB newDB = new DB(getApplicationContext());
                newDB.open();


                Rooms R1 = new Rooms(DB.OP.CREATE, -1, "GA L01");
                Rooms R2 = new Rooms(DB.OP.CREATE, -1, "GA L02");
                Rooms R3 = new Rooms(DB.OP.CREATE, -1, "GA L03");
                Rooms R4 = new Rooms(DB.OP.CREATE, -1, "GB L01");
                Rooms R5 = new Rooms(DB.OP.CREATE, -1, "GB L02");
                Rooms R6 = new Rooms(DB.OP.CREATE, -1, "GB L03");
                Rooms R7 = new Rooms(DB.OP.CREATE, -1, "UA L01");
                Rooms R8 = new Rooms(DB.OP.CREATE, -1, "UA L02");
                Rooms R9 = new Rooms(DB.OP.CREATE, -1, "UA L03");
                Rooms R10 = new Rooms(DB.OP.CREATE, -1, "UA L04");
                Rooms R11 = new Rooms(DB.OP.CREATE, -1, "LAB");
                newDB.runRoom(R1);
                newDB.runRoom(R2);
                newDB.runRoom(R3);
                newDB.runRoom(R4);
                newDB.runRoom(R5);
                newDB.runRoom(R6);
                newDB.runRoom(R7);
                newDB.runRoom(R8);
                newDB.runRoom(R9);
                newDB.runRoom(R10);
                newDB.runRoom(R11);


                Teachers T1 = new Teachers(DB.OP.CREATE, -1, "Prof. Ziya Uddin");
                Teachers T2 = new Teachers(DB.OP.CREATE, -1, "Mrs. Sunayana Baruah");
                Teachers T3 = new Teachers(DB.OP.CREATE, -1, "Dr. Tani Agrawal");
                Teachers T4 = new Teachers(DB.OP.CREATE, -1, "Mrs. Menka Yadav");
                Teachers T5 = new Teachers(DB.OP.CREATE, -1, "Prof. Sateyndr Singh");
                Teachers T6 = new Teachers(DB.OP.CREATE, -1, "Mrs. Goldie Gabrani");
                Teachers T7 = new Teachers(DB.OP.CREATE, -1, "Prof. Maheshwar Dwivedy");
                newDB.runTeachers(T1);
                newDB.runTeachers(T2);
                newDB.runTeachers(T3);
                newDB.runTeachers(T4);
                newDB.runTeachers(T5);
                newDB.runTeachers(T6);
                newDB.runTeachers(T7);


                Periods P1 = new Periods(DB.OP.CREATE, 1, "09:30", "10:20");
                Periods P2 = new Periods(DB.OP.CREATE, 2, "10:30", "11:20");
                Periods P3 = new Periods(DB.OP.CREATE, 3, "11:30", "12:20");
                Periods P4 = new Periods(DB.OP.CREATE, 4, "12:30", "13:20");
                Periods P5 = new Periods(DB.OP.CREATE, 5, "13:30", "14:20");
                Periods P6 = new Periods(DB.OP.CREATE, 6, "14:30", "15:20");
                Periods P7 = new Periods(DB.OP.CREATE, 7, "15:30", "16:20");
                Periods P8 = new Periods(DB.OP.CREATE, 8, "16:30", "17:20");
                Periods P9 = new Periods(DB.OP.CREATE, 9, "09:30", "11:20");
                Periods P10 = new Periods(DB.OP.CREATE, 10, "11:30", "13:20");
                Periods P11 = new Periods(DB.OP.CREATE, 11, "15:30", "17:20");
                Periods P12 = new Periods(DB.OP.CREATE, 12, "14:30", "16:20");
                Periods P13 = new Periods(DB.OP.CREATE, 12, "13:30", "17:20");

                newDB.runPeriod(P1);
                newDB.runPeriod(P2);
                newDB.runPeriod(P3);
                newDB.runPeriod(P4);
                newDB.runPeriod(P5);
                newDB.runPeriod(P6);
                newDB.runPeriod(P7);
                newDB.runPeriod(P8);
                newDB.runPeriod(P9);
                newDB.runPeriod(P10);
                newDB.runPeriod(P11);
                newDB.runPeriod(P12);
                newDB.runPeriod(P13);


                Subjects S1 = new Subjects(getApplicationContext(), DB.OP.CREATE, -1, "Engineering Graphics & Drawing", "AutoCad", 0);
                Subjects S2 = new Subjects(getApplicationContext(), DB.OP.CREATE, -1, "Probability and Statistics", "P&S", 8);
                Subjects S3 = new Subjects(getApplicationContext(), DB.OP.CREATE, -1, "Etiquette and Communication Skills", "ECS", 10);
                Subjects S4 = new Subjects(getApplicationContext(), DB.OP.CREATE, -1, "Mobile Application Development", "MAD", 6);
                Subjects S5 = new Subjects(getApplicationContext(), DB.OP.CREATE, -1, "Emerging Life Sciences", "ELS", 5);
                Subjects S6 = new Subjects(getApplicationContext(), DB.OP.CREATE, -1, "Fundamentals of Digital Design", "FDLD", 2);
                Subjects S7 = new Subjects(getApplicationContext(), DB.OP.CREATE, -1, "Data Structures & Algorithms", "DSA", 3);
                newDB.runSubjects(S1);
                newDB.runSubjects(S2);
                newDB.runSubjects(S3);
                newDB.runSubjects(S4);
                newDB.runSubjects(S5);
                newDB.runSubjects(S6);
                newDB.runSubjects(S7);


                Timetables tT1 = new Timetables(DB.OP.CREATE, 1, "Monday", 9, 3, 1, 2, 1);
                Timetables tT2 = new Timetables(DB.OP.CREATE, 1, "Monday", 10, 1, 3, 7, 1);
                Timetables tT3 = new Timetables(DB.OP.CREATE, 1, "Monday", 11, 1, 11, 7, 1);

                Timetables tT4 = new Timetables(DB.OP.CREATE, 1, "Tuesday", 1, 7, 3, 5, 1);
                Timetables tT5 = new Timetables(DB.OP.CREATE, 1, "Tuesday", 2, 2, 3, 1, 1);
                Timetables tT6 = new Timetables(DB.OP.CREATE, 1, "Tuesday", 10, 5, 3, 3, 1);
                Timetables tT7 = new Timetables(DB.OP.CREATE, 1, "Tuesday", 6, 7, 3, 5, 1);
                Timetables tT8 = new Timetables(DB.OP.CREATE, 1, "Tuesday", 13, 1, 11, 7, 1);

                Timetables tT9 = new Timetables(DB.OP.CREATE, 1, "Wednesday", 2, 6, 3, 4, 1);
                Timetables tT10 = new Timetables(DB.OP.CREATE, 1, "Wednesday", 3, 2, 3, 1, 1);
                Timetables tT11 = new Timetables(DB.OP.CREATE, 1, "Wednesday", 4, 2, 3, 1, 1);
                Timetables tT12 = new Timetables(DB.OP.CREATE, 1, "Wednesday", 11, 7, 11, 5, 1);


                Timetables tT13 = new Timetables(DB.OP.CREATE, 1, "Thursday", 1, 6, 3, 4, 1);
                Timetables tT14 = new Timetables(DB.OP.CREATE, 1, "Thursday", 2, 2, 3, 1, 1);
                Timetables tT15 = new Timetables(DB.OP.CREATE, 1, "Thursday", 12, 6, 11, 4, 1);
                Timetables tT16 = new Timetables(DB.OP.CREATE, 1, "Thursday", 8, 3, 3, 2, 1);

                Timetables tT17 = new Timetables(DB.OP.CREATE, 1, "Friday", 1, 6, 3, 4, 1);
                Timetables tT18 = new Timetables(DB.OP.CREATE, 1, "Friday", 2, 3, 3, 2, 1);
                Timetables tT19 = new Timetables(DB.OP.CREATE, 1, "Friday", 3, 7, 3, 5, 1);
                Timetables tT20 = new Timetables(DB.OP.CREATE, 1, "Friday", 13, 4, 1, 6, 1);


                newDB.runTimetable(tT1);
                newDB.runTimetable(tT2);
                newDB.runTimetable(tT3);
                newDB.runTimetable(tT4);
                newDB.runTimetable(tT5);
                newDB.runTimetable(tT6);
                newDB.runTimetable(tT7);
                newDB.runTimetable(tT8);
                newDB.runTimetable(tT9);
                newDB.runTimetable(tT10);
                newDB.runTimetable(tT11);
                newDB.runTimetable(tT12);
                newDB.runTimetable(tT13);
                newDB.runTimetable(tT14);
                newDB.runTimetable(tT15);
                newDB.runTimetable(tT16);
                newDB.runTimetable(tT17);
                newDB.runTimetable(tT18);
                newDB.runTimetable(tT19);
                newDB.runTimetable(tT20);



                Rotations rR1 = new Rotations(DB.OP.CREATE, -1, "2015/10/05", 1);
                Rotations rR2 = new Rotations(DB.OP.CREATE, -1, "2015/10/12", 1);
                Rotations rR3 = new Rotations(DB.OP.CREATE, -1, "2015/10/19", 1);
                Rotations rR4 = new Rotations(DB.OP.CREATE, -1, "2015/10/26", 1);
                Rotations rR5 = new Rotations(DB.OP.CREATE, -1, "2015/11/02", 1);
                Rotations rR6 = new Rotations(DB.OP.CREATE, -1, "2015/11/09", 1);
                Rotations rR7 = new Rotations(DB.OP.CREATE, -1, "2015/11/16", 1);
                newDB.runRotation(rR1);
                newDB.runRotation(rR2);
                newDB.runRotation(rR3);
                newDB.runRotation(rR4);
                newDB.runRotation(rR5);
                newDB.runRotation(rR6);
                newDB.runRotation(rR7);


                newDB.close();
            }
        });

    }
}
