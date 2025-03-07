package android.hardware.location;

import android.content.Context;
import android.hardware.location.IActivityRecognitionHardware;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import java.lang.reflect.Array;

/* loaded from: classes.dex */
public class ActivityRecognitionHardware extends IActivityRecognitionHardware.Stub {
    private static final String ENFORCE_HW_PERMISSION_MESSAGE = "Permission 'android.permission.LOCATION_HARDWARE' not granted to access ActivityRecognitionHardware";
    private static final int EVENT_TYPE_COUNT = 3;
    private static final int EVENT_TYPE_DISABLED = 0;
    private static final int EVENT_TYPE_ENABLED = 1;
    private static final String HARDWARE_PERMISSION = "android.permission.LOCATION_HARDWARE";
    private static final int INVALID_ACTIVITY_TYPE = -1;
    private static final int NATIVE_SUCCESS_RESULT = 0;
    private static ActivityRecognitionHardware sSingletonInstance;
    private final Context mContext;
    private final SinkList mSinks = new SinkList();
    private final String[] mSupportedActivities;
    private final int mSupportedActivitiesCount;
    private final int[][] mSupportedActivitiesEnabledEvents;
    private static final String TAG = "ActivityRecognitionHW";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final Object sSingletonInstanceLock = new Object();

    private static native void nativeClassInit();

    /* JADX INFO: Access modifiers changed from: private */
    public native int nativeDisableActivityEvent(int i, int i2);

    private native int nativeEnableActivityEvent(int i, int i2, long j);

    private native int nativeFlush();

    private native String[] nativeGetSupportedActivities();

    private native void nativeInitialize();

    private static native boolean nativeIsSupported();

    private native void nativeRelease();

    static {
        nativeClassInit();
    }

    /* loaded from: classes.dex */
    private static class Event {
        public int activity;
        public long timestamp;
        public int type;

        private Event() {
        }
    }

    private ActivityRecognitionHardware(Context context) {
        nativeInitialize();
        this.mContext = context;
        this.mSupportedActivities = fetchSupportedActivities();
        this.mSupportedActivitiesCount = this.mSupportedActivities.length;
        this.mSupportedActivitiesEnabledEvents = (int[][]) Array.newInstance(int.class, this.mSupportedActivitiesCount, 3);
    }

    public static ActivityRecognitionHardware getInstance(Context context) {
        ActivityRecognitionHardware activityRecognitionHardware;
        synchronized (sSingletonInstanceLock) {
            if (sSingletonInstance == null) {
                sSingletonInstance = new ActivityRecognitionHardware(context);
            }
            activityRecognitionHardware = sSingletonInstance;
        }
        return activityRecognitionHardware;
    }

    public static boolean isSupported() {
        return nativeIsSupported();
    }

    @Override // android.hardware.location.IActivityRecognitionHardware
    public String[] getSupportedActivities() {
        checkPermissions();
        return this.mSupportedActivities;
    }

    @Override // android.hardware.location.IActivityRecognitionHardware
    public boolean isActivitySupported(String activity) {
        checkPermissions();
        int activityType = getActivityType(activity);
        return activityType != -1;
    }

    @Override // android.hardware.location.IActivityRecognitionHardware
    public boolean registerSink(IActivityRecognitionHardwareSink sink) {
        checkPermissions();
        return this.mSinks.register(sink);
    }

    @Override // android.hardware.location.IActivityRecognitionHardware
    public boolean unregisterSink(IActivityRecognitionHardwareSink sink) {
        checkPermissions();
        return this.mSinks.unregister(sink);
    }

    @Override // android.hardware.location.IActivityRecognitionHardware
    public boolean enableActivityEvent(String activity, int eventType, long reportLatencyNs) {
        checkPermissions();
        int activityType = getActivityType(activity);
        if (activityType == -1) {
            return false;
        }
        int result = nativeEnableActivityEvent(activityType, eventType, reportLatencyNs);
        if (result != 0) {
            return false;
        }
        this.mSupportedActivitiesEnabledEvents[activityType][eventType] = 1;
        return true;
    }

    @Override // android.hardware.location.IActivityRecognitionHardware
    public boolean disableActivityEvent(String activity, int eventType) {
        checkPermissions();
        int activityType = getActivityType(activity);
        if (activityType == -1) {
            return false;
        }
        int result = nativeDisableActivityEvent(activityType, eventType);
        if (result == 0) {
            this.mSupportedActivitiesEnabledEvents[activityType][eventType] = 0;
            return true;
        }
        return false;
    }

