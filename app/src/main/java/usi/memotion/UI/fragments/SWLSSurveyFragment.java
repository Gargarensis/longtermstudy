package usi.memotion.UI.fragments;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import usi.memotion.R;
import usi.memotion.local.database.controllers.LocalStorageController;
import usi.memotion.local.database.controllers.SQLiteController;
import usi.memotion.local.database.db.LocalSQLiteDBHelper;
import usi.memotion.local.database.tables.PersonalitySurveyTable;
import usi.memotion.local.database.tables.SWLSSurveyTable;
import usi.memotion.local.database.tables.UserTable;
import usi.memotion.remote.database.controllers.SwitchDriveController;
import usi.memotion.remote.database.upload.Uploader;

/**
 * A simple {@link Fragment} subclass.
 */
public class SWLSSurveyFragment extends Fragment {

    private Spinner question1Options, question2Options, question3Options, question4Options, question5Options;

    LocalSQLiteDBHelper dbHelper;
    private LocalStorageController localController;
    SwitchDriveController switchDriveController;
    String androidID;

    private Button submitButton;

    private ImageButton information;

    private LocalStorageController localcontroller;

    public SWLSSurveyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_swls_survey, container, false);

        dbHelper = new LocalSQLiteDBHelper(getContext());
        localController = SQLiteController.getInstance(getContext());

        question1Options = (Spinner) root.findViewById(R.id.swls_q1_options);
        question2Options = (Spinner) root.findViewById(R.id.swls_q2_options);
        question3Options = (Spinner) root.findViewById(R.id.swls_q3_options);
        question4Options = (Spinner) root.findViewById(R.id.swls_q4_options);
        question5Options = (Spinner) root.findViewById(R.id.swls_q5_options);

        localcontroller = SQLiteController.getInstance(getContext());

        submitButton = (Button) root.findViewById(R.id.submit_swls);

        information = (ImageButton) root.findViewById(R.id.info_SWLS);

        Spinner[] spinners = {question1Options, question2Options, question3Options, question4Options, question5Options};

        for (Spinner s : spinners) {
            setupSpinnerOptions(s);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateSpinners()) {
                    ContentValues record = new ContentValues();
                    record.put(SWLSSurveyTable.TIMESTAMP, System.currentTimeMillis());
                    record.put(SWLSSurveyTable.QUESTION_1, Integer.parseInt(question1Options.getSelectedItem().toString()));
                    record.put(SWLSSurveyTable.QUESTION_2, Integer.parseInt(question2Options.getSelectedItem().toString()));
                    record.put(SWLSSurveyTable.QUESTION_3, Integer.parseInt(question3Options.getSelectedItem().toString()));
                    record.put(SWLSSurveyTable.QUESTION_4, Integer.parseInt(question4Options.getSelectedItem().toString()));
                    record.put(SWLSSurveyTable.QUESTION_5, Integer.parseInt(question5Options.getSelectedItem().toString()));
                    localcontroller.insertRecord(SWLSSurveyTable.TABLE_SWLSS_SURVEY, record);

                    Log.d("SWLSS SURVEYS", "Added record: ts: " + record.get(SWLSSurveyTable.TIMESTAMP));


                    //Show thank you message
                    Toast.makeText(getContext(), "Thank you very much!", Toast.LENGTH_SHORT).show();

                    Fragment newFragment = new PSSSurveyFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, newFragment);
                    ft.commit();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.fill_answers);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.TIPI_scale_title);
                builder.setMessage(R.string.TIPI_scale);
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
                R.array.tipi_questions_choices, android.R.layout.simple_spinner_item);

        tipiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(tipiAdapter);
    }

    private boolean validateSpinners() {
        boolean valid1 = isSpinnerValid(question1Options);
        boolean valid2 = isSpinnerValid(question2Options);
        boolean valid3 = isSpinnerValid(question3Options);
        boolean valid4 = isSpinnerValid(question4Options);
        boolean valid5 = isSpinnerValid(question5Options);
        return valid1 && valid2 && valid3 && valid4 && valid5;
    }

        private boolean isSpinnerValid(Spinner spinner){
            int selected = spinner.getSelectedItemPosition();
            TextView text = (TextView)spinner.getSelectedView();
            if(((String)spinner.getItemAtPosition(selected)).equals("Select")){
                text.setError("error");
                return false;
            } else {
                text.setError(null);
                return true;
            }
        }

}


