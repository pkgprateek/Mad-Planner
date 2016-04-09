package com.pkgprateek.madplanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pkgprateek.madplanner.ex.Methods;

/**
 * Created by pkgprateek on 03/11/15.
 */
public class DB extends SQLiteOpenHelper {

    /**
     * com.pkgprateek.madplanner.db contains many classes which are each the
     * entities defined by the tables below.
     * Used for exporting and importing of data to the database
     *
     */

    private Context context;

    private SQLiteDatabase sqlLiteDatabase;
    private static final String DATABASE_NAME = "Schedule";
    private static final int DATABASE_VERSION = 1;


    public static enum OP {CREATE,UPDATE,DESTROY,RETREIVE,NOTEXIST};
    private final String[] daysOfWeek = new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    /**
     * Declaring the fields in all the tables
     * > array[0] is the table name
     * > array[1] ... array[n] are the fields
     */
    private final String[] rooms = new String[] {"Rooms", "ID", "Name"};
    private final String[] subjects = new String[] {"Subjects", "ID", "Name", "Shortname", "Colour"};
    private final String[] teachers = new String[] {"Teachers", "ID", "Name"};
    private final String[] periods = new String[] {"Periods", "ID", "StartTime", "EndTime"};
    private final String[] timetable = new String[] {"Timetables", "Week", "Day", "Period", "Subject", "Room", "Teacher", "Length"};
    private final String[] rotations = new String[] {"Rotations", "ID", "WeekBegin", "Week"};
    private final String[] helper = new String[] {"Helper", "ID", "LinkType", "Link"};
    private final String[] homeworks = new String[] {"Homeworks", "ID", "Period", "Subject", "DueDate", "Title", "Contents", "Done", "Time"};
    private final String[] exams = new String[] {"Exam", "ID", "Subject", "Title", "Contents", "SeatDate", "SeatTime", "Period", "Duration", "RoomID", "SeatNumber"};
    private final String[] links = new String[] {"Links", "ID", "Title", "Link"};
    private final String[] reminders = new String[] {"Reminder", "ID", "Title", "Contents", "DueDate", "Time"};

    private final String DATABASE_CREATE_ROOMS = "CREATE TABLE "
            + rooms[0] + "("
            + rooms[1] + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + rooms[2] + " VARCHAR(40) NOT NULL);";
    private final String DATABASE_CREATE_TEACHERS = "CREATE TABLE "
            + teachers[0] + "("
            + teachers[1] + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + teachers[2] + " VARCHAR(40) NOT NULL);";
    private final String DATABASE_CREATE_PERIODS = "CREATE TABLE "
            + periods[0] + "("
            + periods[1] + " INTEGER PRIMARY KEY,"
            + periods[2] + " CHAR(5) NOT NULL,"
            + periods[3] + " CHAR(5) NOT NULL);";
    private final String DATABASE_CREATE_SUBJECTS = "CREATE TABLE "
            + subjects[0] + "("
            + subjects[1] + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + subjects[2] + " VARCHAR(50) NOT NULL,"
            + subjects[3] + " VARCHAR(6) NOT NULL,"
            + subjects[4] + " INTEGER NOT NULL);";
    private final String DATABASE_CREATE_TIMETABLE = "CREATE TABLE "
            + timetable[0] + "("
            + timetable[1] + " INTEGER NOT NULL,"
            + timetable[2] + " VARCHAR(9) NOT NULL,"
            + timetable[3] + " INTEGER NOT NULL,"
            + timetable[4] + " INTEGER NOT NULL,"
            + timetable[5] + " INTEGER NOT NULL,"
            + timetable[6] + " INTEGER NOT NULL,"
            + timetable[7] + " INTEGER NOT NULL,"
            + "PRIMARY KEY (" + timetable[1] + "," + timetable[2] + "," + timetable[3] + "),"
            + "FOREIGN KEY (" + timetable[4] + ") REFERENCES " + subjects[0] + "(" + subjects[1] + "),"
            + "FOREIGN KEY (" + timetable[5] + ") REFERENCES " + rooms[0] + "(" + rooms[1] + "),"
            + "FOREIGN KEY (" + timetable[6] + ") REFERENCES " + teachers[0] + "(" + teachers[1] + "));";

    private final String DATABASE_CREATE_ROTATIONS = "CREATE TABLE "
            + rotations[0] + "("
            + rotations[1] + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + rotations[2] + " CHAR(10) NOT NULL,"
            + rotations[3] + " INTEGER NOT NULL,"
            + "FOREIGN KEY (" + rotations[3] + ") REFERENCES " + timetable[0] + "(" + timetable[1] + "));";

    private final String DATABASE_CREATE_HOMEWORKS = "CREATE TABLE "
            + homeworks[0] + "("
            + homeworks[1] + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + homeworks[2] + " INTEGER,"
            + homeworks[3] + " INTEGER NOT NULL,"
            + homeworks[4] + " CHAR(10) NOT NULL,"
            + homeworks[5] + " VARCHAR(100) NOT NULL,"
            + homeworks[6] + " VARCHAR(1000) NOT NULL,"
            + homeworks[7] + " INTEGER NOT NULL,"
            + homeworks[8] + " CHAR(5),"
            + "FOREIGN KEY (" + homeworks[2] + ") REFERENCES " + periods[0] + "(" + periods[1] + "),"
            + "FOREIGN KEY (" + homeworks[3] + ") REFERENCES " + subjects[0] + "(" + subjects[1] + "));";

