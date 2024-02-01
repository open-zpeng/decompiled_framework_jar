package com.android.internal.policy;

import android.content.res.Resources;
import com.android.internal.R;

/* loaded from: classes3.dex */
public class ScreenDecorationsUtils {
    public static float getWindowCornerRadius(Resources resources) {
        if (supportsRoundedCornersOnWindows(resources)) {
            float defaultRadius = resources.getDimension(R.dimen.rounded_corner_radius) - resources.getDimension(R.dimen.rounded_corner_radius_adjustment);
            float topRadius = resources.getDimension(R.dimen.rounded_corner_radius_top) - resources.getDimension(R.dimen.rounded_corner_radius_top_adjustment);
            if (topRadius == 0.0f) {
                topRadius = defaultRadius;
            }
            float bottomRadius = resources.getDimension(R.dimen.rounded_corner_radius_bottom) - resources.getDimension(R.dimen.rounded_corner_radius_bottom_adjustment);
            if (bottomRadius == 0.0f) {
                bottomRadius = defaultRadius;
            }
            return Math.min(topRadius, bottomRadius);
        }
        return 0.0f;
    }

    public static boolean supportsRoundedCornersOnWindows(Resources resources) {
        return resources.getBoolean(R.bool.config_supportsRoundedCornersOnWindows);
    }
}
