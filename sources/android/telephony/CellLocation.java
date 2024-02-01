package android.telephony;

import android.annotation.UnsupportedAppUsage;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import com.android.internal.telephony.ITelephony;

/* loaded from: classes2.dex */
public abstract class CellLocation {
    @UnsupportedAppUsage
    public abstract void fillInNotifierBundle(Bundle bundle);

    @UnsupportedAppUsage
    public abstract boolean isEmpty();

    public abstract void setStateInvalid();

    public static void requestLocationUpdate() {
        try {
            ITelephony phone = ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
            if (phone != null) {
                phone.updateServiceLocation();
            }
        } catch (RemoteException e) {
        }
    }

    @UnsupportedAppUsage
    public static CellLocation newFromBundle(Bundle bundle) {
        int currentPhoneType = TelephonyManager.getDefault().getCurrentPhoneType();
        if (currentPhoneType != 1) {
            if (currentPhoneType == 2) {
                return new CdmaCellLocation(bundle);
            }
            return null;
        }
        return new GsmCellLocation(bundle);
    }

    public static CellLocation getEmpty() {
        int currentPhoneType = TelephonyManager.getDefault().getCurrentPhoneType();
        if (currentPhoneType != 1) {
            if (currentPhoneType == 2) {
                return new CdmaCellLocation();
            }
            return null;
        }
        return new GsmCellLocation();
    }
}
