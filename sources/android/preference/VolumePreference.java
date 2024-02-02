package android.preference;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.SeekBarVolumizer;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;
import com.android.internal.R;
/* loaded from: classes2.dex */
public class VolumePreference extends SeekBarDialogPreference implements PreferenceManager.OnActivityStopListener, View.OnKeyListener, SeekBarVolumizer.Callback {
    private SeekBarVolumizer mSeekBarVolumizer;
    public protected int mStreamType;

    /* loaded from: classes2.dex */
    public static class VolumeStore {
        private protected int volume = -1;
        private protected int originalVolume = -1;
    }

    public synchronized VolumePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VolumePreference, defStyleAttr, defStyleRes);
        this.mStreamType = a.getInt(0, 0);
        a.recycle();
    }

    public synchronized VolumePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    private protected VolumePreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.seekBarDialogPreferenceStyle);
    }

    public synchronized VolumePreference(Context context) {
        this(context, null);
    }

    public synchronized void setStreamType(int streamType) {
        this.mStreamType = streamType;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.preference.SeekBarDialogPreference, android.preference.DialogPreference
    public void onBindDialogView(View view) {
        super.onBindDialogView(view);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        this.mSeekBarVolumizer = new SeekBarVolumizer(getContext(), this.mStreamType, null, this);
        this.mSeekBarVolumizer.start();
        this.mSeekBarVolumizer.setSeekBar(seekBar);
        getPreferenceManager().registerOnActivityStopListener(this);
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    @Override // android.view.View.OnKeyListener
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (this.mSeekBarVolumizer == null) {
            return true;
        }
        boolean isdown = event.getAction() == 0;
        if (keyCode != 164) {
            switch (keyCode) {
                case 24:
                    if (isdown) {
                        this.mSeekBarVolumizer.changeVolumeBy(1);
                    }
                    return true;
                case 25:
                    if (isdown) {
                        this.mSeekBarVolumizer.changeVolumeBy(-1);
                    }
                    return true;
                default:
                    return false;
            }
        }
        if (isdown) {
            this.mSeekBarVolumizer.muteVolume();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.preference.DialogPreference
    public void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (!positiveResult && this.mSeekBarVolumizer != null) {
            this.mSeekBarVolumizer.revertVolume();
        }
        cleanup();
    }

    @Override // android.preference.PreferenceManager.OnActivityStopListener
    public void onActivityStop() {
        if (this.mSeekBarVolumizer != null) {
            this.mSeekBarVolumizer.stopSample();
        }
    }

    private synchronized void cleanup() {
        getPreferenceManager().unregisterOnActivityStopListener(this);
        if (this.mSeekBarVolumizer != null) {
            Dialog dialog = getDialog();
            if (dialog != null && dialog.isShowing()) {
                View view = dialog.getWindow().getDecorView().findViewById(R.id.seekbar);
                if (view != null) {
                    view.setOnKeyListener(null);
                }
                this.mSeekBarVolumizer.revertVolume();
            }
            this.mSeekBarVolumizer.stop();
            this.mSeekBarVolumizer = null;
        }
    }

    @Override // android.preference.SeekBarVolumizer.Callback
    public synchronized void onSampleStarting(SeekBarVolumizer volumizer) {
        if (this.mSeekBarVolumizer != null && volumizer != this.mSeekBarVolumizer) {
            this.mSeekBarVolumizer.stopSample();
        }
    }

    @Override // android.preference.SeekBarVolumizer.Callback
    public synchronized void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
    }

    @Override // android.preference.SeekBarVolumizer.Callback
    public synchronized void onMuted(boolean muted, boolean zenMuted) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.preference.DialogPreference, android.preference.Preference
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }
        SavedState myState = new SavedState(superState);
        if (this.mSeekBarVolumizer != null) {
            this.mSeekBarVolumizer.onSaveInstanceState(myState.getVolumeStore());
        }
        return myState;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.preference.DialogPreference, android.preference.Preference
    public void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        if (this.mSeekBarVolumizer != null) {
            this.mSeekBarVolumizer.onRestoreInstanceState(myState.getVolumeStore());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: android.preference.VolumePreference.SavedState.1
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
        VolumeStore mVolumeStore;

        public synchronized SavedState(Parcel source) {
            super(source);
            this.mVolumeStore = new VolumeStore();
            this.mVolumeStore.volume = source.readInt();
            this.mVolumeStore.originalVolume = source.readInt();
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.mVolumeStore.volume);
            dest.writeInt(this.mVolumeStore.originalVolume);
        }

        synchronized VolumeStore getVolumeStore() {
            return this.mVolumeStore;
        }

        public synchronized SavedState(Parcelable superState) {
            super(superState);
            this.mVolumeStore = new VolumeStore();
        }
    }
}
