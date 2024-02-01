package android.service.autofill;

import android.app.assist.AssistStructure;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;

/* loaded from: classes2.dex */
public final class FillContext implements Parcelable {
    public static final Parcelable.Creator<FillContext> CREATOR = new Parcelable.Creator<FillContext>() { // from class: android.service.autofill.FillContext.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FillContext createFromParcel(Parcel parcel) {
            return new FillContext(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FillContext[] newArray(int size) {
            return new FillContext[size];
        }
    };
    private final AutofillId mFocusedId;
    private final int mRequestId;
    private final AssistStructure mStructure;
    private ArrayMap<AutofillId, AssistStructure.ViewNode> mViewNodeLookupTable;

    public FillContext(int requestId, AssistStructure structure, AutofillId autofillId) {
        this.mRequestId = requestId;
        this.mStructure = structure;
        this.mFocusedId = autofillId;
    }

    private FillContext(Parcel parcel) {
        this(parcel.readInt(), (AssistStructure) parcel.readParcelable(null), (AutofillId) parcel.readParcelable(null));
    }

    public int getRequestId() {
        return this.mRequestId;
    }

    public AssistStructure getStructure() {
        return this.mStructure;
    }

    public AutofillId getFocusedId() {
        return this.mFocusedId;
    }

    public String toString() {
        if (Helper.sDebug) {
            return "FillContext [reqId=" + this.mRequestId + ", focusedId=" + this.mFocusedId + "]";
        }
        return super.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.mRequestId);
        parcel.writeParcelable(this.mStructure, flags);
        parcel.writeParcelable(this.mFocusedId, flags);
    }

    /* JADX WARN: Incorrect condition in loop: B:18:0x0051 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public android.app.assist.AssistStructure.ViewNode[] findViewNodesByAutofillIds(android.view.autofill.AutofillId[] r11) {
        /*
            r10 = this;
            java.util.LinkedList r0 = new java.util.LinkedList
            r0.<init>()
            int r1 = r11.length
            android.app.assist.AssistStructure$ViewNode[] r1 = new android.app.assist.AssistStructure.ViewNode[r1]
            android.util.SparseIntArray r2 = new android.util.SparseIntArray
            int r3 = r11.length
            r2.<init>(r3)
            r3 = 0
        Lf:
            int r4 = r11.length
            if (r3 >= r4) goto L34
            android.util.ArrayMap<android.view.autofill.AutofillId, android.app.assist.AssistStructure$ViewNode> r4 = r10.mViewNodeLookupTable
            r5 = 0
            if (r4 == 0) goto L2e
            r6 = r11[r3]
            int r4 = r4.indexOfKey(r6)
            if (r4 < 0) goto L2a
            android.util.ArrayMap<android.view.autofill.AutofillId, android.app.assist.AssistStructure$ViewNode> r5 = r10.mViewNodeLookupTable
            java.lang.Object r5 = r5.valueAt(r4)
            android.app.assist.AssistStructure$ViewNode r5 = (android.app.assist.AssistStructure.ViewNode) r5
            r1[r3] = r5
            goto L2d
        L2a:
            r2.put(r3, r5)
        L2d:
            goto L31
        L2e:
            r2.put(r3, r5)
        L31:
            int r3 = r3 + 1
            goto Lf
        L34:
            android.app.assist.AssistStructure r3 = r10.mStructure
            int r3 = r3.getWindowNodeCount()
            r4 = 0
        L3b:
            if (r4 >= r3) goto L4d
            android.app.assist.AssistStructure r5 = r10.mStructure
            android.app.assist.AssistStructure$WindowNode r5 = r5.getWindowNodeAt(r4)
            android.app.assist.AssistStructure$ViewNode r5 = r5.getRootViewNode()
            r0.add(r5)
            int r4 = r4 + 1
            goto L3b
        L4d:
            int r4 = r2.size()
            if (r4 <= 0) goto La2
            boolean r4 = r0.isEmpty()
            if (r4 != 0) goto La2
            java.lang.Object r4 = r0.removeFirst()
            android.app.assist.AssistStructure$ViewNode r4 = (android.app.assist.AssistStructure.ViewNode) r4
            r5 = 0
        L60:
            int r6 = r2.size()
            if (r5 >= r6) goto L90
            int r6 = r2.keyAt(r5)
            r7 = r11[r6]
            android.view.autofill.AutofillId r8 = r4.getAutofillId()
            boolean r8 = r7.equals(r8)
            if (r8 == 0) goto L8d
            r1[r6] = r4
            android.util.ArrayMap<android.view.autofill.AutofillId, android.app.assist.AssistStructure$ViewNode> r8 = r10.mViewNodeLookupTable
            if (r8 != 0) goto L84
            android.util.ArrayMap r8 = new android.util.ArrayMap
            int r9 = r11.length
            r8.<init>(r9)
            r10.mViewNodeLookupTable = r8
        L84:
            android.util.ArrayMap<android.view.autofill.AutofillId, android.app.assist.AssistStructure$ViewNode> r8 = r10.mViewNodeLookupTable
            r8.put(r7, r4)
            r2.removeAt(r5)
            goto L90
        L8d:
            int r5 = r5 + 1
            goto L60
        L90:
            r5 = 0
        L91:
            int r6 = r4.getChildCount()
            if (r5 >= r6) goto La1
            android.app.assist.AssistStructure$ViewNode r6 = r4.getChildAt(r5)
            r0.addLast(r6)
            int r5 = r5 + 1
            goto L91
        La1:
            goto L4d
        La2:
            r4 = 0
        La3:
            int r5 = r2.size()
            if (r4 >= r5) goto Lc7
            android.util.ArrayMap<android.view.autofill.AutofillId, android.app.assist.AssistStructure$ViewNode> r5 = r10.mViewNodeLookupTable
            if (r5 != 0) goto Lb8
            android.util.ArrayMap r5 = new android.util.ArrayMap
            int r6 = r2.size()
            r5.<init>(r6)
            r10.mViewNodeLookupTable = r5
        Lb8:
            android.util.ArrayMap<android.view.autofill.AutofillId, android.app.assist.AssistStructure$ViewNode> r5 = r10.mViewNodeLookupTable
            int r6 = r2.keyAt(r4)
            r6 = r11[r6]
            r7 = 0
            r5.put(r6, r7)
            int r4 = r4 + 1
            goto La3
        Lc7:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.service.autofill.FillContext.findViewNodesByAutofillIds(android.view.autofill.AutofillId[]):android.app.assist.AssistStructure$ViewNode[]");
    }
}