    private final String DATABASE_CREATE_EXAMS = "CREATE TABLE "
            + exams[0] + "("
            + exams[1] + " INTEGER PRIMARY KEY,"
            + exams[2] + " INTEGER NOT NULL,"
            + exams[3] + " VARCHAR(100) NOT NULL,"
            + exams[4] + " VARCHAR(1000),"
            + exams[5] + " CHAR(10) NOT NULL,"
            + exams[6] + " CHAR(5) NOT NULL,"
            + exams[7] + " INTEGER,"
            + exams[8] + " INTEGER,"
            + exams[9] + " INTEGER,"
            + exams[10] + " VARCHAR(20),"
            + "FOREIGN KEY (" + exams[2] + ") REFERENCES " + subjects[0] + "(" + subjects[1] + "));";

    private final String DATABASE_CREATE_LINKS = "CREATE TABLE "
            + links[0] + "("
            + links[1] + " INTEGER PRIMARY KEY,"
            + links[2] + " VARCHAR(100) NOT NULL,"
            + links[3] + " VARCHAR(250) NOT NULL);";

    private final String DATABASE_CREATE_HELPER = "CREATE TABLE "
            + helper[0] + "("
            + helper[1] + " INTEGER PRIMARY KEY,"
            + helper[2] + " CHAR(1) NOT NULL,"
            + helper[3] + " INTEGER NOT NULL);";

    private final String DATABASE_CREATE_REMINDERS = "CREATE TABLE "
            + reminders[0] + "("
            + reminders[1] + " INTEGER PRIMARY KEY,"
            + reminders[2] + " VARCHAR(100) NOT NULL,"
            + reminders[3] + " VARCHAR(1000),"
            + reminders[4] + " CHAR(10),"
            + reminders[5] + " CHAR(5));";

