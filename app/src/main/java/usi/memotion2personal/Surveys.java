package usi.memotion2personal;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import usi.memotion2personal.UI.fragments.PamSurveyView;
import usi.memotion2personal.local.database.controllers.LocalStorageController;
import usi.memotion2personal.local.database.controllers.SQLiteController;
import usi.memotion2personal.local.database.db.LocalSQLiteDBHelper;
import usi.memotion2personal.local.database.tables.FatigueSurveyTable;
import usi.memotion2personal.local.database.tables.LectureSurveyTable;
import usi.memotion2personal.local.database.tables.OverallSurveyTable;
import usi.memotion2personal.local.database.tables.PAMSurveyTable;
import usi.memotion2personal.local.database.tables.ProductivitySurveyTable;
import usi.memotion2personal.local.database.tables.SleepQualityTable;
import usi.memotion2personal.local.database.tables.StressSurveyTable;
import usi.memotion2personal.local.database.tables.UserTable;
import usi.memotion2personal.local.database.tables.WeeklySurveyTable;
import usi.memotion2personal.remote.database.controllers.SwitchDriveController;
import usi.memotion2personal.remote.database.upload.Uploader;

/**
 * Created by biancastancu
 */

public class Surveys extends AppCompatActivity {

    private LocalStorageController localController;

    LocalSQLiteDBHelper dbHelper;
    SwitchDriveController switchDriveController;
    String androidID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localController = SQLiteController.getInstance(Surveys.this);
        dbHelper = new LocalSQLiteDBHelper(getApplicationContext());


        LayoutInflater inflater = (this).getLayoutInflater();

