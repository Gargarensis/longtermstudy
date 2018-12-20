package usi.memotion2personal;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import usi.memotion2personal.Reminders.Weekday;
import usi.memotion2personal.UI.EdiaryAdapter;
import usi.memotion2personal.UI.fragments.EdiaryFragment;
import usi.memotion2personal.local.database.controllers.LocalStorageController;
import usi.memotion2personal.local.database.controllers.SQLiteController;
import usi.memotion2personal.local.database.db.LocalSQLiteDBHelper;
import usi.memotion2personal.local.database.tables.EdiaryTable;
import usi.memotion2personal.remote.database.controllers.SwitchDriveController;

import static usi.memotion2personal.MyApplication.getContext;

public class WeeklyActivityPopup extends AppCompatActivity {

    private LocalStorageController localController;

    LocalSQLiteDBHelper dbHelper;
    SwitchDriveController switchDriveController;
    String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras;
        if (savedInstanceState == null) {
            extras = getIntent().getExtras();

            if (extras == null || extras.getStringArray("weekday") == null)
                return;

            String[] data = extras.getStringArray("weekday");

            localController = SQLiteController.getInstance(WeeklyActivityPopup.this);
            dbHelper = new LocalSQLiteDBHelper(this);


            LayoutInflater inflater = (this).getLayoutInflater();
            final NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);

            int notificationId = getIntent().getIntExtra("notificationId", -1);

            if (getIntent().getStringExtra("type_yes") != null) {

                String query = "SELECT " + EdiaryTable.START_TIME + "," + EdiaryTable.END_TIME + "," + EdiaryTable.TIMESTAMP + " FROM " + EdiaryTable.TABLE_EDIARY_TABLE + " WHERE " + EdiaryTable.ISWEEKLY + " LIKE \"1\" AND " + EdiaryTable.TIMESTAMP + " LIKE " + data[3] + ";";
                Cursor c = localController.rawQuery(query, null);

                String startTime = "";
                String endTime = "";
                String originalTimestamp = "";


                while (c.moveToNext()) {
                    startTime = c.getString(0);
                    endTime = c.getString(1);
                    originalTimestamp = c.getString(2);
                }
                c.close();

                if ("".equals(startTime) || "".equals(endTime) || "".equals(originalTimestamp)) {
                    return;
                }

                final EdiaryFragment.EdiaryActivity ediary = new EdiaryFragment.EdiaryActivity("", data[0], startTime, endTime, "", "", data[3], originalTimestamp, "0");
                final AlertDialog.Builder builderUpdate = new AlertDialog.Builder(this);
                builderUpdate.setCancelable(false);
                builderUpdate.setView(inflater.inflate(R.layout.create_activity_layout, null));
                builderUpdate.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                builderUpdate.setPositiveButton("SUBMIT", null);
                final AlertDialog update = builderUpdate.create();
                update.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        final Spinner ediary_activity_spinner = (Spinner) ((AlertDialog) update).findViewById(R.id.ediary_activity_spinner);
                        final LinearLayout otherLayout = (LinearLayout) ((AlertDialog) update).findViewById(R.id.otherLayout);
                        final EditText ediary_activity_edit = (EditText) ((AlertDialog) update).findViewById(R.id.ediary_activity_edit);
                        String activity = ediary.getActivity();
                        ArrayAdapter myAdap = (ArrayAdapter) ediary_activity_spinner.getAdapter();
                        Integer spinnerPosition = myAdap.getPosition(activity);
                        if (spinnerPosition != null && spinnerPosition >= 0) {
                            ediary_activity_spinner.setSelection(spinnerPosition);
                            otherLayout.setVisibility(View.GONE);
                        } else {
                            ediary_activity_spinner.setSelection(ediary_activity_spinner.getCount() - 1);
                            otherLayout.setVisibility(View.VISIBLE);
                            ediary_activity_edit.setText(ediary.getActivity());
                        }
                        ediary_activity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (ediary_activity_spinner.getItemAtPosition(i).toString().equals("Other")) {
                                    otherLayout.setVisibility(View.VISIBLE);
                                } else {
                                    ediary_activity_edit.setText("");
                                    otherLayout.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });

                        Spinner activity_start_hour = (Spinner) ((AlertDialog) update).findViewById(R.id.activity_start_hour);
                        Spinner activity_start_minute = (Spinner) ((AlertDialog) update).findViewById(R.id.activity_start_minute);
                        Spinner activity_end_hour = (Spinner) ((AlertDialog) update).findViewById(R.id.activity_end_hour);
                        Spinner activity_end_minute = (Spinner) ((AlertDialog) update).findViewById(R.id.activity_end_minute);

