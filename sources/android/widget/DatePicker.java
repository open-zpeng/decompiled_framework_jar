package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.icu.util.Calendar;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityEvent;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import com.android.internal.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;
/* loaded from: classes3.dex */
public class DatePicker extends FrameLayout {
    private static final String LOG_TAG = DatePicker.class.getSimpleName();
    public static final int MODE_CALENDAR = 2;
    public static final int MODE_SPINNER = 1;
    public protected final DatePickerDelegate mDelegate;
    private final int mMode;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public interface DatePickerDelegate {
        synchronized void autofill(AutofillValue autofillValue);

        synchronized boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        synchronized AutofillValue getAutofillValue();

        synchronized CalendarView getCalendarView();

        synchronized boolean getCalendarViewShown();

        synchronized int getDayOfMonth();

        synchronized int getFirstDayOfWeek();

        synchronized Calendar getMaxDate();

        synchronized Calendar getMinDate();

        synchronized int getMonth();

        synchronized boolean getSpinnersShown();

        synchronized int getYear();

        synchronized void init(int i, int i2, int i3, OnDateChangedListener onDateChangedListener);

        synchronized boolean isEnabled();

        synchronized void onConfigurationChanged(Configuration configuration);

        synchronized void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        synchronized void onRestoreInstanceState(Parcelable parcelable);

        synchronized Parcelable onSaveInstanceState(Parcelable parcelable);

        synchronized void setAutoFillChangeListener(OnDateChangedListener onDateChangedListener);

        synchronized void setCalendarViewShown(boolean z);

        synchronized void setEnabled(boolean z);

        synchronized void setFirstDayOfWeek(int i);

        synchronized void setMaxDate(long j);

        synchronized void setMinDate(long j);

        synchronized void setOnDateChangedListener(OnDateChangedListener onDateChangedListener);

        synchronized void setSpinnersShown(boolean z);

        synchronized void setValidationCallback(ValidationCallback validationCallback);

        synchronized void updateDate(int i, int i2, int i3);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface DatePickerMode {
    }

    /* loaded from: classes3.dex */
    public interface OnDateChangedListener {
        void onDateChanged(DatePicker datePicker, int i, int i2, int i3);
    }

    /* loaded from: classes3.dex */
    public interface ValidationCallback {
        synchronized void onValidationChanged(boolean z);
    }

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 16843612);
    }

    public DatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DatePicker(final Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (getImportantForAutofill() == 0) {
            setImportantForAutofill(1);
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DatePicker, defStyleAttr, defStyleRes);
        boolean isDialogMode = a.getBoolean(17, false);
        int requestedMode = a.getInt(16, 1);
        int firstDayOfWeek = a.getInt(3, 0);
        a.recycle();
        if (requestedMode == 2 && isDialogMode) {
            this.mMode = context.getResources().getInteger(R.integer.date_picker_mode);
        } else {
            this.mMode = requestedMode;
        }
        if (this.mMode == 2) {
            this.mDelegate = createCalendarUIDelegate(context, attrs, defStyleAttr, defStyleRes);
        } else {
            this.mDelegate = createSpinnerUIDelegate(context, attrs, defStyleAttr, defStyleRes);
        }
        if (firstDayOfWeek != 0) {
            setFirstDayOfWeek(firstDayOfWeek);
        }
        this.mDelegate.setAutoFillChangeListener(new OnDateChangedListener() { // from class: android.widget.-$$Lambda$DatePicker$AnJPL5BrPXPJa-Oc-WUAB-HJq84
            @Override // android.widget.DatePicker.OnDateChangedListener
            public final void onDateChanged(DatePicker datePicker, int i, int i2, int i3) {
                DatePicker.lambda$new$0(DatePicker.this, context, datePicker, i, i2, i3);
            }
        });
    }

    public static /* synthetic */ void lambda$new$0(DatePicker datePicker, Context context, DatePicker v, int y, int m, int d) {
        AutofillManager afm = (AutofillManager) context.getSystemService(AutofillManager.class);
        if (afm != null) {
            afm.notifyValueChanged(datePicker);
        }
    }

    private synchronized DatePickerDelegate createSpinnerUIDelegate(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        return new DatePickerSpinnerDelegate(this, context, attrs, defStyleAttr, defStyleRes);
    }

    private synchronized DatePickerDelegate createCalendarUIDelegate(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        return new DatePickerCalendarDelegate(this, context, attrs, defStyleAttr, defStyleRes);
    }

    public int getMode() {
        return this.mMode;
    }

    public void init(int year, int monthOfYear, int dayOfMonth, OnDateChangedListener onDateChangedListener) {
        this.mDelegate.init(year, monthOfYear, dayOfMonth, onDateChangedListener);
    }

    public void setOnDateChangedListener(OnDateChangedListener onDateChangedListener) {
        this.mDelegate.setOnDateChangedListener(onDateChangedListener);
    }

    public void updateDate(int year, int month, int dayOfMonth) {
        this.mDelegate.updateDate(year, month, dayOfMonth);
    }

    public int getYear() {
        return this.mDelegate.getYear();
    }

    public int getMonth() {
        return this.mDelegate.getMonth();
    }

    public int getDayOfMonth() {
        return this.mDelegate.getDayOfMonth();
    }

    public long getMinDate() {
        return this.mDelegate.getMinDate().getTimeInMillis();
    }

    public void setMinDate(long minDate) {
        this.mDelegate.setMinDate(minDate);
    }

    public long getMaxDate() {
        return this.mDelegate.getMaxDate().getTimeInMillis();
    }

    public void setMaxDate(long maxDate) {
        this.mDelegate.setMaxDate(maxDate);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setValidationCallback(ValidationCallback callback) {
        this.mDelegate.setValidationCallback(callback);
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        if (this.mDelegate.isEnabled() == enabled) {
            return;
        }
        super.setEnabled(enabled);
        this.mDelegate.setEnabled(enabled);
    }

    @Override // android.view.View
    public boolean isEnabled() {
        return this.mDelegate.isEnabled();
    }

    @Override // android.view.ViewGroup, android.view.View
    public synchronized boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        return this.mDelegate.dispatchPopulateAccessibilityEvent(event);
    }

    @Override // android.view.View
    public synchronized void onPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        super.onPopulateAccessibilityEventInternal(event);
        this.mDelegate.onPopulateAccessibilityEvent(event);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return DatePicker.class.getName();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mDelegate.onConfigurationChanged(newConfig);
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        if (firstDayOfWeek < 1 || firstDayOfWeek > 7) {
            throw new IllegalArgumentException("firstDayOfWeek must be between 1 and 7");
        }
        this.mDelegate.setFirstDayOfWeek(firstDayOfWeek);
    }

    public int getFirstDayOfWeek() {
        return this.mDelegate.getFirstDayOfWeek();
    }

    @Deprecated
    public boolean getCalendarViewShown() {
        return this.mDelegate.getCalendarViewShown();
    }

    @Deprecated
    public CalendarView getCalendarView() {
        return this.mDelegate.getCalendarView();
    }

    @Deprecated
    public void setCalendarViewShown(boolean shown) {
        this.mDelegate.setCalendarViewShown(shown);
    }

    @Deprecated
    public boolean getSpinnersShown() {
        return this.mDelegate.getSpinnersShown();
    }

    @Deprecated
    public void setSpinnersShown(boolean shown) {
        this.mDelegate.setSpinnersShown(shown);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return this.mDelegate.onSaveInstanceState(superState);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable state) {
        View.BaseSavedState ss = (View.BaseSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mDelegate.onRestoreInstanceState(ss);
    }

    /* loaded from: classes3.dex */
    static abstract class AbstractDatePickerDelegate implements DatePickerDelegate {
        protected OnDateChangedListener mAutoFillChangeListener;
        private long mAutofilledValue;
        protected Context mContext;
        protected Calendar mCurrentDate;
        protected Locale mCurrentLocale;
        protected DatePicker mDelegator;
        protected OnDateChangedListener mOnDateChangedListener;
        protected ValidationCallback mValidationCallback;

        public synchronized AbstractDatePickerDelegate(DatePicker delegator, Context context) {
            this.mDelegator = delegator;
            this.mContext = context;
            setCurrentLocale(Locale.getDefault());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public synchronized void setCurrentLocale(Locale locale) {
            if (!locale.equals(this.mCurrentLocale)) {
                this.mCurrentLocale = locale;
                onLocaleChanged(locale);
            }
        }

        @Override // android.widget.DatePicker.DatePickerDelegate
        public synchronized void setOnDateChangedListener(OnDateChangedListener callback) {
            this.mOnDateChangedListener = callback;
        }

        @Override // android.widget.DatePicker.DatePickerDelegate
        public synchronized void setAutoFillChangeListener(OnDateChangedListener callback) {
            this.mAutoFillChangeListener = callback;
        }

        @Override // android.widget.DatePicker.DatePickerDelegate
        public synchronized void setValidationCallback(ValidationCallback callback) {
            this.mValidationCallback = callback;
        }

        @Override // android.widget.DatePicker.DatePickerDelegate
        public final synchronized void autofill(AutofillValue value) {
            if (value == null || !value.isDate()) {
                String str = DatePicker.LOG_TAG;
                Log.w(str, value + " could not be autofilled into " + this);
                return;
            }
            long time = value.getDateValue();
            Calendar cal = Calendar.getInstance(this.mCurrentLocale);
            cal.setTimeInMillis(time);
            updateDate(cal.get(1), cal.get(2), cal.get(5));
            this.mAutofilledValue = time;
        }

        @Override // android.widget.DatePicker.DatePickerDelegate
        public final synchronized AutofillValue getAutofillValue() {
            long time;
            if (this.mAutofilledValue != 0) {
                time = this.mAutofilledValue;
            } else {
                time = this.mCurrentDate.getTimeInMillis();
            }
            return AutofillValue.forDate(time);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public synchronized void resetAutofilledValue() {
            this.mAutofilledValue = 0L;
        }

        protected synchronized void onValidationChanged(boolean valid) {
            if (this.mValidationCallback != null) {
                this.mValidationCallback.onValidationChanged(valid);
            }
        }

        protected synchronized void onLocaleChanged(Locale locale) {
        }

        @Override // android.widget.DatePicker.DatePickerDelegate
        public synchronized void onPopulateAccessibilityEvent(AccessibilityEvent event) {
            event.getText().add(getFormattedCurrentDate());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public synchronized String getFormattedCurrentDate() {
            return DateUtils.formatDateTime(this.mContext, this.mCurrentDate.getTimeInMillis(), 22);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes3.dex */
        public static class SavedState extends View.BaseSavedState {
            public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: android.widget.DatePicker.AbstractDatePickerDelegate.SavedState.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // android.os.Parcelable.Creator
                public SavedState createFromParcel(Parcel in) {
                    return new SavedState(in);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // android.os.Parcelable.Creator
                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
            private final int mCurrentView;
            private final int mListPosition;
            private final int mListPositionOffset;
            private final long mMaxDate;
            private final long mMinDate;
            private final int mSelectedDay;
            private final int mSelectedMonth;
            private final int mSelectedYear;

            public synchronized SavedState(Parcelable superState, int year, int month, int day, long minDate, long maxDate) {
                this(superState, year, month, day, minDate, maxDate, 0, 0, 0);
            }

            public synchronized SavedState(Parcelable superState, int year, int month, int day, long minDate, long maxDate, int currentView, int listPosition, int listPositionOffset) {
                super(superState);
                this.mSelectedYear = year;
                this.mSelectedMonth = month;
                this.mSelectedDay = day;
                this.mMinDate = minDate;
                this.mMaxDate = maxDate;
                this.mCurrentView = currentView;
                this.mListPosition = listPosition;
                this.mListPositionOffset = listPositionOffset;
            }

            private synchronized SavedState(Parcel in) {
                super(in);
                this.mSelectedYear = in.readInt();
                this.mSelectedMonth = in.readInt();
                this.mSelectedDay = in.readInt();
                this.mMinDate = in.readLong();
                this.mMaxDate = in.readLong();
                this.mCurrentView = in.readInt();
                this.mListPosition = in.readInt();
                this.mListPositionOffset = in.readInt();
            }

            @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
            public void writeToParcel(Parcel dest, int flags) {
                super.writeToParcel(dest, flags);
                dest.writeInt(this.mSelectedYear);
                dest.writeInt(this.mSelectedMonth);
                dest.writeInt(this.mSelectedDay);
                dest.writeLong(this.mMinDate);
                dest.writeLong(this.mMaxDate);
                dest.writeInt(this.mCurrentView);
                dest.writeInt(this.mListPosition);
                dest.writeInt(this.mListPositionOffset);
            }

            public synchronized int getSelectedDay() {
                return this.mSelectedDay;
            }

            public synchronized int getSelectedMonth() {
                return this.mSelectedMonth;
            }

            public synchronized int getSelectedYear() {
                return this.mSelectedYear;
            }

            public synchronized long getMinDate() {
                return this.mMinDate;
            }

            public synchronized long getMaxDate() {
                return this.mMaxDate;
            }

            public synchronized int getCurrentView() {
                return this.mCurrentView;
            }

            public synchronized int getListPosition() {
                return this.mListPosition;
            }

            public synchronized int getListPositionOffset() {
                return this.mListPositionOffset;
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchProvideAutofillStructure(ViewStructure structure, int flags) {
        structure.setAutofillId(getAutofillId());
        onProvideAutofillStructure(structure, flags);
    }

    @Override // android.view.View
    public void autofill(AutofillValue value) {
        if (isEnabled()) {
            this.mDelegate.autofill(value);
        }
    }

    @Override // android.view.View
    public int getAutofillType() {
        return isEnabled() ? 4 : 0;
    }

    @Override // android.view.View
    public AutofillValue getAutofillValue() {
        if (isEnabled()) {
            return this.mDelegate.getAutofillValue();
        }
        return null;
    }
}
