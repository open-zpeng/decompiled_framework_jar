package android.media.effect.effects;

import android.app.slice.SliceItem;
import android.filterpacks.imageproc.ColorTemperatureFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;
/* loaded from: classes.dex */
public class ColorTemperatureEffect extends SingleFilterEffect {
    private protected synchronized ColorTemperatureEffect(EffectContext context, String name) {
        super(context, name, ColorTemperatureFilter.class, SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE, new Object[0]);
    }
}
