package com.android.internal.os;

import android.content.pm.ApplicationInfo;
import android.net.Credentials;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.Build;
import android.os.FactoryTest;
import android.os.Process;
import android.os.SystemProperties;
import android.os.Trace;
import android.provider.DeviceConfig;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import dalvik.system.ZygoteHooks;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import libcore.io.IoUtils;

/* loaded from: classes3.dex */
public final class Zygote {
    private static final String ANDROID_SOCKET_PREFIX = "ANDROID_SOCKET_";
    public static final int API_ENFORCEMENT_POLICY_MASK = 12288;
    public static final String CHILD_ZYGOTE_ABI_LIST_ARG = "--abi-list=";
    public static final String CHILD_ZYGOTE_SOCKET_NAME_ARG = "--zygote-socket=";
    public static final String CHILD_ZYGOTE_UID_RANGE_END = "--uid-range-end=";
    public static final String CHILD_ZYGOTE_UID_RANGE_START = "--uid-range-start=";
    public static final int DEBUG_ALWAYS_JIT = 64;
    public static final int DEBUG_ENABLE_ASSERT = 4;
    public static final int DEBUG_ENABLE_CHECKJNI = 2;
    public static final int DEBUG_ENABLE_JDWP = 1;
    public static final int DEBUG_ENABLE_JNI_LOGGING = 16;
    public static final int DEBUG_ENABLE_SAFEMODE = 8;
    public static final int DEBUG_GENERATE_DEBUG_INFO = 32;
    public static final int DEBUG_GENERATE_MINI_DEBUG_INFO = 2048;
    public static final int DEBUG_JAVA_DEBUGGABLE = 256;
    public static final int DEBUG_NATIVE_DEBUGGABLE = 128;
    public static final int DISABLE_VERIFIER = 512;
    public static final int MOUNT_EXTERNAL_DEFAULT = 1;
    public static final int MOUNT_EXTERNAL_FULL = 6;
    public static final int MOUNT_EXTERNAL_INSTALLER = 5;
    public static final int MOUNT_EXTERNAL_LEGACY = 4;
    public static final int MOUNT_EXTERNAL_NONE = 0;
    public static final int MOUNT_EXTERNAL_READ = 2;
    public static final int MOUNT_EXTERNAL_WRITE = 3;
    public static final int ONLY_USE_SYSTEM_OAT_FILES = 1024;
    public static final String PRIMARY_SOCKET_NAME = "zygote";
    public static final int PROFILE_FROM_SHELL = 32768;
    public static final int PROFILE_SYSTEM_SERVER = 16384;
    public static final long PROPERTY_CHECK_INTERVAL = 60000;
    public static final String SECONDARY_SOCKET_NAME = "zygote_secondary";
    public static final int SOCKET_BUFFER_SIZE = 256;
    private static final String USAP_ERROR_PREFIX = "Invalid command to USAP: ";
    public static final int USAP_MANAGEMENT_MESSAGE_BYTES = 8;
    public static final String USAP_POOL_PRIMARY_SOCKET_NAME = "usap_pool_primary";
    public static final String USAP_POOL_SECONDARY_SOCKET_NAME = "usap_pool_secondary";
    public static final int USE_APP_IMAGE_STARTUP_CACHE = 65536;
    public static final int API_ENFORCEMENT_POLICY_SHIFT = Integer.numberOfTrailingZeros(12288);
    protected static final int[][] INT_ARRAY_2D = (int[][]) Array.newInstance(int.class, 0, 0);

    /* JADX INFO: Access modifiers changed from: protected */
    public static native void nativeAllowFileAcrossFork(String str);

    private static native void nativeBlockSigTerm();

    private static native boolean nativeDisableExecuteOnly();

    private static native void nativeEmptyUsapPool();

    private static native int nativeForkAndSpecialize(int i, int i2, int[] iArr, int i3, int[][] iArr2, int i4, String str, String str2, int[] iArr3, int[] iArr4, boolean z, String str3, String str4);

    private static native int nativeForkSystemServer(int i, int i2, int[] iArr, int i3, int[][] iArr2, long j, long j2);

    private static native int nativeForkUsap(int i, int i2, int[] iArr);

    private static native int[] nativeGetUsapPipeFDs();

    private static native int nativeGetUsapPoolCount();

    private static native int nativeGetUsapPoolEventFD();

    protected static native void nativeInitNativeState(boolean z);

