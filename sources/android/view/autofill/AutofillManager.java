package android.view.autofill;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.metrics.LogMaker;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.provider.SettingsStringUtil;
import android.service.autofill.FillEventHistory;
import android.service.autofill.UserData;
import android.service.notification.ZenModeConfig;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.view.Choreographer;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.autofill.AutofillManager;
import android.view.autofill.IAutoFillManagerClient;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParserException;
import sun.misc.Cleaner;
/* loaded from: classes2.dex */
public final class AutofillManager {
    public static final int ACTION_START_SESSION = 1;
    public static final int ACTION_VALUE_CHANGED = 4;
    public static final int ACTION_VIEW_ENTERED = 2;
    public static final int ACTION_VIEW_EXITED = 3;
    private static final int AUTHENTICATION_ID_DATASET_ID_MASK = 65535;
    private static final int AUTHENTICATION_ID_DATASET_ID_SHIFT = 16;
    public static final int AUTHENTICATION_ID_DATASET_ID_UNDEFINED = 65535;
    public static final String EXTRA_ASSIST_STRUCTURE = "android.view.autofill.extra.ASSIST_STRUCTURE";
    public static final String EXTRA_AUTHENTICATION_RESULT = "android.view.autofill.extra.AUTHENTICATION_RESULT";
    public static final String EXTRA_CLIENT_STATE = "android.view.autofill.extra.CLIENT_STATE";
    public static final String EXTRA_RESTORE_SESSION_TOKEN = "android.view.autofill.extra.RESTORE_SESSION_TOKEN";
    public static final int FC_SERVICE_TIMEOUT = 5000;
    public static final int FLAG_ADD_CLIENT_DEBUG = 2;
    public static final int FLAG_ADD_CLIENT_ENABLED = 1;
    public static final int FLAG_ADD_CLIENT_VERBOSE = 4;
    private static final String LAST_AUTOFILLED_DATA_TAG = "android:lastAutoFilledData";
    public static final int NO_SESSION = Integer.MIN_VALUE;
    public static final int PENDING_UI_OPERATION_CANCEL = 1;
    public static final int PENDING_UI_OPERATION_RESTORE = 2;
    private static final String SESSION_ID_TAG = "android:sessionId";
    public static final int SET_STATE_FLAG_DEBUG = 8;
    public static final int SET_STATE_FLAG_ENABLED = 1;
    public static final int SET_STATE_FLAG_RESET_CLIENT = 4;
    public static final int SET_STATE_FLAG_RESET_SESSION = 2;
    public static final int SET_STATE_FLAG_VERBOSE = 16;
    public static final int STATE_ACTIVE = 1;
    public static final int STATE_DISABLED_BY_SERVICE = 4;
    public static final int STATE_FINISHED = 2;
    public static final int STATE_SHOWING_SAVE_UI = 3;
    private static final String STATE_TAG = "android:state";
    public static final int STATE_UNKNOWN = 0;
    public static final int STATE_UNKNOWN_COMPAT_MODE = 5;
    private static final String TAG = "AutofillManager";
    @GuardedBy("mLock")
    private AutofillCallback mCallback;
    @GuardedBy("mLock")
    private CompatibilityBridge mCompatibilityBridge;
    private final Context mContext;
    @GuardedBy("mLock")
    private boolean mEnabled;
    @GuardedBy("mLock")
    private ArraySet<AutofillId> mEnteredIds;
    @GuardedBy("mLock")
    private ArraySet<AutofillId> mFillableIds;
    private AutofillId mIdShownFillUi;
    @GuardedBy("mLock")
    private ParcelableMap mLastAutofilledData;
    @GuardedBy("mLock")
    private boolean mOnInvisibleCalled;
    @GuardedBy("mLock")
    private boolean mSaveOnFinish;
    @GuardedBy("mLock")
    private AutofillId mSaveTriggerId;
    private final IAutoFillManager mService;
    @GuardedBy("mLock")
    private IAutoFillManagerClient mServiceClient;
    @GuardedBy("mLock")
    private Cleaner mServiceClientCleaner;
    @GuardedBy("mLock")
    private TrackedViews mTrackedViews;
    private final MetricsLogger mMetricsLogger = new MetricsLogger();
    private final Object mLock = new Object();
    @GuardedBy("mLock")
    private int mSessionId = Integer.MIN_VALUE;
    @GuardedBy("mLock")
    private int mState = 0;

    /* loaded from: classes2.dex */
    public interface AutofillClient {
        synchronized void autofillClientAuthenticate(int i, IntentSender intentSender, Intent intent);

        synchronized void autofillClientDispatchUnhandledKey(View view, KeyEvent keyEvent);

        synchronized View autofillClientFindViewByAccessibilityIdTraversal(int i, int i2);

        synchronized View autofillClientFindViewByAutofillIdTraversal(AutofillId autofillId);

        synchronized View[] autofillClientFindViewsByAutofillIdTraversal(AutofillId[] autofillIdArr);

        synchronized IBinder autofillClientGetActivityToken();

        synchronized ComponentName autofillClientGetComponentName();

        synchronized AutofillId autofillClientGetNextAutofillId();

        synchronized boolean[] autofillClientGetViewVisibility(AutofillId[] autofillIdArr);

        synchronized boolean autofillClientIsCompatibilityModeEnabled();

        synchronized boolean autofillClientIsFillUiShowing();

        synchronized boolean autofillClientIsVisibleForAutofill();

        synchronized boolean autofillClientRequestHideFillUi();

        synchronized boolean autofillClientRequestShowFillUi(View view, int i, int i2, Rect rect, IAutofillWindowPresenter iAutofillWindowPresenter);

        synchronized void autofillClientResetableStateAvailable();

        synchronized void autofillClientRunOnUiThread(Runnable runnable);

        synchronized boolean isDisablingEnterExitEventForAutofill();
    }

    public static synchronized int makeAuthenticationId(int requestId, int datasetId) {
        return (requestId << 16) | (65535 & datasetId);
    }

    public static synchronized int getRequestIdFromAuthenticationId(int authRequestId) {
        return authRequestId >> 16;
    }

    public static synchronized int getDatasetIdFromAuthenticationId(int authRequestId) {
        return 65535 & authRequestId;
    }

    public synchronized AutofillManager(Context context, IAutoFillManager service) {
        this.mContext = (Context) Preconditions.checkNotNull(context, "context cannot be null");
        this.mService = service;
    }

    public synchronized void enableCompatibilityMode() {
        synchronized (this.mLock) {
            if (Helper.sDebug) {
                Slog.d(TAG, "creating CompatibilityBridge for " + this.mContext);
            }
            this.mCompatibilityBridge = new CompatibilityBridge();
        }
    }

    public synchronized void onCreate(Bundle savedInstanceState) {
        if (!hasAutofillFeature()) {
            return;
        }
        synchronized (this.mLock) {
            this.mLastAutofilledData = (ParcelableMap) savedInstanceState.getParcelable(LAST_AUTOFILLED_DATA_TAG);
            if (isActiveLocked()) {
                Log.w(TAG, "New session was started before onCreate()");
                return;
            }
            this.mSessionId = savedInstanceState.getInt(SESSION_ID_TAG, Integer.MIN_VALUE);
            this.mState = savedInstanceState.getInt(STATE_TAG, 0);
            if (this.mSessionId != Integer.MIN_VALUE) {
                ensureServiceClientAddedIfNeededLocked();
                AutofillClient client = getClient();
                if (client != null) {
                    try {
                        boolean sessionWasRestored = this.mService.restoreSession(this.mSessionId, client.autofillClientGetActivityToken(), this.mServiceClient.asBinder());
                        if (!sessionWasRestored) {
                            Log.w(TAG, "Session " + this.mSessionId + " could not be restored");
                            this.mSessionId = Integer.MIN_VALUE;
                            this.mState = 0;
                        } else {
                            if (Helper.sDebug) {
                                Log.d(TAG, "session " + this.mSessionId + " was restored");
                            }
                            client.autofillClientResetableStateAvailable();
                        }
                    } catch (RemoteException e) {
                        Log.e(TAG, "Could not figure out if there was an autofill session", e);
                    }
                }
            }
        }
    }

    public synchronized void onVisibleForAutofill() {
        Choreographer.getInstance().postCallback(3, new Runnable() { // from class: android.view.autofill.-$$Lambda$AutofillManager$YfpJNFodEuj5lbXfPlc77fsEvC8
            @Override // java.lang.Runnable
            public final void run() {
                AutofillManager.lambda$onVisibleForAutofill$0(AutofillManager.this);
            }
        }, null);
    }

    public static /* synthetic */ void lambda$onVisibleForAutofill$0(AutofillManager autofillManager) {
        synchronized (autofillManager.mLock) {
            if (autofillManager.mEnabled && autofillManager.isActiveLocked() && autofillManager.mTrackedViews != null) {
                autofillManager.mTrackedViews.onVisibleForAutofillChangedLocked();
            }
        }
    }

