package usi.memotion2personal.UI.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import usi.memotion2personal.R;
import usi.memotion2personal.local.database.controllers.LocalStorageController;
import usi.memotion2personal.local.database.controllers.SQLiteController;
import usi.memotion2personal.local.database.db.LocalSQLiteDBHelper;
import usi.memotion2personal.local.database.tables.UserTable;

/**
 * Created by shkurtagashi
 */


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    TextView username, empaticaId, age, gender, status, email, switchtoken, switchpassword;

    LocalSQLiteDBHelper dbHelper;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_profile, container, false);
        dbHelper = new LocalSQLiteDBHelper(getContext());

        username = (TextView) rootview.findViewById(R.id.usernameValue);
        age = (TextView)rootview.findViewById(R.id.ageValue);
        gender = (TextView)rootview.findViewById(R.id.genderValue);
        status = (TextView) rootview.findViewById(R.id.statusValue);


        LocalStorageController localController = SQLiteController.getInstance(getContext());;
        String query = "SELECT * FROM usersTable";

        Cursor records = localController.rawQuery(query, null);
        records.moveToFirst();

        username.setText(records.getString(records.getColumnIndex(UserTable.USERNAME)));
        age.setText(records.getString(records.getColumnIndex(UserTable.COLUMN_AGE)));
        gender.setText(records.getString(records.getColumnIndex(UserTable.COLUMN_GENDER)));
        status.setText(records.getString(records.getColumnIndex(UserTable.COLUMN_STATUS)));

        records.close();

        return rootview;
    }

}
