package android.hardware.radio;

import android.hardware.radio.ITunerCallback;
import android.hardware.radio.ProgramList;
import android.hardware.radio.RadioManager;
import android.hardware.radio.RadioTuner;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class TunerCallbackAdapter extends ITunerCallback.Stub {
    private static final String TAG = "BroadcastRadio.TunerCallbackAdapter";
    private final RadioTuner.Callback mCallback;
    RadioManager.ProgramInfo mCurrentProgramInfo;
    private final Handler mHandler;
    List<RadioManager.ProgramInfo> mLastCompleteList;
    ProgramList mProgramList;
    private final Object mLock = new Object();
    boolean mIsAntennaConnected = true;
    private boolean mDelayedCompleteCallback = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TunerCallbackAdapter(RadioTuner.Callback callback, Handler handler) {
        this.mCallback = callback;
        if (handler == null) {
            this.mHandler = new Handler(Looper.getMainLooper());
        } else {
            this.mHandler = handler;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        synchronized (this.mLock) {
            if (this.mProgramList != null) {
                this.mProgramList.close();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setProgramListObserver(final ProgramList programList, final ProgramList.OnCloseListener closeListener) {
        Objects.requireNonNull(closeListener);
        synchronized (this.mLock) {
            if (this.mProgramList != null) {
                Log.w(TAG, "Previous program list observer wasn't properly closed, closing it...");
                this.mProgramList.close();
            }
            this.mProgramList = programList;
            if (programList == null) {
                return;
            }
            programList.setOnCloseListener(new ProgramList.OnCloseListener() { // from class: android.hardware.radio.-$$Lambda$TunerCallbackAdapter$Hl80-0ppQ17uTjZuGamwBQMrO6Y
                @Override // android.hardware.radio.ProgramList.OnCloseListener
                public final void onClose() {
                    TunerCallbackAdapter.lambda$setProgramListObserver$0(TunerCallbackAdapter.this, programList, closeListener);
                }
            });
            programList.addOnCompleteListener(new ProgramList.OnCompleteListener() { // from class: android.hardware.radio.-$$Lambda$TunerCallbackAdapter$V-mJUy8dIlOVjsZ1ckkgn490jFI
                @Override // android.hardware.radio.ProgramList.OnCompleteListener
                public final void onComplete() {
                    TunerCallbackAdapter.lambda$setProgramListObserver$1(TunerCallbackAdapter.this, programList);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$setProgramListObserver$0(TunerCallbackAdapter tunerCallbackAdapter, ProgramList programList, ProgramList.OnCloseListener closeListener) {
        synchronized (tunerCallbackAdapter.mLock) {
            if (tunerCallbackAdapter.mProgramList != programList) {
                return;
            }
            tunerCallbackAdapter.mProgramList = null;
            tunerCallbackAdapter.mLastCompleteList = null;
            closeListener.onClose();
        }
    }

    public static /* synthetic */ void lambda$setProgramListObserver$1(TunerCallbackAdapter tunerCallbackAdapter, ProgramList programList) {
        synchronized (tunerCallbackAdapter.mLock) {
            if (tunerCallbackAdapter.mProgramList != programList) {
                return;
            }
            tunerCallbackAdapter.mLastCompleteList = programList.toList();
            if (tunerCallbackAdapter.mDelayedCompleteCallback) {
                Log.d(TAG, "Sending delayed onBackgroundScanComplete callback");
                tunerCallbackAdapter.sendBackgroundScanCompleteLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<RadioManager.ProgramInfo> getLastCompleteList() {
        List<RadioManager.ProgramInfo> list;
        synchronized (this.mLock) {
            list = this.mLastCompleteList;
        }
        return list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearLastCompleteList() {
        synchronized (this.mLock) {
            this.mLastCompleteList = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RadioManager.ProgramInfo getCurrentProgramInformation() {
        RadioManager.ProgramInfo programInfo;
        synchronized (this.mLock) {
            programInfo = this.mCurrentProgramInfo;
        }
        return programInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAntennaConnected() {
        return this.mIsAntennaConnected;
    }

    @Override // android.hardware.radio.ITunerCallback
    public void onError(final int status) {
        this.mHandler.post(new Runnable() { // from class: android.hardware.radio.-$$Lambda$TunerCallbackAdapter$jl29exheqPoYrltfLs9fLsjsI1A
            @Override // java.lang.Runnable
            public final void run() {
                TunerCallbackAdapter.this.mCallback.onError(status);
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x001f, code lost:
        if (r4 != (-1)) goto L13;
     */
    @Override // android.hardware.radio.ITunerCallback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onTuneFailed(final int r4, final android.hardware.radio.ProgramSelector r5) {
        /*
            r3 = this;
            android.os.Handler r0 = r3.mHandler
            android.hardware.radio.-$$Lambda$TunerCallbackAdapter$Hj_P___HTEx_8p7qvYVPXmhwu7w r1 = new android.hardware.radio.-$$Lambda$TunerCallbackAdapter$Hj_P___HTEx_8p7qvYVPXmhwu7w
            r1.<init>()
            r0.post(r1)
            r0 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r4 == r0) goto L24
            r0 = -38
            if (r4 == r0) goto L24
            r0 = -32
            if (r4 == r0) goto L22
            r0 = -22
            if (r4 == r0) goto L24
            r0 = -19
            if (r4 == r0) goto L24
            r0 = -1
            if (r4 == r0) goto L22
            goto L3f
        L22:
            r0 = 1
            goto L40
        L24:
            java.lang.String r0 = "BroadcastRadio.TunerCallbackAdapter"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Got an error with no mapping to the legacy API ("
            r1.append(r2)
            r1.append(r4)
            java.lang.String r2 = "), doing a best-effort conversion to ERROR_SCAN_TIMEOUT"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.i(r0, r1)
        L3f:
            r0 = 3
        L40:
            android.os.Handler r1 = r3.mHandler
            android.hardware.radio.-$$Lambda$TunerCallbackAdapter$HcS5_voI1xju970_jCP6Iz0LgPE r2 = new android.hardware.radio.-$$Lambda$TunerCallbackAdapter$HcS5_voI1xju970_jCP6Iz0LgPE
            r2.<init>()
            r1.post(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.radio.TunerCallbackAdapter.onTuneFailed(int, android.hardware.radio.ProgramSelector):void");
    }

    @Override // android.hardware.radio.ITunerCallback
    public void onConfigurationChanged(final RadioManager.BandConfig config) {
        this.mHandler.post(new Runnable() { // from class: android.hardware.radio.-$$Lambda$TunerCallbackAdapter$B4BuskgdSatf-Xt5wzgLniEltQk
            @Override // java.lang.Runnable
            public final void run() {
                TunerCallbackAdapter.this.mCallback.onConfigurationChanged(config);
            }
        });
    }

    @Override // android.hardware.radio.ITunerCallback
    public void onCurrentProgramInfoChanged(final RadioManager.ProgramInfo info) {
        if (info == null) {
            Log.e(TAG, "ProgramInfo must not be null");
            return;
        }
        synchronized (this.mLock) {
            this.mCurrentProgramInfo = info;
        }
        this.mHandler.post(new Runnable() { // from class: android.hardware.radio.-$$Lambda$TunerCallbackAdapter$RSNrzX5-O3nayC2_jg0kAR6KkKY
            @Override // java.lang.Runnable
            public final void run() {
                TunerCallbackAdapter.lambda$onCurrentProgramInfoChanged$6(TunerCallbackAdapter.this, info);
            }
        });
    }

    public static /* synthetic */ void lambda$onCurrentProgramInfoChanged$6(TunerCallbackAdapter tunerCallbackAdapter, RadioManager.ProgramInfo info) {
        tunerCallbackAdapter.mCallback.onProgramInfoChanged(info);
        RadioMetadata metadata = info.getMetadata();
        if (metadata != null) {
            tunerCallbackAdapter.mCallback.onMetadataChanged(metadata);
        }
    }

    @Override // android.hardware.radio.ITunerCallback
    public void onTrafficAnnouncement(final boolean active) {
        this.mHandler.post(new Runnable() { // from class: android.hardware.radio.-$$Lambda$TunerCallbackAdapter$tiaoLZrR2K56rYeqHvSRh5lRdBI
            @Override // java.lang.Runnable
            public final void run() {
                TunerCallbackAdapter.this.mCallback.onTrafficAnnouncement(active);
            }
        });
    }

    @Override // android.hardware.radio.ITunerCallback
    public void onEmergencyAnnouncement(final boolean active) {
        this.mHandler.post(new Runnable() { // from class: android.hardware.radio.-$$Lambda$TunerCallbackAdapter$ZwPm3xxjeLvbP12KweyzqFJVnj4
            @Override // java.lang.Runnable
            public final void run() {
                TunerCallbackAdapter.this.mCallback.onEmergencyAnnouncement(active);
            }
        });
    }

    @Override // android.hardware.radio.ITunerCallback
    public void onAntennaState(final boolean connected) {
        this.mIsAntennaConnected = connected;
        this.mHandler.post(new Runnable() { // from class: android.hardware.radio.-$$Lambda$TunerCallbackAdapter$dR-VQmFrL_tBD2wpNvborTd8W08
            @Override // java.lang.Runnable
            public final void run() {
                TunerCallbackAdapter.this.mCallback.onAntennaState(connected);
            }
        });
    }

    @Override // android.hardware.radio.ITunerCallback
    public void onBackgroundScanAvailabilityChange(final boolean isAvailable) {
        this.mHandler.post(new Runnable() { // from class: android.hardware.radio.-$$Lambda$TunerCallbackAdapter$4zf9n0sz_rU8z6a9GJmRInWrYkQ
            @Override // java.lang.Runnable
            public final void run() {
                TunerCallbackAdapter.this.mCallback.onBackgroundScanAvailabilityChange(isAvailable);
            }
        });
    }

    private void sendBackgroundScanCompleteLocked() {
        this.mDelayedCompleteCallback = false;
        this.mHandler.post(new Runnable() { // from class: android.hardware.radio.-$$Lambda$TunerCallbackAdapter$xIUT1Qu5TkA83V8ttYy1zv-JuFo
            @Override // java.lang.Runnable
            public final void run() {
                TunerCallbackAdapter.this.mCallback.onBackgroundScanComplete();
            }
        });
    }

    @Override // android.hardware.radio.ITunerCallback
    public void onBackgroundScanComplete() {
        synchronized (this.mLock) {
            if (this.mLastCompleteList == null) {
                Log.i(TAG, "Got onBackgroundScanComplete callback, but the program list didn't get through yet. Delaying it...");
                this.mDelayedCompleteCallback = true;
                return;
            }
            sendBackgroundScanCompleteLocked();
        }
    }

    @Override // android.hardware.radio.ITunerCallback
    public void onProgramListChanged() {
        this.mHandler.post(new Runnable() { // from class: android.hardware.radio.-$$Lambda$TunerCallbackAdapter$UsmGhKordXy4lhCylRP0mm2NcYc
            @Override // java.lang.Runnable
            public final void run() {
                TunerCallbackAdapter.this.mCallback.onProgramListChanged();
            }
        });
    }

    @Override // android.hardware.radio.ITunerCallback
    public void onProgramListUpdated(ProgramList.Chunk chunk) {
        synchronized (this.mLock) {
            if (this.mProgramList == null) {
                return;
            }
            this.mProgramList.apply((ProgramList.Chunk) Objects.requireNonNull(chunk));
        }
    }

    @Override // android.hardware.radio.ITunerCallback
    public void onParametersUpdated(final Map parameters) {
        this.mHandler.post(new Runnable() { // from class: android.hardware.radio.-$$Lambda$TunerCallbackAdapter$Yz-4KCDu1MOynGdkDf_oMxqhjeY
            @Override // java.lang.Runnable
            public final void run() {
                TunerCallbackAdapter.this.mCallback.onParametersUpdated(parameters);
            }
        });
    }
}
