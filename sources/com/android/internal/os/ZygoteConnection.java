package com.android.internal.os;

import android.annotation.UnsupportedAppUsage;
import android.content.pm.ApplicationInfo;
import android.metrics.LogMaker;
import android.net.Credentials;
import android.net.LocalSocket;
import android.os.Parcel;
import android.os.Process;
import android.os.Trace;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import android.util.StatsLog;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto;
import dalvik.system.VMRuntime;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import libcore.io.IoUtils;

/* loaded from: classes3.dex */
class ZygoteConnection {
    private static final String TAG = "Zygote";
    private final String abiList;
    private boolean isEof;
    @UnsupportedAppUsage
    private final LocalSocket mSocket;
    @UnsupportedAppUsage
    private final DataOutputStream mSocketOutStream;
    private final BufferedReader mSocketReader;
    @UnsupportedAppUsage
    private final Credentials peer;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ZygoteConnection(LocalSocket socket, String abiList) throws IOException {
        this.mSocket = socket;
        this.abiList = abiList;
        this.mSocketOutStream = new DataOutputStream(socket.getOutputStream());
        this.mSocketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()), 256);
        this.mSocket.setSoTimeout(1000);
        try {
            this.peer = this.mSocket.getPeerCredentials();
            this.isEof = false;
        } catch (IOException ex) {
            Log.e(TAG, "Cannot read peer credentials", ex);
            throw ex;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FileDescriptor getFileDescriptor() {
        return this.mSocket.getFileDescriptor();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Runnable processOneCommand(ZygoteServer zygoteServer) {
        ZygoteConnection zygoteConnection;
        FileDescriptor serverPipeFd;
        FileDescriptor serverPipeFd2;
        int[] fdsToIgnore;
        try {
            String[] args = Zygote.readArgumentList(this.mSocketReader);
            FileDescriptor[] descriptors = this.mSocket.getAncillaryFileDescriptors();
            if (args == null) {
                this.isEof = true;
                return null;
            }
            ZygoteArguments parsedArgs = new ZygoteArguments(args);
            if (parsedArgs.mAbiListQuery) {
                handleAbiListQuery();
                return null;
            } else if (parsedArgs.mPidQuery) {
                handlePidQuery();
                return null;
            } else if (parsedArgs.mUsapPoolStatusSpecified) {
                return handleUsapPoolStatusChange(zygoteServer, parsedArgs.mUsapPoolEnabled);
            } else {
                if (parsedArgs.mPreloadDefault) {
                    handlePreload();
                    return null;
                } else if (parsedArgs.mPreloadPackage != null) {
                    handlePreloadPackage(parsedArgs.mPreloadPackage, parsedArgs.mPreloadPackageLibs, parsedArgs.mPreloadPackageLibFileName, parsedArgs.mPreloadPackageCacheKey);
                    return null;
                } else if (canPreloadApp() && parsedArgs.mPreloadApp != null) {
                    byte[] rawParcelData = Base64.getDecoder().decode(parsedArgs.mPreloadApp);
                    Parcel appInfoParcel = Parcel.obtain();
                    appInfoParcel.unmarshall(rawParcelData, 0, rawParcelData.length);
                    appInfoParcel.setDataPosition(0);
                    ApplicationInfo appInfo = ApplicationInfo.CREATOR.createFromParcel(appInfoParcel);
                    appInfoParcel.recycle();
                    if (appInfo != null) {
                        handlePreloadApp(appInfo);
                        return null;
                    }
                    throw new IllegalArgumentException("Failed to deserialize --preload-app");
                } else if (parsedArgs.mApiBlacklistExemptions != null) {
                    return handleApiBlacklistExemptions(zygoteServer, parsedArgs.mApiBlacklistExemptions);
                } else {
                    if (parsedArgs.mHiddenApiAccessLogSampleRate != -1) {
                        zygoteConnection = this;
                    } else if (parsedArgs.mHiddenApiAccessStatslogSampleRate == -1) {
                        if (parsedArgs.mPermittedCapabilities != 0 || parsedArgs.mEffectiveCapabilities != 0) {
                            throw new ZygoteSecurityException("Client may not specify capabilities: permitted=0x" + Long.toHexString(parsedArgs.mPermittedCapabilities) + ", effective=0x" + Long.toHexString(parsedArgs.mEffectiveCapabilities));
                        }
                        Zygote.applyUidSecurityPolicy(parsedArgs, this.peer);
                        Zygote.applyInvokeWithSecurityPolicy(parsedArgs, this.peer);
                        Zygote.applyDebuggerSystemProperty(parsedArgs);
                        Zygote.applyInvokeWithSystemProperty(parsedArgs);
                        int[][] rlimits = parsedArgs.mRLimits != null ? (int[][]) parsedArgs.mRLimits.toArray(Zygote.INT_ARRAY_2D) : null;
                        if (parsedArgs.mInvokeWith != null) {
                            try {
                                FileDescriptor[] pipeFds = Os.pipe2(OsConstants.O_CLOEXEC);
                                FileDescriptor childPipeFd = pipeFds[1];
                                FileDescriptor serverPipeFd3 = pipeFds[0];
                                Os.fcntlInt(childPipeFd, OsConstants.F_SETFD, 0);
                                int[] fdsToIgnore2 = {childPipeFd.getInt$(), serverPipeFd3.getInt$()};
                                serverPipeFd = serverPipeFd3;
                                serverPipeFd2 = childPipeFd;
                                fdsToIgnore = fdsToIgnore2;
                            } catch (ErrnoException errnoEx) {
                                throw new IllegalStateException("Unable to set up pipe for invoke-with", errnoEx);
                            }
                        } else {
                            serverPipeFd = null;
                            serverPipeFd2 = null;
                            fdsToIgnore = null;
                        }
                        int[] fdsToIgnore3 = {-1, -1};
                        FileDescriptor fd = this.mSocket.getFileDescriptor();
                        if (fd != null) {
                            fdsToIgnore3[0] = fd.getInt$();
                        }
                        FileDescriptor fd2 = zygoteServer.getZygoteSocketFileDescriptor();
                        if (fd2 != null) {
                            fdsToIgnore3[1] = fd2.getInt$();
                        }
                        FileDescriptor serverPipeFd4 = serverPipeFd;
                        int pid = Zygote.forkAndSpecialize(parsedArgs.mUid, parsedArgs.mGid, parsedArgs.mGids, parsedArgs.mRuntimeFlags, rlimits, parsedArgs.mMountExternal, parsedArgs.mSeInfo, parsedArgs.mNiceName, fdsToIgnore3, fdsToIgnore, parsedArgs.mStartChildZygote, parsedArgs.mInstructionSet, parsedArgs.mAppDataDir, parsedArgs.mTargetSdkVersion);
                        if (pid == 0) {
                            try {
                                zygoteServer.setForkChild();
                                zygoteServer.closeServerSocket();
                                IoUtils.closeQuietly(serverPipeFd4);
                                try {
                                    try {
                                        Runnable handleChildProc = handleChildProc(parsedArgs, descriptors, serverPipeFd2, parsedArgs.mStartChildZygote);
                                        IoUtils.closeQuietly(serverPipeFd2);
                                        IoUtils.closeQuietly((FileDescriptor) null);
                                        return handleChildProc;
                                    } catch (Throwable th) {
                                        th = th;
                                        serverPipeFd4 = null;
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    serverPipeFd4 = null;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                            }
                        } else {
                            try {
                                IoUtils.closeQuietly(serverPipeFd2);
                                serverPipeFd2 = null;
                                try {
                                    handleParentProc(pid, descriptors, serverPipeFd4);
                                    IoUtils.closeQuietly((FileDescriptor) null);
                                    IoUtils.closeQuietly(serverPipeFd4);
                                    return null;
                                } catch (Throwable th4) {
                                    th = th4;
                                    serverPipeFd4 = serverPipeFd4;
                                }
                            } catch (Throwable th5) {
                                th = th5;
                            }
                        }
                        IoUtils.closeQuietly(serverPipeFd2);
                        IoUtils.closeQuietly(serverPipeFd4);
                        throw th;
                    } else {
                        zygoteConnection = this;
                    }
                    return zygoteConnection.handleHiddenApiAccessLogSampleRate(zygoteServer, parsedArgs.mHiddenApiAccessLogSampleRate, parsedArgs.mHiddenApiAccessStatslogSampleRate);
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("IOException on command socket", ex);
        }
    }

    private void handleAbiListQuery() {
        try {
            byte[] abiListBytes = this.abiList.getBytes(StandardCharsets.US_ASCII);
            this.mSocketOutStream.writeInt(abiListBytes.length);
            this.mSocketOutStream.write(abiListBytes);
        } catch (IOException ioe) {
            throw new IllegalStateException("Error writing to command socket", ioe);
        }
    }

    private void handlePidQuery() {
        try {
            String pidString = String.valueOf(Process.myPid());
            byte[] pidStringBytes = pidString.getBytes(StandardCharsets.US_ASCII);
            this.mSocketOutStream.writeInt(pidStringBytes.length);
            this.mSocketOutStream.write(pidStringBytes);
        } catch (IOException ioe) {
            throw new IllegalStateException("Error writing to command socket", ioe);
        }
    }

    private void handlePreload() {
        try {
            if (isPreloadComplete()) {
                this.mSocketOutStream.writeInt(1);
                return;
            }
            preload();
            this.mSocketOutStream.writeInt(0);
        } catch (IOException ioe) {
            throw new IllegalStateException("Error writing to command socket", ioe);
        }
    }

    private Runnable stateChangeWithUsapPoolReset(ZygoteServer zygoteServer, Runnable stateChangeCode) {
        try {
            if (zygoteServer.isUsapPoolEnabled()) {
                Log.i(TAG, "Emptying USAP Pool due to state change.");
                Zygote.emptyUsapPool();
            }
            stateChangeCode.run();
            if (zygoteServer.isUsapPoolEnabled()) {
                Runnable fpResult = zygoteServer.fillUsapPool(new int[]{this.mSocket.getFileDescriptor().getInt$()});
                if (fpResult != null) {
                    zygoteServer.setForkChild();
                    return fpResult;
                }
                Log.i(TAG, "Finished refilling USAP Pool after state change.");
            }
            this.mSocketOutStream.writeInt(0);
            return null;
        } catch (IOException ioe) {
            throw new IllegalStateException("Error writing to command socket", ioe);
        }
    }

    private Runnable handleApiBlacklistExemptions(ZygoteServer zygoteServer, final String[] exemptions) {
        return stateChangeWithUsapPoolReset(zygoteServer, new Runnable() { // from class: com.android.internal.os.-$$Lambda$ZygoteConnection$xjqM7qW7vAjTqh2tR5XRF5Vn5mk
            @Override // java.lang.Runnable
            public final void run() {
                ZygoteInit.setApiBlacklistExemptions(exemptions);
            }
        });
    }

    private Runnable handleUsapPoolStatusChange(ZygoteServer zygoteServer, boolean newStatus) {
        try {
            Runnable fpResult = zygoteServer.setUsapPoolStatus(newStatus, this.mSocket);
            if (fpResult == null) {
                this.mSocketOutStream.writeInt(0);
            } else {
                zygoteServer.setForkChild();
            }
            return fpResult;
        } catch (IOException ioe) {
            throw new IllegalStateException("Error writing to command socket", ioe);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class HiddenApiUsageLogger implements VMRuntime.HiddenApiUsageLogger {
        private static HiddenApiUsageLogger sInstance = new HiddenApiUsageLogger();
        private final MetricsLogger mMetricsLogger = new MetricsLogger();
        private int mHiddenApiAccessLogSampleRate = 0;
        private int mHiddenApiAccessStatslogSampleRate = 0;

        private HiddenApiUsageLogger() {
        }

        public static void setHiddenApiAccessLogSampleRates(int sampleRate, int newSampleRate) {
            if (sampleRate != -1) {
                sInstance.mHiddenApiAccessLogSampleRate = sampleRate;
            }
            if (newSampleRate != -1) {
                sInstance.mHiddenApiAccessStatslogSampleRate = newSampleRate;
            }
        }

        public static HiddenApiUsageLogger getInstance() {
            return sInstance;
        }

        public void hiddenApiUsed(int sampledValue, String packageName, String signature, int accessMethod, boolean accessDenied) {
            if (sampledValue < this.mHiddenApiAccessLogSampleRate) {
                logUsage(packageName, signature, accessMethod, accessDenied);
            }
            if (sampledValue < this.mHiddenApiAccessStatslogSampleRate) {
                newLogUsage(signature, accessMethod, accessDenied);
            }
        }

        private void logUsage(String packageName, String signature, int accessMethod, boolean accessDenied) {
            int accessMethodMetric = 0;
            if (accessMethod != 0) {
                if (accessMethod == 1) {
                    accessMethodMetric = 1;
                } else if (accessMethod == 2) {
                    accessMethodMetric = 2;
                } else if (accessMethod == 3) {
                    accessMethodMetric = 3;
                }
            } else {
                accessMethodMetric = 0;
            }
            LogMaker logMaker = new LogMaker((int) MetricsProto.MetricsEvent.ACTION_HIDDEN_API_ACCESSED).setPackageName(packageName).addTaggedData(MetricsProto.MetricsEvent.FIELD_HIDDEN_API_SIGNATURE, signature).addTaggedData(MetricsProto.MetricsEvent.FIELD_HIDDEN_API_ACCESS_METHOD, Integer.valueOf(accessMethodMetric));
            if (accessDenied) {
                logMaker.addTaggedData(MetricsProto.MetricsEvent.FIELD_HIDDEN_API_ACCESS_DENIED, 1);
            }
            this.mMetricsLogger.write(logMaker);
        }

        private void newLogUsage(String signature, int accessMethod, boolean accessDenied) {
            int accessMethodProto = 0;
            if (accessMethod == 0) {
                accessMethodProto = 0;
            } else if (accessMethod == 1) {
                accessMethodProto = 1;
            } else if (accessMethod == 2) {
                accessMethodProto = 2;
            } else if (accessMethod == 3) {
                accessMethodProto = 3;
            }
            int uid = Process.myUid();
            StatsLog.write(178, uid, signature, accessMethodProto, accessDenied);
        }
    }

    private Runnable handleHiddenApiAccessLogSampleRate(ZygoteServer zygoteServer, final int samplingRate, final int statsdSamplingRate) {
        return stateChangeWithUsapPoolReset(zygoteServer, new Runnable() { // from class: com.android.internal.os.-$$Lambda$ZygoteConnection$KxVsZ-s4KsanePOHCU5JcuypPik
            @Override // java.lang.Runnable
            public final void run() {
                ZygoteConnection.lambda$handleHiddenApiAccessLogSampleRate$1(samplingRate, statsdSamplingRate);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$handleHiddenApiAccessLogSampleRate$1(int samplingRate, int statsdSamplingRate) {
        int maxSamplingRate = Math.max(samplingRate, statsdSamplingRate);
        ZygoteInit.setHiddenApiAccessLogSampleRate(maxSamplingRate);
        HiddenApiUsageLogger.setHiddenApiAccessLogSampleRates(samplingRate, statsdSamplingRate);
        ZygoteInit.setHiddenApiUsageLogger(HiddenApiUsageLogger.getInstance());
    }

    protected void preload() {
        ZygoteInit.lazyPreload();
    }

    protected boolean isPreloadComplete() {
        return ZygoteInit.isPreloadComplete();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DataOutputStream getSocketOutputStream() {
        return this.mSocketOutStream;
    }

    protected void handlePreloadPackage(String packagePath, String libsPath, String libFileName, String cacheKey) {
        throw new RuntimeException("Zygote does not support package preloading");
    }

    protected boolean canPreloadApp() {
        return false;
    }

    protected void handlePreloadApp(ApplicationInfo aInfo) {
        throw new RuntimeException("Zygote does not support app preloading");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public void closeSocket() {
        try {
            this.mSocket.close();
        } catch (IOException ex) {
            Log.e(TAG, "Exception while closing command socket in parent", ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isClosedByPeer() {
        return this.isEof;
    }

    private Runnable handleChildProc(ZygoteArguments parsedArgs, FileDescriptor[] descriptors, FileDescriptor pipeFd, boolean isZygote) {
        closeSocket();
        if (descriptors != null) {
            try {
                Os.dup2(descriptors[0], OsConstants.STDIN_FILENO);
                Os.dup2(descriptors[1], OsConstants.STDOUT_FILENO);
                Os.dup2(descriptors[2], OsConstants.STDERR_FILENO);
                for (FileDescriptor fd : descriptors) {
                    IoUtils.closeQuietly(fd);
                }
            } catch (ErrnoException ex) {
                Log.e(TAG, "Error reopening stdio", ex);
            }
        }
        if (parsedArgs.mNiceName != null) {
            Process.setArgV0(parsedArgs.mNiceName);
        }
        Trace.traceEnd(64L);
        if (parsedArgs.mInvokeWith == null) {
            return !isZygote ? ZygoteInit.zygoteInit(parsedArgs.mTargetSdkVersion, parsedArgs.mRemainingArgs, null) : ZygoteInit.childZygoteInit(parsedArgs.mTargetSdkVersion, parsedArgs.mRemainingArgs, null);
        }
        WrapperInit.execApplication(parsedArgs.mInvokeWith, parsedArgs.mNiceName, parsedArgs.mTargetSdkVersion, VMRuntime.getCurrentInstructionSet(), pipeFd, parsedArgs.mRemainingArgs);
        throw new IllegalStateException("WrapperInit.execApplication unexpectedly returned");
    }

    /* JADX WARN: Removed duplicated region for block: B:52:0x00c9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void handleParentProc(int r27, java.io.FileDescriptor[] r28, java.io.FileDescriptor r29) {
        /*
            Method dump skipped, instructions count: 288
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.ZygoteConnection.handleParentProc(int, java.io.FileDescriptor[], java.io.FileDescriptor):void");
    }

    private void setChildPgid(int pid) {
        try {
            Os.setpgid(pid, Os.getpgid(this.peer.getPid()));
        } catch (ErrnoException e) {
            Log.i(TAG, "Zygote: setpgid failed. This is normal if peer is not in our session");
        }
    }
}