        if (getIntent().getStringExtra("type_survey") != null) {
            if (getIntent().getStringExtra("type_survey").equals(getString(R.string.early_morning))) {
                final AlertDialog.Builder builderSleep = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderFatigue = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderPAM = new AlertDialog.Builder(this);

                builderSleep.setCancelable(false);
                builderFatigue.setCancelable(false);
                builderPAM.setCancelable(false);

                builderSleep.setTitle(getString(R.string.morning_survey));
                builderFatigue.setTitle(getString(R.string.morning_survey));
                builderPAM.setTitle(getString(R.string.morning_survey));

                builderSleep.setView(inflater.inflate(R.layout.early_morning_questionnaire_sleep, null));
                builderFatigue.setView(inflater.inflate(R.layout.daily_questionnaire_fatigue, null));
                builderPAM.setView(inflater.inflate(R.layout.surveys_pam_child_layout, null));


                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getString(R.string.morning_survey));
                alertDialog.setMessage("Please answer the following questionnaires about your: sleep quality, mood and fatigue.");
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                builderSleep.create();
                                builderSleep.show();
                            }
                        });


                builderPAM.setPositiveButton("SKIP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((AlertDialog) dialogInterface).dismiss();
                        final AlertDialog dialogFatigue = builderFatigue.create();
                        dialogFatigue.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                TextView explanation = (TextView)(((AlertDialog)dialogInterface).findViewById(R.id.fatigue_explanation));
                                explanation.setText(getString(R.string.daily_fatigue_q1));
                            }
                        });
                        dialogFatigue.show();
                    }
                });
                builderFatigue.setPositiveButton("FINISH", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        RadioGroup fatigue_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.fatigue_q1);
                        ContentValues record = new ContentValues();
                        record.put(SleepQualityTable.TIMESTAMP, System.currentTimeMillis());
                        int choiceq1 = fatigue_q1.getCheckedRadioButtonId();
                        String answer1;
                        if (choiceq1 > 0) {
                            RadioButton radio1 = (RadioButton) fatigue_q1.findViewById(choiceq1);
                            answer1 = radio1.getText().toString();
                        } else {
                            answer1 = "UNKNOWN";
                        }
                        record.put(FatigueSurveyTable.QUESTION_1, answer1);

                        localController.insertRecord(FatigueSurveyTable.TABLE_FATIGUE_SURVEY, record);

                        Log.d("FATIGUE SURVEYS", "Added record: ts: " + record.get(FatigueSurveyTable.TIMESTAMP));

                        dialog.dismiss();

                        sayThankYou(getString(R.string.thank_you));

                    }
                });
                builderSleep.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Spinner early_morning_q1_hour = (Spinner) ((AlertDialog) dialog).findViewById(R.id.early_morning_q1_hour);
                        Spinner early_morning_q1_minute = (Spinner) ((AlertDialog) dialog).findViewById(R.id.early_morning_q1_minute);
                        Spinner early_morning_q2_hour = (Spinner) ((AlertDialog) dialog).findViewById(R.id.early_morning_q2_hour);
                        Spinner early_morning_q2_minute = (Spinner) ((AlertDialog) dialog).findViewById(R.id.early_morning_q2_minute);
                        RadioGroup early_morning_q3 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.early_morning_q3);
                        RadioGroup early_morning_q4 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.early_morning_q4);
                        ContentValues record = new ContentValues();
                        record.put(SleepQualityTable.TIMESTAMP, System.currentTimeMillis());
                        record.put(SleepQualityTable.QUESTION_1, early_morning_q1_hour.getSelectedItem().toString()+":"+ early_morning_q1_minute.getSelectedItem().toString());
                        record.put(SleepQualityTable.QUESTION_2, early_morning_q2_hour.getSelectedItem().toString()+":"+ early_morning_q2_minute.getSelectedItem().toString());
                        int choiceq3 = early_morning_q3.getCheckedRadioButtonId();
                        String answer3;
                        if (choiceq3 > 0) {
                            RadioButton radio3 = (RadioButton) early_morning_q3.findViewById(choiceq3);
                            answer3 = radio3.getText().toString();
                        } else {
                            answer3 = "UNKNOWN";
                        }
                        record.put(SleepQualityTable.QUESTION_3, answer3);
                        int choiceq4 = early_morning_q4.getCheckedRadioButtonId();
                        String answer4;
                        if (choiceq4 > 0) {
                            RadioButton radio4 = (RadioButton) early_morning_q4.findViewById(choiceq4);
                            answer4 = radio4.getText().toString();
                        } else {
                            answer4 = "UNKNOWN";
                        }
                        record.put(SleepQualityTable.QUESTION_4, answer4);

                        localController.insertRecord(SleepQualityTable.TABLE_SLEEP_QUALITY_SURVEY, record);

                        Log.d("SLEEP QUALITY SURVEYS", "Added record: ts: " + record.get(SleepQualityTable.TIMESTAMP));

                        dialog.dismiss();
                        final AlertDialog PAMdialog = builderPAM.create();
                        PAMdialog.show();
                        if (PAMdialog.isShowing()) {
                            PamSurveyView view = (PamSurveyView) (PAMdialog.findViewById(R.id.pamSurveyView));
                            view.setPAMExplanation(PamSurveyView.EARLY_MORNING);
                            PamSurveyView.OnPAMSelectionListener changeListener = new PamSurveyView.OnPAMSelectionListener() {
                                @Override
                                public void onPAMSelection(int position) {
                                    ContentValues record = new ContentValues();
                                    record.put(PAMSurveyTable.TIMESTAMP, System.currentTimeMillis());
                                    record.put(PAMSurveyTable.IMAGE_CHOSEN, position);
                                    localController.insertRecord(PAMSurveyTable.TABLE_PAM_SURVEY, record);
                                    Log.d("PAM SURVEYS", "Added record: ts: " + record.get(PAMSurveyTable.TIMESTAMP));
                                    PAMdialog.dismiss();
                                    final AlertDialog dialogFatigue = builderFatigue.create();
                                    dialogFatigue.setOnShowListener(new DialogInterface.OnShowListener() {
                                        @Override
                                        public void onShow(DialogInterface dialogInterface) {
                                            TextView explanation = (TextView)(((AlertDialog)dialogInterface).findViewById(R.id.fatigue_explanation));
                                            explanation.setText(getString(R.string.daily_fatigue_q1));
                                        }
                                    });
                                    dialogFatigue.show();
                                }
                            };
                            view.setOnPAMSelectionListener(changeListener);
                        }
                    }
                });

                alertDialog.create();
                alertDialog.show();

            } else if (getIntent().getStringExtra("type_survey").equals(getString(R.string.morning))) {
                final AlertDialog.Builder builderOverall = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderProductivity = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderStress = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderFatigue = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderPAM = new AlertDialog.Builder(this);

                builderOverall.setCancelable(false);
                builderProductivity.setCancelable(false);
                builderStress.setCancelable(false);
                builderFatigue.setCancelable(false);
                builderPAM.setCancelable(false);

                builderOverall.setTitle(getString(R.string.midday_survey));
                builderProductivity.setTitle(getString(R.string.midday_survey));
                builderStress.setTitle(getString(R.string.midday_survey));
                builderFatigue.setTitle(getString(R.string.midday_survey));
                builderPAM.setTitle(getString(R.string.midday_survey));

                builderOverall.setView(inflater.inflate(R.layout.morning_questionnaire_overall, null));
                builderProductivity.setView(inflater.inflate(R.layout.morning_productivity_survey, null));
                builderStress.setView(inflater.inflate(R.layout.morning_questionnaire_stress, null));
                builderFatigue.setView(inflater.inflate(R.layout.daily_questionnaire_fatigue, null));
                builderPAM.setView(inflater.inflate(R.layout.surveys_pam_child_layout, null));

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getString(R.string.midday_survey));
                alertDialog.setMessage("Please answer the following questionnaires about your: ovverall well-being, mood, productivity, stress and fatigue during this afternoon.");
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                builderOverall.create();
                                builderOverall.show();
                            }
                        });



                builderOverall.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        RadioGroup morning_overall_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.morning_overall_q1);
                        ContentValues record = new ContentValues();
                        record.put(SleepQualityTable.TIMESTAMP, System.currentTimeMillis());
                        int choiceq1 = morning_overall_q1.getCheckedRadioButtonId();
                        String answer1;
                        if (choiceq1 > 0) {
                            RadioButton radio1 = (RadioButton) morning_overall_q1.findViewById(choiceq1);
                            answer1 = radio1.getText().toString();
                        } else {
                            answer1 = "UNKNOWN";
                        }
                        record.put(OverallSurveyTable.QUESTION_1, answer1);

                        localController.insertRecord(OverallSurveyTable.TABLE_OVERALL_SURVEY, record);

                        Log.d("OVERALL SURVEYS", "Added record: ts: " + record.get(OverallSurveyTable.TIMESTAMP));

                        dialog.dismiss();

                        builderProductivity.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                RadioGroup productivity_morning_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.productivity_morning_q1);
                                RadioGroup productivity_morning_q2 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.productivity_morning_q2);
                                EditText productivity_morning_q3 = (EditText) ((AlertDialog) dialog).findViewById(R.id.productivity_morning_q3);
                                EditText productivity_morning_q4 = (EditText) ((AlertDialog) dialog).findViewById(R.id.productivity_morning_q4);

                                ContentValues record = new ContentValues();
                                record.put(ProductivitySurveyTable.TIMESTAMP, System.currentTimeMillis());
                                int choiceq1 = productivity_morning_q1.getCheckedRadioButtonId();
                                String answer1;
                                if (choiceq1 > 0) {
                                    RadioButton radio1 = (RadioButton) productivity_morning_q1.findViewById(choiceq1);
                                    answer1 = radio1.getText().toString();
                                } else {
                                    answer1 = "UNKNOWN";
                                }
                                record.put(ProductivitySurveyTable.QUESTION_1, answer1);
                                int choiceq2 = productivity_morning_q2.getCheckedRadioButtonId();
                                String answer2;
                                if (choiceq2 > 0) {
                                    RadioButton radio2 = (RadioButton) productivity_morning_q2.findViewById(choiceq2);
                                    answer2 = radio2.getText().toString();
                                } else {
                                    answer2 = "UNKNOWN";
                                }
                                record.put(ProductivitySurveyTable.QUESTION_2, answer2);
                                record.put(ProductivitySurveyTable.QUESTION_3, productivity_morning_q3.getText().toString());
                                record.put(ProductivitySurveyTable.QUESTION_3, productivity_morning_q4.getText().toString());

                                localController.insertRecord(ProductivitySurveyTable.TABLE_PRODUCTIVITY_SURVEY, record);

                                Log.d("PRODUCTIVITY SURVEYS", "Added record: ts: " + record.get(ProductivitySurveyTable.TIMESTAMP));

                                dialog.dismiss();

                                builderStress.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        RadioGroup stress_morning_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.stress_morning_q1);
                                        ContentValues record = new ContentValues();
                                        record.put(StressSurveyTable.TIMESTAMP, System.currentTimeMillis());
                                        int choiceq1 = stress_morning_q1.getCheckedRadioButtonId();
                                        String answer1;
                                        if (choiceq1 > 0) {
                                            RadioButton radio1 = (RadioButton) stress_morning_q1.findViewById(choiceq1);
                                            answer1 = radio1.getText().toString();
                                        } else {
                                            answer1 = "UNKNOWN";
                                        }
                                        record.put(StressSurveyTable.QUESTION_1, answer1);

                                        localController.insertRecord(StressSurveyTable.TABLE_STRESS_SURVEY, record);

                                        Log.d("STRESS SURVEYS", "Added record: ts: " + record.get(StressSurveyTable.TIMESTAMP));

                                        dialog.dismiss();

                                        builderFatigue.setPositiveButton("FINISH", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, int id) {
                                                RadioGroup fatigue_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.fatigue_q1);
                                                ContentValues record = new ContentValues();
                                                record.put(SleepQualityTable.TIMESTAMP, System.currentTimeMillis());
                                                int choiceq1 = fatigue_q1.getCheckedRadioButtonId();
                                                String answer1;
                                                if (choiceq1 > 0) {
                                                    RadioButton radio1 = (RadioButton) fatigue_q1.findViewById(choiceq1);
                                                    answer1 = radio1.getText().toString();
                                                } else {
                                                    answer1 = "UNKNOWN";
                                                }
                                                record.put(FatigueSurveyTable.QUESTION_1, answer1);

                                                localController.insertRecord(FatigueSurveyTable.TABLE_FATIGUE_SURVEY, record);

                                                Log.d("FATIGUE SURVEYS", "Added record: ts: " + record.get(FatigueSurveyTable.TIMESTAMP));

                                                dialog.dismiss();

                                                sayThankYou(getString(R.string.thank_you));
                                            }
                                        });
                                        final AlertDialog dialogFatigue = builderFatigue.create();
                                        dialogFatigue.setOnShowListener(new DialogInterface.OnShowListener() {
                                            @Override
                                            public void onShow(DialogInterface dialogInterface) {
                                                TextView explanation = (TextView)(((AlertDialog)dialogInterface).findViewById(R.id.fatigue_explanation));
                                                explanation.setText(getString(R.string.daily_fatigue_q1_morning));
                                            }
                                        });
                                        dialogFatigue.show();
                                    }
                                });
                                builderStress.create();
                                builderStress.show();
                            }
                        });

                        builderPAM.setPositiveButton("SKIP", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((AlertDialog) dialogInterface).dismiss();
                                builderProductivity.create();
                                builderProductivity.show();
                            }
                        });

                        final AlertDialog PAMdialog = builderPAM.create();
                        PAMdialog.show();
                        if (PAMdialog.isShowing()) {
                            PamSurveyView view = (PamSurveyView) (PAMdialog.findViewById(R.id.pamSurveyView));
                            view.setPAMExplanation(PamSurveyView.MORNING);
                            PamSurveyView.OnPAMSelectionListener changeListener = new PamSurveyView.OnPAMSelectionListener() {
                                @Override
                                public void onPAMSelection(int position) {
                                    ContentValues record = new ContentValues();
                                    record.put(PAMSurveyTable.TIMESTAMP, System.currentTimeMillis());
                                    record.put(PAMSurveyTable.IMAGE_CHOSEN, position);
                                    localController.insertRecord(PAMSurveyTable.TABLE_PAM_SURVEY, record);
                                    Log.d("PAM SURVEYS", "Added record: ts: " + record.get(PAMSurveyTable.TIMESTAMP));
                                    PAMdialog.dismiss();
                                    builderProductivity.create();
                                    builderProductivity.show();
                                }
                            };
                            view.setOnPAMSelectionListener(changeListener);
                        }
                    }
                });
                alertDialog.create();
                alertDialog.show();

            } else if (getIntent().getStringExtra("type_survey").equals(getString(R.string.afternoon))) {
                final AlertDialog.Builder builderOverall = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderProductivity = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderStress = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderFatigue = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderPAM = new AlertDialog.Builder(this);

                builderOverall.setCancelable(false);
                builderProductivity.setCancelable(false);
                builderStress.setCancelable(false);
                builderFatigue.setCancelable(false);
                builderPAM.setCancelable(false);

                builderOverall.setTitle(getString(R.string.afternoon_survey));
                builderProductivity.setTitle(getString(R.string.afternoon_survey));
                builderStress.setTitle(getString(R.string.afternoon_survey));
                builderFatigue.setTitle(getString(R.string.afternoon_survey));
                builderPAM.setTitle(getString(R.string.afternoon_survey));

                builderOverall.setView(inflater.inflate(R.layout.afternoon_questionnaire_overall, null));
                builderProductivity.setView(inflater.inflate(R.layout.afternoon_productivity_survey, null));
                builderStress.setView(inflater.inflate(R.layout.afternoon_questionnaire_stress, null));
                builderFatigue.setView(inflater.inflate(R.layout.daily_questionnaire_fatigue, null));
                builderPAM.setView(inflater.inflate(R.layout.surveys_pam_child_layout, null));

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getString(R.string.afternoon_survey));
                alertDialog.setMessage("Please answer the following questionnaires about your: ovverall well-being, mood, productivity, stress and fatigue during this afternoon.");
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                builderOverall.create();
                                builderOverall.show();
                            }
                        });

                builderOverall.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        RadioGroup afternoon_overall_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.afternoon_overall_q1);
                        ContentValues record = new ContentValues();
                        record.put(SleepQualityTable.TIMESTAMP, System.currentTimeMillis());
                        int choiceq1 = afternoon_overall_q1.getCheckedRadioButtonId();
                        String answer1;
                        if (choiceq1 > 0) {
                            RadioButton radio1 = (RadioButton) afternoon_overall_q1.findViewById(choiceq1);
                            answer1 = radio1.getText().toString();
                        } else {
                            answer1 = "UNKNOWN";
                        }
                        record.put(OverallSurveyTable.QUESTION_1, answer1);

                        localController.insertRecord(OverallSurveyTable.TABLE_OVERALL_SURVEY, record);

                        Log.d("OVERALL SURVEYS", "Added record: ts: " + record.get(OverallSurveyTable.TIMESTAMP));

                        dialog.dismiss();

                        builderProductivity.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                RadioGroup productivity_afternoon_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.productivity_afternoon_q1);
                                RadioGroup productivity_afternoon_q2 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.productivity_afternoon_q2);
                                EditText productivity_afternoon_q3 = (EditText) ((AlertDialog) dialog).findViewById(R.id.productivity_afternoon_q3);
                                EditText productivity_afternoon_q4 = (EditText) ((AlertDialog) dialog).findViewById(R.id.productivity_afternoon_q4);

                                ContentValues record = new ContentValues();
                                record.put(ProductivitySurveyTable.TIMESTAMP, System.currentTimeMillis());
                                int choiceq1 = productivity_afternoon_q1.getCheckedRadioButtonId();
                                String answer1;
                                if (choiceq1 > 0) {
                                    RadioButton radio1 = (RadioButton) productivity_afternoon_q1.findViewById(choiceq1);
                                    answer1 = radio1.getText().toString();
                                } else {
                                    answer1 = "UNKNOWN";
                                }
                                record.put(ProductivitySurveyTable.QUESTION_1, answer1);
                                int choiceq2 = productivity_afternoon_q2.getCheckedRadioButtonId();
                                String answer2;
                                if (choiceq2 > 0) {
                                    RadioButton radio2 = (RadioButton) productivity_afternoon_q2.findViewById(choiceq2);
                                    answer2 = radio2.getText().toString();
                                } else {
                                    answer2 = "UNKNOWN";
                                }
                                record.put(ProductivitySurveyTable.QUESTION_2, answer2);
                                record.put(ProductivitySurveyTable.QUESTION_3, productivity_afternoon_q3.getText().toString());
                                record.put(ProductivitySurveyTable.QUESTION_3, productivity_afternoon_q4.getText().toString());

                                localController.insertRecord(ProductivitySurveyTable.TABLE_PRODUCTIVITY_SURVEY, record);

                                Log.d("PRODUCTIVITY SURVEYS", "Added record: ts: " + record.get(ProductivitySurveyTable.TIMESTAMP));

                                dialog.dismiss();

                                builderStress.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        RadioGroup stress_afternoon_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.stress_afternoon_q1);
                                        ContentValues record = new ContentValues();
                                        record.put(StressSurveyTable.TIMESTAMP, System.currentTimeMillis());
                                        int choiceq1 = stress_afternoon_q1.getCheckedRadioButtonId();
                                        String answer1;
                                        if (choiceq1 > 0) {
                                            RadioButton radio1 = (RadioButton) stress_afternoon_q1.findViewById(choiceq1);
                                            answer1 = radio1.getText().toString();
                                        } else {
                                            answer1 = "UNKNOWN";
                                        }
                                        record.put(StressSurveyTable.QUESTION_1, answer1);

                                        localController.insertRecord(StressSurveyTable.TABLE_STRESS_SURVEY, record);

                                        Log.d("STRESS SURVEYS", "Added record: ts: " + record.get(StressSurveyTable.TIMESTAMP));

                                        dialog.dismiss();

                                        builderFatigue.setPositiveButton("FINISH", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                RadioGroup fatigue_q1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.fatigue_q1);
                                                ContentValues record = new ContentValues();
                                                record.put(SleepQualityTable.TIMESTAMP, System.currentTimeMillis());
                                                int choiceq1 = fatigue_q1.getCheckedRadioButtonId();
                                                String answer1;
                                                if (choiceq1 > 0) {
                                                    RadioButton radio1 = (RadioButton) fatigue_q1.findViewById(choiceq1);
                                                    answer1 = radio1.getText().toString();
                                                } else {
                                                    answer1 = "UNKNOWN";
                                                }
                                                record.put(FatigueSurveyTable.QUESTION_1, answer1);

                                                localController.insertRecord(FatigueSurveyTable.TABLE_FATIGUE_SURVEY, record);

                                                Log.d("FATIGUE SURVEYS", "Added record: ts: " + record.get(FatigueSurveyTable.TIMESTAMP));

                                                dialog.dismiss();
                                                sayThankYou(getString(R.string.thank_you));
                                            }
                                        });
                                        final AlertDialog dialogFatigue = builderFatigue.create();
                                        dialogFatigue.setOnShowListener(new DialogInterface.OnShowListener() {
                                            @Override
                                            public void onShow(DialogInterface dialogInterface) {
                                                TextView explanation = (TextView)(((AlertDialog)dialogInterface).findViewById(R.id.fatigue_explanation));
                                                explanation.setText(getString(R.string.daily_fatigue_q1_afternoon));
                                            }
                                        });
                                        dialogFatigue.show();
                                    }
                                });
                                builderStress.create();
                                builderStress.show();
                            }
                        });

                        builderPAM.setPositiveButton("SKIP", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((AlertDialog) dialogInterface).dismiss();
                                builderProductivity.create();
                                builderProductivity.show();
                            }
                        });

                        final AlertDialog PAMdialog = builderPAM.create();
                        PAMdialog.show();
                        if (PAMdialog.isShowing()) {
                            PamSurveyView view = (PamSurveyView) (PAMdialog.findViewById(R.id.pamSurveyView));
                            view.setPAMExplanation(PamSurveyView.AFTERNOON);
                            PamSurveyView.OnPAMSelectionListener changeListener = new PamSurveyView.OnPAMSelectionListener() {
                                @Override
                                public void onPAMSelection(int position) {
                                    ContentValues record = new ContentValues();
                                    record.put(PAMSurveyTable.TIMESTAMP, System.currentTimeMillis());
                                    record.put(PAMSurveyTable.IMAGE_CHOSEN, position);
                                    localController.insertRecord(PAMSurveyTable.TABLE_PAM_SURVEY, record);
                                    Log.d("PAM SURVEYS", "Added record: ts: " + record.get(PAMSurveyTable.TIMESTAMP));
                                    PAMdialog.dismiss();
                                    builderProductivity.create();
                                    builderProductivity.show();
                                }
                            };
                            view.setOnPAMSelectionListener(changeListener);
                        }
                    }
                });
                alertDialog.create();
                alertDialog.show();
            } else if (getIntent().getStringExtra("type_survey").equals(getString(R.string.weekly))) {
                final AlertDialog.Builder builderWeekly = new AlertDialog.Builder(this);
                builderWeekly.setCancelable(false);
                builderWeekly.setTitle(getString(R.string.weekly_survey));
                builderWeekly.setView(inflater.inflate(R.layout.weekly_questionnaire, null));
                builderWeekly.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ContentValues record = new ContentValues();
                        record.put(WeeklySurveyTable.TIMESTAMP, System.currentTimeMillis());

                        RadioGroup weeklyq1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq1);
                        RadioGroup weeklyq2 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq2);
                        RadioGroup weeklyq3 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq3);
                        RadioGroup weeklyq4 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq4);
                        RadioGroup weeklyq5 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq5);
                        RadioGroup weeklyq6 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq6);
                        RadioGroup weeklyq7 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq7);
                        RadioGroup weeklyq8 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq8);
                        RadioGroup weeklyq9 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq9);
                        RadioGroup weeklyq10 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq10);
                        RadioGroup weeklyq11 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq11);
                        RadioGroup weeklyq12 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq12);
                        RadioGroup weeklyq13 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq13);
                        RadioGroup weeklyq14 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.weeklyq14);

                        record.put(WeeklySurveyTable.QUESTION_1, getAnswer(weeklyq1));
                        record.put(WeeklySurveyTable.QUESTION_2, getAnswer(weeklyq2));
                        record.put(WeeklySurveyTable.QUESTION_3, getAnswer(weeklyq3));
                        record.put(WeeklySurveyTable.QUESTION_4, getAnswer(weeklyq4));
                        record.put(WeeklySurveyTable.QUESTION_5, getAnswer(weeklyq5));
                        record.put(WeeklySurveyTable.QUESTION_6, getAnswer(weeklyq6));
                        record.put(WeeklySurveyTable.QUESTION_7, getAnswer(weeklyq7));
                        record.put(WeeklySurveyTable.QUESTION_8, getAnswer(weeklyq8));
                        record.put(WeeklySurveyTable.QUESTION_9, getAnswer(weeklyq9));
                        record.put(WeeklySurveyTable.QUESTION_10, getAnswer(weeklyq10));
                        record.put(WeeklySurveyTable.QUESTION_11, getAnswer(weeklyq11));
                        record.put(WeeklySurveyTable.QUESTION_12, getAnswer(weeklyq12));
                        record.put(WeeklySurveyTable.QUESTION_13, getAnswer(weeklyq13));
                        record.put(WeeklySurveyTable.QUESTION_14, getAnswer(weeklyq14));

                        localController.insertRecord(WeeklySurveyTable.TABLE_WEEKLY_SURVEY, record);


                        Log.d("WEEKLY SURVEYS", "Added record: ts: " + record.get(WeeklySurveyTable.TIMESTAMP));

                        //Upload weekly survey
                        uploadRemotely();

                        dialog.dismiss();
                        sayThankYou(getString(R.string.weekly_thank_you));
                    }
                });
                builderWeekly.create();
                builderWeekly.show();
            } else if (getIntent().getStringExtra("type_survey").equals("Wednesday - Break") ||
                    getIntent().getStringExtra("type_survey").equals("Wednesday - Post") ||
                    getIntent().getStringExtra("type_survey").equals("Friday - Break") ||
                    getIntent().getStringExtra("type_survey").equals("Friday - Post")) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderPAM = new AlertDialog.Builder(this);
                final AlertDialog.Builder builderWeekly = new AlertDialog.Builder(this);

                builderPAM.setCancelable(false);
                builderWeekly.setCancelable(false);

                alertDialog.setTitle(getString(R.string.lecture_survey));
                builderPAM.setTitle(getString(R.string.PAM));
                builderWeekly.setTitle(getString(R.string.lecture_survey));

                builderPAM.setView(inflater.inflate(R.layout.surveys_pam_child_layout, null));
                builderWeekly.setView(inflater.inflate(R.layout.lecture_surveys_view, null));

                alertDialog.setMessage("Did you attend today's Mobile Computing lecture?");
                alertDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                final AlertDialog PAMdialog = builderPAM.create();
                                PAMdialog.show();
                                if (PAMdialog.isShowing()) {
                                    PamSurveyView view = (PamSurveyView) (PAMdialog.findViewById(R.id.pamSurveyView));
                                    view.setPAMExplanation(PamSurveyView.LECTURE);

                                    PamSurveyView.OnPAMSelectionListener changeListener = new PamSurveyView.OnPAMSelectionListener() {
                                        @Override
                                        public void onPAMSelection(int position) {
                                            ContentValues record = new ContentValues();
                                            record.put(PAMSurveyTable.TIMESTAMP, System.currentTimeMillis());
                                            record.put(PAMSurveyTable.IMAGE_CHOSEN, position);
                                            localController.insertRecord(PAMSurveyTable.TABLE_PAM_SURVEY, record);
                                            Log.d("PAM SURVEYS", "Added record: ts: " + record.get(PAMSurveyTable.TIMESTAMP));
                                            PAMdialog.dismiss();

                                            builderWeekly.create();
                                            builderWeekly.show();
                                        }
                                    };
                                    view.setOnPAMSelectionListener(changeListener);
                                }
                            }
                        });


                alertDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(), "Thank you very much for your answer!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                alertDialog.create();
                alertDialog.show();

                builderPAM.setPositiveButton("SKIP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((AlertDialog) dialogInterface).dismiss();
                        builderWeekly.create();
                        builderWeekly.show();
                    }
                });

                builderWeekly.setPositiveButton("FINISH", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ContentValues record = new ContentValues();
                        record.put(LectureSurveyTable.TIMESTAMP, System.currentTimeMillis());

                        RadioGroup weeklyq1 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.question1Options);
                        RadioGroup weeklyq2 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.question2Options);
                        RadioGroup weeklyq3 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.question3Options);
                        RadioGroup weeklyq4 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.question4Options);
                        RadioGroup weeklyq5 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.question5Options);
                        RadioGroup weeklyq6 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.question6Options);
                        RadioGroup weeklyq7 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.question7Options);
                        RadioGroup weeklyq8 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.question8Options);
                        RadioGroup weeklyq9 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.question9Options);
                        RadioGroup weeklyq10 = (RadioGroup) ((AlertDialog) dialog).findViewById(R.id.question10Options);

                        record.put(LectureSurveyTable.QUESTION_1, getAnswer(weeklyq1));
                        record.put(LectureSurveyTable.QUESTION_2, getAnswer(weeklyq2));
                        record.put(LectureSurveyTable.QUESTION_3, getAnswer(weeklyq3));
                        record.put(LectureSurveyTable.QUESTION_4, getAnswer(weeklyq4));
                        record.put(LectureSurveyTable.QUESTION_5, getAnswer(weeklyq5));
                        record.put(LectureSurveyTable.QUESTION_6, getAnswer(weeklyq6));
                        record.put(LectureSurveyTable.QUESTION_7, getAnswer(weeklyq7));
                        record.put(LectureSurveyTable.QUESTION_8, getAnswer(weeklyq8));
                        record.put(LectureSurveyTable.QUESTION_9, getAnswer(weeklyq9));
                        record.put(LectureSurveyTable.QUESTION_10, getAnswer(weeklyq10));

                        localController.insertRecord(LectureSurveyTable.TABLE_LECTURE_SURVEY, record);

                        Log.d("LECTURE SURVEYS", "Added record: ts: " + record.get(LectureSurveyTable.TIMESTAMP));

                        dialog.dismiss();
                        sayThankYou(getString(R.string.weekly_thank_you));
                    }
                });


            }  else if (getIntent().getStringExtra("type_survey").equals("Friday - Pre") ||
                    getIntent().getStringExtra("type_survey").equals("Wednesday - Pre") ) {

                final AlertDialog.Builder builderPAM = new AlertDialog.Builder(this);
                builderPAM.setCancelable(false);
                builderPAM.setTitle(getString(R.string.PAM));

                builderPAM.setView(inflater.inflate(R.layout.surveys_pam_child_layout, null));


                builderPAM.setPositiveButton("SKIP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((AlertDialog) dialogInterface).dismiss();
                    }
                });

                final AlertDialog PAMdialog = builderPAM.create();
                PAMdialog.show();
                if (PAMdialog.isShowing()) {
                    PamSurveyView view = (PamSurveyView) (PAMdialog.findViewById(R.id.pamSurveyView));
                    view.setPAMExplanation(PamSurveyView.LECTURE);
                    PamSurveyView.OnPAMSelectionListener changeListener = new PamSurveyView.OnPAMSelectionListener() {
                        @Override
                        public void onPAMSelection(int position) {
                            ContentValues record = new ContentValues();
                            record.put(PAMSurveyTable.TIMESTAMP, System.currentTimeMillis());
                            record.put(PAMSurveyTable.IMAGE_CHOSEN, position);
                            localController.insertRecord(PAMSurveyTable.TABLE_PAM_SURVEY, record);
                            Log.d("PAM SURVEYS", "Added record: ts: " + record.get(PAMSurveyTable.TIMESTAMP));
                            PAMdialog.dismiss();

                            sayThankYou(getString(R.string.thank_you));
                        }
                    };
                    view.setOnPAMSelectionListener(changeListener);
                }

            }else {
                Surveys.this.finish();
            }
        }
    }

    private void sayThankYou (String message) {
            AlertDialog.Builder builderThankYou = new AlertDialog.Builder(Surveys.this);
            builderThankYou.setMessage(message);
            builderThankYou.setCancelable(false);
            builderThankYou.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Surveys.this.finish();
                }
            });
            builderThankYou.create();
            builderThankYou.show();
        }

    private String getAnswer(RadioGroup radioGroup){
        int choice = radioGroup.getCheckedRadioButtonId();
        if (choice > 0) {
            RadioButton radio = (RadioButton) radioGroup.findViewById(choice);
            return radio.getText().toString();
        } else {
            return "UNKNOWN";
        }
    }

    public void uploadRemotely() {
        androidID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        dbHelper = new LocalSQLiteDBHelper(getApplicationContext());
        switchDriveController = new SwitchDriveController(getApplicationContext().getString(R.string.server_address),
                getApplicationContext().getString(R.string.token),AccountUtils.getPassword(getApplicationContext()));
        localController = SQLiteController.getInstance(getApplicationContext());


        String query = "SELECT * FROM usersTable";
        Cursor records = localController.rawQuery(query, null);
        records.moveToFirst();

        String username = null;

        if (records.getCount() > 0) {
            username = records.getString(records.getColumnIndex(UserTable.USERNAME));
        }

        String userName = username + "_" + androidID;
        final Uploader uploader = new Uploader(userName, switchDriveController, localController, dbHelper);
        uploader.weeklyUpload();
    }
}