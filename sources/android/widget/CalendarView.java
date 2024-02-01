package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.R;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/* loaded from: classes3.dex */
public class CalendarView extends FrameLayout {
    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
    private static final String LOG_TAG = "CalendarView";
    private static final int MODE_HOLO = 0;
    private static final int MODE_MATERIAL = 1;
    public protected final CalendarViewDelegate mDelegate;

    /* loaded from: classes3.dex */
    private interface CalendarViewDelegate {
        synchronized boolean getBoundsForDate(long j, Rect rect);

        synchronized long getDate();

        synchronized int getDateTextAppearance();

        synchronized int getFirstDayOfWeek();

        synchronized int getFocusedMonthDateColor();

        synchronized long getMaxDate();

        synchronized long getMinDate();

        synchronized Drawable getSelectedDateVerticalBar();

        synchronized int getSelectedWeekBackgroundColor();

        synchronized boolean getShowWeekNumber();

        synchronized int getShownWeekCount();

        synchronized int getUnfocusedMonthDateColor();

        synchronized int getWeekDayTextAppearance();

        synchronized int getWeekNumberColor();

        synchronized int getWeekSeparatorLineColor();

        synchronized void onConfigurationChanged(Configuration configuration);

        synchronized void setDate(long j);

        synchronized void setDate(long j, boolean z, boolean z2);

        synchronized void setDateTextAppearance(int i);

        synchronized void setFirstDayOfWeek(int i);

        synchronized void setFocusedMonthDateColor(int i);

        synchronized void setMaxDate(long j);

        synchronized void setMinDate(long j);

        synchronized void setOnDateChangeListener(OnDateChangeListener onDateChangeListener);

        synchronized void setSelectedDateVerticalBar(int i);

        synchronized void setSelectedDateVerticalBar(Drawable drawable);

        synchronized void setSelectedWeekBackgroundColor(int i);

        synchronized void setShowWeekNumber(boolean z);

        synchronized void setShownWeekCount(int i);

        synchronized void setUnfocusedMonthDateColor(int i);

        synchronized void setWeekDayTextAppearance(int i);

        synchronized void setWeekNumberColor(int i);

        synchronized void setWeekSeparatorLineColor(int i);
    }

