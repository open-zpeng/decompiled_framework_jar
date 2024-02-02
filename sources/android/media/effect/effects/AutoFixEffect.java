package android.media.effect.effects;

import android.app.slice.SliceItem;
import android.filterpacks.imageproc.AutoFixFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;
/* loaded from: classes.dex */
public class AutoFixEffect extends SingleFilterEffect {
    private protected synchronized AutoFixEffect(EffectContext context, String name) {
        super(context, name, AutoFixFilter.class, SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE, new Object[0]);
    }
}
