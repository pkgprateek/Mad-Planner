package com.pkgprateek.madplanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.Subjects;
import com.pkgprateek.madplanner.db.Timetables;
import com.pkgprateek.madplanner.ex.Methods;

/**
 * Created by pkgprateek on 18/11/15.
 */
public class TimetableDrawer extends View implements View.OnTouchListener{

    private int numberOfRows;
    private int numberOfColumns = 8;
    private int currentWeek;
    private int height;
    private int width;
    private int widthOfScreen = 0;
    private int heightOfScreen = 0;
    private Context context;
    private String[] colours;
    private Paint[] paintP;
    private Rect[][] rectanglesToDraw;
    private Timetables[][] timetables;
    private List<Subjects> subjects;
    private Map<Integer,Paint> colourMap;
    private Paint emptyP;
    private Paint textPaint;
    private int border;
    private boolean released = false;
    private boolean finishedLoading = false;

    private String[] days;

    /**
     * Constructor
     */
    public TimetableDrawer(Context context, int numberOfColumns,  int numberOfRows, int currentWeek) {
        super(context);
        this.numberOfRows = numberOfRows;
        this.currentWeek = currentWeek;
        days = context.getResources().getStringArray(R.array.weekdays);

        colours = context.getResources().getStringArray(R.array.colour_values);
        paintP = new Paint[colours.length];
        for (int i = 0; i < colours.length; i++) {
            paintP[i] = new Paint();
            paintP[i].setColor(Color.parseColor(colours[i]));
        }

        // Setup some sample paint stuff
        emptyP = new Paint();
        emptyP.setColor(Color.WHITE);
        emptyP.setStyle(Paint.Style.FILL);
        textPaint = new Paint();
        textPaint.setTextSize(Methods.getPixels(context, 20));
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        border = (int) Methods.getPixels(context, 2);

        // Try and get the width of the display
        // - Error - returns 0 because View doesn't have any layout properties just yet
        widthOfScreen = getWidth();
        heightOfScreen = getHeight();

        // Setup the map
        colourMap = new HashMap<Integer,Paint>();

        // Attempt to load the initial display now
        invalidate();
    }


    /**
     * OnDraw method for the canvas
     * When this is drawn, the canvas is drawn according to the method below
     *
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Checking if the width and height have any values
        if (getWidth() != 0 && getHeight() != 0) {
            widthOfScreen = getWidth();
            width = widthOfScreen / numberOfColumns;
            heightOfScreen = getHeight();
            height = heightOfScreen / numberOfRows;
        }

        // If the LoadDisplay hasn't run once
        if (!finishedLoading) {
            canvas.drawRect(50, 50, getWidth() - 50, getHeight() - 50, emptyP);
        } else {
            // Go through every one
            for (int a = 0; a < numberOfColumns; a++) {
                for (int b = 0; b < numberOfRows; b++) {
                    if (a != 0) {
                        // If there's a timetable element, draw it with the colour
                        if (timetables[a-1][b] != null) {
                            canvas.drawRect(rectanglesToDraw[a][b], colourMap.get(timetables[a - 1][b].getSubject()));
                        } else {
                            canvas.drawRect(rectanglesToDraw[a][b], emptyP);
                        }
                    } else {
                        // Draw the period identifiers
                        float xOffset = textPaint.measureText(Integer.toString(b + 1), 0, (Integer.toString(b+1).length())) / 2;
                        float yOffset = textPaint.descent() / 2;
                        canvas.drawText(Integer.toString(b + 1)
                                , rectanglesToDraw[a][b].exactCenterX() - xOffset
                                , rectanglesToDraw[a][b].exactCenterY() + yOffset
                                , textPaint);

                    }
                }
            }
        }
    }


    // Reload display method - Called from onResume of parent activity
    public void reloadDisplay() {
        LoadDisplay display = new LoadDisplay(getContext(), this);
        display.execute();
    }


    // Runs when the size of the screen is changed (ie. When it's loaded and rendered)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (getWidth() != 0 && getHeight() != 0) {
            widthOfScreen = getWidth();
            width = widthOfScreen / numberOfColumns;
            heightOfScreen = getHeight();
            height = heightOfScreen / numberOfRows;
        }
    }


    /**
     * ASyncTask for the Load Display - Do all the heavy DB Querying here
     */

