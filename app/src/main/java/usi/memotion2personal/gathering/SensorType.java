package usi.memotion2personal.gathering;


/**
 * Created by Luca Dotti on 30/12/16.
 */
public enum SensorType {
    LOCATION(android.Manifest.permission.ACCESS_FINE_LOCATION),
    WIFI(android.Manifest.permission.CHANGE_WIFI_STATE, android.Manifest.permission.ACCESS_WIFI_STATE),
    ACCELEROMETER(),
    BLUETOOTH(android.Manifest.permission.BLUETOOTH, android.Manifest.permission.BLUETOOTH_ADMIN),
    LOCK(),
    SMS(android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.RECEIVE_SMS),
    USED_APPS(),
    PHONE_CALLS(android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.PROCESS_OUTGOING_CALLS),
    NOTIFICATIONS(),
    APPLICATION_LOGS(),
    ACTIVITY_RECOGNITION();

    private String[] permissions;

    SensorType(String... permissions) {
        this.permissions = permissions;
    }
    public String[] getPermissions() {
        return permissions;
    }
}
