package usi.memotion2personal.UI.fragments;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import usi.memotion2personal.R;
import usi.memotion2personal.local.database.controllers.LocalStorageController;
import usi.memotion2personal.local.database.controllers.SQLiteController;
import usi.memotion2personal.local.database.db.LocalSQLiteDBHelper;
import usi.memotion2personal.local.database.tables.PSSSurveyTable;
import usi.memotion2personal.remote.database.controllers.SwitchDriveController;

/**
 * Created by biancastancu
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class PSSSurveyFragment extends Fragment {

    LocalSQLiteDBHelper dbHelper;
    SwitchDriveController switchDriveController;
    String androidID;
    private Spinner question1Options, question2Options, question3Options, question4Options, question5Options, question6Options,
            question7Options, question8Options, question9Options, question10Options;
    private LocalStorageController localController;
    private Button submitButton;

    private ImageButton information;

    private LocalStorageController localcontroller;

    public PSSSurveyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_pss_survey, container, false);

        question1Options = (Spinner) root.findViewById(R.id.pss_q1_options);
        question2Options = (Spinner) root.findViewById(R.id.pss_q2_options);
        question3Options = (Spinner) root.findViewById(R.id.pss_q3_options);
        question4Options = (Spinner) root.findViewById(R.id.pss_q4_options);
        question5Options = (Spinner) root.findViewById(R.id.pss_q5_options);
        question6Options = (Spinner) root.findViewById(R.id.pss_q6_options);
        question7Options = (Spinner) root.findViewById(R.id.pss_q7_options);
        question8Options = (Spinner) root.findViewById(R.id.pss_q8_options);
        question9Options = (Spinner) root.findViewById(R.id.pss_q9_options);
        question10Options = (Spinner) root.findViewById(R.id.pss_q10_options);

        localcontroller = SQLiteController.getInstance(getContext());

        submitButton = (Button) root.findViewById(R.id.submit_pss);

        information = (ImageButton) root.findViewById(R.id.info_PSS);

        Spinner[] spinners = {question1Options, question2Options, question3Options, question4Options,
                question5Options, question6Options, question7Options, question8Options, question9Options, question10Options};

        for (Spinner s : spinners) {
            setupSpinnerOptions(s);
        }


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues record = new ContentValues();
                record.put(PSSSurveyTable.TIMESTAMP, System.currentTimeMillis());
                record.put(PSSSurveyTable.QUESTION_1, question1Options.getSelectedItem().toString());
                record.put(PSSSurveyTable.QUESTION_2, question2Options.getSelectedItem().toString());
                record.put(PSSSurveyTable.QUESTION_3, question3Options.getSelectedItem().toString());
                record.put(PSSSurveyTable.QUESTION_4, question4Options.getSelectedItem().toString());
                record.put(PSSSurveyTable.QUESTION_5, question5Options.getSelectedItem().toString());
                record.put(PSSSurveyTable.QUESTION_6, question6Options.getSelectedItem().toString());
                record.put(PSSSurveyTable.QUESTION_7, question7Options.getSelectedItem().toString());
                record.put(PSSSurveyTable.QUESTION_8, question8Options.getSelectedItem().toString());
                record.put(PSSSurveyTable.QUESTION_9, question9Options.getSelectedItem().toString());
                record.put(PSSSurveyTable.QUESTION_10,question10Options.getSelectedItem().toString());

                localcontroller.insertRecord(PSSSurveyTable.TABLE_PSS_SURVEY, record);

                Log.d("PSS SURVEYS", "Added record: ts: " + record.get(PSSSurveyTable.TIMESTAMP));

                Fragment newFragment = new PSQISurveyFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, newFragment);
                ft.commit();
            }
        });

        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.PSS_scale_title);
                builder.setMessage(R.string.PSS_scale);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return root;
    }

    /*
     * Questions spinner values
     */
    private void setupSpinnerOptions(Spinner s) {
        ArrayAdapter<CharSequence> tipiAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pss_questions_choices, android.R.layout.simple_spinner_item);

        tipiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(tipiAdapter);
    }
}


