package usi.memotion2personal.UI.fragments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import usi.memotion2personal.R;
import usi.memotion2personal.UI.EdiaryAdapter;
import usi.memotion2personal.local.database.controllers.LocalStorageController;
import usi.memotion2personal.local.database.controllers.SQLiteController;
import usi.memotion2personal.local.database.tables.EdiaryTable;

public class EdiaryFragment extends Fragment {
    ListView listView;
    private LocalStorageController localController;
    private String dateView;


    public EdiaryFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ediary, container, false);
        localController = SQLiteController.getInstance(getContext());
        listView = (ListView) view.findViewById(R.id.entriesList);
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_activity_button);
        final Spinner daySpinner = (Spinner) view.findViewById(R.id.daySpinner);


        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Format formatter = new SimpleDateFormat("dd/MM/yyyy");
                switch (daySpinner.getItemAtPosition(i).toString()) {
                    case "Today":
                        dateView = formatter.format(new DateTime(new Date()).toDate());
                        break;
                    case "Yesterday":
                        dateView = formatter.format(new DateTime(new Date()).minusDays(1).toDate());
                        break;
                }
                new LoadEdiary().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final EdiaryActivity ediary = (EdiaryActivity) listView.getAdapter().getItem(i);
                final AlertDialog.Builder builderUpdate = new AlertDialog.Builder(getContext());
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

                        if (!("0".equals(ediary.getIsWeekly()))) {
                            ediary_is_weekly.setChecked(true);
                        }

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
                                    String isWeekly = "0";
                                    if (((CheckBox) (((AlertDialog) dialog).findViewById(R.id.ediary_is_weekly_activity))).isChecked()) {
                                        if (!("0".equals(ediary.getIsWeekly())))
                                            isWeekly = "1";
                                    }

                                    EdiaryActivity entry = new EdiaryActivity(emotion, activity, start_time, end_time, interaction, comments, "", "", isWeekly);
                                    ContentValues record = new ContentValues();
                                    record.put(EdiaryTable.ACTIVITY, entry.getActivity());
                                    record.put(EdiaryTable.START_TIME, entry.getStart_time());
                                    record.put(EdiaryTable.END_TIME, entry.getEnd_time());
                                    record.put(EdiaryTable.SOCIAL_INTERACTION, entry.getSocial_interaction());
                                    record.put(EdiaryTable.EMOTION, entry.getEmotion());
                                    record.put(EdiaryTable.COMMENTS, entry.getComments());
                                    record.put(EdiaryTable.ISWEEKLY, entry.getIsWeekly());
                                    localController.update(EdiaryTable.TABLE_EDIARY_TABLE, record, EdiaryTable.TIMESTAMP + " = " + timestamp);

                                    if (Long.valueOf(ediary.getIsWeekly()) > 1) {
                                        ContentValues toUpdate = new ContentValues();
                                        toUpdate.put(EdiaryTable.ISWEEKLY, "0");
                                        localController.update(EdiaryTable.TABLE_EDIARY_TABLE, toUpdate, EdiaryTable.TIMESTAMP + " LIKE " + ediary.getIsWeekly());
                                    }

                                    Log.d("EDIARY UPDATE", "Added record: ts: " + record.get(EdiaryTable.TIMESTAMP));
                                    new LoadEdiary().execute();
                                    dialog.dismiss();
                                }
                            }
                        });
                    };
                });
                update.show();
            };
        });
        fab.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builderAdd = new AlertDialog.Builder(getContext());
                builderAdd.setCancelable(false);
                builderAdd.setView(inflater.inflate(R.layout.create_activity_layout, null));
                builderAdd.setPositiveButton("SUBMIT", null);
                builderAdd.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                final AlertDialog add = builderAdd.create();
                add.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        final Spinner ediary_activity_spinner = (Spinner) add.findViewById(R.id.ediary_activity_spinner);
                        final LinearLayout otherLayout = (LinearLayout) add.findViewById(R.id.otherLayout);
                        final EditText ediary_activity_edit = (EditText) add.findViewById(R.id.ediary_activity_edit);
                        otherLayout.setVisibility(View.GONE);
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
                                    String timestamp = String.valueOf(System.currentTimeMillis());
                                    String isWeekly = ((CheckBox) (((AlertDialog) dialog).findViewById(R.id.ediary_is_weekly_activity))).isChecked() ? "1" : "0";
                                    EdiaryActivity entry = new EdiaryActivity(emotion, activity, start_time, end_time, interaction, comments, dateView, timestamp, isWeekly);
                                    ContentValues record = new ContentValues();
                                    record.put(EdiaryTable.TIMESTAMP, timestamp);
                                    record.put(EdiaryTable.ACTIVITY, entry.getActivity());
                                    record.put(EdiaryTable.START_TIME, entry.getStart_time());
                                    record.put(EdiaryTable.END_TIME, entry.getEnd_time());
                                    record.put(EdiaryTable.SOCIAL_INTERACTION, entry.getSocial_interaction());
                                    record.put(EdiaryTable.EMOTION, entry.getEmotion());
                                    record.put(EdiaryTable.COMMENTS, entry.getComments());
                                    record.put(EdiaryTable.ENTRY_DATE, entry.getDate_entry());
                                    record.put(EdiaryTable.ISWEEKLY, entry.getIsWeekly());
                                    localController.insertRecord(EdiaryTable.TABLE_EDIARY_TABLE, record);
                                    Log.d("EDIARY ENTRY", "Added record: ts: " + record.get(EdiaryTable.TIMESTAMP));
                                    new LoadEdiary().execute();
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
                add.show();
            }

            ;
        });
        return view;
    }


    public static class EdiaryActivity {
        String timestamp;
        String emotion;
        String activity;
        String start_time;
        String date_entry;
        String end_time;
        String social_interaction;
        String comments;
        String isWeekly;

        public EdiaryActivity(String emotion, String activity, String start_time, String end_time, String social_interaction, String comments, String date_entry, String timestamp, String isWeekly) {
            this.emotion = emotion;
            this.activity = activity;
            this.start_time = start_time;
            this.end_time = end_time;
            this.social_interaction = social_interaction;
            this.comments = comments;
            this.date_entry = date_entry;
            this.timestamp = timestamp;
            this.isWeekly = isWeekly;
        }

        public String getEmotion() {
            return emotion;
        }

        public String getActivity() {
            return activity;
        }

        public String getStart_time() {
            return start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public String getSocial_interaction() {
            return social_interaction;
        }

        public String getComments() {
            return comments;
        }

        public String getDate_entry() {
            return date_entry;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getIsWeekly() { return isWeekly; }
    }

    public class LoadEdiary extends AsyncTask {
        ArrayList<EdiaryActivity> ediaries = new ArrayList<EdiaryActivity>();

        @Override
        protected Object doInBackground(Object[] objects) {
            String query = "SELECT * FROM " + EdiaryTable.TABLE_EDIARY_TABLE + " WHERE " + EdiaryTable.ENTRY_DATE + " = \"" + dateView + "\" ORDER BY " + EdiaryTable.START_TIME + ";";
            Cursor c = localController.rawQuery(query, null);
            while (c.moveToNext()) {
                EdiaryActivity ediary = new EdiaryActivity(c.getString(6), c.getString(2), c.getString(3), c.getString(4), c.getString(7), c.getString(8), c.getString(5), c.getString(1), c.getString(9));
                ediaries.add(ediary);
            }
            c.close();
            final EdiaryAdapter adapter = new EdiaryAdapter(getContext(), ediaries);
            getActivity().runOnUiThread(new Thread(new Runnable() {
                public void run() {
                    listView.setAdapter(adapter);
                }
            }));
            return null;
        }
    }
}
