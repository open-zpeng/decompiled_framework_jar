package android.media.effect.effects;

import android.app.slice.SliceItem;
import android.filterpacks.imageproc.RotateFilter;
import android.media.effect.EffectContext;
import android.media.effect.SizeChangeEffect;
/* loaded from: classes.dex */
public class RotateEffect extends SizeChangeEffect {
    private protected synchronized RotateEffect(EffectContext context, String name) {
        super(context, name, RotateFilter.class, SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE, new Object[0]);
    }
}
