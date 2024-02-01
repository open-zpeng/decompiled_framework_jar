package android.media.effect.effects;

import android.app.slice.SliceItem;
import android.filterpacks.imageproc.TintFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;
/* loaded from: classes.dex */
public class TintEffect extends SingleFilterEffect {
    private protected synchronized TintEffect(EffectContext context, String name) {
        super(context, name, TintFilter.class, SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE, new Object[0]);
    }
}
