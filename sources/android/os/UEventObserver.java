package android.os;

import android.annotation.UnsupportedAppUsage;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes2.dex */
public abstract class UEventObserver {
    private static final boolean DEBUG = false;
    private static final String TAG = "UEventObserver";
    private static UEventThread sThread;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeAddMatch(String str);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeRemoveMatch(String str);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeSetup();

    private static native String nativeWaitForNextEvent();

    @UnsupportedAppUsage
    public abstract void onUEvent(UEvent uEvent);

    static /* synthetic */ String access$100() {
        return nativeWaitForNextEvent();
    }

    protected void finalize() throws Throwable {
        try {
            stopObserving();
        } finally {
            super.finalize();
        }
    }

    private static UEventThread getThread() {
        UEventThread uEventThread;
        synchronized (UEventObserver.class) {
            if (sThread == null) {
                sThread = new UEventThread();
                sThread.start();
            }
            uEventThread = sThread;
        }
        return uEventThread;
    }

    private static UEventThread peekThread() {
        UEventThread uEventThread;
        synchronized (UEventObserver.class) {
            uEventThread = sThread;
        }
        return uEventThread;
    }

    @UnsupportedAppUsage
    public final void startObserving(String match) {
        if (match == null || match.isEmpty()) {
            throw new IllegalArgumentException("match substring must be non-empty");
        }
        UEventThread t = getThread();
        t.addObserver(match, this);
    }

    @UnsupportedAppUsage
    public final void stopObserving() {
        UEventThread t = peekThread();
        if (t != null) {
            t.removeObserver(this);
        }
    }

    /* loaded from: classes2.dex */
    public static final class UEvent {
        private final HashMap<String, String> mMap = new HashMap<>();

        public UEvent(String message) {
            int offset = 0;
            int length = message.length();
            while (offset < length) {
                int equals = message.indexOf(61, offset);
                int at = message.indexOf(0, offset);
                if (at >= 0) {
                    if (equals > offset && equals < at) {
                        this.mMap.put(message.substring(offset, equals), message.substring(equals + 1, at));
                    }
                    offset = at + 1;
                } else {
                    return;
                }
            }
        }

        @UnsupportedAppUsage
        public String get(String key) {
            return this.mMap.get(key);
        }

        @UnsupportedAppUsage
        public String get(String key, String defaultValue) {
            String result = this.mMap.get(key);
            return result == null ? defaultValue : result;
        }

        public String toString() {
            return this.mMap.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class UEventThread extends Thread {
        private final ArrayList<Object> mKeysAndObservers;
        private final ArrayList<UEventObserver> mTempObserversToSignal;

        public UEventThread() {
            super(UEventObserver.TAG);
            this.mKeysAndObservers = new ArrayList<>();
            this.mTempObserversToSignal = new ArrayList<>();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            UEventObserver.nativeSetup();
            while (true) {
                String message = UEventObserver.access$100();
                if (message != null) {
                    sendEvent(message);
                }
            }
        }

        private void sendEvent(String message) {
            synchronized (this.mKeysAndObservers) {
                int N = this.mKeysAndObservers.size();
                for (int i = 0; i < N; i += 2) {
                    String key = (String) this.mKeysAndObservers.get(i);
                    if (message.contains(key)) {
                        UEventObserver observer = (UEventObserver) this.mKeysAndObservers.get(i + 1);
                        this.mTempObserversToSignal.add(observer);
                    }
                }
            }
            if (!this.mTempObserversToSignal.isEmpty()) {
                UEvent event = new UEvent(message);
                int N2 = this.mTempObserversToSignal.size();
                for (int i2 = 0; i2 < N2; i2++) {
                    UEventObserver observer2 = this.mTempObserversToSignal.get(i2);
                    observer2.onUEvent(event);
                }
                this.mTempObserversToSignal.clear();
            }
        }

        public void addObserver(String match, UEventObserver observer) {
            synchronized (this.mKeysAndObservers) {
                this.mKeysAndObservers.add(match);
                this.mKeysAndObservers.add(observer);
                UEventObserver.nativeAddMatch(match);
            }
        }

        public void removeObserver(UEventObserver observer) {
            synchronized (this.mKeysAndObservers) {
                int i = 0;
                while (i < this.mKeysAndObservers.size()) {
                    if (this.mKeysAndObservers.get(i + 1) == observer) {
                        this.mKeysAndObservers.remove(i + 1);
                        String match = (String) this.mKeysAndObservers.remove(i);
                        UEventObserver.nativeRemoveMatch(match);
                    } else {
                        i += 2;
                    }
                }
            }
        }
    }
}
