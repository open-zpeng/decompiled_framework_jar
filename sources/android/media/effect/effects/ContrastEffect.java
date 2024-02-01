package android.media.effect.effects;

import android.app.slice.SliceItem;
import android.filterpacks.imageproc.ContrastFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;
/* loaded from: classes.dex */
public class ContrastEffect extends SingleFilterEffect {
    private protected synchronized ContrastEffect(EffectContext context, String name) {
        super(context, name, ContrastFilter.class, SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE, new Object[0]);
    }
}
