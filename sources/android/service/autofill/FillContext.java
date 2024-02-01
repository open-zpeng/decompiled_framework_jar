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
    private final int mRequestId;
    private final AssistStructure mStructure;
    private ArrayMap<AutofillId, AssistStructure.ViewNode> mViewNodeLookupTable;

    public synchronized FillContext(int requestId, AssistStructure structure) {
        this.mRequestId = requestId;
        this.mStructure = structure;
    }

    private synchronized FillContext(Parcel parcel) {
        this(parcel.readInt(), (AssistStructure) parcel.readParcelable(null));
    }

    public int getRequestId() {
        return this.mRequestId;
    }

    public AssistStructure getStructure() {
        return this.mStructure;
    }

    public String toString() {
        if (Helper.sDebug) {
            return "FillContext [reqId=" + this.mRequestId + "]";
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
    }

    /* JADX WARN: Incorrect condition in loop: B:18:0x0053 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized android.app.assist.AssistStructure.ViewNode[] findViewNodesByAutofillIds(android.view.autofill.AutofillId[] r12) {
        /*
            r11 = this;
            java.util.LinkedList r0 = new java.util.LinkedList
            r0.<init>()
            int r1 = r12.length
            android.app.assist.AssistStructure$ViewNode[] r1 = new android.app.assist.AssistStructure.ViewNode[r1]
            android.util.SparseIntArray r2 = new android.util.SparseIntArray
            int r3 = r12.length
            r2.<init>(r3)
            r3 = 0
            r4 = r3
        L10:
            int r5 = r12.length
            if (r4 >= r5) goto L36
            android.util.ArrayMap<android.view.autofill.AutofillId, android.app.assist.AssistStructure$ViewNode> r5 = r11.mViewNodeLookupTable
            if (r5 == 0) goto L30
            android.util.ArrayMap<android.view.autofill.AutofillId, android.app.assist.AssistStructure$ViewNode> r5 = r11.mViewNodeLookupTable
            r6 = r12[r4]
            int r5 = r5.indexOfKey(r6)
            if (r5 < 0) goto L2c
            android.util.ArrayMap<android.view.autofill.AutofillId, android.app.assist.AssistStructure$ViewNode> r6 = r11.mViewNodeLookupTable
            java.lang.Object r6 = r6.valueAt(r5)
            android.app.assist.AssistStructure$ViewNode r6 = (android.app.assist.AssistStructure.ViewNode) r6
            r1[r4] = r6
            goto L2f
        L2c:
            r2.put(r4, r3)
        L2f:
            goto L33
        L30:
            r2.put(r4, r3)
        L33:
            int r4 = r4 + 1
            goto L10
        L36:
            android.app.assist.AssistStructure r4 = r11.mStructure
            int r4 = r4.getWindowNodeCount()
            r5 = r3
        L3d:
            if (r5 >= r4) goto L4f
            android.app.assist.AssistStructure r6 = r11.mStructure
            android.app.assist.AssistStructure$WindowNode r6 = r6.getWindowNodeAt(r5)
            android.app.assist.AssistStructure$ViewNode r6 = r6.getRootViewNode()
            r0.add(r6)
            int r5 = r5 + 1
            goto L3d
        L4f:
            int r5 = r2.size()
            if (r5 <= 0) goto La4
            boolean r5 = r0.isEmpty()
            if (r5 != 0) goto La4
            java.lang.Object r5 = r0.removeFirst()
            android.app.assist.AssistStructure$ViewNode r5 = (android.app.assist.AssistStructure.ViewNode) r5
            r6 = r3
        L62:
            int r7 = r2.size()
            if (r6 >= r7) goto L92
            int r7 = r2.keyAt(r6)
            r8 = r12[r7]
            android.view.autofill.AutofillId r9 = r5.getAutofillId()
            boolean r9 = r8.equals(r9)
            if (r9 == 0) goto L8f
            r1[r7] = r5
            android.util.ArrayMap<android.view.autofill.AutofillId, android.app.assist.AssistStructure$ViewNode> r9 = r11.mViewNodeLookupTable
            if (r9 != 0) goto L86
            android.util.ArrayMap r9 = new android.util.ArrayMap
            int r10 = r12.length
            r9.<init>(r10)
            r11.mViewNodeLookupTable = r9
        L86:
            android.util.ArrayMap<android.view.autofill.AutofillId, android.app.assist.AssistStructure$ViewNode> r9 = r11.mViewNodeLookupTable
            r9.put(r8, r5)
            r2.removeAt(r6)
            goto L92
        L8f:
            int r6 = r6 + 1
            goto L62
        L92:
            r6 = r3
        L93:
            int r7 = r5.getChildCount()
            if (r6 >= r7) goto La3
            android.app.assist.AssistStructure$ViewNode r7 = r5.getChildAt(r6)
            r0.addLast(r7)
            int r6 = r6 + 1
            goto L93
        La3:
            goto L4f
        La4:
        La5:
            int r5 = r2.size()
            if (r3 >= r5) goto Lc9
            android.util.ArrayMap<android.view.autofill.AutofillId, android.app.assist.AssistStructure$ViewNode> r5 = r11.mViewNodeLookupTable
            if (r5 != 0) goto Lba
            android.util.ArrayMap r5 = new android.util.ArrayMap
            int r6 = r2.size()
            r5.<init>(r6)
            r11.mViewNodeLookupTable = r5
        Lba:
            android.util.ArrayMap<android.view.autofill.AutofillId, android.app.assist.AssistStructure$ViewNode> r5 = r11.mViewNodeLookupTable
            int r6 = r2.keyAt(r3)
            r6 = r12[r6]
            r7 = 0
            r5.put(r6, r7)
            int r3 = r3 + 1
            goto La5
        Lc9:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.service.autofill.FillContext.findViewNodesByAutofillIds(android.view.autofill.AutofillId[]):android.app.assist.AssistStructure$ViewNode[]");
    }
}
