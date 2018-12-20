package usi.memotion2personal.gathering.gatheringServices.ActivityRecogntion;

import com.google.android.gms.location.DetectedActivity;

/**
 * Created by biancastancu
 */

public class ActivityRecognitionUtil{
    public static String getActivityName(int activity) {
        switch (activity) {
            case DetectedActivity.IN_VEHICLE:
                return "IN_VEHICLE";
            case DetectedActivity.ON_BICYCLE:
                return "ON_BICYCLE";
            case DetectedActivity.RUNNING:
                return "RUNNING";
            case DetectedActivity.STILL:
                return "STILL";
            case DetectedActivity.WALKING:
                return "WALKING";
            default:
                return "UNKNOWN";
        }
    }

    public static String getTransitionName(int transition){
        if(transition==0)
            return "ENTER";
        return "EXIT";
    }
}
