package android.app;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.SettingsStringUtil;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;
@Deprecated
/* loaded from: classes.dex */
public abstract class FragmentHostCallback<E> extends FragmentContainer {
    private final Activity mActivity;
    private ArrayMap<String, LoaderManager> mAllLoaderManagers;
    private boolean mCheckedForLoaderManager;
    final Context mContext;
    final FragmentManagerImpl mFragmentManager;
    private final Handler mHandler;
    private LoaderManagerImpl mLoaderManager;
    public protected boolean mLoadersStarted;
    private boolean mRetainLoaders;
    final int mWindowAnimations;

    public abstract E onGetHost();

    public FragmentHostCallback(Context context, Handler handler, int windowAnimations) {
        this(context instanceof Activity ? (Activity) context : null, context, chooseHandler(context, handler), windowAnimations);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized FragmentHostCallback(Activity activity) {
        this(activity, activity, activity.mHandler, 0);
    }

    synchronized FragmentHostCallback(Activity activity, Context context, Handler handler, int windowAnimations) {
        this.mFragmentManager = new FragmentManagerImpl();
        this.mActivity = activity;
        this.mContext = context;
        this.mHandler = handler;
        this.mWindowAnimations = windowAnimations;
    }

    private static synchronized Handler chooseHandler(Context context, Handler handler) {
        if (handler == null && (context instanceof Activity)) {
            Activity activity = (Activity) context;
            return activity.mHandler;
        }
        return handler;
    }

    public void onDump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
    }

    public boolean onShouldSaveFragmentState(Fragment fragment) {
        return true;
    }

    public LayoutInflater onGetLayoutInflater() {
        return (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public boolean onUseFragmentManagerInflaterFactory() {
        return false;
    }

    public void onInvalidateOptionsMenu() {
    }

    public void onStartActivityFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options) {
        if (requestCode != -1) {
            throw new IllegalStateException("Starting activity with a requestCode requires a FragmentActivity host");
        }
        this.mContext.startActivity(intent);
    }

    public synchronized void onStartActivityAsUserFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options, UserHandle userHandle) {
        if (requestCode != -1) {
            throw new IllegalStateException("Starting activity with a requestCode requires a FragmentActivity host");
        }
        this.mContext.startActivityAsUser(intent, userHandle);
    }

