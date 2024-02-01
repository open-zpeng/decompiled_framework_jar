package android.widget;

import android.content.Context;
import android.os.LocaleList;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.View;
import android.widget.AdapterView;
import com.android.internal.R;
/* loaded from: classes3.dex */
public class TextInputTimePickerView extends RelativeLayout {
    private static final int AM = 0;
    public static final int AMPM = 2;
    public static final int HOURS = 0;
    public static final int MINUTES = 1;
    private static final int PM = 1;
    private final Spinner mAmPmSpinner;
    private final TextView mErrorLabel;
    private boolean mErrorShowing;
    private final EditText mHourEditText;
    private boolean mHourFormatStartsAtZero;
    private final TextView mHourLabel;
    private final TextView mInputSeparatorView;
    private boolean mIs24Hour;
    private OnValueTypedListener mListener;
    private final EditText mMinuteEditText;
    private final TextView mMinuteLabel;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public interface OnValueTypedListener {
        synchronized void onValueChanged(int i, int i2);
    }

    public synchronized TextInputTimePickerView(Context context) {
        this(context, null);
    }

    public synchronized TextInputTimePickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public synchronized TextInputTimePickerView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);
    }

    public synchronized TextInputTimePickerView(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
        inflate(context, R.layout.time_picker_text_input_material, this);
        this.mHourEditText = (EditText) findViewById(R.id.input_hour);
        this.mMinuteEditText = (EditText) findViewById(R.id.input_minute);
        this.mInputSeparatorView = (TextView) findViewById(R.id.input_separator);
        this.mErrorLabel = (TextView) findViewById(R.id.label_error);
        this.mHourLabel = (TextView) findViewById(R.id.label_hour);
        this.mMinuteLabel = (TextView) findViewById(R.id.label_minute);
        this.mHourEditText.addTextChangedListener(new TextWatcher() { // from class: android.widget.TextInputTimePickerView.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                TextInputTimePickerView.this.parseAndSetHourInternal(editable.toString());
            }
        });
        this.mMinuteEditText.addTextChangedListener(new TextWatcher() { // from class: android.widget.TextInputTimePickerView.2
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                TextInputTimePickerView.this.parseAndSetMinuteInternal(editable.toString());
            }
        });
        this.mAmPmSpinner = (Spinner) findViewById(R.id.am_pm_spinner);
        String[] amPmStrings = TimePicker.getAmPmStrings(context);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(context, 17367049);
        adapter.add(TimePickerClockDelegate.obtainVerbatim(amPmStrings[0]));
        adapter.add(TimePickerClockDelegate.obtainVerbatim(amPmStrings[1]));
        this.mAmPmSpinner.setAdapter((SpinnerAdapter) adapter);
        this.mAmPmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: android.widget.TextInputTimePickerView.3
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    TextInputTimePickerView.this.mListener.onValueChanged(2, 0);
                } else {
                    TextInputTimePickerView.this.mListener.onValueChanged(2, 1);
                }
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setListener(OnValueTypedListener listener) {
        this.mListener = listener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setHourFormat(int maxCharLength) {
        this.mHourEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxCharLength)});
        this.mMinuteEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxCharLength)});
        LocaleList locales = this.mContext.getResources().getConfiguration().getLocales();
        this.mHourEditText.setImeHintLocales(locales);
        this.mMinuteEditText.setImeHintLocales(locales);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean validateInput() {
        boolean inputValid = parseAndSetHourInternal(this.mHourEditText.getText().toString()) && parseAndSetMinuteInternal(this.mMinuteEditText.getText().toString());
        setError(inputValid ? false : true);
        return inputValid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void updateSeparator(String separatorText) {
        this.mInputSeparatorView.setText(separatorText);
    }

    private synchronized void setError(boolean enabled) {
        this.mErrorShowing = enabled;
        this.mErrorLabel.setVisibility(enabled ? 0 : 4);
        this.mHourLabel.setVisibility(enabled ? 4 : 0);
        this.mMinuteLabel.setVisibility(enabled ? 4 : 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void updateTextInputValues(int localizedHour, int minute, int amOrPm, boolean is24Hour, boolean hourFormatStartsAtZero) {
        this.mIs24Hour = is24Hour;
        this.mHourFormatStartsAtZero = hourFormatStartsAtZero;
        this.mAmPmSpinner.setVisibility(is24Hour ? 4 : 0);
        if (amOrPm != 0) {
            this.mAmPmSpinner.setSelection(1);
        } else {
            this.mAmPmSpinner.setSelection(0);
        }
        this.mHourEditText.setText(String.format("%d", Integer.valueOf(localizedHour)));
        this.mMinuteEditText.setText(String.format("%02d", Integer.valueOf(minute)));
        if (this.mErrorShowing) {
            validateInput();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean parseAndSetHourInternal(String input) {
        try {
            int hour = Integer.parseInt(input);
            if (isValidLocalizedHour(hour)) {
                this.mListener.onValueChanged(0, getHourOfDayFromLocalizedHour(hour));
                return true;
            }
            int minHour = !this.mHourFormatStartsAtZero ? 1 : 0;
            int maxHour = this.mIs24Hour ? 23 : 11 + minHour;
            this.mListener.onValueChanged(0, getHourOfDayFromLocalizedHour(MathUtils.constrain(hour, minHour, maxHour)));
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean parseAndSetMinuteInternal(String input) {
        try {
            int minutes = Integer.parseInt(input);
            if (minutes >= 0 && minutes <= 59) {
                this.mListener.onValueChanged(1, minutes);
                return true;
            }
            this.mListener.onValueChanged(1, MathUtils.constrain(minutes, 0, 59));
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private synchronized boolean isValidLocalizedHour(int localizedHour) {
        int minHour = !this.mHourFormatStartsAtZero ? 1 : 0;
        int maxHour = (this.mIs24Hour ? 23 : 11) + minHour;
        return localizedHour >= minHour && localizedHour <= maxHour;
    }

    private synchronized int getHourOfDayFromLocalizedHour(int localizedHour) {
        int hourOfDay = localizedHour;
        if (this.mIs24Hour) {
            if (!this.mHourFormatStartsAtZero && localizedHour == 24) {
                return 0;
            }
            return hourOfDay;
        }
        if (!this.mHourFormatStartsAtZero && localizedHour == 12) {
            hourOfDay = 0;
        }
        if (this.mAmPmSpinner.getSelectedItemPosition() == 1) {
            return hourOfDay + 12;
        }
        return hourOfDay;
    }
}
