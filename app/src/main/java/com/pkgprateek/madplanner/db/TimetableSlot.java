package com.pkgprateek.madplanner.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import com.pkgprateek.madplanner.R;

/**
 * Created by pkgprateek on 07/11/15.
 */
public class TimetableSlot {
    Context c;
    int period;
    String startTime;
    String endTime;
    String name;
    String shortName;
    String colour;
    String lightColour;
    String teacher;
    String room;
    int subjectID;
    int length;
    int colourValue;
    List<Homeworks> homeworks;
    List<Exams> exams;

    public TimetableSlot(Context c, int period, String startTime, String endTime, int subjectID, String name, String shortName, int colourValue, String teacher, String room, int length) {
        this.c = c;
        this.period = period;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subjectID = subjectID;
        this.name = name;
        this.shortName = shortName;
        this.colourValue = colourValue;
        this.colour = c.getResources().getStringArray(R.array.colour_values)[colourValue];
        this.lightColour = c.getResources().getStringArray(R.array.colour_light)[colourValue];
        this.teacher = teacher;
        this.room = room;
        this.length = length;
        exams = new ArrayList<Exams>();
        homeworks = new ArrayList<Homeworks>();
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getColour() {
        return colour;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getLightColour() {
        return lightColour;
    }

    public List<Homeworks> getHomeworks() {
        return homeworks;
    }
    public void addHomework(Homeworks h) {
        homeworks.add(h);
    }
    public void addExam(Exams e) {
        exams.add(e);
    }
    public List<Exams> getExams() {
        return exams;
    }
    public void setHomeworks(List<Homeworks> h) {
        this.homeworks = h;
    }
    public void setExams(List<Exams> e) {
        this.exams = e;
    }

    public int getColourValue() {
        return colourValue;
    }

    public void setColourValue(int colourValue) {
        this.colourValue = colourValue;
        this.colour = c.getResources().getStringArray(R.array.colour_values)[colourValue];
        this.lightColour = c.getResources().getStringArray(R.array.colour_light)[colourValue];
    }
}
