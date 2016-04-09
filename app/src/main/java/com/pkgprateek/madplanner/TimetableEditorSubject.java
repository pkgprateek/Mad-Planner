package com.pkgprateek.madplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.Subjects;
import com.pkgprateek.madplanner.db.TimetableSlot;
import com.pkgprateek.madplanner.db.Timetables;

/**
 * Created by pkgprateek on 04/12/15.
 */
public class TimetableEditorSubject extends ArrayAdapter<Subjects>{

    private LayoutInflater inflater;
    private int resource;
    private Context c;

    public TimetableEditorSubject(Context context, int resource, List<Subjects> objects) {
        super(context, resource, objects);
        this.c = context;
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(resource, null);
        return v;
    }
}
