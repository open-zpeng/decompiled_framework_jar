package android.animation;

import java.util.List;
/* loaded from: classes.dex */
public interface Keyframes extends Cloneable {

    /* loaded from: classes.dex */
    public interface FloatKeyframes extends Keyframes {
        synchronized float getFloatValue(float f);
    }

    /* loaded from: classes.dex */
    public interface IntKeyframes extends Keyframes {
        synchronized int getIntValue(float f);
    }

    synchronized Keyframes clone();

    synchronized List<Keyframe> getKeyframes();

    synchronized Class getType();

    synchronized Object getValue(float f);

    synchronized void setEvaluator(TypeEvaluator typeEvaluator);
}
