package android.net;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.android.internal.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Deprecated
/* loaded from: classes2.dex */
public class NetworkBadging {
    private protected static final int BADGING_4K = 30;
    private protected static final int BADGING_HD = 20;
    private protected static final int BADGING_NONE = 0;
    private protected static final int BADGING_SD = 10;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface Badging {
    }

    private synchronized NetworkBadging() {
    }

    private protected static Drawable getWifiIcon(int signalLevel, int badging, Resources.Theme theme) {
        return Resources.getSystem().getDrawable(getWifiSignalResource(signalLevel), theme);
    }

    private static synchronized int getWifiSignalResource(int signalLevel) {
        switch (signalLevel) {
            case 0:
                return R.drawable.ic_wifi_signal_0;
            case 1:
                return R.drawable.ic_wifi_signal_1;
            case 2:
                return R.drawable.ic_wifi_signal_2;
            case 3:
                return R.drawable.ic_wifi_signal_3;
            case 4:
                return R.drawable.ic_wifi_signal_4;
            default:
                throw new IllegalArgumentException("Invalid signal level: " + signalLevel);
        }
    }

    private static synchronized int getBadgedWifiSignalResource(int signalLevel) {
        switch (signalLevel) {
            case 0:
                return R.drawable.ic_signal_wifi_badged_0_bars;
            case 1:
                return R.drawable.ic_signal_wifi_badged_1_bar;
            case 2:
                return R.drawable.ic_signal_wifi_badged_2_bars;
            case 3:
                return R.drawable.ic_signal_wifi_badged_3_bars;
            case 4:
                return R.drawable.ic_signal_wifi_badged_4_bars;
            default:
                throw new IllegalArgumentException("Invalid signal level: " + signalLevel);
        }
    }
}
