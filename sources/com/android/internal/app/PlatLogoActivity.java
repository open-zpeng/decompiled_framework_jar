package com.android.internal.app;

import android.animation.TimeAnimator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
/* loaded from: classes3.dex */
public class PlatLogoActivity extends Activity {
    TimeAnimator anim;
    PBackground bg;
    FrameLayout layout;

    /* loaded from: classes3.dex */
    private class PBackground extends Drawable {
        private int darkest;
        private float dp;
        private float maxRadius;
        private float offset;
        private int[] palette;
        private float radius;
        private float x;
        private float y;

        public PBackground() {
            randomizePalette();
        }

        public void setRadius(float r) {
            this.radius = Math.max(48.0f * this.dp, r);
        }

        public void setPosition(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public void setOffset(float o) {
            this.offset = o;
        }

        public float lum(int rgb) {
            return (((Color.red(rgb) * 299.0f) + (Color.green(rgb) * 587.0f)) + (Color.blue(rgb) * 114.0f)) / 1000.0f;
        }

        public void randomizePalette() {
            int[] iArr;
            int slots = ((int) (Math.random() * 2.0d)) + 2;
            float[] color = {((float) Math.random()) * 360.0f, 1.0f, 1.0f};
            this.palette = new int[slots];
            this.darkest = 0;
            for (int i = 0; i < slots; i++) {
                this.palette[i] = Color.HSVToColor(color);
                color[0] = (color[0] + (360.0f / slots)) % 360.0f;
                if (lum(this.palette[i]) < lum(this.palette[this.darkest])) {
                    this.darkest = i;
                }
            }
            StringBuilder str = new StringBuilder();
            for (int c : this.palette) {
                str.append(String.format("#%08x ", Integer.valueOf(c)));
            }
            Log.v("PlatLogoActivity", "color palette: " + ((Object) str));
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            if (this.dp == 0.0f) {
                this.dp = PlatLogoActivity.this.getResources().getDisplayMetrics().density;
            }
            float width = canvas.getWidth();
            float height = canvas.getHeight();
            float f = 2.0f;
            if (this.radius == 0.0f) {
                setPosition(width / 2.0f, height / 2.0f);
                setRadius(width / 6.0f);
            }
            float inner_w = this.radius * 0.667f;
            Paint paint = new Paint();
            paint.setStrokeCap(Paint.Cap.BUTT);
            canvas.translate(this.x, this.y);
            Path p = new Path();
            p.moveTo(-this.radius, height);
            p.lineTo(-this.radius, 0.0f);
            p.arcTo(-this.radius, -this.radius, this.radius, this.radius, -180.0f, 270.0f, false);
            p.lineTo(-this.radius, this.radius);
            float w = Math.max(canvas.getWidth(), canvas.getHeight()) * 1.414f;
            paint.setStyle(Paint.Style.FILL);
            int i = 0;
            float w2 = w;
            while (true) {
                int i2 = i;
                if (w2 > (this.radius * f) + (inner_w * f)) {
                    paint.setColor(this.palette[i2 % this.palette.length] | (-16777216));
                    canvas.drawOval((-w2) / f, (-w2) / f, w2 / f, w2 / f, paint);
                    w2 = (float) (w2 - (inner_w * (1.100000023841858d + Math.sin(((i2 / 20.0f) + this.offset) * 3.14159f))));
                    i = i2 + 1;
                    p = p;
                    f = 2.0f;
                } else {
                    Path p2 = p;
                    paint.setColor(this.palette[(this.darkest + 1) % this.palette.length] | (-16777216));
                    canvas.drawOval(-this.radius, -this.radius, this.radius, this.radius, paint);
                    p2.reset();
                    p2.moveTo(-this.radius, height);
                    p2.lineTo(-this.radius, 0.0f);
                    p2.arcTo(-this.radius, -this.radius, this.radius, this.radius, -180.0f, 270.0f, false);
                    p2.lineTo((-this.radius) + inner_w, this.radius);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(2.0f * inner_w);
                    paint.setColor(this.palette[this.darkest]);
                    canvas.drawPath(p2, paint);
                    paint.setStrokeWidth(inner_w);
                    paint.setColor(-1);
                    canvas.drawPath(p2, paint);
                    return;
                }
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int alpha) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.layout = new FrameLayout(this);
        setContentView(this.layout);
        this.bg = new PBackground();
        this.layout.setBackground(this.bg);
        final ContentResolver cr = getContentResolver();
        this.layout.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.internal.app.PlatLogoActivity.1
            int maxPointers;
            double pressure_max;
            double pressure_min;
            int tapCount;
            final String TOUCH_STATS = "touch.stats";
            final MotionEvent.PointerCoords pc0 = new MotionEvent.PointerCoords();
            final MotionEvent.PointerCoords pc1 = new MotionEvent.PointerCoords();

            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            /* JADX WARN: Removed duplicated region for block: B:30:0x009d  */
            /* JADX WARN: Removed duplicated region for block: B:33:0x00a7  */
            /* JADX WARN: Removed duplicated region for block: B:36:0x00b2  */
            /* JADX WARN: Removed duplicated region for block: B:38:0x00b6  */
            @Override // android.view.View.OnTouchListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public boolean onTouch(android.view.View r10, android.view.MotionEvent r11) {
                /*
                    Method dump skipped, instructions count: 240
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.app.PlatLogoActivity.AnonymousClass1.onTouch(android.view.View, android.view.MotionEvent):boolean");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchNextStage() {
        ContentResolver cr = getContentResolver();
        if (Settings.System.getLong(cr, Settings.System.EGG_MODE, 0L) == 0) {
            try {
                Settings.System.putLong(cr, Settings.System.EGG_MODE, System.currentTimeMillis());
            } catch (RuntimeException e) {
                Log.e("PlatLogoActivity", "Can't write settings", e);
            }
        }
        try {
            startActivity(new Intent(Intent.ACTION_MAIN).setFlags(AudioManager.FORCE_CHANGEVOL_FLAG).addCategory("com.android.internal.category.PLATLOGO"));
        } catch (ActivityNotFoundException e2) {
            Log.e("PlatLogoActivity", "No more eggs.");
        }
        finish();
    }

    @Override // android.app.Activity
    public void onStart() {
        super.onStart();
        this.bg.randomizePalette();
        this.anim = new TimeAnimator();
        this.anim.setTimeListener(new TimeAnimator.TimeListener() { // from class: com.android.internal.app.PlatLogoActivity.2
            @Override // android.animation.TimeAnimator.TimeListener
            public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
                PlatLogoActivity.this.bg.setOffset(((float) totalTime) / 60000.0f);
                PlatLogoActivity.this.bg.invalidateSelf();
            }
        });
        this.anim.start();
    }

    @Override // android.app.Activity
    public void onStop() {
        if (this.anim != null) {
            this.anim.cancel();
            this.anim = null;
        }
        super.onStop();
    }
}
