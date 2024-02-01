package android.net.wifi;

import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.WorkSource;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.util.AsyncChannel;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SystemApi
/* loaded from: classes2.dex */
public class WifiScanner {
    private static final int BASE = 159744;
    public static final int CMD_DEREGISTER_SCAN_LISTENER = 159772;
    public static final int CMD_DISABLE = 159775;
    public static final int CMD_ENABLE = 159774;
    public static final int CMD_FULL_SCAN_RESULT = 159764;
    public static final int CMD_GET_SCAN_RESULTS = 159748;
    public static final int CMD_GET_SINGLE_SCAN_RESULTS = 159773;
    public static final int CMD_OP_FAILED = 159762;
    public static final int CMD_OP_SUCCEEDED = 159761;
    public static final int CMD_PNO_NETWORK_FOUND = 159770;
    public static final int CMD_REGISTER_SCAN_LISTENER = 159771;
    public static final int CMD_SCAN_RESULT = 159749;
    public static final int CMD_SINGLE_SCAN_COMPLETED = 159767;
    public static final int CMD_START_BACKGROUND_SCAN = 159746;
    public static final int CMD_START_PNO_SCAN = 159768;
    public static final int CMD_START_SINGLE_SCAN = 159765;
    public static final int CMD_STOP_BACKGROUND_SCAN = 159747;
    public static final int CMD_STOP_PNO_SCAN = 159769;
    public static final int CMD_STOP_SINGLE_SCAN = 159766;
    private static final boolean DBG = false;
    public static final String GET_AVAILABLE_CHANNELS_EXTRA = "Channels";
    private static final int INVALID_KEY = 0;
    public static final int MAX_SCAN_PERIOD_MS = 1024000;
    public static final int MIN_SCAN_PERIOD_MS = 1000;
    public static final String PNO_PARAMS_PNO_SETTINGS_KEY = "PnoSettings";
    public static final String PNO_PARAMS_SCAN_SETTINGS_KEY = "ScanSettings";
    public static final int REASON_DUPLICATE_REQEUST = -5;
    public static final int REASON_INVALID_LISTENER = -2;
    public static final int REASON_INVALID_REQUEST = -3;
    public static final int REASON_NOT_AUTHORIZED = -4;
    public static final int REASON_SUCCEEDED = 0;
    public static final int REASON_UNSPECIFIED = -1;
    @Deprecated
    public static final int REPORT_EVENT_AFTER_BUFFER_FULL = 0;
    public static final int REPORT_EVENT_AFTER_EACH_SCAN = 1;
    public static final int REPORT_EVENT_FULL_SCAN_RESULT = 2;
    public static final int REPORT_EVENT_NO_BATCH = 4;
    public static final String REQUEST_PACKAGE_NAME_KEY = "PackageName";
    public static final String SCAN_PARAMS_SCAN_SETTINGS_KEY = "ScanSettings";
    public static final String SCAN_PARAMS_WORK_SOURCE_KEY = "WorkSource";
    private static final String TAG = "WifiScanner";
    public static final int TYPE_HIGH_ACCURACY = 2;
    public static final int TYPE_LOW_LATENCY = 0;
    public static final int TYPE_LOW_POWER = 1;
    public static final int WIFI_BAND_24_GHZ = 1;
    public static final int WIFI_BAND_5_GHZ = 2;
    public static final int WIFI_BAND_5_GHZ_DFS_ONLY = 4;
    public static final int WIFI_BAND_5_GHZ_WITH_DFS = 6;
    public static final int WIFI_BAND_BOTH = 3;
    public static final int WIFI_BAND_BOTH_WITH_DFS = 7;
    public static final int WIFI_BAND_UNSPECIFIED = 0;
    private AsyncChannel mAsyncChannel;
    private Context mContext;
    private final Handler mInternalHandler;
    private int mListenerKey = 1;
    private final SparseArray mListenerMap = new SparseArray();
    private final Object mListenerMapLock = new Object();
    private IWifiScanner mService;

    @SystemApi
    /* loaded from: classes2.dex */
    public interface ActionListener {
        void onFailure(int i, String str);