    /* JADX INFO: Access modifiers changed from: protected */
    public static native void nativeInstallSeccompUidGidFilter(int i, int i2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native void nativePreApplicationInit();

    private static native boolean nativeRemoveUsapTableEntry(int i);

    private static native void nativeSpecializeAppProcess(int i, int i2, int[] iArr, int i3, int[][] iArr2, int i4, String str, String str2, boolean z, String str3, String str4);

    private static native void nativeUnblockSigTerm();

    private Zygote() {
    }

    public static int forkAndSpecialize(int uid, int gid, int[] gids, int runtimeFlags, int[][] rlimits, int mountExternal, String seInfo, String niceName, int[] fdsToClose, int[] fdsToIgnore, boolean startChildZygote, String instructionSet, String appDataDir, int targetSdkVersion) {
        ZygoteHooks.preFork();
        resetNicePriority();
        int pid = nativeForkAndSpecialize(uid, gid, gids, runtimeFlags, rlimits, mountExternal, seInfo, niceName, fdsToClose, fdsToIgnore, startChildZygote, instructionSet, appDataDir);
        if (pid == 0) {
            disableExecuteOnly(targetSdkVersion);
            Trace.setTracingEnabled(true, runtimeFlags);
            Trace.traceBegin(64L, "PostFork");
        }
        ZygoteHooks.postForkCommon();
        return pid;
    }

    public static void specializeAppProcess(int uid, int gid, int[] gids, int runtimeFlags, int[][] rlimits, int mountExternal, String seInfo, String niceName, boolean startChildZygote, String instructionSet, String appDataDir) {
        nativeSpecializeAppProcess(uid, gid, gids, runtimeFlags, rlimits, mountExternal, seInfo, niceName, startChildZygote, instructionSet, appDataDir);
        Trace.setTracingEnabled(true, runtimeFlags);
        Trace.traceBegin(64L, "PostFork");
        ZygoteHooks.postForkCommon();
    }

    public static int forkSystemServer(int uid, int gid, int[] gids, int runtimeFlags, int[][] rlimits, long permittedCapabilities, long effectiveCapabilities) {
        ZygoteHooks.preFork();
        resetNicePriority();
        int pid = nativeForkSystemServer(uid, gid, gids, runtimeFlags, rlimits, permittedCapabilities, effectiveCapabilities);
        if (pid == 0) {
            Trace.setTracingEnabled(true, runtimeFlags);
        }
        ZygoteHooks.postForkCommon();
        return pid;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void allowAppFilesAcrossFork(ApplicationInfo appInfo) {
        String[] allApkPaths;
        for (String path : appInfo.getAllApkPaths()) {
            nativeAllowFileAcrossFork(path);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void initNativeState(boolean isPrimary) {
        nativeInitNativeState(isPrimary);
    }

    public static String getConfigurationProperty(String propertyName, String defaultValue) {
        return SystemProperties.get(String.join(".", "persist.device_config", DeviceConfig.NAMESPACE_RUNTIME_NATIVE, propertyName), defaultValue);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void emptyUsapPool() {
        nativeEmptyUsapPool();
    }

    public static boolean getConfigurationPropertyBoolean(String propertyName, Boolean defaultValue) {
        return SystemProperties.getBoolean(String.join(".", "persist.device_config", DeviceConfig.NAMESPACE_RUNTIME_NATIVE, propertyName), defaultValue.booleanValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getUsapPoolCount() {
        return nativeGetUsapPoolCount();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static FileDescriptor getUsapPoolEventFD() {
        FileDescriptor fd = new FileDescriptor();
        fd.setInt$(nativeGetUsapPoolEventFD());
        return fd;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Runnable forkUsap(LocalServerSocket usapPoolSocket, int[] sessionSocketRawFDs) {
        try {
            FileDescriptor[] pipeFDs = Os.pipe2(OsConstants.O_CLOEXEC);
            int pid = nativeForkUsap(pipeFDs[0].getInt$(), pipeFDs[1].getInt$(), sessionSocketRawFDs);
            if (pid == 0) {
                IoUtils.closeQuietly(pipeFDs[0]);
                return usapMain(usapPoolSocket, pipeFDs[1]);
            }
            IoUtils.closeQuietly(pipeFDs[1]);
            return null;
        } catch (ErrnoException errnoEx) {
            throw new IllegalStateException("Unable to create USAP pipe.", errnoEx);
        }
    }

    private static Runnable usapMain(LocalServerSocket usapPoolSocket, FileDescriptor writePipe) {
        DataOutputStream usapOutputStream;
        Credentials peerCredentials;
        String[] argStrings;
        ZygoteArguments args;
        int[][] rlimits;
        int pid = Process.myPid();
        Process.setArgV0(Process.is64Bit() ? "usap64" : "usap32");
        LocalSocket sessionSocket = null;
        while (true) {
            try {
                LocalSocket sessionSocket2 = usapPoolSocket.accept();
                sessionSocket = sessionSocket2;
                blockSigTerm();
                BufferedReader usapReader = new BufferedReader(new InputStreamReader(sessionSocket.getInputStream()));
                usapOutputStream = new DataOutputStream(sessionSocket.getOutputStream());
                peerCredentials = sessionSocket.getPeerCredentials();
                argStrings = readArgumentList(usapReader);
            } catch (Exception ex) {
                Log.e("USAP", ex.getMessage());
                IoUtils.closeQuietly(sessionSocket);
                unblockSigTerm();
            }
            if (argStrings != null) {
                args = new ZygoteArguments(argStrings);
                validateUsapCommand(args);
                try {
                    break;
                } finally {
                    unblockSigTerm();
                }
            }
            Log.e("USAP", "Truncated command received.");
            IoUtils.closeQuietly(sessionSocket);
            unblockSigTerm();
        }
        applyUidSecurityPolicy(args, peerCredentials);
        applyDebuggerSystemProperty(args);
        if (args.mRLimits == null) {
            rlimits = null;
        } else {
            int[][] rlimits2 = (int[][]) args.mRLimits.toArray(INT_ARRAY_2D);
            rlimits = rlimits2;
        }
        try {
            usapOutputStream.writeInt(pid);
            IoUtils.closeQuietly(sessionSocket);
            try {
                Os.close(usapPoolSocket.getFileDescriptor());
                try {
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream(8);
                    DataOutputStream outputStream = new DataOutputStream(buffer);
                    outputStream.writeLong(pid);
                    outputStream.flush();
                    Os.write(writePipe, buffer.toByteArray(), 0, buffer.size());
                    IoUtils.closeQuietly(writePipe);
                    specializeAppProcess(args.mUid, args.mGid, args.mGids, args.mRuntimeFlags, rlimits, args.mMountExternal, args.mSeInfo, args.mNiceName, args.mStartChildZygote, args.mInstructionSet, args.mAppDataDir);
                    disableExecuteOnly(args.mTargetSdkVersion);
                    if (args.mNiceName != null) {
                        Process.setArgV0(args.mNiceName);
                    }
                    Trace.traceEnd(64L);
                    return ZygoteInit.zygoteInit(args.mTargetSdkVersion, args.mRemainingArgs, null);
                } catch (Exception ex2) {
                    Log.e("USAP", String.format("Failed to write PID (%d) to pipe (%d): %s", Integer.valueOf(pid), Integer.valueOf(writePipe.getInt$()), ex2.getMessage()));
                    throw new RuntimeException(ex2);
                }
            } catch (ErrnoException ex3) {
                Log.e("USAP", "Failed to close USAP pool socket");
                throw new RuntimeException(ex3);
            }
        } catch (IOException ioEx) {
            Log.e("USAP", "Failed to write response to session socket: " + ioEx.getMessage());
            throw new RuntimeException(ioEx);
        }
    }

    private static void blockSigTerm() {
        nativeBlockSigTerm();
    }

    private static void unblockSigTerm() {
        nativeUnblockSigTerm();
    }

    private static void validateUsapCommand(ZygoteArguments args) {
        if (args.mAbiListQuery) {
            throw new IllegalArgumentException("Invalid command to USAP: --query-abi-list");
        }
        if (args.mPidQuery) {
            throw new IllegalArgumentException("Invalid command to USAP: --get-pid");
        }
        if (args.mPreloadDefault) {
            throw new IllegalArgumentException("Invalid command to USAP: --preload-default");
        }
        if (args.mPreloadPackage != null) {
            throw new IllegalArgumentException("Invalid command to USAP: --preload-package");
        }
        if (args.mPreloadApp != null) {
            throw new IllegalArgumentException("Invalid command to USAP: --preload-app");
        }
        if (args.mStartChildZygote) {
            throw new IllegalArgumentException("Invalid command to USAP: --start-child-zygote");
        }
        if (args.mApiBlacklistExemptions != null) {
            throw new IllegalArgumentException("Invalid command to USAP: --set-api-blacklist-exemptions");
        }
        if (args.mHiddenApiAccessLogSampleRate != -1) {
            throw new IllegalArgumentException("Invalid command to USAP: --hidden-api-log-sampling-rate=");
        }
        if (args.mHiddenApiAccessStatslogSampleRate != -1) {
            throw new IllegalArgumentException("Invalid command to USAP: --hidden-api-statslog-sampling-rate=");
        }
        if (args.mInvokeWith != null) {
            throw new IllegalArgumentException("Invalid command to USAP: --invoke-with");
        }
        if (args.mPermittedCapabilities != 0 || args.mEffectiveCapabilities != 0) {
            throw new ZygoteSecurityException("Client may not specify capabilities: permitted=0x" + Long.toHexString(args.mPermittedCapabilities) + ", effective=0x" + Long.toHexString(args.mEffectiveCapabilities));
        }
    }

    protected static void disableExecuteOnly(int targetSdkVersion) {
        if (targetSdkVersion < 29 && !nativeDisableExecuteOnly()) {
            Log.e("Zygote", "Failed to set libraries to read+execute.");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int[] getUsapPipeFDs() {
        return nativeGetUsapPipeFDs();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean removeUsapTableEntry(int usapPID) {
        return nativeRemoveUsapTableEntry(usapPID);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void applyUidSecurityPolicy(ZygoteArguments args, Credentials peer) throws ZygoteSecurityException {
        if (peer.getUid() == 1000) {
            boolean uidRestricted = FactoryTest.getMode() == 0;
            if (uidRestricted && args.mUidSpecified && args.mUid < 1000) {
                throw new ZygoteSecurityException("System UID may not launch process with UID < 1000");
            }
        }
        boolean uidRestricted2 = args.mUidSpecified;
        if (!uidRestricted2) {
            args.mUid = peer.getUid();
            args.mUidSpecified = true;
        }
        if (!args.mGidSpecified) {
            args.mGid = peer.getGid();
            args.mGidSpecified = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void applyDebuggerSystemProperty(ZygoteArguments args) {
        if (RoSystemProperties.DEBUGGABLE) {
            args.mRuntimeFlags |= 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void applyInvokeWithSecurityPolicy(ZygoteArguments args, Credentials peer) throws ZygoteSecurityException {
        int peerUid = peer.getUid();
        if (args.mInvokeWith != null && peerUid != 0 && (args.mRuntimeFlags & 1) == 0) {
            throw new ZygoteSecurityException("Peer is permitted to specify an explicit invoke-with wrapper command only for debuggable applications.");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void applyInvokeWithSystemProperty(ZygoteArguments args) {
        if (args.mInvokeWith == null && args.mNiceName != null) {
            String property = "wrap." + args.mNiceName;
            args.mInvokeWith = SystemProperties.get(property);
            if (!Build.IS_USER && args.mInvokeWith != null && args.mInvokeWith.length() == 0) {
                String property2 = "persist.wrap." + args.mNiceName;
                args.mInvokeWith = SystemProperties.get(property2);
            }
            if (args.mInvokeWith != null && args.mInvokeWith.length() == 0) {
                args.mInvokeWith = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] readArgumentList(BufferedReader socketReader) throws IOException {
        try {
            String argc_string = socketReader.readLine();
            if (argc_string == null) {
                return null;
            }
            int argc = Integer.parseInt(argc_string);
            if (argc > 1024) {
                throw new IOException("Max arg count exceeded");
            }
            String[] args = new String[argc];
            for (int arg_index = 0; arg_index < argc; arg_index++) {
                args[arg_index] = socketReader.readLine();
                if (args[arg_index] == null) {
                    throw new IOException("Truncated request");
                }
            }
            return args;
        } catch (NumberFormatException e) {
            Log.e("Zygote", "Invalid Zygote wire format: non-int at argc");
            throw new IOException("Invalid wire format");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static LocalServerSocket createManagedSocketFromInitSocket(String socketName) {
        String fullSocketName = ANDROID_SOCKET_PREFIX + socketName;
        try {
            String env = System.getenv(fullSocketName);
            int fileDesc = Integer.parseInt(env);
            try {
                FileDescriptor fd = new FileDescriptor();
                fd.setInt$(fileDesc);
                return new LocalServerSocket(fd);
            } catch (IOException ex) {
                throw new RuntimeException("Error building socket from file descriptor: " + fileDesc, ex);
            }
        } catch (RuntimeException ex2) {
            throw new RuntimeException("Socket unset or invalid: " + fullSocketName, ex2);
        }
    }

    private static void callPostForkSystemServerHooks() {
        ZygoteHooks.postForkSystemServer();
    }

    private static void callPostForkChildHooks(int runtimeFlags, boolean isSystemServer, boolean isZygote, String instructionSet) {
        ZygoteHooks.postForkChild(runtimeFlags, isSystemServer, isZygote, instructionSet);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void resetNicePriority() {
        Thread.currentThread().setPriority(5);
    }

    public static void execShell(String command) {
        String[] args = {"/system/bin/sh", "-c", command};
        try {
            Os.execv(args[0], args);
        } catch (ErrnoException e) {
            throw new RuntimeException(e);
        }
    }

    public static void appendQuotedShellArgs(StringBuilder command, String[] args) {
        for (String arg : args) {
            command.append(" '");
            command.append(arg.replace("'", "'\\''"));
            command.append("'");
        }
    }
}
