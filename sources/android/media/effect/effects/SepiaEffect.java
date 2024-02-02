package android.media.effect.effects;

import android.app.slice.SliceItem;
import android.filterpacks.imageproc.SepiaFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;
/* loaded from: classes.dex */
public class SepiaEffect extends SingleFilterEffect {
    private protected synchronized SepiaEffect(EffectContext context, String name) {
        super(context, name, SepiaFilter.class, SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE, new Object[0]);
    }
}
