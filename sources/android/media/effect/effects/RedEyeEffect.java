package android.media.effect.effects;

import android.app.slice.SliceItem;
import android.filterpacks.imageproc.RedEyeFilter;
import android.media.effect.EffectContext;
import android.media.effect.SingleFilterEffect;
/* loaded from: classes.dex */
public class RedEyeEffect extends SingleFilterEffect {
    private protected synchronized RedEyeEffect(EffectContext context, String name) {
        super(context, name, RedEyeFilter.class, SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE, new Object[0]);
    }
}
