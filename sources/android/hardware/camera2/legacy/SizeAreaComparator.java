package android.hardware.camera2.legacy;

import android.hardware.Camera;
import com.android.internal.util.Preconditions;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes.dex */
public class SizeAreaComparator implements Comparator<Camera.Size> {
    @Override // java.util.Comparator
    public int compare(Camera.Size size, Camera.Size size2) {
        Preconditions.checkNotNull(size, "size must not be null");
        Preconditions.checkNotNull(size2, "size2 must not be null");
        if (size.equals(size2)) {
            return 0;
        }
        long width = size.width;
        long width2 = size2.width;
        long area = size.height * width;
        long area2 = size2.height * width2;
        return area == area2 ? width > width2 ? 1 : -1 : area > area2 ? 1 : -1;
    }

    public static Camera.Size findLargestByArea(List<Camera.Size> sizes) {
        Preconditions.checkNotNull(sizes, "sizes must not be null");
        return (Camera.Size) Collections.max(sizes, new SizeAreaComparator());
    }
}