    private class LoadDisplay extends AsyncTask<Integer,Integer,Boolean> {

        private Context c;
        private TimetableDrawer drawer;

        public LoadDisplay(Context c, TimetableDrawer drawer) {
            this.c = c;
            this.drawer = drawer;
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            // Setup all the possible drawing bounds
            DB db = new DB(c);
            db.open();

            // Setup the days method
            days = c.getResources().getStringArray(R.array.weekdays);

            // Here we setup the drawing bounds, plus any assigned timetable
            timetables = new Timetables[numberOfColumns - 1][numberOfRows];
            rectanglesToDraw = new Rect[numberOfColumns][numberOfRows];

            // Render everything
            for (int a = 0; a < numberOfColumns; a++) {
                List<Timetables> timetableList;
                if (a != 0)
                    timetableList = db.getTimetables(currentWeek, days[a-1]);
                else
                    timetableList = new ArrayList<Timetables>();
                int index = 0;
                for (int b = 0; b < numberOfRows; b++) {
                    rectanglesToDraw[a][b] = new Rect(border + (a * width),
                            border + (b * height),
                            ((a + 1) * width) - border,
                            ((b + 1) * height) - border);
                    if (a != 0) {
                        if (timetableList.size() != 0) {
                            if (index == timetableList.size())
                                index = 0;
                            if (timetableList.get(index).getPeriod() == (b + 1)) {
                                // Timetable element exists. Draw something here
                                timetables[a-1][b] = timetableList.get(index);
                                index++;
                            } else {
                                // Timetable element doesn't exist. Draw the blank cell here
                                timetables[a-1][b] = null;
                            }
                        } else {
                            timetables[a-1][b] = null;
                        }
                    }
                }
            }

            // Initialise the layout in the onCreate
            db.close();
            return false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);

            // Get all the subjects
            refreshSubjects();

            // Update the display
            assignOnTouchListener(true);
            finishedLoading = true;
            drawer.invalidate();
            drawer.postInvalidate();
        }
    }


    // Method to enable and disable the onTouch listener
    public void assignOnTouchListener(boolean tf) {
        if (tf) {
            setOnTouchListener(this);
        } else {
            setOnTouchListener(null);
        }
    }


    // Method to re-query the database and find the remaining subjects
    public void refreshSubjects() {
        DB db = new DB(getContext());
        db.open();
        subjects = db.getAllSubjects();
        db.close();
        colourMap.clear();
        for (int i = 0; i < subjects.size(); i++) {
            Paint paintObj = new Paint();
            paintObj.setStyle(Paint.Style.FILL);
            paintObj.setColor(subjects.get(i).getColourInt());

            colourMap.put(subjects.get(i).getId(), paintObj);
        }
    }


    // Method to return the status of finished (ie. If the display has ever been loaded) to a parent
    public boolean getFinished() {
        return finishedLoading;
    }

    // OnTouch method - Use to attempt to find out where the button has been pressed
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int xCord = (int) event.getX();
        int yCord = (int) event.getY();
        Rect intersection = new Rect(xCord, yCord, xCord + (border / 2), yCord + (border / 2));
        for (int a = 0; a < numberOfRows; a++) {
            for (int b = 0; b < numberOfColumns; b++) {
                if (intersection.intersect(rectanglesToDraw[b][a])) {
                    Log.i("Schedule", "Square " + a + "," + b + " has been clicked");
                }
            }
        }
        invalidate();
        return false;
    }
}
