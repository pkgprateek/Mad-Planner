package com.pkgprateek.madplanner.ex;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.pkgprateek.madplanner.R;
import com.pkgprateek.madplanner.db.Homeworks;

/**
 * Created by pkgprateek on 17/11/15.
 */
public class Methods {



    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
    public static SimpleDateFormat dayOfWeekFormatter = new SimpleDateFormat("EEEE");
    public static SimpleDateFormat timeFormatter  = new SimpleDateFormat("HH:mm");

    /**
     * Minutes between two dates method
     */
    public static int minutesBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60));
    }


    /**
     * getDate object method
     *
     */
    public static Date getDate(String input) {
        // input must be in yyyy/MM/dd
        try {
            return dateFormatter.parse(input);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * Get the day based on a date
     *
     */
    public static String getDay(Date d) {
        return dayOfWeekFormatter.format(d);
    }
    public static String getDay(String input) {
        return getDay(getDate(input));
    }


    /**
     * Get string representation of a Calendar object
     *
     */
    public static String getDate(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(d);
    }


    /**
     * getBreakdown method ()
     * - Shows a shorthand view of when a reminder is in (ie. 2 days, 4 hours, 3 minutes)
     * - Convert dueDate="yyyy/MM/dd" dueTime="HH:mm" to ^^^^^^^^^
     */
    public static String getBreakdown(String dueDate, String dueTime, boolean shorter, boolean dueOrProgress) {
        SimpleDateFormat timeDateFormatter = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
        String returnString = "";
        try {

            Date d = timeDateFormatter.parse(dueDate + "/" + dueTime);
            Date now = new Date();
            int minutes = minutesBetween(now, d);

            if (minutes > 0) {
                if ((minutes / 60 / 24) >= 1) {
                    // Days exist
                    int days = (minutes / 60 / 24);
                    if (shorter)
                        returnString += days + "d, ";
                    else
                        returnString += days + " days, ";
                    minutes = minutes - (days * 24 * 60);
                }
                if ((minutes / 60) >= 1) {
                    // Hours exist
                    int hours = (minutes / 60);
                    if (shorter)
                        returnString += hours + "h, ";
                    else
                        returnString += hours + " hours, ";
                    minutes = minutes - (hours * 60);
                }
                if (minutes >= 1) {
                    if (shorter)
                        returnString += minutes + "m";
                    else
                        returnString += minutes + " minutes";
                }
            } else {
                if (dueOrProgress) 
                    returnString = "Due date passed";
                else
                    returnString = "In progress";
            }

            return returnString;
        } catch (Exception e) {
            Log.i("Schedule", "Exception called in getBreakdown");
            e.printStackTrace();
            return "Error.";
        }
    }
    public static String getBreakdown(String dueDate, String dueTime, boolean shorter) {
        return getBreakdown(dueDate, dueTime, shorter, true);
    }
    // Get the shorter time
    public static String getShortTime(Homeworks h) {
        return getBreakdown(h.getDueDate(), h.getTime(), true);
    }
    public static String getDaysBetween(String dueDate) {
        try {
            Date d = dateFormatter.parse(dueDate);
            Date now = new Date();
            int days = (minutesBetween(getStartOfDay(now), getStartOfDay(d)) / 60 / 24);

            return days + " days";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }
    public static int getDaysBetween(Date end, Date start) {
        try {
            int days = (minutesBetween(getStartOfDay(end), getStartOfDay(start)) / 60 / 24);
            return days;
        } catch (Exception e) {
            return -1;
        }
    }
    public static Date getStartOfDay(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Reverse the date stored so it's more legable
     * -ie. Convert yyyy/MM/dd to dd/MM/yyyy
     */
    public static String reverseDate(String s) {
        try {
            String[] ss = s.split("/");
            return ss[2] + "/" + ss[1] + "/" + ss[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.i("Schedule", "IndexOutOfBounds called in reverseDate");
            return "Error reversing date";
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Schedule", "Exception called in reverseDate");
            return "Error reversing date";
        }
    }


    /**
     * Get the string version of the month from the number
     */

    // Get the month with the month integer as an input (ie. 1 > January)
    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }


    /**
     * Make sure an integer is just two digits long
     */
    public static String convert(int i) {
        if (i <= 9)
            return "0" + i;
        else {
            return "" + i;
        }
    }


    /**
     * Method to convert a number of minutes into a notable time
     */
    public static String getTime(Date d) {
        return timeFormatter.format(d);
    }

    public static String getTime(int minutes) {
        return convert(minutes/60) + ":" + convert(minutes%60);
    }

    /**
     * Method for returning the first day of the week given a date
     */
    public static String getFirstDayOfWeek(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        d = c.getTime();
        return getDate(d);
    }
    public static Date getFirstDayOfWeekDate(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        d = c.getTime();
        return getStartOfDay(d);
    }
    public static String getFirstDayOfWeek(String input) {
        return getFirstDayOfWeek(getDate(input));
    }
    public static Date goToNextWeek(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DAY_OF_WEEK, 7);
        d = c.getTime();
        return d;
    }

    /**
     * Method to get the displacement of the day from the start of the week
     *
     */
    public static int getDaysDisplacement(Date da, String day) {
        String date = getDay(da);
        return getDaysFromStart(day) - getDaysFromStart(date);

    }
    public static int getDaysFromStart(String day) {
        switch (day) {
            case "Monday":
                return 0;
            case "Tuesday":
                return 1;
            case "Wednesday":
                return 2;
            case "Thursday":
                return 3;
            case "Friday":
                return 4;
            case "Saturday":
                return 5;
            case "Sunday":
                return 6;
        }
        return -1;
    }
    public static String getDaysFromStart(int i) {
        switch (i) {
            case 0:
                return "Monday";
            case 1:
                return "Tuesday";
            case 2:
                return "Wednesday";
            case 3:
                return "Thursday";
            case 4:
                return "Friday";
            case 5:
                return "Saturday";
            case 6:
                return "Sunday";
            default:
                return "Error";
        }
    }
    public static int getDaysFromStart(Date d) {
        return getDaysFromStart(getDay(d));
    }


    /**
     * Method to get the weeks boolean array
     */
    public static boolean[] getWeeks(List<String> days) {
        boolean[] weeks = new boolean[7];
        for (int i = 0; i < weeks.length; i++) {
            if (days.contains(Methods.getDaysFromStart(i))) {
                weeks[i] = true;
            } else {
                weeks[i] = false;
            }
        }
        return weeks;
    }


    /**
     * Method to get the colour profiles
     */
    public static String[] colourProfiles(String colour, Context c) {
        String[] colours = new String[3];
        colours[1] = colour;
        String[] colourValues = c.getResources().getStringArray(R.array.colour_values);
        String[] colourLight = c.getResources().getStringArray(R.array.colour_light);
        String[] colourNames = c.getResources().getStringArray(R.array.colour_names);

        int index = 0;
        colours[0] = colourNames[index];
        for (int i = 0; i < colourValues.length; i++) {
            if (colourValues[i].equals(colour)) {
                index = i;
                i = colourValues.length;
            }
        }
        colours[0] = colourNames[index];
        colours[2] = colourLight[index];
        return colours;
    }


    /**
     * Method to convert HH:mm into minutes integer
     */
    public static int convert(String time) {
        return Integer.parseInt(time.split(":")[0]) * 60
                + Integer.parseInt(time.split(":")[1]);
    }


    /**
     * Method to convert DPI value into pixels
     */
    public static float getPixels(Context c, int dpi) {
        Resources r = c.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, r.getDisplayMetrics());
    }
}
