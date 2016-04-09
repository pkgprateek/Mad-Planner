package com.pkgprateek.madplanner.db;

import com.pkgprateek.madplanner.ex.Methods;

/**
 * Created by pkgprateek on 03/11/15.
 */
public class PeriodSetup {

    private int startMinutes;
    private int endMinutes;
    private int period;
    private boolean b;

    public PeriodSetup(int period, int startTime, int endMinutes) {
        this.period = period;
        this.endMinutes = endMinutes;
        this.startMinutes = startTime;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setStartTime(int time) {
        this.startMinutes = time;
    }

    public void setDuration(int time) {
        this.endMinutes = startMinutes + time;
    }

    public String getStartTime() {
        return Methods.getTime(startMinutes);
    }

    public String getEndTime() {
        return Methods.getTime(endMinutes);
    }

    public int getStartTimeInt() {
        return startMinutes;
    }

    public int getEndTimeInt() {
        return endMinutes;
    }

    public int getDuration() {
        return endMinutes - startMinutes;
    }

    public void setEndTime(int time) {
        this.endMinutes = time;
    }

    public String toString() {
        return "Period " + period + " : " + getStartTime() + ":" + getEndTime();
    }

    public void mark() {
        this.b = true;
    }
    public void setMarked(boolean b) {
        this.b = b;
    }
    public boolean isMarked() {
        return b;
    }
}
