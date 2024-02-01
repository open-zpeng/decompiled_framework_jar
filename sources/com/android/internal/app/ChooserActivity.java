package com.android.internal.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;
import android.service.chooser.IChooserTargetResult;
import android.service.chooser.IChooserTargetService;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Space;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.ResolverActivity;
import com.android.internal.logging.MetricsLogger;
import com.google.android.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/* loaded from: classes3.dex */
public class ChooserActivity extends ResolverActivity {
    private static final float CALLER_TARGET_SCORE_BOOST = 900.0f;
    private static final int CHOOSER_TARGET_SERVICE_RESULT = 1;
    private static final int CHOOSER_TARGET_SERVICE_WATCHDOG_TIMEOUT = 2;
    private static final boolean DEBUG = false;
    public static final String EXTRA_PRIVATE_RETAIN_IN_ON_STOP = "com.android.internal.app.ChooserActivity.EXTRA_PRIVATE_RETAIN_IN_ON_STOP";
    private static final String PINNED_SHARED_PREFS_NAME = "chooser_pin_settings";
    private static final float PINNED_TARGET_SCORE_BOOST = 1000.0f;
    private static final int QUERY_TARGET_SERVICE_LIMIT = 5;
    private static final String TAG = "ChooserActivity";
    private static final String TARGET_DETAILS_FRAGMENT_TAG = "targetDetailsFragment";
    private static final int WATCHDOG_TIMEOUT_MILLIS = 2000;
    private ChooserTarget[] mCallerChooserTargets;
    private ChooserListAdapter mChooserListAdapter;
    private ChooserRowAdapter mChooserRowAdapter;
    private long mChooserShownTime;
    private IntentSender mChosenComponentSender;
    private ComponentName[] mFilteredComponentNames;
    protected boolean mIsSuccessfullySelected;
    private SharedPreferences mPinnedSharedPrefs;
    private Intent mReferrerFillInIntent;
    private IntentSender mRefinementIntentSender;
    private RefinementResultReceiver mRefinementResultReceiver;
    private Bundle mReplacementExtras;
    private final List<ChooserTargetServiceConnection> mServiceConnections = new ArrayList();
    private final Handler mChooserHandler = new Handler() { // from class: com.android.internal.app.ChooserActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (!ChooserActivity.this.isDestroyed()) {
                        ServiceResultInfo sri = (ServiceResultInfo) msg.obj;
                        if (!ChooserActivity.this.mServiceConnections.contains(sri.connection)) {
                            Log.w(ChooserActivity.TAG, "ChooserTargetServiceConnection " + sri.connection + " returned after being removed from active connections. Have you considered returning results faster?");
                            return;
                        }
                        if (sri.resultTargets != null) {
                            ChooserActivity.this.mChooserListAdapter.addServiceResults(sri.originalTarget, sri.resultTargets);
                        }
                        ChooserActivity.this.unbindService(sri.connection);
                        sri.connection.destroy();
                        ChooserActivity.this.mServiceConnections.remove(sri.connection);
                        if (ChooserActivity.this.mServiceConnections.isEmpty()) {
                            ChooserActivity.this.mChooserHandler.removeMessages(2);
                            ChooserActivity.this.sendVoiceChoicesIfNeeded();
                            ChooserActivity.this.mChooserListAdapter.setShowServiceTargets(true);
                            return;
                        }
                        return;
                    }
                    return;
                case 2:
                    ChooserActivity.this.unbindRemainingServices();
                    ChooserActivity.this.sendVoiceChoicesIfNeeded();
                    ChooserActivity.this.mChooserListAdapter.setShowServiceTargets(true);
                    return;
                default:
                    super.handleMessage(msg);
                    return;
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.internal.app.ResolverActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        Intent target;
        boolean offset;
        long intentReceivedTime = System.currentTimeMillis();
        this.mIsSuccessfullySelected = false;
        Intent intent = getIntent();
        Parcelable targetParcelable = intent.getParcelableExtra(Intent.EXTRA_INTENT);
        if (!(targetParcelable instanceof Intent)) {
            Log.w(TAG, "Target is not an intent: " + targetParcelable);
            finish();
            super.onCreate(null);
            return;
        }
        Intent target2 = (Intent) targetParcelable;
        if (target2 != null) {
            modifyTargetIntent(target2);
        }
        Parcelable[] targetsParcelable = intent.getParcelableArrayExtra(Intent.EXTRA_ALTERNATE_INTENTS);
        if (targetsParcelable != null) {
            if (target2 != null) {
                offset = false;
            } else {
                offset = true;
            }
            Intent[] additionalTargets = new Intent[offset ? targetsParcelable.length - 1 : targetsParcelable.length];
            Intent target3 = target2;
            for (int i = 0; i < targetsParcelable.length; i++) {
                if (!(targetsParcelable[i] instanceof Intent)) {
                    Log.w(TAG, "EXTRA_ALTERNATE_INTENTS array entry #" + i + " is not an Intent: " + targetsParcelable[i]);
                    finish();
                    super.onCreate(null);
                    return;
                }
                Intent additionalTarget = (Intent) targetsParcelable[i];
                if (i == 0 && target3 == null) {
                    target3 = additionalTarget;
                    modifyTargetIntent(target3);
                } else {
                    additionalTargets[offset ? i - 1 : i] = additionalTarget;
                    modifyTargetIntent(additionalTarget);
                }
            }
            setAdditionalTargets(additionalTargets);
            target = target3;
        } else {
            target = target2;
        }
        this.mReplacementExtras = intent.getBundleExtra(Intent.EXTRA_REPLACEMENT_EXTRAS);
        CharSequence title = intent.getCharSequenceExtra(Intent.EXTRA_TITLE);
        int defaultTitleRes = 0;
        if (title == null) {
            defaultTitleRes = R.string.chooseActivity;
        }
        int defaultTitleRes2 = defaultTitleRes;
        Parcelable[] pa = intent.getParcelableArrayExtra(Intent.EXTRA_INITIAL_INTENTS);
        Intent[] initialIntents = null;
        if (pa != null) {
            initialIntents = new Intent[pa.length];
            for (int i2 = 0; i2 < pa.length; i2++) {
                if (!(pa[i2] instanceof Intent)) {
                    Log.w(TAG, "Initial intent #" + i2 + " not an Intent: " + pa[i2]);
                    finish();
                    super.onCreate(null);
                    return;
                }
                Intent in = (Intent) pa[i2];
                modifyTargetIntent(in);
                initialIntents[i2] = in;
            }
        }
        Intent[] initialIntents2 = initialIntents;
        this.mReferrerFillInIntent = new Intent().putExtra(Intent.EXTRA_REFERRER, getReferrer());
        this.mChosenComponentSender = (IntentSender) intent.getParcelableExtra(Intent.EXTRA_CHOSEN_COMPONENT_INTENT_SENDER);
        this.mRefinementIntentSender = (IntentSender) intent.getParcelableExtra(Intent.EXTRA_CHOOSER_REFINEMENT_INTENT_SENDER);
        setSafeForwardingMode(true);
        Parcelable[] pa2 = intent.getParcelableArrayExtra(Intent.EXTRA_EXCLUDE_COMPONENTS);
        if (pa2 != null) {
            ComponentName[] names = new ComponentName[pa2.length];
            int i3 = 0;
            while (true) {
                if (i3 >= pa2.length) {
                    break;
                } else if (!(pa2[i3] instanceof ComponentName)) {
                    Log.w(TAG, "Filtered component #" + i3 + " not a ComponentName: " + pa2[i3]);
                    names = null;
                    break;
                } else {
                    names[i3] = (ComponentName) pa2[i3];
                    i3++;
                }
            }
            this.mFilteredComponentNames = names;
        }
        Parcelable[] pa3 = intent.getParcelableArrayExtra(Intent.EXTRA_CHOOSER_TARGETS);
        if (pa3 != null) {
            ChooserTarget[] targets = new ChooserTarget[pa3.length];
            int i4 = 0;
            while (true) {
                if (i4 >= pa3.length) {
                    break;
                } else if (!(pa3[i4] instanceof ChooserTarget)) {
                    Log.w(TAG, "Chooser target #" + i4 + " not a ChooserTarget: " + pa3[i4]);
                    targets = null;
                    break;
                } else {
                    targets[i4] = (ChooserTarget) pa3[i4];
                    i4++;
                }
            }
            this.mCallerChooserTargets = targets;
        }
        this.mPinnedSharedPrefs = getPinnedSharedPrefs(this);
        setRetainInOnStop(intent.getBooleanExtra(EXTRA_PRIVATE_RETAIN_IN_ON_STOP, false));
        super.onCreate(savedInstanceState, target, title, defaultTitleRes2, initialIntents2, null, false);
        MetricsLogger.action(this, 214);
        this.mChooserShownTime = System.currentTimeMillis();
        long systemCost = this.mChooserShownTime - intentReceivedTime;
        MetricsLogger.histogram(null, "system_cost_for_smart_sharing", (int) systemCost);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SharedPreferences getPinnedSharedPrefs(Context context) {
        File prefsFile = new File(new File(Environment.getDataUserCePackageDirectory(StorageManager.UUID_PRIVATE_INTERNAL, context.getUserId(), context.getPackageName()), "shared_prefs"), "chooser_pin_settings.xml");
        return context.getSharedPreferences(prefsFile, 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.internal.app.ResolverActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        if (this.mRefinementResultReceiver != null) {
            this.mRefinementResultReceiver.destroy();
            this.mRefinementResultReceiver = null;
        }
        unbindRemainingServices();
        this.mChooserHandler.removeMessages(1);
    }

    @Override // com.android.internal.app.ResolverActivity
    public Intent getReplacementIntent(ActivityInfo aInfo, Intent defIntent) {
        Bundle replExtras;
        Intent result = defIntent;
        if (this.mReplacementExtras != null && (replExtras = this.mReplacementExtras.getBundle(aInfo.packageName)) != null) {
            result = new Intent(defIntent);
            result.putExtras(replExtras);
        }
        if (aInfo.name.equals(IntentForwarderActivity.FORWARD_INTENT_TO_PARENT) || aInfo.name.equals(IntentForwarderActivity.FORWARD_INTENT_TO_MANAGED_PROFILE)) {
            Intent result2 = Intent.createChooser(result, getIntent().getCharSequenceExtra(Intent.EXTRA_TITLE));
            result2.putExtra(Intent.EXTRA_AUTO_LAUNCH_SINGLE_CHOICE, false);
            return result2;
        }
        return result;
    }

    @Override // com.android.internal.app.ResolverActivity
    public void onActivityStarted(ResolverActivity.TargetInfo cti) {
        ComponentName target;
        if (this.mChosenComponentSender != null && (target = cti.getResolvedComponentName()) != null) {
            Intent fillIn = new Intent().putExtra(Intent.EXTRA_CHOSEN_COMPONENT, target);
            try {
                this.mChosenComponentSender.sendIntent(this, -1, fillIn, null, null);
            } catch (IntentSender.SendIntentException e) {
                Slog.e(TAG, "Unable to launch supplied IntentSender to report the chosen component: " + e);
            }
        }
    }

    @Override // com.android.internal.app.ResolverActivity
    public void onPrepareAdapterView(AbsListView adapterView, ResolverActivity.ResolveListAdapter adapter) {
        ListView listView = adapterView instanceof ListView ? (ListView) adapterView : null;
        this.mChooserListAdapter = (ChooserListAdapter) adapter;
        if (this.mCallerChooserTargets != null && this.mCallerChooserTargets.length > 0) {
            this.mChooserListAdapter.addServiceResults(null, Lists.newArrayList(this.mCallerChooserTargets));
        }
        this.mChooserRowAdapter = new ChooserRowAdapter(this.mChooserListAdapter);
        this.mChooserRowAdapter.registerDataSetObserver(new OffsetDataSetObserver(adapterView));
        adapterView.setAdapter((ListAdapter) this.mChooserRowAdapter);
        if (listView != null) {
            listView.setItemsCanFocus(true);
        }
    }

    @Override // com.android.internal.app.ResolverActivity
    public int getLayoutResource() {
        return R.layout.chooser_grid;
    }

    @Override // com.android.internal.app.ResolverActivity
    public boolean shouldGetActivityMetadata() {
        return true;
    }

    @Override // com.android.internal.app.ResolverActivity
    public boolean shouldAutoLaunchSingleChoice(ResolverActivity.TargetInfo target) {
        return getIntent().getBooleanExtra(Intent.EXTRA_AUTO_LAUNCH_SINGLE_CHOICE, super.shouldAutoLaunchSingleChoice(target));
    }

    @Override // com.android.internal.app.ResolverActivity
    public void showTargetDetails(ResolveInfo ri) {
        if (ri == null) {
            return;
        }
        ComponentName name = ri.activityInfo.getComponentName();
        boolean pinned = this.mPinnedSharedPrefs.getBoolean(name.flattenToString(), false);
        ResolverTargetActionsDialogFragment f = new ResolverTargetActionsDialogFragment(ri.loadLabel(getPackageManager()), name, pinned);
        f.show(getFragmentManager(), TARGET_DETAILS_FRAGMENT_TAG);
    }

    private void modifyTargetIntent(Intent in) {
        String action = in.getAction();
        if (Intent.ACTION_SEND.equals(action) || Intent.ACTION_SEND_MULTIPLE.equals(action)) {
            in.addFlags(134742016);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.internal.app.ResolverActivity
    public boolean onTargetSelected(ResolverActivity.TargetInfo target, boolean alwaysCheck) {
        if (this.mRefinementIntentSender != null) {
            Intent fillIn = new Intent();
            List<Intent> sourceIntents = target.getAllSourceIntents();
            if (!sourceIntents.isEmpty()) {
                fillIn.putExtra(Intent.EXTRA_INTENT, sourceIntents.get(0));
                if (sourceIntents.size() > 1) {
                    Intent[] alts = new Intent[sourceIntents.size() - 1];
                    int N = sourceIntents.size();
                    for (int i = 1; i < N; i++) {
                        alts[i - 1] = sourceIntents.get(i);
                    }
                    fillIn.putExtra(Intent.EXTRA_ALTERNATE_INTENTS, alts);
                }
                if (this.mRefinementResultReceiver != null) {
                    this.mRefinementResultReceiver.destroy();
                }
                this.mRefinementResultReceiver = new RefinementResultReceiver(this, target, null);
                fillIn.putExtra(Intent.EXTRA_RESULT_RECEIVER, this.mRefinementResultReceiver);
                try {
                    this.mRefinementIntentSender.sendIntent(this, 0, fillIn, null, null);
                    return false;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Refinement IntentSender failed to send", e);
                }
            }
        }
        updateModelAndChooserCounts(target);
        return super.onTargetSelected(target, alwaysCheck);
    }

    @Override // com.android.internal.app.ResolverActivity
    public void startSelected(int which, boolean always, boolean filtered) {
        long selectionCost = System.currentTimeMillis() - this.mChooserShownTime;
        super.startSelected(which, always, filtered);
        if (this.mChooserListAdapter != null) {
            int cat = 0;
            int value = which;
            switch (this.mChooserListAdapter.getPositionTargetType(which)) {
                case 0:
                    cat = 215;
                    break;
                case 1:
                    cat = 216;
                    value -= this.mChooserListAdapter.getCallerTargetCount();
                    break;
                case 2:
                    cat = 217;
                    value -= this.mChooserListAdapter.getCallerTargetCount() + this.mChooserListAdapter.getServiceTargetCount();
                    break;
            }
            if (cat != 0) {
                MetricsLogger.action(this, cat, value);
            }
            if (this.mIsSuccessfullySelected) {
                MetricsLogger.histogram(null, "user_selection_cost_for_smart_sharing", (int) selectionCost);
                MetricsLogger.histogram(null, "app_position_for_smart_sharing", value);
            }
        }
    }

    void queryTargetServices(ChooserListAdapter adapter) {
        String serviceName;
        PackageManager pm = getPackageManager();
        int targetsToQuery = 0;
        int N = adapter.getDisplayResolveInfoCount();
        for (int i = 0; i < N; i++) {
            ResolverActivity.DisplayResolveInfo dri = adapter.getDisplayResolveInfo(i);
            if (adapter.getScore(dri) != 0.0f) {
                ActivityInfo ai = dri.getResolveInfo().activityInfo;
                Bundle md = ai.metaData;
                if (md != null) {
                    serviceName = convertServiceName(ai.packageName, md.getString(ChooserTargetService.META_DATA_NAME));
                } else {
                    serviceName = null;
                }
                if (serviceName != null) {
                    ComponentName serviceComponent = new ComponentName(ai.packageName, serviceName);
                    Intent serviceIntent = new Intent(ChooserTargetService.SERVICE_INTERFACE).setComponent(serviceComponent);
                    try {
                        String perm = pm.getServiceInfo(serviceComponent, 0).permission;
                        if (!"android.permission.BIND_CHOOSER_TARGET_SERVICE".equals(perm)) {
                            Log.w(TAG, "ChooserTargetService " + serviceComponent + " does not require permission android.permission.BIND_CHOOSER_TARGET_SERVICE - this service will not be queried for ChooserTargets. add android:permission=\"android.permission.BIND_CHOOSER_TARGET_SERVICE\" to the <service> tag for " + serviceComponent + " in the manifest.");
                        } else {
                            ChooserTargetServiceConnection conn = new ChooserTargetServiceConnection(this, dri);
                            if (bindServiceAsUser(serviceIntent, conn, 5, Process.myUserHandle())) {
                                this.mServiceConnections.add(conn);
                                targetsToQuery++;
                            }
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.e(TAG, "Could not look up service " + serviceComponent + "; component name not found");
                    }
                }
                if (targetsToQuery >= 5) {
                    break;
                }
            }
        }
        if (!this.mServiceConnections.isEmpty()) {
            this.mChooserHandler.sendEmptyMessageDelayed(2, SurfaceView.SurfaceViewFactory.BACKGROUND_TRANSACTION_DELAY);
        } else {
            sendVoiceChoicesIfNeeded();
        }
    }

    private String convertServiceName(String packageName, String serviceName) {
        String fullName = null;
        if (TextUtils.isEmpty(serviceName)) {
            return null;
        }
        if (serviceName.startsWith(".")) {
            fullName = packageName + serviceName;
        } else if (serviceName.indexOf(46) >= 0) {
            fullName = serviceName;
        }
        return fullName;
    }

    void unbindRemainingServices() {
        int N = this.mServiceConnections.size();
        for (int i = 0; i < N; i++) {
            ChooserTargetServiceConnection conn = this.mServiceConnections.get(i);
            unbindService(conn);
            conn.destroy();
        }
        this.mServiceConnections.clear();
        this.mChooserHandler.removeMessages(2);
    }

    @Override // com.android.internal.app.ResolverActivity
    public void onSetupVoiceInteraction() {
    }

    void updateModelAndChooserCounts(ResolverActivity.TargetInfo info) {
        if (info != null) {
            ResolveInfo ri = info.getResolveInfo();
            Intent targetIntent = getTargetIntent();
            if (ri != null && ri.activityInfo != null && targetIntent != null && this.mAdapter != null) {
                this.mAdapter.updateModel(info.getResolvedComponentName());
                this.mAdapter.updateChooserCounts(ri.activityInfo.packageName, getUserId(), targetIntent.getAction());
            }
        }
        this.mIsSuccessfullySelected = true;
    }

    void onRefinementResult(ResolverActivity.TargetInfo selectedTarget, Intent matchingIntent) {
        if (this.mRefinementResultReceiver != null) {
            this.mRefinementResultReceiver.destroy();
            this.mRefinementResultReceiver = null;
        }
        if (selectedTarget == null) {
            Log.e(TAG, "Refinement result intent did not match any known targets; canceling");
        } else if (!checkTargetSourceIntent(selectedTarget, matchingIntent)) {
            Log.e(TAG, "onRefinementResult: Selected target " + selectedTarget + " cannot match refined source intent " + matchingIntent);
        } else {
            ResolverActivity.TargetInfo clonedTarget = selectedTarget.cloneFilledIn(matchingIntent, 0);
            if (super.onTargetSelected(clonedTarget, false)) {
                updateModelAndChooserCounts(clonedTarget);
                finish();
                return;
            }
        }
        onRefinementCanceled();
    }

    void onRefinementCanceled() {
        if (this.mRefinementResultReceiver != null) {
            this.mRefinementResultReceiver.destroy();
            this.mRefinementResultReceiver = null;
        }
        finish();
    }

    boolean checkTargetSourceIntent(ResolverActivity.TargetInfo target, Intent matchingIntent) {
        List<Intent> targetIntents = target.getAllSourceIntents();
        int N = targetIntents.size();
        for (int i = 0; i < N; i++) {
            Intent targetIntent = targetIntents.get(i);
            if (targetIntent.filterEquals(matchingIntent)) {
                return true;
            }
        }
        return false;
    }

    void filterServiceTargets(String packageName, List<ChooserTarget> targets) {
        if (targets == null) {
            return;
        }
        PackageManager pm = getPackageManager();
        for (int i = targets.size() - 1; i >= 0; i--) {
            ChooserTarget target = targets.get(i);
            ComponentName targetName = target.getComponentName();
            if (packageName == null || !packageName.equals(targetName.getPackageName())) {
                boolean remove = false;
                try {
                    ActivityInfo ai = pm.getActivityInfo(targetName, 0);
                    if (!ai.exported || ai.permission != null) {
                        remove = true;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, "Target " + target + " returned by " + packageName + " component not found");
                    remove = true;
                }
                if (remove) {
                    targets.remove(i);
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public class ChooserListController extends ResolverListController {
        public ChooserListController(Context context, PackageManager pm, Intent targetIntent, String referrerPackageName, int launchedFromUid) {
            super(context, pm, targetIntent, referrerPackageName, launchedFromUid);
        }

        @Override // com.android.internal.app.ResolverListController
        boolean isComponentPinned(ComponentName name) {
            return ChooserActivity.this.mPinnedSharedPrefs.getBoolean(name.flattenToString(), false);
        }

        @Override // com.android.internal.app.ResolverListController
        boolean isComponentFiltered(ComponentName name) {
            ComponentName[] componentNameArr;
            if (ChooserActivity.this.mFilteredComponentNames == null) {
                return false;
            }
            for (ComponentName filteredComponentName : ChooserActivity.this.mFilteredComponentNames) {
                if (name.equals(filteredComponentName)) {
                    return true;
                }
            }
            return false;
        }

        @Override // com.android.internal.app.ResolverListController
        public float getScore(ResolverActivity.DisplayResolveInfo target) {
            if (target == null) {
                return ChooserActivity.CALLER_TARGET_SCORE_BOOST;
            }
            float score = super.getScore(target);
            if (target.isPinned()) {
                return score + ChooserActivity.PINNED_TARGET_SCORE_BOOST;
            }
            return score;
        }
    }

    @Override // com.android.internal.app.ResolverActivity
    public ResolverActivity.ResolveListAdapter createAdapter(Context context, List<Intent> payloadIntents, Intent[] initialIntents, List<ResolveInfo> rList, int launchedFromUid, boolean filterLastUsed) {
        ChooserListAdapter adapter = new ChooserListAdapter(context, payloadIntents, initialIntents, rList, launchedFromUid, filterLastUsed, createListController());
        return adapter;
    }

    @Override // com.android.internal.app.ResolverActivity
    @VisibleForTesting
    protected ResolverListController createListController() {
        return new ChooserListController(this, this.mPm, getTargetIntent(), getReferrerPackageName(), this.mLaunchedFromUid);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class ChooserTargetInfo implements ResolverActivity.TargetInfo {
        private final ResolveInfo mBackupResolveInfo;
        private CharSequence mBadgeContentDescription;
        private Drawable mBadgeIcon;
        private final ChooserTarget mChooserTarget;
        private Drawable mDisplayIcon;
        private final int mFillInFlags;
        private final Intent mFillInIntent;
        private final float mModifiedScore;
        private final ResolverActivity.DisplayResolveInfo mSourceInfo;

        public ChooserTargetInfo(ResolverActivity.DisplayResolveInfo sourceInfo, ChooserTarget chooserTarget, float modifiedScore) {
            ResolveInfo ri;
            ActivityInfo ai;
            this.mBadgeIcon = null;
            this.mSourceInfo = sourceInfo;
            this.mChooserTarget = chooserTarget;
            this.mModifiedScore = modifiedScore;
            if (sourceInfo != null && (ri = sourceInfo.getResolveInfo()) != null && (ai = ri.activityInfo) != null && ai.applicationInfo != null) {
                PackageManager pm = ChooserActivity.this.getPackageManager();
                this.mBadgeIcon = pm.getApplicationIcon(ai.applicationInfo);
                this.mBadgeContentDescription = pm.getApplicationLabel(ai.applicationInfo);
            }
            Icon icon = chooserTarget.getIcon();
            this.mDisplayIcon = icon != null ? icon.loadDrawable(ChooserActivity.this) : null;
            if (sourceInfo == null) {
                this.mBackupResolveInfo = ChooserActivity.this.getPackageManager().resolveActivity(getResolvedIntent(), 0);
            } else {
                this.mBackupResolveInfo = null;
            }
            this.mFillInIntent = null;
            this.mFillInFlags = 0;
        }

        private ChooserTargetInfo(ChooserTargetInfo other, Intent fillInIntent, int flags) {
            this.mBadgeIcon = null;
            this.mSourceInfo = other.mSourceInfo;
            this.mBackupResolveInfo = other.mBackupResolveInfo;
            this.mChooserTarget = other.mChooserTarget;
            this.mBadgeIcon = other.mBadgeIcon;
            this.mBadgeContentDescription = other.mBadgeContentDescription;
            this.mDisplayIcon = other.mDisplayIcon;
            this.mFillInIntent = fillInIntent;
            this.mFillInFlags = flags;
            this.mModifiedScore = other.mModifiedScore;
        }

        public float getModifiedScore() {
            return this.mModifiedScore;
        }

        @Override // com.android.internal.app.ResolverActivity.TargetInfo
        public Intent getResolvedIntent() {
            if (this.mSourceInfo != null) {
                return this.mSourceInfo.getResolvedIntent();
            }
            Intent targetIntent = new Intent(ChooserActivity.this.getTargetIntent());
            targetIntent.setComponent(this.mChooserTarget.getComponentName());
            targetIntent.putExtras(this.mChooserTarget.getIntentExtras());
            return targetIntent;
        }

        @Override // com.android.internal.app.ResolverActivity.TargetInfo
        public ComponentName getResolvedComponentName() {
            if (this.mSourceInfo != null) {
                return this.mSourceInfo.getResolvedComponentName();
            }
            if (this.mBackupResolveInfo != null) {
                return new ComponentName(this.mBackupResolveInfo.activityInfo.packageName, this.mBackupResolveInfo.activityInfo.name);
            }
            return null;
        }

        private Intent getBaseIntentToSend() {
            Intent result = getResolvedIntent();
            if (result == null) {
                Log.e(ChooserActivity.TAG, "ChooserTargetInfo: no base intent available to send");
            } else {
                result = new Intent(result);
                if (this.mFillInIntent != null) {
                    result.fillIn(this.mFillInIntent, this.mFillInFlags);
                }
                result.fillIn(ChooserActivity.this.mReferrerFillInIntent, 0);
            }
            return result;
        }

        @Override // com.android.internal.app.ResolverActivity.TargetInfo
        public boolean start(Activity activity, Bundle options) {
            throw new RuntimeException("ChooserTargets should be started as caller.");
        }

        @Override // com.android.internal.app.ResolverActivity.TargetInfo
        public boolean startAsCaller(Activity activity, Bundle options, int userId) {
            Intent intent = getBaseIntentToSend();
            boolean ignoreTargetSecurity = false;
            if (intent == null) {
                return false;
            }
            intent.setComponent(this.mChooserTarget.getComponentName());
            intent.putExtras(this.mChooserTarget.getIntentExtras());
            if (this.mSourceInfo != null && this.mSourceInfo.getResolvedComponentName().getPackageName().equals(this.mChooserTarget.getComponentName().getPackageName())) {
                ignoreTargetSecurity = true;
            }
            activity.startActivityAsCaller(intent, options, ignoreTargetSecurity, userId);
            return true;
        }

        @Override // com.android.internal.app.ResolverActivity.TargetInfo
        public boolean startAsUser(Activity activity, Bundle options, UserHandle user) {
            throw new RuntimeException("ChooserTargets should be started as caller.");
        }

        @Override // com.android.internal.app.ResolverActivity.TargetInfo
        public ResolveInfo getResolveInfo() {
            return this.mSourceInfo != null ? this.mSourceInfo.getResolveInfo() : this.mBackupResolveInfo;
        }

        @Override // com.android.internal.app.ResolverActivity.TargetInfo
        public CharSequence getDisplayLabel() {
            return this.mChooserTarget.getTitle();
        }

        @Override // com.android.internal.app.ResolverActivity.TargetInfo
        public CharSequence getExtendedInfo() {
            return null;
        }

        @Override // com.android.internal.app.ResolverActivity.TargetInfo
        public Drawable getDisplayIcon() {
            return this.mDisplayIcon;
        }

        @Override // com.android.internal.app.ResolverActivity.TargetInfo
        public Drawable getBadgeIcon() {
            return this.mBadgeIcon;
        }

        @Override // com.android.internal.app.ResolverActivity.TargetInfo
        public CharSequence getBadgeContentDescription() {
            return this.mBadgeContentDescription;
        }

        @Override // com.android.internal.app.ResolverActivity.TargetInfo
        public ResolverActivity.TargetInfo cloneFilledIn(Intent fillInIntent, int flags) {
            return new ChooserTargetInfo(this, fillInIntent, flags);
        }

        @Override // com.android.internal.app.ResolverActivity.TargetInfo
        public List<Intent> getAllSourceIntents() {
            List<Intent> results = new ArrayList<>();
            if (this.mSourceInfo != null) {
                results.add(this.mSourceInfo.getAllSourceIntents().get(0));
            }
            return results;
        }

        @Override // com.android.internal.app.ResolverActivity.TargetInfo
        public boolean isPinned() {
            if (this.mSourceInfo != null) {
                return this.mSourceInfo.isPinned();
            }
            return false;
        }
    }

    /* loaded from: classes3.dex */
    public class ChooserListAdapter extends ResolverActivity.ResolveListAdapter {
        private static final int MAX_SERVICE_TARGETS = 4;
        private static final int MAX_TARGETS_PER_SERVICE = 2;
        public static final int TARGET_BAD = -1;
        public static final int TARGET_CALLER = 0;
        public static final int TARGET_SERVICE = 1;
        public static final int TARGET_STANDARD = 2;
        private final BaseChooserTargetComparator mBaseTargetComparator;
        private final List<ResolverActivity.TargetInfo> mCallerTargets;
        private float mLateFee;
        private final List<ChooserTargetInfo> mServiceTargets;
        private boolean mShowServiceTargets;
        private boolean mTargetsNeedPruning;

        public ChooserListAdapter(Context context, List<Intent> payloadIntents, Intent[] initialIntents, List<ResolveInfo> rList, int launchedFromUid, boolean filterLastUsed, ResolverListController resolverListController) {
            super(context, payloadIntents, null, rList, launchedFromUid, filterLastUsed, resolverListController);
            ResolveInfo ri;
            this.mServiceTargets = new ArrayList();
            this.mCallerTargets = new ArrayList();
            this.mLateFee = 1.0f;
            int i = 0;
            this.mTargetsNeedPruning = false;
            this.mBaseTargetComparator = new BaseChooserTargetComparator();
            if (initialIntents != null) {
                PackageManager pm = ChooserActivity.this.getPackageManager();
                int i2 = 0;
                while (true) {
                    int i3 = i2;
                    int i4 = initialIntents.length;
                    if (i3 < i4) {
                        Intent ii = initialIntents[i3];
                        if (ii != null) {
                            ResolveInfo ri2 = null;
                            ActivityInfo ai = null;
                            ComponentName cn = ii.getComponent();
                            if (cn != null) {
                                try {
                                    ai = pm.getActivityInfo(ii.getComponent(), i);
                                    ri2 = new ResolveInfo();
                                    ri2.activityInfo = ai;
                                } catch (PackageManager.NameNotFoundException e) {
                                }
                            }
                            if (ai == null) {
                                ResolveInfo ri3 = pm.resolveActivity(ii, 65536);
                                ai = ri3 != null ? ri3.activityInfo : null;
                                ri = ri3;
                            } else {
                                ri = ri2;
                            }
                            if (ai == null) {
                                Log.w(ChooserActivity.TAG, "No activity found for " + ii);
                            } else {
                                UserManager userManager = (UserManager) ChooserActivity.this.getSystemService("user");
                                if (ii instanceof LabeledIntent) {
                                    LabeledIntent li = (LabeledIntent) ii;
                                    ri.resolvePackageName = li.getSourcePackage();
                                    ri.labelRes = li.getLabelResource();
                                    ri.nonLocalizedLabel = li.getNonLocalizedLabel();
                                    ri.icon = li.getIconResource();
                                    ri.iconResourceId = ri.icon;
                                }
                                if (userManager.isManagedProfile()) {
                                    ri.noResourceId = true;
                                    ri.icon = i;
                                }
                                this.mCallerTargets.add(new ResolverActivity.DisplayResolveInfo(ii, ri, ri.loadLabel(pm), null, ii));
                            }
                        }
                        i2 = i3 + 1;
                        i = 0;
                    } else {
                        return;
                    }
                }
            }
        }

        @Override // com.android.internal.app.ResolverActivity.ResolveListAdapter
        public boolean showsExtendedInfo(ResolverActivity.TargetInfo info) {
            return false;
        }

        @Override // com.android.internal.app.ResolverActivity.ResolveListAdapter
        public boolean isComponentPinned(ComponentName name) {
            return ChooserActivity.this.mPinnedSharedPrefs.getBoolean(name.flattenToString(), false);
        }

        @Override // com.android.internal.app.ResolverActivity.ResolveListAdapter
        public View onCreateView(ViewGroup parent) {
            return this.mInflater.inflate(R.layout.resolve_grid_item, parent, false);
        }

        @Override // com.android.internal.app.ResolverActivity.ResolveListAdapter
        public void onListRebuilt() {
            if (ActivityManager.isLowRamDeviceStatic()) {
                return;
            }
            if (this.mServiceTargets != null && getDisplayInfoCount() == 0) {
                this.mTargetsNeedPruning = true;
            }
            ChooserActivity.this.queryTargetServices(this);
        }

        @Override // com.android.internal.app.ResolverActivity.ResolveListAdapter
        public boolean shouldGetResolvedFilter() {
            return true;
        }

        @Override // com.android.internal.app.ResolverActivity.ResolveListAdapter, android.widget.Adapter
        public int getCount() {
            return super.getCount() + getServiceTargetCount() + getCallerTargetCount();
        }

        @Override // com.android.internal.app.ResolverActivity.ResolveListAdapter
        public int getUnfilteredCount() {
            return super.getUnfilteredCount() + getServiceTargetCount() + getCallerTargetCount();
        }

        public int getCallerTargetCount() {
            return this.mCallerTargets.size();
        }

        public int getServiceTargetCount() {
            if (!this.mShowServiceTargets) {
                return 0;
            }
            return Math.min(this.mServiceTargets.size(), 4);
        }

        public int getStandardTargetCount() {
            return super.getCount();
        }

        public int getPositionTargetType(int position) {
            int callerTargetCount = getCallerTargetCount();
            if (position < callerTargetCount) {
                return 0;
            }
            int offset = 0 + callerTargetCount;
            int serviceTargetCount = getServiceTargetCount();
            if (position - offset < serviceTargetCount) {
                return 1;
            }
            int standardTargetCount = super.getCount();
            if (position - (offset + serviceTargetCount) < standardTargetCount) {
                return 2;
            }
            return -1;
        }

        @Override // com.android.internal.app.ResolverActivity.ResolveListAdapter, android.widget.Adapter
        public ResolverActivity.TargetInfo getItem(int position) {
            return targetInfoForPosition(position, true);
        }

        @Override // com.android.internal.app.ResolverActivity.ResolveListAdapter
        public ResolverActivity.TargetInfo targetInfoForPosition(int position, boolean filtered) {
            int callerTargetCount = getCallerTargetCount();
            if (position < callerTargetCount) {
                return this.mCallerTargets.get(position);
            }
            int offset = 0 + callerTargetCount;
            int serviceTargetCount = getServiceTargetCount();
            if (position - offset < serviceTargetCount) {
                return this.mServiceTargets.get(position - offset);
            }
            int offset2 = offset + serviceTargetCount;
            return filtered ? super.getItem(position - offset2) : getDisplayInfoAt(position - offset2);
        }

        public void addServiceResults(ResolverActivity.DisplayResolveInfo origTarget, List<ChooserTarget> targets) {
            if (this.mTargetsNeedPruning && targets.size() > 0) {
                this.mServiceTargets.clear();
                this.mTargetsNeedPruning = false;
            }
            float parentScore = getScore(origTarget);
            Collections.sort(targets, this.mBaseTargetComparator);
            float lastScore = 0.0f;
            int N = Math.min(targets.size(), 2);
            for (int i = 0; i < N; i++) {
                ChooserTarget target = targets.get(i);
                float targetScore = target.getScore() * parentScore * this.mLateFee;
                if (i > 0 && targetScore >= lastScore) {
                    targetScore = lastScore * 0.95f;
                }
                insertServiceTarget(new ChooserTargetInfo(origTarget, target, targetScore));
                lastScore = targetScore;
            }
            this.mLateFee *= 0.95f;
            notifyDataSetChanged();
        }

        public void setShowServiceTargets(boolean show) {
            if (show != this.mShowServiceTargets) {
                this.mShowServiceTargets = show;
                notifyDataSetChanged();
            }
        }

        private void insertServiceTarget(ChooserTargetInfo chooserTargetInfo) {
            float newScore = chooserTargetInfo.getModifiedScore();
            int N = this.mServiceTargets.size();
            for (int i = 0; i < N; i++) {
                ChooserTargetInfo serviceTarget = this.mServiceTargets.get(i);
                if (newScore > serviceTarget.getModifiedScore()) {
                    this.mServiceTargets.add(i, chooserTargetInfo);
                    return;
                }
            }
            this.mServiceTargets.add(chooserTargetInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class BaseChooserTargetComparator implements Comparator<ChooserTarget> {
        BaseChooserTargetComparator() {
        }

        @Override // java.util.Comparator
        public int compare(ChooserTarget lhs, ChooserTarget rhs) {
            return (int) Math.signum(rhs.getScore() - lhs.getScore());
        }
    }

    /* loaded from: classes3.dex */
    class ChooserRowAdapter extends BaseAdapter {
        private ChooserListAdapter mChooserListAdapter;
        private final LayoutInflater mLayoutInflater;
        private final int mColumnCount = 4;
        private int mAnimationCount = 0;

        public ChooserRowAdapter(ChooserListAdapter wrappedAdapter) {
            this.mChooserListAdapter = wrappedAdapter;
            this.mLayoutInflater = LayoutInflater.from(ChooserActivity.this);
            wrappedAdapter.registerDataSetObserver(new DataSetObserver() { // from class: com.android.internal.app.ChooserActivity.ChooserRowAdapter.1
                @Override // android.database.DataSetObserver
                public void onChanged() {
                    super.onChanged();
                    ChooserRowAdapter.this.notifyDataSetChanged();
                }

                @Override // android.database.DataSetObserver
                public void onInvalidated() {
                    super.onInvalidated();
                    ChooserRowAdapter.this.notifyDataSetInvalidated();
                }
            });
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return (int) (getCallerTargetRowCount() + getServiceTargetRowCount() + Math.ceil(this.mChooserListAdapter.getStandardTargetCount() / 4.0f));
        }

        public int getCallerTargetRowCount() {
            return (int) Math.ceil(this.mChooserListAdapter.getCallerTargetCount() / 4.0f);
        }

        public int getServiceTargetRowCount() {
            return this.mChooserListAdapter.getServiceTargetCount() == 0 ? 0 : 1;
        }

        @Override // android.widget.Adapter
        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return position;
        }

        @Override // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            RowViewHolder holder;
            if (convertView == null) {
                holder = createViewHolder(parent);
            } else {
                holder = (RowViewHolder) convertView.getTag();
            }
            bindViewHolder(position, holder);
            return holder.row;
        }

        RowViewHolder createViewHolder(ViewGroup parent) {
            ViewGroup row = (ViewGroup) this.mLayoutInflater.inflate(R.layout.chooser_row, parent, false);
            final RowViewHolder holder = new RowViewHolder(row, 4);
            int spec = View.MeasureSpec.makeMeasureSpec(0, 0);
            for (int i = 0; i < 4; i++) {
                View v = this.mChooserListAdapter.createView(row);
                final int column = i;
                v.setOnClickListener(new View.OnClickListener() { // from class: com.android.internal.app.ChooserActivity.ChooserRowAdapter.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v2) {
                        ChooserActivity.this.startSelected(holder.itemIndices[column], false, true);
                    }
                });
                v.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.internal.app.ChooserActivity.ChooserRowAdapter.3
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View v2) {
                        ChooserActivity.this.showTargetDetails(ChooserRowAdapter.this.mChooserListAdapter.resolveInfoForPosition(holder.itemIndices[column], true));
                        return true;
                    }
                });
                row.addView(v);
                holder.cells[i] = v;
                ViewGroup.LayoutParams lp = v.getLayoutParams();
                v.measure(spec, spec);
                if (lp == null) {
                    row.setLayoutParams(new ViewGroup.LayoutParams(-1, v.getMeasuredHeight()));
                } else {
                    lp.height = v.getMeasuredHeight();
                }
                if (i != 3) {
                    row.addView(new Space(ChooserActivity.this), new LinearLayout.LayoutParams(0, 0, 1.0f));
                }
            }
            holder.measure();
            ViewGroup.LayoutParams lp2 = row.getLayoutParams();
            if (lp2 == null) {
                row.setLayoutParams(new ViewGroup.LayoutParams(-1, holder.measuredRowHeight));
            } else {
                lp2.height = holder.measuredRowHeight;
            }
            row.setTag(holder);
            return holder;
        }

        void bindViewHolder(int rowPosition, RowViewHolder holder) {
            int start = getFirstRowPosition(rowPosition);
            int startType = this.mChooserListAdapter.getPositionTargetType(start);
            int end = (start + 4) - 1;
            while (this.mChooserListAdapter.getPositionTargetType(end) != startType && end >= start) {
                end--;
            }
            if (startType == 1) {
                holder.row.setBackgroundColor(ChooserActivity.this.getColor(R.color.chooser_service_row_background_color));
                int nextStartType = this.mChooserListAdapter.getPositionTargetType(getFirstRowPosition(rowPosition + 1));
                int serviceSpacing = holder.row.getContext().getResources().getDimensionPixelSize(R.dimen.chooser_service_spacing);
                if (rowPosition == 0 && nextStartType != 1) {
                    setVertPadding(holder, 0, 0);
                } else {
                    int top = rowPosition == 0 ? serviceSpacing : 0;
                    if (nextStartType != 1) {
                        setVertPadding(holder, top, serviceSpacing);
                    } else {
                        setVertPadding(holder, top, 0);
                    }
                }
            } else {
                holder.row.setBackgroundColor(0);
                int lastStartType = this.mChooserListAdapter.getPositionTargetType(getFirstRowPosition(rowPosition - 1));
                if (lastStartType == 1 || rowPosition == 0) {
                    setVertPadding(holder, holder.row.getContext().getResources().getDimensionPixelSize(R.dimen.chooser_service_spacing), 0);
                } else {
                    setVertPadding(holder, 0, 0);
                }
            }
            int oldHeight = holder.row.getLayoutParams().height;
            holder.row.getLayoutParams().height = Math.max(1, holder.measuredRowHeight);
            if (holder.row.getLayoutParams().height != oldHeight) {
                holder.row.requestLayout();
            }
            for (int i = 0; i < 4; i++) {
                View v = holder.cells[i];
                if (start + i <= end) {
                    v.setVisibility(0);
                    holder.itemIndices[i] = start + i;
                    this.mChooserListAdapter.bindView(holder.itemIndices[i], v);
                } else {
                    v.setVisibility(4);
                }
            }
        }

        private void setVertPadding(RowViewHolder holder, int top, int bottom) {
            holder.row.setPadding(holder.row.getPaddingLeft(), top, holder.row.getPaddingRight(), bottom);
        }

        int getFirstRowPosition(int row) {
            int callerCount = this.mChooserListAdapter.getCallerTargetCount();
            int callerRows = (int) Math.ceil(callerCount / 4.0f);
            if (row < callerRows) {
                return row * 4;
            }
            int serviceCount = this.mChooserListAdapter.getServiceTargetCount();
            int serviceRows = (int) Math.ceil(serviceCount / 4.0f);
            if (row < callerRows + serviceRows) {
                return ((row - callerRows) * 4) + callerCount;
            }
            return callerCount + serviceCount + (((row - callerRows) - serviceRows) * 4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class RowViewHolder {
        final View[] cells;
        int[] itemIndices;
        int measuredRowHeight;
        final ViewGroup row;

        public RowViewHolder(ViewGroup row, int cellCount) {
            this.row = row;
            this.cells = new View[cellCount];
            this.itemIndices = new int[cellCount];
        }

        public void measure() {
            int spec = View.MeasureSpec.makeMeasureSpec(0, 0);
            this.row.measure(spec, spec);
            this.measuredRowHeight = this.row.getMeasuredHeight();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class ChooserTargetServiceConnection implements ServiceConnection {
        private ChooserActivity mChooserActivity;
        private ComponentName mConnectedComponent;
        private ResolverActivity.DisplayResolveInfo mOriginalTarget;
        private final Object mLock = new Object();
        private final IChooserTargetResult mChooserTargetResult = new IChooserTargetResult.Stub() { // from class: com.android.internal.app.ChooserActivity.ChooserTargetServiceConnection.1
            @Override // android.service.chooser.IChooserTargetResult
            public void sendResult(List<ChooserTarget> targets) throws RemoteException {
                synchronized (ChooserTargetServiceConnection.this.mLock) {
                    if (ChooserTargetServiceConnection.this.mChooserActivity == null) {
                        Log.e(ChooserActivity.TAG, "destroyed ChooserTargetServiceConnection received result from " + ChooserTargetServiceConnection.this.mConnectedComponent + "; ignoring...");
                        return;
                    }
                    ChooserTargetServiceConnection.this.mChooserActivity.filterServiceTargets(ChooserTargetServiceConnection.this.mOriginalTarget.getResolveInfo().activityInfo.packageName, targets);
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = new ServiceResultInfo(ChooserTargetServiceConnection.this.mOriginalTarget, targets, ChooserTargetServiceConnection.this);
                    ChooserTargetServiceConnection.this.mChooserActivity.mChooserHandler.sendMessage(msg);
                }
            }
        };

        public ChooserTargetServiceConnection(ChooserActivity chooserActivity, ResolverActivity.DisplayResolveInfo dri) {
            this.mChooserActivity = chooserActivity;
            this.mOriginalTarget = dri;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized (this.mLock) {
                if (this.mChooserActivity == null) {
                    Log.e(ChooserActivity.TAG, "destroyed ChooserTargetServiceConnection got onServiceConnected");
                    return;
                }
                IChooserTargetService icts = IChooserTargetService.Stub.asInterface(service);
                try {
                    icts.getChooserTargets(this.mOriginalTarget.getResolvedComponentName(), this.mOriginalTarget.getResolveInfo().filter, this.mChooserTargetResult);
                } catch (RemoteException e) {
                    Log.e(ChooserActivity.TAG, "Querying ChooserTargetService " + name + " failed.", e);
                    this.mChooserActivity.unbindService(this);
                    this.mChooserActivity.mServiceConnections.remove(this);
                    destroy();
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            synchronized (this.mLock) {
                if (this.mChooserActivity == null) {
                    Log.e(ChooserActivity.TAG, "destroyed ChooserTargetServiceConnection got onServiceDisconnected");
                    return;
                }
                this.mChooserActivity.unbindService(this);
                this.mChooserActivity.mServiceConnections.remove(this);
                if (this.mChooserActivity.mServiceConnections.isEmpty()) {
                    this.mChooserActivity.mChooserHandler.removeMessages(2);
                    this.mChooserActivity.sendVoiceChoicesIfNeeded();
                }
                this.mConnectedComponent = null;
                destroy();
            }
        }

        public void destroy() {
            synchronized (this.mLock) {
                this.mChooserActivity = null;
                this.mOriginalTarget = null;
            }
        }

        public String toString() {
            String str;
            StringBuilder sb = new StringBuilder();
            sb.append("ChooserTargetServiceConnection{service=");
            sb.append(this.mConnectedComponent);
            sb.append(", activity=");
            if (this.mOriginalTarget != null) {
                str = this.mOriginalTarget.getResolveInfo().activityInfo.toString();
            } else {
                str = "<connection destroyed>";
            }
            sb.append(str);
            sb.append("}");
            return sb.toString();
        }
    }

    /* loaded from: classes3.dex */
    static class ServiceResultInfo {
        public final ChooserTargetServiceConnection connection;
        public final ResolverActivity.DisplayResolveInfo originalTarget;
        public final List<ChooserTarget> resultTargets;

        public ServiceResultInfo(ResolverActivity.DisplayResolveInfo ot, List<ChooserTarget> rt, ChooserTargetServiceConnection c) {
            this.originalTarget = ot;
            this.resultTargets = rt;
            this.connection = c;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class RefinementResultReceiver extends ResultReceiver {
        private ChooserActivity mChooserActivity;
        private ResolverActivity.TargetInfo mSelectedTarget;

        public RefinementResultReceiver(ChooserActivity host, ResolverActivity.TargetInfo target, Handler handler) {
            super(handler);
            this.mChooserActivity = host;
            this.mSelectedTarget = target;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.ResultReceiver
        public void onReceiveResult(int resultCode, Bundle resultData) {
            if (this.mChooserActivity == null) {
                Log.e(ChooserActivity.TAG, "Destroyed RefinementResultReceiver received a result");
            } else if (resultData == null) {
                Log.e(ChooserActivity.TAG, "RefinementResultReceiver received null resultData");
            } else {
                switch (resultCode) {
                    case -1:
                        Parcelable intentParcelable = resultData.getParcelable(Intent.EXTRA_INTENT);
                        if (intentParcelable instanceof Intent) {
                            this.mChooserActivity.onRefinementResult(this.mSelectedTarget, (Intent) intentParcelable);
                            return;
                        } else {
                            Log.e(ChooserActivity.TAG, "RefinementResultReceiver received RESULT_OK but no Intent in resultData with key Intent.EXTRA_INTENT");
                            return;
                        }
                    case 0:
                        this.mChooserActivity.onRefinementCanceled();
                        return;
                    default:
                        Log.w(ChooserActivity.TAG, "Unknown result code " + resultCode + " sent to RefinementResultReceiver");
                        return;
                }
            }
        }

        public void destroy() {
            this.mChooserActivity = null;
            this.mSelectedTarget = null;
        }
    }

    /* loaded from: classes3.dex */
    class OffsetDataSetObserver extends DataSetObserver {
        private View mCachedView;
        private int mCachedViewType = -1;
        private final AbsListView mListView;

        public OffsetDataSetObserver(AbsListView listView) {
            this.mListView = listView;
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            if (ChooserActivity.this.mResolverDrawerLayout != null) {
                int chooserTargetRows = ChooserActivity.this.mChooserRowAdapter.getServiceTargetRowCount();
                int offset = 0;
                for (int i = 0; i < chooserTargetRows; i++) {
                    int pos = ChooserActivity.this.mChooserRowAdapter.getCallerTargetRowCount() + i;
                    int vt = ChooserActivity.this.mChooserRowAdapter.getItemViewType(pos);
                    if (vt != this.mCachedViewType) {
                        this.mCachedView = null;
                    }
                    View v = ChooserActivity.this.mChooserRowAdapter.getView(pos, this.mCachedView, this.mListView);
                    int height = ((RowViewHolder) v.getTag()).measuredRowHeight;
                    offset += height;
                    if (vt >= 0) {
                        this.mCachedViewType = vt;
                        this.mCachedView = v;
                    } else {
                        this.mCachedViewType = -1;
                    }
                }
                ChooserActivity.this.mResolverDrawerLayout.setCollapsibleHeightReserved(offset);
            }
        }
    }
}
