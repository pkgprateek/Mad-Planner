package com.pkgprateek.madplanner.db;

/**
 * Created by pkgprateek on 07/11/15.
 */
public class Rotations {
    int id;
    String weekBegin;
    int week;
    DB.OP op;
    public Rotations(DB.OP op, int id, String weekBegin, int week) {
        this.op = op;
        this.id = id;
        this.week = week;
        this.weekBegin = weekBegin;
    }

    public Rotations(DB.OP op) {
        this.op = op;
    }


    public String toString() {
        return "[" + id + "," + week + "," + weekBegin + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeekBegin() {
        return weekBegin;
    }

    public void setWeekBegin(String weekBegin) {
        this.weekBegin = weekBegin;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public DB.OP getOp() {
        return op;
    }

    public void setOp(DB.OP op) {
        this.op = op;
    }
}
