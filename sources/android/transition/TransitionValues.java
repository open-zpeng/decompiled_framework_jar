package android.transition;

import android.util.ArrayMap;
import android.view.View;
import java.util.ArrayList;
import java.util.Map;
/* loaded from: classes2.dex */
public class TransitionValues {
    public View view;
    public final Map<String, Object> values = new ArrayMap();
    final ArrayList<Transition> targetedTransitions = new ArrayList<>();

    public boolean equals(Object other) {
        if ((other instanceof TransitionValues) && this.view == ((TransitionValues) other).view && this.values.equals(((TransitionValues) other).values)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (31 * this.view.hashCode()) + this.values.hashCode();
    }

    public String toString() {
        String returnValue = "TransitionValues@" + Integer.toHexString(hashCode()) + ":\n";
        String returnValue2 = (returnValue + "    view = " + this.view + "\n") + "    values:";
        for (String s : this.values.keySet()) {
            returnValue2 = returnValue2 + "    " + s + ": " + this.values.get(s) + "\n";
        }
        return returnValue2;
    }
}
