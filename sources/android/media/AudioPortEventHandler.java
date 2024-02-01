package android.media;

import android.annotation.UnsupportedAppUsage;
import android.media.AudioManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class AudioPortEventHandler {
    private static final int AUDIOPORT_EVENT_NEW_LISTENER = 4;
    private static final int AUDIOPORT_EVENT_PATCH_LIST_UPDATED = 2;
    private static final int AUDIOPORT_EVENT_PORT_LIST_UPDATED = 1;
    private static final int AUDIOPORT_EVENT_SERVICE_DIED = 3;
    private static final long RESCHEDULE_MESSAGE_DELAY_MS = 100;
    private static final String TAG = "AudioPortEventHandler";
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    @UnsupportedAppUsage
    private long mJniCallback;
    private final ArrayList<AudioManager.OnAudioPortUpdateListener> mListeners = new ArrayList<>();

    private native void native_finalize();

    private native void native_setup(Object obj);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init() {
        synchronized (this) {
            if (this.mHandler != null) {
                return;
            }
            this.mHandlerThread = new HandlerThread(TAG);
            this.mHandlerThread.start();
            if (this.mHandlerThread.getLooper() != null) {
                this.mHandler = new Handler(this.mHandlerThread.getLooper()) { // from class: android.media.AudioPortEventHandler.1
                    /* JADX WARN: Removed duplicated region for block: B:47:0x00b3 A[LOOP:1: B:45:0x00ad->B:47:0x00b3, LOOP_END] */
                    @Override // android.os.Handler
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct code enable 'Show inconsistent code' option in preferences
                    */
                    public void handleMessage(android.os.Message r10) {
                        /*
                            r9 = this;
                            monitor-enter(r9)
                            int r0 = r10.what     // Catch: java.lang.Throwable -> Lc1
                            r1 = 4
                            if (r0 != r1) goto L21
                            java.util.ArrayList r0 = new java.util.ArrayList     // Catch: java.lang.Throwable -> Lc1
                            r0.<init>()     // Catch: java.lang.Throwable -> Lc1
                            android.media.AudioPortEventHandler r2 = android.media.AudioPortEventHandler.this     // Catch: java.lang.Throwable -> Lc1
                            java.util.ArrayList r2 = android.media.AudioPortEventHandler.access$000(r2)     // Catch: java.lang.Throwable -> Lc1
                            java.lang.Object r3 = r10.obj     // Catch: java.lang.Throwable -> Lc1
                            boolean r2 = r2.contains(r3)     // Catch: java.lang.Throwable -> Lc1
                            if (r2 == 0) goto L27
                            java.lang.Object r2 = r10.obj     // Catch: java.lang.Throwable -> Lc1
                            android.media.AudioManager$OnAudioPortUpdateListener r2 = (android.media.AudioManager.OnAudioPortUpdateListener) r2     // Catch: java.lang.Throwable -> Lc1
                            r0.add(r2)     // Catch: java.lang.Throwable -> Lc1
                            goto L27
                        L21:
                            android.media.AudioPortEventHandler r0 = android.media.AudioPortEventHandler.this     // Catch: java.lang.Throwable -> Lc1
                            java.util.ArrayList r0 = android.media.AudioPortEventHandler.access$000(r0)     // Catch: java.lang.Throwable -> Lc1
                        L27:
                            monitor-exit(r9)     // Catch: java.lang.Throwable -> Lc1
                            int r2 = r10.what
                            r3 = 2
                            r4 = 3
                            r5 = 1
                            if (r2 == r5) goto L37
                            int r2 = r10.what
                            if (r2 == r3) goto L37
                            int r2 = r10.what
                            if (r2 != r4) goto L3a
                        L37:
                            android.media.AudioManager.resetAudioPortGeneration()
                        L3a:
                            boolean r2 = r0.isEmpty()
                            if (r2 == 0) goto L41
                            return
                        L41:
                            java.util.ArrayList r2 = new java.util.ArrayList
                            r2.<init>()
                            java.util.ArrayList r6 = new java.util.ArrayList
                            r6.<init>()
                            int r7 = r10.what
                            if (r7 == r4) goto L64
                            r7 = 0
                            int r7 = android.media.AudioManager.updateAudioPortCache(r2, r6, r7)
                            if (r7 == 0) goto L64
                            int r1 = r10.what
                            java.lang.Object r3 = r10.obj
                            android.os.Message r1 = r9.obtainMessage(r1, r3)
                            r3 = 100
                            r9.sendMessageDelayed(r1, r3)
                            return
                        L64:
                            int r7 = r10.what
                            r8 = 0
                            if (r7 == r5) goto L84
                            if (r7 == r3) goto La4
                            if (r7 == r4) goto L70
                            if (r7 == r1) goto L84
                            goto Lc0
                        L70:
                            r1 = 0
                        L71:
                            int r3 = r0.size()
                            if (r1 >= r3) goto L83
                            java.lang.Object r3 = r0.get(r1)
                            android.media.AudioManager$OnAudioPortUpdateListener r3 = (android.media.AudioManager.OnAudioPortUpdateListener) r3
                            r3.onServiceDied()
                            int r1 = r1 + 1
                            goto L71
                        L83:
                            goto Lc0
                        L84:
                            android.media.AudioPort[] r1 = new android.media.AudioPort[r8]
                            java.lang.Object[] r1 = r2.toArray(r1)
                            android.media.AudioPort[] r1 = (android.media.AudioPort[]) r1
                            r3 = 0
                        L8d:
                            int r4 = r0.size()
                            if (r3 >= r4) goto L9f
                            java.lang.Object r4 = r0.get(r3)
                            android.media.AudioManager$OnAudioPortUpdateListener r4 = (android.media.AudioManager.OnAudioPortUpdateListener) r4
                            r4.onAudioPortListUpdate(r1)
                            int r3 = r3 + 1
                            goto L8d
                        L9f:
                            int r3 = r10.what
                            if (r3 != r5) goto La4
                            goto Lc0
                        La4:
                            android.media.AudioPatch[] r1 = new android.media.AudioPatch[r8]
                            java.lang.Object[] r1 = r6.toArray(r1)
                            android.media.AudioPatch[] r1 = (android.media.AudioPatch[]) r1
                            r3 = 0
                        Lad:
                            int r4 = r0.size()
                            if (r3 >= r4) goto Lbf
                            java.lang.Object r4 = r0.get(r3)
                            android.media.AudioManager$OnAudioPortUpdateListener r4 = (android.media.AudioManager.OnAudioPortUpdateListener) r4
                            r4.onAudioPatchListUpdate(r1)
                            int r3 = r3 + 1
                            goto Lad
                        Lbf:
                        Lc0:
                            return
                        Lc1:
                            r0 = move-exception
                            monitor-exit(r9)     // Catch: java.lang.Throwable -> Lc1
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
    }

    protected void finalize() {
        native_finalize();
        if (this.mHandlerThread.isAlive()) {
            this.mHandlerThread.quit();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerListener(AudioManager.OnAudioPortUpdateListener l) {
        synchronized (this) {
            this.mListeners.add(l);
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            Message m = handler.obtainMessage(4, 0, 0, l);
            this.mHandler.sendMessage(m);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterListener(AudioManager.OnAudioPortUpdateListener l) {
        synchronized (this) {
            this.mListeners.remove(l);
        }
    }

    Handler handler() {
        return this.mHandler;
    }

    @UnsupportedAppUsage
    private static void postEventFromNative(Object module_ref, int what, int arg1, int arg2, Object obj) {
        Handler handler;
        AudioPortEventHandler eventHandler = (AudioPortEventHandler) ((WeakReference) module_ref).get();
        if (eventHandler != null && (handler = eventHandler.handler()) != null) {
            Message m = handler.obtainMessage(what, arg1, arg2, obj);
            if (what != 4) {
                handler.removeMessages(what);
            }
            handler.sendMessage(m);
        }
    }
}