                        String start[] = ediary.getStart_time().split(":");
                        String end[] = ediary.getEnd_time().split(":");

                        if (start[0] == "Select") {
                            activity_start_hour.setSelection(0);
                        } else {
                            activity_start_hour.setSelection(Integer.parseInt(start[0]) + 1);
                        }

                        if (start[1] == "Select") {
                            activity_start_minute.setSelection(0);
                        } else {
                            activity_start_minute.setSelection(Integer.parseInt(start[1]) + 1);
                        }

                        if (end[0] == "Select") {
                            activity_end_hour.setSelection(0);
                        } else {
                            activity_end_hour.setSelection(Integer.parseInt(end[0]) + 1);
                        }

                        if (end[1] == "Select") {
                            activity_end_minute.setSelection(0);
                        } else {
                            activity_end_minute.setSelection(Integer.parseInt(end[1]) + 1);
                        }

                        RadioButton ediary_interaction_yes = (RadioButton) ((AlertDialog) update).findViewById(R.id.ediary_interaction_yes);
                        RadioButton ediary_interaction_no = (RadioButton) ((AlertDialog) update).findViewById(R.id.ediary_interaction_no);

                        switch (ediary.getSocial_interaction()) {
                            case "Yes":
                                ediary_interaction_yes.setChecked(true);
                                break;
                            case "No":
                                ediary_interaction_no.setChecked(true);
                                break;
                        }

                        RadioButton ediary_emotion_very_happy = (RadioButton) ((AlertDialog) update).findViewById(R.id.ediary_emotion_very_happy);
                        RadioButton ediary_emotion_happy = (RadioButton) ((AlertDialog) update).findViewById(R.id.ediary_emotion_happy);
                        RadioButton ediary_emotion_neutral = (RadioButton) ((AlertDialog) update).findViewById(R.id.ediary_emotion_neutral);
                        RadioButton ediary_emotion_sad = (RadioButton) ((AlertDialog) update).findViewById(R.id.ediary_emotion_sad);
                        RadioButton ediary_emotion_very_sad = (RadioButton) ((AlertDialog) update).findViewById(R.id.ediary_emotion_very_sad);

                        switch (ediary.getEmotion()) {
                            case "very_happy":
                                ediary_emotion_very_happy.setChecked(true);
                                break;
                            case "happy":
                                ediary_emotion_happy.setChecked(true);
                                break;
                            case "neutral":
                                ediary_emotion_neutral.setChecked(true);
                                break;
                            case "sad":
                                ediary_emotion_sad.setChecked(true);
                                break;
                            case "very_sad":
                                ediary_emotion_very_sad.setChecked(true);
                                break;
                        }

                        CheckBox ediary_is_weekly = (CheckBox) ((AlertDialog) update).findViewById(R.id.ediary_is_weekly_activity);

                        ediary_is_weekly.setVisibility(View.GONE);

