package com.pkgprateek.madplanner.db;

/**
 * Created by pkgprateek on 03/11/15.
 */
public class Reminders {
    int id;
    String title;
    String contents;
    String dueDate;
    String time;
    DB.OP op;
    boolean viewed;
    public Reminders(DB.OP op, int id, String title, String contents, String dueDate, String time) {
        this.op = op;
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.dueDate = dueDate;
        this.time = time;
        viewed = false;
    }

    public Reminders(DB.OP op) {
        this.op = op;
    }


    public String toString() {
        return "[" + id + "," + title + "," + contents + "," + dueDate + "," + time + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
        if (dueDate.equals(""))
            this.dueDate = null;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        if (time.equals(""))
            this.time = null;
    }

    public DB.OP getOp() {
        return op;
    }

    public void setOp(DB.OP op) {
        this.op = op;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
