package android.app;

import android.accounts.AccountManager;
import android.accounts.IAccountManager;
import android.app.IAlarmManager;
import android.app.IWallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.app.admin.IDevicePolicyManager;
import android.app.job.IJobScheduler;
import android.app.job.JobScheduler;
import android.app.slice.SliceManager;
import android.app.timezone.RulesManager;
import android.app.trust.TrustManager;
import android.app.usage.IStorageStatsManager;
import android.app.usage.IUsageStatsManager;
import android.app.usage.NetworkStatsManager;
import android.app.usage.StorageStatsManager;
import android.app.usage.UsageStatsManager;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothManager;
import android.companion.CompanionDeviceManager;
import android.companion.ICompanionDeviceManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.IRestrictionsManager;
import android.content.RestrictionsManager;
import android.content.pm.CrossProfileApps;
import android.content.pm.ICrossProfileApps;
import android.content.pm.IShortcutService;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.hardware.ConsumerIrManager;
import android.hardware.ISerialManager;
import android.hardware.SensorManager;
import android.hardware.SerialManager;
import android.hardware.SystemSensorManager;
import android.hardware.camera2.CameraManager;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.IFingerprintService;
import android.hardware.hdmi.HdmiControlManager;
import android.hardware.hdmi.IHdmiControlService;
import android.hardware.input.InputManager;
import android.hardware.location.ContextHubManager;
import android.hardware.radio.RadioManager;
import android.hardware.usb.IUsbManager;
import android.hardware.usb.UsbManager;
import android.location.CountryDetector;
import android.location.ICountryDetector;
import android.location.ILocationManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.media.midi.IMidiManager;
import android.media.midi.MidiManager;
import android.media.projection.MediaProjectionManager;
import android.media.session.MediaSessionManager;
import android.media.soundtrigger.SoundTriggerManager;
import android.media.tv.ITvInputManager;
import android.media.tv.TvInputManager;
import android.net.ConnectivityManager;
import android.net.ConnectivityThread;
import android.net.EthernetManager;
import android.net.IConnectivityManager;
import android.net.IEthernetManager;
import android.net.IIpSecService;
import android.net.INetworkPolicyManager;
import android.net.IpSecManager;
import android.net.NetworkPolicyManager;
import android.net.NetworkScoreManager;
import android.net.NetworkWatchlistManager;
import android.net.lowpan.ILowpanManager;
import android.net.lowpan.LowpanManager;
import android.net.nsd.INsdManager;
import android.net.nsd.NsdManager;
import android.net.wifi.IWifiManager;
import android.net.wifi.IWifiScanner;
import android.net.wifi.RttManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiScanner;
import android.net.wifi.aware.IWifiAwareManager;
import android.net.wifi.aware.WifiAwareManager;
import android.net.wifi.p2p.IWifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.rtt.IWifiRttManager;
import android.net.wifi.rtt.WifiRttManager;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.BatteryStats;
import android.os.DeviceIdleManager;
import android.os.DropBoxManager;
import android.os.HardwarePropertiesManager;
import android.os.IBinder;
import android.os.IDeviceIdleController;
import android.os.IHardwarePropertiesManager;
import android.os.IPowerManager;
import android.os.IRecoverySystem;
import android.os.ISystemUpdateManager;
import android.os.IUserManager;
import android.os.IncidentManager;
import android.os.PowerManager;
import android.os.Process;
import android.os.RecoverySystem;
import android.os.ServiceManager;
import android.os.SystemUpdateManager;
import android.os.SystemVibrator;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.Vibrator;
import android.os.health.SystemHealthManager;
import android.os.storage.StorageManager;
import android.print.IPrintManager;
import android.print.PrintManager;
import android.service.oemlock.IOemLockService;
import android.service.oemlock.OemLockManager;
import android.service.persistentdata.IPersistentDataBlockService;
import android.service.persistentdata.PersistentDataBlockManager;
import android.service.vr.IVrManager;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.euicc.EuiccCardManager;
import android.telephony.euicc.EuiccManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.autofill.AutofillManager;
import android.view.autofill.IAutoFillManager;
import android.view.inputmethod.InputMethodManager;
import android.view.textclassifier.TextClassificationManager;
import android.view.textservice.TextServicesManager;
import com.android.internal.app.IAppOpsService;
import com.android.internal.app.IBatteryStats;
import com.android.internal.app.ISoundTriggerService;
import com.android.internal.appwidget.IAppWidgetService;
import com.android.internal.net.INetworkWatchlistManager;
import com.android.internal.os.IDropBoxManagerService;
import com.android.internal.policy.PhoneLayoutInflater;
import com.xiaopeng.aftersales.AfterSalesManager;
import com.xiaopeng.aftersales.IAfterSalesManager;
import java.util.HashMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SystemServiceRegistry {
    private static final String TAG = "SystemServiceRegistry";
    private static int sServiceCacheSize;
    private static final HashMap<Class<?>, String> SYSTEM_SERVICE_NAMES = new HashMap<>();
    private static final HashMap<String, ServiceFetcher<?>> SYSTEM_SERVICE_FETCHERS = new HashMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface ServiceFetcher<T> {
        T getService(ContextImpl contextImpl);
    }

    static /* synthetic */ int access$008() {
        int i = sServiceCacheSize;
        sServiceCacheSize = i + 1;
        return i;
    }

    static {
        registerService(Context.ACCESSIBILITY_SERVICE, AccessibilityManager.class, new CachedServiceFetcher<AccessibilityManager>() { // from class: android.app.SystemServiceRegistry.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public AccessibilityManager createService(ContextImpl ctx) {
                return AccessibilityManager.getInstance(ctx);
            }
        });
        registerService(Context.CAPTIONING_SERVICE, CaptioningManager.class, new CachedServiceFetcher<CaptioningManager>() { // from class: android.app.SystemServiceRegistry.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public CaptioningManager createService(ContextImpl ctx) {
                return new CaptioningManager(ctx);
            }
        });
        registerService("account", AccountManager.class, new CachedServiceFetcher<AccountManager>() { // from class: android.app.SystemServiceRegistry.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public AccountManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("account");
                IAccountManager service = IAccountManager.Stub.asInterface(b);
                return new AccountManager(ctx, service);
            }
        });
        registerService(Context.ACTIVITY_SERVICE, ActivityManager.class, new CachedServiceFetcher<ActivityManager>() { // from class: android.app.SystemServiceRegistry.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public ActivityManager createService(ContextImpl ctx) {
                return new ActivityManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        registerService("alarm", AlarmManager.class, new CachedServiceFetcher<AlarmManager>() { // from class: android.app.SystemServiceRegistry.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public AlarmManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("alarm");
                IAlarmManager service = IAlarmManager.Stub.asInterface(b);
                return new AlarmManager(service, ctx);
            }
        });
        registerService("audio", AudioManager.class, new CachedServiceFetcher<AudioManager>() { // from class: android.app.SystemServiceRegistry.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public AudioManager createService(ContextImpl ctx) {
                return new AudioManager(ctx);
            }
        });
        registerService(Context.MEDIA_ROUTER_SERVICE, MediaRouter.class, new CachedServiceFetcher<MediaRouter>() { // from class: android.app.SystemServiceRegistry.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public MediaRouter createService(ContextImpl ctx) {
                return new MediaRouter(ctx);
            }
        });
        registerService("bluetooth", BluetoothManager.class, new CachedServiceFetcher<BluetoothManager>() { // from class: android.app.SystemServiceRegistry.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public BluetoothManager createService(ContextImpl ctx) {
                return new BluetoothManager(ctx);
            }
        });
        registerService(Context.HDMI_CONTROL_SERVICE, HdmiControlManager.class, new StaticServiceFetcher<HdmiControlManager>() { // from class: android.app.SystemServiceRegistry.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            public HdmiControlManager createService() throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.HDMI_CONTROL_SERVICE);
                return new HdmiControlManager(IHdmiControlService.Stub.asInterface(b));
            }
        });
        registerService(Context.TEXT_CLASSIFICATION_SERVICE, TextClassificationManager.class, new CachedServiceFetcher<TextClassificationManager>() { // from class: android.app.SystemServiceRegistry.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public TextClassificationManager createService(ContextImpl ctx) {
                return new TextClassificationManager(ctx);
            }
        });
        registerService(Context.CLIPBOARD_SERVICE, ClipboardManager.class, new CachedServiceFetcher<ClipboardManager>() { // from class: android.app.SystemServiceRegistry.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public ClipboardManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new ClipboardManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        SYSTEM_SERVICE_NAMES.put(android.text.ClipboardManager.class, Context.CLIPBOARD_SERVICE);
        registerService(Context.CONNECTIVITY_SERVICE, ConnectivityManager.class, new StaticApplicationContextServiceFetcher<ConnectivityManager>() { // from class: android.app.SystemServiceRegistry.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticApplicationContextServiceFetcher
            public ConnectivityManager createService(Context context) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.CONNECTIVITY_SERVICE);
                IConnectivityManager service = IConnectivityManager.Stub.asInterface(b);
                return new ConnectivityManager(context, service);
            }
        });
        registerService("ipsec", IpSecManager.class, new CachedServiceFetcher<IpSecManager>() { // from class: android.app.SystemServiceRegistry.13
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public IpSecManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getService("ipsec");
                IIpSecService service = IIpSecService.Stub.asInterface(b);
                return new IpSecManager(ctx, service);
            }
        });
        registerService("country_detector", CountryDetector.class, new StaticServiceFetcher<CountryDetector>() { // from class: android.app.SystemServiceRegistry.14
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            public CountryDetector createService() throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("country_detector");
                return new CountryDetector(ICountryDetector.Stub.asInterface(b));
            }
        });
        registerService(Context.DEVICE_POLICY_SERVICE, DevicePolicyManager.class, new CachedServiceFetcher<DevicePolicyManager>() { // from class: android.app.SystemServiceRegistry.15
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public DevicePolicyManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.DEVICE_POLICY_SERVICE);
                return new DevicePolicyManager(ctx, IDevicePolicyManager.Stub.asInterface(b));
            }
        });
        registerService(Context.DOWNLOAD_SERVICE, DownloadManager.class, new CachedServiceFetcher<DownloadManager>() { // from class: android.app.SystemServiceRegistry.16
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public DownloadManager createService(ContextImpl ctx) {
                return new DownloadManager(ctx);
            }
        });
        registerService(Context.BATTERY_SERVICE, BatteryManager.class, new CachedServiceFetcher<BatteryManager>() { // from class: android.app.SystemServiceRegistry.17
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public BatteryManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBatteryStats stats = IBatteryStats.Stub.asInterface(ServiceManager.getServiceOrThrow(BatteryStats.SERVICE_NAME));
                return new BatteryManager(ctx, stats, null);
            }
        });
        registerService("nfc", NfcManager.class, new CachedServiceFetcher<NfcManager>() { // from class: android.app.SystemServiceRegistry.18
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public NfcManager createService(ContextImpl ctx) {
                return new NfcManager(ctx);
            }
        });
        registerService(Context.DROPBOX_SERVICE, DropBoxManager.class, new CachedServiceFetcher<DropBoxManager>() { // from class: android.app.SystemServiceRegistry.19
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public DropBoxManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.DROPBOX_SERVICE);
                IDropBoxManagerService service = IDropBoxManagerService.Stub.asInterface(b);
                return new DropBoxManager(ctx, service);
            }
        });
        registerService("input", InputManager.class, new StaticServiceFetcher<InputManager>() { // from class: android.app.SystemServiceRegistry.20
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            public InputManager createService() {
                return InputManager.getInstance();
            }
        });
        registerService(Context.DISPLAY_SERVICE, DisplayManager.class, new CachedServiceFetcher<DisplayManager>() { // from class: android.app.SystemServiceRegistry.21
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public DisplayManager createService(ContextImpl ctx) {
                return new DisplayManager(ctx.getOuterContext());
            }
        });
        registerService(Context.INPUT_METHOD_SERVICE, InputMethodManager.class, new StaticServiceFetcher<InputMethodManager>() { // from class: android.app.SystemServiceRegistry.22
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            public InputMethodManager createService() {
                return InputMethodManager.getInstance();
            }
        });
        registerService(Context.TEXT_SERVICES_MANAGER_SERVICE, TextServicesManager.class, new StaticServiceFetcher<TextServicesManager>() { // from class: android.app.SystemServiceRegistry.23
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            public TextServicesManager createService() {
                return TextServicesManager.getInstance();
            }
        });
        registerService(Context.KEYGUARD_SERVICE, KeyguardManager.class, new CachedServiceFetcher<KeyguardManager>() { // from class: android.app.SystemServiceRegistry.24
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public KeyguardManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new KeyguardManager(ctx);
            }
        });
        registerService(Context.LAYOUT_INFLATER_SERVICE, LayoutInflater.class, new CachedServiceFetcher<LayoutInflater>() { // from class: android.app.SystemServiceRegistry.25
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public LayoutInflater createService(ContextImpl ctx) {
                return new PhoneLayoutInflater(ctx.getOuterContext());
            }
        });
        registerService("location", LocationManager.class, new CachedServiceFetcher<LocationManager>() { // from class: android.app.SystemServiceRegistry.26
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public LocationManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("location");
                return new LocationManager(ctx, ILocationManager.Stub.asInterface(b));
            }
        });
        registerService(Context.NETWORK_POLICY_SERVICE, NetworkPolicyManager.class, new CachedServiceFetcher<NetworkPolicyManager>() { // from class: android.app.SystemServiceRegistry.27
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public NetworkPolicyManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new NetworkPolicyManager(ctx, INetworkPolicyManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.NETWORK_POLICY_SERVICE)));
            }
        });
        registerService(Context.NOTIFICATION_SERVICE, NotificationManager.class, new CachedServiceFetcher<NotificationManager>() { // from class: android.app.SystemServiceRegistry.28
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public NotificationManager createService(ContextImpl ctx) {
                Context outerContext = ctx.getOuterContext();
                return new NotificationManager(new ContextThemeWrapper(outerContext, Resources.selectSystemTheme(0, outerContext.getApplicationInfo().targetSdkVersion, 16973835, 16973935, 16974126, 16974130)), ctx.mMainThread.getHandler());
            }
        });
        registerService(Context.NSD_SERVICE, NsdManager.class, new CachedServiceFetcher<NsdManager>() { // from class: android.app.SystemServiceRegistry.29
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public NsdManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.NSD_SERVICE);
                INsdManager service = INsdManager.Stub.asInterface(b);
                return new NsdManager(ctx.getOuterContext(), service);
            }
        });
        registerService(Context.POWER_SERVICE, PowerManager.class, new CachedServiceFetcher<PowerManager>() { // from class: android.app.SystemServiceRegistry.30
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public PowerManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.POWER_SERVICE);
                IPowerManager service = IPowerManager.Stub.asInterface(b);
                return new PowerManager(ctx.getOuterContext(), service, ctx.mMainThread.getHandler());
            }
        });
        registerService("recovery", RecoverySystem.class, new CachedServiceFetcher<RecoverySystem>() { // from class: android.app.SystemServiceRegistry.31
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public RecoverySystem createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("recovery");
                IRecoverySystem service = IRecoverySystem.Stub.asInterface(b);
                return new RecoverySystem(service);
            }
        });
        registerService("search", SearchManager.class, new CachedServiceFetcher<SearchManager>() { // from class: android.app.SystemServiceRegistry.32
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public SearchManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new SearchManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        registerService(Context.SENSOR_SERVICE, SensorManager.class, new CachedServiceFetcher<SensorManager>() { // from class: android.app.SystemServiceRegistry.33
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public SensorManager createService(ContextImpl ctx) {
                return new SystemSensorManager(ctx.getOuterContext(), ctx.mMainThread.getHandler().getLooper());
            }
        });
        registerService(Context.STATS_MANAGER, StatsManager.class, new CachedServiceFetcher<StatsManager>() { // from class: android.app.SystemServiceRegistry.34
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public StatsManager createService(ContextImpl ctx) {
                return new StatsManager(ctx.getOuterContext());
            }
        });
        registerService("statusbar", StatusBarManager.class, new CachedServiceFetcher<StatusBarManager>() { // from class: android.app.SystemServiceRegistry.35
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public StatusBarManager createService(ContextImpl ctx) {
                return new StatusBarManager(ctx.getOuterContext());
            }
        });
        registerService(Context.STORAGE_SERVICE, StorageManager.class, new CachedServiceFetcher<StorageManager>() { // from class: android.app.SystemServiceRegistry.36
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public StorageManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new StorageManager(ctx, ctx.mMainThread.getHandler().getLooper());
            }
        });
        registerService(Context.STORAGE_STATS_SERVICE, StorageStatsManager.class, new CachedServiceFetcher<StorageStatsManager>() { // from class: android.app.SystemServiceRegistry.37
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public StorageStatsManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IStorageStatsManager service = IStorageStatsManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.STORAGE_STATS_SERVICE));
                return new StorageStatsManager(ctx, service);
            }
        });
        registerService(Context.SYSTEM_UPDATE_SERVICE, SystemUpdateManager.class, new CachedServiceFetcher<SystemUpdateManager>() { // from class: android.app.SystemServiceRegistry.38
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public SystemUpdateManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.SYSTEM_UPDATE_SERVICE);
                ISystemUpdateManager service = ISystemUpdateManager.Stub.asInterface(b);
                return new SystemUpdateManager(service);
            }
        });
        registerService("phone", TelephonyManager.class, new CachedServiceFetcher<TelephonyManager>() { // from class: android.app.SystemServiceRegistry.39
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public TelephonyManager createService(ContextImpl ctx) {
                return new TelephonyManager(ctx.getOuterContext());
            }
        });
        registerService(Context.TELEPHONY_SUBSCRIPTION_SERVICE, SubscriptionManager.class, new CachedServiceFetcher<SubscriptionManager>() { // from class: android.app.SystemServiceRegistry.40
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public SubscriptionManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new SubscriptionManager(ctx.getOuterContext());
            }
        });
        registerService(Context.CARRIER_CONFIG_SERVICE, CarrierConfigManager.class, new CachedServiceFetcher<CarrierConfigManager>() { // from class: android.app.SystemServiceRegistry.41
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public CarrierConfigManager createService(ContextImpl ctx) {
                return new CarrierConfigManager();
            }
        });
        registerService(Context.TELECOM_SERVICE, TelecomManager.class, new CachedServiceFetcher<TelecomManager>() { // from class: android.app.SystemServiceRegistry.42
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public TelecomManager createService(ContextImpl ctx) {
                return new TelecomManager(ctx.getOuterContext());
            }
        });
        registerService(Context.EUICC_SERVICE, EuiccManager.class, new CachedServiceFetcher<EuiccManager>() { // from class: android.app.SystemServiceRegistry.43
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public EuiccManager createService(ContextImpl ctx) {
                return new EuiccManager(ctx.getOuterContext());
            }
        });
        registerService(Context.EUICC_CARD_SERVICE, EuiccCardManager.class, new CachedServiceFetcher<EuiccCardManager>() { // from class: android.app.SystemServiceRegistry.44
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public EuiccCardManager createService(ContextImpl ctx) {
                return new EuiccCardManager(ctx.getOuterContext());
            }
        });
        registerService(Context.UI_MODE_SERVICE, UiModeManager.class, new CachedServiceFetcher<UiModeManager>() { // from class: android.app.SystemServiceRegistry.45
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public UiModeManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new UiModeManager();
            }
        });
        registerService(Context.USB_SERVICE, UsbManager.class, new CachedServiceFetcher<UsbManager>() { // from class: android.app.SystemServiceRegistry.46
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public UsbManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.USB_SERVICE);
                return new UsbManager(ctx, IUsbManager.Stub.asInterface(b));
            }
        });
        registerService(Context.SERIAL_SERVICE, SerialManager.class, new CachedServiceFetcher<SerialManager>() { // from class: android.app.SystemServiceRegistry.47
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public SerialManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.SERIAL_SERVICE);
                return new SerialManager(ctx, ISerialManager.Stub.asInterface(b));
            }
        });
        registerService(Context.VIBRATOR_SERVICE, Vibrator.class, new CachedServiceFetcher<Vibrator>() { // from class: android.app.SystemServiceRegistry.48
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public Vibrator createService(ContextImpl ctx) {
                return new SystemVibrator(ctx);
            }
        });
        registerService(Context.WALLPAPER_SERVICE, WallpaperManager.class, new CachedServiceFetcher<WallpaperManager>() { // from class: android.app.SystemServiceRegistry.49
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public WallpaperManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b;
                if (ctx.getApplicationInfo().targetSdkVersion >= 28) {
                    b = ServiceManager.getServiceOrThrow(Context.WALLPAPER_SERVICE);
                } else {
                    b = ServiceManager.getService(Context.WALLPAPER_SERVICE);
                }
                IWallpaperManager service = IWallpaperManager.Stub.asInterface(b);
                return new WallpaperManager(service, ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        registerService(Context.LOWPAN_SERVICE, LowpanManager.class, new CachedServiceFetcher<LowpanManager>() { // from class: android.app.SystemServiceRegistry.50
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public LowpanManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.LOWPAN_SERVICE);
                ILowpanManager service = ILowpanManager.Stub.asInterface(b);
                return new LowpanManager(ctx.getOuterContext(), service, ConnectivityThread.getInstanceLooper());
            }
        });
        registerService("wifi", WifiManager.class, new CachedServiceFetcher<WifiManager>() { // from class: android.app.SystemServiceRegistry.51
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public WifiManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("wifi");
                IWifiManager service = IWifiManager.Stub.asInterface(b);
                return new WifiManager(ctx.getOuterContext(), service, ConnectivityThread.getInstanceLooper());
            }
        });
        registerService(Context.WIFI_P2P_SERVICE, WifiP2pManager.class, new StaticServiceFetcher<WifiP2pManager>() { // from class: android.app.SystemServiceRegistry.52
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            public WifiP2pManager createService() throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.WIFI_P2P_SERVICE);
                IWifiP2pManager service = IWifiP2pManager.Stub.asInterface(b);
                return new WifiP2pManager(service);
            }
        });
        registerService(Context.WIFI_AWARE_SERVICE, WifiAwareManager.class, new CachedServiceFetcher<WifiAwareManager>() { // from class: android.app.SystemServiceRegistry.53
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public WifiAwareManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.WIFI_AWARE_SERVICE);
                IWifiAwareManager service = IWifiAwareManager.Stub.asInterface(b);
                if (service == null) {
                    return null;
                }
                return new WifiAwareManager(ctx.getOuterContext(), service);
            }
        });
        registerService(Context.WIFI_SCANNING_SERVICE, WifiScanner.class, new CachedServiceFetcher<WifiScanner>() { // from class: android.app.SystemServiceRegistry.54
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public WifiScanner createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.WIFI_SCANNING_SERVICE);
                IWifiScanner service = IWifiScanner.Stub.asInterface(b);
                return new WifiScanner(ctx.getOuterContext(), service, ConnectivityThread.getInstanceLooper());
            }
        });
        registerService(Context.WIFI_RTT_SERVICE, RttManager.class, new CachedServiceFetcher<RttManager>() { // from class: android.app.SystemServiceRegistry.55
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public RttManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.WIFI_RTT_RANGING_SERVICE);
                IWifiRttManager service = IWifiRttManager.Stub.asInterface(b);
                return new RttManager(ctx.getOuterContext(), new WifiRttManager(ctx.getOuterContext(), service));
            }
        });
        registerService(Context.WIFI_RTT_RANGING_SERVICE, WifiRttManager.class, new CachedServiceFetcher<WifiRttManager>() { // from class: android.app.SystemServiceRegistry.56
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public WifiRttManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.WIFI_RTT_RANGING_SERVICE);
                IWifiRttManager service = IWifiRttManager.Stub.asInterface(b);
                return new WifiRttManager(ctx.getOuterContext(), service);
            }
        });
        registerService("ethernet", EthernetManager.class, new CachedServiceFetcher<EthernetManager>() { // from class: android.app.SystemServiceRegistry.57
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public EthernetManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("ethernet");
                IEthernetManager service = IEthernetManager.Stub.asInterface(b);
                return new EthernetManager(ctx.getOuterContext(), service);
            }
        });
        registerService(Context.WINDOW_SERVICE, WindowManager.class, new CachedServiceFetcher<WindowManager>() { // from class: android.app.SystemServiceRegistry.58
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public WindowManager createService(ContextImpl ctx) {
                return new WindowManagerImpl(ctx);
            }
        });
        registerService("user", UserManager.class, new CachedServiceFetcher<UserManager>() { // from class: android.app.SystemServiceRegistry.59
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public UserManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("user");
                IUserManager service = IUserManager.Stub.asInterface(b);
                return new UserManager(ctx, service);
            }
        });
        registerService(Context.APP_OPS_SERVICE, AppOpsManager.class, new CachedServiceFetcher<AppOpsManager>() { // from class: android.app.SystemServiceRegistry.60
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public AppOpsManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.APP_OPS_SERVICE);
                IAppOpsService service = IAppOpsService.Stub.asInterface(b);
                return new AppOpsManager(ctx, service);
            }
        });
        registerService(Context.CAMERA_SERVICE, CameraManager.class, new CachedServiceFetcher<CameraManager>() { // from class: android.app.SystemServiceRegistry.61
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public CameraManager createService(ContextImpl ctx) {
                return new CameraManager(ctx);
            }
        });
        registerService(Context.LAUNCHER_APPS_SERVICE, LauncherApps.class, new CachedServiceFetcher<LauncherApps>() { // from class: android.app.SystemServiceRegistry.62
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public LauncherApps createService(ContextImpl ctx) {
                return new LauncherApps(ctx);
            }
        });
        registerService(Context.RESTRICTIONS_SERVICE, RestrictionsManager.class, new CachedServiceFetcher<RestrictionsManager>() { // from class: android.app.SystemServiceRegistry.63
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public RestrictionsManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.RESTRICTIONS_SERVICE);
                IRestrictionsManager service = IRestrictionsManager.Stub.asInterface(b);
                return new RestrictionsManager(ctx, service);
            }
        });
        registerService(Context.PRINT_SERVICE, PrintManager.class, new CachedServiceFetcher<PrintManager>() { // from class: android.app.SystemServiceRegistry.64
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public PrintManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IPrintManager service = null;
                if (ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_PRINTING)) {
                    service = IPrintManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.PRINT_SERVICE));
                }
                int userId = ctx.getUserId();
                int appId = UserHandle.getAppId(ctx.getApplicationInfo().uid);
                return new PrintManager(ctx.getOuterContext(), service, userId, appId);
            }
        });
        registerService(Context.COMPANION_DEVICE_SERVICE, CompanionDeviceManager.class, new CachedServiceFetcher<CompanionDeviceManager>() { // from class: android.app.SystemServiceRegistry.65
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public CompanionDeviceManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                ICompanionDeviceManager service = null;
                if (ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_COMPANION_DEVICE_SETUP)) {
                    service = ICompanionDeviceManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.COMPANION_DEVICE_SERVICE));
                }
                return new CompanionDeviceManager(service, ctx.getOuterContext());
            }
        });
        registerService(Context.CONSUMER_IR_SERVICE, ConsumerIrManager.class, new CachedServiceFetcher<ConsumerIrManager>() { // from class: android.app.SystemServiceRegistry.66
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public ConsumerIrManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new ConsumerIrManager(ctx);
            }
        });
        registerService(Context.MEDIA_SESSION_SERVICE, MediaSessionManager.class, new CachedServiceFetcher<MediaSessionManager>() { // from class: android.app.SystemServiceRegistry.67
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public MediaSessionManager createService(ContextImpl ctx) {
                return new MediaSessionManager(ctx);
            }
        });
        registerService(Context.TRUST_SERVICE, TrustManager.class, new StaticServiceFetcher<TrustManager>() { // from class: android.app.SystemServiceRegistry.68
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            public TrustManager createService() throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.TRUST_SERVICE);
                return new TrustManager(b);
            }
        });
        registerService(Context.FINGERPRINT_SERVICE, FingerprintManager.class, new CachedServiceFetcher<FingerprintManager>() { // from class: android.app.SystemServiceRegistry.69
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public FingerprintManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder binder;
                if (ctx.getApplicationInfo().targetSdkVersion >= 26) {
                    binder = ServiceManager.getServiceOrThrow(Context.FINGERPRINT_SERVICE);
                } else {
                    binder = ServiceManager.getService(Context.FINGERPRINT_SERVICE);
                }
                IFingerprintService service = IFingerprintService.Stub.asInterface(binder);
                return new FingerprintManager(ctx.getOuterContext(), service);
            }
        });
        registerService(Context.TV_INPUT_SERVICE, TvInputManager.class, new CachedServiceFetcher<TvInputManager>() { // from class: android.app.SystemServiceRegistry.70
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public TvInputManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder iBinder = ServiceManager.getServiceOrThrow(Context.TV_INPUT_SERVICE);
                ITvInputManager service = ITvInputManager.Stub.asInterface(iBinder);
                return new TvInputManager(service, ctx.getUserId());
            }
        });
        registerService(Context.NETWORK_SCORE_SERVICE, NetworkScoreManager.class, new CachedServiceFetcher<NetworkScoreManager>() { // from class: android.app.SystemServiceRegistry.71
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public NetworkScoreManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new NetworkScoreManager(ctx);
            }
        });
        registerService(Context.USAGE_STATS_SERVICE, UsageStatsManager.class, new CachedServiceFetcher<UsageStatsManager>() { // from class: android.app.SystemServiceRegistry.72
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public UsageStatsManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder iBinder = ServiceManager.getServiceOrThrow(Context.USAGE_STATS_SERVICE);
                IUsageStatsManager service = IUsageStatsManager.Stub.asInterface(iBinder);
                return new UsageStatsManager(ctx.getOuterContext(), service);
            }
        });
        registerService(Context.NETWORK_STATS_SERVICE, NetworkStatsManager.class, new CachedServiceFetcher<NetworkStatsManager>() { // from class: android.app.SystemServiceRegistry.73
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public NetworkStatsManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new NetworkStatsManager(ctx.getOuterContext());
            }
        });
        registerService(Context.JOB_SCHEDULER_SERVICE, JobScheduler.class, new StaticServiceFetcher<JobScheduler>() { // from class: android.app.SystemServiceRegistry.74
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            public JobScheduler createService() throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.JOB_SCHEDULER_SERVICE);
                return new JobSchedulerImpl(IJobScheduler.Stub.asInterface(b));
            }
        });
        registerService(Context.PERSISTENT_DATA_BLOCK_SERVICE, PersistentDataBlockManager.class, new StaticServiceFetcher<PersistentDataBlockManager>() { // from class: android.app.SystemServiceRegistry.75
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            public PersistentDataBlockManager createService() throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.PERSISTENT_DATA_BLOCK_SERVICE);
                IPersistentDataBlockService persistentDataBlockService = IPersistentDataBlockService.Stub.asInterface(b);
                if (persistentDataBlockService != null) {
                    return new PersistentDataBlockManager(persistentDataBlockService);
                }
                return null;
            }
        });
        registerService(Context.OEM_LOCK_SERVICE, OemLockManager.class, new StaticServiceFetcher<OemLockManager>() { // from class: android.app.SystemServiceRegistry.76
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.StaticServiceFetcher
            public OemLockManager createService() throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.OEM_LOCK_SERVICE);
                IOemLockService oemLockService = IOemLockService.Stub.asInterface(b);
                if (oemLockService != null) {
                    return new OemLockManager(oemLockService);
                }
                return null;
            }
        });
        registerService(Context.MEDIA_PROJECTION_SERVICE, MediaProjectionManager.class, new CachedServiceFetcher<MediaProjectionManager>() { // from class: android.app.SystemServiceRegistry.77
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public MediaProjectionManager createService(ContextImpl ctx) {
                return new MediaProjectionManager(ctx);
            }
        });
        registerService(Context.APPWIDGET_SERVICE, AppWidgetManager.class, new CachedServiceFetcher<AppWidgetManager>() { // from class: android.app.SystemServiceRegistry.78
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public AppWidgetManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.APPWIDGET_SERVICE);
                return new AppWidgetManager(ctx, IAppWidgetService.Stub.asInterface(b));
            }
        });
        registerService("midi", MidiManager.class, new CachedServiceFetcher<MidiManager>() { // from class: android.app.SystemServiceRegistry.79
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public MidiManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("midi");
                return new MidiManager(IMidiManager.Stub.asInterface(b));
            }
        });
        registerService(Context.RADIO_SERVICE, RadioManager.class, new CachedServiceFetcher<RadioManager>() { // from class: android.app.SystemServiceRegistry.80
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public RadioManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new RadioManager(ctx);
            }
        });
        registerService(Context.HARDWARE_PROPERTIES_SERVICE, HardwarePropertiesManager.class, new CachedServiceFetcher<HardwarePropertiesManager>() { // from class: android.app.SystemServiceRegistry.81
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public HardwarePropertiesManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.HARDWARE_PROPERTIES_SERVICE);
                IHardwarePropertiesManager service = IHardwarePropertiesManager.Stub.asInterface(b);
                return new HardwarePropertiesManager(ctx, service);
            }
        });
        registerService(Context.SOUND_TRIGGER_SERVICE, SoundTriggerManager.class, new CachedServiceFetcher<SoundTriggerManager>() { // from class: android.app.SystemServiceRegistry.82
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public SoundTriggerManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.SOUND_TRIGGER_SERVICE);
                return new SoundTriggerManager(ctx, ISoundTriggerService.Stub.asInterface(b));
            }
        });
        registerService("shortcut", ShortcutManager.class, new CachedServiceFetcher<ShortcutManager>() { // from class: android.app.SystemServiceRegistry.83
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public ShortcutManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow("shortcut");
                return new ShortcutManager(ctx, IShortcutService.Stub.asInterface(b));
            }
        });
        registerService(Context.NETWORK_WATCHLIST_SERVICE, NetworkWatchlistManager.class, new CachedServiceFetcher<NetworkWatchlistManager>() { // from class: android.app.SystemServiceRegistry.84
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public NetworkWatchlistManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.NETWORK_WATCHLIST_SERVICE);
                return new NetworkWatchlistManager(ctx, INetworkWatchlistManager.Stub.asInterface(b));
            }
        });
        registerService(Context.SYSTEM_HEALTH_SERVICE, SystemHealthManager.class, new CachedServiceFetcher<SystemHealthManager>() { // from class: android.app.SystemServiceRegistry.85
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public SystemHealthManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(BatteryStats.SERVICE_NAME);
                return new SystemHealthManager(IBatteryStats.Stub.asInterface(b));
            }
        });
        registerService(Context.CONTEXTHUB_SERVICE, ContextHubManager.class, new CachedServiceFetcher<ContextHubManager>() { // from class: android.app.SystemServiceRegistry.86
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public ContextHubManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new ContextHubManager(ctx.getOuterContext(), ctx.mMainThread.getHandler().getLooper());
            }
        });
        registerService(Context.INCIDENT_SERVICE, IncidentManager.class, new CachedServiceFetcher<IncidentManager>() { // from class: android.app.SystemServiceRegistry.87
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public IncidentManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new IncidentManager(ctx);
            }
        });
        registerService(Context.AUTOFILL_MANAGER_SERVICE, AutofillManager.class, new CachedServiceFetcher<AutofillManager>() { // from class: android.app.SystemServiceRegistry.88
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public AutofillManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getService(Context.AUTOFILL_MANAGER_SERVICE);
                IAutoFillManager service = IAutoFillManager.Stub.asInterface(b);
                return new AutofillManager(ctx.getOuterContext(), service);
            }
        });
        registerService(Context.VR_SERVICE, VrManager.class, new CachedServiceFetcher<VrManager>() { // from class: android.app.SystemServiceRegistry.89
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public VrManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.VR_SERVICE);
                return new VrManager(IVrManager.Stub.asInterface(b));
            }
        });
        registerService(Context.TIME_ZONE_RULES_MANAGER_SERVICE, RulesManager.class, new CachedServiceFetcher<RulesManager>() { // from class: android.app.SystemServiceRegistry.90
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public RulesManager createService(ContextImpl ctx) {
                return new RulesManager(ctx.getOuterContext());
            }
        });
        registerService(Context.CROSS_PROFILE_APPS_SERVICE, CrossProfileApps.class, new CachedServiceFetcher<CrossProfileApps>() { // from class: android.app.SystemServiceRegistry.91
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public CrossProfileApps createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getServiceOrThrow(Context.CROSS_PROFILE_APPS_SERVICE);
                return new CrossProfileApps(ctx.getOuterContext(), ICrossProfileApps.Stub.asInterface(b));
            }
        });
        registerService("slice", SliceManager.class, new CachedServiceFetcher<SliceManager>() { // from class: android.app.SystemServiceRegistry.92
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public SliceManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                return new SliceManager(ctx.getOuterContext(), ctx.mMainThread.getHandler());
            }
        });
        registerService(Context.DEVICE_IDLE_CONTROLLER, DeviceIdleManager.class, new CachedServiceFetcher<DeviceIdleManager>() { // from class: android.app.SystemServiceRegistry.93
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public DeviceIdleManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IDeviceIdleController service = IDeviceIdleController.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.DEVICE_IDLE_CONTROLLER));
                return new DeviceIdleManager(ctx.getOuterContext(), service);
            }
        });
        registerService("xiaopeng_aftersales", AfterSalesManager.class, new CachedServiceFetcher<AfterSalesManager>() { // from class: android.app.SystemServiceRegistry.94
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.app.SystemServiceRegistry.CachedServiceFetcher
            public AfterSalesManager createService(ContextImpl ctx) throws ServiceManager.ServiceNotFoundException {
                IBinder b = ServiceManager.getService("xiaopeng_aftersales");
                IAfterSalesManager service = IAfterSalesManager.Stub.asInterface(b);
                return new AfterSalesManager(ctx.getOuterContext(), service);
            }
        });
    }

    private SystemServiceRegistry() {
    }

    public static Object[] createServiceCache() {
        return new Object[sServiceCacheSize];
    }

    public static Object getSystemService(ContextImpl ctx, String name) {
        ServiceFetcher<?> fetcher = SYSTEM_SERVICE_FETCHERS.get(name);
        if (fetcher != null) {
            return fetcher.getService(ctx);
        }
        return null;
    }

    public static String getSystemServiceName(Class<?> serviceClass) {
        return SYSTEM_SERVICE_NAMES.get(serviceClass);
    }

    private static <T> void registerService(String serviceName, Class<T> serviceClass, ServiceFetcher<T> serviceFetcher) {
        SYSTEM_SERVICE_NAMES.put(serviceClass, serviceName);
        SYSTEM_SERVICE_FETCHERS.put(serviceName, serviceFetcher);
    }

    /* loaded from: classes.dex */
    static abstract class CachedServiceFetcher<T> implements ServiceFetcher<T> {
        private final int mCacheIndex = SystemServiceRegistry.access$008();

        public abstract T createService(ContextImpl contextImpl) throws ServiceManager.ServiceNotFoundException;

        CachedServiceFetcher() {
        }

        @Override // android.app.SystemServiceRegistry.ServiceFetcher
        public final T getService(ContextImpl ctx) {
            T service;
            Object[] cache = ctx.mServiceCache;
            int[] gates = ctx.mServiceInitializationStateArray;
            while (true) {
                boolean doInitialize = false;
                synchronized (cache) {
                    service = (T) cache[this.mCacheIndex];
                    if (service != null || gates[this.mCacheIndex] == 3) {
                        break;
                    }
                    if (gates[this.mCacheIndex] == 2) {
                        gates[this.mCacheIndex] = 0;
                    }
                    if (gates[this.mCacheIndex] == 0) {
                        doInitialize = true;
                        gates[this.mCacheIndex] = 1;
                    }
                    if (doInitialize) {
                        try {
                            try {
                                T service2 = createService(ctx);
                                synchronized (cache) {
                                    cache[this.mCacheIndex] = service2;
                                    gates[this.mCacheIndex] = 2;
                                    cache.notifyAll();
                                }
                                return service2;
                            } catch (ServiceManager.ServiceNotFoundException e) {
                                SystemServiceRegistry.onServiceNotFound(e);
                                synchronized (cache) {
                                    cache[this.mCacheIndex] = null;
                                    gates[this.mCacheIndex] = 3;
                                    cache.notifyAll();
                                    return null;
                                }
                            }
                        } catch (Throwable th) {
                            synchronized (cache) {
                                cache[this.mCacheIndex] = null;
                                gates[this.mCacheIndex] = 3;
                                cache.notifyAll();
                                throw th;
                            }
                        }
                    }
                    synchronized (cache) {
                        while (gates[this.mCacheIndex] < 2) {
                            try {
                                cache.wait();
                            } catch (InterruptedException e2) {
                                Log.w(SystemServiceRegistry.TAG, "getService() interrupted");
                                Thread.currentThread().interrupt();
                                return null;
                            }
                        }
                    }
                }
            }
            return service;
        }
    }

    /* loaded from: classes.dex */
    static abstract class StaticServiceFetcher<T> implements ServiceFetcher<T> {
        private T mCachedInstance;

        public abstract T createService() throws ServiceManager.ServiceNotFoundException;

        StaticServiceFetcher() {
        }

        @Override // android.app.SystemServiceRegistry.ServiceFetcher
        public final T getService(ContextImpl ctx) {
            T t;
            synchronized (this) {
                if (this.mCachedInstance == null) {
                    try {
                        this.mCachedInstance = createService();
                    } catch (ServiceManager.ServiceNotFoundException e) {
                        SystemServiceRegistry.onServiceNotFound(e);
                    }
                }
                t = this.mCachedInstance;
            }
            return t;
        }
    }

    /* loaded from: classes.dex */
    static abstract class StaticApplicationContextServiceFetcher<T> implements ServiceFetcher<T> {
        private T mCachedInstance;

        public abstract T createService(Context context) throws ServiceManager.ServiceNotFoundException;

        StaticApplicationContextServiceFetcher() {
        }

        @Override // android.app.SystemServiceRegistry.ServiceFetcher
        public final T getService(ContextImpl ctx) {
            T t;
            synchronized (this) {
                if (this.mCachedInstance == null) {
                    Context appContext = ctx.getApplicationContext();
                    try {
                        this.mCachedInstance = createService(appContext != null ? appContext : ctx);
                    } catch (ServiceManager.ServiceNotFoundException e) {
                        SystemServiceRegistry.onServiceNotFound(e);
                    }
                }
                t = this.mCachedInstance;
            }
            return t;
        }
    }

    public static void onServiceNotFound(ServiceManager.ServiceNotFoundException e) {
        if (Process.myUid() < 10000) {
            Log.wtf(TAG, e.getMessage(), e);
        } else {
            Log.w(TAG, e.getMessage());
        }
    }
}
