package android.view.accessibility;

import android.os.Parcelable;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class AccessibilityRecord {
    protected static final boolean DEBUG_CONCISE_TOSTRING = false;
    private static final int GET_SOURCE_PREFETCH_FLAGS = 7;
    private static final int MAX_POOL_SIZE = 10;
    private static final int PROPERTY_CHECKED = 1;
    private static final int PROPERTY_ENABLED = 2;
    private static final int PROPERTY_FULL_SCREEN = 128;
    private static final int PROPERTY_IMPORTANT_FOR_ACCESSIBILITY = 512;
    private static final int PROPERTY_PASSWORD = 4;
    private static final int PROPERTY_SCROLLABLE = 256;
    private static final int UNDEFINED = -1;
    private static AccessibilityRecord sPool;
    private static final Object sPoolLock = new Object();
    private static int sPoolSize;
    CharSequence mBeforeText;
    CharSequence mClassName;
    CharSequence mContentDescription;
    private boolean mIsInPool;
    private AccessibilityRecord mNext;
    Parcelable mParcelableData;
    public private protected boolean mSealed;
    int mBooleanProperties = 0;
    int mCurrentItemIndex = -1;
    int mItemCount = -1;
    int mFromIndex = -1;
    int mToIndex = -1;
    int mScrollX = -1;
    int mScrollY = -1;
    int mScrollDeltaX = -1;
    int mScrollDeltaY = -1;
    int mMaxScrollX = -1;
    int mMaxScrollY = -1;
    int mAddedCount = -1;
    int mRemovedCount = -1;
    public private protected long mSourceNodeId = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
    int mSourceWindowId = -1;
    final List<CharSequence> mText = new ArrayList();
    int mConnectionId = -1;

    public void setSource(View source) {
        setSource(source, -1);
    }

    public void setSource(View root, int virtualDescendantId) {
        enforceNotSealed();
        boolean important = true;
        int rootViewId = Integer.MAX_VALUE;
        this.mSourceWindowId = -1;
        if (root != null) {
            important = root.isImportantForAccessibility();
            rootViewId = root.getAccessibilityViewId();
            this.mSourceWindowId = root.getAccessibilityWindowId();
        }
        setBooleanProperty(512, important);
        this.mSourceNodeId = AccessibilityNodeInfo.makeNodeId(rootViewId, virtualDescendantId);
    }

    public synchronized void setSourceNodeId(long sourceNodeId) {
        this.mSourceNodeId = sourceNodeId;
    }

    public AccessibilityNodeInfo getSource() {
        enforceSealed();
        if (this.mConnectionId == -1 || this.mSourceWindowId == -1 || AccessibilityNodeInfo.getAccessibilityViewId(this.mSourceNodeId) == Integer.MAX_VALUE) {
            return null;
        }
        AccessibilityInteractionClient client = AccessibilityInteractionClient.getInstance();
        return client.findAccessibilityNodeInfoByAccessibilityId(this.mConnectionId, this.mSourceWindowId, this.mSourceNodeId, false, 7, null);
    }

    public synchronized void setWindowId(int windowId) {
        this.mSourceWindowId = windowId;
    }

    public int getWindowId() {
        return this.mSourceWindowId;
    }

    public boolean isChecked() {
        return getBooleanProperty(1);
    }

    public void setChecked(boolean isChecked) {
        enforceNotSealed();
        setBooleanProperty(1, isChecked);
    }

    public boolean isEnabled() {
        return getBooleanProperty(2);
    }

    public void setEnabled(boolean isEnabled) {
        enforceNotSealed();
        setBooleanProperty(2, isEnabled);
    }

    public boolean isPassword() {
        return getBooleanProperty(4);
    }

    public void setPassword(boolean isPassword) {
        enforceNotSealed();
        setBooleanProperty(4, isPassword);
    }

    public boolean isFullScreen() {
        return getBooleanProperty(128);
    }

    public void setFullScreen(boolean isFullScreen) {
        enforceNotSealed();
        setBooleanProperty(128, isFullScreen);
    }

    public boolean isScrollable() {
        return getBooleanProperty(256);
    }

    public void setScrollable(boolean scrollable) {
        enforceNotSealed();
        setBooleanProperty(256, scrollable);
    }

    public synchronized boolean isImportantForAccessibility() {
        return getBooleanProperty(512);
    }

    public synchronized void setImportantForAccessibility(boolean importantForAccessibility) {
        enforceNotSealed();
        setBooleanProperty(512, importantForAccessibility);
    }

    public int getItemCount() {
        return this.mItemCount;
    }

    public void setItemCount(int itemCount) {
        enforceNotSealed();
        this.mItemCount = itemCount;
    }

    public int getCurrentItemIndex() {
        return this.mCurrentItemIndex;
    }

    public void setCurrentItemIndex(int currentItemIndex) {
        enforceNotSealed();
        this.mCurrentItemIndex = currentItemIndex;
    }

    public int getFromIndex() {
        return this.mFromIndex;
    }

    public void setFromIndex(int fromIndex) {
        enforceNotSealed();
        this.mFromIndex = fromIndex;
    }

    public int getToIndex() {
        return this.mToIndex;
    }

    public void setToIndex(int toIndex) {
        enforceNotSealed();
        this.mToIndex = toIndex;
    }

    public int getScrollX() {
        return this.mScrollX;
    }

    public void setScrollX(int scrollX) {
        enforceNotSealed();
        this.mScrollX = scrollX;
    }

    public int getScrollY() {
        return this.mScrollY;
    }

    public void setScrollY(int scrollY) {
        enforceNotSealed();
        this.mScrollY = scrollY;
    }

    public int getScrollDeltaX() {
        return this.mScrollDeltaX;
    }

    public void setScrollDeltaX(int scrollDeltaX) {
        enforceNotSealed();
        this.mScrollDeltaX = scrollDeltaX;
    }

    public int getScrollDeltaY() {
        return this.mScrollDeltaY;
    }

    public void setScrollDeltaY(int scrollDeltaY) {
        enforceNotSealed();
        this.mScrollDeltaY = scrollDeltaY;
    }

    public int getMaxScrollX() {
        return this.mMaxScrollX;
    }

    public void setMaxScrollX(int maxScrollX) {
        enforceNotSealed();
        this.mMaxScrollX = maxScrollX;
    }

    public int getMaxScrollY() {
        return this.mMaxScrollY;
    }

    public void setMaxScrollY(int maxScrollY) {
        enforceNotSealed();
        this.mMaxScrollY = maxScrollY;
    }

    public int getAddedCount() {
        return this.mAddedCount;
    }

    public void setAddedCount(int addedCount) {
        enforceNotSealed();
        this.mAddedCount = addedCount;
    }

    public int getRemovedCount() {
        return this.mRemovedCount;
    }

    public void setRemovedCount(int removedCount) {
        enforceNotSealed();
        this.mRemovedCount = removedCount;
    }

    public CharSequence getClassName() {
        return this.mClassName;
    }

    public void setClassName(CharSequence className) {
        enforceNotSealed();
        this.mClassName = className;
    }

    public List<CharSequence> getText() {
        return this.mText;
    }

    public CharSequence getBeforeText() {
        return this.mBeforeText;
    }

    public void setBeforeText(CharSequence beforeText) {
        enforceNotSealed();
        this.mBeforeText = beforeText == null ? null : beforeText.subSequence(0, beforeText.length());
    }

    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }

    public void setContentDescription(CharSequence contentDescription) {
        enforceNotSealed();
        this.mContentDescription = contentDescription == null ? null : contentDescription.subSequence(0, contentDescription.length());
    }

    public Parcelable getParcelableData() {
        return this.mParcelableData;
    }

    public void setParcelableData(Parcelable parcelableData) {
        enforceNotSealed();
        this.mParcelableData = parcelableData;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getSourceNodeId() {
        return this.mSourceNodeId;
    }

    public synchronized void setConnectionId(int connectionId) {
        enforceNotSealed();
        this.mConnectionId = connectionId;
    }

    public synchronized void setSealed(boolean sealed) {
        this.mSealed = sealed;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isSealed() {
        return this.mSealed;
    }

    synchronized void enforceSealed() {
        if (!isSealed()) {
            throw new IllegalStateException("Cannot perform this action on a not sealed instance.");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void enforceNotSealed() {
        if (isSealed()) {
            throw new IllegalStateException("Cannot perform this action on a sealed instance.");
        }
    }

    private synchronized boolean getBooleanProperty(int property) {
        return (this.mBooleanProperties & property) == property;
    }

    private synchronized void setBooleanProperty(int property, boolean value) {
        if (value) {
            this.mBooleanProperties |= property;
        } else {
            this.mBooleanProperties &= ~property;
        }
    }

    public static AccessibilityRecord obtain(AccessibilityRecord record) {
        AccessibilityRecord clone = obtain();
        clone.init(record);
        return clone;
    }

    public static AccessibilityRecord obtain() {
        synchronized (sPoolLock) {
            if (sPool != null) {
                AccessibilityRecord record = sPool;
                sPool = sPool.mNext;
                sPoolSize--;
                record.mNext = null;
                record.mIsInPool = false;
                return record;
            }
            return new AccessibilityRecord();
        }
    }

    public void recycle() {
        if (this.mIsInPool) {
            throw new IllegalStateException("Record already recycled!");
        }
        clear();
        synchronized (sPoolLock) {
            if (sPoolSize <= 10) {
                this.mNext = sPool;
                sPool = this;
                this.mIsInPool = true;
                sPoolSize++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void init(AccessibilityRecord record) {
        this.mSealed = record.mSealed;
        this.mBooleanProperties = record.mBooleanProperties;
        this.mCurrentItemIndex = record.mCurrentItemIndex;
        this.mItemCount = record.mItemCount;
        this.mFromIndex = record.mFromIndex;
        this.mToIndex = record.mToIndex;
        this.mScrollX = record.mScrollX;
        this.mScrollY = record.mScrollY;
        this.mMaxScrollX = record.mMaxScrollX;
        this.mMaxScrollY = record.mMaxScrollY;
        this.mAddedCount = record.mAddedCount;
        this.mRemovedCount = record.mRemovedCount;
        this.mClassName = record.mClassName;
        this.mContentDescription = record.mContentDescription;
        this.mBeforeText = record.mBeforeText;
        this.mParcelableData = record.mParcelableData;
        this.mText.addAll(record.mText);
        this.mSourceWindowId = record.mSourceWindowId;
        this.mSourceNodeId = record.mSourceNodeId;
        this.mConnectionId = record.mConnectionId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void clear() {
        this.mSealed = false;
        this.mBooleanProperties = 0;
        this.mCurrentItemIndex = -1;
        this.mItemCount = -1;
        this.mFromIndex = -1;
        this.mToIndex = -1;
        this.mScrollX = -1;
        this.mScrollY = -1;
        this.mMaxScrollX = -1;
        this.mMaxScrollY = -1;
        this.mAddedCount = -1;
        this.mRemovedCount = -1;
        this.mClassName = null;
        this.mContentDescription = null;
        this.mBeforeText = null;
        this.mParcelableData = null;
        this.mText.clear();
        this.mSourceNodeId = 2147483647L;
        this.mSourceWindowId = -1;
        this.mConnectionId = -1;
    }

    public String toString() {
        return appendTo(new StringBuilder()).toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized StringBuilder appendTo(StringBuilder builder) {
        builder.append(" [ ClassName: ");
        builder.append(this.mClassName);
        appendPropName(builder, "Text").append(this.mText);
        append(builder, "ContentDescription", this.mContentDescription);
        append(builder, "ItemCount", this.mItemCount);
        append(builder, "CurrentItemIndex", this.mCurrentItemIndex);
        appendUnless(true, 2, builder);
        appendUnless(false, 4, builder);
        appendUnless(false, 1, builder);
        appendUnless(false, 128, builder);
        appendUnless(false, 256, builder);
        append(builder, "BeforeText", this.mBeforeText);
        append(builder, "FromIndex", this.mFromIndex);
        append(builder, "ToIndex", this.mToIndex);
        append(builder, "ScrollX", this.mScrollX);
        append(builder, "ScrollY", this.mScrollY);
        append(builder, "MaxScrollX", this.mMaxScrollX);
        append(builder, "MaxScrollY", this.mMaxScrollY);
        append(builder, "AddedCount", this.mAddedCount);
        append(builder, "RemovedCount", this.mRemovedCount);
        append(builder, "ParcelableData", this.mParcelableData);
        builder.append(" ]");
        return builder;
    }

    private synchronized void appendUnless(boolean defValue, int prop, StringBuilder builder) {
        boolean value = getBooleanProperty(prop);
        appendPropName(builder, singleBooleanPropertyToString(prop)).append(value);
    }

    private static synchronized String singleBooleanPropertyToString(int prop) {
        if (prop != 4) {
            if (prop != 128) {
                if (prop != 256) {
                    if (prop != 512) {
                        switch (prop) {
                            case 1:
                                return "Checked";
                            case 2:
                                return "Enabled";
                            default:
                                return Integer.toHexString(prop);
                        }
                    }
                    return "ImportantForAccessibility";
                }
                return "Scrollable";
            }
            return "FullScreen";
        }
        return "Password";
    }

    private synchronized void append(StringBuilder builder, String propName, int propValue) {
        appendPropName(builder, propName).append(propValue);
    }

    private synchronized void append(StringBuilder builder, String propName, Object propValue) {
        appendPropName(builder, propName).append(propValue);
    }

    private synchronized StringBuilder appendPropName(StringBuilder builder, String propName) {
        builder.append("; ");
        builder.append(propName);
        builder.append(": ");
        return builder;
    }
}
