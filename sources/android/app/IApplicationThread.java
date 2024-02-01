package android.app;

import android.app.IInstrumentationWatcher;
import android.app.IUiAutomationConnection;
import android.app.servertransaction.ClientTransaction;
import android.content.ComponentName;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ParceledListSlice;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.android.internal.app.IVoiceInteractor;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public interface IApplicationThread extends IInterface {
    synchronized void attachAgent(String str) throws RemoteException;

    synchronized void bindApplication(String str, ApplicationInfo applicationInfo, List<ProviderInfo> list, ComponentName componentName, ProfilerInfo profilerInfo, Bundle bundle, IInstrumentationWatcher iInstrumentationWatcher, IUiAutomationConnection iUiAutomationConnection, int i, boolean z, boolean z2, boolean z3, boolean z4, Configuration configuration, CompatibilityInfo compatibilityInfo, Map map, Bundle bundle2, String str2, boolean z5) throws RemoteException;

    synchronized void clearDnsCache() throws RemoteException;

    synchronized void dispatchPackageBroadcast(int i, String[] strArr) throws RemoteException;

    synchronized void dumpActivity(ParcelFileDescriptor parcelFileDescriptor, IBinder iBinder, String str, String[] strArr) throws RemoteException;

    synchronized void dumpDbInfo(ParcelFileDescriptor parcelFileDescriptor, String[] strArr) throws RemoteException;

    synchronized void dumpGfxInfo(ParcelFileDescriptor parcelFileDescriptor, String[] strArr) throws RemoteException;

    synchronized void dumpHeap(boolean z, boolean z2, boolean z3, String str, ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    synchronized void dumpMemInfo(ParcelFileDescriptor parcelFileDescriptor, Debug.MemoryInfo memoryInfo, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, String[] strArr) throws RemoteException;

    synchronized void dumpMemInfoProto(ParcelFileDescriptor parcelFileDescriptor, Debug.MemoryInfo memoryInfo, boolean z, boolean z2, boolean z3, boolean z4, String[] strArr) throws RemoteException;

    synchronized void dumpProvider(ParcelFileDescriptor parcelFileDescriptor, IBinder iBinder, String[] strArr) throws RemoteException;

    synchronized void dumpService(ParcelFileDescriptor parcelFileDescriptor, IBinder iBinder, String[] strArr) throws RemoteException;

    synchronized void handleTrustStorageUpdate() throws RemoteException;

    synchronized void notifyCleartextNetwork(byte[] bArr) throws RemoteException;

    private protected void processInBackground() throws RemoteException;

    synchronized void profilerControl(boolean z, ProfilerInfo profilerInfo, int i) throws RemoteException;

    synchronized void requestAssistContextExtras(IBinder iBinder, IBinder iBinder2, int i, int i2, int i3) throws RemoteException;

    synchronized void runIsolatedEntryPoint(String str, String[] strArr) throws RemoteException;

    synchronized void scheduleApplicationInfoChanged(ApplicationInfo applicationInfo) throws RemoteException;

    private protected void scheduleBindService(IBinder iBinder, Intent intent, boolean z, int i) throws RemoteException;

    synchronized void scheduleCrash(String str) throws RemoteException;

    synchronized void scheduleCreateBackupAgent(ApplicationInfo applicationInfo, CompatibilityInfo compatibilityInfo, int i) throws RemoteException;

    private protected void scheduleCreateService(IBinder iBinder, ServiceInfo serviceInfo, CompatibilityInfo compatibilityInfo, int i) throws RemoteException;

    synchronized void scheduleDestroyBackupAgent(ApplicationInfo applicationInfo, CompatibilityInfo compatibilityInfo) throws RemoteException;

    synchronized void scheduleEnterAnimationComplete(IBinder iBinder) throws RemoteException;

    private protected void scheduleExit() throws RemoteException;

    synchronized void scheduleInstallProvider(ProviderInfo providerInfo) throws RemoteException;

    synchronized void scheduleLocalVoiceInteractionStarted(IBinder iBinder, IVoiceInteractor iVoiceInteractor) throws RemoteException;

    private protected void scheduleLowMemory() throws RemoteException;

    synchronized void scheduleOnNewActivityOptions(IBinder iBinder, Bundle bundle) throws RemoteException;

    synchronized void scheduleReceiver(Intent intent, ActivityInfo activityInfo, CompatibilityInfo compatibilityInfo, int i, String str, Bundle bundle, boolean z, int i2, int i3) throws RemoteException;

    synchronized void scheduleRegisteredReceiver(IIntentReceiver iIntentReceiver, Intent intent, int i, String str, Bundle bundle, boolean z, boolean z2, int i2, int i3) throws RemoteException;

    synchronized void scheduleServiceArgs(IBinder iBinder, ParceledListSlice parceledListSlice) throws RemoteException;

    synchronized void scheduleSleeping(IBinder iBinder, boolean z) throws RemoteException;

    private protected void scheduleStopService(IBinder iBinder) throws RemoteException;

    private protected void scheduleSuicide() throws RemoteException;

    synchronized void scheduleTransaction(ClientTransaction clientTransaction) throws RemoteException;

    synchronized void scheduleTranslucentConversionComplete(IBinder iBinder, boolean z) throws RemoteException;

    private protected void scheduleTrimMemory(int i) throws RemoteException;

    private protected void scheduleUnbindService(IBinder iBinder, Intent intent) throws RemoteException;

    synchronized void setCoreSettings(Bundle bundle) throws RemoteException;

    synchronized void setHttpProxy(String str, String str2, String str3, Uri uri) throws RemoteException;

    synchronized void setNetworkBlockSeq(long j) throws RemoteException;

    synchronized void setProcessState(int i) throws RemoteException;

    synchronized void setSchedulingGroup(int i) throws RemoteException;

    synchronized void startBinderTracking() throws RemoteException;

    synchronized void stopBinderTrackingAndDump(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    synchronized void unstableProviderDied(IBinder iBinder) throws RemoteException;

    synchronized void updatePackageCompatibilityInfo(String str, CompatibilityInfo compatibilityInfo) throws RemoteException;

    synchronized void updateTimePrefs(int i) throws RemoteException;

    private protected void updateTimeZone() throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IApplicationThread {
        private static final String DESCRIPTOR = "android.app.IApplicationThread";
        static final int TRANSACTION_attachAgent = 48;
        static final int TRANSACTION_bindApplication = 4;
        static final int TRANSACTION_clearDnsCache = 26;
        static final int TRANSACTION_dispatchPackageBroadcast = 22;
        static final int TRANSACTION_dumpActivity = 25;
        static final int TRANSACTION_dumpDbInfo = 35;
        static final int TRANSACTION_dumpGfxInfo = 33;
        static final int TRANSACTION_dumpHeap = 24;
        static final int TRANSACTION_dumpMemInfo = 31;
        static final int TRANSACTION_dumpMemInfoProto = 32;
        static final int TRANSACTION_dumpProvider = 34;
        static final int TRANSACTION_dumpService = 12;
        static final int TRANSACTION_handleTrustStorageUpdate = 47;
        static final int TRANSACTION_notifyCleartextNetwork = 43;
        static final int TRANSACTION_processInBackground = 9;
        static final int TRANSACTION_profilerControl = 16;
        static final int TRANSACTION_requestAssistContextExtras = 37;
        static final int TRANSACTION_runIsolatedEntryPoint = 5;
        static final int TRANSACTION_scheduleApplicationInfoChanged = 49;
        static final int TRANSACTION_scheduleBindService = 10;
        static final int TRANSACTION_scheduleCrash = 23;
        static final int TRANSACTION_scheduleCreateBackupAgent = 18;
        static final int TRANSACTION_scheduleCreateService = 2;
        static final int TRANSACTION_scheduleDestroyBackupAgent = 19;
        static final int TRANSACTION_scheduleEnterAnimationComplete = 42;
        static final int TRANSACTION_scheduleExit = 6;
        static final int TRANSACTION_scheduleInstallProvider = 40;
        static final int TRANSACTION_scheduleLocalVoiceInteractionStarted = 46;
        static final int TRANSACTION_scheduleLowMemory = 14;
        static final int TRANSACTION_scheduleOnNewActivityOptions = 20;
        static final int TRANSACTION_scheduleReceiver = 1;
        static final int TRANSACTION_scheduleRegisteredReceiver = 13;
        static final int TRANSACTION_scheduleServiceArgs = 7;
        static final int TRANSACTION_scheduleSleeping = 15;
        static final int TRANSACTION_scheduleStopService = 3;
        static final int TRANSACTION_scheduleSuicide = 21;
        static final int TRANSACTION_scheduleTransaction = 51;
        static final int TRANSACTION_scheduleTranslucentConversionComplete = 38;
        static final int TRANSACTION_scheduleTrimMemory = 30;
        static final int TRANSACTION_scheduleUnbindService = 11;
        static final int TRANSACTION_setCoreSettings = 28;
        static final int TRANSACTION_setHttpProxy = 27;
        static final int TRANSACTION_setNetworkBlockSeq = 50;
        static final int TRANSACTION_setProcessState = 39;
        static final int TRANSACTION_setSchedulingGroup = 17;
        static final int TRANSACTION_startBinderTracking = 44;
        static final int TRANSACTION_stopBinderTrackingAndDump = 45;
        static final int TRANSACTION_unstableProviderDied = 36;
        static final int TRANSACTION_updatePackageCompatibilityInfo = 29;
        static final int TRANSACTION_updateTimePrefs = 41;
        static final int TRANSACTION_updateTimeZone = 8;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IApplicationThread asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IApplicationThread)) {
                return (IApplicationThread) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Intent _arg0;
            ActivityInfo _arg1;
            CompatibilityInfo _arg2;
            Bundle _arg5;
            ServiceInfo _arg12;
            ComponentName _arg3;
            ProfilerInfo _arg4;
            Bundle _arg52;
            Configuration _arg13;
            CompatibilityInfo _arg14;
            boolean _arg15;
            Bundle _arg42;
            ApplicationInfo _arg02;
            ApplicationInfo _arg03;
            ParcelFileDescriptor _arg43;
            ParcelFileDescriptor _arg04;
            Debug.MemoryInfo _arg16;
            ParcelFileDescriptor _arg05;
            Debug.MemoryInfo _arg17;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = ActivityInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg2 = CompatibilityInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    int _arg32 = data.readInt();
                    String _arg44 = data.readString();
                    if (data.readInt() != 0) {
                        Bundle _arg53 = Bundle.CREATOR.createFromParcel(data);
                        _arg5 = _arg53;
                    } else {
                        _arg5 = null;
                    }
                    boolean _arg6 = data.readInt() != 0;
                    int _arg7 = data.readInt();
                    int _arg8 = data.readInt();
                    scheduleReceiver(_arg0, _arg1, _arg2, _arg32, _arg44, _arg5, _arg6, _arg7, _arg8);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg06 = data.readStrongBinder();
                    if (data.readInt() != 0) {
                        _arg12 = ServiceInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    CompatibilityInfo _arg22 = data.readInt() != 0 ? CompatibilityInfo.CREATOR.createFromParcel(data) : null;
                    int _arg33 = data.readInt();
                    scheduleCreateService(_arg06, _arg12, _arg22, _arg33);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg07 = data.readStrongBinder();
                    scheduleStopService(_arg07);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    ApplicationInfo _arg18 = data.readInt() != 0 ? ApplicationInfo.CREATOR.createFromParcel(data) : null;
                    List<ProviderInfo> _arg23 = data.createTypedArrayList(ProviderInfo.CREATOR);
                    if (data.readInt() != 0) {
                        ComponentName _arg34 = ComponentName.CREATOR.createFromParcel(data);
                        _arg3 = _arg34;
                    } else {
                        _arg3 = null;
                    }
                    if (data.readInt() != 0) {
                        ProfilerInfo _arg45 = ProfilerInfo.CREATOR.createFromParcel(data);
                        _arg4 = _arg45;
                    } else {
                        _arg4 = null;
                    }
                    if (data.readInt() != 0) {
                        Bundle _arg54 = Bundle.CREATOR.createFromParcel(data);
                        _arg52 = _arg54;
                    } else {
                        _arg52 = null;
                    }
                    IInstrumentationWatcher _arg62 = IInstrumentationWatcher.Stub.asInterface(data.readStrongBinder());
                    IUiAutomationConnection _arg72 = IUiAutomationConnection.Stub.asInterface(data.readStrongBinder());
                    int _arg82 = data.readInt();
                    boolean _arg9 = data.readInt() != 0;
                    boolean _arg10 = data.readInt() != 0;
                    boolean _arg11 = data.readInt() != 0;
                    boolean _arg122 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        _arg13 = Configuration.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    Configuration _arg132 = _arg13;
                    if (data.readInt() != 0) {
                        _arg14 = CompatibilityInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    CompatibilityInfo _arg142 = _arg14;
                    ClassLoader cl = getClass().getClassLoader();
                    Map _arg152 = data.readHashMap(cl);
                    Bundle _arg162 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    String _arg172 = data.readString();
                    boolean _arg182 = data.readInt() != 0;
                    bindApplication(_arg08, _arg18, _arg23, _arg3, _arg4, _arg52, _arg62, _arg72, _arg82, _arg9, _arg10, _arg11, _arg122, _arg132, _arg142, _arg152, _arg162, _arg172, _arg182);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    runIsolatedEntryPoint(_arg09, data.createStringArray());
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    scheduleExit();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg010 = data.readStrongBinder();
                    scheduleServiceArgs(_arg010, data.readInt() != 0 ? ParceledListSlice.CREATOR.createFromParcel(data) : null);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    updateTimeZone();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    processInBackground();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg011 = data.readStrongBinder();
                    Intent _arg19 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    _arg15 = data.readInt() != 0;
                    int _arg35 = data.readInt();
                    scheduleBindService(_arg011, _arg19, _arg15, _arg35);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg012 = data.readStrongBinder();
                    scheduleUnbindService(_arg012, data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelFileDescriptor _arg013 = data.readInt() != 0 ? ParcelFileDescriptor.CREATOR.createFromParcel(data) : null;
                    IBinder _arg110 = data.readStrongBinder();
                    String[] _arg24 = data.createStringArray();
                    dumpService(_arg013, _arg110, _arg24);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    IIntentReceiver _arg014 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                    Intent _arg111 = data.readInt() != 0 ? Intent.CREATOR.createFromParcel(data) : null;
                    int _arg25 = data.readInt();
                    String _arg36 = data.readString();
                    if (data.readInt() != 0) {
                        Bundle _arg46 = Bundle.CREATOR.createFromParcel(data);
                        _arg42 = _arg46;
                    } else {
                        _arg42 = null;
                    }
                    boolean _arg55 = data.readInt() != 0;
                    boolean _arg63 = data.readInt() != 0;
                    int _arg73 = data.readInt();
                    int _arg83 = data.readInt();
                    scheduleRegisteredReceiver(_arg014, _arg111, _arg25, _arg36, _arg42, _arg55, _arg63, _arg73, _arg83);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    scheduleLowMemory();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg015 = data.readStrongBinder();
                    _arg15 = data.readInt() != 0;
                    scheduleSleeping(_arg015, _arg15);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    _arg15 = data.readInt() != 0;
                    ProfilerInfo _arg112 = data.readInt() != 0 ? ProfilerInfo.CREATOR.createFromParcel(data) : null;
                    int _arg26 = data.readInt();
                    profilerControl(_arg15, _arg112, _arg26);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg016 = data.readInt();
                    setSchedulingGroup(_arg016);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = ApplicationInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    CompatibilityInfo _arg113 = data.readInt() != 0 ? CompatibilityInfo.CREATOR.createFromParcel(data) : null;
                    int _arg27 = data.readInt();
                    scheduleCreateBackupAgent(_arg02, _arg113, _arg27);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = ApplicationInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    CompatibilityInfo _arg114 = data.readInt() != 0 ? CompatibilityInfo.CREATOR.createFromParcel(data) : null;
                    scheduleDestroyBackupAgent(_arg03, _arg114);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg017 = data.readStrongBinder();
                    Bundle _arg115 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    scheduleOnNewActivityOptions(_arg017, _arg115);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    scheduleSuicide();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg018 = data.readInt();
                    dispatchPackageBroadcast(_arg018, data.createStringArray());
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    scheduleCrash(_arg019);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg020 = data.readInt() != 0;
                    boolean _arg116 = data.readInt() != 0;
                    boolean _arg28 = data.readInt() != 0;
                    String _arg37 = data.readString();
                    if (data.readInt() != 0) {
                        ParcelFileDescriptor _arg47 = ParcelFileDescriptor.CREATOR.createFromParcel(data);
                        _arg43 = _arg47;
                    } else {
                        _arg43 = null;
                    }
                    dumpHeap(_arg020, _arg116, _arg28, _arg37, _arg43);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelFileDescriptor _arg021 = data.readInt() != 0 ? ParcelFileDescriptor.CREATOR.createFromParcel(data) : null;
                    IBinder _arg117 = data.readStrongBinder();
                    String _arg29 = data.readString();
                    String[] _arg38 = data.createStringArray();
                    dumpActivity(_arg021, _arg117, _arg29, _arg38);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    clearDnsCache();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg022 = data.readString();
                    String _arg118 = data.readString();
                    String _arg210 = data.readString();
                    Uri _arg39 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    setHttpProxy(_arg022, _arg118, _arg210, _arg39);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    Bundle _arg023 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    setCoreSettings(_arg023);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg024 = data.readString();
                    CompatibilityInfo _arg119 = data.readInt() != 0 ? CompatibilityInfo.CREATOR.createFromParcel(data) : null;
                    updatePackageCompatibilityInfo(_arg024, _arg119);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg025 = data.readInt();
                    scheduleTrimMemory(_arg025);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = ParcelFileDescriptor.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg16 = Debug.MemoryInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg16 = null;
                    }
                    boolean _arg211 = data.readInt() != 0;
                    boolean _arg310 = data.readInt() != 0;
                    boolean _arg48 = data.readInt() != 0;
                    boolean _arg56 = data.readInt() != 0;
                    boolean _arg64 = data.readInt() != 0;
                    String[] _arg74 = data.createStringArray();
                    dumpMemInfo(_arg04, _arg16, _arg211, _arg310, _arg48, _arg56, _arg64, _arg74);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = ParcelFileDescriptor.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg17 = Debug.MemoryInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg17 = null;
                    }
                    boolean _arg212 = data.readInt() != 0;
                    boolean _arg311 = data.readInt() != 0;
                    boolean _arg49 = data.readInt() != 0;
                    boolean _arg57 = data.readInt() != 0;
                    String[] _arg65 = data.createStringArray();
                    dumpMemInfoProto(_arg05, _arg17, _arg212, _arg311, _arg49, _arg57, _arg65);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelFileDescriptor _arg026 = data.readInt() != 0 ? ParcelFileDescriptor.CREATOR.createFromParcel(data) : null;
                    dumpGfxInfo(_arg026, data.createStringArray());
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelFileDescriptor _arg027 = data.readInt() != 0 ? ParcelFileDescriptor.CREATOR.createFromParcel(data) : null;
                    IBinder _arg120 = data.readStrongBinder();
                    String[] _arg213 = data.createStringArray();
                    dumpProvider(_arg027, _arg120, _arg213);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelFileDescriptor _arg028 = data.readInt() != 0 ? ParcelFileDescriptor.CREATOR.createFromParcel(data) : null;
                    dumpDbInfo(_arg028, data.createStringArray());
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg029 = data.readStrongBinder();
                    unstableProviderDied(_arg029);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg030 = data.readStrongBinder();
                    IBinder _arg121 = data.readStrongBinder();
                    int _arg214 = data.readInt();
                    int _arg312 = data.readInt();
                    int _arg410 = data.readInt();
                    requestAssistContextExtras(_arg030, _arg121, _arg214, _arg312, _arg410);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg031 = data.readStrongBinder();
                    _arg15 = data.readInt() != 0;
                    scheduleTranslucentConversionComplete(_arg031, _arg15);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg032 = data.readInt();
                    setProcessState(_arg032);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    ProviderInfo _arg033 = data.readInt() != 0 ? ProviderInfo.CREATOR.createFromParcel(data) : null;
                    scheduleInstallProvider(_arg033);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg034 = data.readInt();
                    updateTimePrefs(_arg034);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg035 = data.readStrongBinder();
                    scheduleEnterAnimationComplete(_arg035);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg036 = data.createByteArray();
                    notifyCleartextNetwork(_arg036);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    startBinderTracking();
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    ParcelFileDescriptor _arg037 = data.readInt() != 0 ? ParcelFileDescriptor.CREATOR.createFromParcel(data) : null;
                    stopBinderTrackingAndDump(_arg037);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg038 = data.readStrongBinder();
                    scheduleLocalVoiceInteractionStarted(_arg038, IVoiceInteractor.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    handleTrustStorageUpdate();
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg039 = data.readString();
                    attachAgent(_arg039);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    ApplicationInfo _arg040 = data.readInt() != 0 ? ApplicationInfo.CREATOR.createFromParcel(data) : null;
                    scheduleApplicationInfoChanged(_arg040);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg041 = data.readLong();
                    setNetworkBlockSeq(_arg041);
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    ClientTransaction _arg042 = data.readInt() != 0 ? ClientTransaction.CREATOR.createFromParcel(data) : null;
                    scheduleTransaction(_arg042);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IApplicationThread {
            private IBinder mRemote;

            synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.app.IApplicationThread
            public synchronized void scheduleReceiver(Intent intent, ActivityInfo info, CompatibilityInfo compatInfo, int resultCode, String data, Bundle extras, boolean sync, int sendingUser, int processState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (compatInfo != null) {
                        _data.writeInt(1);
                        compatInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(resultCode);
                    _data.writeString(data);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sync ? 1 : 0);
                    _data.writeInt(sendingUser);
                    _data.writeInt(processState);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void scheduleCreateService(IBinder token, ServiceInfo info, CompatibilityInfo compatInfo, int processState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (compatInfo != null) {
                        _data.writeInt(1);
                        compatInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(processState);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void scheduleStopService(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void bindApplication(String packageName, ApplicationInfo info, List<ProviderInfo> providers, ComponentName testName, ProfilerInfo profilerInfo, Bundle testArguments, IInstrumentationWatcher testWatcher, IUiAutomationConnection uiAutomationConnection, int debugMode, boolean enableBinderTracking, boolean trackAllocation, boolean restrictedBackupMode, boolean persistent, Configuration config, CompatibilityInfo compatInfo, Map services, Bundle coreSettings, String buildSerial, boolean isAutofillCompatEnabled) throws RemoteException {
                int i;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                        if (info != null) {
                            _data.writeInt(1);
                            info.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeTypedList(providers);
                            if (testName != null) {
                                _data.writeInt(1);
                                testName.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (profilerInfo != null) {
                                _data.writeInt(1);
                                profilerInfo.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (testArguments != null) {
                                _data.writeInt(1);
                                testArguments.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeStrongBinder(testWatcher != null ? testWatcher.asBinder() : null);
                            _data.writeStrongBinder(uiAutomationConnection != null ? uiAutomationConnection.asBinder() : null);
                            try {
                                _data.writeInt(debugMode);
                            } catch (Throwable th) {
                                th = th;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
                try {
                    _data.writeInt(enableBinderTracking ? 1 : 0);
                    try {
                        _data.writeInt(trackAllocation ? 1 : 0);
                        _data.writeInt(restrictedBackupMode ? 1 : 0);
                        _data.writeInt(persistent ? 1 : 0);
                        if (config != null) {
                            _data.writeInt(1);
                            i = 0;
                            config.writeToParcel(_data, 0);
                        } else {
                            i = 0;
                            _data.writeInt(0);
                        }
                        if (compatInfo != null) {
                            _data.writeInt(1);
                            compatInfo.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(i);
                        }
                        _data.writeMap(services);
                        if (coreSettings != null) {
                            _data.writeInt(1);
                            coreSettings.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeString(buildSerial);
                        _data.writeInt(isAutofillCompatEnabled ? 1 : 0);
                        this.mRemote.transact(4, _data, null, 1);
                        _data.recycle();
                    } catch (Throwable th5) {
                        th = th5;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void runIsolatedEntryPoint(String entryPoint, String[] entryPointArgs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(entryPoint);
                    _data.writeStringArray(entryPointArgs);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void scheduleExit() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void scheduleServiceArgs(IBinder token, ParceledListSlice args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void updateTimeZone() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void processInBackground() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void scheduleBindService(IBinder token, Intent intent, boolean rebind, int processState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(rebind ? 1 : 0);
                    _data.writeInt(processState);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void scheduleUnbindService(IBinder token, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void dumpService(ParcelFileDescriptor fd, IBinder servicetoken, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(servicetoken);
                    _data.writeStringArray(args);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void scheduleRegisteredReceiver(IIntentReceiver receiver, Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky, int sendingUser, int processState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(resultCode);
                    _data.writeString(data);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(ordered ? 1 : 0);
                    _data.writeInt(sticky ? 1 : 0);
                    _data.writeInt(sendingUser);
                    _data.writeInt(processState);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void scheduleLowMemory() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void scheduleSleeping(IBinder token, boolean sleeping) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(sleeping ? 1 : 0);
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void profilerControl(boolean start, ProfilerInfo profilerInfo, int profileType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(start ? 1 : 0);
                    if (profilerInfo != null) {
                        _data.writeInt(1);
                        profilerInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(profileType);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void setSchedulingGroup(int group) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(group);
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void scheduleCreateBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo, int backupMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (app != null) {
                        _data.writeInt(1);
                        app.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (compatInfo != null) {
                        _data.writeInt(1);
                        compatInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(backupMode);
                    this.mRemote.transact(18, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void scheduleDestroyBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (app != null) {
                        _data.writeInt(1);
                        app.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (compatInfo != null) {
                        _data.writeInt(1);
                        compatInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(19, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void scheduleOnNewActivityOptions(IBinder token, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(20, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void scheduleSuicide() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void dispatchPackageBroadcast(int cmd, String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cmd);
                    _data.writeStringArray(packages);
                    this.mRemote.transact(22, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void scheduleCrash(String msg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(msg);
                    this.mRemote.transact(23, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void dumpHeap(boolean managed, boolean mallocInfo, boolean runGc, String path, ParcelFileDescriptor fd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(managed ? 1 : 0);
                    _data.writeInt(mallocInfo ? 1 : 0);
                    _data.writeInt(runGc ? 1 : 0);
                    _data.writeString(path);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(24, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void dumpActivity(ParcelFileDescriptor fd, IBinder servicetoken, String prefix, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(servicetoken);
                    _data.writeString(prefix);
                    _data.writeStringArray(args);
                    this.mRemote.transact(25, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void clearDnsCache() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void setHttpProxy(String proxy, String port, String exclList, Uri pacFileUrl) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(proxy);
                    _data.writeString(port);
                    _data.writeString(exclList);
                    if (pacFileUrl != null) {
                        _data.writeInt(1);
                        pacFileUrl.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(27, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void setCoreSettings(Bundle coreSettings) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (coreSettings != null) {
                        _data.writeInt(1);
                        coreSettings.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(28, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void updatePackageCompatibilityInfo(String pkg, CompatibilityInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(29, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void scheduleTrimMemory(int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(level);
                    this.mRemote.transact(30, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void dumpMemInfo(ParcelFileDescriptor fd, Debug.MemoryInfo mem, boolean checkin, boolean dumpInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (mem != null) {
                        _data.writeInt(1);
                        mem.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(checkin ? 1 : 0);
                    _data.writeInt(dumpInfo ? 1 : 0);
                    _data.writeInt(dumpDalvik ? 1 : 0);
                    _data.writeInt(dumpSummaryOnly ? 1 : 0);
                    _data.writeInt(dumpUnreachable ? 1 : 0);
                    _data.writeStringArray(args);
                    this.mRemote.transact(31, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void dumpMemInfoProto(ParcelFileDescriptor fd, Debug.MemoryInfo mem, boolean dumpInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (mem != null) {
                        _data.writeInt(1);
                        mem.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(dumpInfo ? 1 : 0);
                    _data.writeInt(dumpDalvik ? 1 : 0);
                    _data.writeInt(dumpSummaryOnly ? 1 : 0);
                    _data.writeInt(dumpUnreachable ? 1 : 0);
                    _data.writeStringArray(args);
                    this.mRemote.transact(32, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void dumpGfxInfo(ParcelFileDescriptor fd, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(args);
                    this.mRemote.transact(33, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void dumpProvider(ParcelFileDescriptor fd, IBinder servicetoken, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(servicetoken);
                    _data.writeStringArray(args);
                    this.mRemote.transact(34, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void dumpDbInfo(ParcelFileDescriptor fd, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(args);
                    this.mRemote.transact(35, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void unstableProviderDied(IBinder provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(provider);
                    this.mRemote.transact(36, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void requestAssistContextExtras(IBinder activityToken, IBinder requestToken, int requestType, int sessionId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    _data.writeStrongBinder(requestToken);
                    _data.writeInt(requestType);
                    _data.writeInt(sessionId);
                    _data.writeInt(flags);
                    this.mRemote.transact(37, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void scheduleTranslucentConversionComplete(IBinder token, boolean timeout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(timeout ? 1 : 0);
                    this.mRemote.transact(38, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void setProcessState(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    this.mRemote.transact(39, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void scheduleInstallProvider(ProviderInfo provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (provider != null) {
                        _data.writeInt(1);
                        provider.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(40, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void updateTimePrefs(int timeFormatPreference) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(timeFormatPreference);
                    this.mRemote.transact(41, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void scheduleEnterAnimationComplete(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    this.mRemote.transact(42, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void notifyCleartextNetwork(byte[] firstPacket) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(firstPacket);
                    this.mRemote.transact(43, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void startBinderTracking() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(44, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void stopBinderTrackingAndDump(ParcelFileDescriptor fd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(45, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void scheduleLocalVoiceInteractionStarted(IBinder token, IVoiceInteractor voiceInteractor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeStrongBinder(voiceInteractor != null ? voiceInteractor.asBinder() : null);
                    this.mRemote.transact(46, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void handleTrustStorageUpdate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(47, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void attachAgent(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    this.mRemote.transact(48, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void scheduleApplicationInfoChanged(ApplicationInfo ai) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ai != null) {
                        _data.writeInt(1);
                        ai.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(49, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void setNetworkBlockSeq(long procStateSeq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(procStateSeq);
                    this.mRemote.transact(50, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public synchronized void scheduleTransaction(ClientTransaction transaction) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (transaction != null) {
                        _data.writeInt(1);
                        transaction.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(51, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