    @Override // android.hardware.location.IActivityRecognitionHardware
    public boolean flush() {
        checkPermissions();
        int result = nativeFlush();
        return result == 0;
    }

    private void onActivityChanged(Event[] events) {
        if (events == null || events.length == 0) {
            if (DEBUG) {
                Log.d(TAG, "No events to broadcast for onActivityChanged.");
                return;
            }
            return;
        }
        int eventsLength = events.length;
        ActivityRecognitionEvent[] activityRecognitionEventArray = new ActivityRecognitionEvent[eventsLength];
        for (int i = 0; i < eventsLength; i++) {
            Event event = events[i];
            String activityName = getActivityName(event.activity);
            activityRecognitionEventArray[i] = new ActivityRecognitionEvent(activityName, event.type, event.timestamp);
        }
        ActivityChangedEvent activityChangedEvent = new ActivityChangedEvent(activityRecognitionEventArray);
        int size = this.mSinks.beginBroadcast();
        for (int i2 = 0; i2 < size; i2++) {
            IActivityRecognitionHardwareSink sink = this.mSinks.getBroadcastItem(i2);
            try {
                sink.onActivityChanged(activityChangedEvent);
            } catch (RemoteException e) {
                Log.e(TAG, "Error delivering activity changed event.", e);
            }
        }
        this.mSinks.finishBroadcast();
    }

    private String getActivityName(int activityType) {
        if (activityType >= 0) {
            String[] strArr = this.mSupportedActivities;
            if (activityType < strArr.length) {
                return strArr[activityType];
            }
        }
        String message = String.format("Invalid ActivityType: %d, SupportedActivities: %d", Integer.valueOf(activityType), Integer.valueOf(this.mSupportedActivities.length));
        Log.e(TAG, message);
        return null;
    }

    private int getActivityType(String activity) {
        if (TextUtils.isEmpty(activity)) {
            return -1;
        }
        int supportedActivitiesLength = this.mSupportedActivities.length;
        for (int i = 0; i < supportedActivitiesLength; i++) {
            if (activity.equals(this.mSupportedActivities[i])) {
                return i;
            }
        }
        return -1;
    }

    private void checkPermissions() {
        this.mContext.enforceCallingPermission("android.permission.LOCATION_HARDWARE", ENFORCE_HW_PERMISSION_MESSAGE);
    }

    private String[] fetchSupportedActivities() {
        String[] supportedActivities = nativeGetSupportedActivities();
        if (supportedActivities != null) {
            return supportedActivities;
        }
        return new String[0];
    }

    /* loaded from: classes.dex */
    private class SinkList extends RemoteCallbackList<IActivityRecognitionHardwareSink> {
        private SinkList() {
        }

        @Override // android.os.RemoteCallbackList
        public void onCallbackDied(IActivityRecognitionHardwareSink callback) {
            int callbackCount = ActivityRecognitionHardware.this.mSinks.getRegisteredCallbackCount();
            if (ActivityRecognitionHardware.DEBUG) {
                Log.d(ActivityRecognitionHardware.TAG, "RegisteredCallbackCount: " + callbackCount);
            }
            if (callbackCount != 0) {
                return;
            }
            for (int activity = 0; activity < ActivityRecognitionHardware.this.mSupportedActivitiesCount; activity++) {
                for (int event = 0; event < 3; event++) {
                    disableActivityEventIfEnabled(activity, event);
                }
            }
        }

        private void disableActivityEventIfEnabled(int activityType, int eventType) {
            if (ActivityRecognitionHardware.this.mSupportedActivitiesEnabledEvents[activityType][eventType] == 1) {
                int result = ActivityRecognitionHardware.this.nativeDisableActivityEvent(activityType, eventType);
                ActivityRecognitionHardware.this.mSupportedActivitiesEnabledEvents[activityType][eventType] = 0;
                String message = String.format("DisableActivityEvent: activityType=%d, eventType=%d, result=%d", Integer.valueOf(activityType), Integer.valueOf(eventType), Integer.valueOf(result));
                Log.e(ActivityRecognitionHardware.TAG, message);
            }
        }
    }
}
