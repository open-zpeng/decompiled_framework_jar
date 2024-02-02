package android.os;

import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.HashMap;
/* loaded from: classes2.dex */
public abstract class FileObserver {
    public static final int ACCESS = 1;
    public static final int ALL_EVENTS = 4095;
    public static final int ATTRIB = 4;
    public static final int CLOSE_NOWRITE = 16;
    public static final int CLOSE_WRITE = 8;
    public static final int CREATE = 256;
    public static final int DELETE = 512;
    public static final int DELETE_SELF = 1024;
    private static final String LOG_TAG = "FileObserver";
    public static final int MODIFY = 2;
    public static final int MOVED_FROM = 64;
    public static final int MOVED_TO = 128;
    public static final int MOVE_SELF = 2048;
    public static final int OPEN = 32;
    public protected static ObserverThread s_observerThread = new ObserverThread();
    private Integer m_descriptor;
    private int m_mask;
    private String m_path;

    public abstract void onEvent(int i, String str);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class ObserverThread extends Thread {
        private int m_fd;
        private HashMap<Integer, WeakReference> m_observers;

        private native int init();

        private native void observe(int i);

        private native int startWatching(int i, String str, int i2);

        private native void stopWatching(int i, int i2);

        public synchronized ObserverThread() {
            super(FileObserver.LOG_TAG);
            this.m_observers = new HashMap<>();
            this.m_fd = init();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            observe(this.m_fd);
        }

        public synchronized int startWatching(String path, int mask, FileObserver observer) {
            int wfd = startWatching(this.m_fd, path, mask);
            Integer i = new Integer(wfd);
            if (wfd >= 0) {
                synchronized (this.m_observers) {
                    this.m_observers.put(i, new WeakReference(observer));
                }
            }
            return i.intValue();
        }

        public synchronized void stopWatching(int descriptor) {
            stopWatching(this.m_fd, descriptor);
        }

        private protected void onEvent(int wfd, int mask, String path) {
            FileObserver observer = null;
            synchronized (this.m_observers) {
                WeakReference weak = this.m_observers.get(Integer.valueOf(wfd));
                if (weak != null && (observer = (FileObserver) weak.get()) == null) {
                    this.m_observers.remove(Integer.valueOf(wfd));
                }
            }
            if (observer != null) {
                try {
                    observer.onEvent(mask, path);
                } catch (Throwable throwable) {
                    Log.wtf(FileObserver.LOG_TAG, "Unhandled exception in FileObserver " + observer, throwable);
                }
            }
        }
    }

    static {
        s_observerThread.start();
    }

    public FileObserver(String path) {
        this(path, ALL_EVENTS);
    }

    public FileObserver(String path, int mask) {
        this.m_path = path;
        this.m_mask = mask;
        this.m_descriptor = -1;
    }

    protected void finalize() {
        stopWatching();
    }

    public void startWatching() {
        if (this.m_descriptor.intValue() < 0) {
            this.m_descriptor = Integer.valueOf(s_observerThread.startWatching(this.m_path, this.m_mask, this));
        }
    }

    public void stopWatching() {
        if (this.m_descriptor.intValue() >= 0) {
            s_observerThread.stopWatching(this.m_descriptor.intValue());
            this.m_descriptor = -1;
        }
    }
}
