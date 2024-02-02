package com.android.ims.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes3.dex */
public interface IImsRcsFeature extends IInterface {

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IImsRcsFeature {
        private static final String DESCRIPTOR = "com.android.ims.internal.IImsRcsFeature";

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IImsRcsFeature asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IImsRcsFeature)) {
                return (IImsRcsFeature) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            return super.onTransact(code, data, reply, flags);
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements IImsRcsFeature {
            private IBinder mRemote;

            synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }
        }
    }
}