    // Constructor
    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * The OnCreate method for the database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_ROOMS);
        db.execSQL(DATABASE_CREATE_TEACHERS);
        db.execSQL(DATABASE_CREATE_PERIODS);
        db.execSQL(DATABASE_CREATE_SUBJECTS);
        db.execSQL(DATABASE_CREATE_TIMETABLE);
        db.execSQL(DATABASE_CREATE_ROTATIONS);
        db.execSQL(DATABASE_CREATE_HOMEWORKS);
        db.execSQL(DATABASE_CREATE_EXAMS);
        db.execSQL(DATABASE_CREATE_LINKS);
        db.execSQL(DATABASE_CREATE_HELPER);
        db.execSQL(DATABASE_CREATE_REMINDERS);
    }

    /**
     * OnUpgrade method for the database - Drop the tables and re-initialise the database
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS " + rooms[0]);
        db.execSQL("DROP TABLE IF EXISTS " + teachers[0]);
        db.execSQL("DROP TABLE IF EXISTS " + periods[0]);
        db.execSQL("DROP TABLE IF EXISTS " + subjects[0]);
        db.execSQL("DROP TABLE IF EXISTS " + timetable[0]);
        db.execSQL("DROP TABLE IF EXISTS " + rotations[0]);
        db.execSQL("DROP TABLE IF EXISTS " + homeworks[0]);
        db.execSQL("DROP TABLE IF EXISTS " + exams[0]);
        db.execSQL("DROP TABLE IF EXISTS " + links[0]);
        db.execSQL("DROP TABLE IF EXISTS " + helper[0]);
        db.execSQL("DROP TABLE IF EXISTS " + reminders[0]);

        // create fresh books table
        this.onCreate(db);
    }


    /**
     * Open and close methods. To be called before using and after using the database.
     */

    // Open
    public void open() {
        sqlLiteDatabase = getWritableDatabase();
    }

    // Close
    public void close() {
        sqlLiteDatabase.close();
    }


    /**
     * DMS methods for the SQL Database for each table
     */





    /**
     *                      EXAMS
     *      ======================================
     */
    public List<Exams> getAllExams() {
        List<Exams> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + exams[0] + " ORDER BY " + exams[5] + "," + exams[6] + ";";

        Cursor c = sqlLiteDatabase.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                list.add(new Exams(OP.RETREIVE, c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6), c.getInt(7), c.getInt(8), c.getString(9)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
    public void runExam(Exams exam) {
        if (exam.getOp() == OP.CREATE) {
            ContentValues cv = new ContentValues();
            cv.put(exams[2], exam.getSubject());
            cv.put(exams[3], exam.getTitle());
            cv.put(exams[4], exam.getContents());
            cv.put(exams[5], exam.getDate());
            cv.put(exams[6], exam.getTime());
            cv.put(exams[7], exam.getPeriod());
            cv.put(exams[8], exam.getDuration());
            cv.put(exams[9], exam.getRoom());
            cv.put(exams[10], exam.getSeatnumber());
            sqlLiteDatabase.insert(exams[0],null,cv);
        }
        if (exam.getOp() == OP.UPDATE) {
            ContentValues cv = new ContentValues();
            cv.put(exams[2], exam.getSubject());
            cv.put(exams[3], exam.getTitle());
            cv.put(exams[4], exam.getContents());
            cv.put(exams[5], exam.getDate());
            cv.put(exams[6], exam.getTime());
            cv.put(exams[7], exam.getPeriod());
            cv.put(exams[8], exam.getDuration());
            cv.put(exams[9], exam.getRoom());
            cv.put(exams[10], exam.getSeatnumber());
            sqlLiteDatabase.update(exams[0], cv, exams[1] + "=?;", new String[] {exam.getId() + ""});
        }
        if (exam.getOp() == OP.DESTROY) {
            sqlLiteDatabase.delete(exams[0], exams[1] + "=" + exam.getId(), null);
        }
    }
    public Exams getExam(int id) {

        String rawQuery = "SELECT * FROM " + exams[0] + " WHERE " + exams[1] + "=?;";
        String[] selectionArgs = new String[] {id + ""};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        if (c.moveToFirst()) {
            Exams e = new Exams(OP.RETREIVE, c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6), c.getInt(7), c.getInt(8), c.getString(9));
            c.close();
            return e;
        } else {
            c.close();
            return null;
        }
    }

    public List<Exams> getExams(String date, int subject, int period) {
        List<Exams> list = new ArrayList<>();
        String rawQuery = "SELECT * FROM " + exams[0] + " WHERE " + exams[5] + "=? AND " + exams[2] + "=? AND " + exams[7] + "=?;";
        String[] selectionArgs = new String[] {date, Integer.toString(subject), Integer.toString(period)};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        if (c.moveToFirst()) {
            do {
                list.add(new Exams(OP.RETREIVE, c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6), c.getInt(7), c.getInt(8), c.getString(9)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<Exams> getExams(String date, boolean assigned) {
        List<Exams> list = new ArrayList<>();
        String rawQuery = "SELECT * FROM " + exams[0] + " WHERE " + exams[5] + "=? AND " + exams[7] + "=?;";
        String[] selectionArgs = new String[] {date, "-" + 1};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        if (c.moveToFirst()) {
            do {
                list.add(new Exams(OP.RETREIVE, c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6), c.getInt(7), c.getInt(8), c.getString(9)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public int getNumberOfExams() {
        String rawQuery = "SELECT * FROM " + exams[0] + ";";
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, null);
        int count = c.getCount();
        c.close();
        return count;
    }
    public int getNumberOfExams(Date d) {
        String rawQuery = "SELECT * FROM " + exams[0] + " WHERE " + exams[5] + "=?;";
        String[] selectionArgs = new String[] {Methods.getDate(d)};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery,selectionArgs);
        int count = c.getCount();
        c.close();
        return count;
    }
    public void clearAllExams() {
        String rawQuery = "DELETE FROM " + DATABASE_NAME + " WHERE NAME=" + exams[0] + ";";
    }






    /**
     *                      ROOMS
     *      ======================================
     */
    public List<Rooms> getAllRooms() {
        List<Rooms> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + rooms[0] + " ORDER BY " + rooms[2] + ";";

        Cursor c = sqlLiteDatabase.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                list.add(new Rooms(OP.RETREIVE, c.getInt(0), c.getString(1)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
    public void runRoom(Rooms room) {
        if (room.getOp() == OP.CREATE) {
            ContentValues cv = new ContentValues();
            cv.put(rooms[2], room.getName());
            sqlLiteDatabase.insert(rooms[0], null, cv);
        }
    }
    public String getRoom(int id) {
        String rawQuery = "SELECT * FROM " + rooms[0] + " WHERE " + rooms[1] + "=?;";
        String[] selectionArgs = new String[] {id + ""};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        if (c.moveToFirst()) {
            String room = c.getString(1);
            c.close();
            return room;
        } else {
            c.close();
            return "N/A";
        }
    }
    public int getRoom(String name) {

        String rawQuery = "SELECT * FROM " + rooms[0] + " WHERE LOWER(" + rooms[2] + ")=LOWER(?);";
        String[] selectionArgs = new String[] {name};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        if (c.moveToFirst()) {
            int period = c.getInt(0);
            c.close();
            return period;
        } else {
            return -1;
        }
    }
    public void clearAllRooms() {
        String rawQuery = "DELETE FROM " + DATABASE_NAME + " WHERE NAME=" + rooms[0] + ";";
    }









    /**
     *                    TEACHERS
     *      ======================================
     */
    public List<Teachers> getAllTeachers() {
        List<Teachers> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + teachers[0] + ";";

        Cursor c = sqlLiteDatabase.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                list.add(new Teachers(OP.RETREIVE, c.getInt(0), c.getString(1)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
    public void runTeachers(Teachers teacher) {
        if (teacher.getOp() == OP.CREATE) {
            ContentValues cv = new ContentValues();
            cv.put(teachers[2], teacher.getName());
            sqlLiteDatabase.insert(teachers[0], null, cv);
        }
    }
    public String getTeacher(int id) {
        String rawQuery = "SELECT * FROM " + teachers[0] + " WHERE " + teachers[1] + "=?;";
        String[] selectionArgs = new String[] {id + ""};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        if (c.moveToFirst()) {
            String room = c.getString(1);
            c.close();
            return room;
        } else {
            c.close();
            return "N/A";
        }
    }
    public void clearAllTeachers() {
        String rawQuery = "DELETE FROM " + DATABASE_NAME + " WHERE NAME=" + teachers[0] + ";";
    }








    /**
     *                      SUBJECTS
     *      ======================================
     */
    // Get all the subjects
    public List<Subjects> getAllSubjects() {
        List<Subjects> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + subjects[0] + " ORDER BY " + subjects[2] + ";";

        Cursor c = sqlLiteDatabase.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                list.add(new Subjects(context, OP.RETREIVE, c.getInt(0), c.getString(1), c.getString(2), c.getInt(3)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
    // Modify an individual record
    public void runSubjects(Subjects subject) {
        if (subject.getOp() == OP.CREATE) {
            ContentValues cv = new ContentValues();
            cv.put(subjects[2], subject.getName());
            cv.put(subjects[3], subject.getShortName());
            cv.put(subjects[4], subject.getColourValue());
            sqlLiteDatabase.insert(subjects[0], null, cv);
        }
    }
    // Get the number of subjects stored in the database
    public int getNumberOfSubjects() {
        String selectQuery = "SELECT * FROM " + subjects[0] + ";";
        Cursor c = sqlLiteDatabase.rawQuery(selectQuery, null);
        int count = c.getCount();
        c.close();
        return count;
    }
    // Get a specific subject
    public Subjects getSubject(int id) {
        Subjects s;
        String rawQuery = "SELECT * FROM " + subjects[0] + " WHERE " + subjects[1] + "=?;";
        String[] selectionArgs = new String[] {id + ""};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        if (c.moveToFirst()) {
            s = new Subjects(context, OP.RETREIVE, c.getInt(0), c.getString(1), c.getString(2), c.getInt(3));
        } else {
            s = new Subjects(OP.NOTEXIST);
        }
        c.close();
        return s;
    }
    public void clearAllSubjects() {
        String rawQuery = "DELETE FROM " + DATABASE_NAME + " WHERE NAME=" + subjects[0] + ";";
    }
    public List<TimetableSlot> getSlotsForSubject(int id, int week) {
        List<TimetableSlot> list = new ArrayList<TimetableSlot>();
        String rawQuery = "SELECT "
                + timetable[0] + "." + timetable[3] + ", "
                + periods[0] + "." + periods[2] + ", "
                + periods[0] + "." + periods[3] + ", "
                + subjects[0] + "." + subjects[1] + ", "
                + subjects[0] + "." + subjects[2] + ", "
                + subjects[0] + "." + subjects[3] + ", "
                + subjects[0] + "." + subjects[4] + ", "
                + teachers[0] + "." + teachers[2] + ", "
                + rooms[0] + "." + rooms[2] + ","
                + timetable[0] + "." + timetable[7]
                + " FROM "
                + timetable[0] + ","
                + periods[0] + ","
                + subjects[0] + ","
                + teachers[0] + ","
                + rooms[0]
                + " WHERE "
                + timetable[0] + "." + timetable[4] + "=? AND "
                + timetable[0] + "." + timetable[1] + "=? AND "
                + timetable[0] + "." + timetable[3] + "=" + periods[0] + "." + periods[1] + " AND "
                + timetable[0] + "." + timetable[4] + "=" + subjects[0] + "." + subjects[1] + " AND "
                + timetable[0] + "." + timetable[5] + "=" + rooms[0] + "." + rooms[1] + " AND "
                + timetable[0] + "." + timetable[6] + "=" + teachers[0] + "." + teachers[1]
                + " ORDER BY " + timetable[0] + "." + timetable[3] + ";";
        String[] selectionArgs = new String[] {Integer.toString(id), Integer.toString(week)};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        if (c.moveToFirst()) {
            do {
                list.add(new TimetableSlot(context, c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4), c.getString(5), c.getInt(6), c.getString(7), c.getString(8), c.getInt(9)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }






    /**
     *                      PERIODS
     *      ======================================
     */
    public List<Periods> getAllPeriods() {
        List<Periods> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + periods[0] + ";";

        Cursor c = sqlLiteDatabase.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                list.add(new Periods(OP.RETREIVE, c.getInt(0), c.getString(1), c.getString(2)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public String getEndTime(int period) {
        String rawQuery = "SELECT " + periods[3]
                + " FROM " + periods[0] + " WHERE " + periods[1] + " = ?;";
        String[] selectionArgs = new String[] {"" + period};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);

        if (c.moveToFirst()) {
            return c.getString(0);
        }
        c.close();
        return "N/A";
    }
    public String getStartTime(int period) {
        String rawQuery = "SELECT " + periods[2]
                + " FROM " + periods[0] + " WHERE " + periods[1] + " = ?;";
        String[] selectionArgs = new String[] {"" + period};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);

        if (c.moveToFirst()) {
            return c.getString(0);
        }
        c.close();
        return "N/A";
    }

    public void runPeriod(Periods period) {
        if (period.getOp() == OP.CREATE) {
            ContentValues cv = new ContentValues();
            cv.put(periods[2], period.getStartTime());
            cv.put(periods[3], period.getEndTime());
            sqlLiteDatabase.insert(periods[0], null, cv);
        }
    }
    public void clearAllPeriods() {
        String rawQuery = "DELETE FROM " + DATABASE_NAME + " WHERE NAME=" + periods[0] + ";";
    }






    /**
     *                    TIMETABLE
     *      ======================================
     */
    public List<Timetables> getAllTimetables() {
        List<Timetables> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + timetable[0] + ";";

        Cursor c = sqlLiteDatabase.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                list.add(new Timetables(OP.RETREIVE, c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
    public void runTimetable(Timetables tt) {
        if (tt.getOp() == OP.CREATE) {
            ContentValues cv = new ContentValues();
            cv.put(timetable[1], tt.getWeek());
            cv.put(timetable[2], tt.getDay());
            cv.put(timetable[3], tt.getPeriod());
            cv.put(timetable[4], tt.getSubject());
            cv.put(timetable[5], tt.getRoom());
            cv.put(timetable[6], tt.getTeacher());
            cv.put(timetable[7], tt.getLength());
            sqlLiteDatabase.insert(timetable[0], null, cv);
        }
    }

    public List<String> getDays(int week) {
        List<String> list = new ArrayList<String>();
        String query = "SELECT DISTINCT " + timetable[2] + " FROM " + timetable[0] + " WHERE " + timetable[1] + "=?;";
        String[] selectionArgs = new String[] {Integer.toString(week)};
        Cursor c = sqlLiteDatabase.rawQuery(query, selectionArgs);
        if (c.moveToFirst()) {
            do {
                list.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        // Put the days in order!
        if (list.size() != 0) {
            List<String> orderedList = new ArrayList<>();
            for (int i = 0; i < daysOfWeek.length; i++) {
                if (list.contains(daysOfWeek[i])) {
                    orderedList.add(daysOfWeek[i]);
                }
            }
            return orderedList;
        }
        return list;
    }
    public List<Timetables> getTimetables(int period) {
        List<Timetables> list = new ArrayList<>();
        String query = "SELECT * FROM " + timetable[0] + " WHERE " + timetable[3] + "=? ORDER BY CASE "
                + timetable[2] + " WHEN 'Monday' THEN 7"
                                + " WHEN 'Tuesday' THEN 6"
                                + " WHEN 'Wednesday' THEN 5"
                                + " WHEN 'Thursday' THEN 4"
                                + " WHEN 'Friday' THEN 3"
                                + " WHEN 'Saturday' THEN 2"
                                + " WHEN 'Sunday' THEN 1"
                                + " ELSE 0 END DESC;";
        String[] selectionArgs = new String[] {Integer.toString(period)};
        Cursor c = sqlLiteDatabase.rawQuery(query, selectionArgs);
        if (c.moveToFirst()) {
            do {
                list.add(new Timetables(OP.RETREIVE, c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6)));
            } while (c.moveToLast());
        }
        c.close();
        return list;
    }
    public List<Timetables> getTimetables(int week,String day) {
        List<Timetables> list = new ArrayList<>();
        String query = "SELECT * FROM " + timetable[0] + " WHERE " + timetable[1] + "=? AND " + timetable[2] + "=? ORDER BY " + timetable[3] + " ASC;";
        String[] selectionArgs = new String[] {Integer.toString(week), day};
        Cursor c = sqlLiteDatabase.rawQuery(query, selectionArgs);
        if (c.moveToFirst()) {
            do {
                list.add(new Timetables(OP.RETREIVE, c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6)));
            } while (c.moveToNext());
        }
        c.close();
        return list;

    }
    public void clearAllTimetables() {
        String rawQuery = "DELETE FROM " + DATABASE_NAME + " WHERE NAME=" + timetable[0] + ";";
    }







    /**
     *                    Homeworks
     *      ======================================
     */
    public List<Homeworks> getAllHomeworks() {
        List<Homeworks> list = new ArrayList<>();
        String rawQuery = "SELECT * FROM " + homeworks[0] + " ORDER BY " + homeworks[7] + "," + homeworks[4] + "," + homeworks[2] + "," + homeworks[8];
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, null);
        if (c.moveToFirst()) {
            do {
                list.add(new Homeworks(OP.RETREIVE, c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6), c.getString(7)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
    public List<Homeworks> getHomeworks(String date, boolean assigned) {
        List<Homeworks> list = new ArrayList<>();
        // Date must be in DD/MM/YYYY
        String rawQuery = "SELECT * FROM " + homeworks[0] + " WHERE " + homeworks[4] + "=? AND " + homeworks[2] + "=?;";
        String[] selectionArgs = new String[] {date, "-" + 1};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        if (c.moveToFirst()) {
            do {
                list.add(new Homeworks(OP.RETREIVE, c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6), c.getString(7)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
    public Homeworks getHomework(int id) {
        Homeworks h;
        String rawQuery = "SELECT * FROM " + homeworks[0] + " WHERE " + homeworks[1] + "=?;";
        String[] selectionArgs = new String[] {id + ""};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        if (c.moveToFirst()) {
            h = new Homeworks(OP.RETREIVE, c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6), c.getString(7));
        } else {
            h = new Homeworks(OP.NOTEXIST);
        }
        c.close();
        return h;
    }
    public List<Homeworks> getHomeworks(String date, int subject, int period) {
        List<Homeworks> list = new ArrayList<>();
        // Date must be in DD/MM/YYYY
        String rawQuery = "SELECT * FROM " + homeworks[0] + " WHERE " + homeworks[4] + "=? AND " + homeworks[3] + "=? AND " + homeworks[2] + "=?;";
        String[] selectionArgs = new String[] {date, Integer.toString(subject), Integer.toString(period)};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        if (c.moveToFirst()) {
            do {
                list.add(new Homeworks(OP.RETREIVE, c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6), c.getString(7)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public void runHomework(Homeworks h) {
        if (h.getOp() == OP.CREATE) {
            ContentValues cv = new ContentValues();
            cv.put(homeworks[2], h.getPeriod());
            cv.put(homeworks[3], h.getSubject());
            cv.put(homeworks[4], h.getDueDate());
            cv.put(homeworks[5], h.getTitle());
            cv.put(homeworks[6], h.getContents());
            cv.put(homeworks[7], h.getDone());
            cv.put(homeworks[8], h.getTime());
            sqlLiteDatabase.insert(homeworks[0], null, cv);
        }
        if (h.getOp() == OP.UPDATE) {
            ContentValues cv = new ContentValues();
            cv.put(homeworks[2], h.getPeriod());
            cv.put(homeworks[3], h.getSubject());
            cv.put(homeworks[4], h.getDueDate());
            cv.put(homeworks[5], h.getTitle());
            cv.put(homeworks[6], h.getContents());
            cv.put(homeworks[7], h.getDone());
            cv.put(homeworks[8], h.getTime());
            sqlLiteDatabase.update(homeworks[0], cv, homeworks[1] + "=?;", new String[]{h.getId() + ""});
        }
        if (h.getOp() == OP.DESTROY) {
            sqlLiteDatabase.delete(homeworks[0], homeworks[1] + "=" + h.getId(), null);
        }
    }
    public int getNumberOfHomeworks() {
        String rawQuery = "SELECT * FROM " + homeworks[0] + ";";
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, null);
        int count = c.getCount();
        c.close();
        return count;
    }
    public int getNumberOfHomeworks(Date d) {
        String rawQuery = "SELECT * FROM " + homeworks[0] + " WHERE " + homeworks[4] + "=?;";
        String[] selectionArgs = new String[] {Methods.getDate(d)};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery,selectionArgs);
        int count = c.getCount();
        c.close();
        return count;
    }
    public void clearAllHomeworks() {
        String rawQuery = "DELETE FROM " + DATABASE_NAME + " WHERE NAME=" + homeworks[0] + ";";
    }








    /**
     *                    ROTATIONS
     *      ======================================
     */
    public void runRotation(Rotations rotation) {
        if (rotation.getOp() == OP.CREATE) {
            ContentValues cv = new ContentValues();
            cv.put(rotations[2], rotation.getWeekBegin());
            cv.put(rotations[3], rotation.getWeek());
            sqlLiteDatabase.insert(rotations[0], null, cv);
        }
    }

    public Rotations getRotation(String weekBegin) {
        Rotations r;
        String rawQuery = "SELECT * FROM " + rotations[0] + " WHERE " + rotations[2] + "=?;";
        String[]  selectionArgs = new String[] {weekBegin};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        if (c.moveToFirst()) {
            r = new Rotations(OP.RETREIVE, c.getInt(0), c.getString(1), c.getInt(2));
        } else {
            r = new Rotations(OP.NOTEXIST);
        }
        c.close();
        return r;
    }
    public int getWeek(String anyWeek) {
        // Convert the anyWeek into weekBegin
        String weekBegin = Methods.getFirstDayOfWeek(anyWeek);
        // Find the week
        int week;
        String rawQuery = "SELECT " + rotations[3] + " FROM " + rotations[0] + " WHERE " + rotations[2] + "=?;";
        String[]  selectionArgs = new String[] {weekBegin};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        if (c.moveToFirst()) {
            week = c.getInt(0);
        } else {
            week = -1;
        }
        c.close();
        return week;
    }
    public int getWeek(Date d) {
        return getWeek(Methods.getDate(d));
    }

    public void clearAllRotations() {
        String rawQuery = "DELETE FROM " + DATABASE_NAME + " WHERE NAME=" + rotations[0] + ";";
    }






    /**
     *                    REMINDERS
     *      ======================================
     */
    public List<Reminders> getAllReminders() {
        List<Reminders> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + reminders[0] + " ORDER BY " + reminders[3] + "," + reminders[4] + ";";

        Cursor c = sqlLiteDatabase.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                list.add(new Reminders(OP.RETREIVE, c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public void runReminder(Reminders reminder) {
        if (reminder.getOp() == OP.CREATE) {
            ContentValues cv = new ContentValues();
            cv.put(reminders[2], reminder.getTitle());
            cv.put(reminders[3], reminder.getContents());
            cv.put(reminders[4], reminder.getDueDate());
            cv.put(reminders[5], reminder.getTime());
            sqlLiteDatabase.insert(reminders[0], null, cv);
        }
        if (reminder.getOp() == OP.UPDATE) {
            ContentValues cv = new ContentValues();
            cv.put(reminders[2], reminder.getTitle());
            cv.put(reminders[3], reminder.getContents());
            cv.put(reminders[4], reminder.getDueDate());
            cv.put(reminders[5], reminder.getTime());
            sqlLiteDatabase.update(reminders[0], cv, reminders[1] + "=?;", new String[] {reminder.getId() + ""});
        }
        if (reminder.getOp() == OP.DESTROY) {
            sqlLiteDatabase.delete(reminders[0], reminders[1] + "=" + reminder.getId(), null);
        }
    }
    public Reminders getReminder(int id) {
        Reminders r;
        String rawQuery = "SELECT * FROM " + reminders[0] + " WHERE " + reminders[1] + "=?;";
        String[] selectionArgs = new String[] {id + ""};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        if (c.moveToFirst()) {
            r = new Reminders(OP.RETREIVE, c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
        } else {
            r = new Reminders(OP.NOTEXIST);
        }
        c.close();
        return r;
    }

    public void clearAllReminders() {
        String rawQuery = "DELETE FROM " + DATABASE_NAME + " WHERE NAME=" + reminders[0] + ";";
    }
    public int getNumberOfReminders(Date d) {
        String rawQuery = "SELECT * FROM " + reminders[0] + " WHERE " + reminders[4] + "=?;";
        String[] selectionArgs = new String[] {Methods.getDate(d)};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, null);
        int count = c.getCount();
        c.close();
        return count;
    }
    public List<Reminders> getReminders(String date) {
        List<Reminders> list = new ArrayList<>();
        String rawQuery = "SELECT * FROM " + reminders[0] + " WHERE " + reminders[4] + "=?;";
        String[] selectionArgs = new String[] {date};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        if (c.moveToFirst()) {
            do {
                list.add(new Reminders(OP.RETREIVE, c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }







    /**
     * Method to get all the lessons of a given day of the week and week.
     */

    public List<TimetableSlot> getDaysLessons(int week, String weekday) {// Query the database for lessons on this week and day
        String rawQuery = "SELECT "
                + timetable[0] + "." + timetable[3] + ", "
                + periods[0] + "." + periods[2] + ", "
                + periods[0] + "." + periods[3] + ", "
                + subjects[0] + "." + subjects[1] + ", "
                + subjects[0] + "." + subjects[2] + ", "
                + subjects[0] + "." + subjects[3] + ", "
                + subjects[0] + "." + subjects[4] + ", "
                + teachers[0] + "." + teachers[2] + ", "
                + rooms[0] + "." + rooms[2] + ","
                + timetable[0] + "." + timetable[7]
                + " FROM "
                + timetable[0] + ","
                + periods[0] + ","
                + subjects[0] + ","
                + teachers[0] + ","
                + rooms[0]
                + " WHERE "
                + timetable[0] + "." + timetable[1] + "=? AND "
                + timetable[0] + "." + timetable[2] + "=? AND "
                + timetable[0] + "." + timetable[3] + "=" + periods[0] + "." + periods[1] + " AND "
                + timetable[0] + "." + timetable[4] + "=" + subjects[0] + "." + subjects[1] + " AND "
                + timetable[0] + "." + timetable[5] + "=" + rooms[0] + "." + rooms[1] + " AND "
                + periods[0] + "." + periods[3] + ">? AND "
                + timetable[0] + "." + timetable[6] + "=" + teachers[0] + "." + teachers[1] + ";";
        String[] selectionArgs = new String[] {Integer.toString(week),weekday, "00:00"};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery,selectionArgs);
        // Get the list, add each record as a TimetableSlot Class and return
        List<TimetableSlot> lessons = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                // Timetable element found!
                TimetableSlot t = new TimetableSlot(context, c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4), c.getString(5), c.getInt(6), c.getString(7), c.getString(8), c.getInt(9));
                // Add the completed item to the lesson list
                lessons.add(t);
            } while (c.moveToNext());
        }

        // Return the lesson list
        c.close();
        return lessons;
    }

    public List<TimetableSlot> getDaysLessons(String date) {
        return getDaysLessons(date, "00:00", true);
    }

    public List<TimetableSlot> getDaysLessons(Date da) {
        return getDaysLessons(Methods.getDate(da), "00:00", true);
    }

    public List<TimetableSlot> getDaysLessons(Date da, String time) {
        return getDaysLessons(Methods.getDate(da), time, true);
    }

    public List<TimetableSlot> getDaysLessons(String date, String time, boolean checkForRelatedFeeds) {
        // Get the current week it is, as well as what day it is
        int week = getWeek(date);
        String weekday = Methods.getDay(date);

        // Query the database for lessons on this week and day
        String rawQuery = "SELECT "
                + timetable[0] + "." + timetable[3] + ", "
                + periods[0] + "." + periods[2] + ", "
                + periods[0] + "." + periods[3] + ", "
                + subjects[0] + "." + subjects[1] + ", "
                + subjects[0] + "." + subjects[2] + ", "
                + subjects[0] + "." + subjects[3] + ", "
                + subjects[0] + "." + subjects[4] + ", "
                + teachers[0] + "." + teachers[2] + ", "
                + rooms[0] + "." + rooms[2] + ","
                + timetable[0] + "." + timetable[7]
                + " FROM "
                + timetable[0] + ","
                + periods[0] + ","
                + subjects[0] + ","
                + teachers[0] + ","
                + rooms[0]
                + " WHERE "
                + timetable[0] + "." + timetable[1] + "=? AND "
                + timetable[0] + "." + timetable[2] + "=? AND "
                + timetable[0] + "." + timetable[3] + "=" + periods[0] + "." + periods[1] + " AND "
                + timetable[0] + "." + timetable[4] + "=" + subjects[0] + "." + subjects[1] + " AND "
                + timetable[0] + "." + timetable[5] + "=" + rooms[0] + "." + rooms[1] + " AND "
                + periods[0] + "." + periods[3] + ">? AND "
                + timetable[0] + "." + timetable[6] + "=" + teachers[0] + "." + teachers[1] + ";";
        String[] selectionArgs = new String[] {Integer.toString(week),weekday, time};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery,selectionArgs);
        // Get the list, add each record as a TimetableSlot Class and return
        List<TimetableSlot> lessons = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                // Timetable element found!
                TimetableSlot t = new TimetableSlot(context, c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4), c.getString(5), c.getInt(6), c.getString(7), c.getString(8), c.getInt(9));

                if (checkForRelatedFeeds) {
                    // Homework query - query the database for if there is a homework due in for this lesso
                    t.setHomeworks(getHomeworks(date, t.getSubjectID(), t.getPeriod()));

                    // Exam query - query the database for if there is exam elements belonging to this lesson
                    t.setExams(getExams(date, t.getSubjectID(), t.getPeriod()));
                }
                // Add the completed item to the lesson list
                lessons.add(t);
            } while (c.moveToNext());
        }

        // Return the lesson list
        c.close();
        return lessons;
    }

    public List<Periods> getPeriodsAvailableToHomeworks(String day, String startOfWeek, int subject) {
        // Link StartOfWeek with Week in Rotation, then Week with day and subject to get PeriodIDs
        List<Periods> list = new ArrayList<>();

        String rawQuery = "SELECT " + periods[0] + ".* FROM "
                + periods[0] + "," + timetable[0] + "," + rotations[0]
                + " WHERE "
                + rotations[0] + "." + rotations[2] + "=? AND "
                + rotations[0] + "." + rotations[3] + "=" + timetable[0] + "." + timetable[1] + " AND "
                + timetable[0] + "." + timetable[2] + "=? AND "
                + timetable[0] + "." + timetable[3] + "=" + periods[0] + "." + periods[1] + " AND "
                + timetable[0] + "." + timetable[4] + "=?;";
        String[] selectionArgs = new String[] {startOfWeek,day,subject + ""};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        if (c.moveToFirst()) {
            do {
                list.add(new Periods(OP.RETREIVE, c.getInt(0), c.getString(1), c.getString(2)));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }


    // Returns the room being used from the given day of the week, when the start of the week is and what period
    public String getRoomFromTimetable(String day, String startOfWeek, int period) {
        String rawQuery = "SELECT " + rooms[0] + "." + rooms[2] + " FROM "
                + rooms[0] + "," + rotations[0] + "," + timetable[0] + " WHERE "
                + rotations[0] + "." + rotations[2] + "=? AND "
                + rotations[0] + "." + rotations[3] + "=" + timetable[0] + "." + timetable[1] + " AND "
                + timetable[0] + "." + timetable[2] + "=? AND "
                + timetable[0] + "." + timetable[3] + "=? AND "
                + timetable[0] + "." + timetable[5] + "=" + rooms[0] + "." + rooms[1] + ";";
        String[] selectionArgs = new String[]{startOfWeek, day, period + ""};
        Cursor c = sqlLiteDatabase.rawQuery(rawQuery, selectionArgs);
        String room = "";
        if (c.moveToFirst()) {
            room = c.getString(0);
        }
        c.close();
        return room;
    }




    // Method to clean up the database!
    // Need to be incredibly careful here - Not deleting the wrong records!
    public void cleanUpDatabase() {

    }
}
