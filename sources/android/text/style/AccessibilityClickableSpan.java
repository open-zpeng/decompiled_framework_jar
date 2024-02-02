package android.text.style;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.ParcelableSpan;
import android.text.Spanned;
import android.view.View;
import android.view.accessibility.AccessibilityInteractionClient;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.R;
/* loaded from: classes2.dex */
public class AccessibilityClickableSpan extends ClickableSpan implements ParcelableSpan {
    public static final Parcelable.Creator<AccessibilityClickableSpan> CREATOR = new Parcelable.Creator<AccessibilityClickableSpan>() { // from class: android.text.style.AccessibilityClickableSpan.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AccessibilityClickableSpan createFromParcel(Parcel parcel) {
            return new AccessibilityClickableSpan(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AccessibilityClickableSpan[] newArray(int size) {
            return new AccessibilityClickableSpan[size];
        }
    };
    private final int mOriginalClickableSpanId;
    private int mWindowId = -1;
    private long mSourceNodeId = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
    private int mConnectionId = -1;

    public synchronized AccessibilityClickableSpan(int originalClickableSpanId) {
        this.mOriginalClickableSpanId = originalClickableSpanId;
    }

    public synchronized AccessibilityClickableSpan(Parcel p) {
        this.mOriginalClickableSpanId = p.readInt();
    }

    @Override // android.text.ParcelableSpan
    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    @Override // android.text.ParcelableSpan
    public synchronized int getSpanTypeIdInternal() {
        return 25;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    @Override // android.text.ParcelableSpan
    public synchronized void writeToParcelInternal(Parcel dest, int flags) {
        dest.writeInt(this.mOriginalClickableSpanId);
    }

    public synchronized ClickableSpan findClickableSpan(CharSequence text) {
        if (!(text instanceof Spanned)) {
            return null;
        }
        Spanned sp = (Spanned) text;
        int i = 0;
        ClickableSpan[] os = (ClickableSpan[]) sp.getSpans(0, text.length(), ClickableSpan.class);
        while (true) {
            int i2 = i;
            int i3 = os.length;
            if (i2 >= i3) {
                return null;
            }
            if (os[i2].getId() != this.mOriginalClickableSpanId) {
                i = i2 + 1;
            } else {
                return os[i2];
            }
        }
    }

    public synchronized void copyConnectionDataFrom(AccessibilityNodeInfo accessibilityNodeInfo) {
        this.mConnectionId = accessibilityNodeInfo.getConnectionId();
        this.mWindowId = accessibilityNodeInfo.getWindowId();
        this.mSourceNodeId = accessibilityNodeInfo.getSourceNodeId();
    }

    @Override // android.text.style.ClickableSpan
    public void onClick(View unused) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(AccessibilityNodeInfo.ACTION_ARGUMENT_ACCESSIBLE_CLICKABLE_SPAN, this);
        if (this.mWindowId == -1 || this.mSourceNodeId == AccessibilityNodeInfo.UNDEFINED_NODE_ID || this.mConnectionId == -1) {
            throw new RuntimeException("ClickableSpan for accessibility service not properly initialized");
        }
        AccessibilityInteractionClient client = AccessibilityInteractionClient.getInstance();
        client.performAccessibilityAction(this.mConnectionId, this.mWindowId, this.mSourceNodeId, R.id.accessibilityActionClickOnClickableSpan, arguments);
    }
}
