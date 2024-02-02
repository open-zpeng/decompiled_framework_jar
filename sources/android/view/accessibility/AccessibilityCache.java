package android.view.accessibility;

import android.os.Build;
import android.util.ArraySet;
import android.util.Log;
import android.util.LongArray;
import android.util.LongSparseArray;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class AccessibilityCache {
    public static final int CACHE_CRITICAL_EVENTS_MASK = 4307005;
    private static final boolean CHECK_INTEGRITY = Build.IS_ENG;
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "AccessibilityCache";
    private final AccessibilityNodeRefresher mAccessibilityNodeRefresher;
    private boolean mIsAllWindowsCached;
    private final Object mLock = new Object();
    private long mAccessibilityFocus = 2147483647L;
    private long mInputFocus = 2147483647L;
    private final SparseArray<AccessibilityWindowInfo> mWindowCache = new SparseArray<>();
    private final SparseArray<LongSparseArray<AccessibilityNodeInfo>> mNodeCache = new SparseArray<>();
    private final SparseArray<AccessibilityWindowInfo> mTempWindowArray = new SparseArray<>();

    public synchronized AccessibilityCache(AccessibilityNodeRefresher nodeRefresher) {
        this.mAccessibilityNodeRefresher = nodeRefresher;
    }

    public synchronized void setWindows(List<AccessibilityWindowInfo> windows) {
        synchronized (this.mLock) {
            clearWindowCache();
            if (windows == null) {
                return;
            }
            int windowCount = windows.size();
            for (int i = 0; i < windowCount; i++) {
                AccessibilityWindowInfo window = windows.get(i);
                addWindow(window);
            }
            this.mIsAllWindowsCached = true;
        }
    }

    public synchronized void addWindow(AccessibilityWindowInfo window) {
        synchronized (this.mLock) {
            int windowId = window.getId();
            AccessibilityWindowInfo oldWindow = this.mWindowCache.get(windowId);
            if (oldWindow != null) {
                oldWindow.recycle();
            }
            this.mWindowCache.put(windowId, AccessibilityWindowInfo.obtain(window));
        }
    }

    public synchronized void onAccessibilityEvent(AccessibilityEvent event) {
        synchronized (this.mLock) {
            int eventType = event.getEventType();
            switch (eventType) {
                case 1:
                case 4:
                case 16:
                case 8192:
                    refreshCachedNodeLocked(event.getWindowId(), event.getSourceNodeId());
                    break;
                case 8:
                    if (this.mInputFocus != 2147483647L) {
                        refreshCachedNodeLocked(event.getWindowId(), this.mInputFocus);
                    }
                    this.mInputFocus = event.getSourceNodeId();
                    refreshCachedNodeLocked(event.getWindowId(), this.mInputFocus);
                    break;
                case 32:
                case 4194304:
                    clear();
                    break;
                case 2048:
                    synchronized (this.mLock) {
                        int windowId = event.getWindowId();
                        long sourceId = event.getSourceNodeId();
                        if ((event.getContentChangeTypes() & 1) != 0) {
                            clearSubTreeLocked(windowId, sourceId);
                        } else {
                            refreshCachedNodeLocked(windowId, sourceId);
                        }
                    }
                    break;
                case 4096:
                    clearSubTreeLocked(event.getWindowId(), event.getSourceNodeId());
                    break;
                case 32768:
                    if (this.mAccessibilityFocus != 2147483647L) {
                        refreshCachedNodeLocked(event.getWindowId(), this.mAccessibilityFocus);
                    }
                    this.mAccessibilityFocus = event.getSourceNodeId();
                    refreshCachedNodeLocked(event.getWindowId(), this.mAccessibilityFocus);
                    break;
                case 65536:
                    if (this.mAccessibilityFocus == event.getSourceNodeId()) {
                        refreshCachedNodeLocked(event.getWindowId(), this.mAccessibilityFocus);
                        this.mAccessibilityFocus = 2147483647L;
                        break;
                    }
                    break;
            }
        }
        if (CHECK_INTEGRITY) {
            checkIntegrity();
        }
    }

    private synchronized void refreshCachedNodeLocked(int windowId, long sourceId) {
        AccessibilityNodeInfo cachedInfo;
        LongSparseArray<AccessibilityNodeInfo> nodes = this.mNodeCache.get(windowId);
        if (nodes == null || (cachedInfo = nodes.get(sourceId)) == null || this.mAccessibilityNodeRefresher.refreshNode(cachedInfo, true)) {
            return;
        }
        clearSubTreeLocked(windowId, sourceId);
    }

    public synchronized AccessibilityNodeInfo getNode(int windowId, long accessibilityNodeId) {
        synchronized (this.mLock) {
            LongSparseArray<AccessibilityNodeInfo> nodes = this.mNodeCache.get(windowId);
            if (nodes == null) {
                return null;
            }
            AccessibilityNodeInfo info = nodes.get(accessibilityNodeId);
            if (info != null) {
                info = AccessibilityNodeInfo.obtain(info);
            }
            return info;
        }
    }

    public synchronized List<AccessibilityWindowInfo> getWindows() {
        synchronized (this.mLock) {
            if (this.mIsAllWindowsCached) {
                int windowCount = this.mWindowCache.size();
                if (windowCount > 0) {
                    SparseArray<AccessibilityWindowInfo> sortedWindows = this.mTempWindowArray;
                    sortedWindows.clear();
                    for (int i = 0; i < windowCount; i++) {
                        AccessibilityWindowInfo window = this.mWindowCache.valueAt(i);
                        sortedWindows.put(window.getLayer(), window);
                    }
                    int sortedWindowCount = sortedWindows.size();
                    List<AccessibilityWindowInfo> windows = new ArrayList<>(sortedWindowCount);
                    for (int i2 = sortedWindowCount - 1; i2 >= 0; i2--) {
                        windows.add(AccessibilityWindowInfo.obtain(sortedWindows.valueAt(i2)));
                        sortedWindows.removeAt(i2);
                    }
                    return windows;
                }
                return null;
            }
            return null;
        }
    }

    public synchronized AccessibilityWindowInfo getWindow(int windowId) {
        synchronized (this.mLock) {
            AccessibilityWindowInfo window = this.mWindowCache.get(windowId);
            if (window != null) {
                return AccessibilityWindowInfo.obtain(window);
            }
            return null;
        }
    }

    public synchronized void add(AccessibilityNodeInfo info) {
        synchronized (this.mLock) {
            int windowId = info.getWindowId();
            LongSparseArray<AccessibilityNodeInfo> nodes = this.mNodeCache.get(windowId);
            if (nodes == null) {
                nodes = new LongSparseArray<>();
                this.mNodeCache.put(windowId, nodes);
            }
            long sourceId = info.getSourceNodeId();
            AccessibilityNodeInfo oldInfo = nodes.get(sourceId);
            if (oldInfo != null) {
                LongArray newChildrenIds = info.getChildNodeIds();
                int oldChildCount = oldInfo.getChildCount();
                for (int i = 0; i < oldChildCount; i++) {
                    if (nodes.get(sourceId) == null) {
                        clearNodesForWindowLocked(windowId);
                        return;
                    }
                    long oldChildId = oldInfo.getChildId(i);
                    if (newChildrenIds == null || newChildrenIds.indexOf(oldChildId) < 0) {
                        clearSubTreeLocked(windowId, oldChildId);
                    }
                }
                long oldParentId = oldInfo.getParentNodeId();
                if (info.getParentNodeId() != oldParentId) {
                    clearSubTreeLocked(windowId, oldParentId);
                } else {
                    oldInfo.recycle();
                }
            }
            AccessibilityNodeInfo clone = AccessibilityNodeInfo.obtain(info);
            nodes.put(sourceId, clone);
            if (clone.isAccessibilityFocused()) {
                this.mAccessibilityFocus = sourceId;
            }
            if (clone.isFocused()) {
                this.mInputFocus = sourceId;
            }
        }
    }

    public synchronized void clear() {
        synchronized (this.mLock) {
            clearWindowCache();
            int nodesForWindowCount = this.mNodeCache.size();
            for (int i = 0; i < nodesForWindowCount; i++) {
                int windowId = this.mNodeCache.keyAt(i);
                clearNodesForWindowLocked(windowId);
            }
            this.mAccessibilityFocus = 2147483647L;
            this.mInputFocus = 2147483647L;
        }
    }

    private synchronized void clearWindowCache() {
        int windowCount = this.mWindowCache.size();
        for (int i = windowCount - 1; i >= 0; i--) {
            AccessibilityWindowInfo window = this.mWindowCache.valueAt(i);
            window.recycle();
            this.mWindowCache.removeAt(i);
        }
        this.mIsAllWindowsCached = false;
    }

    private synchronized void clearNodesForWindowLocked(int windowId) {
        LongSparseArray<AccessibilityNodeInfo> nodes = this.mNodeCache.get(windowId);
        if (nodes == null) {
            return;
        }
        int nodeCount = nodes.size();
        for (int i = nodeCount - 1; i >= 0; i--) {
            AccessibilityNodeInfo info = nodes.valueAt(i);
            nodes.removeAt(i);
            info.recycle();
        }
        this.mNodeCache.remove(windowId);
    }

    private synchronized void clearSubTreeLocked(int windowId, long rootNodeId) {
        LongSparseArray<AccessibilityNodeInfo> nodes = this.mNodeCache.get(windowId);
        if (nodes != null) {
            clearSubTreeRecursiveLocked(nodes, rootNodeId);
        }
    }

    private synchronized void clearSubTreeRecursiveLocked(LongSparseArray<AccessibilityNodeInfo> nodes, long rootNodeId) {
        AccessibilityNodeInfo current = nodes.get(rootNodeId);
        if (current == null) {
            return;
        }
        nodes.remove(rootNodeId);
        int childCount = current.getChildCount();
        for (int i = 0; i < childCount; i++) {
            long childNodeId = current.getChildId(i);
            clearSubTreeRecursiveLocked(nodes, childNodeId);
        }
        current.recycle();
    }

    public synchronized void checkIntegrity() {
        AccessibilityWindowInfo focusedWindow;
        int windowCount;
        AccessibilityWindowInfo activeWindow;
        int nodesForWindowCount;
        AccessibilityWindowInfo focusedWindow2;
        int windowCount2;
        AccessibilityWindowInfo activeWindow2;
        int nodesForWindowCount2;
        int childCount;
        int nodesForWindowCount3;
        AccessibilityNodeInfo accessFocus;
        boolean childOfItsParent;
        AccessibilityCache accessibilityCache = this;
        synchronized (accessibilityCache.mLock) {
            if (accessibilityCache.mWindowCache.size() > 0 || accessibilityCache.mNodeCache.size() != 0) {
                int windowCount3 = accessibilityCache.mWindowCache.size();
                AccessibilityWindowInfo activeWindow3 = null;
                AccessibilityWindowInfo focusedWindow3 = null;
                for (int i = 0; i < windowCount3; i++) {
                    AccessibilityWindowInfo window = accessibilityCache.mWindowCache.valueAt(i);
                    if (window.isActive()) {
                        if (activeWindow3 != null) {
                            Log.e(LOG_TAG, "Duplicate active window:" + window);
                        } else {
                            activeWindow3 = window;
                        }
                    }
                    if (window.isFocused()) {
                        if (focusedWindow3 != null) {
                            Log.e(LOG_TAG, "Duplicate focused window:" + window);
                        } else {
                            focusedWindow3 = window;
                        }
                    }
                }
                int nodesForWindowCount4 = accessibilityCache.mNodeCache.size();
                AccessibilityNodeInfo inputFocus = null;
                AccessibilityNodeInfo inputFocus2 = null;
                int i2 = 0;
                while (i2 < nodesForWindowCount4) {
                    LongSparseArray<AccessibilityNodeInfo> nodes = accessibilityCache.mNodeCache.valueAt(i2);
                    if (nodes.size() <= 0) {
                        focusedWindow = focusedWindow3;
                        windowCount = windowCount3;
                        activeWindow = activeWindow3;
                        nodesForWindowCount = nodesForWindowCount4;
                    } else {
                        ArraySet<AccessibilityNodeInfo> seen = new ArraySet<>();
                        int windowId = accessibilityCache.mNodeCache.keyAt(i2);
                        int nodeCount = nodes.size();
                        AccessibilityNodeInfo inputFocus3 = inputFocus;
                        AccessibilityNodeInfo inputFocus4 = inputFocus2;
                        int j = 0;
                        while (j < nodeCount) {
                            AccessibilityNodeInfo node = nodes.valueAt(j);
                            if (!seen.add(node)) {
                                StringBuilder sb = new StringBuilder();
                                focusedWindow2 = focusedWindow3;
                                sb.append("Duplicate node: ");
                                sb.append(node);
                                sb.append(" in window:");
                                sb.append(windowId);
                                Log.e(LOG_TAG, sb.toString());
                                windowCount2 = windowCount3;
                                activeWindow2 = activeWindow3;
                                nodesForWindowCount2 = nodesForWindowCount4;
                            } else {
                                focusedWindow2 = focusedWindow3;
                                if (node.isAccessibilityFocused()) {
                                    if (inputFocus4 != null) {
                                        Log.e(LOG_TAG, "Duplicate accessibility focus:" + node + " in window:" + windowId);
                                    } else {
                                        inputFocus4 = node;
                                    }
                                }
                                if (node.isFocused()) {
                                    if (inputFocus3 != null) {
                                        Log.e(LOG_TAG, "Duplicate input focus: " + node + " in window:" + windowId);
                                    } else {
                                        inputFocus3 = node;
                                    }
                                }
                                windowCount2 = windowCount3;
                                AccessibilityNodeInfo nodeParent = nodes.get(node.getParentNodeId());
                                if (nodeParent != null) {
                                    boolean childOfItsParent2 = false;
                                    int childCount2 = nodeParent.getChildCount();
                                    int k = 0;
                                    while (true) {
                                        if (k < childCount2) {
                                            boolean childOfItsParent3 = childOfItsParent2;
                                            int childCount3 = childCount2;
                                            if (nodes.get(nodeParent.getChildId(k)) != node) {
                                                k++;
                                                childOfItsParent2 = childOfItsParent3;
                                                childCount2 = childCount3;
                                            } else {
                                                childOfItsParent = true;
                                                break;
                                            }
                                        } else {
                                            childOfItsParent = childOfItsParent2;
                                            break;
                                        }
                                    }
                                    if (!childOfItsParent) {
                                        Log.e(LOG_TAG, "Invalid parent-child relation between parent: " + nodeParent + " and child: " + node);
                                    }
                                }
                                int childCount4 = node.getChildCount();
                                int k2 = 0;
                                while (k2 < childCount4) {
                                    AccessibilityWindowInfo activeWindow4 = activeWindow3;
                                    AccessibilityNodeInfo child = nodes.get(node.getChildId(k2));
                                    if (child != null) {
                                        nodesForWindowCount3 = nodesForWindowCount4;
                                        accessFocus = inputFocus4;
                                        AccessibilityNodeInfo parent = nodes.get(child.getParentNodeId());
                                        if (parent == node) {
                                            childCount = childCount4;
                                        } else {
                                            StringBuilder sb2 = new StringBuilder();
                                            childCount = childCount4;
                                            sb2.append("Invalid child-parent relation between child: ");
                                            sb2.append(node);
                                            sb2.append(" and parent: ");
                                            sb2.append(nodeParent);
                                            Log.e(LOG_TAG, sb2.toString());
                                        }
                                    } else {
                                        childCount = childCount4;
                                        nodesForWindowCount3 = nodesForWindowCount4;
                                        accessFocus = inputFocus4;
                                    }
                                    k2++;
                                    activeWindow3 = activeWindow4;
                                    nodesForWindowCount4 = nodesForWindowCount3;
                                    inputFocus4 = accessFocus;
                                    childCount4 = childCount;
                                }
                                activeWindow2 = activeWindow3;
                                nodesForWindowCount2 = nodesForWindowCount4;
                            }
                            j++;
                            focusedWindow3 = focusedWindow2;
                            windowCount3 = windowCount2;
                            activeWindow3 = activeWindow2;
                            nodesForWindowCount4 = nodesForWindowCount2;
                        }
                        focusedWindow = focusedWindow3;
                        windowCount = windowCount3;
                        activeWindow = activeWindow3;
                        nodesForWindowCount = nodesForWindowCount4;
                        inputFocus2 = inputFocus4;
                        inputFocus = inputFocus3;
                    }
                    i2++;
                    focusedWindow3 = focusedWindow;
                    windowCount3 = windowCount;
                    activeWindow3 = activeWindow;
                    nodesForWindowCount4 = nodesForWindowCount;
                    accessibilityCache = this;
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class AccessibilityNodeRefresher {
        public synchronized boolean refreshNode(AccessibilityNodeInfo info, boolean bypassCache) {
            return info.refresh(null, bypassCache);
        }
    }
}
