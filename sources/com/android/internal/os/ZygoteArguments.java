package com.android.internal.os;

import java.util.ArrayList;

/* loaded from: classes3.dex */
class ZygoteArguments {
    boolean mAbiListQuery;
    String[] mApiBlacklistExemptions;
    String mAppDataDir;
    boolean mCapabilitiesSpecified;
    long mEffectiveCapabilities;
    boolean mGidSpecified;
    int[] mGids;
    String mInstructionSet;
    String mInvokeWith;
    String mNiceName;
    String mPackageName;
    long mPermittedCapabilities;
    boolean mPidQuery;
    String mPreloadApp;
    boolean mPreloadDefault;
    String mPreloadPackage;
    String mPreloadPackageCacheKey;
    String mPreloadPackageLibFileName;
    String mPreloadPackageLibs;
    ArrayList<int[]> mRLimits;
    String[] mRemainingArgs;
    int mRuntimeFlags;
    String mSeInfo;
    boolean mSeInfoSpecified;
    boolean mStartChildZygote;
    int mTargetSdkVersion;
    boolean mTargetSdkVersionSpecified;
    boolean mUidSpecified;
    boolean mUsapPoolEnabled;
    int mUid = 0;
    int mGid = 0;
    int mMountExternal = 0;
    boolean mUsapPoolStatusSpecified = false;
    int mHiddenApiAccessLogSampleRate = -1;
    int mHiddenApiAccessStatslogSampleRate = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ZygoteArguments(String[] args) throws IllegalArgumentException {
        parseArgs(args);
    }

    /* JADX WARN: Code restructure failed: missing block: B:169:0x0372, code lost:
        if (r12.mAbiListQuery != false) goto L249;
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x0376, code lost:
        if (r12.mPidQuery == false) goto L217;
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x037b, code lost:
        if (r12.mPreloadPackage == null) goto L238;
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x037f, code lost:
        if ((r13.length - r0) > 0) goto L221;
     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x0389, code lost:
        throw new java.lang.IllegalArgumentException("Unexpected arguments after --preload-package.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x038c, code lost:
        if (r12.mPreloadApp == null) goto L244;
     */
    /* JADX WARN: Code restructure failed: missing block: B:183:0x0390, code lost:
        if ((r13.length - r0) > 0) goto L242;
     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x039a, code lost:
        throw new java.lang.IllegalArgumentException("Unexpected arguments after --preload-app.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x039b, code lost:
        if (r2 == false) goto L223;
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x039d, code lost:
        if (r1 == false) goto L247;
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x039f, code lost:
        r12.mRemainingArgs = new java.lang.String[r13.length - r0];
        r3 = r12.mRemainingArgs;
        java.lang.System.arraycopy(r13, r0, r3, 0, r3.length);
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x03c4, code lost:
        throw new java.lang.IllegalArgumentException("Unexpected argument : " + r13[r0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x03c7, code lost:
        if ((r13.length - r0) > 0) goto L251;
     */
    /* JADX WARN: Code restructure failed: missing block: B:195:0x03cb, code lost:
        if (r12.mStartChildZygote == false) goto L237;
     */
    /* JADX WARN: Code restructure failed: missing block: B:196:0x03cd, code lost:
        r3 = false;
        r5 = r12.mRemainingArgs;
        r6 = r5.length;
     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x03d1, code lost:
        if (r4 >= r6) goto L236;
     */
    /* JADX WARN: Code restructure failed: missing block: B:199:0x03db, code lost:
        if (r5[r4].startsWith(com.android.internal.os.Zygote.CHILD_ZYGOTE_SOCKET_NAME_ARG) == false) goto L229;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x03dd, code lost:
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x03df, code lost:
        r4 = r4 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x03e2, code lost:
        if (r3 == false) goto L233;
     */
    /* JADX WARN: Code restructure failed: missing block: B:205:0x03ec, code lost:
        throw new java.lang.IllegalArgumentException("--start-child-zygote specified without --zygote-socket=");
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x03ed, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:208:0x03f5, code lost:
        throw new java.lang.IllegalArgumentException("Unexpected arguments after --query-abi-list.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:262:?, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void parseArgs(java.lang.String[] r13) throws java.lang.IllegalArgumentException {
        /*
            Method dump skipped, instructions count: 1014
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.ZygoteArguments.parseArgs(java.lang.String[]):void");
    }
}
