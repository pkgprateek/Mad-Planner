package com.pkgprateek.madplanner.db;

/**
 * Created by pkgprateek on 07/11/15.
 */
public class Timetables {
    int week;
    String day;
    int period;
    int subject;
    int room;
    int teacher;
    int length;
    DB.OP op;
    public Timetables(DB.OP op, int week, String day, int period, int subject, int room, int teacher, int length) {
        this.op = op;
        this.week = week;
        this.day = day;
        this.period = period;
        this.subject = subject;
        this.room = room;
        this.teacher = teacher;
        this.length = length;
    }


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String toString() {
        return "[" + week + "," + day + "," + period + "," + subject + "," + room + "," + teacher + "]";
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getTeacher() {
        return teacher;
    }

    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }

    public DB.OP getOp() {
        return op;
    }

    public void setOp(DB.OP op) {
        this.op = op;
    }
}
