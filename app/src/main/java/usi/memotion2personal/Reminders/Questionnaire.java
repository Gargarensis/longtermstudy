package usi.memotion2personal.Reminders;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


import usi.memotion2personal.MainActivity;
import usi.memotion2personal.R;

/**
 * Created by biancastancu
 */

public class Questionnaire extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Lecture Survey Time");
        alertDialog.setMessage("Did you attend today's Mobile Computing lecture?");
        alertDialog.setIcon(R.drawable.account);

        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String session = getIntent().getStringExtra("LectureSession");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("fragmentChoice", "lectureSurveys");
                        intent.putExtra("LectureSession", session);
                        startActivity(intent);
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
        alertDialog.show();
    }
}
