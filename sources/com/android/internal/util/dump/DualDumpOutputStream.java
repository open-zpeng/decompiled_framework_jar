package com.android.internal.util.dump;

import android.util.Log;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.IndentingPrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
/* loaded from: classes3.dex */
public class DualDumpOutputStream {
    public protected static final String LOG_TAG = DualDumpOutputStream.class.getSimpleName();
    public protected final LinkedList<DumpObject> mDumpObjects;
    public protected final IndentingPrintWriter mIpw;
    public protected final ProtoOutputStream mProtoStream;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static abstract class Dumpable {
        public private protected final String name;

        public private protected abstract synchronized void print(IndentingPrintWriter indentingPrintWriter, boolean z);

        public protected synchronized Dumpable(String name) {
            this.name = name;
        }
    }

    /* loaded from: classes3.dex */
    private static class DumpObject extends Dumpable {
        public protected final LinkedHashMap<String, ArrayList<Dumpable>> mSubObjects;

        public protected synchronized DumpObject(String name) {
            super(name);
            this.mSubObjects = new LinkedHashMap<>();
        }

        public private protected synchronized void print(IndentingPrintWriter ipw, boolean printName) {
            if (printName) {
                ipw.println(this.name + "={");
            } else {
                ipw.println("{");
            }
            ipw.increaseIndent();
            for (ArrayList<Dumpable> subObject : this.mSubObjects.values()) {
                int numDumpables = subObject.size();
                if (numDumpables == 1) {
                    subObject.get(0).print(ipw, true);
                } else {
                    ipw.println(subObject.get(0).name + "=[");
                    ipw.increaseIndent();
                    for (int i = 0; i < numDumpables; i++) {
                        subObject.get(i).print(ipw, false);
                    }
                    ipw.decreaseIndent();
                    ipw.println("]");
                }
            }
            ipw.decreaseIndent();
            ipw.println("}");
        }

        private protected synchronized void add(String fieldName, Dumpable d) {
            ArrayList<Dumpable> l = this.mSubObjects.get(fieldName);
            if (l == null) {
                l = new ArrayList<>(1);
                this.mSubObjects.put(fieldName, l);
            }
            l.add(d);
        }
    }

    /* loaded from: classes3.dex */
    private static class DumpField extends Dumpable {
        public protected final String mValue;

        public protected synchronized DumpField(String name, String value) {
            super(name);
            this.mValue = value;
        }

        public private protected synchronized void print(IndentingPrintWriter ipw, boolean printName) {
            if (printName) {
                ipw.println(this.name + "=" + this.mValue);
                return;
            }
            ipw.println(this.mValue);
        }
    }

    private protected synchronized DualDumpOutputStream(ProtoOutputStream proto) {
        this.mDumpObjects = new LinkedList<>();
        this.mProtoStream = proto;
        this.mIpw = null;
    }

    private protected synchronized DualDumpOutputStream(IndentingPrintWriter ipw) {
        this.mDumpObjects = new LinkedList<>();
        this.mProtoStream = null;
        this.mIpw = ipw;
        this.mDumpObjects.add(new DumpObject(null));
    }

    private protected synchronized void write(String fieldName, long fieldId, double val) {
        if (this.mProtoStream != null) {
            this.mProtoStream.write(fieldId, val);
        } else {
            this.mDumpObjects.getLast().add(fieldName, new DumpField(fieldName, String.valueOf(val)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void write(String fieldName, long fieldId, boolean val) {
        if (this.mProtoStream != null) {
            this.mProtoStream.write(fieldId, val);
        } else {
            this.mDumpObjects.getLast().add(fieldName, new DumpField(fieldName, String.valueOf(val)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void write(String fieldName, long fieldId, int val) {
        if (this.mProtoStream != null) {
            this.mProtoStream.write(fieldId, val);
        } else {
            this.mDumpObjects.getLast().add(fieldName, new DumpField(fieldName, String.valueOf(val)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void write(String fieldName, long fieldId, float val) {
        if (this.mProtoStream != null) {
            this.mProtoStream.write(fieldId, val);
        } else {
            this.mDumpObjects.getLast().add(fieldName, new DumpField(fieldName, String.valueOf(val)));
        }
    }

    private protected synchronized void write(String fieldName, long fieldId, byte[] val) {
        if (this.mProtoStream != null) {
            this.mProtoStream.write(fieldId, val);
        } else {
            this.mDumpObjects.getLast().add(fieldName, new DumpField(fieldName, Arrays.toString(val)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void write(String fieldName, long fieldId, long val) {
        if (this.mProtoStream != null) {
            this.mProtoStream.write(fieldId, val);
        } else {
            this.mDumpObjects.getLast().add(fieldName, new DumpField(fieldName, String.valueOf(val)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void write(String fieldName, long fieldId, String val) {
        if (this.mProtoStream != null) {
            this.mProtoStream.write(fieldId, val);
        } else {
            this.mDumpObjects.getLast().add(fieldName, new DumpField(fieldName, String.valueOf(val)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized long start(String fieldName, long fieldId) {
        if (this.mProtoStream != null) {
            return this.mProtoStream.start(fieldId);
        }
        DumpObject d = new DumpObject(fieldName);
        this.mDumpObjects.getLast().add(fieldName, d);
        this.mDumpObjects.addLast(d);
        return System.identityHashCode(d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void end(long token) {
        if (this.mProtoStream != null) {
            this.mProtoStream.end(token);
            return;
        }
        if (System.identityHashCode(this.mDumpObjects.getLast()) != token) {
            String str = LOG_TAG;
            Log.w(str, "Unexpected token for ending " + this.mDumpObjects.getLast().name + " at " + Arrays.toString(Thread.currentThread().getStackTrace()));
        }
        this.mDumpObjects.removeLast();
    }

    private protected synchronized void flush() {
        if (this.mProtoStream != null) {
            this.mProtoStream.flush();
            return;
        }
        if (this.mDumpObjects.size() == 1) {
            this.mDumpObjects.getFirst().print(this.mIpw, false);
            this.mDumpObjects.clear();
            this.mDumpObjects.add(new DumpObject(null));
        }
        this.mIpw.flush();
    }

    private protected synchronized void writeNested(String fieldName, byte[] nestedState) {
        if (this.mIpw == null) {
            Log.w(LOG_TAG, "writeNested does not work for proto logging");
        } else {
            this.mDumpObjects.getLast().add(fieldName, new DumpField(fieldName, new String(nestedState, StandardCharsets.UTF_8).trim()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean isProto() {
        return this.mProtoStream != null;
    }
}
