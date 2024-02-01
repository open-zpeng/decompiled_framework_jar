package android.hardware;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes.dex */
public class SensorEvent {
    public int accuracy;
    public Sensor sensor;
    public long timestamp;
    public final float[] values;

    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public SensorEvent(int valueSize) {
        this.values = new float[valueSize];
    }
}
