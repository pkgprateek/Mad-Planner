package com.pkgprateek.madplanner.db;

/**
 * Created by pkgprateek on 03/11/15.
 */
public class Periods {
    int id;
    String startTime;
    String endTime;
    DB.OP op;
    public Periods(DB.OP op, int id, String startTime, String endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.op = op;
    }


    public String toString() {
        return "[" + id + "," + startTime + "," + endTime + "]";
    }

    public String toPresentableString() {
        return "Period " + id + " - From " + startTime + " to " + endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public DB.OP getOp() {
        return op;
    }

    public void setOp(DB.OP op) {
        this.op = op;
    }
}
