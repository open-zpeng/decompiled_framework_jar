package android.content;

import android.net.Uri;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class Entity {
    public protected final ArrayList<NamedContentValues> mSubValues = new ArrayList<>();
    public protected final ContentValues mValues;

    public Entity(ContentValues values) {
        this.mValues = values;
    }

    public ContentValues getEntityValues() {
        return this.mValues;
    }

    public ArrayList<NamedContentValues> getSubValues() {
        return this.mSubValues;
    }

    public void addSubValue(Uri uri, ContentValues values) {
        this.mSubValues.add(new NamedContentValues(uri, values));
    }

    /* loaded from: classes.dex */
    public static class NamedContentValues {
        public final Uri uri;
        public final ContentValues values;

        public NamedContentValues(Uri uri, ContentValues values) {
            this.uri = uri;
            this.values = values;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Entity: ");
        sb.append(getEntityValues());
        Iterator<NamedContentValues> it = getSubValues().iterator();
        while (it.hasNext()) {
            NamedContentValues namedValue = it.next();
            sb.append("\n  ");
            sb.append(namedValue.uri);
            sb.append("\n  -> ");
            sb.append(namedValue.values);
        }
        return sb.toString();
    }
}
