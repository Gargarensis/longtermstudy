package usi.memotion2personal;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import usi.memotion2personal.Reminders.FinalScheduler;
import usi.memotion2personal.UI.fragments.AboutApplicationFragment;
import usi.memotion2personal.UI.fragments.AboutFragment;
import usi.memotion2personal.UI.fragments.EdiaryFragment;
import usi.memotion2personal.UI.fragments.HomeFragment;
import usi.memotion2personal.UI.fragments.LectureSurveysFragment;
import usi.memotion2personal.UI.fragments.ProfileFragment;
import usi.memotion2personal.UI.views.RegistrationView;
import usi.memotion2personal.gathering.GatheringSystem;
import usi.memotion2personal.gathering.SensorType;
import usi.memotion2personal.gathering.gatheringServices.Notifications.Utils.SharedPref;
import usi.memotion2personal.local.database.controllers.LocalStorageController;
import usi.memotion2personal.local.database.controllers.SQLiteController;
import usi.memotion2personal.remote.database.upload.UploadAlarmReceiver;

/**
 * Created by shkurtagashi
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ENABLED_USAGE_LISTENERS = "enabled_usage_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    private final int PERMISSION_REQUEST_STATUS = 0;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    Calendar calendar;
    String weekday;
    SimpleDateFormat dayFormat;
    int month;
    int dayOfMonth;
    FinalScheduler scheduler;
    ExpandableRelativeLayout expandableLayout1, expandableLayout2;
    private SharedPref sp;
    private AlertDialog enableNotificationListenerAlertDialog;
    private AlertDialog enableUsageServiceAlertDialog;
    private GatheringSystem gSys;
    private boolean viewIsAtHome;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displayView(R.id.nav_home);

        this.sp = new SharedPref(getApplicationContext());

        triggerReminders();

        if (!checkAndroidID()) {
            AlertDialog.Builder newAccountBuilder = new AlertDialog.Builder(MainActivity.this);
            newAccountBuilder.setTitle("Please create an account");
            newAccountBuilder.setCancelable(false);
            newAccountBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    displayView(R.id.nav_register);
                }
            });
            newAccountBuilder.create();
            newAccountBuilder.show();
        }

        if (!checkPermissions()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.PROCESS_OUTGOING_CALLS,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.INTERNET},
                    PERMISSION_REQUEST_STATUS);
        } else {
            initServices(grantedPermissions());
        }
        // If the user did not turn the notification listener service on we prompt him to do so
        if (!isNotificationServiceEnabled() && checkNotificationPermission()) {
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }
        if (checkUsagePermission()) {
            enableUsageServiceAlertDialog = buildUsageStatsManagerAlertDialog();
            enableUsageServiceAlertDialog.show();
        }

        if (AccountUtils.getPassword(getApplicationContext()) == null) {
            buildDrivePasswordAlertDialog();
        } else {
            navigationView.getMenu().findItem(R.id.nav_upload).setVisible(false);
        }
        }

    private void initServices(List<String> grantedPermissions) {
        gSys = new GatheringSystem(getApplicationContext());

        for (SensorType type : SensorType.values()) {
            if (permissionAreGranted(grantedPermissions, type.getPermissions())) {
                gSys.addSensor(type);
                Log.d("MainActivity", "All permissions granted for sensor " + type);
            } else {
                Log.d("MainActivity", "Not all permissions granted for sensor " + type);
            }
        }
        gSys.start();

    }

    private boolean permissionAreGranted(List<String> grantedPermissions, String[] requiredPermissions) {
        for (String permission : requiredPermissions) {
            if (!grantedPermissions.contains(permission)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_STATUS) {
            initServices(convertPermissionResultsToList(permissions, grantResults));
        }
    }

    private List<String> convertPermissionResultsToList(String[] permissions, int[] grantResults) {
        List<String> grantedPermissions = new ArrayList<>();

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] >= 0) {
                grantedPermissions.add(permissions[i]);
            }
        }

        return grantedPermissions;
    }

    private List<String> grantedPermissions() {
        List<String> granted = new ArrayList<String>();
        try {
            PackageInfo pi = getPackageManager().getPackageInfo("usi.memotion2", PackageManager.GET_PERMISSIONS);
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    granted.add(pi.requestedPermissions[i]);
                }
            }
        } catch (Exception e) {
        }
        return granted;
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public void expandableButton1(View view) {
        expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        expandableLayout1.toggle(); // toggle expand and collapse
    }

    public void expandableButton2(View view) {
        expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);
        expandableLayout2.toggle(); // toggle expand and collapse
    }

    public void displayView(int viewId) {
        android.support.v4.app.Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                title = "Welcome";
                viewIsAtHome = true;
                break;
            case R.id.nav_lecture_surveys:
                fragment = new LectureSurveysFragment();
                title = "Lecture Surveys";
                viewIsAtHome = false;
                break;
            case R.id.nav_ediary:
                fragment = new EdiaryFragment();
                title = "E-diary";
                viewIsAtHome = false;
                break;
            case R.id.nav_register:
                if (checkAndroidID()) {
                    fragment = new ProfileFragment();
                    title = "Account Details";
                } else {
                    fragment = new RegistrationView();
                    title = "Create Account";
                }

                viewIsAtHome = false;
                break;

            case R.id.nav_about_study:
                fragment = new AboutFragment();
                title = "About Study";
                viewIsAtHome = false;
                break;

            case R.id.nav_about_app:
                fragment = new AboutApplicationFragment();
                title = "About App";
                viewIsAtHome = false;
                break;
            case R.id.nav_upload:
                buildDrivePasswordAlertDialog();
                viewIsAtHome = false;
                break;
        }


        if (fragment != null) {
            String menuFragment = getIntent().getStringExtra("fragmentChoice");
            String questionnaireSession = getIntent().getStringExtra("LectureSession");

            Log.v("displayView", "The received extras are: " + menuFragment);

            // If menuFragment is defined, then this activity was launched with a fragment selection
            if (menuFragment != null) {
                if (menuFragment.equals("lectureSurveys")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("LectureSession", questionnaireSession);
                    bundle.putString("fragmentChoice", null);
                    fragment = new LectureSurveysFragment();
                    fragment.setArguments(bundle);
                } else if (menuFragment.equals("ediary")) {
                    fragment = new EdiaryFragment();
                }
            }
            // Activity was not launched with a menuFragment selected -- continue as if this activity was opened from a launcher (for example)
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    private boolean checkAndroidID() {
        String query = "SELECT * FROM usersTable";

        LocalStorageController localController = SQLiteController.getInstance(getApplicationContext());
        ;

        Cursor records = localController.rawQuery(query, null);
        records.moveToFirst();

        if (records.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (!viewIsAtHome) {
            displayView(R.id.nav_home);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displayView(item.getItemId());
        return true;

    }

    public void triggerReminders() {
        dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        calendar = Calendar.getInstance();
        weekday = dayFormat.format(calendar.getTime());
        scheduler = new FinalScheduler();
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


        //January - 0 - if(month == Calendar.SEPTEMBER && dayOfMonth >= 1 && dayOfMonth <= 30){
        if (month == Calendar.SEPTEMBER || month == Calendar.OCTOBER || month == Calendar.NOVEMBER || month == Calendar.DECEMBER) {
            Log.v("Homeee", "Alarms Triggered");
            scheduler.createReminder(getApplicationContext());
            uploadDataEveryday();
        }
    }

    public void uploadDataEveryday() {

        // Retrieve a PendingIntent that will perform a broadcast
        Intent intent = new Intent(getApplicationContext(), UploadAlarmReceiver.class);
        AlarmManager am = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 10);
        cal.set(Calendar.SECOND, 0);

        if (cal.getTimeInMillis() > System.currentTimeMillis()) { //if it is more than 19:00 o'clock, trigger it tomorrow
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.GERMANY);

            String time = sdf.format(new Date());
            Log.v("HOMEEE", time + "Upload alarm triggered for today");
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.GERMANY);
            Log.v("HOMEEE", "Upload Alarm Triggered for next day");
            cal.add(Calendar.DAY_OF_MONTH, 1); //trigger alarm tomorrow
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT));
        }
    }


    /**
     * Is Notification Service Enabled.
     * Verifies if the notification listener service is enabled.
     * Got it from: https://github.com/kpbird/NotificationListenerService-Example/blob/master/NLSExample/src/main/java/com/kpbird/nlsexample/NLService.java
     *
     * @return True if eanbled, false otherwise.
     */
    private boolean isNotificationServiceEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Is Notification Service Enabled.
     * Verifies if the notification listener service is enabled.
     * Got it from: https://github.com/kpbird/NotificationListenerService-Example/blob/master/NLSExample/src/main/java/com/kpbird/nlsexample/NLService.java
     *
     * @return True if eanbled, false otherwise.
     */
    private boolean isUsageAccessServiceEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_USAGE_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Build Notification Listener Alert Dialog.
     * Builds the alert dialog that pops up if the user has not turned
     * the Notification Listener Service on yet.
     *
     * @return An alert dialog which leads to the notification enabling screen
     */
    private AlertDialog buildNotificationServiceAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Permission needed");
        alertDialogBuilder.setMessage("We need the permission to collect data about notifications.");
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sp.add("notification_permission", false);
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                        sp.add("notification_permission", true);
                    }
                });
        return (alertDialogBuilder.create());
    }

    private boolean checkNotificationPermission() {
        return !sp.getBoolean("notification_permission");
    }

    /**
     * Build UsageStatsManager Dialog.
     * Builds the alert dialog that pops up if the user has not turned
     * the UsageStatsManager on yet.
     *
     * @return An alert dialog which leads to the notification enabling screen
     */
    private AlertDialog buildUsageStatsManagerAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Permission needed");
        alertDialogBuilder.setMessage("We need the permission to collect data about the phone usage statistics.");
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Temporary fix so it doesn't always ask
                        sp.add("usage_permission", true);
                        //Actual code:
                        //sp.add("usage_permission", false);
                        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                        sp.add("usage_permission", true);
                    }
                });
        return (alertDialogBuilder.create());
    }

    private void buildDrivePasswordAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Password needed");
        alertDialogBuilder.setMessage("Please enter the SwitchDrive password:");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setView(R.layout.drive_password_layout);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView passTextView = (TextView) ((AlertDialog) dialogInterface).findViewById(R.id.drive_password);
                String pass = passTextView.getText().toString();
                if (!pass.isEmpty()) {
                    AccountUtils.addAccount(getApplicationContext(), "user", pass);
                    navigationView.getMenu().findItem(R.id.nav_upload).setVisible(false);
                }
            }
        });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();

    }

    private boolean checkUsagePermission() {
        return !sp.getBoolean("usage_permission");
    }

}

