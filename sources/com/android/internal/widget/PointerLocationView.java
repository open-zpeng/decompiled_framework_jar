package com.android.internal.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.input.InputManager;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Slog;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManagerPolicyConstants;
import java.util.ArrayList;
/* loaded from: classes3.dex */
public class PointerLocationView extends View implements InputManager.InputDeviceListener, WindowManagerPolicyConstants.PointerEventListener {
    private static final String ALT_STRATEGY_PROPERY_KEY = "debug.velocitytracker.alt";
    private static final String TAG = "Pointer";
    private final int ESTIMATE_FUTURE_POINTS;
    private final float ESTIMATE_INTERVAL;
    private final int ESTIMATE_PAST_POINTS;
    private int mActivePointerId;
    private final VelocityTracker mAltVelocity;
    private boolean mCurDown;
    private int mCurNumPointers;
    private final Paint mCurrentPointPaint;
    private int mHeaderBottom;
    private final InputManager mIm;
    private int mMaxNumPointers;
    private final Paint mPaint;
    private final Paint mPathPaint;
    private final ArrayList<PointerState> mPointers;
    private boolean mPrintCoords;
    private RectF mReusableOvalRect;
    private final Paint mTargetPaint;
    private final MotionEvent.PointerCoords mTempCoords;
    private final FasterStringBuilder mText;
    private final Paint mTextBackgroundPaint;
    private final Paint mTextLevelPaint;
    private final Paint.FontMetricsInt mTextMetrics;
    private final Paint mTextPaint;
    private final ViewConfiguration mVC;
    private final VelocityTracker mVelocity;

    /* loaded from: classes3.dex */
    public static class PointerState {
        private float mAltXVelocity;
        private float mAltYVelocity;
        private float mBoundingBottom;
        private float mBoundingLeft;
        private float mBoundingRight;
        private float mBoundingTop;
        private boolean mCurDown;
        private boolean mHasBoundingBox;
        private int mToolType;
        private int mTraceCount;
        private float mXVelocity;
        private float mYVelocity;
        private float[] mTraceX = new float[32];
        private float[] mTraceY = new float[32];
        private boolean[] mTraceCurrent = new boolean[32];
        private MotionEvent.PointerCoords mCoords = new MotionEvent.PointerCoords();
        private VelocityTracker.Estimator mEstimator = new VelocityTracker.Estimator();
        private VelocityTracker.Estimator mAltEstimator = new VelocityTracker.Estimator();

        public void clearTrace() {
            this.mTraceCount = 0;
        }

        public void addTrace(float x, float y, boolean current) {
            int traceCapacity = this.mTraceX.length;
            if (this.mTraceCount == traceCapacity) {
                int traceCapacity2 = traceCapacity * 2;
                float[] newTraceX = new float[traceCapacity2];
                System.arraycopy(this.mTraceX, 0, newTraceX, 0, this.mTraceCount);
                this.mTraceX = newTraceX;
                float[] newTraceY = new float[traceCapacity2];
                System.arraycopy(this.mTraceY, 0, newTraceY, 0, this.mTraceCount);
                this.mTraceY = newTraceY;
                boolean[] newTraceCurrent = new boolean[traceCapacity2];
                System.arraycopy(this.mTraceCurrent, 0, newTraceCurrent, 0, this.mTraceCount);
                this.mTraceCurrent = newTraceCurrent;
            }
            this.mTraceX[this.mTraceCount] = x;
            this.mTraceY[this.mTraceCount] = y;
            this.mTraceCurrent[this.mTraceCount] = current;
            this.mTraceCount++;
        }
    }

