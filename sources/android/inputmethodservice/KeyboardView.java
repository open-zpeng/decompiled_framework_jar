package android.inputmethodservice;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.android.internal.R;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class KeyboardView extends View implements View.OnClickListener {
    private static final int DEBOUNCE_TIME = 70;
    private static final boolean DEBUG = false;
    private static final int DELAY_AFTER_PREVIEW = 70;
    private static final int DELAY_BEFORE_PREVIEW = 0;
    private static final int MSG_LONGPRESS = 4;
    private static final int MSG_REMOVE_PREVIEW = 2;
    private static final int MSG_REPEAT = 3;
    private static final int MSG_SHOW_PREVIEW = 1;
    private static final int MULTITAP_INTERVAL = 800;
    private static final int NOT_A_KEY = -1;
    private static final int REPEAT_INTERVAL = 50;
    private static final int REPEAT_START_DELAY = 400;
    private boolean mAbortKey;
    private AccessibilityManager mAccessibilityManager;
    private AudioManager mAudioManager;
    private float mBackgroundDimAmount;
    private Bitmap mBuffer;
    private Canvas mCanvas;
    private Rect mClipRegion;
    private final int[] mCoordinates;
    private int mCurrentKey;
    private int mCurrentKeyIndex;
    private long mCurrentKeyTime;
    private Rect mDirtyRect;
    private boolean mDisambiguateSwipe;
    private int[] mDistances;
    private int mDownKey;
    private long mDownTime;
    private boolean mDrawPending;
    private GestureDetector mGestureDetector;
    Handler mHandler;
    private boolean mHeadsetRequiredToHearPasswordsAnnounced;
    private boolean mInMultiTap;
    private Keyboard.Key mInvalidatedKey;
    public protected Drawable mKeyBackground;
    private int[] mKeyIndices;
    private int mKeyTextColor;
    private int mKeyTextSize;
    private Keyboard mKeyboard;
    private OnKeyboardActionListener mKeyboardActionListener;
    private boolean mKeyboardChanged;
    private Keyboard.Key[] mKeys;
    public protected int mLabelTextSize;
    private int mLastCodeX;
    private int mLastCodeY;
    private int mLastKey;
    private long mLastKeyTime;
    private long mLastMoveTime;
    private int mLastSentIndex;
    private long mLastTapTime;
    private int mLastX;
    private int mLastY;
    private KeyboardView mMiniKeyboard;
    private Map<Keyboard.Key, View> mMiniKeyboardCache;
    private View mMiniKeyboardContainer;
    private int mMiniKeyboardOffsetX;
    private int mMiniKeyboardOffsetY;
    private boolean mMiniKeyboardOnScreen;
    private int mOldPointerCount;
    private float mOldPointerX;
    private float mOldPointerY;
    private Rect mPadding;
    private Paint mPaint;
    private PopupWindow mPopupKeyboard;
    private int mPopupLayout;
    private View mPopupParent;
    private int mPopupPreviewX;
    private int mPopupPreviewY;
    private int mPopupX;
    private int mPopupY;
    private boolean mPossiblePoly;
    private boolean mPreviewCentered;
    private int mPreviewHeight;
    private StringBuilder mPreviewLabel;
    private int mPreviewOffset;
    private PopupWindow mPreviewPopup;
    public protected TextView mPreviewText;
    private int mPreviewTextSizeLarge;
    private boolean mProximityCorrectOn;
    private int mProximityThreshold;
    private int mRepeatKeyIndex;
    private int mShadowColor;
    private float mShadowRadius;
    private boolean mShowPreview;
    private boolean mShowTouchPoints;
    private int mStartX;
    private int mStartY;
    private int mSwipeThreshold;
    private SwipeTracker mSwipeTracker;
    private int mTapCount;
    private int mVerticalCorrection;
    private static final int[] KEY_DELETE = {-5};
    private static final int[] LONG_PRESSABLE_STATE_SET = {16843324};
    private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    private static int MAX_NEARBY_KEYS = 12;

    /* loaded from: classes.dex */
    public interface OnKeyboardActionListener {
        void onKey(int i, int[] iArr);

        void onPress(int i);

        void onRelease(int i);

        void onText(CharSequence charSequence);

        void swipeDown();

        void swipeLeft();

        void swipeRight();

        void swipeUp();
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.keyboardViewStyle);
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCurrentKeyIndex = -1;
        this.mCoordinates = new int[2];
        this.mPreviewCentered = false;
        this.mShowPreview = true;
        this.mShowTouchPoints = true;
        this.mCurrentKey = -1;
        this.mDownKey = -1;
        this.mKeyIndices = new int[12];
        this.mRepeatKeyIndex = -1;
        this.mClipRegion = new Rect(0, 0, 0, 0);
        this.mSwipeTracker = new SwipeTracker();
        this.mOldPointerCount = 1;
        this.mDistances = new int[MAX_NEARBY_KEYS];
        this.mPreviewLabel = new StringBuilder(1);
        this.mDirtyRect = new Rect();
        TypedArray a = context.obtainStyledAttributes(attrs, android.R.styleable.KeyboardView, defStyleAttr, defStyleRes);
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int n = a.getIndexCount();
        int previewLayout = 0;
        for (int previewLayout2 = 0; previewLayout2 < n; previewLayout2++) {
            int attr = a.getIndex(previewLayout2);
            switch (attr) {
                case 0:
                    this.mShadowColor = a.getColor(attr, 0);
                    break;
                case 1:
                    this.mShadowRadius = a.getFloat(attr, 0.0f);
                    break;
                case 2:
                    this.mKeyBackground = a.getDrawable(attr);
                    break;
                case 3:
                    this.mKeyTextSize = a.getDimensionPixelSize(attr, 18);
                    break;
                case 4:
                    this.mLabelTextSize = a.getDimensionPixelSize(attr, 14);
                    break;
                case 5:
                    this.mKeyTextColor = a.getColor(attr, -16777216);
                    break;
                case 6:
                    int previewLayout3 = a.getResourceId(attr, 0);
                    previewLayout = previewLayout3;
                    break;
                case 7:
                    this.mPreviewOffset = a.getDimensionPixelOffset(attr, 0);
                    break;
                case 8:
                    this.mPreviewHeight = a.getDimensionPixelSize(attr, 80);
                    break;
                case 9:
                    this.mVerticalCorrection = a.getDimensionPixelOffset(attr, 0);
                    break;
                case 10:
                    this.mPopupLayout = a.getResourceId(attr, 0);
                    break;
            }
        }
        this.mBackgroundDimAmount = this.mContext.obtainStyledAttributes(R.styleable.Theme).getFloat(2, 0.5f);
        this.mPreviewPopup = new PopupWindow(context);
        if (previewLayout != 0) {
            this.mPreviewText = (TextView) inflate.inflate(previewLayout, (ViewGroup) null);
            this.mPreviewTextSizeLarge = (int) this.mPreviewText.getTextSize();
            this.mPreviewPopup.setContentView(this.mPreviewText);
            this.mPreviewPopup.setBackgroundDrawable(null);
        } else {
            this.mShowPreview = false;
        }
        this.mPreviewPopup.setTouchable(false);
        this.mPopupKeyboard = new PopupWindow(context);
        this.mPopupKeyboard.setBackgroundDrawable(null);
        this.mPopupParent = this;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setTextSize(0);
        this.mPaint.setTextAlign(Paint.Align.CENTER);
        this.mPaint.setAlpha(255);
        this.mPadding = new Rect(0, 0, 0, 0);
        this.mMiniKeyboardCache = new HashMap();
        this.mKeyBackground.getPadding(this.mPadding);
        this.mSwipeThreshold = (int) (500.0f * getResources().getDisplayMetrics().density);
        this.mDisambiguateSwipe = getResources().getBoolean(R.bool.config_swipeDisambiguation);
        this.mAccessibilityManager = AccessibilityManager.getInstance(context);
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        resetMultiTap();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        initGestureDetector();
        if (this.mHandler == null) {
            this.mHandler = new Handler() { // from class: android.inputmethodservice.KeyboardView.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            KeyboardView.this.showKey(msg.arg1);
                            return;
                        case 2:
                            KeyboardView.this.mPreviewText.setVisibility(4);
                            return;
                        case 3:
                            if (KeyboardView.this.repeatKey()) {
                                Message repeat = Message.obtain(this, 3);
                                sendMessageDelayed(repeat, 50L);
                                return;
                            }
                            return;
                        case 4:
                            KeyboardView.this.openPopupIfRequired((MotionEvent) msg.obj);
                            return;
                        default:
                            return;
                    }
                }
            };
        }
    }

    private synchronized void initGestureDetector() {
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() { // from class: android.inputmethodservice.KeyboardView.2
                @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
                public boolean onFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
                    if (KeyboardView.this.mPossiblePoly) {
                        return false;
                    }
                    float absX = Math.abs(velocityX);
                    float absY = Math.abs(velocityY);
                    float deltaX = me2.getX() - me1.getX();
                    float deltaY = me2.getY() - me1.getY();
                    int travelX = KeyboardView.this.getWidth() / 2;
                    int travelY = KeyboardView.this.getHeight() / 2;
                    KeyboardView.this.mSwipeTracker.computeCurrentVelocity(1000);
                    float endingVelocityX = KeyboardView.this.mSwipeTracker.getXVelocity();
                    float endingVelocityY = KeyboardView.this.mSwipeTracker.getYVelocity();
                    boolean sendDownKey = false;
                    if (velocityX <= KeyboardView.this.mSwipeThreshold || absY >= absX || deltaX <= travelX) {
                        if (velocityX >= (-KeyboardView.this.mSwipeThreshold) || absY >= absX || deltaX >= (-travelX)) {
                            if (velocityY >= (-KeyboardView.this.mSwipeThreshold) || absX >= absY || deltaY >= (-travelY)) {
                                if (velocityY > KeyboardView.this.mSwipeThreshold && absX < absY / 2.0f && deltaY > travelY) {
                                    if (KeyboardView.this.mDisambiguateSwipe && endingVelocityY < velocityY / 4.0f) {
                                        sendDownKey = true;
                                    } else {
                                        KeyboardView.this.swipeDown();
                                        return true;
                                    }
                                }
                            } else if (KeyboardView.this.mDisambiguateSwipe && endingVelocityY > velocityY / 4.0f) {
                                sendDownKey = true;
                            } else {
                                KeyboardView.this.swipeUp();
                                return true;
                            }
                        } else if (KeyboardView.this.mDisambiguateSwipe && endingVelocityX > velocityX / 4.0f) {
                            sendDownKey = true;
                        } else {
                            KeyboardView.this.swipeLeft();
                            return true;
                        }
                    } else if (KeyboardView.this.mDisambiguateSwipe && endingVelocityX < velocityX / 4.0f) {
                        sendDownKey = true;
                    } else {
                        KeyboardView.this.swipeRight();
                        return true;
                    }
                    if (sendDownKey) {
                        KeyboardView.this.detectAndSendKey(KeyboardView.this.mDownKey, KeyboardView.this.mStartX, KeyboardView.this.mStartY, me1.getEventTime());
                        return false;
                    }
                    return false;
                }
            });
            this.mGestureDetector.setIsLongpressEnabled(false);
        }
    }

    public void setOnKeyboardActionListener(OnKeyboardActionListener listener) {
        this.mKeyboardActionListener = listener;
    }

    protected OnKeyboardActionListener getOnKeyboardActionListener() {
        return this.mKeyboardActionListener;
    }

    public void setKeyboard(Keyboard keyboard) {
        if (this.mKeyboard != null) {
            showPreview(-1);
        }
        removeMessages();
        this.mKeyboard = keyboard;
        List<Keyboard.Key> keys = this.mKeyboard.getKeys();
        this.mKeys = (Keyboard.Key[]) keys.toArray(new Keyboard.Key[keys.size()]);
        requestLayout();
        this.mKeyboardChanged = true;
        invalidateAllKeys();
        computeProximityThreshold(keyboard);
        this.mMiniKeyboardCache.clear();
        this.mAbortKey = true;
    }

    public Keyboard getKeyboard() {
        return this.mKeyboard;
    }

    public boolean setShifted(boolean shifted) {
        if (this.mKeyboard != null && this.mKeyboard.setShifted(shifted)) {
            invalidateAllKeys();
            return true;
        }
        return false;
    }

    public boolean isShifted() {
        if (this.mKeyboard != null) {
            return this.mKeyboard.isShifted();
        }
        return false;
    }

    public void setPreviewEnabled(boolean previewEnabled) {
        this.mShowPreview = previewEnabled;
    }

    public boolean isPreviewEnabled() {
        return this.mShowPreview;
    }

    public void setVerticalCorrection(int verticalOffset) {
    }

    public void setPopupParent(View v) {
        this.mPopupParent = v;
    }

    public void setPopupOffset(int x, int y) {
        this.mMiniKeyboardOffsetX = x;
        this.mMiniKeyboardOffsetY = y;
        if (this.mPreviewPopup.isShowing()) {
            this.mPreviewPopup.dismiss();
        }
    }

    public void setProximityCorrectionEnabled(boolean enabled) {
        this.mProximityCorrectOn = enabled;
    }

    public boolean isProximityCorrectionEnabled() {
        return this.mProximityCorrectOn;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        dismissPopupKeyboard();
    }

    private synchronized CharSequence adjustCase(CharSequence label) {
        if (this.mKeyboard.isShifted() && label != null && label.length() < 3 && Character.isLowerCase(label.charAt(0))) {
            return label.toString().toUpperCase();
        }
        return label;
    }

    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mKeyboard == null) {
            setMeasuredDimension(this.mPaddingLeft + this.mPaddingRight, this.mPaddingTop + this.mPaddingBottom);
            return;
        }
        int width = this.mKeyboard.getMinWidth() + this.mPaddingLeft + this.mPaddingRight;
        if (View.MeasureSpec.getSize(widthMeasureSpec) < width + 10) {
            width = View.MeasureSpec.getSize(widthMeasureSpec);
        }
        setMeasuredDimension(width, this.mKeyboard.getHeight() + this.mPaddingTop + this.mPaddingBottom);
    }

    private synchronized void computeProximityThreshold(Keyboard keyboard) {
        Keyboard.Key[] keys;
        if (keyboard == null || (keys = this.mKeys) == null) {
            return;
        }
        int length = keys.length;
        int dimensionSum = 0;
        for (Keyboard.Key key : keys) {
            dimensionSum += Math.min(key.width, key.height) + key.gap;
        }
        if (dimensionSum < 0 || length == 0) {
            return;
        }
        this.mProximityThreshold = (int) ((dimensionSum * 1.4f) / length);
        this.mProximityThreshold *= this.mProximityThreshold;
    }

    @Override // android.view.View
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.mKeyboard != null) {
            this.mKeyboard.resize(w, h);
        }
        this.mBuffer = null;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mDrawPending || this.mBuffer == null || this.mKeyboardChanged) {
            onBufferDraw();
        }
        canvas.drawBitmap(this.mBuffer, 0.0f, 0.0f, (Paint) null);
    }

    private synchronized void onBufferDraw() {
        int keyCount;
        Keyboard.Key invalidKey;
        Keyboard.Key[] keys;
        if (this.mBuffer == null || this.mKeyboardChanged) {
            if (this.mBuffer == null || (this.mKeyboardChanged && (this.mBuffer.getWidth() != getWidth() || this.mBuffer.getHeight() != getHeight()))) {
                int width = Math.max(1, getWidth());
                int height = Math.max(1, getHeight());
                this.mBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                this.mCanvas = new Canvas(this.mBuffer);
            }
            invalidateAllKeys();
            this.mKeyboardChanged = false;
        }
        if (this.mKeyboard == null) {
            return;
        }
        this.mCanvas.save();
        Canvas canvas = this.mCanvas;
        canvas.clipRect(this.mDirtyRect);
        Paint paint = this.mPaint;
        Drawable keyBackground = this.mKeyBackground;
        Rect clipRegion = this.mClipRegion;
        Rect padding = this.mPadding;
        int kbdPaddingLeft = this.mPaddingLeft;
        int kbdPaddingTop = this.mPaddingTop;
        Keyboard.Key[] keys2 = this.mKeys;
        Keyboard.Key invalidKey2 = this.mInvalidatedKey;
        paint.setColor(this.mKeyTextColor);
        boolean drawSingleKey = false;
        if (invalidKey2 != null && canvas.getClipBounds(clipRegion) && (invalidKey2.x + kbdPaddingLeft) - 1 <= clipRegion.left && (invalidKey2.y + kbdPaddingTop) - 1 <= clipRegion.top && invalidKey2.x + invalidKey2.width + kbdPaddingLeft + 1 >= clipRegion.right && invalidKey2.y + invalidKey2.height + kbdPaddingTop + 1 >= clipRegion.bottom) {
            drawSingleKey = true;
        }
        boolean drawSingleKey2 = drawSingleKey;
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        int keyCount2 = keys2.length;
        int i = 0;
        while (i < keyCount2) {
            Keyboard.Key key = keys2[i];
            if (drawSingleKey2 && invalidKey2 != key) {
                keyCount = keyCount2;
                invalidKey = invalidKey2;
                keys = keys2;
            } else {
                int[] drawableState = key.getCurrentDrawableState();
                keyBackground.setState(drawableState);
                String label = key.label == null ? null : adjustCase(key.label).toString();
                Rect bounds = keyBackground.getBounds();
                int i2 = key.width;
                keyCount = keyCount2;
                int keyCount3 = bounds.right;
                if (i2 != keyCount3 || key.height != bounds.bottom) {
                    keyBackground.setBounds(0, 0, key.width, key.height);
                }
                canvas.translate(key.x + kbdPaddingLeft, key.y + kbdPaddingTop);
                keyBackground.draw(canvas);
                if (label != null) {
                    if (label.length() > 1 && key.codes.length < 2) {
                        paint.setTextSize(this.mLabelTextSize);
                        paint.setTypeface(Typeface.DEFAULT_BOLD);
                    } else {
                        paint.setTextSize(this.mKeyTextSize);
                        paint.setTypeface(Typeface.DEFAULT);
                    }
                    paint.setShadowLayer(this.mShadowRadius, 0.0f, 0.0f, this.mShadowColor);
                    canvas.drawText(label, (((key.width - padding.left) - padding.right) / 2) + padding.left, (((key.height - padding.top) - padding.bottom) / 2) + ((paint.getTextSize() - paint.descent()) / 2.0f) + padding.top, paint);
                    paint.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
                    invalidKey = invalidKey2;
                    keys = keys2;
                } else if (key.icon != null) {
                    int drawableX = ((((key.width - padding.left) - padding.right) - key.icon.getIntrinsicWidth()) / 2) + padding.left;
                    int drawableY = ((((key.height - padding.top) - padding.bottom) - key.icon.getIntrinsicHeight()) / 2) + padding.top;
                    canvas.translate(drawableX, drawableY);
                    invalidKey = invalidKey2;
                    keys = keys2;
                    key.icon.setBounds(0, 0, key.icon.getIntrinsicWidth(), key.icon.getIntrinsicHeight());
                    key.icon.draw(canvas);
                    canvas.translate(-drawableX, -drawableY);
                } else {
                    invalidKey = invalidKey2;
                    keys = keys2;
                }
                canvas.translate((-key.x) - kbdPaddingLeft, (-key.y) - kbdPaddingTop);
            }
            i++;
            keyCount2 = keyCount;
            invalidKey2 = invalidKey;
            keys2 = keys;
        }
        this.mInvalidatedKey = null;
        if (this.mMiniKeyboardOnScreen) {
            paint.setColor(((int) (this.mBackgroundDimAmount * 255.0f)) << 24);
            canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), paint);
        }
        this.mCanvas.restore();
        this.mDrawPending = false;
        this.mDirtyRect.setEmpty();
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x003d, code lost:
        if (r15 >= r18.mProximityThreshold) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x003f, code lost:
        if (r14 != false) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0048, code lost:
        if (r12.codes[0] <= 32) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x004a, code lost:
        r10 = r12.codes.length;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x004d, code lost:
        if (r13 >= r11) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x004f, code lost:
        r11 = r13;
        r7 = r8[r5];
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0052, code lost:
        if (r21 != null) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0054, code lost:
        r16 = r4;
        r17 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0059, code lost:
        r15 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x005d, code lost:
        if (r15 >= r18.mDistances.length) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0063, code lost:
        if (r18.mDistances[r15] <= r13) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0065, code lost:
        r16 = r4;
        r17 = r6;
        r6 = r18.mDistances.length;
        java.lang.System.arraycopy(r18.mDistances, r15, r18.mDistances, r15 + r10, (r6 - r15) - r10);
        java.lang.System.arraycopy(r21, r15, r21, r15 + r10, (r21.length - r15) - r10);
        r1 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0080, code lost:
        if (r1 >= r10) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0082, code lost:
        r21[r15 + r1] = r12.codes[r1];
        r18.mDistances[r15 + r1] = r13;
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0094, code lost:
        r15 = r15 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x009f, code lost:
        r16 = r4;
        r17 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00a3, code lost:
        r5 = r5 + 1;
        r4 = r16;
        r6 = r17;
        r1 = r19;
        r2 = r20;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized int getKeyIndices(int r19, int r20, int[] r21) {
        /*
            r18 = this;
            r0 = r18
            r1 = r19
            r2 = r20
            r3 = r21
            android.inputmethodservice.Keyboard$Key[] r4 = r0.mKeys
            r5 = -1
            r6 = -1
            int r7 = r0.mProximityThreshold
            int r7 = r7 + 1
            int[] r8 = r0.mDistances
            r9 = 2147483647(0x7fffffff, float:NaN)
            java.util.Arrays.fill(r8, r9)
            android.inputmethodservice.Keyboard r8 = r0.mKeyboard
            int[] r8 = r8.getNearestKeys(r1, r2)
            int r9 = r8.length
            r11 = r7
            r7 = r6
            r6 = r5
            r5 = 0
        L23:
            if (r5 >= r9) goto Laf
            r12 = r8[r5]
            r12 = r4[r12]
            r13 = 0
            boolean r14 = r12.isInside(r1, r2)
            if (r14 == 0) goto L32
            r6 = r8[r5]
        L32:
            boolean r15 = r0.mProximityCorrectOn
            if (r15 == 0) goto L3f
            int r15 = r12.squaredDistanceFrom(r1, r2)
            r13 = r15
            int r10 = r0.mProximityThreshold
            if (r15 < r10) goto L41
        L3f:
            if (r14 == 0) goto L9f
        L41:
            int[] r10 = r12.codes
            r15 = 0
            r10 = r10[r15]
            r15 = 32
            if (r10 <= r15) goto L9f
            int[] r10 = r12.codes
            int r10 = r10.length
            if (r13 >= r11) goto L52
            r11 = r13
            r7 = r8[r5]
        L52:
            if (r3 != 0) goto L59
            r16 = r4
            r17 = r6
            goto La3
        L59:
            r15 = 0
        L5a:
            int[] r1 = r0.mDistances
            int r1 = r1.length
            if (r15 >= r1) goto L9f
            int[] r1 = r0.mDistances
            r1 = r1[r15]
            if (r1 <= r13) goto L94
            int[] r1 = r0.mDistances
            int[] r2 = r0.mDistances
            r16 = r4
            int r4 = r15 + r10
            r17 = r6
            int[] r6 = r0.mDistances
            int r6 = r6.length
            int r6 = r6 - r15
            int r6 = r6 - r10
            java.lang.System.arraycopy(r1, r15, r2, r4, r6)
            int r1 = r15 + r10
            int r2 = r3.length
            int r2 = r2 - r15
            int r2 = r2 - r10
            java.lang.System.arraycopy(r3, r15, r3, r1, r2)
            r1 = 0
        L80:
            if (r1 >= r10) goto L93
            int r2 = r15 + r1
            int[] r4 = r12.codes
            r4 = r4[r1]
            r3[r2] = r4
            int[] r2 = r0.mDistances
            int r4 = r15 + r1
            r2[r4] = r13
            int r1 = r1 + 1
            goto L80
        L93:
            goto La3
        L94:
            r16 = r4
            r17 = r6
            int r15 = r15 + 1
            r1 = r19
            r2 = r20
            goto L5a
        L9f:
            r16 = r4
            r17 = r6
        La3:
            int r5 = r5 + 1
            r4 = r16
            r6 = r17
            r1 = r19
            r2 = r20
            goto L23
        Laf:
            r16 = r4
            r1 = -1
            if (r6 != r1) goto Lb5
            r6 = r7
        Lb5:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: android.inputmethodservice.KeyboardView.getKeyIndices(int, int, int[]):int");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void detectAndSendKey(int index, int x, int y, long eventTime) {
        if (index != -1 && index < this.mKeys.length) {
            Keyboard.Key key = this.mKeys[index];
            if (key.text != null) {
                this.mKeyboardActionListener.onText(key.text);
                this.mKeyboardActionListener.onRelease(-1);
            } else {
                int code = key.codes[0];
                int[] codes = new int[MAX_NEARBY_KEYS];
                Arrays.fill(codes, -1);
                getKeyIndices(x, y, codes);
                if (this.mInMultiTap) {
                    if (this.mTapCount != -1) {
                        this.mKeyboardActionListener.onKey(-5, KEY_DELETE);
                    } else {
                        this.mTapCount = 0;
                    }
                    code = key.codes[this.mTapCount];
                }
                this.mKeyboardActionListener.onKey(code, codes);
                this.mKeyboardActionListener.onRelease(code);
            }
            this.mLastSentIndex = index;
            this.mLastTapTime = eventTime;
        }
    }

    private synchronized CharSequence getPreviewText(Keyboard.Key key) {
        if (this.mInMultiTap) {
            this.mPreviewLabel.setLength(0);
            this.mPreviewLabel.append((char) key.codes[this.mTapCount >= 0 ? this.mTapCount : 0]);
            return adjustCase(this.mPreviewLabel);
        }
        return adjustCase(key.label);
    }

    private synchronized void showPreview(int keyIndex) {
        int oldKeyIndex = this.mCurrentKeyIndex;
        PopupWindow previewPopup = this.mPreviewPopup;
        this.mCurrentKeyIndex = keyIndex;
        Keyboard.Key[] keys = this.mKeys;
        if (oldKeyIndex != this.mCurrentKeyIndex) {
            if (oldKeyIndex != -1 && keys.length > oldKeyIndex) {
                Keyboard.Key oldKey = keys[oldKeyIndex];
                oldKey.onReleased(this.mCurrentKeyIndex == -1);
                invalidateKey(oldKeyIndex);
                int keyCode = oldKey.codes[0];
                sendAccessibilityEventForUnicodeCharacter(256, keyCode);
                sendAccessibilityEventForUnicodeCharacter(65536, keyCode);
            }
            if (this.mCurrentKeyIndex != -1 && keys.length > this.mCurrentKeyIndex) {
                Keyboard.Key newKey = keys[this.mCurrentKeyIndex];
                newKey.onPressed();
                invalidateKey(this.mCurrentKeyIndex);
                int keyCode2 = newKey.codes[0];
                sendAccessibilityEventForUnicodeCharacter(128, keyCode2);
                sendAccessibilityEventForUnicodeCharacter(32768, keyCode2);
            }
        }
        if (oldKeyIndex != this.mCurrentKeyIndex && this.mShowPreview) {
            this.mHandler.removeMessages(1);
            if (previewPopup.isShowing() && keyIndex == -1) {
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(2), 70L);
            }
            if (keyIndex != -1) {
                if (previewPopup.isShowing() && this.mPreviewText.getVisibility() == 0) {
                    showKey(keyIndex);
                } else {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, keyIndex, 0), 0L);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: public */
    public void showKey(int keyIndex) {
        PopupWindow previewPopup = this.mPreviewPopup;
        Keyboard.Key[] keys = this.mKeys;
        if (keyIndex < 0 || keyIndex >= this.mKeys.length) {
            return;
        }
        Keyboard.Key key = keys[keyIndex];
        if (key.icon != null) {
            this.mPreviewText.setCompoundDrawables(null, null, null, key.iconPreview != null ? key.iconPreview : key.icon);
            this.mPreviewText.setText((CharSequence) null);
        } else {
            this.mPreviewText.setCompoundDrawables(null, null, null, null);
            this.mPreviewText.setText(getPreviewText(key));
            if (key.label.length() > 1 && key.codes.length < 2) {
                this.mPreviewText.setTextSize(0, this.mKeyTextSize);
                this.mPreviewText.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                this.mPreviewText.setTextSize(0, this.mPreviewTextSizeLarge);
                this.mPreviewText.setTypeface(Typeface.DEFAULT);
            }
        }
        this.mPreviewText.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        int popupWidth = Math.max(this.mPreviewText.getMeasuredWidth(), key.width + this.mPreviewText.getPaddingLeft() + this.mPreviewText.getPaddingRight());
        int popupHeight = this.mPreviewHeight;
        ViewGroup.LayoutParams lp = this.mPreviewText.getLayoutParams();
        if (lp != null) {
            lp.width = popupWidth;
            lp.height = popupHeight;
        }
        if (!this.mPreviewCentered) {
            this.mPopupPreviewX = (key.x - this.mPreviewText.getPaddingLeft()) + this.mPaddingLeft;
            this.mPopupPreviewY = (key.y - popupHeight) + this.mPreviewOffset;
        } else {
            this.mPopupPreviewX = 160 - (this.mPreviewText.getMeasuredWidth() / 2);
            this.mPopupPreviewY = -this.mPreviewText.getMeasuredHeight();
        }
        this.mHandler.removeMessages(2);
        getLocationInWindow(this.mCoordinates);
        int[] iArr = this.mCoordinates;
        iArr[0] = iArr[0] + this.mMiniKeyboardOffsetX;
        int[] iArr2 = this.mCoordinates;
        iArr2[1] = iArr2[1] + this.mMiniKeyboardOffsetY;
        this.mPreviewText.getBackground().setState(key.popupResId != 0 ? LONG_PRESSABLE_STATE_SET : EMPTY_STATE_SET);
        this.mPopupPreviewX += this.mCoordinates[0];
        this.mPopupPreviewY += this.mCoordinates[1];
        getLocationOnScreen(this.mCoordinates);
        if (this.mPopupPreviewY + this.mCoordinates[1] < 0) {
            if (key.x + key.width <= getWidth() / 2) {
                this.mPopupPreviewX += (int) (key.width * 2.5d);
            } else {
                this.mPopupPreviewX -= (int) (key.width * 2.5d);
            }
            this.mPopupPreviewY += popupHeight;
        }
        if (previewPopup.isShowing()) {
            previewPopup.update(this.mPopupPreviewX, this.mPopupPreviewY, popupWidth, popupHeight);
        } else {
            previewPopup.setWidth(popupWidth);
            previewPopup.setHeight(popupHeight);
            previewPopup.showAtLocation(this.mPopupParent, 0, this.mPopupPreviewX, this.mPopupPreviewY);
        }
        this.mPreviewText.setVisibility(0);
    }

    private synchronized void sendAccessibilityEventForUnicodeCharacter(int eventType, int code) {
        String text;
        if (this.mAccessibilityManager.isEnabled()) {
            AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
            onInitializeAccessibilityEvent(event);
            if (code != 10) {
                switch (code) {
                    case -6:
                        text = this.mContext.getString(R.string.keyboardview_keycode_alt);
                        break;
                    case -5:
                        text = this.mContext.getString(R.string.keyboardview_keycode_delete);
                        break;
                    case -4:
                        text = this.mContext.getString(R.string.keyboardview_keycode_done);
                        break;
                    case -3:
                        text = this.mContext.getString(R.string.keyboardview_keycode_cancel);
                        break;
                    case -2:
                        text = this.mContext.getString(R.string.keyboardview_keycode_mode_change);
                        break;
                    case -1:
                        text = this.mContext.getString(R.string.keyboardview_keycode_shift);
                        break;
                    default:
                        text = String.valueOf((char) code);
                        break;
                }
            } else {
                text = this.mContext.getString(R.string.keyboardview_keycode_enter);
            }
            event.getText().add(text);
            this.mAccessibilityManager.sendAccessibilityEvent(event);
        }
    }

    public void invalidateAllKeys() {
        this.mDirtyRect.union(0, 0, getWidth(), getHeight());
        this.mDrawPending = true;
        invalidate();
    }

    public void invalidateKey(int keyIndex) {
        if (this.mKeys == null || keyIndex < 0 || keyIndex >= this.mKeys.length) {
            return;
        }
        Keyboard.Key key = this.mKeys[keyIndex];
        this.mInvalidatedKey = key;
        this.mDirtyRect.union(key.x + this.mPaddingLeft, key.y + this.mPaddingTop, key.x + key.width + this.mPaddingLeft, key.y + key.height + this.mPaddingTop);
        onBufferDraw();
        invalidate(key.x + this.mPaddingLeft, key.y + this.mPaddingTop, key.x + key.width + this.mPaddingLeft, key.y + key.height + this.mPaddingTop);
    }

    /* JADX INFO: Access modifiers changed from: public */
    public boolean openPopupIfRequired(MotionEvent me) {
        if (this.mPopupLayout != 0 && this.mCurrentKey >= 0 && this.mCurrentKey < this.mKeys.length) {
            Keyboard.Key popupKey = this.mKeys[this.mCurrentKey];
            boolean result = onLongPress(popupKey);
            if (result) {
                this.mAbortKey = true;
                showPreview(-1);
            }
            return result;
        }
        return false;
    }

    protected boolean onLongPress(Keyboard.Key popupKey) {
        Keyboard keyboard;
        int popupKeyboardId = popupKey.popupResId;
        if (popupKeyboardId == 0) {
            return false;
        }
        this.mMiniKeyboardContainer = this.mMiniKeyboardCache.get(popupKey);
        if (this.mMiniKeyboardContainer == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mMiniKeyboardContainer = inflater.inflate(this.mPopupLayout, (ViewGroup) null);
            this.mMiniKeyboard = (KeyboardView) this.mMiniKeyboardContainer.findViewById(android.R.id.keyboardView);
            View closeButton = this.mMiniKeyboardContainer.findViewById(android.R.id.closeButton);
            if (closeButton != null) {
                closeButton.setOnClickListener(this);
            }
            this.mMiniKeyboard.setOnKeyboardActionListener(new OnKeyboardActionListener() { // from class: android.inputmethodservice.KeyboardView.3
                @Override // android.inputmethodservice.KeyboardView.OnKeyboardActionListener
                public void onKey(int primaryCode, int[] keyCodes) {
                    KeyboardView.this.mKeyboardActionListener.onKey(primaryCode, keyCodes);
                    KeyboardView.this.dismissPopupKeyboard();
                }

                @Override // android.inputmethodservice.KeyboardView.OnKeyboardActionListener
                public void onText(CharSequence text) {
                    KeyboardView.this.mKeyboardActionListener.onText(text);
                    KeyboardView.this.dismissPopupKeyboard();
                }

                @Override // android.inputmethodservice.KeyboardView.OnKeyboardActionListener
                public void swipeLeft() {
                }

                @Override // android.inputmethodservice.KeyboardView.OnKeyboardActionListener
                public void swipeRight() {
                }

                @Override // android.inputmethodservice.KeyboardView.OnKeyboardActionListener
                public void swipeUp() {
                }

                @Override // android.inputmethodservice.KeyboardView.OnKeyboardActionListener
                public void swipeDown() {
                }

                @Override // android.inputmethodservice.KeyboardView.OnKeyboardActionListener
                public void onPress(int primaryCode) {
                    KeyboardView.this.mKeyboardActionListener.onPress(primaryCode);
                }

                @Override // android.inputmethodservice.KeyboardView.OnKeyboardActionListener
                public void onRelease(int primaryCode) {
                    KeyboardView.this.mKeyboardActionListener.onRelease(primaryCode);
                }
            });
            if (popupKey.popupCharacters != null) {
                keyboard = new Keyboard(getContext(), popupKeyboardId, popupKey.popupCharacters, -1, getPaddingLeft() + getPaddingRight());
            } else {
                keyboard = new Keyboard(getContext(), popupKeyboardId);
            }
            this.mMiniKeyboard.setKeyboard(keyboard);
            this.mMiniKeyboard.setPopupParent(this);
            this.mMiniKeyboardContainer.measure(View.MeasureSpec.makeMeasureSpec(getWidth(), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(getHeight(), Integer.MIN_VALUE));
            this.mMiniKeyboardCache.put(popupKey, this.mMiniKeyboardContainer);
        } else {
            this.mMiniKeyboard = (KeyboardView) this.mMiniKeyboardContainer.findViewById(android.R.id.keyboardView);
        }
        getLocationInWindow(this.mCoordinates);
        this.mPopupX = popupKey.x + this.mPaddingLeft;
        this.mPopupY = popupKey.y + this.mPaddingTop;
        this.mPopupX = (this.mPopupX + popupKey.width) - this.mMiniKeyboardContainer.getMeasuredWidth();
        this.mPopupY -= this.mMiniKeyboardContainer.getMeasuredHeight();
        int x = this.mPopupX + this.mMiniKeyboardContainer.getPaddingRight() + this.mCoordinates[0];
        int y = this.mPopupY + this.mMiniKeyboardContainer.getPaddingBottom() + this.mCoordinates[1];
        this.mMiniKeyboard.setPopupOffset(x < 0 ? 0 : x, y);
        this.mMiniKeyboard.setShifted(isShifted());
        this.mPopupKeyboard.setContentView(this.mMiniKeyboardContainer);
        this.mPopupKeyboard.setWidth(this.mMiniKeyboardContainer.getMeasuredWidth());
        this.mPopupKeyboard.setHeight(this.mMiniKeyboardContainer.getMeasuredHeight());
        this.mPopupKeyboard.showAtLocation(this, 0, x, y);
        this.mMiniKeyboardOnScreen = true;
        invalidateAllKeys();
        return true;
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent event) {
        if (this.mAccessibilityManager.isTouchExplorationEnabled() && event.getPointerCount() == 1) {
            int action = event.getAction();
            if (action != 7) {
                switch (action) {
                    case 9:
                        event.setAction(0);
                        break;
                    case 10:
                        event.setAction(1);
                        break;
                }
            } else {
                event.setAction(2);
            }
            return onTouchEvent(event);
        }
        return true;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent me) {
        boolean result;
        int pointerCount = me.getPointerCount();
        int action = me.getAction();
        long now = me.getEventTime();
        if (pointerCount != this.mOldPointerCount) {
            if (pointerCount == 1) {
                MotionEvent down = MotionEvent.obtain(now, now, 0, me.getX(), me.getY(), me.getMetaState());
                result = onModifiedTouchEvent(down, false);
                down.recycle();
                if (action == 1) {
                    result = onModifiedTouchEvent(me, true);
                }
            } else {
                MotionEvent up = MotionEvent.obtain(now, now, 1, this.mOldPointerX, this.mOldPointerY, me.getMetaState());
                result = onModifiedTouchEvent(up, true);
                up.recycle();
            }
        } else if (pointerCount == 1) {
            result = onModifiedTouchEvent(me, false);
            this.mOldPointerX = me.getX();
            this.mOldPointerY = me.getY();
        } else {
            result = true;
        }
        this.mOldPointerCount = pointerCount;
        return result;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private synchronized boolean onModifiedTouchEvent(MotionEvent me, boolean possiblePoly) {
        int touchX;
        int touchY;
        int touchX2 = ((int) me.getX()) - this.mPaddingLeft;
        int touchY2 = ((int) me.getY()) - this.mPaddingTop;
        if (touchY2 >= (-this.mVerticalCorrection)) {
            touchY2 += this.mVerticalCorrection;
        }
        int action = me.getAction();
        long eventTime = me.getEventTime();
        int keyIndex = getKeyIndices(touchX2, touchY2, null);
        this.mPossiblePoly = possiblePoly;
        if (action == 0) {
            this.mSwipeTracker.clear();
        }
        this.mSwipeTracker.addMovement(me);
        if (!this.mAbortKey || action == 0 || action == 3) {
            if (this.mGestureDetector.onTouchEvent(me)) {
                showPreview(-1);
                this.mHandler.removeMessages(3);
                this.mHandler.removeMessages(4);
                return true;
            } else if (!this.mMiniKeyboardOnScreen || action == 3) {
                switch (action) {
                    case 0:
                        this.mAbortKey = false;
                        this.mStartX = touchX2;
                        this.mStartY = touchY2;
                        this.mLastCodeX = touchX2;
                        this.mLastCodeY = touchY2;
                        this.mLastKeyTime = 0L;
                        this.mCurrentKeyTime = 0L;
                        this.mLastKey = -1;
                        this.mCurrentKey = keyIndex;
                        this.mDownKey = keyIndex;
                        this.mDownTime = me.getEventTime();
                        this.mLastMoveTime = this.mDownTime;
                        checkMultiTap(eventTime, keyIndex);
                        this.mKeyboardActionListener.onPress(keyIndex != -1 ? this.mKeys[keyIndex].codes[0] : 0);
                        if (this.mCurrentKey >= 0 && this.mKeys[this.mCurrentKey].repeatable) {
                            this.mRepeatKeyIndex = this.mCurrentKey;
                            Message msg = this.mHandler.obtainMessage(3);
                            this.mHandler.sendMessageDelayed(msg, 400L);
                            repeatKey();
                            if (this.mAbortKey) {
                                this.mRepeatKeyIndex = -1;
                                touchX = touchX2;
                                touchY = touchY2;
                                break;
                            }
                        }
                        if (this.mCurrentKey != -1) {
                            Message msg2 = this.mHandler.obtainMessage(4, me);
                            this.mHandler.sendMessageDelayed(msg2, LONGPRESS_TIMEOUT);
                        }
                        showPreview(keyIndex);
                        touchX = touchX2;
                        touchY = touchY2;
                        break;
                    case 1:
                        removeMessages();
                        if (keyIndex == this.mCurrentKey) {
                            this.mCurrentKeyTime += eventTime - this.mLastMoveTime;
                        } else {
                            resetMultiTap();
                            this.mLastKey = this.mCurrentKey;
                            this.mLastKeyTime = (this.mCurrentKeyTime + eventTime) - this.mLastMoveTime;
                            this.mCurrentKey = keyIndex;
                            this.mCurrentKeyTime = 0L;
                        }
                        if (this.mCurrentKeyTime < this.mLastKeyTime && this.mCurrentKeyTime < 70 && this.mLastKey != -1) {
                            this.mCurrentKey = this.mLastKey;
                            touchX2 = this.mLastCodeX;
                            touchY2 = this.mLastCodeY;
                        }
                        touchX = touchX2;
                        touchY = touchY2;
                        showPreview(-1);
                        Arrays.fill(this.mKeyIndices, -1);
                        if (this.mRepeatKeyIndex == -1 && !this.mMiniKeyboardOnScreen && !this.mAbortKey) {
                            detectAndSendKey(this.mCurrentKey, touchX, touchY, eventTime);
                        }
                        invalidateKey(keyIndex);
                        this.mRepeatKeyIndex = -1;
                        break;
                    case 2:
                        boolean continueLongPress = false;
                        if (keyIndex != -1) {
                            if (this.mCurrentKey == -1) {
                                this.mCurrentKey = keyIndex;
                                this.mCurrentKeyTime = eventTime - this.mDownTime;
                            } else if (keyIndex == this.mCurrentKey) {
                                this.mCurrentKeyTime += eventTime - this.mLastMoveTime;
                                continueLongPress = true;
                            } else if (this.mRepeatKeyIndex == -1) {
                                resetMultiTap();
                                this.mLastKey = this.mCurrentKey;
                                this.mLastCodeX = this.mLastX;
                                this.mLastCodeY = this.mLastY;
                                this.mLastKeyTime = (this.mCurrentKeyTime + eventTime) - this.mLastMoveTime;
                                this.mCurrentKey = keyIndex;
                                this.mCurrentKeyTime = 0L;
                            }
                        }
                        if (!continueLongPress) {
                            this.mHandler.removeMessages(4);
                            if (keyIndex != -1) {
                                Message msg3 = this.mHandler.obtainMessage(4, me);
                                this.mHandler.sendMessageDelayed(msg3, LONGPRESS_TIMEOUT);
                            }
                        }
                        showPreview(this.mCurrentKey);
                        this.mLastMoveTime = eventTime;
                        touchX = touchX2;
                        touchY = touchY2;
                        break;
                    case 3:
                        removeMessages();
                        dismissPopupKeyboard();
                        this.mAbortKey = true;
                        showPreview(-1);
                        invalidateKey(this.mCurrentKey);
                        touchX = touchX2;
                        touchY = touchY2;
                        break;
                    default:
                        touchX = touchX2;
                        touchY = touchY2;
                        break;
                }
                this.mLastX = touchX;
                this.mLastY = touchY;
                return true;
            } else {
                return true;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: public */
    public boolean repeatKey() {
        Keyboard.Key key = this.mKeys[this.mRepeatKeyIndex];
        detectAndSendKey(this.mCurrentKey, key.x, key.y, this.mLastTapTime);
        return true;
    }

    protected void swipeRight() {
        this.mKeyboardActionListener.swipeRight();
    }

    protected void swipeLeft() {
        this.mKeyboardActionListener.swipeLeft();
    }

    protected void swipeUp() {
        this.mKeyboardActionListener.swipeUp();
    }

    protected void swipeDown() {
        this.mKeyboardActionListener.swipeDown();
    }

    public void closing() {
        if (this.mPreviewPopup.isShowing()) {
            this.mPreviewPopup.dismiss();
        }
        removeMessages();
        dismissPopupKeyboard();
        this.mBuffer = null;
        this.mCanvas = null;
        this.mMiniKeyboardCache.clear();
    }

    private synchronized void removeMessages() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(3);
            this.mHandler.removeMessages(4);
            this.mHandler.removeMessages(1);
        }
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        closing();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dismissPopupKeyboard() {
        if (this.mPopupKeyboard.isShowing()) {
            this.mPopupKeyboard.dismiss();
            this.mMiniKeyboardOnScreen = false;
            invalidateAllKeys();
        }
    }

    public boolean handleBack() {
        if (this.mPopupKeyboard.isShowing()) {
            dismissPopupKeyboard();
            return true;
        }
        return false;
    }

    private synchronized void resetMultiTap() {
        this.mLastSentIndex = -1;
        this.mTapCount = 0;
        this.mLastTapTime = -1L;
        this.mInMultiTap = false;
    }

    private synchronized void checkMultiTap(long eventTime, int keyIndex) {
        if (keyIndex == -1) {
            return;
        }
        Keyboard.Key key = this.mKeys[keyIndex];
        if (key.codes.length > 1) {
            this.mInMultiTap = true;
            if (eventTime < this.mLastTapTime + 800 && keyIndex == this.mLastSentIndex) {
                this.mTapCount = (this.mTapCount + 1) % key.codes.length;
            } else {
                this.mTapCount = -1;
            }
        } else if (eventTime > this.mLastTapTime + 800 || keyIndex != this.mLastSentIndex) {
            resetMultiTap();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SwipeTracker {
        static final int LONGEST_PAST_TIME = 200;
        static final int NUM_PAST = 4;
        final long[] mPastTime;
        final float[] mPastX;
        final float[] mPastY;
        float mXVelocity;
        float mYVelocity;

        private synchronized SwipeTracker() {
            this.mPastX = new float[4];
            this.mPastY = new float[4];
            this.mPastTime = new long[4];
        }

        public synchronized void clear() {
            this.mPastTime[0] = 0;
        }

        public synchronized void addMovement(MotionEvent ev) {
            long time = ev.getEventTime();
            int N = ev.getHistorySize();
            for (int i = 0; i < N; i++) {
                addPoint(ev.getHistoricalX(i), ev.getHistoricalY(i), ev.getHistoricalEventTime(i));
            }
            addPoint(ev.getX(), ev.getY(), time);
        }

        private synchronized void addPoint(float x, float y, long time) {
            long[] pastTime = this.mPastTime;
            int drop = -1;
            int drop2 = 0;
            while (drop2 < 4 && pastTime[drop2] != 0) {
                if (pastTime[drop2] < time - 200) {
                    drop = drop2;
                }
                drop2++;
            }
            if (drop2 == 4 && drop < 0) {
                drop = 0;
            }
            if (drop == drop2) {
                drop--;
            }
            float[] pastX = this.mPastX;
            float[] pastY = this.mPastY;
            if (drop >= 0) {
                int start = drop + 1;
                int count = (4 - drop) - 1;
                System.arraycopy(pastX, start, pastX, 0, count);
                System.arraycopy(pastY, start, pastY, 0, count);
                System.arraycopy(pastTime, start, pastTime, 0, count);
                drop2 -= drop + 1;
            }
            pastX[drop2] = x;
            pastY[drop2] = y;
            pastTime[drop2] = time;
            int i = drop2 + 1;
            if (i < 4) {
                pastTime[i] = 0;
            }
        }

        public synchronized void computeCurrentVelocity(int units) {
            computeCurrentVelocity(units, Float.MAX_VALUE);
        }

        public synchronized void computeCurrentVelocity(int units, float maxVelocity) {
            float[] pastX;
            long[] pastTime;
            float[] pastX2 = this.mPastX;
            float[] pastY = this.mPastY;
            long[] pastTime2 = this.mPastTime;
            int N = 0;
            float oldestX = pastX2[0];
            float oldestY = pastY[0];
            long oldestTime = pastTime2[0];
            float accumX = 0.0f;
            float accumY = 0.0f;
            while (N < 4 && pastTime2[N] != 0) {
                N++;
            }
            int i = 1;
            while (i < N) {
                int dur = (int) (pastTime2[i] - oldestTime);
                if (dur == 0) {
                    pastX = pastX2;
                    pastTime = pastTime2;
                } else {
                    float dist = pastX2[i] - oldestX;
                    pastX = pastX2;
                    pastTime = pastTime2;
                    float vel = (dist / dur) * units;
                    accumX = accumX == 0.0f ? vel : (accumX + vel) * 0.5f;
                    float dist2 = pastY[i] - oldestY;
                    float dist3 = dur;
                    float vel2 = (dist2 / dist3) * units;
                    accumY = accumY == 0.0f ? vel2 : (accumY + vel2) * 0.5f;
                }
                i++;
                pastX2 = pastX;
                pastTime2 = pastTime;
            }
            this.mXVelocity = accumX < 0.0f ? Math.max(accumX, -maxVelocity) : Math.min(accumX, maxVelocity);
            this.mYVelocity = accumY < 0.0f ? Math.max(accumY, -maxVelocity) : Math.min(accumY, maxVelocity);
        }

        public synchronized float getXVelocity() {
            return this.mXVelocity;
        }

        public synchronized float getYVelocity() {
            return this.mYVelocity;
        }
    }
}
