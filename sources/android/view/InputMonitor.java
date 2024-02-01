package android.view;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.view.IInputMonitorHost;

/* loaded from: classes3.dex */
public final class InputMonitor implements Parcelable {
    public static final Parcelable.Creator<InputMonitor> CREATOR = new Parcelable.Creator<InputMonitor>() { // from class: android.view.InputMonitor.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public InputMonitor createFromParcel(Parcel source) {
            return new InputMonitor(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public InputMonitor[] newArray(int size) {
            return new InputMonitor[size];
        }
    };
    private static final boolean DEBUG = false;
    private static final String TAG = "InputMonitor";
    private final InputChannel mChannel;
    private final IInputMonitorHost mHost;
    private final String mName;

    public InputMonitor(String name, InputChannel channel, IInputMonitorHost host) {
        this.mName = name;
        this.mChannel = channel;
        this.mHost = host;
    }

    public InputMonitor(Parcel in) {
        this.mName = in.readString();
        this.mChannel = (InputChannel) in.readParcelable(null);
        this.mHost = IInputMonitorHost.Stub.asInterface(in.readStrongBinder());
    }

    public InputChannel getInputChannel() {
        return this.mChannel;
    }

    public String getName() {
        return this.mName;
    }

    public void pilferPointers() {
        try {
            this.mHost.pilferPointers();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public void dispose() {
        this.mChannel.dispose();
        try {
            this.mHost.dispose();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mName);
        out.writeParcelable(this.mChannel, flags);
        out.writeStrongBinder(this.mHost.asBinder());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "InputMonitor{mName=" + this.mName + ", mChannel=" + this.mChannel + ", mHost=" + this.mHost + "}";
    }
}
