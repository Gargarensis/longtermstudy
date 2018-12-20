package usi.memotion2personal.gathering;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import usi.memotion2personal.gathering.gatheringServices.AccelerometerGatheringService;
import usi.memotion2personal.gathering.gatheringServices.ActivityRecogntion.ActivityRecognitionBackgroundService;
import usi.memotion2personal.gathering.gatheringServices.BluethootGatheringService;
import usi.memotion2personal.gathering.gatheringServices.LocationGatheringService;
import usi.memotion2personal.gathering.gatheringServices.LockGatheringService;
import usi.memotion2personal.gathering.gatheringServices.Notifications.NotificationMonitorService;
import usi.memotion2personal.gathering.gatheringServices.PhoneCallGatheringService;
import usi.memotion2personal.gathering.gatheringServices.SMSGatheringService;
import usi.memotion2personal.gathering.gatheringServices.WifiGatheringService;

import android.util.Log;

/**
 * Created by usi on 03/01/17.
 */

public class GatheringSystem {
    private List<SensorType> sensors;
    private Context context;

    public GatheringSystem(Context context) {
        sensors = new ArrayList<>();
        this.context = context;
    }

    public void addSensor(SensorType sensor) {
        sensors.add(sensor);
    }

    public void start() {
        for (SensorType sensor: sensors) {
            switch(sensor) {
                case ACCELEROMETER:
                    Log.d("GATHERING SYSTEM", "Started accelerometer service");
                    context.startService(new Intent(context, AccelerometerGatheringService.class));
                    break;
                case LOCATION:
                    Log.d("GATHERING SYSTEM", "Started location service");
                    context.startService(new Intent(context, LocationGatheringService.class));
                    break;
                case LOCK:
                    Log.d("GATHERING SYSTEM", "Started lock service");
                    context.startService(new Intent(context, LockGatheringService.class));
                    break;
                case SMS:
                    Log.d("GATHERING SYSTEM", "Started sms service");
                    context.startService(new Intent(context, SMSGatheringService.class));
                    break;
                case WIFI:
                    Log.d("GATHERING SYSTEM", "Started wifi service");
                    context.startService(new Intent(context, WifiGatheringService.class));
                    break;
                case PHONE_CALLS:
                    Log.d("GATHERING SYSTEM", "Started calls service");
                    context.startService(new Intent(context, PhoneCallGatheringService.class));
                    break;
                case NOTIFICATIONS:
                    Log.d("GATHERING SYSTEM", "Started notifications service");
                    context.startService(new Intent(context, NotificationMonitorService.class));
                    break;
                case ACTIVITY_RECOGNITION:
                    Log.d("GATHERING SYSTEM", "Started activity recognition service");
                    context.startService(new Intent(context,ActivityRecognitionBackgroundService.class));
                    break;
                default:
                    break;
            }
        }
    }

    public void stopServices() {
        context.stopService(new Intent(context, AccelerometerGatheringService.class));
        context.stopService(new Intent(context, BluethootGatheringService.class));
        context.stopService(new Intent(context, LocationGatheringService.class));
        context.stopService(new Intent(context, LockGatheringService.class));
        context.stopService(new Intent(context, SMSGatheringService.class));
        context.stopService(new Intent(context, WifiGatheringService.class));
        context.stopService(new Intent(context, PhoneCallGatheringService.class));
        context.stopService(new Intent(context, NotificationMonitorService.class));
        context.stopService(new Intent(context, ActivityRecognitionBackgroundService.class));
    }
}