    /* loaded from: classes3.dex */
    public interface OnDateChangeListener {
        void onSelectedDayChange(CalendarView calendarView, int i, int i2, int i3);
    }

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 16843613);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, defStyleRes);
        int mode = a.getInt(13, 0);
        a.recycle();
        switch (mode) {
            case 0:
                this.mDelegate = new CalendarViewLegacyDelegate(this, context, attrs, defStyleAttr, defStyleRes);
                return;
            case 1:
                this.mDelegate = new CalendarViewMaterialDelegate(this, context, attrs, defStyleAttr, defStyleRes);
                return;
            default:
                throw new IllegalArgumentException("invalid calendarViewMode attribute");
        }
    }

    @Deprecated
    public void setShownWeekCount(int count) {
        this.mDelegate.setShownWeekCount(count);
    }

    @Deprecated
    public int getShownWeekCount() {
        return this.mDelegate.getShownWeekCount();
    }

    @Deprecated
    public void setSelectedWeekBackgroundColor(int color) {
        this.mDelegate.setSelectedWeekBackgroundColor(color);
    }

    @Deprecated
    public int getSelectedWeekBackgroundColor() {
        return this.mDelegate.getSelectedWeekBackgroundColor();
    }

    @Deprecated
    public void setFocusedMonthDateColor(int color) {
        this.mDelegate.setFocusedMonthDateColor(color);
    }

    @Deprecated
    public int getFocusedMonthDateColor() {
        return this.mDelegate.getFocusedMonthDateColor();
    }

    @Deprecated
    public void setUnfocusedMonthDateColor(int color) {
        this.mDelegate.setUnfocusedMonthDateColor(color);
    }

    @Deprecated
    public int getUnfocusedMonthDateColor() {
        return this.mDelegate.getUnfocusedMonthDateColor();
    }

    @Deprecated
    public void setWeekNumberColor(int color) {
        this.mDelegate.setWeekNumberColor(color);
    }

    @Deprecated
    public int getWeekNumberColor() {
        return this.mDelegate.getWeekNumberColor();
    }

    @Deprecated
    public void setWeekSeparatorLineColor(int color) {
        this.mDelegate.setWeekSeparatorLineColor(color);
    }

    @Deprecated
    public int getWeekSeparatorLineColor() {
        return this.mDelegate.getWeekSeparatorLineColor();
    }

    @Deprecated
    public void setSelectedDateVerticalBar(int resourceId) {
        this.mDelegate.setSelectedDateVerticalBar(resourceId);
    }

    @Deprecated
    public void setSelectedDateVerticalBar(Drawable drawable) {
        this.mDelegate.setSelectedDateVerticalBar(drawable);
    }

    @Deprecated
    public Drawable getSelectedDateVerticalBar() {
        return this.mDelegate.getSelectedDateVerticalBar();
    }

    public void setWeekDayTextAppearance(int resourceId) {
        this.mDelegate.setWeekDayTextAppearance(resourceId);
    }

    public int getWeekDayTextAppearance() {
        return this.mDelegate.getWeekDayTextAppearance();
    }

    public void setDateTextAppearance(int resourceId) {
        this.mDelegate.setDateTextAppearance(resourceId);
    }

    public int getDateTextAppearance() {
        return this.mDelegate.getDateTextAppearance();
    }

    public long getMinDate() {
        return this.mDelegate.getMinDate();
    }

    public void setMinDate(long minDate) {
        this.mDelegate.setMinDate(minDate);
    }

    public long getMaxDate() {
        return this.mDelegate.getMaxDate();
    }

    public void setMaxDate(long maxDate) {
        this.mDelegate.setMaxDate(maxDate);
    }

    @Deprecated
    public void setShowWeekNumber(boolean showWeekNumber) {
        this.mDelegate.setShowWeekNumber(showWeekNumber);
    }

    @Deprecated
    public boolean getShowWeekNumber() {
        return this.mDelegate.getShowWeekNumber();
    }

    public int getFirstDayOfWeek() {
        return this.mDelegate.getFirstDayOfWeek();
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.mDelegate.setFirstDayOfWeek(firstDayOfWeek);
    }

    public void setOnDateChangeListener(OnDateChangeListener listener) {
        this.mDelegate.setOnDateChangeListener(listener);
    }

    public long getDate() {
        return this.mDelegate.getDate();
    }

    public void setDate(long date) {
        this.mDelegate.setDate(date);
    }

    public void setDate(long date, boolean animate, boolean center) {
        this.mDelegate.setDate(date, animate, center);
    }

    public boolean getBoundsForDate(long date, Rect outBounds) {
        return this.mDelegate.getBoundsForDate(date, outBounds);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mDelegate.onConfigurationChanged(newConfig);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return CalendarView.class.getName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static abstract class AbstractCalendarViewDelegate implements CalendarViewDelegate {
        protected static final String DEFAULT_MAX_DATE = "01/01/2100";
        protected static final String DEFAULT_MIN_DATE = "01/01/1900";
        protected Context mContext;
        protected Locale mCurrentLocale;
        protected CalendarView mDelegator;

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized AbstractCalendarViewDelegate(CalendarView delegator, Context context) {
            this.mDelegator = delegator;
            this.mContext = context;
            setCurrentLocale(Locale.getDefault());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public synchronized void setCurrentLocale(Locale locale) {
            if (locale.equals(this.mCurrentLocale)) {
                return;
            }
            this.mCurrentLocale = locale;
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized void setShownWeekCount(int count) {
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized int getShownWeekCount() {
            return 0;
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized void setSelectedWeekBackgroundColor(int color) {
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized int getSelectedWeekBackgroundColor() {
            return 0;
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized void setFocusedMonthDateColor(int color) {
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized int getFocusedMonthDateColor() {
            return 0;
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized void setUnfocusedMonthDateColor(int color) {
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized int getUnfocusedMonthDateColor() {
            return 0;
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized void setWeekNumberColor(int color) {
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized int getWeekNumberColor() {
            return 0;
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized void setWeekSeparatorLineColor(int color) {
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized int getWeekSeparatorLineColor() {
            return 0;
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized void setSelectedDateVerticalBar(int resId) {
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized void setSelectedDateVerticalBar(Drawable drawable) {
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized Drawable getSelectedDateVerticalBar() {
            return null;
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized void setShowWeekNumber(boolean showWeekNumber) {
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized boolean getShowWeekNumber() {
            return false;
        }

        @Override // android.widget.CalendarView.CalendarViewDelegate
        public synchronized void onConfigurationChanged(Configuration newConfig) {
        }
    }

    public static synchronized boolean parseDate(String date, Calendar outDate) {
        if (date == null || date.isEmpty()) {
            return false;
        }
        try {
            Date parsedDate = DATE_FORMATTER.parse(date);
            outDate.setTime(parsedDate);
            return true;
        } catch (ParseException e) {
            Log.w(LOG_TAG, "Date: " + date + " not in format: " + DATE_FORMAT);
            return false;
        }
    }
}
