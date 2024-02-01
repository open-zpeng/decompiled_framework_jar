package android.os;

import android.content.pm.ApplicationInfo;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Process;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.Zygote;
import com.android.internal.os.ZygoteConfig;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/* loaded from: classes2.dex */
public class ZygoteProcess {
    private static final String[] INVALID_USAP_FLAGS = {"--query-abi-list", "--get-pid", "--preload-default", "--preload-package", "--preload-app", "--start-child-zygote", "--set-api-blacklist-exemptions", "--hidden-api-log-sampling-rate", "--hidden-api-statslog-sampling-rate", "--invoke-with"};
    private static final String LOG_TAG = "ZygoteProcess";
    private static final String USAP_POOL_ENABLED_DEFAULT = "false";
    private static final int ZYGOTE_CONNECT_RETRY_DELAY_MS = 50;
    private static final int ZYGOTE_CONNECT_TIMEOUT_MS = 20000;
    static final int ZYGOTE_RETRY_MILLIS = 500;
    private List<String> mApiBlacklistExemptions;
    private int mHiddenApiAccessLogSampleRate;
    private int mHiddenApiAccessStatslogSampleRate;
    private boolean mIsFirstPropCheck;
    private long mLastPropCheckTimestamp;
    private final Object mLock;
    private boolean mUsapPoolEnabled;
    private final LocalSocketAddress mUsapPoolSecondarySocketAddress;
    private final LocalSocketAddress mUsapPoolSocketAddress;
    private final LocalSocketAddress mZygoteSecondarySocketAddress;
    private final LocalSocketAddress mZygoteSocketAddress;
    private ZygoteState primaryZygoteState;
    private ZygoteState secondaryZygoteState;

    public ZygoteProcess() {
        this.mLock = new Object();
        this.mApiBlacklistExemptions = Collections.emptyList();
        this.mUsapPoolEnabled = false;
        this.mIsFirstPropCheck = true;
        this.mLastPropCheckTimestamp = 0L;
        this.mZygoteSocketAddress = new LocalSocketAddress(Zygote.PRIMARY_SOCKET_NAME, LocalSocketAddress.Namespace.RESERVED);
        this.mZygoteSecondarySocketAddress = new LocalSocketAddress(Zygote.SECONDARY_SOCKET_NAME, LocalSocketAddress.Namespace.RESERVED);
        this.mUsapPoolSocketAddress = new LocalSocketAddress(Zygote.USAP_POOL_PRIMARY_SOCKET_NAME, LocalSocketAddress.Namespace.RESERVED);
        this.mUsapPoolSecondarySocketAddress = new LocalSocketAddress(Zygote.USAP_POOL_SECONDARY_SOCKET_NAME, LocalSocketAddress.Namespace.RESERVED);
    }

    public ZygoteProcess(LocalSocketAddress primarySocketAddress, LocalSocketAddress secondarySocketAddress) {
        this.mLock = new Object();
        this.mApiBlacklistExemptions = Collections.emptyList();
        this.mUsapPoolEnabled = false;
        this.mIsFirstPropCheck = true;
        this.mLastPropCheckTimestamp = 0L;
        this.mZygoteSocketAddress = primarySocketAddress;
        this.mZygoteSecondarySocketAddress = secondarySocketAddress;
        this.mUsapPoolSocketAddress = null;
        this.mUsapPoolSecondarySocketAddress = null;
    }