    public synchronized void onInvisibleForAutofill() {
        synchronized (this.mLock) {
            this.mOnInvisibleCalled = true;
        }
    }

    public synchronized void onSaveInstanceState(Bundle outState) {
        if (!hasAutofillFeature()) {
            return;
        }
        synchronized (this.mLock) {
            if (this.mSessionId != Integer.MIN_VALUE) {
                outState.putInt(SESSION_ID_TAG, this.mSessionId);
            }
            if (this.mState != 0) {
                outState.putInt(STATE_TAG, this.mState);
            }
            if (this.mLastAutofilledData != null) {
                outState.putParcelable(LAST_AUTOFILLED_DATA_TAG, this.mLastAutofilledData);
            }
        }
    }

    @GuardedBy("mLock")
    public synchronized boolean isCompatibilityModeEnabledLocked() {
        return this.mCompatibilityBridge != null;
    }

    public boolean isEnabled() {
        if (hasAutofillFeature()) {
            synchronized (this.mLock) {
                if (isDisabledByServiceLocked()) {
                    return false;
                }
                ensureServiceClientAddedIfNeededLocked();
                return this.mEnabled;
            }
        }
        return false;
    }

    public synchronized FillEventHistory getFillEventHistory() {
        try {
            return this.mService.getFillEventHistory();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public void requestAutofill(View view) {
        notifyViewEntered(view, 1);
    }

    public void requestAutofill(View view, int virtualId, Rect absBounds) {
        notifyViewEntered(view, virtualId, absBounds, 1);
    }

    public void notifyViewEntered(View view) {
        notifyViewEntered(view, 0);
    }

    @GuardedBy("mLock")
    private synchronized boolean shouldIgnoreViewEnteredLocked(AutofillId id, int flags) {
        if (isDisabledByServiceLocked()) {
            if (Helper.sVerbose) {
                Log.v(TAG, "ignoring notifyViewEntered(flags=" + flags + ", view=" + id + ") on state " + getStateAsStringLocked() + " because disabled by svc");
            }
            return true;
        } else if (isFinishedLocked() && (flags & 1) == 0 && this.mEnteredIds != null && this.mEnteredIds.contains(id)) {
            if (Helper.sVerbose) {
                Log.v(TAG, "ignoring notifyViewEntered(flags=" + flags + ", view=" + id + ") on state " + getStateAsStringLocked() + " because view was already entered: " + this.mEnteredIds);
            }
            return true;
        } else if (Helper.sVerbose) {
            Log.v(TAG, "not ignoring notifyViewEntered(flags=" + flags + ", view=" + id + ", state " + getStateAsStringLocked() + ", enteredIds=" + this.mEnteredIds);
            return false;
        } else {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean isClientVisibleForAutofillLocked() {
        AutofillClient client = getClient();
        return client != null && client.autofillClientIsVisibleForAutofill();
    }

    private synchronized boolean isClientDisablingEnterExitEvent() {
        AutofillClient client = getClient();
        return client != null && client.isDisablingEnterExitEventForAutofill();
    }

    private synchronized void notifyViewEntered(View view, int flags) {
        AutofillCallback callback;
        if (!hasAutofillFeature()) {
            return;
        }
        synchronized (this.mLock) {
            callback = notifyViewEnteredLocked(view, flags);
        }
        if (callback != null) {
            this.mCallback.onAutofillEvent(view, 3);
        }
    }

    @GuardedBy("mLock")
    private synchronized AutofillCallback notifyViewEnteredLocked(View view, int flags) {
        AutofillId id = view.getAutofillId();
        if (shouldIgnoreViewEnteredLocked(id, flags)) {
            return null;
        }
        ensureServiceClientAddedIfNeededLocked();
        if (!this.mEnabled) {
            if (this.mCallback == null) {
                return null;
            }
            AutofillCallback callback = this.mCallback;
            return callback;
        } else if (isClientDisablingEnterExitEvent()) {
            return null;
        } else {
            AutofillValue value = view.getAutofillValue();
            if (!isActiveLocked()) {
                startSessionLocked(id, null, value, flags);
            } else {
                updateSessionLocked(id, null, value, 2, flags);
            }
            addEnteredIdLocked(id);
            return null;
        }
    }

    public void notifyViewExited(View view) {
        if (!hasAutofillFeature()) {
            return;
        }
        synchronized (this.mLock) {
            notifyViewExitedLocked(view);
        }
    }

    @GuardedBy("mLock")
    synchronized void notifyViewExitedLocked(View view) {
        ensureServiceClientAddedIfNeededLocked();
        if (this.mEnabled && isActiveLocked() && !isClientDisablingEnterExitEvent()) {
            AutofillId id = view.getAutofillId();
            updateSessionLocked(id, null, null, 3, 0);
        }
    }

    public void notifyViewVisibilityChanged(View view, boolean isVisible) {
        notifyViewVisibilityChangedInternal(view, 0, isVisible, false);
    }

    public void notifyViewVisibilityChanged(View view, int virtualId, boolean isVisible) {
        notifyViewVisibilityChangedInternal(view, virtualId, isVisible, true);
    }

    private synchronized void notifyViewVisibilityChangedInternal(View view, int virtualId, boolean isVisible, boolean virtual) {
        synchronized (this.mLock) {
            if (this.mEnabled && isActiveLocked()) {
                AutofillId id = virtual ? getAutofillId(view, virtualId) : view.getAutofillId();
                if (Helper.sVerbose) {
                    Log.v(TAG, "visibility changed for " + id + ": " + isVisible);
                }
                if (!isVisible && this.mFillableIds != null && this.mFillableIds.contains(id)) {
                    if (Helper.sDebug) {
                        Log.d(TAG, "Hidding UI when view " + id + " became invisible");
                    }
                    requestHideFillUi(id, view);
                }
                if (this.mTrackedViews != null) {
                    this.mTrackedViews.notifyViewVisibilityChangedLocked(id, isVisible);
                } else if (Helper.sVerbose) {
                    Log.v(TAG, "Ignoring visibility change on " + id + ": no tracked views");
                }
            }
        }
    }

    public void notifyViewEntered(View view, int virtualId, Rect absBounds) {
        notifyViewEntered(view, virtualId, absBounds, 0);
    }

    private synchronized void notifyViewEntered(View view, int virtualId, Rect bounds, int flags) {
        AutofillCallback callback;
        if (!hasAutofillFeature()) {
            return;
        }
        synchronized (this.mLock) {
            callback = notifyViewEnteredLocked(view, virtualId, bounds, flags);
        }
        if (callback != null) {
            callback.onAutofillEvent(view, virtualId, 3);
        }
    }

    @GuardedBy("mLock")
    private synchronized AutofillCallback notifyViewEnteredLocked(View view, int virtualId, Rect bounds, int flags) {
        AutofillId id = getAutofillId(view, virtualId);
        if (shouldIgnoreViewEnteredLocked(id, flags)) {
            return null;
        }
        ensureServiceClientAddedIfNeededLocked();
        if (!this.mEnabled) {
            if (this.mCallback == null) {
                return null;
            }
            AutofillCallback callback = this.mCallback;
            return callback;
        } else if (isClientDisablingEnterExitEvent()) {
            return null;
        } else {
            if (!isActiveLocked()) {
                startSessionLocked(id, bounds, null, flags);
            } else {
                updateSessionLocked(id, bounds, null, 2, flags);
            }
            addEnteredIdLocked(id);
            return null;
        }
    }

    @GuardedBy("mLock")
    private synchronized void addEnteredIdLocked(AutofillId id) {
        if (this.mEnteredIds == null) {
            this.mEnteredIds = new ArraySet<>(1);
        }
        this.mEnteredIds.add(id);
    }

    public void notifyViewExited(View view, int virtualId) {
        if (Helper.sVerbose) {
            Log.v(TAG, "notifyViewExited(" + view.getAutofillId() + ", " + virtualId);
        }
        if (!hasAutofillFeature()) {
            return;
        }
        synchronized (this.mLock) {
            notifyViewExitedLocked(view, virtualId);
        }
    }

    @GuardedBy("mLock")
    private synchronized void notifyViewExitedLocked(View view, int virtualId) {
        ensureServiceClientAddedIfNeededLocked();
        if (this.mEnabled && isActiveLocked() && !isClientDisablingEnterExitEvent()) {
            AutofillId id = getAutofillId(view, virtualId);
            updateSessionLocked(id, null, null, 3, 0);
        }
    }

    public void notifyValueChanged(View view) {
        if (!hasAutofillFeature()) {
            return;
        }
        AutofillId id = null;
        boolean valueWasRead = false;
        AutofillValue value = null;
        synchronized (this.mLock) {
            if (this.mLastAutofilledData == null) {
                view.setAutofilled(false);
            } else {
                id = view.getAutofillId();
                if (this.mLastAutofilledData.containsKey(id)) {
                    value = view.getAutofillValue();
                    valueWasRead = true;
                    if (Objects.equals(this.mLastAutofilledData.get(id), value)) {
                        view.setAutofilled(true);
                    } else {
                        view.setAutofilled(false);
                        this.mLastAutofilledData.remove(id);
                    }
                } else {
                    view.setAutofilled(false);
                }
            }
            if (this.mEnabled && isActiveLocked()) {
                if (id == null) {
                    id = view.getAutofillId();
                }
                if (!valueWasRead) {
                    value = view.getAutofillValue();
                }
                updateSessionLocked(id, null, value, 4, 0);
                return;
            }
            if (Helper.sVerbose) {
                Log.v(TAG, "notifyValueChanged(" + view.getAutofillId() + "): ignoring on state " + getStateAsStringLocked());
            }
        }
    }

    public void notifyValueChanged(View view, int virtualId, AutofillValue value) {
        if (!hasAutofillFeature()) {
            return;
        }
        synchronized (this.mLock) {
            if (this.mEnabled && isActiveLocked()) {
                AutofillId id = getAutofillId(view, virtualId);
                updateSessionLocked(id, null, value, 4, 0);
                return;
            }
            if (Helper.sVerbose) {
                Log.v(TAG, "notifyValueChanged(" + view.getAutofillId() + SettingsStringUtil.DELIMITER + virtualId + "): ignoring on state " + getStateAsStringLocked());
            }
        }
    }

    public void notifyViewClicked(View view) {
        notifyViewClicked(view.getAutofillId());
    }

    public void notifyViewClicked(View view, int virtualId) {
        notifyViewClicked(getAutofillId(view, virtualId));
    }

    private synchronized void notifyViewClicked(AutofillId id) {
        if (!hasAutofillFeature()) {
            return;
        }
        if (Helper.sVerbose) {
            Log.v(TAG, "notifyViewClicked(): id=" + id + ", trigger=" + this.mSaveTriggerId);
        }
        synchronized (this.mLock) {
            if (this.mEnabled && isActiveLocked()) {
                if (this.mSaveTriggerId != null && this.mSaveTriggerId.equals(id)) {
                    if (Helper.sDebug) {
                        Log.d(TAG, "triggering commit by click of " + id);
                    }
                    commitLocked();
                    this.mMetricsLogger.write(newLog(MetricsProto.MetricsEvent.AUTOFILL_SAVE_EXPLICITLY_TRIGGERED));
                }
            }
        }
    }

    public synchronized void onActivityFinishing() {
        if (!hasAutofillFeature()) {
            return;
        }
        synchronized (this.mLock) {
            if (this.mSaveOnFinish) {
                if (Helper.sDebug) {
                    Log.d(TAG, "onActivityFinishing(): calling commitLocked()");
                }
                commitLocked();
            } else {
                if (Helper.sDebug) {
                    Log.d(TAG, "onActivityFinishing(): calling cancelLocked()");
                }
                cancelLocked();
            }
        }
    }

    public void commit() {
        if (!hasAutofillFeature()) {
            return;
        }
        if (Helper.sVerbose) {
            Log.v(TAG, "commit() called by app");
        }
        synchronized (this.mLock) {
            commitLocked();
        }
    }

    @GuardedBy("mLock")
    private synchronized void commitLocked() {
        if (!this.mEnabled && !isActiveLocked()) {
            return;
        }
        finishSessionLocked();
    }

    public void cancel() {
        if (Helper.sVerbose) {
            Log.v(TAG, "cancel() called by app");
        }
        if (!hasAutofillFeature()) {
            return;
        }
        synchronized (this.mLock) {
            cancelLocked();
        }
    }

    @GuardedBy("mLock")
    private synchronized void cancelLocked() {
        if (!this.mEnabled && !isActiveLocked()) {
            return;
        }
        cancelSessionLocked();
    }

    public synchronized void disableOwnedAutofillServices() {
        disableAutofillServices();
    }

    public void disableAutofillServices() {
        if (!hasAutofillFeature()) {
            return;
        }
        try {
            this.mService.disableOwnedAutofillServices(this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean hasEnabledAutofillServices() {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.isServiceEnabled(this.mContext.getUserId(), this.mContext.getPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ComponentName getAutofillServiceComponentName() {
        if (this.mService == null) {
            return null;
        }
        try {
            return this.mService.getAutofillServiceComponentName();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getUserDataId() {
        try {
            return this.mService.getUserDataId();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public UserData getUserData() {
        try {
            return this.mService.getUserData();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public void setUserData(UserData userData) {
        try {
            this.mService.setUserData(userData);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
        }
    }

    public boolean isFieldClassificationEnabled() {
        try {
            return this.mService.isFieldClassificationEnabled();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public String getDefaultFieldClassificationAlgorithm() {
        try {
            return this.mService.getDefaultFieldClassificationAlgorithm();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public List<String> getAvailableFieldClassificationAlgorithms() {
        try {
            String[] algorithms = this.mService.getAvailableFieldClassificationAlgorithms();
            return algorithms != null ? Arrays.asList(algorithms) : Collections.emptyList();
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public boolean isAutofillSupported() {
        if (this.mService == null) {
            return false;
        }
        try {
            return this.mService.isServiceSupported(this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized AutofillClient getClient() {
        AutofillClient client = this.mContext.getAutofillClient();
        if (client == null && Helper.sDebug) {
            Log.d(TAG, "No AutofillClient for " + this.mContext.getPackageName() + " on context " + this.mContext);
        }
        return client;
    }

    public synchronized boolean isAutofillUiShowing() {
        AutofillClient client = this.mContext.getAutofillClient();
        return client != null && client.autofillClientIsFillUiShowing();
    }

    public synchronized void onAuthenticationResult(int authenticationId, Intent data, View focusView) {
        if (!hasAutofillFeature()) {
            return;
        }
        if (Helper.sDebug) {
            Log.d(TAG, "onAuthenticationResult(): d=" + data);
        }
        synchronized (this.mLock) {
            if (isActiveLocked()) {
                if (!this.mOnInvisibleCalled && focusView != null && focusView.canNotifyAutofillEnterExitEvent()) {
                    notifyViewExitedLocked(focusView);
                    notifyViewEnteredLocked(focusView, 0);
                }
                if (data == null) {
                    return;
                }
                Parcelable result = data.getParcelableExtra(EXTRA_AUTHENTICATION_RESULT);
                Bundle responseData = new Bundle();
                responseData.putParcelable(EXTRA_AUTHENTICATION_RESULT, result);
                Bundle newClientState = data.getBundleExtra(EXTRA_CLIENT_STATE);
                if (newClientState != null) {
                    responseData.putBundle(EXTRA_CLIENT_STATE, newClientState);
                }
                try {
                    this.mService.setAuthenticationResult(responseData, this.mSessionId, authenticationId, this.mContext.getUserId());
                } catch (RemoteException e) {
                    Log.e(TAG, "Error delivering authentication result", e);
                }
            }
        }
    }

    public AutofillId getNextAutofillId() {
        AutofillClient client = getClient();
        if (client == null) {
            return null;
        }
        AutofillId id = client.autofillClientGetNextAutofillId();
        if (id == null && Helper.sDebug) {
            Log.d(TAG, "getNextAutofillId(): client " + client + " returned null");
        }
        return id;
    }

    private static synchronized AutofillId getAutofillId(View parent, int virtualId) {
        return new AutofillId(parent.getAutofillViewId(), virtualId);
    }

    @GuardedBy("mLock")
    private synchronized void startSessionLocked(AutofillId id, Rect bounds, AutofillValue value, int flags) {
        Rect rect;
        AutofillValue autofillValue;
        if (Helper.sVerbose) {
            StringBuilder sb = new StringBuilder();
            sb.append("startSessionLocked(): id=");
            sb.append(id);
            sb.append(", bounds=");
            rect = bounds;
            sb.append(rect);
            sb.append(", value=");
            autofillValue = value;
            sb.append(autofillValue);
            sb.append(", flags=");
            sb.append(flags);
            sb.append(", state=");
            sb.append(getStateAsStringLocked());
            sb.append(", compatMode=");
            sb.append(isCompatibilityModeEnabledLocked());
            sb.append(", enteredIds=");
            sb.append(this.mEnteredIds);
            Log.v(TAG, sb.toString());
        } else {
            rect = bounds;
            autofillValue = value;
        }
        if (this.mState != 0 && !isFinishedLocked() && (flags & 1) == 0) {
            if (Helper.sVerbose) {
                Log.v(TAG, "not automatically starting session for " + id + " on state " + getStateAsStringLocked() + " and flags " + flags);
                return;
            }
            return;
        }
        try {
            AutofillClient client = getClient();
            if (client == null) {
                return;
            }
            this.mSessionId = this.mService.startSession(client.autofillClientGetActivityToken(), this.mServiceClient.asBinder(), id, rect, autofillValue, this.mContext.getUserId(), this.mCallback != null, flags, client.autofillClientGetComponentName(), isCompatibilityModeEnabledLocked());
            if (this.mSessionId != Integer.MIN_VALUE) {
                this.mState = 1;
            }
            client.autofillClientResetableStateAvailable();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy("mLock")
    public synchronized void finishSessionLocked() {
        if (Helper.sVerbose) {
            Log.v(TAG, "finishSessionLocked(): " + getStateAsStringLocked());
        }
        if (isActiveLocked()) {
            try {
                this.mService.finishSession(this.mSessionId, this.mContext.getUserId());
                resetSessionLocked(true);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @GuardedBy("mLock")
    private synchronized void cancelSessionLocked() {
        if (Helper.sVerbose) {
            Log.v(TAG, "cancelSessionLocked(): " + getStateAsStringLocked());
        }
        if (isActiveLocked()) {
            try {
                this.mService.cancelSession(this.mSessionId, this.mContext.getUserId());
                resetSessionLocked(true);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @GuardedBy("mLock")
    private synchronized void resetSessionLocked(boolean resetEnteredIds) {
        this.mSessionId = Integer.MIN_VALUE;
        this.mState = 0;
        this.mTrackedViews = null;
        this.mFillableIds = null;
        this.mSaveTriggerId = null;
        this.mIdShownFillUi = null;
        if (resetEnteredIds) {
            this.mEnteredIds = null;
        }
    }

    @GuardedBy("mLock")
    private synchronized void updateSessionLocked(AutofillId id, Rect bounds, AutofillValue value, int action, int flags) {
        AutofillId autofillId;
        Rect rect;
        AutofillValue autofillValue;
        if (Helper.sVerbose) {
            StringBuilder sb = new StringBuilder();
            sb.append("updateSessionLocked(): id=");
            autofillId = id;
            sb.append(autofillId);
            sb.append(", bounds=");
            rect = bounds;
            sb.append(rect);
            sb.append(", value=");
            autofillValue = value;
            sb.append(autofillValue);
            sb.append(", action=");
            sb.append(action);
            sb.append(", flags=");
            sb.append(flags);
            Log.v(TAG, sb.toString());
        } else {
            autofillId = id;
            rect = bounds;
            autofillValue = value;
        }
        boolean restartIfNecessary = (flags & 1) != 0;
        try {
            if (restartIfNecessary) {
                AutofillClient client = getClient();
                if (client == null) {
                    return;
                }
                int newId = this.mService.updateOrRestartSession(client.autofillClientGetActivityToken(), this.mServiceClient.asBinder(), autofillId, rect, autofillValue, this.mContext.getUserId(), this.mCallback != null, flags, client.autofillClientGetComponentName(), this.mSessionId, action, isCompatibilityModeEnabledLocked());
                if (newId != this.mSessionId) {
                    if (Helper.sDebug) {
                        Log.d(TAG, "Session restarted: " + this.mSessionId + "=>" + newId);
                    }
                    this.mSessionId = newId;
                    this.mState = this.mSessionId == Integer.MIN_VALUE ? 0 : 1;
                    client.autofillClientResetableStateAvailable();
                }
                return;
            }
            this.mService.updateSession(this.mSessionId, id, bounds, value, action, flags, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @GuardedBy("mLock")
    private synchronized void ensureServiceClientAddedIfNeededLocked() {
        if (getClient() != null && this.mServiceClient == null) {
            this.mServiceClient = new AutofillManagerClient(this);
            try {
                final int userId = this.mContext.getUserId();
                int flags = this.mService.addClient(this.mServiceClient, userId);
                this.mEnabled = (flags & 1) != 0;
                Helper.sDebug = (flags & 2) != 0;
                Helper.sVerbose = (flags & 4) != 0;
                final IAutoFillManager service = this.mService;
                final IAutoFillManagerClient serviceClient = this.mServiceClient;
                this.mServiceClientCleaner = Cleaner.create(this, new Runnable() { // from class: android.view.autofill.-$$Lambda$AutofillManager$V76JiQu509LCUz3-ckpb-nB3JhA
                    @Override // java.lang.Runnable
                    public final void run() {
                        IAutoFillManager.this.removeClient(serviceClient, userId);
                    }
                });
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void registerCallback(AutofillCallback callback) {
        if (!hasAutofillFeature()) {
            return;
        }
        synchronized (this.mLock) {
            try {
                if (callback == null) {
                    return;
                }
                boolean hadCallback = this.mCallback != null;
                this.mCallback = callback;
                if (!hadCallback) {
                    try {
                        this.mService.setHasCallback(this.mSessionId, this.mContext.getUserId(), true);
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void unregisterCallback(AutofillCallback callback) {
        if (!hasAutofillFeature()) {
            return;
        }
        synchronized (this.mLock) {
            if (callback != null) {
                try {
                    if (this.mCallback != null && callback == this.mCallback) {
                        this.mCallback = null;
                        try {
                            this.mService.setHasCallback(this.mSessionId, this.mContext.getUserId(), false);
                        } catch (RemoteException e) {
                            throw e.rethrowFromSystemServer();
                        }
                    }
                } finally {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void requestShowFillUi(int sessionId, AutofillId id, int width, int height, Rect anchorBounds, IAutofillWindowPresenter presenter) {
        AutofillClient client;
        View anchor = findView(id);
        if (anchor == null) {
            return;
        }
        AutofillCallback callback = null;
        synchronized (this.mLock) {
            try {
                try {
                    if (this.mSessionId == sessionId && (client = getClient()) != null && client.autofillClientRequestShowFillUi(anchor, width, height, anchorBounds, presenter)) {
                        callback = this.mCallback;
                        this.mIdShownFillUi = id;
                    }
                    if (callback != null) {
                        if (id.isVirtual()) {
                            callback.onAutofillEvent(anchor, id.getVirtualChildId(), 1);
                        } else {
                            callback.onAutofillEvent(anchor, 1);
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void authenticate(int sessionId, int authenticationId, IntentSender intent, Intent fillInIntent) {
        AutofillClient client;
        synchronized (this.mLock) {
            if (sessionId == this.mSessionId && (client = getClient()) != null) {
                this.mOnInvisibleCalled = false;
                client.autofillClientAuthenticate(authenticationId, intent, fillInIntent);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchUnhandledKey(int sessionId, AutofillId id, KeyEvent keyEvent) {
        AutofillClient client;
        View anchor = findView(id);
        if (anchor == null) {
            return;
        }
        synchronized (this.mLock) {
            if (this.mSessionId == sessionId && (client = getClient()) != null) {
                client.autofillClientDispatchUnhandledKey(anchor, keyEvent);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setState(int flags) {
        if (Helper.sVerbose) {
            Log.v(TAG, "setState(" + flags + ")");
        }
        synchronized (this.mLock) {
            this.mEnabled = (flags & 1) != 0;
            if (!this.mEnabled || (flags & 2) != 0) {
                resetSessionLocked(true);
            }
            if ((flags & 4) != 0) {
                this.mServiceClient = null;
                if (this.mServiceClientCleaner != null) {
                    this.mServiceClientCleaner.clean();
                    this.mServiceClientCleaner = null;
                }
            }
        }
        Helper.sDebug = (flags & 8) != 0;
        Helper.sVerbose = (flags & 16) != 0;
    }

    private synchronized void setAutofilledIfValuesIs(View view, AutofillValue targetValue) {
        AutofillValue currentValue = view.getAutofillValue();
        if (Objects.equals(currentValue, targetValue)) {
            synchronized (this.mLock) {
                if (this.mLastAutofilledData == null) {
                    this.mLastAutofilledData = new ParcelableMap(1);
                }
                this.mLastAutofilledData.put(view.getAutofillId(), targetValue);
            }
            view.setAutofilled(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void autofill(int sessionId, List<AutofillId> ids, List<AutofillValue> values) {
        AutofillClient client;
        View[] views;
        synchronized (this.mLock) {
            try {
                if (sessionId != this.mSessionId) {
                    return;
                }
                AutofillClient client2 = getClient();
                if (client2 == null) {
                    return;
                }
                int itemCount = ids.size();
                ArrayMap<View, SparseArray<AutofillValue>> virtualValues = null;
                View[] views2 = client2.autofillClientFindViewsByAutofillIdTraversal(Helper.toArray(ids));
                ArrayList<AutofillId> failedIds = null;
                int numApplied = 0;
                int numApplied2 = 0;
                while (numApplied2 < itemCount) {
                    try {
                        AutofillId id = ids.get(numApplied2);
                        try {
                            AutofillValue value = values.get(numApplied2);
                            id.getViewId();
                            View view = views2[numApplied2];
                            if (view == null) {
                                client = client2;
                                StringBuilder sb = new StringBuilder();
                                views = views2;
                                sb.append("autofill(): no View with id ");
                                sb.append(id);
                                Log.d(TAG, sb.toString());
                                if (failedIds == null) {
                                    failedIds = new ArrayList<>();
                                }
                                failedIds.add(id);
                            } else {
                                client = client2;
                                views = views2;
                                if (id.isVirtual()) {
                                    if (virtualValues == null) {
                                        virtualValues = new ArrayMap<>(1);
                                    }
                                    SparseArray<AutofillValue> valuesByParent = virtualValues.get(view);
                                    if (valuesByParent == null) {
                                        valuesByParent = new SparseArray<>(5);
                                        virtualValues.put(view, valuesByParent);
                                    }
                                    valuesByParent.put(id.getVirtualChildId(), value);
                                } else {
                                    if (this.mLastAutofilledData == null) {
                                        this.mLastAutofilledData = new ParcelableMap(itemCount - numApplied2);
                                    }
                                    this.mLastAutofilledData.put(id, value);
                                    view.autofill(value);
                                    setAutofilledIfValuesIs(view, value);
                                    numApplied++;
                                }
                            }
                            numApplied2++;
                            client2 = client;
                            views2 = views;
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
                if (failedIds != null) {
                    if (Helper.sVerbose) {
                        Log.v(TAG, "autofill(): total failed views: " + failedIds);
                    }
                    try {
                        this.mService.setAutofillFailure(this.mSessionId, failedIds, this.mContext.getUserId());
                    } catch (RemoteException e) {
                        e.rethrowFromSystemServer();
                    }
                }
                if (virtualValues != null) {
                    int i = 0;
                    while (true) {
                        int i2 = i;
                        if (i2 >= virtualValues.size()) {
                            break;
                        }
                        View parent = virtualValues.keyAt(i2);
                        SparseArray<AutofillValue> childrenValues = virtualValues.valueAt(i2);
                        parent.autofill(childrenValues);
                        numApplied += childrenValues.size();
                        i = i2 + 1;
                    }
                }
                this.mMetricsLogger.write(newLog(MetricsProto.MetricsEvent.AUTOFILL_DATASET_APPLIED).addTaggedData(MetricsProto.MetricsEvent.FIELD_AUTOFILL_NUM_VALUES, Integer.valueOf(itemCount)).addTaggedData(MetricsProto.MetricsEvent.FIELD_AUTOFILL_NUM_VIEWS_FILLED, Integer.valueOf(numApplied)));
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    private synchronized LogMaker newLog(int category) {
        LogMaker log = new LogMaker(category).addTaggedData(MetricsProto.MetricsEvent.FIELD_AUTOFILL_SESSION_ID, Integer.valueOf(this.mSessionId));
        if (isCompatibilityModeEnabledLocked()) {
            log.addTaggedData(MetricsProto.MetricsEvent.FIELD_AUTOFILL_COMPAT_MODE, 1);
        }
        AutofillClient client = getClient();
        if (client == null) {
            log.setPackageName(this.mContext.getPackageName());
        } else {
            log.setComponentName(client.autofillClientGetComponentName());
        }
        return log;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setTrackedViews(int sessionId, AutofillId[] trackedIds, boolean saveOnAllViewsInvisible, boolean saveOnFinish, AutofillId[] fillableIds, AutofillId saveTriggerId) {
        synchronized (this.mLock) {
            if (this.mEnabled && this.mSessionId == sessionId) {
                if (saveOnAllViewsInvisible) {
                    this.mTrackedViews = new TrackedViews(trackedIds);
                } else {
                    this.mTrackedViews = null;
                }
                this.mSaveOnFinish = saveOnFinish;
                if (fillableIds != null) {
                    if (this.mFillableIds == null) {
                        this.mFillableIds = new ArraySet<>(fillableIds.length);
                    }
                    for (AutofillId id : fillableIds) {
                        this.mFillableIds.add(id);
                    }
                    if (Helper.sVerbose) {
                        Log.v(TAG, "setTrackedViews(): fillableIds=" + fillableIds + ", mFillableIds" + this.mFillableIds);
                    }
                }
                if (this.mSaveTriggerId != null && !this.mSaveTriggerId.equals(saveTriggerId)) {
                    setNotifyOnClickLocked(this.mSaveTriggerId, false);
                }
                if (saveTriggerId != null && !saveTriggerId.equals(this.mSaveTriggerId)) {
                    this.mSaveTriggerId = saveTriggerId;
                    setNotifyOnClickLocked(this.mSaveTriggerId, true);
                }
            }
        }
    }

    private synchronized void setNotifyOnClickLocked(AutofillId id, boolean notify) {
        View view = findView(id);
        if (view == null) {
            Log.w(TAG, "setNotifyOnClick(): invalid id: " + id);
            return;
        }
        view.setNotifyAutofillManagerOnClick(notify);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setSaveUiState(int sessionId, boolean shown) {
        if (Helper.sDebug) {
            Log.d(TAG, "setSaveUiState(" + sessionId + "): " + shown);
        }
        synchronized (this.mLock) {
            if (this.mSessionId != Integer.MIN_VALUE) {
                Log.w(TAG, "setSaveUiState(" + sessionId + ", " + shown + ") called on existing session " + this.mSessionId + "; cancelling it");
                cancelSessionLocked();
            }
            if (shown) {
                this.mSessionId = sessionId;
                this.mState = 3;
            } else {
                this.mSessionId = Integer.MIN_VALUE;
                this.mState = 0;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setSessionFinished(int newState) {
        synchronized (this.mLock) {
            if (Helper.sVerbose) {
                Log.v(TAG, "setSessionFinished(): from " + getStateAsStringLocked() + " to " + getStateAsString(newState));
            }
            if (newState == 5) {
                resetSessionLocked(true);
                this.mState = 0;
            } else {
                resetSessionLocked(false);
                this.mState = newState;
            }
        }
    }

    public synchronized void requestHideFillUi() {
        requestHideFillUi(this.mIdShownFillUi, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void requestHideFillUi(AutofillId id, boolean force) {
        AutofillClient client;
        View anchor = id == null ? null : findView(id);
        if (Helper.sVerbose) {
            Log.v(TAG, "requestHideFillUi(" + id + "): anchor = " + anchor);
        }
        if (anchor == null) {
            if (force && (client = getClient()) != null) {
                client.autofillClientRequestHideFillUi();
                return;
            }
            return;
        }
        requestHideFillUi(id, anchor);
    }

    private synchronized void requestHideFillUi(AutofillId id, View anchor) {
        AutofillCallback callback = null;
        synchronized (this.mLock) {
            AutofillClient client = getClient();
            if (client != null && client.autofillClientRequestHideFillUi()) {
                this.mIdShownFillUi = null;
                callback = this.mCallback;
            }
        }
        if (callback != null) {
            if (id.isVirtual()) {
                callback.onAutofillEvent(anchor, id.getVirtualChildId(), 2);
            } else {
                callback.onAutofillEvent(anchor, 2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void notifyNoFillUi(int sessionId, AutofillId id, int sessionFinishedState) {
        if (Helper.sVerbose) {
            Log.v(TAG, "notifyNoFillUi(): sessionId=" + sessionId + ", autofillId=" + id + ", sessionFinishedState=" + sessionFinishedState);
        }
        View anchor = findView(id);
        if (anchor == null) {
            return;
        }
        AutofillCallback callback = null;
        synchronized (this.mLock) {
            if (this.mSessionId == sessionId && getClient() != null) {
                callback = this.mCallback;
            }
        }
        if (callback != null) {
            if (id.isVirtual()) {
                callback.onAutofillEvent(anchor, id.getVirtualChildId(), 3);
            } else {
                callback.onAutofillEvent(anchor, 3);
            }
        }
        if (sessionFinishedState != 0) {
            setSessionFinished(sessionFinishedState);
        }
    }

    private synchronized View findView(AutofillId autofillId) {
        AutofillClient client = getClient();
        if (client != null) {
            return client.autofillClientFindViewByAutofillIdTraversal(autofillId);
        }
        return null;
    }

    public synchronized boolean hasAutofillFeature() {
        return this.mService != null;
    }

    public synchronized void onPendingSaveUi(int operation, IBinder token) {
        if (Helper.sVerbose) {
            Log.v(TAG, "onPendingSaveUi(" + operation + "): " + token);
        }
        synchronized (this.mLock) {
            try {
                this.mService.onPendingSaveUi(operation, token);
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
            }
        }
    }

    public synchronized void dump(String outerPrefix, PrintWriter pw) {
        pw.print(outerPrefix);
        pw.println("AutofillManager:");
        String pfx = outerPrefix + "  ";
        pw.print(pfx);
        pw.print("sessionId: ");
        pw.println(this.mSessionId);
        pw.print(pfx);
        pw.print("state: ");
        pw.println(getStateAsStringLocked());
        pw.print(pfx);
        pw.print("context: ");
        pw.println(this.mContext);
        AutofillClient client = getClient();
        if (client != null) {
            pw.print(pfx);
            pw.print("client: ");
            pw.print(client);
            pw.print(" (");
            pw.print(client.autofillClientGetActivityToken());
            pw.println(')');
        }
        pw.print(pfx);
        pw.print("enabled: ");
        pw.println(this.mEnabled);
        pw.print(pfx);
        pw.print("hasService: ");
        pw.println(this.mService != null);
        pw.print(pfx);
        pw.print("hasCallback: ");
        pw.println(this.mCallback != null);
        pw.print(pfx);
        pw.print("onInvisibleCalled ");
        pw.println(this.mOnInvisibleCalled);
        pw.print(pfx);
        pw.print("last autofilled data: ");
        pw.println(this.mLastAutofilledData);
        pw.print(pfx);
        pw.print("tracked views: ");
        if (this.mTrackedViews == null) {
            pw.println("null");
        } else {
            String pfx2 = pfx + "  ";
            pw.println();
            pw.print(pfx2);
            pw.print("visible:");
            pw.println(this.mTrackedViews.mVisibleTrackedIds);
            pw.print(pfx2);
            pw.print("invisible:");
            pw.println(this.mTrackedViews.mInvisibleTrackedIds);
        }
        pw.print(pfx);
        pw.print("fillable ids: ");
        pw.println(this.mFillableIds);
        pw.print(pfx);
        pw.print("entered ids: ");
        pw.println(this.mEnteredIds);
        pw.print(pfx);
        pw.print("save trigger id: ");
        pw.println(this.mSaveTriggerId);
        pw.print(pfx);
        pw.print("save on finish(): ");
        pw.println(this.mSaveOnFinish);
        pw.print(pfx);
        pw.print("compat mode enabled: ");
        synchronized (this.mLock) {
            if (this.mCompatibilityBridge != null) {
                String pfx22 = pfx + "  ";
                pw.println("true");
                pw.print(pfx22);
                pw.print("windowId: ");
                pw.println(this.mCompatibilityBridge.mFocusedWindowId);
                pw.print(pfx22);
                pw.print("nodeId: ");
                pw.println(this.mCompatibilityBridge.mFocusedNodeId);
                pw.print(pfx22);
                pw.print("virtualId: ");
                pw.println(AccessibilityNodeInfo.getVirtualDescendantId(this.mCompatibilityBridge.mFocusedNodeId));
                pw.print(pfx22);
                pw.print("focusedBounds: ");
                pw.println(this.mCompatibilityBridge.mFocusedBounds);
            } else {
                pw.println("false");
            }
        }
        pw.print(pfx);
        pw.print("debug: ");
        pw.print(Helper.sDebug);
        pw.print(" verbose: ");
        pw.println(Helper.sVerbose);
    }

    @GuardedBy("mLock")
    private synchronized String getStateAsStringLocked() {
        return getStateAsString(this.mState);
    }

    private static synchronized String getStateAsString(int state) {
        switch (state) {
            case 0:
                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
            case 1:
                return "ACTIVE";
            case 2:
                return "FINISHED";
            case 3:
                return "SHOWING_SAVE_UI";
            case 4:
                return "DISABLED_BY_SERVICE";
            case 5:
                return "UNKNOWN_COMPAT_MODE";
            default:
                return "INVALID:" + state;
        }
    }

    @GuardedBy("mLock")
    private synchronized boolean isActiveLocked() {
        return this.mState == 1;
    }

    @GuardedBy("mLock")
    private synchronized boolean isDisabledByServiceLocked() {
        return this.mState == 4;
    }

    @GuardedBy("mLock")
    private synchronized boolean isFinishedLocked() {
        return this.mState == 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void post(Runnable runnable) {
        AutofillClient client = getClient();
        if (client == null) {
            if (Helper.sVerbose) {
                Log.v(TAG, "ignoring post() because client is null");
                return;
            }
            return;
        }
        client.autofillClientRunOnUiThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class CompatibilityBridge implements AccessibilityManager.AccessibilityPolicy {
        @GuardedBy("mLock")
        AccessibilityServiceInfo mCompatServiceInfo;
        @GuardedBy("mLock")
        private final Rect mFocusedBounds = new Rect();
        @GuardedBy("mLock")
        private final Rect mTempBounds = new Rect();
        @GuardedBy("mLock")
        private int mFocusedWindowId = -1;
        @GuardedBy("mLock")
        private long mFocusedNodeId = AccessibilityNodeInfo.UNDEFINED_NODE_ID;

        CompatibilityBridge() {
            AccessibilityManager am = AccessibilityManager.getInstance(AutofillManager.this.mContext);
            am.setAccessibilityPolicy(this);
        }

        private synchronized AccessibilityServiceInfo getCompatServiceInfo() {
            synchronized (AutofillManager.this.mLock) {
                if (this.mCompatServiceInfo != null) {
                    return this.mCompatServiceInfo;
                }
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(ZenModeConfig.SYSTEM_AUTHORITY, "com.android.server.autofill.AutofillCompatAccessibilityService"));
                ResolveInfo resolveInfo = AutofillManager.this.mContext.getPackageManager().resolveService(intent, 1048704);
                try {
                    this.mCompatServiceInfo = new AccessibilityServiceInfo(resolveInfo, AutofillManager.this.mContext);
                    return this.mCompatServiceInfo;
                } catch (IOException | XmlPullParserException e) {
                    Log.e(AutofillManager.TAG, "Cannot find compat autofill service:" + intent);
                    throw new IllegalStateException("Cannot find compat autofill service");
                }
            }
        }

        @Override // android.view.accessibility.AccessibilityManager.AccessibilityPolicy
        public synchronized boolean isEnabled(boolean accessibilityEnabled) {
            return true;
        }

        @Override // android.view.accessibility.AccessibilityManager.AccessibilityPolicy
        public synchronized int getRelevantEventTypes(int relevantEventTypes) {
            return relevantEventTypes | 8 | 16 | 1 | 2048;
        }

        @Override // android.view.accessibility.AccessibilityManager.AccessibilityPolicy
        public synchronized List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(List<AccessibilityServiceInfo> installedServices) {
            if (installedServices == null) {
                installedServices = new ArrayList();
            }
            installedServices.add(getCompatServiceInfo());
            return installedServices;
        }

        @Override // android.view.accessibility.AccessibilityManager.AccessibilityPolicy
        public synchronized List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int feedbackTypeFlags, List<AccessibilityServiceInfo> enabledService) {
            if (enabledService == null) {
                enabledService = new ArrayList();
            }
            enabledService.add(getCompatServiceInfo());
            return enabledService;
        }

        @Override // android.view.accessibility.AccessibilityManager.AccessibilityPolicy
        public synchronized AccessibilityEvent onAccessibilityEvent(AccessibilityEvent event, boolean accessibilityEnabled, int relevantEventTypes) {
            AutofillClient client;
            int type = event.getEventType();
            if (Helper.sVerbose) {
                Log.v(AutofillManager.TAG, "onAccessibilityEvent(" + AccessibilityEvent.eventTypeToString(type) + "): virtualId=" + AccessibilityNodeInfo.getVirtualDescendantId(event.getSourceNodeId()) + ", client=" + AutofillManager.this.getClient());
            }
            if (type == 1) {
                synchronized (AutofillManager.this.mLock) {
                    notifyViewClicked(event.getWindowId(), event.getSourceNodeId());
                }
            } else if (type == 8) {
                synchronized (AutofillManager.this.mLock) {
                    if (this.mFocusedWindowId == event.getWindowId() && this.mFocusedNodeId == event.getSourceNodeId()) {
                        return event;
                    }
                    if (this.mFocusedWindowId != -1 && this.mFocusedNodeId != AccessibilityNodeInfo.UNDEFINED_NODE_ID) {
                        notifyViewExited(this.mFocusedWindowId, this.mFocusedNodeId);
                        this.mFocusedWindowId = -1;
                        this.mFocusedNodeId = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
                        this.mFocusedBounds.set(0, 0, 0, 0);
                    }
                    int windowId = event.getWindowId();
                    long nodeId = event.getSourceNodeId();
                    if (notifyViewEntered(windowId, nodeId, this.mFocusedBounds)) {
                        this.mFocusedWindowId = windowId;
                        this.mFocusedNodeId = nodeId;
                    }
                }
            } else if (type == 16) {
                synchronized (AutofillManager.this.mLock) {
                    if (this.mFocusedWindowId == event.getWindowId() && this.mFocusedNodeId == event.getSourceNodeId()) {
                        notifyValueChanged(event.getWindowId(), event.getSourceNodeId());
                    }
                }
            } else if (type == 2048 && (client = AutofillManager.this.getClient()) != null) {
                synchronized (AutofillManager.this.mLock) {
                    if (client.autofillClientIsFillUiShowing()) {
                        notifyViewEntered(this.mFocusedWindowId, this.mFocusedNodeId, this.mFocusedBounds);
                    }
                    updateTrackedViewsLocked();
                }
            }
            if (accessibilityEnabled) {
                return event;
            }
            return null;
        }

        private synchronized boolean notifyViewEntered(int windowId, long nodeId, Rect focusedBounds) {
            View view;
            AccessibilityNodeInfo node;
            int virtualId = AccessibilityNodeInfo.getVirtualDescendantId(nodeId);
            if (isVirtualNode(virtualId) && (view = findViewByAccessibilityId(windowId, nodeId)) != null && (node = findVirtualNodeByAccessibilityId(view, virtualId)) != null && node.isEditable()) {
                Rect newBounds = this.mTempBounds;
                node.getBoundsInScreen(newBounds);
                if (newBounds.equals(focusedBounds)) {
                    return false;
                }
                focusedBounds.set(newBounds);
                AutofillManager.this.notifyViewEntered(view, virtualId, newBounds);
                return true;
            }
            return false;
        }

        private synchronized void notifyViewExited(int windowId, long nodeId) {
            View view;
            int virtualId = AccessibilityNodeInfo.getVirtualDescendantId(nodeId);
            if (!isVirtualNode(virtualId) || (view = findViewByAccessibilityId(windowId, nodeId)) == null) {
                return;
            }
            AutofillManager.this.notifyViewExited(view, virtualId);
        }

        private synchronized void notifyValueChanged(int windowId, long nodeId) {
            View view;
            AccessibilityNodeInfo node;
            int virtualId = AccessibilityNodeInfo.getVirtualDescendantId(nodeId);
            if (!isVirtualNode(virtualId) || (view = findViewByAccessibilityId(windowId, nodeId)) == null || (node = findVirtualNodeByAccessibilityId(view, virtualId)) == null) {
                return;
            }
            AutofillManager.this.notifyValueChanged(view, virtualId, AutofillValue.forText(node.getText()));
        }

        private synchronized void notifyViewClicked(int windowId, long nodeId) {
            View view;
            int virtualId = AccessibilityNodeInfo.getVirtualDescendantId(nodeId);
            if (!isVirtualNode(virtualId) || (view = findViewByAccessibilityId(windowId, nodeId)) == null) {
                return;
            }
            AccessibilityNodeInfo node = findVirtualNodeByAccessibilityId(view, virtualId);
            if (node == null) {
                return;
            }
            AutofillManager.this.notifyViewClicked(view, virtualId);
        }

        @GuardedBy("mLock")
        private synchronized void updateTrackedViewsLocked() {
            if (AutofillManager.this.mTrackedViews != null) {
                AutofillManager.this.mTrackedViews.onVisibleForAutofillChangedLocked();
            }
        }

        private synchronized View findViewByAccessibilityId(int windowId, long nodeId) {
            AutofillClient client = AutofillManager.this.getClient();
            if (client == null) {
                return null;
            }
            int viewId = AccessibilityNodeInfo.getAccessibilityViewId(nodeId);
            return client.autofillClientFindViewByAccessibilityIdTraversal(viewId, windowId);
        }

        private synchronized AccessibilityNodeInfo findVirtualNodeByAccessibilityId(View view, int virtualId) {
            AccessibilityNodeProvider provider = view.getAccessibilityNodeProvider();
            if (provider == null) {
                return null;
            }
            return provider.createAccessibilityNodeInfo(virtualId);
        }

        private synchronized boolean isVirtualNode(int nodeId) {
            return (nodeId == -1 || nodeId == Integer.MAX_VALUE) ? false : true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class TrackedViews {
        private ArraySet<AutofillId> mInvisibleTrackedIds;
        private ArraySet<AutofillId> mVisibleTrackedIds;

        private synchronized <T> boolean isInSet(ArraySet<T> set, T value) {
            return set != null && set.contains(value);
        }

        private synchronized <T> ArraySet<T> addToSet(ArraySet<T> set, T valueToAdd) {
            if (set == null) {
                set = new ArraySet<>(1);
            }
            set.add(valueToAdd);
            return set;
        }

        private synchronized <T> ArraySet<T> removeFromSet(ArraySet<T> set, T valueToRemove) {
            if (set == null) {
                return null;
            }
            set.remove(valueToRemove);
            if (set.isEmpty()) {
                return null;
            }
            return set;
        }

        TrackedViews(AutofillId[] trackedIds) {
            boolean[] isVisible;
            AutofillClient client = AutofillManager.this.getClient();
            if (!ArrayUtils.isEmpty(trackedIds) && client != null) {
                if (client.autofillClientIsVisibleForAutofill()) {
                    if (Helper.sVerbose) {
                        Log.v(AutofillManager.TAG, "client is visible, check tracked ids");
                    }
                    isVisible = client.autofillClientGetViewVisibility(trackedIds);
                } else {
                    isVisible = new boolean[trackedIds.length];
                }
                int numIds = trackedIds.length;
                for (int i = 0; i < numIds; i++) {
                    AutofillId id = trackedIds[i];
                    if (isVisible[i]) {
                        this.mVisibleTrackedIds = addToSet(this.mVisibleTrackedIds, id);
                    } else {
                        this.mInvisibleTrackedIds = addToSet(this.mInvisibleTrackedIds, id);
                    }
                }
            }
            if (Helper.sVerbose) {
                Log.v(AutofillManager.TAG, "TrackedViews(trackedIds=" + Arrays.toString(trackedIds) + "):  mVisibleTrackedIds=" + this.mVisibleTrackedIds + " mInvisibleTrackedIds=" + this.mInvisibleTrackedIds);
            }
            if (this.mVisibleTrackedIds == null) {
                AutofillManager.this.finishSessionLocked();
            }
        }

        @GuardedBy("mLock")
        synchronized void notifyViewVisibilityChangedLocked(AutofillId id, boolean isVisible) {
            if (Helper.sDebug) {
                Log.d(AutofillManager.TAG, "notifyViewVisibilityChangedLocked(): id=" + id + " isVisible=" + isVisible);
            }
            if (AutofillManager.this.isClientVisibleForAutofillLocked()) {
                if (isVisible) {
                    if (isInSet(this.mInvisibleTrackedIds, id)) {
                        this.mInvisibleTrackedIds = removeFromSet(this.mInvisibleTrackedIds, id);
                        this.mVisibleTrackedIds = addToSet(this.mVisibleTrackedIds, id);
                    }
                } else if (isInSet(this.mVisibleTrackedIds, id)) {
                    this.mVisibleTrackedIds = removeFromSet(this.mVisibleTrackedIds, id);
                    this.mInvisibleTrackedIds = addToSet(this.mInvisibleTrackedIds, id);
                }
            }
            if (this.mVisibleTrackedIds == null) {
                if (Helper.sVerbose) {
                    Log.v(AutofillManager.TAG, "No more visible ids. Invisibile = " + this.mInvisibleTrackedIds);
                }
                AutofillManager.this.finishSessionLocked();
            }
        }

        @GuardedBy("mLock")
        synchronized void onVisibleForAutofillChangedLocked() {
            AutofillClient client = AutofillManager.this.getClient();
            ArraySet<AutofillId> updatedVisibleTrackedIds = null;
            ArraySet<AutofillId> updatedVisibleTrackedIds2 = null;
            if (client != null) {
                if (Helper.sVerbose) {
                    Log.v(AutofillManager.TAG, "onVisibleForAutofillChangedLocked(): inv= " + this.mInvisibleTrackedIds + " vis=" + this.mVisibleTrackedIds);
                }
                if (this.mInvisibleTrackedIds != null) {
                    ArrayList<AutofillId> orderedInvisibleIds = new ArrayList<>(this.mInvisibleTrackedIds);
                    boolean[] isVisible = client.autofillClientGetViewVisibility(Helper.toArray(orderedInvisibleIds));
                    int numInvisibleTrackedIds = orderedInvisibleIds.size();
                    ArraySet<AutofillId> updatedInvisibleTrackedIds = null;
                    ArraySet<AutofillId> updatedVisibleTrackedIds3 = null;
                    for (int i = 0; i < numInvisibleTrackedIds; i++) {
                        AutofillId id = orderedInvisibleIds.get(i);
                        if (isVisible[i]) {
                            updatedVisibleTrackedIds3 = addToSet(updatedVisibleTrackedIds3, id);
                            if (Helper.sDebug) {
                                Log.d(AutofillManager.TAG, "onVisibleForAutofill() " + id + " became visible");
                            }
                        } else {
                            updatedInvisibleTrackedIds = addToSet(updatedInvisibleTrackedIds, id);
                        }
                    }
                    updatedVisibleTrackedIds = updatedVisibleTrackedIds3;
                    updatedVisibleTrackedIds2 = updatedInvisibleTrackedIds;
                }
                if (this.mVisibleTrackedIds != null) {
                    ArrayList<AutofillId> orderedVisibleIds = new ArrayList<>(this.mVisibleTrackedIds);
                    boolean[] isVisible2 = client.autofillClientGetViewVisibility(Helper.toArray(orderedVisibleIds));
                    int numVisibleTrackedIds = orderedVisibleIds.size();
                    for (int i2 = 0; i2 < numVisibleTrackedIds; i2++) {
                        AutofillId id2 = orderedVisibleIds.get(i2);
                        if (isVisible2[i2]) {
                            updatedVisibleTrackedIds = addToSet(updatedVisibleTrackedIds, id2);
                        } else {
                            updatedVisibleTrackedIds2 = addToSet(updatedVisibleTrackedIds2, id2);
                            if (Helper.sDebug) {
                                Log.d(AutofillManager.TAG, "onVisibleForAutofill() " + id2 + " became invisible");
                            }
                        }
                    }
                }
                this.mInvisibleTrackedIds = updatedVisibleTrackedIds2;
                this.mVisibleTrackedIds = updatedVisibleTrackedIds;
            }
            if (this.mVisibleTrackedIds == null) {
                if (Helper.sVerbose) {
                    Log.v(AutofillManager.TAG, "onVisibleForAutofillChangedLocked(): no more visible ids");
                }
                AutofillManager.this.finishSessionLocked();
            }
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class AutofillCallback {
        public static final int EVENT_INPUT_HIDDEN = 2;
        public static final int EVENT_INPUT_SHOWN = 1;
        public static final int EVENT_INPUT_UNAVAILABLE = 3;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes2.dex */
        public @interface AutofillEventType {
        }

        public void onAutofillEvent(View view, int event) {
        }

        public void onAutofillEvent(View view, int virtualId, int event) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class AutofillManagerClient extends IAutoFillManagerClient.Stub {
        private final WeakReference<AutofillManager> mAfm;

        synchronized AutofillManagerClient(AutofillManager autofillManager) {
            this.mAfm = new WeakReference<>(autofillManager);
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public synchronized void setState(final int flags) {
            final AutofillManager afm = this.mAfm.get();
            if (afm != null) {
                afm.post(new Runnable() { // from class: android.view.autofill.-$$Lambda$AutofillManager$AutofillManagerClient$qH36EJk2Hkdja9ZZmTxqYPyr0YA
                    @Override // java.lang.Runnable
                    public final void run() {
                        AutofillManager.this.setState(flags);
                    }
                });
            }
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public synchronized void autofill(final int sessionId, final List<AutofillId> ids, final List<AutofillValue> values) {
            final AutofillManager afm = this.mAfm.get();
            if (afm != null) {
                afm.post(new Runnable() { // from class: android.view.autofill.-$$Lambda$AutofillManager$AutofillManagerClient$1jAzMluMSJksx55SMUQn4BKB2Ng
                    @Override // java.lang.Runnable
                    public final void run() {
                        AutofillManager.this.autofill(sessionId, ids, values);
                    }
                });
            }
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public synchronized void authenticate(final int sessionId, final int authenticationId, final IntentSender intent, final Intent fillInIntent) {
            final AutofillManager afm = this.mAfm.get();
            if (afm != null) {
                afm.post(new Runnable() { // from class: android.view.autofill.-$$Lambda$AutofillManager$AutofillManagerClient$qyxZ4PACUgHFGSvMBHzgwjJ3yns
                    @Override // java.lang.Runnable
                    public final void run() {
                        AutofillManager.this.authenticate(sessionId, authenticationId, intent, fillInIntent);
                    }
                });
            }
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public synchronized void requestShowFillUi(final int sessionId, final AutofillId id, final int width, final int height, final Rect anchorBounds, final IAutofillWindowPresenter presenter) {
            final AutofillManager afm = this.mAfm.get();
            if (afm != null) {
                afm.post(new Runnable() { // from class: android.view.autofill.-$$Lambda$AutofillManager$AutofillManagerClient$kRL9XILLc2XNr90gxVDACLzcyqc
                    @Override // java.lang.Runnable
                    public final void run() {
                        AutofillManager.this.requestShowFillUi(sessionId, id, width, height, anchorBounds, presenter);
                    }
                });
            }
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public synchronized void requestHideFillUi(int sessionId, final AutofillId id) {
            final AutofillManager afm = this.mAfm.get();
            if (afm != null) {
                afm.post(new Runnable() { // from class: android.view.autofill.-$$Lambda$AutofillManager$AutofillManagerClient$dCTetwfU0gT1ZrSzZGZiGStXlOY
                    @Override // java.lang.Runnable
                    public final void run() {
                        AutofillManager.this.requestHideFillUi(id, false);
                    }
                });
            }
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public synchronized void notifyNoFillUi(final int sessionId, final AutofillId id, final int sessionFinishedState) {
            final AutofillManager afm = this.mAfm.get();
            if (afm != null) {
                afm.post(new Runnable() { // from class: android.view.autofill.-$$Lambda$AutofillManager$AutofillManagerClient$K79QnIPRaZuikYDQdsLcIUBhqiI
                    @Override // java.lang.Runnable
                    public final void run() {
                        AutofillManager.this.notifyNoFillUi(sessionId, id, sessionFinishedState);
                    }
                });
            }
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public synchronized void dispatchUnhandledKey(final int sessionId, final AutofillId id, final KeyEvent fullScreen) {
            final AutofillManager afm = this.mAfm.get();
            if (afm != null) {
                afm.post(new Runnable() { // from class: android.view.autofill.-$$Lambda$AutofillManager$AutofillManagerClient$xqXjXW0fvc8JdYR5fgGKw9lJc3I
                    @Override // java.lang.Runnable
                    public final void run() {
                        AutofillManager.this.dispatchUnhandledKey(sessionId, id, fullScreen);
                    }
                });
            }
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public synchronized void startIntentSender(final IntentSender intentSender, final Intent intent) {
            final AutofillManager afm = this.mAfm.get();
            if (afm != null) {
                afm.post(new Runnable() { // from class: android.view.autofill.-$$Lambda$AutofillManager$AutofillManagerClient$pM5e3ez5KTBdZt4d8qLEERBUSiU
                    @Override // java.lang.Runnable
                    public final void run() {
                        AutofillManager.AutofillManagerClient.lambda$startIntentSender$7(AutofillManager.this, intentSender, intent);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$startIntentSender$7(AutofillManager afm, IntentSender intentSender, Intent intent) {
            try {
                afm.mContext.startIntentSender(intentSender, intent, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.e(AutofillManager.TAG, "startIntentSender() failed for intent:" + intentSender, e);
            }
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public synchronized void setTrackedViews(final int sessionId, final AutofillId[] ids, final boolean saveOnAllViewsInvisible, final boolean saveOnFinish, final AutofillId[] fillableIds, final AutofillId saveTriggerId) {
            final AutofillManager afm = this.mAfm.get();
            if (afm != null) {
                afm.post(new Runnable() { // from class: android.view.autofill.-$$Lambda$AutofillManager$AutofillManagerClient$BPlC2x7GLNHFS92rPUSzbcpFhUc
                    @Override // java.lang.Runnable
                    public final void run() {
                        AutofillManager.this.setTrackedViews(sessionId, ids, saveOnAllViewsInvisible, saveOnFinish, fillableIds, saveTriggerId);
                    }
                });
            }
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public synchronized void setSaveUiState(final int sessionId, final boolean shown) {
            final AutofillManager afm = this.mAfm.get();
            if (afm != null) {
                afm.post(new Runnable() { // from class: android.view.autofill.-$$Lambda$AutofillManager$AutofillManagerClient$QIW-100CKwHzdHffwaus9KOEHCA
                    @Override // java.lang.Runnable
                    public final void run() {
                        AutofillManager.this.setSaveUiState(sessionId, shown);
                    }
                });
            }
        }

        @Override // android.view.autofill.IAutoFillManagerClient
        public synchronized void setSessionFinished(final int newState) {
            final AutofillManager afm = this.mAfm.get();
            if (afm != null) {
                afm.post(new Runnable() { // from class: android.view.autofill.-$$Lambda$AutofillManager$AutofillManagerClient$V-s28jF4_S72cRk4llkGpwbJnmk
                    @Override // java.lang.Runnable
                    public final void run() {
                        AutofillManager.this.setSessionFinished(newState);
                    }
                });
            }
        }
    }
}
