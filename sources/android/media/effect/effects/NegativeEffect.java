package android.media.effect.effects;

import android.app.slice.SliceItem;
import android.filterpacks.imageproc.NegativeFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;
/* loaded from: classes.dex */
public class NegativeEffect extends SingleFilterEffect {
    private protected synchronized NegativeEffect(EffectContext context, String name) {
        super(context, name, NegativeFilter.class, SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE, new Object[0]);
    }
}
