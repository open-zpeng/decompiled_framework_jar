package com.android.internal.app;

import android.animation.ObjectAnimator;
import android.animation.TimeAnimator;
import android.app.Activity;
import android.app.slice.Slice;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.internal.R;
import com.xiaopeng.util.FeatureOption;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class PlatLogoActivity extends Activity {
    static final String TOUCH_STATS = "touch.stats";
    static final Paint sPaint = new Paint();
    BackslashDrawable mBackslash;
    int mClicks;
    ImageView mOneView;
    ImageView mZeroView;
    double mPressureMin = FeatureOption.FO_BOOT_POLICY_CPU;
    double mPressureMax = -1.0d;

    static {
        sPaint.setStyle(Paint.Style.STROKE);
        sPaint.setStrokeWidth(4.0f);
        sPaint.setStrokeCap(Paint.Cap.SQUARE);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onPause() {
        BackslashDrawable backslashDrawable = this.mBackslash;
        if (backslashDrawable != null) {
            backslashDrawable.stopAnimating();
        }
        this.mClicks = 0;
        super.onPause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        float dp = getResources().getDisplayMetrics().density;
        getWindow().getDecorView().setSystemUiVisibility(768);
        getWindow().setNavigationBarColor(0);
        getWindow().setStatusBarColor(0);
        getActionBar().hide();
        setContentView(R.layout.platlogo_layout);
        this.mBackslash = new BackslashDrawable((int) (50.0f * dp));
        this.mOneView = (ImageView) findViewById(R.id.one);
        this.mOneView.setImageDrawable(new OneDrawable());
        this.mZeroView = (ImageView) findViewById(R.id.zero);
        this.mZeroView.setImageDrawable(new ZeroDrawable());
        ViewGroup root = (ViewGroup) this.mOneView.getParent();
        root.setClipChildren(false);
        root.setBackground(this.mBackslash);
        root.getBackground().setAlpha(32);
        View.OnTouchListener tl = new View.OnTouchListener() { // from class: com.android.internal.app.PlatLogoActivity.1
            long mClickTime;
            float mOffsetX;
            float mOffsetY;
            ObjectAnimator mRotAnim;

            /* JADX WARN: Code restructure failed: missing block: B:7:0x0012, code lost:
                if (r0 != 3) goto L11;
             */
            @Override // android.view.View.OnTouchListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public boolean onTouch(android.view.View r10, android.view.MotionEvent r11) {
                /*
                    r9 = this;
                    com.android.internal.app.PlatLogoActivity r0 = com.android.internal.app.PlatLogoActivity.this
                    com.android.internal.app.PlatLogoActivity.access$000(r0, r11)
                    int r0 = r11.getActionMasked()
                    r1 = 2
                    r2 = 1
                    if (r0 == 0) goto L4e
                    if (r0 == r2) goto L31
                    if (r0 == r1) goto L16
                    r1 = 3
                    if (r0 == r1) goto L34
                    goto Lb5
                L16:
                    float r0 = r11.getRawX()
                    float r1 = r9.mOffsetX
                    float r0 = r0 - r1
                    r10.setX(r0)
                    float r0 = r11.getRawY()
                    float r1 = r9.mOffsetY
                    float r0 = r0 - r1
                    r10.setY(r0)
                    r0 = 9
                    r10.performHapticFeedback(r0)
                    goto Lb5
                L31:
                    r10.performClick()
                L34:
                    android.view.ViewPropertyAnimator r0 = r10.animate()
                    r1 = 1065353216(0x3f800000, float:1.0)
                    android.view.ViewPropertyAnimator r0 = r0.scaleX(r1)
                    r0.scaleY(r1)
                    android.animation.ObjectAnimator r0 = r9.mRotAnim
                    if (r0 == 0) goto L48
                    r0.cancel()
                L48:
                    com.android.internal.app.PlatLogoActivity r0 = com.android.internal.app.PlatLogoActivity.this
                    com.android.internal.app.PlatLogoActivity.access$100(r0)
                    goto Lb5
                L4e:
                    android.view.ViewPropertyAnimator r0 = r10.animate()
                    r3 = 1066192077(0x3f8ccccd, float:1.1)
                    android.view.ViewPropertyAnimator r0 = r0.scaleX(r3)
                    r0.scaleY(r3)
                    android.view.ViewParent r0 = r10.getParent()
                    r0.bringChildToFront(r10)
                    float r0 = r11.getRawX()
                    float r3 = r10.getX()
                    float r0 = r0 - r3
                    r9.mOffsetX = r0
                    float r0 = r11.getRawY()
                    float r3 = r10.getY()
                    float r0 = r0 - r3
                    r9.mOffsetY = r0
                    long r3 = java.lang.System.currentTimeMillis()
                    long r5 = r9.mClickTime
                    long r5 = r3 - r5
                    r7 = 350(0x15e, double:1.73E-321)
                    int r0 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
                    if (r0 >= 0) goto Lb2
                    android.util.Property<android.view.View, java.lang.Float> r0 = android.view.View.ROTATION
                    float[] r1 = new float[r1]
                    r5 = 0
                    float r6 = r10.getRotation()
                    r1[r5] = r6
                    float r5 = r10.getRotation()
                    r6 = 1163984896(0x45610000, float:3600.0)
                    float r5 = r5 + r6
                    r1[r2] = r5
                    android.animation.ObjectAnimator r0 = android.animation.ObjectAnimator.ofFloat(r10, r0, r1)
                    r9.mRotAnim = r0
                    android.animation.ObjectAnimator r0 = r9.mRotAnim
                    r5 = 10000(0x2710, double:4.9407E-320)
                    r0.setDuration(r5)
                    android.animation.ObjectAnimator r0 = r9.mRotAnim
                    r0.start()
                    r0 = 0
                    r9.mClickTime = r0
                    goto Lb5
                Lb2:
                    r9.mClickTime = r3
                Lb5:
                    return r2
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.app.PlatLogoActivity.AnonymousClass1.onTouch(android.view.View, android.view.MotionEvent):boolean");
            }
        };
        findViewById(R.id.one).setOnTouchListener(tl);
        findViewById(R.id.zero).setOnTouchListener(tl);
        findViewById(R.id.text).setOnTouchListener(tl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void testOverlap() {
        float width = this.mZeroView.getWidth();
        float targetX = this.mZeroView.getX() + (width * 0.2f);
        float targetY = this.mZeroView.getY() + (width * 0.3f);
        if (Math.hypot(targetX - this.mOneView.getX(), targetY - this.mOneView.getY()) < width * 0.2f && Math.abs((this.mOneView.getRotation() % 360.0f) - 315.0f) < 15.0f) {
            this.mOneView.animate().x(this.mZeroView.getX() + (0.2f * width));
            this.mOneView.animate().y(this.mZeroView.getY() + (0.3f * width));
            ImageView imageView = this.mOneView;
            imageView.setRotation(imageView.getRotation() % 360.0f);
            this.mOneView.animate().rotation(315.0f);
            this.mOneView.performHapticFeedback(16);
            this.mBackslash.startAnimating();
            this.mClicks++;
            if (this.mClicks >= 7) {
                launchNextStage();
                return;
            }
            return;
        }
        this.mBackslash.stopAnimating();
    }

    private void launchNextStage() {
        ContentResolver cr = getContentResolver();
        if (Settings.System.getLong(cr, Settings.System.EGG_MODE, 0L) == 0) {
            try {
                Settings.System.putLong(cr, Settings.System.EGG_MODE, System.currentTimeMillis());
            } catch (RuntimeException e) {
                Log.e("com.android.internal.app.PlatLogoActivity", "Can't write settings", e);
            }
        }
        try {
            startActivity(new Intent(Intent.ACTION_MAIN).setFlags(AudioManager.FORCE_CHANGEVOL_FLAG).addCategory("com.android.internal.category.PLATLOGO"));
        } catch (ActivityNotFoundException e2) {
            Log.e("com.android.internal.app.PlatLogoActivity", "No more eggs.");
        }
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void measureTouchPressure(MotionEvent event) {
        float pressure = event.getPressure();
        int actionMasked = event.getActionMasked();
        if (actionMasked == 0) {
            if (this.mPressureMax < FeatureOption.FO_BOOT_POLICY_CPU) {
                double d = pressure;
                this.mPressureMax = d;
                this.mPressureMin = d;
            }
        } else if (actionMasked == 2) {
            if (pressure < this.mPressureMin) {
                this.mPressureMin = pressure;
            }
            if (pressure > this.mPressureMax) {
                this.mPressureMax = pressure;
            }
        }
    }

    private void syncTouchPressure() {
        try {
            String touchDataJson = Settings.System.getString(getContentResolver(), TOUCH_STATS);
            JSONObject touchData = new JSONObject(touchDataJson != null ? touchDataJson : "{}");
            if (touchData.has("min")) {
                this.mPressureMin = Math.min(this.mPressureMin, touchData.getDouble("min"));
            }
            if (touchData.has(Slice.SUBTYPE_MAX)) {
                this.mPressureMax = Math.max(this.mPressureMax, touchData.getDouble(Slice.SUBTYPE_MAX));
            }
            if (this.mPressureMax >= FeatureOption.FO_BOOT_POLICY_CPU) {
                touchData.put("min", this.mPressureMin);
                touchData.put(Slice.SUBTYPE_MAX, this.mPressureMax);
                Settings.System.putString(getContentResolver(), TOUCH_STATS, touchData.toString());
            }
        } catch (Exception e) {
            Log.e("com.android.internal.app.PlatLogoActivity", "Can't write touch settings", e);
        }
    }

    @Override // android.app.Activity
    public void onStart() {
        super.onStart();
        syncTouchPressure();
    }

    @Override // android.app.Activity
    public void onStop() {
        syncTouchPressure();
        super.onStop();
    }

    /* loaded from: classes3.dex */
    static class ZeroDrawable extends Drawable {
        int mTintColor;

        ZeroDrawable() {
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            PlatLogoActivity.sPaint.setColor(this.mTintColor | (-16777216));
            canvas.save();
            canvas.scale(canvas.getWidth() / 24.0f, canvas.getHeight() / 24.0f);
            canvas.drawCircle(12.0f, 12.0f, 10.0f, PlatLogoActivity.sPaint);
            canvas.restore();
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int alpha) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setTintList(ColorStateList tint) {
            this.mTintColor = tint.getDefaultColor();
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -3;
        }
    }

    /* loaded from: classes3.dex */
    static class OneDrawable extends Drawable {
        int mTintColor;

        OneDrawable() {
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            PlatLogoActivity.sPaint.setColor(this.mTintColor | (-16777216));
            canvas.save();
            canvas.scale(canvas.getWidth() / 24.0f, canvas.getHeight() / 24.0f);
            Path p = new Path();
            p.moveTo(12.0f, 21.83f);
            p.rLineTo(0.0f, -19.67f);
            p.rLineTo(-5.0f, 0.0f);
            canvas.drawPath(p, PlatLogoActivity.sPaint);
            canvas.restore();
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int alpha) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setTintList(ColorStateList tint) {
            this.mTintColor = tint.getDefaultColor();
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class BackslashDrawable extends Drawable implements TimeAnimator.TimeListener {
        BitmapShader mShader;
        Bitmap mTile;
        Paint mPaint = new Paint();
        TimeAnimator mAnimator = new TimeAnimator();
        Matrix mMatrix = new Matrix();

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            canvas.drawPaint(this.mPaint);
        }

        BackslashDrawable(int width) {
            this.mTile = Bitmap.createBitmap(width, width, Bitmap.Config.ALPHA_8);
            this.mAnimator.setTimeListener(this);
            Canvas tileCanvas = new Canvas(this.mTile);
            float w = tileCanvas.getWidth();
            float h = tileCanvas.getHeight();
            Path path = new Path();
            path.moveTo(0.0f, 0.0f);
            path.lineTo(w / 2.0f, 0.0f);
            path.lineTo(w, h / 2.0f);
            path.lineTo(w, h);
            path.close();
            path.moveTo(0.0f, h / 2.0f);
            path.lineTo(w / 2.0f, h);
            path.lineTo(0.0f, h);
            path.close();
            Paint slashPaint = new Paint();
            slashPaint.setAntiAlias(true);
            slashPaint.setStyle(Paint.Style.FILL);
            slashPaint.setColor(-16777216);
            tileCanvas.drawPath(path, slashPaint);
            this.mShader = new BitmapShader(this.mTile, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            this.mPaint.setShader(this.mShader);
        }

        public void startAnimating() {
            if (!this.mAnimator.isStarted()) {
                this.mAnimator.start();
            }
        }

        public void stopAnimating() {
            if (this.mAnimator.isStarted()) {
                this.mAnimator.cancel();
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int alpha) {
            this.mPaint.setAlpha(alpha);
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
            this.mPaint.setColorFilter(colorFilter);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -3;
        }

        @Override // android.animation.TimeAnimator.TimeListener
        public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
            if (this.mShader != null) {
                this.mMatrix.postTranslate(((float) deltaTime) / 4.0f, 0.0f);
                this.mShader.setLocalMatrix(this.mMatrix);
                invalidateSelf();
            }
        }
    }
}
