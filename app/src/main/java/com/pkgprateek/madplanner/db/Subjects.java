package com.pkgprateek.madplanner.db;

import android.content.Context;
import android.graphics.Color;

import com.pkgprateek.madplanner.R;

/**
 * Created by pkgprateek on 07/11/15.
 */
public class Subjects {
    int id;
    String name;
    String shortName;
    String colour;
    String lightColour;
    DB.OP op;
    int colourValue;
    Context c;

    public Subjects(Context c,DB.OP op, int id, String name, String shortName, int colourValue) {
        this.c = c;
        this.op = op;
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.colourValue = colourValue;
        this.colour = c.getResources().getStringArray(R.array.colour_values)[colourValue];
        this.lightColour = c.getResources().getStringArray(R.array.colour_light)[colourValue];
    }

    public Subjects(DB.OP op) {
        this.op=op;
    }


    public String toString() {
        return "[" + id + "," + name + "," + shortName + "," + colour + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public DB.OP getOp() {
        return op;
    }

    public void setOp(DB.OP op) {
        this.op = op;
    }

    public String getLightColour() {
        return lightColour;
    }

    public int getColourInt() {
        return Color.parseColor(getColour());
    }

    public int getLightColourInt() {
        return Color.parseColor(getLightColour());
    }

    public int getColourValue() {
        return colourValue;
    }

    public void setColourValue(int value) {
        this.colourValue = colourValue;
        this.colour = c.getResources().getStringArray(R.array.colour_values)[colourValue];
        this.lightColour = c.getResources().getStringArray(R.array.colour_light)[colourValue];
    }
}
