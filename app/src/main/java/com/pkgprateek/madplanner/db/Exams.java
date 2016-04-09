package com.pkgprateek.madplanner.db;

/**
 * Created by pkgprateek on 03/11/15.
 */
public class Exams {
    int id;
    int subject;
    String title;
    String contents;
    String date;
    String time;
    int duration;
    int room;
    String seatnumber;
    int period;
    DB.OP op;
    boolean viewed;
    public Exams(DB.OP op, int id, int subject, String title, String contents, String date, String time, int period, int duration, int room, String seatnumber) {
        this.op = op;
        this.id = id;
        this.subject = subject;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.room = room;
        this.seatnumber = seatnumber;
        this.period = period;
        viewed = false;
    }

    public Exams(DB.OP op) {
        this.op = op;
    }

    public String toString() {
        return "[" + id + "," + subject + "," + title + "," + contents + "," + date + "," + time + "]";
    }

    public DB.OP getOp() {
        return op;
    }

    public void setOp(DB.OP op) {
        this.op = op;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public String getSeatnumber() {
        return seatnumber;
    }

    public void setSeatnumber(String seatnumber) {
        this.seatnumber = seatnumber;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