    public void onStartIntentSenderFromFragment(Fragment fragment, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        if (requestCode != -1) {
            throw new IllegalStateException("Starting intent sender with a requestCode requires a FragmentActivity host");
        }
        this.mContext.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, options);
    }

    public void onRequestPermissionsFromFragment(Fragment fragment, String[] permissions, int requestCode) {
    }

    public boolean onHasWindowAnimations() {
        return true;
    }

    public int onGetWindowAnimations() {
        return this.mWindowAnimations;
    }

    public void onAttachFragment(Fragment fragment) {
    }

    @Override // android.app.FragmentContainer
    public <T extends View> T onFindViewById(int id) {
        return null;
    }

    @Override // android.app.FragmentContainer
    public boolean onHasView() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean getRetainLoaders() {
        return this.mRetainLoaders;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Activity getActivity() {
        return this.mActivity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Context getContext() {
        return this.mContext;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Handler getHandler() {
        return this.mHandler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized FragmentManagerImpl getFragmentManagerImpl() {
        return this.mFragmentManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized LoaderManagerImpl getLoaderManagerImpl() {
        if (this.mLoaderManager != null) {
            return this.mLoaderManager;
        }
        this.mCheckedForLoaderManager = true;
        this.mLoaderManager = getLoaderManager("(root)", this.mLoadersStarted, true);
        return this.mLoaderManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void inactivateFragment(String who) {
        LoaderManagerImpl lm;
        if (this.mAllLoaderManagers != null && (lm = (LoaderManagerImpl) this.mAllLoaderManagers.get(who)) != null && !lm.mRetaining) {
            lm.doDestroy();
            this.mAllLoaderManagers.remove(who);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void doLoaderStart() {
        if (this.mLoadersStarted) {
            return;
        }
        this.mLoadersStarted = true;
        if (this.mLoaderManager != null) {
            this.mLoaderManager.doStart();
        } else if (!this.mCheckedForLoaderManager) {
            this.mLoaderManager = getLoaderManager("(root)", this.mLoadersStarted, false);
        }
        this.mCheckedForLoaderManager = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void doLoaderStop(boolean retain) {
        this.mRetainLoaders = retain;
        if (this.mLoaderManager == null || !this.mLoadersStarted) {
            return;
        }
        this.mLoadersStarted = false;
        if (retain) {
            this.mLoaderManager.doRetain();
        } else {
            this.mLoaderManager.doStop();
        }
    }

    synchronized void doLoaderRetain() {
        if (this.mLoaderManager == null) {
            return;
        }
        this.mLoaderManager.doRetain();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void doLoaderDestroy() {
        if (this.mLoaderManager == null) {
            return;
        }
        this.mLoaderManager.doDestroy();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void reportLoaderStart() {
        if (this.mAllLoaderManagers != null) {
            int N = this.mAllLoaderManagers.size();
            LoaderManagerImpl[] loaders = new LoaderManagerImpl[N];
            for (int i = N - 1; i >= 0; i--) {
                loaders[i] = (LoaderManagerImpl) this.mAllLoaderManagers.valueAt(i);
            }
            for (int i2 = 0; i2 < N; i2++) {
                LoaderManagerImpl lm = loaders[i2];
                lm.finishRetain();
                lm.doReportStart();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized LoaderManagerImpl getLoaderManager(String who, boolean started, boolean create) {
        if (this.mAllLoaderManagers == null) {
            this.mAllLoaderManagers = new ArrayMap<>();
        }
        LoaderManagerImpl lm = (LoaderManagerImpl) this.mAllLoaderManagers.get(who);
        if (lm == null && create) {
            LoaderManagerImpl lm2 = new LoaderManagerImpl(who, this, started);
            this.mAllLoaderManagers.put(who, lm2);
            return lm2;
        } else if (started && lm != null && !lm.mStarted) {
            lm.doStart();
            return lm;
        } else {
            return lm;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ArrayMap<String, LoaderManager> retainLoaderNonConfig() {
        boolean retainLoaders = false;
        if (this.mAllLoaderManagers != null) {
            int N = this.mAllLoaderManagers.size();
            LoaderManagerImpl[] loaders = new LoaderManagerImpl[N];
            for (int i = N - 1; i >= 0; i--) {
                loaders[i] = (LoaderManagerImpl) this.mAllLoaderManagers.valueAt(i);
            }
            boolean doRetainLoaders = getRetainLoaders();
            for (int i2 = 0; i2 < N; i2++) {
                LoaderManagerImpl lm = loaders[i2];
                if (!lm.mRetaining && doRetainLoaders) {
                    if (!lm.mStarted) {
                        lm.doStart();
                    }
                    lm.doRetain();
                }
                if (lm.mRetaining) {
                    retainLoaders = true;
                } else {
                    lm.doDestroy();
                    this.mAllLoaderManagers.remove(lm.mWho);
                }
            }
        }
        if (retainLoaders) {
            return this.mAllLoaderManagers;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void restoreLoaderNonConfig(ArrayMap<String, LoaderManager> loaderManagers) {
        if (loaderManagers != null) {
            int N = loaderManagers.size();
            for (int i = 0; i < N; i++) {
                ((LoaderManagerImpl) loaderManagers.valueAt(i)).updateHostController(this);
            }
        }
        this.mAllLoaderManagers = loaderManagers;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void dumpLoaders(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        writer.print(prefix);
        writer.print("mLoadersStarted=");
        writer.println(this.mLoadersStarted);
        if (this.mLoaderManager != null) {
            writer.print(prefix);
            writer.print("Loader Manager ");
            writer.print(Integer.toHexString(System.identityHashCode(this.mLoaderManager)));
            writer.println(SettingsStringUtil.DELIMITER);
            LoaderManagerImpl loaderManagerImpl = this.mLoaderManager;
            loaderManagerImpl.dump(prefix + "  ", fd, writer, args);
        }
    }
}
