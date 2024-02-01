package android.content;

import android.content.IClipboard;
import android.content.IOnPrimaryClipChangedListener;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class ClipboardManager extends android.text.ClipboardManager {
    private final Context mContext;
    private final Handler mHandler;
    private final ArrayList<OnPrimaryClipChangedListener> mPrimaryClipChangedListeners = new ArrayList<>();
    private final IOnPrimaryClipChangedListener.Stub mPrimaryClipChangedServiceListener = new AnonymousClass1();
    private final IClipboard mService = IClipboard.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.CLIPBOARD_SERVICE));

    /* loaded from: classes.dex */
    public interface OnPrimaryClipChangedListener {
        void onPrimaryClipChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.content.ClipboardManager$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends IOnPrimaryClipChangedListener.Stub {
        AnonymousClass1() {
        }

        @Override // android.content.IOnPrimaryClipChangedListener
        public void dispatchPrimaryClipChanged() {
            ClipboardManager.this.mHandler.post(new Runnable() { // from class: android.content.-$$Lambda$ClipboardManager$1$hQk8olbGAgUi4WWNG4ZuDZsM39s
                @Override // java.lang.Runnable
                public final void run() {
                    ClipboardManager.this.reportPrimaryClipChanged();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ClipboardManager(Context context, Handler handler) throws ServiceManager.ServiceNotFoundException {
        this.mContext = context;
        this.mHandler = handler;
    }

    public void setPrimaryClip(ClipData clip) {
        try {
            Preconditions.checkNotNull(clip);
            clip.prepareToLeaveProcess(true);
            this.mService.setPrimaryClip(clip, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void clearPrimaryClip() {
        try {
            this.mService.clearPrimaryClip(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ClipData getPrimaryClip() {
        try {
            return this.mService.getPrimaryClip(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ClipDescription getPrimaryClipDescription() {
        try {
            return this.mService.getPrimaryClipDescription(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean hasPrimaryClip() {
        try {
            return this.mService.hasPrimaryClip(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void addPrimaryClipChangedListener(OnPrimaryClipChangedListener what) {
        synchronized (this.mPrimaryClipChangedListeners) {
            if (this.mPrimaryClipChangedListeners.isEmpty()) {
                try {
                    this.mService.addPrimaryClipChangedListener(this.mPrimaryClipChangedServiceListener, this.mContext.getOpPackageName());
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            this.mPrimaryClipChangedListeners.add(what);
        }
    }

    public void removePrimaryClipChangedListener(OnPrimaryClipChangedListener what) {
        synchronized (this.mPrimaryClipChangedListeners) {
            this.mPrimaryClipChangedListeners.remove(what);
            if (this.mPrimaryClipChangedListeners.isEmpty()) {
                try {
                    this.mService.removePrimaryClipChangedListener(this.mPrimaryClipChangedServiceListener);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    @Override // android.text.ClipboardManager
    @Deprecated
    public CharSequence getText() {
        ClipData clip = getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(this.mContext);
        }
        return null;
    }

    @Override // android.text.ClipboardManager
    @Deprecated
    public void setText(CharSequence text) {
        setPrimaryClip(ClipData.newPlainText(null, text));
    }

    @Override // android.text.ClipboardManager
    @Deprecated
    public boolean hasText() {
        try {
            return this.mService.hasClipboardText(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: public */
    public void reportPrimaryClipChanged() {
        synchronized (this.mPrimaryClipChangedListeners) {
            int N = this.mPrimaryClipChangedListeners.size();
            if (N <= 0) {
                return;
            }
            Object[] listeners = this.mPrimaryClipChangedListeners.toArray();
            for (Object obj : listeners) {
                ((OnPrimaryClipChangedListener) obj).onPrimaryClipChanged();
            }
        }
    }
}