                        EditText ediary_comments = (EditText) ((AlertDialog) update).findViewById(R.id.ediary_comments);
                        ediary_comments.setText(ediary.getComments());
                        Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Spinner ediary_activity_spinner = (Spinner) ((AlertDialog) dialog).findViewById(R.id.ediary_activity_spinner);
                                LinearLayout otherLayout = (LinearLayout) ((AlertDialog) dialog).findViewById(R.id.otherLayout);
                                EditText ediary_activity_edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.ediary_activity_edit);

                                Spinner activity_start_hour = (Spinner) ((AlertDialog) dialog).findViewById(R.id.activity_start_hour);
                                Spinner activity_start_minute = (Spinner) ((AlertDialog) dialog).findViewById(R.id.activity_start_minute);

                                Spinner activity_end_hour = (Spinner) ((AlertDialog) dialog).findViewById(R.id.activity_end_hour);
                                Spinner activity_end_minute = (Spinner) ((AlertDialog) dialog).findViewById(R.id.activity_end_minute);

                                RadioGroup ediary_interaction_group = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.ediary_interaction_group);

                                RadioGroup ediary_emotion_group = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.ediary_emotion_group);

                                EditText ediary_comments = (EditText) ((AlertDialog) dialog).findViewById(R.id.ediary_comments);

                                boolean error = false;
                                String activity = "";
                                if (otherLayout.getVisibility() == View.VISIBLE) {
                                    if (ediary_activity_edit.getText().toString().isEmpty()) {
                                        ediary_activity_edit.setError("Please enter a value!");
                                        error = true;
                                    } else {
                                        activity = ediary_activity_edit.getText().toString();
                                    }
                                } else {
                                    if (ediary_activity_spinner.getSelectedItem().toString().equals("Select")) {
                                        error = true;
                                        ((TextView) ediary_activity_spinner.getSelectedView()).setError("Please enter a value!");
                                    }
                                    activity = ediary_activity_spinner.getSelectedItem().toString();
                                }

                                if (activity_start_hour.getSelectedItem().toString().equals("Select")) {
                                    error = true;
                                    ((TextView) activity_start_hour.getSelectedView()).setError("Please enter a value!");
                                }
                                if (activity_end_hour.getSelectedItem().toString().equals("Select")) {
                                    error = true;
                                    ((TextView) activity_end_hour.getSelectedView()).setError("Please enter a value!");
                                }
                                if (activity_start_minute.getSelectedItem().toString().equals("Select")) {
                                    error = true;
                                    ((TextView) activity_start_minute.getSelectedView()).setError("Please enter a value!");
                                }
                                if (activity_end_minute.getSelectedItem().toString().equals("Select")) {
                                    error = true;
                                    ((TextView) activity_end_minute.getSelectedView()).setError("Please enter a value!");
                                }
                                if (!error) {
                                    int choice = ediary_interaction_group.getCheckedRadioButtonId();
                                    String interaction = "";
                                    if (choice > 0) {
                                        RadioButton radio = (RadioButton) ediary_interaction_group.findViewById(choice);
                                        interaction = radio.getText().toString();
                                    }

                                    String start_time = activity_start_hour.getSelectedItem().toString() + ":" + activity_start_minute.getSelectedItem().toString();
                                    String end_time = activity_end_hour.getSelectedItem().toString() + ":" + activity_end_minute.getSelectedItem().toString();
                                    String comments = ediary_comments.getText().toString().isEmpty() ? "" : ediary_comments.getText().toString();

                                    int choice_emotion = ediary_emotion_group.getCheckedRadioButtonId();
                                    String emotion = "";
                                    if (choice_emotion > 0) {
                                        switch (choice_emotion) {
                                            case R.id.ediary_emotion_very_happy:
                                                emotion = "very_happy";
                                                break;
                                            case R.id.ediary_emotion_happy:
                                                emotion = "happy";
                                                break;
                                            case R.id.ediary_emotion_neutral:
                                                emotion = "neutral";
                                                break;
                                            case R.id.ediary_emotion_sad:
                                                emotion = "sad";
                                                break;
                                            case R.id.ediary_emotion_very_sad:
                                                emotion = "very_sad";
                                                break;
                                        }
                                    }
                                    String timestamp = ediary.getTimestamp();
                                    String isWeekly = timestamp;
                                    Format formatter = new SimpleDateFormat("dd/MM/yyyy");
                                    EdiaryFragment.EdiaryActivity entry = new EdiaryFragment.EdiaryActivity(emotion, activity, start_time, end_time, interaction, comments, formatter.format(new DateTime(new Date()).toDate()), String.valueOf(System.currentTimeMillis()), isWeekly);
                                    ContentValues record = new ContentValues();
                                    record.put(EdiaryTable.ACTIVITY, entry.getActivity());
                                    record.put(EdiaryTable.TIMESTAMP, entry.getTimestamp());
                                    record.put(EdiaryTable.START_TIME, entry.getStart_time());
                                    record.put(EdiaryTable.END_TIME, entry.getEnd_time());
                                    record.put(EdiaryTable.SOCIAL_INTERACTION, entry.getSocial_interaction());
                                    record.put(EdiaryTable.EMOTION, entry.getEmotion());
                                    record.put(EdiaryTable.COMMENTS, entry.getComments());
                                    record.put(EdiaryTable.ENTRY_DATE, entry.getDate_entry());
                                    record.put(EdiaryTable.ISWEEKLY, entry.getIsWeekly());
                                    localController.insertRecord(EdiaryTable.TABLE_EDIARY_TABLE, record);
                                    Log.d("EDIARY UPDATE", "Added record: ts: " + record.get(EdiaryTable.TIMESTAMP));
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
                update.show();
            }

            if (getIntent().getStringExtra("type_no") != null) {
                // nothing to do, we just dismiss the notification
            }

            if (getIntent().getStringExtra("type_remove") != null) {
                ContentValues toUpdate = new ContentValues();
                toUpdate.put(EdiaryTable.ISWEEKLY, "0");
                localController.update(EdiaryTable.TABLE_EDIARY_TABLE, toUpdate, EdiaryTable.TIMESTAMP + " LIKE " + data[3]);

            }

            if (notificationId > 0) {
                notificationManager.cancel(notificationId);
            }
        }
    }
}