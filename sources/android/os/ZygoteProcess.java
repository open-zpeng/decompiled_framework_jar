package android.os;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Process;
import android.util.Log;
import android.util.Slog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.Zygote;
import com.android.internal.util.Preconditions;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
/* loaded from: classes2.dex */
public class ZygoteProcess {
    private static final String LOG_TAG = "ZygoteProcess";
    static final int ZYGOTE_RETRY_MILLIS = 500;
    private List<String> mApiBlacklistExemptions;
    private int mHiddenApiAccessLogSampleRate;
    private final Object mLock;
    private final LocalSocketAddress mSecondarySocket;
    private final LocalSocketAddress mSocket;
    private ZygoteState primaryZygoteState;
    private ZygoteState secondaryZygoteState;

    public synchronized ZygoteProcess(String primarySocket, String secondarySocket) {
        this(new LocalSocketAddress(primarySocket, LocalSocketAddress.Namespace.RESERVED), new LocalSocketAddress(secondarySocket, LocalSocketAddress.Namespace.RESERVED));
    }

    public synchronized ZygoteProcess(LocalSocketAddress primarySocket, LocalSocketAddress secondarySocket) {
        this.mLock = new Object();
        this.mApiBlacklistExemptions = Collections.emptyList();
        this.mSocket = primarySocket;
        this.mSecondarySocket = secondarySocket;
    }

    public synchronized LocalSocketAddress getPrimarySocketAddress() {
        return this.mSocket;
    }

    /* loaded from: classes2.dex */
    public static class ZygoteState {
        final List<String> abiList;
        final DataInputStream inputStream;
        boolean mClosed;
        final LocalSocket socket;
        final BufferedWriter writer;

        private synchronized ZygoteState(LocalSocket socket, DataInputStream inputStream, BufferedWriter writer, List<String> abiList) {
            this.socket = socket;
            this.inputStream = inputStream;
            this.writer = writer;
            this.abiList = abiList;
        }

        public static synchronized ZygoteState connect(LocalSocketAddress address) throws IOException {
            LocalSocket zygoteSocket = new LocalSocket();
            try {
                zygoteSocket.connect(address);
                DataInputStream zygoteInputStream = new DataInputStream(zygoteSocket.getInputStream());
                BufferedWriter zygoteWriter = new BufferedWriter(new OutputStreamWriter(zygoteSocket.getOutputStream()), 256);
                String abiListString = ZygoteProcess.getAbiList(zygoteWriter, zygoteInputStream);
                Log.i("Zygote", "Process: zygote socket " + address.getNamespace() + "/" + address.getName() + " opened, supported ABIS: " + abiListString);
                return new ZygoteState(zygoteSocket, zygoteInputStream, zygoteWriter, Arrays.asList(abiListString.split(",")));
            } catch (IOException ex) {
                try {
                    zygoteSocket.close();
                } catch (IOException e) {
                }
                throw ex;
            }
        }

        synchronized boolean matches(String abi) {
            return this.abiList.contains(abi);
        }

        public synchronized void close() {
            try {
                this.socket.close();
            } catch (IOException ex) {
                Log.e(ZygoteProcess.LOG_TAG, "I/O exception on routine close", ex);
            }
            this.mClosed = true;
        }

        synchronized boolean isClosed() {
            return this.mClosed;
        }
    }

