package android.media.effect.effects;

import android.app.slice.SliceItem;
import android.filterpacks.imageproc.CrossProcessFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;
/* loaded from: classes.dex */
public class CrossProcessEffect extends SingleFilterEffect {
    private protected synchronized CrossProcessEffect(EffectContext context, String name) {
        super(context, name, CrossProcessFilter.class, SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE, new Object[0]);
    }
}
