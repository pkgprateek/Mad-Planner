package com.pkgprateek.madplanner.db;

/**
 * Created by pkgprateek on 03/11/15.
 */
public class Homeworks {
    int id;
    int period;
    int subject = -1;
    String dueDate;
    String title = "";
    String contents = "";
    int done;
    String time;
    DB.OP op;
    boolean viewed;

    public Homeworks(DB.OP op, int id, int period, int subject, String dueDate, String title, String contents, int done, String time) {
        this.op = op;
        this.id = id;
        this.period = period;
        this.subject = subject;
        this.dueDate = dueDate;
        this.title = title;
        this.contents = contents;
        this.done = done;
        this.time = time;
        expanded = false;
        viewed = false;
    }

    public Homeworks(DB.OP op) {
        this.op = op;
    }


    public String toString() {
        return "[" + id + "," + period + "," + subject + "," + dueDate + "," + title + "," + contents + "," + done + "," + time + "]";
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
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

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public boolean validate() {
        boolean p1 = false, p2 = false, p3 = false;
        if (subject != -1)
            p1 = true;
        if (dueDate.length() == 10)
            p2 = true;
        if (time.length() == 5)
            p3 = true;
        return p1 && p2 && p3;
    }

    /**
     * Variables for the layout, not the database
     */
    public boolean expanded = false;

    public boolean isExpanded() {
        return expanded;
    }

    public void expand() {
        setExpanded(true);
    }
    public void collapse() {
        setExpanded(true);
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