    public final synchronized Process.ProcessStartResult start(String processClass, String niceName, int uid, int gid, int[] gids, int runtimeFlags, int mountExternal, int targetSdkVersion, String seInfo, String abi, String instructionSet, String appDataDir, String invokeWith, String[] zygoteArgs) {
        try {
            return startViaZygote(processClass, niceName, uid, gid, gids, runtimeFlags, mountExternal, targetSdkVersion, seInfo, abi, instructionSet, appDataDir, invokeWith, false, zygoteArgs);
        } catch (ZygoteStartFailedEx ex) {
            Log.e(LOG_TAG, "Starting VM process through Zygote failed");
            throw new RuntimeException("Starting VM process through Zygote failed", ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy("mLock")
    public static synchronized String getAbiList(BufferedWriter writer, DataInputStream inputStream) throws IOException {
        writer.write("1");
        writer.newLine();
        writer.write("--query-abi-list");
        writer.newLine();
        writer.flush();
        int numBytes = inputStream.readInt();
        byte[] bytes = new byte[numBytes];
        inputStream.readFully(bytes);
        return new String(bytes, StandardCharsets.US_ASCII);
    }

    @GuardedBy("mLock")
    private static synchronized Process.ProcessStartResult zygoteSendArgsAndGetResult(ZygoteState zygoteState, ArrayList<String> args) throws ZygoteStartFailedEx {
        try {
            int sz = args.size();
            for (int i = 0; i < sz; i++) {
                if (args.get(i).indexOf(10) >= 0) {
                    throw new ZygoteStartFailedEx("embedded newlines not allowed");
                }
            }
            BufferedWriter writer = zygoteState.writer;
            DataInputStream inputStream = zygoteState.inputStream;
            writer.write(Integer.toString(args.size()));
            writer.newLine();
            for (int i2 = 0; i2 < sz; i2++) {
                String arg = args.get(i2);
                writer.write(arg);
                writer.newLine();
            }
            writer.flush();
            Process.ProcessStartResult result = new Process.ProcessStartResult();
            result.pid = inputStream.readInt();
            result.usingWrapper = inputStream.readBoolean();
            if (result.pid < 0) {
                throw new ZygoteStartFailedEx("fork() failed");
            }
            return result;
        } catch (IOException ex) {
            zygoteState.close();
            throw new ZygoteStartFailedEx(ex);
        }
    }

    private synchronized Process.ProcessStartResult startViaZygote(String processClass, String niceName, int uid, int gid, int[] gids, int runtimeFlags, int mountExternal, int targetSdkVersion, String seInfo, String abi, String instructionSet, String appDataDir, String invokeWith, boolean startChildZygote, String[] extraArgs) throws ZygoteStartFailedEx {
        Process.ProcessStartResult zygoteSendArgsAndGetResult;
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
        }
        argsForZygote.add("--target-sdk-version=" + targetSdkVersion);
        if (gids != null && gids.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("--setgroups=");
            int sz2 = gids.length;
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= sz2) {
                    break;
                }
                if (i2 != 0) {
                    sz = sz2;
                    sb.append(',');
                } else {
                    sz = sz2;
                }
                int sz3 = gids[i2];
                sb.append(sz3);
                i = i2 + 1;
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
        argsForZygote.add(processClass);
        if (extraArgs != null) {
            int length = extraArgs.length;
            int i3 = 0;
            while (i3 < length) {
                int i4 = length;
                String arg = extraArgs[i3];
                argsForZygote.add(arg);
                i3++;
                length = i4;
            }
        }
        synchronized (this.mLock) {
            zygoteSendArgsAndGetResult = zygoteSendArgsAndGetResult(openZygoteSocketIfNeeded(abi), argsForZygote);
        }
        return zygoteSendArgsAndGetResult;
    }

    public synchronized void close() {
        if (this.primaryZygoteState != null) {
            this.primaryZygoteState.close();
        }
        if (this.secondaryZygoteState != null) {
            this.secondaryZygoteState.close();
        }
    }

    public synchronized void establishZygoteConnectionForAbi(String abi) {
        try {
            synchronized (this.mLock) {
                openZygoteSocketIfNeeded(abi);
            }
        } catch (ZygoteStartFailedEx ex) {
            throw new RuntimeException("Unable to connect to zygote for abi: " + abi, ex);
        }
    }

    public synchronized boolean setApiBlacklistExemptions(List<String> exemptions) {
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

    public synchronized void setHiddenApiAccessLogSampleRate(int rate) {
        synchronized (this.mLock) {
            this.mHiddenApiAccessLogSampleRate = rate;
            maybeSetHiddenApiAccessLogSampleRate(this.primaryZygoteState);
            maybeSetHiddenApiAccessLogSampleRate(this.secondaryZygoteState);
        }
    }

    @GuardedBy("mLock")
    private synchronized boolean maybeSetApiBlacklistExemptions(ZygoteState state, boolean sendIfEmpty) {
        if (state == null || state.isClosed()) {
            Slog.e(LOG_TAG, "Can't set API blacklist exemptions: no zygote connection");
            return false;
        } else if (sendIfEmpty || !this.mApiBlacklistExemptions.isEmpty()) {
            try {
                state.writer.write(Integer.toString(this.mApiBlacklistExemptions.size() + 1));
                state.writer.newLine();
                state.writer.write("--set-api-blacklist-exemptions");
                state.writer.newLine();
                for (int i = 0; i < this.mApiBlacklistExemptions.size(); i++) {
                    state.writer.write(this.mApiBlacklistExemptions.get(i));
                    state.writer.newLine();
                }
                state.writer.flush();
                int status = state.inputStream.readInt();
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

    private synchronized void maybeSetHiddenApiAccessLogSampleRate(ZygoteState state) {
        if (state == null || state.isClosed() || this.mHiddenApiAccessLogSampleRate == -1) {
            return;
        }
        try {
            state.writer.write(Integer.toString(1));
            state.writer.newLine();
            BufferedWriter bufferedWriter = state.writer;
            bufferedWriter.write("--hidden-api-log-sampling-rate=" + Integer.toString(this.mHiddenApiAccessLogSampleRate));
            state.writer.newLine();
            state.writer.flush();
            int status = state.inputStream.readInt();
            if (status != 0) {
                Slog.e(LOG_TAG, "Failed to set hidden API log sampling rate; status " + status);
            }
        } catch (IOException ioe) {
            Slog.e(LOG_TAG, "Failed to set hidden API log sampling rate", ioe);
        }
    }

    @GuardedBy("mLock")
    private synchronized ZygoteState openZygoteSocketIfNeeded(String abi) throws ZygoteStartFailedEx {
        Preconditions.checkState(Thread.holdsLock(this.mLock), "ZygoteProcess lock not held");
        if (this.primaryZygoteState == null || this.primaryZygoteState.isClosed()) {
            try {
                this.primaryZygoteState = ZygoteState.connect(this.mSocket);
                maybeSetApiBlacklistExemptions(this.primaryZygoteState, false);
                maybeSetHiddenApiAccessLogSampleRate(this.primaryZygoteState);
            } catch (IOException ioe) {
                throw new ZygoteStartFailedEx("Error connecting to primary zygote", ioe);
            }
        }
        if (this.primaryZygoteState.matches(abi)) {
            return this.primaryZygoteState;
        }
        if (this.secondaryZygoteState == null || this.secondaryZygoteState.isClosed()) {
            try {
                this.secondaryZygoteState = ZygoteState.connect(this.mSecondarySocket);
                maybeSetApiBlacklistExemptions(this.secondaryZygoteState, false);
                maybeSetHiddenApiAccessLogSampleRate(this.secondaryZygoteState);
            } catch (IOException ioe2) {
                throw new ZygoteStartFailedEx("Error connecting to secondary zygote", ioe2);
            }
        }
        if (this.secondaryZygoteState.matches(abi)) {
            return this.secondaryZygoteState;
        }
        throw new ZygoteStartFailedEx("Unsupported zygote ABI: " + abi);
    }

    public synchronized boolean preloadPackageForAbi(String packagePath, String libsPath, String libFileName, String cacheKey, String abi) throws ZygoteStartFailedEx, IOException {
        boolean z;
        synchronized (this.mLock) {
            ZygoteState state = openZygoteSocketIfNeeded(abi);
            state.writer.write("5");
            state.writer.newLine();
            state.writer.write("--preload-package");
            state.writer.newLine();
            state.writer.write(packagePath);
            state.writer.newLine();
            state.writer.write(libsPath);
            state.writer.newLine();
            state.writer.write(libFileName);
            state.writer.newLine();
            state.writer.write(cacheKey);
            state.writer.newLine();
            state.writer.flush();
            z = state.inputStream.readInt() == 0;
        }
        return z;
    }

    public synchronized boolean preloadDefault(String abi) throws ZygoteStartFailedEx, IOException {
        boolean z;
        synchronized (this.mLock) {
            ZygoteState state = openZygoteSocketIfNeeded(abi);
            state.writer.write("1");
            state.writer.newLine();
            state.writer.write("--preload-default");
            state.writer.newLine();
            state.writer.flush();
            z = state.inputStream.readInt() == 0;
        }
        return z;
    }

    public static synchronized void waitForConnectionToZygote(String socketName) {
        LocalSocketAddress address = new LocalSocketAddress(socketName, LocalSocketAddress.Namespace.RESERVED);
        waitForConnectionToZygote(address);
    }

    public static synchronized void waitForConnectionToZygote(LocalSocketAddress address) {
        for (int n = 20; n >= 0; n--) {
            try {
                ZygoteState zs = ZygoteState.connect(address);
                zs.close();
                return;
            } catch (IOException ioe) {
                Log.w(LOG_TAG, "Got error connecting to zygote, retrying. msg= " + ioe.getMessage());
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                }
            }
        }
        Slog.wtf(LOG_TAG, "Failed to connect to Zygote through socket " + address.getName());
    }

    public synchronized ChildZygoteProcess startChildZygote(String processClass, String niceName, int uid, int gid, int[] gids, int runtimeFlags, String seInfo, String abi, String instructionSet) {
        LocalSocketAddress serverAddress = new LocalSocketAddress(processClass + "/" + UUID.randomUUID().toString());
        StringBuilder sb = new StringBuilder();
        sb.append(Zygote.CHILD_ZYGOTE_SOCKET_NAME_ARG);
        sb.append(serverAddress.getName());
        String[] extraArgs = {sb.toString()};
        try {
            Process.ProcessStartResult result = startViaZygote(processClass, niceName, uid, gid, gids, runtimeFlags, 0, 0, seInfo, abi, instructionSet, null, null, true, extraArgs);
            return new ChildZygoteProcess(serverAddress, result.pid);
        } catch (ZygoteStartFailedEx ex) {
            throw new RuntimeException("Starting child-zygote through Zygote failed", ex);
        }
    }
}