    public PointerLocationView(Context c) {
        super(c);
        this.ESTIMATE_PAST_POINTS = 4;
        this.ESTIMATE_FUTURE_POINTS = 2;
        this.ESTIMATE_INTERVAL = 0.02f;
        this.mTextMetrics = new Paint.FontMetricsInt();
        this.mPointers = new ArrayList<>();
        this.mTempCoords = new MotionEvent.PointerCoords();
        this.mText = new FasterStringBuilder();
        this.mPrintCoords = true;
        this.mReusableOvalRect = new RectF();
        setFocusableInTouchMode(true);
        this.mIm = (InputManager) c.getSystemService(InputManager.class);
        this.mVC = ViewConfiguration.get(c);
        this.mTextPaint = new Paint();
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextSize(10.0f * getResources().getDisplayMetrics().density);
        this.mTextPaint.setARGB(255, 0, 0, 0);
        this.mTextBackgroundPaint = new Paint();
        this.mTextBackgroundPaint.setAntiAlias(false);
        this.mTextBackgroundPaint.setARGB(128, 255, 255, 255);
        this.mTextLevelPaint = new Paint();
        this.mTextLevelPaint.setAntiAlias(false);
        this.mTextLevelPaint.setARGB(192, 255, 0, 0);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setARGB(255, 255, 255, 255);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(2.0f);
        this.mCurrentPointPaint = new Paint();
        this.mCurrentPointPaint.setAntiAlias(true);
        this.mCurrentPointPaint.setARGB(255, 255, 0, 0);
        this.mCurrentPointPaint.setStyle(Paint.Style.STROKE);
        this.mCurrentPointPaint.setStrokeWidth(2.0f);
        this.mTargetPaint = new Paint();
        this.mTargetPaint.setAntiAlias(false);
        this.mTargetPaint.setARGB(255, 0, 0, 192);
        this.mPathPaint = new Paint();
        this.mPathPaint.setAntiAlias(false);
        this.mPathPaint.setARGB(255, 0, 96, 255);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(1.0f);
        PointerState ps = new PointerState();
        this.mPointers.add(ps);
        this.mActivePointerId = 0;
        this.mVelocity = VelocityTracker.obtain();
        String altStrategy = SystemProperties.get(ALT_STRATEGY_PROPERY_KEY);
        if (altStrategy.length() != 0) {
            Log.d(TAG, "Comparing default velocity tracker strategy with " + altStrategy);
            this.mAltVelocity = VelocityTracker.obtain(altStrategy);
            return;
        }
        this.mAltVelocity = null;
    }

    public void setPrintCoords(boolean state) {
        this.mPrintCoords = state;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mTextPaint.getFontMetricsInt(this.mTextMetrics);
        this.mHeaderBottom = (-this.mTextMetrics.ascent) + this.mTextMetrics.descent + 2;
    }

