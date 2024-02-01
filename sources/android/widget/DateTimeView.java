package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.os.Handler;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inspector.InspectionCompanion;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import android.widget.RemoteViews;
import com.android.internal.R;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@RemoteViews.RemoteView
/* loaded from: classes3.dex */
public class DateTimeView extends TextView {
    private static final int SHOW_MONTH_DAY_YEAR = 1;
    private static final int SHOW_TIME = 0;
    private static final ThreadLocal<ReceiverInfo> sReceiverInfo = new ThreadLocal<>();
    int mLastDisplay;
    DateFormat mLastFormat;
    private String mNowText;
    private boolean mShowRelativeTime;
    Date mTime;
    long mTimeMillis;
    private long mUpdateTimeMillis;

    /* loaded from: classes3.dex */
    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<DateTimeView> {
        private boolean mPropertiesMapped = false;
        private int mShowReleativeId;

        @Override // android.view.inspector.InspectionCompanion
        public void mapProperties(PropertyMapper propertyMapper) {
            this.mShowReleativeId = propertyMapper.mapBoolean("showReleative", 0);
            this.mPropertiesMapped = true;
        }

        @Override // android.view.inspector.InspectionCompanion
        public void readProperties(DateTimeView node, PropertyReader propertyReader) {
            if (!this.mPropertiesMapped) {
                throw new InspectionCompanion.UninitializedPropertyMapException();
            }
            propertyReader.readBoolean(this.mShowReleativeId, node.isShowRelativeTime());
        }
    }

    public DateTimeView(Context context) {
        this(context, null);
    }

