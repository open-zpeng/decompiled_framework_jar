package android.hardware;
/* loaded from: classes.dex */
public class SensorEvent {
    public int accuracy;
    public Sensor sensor;
    public long timestamp;
    public final float[] values;

    public private protected SensorEvent(int valueSize) {
        this.values = new float[valueSize];
    }
}
