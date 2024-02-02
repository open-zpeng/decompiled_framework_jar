package android.filterfw.io;

import android.content.Context;
import android.filterfw.core.FilterGraph;
import android.filterfw.core.KeyValueMap;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
/* loaded from: classes.dex */
public abstract class GraphReader {
    public private KeyValueMap mReferences = new KeyValueMap();

    /* JADX INFO: Access modifiers changed from: private */
    public abstract synchronized FilterGraph readGraphString(String str) throws GraphIOException;

    private protected abstract synchronized KeyValueMap readKeyValueAssignments(String str) throws GraphIOException;

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized FilterGraph readGraphResource(Context context, int resourceId) throws GraphIOException {
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        InputStreamReader reader = new InputStreamReader(inputStream);
        StringWriter writer = new StringWriter();
        char[] buffer = new char[1024];
        while (true) {
            try {
                int bytesRead = reader.read(buffer, 0, 1024);
                if (bytesRead > 0) {
                    writer.write(buffer, 0, bytesRead);
                } else {
                    return readGraphString(writer.toString());
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not read specified resource file!");
            }
        }
    }

    private protected synchronized void addReference(String name, Object object) {
        this.mReferences.put(name, object);
    }

    private protected synchronized void addReferencesByMap(KeyValueMap refs) {
        this.mReferences.putAll(refs);
    }

    public void addReferencesByKeysAndValues(Object... references) {
        this.mReferences.setKeyValues(references);
    }
}