        void onSuccess();
    }

    @Deprecated
    /* loaded from: classes2.dex */
    public static class BssidInfo {
        public String bssid;
        public int frequencyHint;
        public int high;
        public int low;
    }

    @Deprecated
    /* loaded from: classes2.dex */
    public interface BssidListener extends ActionListener {
        void onFound(ScanResult[] scanResultArr);

        void onLost(ScanResult[] scanResultArr);
    }

    /* loaded from: classes2.dex */
    public interface PnoScanListener extends ScanListener {
        void onPnoNetworkFound(ScanResult[] scanResultArr);
    }

    /* loaded from: classes2.dex */
    public interface ScanListener extends ActionListener {
        void onFullResult(ScanResult scanResult);

        void onPeriodChanged(int i);

        void onResults(ScanData[] scanDataArr);
    }

    @Deprecated
    /* loaded from: classes2.dex */
    public interface WifiChangeListener extends ActionListener {
        void onChanging(ScanResult[] scanResultArr);

        void onQuiescence(ScanResult[] scanResultArr);
    }

    public List<Integer> getAvailableChannels(int band) {
        try {
            Bundle bundle = this.mService.getAvailableChannels(band);
            return bundle.getIntegerArrayList(GET_AVAILABLE_CHANNELS_EXTRA);
        } catch (RemoteException e) {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static class ChannelSpec {
        public int frequency;
        public boolean passive = false;
        public int dwellTimeMS = 0;

        public ChannelSpec(int frequency) {
            this.frequency = frequency;
        }
    }

    /* loaded from: classes2.dex */
    public static class ScanSettings implements Parcelable {
        public static final Parcelable.Creator<ScanSettings> CREATOR = new Parcelable.Creator<ScanSettings>() { // from class: android.net.wifi.WifiScanner.ScanSettings.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ScanSettings createFromParcel(Parcel in) {
                ScanSettings settings = new ScanSettings();
                settings.band = in.readInt();
                settings.periodInMs = in.readInt();
                settings.reportEvents = in.readInt();
                settings.numBssidsPerScan = in.readInt();
                settings.maxScansToCache = in.readInt();
                settings.maxPeriodInMs = in.readInt();
                settings.stepCount = in.readInt();
                settings.isPnoScan = in.readInt() == 1;
                settings.type = in.readInt();
                settings.ignoreLocationSettings = in.readInt() == 1;
                settings.hideFromAppOps = in.readInt() == 1;
                int num_channels = in.readInt();
                settings.channels = new ChannelSpec[num_channels];
                for (int i = 0; i < num_channels; i++) {
                    int frequency = in.readInt();
                    ChannelSpec spec = new ChannelSpec(frequency);
                    spec.dwellTimeMS = in.readInt();
                    spec.passive = in.readInt() == 1;
                    settings.channels[i] = spec;
                }
                int numNetworks = in.readInt();
                settings.hiddenNetworks = new HiddenNetwork[numNetworks];
                for (int i2 = 0; i2 < numNetworks; i2++) {
                    String ssid = in.readString();
                    settings.hiddenNetworks[i2] = new HiddenNetwork(ssid);
                }
                return settings;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ScanSettings[] newArray(int size) {
                return new ScanSettings[size];
            }
        };
        public int band;
        public ChannelSpec[] channels;
        public HiddenNetwork[] hiddenNetworks;
        @SystemApi
        public boolean hideFromAppOps;
        @SystemApi
        public boolean ignoreLocationSettings;
        public boolean isPnoScan;
        public int maxPeriodInMs;
        public int maxScansToCache;
        public int numBssidsPerScan;
        public int periodInMs;
        public int reportEvents;
        public int stepCount;
        public int type = 0;

        /* loaded from: classes2.dex */
        public static class HiddenNetwork {
            public String ssid;

            public HiddenNetwork(String ssid) {
                this.ssid = ssid;
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.band);
            dest.writeInt(this.periodInMs);
            dest.writeInt(this.reportEvents);
            dest.writeInt(this.numBssidsPerScan);
            dest.writeInt(this.maxScansToCache);
            dest.writeInt(this.maxPeriodInMs);
            dest.writeInt(this.stepCount);
            dest.writeInt(this.isPnoScan ? 1 : 0);
            dest.writeInt(this.type);
            dest.writeInt(this.ignoreLocationSettings ? 1 : 0);
            dest.writeInt(this.hideFromAppOps ? 1 : 0);
            ChannelSpec[] channelSpecArr = this.channels;
            if (channelSpecArr != null) {
                dest.writeInt(channelSpecArr.length);
                int i = 0;
                while (true) {
                    ChannelSpec[] channelSpecArr2 = this.channels;
                    if (i >= channelSpecArr2.length) {
                        break;
                    }
                    dest.writeInt(channelSpecArr2[i].frequency);
                    dest.writeInt(this.channels[i].dwellTimeMS);
                    dest.writeInt(this.channels[i].passive ? 1 : 0);
                    i++;
                }
            } else {
                dest.writeInt(0);
            }
            HiddenNetwork[] hiddenNetworkArr = this.hiddenNetworks;
            if (hiddenNetworkArr != null) {
                dest.writeInt(hiddenNetworkArr.length);
                int i2 = 0;
                while (true) {
                    HiddenNetwork[] hiddenNetworkArr2 = this.hiddenNetworks;
                    if (i2 < hiddenNetworkArr2.length) {
                        dest.writeString(hiddenNetworkArr2[i2].ssid);
                        i2++;
                    } else {
                        return;
                    }
                }
            } else {
                dest.writeInt(0);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class ScanData implements Parcelable {
        public static final Parcelable.Creator<ScanData> CREATOR = new Parcelable.Creator<ScanData>() { // from class: android.net.wifi.WifiScanner.ScanData.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ScanData createFromParcel(Parcel in) {
                int id = in.readInt();
                int flags = in.readInt();
                int bucketsScanned = in.readInt();
                int bandScanned = in.readInt();
                int n = in.readInt();
                ScanResult[] results = new ScanResult[n];
                for (int i = 0; i < n; i++) {
                    results[i] = ScanResult.CREATOR.createFromParcel(in);
                }
                return new ScanData(id, flags, bucketsScanned, bandScanned, results);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ScanData[] newArray(int size) {
                return new ScanData[size];
            }
        };
        private int mBandScanned;
        private int mBucketsScanned;
        private int mFlags;
        private int mId;
        private ScanResult[] mResults;

        ScanData() {
        }

        public ScanData(int id, int flags, ScanResult[] results) {
            this.mId = id;
            this.mFlags = flags;
            this.mResults = results;
        }

        public ScanData(int id, int flags, int bucketsScanned, int bandScanned, ScanResult[] results) {
            this.mId = id;
            this.mFlags = flags;
            this.mBucketsScanned = bucketsScanned;
            this.mBandScanned = bandScanned;
            this.mResults = results;
        }

        public ScanData(ScanData s) {
            this.mId = s.mId;
            this.mFlags = s.mFlags;
            this.mBucketsScanned = s.mBucketsScanned;
            this.mBandScanned = s.mBandScanned;
            this.mResults = new ScanResult[s.mResults.length];
            int i = 0;
            while (true) {
                ScanResult[] scanResultArr = s.mResults;
                if (i < scanResultArr.length) {
                    ScanResult result = scanResultArr[i];
                    ScanResult newResult = new ScanResult(result);
                    this.mResults[i] = newResult;
                    i++;
                } else {
                    return;
                }
            }
        }

        public int getId() {
            return this.mId;
        }

        public int getFlags() {
            return this.mFlags;
        }

        public int getBucketsScanned() {
            return this.mBucketsScanned;
        }

        public int getBandScanned() {
            return this.mBandScanned;
        }

        public ScanResult[] getResults() {
            return this.mResults;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            if (this.mResults != null) {
                dest.writeInt(this.mId);
                dest.writeInt(this.mFlags);
                dest.writeInt(this.mBucketsScanned);
                dest.writeInt(this.mBandScanned);
                dest.writeInt(this.mResults.length);
                int i = 0;
                while (true) {
                    ScanResult[] scanResultArr = this.mResults;
                    if (i < scanResultArr.length) {
                        ScanResult result = scanResultArr[i];
                        result.writeToParcel(dest, flags);
                        i++;
                    } else {
                        return;
                    }
                }
            } else {
                dest.writeInt(0);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class ParcelableScanData implements Parcelable {
        public static final Parcelable.Creator<ParcelableScanData> CREATOR = new Parcelable.Creator<ParcelableScanData>() { // from class: android.net.wifi.WifiScanner.ParcelableScanData.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ParcelableScanData createFromParcel(Parcel in) {
                int n = in.readInt();
                ScanData[] results = new ScanData[n];
                for (int i = 0; i < n; i++) {
                    results[i] = ScanData.CREATOR.createFromParcel(in);
                }
                return new ParcelableScanData(results);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ParcelableScanData[] newArray(int size) {
                return new ParcelableScanData[size];
            }
        };
        public ScanData[] mResults;

        public ParcelableScanData(ScanData[] results) {
            this.mResults = results;
        }

        public ScanData[] getResults() {
            return this.mResults;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            ScanData[] scanDataArr = this.mResults;
            if (scanDataArr != null) {
                dest.writeInt(scanDataArr.length);
                int i = 0;
                while (true) {
                    ScanData[] scanDataArr2 = this.mResults;
                    if (i < scanDataArr2.length) {
                        ScanData result = scanDataArr2[i];
                        result.writeToParcel(dest, flags);
                        i++;
                    } else {
                        return;
                    }
                }
            } else {
                dest.writeInt(0);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class ParcelableScanResults implements Parcelable {
        public static final Parcelable.Creator<ParcelableScanResults> CREATOR = new Parcelable.Creator<ParcelableScanResults>() { // from class: android.net.wifi.WifiScanner.ParcelableScanResults.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ParcelableScanResults createFromParcel(Parcel in) {
                int n = in.readInt();
                ScanResult[] results = new ScanResult[n];
                for (int i = 0; i < n; i++) {
                    results[i] = ScanResult.CREATOR.createFromParcel(in);
                }
                return new ParcelableScanResults(results);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ParcelableScanResults[] newArray(int size) {
                return new ParcelableScanResults[size];
            }
        };
        public ScanResult[] mResults;

        public ParcelableScanResults(ScanResult[] results) {
            this.mResults = results;
        }

        public ScanResult[] getResults() {
            return this.mResults;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            ScanResult[] scanResultArr = this.mResults;
            if (scanResultArr != null) {
                dest.writeInt(scanResultArr.length);
                int i = 0;
                while (true) {
                    ScanResult[] scanResultArr2 = this.mResults;
                    if (i < scanResultArr2.length) {
                        ScanResult result = scanResultArr2[i];
                        result.writeToParcel(dest, flags);
                        i++;
                    } else {
                        return;
                    }
                }
            } else {
                dest.writeInt(0);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class PnoSettings implements Parcelable {
        public static final Parcelable.Creator<PnoSettings> CREATOR = new Parcelable.Creator<PnoSettings>() { // from class: android.net.wifi.WifiScanner.PnoSettings.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public PnoSettings createFromParcel(Parcel in) {
                PnoSettings settings = new PnoSettings();
                settings.isConnected = in.readInt() == 1;
                settings.min5GHzRssi = in.readInt();
                settings.min24GHzRssi = in.readInt();
                settings.initialScoreMax = in.readInt();
                settings.currentConnectionBonus = in.readInt();
                settings.sameNetworkBonus = in.readInt();
                settings.secureBonus = in.readInt();
                settings.band5GHzBonus = in.readInt();
                int numNetworks = in.readInt();
                settings.networkList = new PnoNetwork[numNetworks];
                for (int i = 0; i < numNetworks; i++) {
                    String ssid = in.readString();
                    PnoNetwork network = new PnoNetwork(ssid);
                    network.flags = in.readByte();
                    network.authBitField = in.readByte();
                    network.frequencies = in.createIntArray();
                    settings.networkList[i] = network;
                }
                return settings;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public PnoSettings[] newArray(int size) {
                return new PnoSettings[size];
            }
        };
        public int band5GHzBonus;
        public int currentConnectionBonus;
        public int initialScoreMax;
        public boolean isConnected;
        public int min24GHzRssi;
        public int min5GHzRssi;
        public PnoNetwork[] networkList;
        public int sameNetworkBonus;
        public int secureBonus;

        /* loaded from: classes2.dex */
        public static class PnoNetwork {
            public static final byte AUTH_CODE_EAPOL = 4;
            public static final byte AUTH_CODE_OPEN = 1;
            public static final byte AUTH_CODE_PSK = 2;
            public static final byte FLAG_A_BAND = 2;
            public static final byte FLAG_DIRECTED_SCAN = 1;
            public static final byte FLAG_G_BAND = 4;
            public static final byte FLAG_SAME_NETWORK = 16;
            public static final byte FLAG_STRICT_MATCH = 8;
            public String ssid;
            public byte flags = 0;
            public byte authBitField = 0;
            public int[] frequencies = new int[0];

            public PnoNetwork(String ssid) {
                this.ssid = ssid;
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.isConnected ? 1 : 0);
            dest.writeInt(this.min5GHzRssi);
            dest.writeInt(this.min24GHzRssi);
            dest.writeInt(this.initialScoreMax);
            dest.writeInt(this.currentConnectionBonus);
            dest.writeInt(this.sameNetworkBonus);
            dest.writeInt(this.secureBonus);
            dest.writeInt(this.band5GHzBonus);
            PnoNetwork[] pnoNetworkArr = this.networkList;
            if (pnoNetworkArr != null) {
                dest.writeInt(pnoNetworkArr.length);
                int i = 0;
                while (true) {
                    PnoNetwork[] pnoNetworkArr2 = this.networkList;
                    if (i < pnoNetworkArr2.length) {
                        dest.writeString(pnoNetworkArr2[i].ssid);
                        dest.writeByte(this.networkList[i].flags);
                        dest.writeByte(this.networkList[i].authBitField);
                        dest.writeIntArray(this.networkList[i].frequencies);
                        i++;
                    } else {
                        return;
                    }
                }
            } else {
                dest.writeInt(0);
            }
        }
    }

    public void setScanningEnabled(boolean enable) {
        validateChannel();
        this.mAsyncChannel.sendMessage(enable ? CMD_ENABLE : CMD_DISABLE);
    }

    public void registerScanListener(ScanListener listener) {
        Preconditions.checkNotNull(listener, "listener cannot be null");
        int key = addListener(listener);
        if (key == 0) {
            return;
        }
        validateChannel();
        this.mAsyncChannel.sendMessage(CMD_REGISTER_SCAN_LISTENER, 0, key);
    }

    public void deregisterScanListener(ScanListener listener) {
        Preconditions.checkNotNull(listener, "listener cannot be null");
        int key = removeListener(listener);
        if (key == 0) {
            return;
        }
        validateChannel();
        this.mAsyncChannel.sendMessage(CMD_DEREGISTER_SCAN_LISTENER, 0, key);
    }

    public void startBackgroundScan(ScanSettings settings, ScanListener listener) {
        startBackgroundScan(settings, listener, null);
    }

    public void startBackgroundScan(ScanSettings settings, ScanListener listener, WorkSource workSource) {
        Preconditions.checkNotNull(listener, "listener cannot be null");
        int key = addListener(listener);
        if (key == 0) {
            return;
        }
        validateChannel();
        Bundle scanParams = new Bundle();
        scanParams.putParcelable("ScanSettings", settings);
        scanParams.putParcelable(SCAN_PARAMS_WORK_SOURCE_KEY, workSource);
        scanParams.putString(REQUEST_PACKAGE_NAME_KEY, this.mContext.getOpPackageName());
        this.mAsyncChannel.sendMessage(CMD_START_BACKGROUND_SCAN, 0, key, scanParams);
    }

    public void stopBackgroundScan(ScanListener listener) {
        Preconditions.checkNotNull(listener, "listener cannot be null");
        int key = removeListener(listener);
        if (key == 0) {
            return;
        }
        validateChannel();
        Bundle scanParams = new Bundle();
        scanParams.putString(REQUEST_PACKAGE_NAME_KEY, this.mContext.getOpPackageName());
        this.mAsyncChannel.sendMessage(CMD_STOP_BACKGROUND_SCAN, 0, key, scanParams);
    }

    public boolean getScanResults() {
        validateChannel();
        Bundle scanParams = new Bundle();
        scanParams.putString(REQUEST_PACKAGE_NAME_KEY, this.mContext.getOpPackageName());
        Message reply = this.mAsyncChannel.sendMessageSynchronously(CMD_GET_SCAN_RESULTS, 0, 0, scanParams);
        return reply.what == 159761;
    }

    public void startScan(ScanSettings settings, ScanListener listener) {
        startScan(settings, listener, null);
    }

    public void startScan(ScanSettings settings, ScanListener listener, WorkSource workSource) {
        Preconditions.checkNotNull(listener, "listener cannot be null");
        int key = addListener(listener);
        if (key == 0) {
            return;
        }
        validateChannel();
        Bundle scanParams = new Bundle();
        scanParams.putParcelable("ScanSettings", settings);
        scanParams.putParcelable(SCAN_PARAMS_WORK_SOURCE_KEY, workSource);
        scanParams.putString(REQUEST_PACKAGE_NAME_KEY, this.mContext.getOpPackageName());
        SystemProperties.set("wifi.scanning", "ture");
        this.mAsyncChannel.sendMessage(CMD_START_SINGLE_SCAN, 0, key, scanParams);
    }

    public void stopScan(ScanListener listener) {
        SystemProperties.set("wifi.scanning", "false");
        Preconditions.checkNotNull(listener, "listener cannot be null");
        int key = removeListener(listener);
        if (key == 0) {
            return;
        }
        validateChannel();
        Bundle scanParams = new Bundle();
        scanParams.putString(REQUEST_PACKAGE_NAME_KEY, this.mContext.getOpPackageName());
        this.mAsyncChannel.sendMessage(CMD_STOP_SINGLE_SCAN, 0, key, scanParams);
    }

    public List<ScanResult> getSingleScanResults() {
        validateChannel();
        Bundle scanParams = new Bundle();
        scanParams.putString(REQUEST_PACKAGE_NAME_KEY, this.mContext.getOpPackageName());
        Message reply = this.mAsyncChannel.sendMessageSynchronously(CMD_GET_SINGLE_SCAN_RESULTS, 0, 0, scanParams);
        if (reply.what == 159761) {
            return Arrays.asList(((ParcelableScanResults) reply.obj).getResults());
        }
        OperationResult result = (OperationResult) reply.obj;
        Log.e(TAG, "Error retrieving SingleScan results reason: " + result.reason + " description: " + result.description);
        return new ArrayList();
    }

    private void startPnoScan(ScanSettings scanSettings, PnoSettings pnoSettings, int key) {
        Bundle pnoParams = new Bundle();
        scanSettings.isPnoScan = true;
        pnoParams.putParcelable("ScanSettings", scanSettings);
        pnoParams.putParcelable(PNO_PARAMS_PNO_SETTINGS_KEY, pnoSettings);
        this.mAsyncChannel.sendMessage(CMD_START_PNO_SCAN, 0, key, pnoParams);
    }

    public void startConnectedPnoScan(ScanSettings scanSettings, PnoSettings pnoSettings, PnoScanListener listener) {
        Preconditions.checkNotNull(listener, "listener cannot be null");
        Preconditions.checkNotNull(pnoSettings, "pnoSettings cannot be null");
        int key = addListener(listener);
        if (key == 0) {
            return;
        }
        validateChannel();
        pnoSettings.isConnected = true;
        startPnoScan(scanSettings, pnoSettings, key);
    }

    public void startDisconnectedPnoScan(ScanSettings scanSettings, PnoSettings pnoSettings, PnoScanListener listener) {
        Preconditions.checkNotNull(listener, "listener cannot be null");
        Preconditions.checkNotNull(pnoSettings, "pnoSettings cannot be null");
        int key = addListener(listener);
        if (key == 0) {
            return;
        }
        validateChannel();
        pnoSettings.isConnected = false;
        startPnoScan(scanSettings, pnoSettings, key);
    }

    public void stopPnoScan(ScanListener listener) {
        Preconditions.checkNotNull(listener, "listener cannot be null");
        int key = removeListener(listener);
        if (key == 0) {
            return;
        }
        validateChannel();
        this.mAsyncChannel.sendMessage(CMD_STOP_PNO_SCAN, 0, key);
    }

    @SystemApi
    @Deprecated
    /* loaded from: classes2.dex */
    public static class WifiChangeSettings implements Parcelable {
        public static final Parcelable.Creator<WifiChangeSettings> CREATOR = new Parcelable.Creator<WifiChangeSettings>() { // from class: android.net.wifi.WifiScanner.WifiChangeSettings.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public WifiChangeSettings createFromParcel(Parcel in) {
                return new WifiChangeSettings();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public WifiChangeSettings[] newArray(int size) {
                return new WifiChangeSettings[size];
            }
        };
        public BssidInfo[] bssidInfos;
        public int lostApSampleSize;
        public int minApsBreachingThreshold;
        public int periodInMs;
        public int rssiSampleSize;
        public int unchangedSampleSize;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
        }
    }

    @SuppressLint({"Doclava125"})
    @Deprecated
    public void configureWifiChange(int rssiSampleSize, int lostApSampleSize, int unchangedSampleSize, int minApsBreachingThreshold, int periodInMs, BssidInfo[] bssidInfos) {
        throw new UnsupportedOperationException();
    }

    @SuppressLint({"Doclava125"})
    @Deprecated
    public void startTrackingWifiChange(WifiChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    @SuppressLint({"Doclava125"})
    @Deprecated
    public void stopTrackingWifiChange(WifiChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    @SystemApi
    @SuppressLint({"Doclava125"})
    @Deprecated
    public void configureWifiChange(WifiChangeSettings settings) {
        throw new UnsupportedOperationException();
    }

    @SystemApi
    @Deprecated
    /* loaded from: classes2.dex */
    public static class HotlistSettings implements Parcelable {
        public static final Parcelable.Creator<HotlistSettings> CREATOR = new Parcelable.Creator<HotlistSettings>() { // from class: android.net.wifi.WifiScanner.HotlistSettings.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public HotlistSettings createFromParcel(Parcel in) {
                HotlistSettings settings = new HotlistSettings();
                return settings;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public HotlistSettings[] newArray(int size) {
                return new HotlistSettings[size];
            }
        };
        public int apLostThreshold;
        public BssidInfo[] bssidInfos;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
        }
    }

    @SuppressLint({"Doclava125"})
    @Deprecated
    public void startTrackingBssids(BssidInfo[] bssidInfos, int apLostThreshold, BssidListener listener) {
        throw new UnsupportedOperationException();
    }

    @SuppressLint({"Doclava125"})
    @Deprecated
    public void stopTrackingBssids(BssidListener listener) {
        throw new UnsupportedOperationException();
    }

    public WifiScanner(Context context, IWifiScanner service, Looper looper) {
        this.mContext = context;
        this.mService = service;
        try {
            Messenger messenger = this.mService.getMessenger();
            if (messenger == null) {
                throw new IllegalStateException("getMessenger() returned null!  This is invalid.");
            }
            this.mAsyncChannel = new AsyncChannel();
            this.mInternalHandler = new ServiceHandler(looper);
            this.mAsyncChannel.connectSync(this.mContext, this.mInternalHandler, messenger);
            this.mAsyncChannel.sendMessage(AsyncChannel.CMD_CHANNEL_FULL_CONNECTION);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void validateChannel() {
        if (this.mAsyncChannel == null) {
            throw new IllegalStateException("No permission to access and change wifi or a bad initialization");
        }
    }

    private int addListener(ActionListener listener) {
        synchronized (this.mListenerMapLock) {
            boolean keyExists = getListenerKey(listener) != 0;
            int key = putListener(listener);
            if (keyExists) {
                OperationResult operationResult = new OperationResult(-5, "Outstanding request with same key not stopped yet");
                Message message = Message.obtain(this.mInternalHandler, CMD_OP_FAILED, 0, key, operationResult);
                message.sendToTarget();
                return 0;
            }
            return key;
        }
    }

    private int putListener(Object listener) {
        int key;
        if (listener == null) {
            return 0;
        }
        synchronized (this.mListenerMapLock) {
            do {
                key = this.mListenerKey;
                this.mListenerKey = key + 1;
            } while (key == 0);
            this.mListenerMap.put(key, listener);
        }
        return key;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object getListener(int key) {
        Object listener;
        if (key == 0) {
            return null;
        }
        synchronized (this.mListenerMapLock) {
            listener = this.mListenerMap.get(key);
        }
        return listener;
    }

    private int getListenerKey(Object listener) {
        if (listener == null) {
            return 0;
        }
        synchronized (this.mListenerMapLock) {
            int index = this.mListenerMap.indexOfValue(listener);
            if (index == -1) {
                return 0;
            }
            return this.mListenerMap.keyAt(index);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object removeListener(int key) {
        Object listener;
        if (key == 0) {
            return null;
        }
        synchronized (this.mListenerMapLock) {
            listener = this.mListenerMap.get(key);
            this.mListenerMap.remove(key);
        }
        return listener;
    }

    private int removeListener(Object listener) {
        int key = getListenerKey(listener);
        if (key == 0) {
            Log.e(TAG, "listener cannot be found");
            return key;
        }
        synchronized (this.mListenerMapLock) {
            this.mListenerMap.remove(key);
        }
        return key;
    }

    /* loaded from: classes2.dex */
    public static class OperationResult implements Parcelable {
        public static final Parcelable.Creator<OperationResult> CREATOR = new Parcelable.Creator<OperationResult>() { // from class: android.net.wifi.WifiScanner.OperationResult.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public OperationResult createFromParcel(Parcel in) {
                int reason = in.readInt();
                String description = in.readString();
                return new OperationResult(reason, description);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public OperationResult[] newArray(int size) {
                return new OperationResult[size];
            }
        };
        public String description;
        public int reason;

        public OperationResult(int reason, String description) {
            this.reason = reason;
            this.description = description;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.reason);
            dest.writeString(this.description);
        }
    }

    /* loaded from: classes2.dex */
    private class ServiceHandler extends Handler {
        ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i != 69634) {
                if (i != 69636) {
                    Object listener = WifiScanner.this.getListener(msg.arg2);
                    if (listener == null) {
                        return;
                    }
                    switch (msg.what) {
                        case WifiScanner.CMD_SCAN_RESULT /* 159749 */:
                            ((ScanListener) listener).onResults(((ParcelableScanData) msg.obj).getResults());
                            return;
                        case WifiScanner.CMD_OP_SUCCEEDED /* 159761 */:
                            ((ActionListener) listener).onSuccess();
                            return;
                        case WifiScanner.CMD_OP_FAILED /* 159762 */:
                            OperationResult result = (OperationResult) msg.obj;
                            ((ActionListener) listener).onFailure(result.reason, result.description);
                            WifiScanner.this.removeListener(msg.arg2);
                            return;
                        case WifiScanner.CMD_FULL_SCAN_RESULT /* 159764 */:
                            ((ScanListener) listener).onFullResult((ScanResult) msg.obj);
                            return;
                        case WifiScanner.CMD_SINGLE_SCAN_COMPLETED /* 159767 */:
                            WifiScanner.this.removeListener(msg.arg2);
                            return;
                        case WifiScanner.CMD_PNO_NETWORK_FOUND /* 159770 */:
                            ((PnoScanListener) listener).onPnoNetworkFound(((ParcelableScanResults) msg.obj).getResults());
                            return;
                        default:
                            return;
                    }
                }
                Log.e(WifiScanner.TAG, "Channel connection lost");
                WifiScanner.this.mAsyncChannel = null;
                getLooper().quit();
            }
        }
    }
}
