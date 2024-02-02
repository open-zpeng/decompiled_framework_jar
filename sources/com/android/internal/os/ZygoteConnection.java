package com.android.internal.os;

import android.net.Credentials;
import android.net.LocalSocket;
import android.os.FactoryTest;
import android.os.Process;
import android.os.SystemProperties;
import android.os.Trace;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructPollfd;
import android.util.Log;
import dalvik.system.VMRuntime;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import libcore.io.IoUtils;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class ZygoteConnection {
    private static final String TAG = "Zygote";
    private static final int[][] intArray2d = (int[][]) Array.newInstance(int.class, 0, 0);
    private final String abiList;
    private boolean isEof;
    private final LocalSocket mSocket;
    private final DataOutputStream mSocketOutStream;
    private final BufferedReader mSocketReader;
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
    public FileDescriptor getFileDesciptor() {
        return this.mSocket.getFileDescriptor();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Runnable processOneCommand(ZygoteServer zygoteServer) {
        try {
            String[] args = readArgumentList();
            FileDescriptor[] descriptors = this.mSocket.getAncillaryFileDescriptors();
            if (args == null) {
                this.isEof = true;
                return null;
            }
            FileDescriptor childPipeFd = null;
            FileDescriptor childPipeFd2 = null;
            Arguments parsedArgs = new Arguments(args);
            if (parsedArgs.abiListQuery) {
                handleAbiListQuery();
                return null;
            } else if (parsedArgs.preloadDefault) {
                handlePreload();
                return null;
            } else if (parsedArgs.preloadPackage != null) {
                handlePreloadPackage(parsedArgs.preloadPackage, parsedArgs.preloadPackageLibs, parsedArgs.preloadPackageLibFileName, parsedArgs.preloadPackageCacheKey);
                return null;
            } else if (parsedArgs.apiBlacklistExemptions != null) {
                handleApiBlacklistExemptions(parsedArgs.apiBlacklistExemptions);
                return null;
            } else if (parsedArgs.hiddenApiAccessLogSampleRate != -1) {
                handleHiddenApiAccessLogSampleRate(parsedArgs.hiddenApiAccessLogSampleRate);
                return null;
            } else if (parsedArgs.permittedCapabilities != 0 || parsedArgs.effectiveCapabilities != 0) {
                throw new ZygoteSecurityException("Client may not specify capabilities: permitted=0x" + Long.toHexString(parsedArgs.permittedCapabilities) + ", effective=0x" + Long.toHexString(parsedArgs.effectiveCapabilities));
            } else {
                applyUidSecurityPolicy(parsedArgs, this.peer);
                applyInvokeWithSecurityPolicy(parsedArgs, this.peer);
                applyDebuggerSystemProperty(parsedArgs);
                applyInvokeWithSystemProperty(parsedArgs);
                int[][] rlimits = parsedArgs.rlimits != null ? (int[][]) parsedArgs.rlimits.toArray(intArray2d) : null;
                int[] fdsToIgnore = null;
                if (parsedArgs.invokeWith != null) {
                    try {
                        FileDescriptor[] pipeFds = Os.pipe2(OsConstants.O_CLOEXEC);
                        childPipeFd = pipeFds[1];
                        childPipeFd2 = pipeFds[0];
                        Os.fcntlInt(childPipeFd, OsConstants.F_SETFD, 0);
                        fdsToIgnore = new int[]{childPipeFd.getInt$(), childPipeFd2.getInt$()};
                    } catch (ErrnoException errnoEx) {
                        throw new IllegalStateException("Unable to set up pipe for invoke-with", errnoEx);
                    }
                }
                FileDescriptor serverPipeFd = childPipeFd2;
                FileDescriptor childPipeFd3 = childPipeFd;
                int[] fdsToIgnore2 = fdsToIgnore;
                int[] fdsToClose = {-1, -1};
                FileDescriptor fd = this.mSocket.getFileDescriptor();
                if (fd != null) {
                    fdsToClose[0] = fd.getInt$();
                }
                FileDescriptor fd2 = zygoteServer.getServerSocketFileDescriptor();
                if (fd2 != null) {
                    fdsToClose[1] = fd2.getInt$();
                }
                int i = parsedArgs.uid;
                int i2 = parsedArgs.gid;
                int[] iArr = parsedArgs.gids;
                int i3 = parsedArgs.runtimeFlags;
                int i4 = parsedArgs.mountExternal;
                String str = parsedArgs.seInfo;
                String str2 = parsedArgs.niceName;
                boolean z = parsedArgs.startChildZygote;
                String str3 = parsedArgs.instructionSet;
                String str4 = parsedArgs.appDataDir;
                int[][] iArr2 = rlimits;
                FileDescriptor serverPipeFd2 = serverPipeFd;
                int pid = Zygote.forkAndSpecialize(i, i2, iArr, i3, iArr2, i4, str, str2, fdsToClose, fdsToIgnore2, z, str3, str4);
                if (pid == 0) {
                    try {
                        zygoteServer.setForkChild();
                        zygoteServer.closeServerSocket();
                        IoUtils.closeQuietly(serverPipeFd2);
                        try {
                            try {
                                Runnable handleChildProc = handleChildProc(parsedArgs, descriptors, childPipeFd3, parsedArgs.startChildZygote);
                                IoUtils.closeQuietly(childPipeFd3);
                                IoUtils.closeQuietly((FileDescriptor) null);
                                return handleChildProc;
                            } catch (Throwable th) {
                                th = th;
                                serverPipeFd2 = null;
                                IoUtils.closeQuietly(childPipeFd3);
                                IoUtils.closeQuietly(serverPipeFd2);
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } else {
                    try {
                        IoUtils.closeQuietly(childPipeFd3);
                        childPipeFd3 = null;
                        handleParentProc(pid, descriptors, serverPipeFd2);
                        IoUtils.closeQuietly((FileDescriptor) null);
                        IoUtils.closeQuietly(serverPipeFd2);
                        return null;
                    } catch (Throwable th4) {
                        th = th4;
                    }
                }
                IoUtils.closeQuietly(childPipeFd3);
                IoUtils.closeQuietly(serverPipeFd2);
                throw th;
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

    private void handleApiBlacklistExemptions(String[] exemptions) {
        try {
            ZygoteInit.setApiBlacklistExemptions(exemptions);
            this.mSocketOutStream.writeInt(0);
        } catch (IOException ioe) {
            throw new IllegalStateException("Error writing to command socket", ioe);
        }
    }

    private void handleHiddenApiAccessLogSampleRate(int percent) {
        try {
            ZygoteInit.setHiddenApiAccessLogSampleRate(percent);
            this.mSocketOutStream.writeInt(0);
        } catch (IOException ioe) {
            throw new IllegalStateException("Error writing to command socket", ioe);
        }
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
        throw new RuntimeException("Zyogte does not support package preloading");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class Arguments {
        boolean abiListQuery;
        String[] apiBlacklistExemptions;
        String appDataDir;
        boolean capabilitiesSpecified;
        long effectiveCapabilities;
        boolean gidSpecified;
        int[] gids;
        String instructionSet;
        String invokeWith;
        String niceName;
        long permittedCapabilities;
        boolean preloadDefault;
        String preloadPackage;
        String preloadPackageCacheKey;
        String preloadPackageLibFileName;
        String preloadPackageLibs;
        String[] remainingArgs;
        ArrayList<int[]> rlimits;
        int runtimeFlags;
        String seInfo;
        boolean seInfoSpecified;
        boolean startChildZygote;
        int targetSdkVersion;
        boolean targetSdkVersionSpecified;
        boolean uidSpecified;
        int uid = 0;
        int gid = 0;
        int mountExternal = 0;
        int hiddenApiAccessLogSampleRate = -1;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Arguments(String[] args) throws IllegalArgumentException {
            parseArgs(args);
        }

        /* JADX WARN: Code restructure failed: missing block: B:136:0x02d2, code lost:
            if (r10.abiListQuery == false) goto L190;
         */
        /* JADX WARN: Code restructure failed: missing block: B:138:0x02d6, code lost:
            if ((r11.length - r1) > 0) goto L173;
         */
        /* JADX WARN: Code restructure failed: missing block: B:141:0x02e0, code lost:
            throw new java.lang.IllegalArgumentException("Unexpected arguments after --query-abi-list.");
         */
        /* JADX WARN: Code restructure failed: missing block: B:143:0x02e3, code lost:
            if (r10.preloadPackage == null) goto L196;
         */
        /* JADX WARN: Code restructure failed: missing block: B:145:0x02e7, code lost:
            if ((r11.length - r1) > 0) goto L194;
         */
        /* JADX WARN: Code restructure failed: missing block: B:148:0x02f1, code lost:
            throw new java.lang.IllegalArgumentException("Unexpected arguments after --preload-package.");
         */
        /* JADX WARN: Code restructure failed: missing block: B:149:0x02f2, code lost:
            if (r0 == 0) goto L175;
         */
        /* JADX WARN: Code restructure failed: missing block: B:150:0x02f4, code lost:
            if (r3 == false) goto L199;
         */
        /* JADX WARN: Code restructure failed: missing block: B:151:0x02f6, code lost:
            r10.remainingArgs = new java.lang.String[r11.length - r1];
            java.lang.System.arraycopy(r11, r1, r10.remainingArgs, 0, r10.remainingArgs.length);
         */
        /* JADX WARN: Code restructure failed: missing block: B:153:0x031d, code lost:
            throw new java.lang.IllegalArgumentException("Unexpected argument : " + r11[r1]);
         */
        /* JADX WARN: Code restructure failed: missing block: B:155:0x0320, code lost:
            if (r10.startChildZygote == false) goto L189;
         */
        /* JADX WARN: Code restructure failed: missing block: B:156:0x0322, code lost:
            r2 = false;
            r4 = r10.remainingArgs;
            r6 = r4.length;
         */
        /* JADX WARN: Code restructure failed: missing block: B:157:0x0326, code lost:
            if (r5 >= r6) goto L188;
         */
        /* JADX WARN: Code restructure failed: missing block: B:159:0x0330, code lost:
            if (r4[r5].startsWith(com.android.internal.os.Zygote.CHILD_ZYGOTE_SOCKET_NAME_ARG) == false) goto L181;
         */
        /* JADX WARN: Code restructure failed: missing block: B:160:0x0332, code lost:
            r2 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:161:0x0334, code lost:
            r5 = r5 + 1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:162:0x0337, code lost:
            if (r2 == false) goto L185;
         */
        /* JADX WARN: Code restructure failed: missing block: B:165:0x0341, code lost:
            throw new java.lang.IllegalArgumentException("--start-child-zygote specified without --zygote-socket=");
         */
        /* JADX WARN: Code restructure failed: missing block: B:166:0x0342, code lost:
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:209:?, code lost:
            return;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void parseArgs(java.lang.String[] r11) throws java.lang.IllegalArgumentException {
            /*
                Method dump skipped, instructions count: 835
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.ZygoteConnection.Arguments.parseArgs(java.lang.String[]):void");
        }
    }

    private String[] readArgumentList() throws IOException {
        try {
            String s = this.mSocketReader.readLine();
            if (s == null) {
                return null;
            }
            int argc = Integer.parseInt(s);
            if (argc > 1024) {
                throw new IOException("max arg count exceeded");
            }
            String[] result = new String[argc];
            for (int i = 0; i < argc; i++) {
                result[i] = this.mSocketReader.readLine();
                if (result[i] == null) {
                    throw new IOException("truncated request");
                }
            }
            return result;
        } catch (NumberFormatException e) {
            Log.e(TAG, "invalid Zygote wire format: non-int at argc");
            throw new IOException("invalid wire format");
        }
    }

    private static void applyUidSecurityPolicy(Arguments args, Credentials peer) throws ZygoteSecurityException {
        if (peer.getUid() == 1000) {
            boolean uidRestricted = FactoryTest.getMode() == 0;
            if (uidRestricted && args.uidSpecified && args.uid < 1000) {
                throw new ZygoteSecurityException("System UID may not launch process with UID < 1000");
            }
        }
        boolean uidRestricted2 = args.uidSpecified;
        if (!uidRestricted2) {
            args.uid = peer.getUid();
            args.uidSpecified = true;
        }
        if (!args.gidSpecified) {
            args.gid = peer.getGid();
            args.gidSpecified = true;
        }
    }

    public static void applyDebuggerSystemProperty(Arguments args) {
        if (RoSystemProperties.DEBUGGABLE) {
            args.runtimeFlags |= 1;
        }
    }

    private static void applyInvokeWithSecurityPolicy(Arguments args, Credentials peer) throws ZygoteSecurityException {
        int peerUid = peer.getUid();
        if (args.invokeWith != null && peerUid != 0 && (args.runtimeFlags & 1) == 0) {
            throw new ZygoteSecurityException("Peer is permitted to specify anexplicit invoke-with wrapper command only for debuggableapplications.");
        }
    }

    public static void applyInvokeWithSystemProperty(Arguments args) {
        if (args.invokeWith == null && args.niceName != null) {
            String property = "wrap." + args.niceName;
            args.invokeWith = SystemProperties.get(property);
            if (args.invokeWith != null && args.invokeWith.length() == 0) {
                args.invokeWith = null;
            }
        }
    }

    private Runnable handleChildProc(Arguments parsedArgs, FileDescriptor[] descriptors, FileDescriptor pipeFd, boolean isZygote) {
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
        if (parsedArgs.niceName != null) {
            Process.setArgV0(parsedArgs.niceName);
        }
        Trace.traceEnd(64L);
        if (parsedArgs.invokeWith == null) {
            return !isZygote ? ZygoteInit.zygoteInit(parsedArgs.targetSdkVersion, parsedArgs.remainingArgs, null) : ZygoteInit.childZygoteInit(parsedArgs.targetSdkVersion, parsedArgs.remainingArgs, null);
        }
        WrapperInit.execApplication(parsedArgs.invokeWith, parsedArgs.niceName, parsedArgs.targetSdkVersion, VMRuntime.getCurrentInstructionSet(), pipeFd, parsedArgs.remainingArgs);
        throw new IllegalStateException("WrapperInit.execApplication unexpectedly returned");
    }

    private void handleParentProc(int pid, FileDescriptor[] descriptors, FileDescriptor pipeFd) {
        int pid2;
        if (pid > 0) {
            setChildPgid(pid);
        }
        short s = 0;
        if (descriptors != null) {
            for (FileDescriptor fd : descriptors) {
                IoUtils.closeQuietly(fd);
            }
        }
        boolean usingWrapper = false;
        try {
            if (pipeFd != null && pid > 0) {
                int innerPid = -1;
                try {
                    StructPollfd[] fds = {new StructPollfd()};
                    byte[] data = new byte[4];
                    int remainingSleepTime = 30000;
                    int dataIndex = 0;
                    long startTime = System.nanoTime();
                    while (dataIndex < data.length && remainingSleepTime > 0) {
                        fds[s].fd = pipeFd;
                        fds[s].events = (short) OsConstants.POLLIN;
                        fds[s].revents = s;
                        fds[s].userData = null;
                        int res = Os.poll(fds, remainingSleepTime);
                        long endTime = System.nanoTime();
                        int elapsedTimeMs = (int) ((endTime - startTime) / 1000000);
                        remainingSleepTime = 30000 - elapsedTimeMs;
                        if (res > 0) {
                            if ((fds[0].revents & OsConstants.POLLIN) == 0) {
                                break;
                            }
                            int readBytes = Os.read(pipeFd, data, dataIndex, 1);
                            if (readBytes < 0) {
                                throw new RuntimeException("Some error");
                            }
                            dataIndex += readBytes;
                        } else if (res == 0) {
                            Log.w(TAG, "Timed out waiting for child.");
                        }
                        s = 0;
                    }
                    if (dataIndex == data.length) {
                        DataInputStream is = new DataInputStream(new ByteArrayInputStream(data));
                        innerPid = is.readInt();
                    }
                    if (innerPid == -1) {
                        Log.w(TAG, "Error reading pid from wrapped process, child may have died");
                    }
                } catch (Exception ex) {
                    Log.w(TAG, "Error reading pid from wrapped process, child may have died", ex);
                }
                if (innerPid > 0) {
                    int parentPid = innerPid;
                    while (parentPid > 0 && parentPid != pid) {
                        parentPid = Process.getParentPid(parentPid);
                    }
                    if (parentPid > 0) {
                        Log.i(TAG, "Wrapped process has pid " + innerPid);
                        pid2 = innerPid;
                        usingWrapper = true;
                        this.mSocketOutStream.writeInt(pid2);
                        this.mSocketOutStream.writeBoolean(usingWrapper);
                        return;
                    }
                    Log.w(TAG, "Wrapped process reported a pid that is not a child of the process that we forked: childPid=" + pid + " innerPid=" + innerPid);
                }
            }
            this.mSocketOutStream.writeInt(pid2);
            this.mSocketOutStream.writeBoolean(usingWrapper);
            return;
        } catch (IOException ex2) {
            throw new IllegalStateException("Error writing to command socket", ex2);
        }
        pid2 = pid;
    }

    private void setChildPgid(int pid) {
        try {
            Os.setpgid(pid, Os.getpgid(this.peer.getPid()));
        } catch (ErrnoException e) {
            Log.i(TAG, "Zygote: setpgid failed. This is normal if peer is not in our session");
        }
    }
}
