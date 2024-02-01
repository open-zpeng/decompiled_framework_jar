package com.android.internal.view;

import android.graphics.Rect;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.MergedConfiguration;
import android.view.DisplayCutout;
import android.view.DragEvent;
import android.view.IWindow;
import android.view.IWindowSession;
import com.android.internal.os.IResultReceiver;
/* loaded from: classes3.dex */
public class BaseIWindow extends IWindow.Stub {
    public int mSeq;
    private IWindowSession mSession;

    public synchronized void setSession(IWindowSession session) {
        this.mSession = session;
    }

    public synchronized void resized(Rect frame, Rect overscanInsets, Rect contentInsets, Rect visibleInsets, Rect stableInsets, Rect outsets, boolean reportDraw, MergedConfiguration mergedConfiguration, Rect backDropFrame, boolean forceLayout, boolean alwaysConsumeNavBar, int displayId, DisplayCutout.ParcelableWrapper displayCutout) {
        if (reportDraw) {
            try {
                this.mSession.finishDrawing(this);
            } catch (RemoteException e) {
            }
        }
    }

    public synchronized void moved(int newX, int newY) {
    }

    public synchronized void dispatchAppVisibility(boolean visible) {
    }

    public synchronized void dispatchGetNewSurface() {
    }

    public synchronized void windowFocusChanged(boolean hasFocus, boolean touchEnabled) {
    }

    @Override // android.view.IWindow
    public synchronized void executeCommand(String command, String parameters, ParcelFileDescriptor out) {
    }

    public synchronized void closeSystemDialogs(String reason) {
    }

    public synchronized void dispatchWallpaperOffsets(float x, float y, float xStep, float yStep, boolean sync) {
        if (sync) {
            try {
                this.mSession.wallpaperOffsetsComplete(asBinder());
            } catch (RemoteException e) {
            }
        }
    }

    @Override // android.view.IWindow
    public synchronized void dispatchDragEvent(DragEvent event) {
        if (event.getAction() == 3) {
            try {
                this.mSession.reportDropResult(this, false);
            } catch (RemoteException e) {
            }
        }
    }

    @Override // android.view.IWindow
    public synchronized void updatePointerIcon(float x, float y) {
        InputManager.getInstance().setPointerIconType(1);
    }

    @Override // android.view.IWindow
    public synchronized void dispatchSystemUiVisibilityChanged(int seq, int globalUi, int localValue, int localChanges) {
        this.mSeq = seq;
    }

    public synchronized void dispatchWallpaperCommand(String action, int x, int y, int z, Bundle extras, boolean sync) {
        if (sync) {
            try {
                this.mSession.wallpaperCommandComplete(asBinder(), null);
            } catch (RemoteException e) {
            }
        }
    }

    @Override // android.view.IWindow
    public synchronized void dispatchWindowShown() {
    }

    @Override // android.view.IWindow
    public synchronized void requestAppKeyboardShortcuts(IResultReceiver receiver, int deviceId) {
    }

    @Override // android.view.IWindow
    public synchronized void dispatchPointerCaptureChanged(boolean hasCapture) {
    }
}
