package android.os;

import android.annotation.SystemApi;

@SystemApi
/* loaded from: classes2.dex */
public interface IHwBinder {

    /* loaded from: classes2.dex */
    public interface DeathRecipient {
        void serviceDied(long j);
    }

    boolean linkToDeath(DeathRecipient deathRecipient, long j);

    IHwInterface queryLocalInterface(String str);

    void transact(int i, HwParcel hwParcel, HwParcel hwParcel2, int i2) throws RemoteException;

    boolean unlinkToDeath(DeathRecipient deathRecipient);
}