    public LocalSocketAddress getPrimarySocketAddress() {
        return this.mZygoteSocketAddress;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class ZygoteState implements AutoCloseable {
        private final List<String> mAbiList;
        private boolean mClosed;
        final LocalSocketAddress mUsapSocketAddress;
        final DataInputStream mZygoteInputStream;
        final BufferedWriter mZygoteOutputWriter;
        private final LocalSocket mZygoteSessionSocket;
        final LocalSocketAddress mZygoteSocketAddress;

        private ZygoteState(LocalSocketAddress zygoteSocketAddress, LocalSocketAddress usapSocketAddress, LocalSocket zygoteSessionSocket, DataInputStream zygoteInputStream, BufferedWriter zygoteOutputWriter, List<String> abiList) {
            this.mZygoteSocketAddress = zygoteSocketAddress;
            this.mUsapSocketAddress = usapSocketAddress;
            this.mZygoteSessionSocket = zygoteSessionSocket;
            this.mZygoteInputStream = zygoteInputStream;
            this.mZygoteOutputWriter = zygoteOutputWriter;
            this.mAbiList = abiList;
        }

        static ZygoteState connect(LocalSocketAddress zygoteSocketAddress, LocalSocketAddress usapSocketAddress) throws IOException {
            LocalSocket zygoteSessionSocket = new LocalSocket();
            if (zygoteSocketAddress == null) {
                throw new IllegalArgumentException("zygoteSocketAddress can't be null");
            }
            try {
                zygoteSessionSocket.connect(zygoteSocketAddress);
                DataInputStream zygoteInputStream = new DataInputStream(zygoteSessionSocket.getInputStream());
                BufferedWriter zygoteOutputWriter = new BufferedWriter(new OutputStreamWriter(zygoteSessionSocket.getOutputStream()), 256);
                return new ZygoteState(zygoteSocketAddress, usapSocketAddress, zygoteSessionSocket, zygoteInputStream, zygoteOutputWriter, ZygoteProcess.getAbiList(zygoteOutputWriter, zygoteInputStream));
            } catch (IOException ex) {
                try {
                    zygoteSessionSocket.close();
                } catch (IOException e) {
                }
                throw ex;
            }
        }

        LocalSocket getUsapSessionSocket() throws IOException {
            LocalSocket usapSessionSocket = new LocalSocket();
            usapSessionSocket.connect(this.mUsapSocketAddress);
            return usapSessionSocket;
        }

        boolean matches(String abi) {
            return this.mAbiList.contains(abi);
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            try {
                this.mZygoteSessionSocket.close();
            } catch (IOException ex) {
                Log.e(ZygoteProcess.LOG_TAG, "I/O exception on routine close", ex);
            }
            this.mClosed = true;
        }

        boolean isClosed() {
            return this.mClosed;
        }
    }

    public final Process.ProcessStartResult start(String processClass, String niceName, int uid, int gid, int[] gids, int runtimeFlags, int mountExternal, int targetSdkVersion, String seInfo, String abi, String instructionSet, String appDataDir, String invokeWith, String packageName, boolean useUsapPool, String[] zygoteArgs) {
        if (fetchUsapPoolEnabledPropWithMinInterval()) {
            informZygotesOfUsapPoolStatus();
        }
        try {
            return startViaZygote(processClass, niceName, uid, gid, gids, runtimeFlags, mountExternal, targetSdkVersion, seInfo, abi, instructionSet, appDataDir, invokeWith, false, packageName, useUsapPool, zygoteArgs);
        } catch (ZygoteStartFailedEx ex) {
            Log.e(LOG_TAG, "Starting VM process through Zygote failed");
            throw new RuntimeException("Starting VM process through Zygote failed", ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy({"mLock"})
    public static List<String> getAbiList(BufferedWriter writer, DataInputStream inputStream) throws IOException {
        writer.write(WifiEnterpriseConfig.ENGINE_ENABLE);
        writer.newLine();
        writer.write("--query-abi-list");
        writer.newLine();
        writer.flush();
        int numBytes = inputStream.readInt();
        byte[] bytes = new byte[numBytes];
        inputStream.readFully(bytes);
        String rawList = new String(bytes, StandardCharsets.US_ASCII);
        return Arrays.asList(rawList.split(SmsManager.REGEX_PREFIX_DELIMITER));
    }

    @GuardedBy({"mLock"})
    private Process.ProcessStartResult zygoteSendArgsAndGetResult(ZygoteState zygoteState, boolean useUsapPool, ArrayList<String> args) throws ZygoteStartFailedEx {
        Iterator<String> it = args.iterator();
        while (it.hasNext()) {
            String arg = it.next();
            if (arg.indexOf(10) >= 0) {
                throw new ZygoteStartFailedEx("Embedded newlines not allowed");
            }
            if (arg.indexOf(13) >= 0) {
                throw new ZygoteStartFailedEx("Embedded carriage returns not allowed");
            }
        }
        String msgStr = args.size() + "\n" + String.join("\n", args) + "\n";
        if (useUsapPool && this.mUsapPoolEnabled && canAttemptUsap(args)) {
            try {
                return attemptUsapSendArgsAndGetResult(zygoteState, msgStr);
            } catch (IOException ex) {
                Log.e(LOG_TAG, "IO Exception while communicating with USAP pool - " + ex.getMessage());
            }
        }
        return attemptZygoteSendArgsAndGetResult(zygoteState, msgStr);
    }

    private Process.ProcessStartResult attemptZygoteSendArgsAndGetResult(ZygoteState zygoteState, String msgStr) throws ZygoteStartFailedEx {
        try {
            BufferedWriter zygoteWriter = zygoteState.mZygoteOutputWriter;
            DataInputStream zygoteInputStream = zygoteState.mZygoteInputStream;
            zygoteWriter.write(msgStr);
            zygoteWriter.flush();
            Process.ProcessStartResult result = new Process.ProcessStartResult();
            result.pid = zygoteInputStream.readInt();
            result.usingWrapper = zygoteInputStream.readBoolean();
            if (result.pid < 0) {
                throw new ZygoteStartFailedEx("fork() failed");
            }
            return result;
        } catch (IOException ex) {
            zygoteState.close();
            Log.e(LOG_TAG, "IO Exception while communicating with Zygote - " + ex.toString());
            throw new ZygoteStartFailedEx(ex);
        }
    }

    private Process.ProcessStartResult attemptUsapSendArgsAndGetResult(ZygoteState zygoteState, String msgStr) throws ZygoteStartFailedEx, IOException {
        LocalSocket usapSessionSocket = zygoteState.getUsapSessionSocket();
        try {
            BufferedWriter usapWriter = new BufferedWriter(new OutputStreamWriter(usapSessionSocket.getOutputStream()), 256);
            DataInputStream usapReader = new DataInputStream(usapSessionSocket.getInputStream());
            usapWriter.write(msgStr);
            usapWriter.flush();
            Process.ProcessStartResult result = new Process.ProcessStartResult();
            result.pid = usapReader.readInt();
            result.usingWrapper = false;
            if (result.pid < 0) {
                throw new ZygoteStartFailedEx("USAP specialization failed");
            }
            usapSessionSocket.close();
            return result;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (usapSessionSocket != null) {
                    try {
                        usapSessionSocket.close();
                    } catch (Throwable th3) {
                        th.addSuppressed(th3);
                    }
                }
                throw th2;
            }
        }
    }

    private static boolean canAttemptUsap(ArrayList<String> args) {
        String[] strArr;
        Iterator<String> it = args.iterator();
        while (it.hasNext()) {
            String flag = it.next();
            for (String badFlag : INVALID_USAP_FLAGS) {
                if (flag.startsWith(badFlag)) {
                    return false;
                }
            }
            if (flag.startsWith("--nice-name=")) {
                String niceName = flag.substring(12);
                String property_value = SystemProperties.get("wrap." + niceName);
                if (property_value != null && property_value.length() != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private Process.ProcessStartResult startViaZygote(String processClass, String niceName, int uid, int gid, int[] gids, int runtimeFlags, int mountExternal, int targetSdkVersion, String seInfo, String abi, String instructionSet, String appDataDir, String invokeWith, boolean startChildZygote, String packageName, boolean useUsapPool, String[] extraArgs) throws ZygoteStartFailedEx {
        int sz;
        ArrayList<String> argsForZygote = new ArrayList<>();
        argsForZygote.add("--runtime-args");
        argsForZygote.add("--setuid=" + uid);
        argsForZygote.add("--setgid=" + gid);
        argsForZygote.add("--runtime-flags=" + runtimeFlags);
        if (mountExternal == 1) {
            argsForZygote.add("--mount-external-default");
        } else if (mountExternal == 2) {
            argsForZygote.add("--mount-external-read");
        } else if (mountExternal == 3) {
            argsForZygote.add("--mount-external-write");
        } else if (mountExternal == 6) {
            argsForZygote.add("--mount-external-full");
        } else if (mountExternal == 5) {
            argsForZygote.add("--mount-external-installer");
        } else if (mountExternal == 4) {
            argsForZygote.add("--mount-external-legacy");
        }
        argsForZygote.add("--target-sdk-version=" + targetSdkVersion);
        if (gids != null && gids.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("--setgroups=");
            int sz2 = gids.length;
            int i = 0;
            while (i < sz2) {
                if (i == 0) {
                    sz = sz2;
                } else {
                    sz = sz2;
                    sb.append(',');
                }
                int sz3 = gids[i];
                sb.append(sz3);
                i++;
                sz2 = sz;
            }
            argsForZygote.add(sb.toString());
        }
        if (niceName != null) {
            argsForZygote.add("--nice-name=" + niceName);
        }
        if (seInfo != null) {
            argsForZygote.add("--seinfo=" + seInfo);
        }
        if (instructionSet != null) {
            argsForZygote.add("--instruction-set=" + instructionSet);
        }
        if (appDataDir != null) {
            argsForZygote.add("--app-data-dir=" + appDataDir);
        }
        if (invokeWith != null) {
            argsForZygote.add("--invoke-with");
            argsForZygote.add(invokeWith);
        }
        if (startChildZygote) {
            argsForZygote.add("--start-child-zygote");
        }
        if (packageName != null) {
            argsForZygote.add("--package-name=" + packageName);
        }
        argsForZygote.add(processClass);
        if (extraArgs != null) {
            Collections.addAll(argsForZygote, extraArgs);
        }
        synchronized (this.mLock) {
            try {
                try {
                    return zygoteSendArgsAndGetResult(openZygoteSocketIfNeeded(abi), useUsapPool, argsForZygote);
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    private boolean fetchUsapPoolEnabledProp() {
        boolean origVal = this.mUsapPoolEnabled;
        String propertyString = Zygote.getConfigurationProperty(ZygoteConfig.USAP_POOL_ENABLED, USAP_POOL_ENABLED_DEFAULT);
        if (!propertyString.isEmpty()) {
            this.mUsapPoolEnabled = Zygote.getConfigurationPropertyBoolean(ZygoteConfig.USAP_POOL_ENABLED, Boolean.valueOf(Boolean.parseBoolean(USAP_POOL_ENABLED_DEFAULT)));
        }
        boolean valueChanged = origVal != this.mUsapPoolEnabled;
        if (valueChanged) {
            Log.i(LOG_TAG, "usapPoolEnabled = " + this.mUsapPoolEnabled);
        }
        return valueChanged;
    }

    private boolean fetchUsapPoolEnabledPropWithMinInterval() {
        long currentTimestamp = SystemClock.elapsedRealtime();
        if (!SystemProperties.get("dalvik.vm.boot-image", "").endsWith("apex.art") || currentTimestamp > 15000) {
            if (this.mIsFirstPropCheck || currentTimestamp - this.mLastPropCheckTimestamp >= 60000) {
                this.mIsFirstPropCheck = false;
                this.mLastPropCheckTimestamp = currentTimestamp;
                return fetchUsapPoolEnabledProp();
            }
            return false;
        }
        return false;
    }

    public void close() {
        ZygoteState zygoteState = this.primaryZygoteState;
        if (zygoteState != null) {
            zygoteState.close();
        }
        ZygoteState zygoteState2 = this.secondaryZygoteState;
        if (zygoteState2 != null) {
            zygoteState2.close();
        }
    }

    public void establishZygoteConnectionForAbi(String abi) {
        try {
            synchronized (this.mLock) {
                openZygoteSocketIfNeeded(abi);
            }
        } catch (ZygoteStartFailedEx ex) {
            throw new RuntimeException("Unable to connect to zygote for abi: " + abi, ex);
        }
    }

    public int getZygotePid(String abi) {
        int parseInt;
        try {
            synchronized (this.mLock) {
                ZygoteState state = openZygoteSocketIfNeeded(abi);
                state.mZygoteOutputWriter.write(WifiEnterpriseConfig.ENGINE_ENABLE);
                state.mZygoteOutputWriter.newLine();
                state.mZygoteOutputWriter.write("--get-pid");
                state.mZygoteOutputWriter.newLine();
                state.mZygoteOutputWriter.flush();
                int numBytes = state.mZygoteInputStream.readInt();
                byte[] bytes = new byte[numBytes];
                state.mZygoteInputStream.readFully(bytes);
                parseInt = Integer.parseInt(new String(bytes, StandardCharsets.US_ASCII));
            }
            return parseInt;
        } catch (Exception ex) {
            throw new RuntimeException("Failure retrieving pid", ex);
        }
    }

    public boolean setApiBlacklistExemptions(List<String> exemptions) {
        boolean ok;
        synchronized (this.mLock) {
            this.mApiBlacklistExemptions = exemptions;
            ok = maybeSetApiBlacklistExemptions(this.primaryZygoteState, true);
            if (ok) {
                ok = maybeSetApiBlacklistExemptions(this.secondaryZygoteState, true);
            }
        }
        return ok;
    }

    public void setHiddenApiAccessLogSampleRate(int rate) {
        synchronized (this.mLock) {
            this.mHiddenApiAccessLogSampleRate = rate;
            maybeSetHiddenApiAccessLogSampleRate(this.primaryZygoteState);
            maybeSetHiddenApiAccessLogSampleRate(this.secondaryZygoteState);
        }
    }

    public void setHiddenApiAccessStatslogSampleRate(int rate) {
        synchronized (this.mLock) {
            this.mHiddenApiAccessStatslogSampleRate = rate;
            maybeSetHiddenApiAccessStatslogSampleRate(this.primaryZygoteState);
            maybeSetHiddenApiAccessStatslogSampleRate(this.secondaryZygoteState);
        }
    }

    @GuardedBy({"mLock"})
    private boolean maybeSetApiBlacklistExemptions(ZygoteState state, boolean sendIfEmpty) {
        if (state == null || state.isClosed()) {
            Slog.e(LOG_TAG, "Can't set API blacklist exemptions: no zygote connection");
            return false;
        } else if (sendIfEmpty || !this.mApiBlacklistExemptions.isEmpty()) {
            try {
                state.mZygoteOutputWriter.write(Integer.toString(this.mApiBlacklistExemptions.size() + 1));
                state.mZygoteOutputWriter.newLine();
                state.mZygoteOutputWriter.write("--set-api-blacklist-exemptions");
                state.mZygoteOutputWriter.newLine();
                for (int i = 0; i < this.mApiBlacklistExemptions.size(); i++) {
                    state.mZygoteOutputWriter.write(this.mApiBlacklistExemptions.get(i));
                    state.mZygoteOutputWriter.newLine();
                }
                state.mZygoteOutputWriter.flush();
                int status = state.mZygoteInputStream.readInt();
                if (status != 0) {
                    Slog.e(LOG_TAG, "Failed to set API blacklist exemptions; status " + status);
                }
                return true;
            } catch (IOException ioe) {
                Slog.e(LOG_TAG, "Failed to set API blacklist exemptions", ioe);
                this.mApiBlacklistExemptions = Collections.emptyList();
                return false;
            }
        } else {
            return true;
        }
    }

    private void maybeSetHiddenApiAccessLogSampleRate(ZygoteState state) {
        if (state == null || state.isClosed() || this.mHiddenApiAccessLogSampleRate == -1) {
            return;
        }
        try {
            state.mZygoteOutputWriter.write(Integer.toString(1));
            state.mZygoteOutputWriter.newLine();
            BufferedWriter bufferedWriter = state.mZygoteOutputWriter;
            bufferedWriter.write("--hidden-api-log-sampling-rate=" + this.mHiddenApiAccessLogSampleRate);
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.flush();
            int status = state.mZygoteInputStream.readInt();
            if (status != 0) {
                Slog.e(LOG_TAG, "Failed to set hidden API log sampling rate; status " + status);
            }
        } catch (IOException ioe) {
            Slog.e(LOG_TAG, "Failed to set hidden API log sampling rate", ioe);
        }
    }

    private void maybeSetHiddenApiAccessStatslogSampleRate(ZygoteState state) {
        if (state == null || state.isClosed() || this.mHiddenApiAccessStatslogSampleRate == -1) {
            return;
        }
        try {
            state.mZygoteOutputWriter.write(Integer.toString(1));
            state.mZygoteOutputWriter.newLine();
            BufferedWriter bufferedWriter = state.mZygoteOutputWriter;
            bufferedWriter.write("--hidden-api-statslog-sampling-rate=" + this.mHiddenApiAccessStatslogSampleRate);
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.flush();
            int status = state.mZygoteInputStream.readInt();
            if (status != 0) {
                Slog.e(LOG_TAG, "Failed to set hidden API statslog sampling rate; status " + status);
            }
        } catch (IOException ioe) {
            Slog.e(LOG_TAG, "Failed to set hidden API statslog sampling rate", ioe);
        }
    }

    @GuardedBy({"mLock"})
    private void attemptConnectionToPrimaryZygote() throws IOException {
        ZygoteState zygoteState = this.primaryZygoteState;
        if (zygoteState == null || zygoteState.isClosed()) {
            this.primaryZygoteState = ZygoteState.connect(this.mZygoteSocketAddress, this.mUsapPoolSocketAddress);
            maybeSetApiBlacklistExemptions(this.primaryZygoteState, false);
            maybeSetHiddenApiAccessLogSampleRate(this.primaryZygoteState);
            maybeSetHiddenApiAccessStatslogSampleRate(this.primaryZygoteState);
        }
    }

    @GuardedBy({"mLock"})
    private void attemptConnectionToSecondaryZygote() throws IOException {
        ZygoteState zygoteState = this.secondaryZygoteState;
        if (zygoteState == null || zygoteState.isClosed()) {
            this.secondaryZygoteState = ZygoteState.connect(this.mZygoteSecondarySocketAddress, this.mUsapPoolSecondarySocketAddress);
            maybeSetApiBlacklistExemptions(this.secondaryZygoteState, false);
            maybeSetHiddenApiAccessLogSampleRate(this.secondaryZygoteState);
            maybeSetHiddenApiAccessStatslogSampleRate(this.secondaryZygoteState);
        }
    }

    @GuardedBy({"mLock"})
    private ZygoteState openZygoteSocketIfNeeded(String abi) throws ZygoteStartFailedEx {
        try {
            attemptConnectionToPrimaryZygote();
            if (this.primaryZygoteState.matches(abi)) {
                return this.primaryZygoteState;
            }
            if (this.mZygoteSecondarySocketAddress != null) {
                attemptConnectionToSecondaryZygote();
                if (this.secondaryZygoteState.matches(abi)) {
                    return this.secondaryZygoteState;
                }
            }
            throw new ZygoteStartFailedEx("Unsupported zygote ABI: " + abi);
        } catch (IOException ioe) {
            throw new ZygoteStartFailedEx("Error connecting to zygote", ioe);
        }
    }

    public boolean preloadApp(ApplicationInfo appInfo, String abi) throws ZygoteStartFailedEx, IOException {
        boolean z;
        synchronized (this.mLock) {
            ZygoteState state = openZygoteSocketIfNeeded(abi);
            state.mZygoteOutputWriter.write("2");
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.write("--preload-app");
            state.mZygoteOutputWriter.newLine();
            Parcel parcel = Parcel.obtain();
            appInfo.writeToParcel(parcel, 0);
            String encodedParcelData = Base64.getEncoder().encodeToString(parcel.marshall());
            parcel.recycle();
            state.mZygoteOutputWriter.write(encodedParcelData);
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.flush();
            z = state.mZygoteInputStream.readInt() == 0;
        }
        return z;
    }

    public boolean preloadPackageForAbi(String packagePath, String libsPath, String libFileName, String cacheKey, String abi) throws ZygoteStartFailedEx, IOException {
        boolean z;
        synchronized (this.mLock) {
            ZygoteState state = openZygoteSocketIfNeeded(abi);
            state.mZygoteOutputWriter.write("5");
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.write("--preload-package");
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.write(packagePath);
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.write(libsPath);
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.write(libFileName);
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.write(cacheKey);
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.flush();
            z = state.mZygoteInputStream.readInt() == 0;
        }
        return z;
    }

    public boolean preloadDefault(String abi) throws ZygoteStartFailedEx, IOException {
        boolean z;
        synchronized (this.mLock) {
            ZygoteState state = openZygoteSocketIfNeeded(abi);
            state.mZygoteOutputWriter.write(WifiEnterpriseConfig.ENGINE_ENABLE);
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.write("--preload-default");
            state.mZygoteOutputWriter.newLine();
            state.mZygoteOutputWriter.flush();
            z = state.mZygoteInputStream.readInt() == 0;
        }
        return z;
    }

    public static void waitForConnectionToZygote(String zygoteSocketName) {
        LocalSocketAddress zygoteSocketAddress = new LocalSocketAddress(zygoteSocketName, LocalSocketAddress.Namespace.RESERVED);
        waitForConnectionToZygote(zygoteSocketAddress);
    }

    public static void waitForConnectionToZygote(LocalSocketAddress zygoteSocketAddress) {
        for (int n = 400; n >= 0; n--) {
            try {
                ZygoteState zs = ZygoteState.connect(zygoteSocketAddress, null);
                zs.close();
                return;
            } catch (IOException ioe) {
                Log.w(LOG_TAG, "Got error connecting to zygote, retrying. msg= " + ioe.getMessage());
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                }
            }
        }
        Slog.wtf(LOG_TAG, "Failed to connect to Zygote through socket " + zygoteSocketAddress.getName());
    }

    private void informZygotesOfUsapPoolStatus() {
        String command = "1\n--usap-pool-enabled=" + this.mUsapPoolEnabled + "\n";
        synchronized (this.mLock) {
            try {
                try {
                    attemptConnectionToPrimaryZygote();
                    this.primaryZygoteState.mZygoteOutputWriter.write(command);
                    this.primaryZygoteState.mZygoteOutputWriter.flush();
                    if (this.mZygoteSecondarySocketAddress != null) {
                        try {
                            attemptConnectionToSecondaryZygote();
                            try {
                                this.secondaryZygoteState.mZygoteOutputWriter.write(command);
                                this.secondaryZygoteState.mZygoteOutputWriter.flush();
                                this.secondaryZygoteState.mZygoteInputStream.readInt();
                            } catch (IOException ioe) {
                                throw new IllegalStateException("USAP pool state change cause an irrecoverable error", ioe);
                            }
                        } catch (IOException e) {
                        }
                    }
                    try {
                        this.primaryZygoteState.mZygoteInputStream.readInt();
                    } catch (IOException ioe2) {
                        throw new IllegalStateException("USAP pool state change cause an irrecoverable error", ioe2);
                    }
                } catch (Throwable ioe3) {
                    throw ioe3;
                }
            } catch (IOException ioe4) {
                this.mUsapPoolEnabled = !this.mUsapPoolEnabled;
                Log.w(LOG_TAG, "Failed to inform zygotes of USAP pool status: " + ioe4.getMessage());
            }
        }
    }

    public ChildZygoteProcess startChildZygote(String processClass, String niceName, int uid, int gid, int[] gids, int runtimeFlags, String seInfo, String abi, String acceptedAbiList, String instructionSet, int uidRangeStart, int uidRangeEnd) {
        LocalSocketAddress serverAddress = new LocalSocketAddress(processClass + "/" + UUID.randomUUID().toString());
        String[] extraArgs = {Zygote.CHILD_ZYGOTE_SOCKET_NAME_ARG + serverAddress.getName(), Zygote.CHILD_ZYGOTE_ABI_LIST_ARG + acceptedAbiList, Zygote.CHILD_ZYGOTE_UID_RANGE_START + uidRangeStart, Zygote.CHILD_ZYGOTE_UID_RANGE_END + uidRangeEnd};
        try {
            Process.ProcessStartResult result = startViaZygote(processClass, niceName, uid, gid, gids, runtimeFlags, 0, 0, seInfo, abi, instructionSet, null, null, true, null, false, extraArgs);
            return new ChildZygoteProcess(serverAddress, result.pid);
        } catch (ZygoteStartFailedEx ex) {
            throw new RuntimeException("Starting child-zygote through Zygote failed", ex);
        }
    }
}
