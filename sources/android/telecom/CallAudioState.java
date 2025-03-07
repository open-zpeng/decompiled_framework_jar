package android.telecom;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.telephony.IccCardConstants;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public final class CallAudioState implements Parcelable {
    public static final Parcelable.Creator<CallAudioState> CREATOR = new Parcelable.Creator<CallAudioState>() { // from class: android.telecom.CallAudioState.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CallAudioState createFromParcel(Parcel source) {
            boolean isMuted = source.readByte() != 0;
            int route = source.readInt();
            int supportedRouteMask = source.readInt();
            BluetoothDevice activeBluetoothDevice = (BluetoothDevice) source.readParcelable(ClassLoader.getSystemClassLoader());
            ArrayList arrayList = new ArrayList();
            source.readParcelableList(arrayList, ClassLoader.getSystemClassLoader());
            return new CallAudioState(isMuted, route, supportedRouteMask, activeBluetoothDevice, arrayList);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CallAudioState[] newArray(int size) {
            return new CallAudioState[size];
        }
    };
    public static final int ROUTE_ALL = 15;
    public static final int ROUTE_BLUETOOTH = 2;
    public static final int ROUTE_EARPIECE = 1;
    public static final int ROUTE_SPEAKER = 8;
    public static final int ROUTE_WIRED_HEADSET = 4;
    public static final int ROUTE_WIRED_OR_EARPIECE = 5;
    private final BluetoothDevice activeBluetoothDevice;
    private final boolean isMuted;
    private final int route;
    private final Collection<BluetoothDevice> supportedBluetoothDevices;
    private final int supportedRouteMask;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface CallAudioRoute {
    }

    public CallAudioState(boolean muted, int route, int supportedRouteMask) {
        this(muted, route, supportedRouteMask, null, Collections.emptyList());
    }

    public CallAudioState(boolean isMuted, int route, int supportedRouteMask, BluetoothDevice activeBluetoothDevice, Collection<BluetoothDevice> supportedBluetoothDevices) {
        this.isMuted = isMuted;
        this.route = route;
        this.supportedRouteMask = supportedRouteMask;
        this.activeBluetoothDevice = activeBluetoothDevice;
        this.supportedBluetoothDevices = supportedBluetoothDevices;
    }

    public CallAudioState(CallAudioState state) {
        this.isMuted = state.isMuted();
        this.route = state.getRoute();
        this.supportedRouteMask = state.getSupportedRouteMask();
        this.activeBluetoothDevice = state.activeBluetoothDevice;
        this.supportedBluetoothDevices = state.getSupportedBluetoothDevices();
    }

    public CallAudioState(AudioState state) {
        this.isMuted = state.isMuted();
        this.route = state.getRoute();
        this.supportedRouteMask = state.getSupportedRouteMask();
        this.activeBluetoothDevice = null;
        this.supportedBluetoothDevices = Collections.emptyList();
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CallAudioState)) {
            return false;
        }
        CallAudioState state = (CallAudioState) obj;
        if (this.supportedBluetoothDevices.size() != state.supportedBluetoothDevices.size()) {
            return false;
        }
        for (BluetoothDevice device : this.supportedBluetoothDevices) {
            if (!state.supportedBluetoothDevices.contains(device)) {
                return false;
            }
        }
        return Objects.equals(this.activeBluetoothDevice, state.activeBluetoothDevice) && isMuted() == state.isMuted() && getRoute() == state.getRoute() && getSupportedRouteMask() == state.getSupportedRouteMask();
    }

    public String toString() {
        String bluetoothDeviceList = (String) this.supportedBluetoothDevices.stream().map(new Function() { // from class: android.telecom.-$$Lambda$cyYWqCYT05eM23eLVm4oQ5DrYjw
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((BluetoothDevice) obj).getAddress();
            }
        }).collect(Collectors.joining(", "));
        return String.format(Locale.US, "[AudioState isMuted: %b, route: %s, supportedRouteMask: %s, activeBluetoothDevice: [%s], supportedBluetoothDevices: [%s]]", Boolean.valueOf(this.isMuted), audioRouteToString(this.route), audioRouteToString(this.supportedRouteMask), this.activeBluetoothDevice, bluetoothDeviceList);
    }

    public boolean isMuted() {
        return this.isMuted;
    }

    public int getRoute() {
        return this.route;
    }

    public int getSupportedRouteMask() {
        return this.supportedRouteMask;
    }

    public BluetoothDevice getActiveBluetoothDevice() {
        return this.activeBluetoothDevice;
    }

    public Collection<BluetoothDevice> getSupportedBluetoothDevices() {
        return this.supportedBluetoothDevices;
    }

    public static String audioRouteToString(int route) {
        if (route == 0 || (route & (-16)) != 0) {
            return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
        StringBuffer buffer = new StringBuffer();
        if ((route & 1) == 1) {
            listAppend(buffer, "EARPIECE");
        }
        if ((route & 2) == 2) {
            listAppend(buffer, "BLUETOOTH");
        }
        if ((route & 4) == 4) {
            listAppend(buffer, "WIRED_HEADSET");
        }
        if ((route & 8) == 8) {
            listAppend(buffer, "SPEAKER");
        }
        return buffer.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeByte(this.isMuted ? (byte) 1 : (byte) 0);
        destination.writeInt(this.route);
        destination.writeInt(this.supportedRouteMask);
        destination.writeParcelable(this.activeBluetoothDevice, 0);
        destination.writeParcelableList(new ArrayList(this.supportedBluetoothDevices), 0);
    }

    private static void listAppend(StringBuffer buffer, String str) {
        if (buffer.length() > 0) {
            buffer.append(", ");
        }
        buffer.append(str);
    }
}
