package android.media;

import android.media.AudioManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class AudioPortEventHandler {
    private static final int AUDIOPORT_EVENT_NEW_LISTENER = 4;
    private static final int AUDIOPORT_EVENT_PATCH_LIST_UPDATED = 2;
    private static final int AUDIOPORT_EVENT_PORT_LIST_UPDATED = 1;
    private static final int AUDIOPORT_EVENT_SERVICE_DIED = 3;
    private static final long RESCHEDULE_MESSAGE_DELAY_MS = 100;
    private static final String TAG = "AudioPortEventHandler";
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    public protected long mJniCallback;
    private final ArrayList<AudioManager.OnAudioPortUpdateListener> mListeners = new ArrayList<>();

    private native void native_finalize();

    private native void native_setup(Object obj);

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void init() {
        if (this.mHandler != null) {
            return;
        }
        this.mHandlerThread = new HandlerThread(TAG);
        this.mHandlerThread.start();
        if (this.mHandlerThread.getLooper() != null) {
            this.mHandler = new Handler(this.mHandlerThread.getLooper()) { // from class: android.media.AudioPortEventHandler.1
                /* JADX WARN: Removed duplicated region for block: B:44:0x00b0 A[LOOP:1: B:42:0x00a9->B:44:0x00b0, LOOP_END] */
                /* JADX WARN: Removed duplicated region for block: B:53:0x00bd A[SYNTHETIC] */
                @Override // android.os.Handler
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public void handleMessage(android.os.Message r9) {
                    /*
                        r8 = this;
                        monitor-enter(r8)
                        int r0 = r9.what     // Catch: java.lang.Throwable -> Lbe
                        r1 = 4
                        if (r0 != r1) goto L21
                        java.util.ArrayList r0 = new java.util.ArrayList     // Catch: java.lang.Throwable -> Lbe
                        r0.<init>()     // Catch: java.lang.Throwable -> Lbe
                        android.media.AudioPortEventHandler r1 = android.media.AudioPortEventHandler.this     // Catch: java.lang.Throwable -> Lbe
                        java.util.ArrayList r1 = android.media.AudioPortEventHandler.access$000(r1)     // Catch: java.lang.Throwable -> Lbe
                        java.lang.Object r2 = r9.obj     // Catch: java.lang.Throwable -> Lbe
                        boolean r1 = r1.contains(r2)     // Catch: java.lang.Throwable -> Lbe
                        if (r1 == 0) goto L27
                        java.lang.Object r1 = r9.obj     // Catch: java.lang.Throwable -> Lbe
                        android.media.AudioManager$OnAudioPortUpdateListener r1 = (android.media.AudioManager.OnAudioPortUpdateListener) r1     // Catch: java.lang.Throwable -> Lbe
                        r0.add(r1)     // Catch: java.lang.Throwable -> Lbe
                        goto L27
                    L21:
                        android.media.AudioPortEventHandler r0 = android.media.AudioPortEventHandler.this     // Catch: java.lang.Throwable -> Lbe
                        java.util.ArrayList r0 = android.media.AudioPortEventHandler.access$000(r0)     // Catch: java.lang.Throwable -> Lbe
                    L27:
                        monitor-exit(r8)     // Catch: java.lang.Throwable -> Lbe
                        int r1 = r9.what
                        r2 = 3
                        r3 = 1
                        if (r1 == r3) goto L37
                        int r1 = r9.what
                        r4 = 2
                        if (r1 == r4) goto L37
                        int r1 = r9.what
                        if (r1 != r2) goto L3a
                    L37:
                        android.media.AudioManager.resetAudioPortGeneration()
                    L3a:
                        boolean r1 = r0.isEmpty()
                        if (r1 == 0) goto L41
                        return
                    L41:
                        java.util.ArrayList r1 = new java.util.ArrayList
                        r1.<init>()
                        java.util.ArrayList r4 = new java.util.ArrayList
                        r4.<init>()
                        int r5 = r9.what
                        if (r5 == r2) goto L64
                        r2 = 0
                        int r2 = android.media.AudioManager.updateAudioPortCache(r1, r4, r2)
                        if (r2 == 0) goto L64
                        int r3 = r9.what
                        java.lang.Object r5 = r9.obj
                        android.os.Message r3 = r8.obtainMessage(r3, r5)
                        r5 = 100
                        r8.sendMessageDelayed(r3, r5)
                        return
                    L64:
                        int r2 = r9.what
                        r5 = 0
                        switch(r2) {
                            case 1: goto L80;
                            case 2: goto La0;
                            case 3: goto L6b;
                            case 4: goto L80;
                            default: goto L6a;
                        }
                    L6a:
                        goto Lbd
                    L6b:
                    L6c:
                        r2 = r5
                        int r3 = r0.size()
                        if (r2 >= r3) goto L7f
                        java.lang.Object r3 = r0.get(r2)
                        android.media.AudioManager$OnAudioPortUpdateListener r3 = (android.media.AudioManager.OnAudioPortUpdateListener) r3
                        r3.onServiceDied()
                        int r5 = r2 + 1
                        goto L6c
                    L7f:
                        goto Lbd
                    L80:
                        android.media.AudioPort[] r2 = new android.media.AudioPort[r5]
                        java.lang.Object[] r2 = r1.toArray(r2)
                        android.media.AudioPort[] r2 = (android.media.AudioPort[]) r2
                        r6 = r5
                    L89:
                        int r7 = r0.size()
                        if (r6 >= r7) goto L9b
                        java.lang.Object r7 = r0.get(r6)
                        android.media.AudioManager$OnAudioPortUpdateListener r7 = (android.media.AudioManager.OnAudioPortUpdateListener) r7
                        r7.onAudioPortListUpdate(r2)
                        int r6 = r6 + 1
                        goto L89
                    L9b:
                        int r6 = r9.what
                        if (r6 != r3) goto La0
                        goto Lbd
                    La0:
                        android.media.AudioPatch[] r2 = new android.media.AudioPatch[r5]
                        java.lang.Object[] r2 = r4.toArray(r2)
                        android.media.AudioPatch[] r2 = (android.media.AudioPatch[]) r2
                    La9:
                        r3 = r5
                        int r5 = r0.size()
                        if (r3 >= r5) goto Lbc
                        java.lang.Object r5 = r0.get(r3)
                        android.media.AudioManager$OnAudioPortUpdateListener r5 = (android.media.AudioManager.OnAudioPortUpdateListener) r5
                        r5.onAudioPatchListUpdate(r2)
                        int r5 = r3 + 1
                        goto La9
                    Lbc:
                    Lbd:
                        return
                    Lbe:
                        r0 = move-exception
                        monitor-exit(r8)     // Catch: java.lang.Throwable -> Lbe
                        throw r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: android.media.AudioPortEventHandler.AnonymousClass1.handleMessage(android.os.Message):void");
                }
            };
            native_setup(new WeakReference(this));
        } else {
            this.mHandler = null;
        }
    }

    protected void finalize() {
        native_finalize();
        if (this.mHandlerThread.isAlive()) {
            this.mHandlerThread.quit();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void registerListener(AudioManager.OnAudioPortUpdateListener l) {
        this.mListeners.add(l);
        if (this.mHandler != null) {
            Message m = this.mHandler.obtainMessage(4, 0, 0, l);
            this.mHandler.sendMessage(m);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void unregisterListener(AudioManager.OnAudioPortUpdateListener l) {
        this.mListeners.remove(l);
    }

    synchronized Handler handler() {
        return this.mHandler;
    }

    public protected static void postEventFromNative(Object module_ref, int what, int arg1, int arg2, Object obj) {
        Handler handler;
        AudioPortEventHandler eventHandler = (AudioPortEventHandler) ((WeakReference) module_ref).get();
        if (eventHandler != null && eventHandler != null && (handler = eventHandler.handler()) != null) {
            Message m = handler.obtainMessage(what, arg1, arg2, obj);
            if (what != 4) {
                handler.removeMessages(what);
            }
            handler.sendMessage(m);
        }
    }
}
