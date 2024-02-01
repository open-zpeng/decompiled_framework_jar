package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.icu.util.Calendar;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityEvent;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import com.android.internal.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;
import libcore.icu.LocaleData;
/* loaded from: classes3.dex */
public class TimePicker extends FrameLayout {
    private static final String LOG_TAG = TimePicker.class.getSimpleName();
    public static final int MODE_CLOCK = 2;
    public static final int MODE_SPINNER = 1;
    public protected final TimePickerDelegate mDelegate;
    private final int mMode;

    /* loaded from: classes3.dex */
    public interface OnTimeChangedListener {
        void onTimeChanged(TimePicker timePicker, int i, int i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public interface TimePickerDelegate {
        synchronized void autofill(AutofillValue autofillValue);

        synchronized boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        synchronized View getAmView();

        synchronized AutofillValue getAutofillValue();

        synchronized int getBaseline();

        synchronized int getHour();

        synchronized View getHourView();

        synchronized int getMinute();

        synchronized View getMinuteView();

        synchronized View getPmView();

        synchronized boolean is24Hour();

        synchronized boolean isEnabled();

        synchronized void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent);

        synchronized void onRestoreInstanceState(Parcelable parcelable);

        synchronized Parcelable onSaveInstanceState(Parcelable parcelable);

        synchronized void setAutoFillChangeListener(OnTimeChangedListener onTimeChangedListener);

        synchronized void setDate(int i, int i2);

        synchronized void setEnabled(boolean z);

        synchronized void setHour(int i);

        synchronized void setIs24Hour(boolean z);

        synchronized void setMinute(int i);

        synchronized void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener);

        synchronized boolean validateInput();
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface TimePickerMode {
    }

    public TimePicker(Context context) {
        this(context, null);
    }