    private void drawOval(Canvas canvas, float x, float y, float major, float minor, float angle, Paint paint) {
        canvas.save(1);
        canvas.rotate((float) ((180.0f * angle) / 3.141592653589793d), x, y);
        this.mReusableOvalRect.left = x - (minor / 2.0f);
        this.mReusableOvalRect.right = (minor / 2.0f) + x;
        this.mReusableOvalRect.top = y - (major / 2.0f);
        this.mReusableOvalRect.bottom = (major / 2.0f) + y;
        canvas.drawOval(this.mReusableOvalRect, paint);
        canvas.restore();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:70:0x05c7  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x05e9 A[SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onDraw(android.graphics.Canvas r37) {
        /*
            Method dump skipped, instructions count: 1534
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.PointerLocationView.onDraw(android.graphics.Canvas):void");
    }

    private void logMotionEvent(String type, MotionEvent event) {
        int action = event.getAction();
        int N = event.getHistorySize();
        int NI = event.getPointerCount();
        int i = 0;
        while (true) {
            int historyPos = i;
            if (historyPos >= N) {
                break;
            }
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < NI) {
                    int id = event.getPointerId(i3);
                    event.getHistoricalPointerCoords(i3, historyPos, this.mTempCoords);
                    logCoords(type, action, i3, this.mTempCoords, id, event);
                    i2 = i3 + 1;
                }
            }
            i = historyPos + 1;
        }
        for (int i4 = 0; i4 < NI; i4++) {
            int id2 = event.getPointerId(i4);
            event.getPointerCoords(i4, this.mTempCoords);
            logCoords(type, action, i4, this.mTempCoords, id2, event);
        }
    }

    private void logCoords(String type, int action, int index, MotionEvent.PointerCoords coords, int id, MotionEvent event) {
        String prefix;
        int toolType = event.getToolType(index);
        int buttonState = event.getButtonState();
        switch (action & 255) {
            case 0:
                prefix = "DOWN";
                break;
            case 1:
                prefix = "UP";
                break;
            case 2:
                prefix = "MOVE";
                break;
            case 3:
                prefix = "CANCEL";
                break;
            case 4:
                prefix = "OUTSIDE";
                break;
            case 5:
                if (index == ((action & 65280) >> 8)) {
                    prefix = "DOWN";
                    break;
                } else {
                    prefix = "MOVE";
                    break;
                }
            case 6:
                if (index == ((action & 65280) >> 8)) {
                    prefix = "UP";
                    break;
                } else {
                    prefix = "MOVE";
                    break;
                }
            case 7:
                prefix = "HOVER MOVE";
                break;
            case 8:
                prefix = "SCROLL";
                break;
            case 9:
                prefix = "HOVER ENTER";
                break;
            case 10:
                prefix = "HOVER EXIT";
                break;
            default:
                prefix = Integer.toString(action);
                break;
        }
        Log.i(TAG, this.mText.clear().append(type).append(" id ").append(id + 1).append(": ").append(prefix).append(" (").append(coords.x, 3).append(", ").append(coords.y, 3).append(") Pressure=").append(coords.pressure, 3).append(" Size=").append(coords.size, 3).append(" TouchMajor=").append(coords.touchMajor, 3).append(" TouchMinor=").append(coords.touchMinor, 3).append(" ToolMajor=").append(coords.toolMajor, 3).append(" ToolMinor=").append(coords.toolMinor, 3).append(" Orientation=").append((float) ((coords.orientation * 180.0f) / 3.141592653589793d), 1).append("deg").append(" Tilt=").append((float) ((coords.getAxisValue(25) * 180.0f) / 3.141592653589793d), 1).append("deg").append(" Distance=").append(coords.getAxisValue(24), 1).append(" VScroll=").append(coords.getAxisValue(9), 1).append(" HScroll=").append(coords.getAxisValue(10), 1).append(" BoundingBox=[(").append(event.getAxisValue(32), 3).append(", ").append(event.getAxisValue(33), 3).append(")").append(", (").append(event.getAxisValue(34), 3).append(", ").append(event.getAxisValue(35), 3).append(")]").append(" ToolType=").append(MotionEvent.toolTypeToString(toolType)).append(" ButtonState=").append(MotionEvent.buttonStateToString(buttonState)).toString());
    }

    @Override // android.view.WindowManagerPolicyConstants.PointerEventListener
    public void onPointerEvent(MotionEvent event) {
        MotionEvent.PointerCoords coords;
        PointerState ps;
        int id;
        char c;
        MotionEvent.PointerCoords coords2;
        PointerState ps2;
        int i;
        int historyPos;
        int N;
        int action = event.getAction();
        int NP = this.mPointers.size();
        if (action == 0 || (action & 255) == 5) {
            int index = (action & 65280) >> 8;
            if (action == 0) {
                for (int p = 0; p < NP; p++) {
                    PointerState ps3 = this.mPointers.get(p);
                    ps3.clearTrace();
                    ps3.mCurDown = false;
                }
                this.mCurDown = true;
                this.mCurNumPointers = 0;
                this.mMaxNumPointers = 0;
                this.mVelocity.clear();
                if (this.mAltVelocity != null) {
                    this.mAltVelocity.clear();
                }
            }
            this.mCurNumPointers++;
            if (this.mMaxNumPointers < this.mCurNumPointers) {
                this.mMaxNumPointers = this.mCurNumPointers;
            }
            int id2 = event.getPointerId(index);
            while (NP <= id2) {
                this.mPointers.add(new PointerState());
                NP++;
            }
            if (this.mActivePointerId < 0 || !this.mPointers.get(this.mActivePointerId).mCurDown) {
                this.mActivePointerId = id2;
            }
            PointerState ps4 = this.mPointers.get(id2);
            ps4.mCurDown = true;
            InputDevice device = InputDevice.getDevice(event.getDeviceId());
            ps4.mHasBoundingBox = (device == null || device.getMotionRange(32) == null) ? false : true;
        }
        int NP2 = NP;
        int NI = event.getPointerCount();
        this.mVelocity.addMovement(event);
        this.mVelocity.computeCurrentVelocity(1);
        if (this.mAltVelocity != null) {
            this.mAltVelocity.addMovement(event);
            this.mAltVelocity.computeCurrentVelocity(1);
        }
        int N2 = event.getHistorySize();
        int historyPos2 = 0;
        while (true) {
            int historyPos3 = historyPos2;
            if (historyPos3 >= N2) {
                break;
            }
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < NI) {
                    int id3 = event.getPointerId(i3);
                    PointerState ps5 = this.mCurDown ? this.mPointers.get(id3) : null;
                    MotionEvent.PointerCoords coords3 = ps5 != null ? ps5.mCoords : this.mTempCoords;
                    event.getHistoricalPointerCoords(i3, historyPos3, coords3);
                    if (this.mPrintCoords) {
                        coords2 = coords3;
                        ps2 = ps5;
                        i = i3;
                        historyPos = historyPos3;
                        N = N2;
                        logCoords(TAG, action, i3, coords2, id3, event);
                    } else {
                        coords2 = coords3;
                        ps2 = ps5;
                        i = i3;
                        historyPos = historyPos3;
                        N = N2;
                    }
                    if (ps2 != null) {
                        MotionEvent.PointerCoords coords4 = coords2;
                        ps2.addTrace(coords4.x, coords4.y, false);
                    }
                    i2 = i + 1;
                    historyPos3 = historyPos;
                    N2 = N;
                }
            }
            historyPos2 = historyPos3 + 1;
        }
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 >= NI) {
                break;
            }
            int id4 = event.getPointerId(i5);
            PointerState ps6 = this.mCurDown ? this.mPointers.get(id4) : null;
            MotionEvent.PointerCoords coords5 = ps6 != null ? ps6.mCoords : this.mTempCoords;
            event.getPointerCoords(i5, coords5);
            if (this.mPrintCoords) {
                coords = coords5;
                ps = ps6;
                id = id4;
                logCoords(TAG, action, i5, coords5, id4, event);
            } else {
                coords = coords5;
                ps = ps6;
                id = id4;
            }
            if (ps != null) {
                MotionEvent.PointerCoords coords6 = coords;
                ps.addTrace(coords6.x, coords6.y, true);
                ps.mXVelocity = this.mVelocity.getXVelocity(id);
                ps.mYVelocity = this.mVelocity.getYVelocity(id);
                this.mVelocity.getEstimator(id, ps.mEstimator);
                if (this.mAltVelocity != null) {
                    ps.mAltXVelocity = this.mAltVelocity.getXVelocity(id);
                    ps.mAltYVelocity = this.mAltVelocity.getYVelocity(id);
                    this.mAltVelocity.getEstimator(id, ps.mAltEstimator);
                }
                ps.mToolType = event.getToolType(i5);
                if (ps.mHasBoundingBox) {
                    c = ' ';
                    ps.mBoundingLeft = event.getAxisValue(32, i5);
                    ps.mBoundingTop = event.getAxisValue(33, i5);
                    ps.mBoundingRight = event.getAxisValue(34, i5);
                    ps.mBoundingBottom = event.getAxisValue(35, i5);
                    i4 = i5 + 1;
                }
            }
            c = ' ';
            i4 = i5 + 1;
        }
        if (action == 1 || action == 3 || (action & 255) == 6) {
            int index2 = (65280 & action) >> 8;
            int id5 = event.getPointerId(index2);
            if (id5 >= NP2) {
                Slog.wtf(TAG, "Got pointer ID out of bounds: id=" + id5 + " arraysize=" + NP2 + " pointerindex=" + index2 + " action=0x" + Integer.toHexString(action));
                return;
            }
            PointerState ps7 = this.mPointers.get(id5);
            ps7.mCurDown = false;
            if (action == 1 || action == 3) {
                this.mCurDown = false;
                this.mCurNumPointers = 0;
            } else {
                this.mCurNumPointers--;
                if (this.mActivePointerId == id5) {
                    this.mActivePointerId = event.getPointerId(index2 != 0 ? 0 : 1);
                }
                ps7.addTrace(Float.NaN, Float.NaN, false);
            }
        }
        invalidate();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        onPointerEvent(event);
        if (event.getAction() == 0 && !isFocused()) {
            requestFocus();
            return true;
        }
        return true;
    }

    @Override // android.view.View
    public boolean onGenericMotionEvent(MotionEvent event) {
        int source = event.getSource();
        if ((source & 2) != 0) {
            onPointerEvent(event);
            return true;
        } else if ((source & 16) != 0) {
            logMotionEvent("Joystick", event);
            return true;
        } else if ((source & 8) != 0) {
            logMotionEvent("Position", event);
            return true;
        } else {
            logMotionEvent("Generic", event);
            return true;
        }
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (shouldLogKey(keyCode)) {
            int repeatCount = event.getRepeatCount();
            if (repeatCount == 0) {
                Log.i(TAG, "Key Down: " + event);
                return true;
            }
            Log.i(TAG, "Key Repeat #" + repeatCount + ": " + event);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (shouldLogKey(keyCode)) {
            Log.i(TAG, "Key Up: " + event);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private static boolean shouldLogKey(int keyCode) {
        switch (keyCode) {
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                return true;
            default:
                if (KeyEvent.isGamepadButton(keyCode) || KeyEvent.isModifierKey(keyCode)) {
                    return true;
                }
                return false;
        }
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent event) {
        logMotionEvent("Trackball", event);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mIm.registerInputDeviceListener(this, getHandler());
        logInputDevices();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mIm.unregisterInputDeviceListener(this);
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceAdded(int deviceId) {
        logInputDeviceState(deviceId, "Device Added");
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceChanged(int deviceId) {
        logInputDeviceState(deviceId, "Device Changed");
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceRemoved(int deviceId) {
        logInputDeviceState(deviceId, "Device Removed");
    }

    private void logInputDevices() {
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int i : deviceIds) {
            logInputDeviceState(i, "Device Enumerated");
        }
    }

    private void logInputDeviceState(int deviceId, String state) {
        InputDevice device = this.mIm.getInputDevice(deviceId);
        if (device != null) {
            Log.i(TAG, state + ": " + device);
            return;
        }
        Log.i(TAG, state + ": " + deviceId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class FasterStringBuilder {
        private char[] mChars = new char[64];
        private int mLength;

        public FasterStringBuilder clear() {
            this.mLength = 0;
            return this;
        }

        public FasterStringBuilder append(String value) {
            int valueLength = value.length();
            int index = reserve(valueLength);
            value.getChars(0, valueLength, this.mChars, index);
            this.mLength += valueLength;
            return this;
        }

        public FasterStringBuilder append(int value) {
            return append(value, 0);
        }

        public FasterStringBuilder append(int value, int zeroPadWidth) {
            int index;
            boolean negative = value < 0;
            if (negative && (value = -value) < 0) {
                append("-2147483648");
                return this;
            }
            int index2 = reserve(11);
            char[] chars = this.mChars;
            if (value == 0) {
                int i = index2 + 1;
                chars[index2] = '0';
                this.mLength++;
                return this;
            }
            if (negative) {
                index = index2 + 1;
                chars[index2] = '-';
            } else {
                index = index2;
            }
            int divisor = 1000000000;
            int index3 = index;
            int numberWidth = 10;
            while (value < divisor) {
                divisor /= 10;
                numberWidth--;
                if (numberWidth < zeroPadWidth) {
                    chars[index3] = '0';
                    index3++;
                }
            }
            while (true) {
                int digit = value / divisor;
                value -= digit * divisor;
                divisor /= 10;
                int index4 = index3 + 1;
                chars[index3] = (char) (digit + 48);
                if (divisor != 0) {
                    index3 = index4;
                } else {
                    this.mLength = index4;
                    return this;
                }
            }
        }

        public FasterStringBuilder append(float value, int precision) {
            int scale = 1;
            for (int i = 0; i < precision; i++) {
                scale *= 10;
            }
            float value2 = (float) (Math.rint(scale * value) / scale);
            append((int) value2);
            if (precision != 0) {
                append(".");
                float value3 = Math.abs(value2);
                append((int) (scale * ((float) (value3 - Math.floor(value3)))), precision);
            }
            return this;
        }

        public String toString() {
            return new String(this.mChars, 0, this.mLength);
        }

        private int reserve(int length) {
            int oldLength = this.mLength;
            int newLength = this.mLength + length;
            char[] oldChars = this.mChars;
            int oldCapacity = oldChars.length;
            if (newLength > oldCapacity) {
                int newCapacity = oldCapacity * 2;
                char[] newChars = new char[newCapacity];
                System.arraycopy(oldChars, 0, newChars, 0, oldLength);
                this.mChars = newChars;
            }
            return oldLength;
        }
    }
}
