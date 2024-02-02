package android.net.lowpan;

import java.util.Map;
/* loaded from: classes2.dex */
public abstract class LowpanProperty<T> {
    private protected abstract synchronized String getName();

    private protected abstract synchronized Class<T> getType();

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void putInMap(Map map, T value) {
        map.put(getName(), value);
    }

    private protected synchronized T getFromMap(Map map) {
        return (T) map.get(getName());
    }
}
