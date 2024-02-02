package android.media.effect.effects;

import android.app.slice.SliceItem;
import android.filterpacks.imageproc.StraightenFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;
/* loaded from: classes.dex */
public class StraightenEffect extends SingleFilterEffect {
    private protected synchronized StraightenEffect(EffectContext context, String name) {
        super(context, name, StraightenFilter.class, SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE, new Object[0]);
    }
}
