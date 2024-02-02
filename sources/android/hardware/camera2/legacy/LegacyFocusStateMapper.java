package android.hardware.camera2.legacy;

import android.hardware.Camera;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.utils.ParamsUtils;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.Objects;
/* loaded from: classes.dex */
public class LegacyFocusStateMapper {
    public protected static final boolean DEBUG = false;
    public protected static String TAG = "LegacyFocusStateMapper";
    public protected final Camera mCamera;
    public protected int mAfStatePrevious = 0;
    public protected String mAfModePrevious = null;
    public protected final Object mLock = new Object();
    public protected int mAfRun = 0;
    public protected int mAfState = 0;

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized LegacyFocusStateMapper(Camera camera) {
        this.mCamera = (Camera) Preconditions.checkNotNull(camera, "camera must not be null");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processRequestTriggers(CaptureRequest captureRequest, Camera.Parameters parameters) {
        final int currentAfRun;
        boolean z;
        final int currentAfRun2;
        Preconditions.checkNotNull(captureRequest, "captureRequest must not be null");
        int afStateAfterStart = 0;
        int afTrigger = ((Integer) ParamsUtils.getOrDefault(captureRequest, CaptureRequest.CONTROL_AF_TRIGGER, 0)).intValue();
        final String afMode = parameters.getFocusMode();
        if (!Objects.equals(this.mAfModePrevious, afMode)) {
            synchronized (this.mLock) {
                this.mAfRun++;
                this.mAfState = 0;
            }
            this.mCamera.cancelAutoFocus();
        }
        this.mAfModePrevious = afMode;
        synchronized (this.mLock) {
            currentAfRun = this.mAfRun;
        }
        Camera.AutoFocusMoveCallback afMoveCallback = new Camera.AutoFocusMoveCallback() { // from class: android.hardware.camera2.legacy.LegacyFocusStateMapper.1
            /* JADX WARN: Removed duplicated region for block: B:26:0x005e A[Catch: all -> 0x0082, TryCatch #0 {, blocks: (B:4:0x0007, B:6:0x0011, B:7:0x002c, B:13:0x0035, B:25:0x005b, B:26:0x005e, B:28:0x0064, B:29:0x007b, B:30:0x0080, B:18:0x0047, B:21:0x0051), top: B:35:0x0007 }] */
            /* JADX WARN: Removed duplicated region for block: B:27:0x0063  */
            @Override // android.hardware.Camera.AutoFocusMoveCallback
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onAutoFocusMoving(boolean r9, android.hardware.Camera r10) {
                /*
                    r8 = this;
                    android.hardware.camera2.legacy.LegacyFocusStateMapper r0 = android.hardware.camera2.legacy.LegacyFocusStateMapper.this
                    java.lang.Object r0 = android.hardware.camera2.legacy.LegacyFocusStateMapper.access$000(r0)
                    monitor-enter(r0)
                    android.hardware.camera2.legacy.LegacyFocusStateMapper r1 = android.hardware.camera2.legacy.LegacyFocusStateMapper.this     // Catch: java.lang.Throwable -> L82
                    int r1 = android.hardware.camera2.legacy.LegacyFocusStateMapper.access$100(r1)     // Catch: java.lang.Throwable -> L82
                    int r2 = r2     // Catch: java.lang.Throwable -> L82
                    if (r2 == r1) goto L2e
                    java.lang.String r2 = android.hardware.camera2.legacy.LegacyFocusStateMapper.access$200()     // Catch: java.lang.Throwable -> L82
                    java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L82
                    r3.<init>()     // Catch: java.lang.Throwable -> L82
                    java.lang.String r4 = "onAutoFocusMoving - ignoring move callbacks from old af run"
                    r3.append(r4)     // Catch: java.lang.Throwable -> L82
                    int r4 = r2     // Catch: java.lang.Throwable -> L82
                    r3.append(r4)     // Catch: java.lang.Throwable -> L82
                    java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L82
                    android.util.Log.d(r2, r3)     // Catch: java.lang.Throwable -> L82
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> L82
                    return
                L2e:
                    r2 = 1
                    if (r9 == 0) goto L34
                L32:
                    r3 = r2
                    goto L35
                L34:
                    r3 = 2
                L35:
                    java.lang.String r4 = r3     // Catch: java.lang.Throwable -> L82
                    r5 = -1
                    int r6 = r4.hashCode()     // Catch: java.lang.Throwable -> L82
                    r7 = -194628547(0xfffffffff466343d, float:-7.2954577E31)
                    if (r6 == r7) goto L51
                    r2 = 910005312(0x363d9440, float:2.8249488E-6)
                    if (r6 == r2) goto L47
                    goto L5a
                L47:
                    java.lang.String r2 = "continuous-picture"
                    boolean r2 = r4.equals(r2)     // Catch: java.lang.Throwable -> L82
                    if (r2 == 0) goto L5a
                    r2 = 0
                    goto L5b
                L51:
                    java.lang.String r6 = "continuous-video"
                    boolean r4 = r4.equals(r6)     // Catch: java.lang.Throwable -> L82
                    if (r4 == 0) goto L5a
                    goto L5b
                L5a:
                    r2 = r5
                L5b:
                    switch(r2) {
                        case 0: goto L63;
                        case 1: goto L63;
                        default: goto L5e;
                    }     // Catch: java.lang.Throwable -> L82
                L5e:
                    java.lang.String r2 = android.hardware.camera2.legacy.LegacyFocusStateMapper.access$200()     // Catch: java.lang.Throwable -> L82
                    goto L64
                L63:
                    goto L7b
                L64:
                    java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L82
                    r4.<init>()     // Catch: java.lang.Throwable -> L82
                    java.lang.String r5 = "onAutoFocus - got unexpected onAutoFocus in mode "
                    r4.append(r5)     // Catch: java.lang.Throwable -> L82
                    java.lang.String r5 = r3     // Catch: java.lang.Throwable -> L82
                    r4.append(r5)     // Catch: java.lang.Throwable -> L82
                    java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> L82
                    android.util.Log.w(r2, r4)     // Catch: java.lang.Throwable -> L82
                L7b:
                    android.hardware.camera2.legacy.LegacyFocusStateMapper r2 = android.hardware.camera2.legacy.LegacyFocusStateMapper.this     // Catch: java.lang.Throwable -> L82
                    android.hardware.camera2.legacy.LegacyFocusStateMapper.access$302(r2, r3)     // Catch: java.lang.Throwable -> L82
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> L82
                    return
                L82:
                    r1 = move-exception
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> L82
                    throw r1
                */
                throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.legacy.LegacyFocusStateMapper.AnonymousClass1.onAutoFocusMoving(boolean, android.hardware.Camera):void");
            }
        };
        int hashCode = afMode.hashCode();
        char c = 65535;
        if (hashCode == -194628547) {
            if (afMode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                z = true;
            }
            z = true;
        } else if (hashCode == 3005871) {
            if (afMode.equals("auto")) {
                z = false;
            }
            z = true;
        } else if (hashCode != 103652300) {
            if (hashCode == 910005312 && afMode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                z = true;
            }
            z = true;
        } else {
            if (afMode.equals(Camera.Parameters.FOCUS_MODE_MACRO)) {
                z = true;
            }
            z = true;
        }
        switch (z) {
            case false:
            case true:
            case true:
            case true:
                this.mCamera.setAutoFocusMoveCallback(afMoveCallback);
                break;
        }
        switch (afTrigger) {
            case 0:
                return;
            case 1:
                int hashCode2 = afMode.hashCode();
                if (hashCode2 != -194628547) {
                    if (hashCode2 != 3005871) {
                        if (hashCode2 != 103652300) {
                            if (hashCode2 == 910005312 && afMode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                                c = 2;
                            }
                        } else if (afMode.equals(Camera.Parameters.FOCUS_MODE_MACRO)) {
                            c = 1;
                        }
                    } else if (afMode.equals("auto")) {
                        c = 0;
                    }
                } else if (afMode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                    c = 3;
                }
                switch (c) {
                    case 0:
                    case 1:
                        afStateAfterStart = 3;
                        break;
                    case 2:
                    case 3:
                        afStateAfterStart = 1;
                        break;
                }
                synchronized (this.mLock) {
                    currentAfRun2 = this.mAfRun + 1;
                    this.mAfRun = currentAfRun2;
                    this.mAfState = afStateAfterStart;
                }
                if (afStateAfterStart != 0) {
                    this.mCamera.autoFocus(new Camera.AutoFocusCallback() { // from class: android.hardware.camera2.legacy.LegacyFocusStateMapper.2
                        /* JADX WARN: Removed duplicated region for block: B:35:0x0080 A[Catch: all -> 0x00a4, TryCatch #0 {, blocks: (B:4:0x0007, B:6:0x0014, B:7:0x0032, B:12:0x0039, B:34:0x007d, B:35:0x0080, B:37:0x0086, B:38:0x009d, B:39:0x00a2, B:21:0x0055, B:24:0x005e, B:27:0x0068, B:30:0x0072), top: B:44:0x0007 }] */
                        /* JADX WARN: Removed duplicated region for block: B:36:0x0085  */
                        @Override // android.hardware.Camera.AutoFocusCallback
                        /*
                            Code decompiled incorrectly, please refer to instructions dump.
                            To view partially-correct add '--show-bad-code' argument
                        */
                        public void onAutoFocus(boolean r11, android.hardware.Camera r12) {
                            /*
                                r10 = this;
                                android.hardware.camera2.legacy.LegacyFocusStateMapper r0 = android.hardware.camera2.legacy.LegacyFocusStateMapper.this
                                java.lang.Object r0 = android.hardware.camera2.legacy.LegacyFocusStateMapper.access$000(r0)
                                monitor-enter(r0)
                                android.hardware.camera2.legacy.LegacyFocusStateMapper r1 = android.hardware.camera2.legacy.LegacyFocusStateMapper.this     // Catch: java.lang.Throwable -> La4
                                int r1 = android.hardware.camera2.legacy.LegacyFocusStateMapper.access$100(r1)     // Catch: java.lang.Throwable -> La4
                                int r2 = r2     // Catch: java.lang.Throwable -> La4
                                r3 = 1
                                r4 = 0
                                r5 = 2
                                if (r1 == r2) goto L34
                                java.lang.String r2 = android.hardware.camera2.legacy.LegacyFocusStateMapper.access$200()     // Catch: java.lang.Throwable -> La4
                                java.lang.String r6 = "onAutoFocus - ignoring AF callback (old run %d, new run %d)"
                                java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch: java.lang.Throwable -> La4
                                int r7 = r2     // Catch: java.lang.Throwable -> La4
                                java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch: java.lang.Throwable -> La4
                                r5[r4] = r7     // Catch: java.lang.Throwable -> La4
                                java.lang.Integer r4 = java.lang.Integer.valueOf(r1)     // Catch: java.lang.Throwable -> La4
                                r5[r3] = r4     // Catch: java.lang.Throwable -> La4
                                java.lang.String r3 = java.lang.String.format(r6, r5)     // Catch: java.lang.Throwable -> La4
                                android.util.Log.d(r2, r3)     // Catch: java.lang.Throwable -> La4
                                monitor-exit(r0)     // Catch: java.lang.Throwable -> La4
                                return
                            L34:
                                if (r11 == 0) goto L38
                                r2 = 4
                                goto L39
                            L38:
                                r2 = 5
                            L39:
                                java.lang.String r6 = r3     // Catch: java.lang.Throwable -> La4
                                r7 = -1
                                int r8 = r6.hashCode()     // Catch: java.lang.Throwable -> La4
                                r9 = -194628547(0xfffffffff466343d, float:-7.2954577E31)
                                if (r8 == r9) goto L72
                                r5 = 3005871(0x2dddaf, float:4.212122E-39)
                                if (r8 == r5) goto L68
                                r4 = 103652300(0x62d9bcc, float:3.2652145E-35)
                                if (r8 == r4) goto L5e
                                r4 = 910005312(0x363d9440, float:2.8249488E-6)
                                if (r8 == r4) goto L55
                                goto L7c
                            L55:
                                java.lang.String r4 = "continuous-picture"
                                boolean r4 = r6.equals(r4)     // Catch: java.lang.Throwable -> La4
                                if (r4 == 0) goto L7c
                                goto L7d
                            L5e:
                                java.lang.String r3 = "macro"
                                boolean r3 = r6.equals(r3)     // Catch: java.lang.Throwable -> La4
                                if (r3 == 0) goto L7c
                                r3 = 3
                                goto L7d
                            L68:
                                java.lang.String r3 = "auto"
                                boolean r3 = r6.equals(r3)     // Catch: java.lang.Throwable -> La4
                                if (r3 == 0) goto L7c
                                r3 = r4
                                goto L7d
                            L72:
                                java.lang.String r3 = "continuous-video"
                                boolean r3 = r6.equals(r3)     // Catch: java.lang.Throwable -> La4
                                if (r3 == 0) goto L7c
                                r3 = r5
                                goto L7d
                            L7c:
                                r3 = r7
                            L7d:
                                switch(r3) {
                                    case 0: goto L85;
                                    case 1: goto L85;
                                    case 2: goto L85;
                                    case 3: goto L85;
                                    default: goto L80;
                                }     // Catch: java.lang.Throwable -> La4
                            L80:
                                java.lang.String r3 = android.hardware.camera2.legacy.LegacyFocusStateMapper.access$200()     // Catch: java.lang.Throwable -> La4
                                goto L86
                            L85:
                                goto L9d
                            L86:
                                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La4
                                r4.<init>()     // Catch: java.lang.Throwable -> La4
                                java.lang.String r5 = "onAutoFocus - got unexpected onAutoFocus in mode "
                                r4.append(r5)     // Catch: java.lang.Throwable -> La4
                                java.lang.String r5 = r3     // Catch: java.lang.Throwable -> La4
                                r4.append(r5)     // Catch: java.lang.Throwable -> La4
                                java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> La4
                                android.util.Log.w(r3, r4)     // Catch: java.lang.Throwable -> La4
                            L9d:
                                android.hardware.camera2.legacy.LegacyFocusStateMapper r3 = android.hardware.camera2.legacy.LegacyFocusStateMapper.this     // Catch: java.lang.Throwable -> La4
                                android.hardware.camera2.legacy.LegacyFocusStateMapper.access$302(r3, r2)     // Catch: java.lang.Throwable -> La4
                                monitor-exit(r0)     // Catch: java.lang.Throwable -> La4
                                return
                            La4:
                                r1 = move-exception
                                monitor-exit(r0)     // Catch: java.lang.Throwable -> La4
                                throw r1
                            */
                            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.legacy.LegacyFocusStateMapper.AnonymousClass2.onAutoFocus(boolean, android.hardware.Camera):void");
                        }
                    });
                    return;
                }
                return;
            case 2:
                synchronized (this.mLock) {
                    synchronized (this.mLock) {
                        this.mAfRun++;
                        this.mAfState = 0;
                    }
                    this.mCamera.cancelAutoFocus();
                }
                return;
            default:
                Log.w(TAG, "processRequestTriggers - ignoring unknown control.afTrigger = " + afTrigger);
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void mapResultTriggers(CameraMetadataNative result) {
        int newAfState;
        Preconditions.checkNotNull(result, "result must not be null");
        synchronized (this.mLock) {
            newAfState = this.mAfState;
        }
        result.set((CaptureResult.Key<CaptureResult.Key<Integer>>) CaptureResult.CONTROL_AF_STATE, (CaptureResult.Key<Integer>) Integer.valueOf(newAfState));
        this.mAfStatePrevious = newAfState;
    }

    public protected static synchronized String afStateToString(int afState) {
        switch (afState) {
            case 0:
                return "INACTIVE";
            case 1:
                return "PASSIVE_SCAN";
            case 2:
                return "PASSIVE_FOCUSED";
            case 3:
                return "ACTIVE_SCAN";
            case 4:
                return "FOCUSED_LOCKED";
            case 5:
                return "NOT_FOCUSED_LOCKED";
            case 6:
                return "PASSIVE_UNFOCUSED";
            default:
                return "UNKNOWN(" + afState + ")";
        }
    }
}
