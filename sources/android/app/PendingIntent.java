package android.app;

import android.content.Context;
import android.content.IIntentReceiver;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.AndroidException;
import android.util.ArraySet;
import android.util.proto.ProtoOutputStream;
import com.android.internal.os.IResultReceiver;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public final class PendingIntent implements Parcelable {
    public static final int FLAG_CANCEL_CURRENT = 268435456;
    public static final int FLAG_IMMUTABLE = 67108864;
    public static final int FLAG_NO_CREATE = 536870912;
    public static final int FLAG_ONE_SHOT = 1073741824;
    public static final int FLAG_UPDATE_CURRENT = 134217728;
    private ArraySet<CancelListener> mCancelListeners;
    private IResultReceiver mCancelReceiver;
    private final IIntentSender mTarget;
    private IBinder mWhitelistToken;
    private static final ThreadLocal<OnMarshaledListener> sOnMarshaledListener = new ThreadLocal<>();
    public static final Parcelable.Creator<PendingIntent> CREATOR = new Parcelable.Creator<PendingIntent>() { // from class: android.app.PendingIntent.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PendingIntent createFromParcel(Parcel in) {
            IBinder target = in.readStrongBinder();
            if (target != null) {
                return new PendingIntent(target, in.getClassCookie(PendingIntent.class));
            }
            return null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PendingIntent[] newArray(int size) {
            return new PendingIntent[size];
        }
    };

    /* loaded from: classes.dex */
    public interface CancelListener {
        synchronized void onCancelled(PendingIntent pendingIntent);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Flags {
    }

    /* loaded from: classes.dex */
    public interface OnFinished {
        void onSendFinished(PendingIntent pendingIntent, Intent intent, int i, String str, Bundle bundle);
    }

    /* loaded from: classes.dex */
    public interface OnMarshaledListener {
        synchronized void onMarshaled(PendingIntent pendingIntent, Parcel parcel, int i);
    }

    /* loaded from: classes.dex */
    public static class CanceledException extends AndroidException {
        public CanceledException() {
        }

        public CanceledException(String name) {
            super(name);
        }

        public CanceledException(Exception cause) {
            super(cause);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class FinishedDispatcher extends IIntentReceiver.Stub implements Runnable {
        private static Handler sDefaultSystemHandler;
        private final Handler mHandler;
        private Intent mIntent;
        private final PendingIntent mPendingIntent;
        private int mResultCode;
        private String mResultData;
        private Bundle mResultExtras;
        private final OnFinished mWho;

        synchronized FinishedDispatcher(PendingIntent pi, OnFinished who, Handler handler) {
            this.mPendingIntent = pi;
            this.mWho = who;
            if (handler == null && ActivityThread.isSystem()) {
                if (sDefaultSystemHandler == null) {
                    sDefaultSystemHandler = new Handler(Looper.getMainLooper());
                }
                this.mHandler = sDefaultSystemHandler;
                return;
            }
            this.mHandler = handler;
        }

        public synchronized void performReceive(Intent intent, int resultCode, String data, Bundle extras, boolean serialized, boolean sticky, int sendingUser) {
            this.mIntent = intent;
            this.mResultCode = resultCode;
            this.mResultData = data;
            this.mResultExtras = extras;
            if (this.mHandler == null) {
                run();
            } else {
                this.mHandler.post(this);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mWho.onSendFinished(this.mPendingIntent, this.mIntent, this.mResultCode, this.mResultData, this.mResultExtras);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setOnMarshaledListener(OnMarshaledListener listener) {
        sOnMarshaledListener.set(listener);
    }

    public static PendingIntent getActivity(Context context, int requestCode, Intent intent, int flags) {
        return getActivity(context, requestCode, intent, flags, null);
    }

    public static PendingIntent getActivity(Context context, int requestCode, Intent intent, int flags, Bundle options) {
        String str;
        String packageName = context.getPackageName();
        if (intent != null) {
            str = intent.resolveTypeIfNeeded(context.getContentResolver());
        } else {
            str = null;
        }
        String resolvedType = str;
        try {
            intent.migrateExtraStreamToClipData();
        } catch (RemoteException e) {
            e = e;
        }
        try {
            intent.prepareToLeaveProcess(context);
            IIntentSender target = ActivityManager.getService().getIntentSender(2, packageName, null, null, requestCode, new Intent[]{intent}, resolvedType != null ? new String[]{resolvedType} : null, flags, options, context.getUserId());
            if (target != null) {
                return new PendingIntent(target);
            }
            return null;
        } catch (RemoteException e2) {
            e = e2;
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static PendingIntent getActivityAsUser(Context context, int requestCode, Intent intent, int flags, Bundle options, UserHandle user) {
        String str;
        String packageName = context.getPackageName();
        if (intent != null) {
            str = intent.resolveTypeIfNeeded(context.getContentResolver());
        } else {
            str = null;
        }
        String resolvedType = str;
        try {
            intent.migrateExtraStreamToClipData();
        } catch (RemoteException e) {
            e = e;
        }
        try {
            intent.prepareToLeaveProcess(context);
            IIntentSender target = ActivityManager.getService().getIntentSender(2, packageName, null, null, requestCode, new Intent[]{intent}, resolvedType != null ? new String[]{resolvedType} : null, flags, options, user.getIdentifier());
            if (target != null) {
                return new PendingIntent(target);
            }
            return null;
        } catch (RemoteException e2) {
            e = e2;
            throw e.rethrowFromSystemServer();
        }
    }

    public static PendingIntent getActivities(Context context, int requestCode, Intent[] intents, int flags) {
        return getActivities(context, requestCode, intents, flags, null);
    }

    public static PendingIntent getActivities(Context context, int requestCode, Intent[] intents, int flags, Bundle options) {
        String packageName = context.getPackageName();
        String[] resolvedTypes = new String[intents.length];
        for (int i = 0; i < intents.length; i++) {
            intents[i].migrateExtraStreamToClipData();
            intents[i].prepareToLeaveProcess(context);
            resolvedTypes[i] = intents[i].resolveTypeIfNeeded(context.getContentResolver());
        }
        try {
            IIntentSender target = ActivityManager.getService().getIntentSender(2, packageName, null, null, requestCode, intents, resolvedTypes, flags, options, context.getUserId());
            if (target != null) {
                return new PendingIntent(target);
            }
            return null;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static synchronized PendingIntent getActivitiesAsUser(Context context, int requestCode, Intent[] intents, int flags, Bundle options, UserHandle user) {
        String packageName = context.getPackageName();
        String[] resolvedTypes = new String[intents.length];
        for (int i = 0; i < intents.length; i++) {
            intents[i].migrateExtraStreamToClipData();
            intents[i].prepareToLeaveProcess(context);
            resolvedTypes[i] = intents[i].resolveTypeIfNeeded(context.getContentResolver());
        }
        try {
            IIntentSender target = ActivityManager.getService().getIntentSender(2, packageName, null, null, requestCode, intents, resolvedTypes, flags, options, user.getIdentifier());
            if (target != null) {
                return new PendingIntent(target);
            }
            return null;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static PendingIntent getBroadcast(Context context, int requestCode, Intent intent, int flags) {
        return getBroadcastAsUser(context, requestCode, intent, flags, context.getUser());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static PendingIntent getBroadcastAsUser(Context context, int requestCode, Intent intent, int flags, UserHandle userHandle) {
        String str;
        String packageName = context.getPackageName();
        if (intent != null) {
            str = intent.resolveTypeIfNeeded(context.getContentResolver());
        } else {
            str = null;
        }
        String resolvedType = str;
        try {
            intent.prepareToLeaveProcess(context);
            IIntentSender target = ActivityManager.getService().getIntentSender(1, packageName, null, null, requestCode, new Intent[]{intent}, resolvedType != null ? new String[]{resolvedType} : null, flags, null, userHandle.getIdentifier());
            if (target != null) {
                return new PendingIntent(target);
            }
            return null;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static PendingIntent getService(Context context, int requestCode, Intent intent, int flags) {
        return buildServicePendingIntent(context, requestCode, intent, flags, 4);
    }

    public static PendingIntent getForegroundService(Context context, int requestCode, Intent intent, int flags) {
        return buildServicePendingIntent(context, requestCode, intent, flags, 5);
    }

    private static synchronized PendingIntent buildServicePendingIntent(Context context, int requestCode, Intent intent, int flags, int serviceKind) {
        String str;
        String packageName = context.getPackageName();
        if (intent != null) {
            str = intent.resolveTypeIfNeeded(context.getContentResolver());
        } else {
            str = null;
        }
        String resolvedType = str;
        try {
            intent.prepareToLeaveProcess(context);
            IIntentSender target = ActivityManager.getService().getIntentSender(serviceKind, packageName, null, null, requestCode, new Intent[]{intent}, resolvedType != null ? new String[]{resolvedType} : null, flags, null, context.getUserId());
            if (target != null) {
                return new PendingIntent(target);
            }
            return null;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public IntentSender getIntentSender() {
        return new IntentSender(this.mTarget, this.mWhitelistToken);
    }

    public void cancel() {
        try {
            ActivityManager.getService().cancelIntentSender(this.mTarget);
        } catch (RemoteException e) {
        }
    }

    public void send() throws CanceledException {
        send(null, 0, null, null, null, null, null);
    }

    public void send(int code) throws CanceledException {
        send(null, code, null, null, null, null, null);
    }

    public void send(Context context, int code, Intent intent) throws CanceledException {
        send(context, code, intent, null, null, null, null);
    }

    public void send(int code, OnFinished onFinished, Handler handler) throws CanceledException {
        send(null, code, null, onFinished, handler, null, null);
    }

    public void send(Context context, int code, Intent intent, OnFinished onFinished, Handler handler) throws CanceledException {
        send(context, code, intent, onFinished, handler, null, null);
    }

    public void send(Context context, int code, Intent intent, OnFinished onFinished, Handler handler, String requiredPermission) throws CanceledException {
        send(context, code, intent, onFinished, handler, requiredPermission, null);
    }

    public void send(Context context, int code, Intent intent, OnFinished onFinished, Handler handler, String requiredPermission, Bundle options) throws CanceledException {
        if (sendAndReturnResult(context, code, intent, onFinished, handler, requiredPermission, options) < 0) {
            throw new CanceledException();
        }
    }

    public synchronized int sendAndReturnResult(Context context, int code, Intent intent, OnFinished onFinished, Handler handler, String requiredPermission, Bundle options) throws CanceledException {
        String resolvedType;
        FinishedDispatcher finishedDispatcher = null;
        if (intent != null) {
            try {
                resolvedType = intent.resolveTypeIfNeeded(context.getContentResolver());
            } catch (RemoteException e) {
                e = e;
                throw new CanceledException(e);
            }
        } else {
            resolvedType = null;
        }
        IActivityManager service = ActivityManager.getService();
        IIntentSender iIntentSender = this.mTarget;
        IBinder iBinder = this.mWhitelistToken;
        if (onFinished != null) {
            try {
                finishedDispatcher = new FinishedDispatcher(this, onFinished, handler);
            } catch (RemoteException e2) {
                e = e2;
                throw new CanceledException(e);
            }
        }
        return service.sendIntentSender(iIntentSender, iBinder, code, intent, resolvedType, finishedDispatcher, requiredPermission, options);
    }

    @Deprecated
    public String getTargetPackage() {
        try {
            return ActivityManager.getService().getPackageForIntentSender(this.mTarget);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getCreatorPackage() {
        try {
            return ActivityManager.getService().getPackageForIntentSender(this.mTarget);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getCreatorUid() {
        try {
            return ActivityManager.getService().getUidForIntentSender(this.mTarget);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void registerCancelListener(CancelListener cancelListener) {
        if (this.mCancelReceiver == null) {
            this.mCancelReceiver = new IResultReceiver.Stub() { // from class: android.app.PendingIntent.1
                @Override // com.android.internal.os.IResultReceiver
                public void send(int resultCode, Bundle resultData) throws RemoteException {
                    PendingIntent.this.notifyCancelListeners();
                }
            };
        }
        if (this.mCancelListeners == null) {
            this.mCancelListeners = new ArraySet<>();
        }
        boolean wasEmpty = this.mCancelListeners.isEmpty();
        this.mCancelListeners.add(cancelListener);
        if (wasEmpty) {
            try {
                ActivityManager.getService().registerIntentSenderCancelListener(this.mTarget, this.mCancelReceiver);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void notifyCancelListeners() {
        ArraySet<CancelListener> cancelListeners;
        cancelListeners = new ArraySet<>(this.mCancelListeners);
        int size = cancelListeners.size();
        for (int i = 0; i < size; i++) {
            cancelListeners.valueAt(i).onCancelled(this);
        }
    }

    public synchronized void unregisterCancelListener(CancelListener cancelListener) {
        if (this.mCancelListeners == null) {
            return;
        }
        boolean wasEmpty = this.mCancelListeners.isEmpty();
        this.mCancelListeners.remove(cancelListener);
        if (this.mCancelListeners.isEmpty() && !wasEmpty) {
            try {
                ActivityManager.getService().unregisterIntentSenderCancelListener(this.mTarget, this.mCancelReceiver);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public UserHandle getCreatorUserHandle() {
        try {
            int uid = ActivityManager.getService().getUidForIntentSender(this.mTarget);
            if (uid > 0) {
                return new UserHandle(UserHandle.getUserId(uid));
            }
            return null;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized boolean isTargetedToPackage() {
        try {
            return ActivityManager.getService().isIntentSenderTargetedToPackage(this.mTarget);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private protected boolean isActivity() {
        try {
            return ActivityManager.getService().isIntentSenderAnActivity(this.mTarget);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized boolean isForegroundService() {
        try {
            return ActivityManager.getService().isIntentSenderAForegroundService(this.mTarget);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Intent getIntent() {
        try {
            return ActivityManager.getService().getIntentForIntentSender(this.mTarget);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private protected String getTag(String prefix) {
        try {
            return ActivityManager.getService().getTagForIntentSender(this.mTarget, prefix);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean equals(Object otherObj) {
        if (otherObj instanceof PendingIntent) {
            return this.mTarget.asBinder().equals(((PendingIntent) otherObj).mTarget.asBinder());
        }
        return false;
    }

    public int hashCode() {
        return this.mTarget.asBinder().hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("PendingIntent{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(": ");
        sb.append(this.mTarget != null ? this.mTarget.asBinder() : null);
        sb.append('}');
        return sb.toString();
    }

    public synchronized void writeToProto(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        if (this.mTarget != null) {
            proto.write(1138166333441L, this.mTarget.asBinder().toString());
        }
        proto.end(token);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeStrongBinder(this.mTarget.asBinder());
        OnMarshaledListener listener = sOnMarshaledListener.get();
        if (listener != null) {
            listener.onMarshaled(this, out, flags);
        }
    }

    public static void writePendingIntentOrNullToParcel(PendingIntent sender, Parcel out) {
        OnMarshaledListener listener;
        out.writeStrongBinder(sender != null ? sender.mTarget.asBinder() : null);
        if (sender != null && (listener = sOnMarshaledListener.get()) != null) {
            listener.onMarshaled(sender, out, 0);
        }
    }

    public static PendingIntent readPendingIntentOrNullFromParcel(Parcel in) {
        IBinder b = in.readStrongBinder();
        if (b != null) {
            return new PendingIntent(b, in.getClassCookie(PendingIntent.class));
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized PendingIntent(IIntentSender target) {
        this.mTarget = target;
    }

    synchronized PendingIntent(IBinder target, Object cookie) {
        this.mTarget = IIntentSender.Stub.asInterface(target);
        if (cookie != null) {
            this.mWhitelistToken = (IBinder) cookie;
        }
    }

    public synchronized IIntentSender getTarget() {
        return this.mTarget;
    }

    public synchronized IBinder getWhitelistToken() {
        return this.mWhitelistToken;
    }
}