    @UnsupportedAppUsage
    public DateTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mLastDisplay = -1;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DateTimeView, 0, 0);
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            if (attr == 0) {
                boolean relative = a.getBoolean(i, false);
                setShowRelativeTime(relative);
            }
        }
        a.recycle();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ReceiverInfo ri = sReceiverInfo.get();
        if (ri == null) {
            ri = new ReceiverInfo();
            sReceiverInfo.set(ri);
        }
        ri.addView(this);
        if (this.mShowRelativeTime) {
            update();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ReceiverInfo ri = sReceiverInfo.get();
        if (ri != null) {
            ri.removeView(this);
        }
    }

    @UnsupportedAppUsage
    @RemotableViewMethod
    public void setTime(long time) {
        Time t = new Time();
        t.set(time);
        this.mTimeMillis = t.toMillis(false);
        this.mTime = new Date(t.year - 1900, t.month, t.monthDay, t.hour, t.minute, 0);
        update();
    }

    @RemotableViewMethod
    public void setShowRelativeTime(boolean showRelativeTime) {
        this.mShowRelativeTime = showRelativeTime;
        updateNowText();
        update();
    }

    public boolean isShowRelativeTime() {
        return this.mShowRelativeTime;
    }

    @Override // android.view.View
    @RemotableViewMethod
    public void setVisibility(int visibility) {
        boolean gotVisible = visibility != 8 && getVisibility() == 8;
        super.setVisibility(visibility);
        if (gotVisible) {
            update();
        }
    }

    @UnsupportedAppUsage
    void update() {
        int display;
        DateFormat format;
        if (this.mTime == null || getVisibility() == 8) {
            return;
        }
        if (this.mShowRelativeTime) {
            updateRelativeTime();
            return;
        }
        Date date = this.mTime;
        Time t = new Time();
        t.set(this.mTimeMillis);
        t.second = 0;
        t.hour -= 12;
        long twelveHoursBefore = t.toMillis(false);
        t.hour += 12;
        long twelveHoursAfter = t.toMillis(false);
        t.hour = 0;
        t.minute = 0;
        long midnightBefore = t.toMillis(false);
        t.monthDay++;
        long midnightAfter = t.toMillis(false);
        t.set(System.currentTimeMillis());
        t.second = 0;
        long nowMillis = t.normalize(false);
        if ((nowMillis >= midnightBefore && nowMillis < midnightAfter) || (nowMillis >= twelveHoursBefore && nowMillis < twelveHoursAfter)) {
            display = 0;
        } else {
            display = 1;
        }
        if (display == this.mLastDisplay && this.mLastFormat != null) {
            format = this.mLastFormat;
        } else {
            if (display == 0) {
                DateFormat format2 = getTimeFormat();
                format = format2;
            } else if (display == 1) {
                format = DateFormat.getDateInstance(3);
            } else {
                throw new RuntimeException("unknown display value: " + display);
            }
            this.mLastFormat = format;
        }
        String text = format.format(this.mTime);
        setText(text);
        if (display == 0) {
            this.mUpdateTimeMillis = twelveHoursAfter > midnightAfter ? twelveHoursAfter : midnightAfter;
        } else if (this.mTimeMillis < nowMillis) {
            this.mUpdateTimeMillis = 0L;
        } else {
            this.mUpdateTimeMillis = twelveHoursBefore < midnightBefore ? twelveHoursBefore : midnightBefore;
        }
    }

    private void updateRelativeTime() {
        int count;
        int i;
        String result;
        long millisIncrease;
        int i2;
        long millisIncrease2;
        int i3;
        int i4;
        long now = System.currentTimeMillis();
        long duration = Math.abs(now - this.mTimeMillis);
        boolean past = now >= this.mTimeMillis;
        if (duration < 60000) {
            setText(this.mNowText);
            this.mUpdateTimeMillis = this.mTimeMillis + 60000 + 1;
            return;
        }
        if (duration < 3600000) {
            count = (int) (duration / 60000);
            Resources resources = getContext().getResources();
            if (past) {
                i4 = R.plurals.duration_minutes_shortest;
            } else {
                i4 = R.plurals.duration_minutes_shortest_future;
            }
            result = String.format(resources.getQuantityString(i4, count), Integer.valueOf(count));
            millisIncrease = 60000;
        } else if (duration < 86400000) {
            count = (int) (duration / 3600000);
            Resources resources2 = getContext().getResources();
            if (past) {
                i3 = R.plurals.duration_hours_shortest;
            } else {
                i3 = R.plurals.duration_hours_shortest_future;
            }
            result = String.format(resources2.getQuantityString(i3, count), Integer.valueOf(count));
            millisIncrease = 3600000;
        } else if (duration < DateUtils.YEAR_IN_MILLIS) {
            TimeZone timeZone = TimeZone.getDefault();
            int count2 = Math.max(Math.abs(dayDistance(timeZone, this.mTimeMillis, now)), 1);
            Resources resources3 = getContext().getResources();
            if (past) {
                i2 = R.plurals.duration_days_shortest;
            } else {
                i2 = R.plurals.duration_days_shortest_future;
            }
            result = String.format(resources3.getQuantityString(i2, count2), Integer.valueOf(count2));
            if (past || count2 != 1) {
                this.mUpdateTimeMillis = computeNextMidnight(timeZone);
                millisIncrease2 = -1;
            } else {
                millisIncrease2 = 86400000;
            }
            count = count2;
            millisIncrease = millisIncrease2;
        } else {
            count = (int) (duration / DateUtils.YEAR_IN_MILLIS);
            Resources resources4 = getContext().getResources();
            if (past) {
                i = R.plurals.duration_years_shortest;
            } else {
                i = R.plurals.duration_years_shortest_future;
            }
            result = String.format(resources4.getQuantityString(i, count), Integer.valueOf(count));
            millisIncrease = DateUtils.YEAR_IN_MILLIS;
        }
        if (millisIncrease != -1) {
            if (past) {
                this.mUpdateTimeMillis = this.mTimeMillis + ((count + 1) * millisIncrease) + 1;
            } else {
                this.mUpdateTimeMillis = (this.mTimeMillis - (count * millisIncrease)) + 1;
            }
        }
        setText(result);
    }

    private long computeNextMidnight(TimeZone timeZone) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(timeZone);
        c.add(5, 1);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);
        return c.getTimeInMillis();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateNowText();
        update();
    }

    private void updateNowText() {
        if (!this.mShowRelativeTime) {
            return;
        }
        this.mNowText = getContext().getResources().getString(R.string.now_string_shortest);
    }

    private static int dayDistance(TimeZone timeZone, long startTime, long endTime) {
        return Time.getJulianDay(endTime, timeZone.getOffset(endTime) / 1000) - Time.getJulianDay(startTime, timeZone.getOffset(startTime) / 1000);
    }

    private DateFormat getTimeFormat() {
        return android.text.format.DateFormat.getTimeFormat(getContext());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearFormatAndUpdate() {
        this.mLastFormat = null;
        update();
    }

    @Override // android.widget.TextView, android.view.View
    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        int i;
        String result;
        int i2;
        int i3;
        int i4;
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (this.mShowRelativeTime) {
            long now = System.currentTimeMillis();
            long duration = Math.abs(now - this.mTimeMillis);
            boolean past = now >= this.mTimeMillis;
            if (duration < 60000) {
                result = this.mNowText;
            } else if (duration < 3600000) {
                int count = (int) (duration / 60000);
                Resources resources = getContext().getResources();
                if (past) {
                    i4 = R.plurals.duration_minutes_relative;
                } else {
                    i4 = R.plurals.duration_minutes_relative_future;
                }
                result = String.format(resources.getQuantityString(i4, count), Integer.valueOf(count));
            } else if (duration < 86400000) {
                int count2 = (int) (duration / 3600000);
                Resources resources2 = getContext().getResources();
                if (past) {
                    i3 = R.plurals.duration_hours_relative;
                } else {
                    i3 = R.plurals.duration_hours_relative_future;
                }
                result = String.format(resources2.getQuantityString(i3, count2), Integer.valueOf(count2));
            } else if (duration < DateUtils.YEAR_IN_MILLIS) {
                TimeZone timeZone = TimeZone.getDefault();
                int count3 = Math.max(Math.abs(dayDistance(timeZone, this.mTimeMillis, now)), 1);
                Resources resources3 = getContext().getResources();
                if (past) {
                    i2 = R.plurals.duration_days_relative;
                } else {
                    i2 = R.plurals.duration_days_relative_future;
                }
                result = String.format(resources3.getQuantityString(i2, count3), Integer.valueOf(count3));
            } else {
                int count4 = (int) (duration / DateUtils.YEAR_IN_MILLIS);
                Resources resources4 = getContext().getResources();
                if (past) {
                    i = R.plurals.duration_years_relative;
                } else {
                    i = R.plurals.duration_years_relative_future;
                }
                result = String.format(resources4.getQuantityString(i, count4), Integer.valueOf(count4));
            }
            info.setText(result);
        }
    }

    public static void setReceiverHandler(Handler handler) {
        ReceiverInfo ri = sReceiverInfo.get();
        if (ri == null) {
            ri = new ReceiverInfo();
            sReceiverInfo.set(ri);
        }
        ri.setHandler(handler);
    }

    /* loaded from: classes3.dex */
    private static class ReceiverInfo {
        private final ArrayList<DateTimeView> mAttachedViews;
        private Handler mHandler;
        private final ContentObserver mObserver;
        private final BroadcastReceiver mReceiver;

        private ReceiverInfo() {
            this.mAttachedViews = new ArrayList<>();
            this.mReceiver = new BroadcastReceiver() { // from class: android.widget.DateTimeView.ReceiverInfo.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (Intent.ACTION_TIME_TICK.equals(action) && System.currentTimeMillis() < ReceiverInfo.this.getSoonestUpdateTime()) {
                        return;
                    }
                    ReceiverInfo.this.updateAll();
                }
            };
            this.mObserver = new ContentObserver(new Handler()) { // from class: android.widget.DateTimeView.ReceiverInfo.2
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange) {
                    ReceiverInfo.this.updateAll();
                }
            };
            this.mHandler = new Handler();
        }

        public void addView(DateTimeView v) {
            synchronized (this.mAttachedViews) {
                boolean register = this.mAttachedViews.isEmpty();
                this.mAttachedViews.add(v);
                if (register) {
                    register(getApplicationContextIfAvailable(v.getContext()));
                }
            }
        }

        public void removeView(DateTimeView v) {
            synchronized (this.mAttachedViews) {
                boolean removed = this.mAttachedViews.remove(v);
                if (removed && this.mAttachedViews.isEmpty()) {
                    unregister(getApplicationContextIfAvailable(v.getContext()));
                }
            }
        }

        void updateAll() {
            synchronized (this.mAttachedViews) {
                int count = this.mAttachedViews.size();
                for (int i = 0; i < count; i++) {
                    final DateTimeView view = this.mAttachedViews.get(i);
                    view.post(new Runnable() { // from class: android.widget.-$$Lambda$DateTimeView$ReceiverInfo$AVLnX7U5lTcE9jLnlKKNAT1GUeI
                        @Override // java.lang.Runnable
                        public final void run() {
                            DateTimeView.this.clearFormatAndUpdate();
                        }
                    });
                }
            }
        }

        long getSoonestUpdateTime() {
            long result = Long.MAX_VALUE;
            synchronized (this.mAttachedViews) {
                int count = this.mAttachedViews.size();
                for (int i = 0; i < count; i++) {
                    long time = this.mAttachedViews.get(i).mUpdateTimeMillis;
                    if (time < result) {
                        result = time;
                    }
                }
            }
            return result;
        }

        static final Context getApplicationContextIfAvailable(Context context) {
            Context ac = context.getApplicationContext();
            return ac != null ? ac : ActivityThread.currentApplication().getApplicationContext();
        }

        void register(Context context) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            context.registerReceiver(this.mReceiver, filter, null, this.mHandler);
        }

        void unregister(Context context) {
            context.unregisterReceiver(this.mReceiver);
        }

        public void setHandler(Handler handler) {
            this.mHandler = handler;
            synchronized (this.mAttachedViews) {
                if (!this.mAttachedViews.isEmpty()) {
                    unregister(this.mAttachedViews.get(0).getContext());
                    register(this.mAttachedViews.get(0).getContext());
                }
            }
        }
    }
}