    public TimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 16843933);
    }

    public TimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TimePicker(final Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (getImportantForAutofill() == 0) {
            setImportantForAutofill(1);
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimePicker, defStyleAttr, defStyleRes);
        boolean isDialogMode = a.getBoolean(10, false);
        int requestedMode = a.getInt(8, 1);
        a.recycle();
        if (requestedMode == 2 && isDialogMode) {
            this.mMode = context.getResources().getInteger(R.integer.time_picker_mode);
        } else {
            this.mMode = requestedMode;
        }
        if (this.mMode == 2) {
            this.mDelegate = new TimePickerClockDelegate(this, context, attrs, defStyleAttr, defStyleRes);
        } else {
            this.mDelegate = new TimePickerSpinnerDelegate(this, context, attrs, defStyleAttr, defStyleRes);
        }
        this.mDelegate.setAutoFillChangeListener(new OnTimeChangedListener() { // from class: android.widget.-$$Lambda$TimePicker$2FhAB9WgnLgn4zn4f9rRT7DNfjw
            @Override // android.widget.TimePicker.OnTimeChangedListener
            public final void onTimeChanged(TimePicker timePicker, int i, int i2) {
                TimePicker.lambda$new$0(TimePicker.this, context, timePicker, i, i2);
            }
        });
    }

    public static /* synthetic */ void lambda$new$0(TimePicker timePicker, Context context, TimePicker v, int h, int m) {
        AutofillManager afm = (AutofillManager) context.getSystemService(AutofillManager.class);
        if (afm != null) {
            afm.notifyValueChanged(timePicker);
        }
    }

    public int getMode() {
        return this.mMode;
    }

    public void setHour(int hour) {
        this.mDelegate.setHour(MathUtils.constrain(hour, 0, 23));
    }

    public int getHour() {
        return this.mDelegate.getHour();
    }

    public void setMinute(int minute) {
        this.mDelegate.setMinute(MathUtils.constrain(minute, 0, 59));
    }

    public int getMinute() {
        return this.mDelegate.getMinute();
    }

    @Deprecated
    public void setCurrentHour(Integer currentHour) {
        setHour(currentHour.intValue());
    }

    @Deprecated
    public Integer getCurrentHour() {
        return Integer.valueOf(getHour());
    }

    @Deprecated
    public void setCurrentMinute(Integer currentMinute) {
        setMinute(currentMinute.intValue());
    }

    @Deprecated
    public Integer getCurrentMinute() {
        return Integer.valueOf(getMinute());
    }

    public void setIs24HourView(Boolean is24HourView) {
        if (is24HourView == null) {
            return;
        }
        this.mDelegate.setIs24Hour(is24HourView.booleanValue());
    }

    public boolean is24HourView() {
        return this.mDelegate.is24Hour();
    }

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        this.mDelegate.setOnTimeChangedListener(onTimeChangedListener);
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.mDelegate.setEnabled(enabled);
    }

    @Override // android.view.View
    public boolean isEnabled() {
        return this.mDelegate.isEnabled();
    }

    @Override // android.view.View
    public int getBaseline() {
        return this.mDelegate.getBaseline();
    }

    public boolean validateInput() {
        return this.mDelegate.validateInput();
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

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return TimePicker.class.getName();
    }

    @Override // android.view.ViewGroup, android.view.View
    public synchronized boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        return this.mDelegate.dispatchPopulateAccessibilityEvent(event);
    }

    public View getHourView() {
        return this.mDelegate.getHourView();
    }

    public View getMinuteView() {
        return this.mDelegate.getMinuteView();
    }

    public View getAmView() {
        return this.mDelegate.getAmView();
    }

    public View getPmView() {
        return this.mDelegate.getPmView();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized String[] getAmPmStrings(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        LocaleData d = LocaleData.get(locale);
        String[] result = new String[2];
        result[0] = d.amPm[0].length() > 4 ? d.narrowAm : d.amPm[0];
        result[1] = d.amPm[1].length() > 4 ? d.narrowPm : d.amPm[1];
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static abstract class AbstractTimePickerDelegate implements TimePickerDelegate {
        protected OnTimeChangedListener mAutoFillChangeListener;
        private long mAutofilledValue;
        protected final Context mContext;
        protected final TimePicker mDelegator;
        protected final Locale mLocale;
        protected OnTimeChangedListener mOnTimeChangedListener;

        public synchronized AbstractTimePickerDelegate(TimePicker delegator, Context context) {
            this.mDelegator = delegator;
            this.mContext = context;
            this.mLocale = context.getResources().getConfiguration().locale;
        }

        @Override // android.widget.TimePicker.TimePickerDelegate
        public synchronized void setOnTimeChangedListener(OnTimeChangedListener callback) {
            this.mOnTimeChangedListener = callback;
        }

        @Override // android.widget.TimePicker.TimePickerDelegate
        public synchronized void setAutoFillChangeListener(OnTimeChangedListener callback) {
            this.mAutoFillChangeListener = callback;
        }

        @Override // android.widget.TimePicker.TimePickerDelegate
        public final synchronized void autofill(AutofillValue value) {
            if (value == null || !value.isDate()) {
                String str = TimePicker.LOG_TAG;
                Log.w(str, value + " could not be autofilled into " + this);
                return;
            }
            long time = value.getDateValue();
            Calendar cal = Calendar.getInstance(this.mLocale);
            cal.setTimeInMillis(time);
            setDate(cal.get(11), cal.get(12));
            this.mAutofilledValue = time;
        }

        @Override // android.widget.TimePicker.TimePickerDelegate
        public final synchronized AutofillValue getAutofillValue() {
            if (this.mAutofilledValue != 0) {
                return AutofillValue.forDate(this.mAutofilledValue);
            }
            Calendar cal = Calendar.getInstance(this.mLocale);
            cal.set(11, getHour());
            cal.set(12, getMinute());
            return AutofillValue.forDate(cal.getTimeInMillis());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public synchronized void resetAutofilledValue() {
            this.mAutofilledValue = 0L;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: classes3.dex */
        public static class SavedState extends View.BaseSavedState {
            public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: android.widget.TimePicker.AbstractTimePickerDelegate.SavedState.1
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
            private final int mCurrentItemShowing;
            private final int mHour;
            private final boolean mIs24HourMode;
            private final int mMinute;

            public synchronized SavedState(Parcelable superState, int hour, int minute, boolean is24HourMode) {
                this(superState, hour, minute, is24HourMode, 0);
            }

            public synchronized SavedState(Parcelable superState, int hour, int minute, boolean is24HourMode, int currentItemShowing) {
                super(superState);
                this.mHour = hour;
                this.mMinute = minute;
                this.mIs24HourMode = is24HourMode;
                this.mCurrentItemShowing = currentItemShowing;
            }

            private synchronized SavedState(Parcel in) {
                super(in);
                this.mHour = in.readInt();
                this.mMinute = in.readInt();
                this.mIs24HourMode = in.readInt() == 1;
                this.mCurrentItemShowing = in.readInt();
            }

            public synchronized int getHour() {
                return this.mHour;
            }

            public synchronized int getMinute() {
                return this.mMinute;
            }

            public synchronized boolean is24HourMode() {
                return this.mIs24HourMode;
            }

            public synchronized int getCurrentItemShowing() {
                return this.mCurrentItemShowing;
            }

            @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
            public void writeToParcel(Parcel dest, int flags) {
                super.writeToParcel(dest, flags);
                dest.writeInt(this.mHour);
                dest.writeInt(this.mMinute);
                dest.writeInt(this.mIs24HourMode ? 1 : 0);
                dest.writeInt(this.mCurrentItemShowing);
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
