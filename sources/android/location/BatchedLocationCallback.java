package android.location;

import android.annotation.SystemApi;
import java.util.List;

@SystemApi
/* loaded from: classes.dex */
public abstract class BatchedLocationCallback {
    public void onLocationBatch(List<Location> locations) {
    }
}
