package com.pkgprateek.madplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.software.shell.fab.ActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.pkgprateek.madplanner.db.DB;
import com.pkgprateek.madplanner.db.Reminders;
import com.pkgprateek.madplanner.ex.Methods;

/**
 * Created by pkgprateek on 18/11/15.
 */
public class Reminder extends Fragment {

    private ListView lV;
    private ReminderAdapter adapter;
    private List<Reminders> reminders;
    private LinearLayout error;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reminders, null);
        lV = (ListView) v.findViewById(R.id.listView);
        error = (LinearLayout) v.findViewById(R.id.error);
        error.setVisibility(View.GONE);
        ((TextView) v.findViewById(R.id.title)).setText("No reminders");

        // Add button
        ActionButton button = (ActionButton) v.findViewById(R.id.action_button);
        button.setButtonColor(getResources().getColor(R.color.accent));
        button.setButtonColorPressed(getResources().getColor(R.color.accent_light));
        button.setImageDrawable(getResources().getDrawable(R.drawable.fab_plus_icon));
        button.setImageResource(R.drawable.fab_plus_icon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("Schedule.ADDREMINDER");
                startActivity(i);
            }
        });

        /**
         * Get all the reminders for the database
         */
        DB db = new DB(getContext());
        db.open();
        reminders = db.getAllReminders();
        if (reminders.size() == 0) {
            error.setVisibility(View.VISIBLE);
        }

        /**
         * Set the adapters for the listview -
         */
        adapter = new ReminderAdapter(getContext(), R.layout.fragment_homework_item, reminders);
        lV.setAdapter(adapter);


        /**
         * Setup the item click listener
         * - When the reminder item is pressed, a dialog will show up showing what it is
         *   It will give the option to edit, delete, or simple close
         */
        lV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final Reminders r = reminders.get(position);
                builder.setTitle(r.getTitle());
                if (r.getDueDate() == null && r.getTime() == null)
                    builder.setMessage(r.getContents());
                else
                    builder.setMessage(r.getContents() + "\n\nDue in " + Methods.getBreakdown(r.getDueDate(), r.getTime(),false));
                builder.setPositiveButton("Close", null);
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        /**
                         * Delete confirmer. Will ask if the user wants to delete this selected
                         * item
                         */

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Delete this item?");
                        builder.setMessage("Are you sure you want to delete:\n" + r.getTitle());
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                r.setOp(DB.OP.DESTROY);
                                DB db = new DB(getContext());
                                db.open();
                                db.runReminder(r);
                                db.close();
                                reminders.remove(position);
                                if (reminders.size() == 0) {
                                    error.setVisibility(View.VISIBLE);
                                }
                                adapter.notifyDataSetChanged();
                                lV.invalidate();
                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        builder.create().show();
                    }
                });
                builder.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent("Schedule.ADDREMINDER");
                        i.putExtra("ID", reminders.get(position).getId());
                        i.putExtra("EDIT", true);
                        startActivity(i);
                    }
                });
                builder.create().show();
            }
        });
        return v;
    }

}
