package android.location;

import android.os.Bundle;
/* loaded from: classes.dex */
public interface LocationListener {
    void onLocationChanged(Location location);

    void onProviderDisabled(String str);

    void onProviderEnabled(String str);

    void onStatusChanged(String str, int i, Bundle bundle);
}
