package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.icu.text.DateFormat;
import android.icu.text.DisplayContext;
import android.icu.util.Calendar;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.DatePicker;
import android.widget.DayPickerView;
import android.widget.YearPickerView;
import com.android.internal.R;
import java.util.Locale;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class DatePickerCalendarDelegate extends DatePicker.AbstractDatePickerDelegate {
    private static final int ANIMATION_DURATION = 300;
    private static final int DEFAULT_END_YEAR = 2100;
    private static final int DEFAULT_START_YEAR = 1900;
    private static final int UNINITIALIZED = -1;
    private static final int USE_LOCALE = 0;
    private static final int VIEW_MONTH_DAY = 0;
    private static final int VIEW_YEAR = 1;
    private ViewAnimator mAnimator;
    private ViewGroup mContainer;
    private int mCurrentView;
    private DayPickerView mDayPickerView;
    private int mFirstDayOfWeek;
    private TextView mHeaderMonthDay;
    private TextView mHeaderYear;
    private final Calendar mMaxDate;
    private final Calendar mMinDate;
    private DateFormat mMonthDayFormat;
    private final DayPickerView.OnDaySelectedListener mOnDaySelectedListener;
    private final View.OnClickListener mOnHeaderClickListener;
    private final YearPickerView.OnYearSelectedListener mOnYearSelectedListener;
    private String mSelectDay;
    private String mSelectYear;
    private final Calendar mTempDate;
    private DateFormat mYearFormat;
    private YearPickerView mYearPickerView;
    private static final int[] ATTRS_TEXT_COLOR = {16842904};
    private static final int[] ATTRS_DISABLED_ALPHA = {16842803};

    public DatePickerCalendarDelegate(DatePicker delegator, Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(delegator, context);
        this.mCurrentView = -1;
        this.mFirstDayOfWeek = 0;
        this.mOnDaySelectedListener = new DayPickerView.OnDaySelectedListener() { // from class: android.widget.DatePickerCalendarDelegate.1
            @Override // android.widget.DayPickerView.OnDaySelectedListener
            public void onDaySelected(DayPickerView view, Calendar day) {
                DatePickerCalendarDelegate.this.mCurrentDate.setTimeInMillis(day.getTimeInMillis());
                DatePickerCalendarDelegate.this.onDateChanged(true, true);
            }
        };
        this.mOnYearSelectedListener = new YearPickerView.OnYearSelectedListener() { // from class: android.widget.DatePickerCalendarDelegate.2
            @Override // android.widget.YearPickerView.OnYearSelectedListener
            public void onYearChanged(YearPickerView view, int year) {
                int day = DatePickerCalendarDelegate.this.mCurrentDate.get(5);
                int month = DatePickerCalendarDelegate.this.mCurrentDate.get(2);
                int daysInMonth = DatePickerCalendarDelegate.getDaysInMonth(month, year);
                if (day > daysInMonth) {
                    DatePickerCalendarDelegate.this.mCurrentDate.set(5, daysInMonth);
                }
                DatePickerCalendarDelegate.this.mCurrentDate.set(1, year);
                DatePickerCalendarDelegate.this.onDateChanged(true, true);
                DatePickerCalendarDelegate.this.setCurrentView(0);
                DatePickerCalendarDelegate.this.mHeaderYear.requestFocus();
            }
        };
        this.mOnHeaderClickListener = new View.OnClickListener() { // from class: android.widget.-$$Lambda$DatePickerCalendarDelegate$GuCiuXPsIV2EU6oKGRXrsGY-DHM
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DatePickerCalendarDelegate.this.lambda$new$0$DatePickerCalendarDelegate(view);
            }
        };
        Locale locale = this.mCurrentLocale;
        this.mCurrentDate = Calendar.getInstance(locale);
        this.mTempDate = Calendar.getInstance(locale);
        this.mMinDate = Calendar.getInstance(locale);
        this.mMaxDate = Calendar.getInstance(locale);
        this.mMinDate.set(1900, 0, 1);
        this.mMaxDate.set(2100, 11, 31);
        Resources res = this.mDelegator.getResources();
        TypedArray a = this.mContext.obtainStyledAttributes(attrs, R.styleable.DatePicker, defStyleAttr, defStyleRes);
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int layoutResourceId = a.getResourceId(19, R.layout.date_picker_material);
        this.mContainer = (ViewGroup) inflater.inflate(layoutResourceId, (ViewGroup) this.mDelegator, false);
        this.mContainer.setSaveFromParentEnabled(false);
        this.mDelegator.addView(this.mContainer);
        ViewGroup header = (ViewGroup) this.mContainer.findViewById(R.id.date_picker_header);
        this.mHeaderYear = (TextView) header.findViewById(R.id.date_picker_header_year);
        this.mHeaderYear.setOnClickListener(this.mOnHeaderClickListener);
        this.mHeaderMonthDay = (TextView) header.findViewById(R.id.date_picker_header_date);
        this.mHeaderMonthDay.setOnClickListener(this.mOnHeaderClickListener);
        ColorStateList headerTextColor = null;
        int monthHeaderTextAppearance = a.getResourceId(10, 0);
        if (monthHeaderTextAppearance != 0) {
            TypedArray textAppearance = this.mContext.obtainStyledAttributes(null, ATTRS_TEXT_COLOR, 0, monthHeaderTextAppearance);
            ColorStateList legacyHeaderTextColor = textAppearance.getColorStateList(0);
            headerTextColor = applyLegacyColorFixes(legacyHeaderTextColor);
            textAppearance.recycle();
        }
        headerTextColor = headerTextColor == null ? a.getColorStateList(18) : headerTextColor;
        if (headerTextColor != null) {
            this.mHeaderYear.setTextColor(headerTextColor);
            this.mHeaderMonthDay.setTextColor(headerTextColor);
        }
        if (a.hasValueOrEmpty(0)) {
            header.setBackground(a.getDrawable(0));
        }
        a.recycle();
        this.mAnimator = (ViewAnimator) this.mContainer.findViewById(R.id.animator);
        this.mDayPickerView = (DayPickerView) this.mAnimator.findViewById(R.id.date_picker_day_picker);
        this.mDayPickerView.setFirstDayOfWeek(this.mFirstDayOfWeek);
        this.mDayPickerView.setMinDate(this.mMinDate.getTimeInMillis());
        this.mDayPickerView.setMaxDate(this.mMaxDate.getTimeInMillis());
        this.mDayPickerView.setDate(this.mCurrentDate.getTimeInMillis());
        this.mDayPickerView.setOnDaySelectedListener(this.mOnDaySelectedListener);
        this.mYearPickerView = (YearPickerView) this.mAnimator.findViewById(R.id.date_picker_year_picker);
        this.mYearPickerView.setRange(this.mMinDate, this.mMaxDate);
        this.mYearPickerView.setYear(this.mCurrentDate.get(1));
        this.mYearPickerView.setOnYearSelectedListener(this.mOnYearSelectedListener);
        this.mSelectDay = res.getString(R.string.select_day);
        this.mSelectYear = res.getString(R.string.select_year);
        onLocaleChanged(this.mCurrentLocale);
        setCurrentView(0);
    }

    private ColorStateList applyLegacyColorFixes(ColorStateList color) {
        int activatedColor;
        int defaultColor;
        if (color == null || color.hasState(16843518)) {
            return color;
        }
        if (color.hasState(16842913)) {
            activatedColor = color.getColorForState(StateSet.get(10), 0);
            defaultColor = color.getColorForState(StateSet.get(8), 0);
        } else {
            activatedColor = color.getDefaultColor();
            TypedArray ta = this.mContext.obtainStyledAttributes(ATTRS_DISABLED_ALPHA);
            float disabledAlpha = ta.getFloat(0, 0.3f);
            defaultColor = multiplyAlphaComponent(activatedColor, disabledAlpha);
        }
        if (activatedColor == 0 || defaultColor == 0) {
            return null;
        }
        int[][] stateSet = {new int[]{16843518}, new int[0]};
        int[] colors = {activatedColor, defaultColor};
        return new ColorStateList(stateSet, colors);
    }

    private int multiplyAlphaComponent(int color, float alphaMod) {
        int srcRgb = 16777215 & color;
        int srcAlpha = (color >> 24) & 255;
        int dstAlpha = (int) ((srcAlpha * alphaMod) + 0.5f);
        return (dstAlpha << 24) | srcRgb;
    }

    public /* synthetic */ void lambda$new$0$DatePickerCalendarDelegate(View v) {
        tryVibrate();
        switch (v.getId()) {
            case R.id.date_picker_header_date /* 16908961 */:
                setCurrentView(0);
                return;
            case R.id.date_picker_header_year /* 16908962 */:
                setCurrentView(1);
                return;
            default:
                return;
        }
    }

    @Override // android.widget.DatePicker.AbstractDatePickerDelegate
    protected void onLocaleChanged(Locale locale) {
        TextView headerYear = this.mHeaderYear;
        if (headerYear == null) {
            return;
        }
        this.mMonthDayFormat = DateFormat.getInstanceForSkeleton("EMMMd", locale);
        this.mMonthDayFormat.setContext(DisplayContext.CAPITALIZATION_FOR_STANDALONE);
        this.mYearFormat = DateFormat.getInstanceForSkeleton("y", locale);
        onCurrentDateChanged(false);
    }

    private void onCurrentDateChanged(boolean announce) {
        if (this.mHeaderYear == null) {
            return;
        }
        String year = this.mYearFormat.format(this.mCurrentDate.getTime());
        this.mHeaderYear.setText(year);
        String monthDay = this.mMonthDayFormat.format(this.mCurrentDate.getTime());
        this.mHeaderMonthDay.setText(monthDay);
        if (announce) {
            this.mAnimator.announceForAccessibility(getFormattedCurrentDate());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCurrentView(int viewIndex) {
        if (viewIndex == 0) {
            this.mDayPickerView.setDate(this.mCurrentDate.getTimeInMillis());
            if (this.mCurrentView != viewIndex) {
                this.mHeaderMonthDay.setActivated(true);
                this.mHeaderYear.setActivated(false);
                this.mAnimator.setDisplayedChild(0);
                this.mCurrentView = viewIndex;
            }
            this.mAnimator.announceForAccessibility(this.mSelectDay);
        } else if (viewIndex == 1) {
            int year = this.mCurrentDate.get(1);
            this.mYearPickerView.setYear(year);
            this.mYearPickerView.post(new Runnable() { // from class: android.widget.-$$Lambda$DatePickerCalendarDelegate$-6rynvAYPe1gU9xVgvSm4VMsr2M
                @Override // java.lang.Runnable
                public final void run() {
                    DatePickerCalendarDelegate.this.lambda$setCurrentView$1$DatePickerCalendarDelegate();
                }
            });
            if (this.mCurrentView != viewIndex) {
                this.mHeaderMonthDay.setActivated(false);
                this.mHeaderYear.setActivated(true);
                this.mAnimator.setDisplayedChild(1);
                this.mCurrentView = viewIndex;
            }
            this.mAnimator.announceForAccessibility(this.mSelectYear);
        }
    }

    public /* synthetic */ void lambda$setCurrentView$1$DatePickerCalendarDelegate() {
        this.mYearPickerView.requestFocus();
        View selected = this.mYearPickerView.getSelectedView();
        if (selected != null) {
            selected.requestFocus();
        }
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public void init(int year, int month, int dayOfMonth, DatePicker.OnDateChangedListener callBack) {
        setDate(year, month, dayOfMonth);
        onDateChanged(false, false);
        this.mOnDateChangedListener = callBack;
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public void updateDate(int year, int month, int dayOfMonth) {
        setDate(year, month, dayOfMonth);
        onDateChanged(false, true);
    }

    private void setDate(int year, int month, int dayOfMonth) {
        this.mCurrentDate.set(1, year);
        this.mCurrentDate.set(2, month);
        this.mCurrentDate.set(5, dayOfMonth);
        resetAutofilledValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDateChanged(boolean fromUser, boolean callbackToClient) {
        int year = this.mCurrentDate.get(1);
        if (callbackToClient && (this.mOnDateChangedListener != null || this.mAutoFillChangeListener != null)) {
            int monthOfYear = this.mCurrentDate.get(2);
            int dayOfMonth = this.mCurrentDate.get(5);
            if (this.mOnDateChangedListener != null) {
                this.mOnDateChangedListener.onDateChanged(this.mDelegator, year, monthOfYear, dayOfMonth);
            }
            if (this.mAutoFillChangeListener != null) {
                this.mAutoFillChangeListener.onDateChanged(this.mDelegator, year, monthOfYear, dayOfMonth);
            }
        }
        this.mDayPickerView.setDate(this.mCurrentDate.getTimeInMillis());
        this.mYearPickerView.setYear(year);
        onCurrentDateChanged(fromUser);
        if (fromUser) {
            tryVibrate();
        }
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public int getYear() {
        return this.mCurrentDate.get(1);
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public int getMonth() {
        return this.mCurrentDate.get(2);
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public int getDayOfMonth() {
        return this.mCurrentDate.get(5);
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public void setMinDate(long minDate) {
        this.mTempDate.setTimeInMillis(minDate);
        if (this.mTempDate.get(1) == this.mMinDate.get(1) && this.mTempDate.get(6) == this.mMinDate.get(6)) {
            return;
        }
        if (this.mCurrentDate.before(this.mTempDate)) {
            this.mCurrentDate.setTimeInMillis(minDate);
            onDateChanged(false, true);
        }
        this.mMinDate.setTimeInMillis(minDate);
        this.mDayPickerView.setMinDate(minDate);
        this.mYearPickerView.setRange(this.mMinDate, this.mMaxDate);
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public Calendar getMinDate() {
        return this.mMinDate;
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public void setMaxDate(long maxDate) {
        this.mTempDate.setTimeInMillis(maxDate);
        if (this.mTempDate.get(1) == this.mMaxDate.get(1) && this.mTempDate.get(6) == this.mMaxDate.get(6)) {
            return;
        }
        if (this.mCurrentDate.after(this.mTempDate)) {
            this.mCurrentDate.setTimeInMillis(maxDate);
            onDateChanged(false, true);
        }
        this.mMaxDate.setTimeInMillis(maxDate);
        this.mDayPickerView.setMaxDate(maxDate);
        this.mYearPickerView.setRange(this.mMinDate, this.mMaxDate);
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public Calendar getMaxDate() {
        return this.mMaxDate;
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.mFirstDayOfWeek = firstDayOfWeek;
        this.mDayPickerView.setFirstDayOfWeek(firstDayOfWeek);
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public int getFirstDayOfWeek() {
        int i = this.mFirstDayOfWeek;
        if (i != 0) {
            return i;
        }
        return this.mCurrentDate.getFirstDayOfWeek();
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public void setEnabled(boolean enabled) {
        this.mContainer.setEnabled(enabled);
        this.mDayPickerView.setEnabled(enabled);
        this.mYearPickerView.setEnabled(enabled);
        this.mHeaderYear.setEnabled(enabled);
        this.mHeaderMonthDay.setEnabled(enabled);
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public boolean isEnabled() {
        return this.mContainer.isEnabled();
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public CalendarView getCalendarView() {
        throw new UnsupportedOperationException("Not supported by calendar-mode DatePicker");
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public void setCalendarViewShown(boolean shown) {
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public boolean getCalendarViewShown() {
        return false;
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public void setSpinnersShown(boolean shown) {
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public boolean getSpinnersShown() {
        return false;
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public void onConfigurationChanged(Configuration newConfig) {
        setCurrentLocale(newConfig.locale);
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public Parcelable onSaveInstanceState(Parcelable superState) {
        int listPosition;
        int listPositionOffset;
        int year = this.mCurrentDate.get(1);
        int month = this.mCurrentDate.get(2);
        int day = this.mCurrentDate.get(5);
        int i = this.mCurrentView;
        if (i == 0) {
            listPosition = this.mDayPickerView.getMostVisiblePosition();
            listPositionOffset = -1;
        } else if (i != 1) {
            listPosition = -1;
            listPositionOffset = -1;
        } else {
            listPosition = this.mYearPickerView.getFirstVisiblePosition();
            listPositionOffset = this.mYearPickerView.getFirstPositionOffset();
        }
        return new DatePicker.AbstractDatePickerDelegate.SavedState(superState, year, month, day, this.mMinDate.getTimeInMillis(), this.mMaxDate.getTimeInMillis(), this.mCurrentView, listPosition, listPositionOffset);
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof DatePicker.AbstractDatePickerDelegate.SavedState) {
            DatePicker.AbstractDatePickerDelegate.SavedState ss = (DatePicker.AbstractDatePickerDelegate.SavedState) state;
            this.mCurrentDate.set(ss.getSelectedYear(), ss.getSelectedMonth(), ss.getSelectedDay());
            this.mMinDate.setTimeInMillis(ss.getMinDate());
            this.mMaxDate.setTimeInMillis(ss.getMaxDate());
            onCurrentDateChanged(false);
            int currentView = ss.getCurrentView();
            setCurrentView(currentView);
            int listPosition = ss.getListPosition();
            if (listPosition != -1) {
                if (currentView == 0) {
                    this.mDayPickerView.setPosition(listPosition);
                } else if (currentView == 1) {
                    int listPositionOffset = ss.getListPositionOffset();
                    this.mYearPickerView.setSelectionFromTop(listPosition, listPositionOffset);
                }
            }
        }
    }

    @Override // android.widget.DatePicker.DatePickerDelegate
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        return true;
    }

    public CharSequence getAccessibilityClassName() {
        return DatePicker.class.getName();
    }

    public static int getDaysInMonth(int month, int year) {
        switch (month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 1:
                return year % 4 == 0 ? 29 : 28;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    private void tryVibrate() {
        this.mDelegator.performHapticFeedback(5);
    }
}
