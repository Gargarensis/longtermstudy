package usi.memotion2personal.gathering.gatheringServices.ActivityRecogntion;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;

import usi.memotion2personal.local.database.controllers.LocalStorageController;
import usi.memotion2personal.local.database.controllers.SQLiteController;
import usi.memotion2personal.local.database.tables.ActivityRecognitionTable;

/**
 * Created by biancastancu
 */

public class DetectedActivitiesIntentService extends IntentService {

    protected static final String TAG = DetectedActivitiesIntentService.class.getSimpleName();
    private LocalStorageController localStorageController;

    public DetectedActivitiesIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        localStorageController = SQLiteController.getInstance(getApplicationContext());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityTransitionResult.hasResult(intent)) {
            ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);
            for (ActivityTransitionEvent event : result.getTransitionEvents()) {
                    saveActivityRecognition(event.getActivityType(), event.getTransitionType());
            }
        }
    }

    private void saveActivityRecognition(int activity, int transition) {
        ContentValues record = new ContentValues();

        record.put(ActivityRecognitionTable.KEY_TIMESTAMP, Long.toString(System.currentTimeMillis()));
        record.put(ActivityRecognitionTable.KEY_ACTIVITY,activity);
        record.put(ActivityRecognitionTable.KEY_TRANSITION_TYPE,transition);

        localStorageController.insertRecord(ActivityRecognitionTable.TABLE_ACTIVITY_RECOGNITION, record);
        Log.d(TAG, "Detected activity at " + record.get(ActivityRecognitionTable.KEY_TIMESTAMP)
                + ", activity: " + ActivityRecognitionUtil.getActivityName((int)record.get(ActivityRecognitionTable.KEY_ACTIVITY)) +
                ", transition: " + ActivityRecognitionUtil.getTransitionName((int)record.get(ActivityRecognitionTable.KEY_TRANSITION_TYPE)));
    }
}

