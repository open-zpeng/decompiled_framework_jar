package android.os;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.IRecoverySystemProgressListener;
import android.provider.Settings;
import android.telephony.euicc.EuiccManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import com.android.internal.logging.MetricsLogger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
/* loaded from: classes2.dex */
public class RecoverySystem {
    private static final String ACTION_EUICC_FACTORY_RESET = "com.android.internal.action.EUICC_FACTORY_RESET";
    private static final long DEFAULT_EUICC_FACTORY_RESET_TIMEOUT_MILLIS = 30000;
    private static final String LAST_PREFIX = "last_";
    private static final int LOG_FILE_MAX_LENGTH = 65536;
    private static final long MAX_EUICC_FACTORY_RESET_TIMEOUT_MILLIS = 60000;
    private static final long MIN_EUICC_FACTORY_RESET_TIMEOUT_MILLIS = 5000;
    private static final String PACKAGE_NAME_WIPING_EUICC_DATA_CALLBACK = "android";
    private static final long PUBLISH_PROGRESS_INTERVAL_MS = 500;
    private static final String TAG = "RecoverySystem";
    private final IRecoverySystem mService;
    private static final File DEFAULT_KEYSTORE = new File("/system/etc/security/otacerts.zip");
    private static final File RECOVERY_DIR = new File("/cache/recovery");
    private static final File LOG_FILE = new File(RECOVERY_DIR, "log");
    private static final File LAST_INSTALL_FILE = new File(RECOVERY_DIR, "last_install");
    public static final File BLOCK_MAP_FILE = new File(RECOVERY_DIR, "block.map");
    public static final File UNCRYPT_PACKAGE_FILE = new File(RECOVERY_DIR, "uncrypt_file");
    public static final File UNCRYPT_STATUS_FILE = new File(RECOVERY_DIR, "uncrypt_status");
    private static final Object sRequestLock = new Object();

    /* loaded from: classes2.dex */
    public interface ProgressListener {
        void onProgress(int i);
    }

    private static synchronized HashSet<X509Certificate> getTrustedCerts(File keystore) throws IOException, GeneralSecurityException {
        HashSet<X509Certificate> trusted = new HashSet<>();
        if (keystore == null) {
            keystore = DEFAULT_KEYSTORE;
        }
        ZipFile zip = new ZipFile(keystore);
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                InputStream is = zip.getInputStream(entry);
                trusted.add((X509Certificate) cf.generateCertificate(is));
                is.close();
            }
            return trusted;
        } finally {
            zip.close();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x00a1, code lost:
        r1 = new sun.security.pkcs.PKCS7(new java.io.ByteArrayInputStream(r4, (r15 + 22) - r7, r7));
        r1 = r1.getCertificates();
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00b4, code lost:
        if (r1 == null) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00b7, code lost:
        if (r1.length == 0) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00b9, code lost:
        r1 = r1[0];
        r1 = r1.getPublicKey();
        r8 = r1.getSignerInfos();
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00c4, code lost:
        if (r8 == null) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00c7, code lost:
        if (r8.length == 0) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00c9, code lost:
        r0 = r8[0];
        r16 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00ce, code lost:
        if (r33 != null) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00d0, code lost:
        r24 = r1;
        r2 = android.os.RecoverySystem.DEFAULT_KEYSTORE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00d7, code lost:
        r24 = r1;
        r2 = r33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00db, code lost:
        r2 = getTrustedCerts(r2);
        r3 = r2.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00e9, code lost:
        if (r3.hasNext() == false) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00eb, code lost:
        r18 = r3.next();
        r27 = r2;
        r28 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0101, code lost:
        if (r18.getPublicKey().equals(r1) == false) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0103, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0106, code lost:
        r2 = r27;
        r3 = r28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x010c, code lost:
        r27 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x010e, code lost:
        if (r16 == false) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0110, code lost:
        r0.seek(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0130, code lost:
        r1 = r1.verify(r0, new android.os.RecoverySystem.AnonymousClass1());
        r2 = java.lang.Thread.interrupted();
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x013b, code lost:
        if (r32 == null) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x013d, code lost:
        r32.onProgress(100);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0142, code lost:
        if (r2 != false) goto L80;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0144, code lost:
        if (r1 == null) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0146, code lost:
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x014e, code lost:
        if (readAndVerifyPackageCompatibilityEntry(r31) == false) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0150, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0159, code lost:
        throw new java.security.SignatureException("package compatibility verification failed");
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0162, code lost:
        throw new java.security.SignatureException("signature digest verification failed");
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x016b, code lost:
        throw new java.security.SignatureException("verification was interrupted");
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x016c, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x018a, code lost:
        throw new java.security.SignatureException("signature doesn't match any trusted key");
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x01a3, code lost:
        throw new java.security.SignatureException("signature contains no signedData");
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x01b6, code lost:
        throw new java.security.SignatureException("signature contains no certificates");
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x01dc, code lost:
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x01df, code lost:
        throw r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void verifyPackage(java.io.File r31, final android.os.RecoverySystem.ProgressListener r32, java.io.File r33) throws java.io.IOException, java.security.GeneralSecurityException {
        /*
            Method dump skipped, instructions count: 480
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.RecoverySystem.verifyPackage(java.io.File, android.os.RecoverySystem$ProgressListener, java.io.File):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x004e, code lost:
        throw new java.io.IOException("invalid entry size (" + r4 + ") in the compatibility file");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public protected static boolean verifyPackageCompatibility(java.io.InputStream r8) throws java.io.IOException {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.util.zip.ZipInputStream r1 = new java.util.zip.ZipInputStream
            r1.<init>(r8)
        La:
            java.util.zip.ZipEntry r2 = r1.getNextEntry()
            r3 = r2
            if (r2 == 0) goto L4f
            long r4 = r3.getSize()
            r6 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r2 > 0) goto L33
            r6 = 0
            int r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r2 < 0) goto L33
            int r2 = (int) r4
            byte[] r2 = new byte[r2]
            libcore.io.Streams.readFully(r1, r2)
            java.lang.String r6 = new java.lang.String
            java.nio.charset.Charset r7 = java.nio.charset.StandardCharsets.UTF_8
            r6.<init>(r2, r7)
            r0.add(r6)
            goto La
        L33:
            java.io.IOException r2 = new java.io.IOException
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "invalid entry size ("
            r6.append(r7)
            r6.append(r4)
            java.lang.String r7 = ") in the compatibility file"
            r6.append(r7)
            java.lang.String r6 = r6.toString()
            r2.<init>(r6)
            throw r2
        L4f:
            boolean r2 = r0.isEmpty()
            if (r2 != 0) goto L6b
            int r2 = r0.size()
            java.lang.String[] r2 = new java.lang.String[r2]
            java.lang.Object[] r2 = r0.toArray(r2)
            java.lang.String[] r2 = (java.lang.String[]) r2
            int r2 = android.os.VintfObject.verify(r2)
            if (r2 != 0) goto L69
            r2 = 1
            goto L6a
        L69:
            r2 = 0
        L6a:
            return r2
        L6b:
            java.io.IOException r2 = new java.io.IOException
            java.lang.String r4 = "no entries found in the compatibility file"
            r2.<init>(r4)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.RecoverySystem.verifyPackageCompatibility(java.io.InputStream):boolean");
    }

    private static synchronized boolean readAndVerifyPackageCompatibilityEntry(File packageFile) throws IOException {
        ZipFile zip = new ZipFile(packageFile);
        try {
            ZipEntry entry = zip.getEntry("compatibility.zip");
            if (entry == null) {
                $closeResource(null, zip);
                return true;
            }
            InputStream inputStream = zip.getInputStream(entry);
            boolean verifyPackageCompatibility = verifyPackageCompatibility(inputStream);
            $closeResource(null, zip);
            return verifyPackageCompatibility;
        } finally {
        }
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 == null) {
            x1.close();
            return;
        }
        try {
            x1.close();
        } catch (Throwable th) {
            x0.addSuppressed(th);
        }
    }

    @SystemApi
    @SuppressLint({"Doclava125"})
    public static boolean verifyPackageCompatibility(File compatibilityFile) throws IOException {
        InputStream inputStream = new FileInputStream(compatibilityFile);
        try {
            boolean verifyPackageCompatibility = verifyPackageCompatibility(inputStream);
            $closeResource(null, inputStream);
            return verifyPackageCompatibility;
        } finally {
        }
    }

    @SystemApi
    public static void processPackage(Context context, File packageFile, ProgressListener listener, Handler handler) throws IOException {
        Handler progressHandler;
        String filename = packageFile.getCanonicalPath();
        if (!filename.startsWith("/data/")) {
            return;
        }
        RecoverySystem rs = (RecoverySystem) context.getSystemService("recovery");
        IRecoverySystemProgressListener progressListener = null;
        if (listener != null) {
            if (handler != null) {
                progressHandler = handler;
            } else {
                progressHandler = new Handler(context.getMainLooper());
            }
            progressListener = new AnonymousClass2(progressHandler, listener);
        }
        if (!rs.uncrypt(filename, progressListener)) {
            throw new IOException("process package failed");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.os.RecoverySystem$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends IRecoverySystemProgressListener.Stub {
        int lastProgress = 0;
        long lastPublishTime = System.currentTimeMillis();
        final /* synthetic */ ProgressListener val$listener;
        final /* synthetic */ Handler val$progressHandler;

        AnonymousClass2(Handler handler, ProgressListener progressListener) {
            this.val$progressHandler = handler;
            this.val$listener = progressListener;
        }

        @Override // android.os.IRecoverySystemProgressListener
        public void onProgress(final int progress) {
            final long now = System.currentTimeMillis();
            this.val$progressHandler.post(new Runnable() { // from class: android.os.RecoverySystem.2.1
                @Override // java.lang.Runnable
                public void run() {
                    if (progress > AnonymousClass2.this.lastProgress && now - AnonymousClass2.this.lastPublishTime > 500) {
                        AnonymousClass2.this.lastProgress = progress;
                        AnonymousClass2.this.lastPublishTime = now;
                        AnonymousClass2.this.val$listener.onProgress(progress);
                    }
                }
            });
        }
    }

    @SystemApi
    public static void processPackage(Context context, File packageFile, ProgressListener listener) throws IOException {
        processPackage(context, packageFile, listener, null);
    }

    public static void installPackage(Context context, File packageFile) throws IOException {
        installPackage(context, packageFile, false);
    }

    @SystemApi
    public static void installPackage(Context context, File packageFile, boolean processed) throws IOException {
        synchronized (sRequestLock) {
            LOG_FILE.delete();
            UNCRYPT_PACKAGE_FILE.delete();
            String filename = packageFile.getCanonicalPath();
            Log.w(TAG, "!!! REBOOTING TO INSTALL " + filename + " !!!");
            boolean securityUpdate = filename.endsWith("_s.zip");
            if (filename.startsWith("/data/")) {
                if (processed) {
                    if (!BLOCK_MAP_FILE.exists()) {
                        Log.e(TAG, "Package claimed to have been processed but failed to find the block map file.");
                        throw new IOException("Failed to find block map file");
                    }
                } else {
                    FileWriter uncryptFile = new FileWriter(UNCRYPT_PACKAGE_FILE);
                    uncryptFile.write(filename + "\n");
                    uncryptFile.close();
                    if (!UNCRYPT_PACKAGE_FILE.setReadable(true, false) || !UNCRYPT_PACKAGE_FILE.setWritable(true, false)) {
                        Log.e(TAG, "Error setting permission for " + UNCRYPT_PACKAGE_FILE);
                    }
                    BLOCK_MAP_FILE.delete();
                }
                filename = "@/cache/recovery/block.map";
            }
            String filenameArg = "--update_package=" + filename + "\n";
            String localeArg = "--locale=" + Locale.getDefault().toLanguageTag() + "\n";
            String command = filenameArg + localeArg;
            if (securityUpdate) {
                command = command + "--security\n";
            }
            RecoverySystem rs = (RecoverySystem) context.getSystemService("recovery");
            if (!rs.setupBcb(command)) {
                throw new IOException("Setup BCB failed");
            }
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            String reason = PowerManager.REBOOT_RECOVERY_UPDATE;
            if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LEANBACK)) {
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                if (wm.getDefaultDisplay().getState() != 2) {
                    reason = PowerManager.REBOOT_RECOVERY_UPDATE + ",quiescent";
                }
            }
            pm.reboot(reason);
            throw new IOException("Reboot failed (no permissions?)");
        }
    }

    @SystemApi
    public static void scheduleUpdateOnBoot(Context context, File packageFile) throws IOException {
        String filename = packageFile.getCanonicalPath();
        boolean securityUpdate = filename.endsWith("_s.zip");
        if (filename.startsWith("/data/")) {
            filename = "@/cache/recovery/block.map";
        }
        String filenameArg = "--update_package=" + filename + "\n";
        String localeArg = "--locale=" + Locale.getDefault().toLanguageTag() + "\n";
        String command = filenameArg + localeArg;
        if (securityUpdate) {
            command = command + "--security\n";
        }
        RecoverySystem rs = (RecoverySystem) context.getSystemService("recovery");
        if (!rs.setupBcb(command)) {
            throw new IOException("schedule update on boot failed");
        }
    }

    @SystemApi
    public static void cancelScheduledUpdate(Context context) throws IOException {
        RecoverySystem rs = (RecoverySystem) context.getSystemService("recovery");
        if (!rs.clearBcb()) {
            throw new IOException("cancel scheduled update failed");
        }
    }

    public static void rebootWipeUserData(Context context) throws IOException {
        rebootWipeUserData(context, false, context.getPackageName(), false, false);
    }

    public static synchronized void rebootWipeUserData(Context context, String reason) throws IOException {
        rebootWipeUserData(context, false, reason, false, false);
    }

    public static synchronized void rebootWipeUserData(Context context, boolean shutdown) throws IOException {
        rebootWipeUserData(context, shutdown, context.getPackageName(), false, false);
    }

    public static synchronized void rebootWipeUserData(Context context, boolean shutdown, String reason, boolean force) throws IOException {
        rebootWipeUserData(context, shutdown, reason, force, false);
    }

    public static synchronized void rebootWipeUserData(Context context, boolean shutdown, String reason, boolean force, boolean wipeEuicc) throws IOException {
        UserManager um = (UserManager) context.getSystemService("user");
        if (!force && um.hasUserRestriction(UserManager.DISALLOW_FACTORY_RESET)) {
            throw new SecurityException("Wiping data is not allowed for this user.");
        }
        final ConditionVariable condition = new ConditionVariable();
        Intent intent = new Intent(Intent.ACTION_MASTER_CLEAR_NOTIFICATION);
        intent.addFlags(285212672);
        context.sendOrderedBroadcastAsUser(intent, UserHandle.SYSTEM, Manifest.permission.MASTER_CLEAR, new BroadcastReceiver() { // from class: android.os.RecoverySystem.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent2) {
                ConditionVariable.this.open();
            }
        }, null, 0, null, null);
        condition.block();
        if (wipeEuicc) {
            wipeEuiccData(context, "android");
        }
        String shutdownArg = null;
        if (shutdown) {
            shutdownArg = "--shutdown_after";
        }
        String reasonArg = null;
        if (!TextUtils.isEmpty(reason)) {
            reasonArg = "--reason=" + sanitizeArg(reason);
        }
        String localeArg = "--locale=" + Locale.getDefault().toLanguageTag();
        bootCommand(context, shutdownArg, "--wipe_data", reasonArg, localeArg);
    }

    public static synchronized boolean wipeEuiccData(Context context, String packageName) {
        ContentResolver cr = context.getContentResolver();
        if (Settings.Global.getInt(cr, Settings.Global.EUICC_PROVISIONED, 0) == 0) {
            Log.d(TAG, "Skipping eUICC wipe/retain as it is not provisioned");
            return true;
        }
        EuiccManager euiccManager = (EuiccManager) context.getSystemService(Context.EUICC_SERVICE);
        if (euiccManager == null || !euiccManager.isEnabled()) {
            return false;
        }
        final CountDownLatch euiccFactoryResetLatch = new CountDownLatch(1);
        final AtomicBoolean wipingSucceeded = new AtomicBoolean(false);
        BroadcastReceiver euiccWipeFinishReceiver = new BroadcastReceiver() { // from class: android.os.RecoverySystem.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (RecoverySystem.ACTION_EUICC_FACTORY_RESET.equals(intent.getAction())) {
                    if (getResultCode() != 0) {
                        int detailedCode = intent.getIntExtra(EuiccManager.EXTRA_EMBEDDED_SUBSCRIPTION_DETAILED_CODE, 0);
                        Log.e(RecoverySystem.TAG, "Error wiping euicc data, Detailed code = " + detailedCode);
                    } else {
                        Log.d(RecoverySystem.TAG, "Successfully wiped euicc data.");
                        wipingSucceeded.set(true);
                    }
                    euiccFactoryResetLatch.countDown();
                }
            }
        };
        Intent intent = new Intent(ACTION_EUICC_FACTORY_RESET);
        intent.setPackage(packageName);
        PendingIntent callbackIntent = PendingIntent.getBroadcastAsUser(context, 0, intent, 134217728, UserHandle.SYSTEM);
        IntentFilter filterConsent = new IntentFilter();
        filterConsent.addAction(ACTION_EUICC_FACTORY_RESET);
        HandlerThread euiccHandlerThread = new HandlerThread("euiccWipeFinishReceiverThread");
        euiccHandlerThread.start();
        Handler euiccHandler = new Handler(euiccHandlerThread.getLooper());
        context.getApplicationContext().registerReceiver(euiccWipeFinishReceiver, filterConsent, null, euiccHandler);
        euiccManager.eraseSubscriptions(callbackIntent);
        try {
        } catch (InterruptedException e) {
            e = e;
        } catch (Throwable th) {
            e = th;
        }
        try {
            long waitingTimeMillis = Settings.Global.getLong(context.getContentResolver(), Settings.Global.EUICC_FACTORY_RESET_TIMEOUT_MILLIS, 30000L);
            if (waitingTimeMillis < 5000) {
                waitingTimeMillis = 5000;
            } else if (waitingTimeMillis > 60000) {
                waitingTimeMillis = 60000;
            }
            try {
                try {
                    if (euiccFactoryResetLatch.await(waitingTimeMillis, TimeUnit.MILLISECONDS)) {
                        context.getApplicationContext().unregisterReceiver(euiccWipeFinishReceiver);
                        return wipingSucceeded.get();
                    }
                    Log.e(TAG, "Timeout wiping eUICC data.");
                    context.getApplicationContext().unregisterReceiver(euiccWipeFinishReceiver);
                    return false;
                } catch (InterruptedException e2) {
                    e = e2;
                    Thread.currentThread().interrupt();
                    Log.e(TAG, "Wiping eUICC data interrupted", e);
                    context.getApplicationContext().unregisterReceiver(euiccWipeFinishReceiver);
                    return false;
                }
            } catch (Throwable th2) {
                e = th2;
                context.getApplicationContext().unregisterReceiver(euiccWipeFinishReceiver);
                throw e;
            }
        } catch (InterruptedException e3) {
            e = e3;
        } catch (Throwable th3) {
            e = th3;
            context.getApplicationContext().unregisterReceiver(euiccWipeFinishReceiver);
            throw e;
        }
    }

    public static synchronized void rebootPromptAndWipeUserData(Context context, String reason) throws IOException {
        String reasonArg = null;
        if (!TextUtils.isEmpty(reason)) {
            reasonArg = "--reason=" + sanitizeArg(reason);
        }
        String localeArg = "--locale=" + Locale.getDefault().toString();
        bootCommand(context, null, "--prompt_and_wipe_data", reasonArg, localeArg);
    }

    public static void rebootWipeCache(Context context) throws IOException {
        rebootWipeCache(context, context.getPackageName());
    }

    public static synchronized void rebootWipeCache(Context context, String reason) throws IOException {
        String reasonArg = null;
        if (!TextUtils.isEmpty(reason)) {
            reasonArg = "--reason=" + sanitizeArg(reason);
        }
        String localeArg = "--locale=" + Locale.getDefault().toLanguageTag();
        bootCommand(context, "--wipe_cache", reasonArg, localeArg);
    }

    @SystemApi
    public static void rebootWipeAb(Context context, File packageFile, String reason) throws IOException {
        String reasonArg = null;
        if (!TextUtils.isEmpty(reason)) {
            reasonArg = "--reason=" + sanitizeArg(reason);
        }
        String filename = packageFile.getCanonicalPath();
        String filenameArg = "--wipe_package=" + filename;
        String localeArg = "--locale=" + Locale.getDefault().toLanguageTag();
        bootCommand(context, "--wipe_ab", filenameArg, reasonArg, localeArg);
    }

    private static void bootCommand(Context context, String... args) throws IOException {
        LOG_FILE.delete();
        StringBuilder command = new StringBuilder();
        for (String arg : args) {
            if (!TextUtils.isEmpty(arg)) {
                command.append(arg);
                command.append("\n");
            }
        }
        RecoverySystem rs = (RecoverySystem) context.getSystemService("recovery");
        rs.rebootRecoveryWithCommand(command.toString());
        throw new IOException("Reboot failed (no permissions?)");
    }

    private static synchronized void parseLastInstallLog(Context context) {
        int causeCode;
        Throwable th;
        BufferedReader in;
        int errorCode;
        try {
            BufferedReader in2 = new BufferedReader(new FileReader(LAST_INSTALL_FILE));
            int timeTotal = -1;
            int uncryptTime = -1;
            int i = -1;
            int errorCode2 = -1;
            int temperatureMax = -1;
            int temperatureEnd = -1;
            int temperatureStart = -1;
            int sourceVersion = -1;
            int bytesStashedInMiB = -1;
            int bytesWrittenInMiB = -1;
            int scaled = -1;
            while (true) {
                causeCode = scaled;
                try {
                    String line = in2.readLine();
                    if (line == null) {
                        break;
                    }
                    try {
                        int numIndex = line.indexOf(58);
                        if (numIndex == i) {
                            in = in2;
                            errorCode = errorCode2;
                        } else if (numIndex + 1 >= line.length()) {
                            in = in2;
                            errorCode = errorCode2;
                        } else {
                            String numString = line.substring(numIndex + 1).trim();
                            try {
                                try {
                                    long parsedNum = Long.parseLong(numString);
                                    try {
                                        if (line.startsWith("bytes")) {
                                            in = in2;
                                            errorCode = errorCode2;
                                            try {
                                                try {
                                                    scaled = Math.toIntExact(parsedNum / 1048576);
                                                } catch (Throwable th2) {
                                                    th = th2;
                                                    in2 = in;
                                                }
                                            } catch (ArithmeticException e) {
                                                ignored = e;
                                                Log.e(TAG, "Number overflows in " + line);
                                                scaled = causeCode;
                                                in2 = in;
                                                errorCode2 = errorCode;
                                                i = -1;
                                            } catch (Throwable th3) {
                                                th = th3;
                                                in2 = in;
                                                th = null;
                                                $closeResource(th, in2);
                                                throw th;
                                            }
                                        } else {
                                            in = in2;
                                            errorCode = errorCode2;
                                            scaled = Math.toIntExact(parsedNum);
                                        }
                                    } catch (ArithmeticException e2) {
                                        ignored = e2;
                                        in = in2;
                                        errorCode = errorCode2;
                                    }
                                } catch (NumberFormatException e3) {
                                    in = in2;
                                    errorCode = errorCode2;
                                    Log.e(TAG, "Failed to parse numbers in " + line);
                                }
                                if (line.startsWith(DropBoxManager.EXTRA_TIME)) {
                                    timeTotal = scaled;
                                } else if (line.startsWith("uncrypt_time")) {
                                    uncryptTime = scaled;
                                } else if (line.startsWith("source_build")) {
                                    sourceVersion = scaled;
                                } else if (line.startsWith("bytes_written")) {
                                    bytesWrittenInMiB = bytesWrittenInMiB == -1 ? scaled : bytesWrittenInMiB + scaled;
                                } else if (line.startsWith("bytes_stashed")) {
                                    bytesStashedInMiB = bytesStashedInMiB == -1 ? scaled : bytesStashedInMiB + scaled;
                                } else if (line.startsWith("temperature_start")) {
                                    temperatureStart = scaled;
                                } else if (line.startsWith("temperature_end")) {
                                    temperatureEnd = scaled;
                                } else if (line.startsWith("temperature_max")) {
                                    temperatureMax = scaled;
                                } else if (line.startsWith("error")) {
                                    int errorCode3 = scaled;
                                    errorCode2 = errorCode3;
                                    scaled = causeCode;
                                    in2 = in;
                                    i = -1;
                                } else if (line.startsWith("cause")) {
                                    errorCode2 = errorCode;
                                    in2 = in;
                                    i = -1;
                                }
                                scaled = causeCode;
                                errorCode2 = errorCode;
                                in2 = in;
                                i = -1;
                            } catch (Throwable th4) {
                                th = th4;
                                th = null;
                            }
                        }
                        scaled = causeCode;
                        in2 = in;
                        errorCode2 = errorCode;
                        i = -1;
                    } catch (Throwable th5) {
                        th = th5;
                    }
                    th = th2;
                    in2 = in;
                } catch (Throwable th6) {
                    th = th6;
                }
                try {
                    throw th;
                } catch (Throwable th7) {
                    th = th7;
                    $closeResource(th, in2);
                    throw th;
                }
            }
            in = in2;
            int errorCode4 = errorCode2;
            if (timeTotal != -1) {
                MetricsLogger.histogram(context, "ota_time_total", timeTotal);
            }
            if (uncryptTime != -1) {
                MetricsLogger.histogram(context, "ota_uncrypt_time", uncryptTime);
            }
            if (sourceVersion != -1) {
                MetricsLogger.histogram(context, "ota_source_version", sourceVersion);
            }
            if (bytesWrittenInMiB != -1) {
                MetricsLogger.histogram(context, "ota_written_in_MiBs", bytesWrittenInMiB);
            }
            if (bytesStashedInMiB != -1) {
                MetricsLogger.histogram(context, "ota_stashed_in_MiBs", bytesStashedInMiB);
            }
            if (temperatureStart != -1) {
                MetricsLogger.histogram(context, "ota_temperature_start", temperatureStart);
            }
            if (temperatureEnd != -1) {
                MetricsLogger.histogram(context, "ota_temperature_end", temperatureEnd);
            }
            if (temperatureMax != -1) {
                MetricsLogger.histogram(context, "ota_temperature_max", temperatureMax);
            }
            if (errorCode4 != -1) {
                MetricsLogger.histogram(context, "ota_non_ab_error_code", errorCode4);
            }
            if (causeCode != -1) {
                MetricsLogger.histogram(context, "ota_non_ab_cause_code", causeCode);
            }
            $closeResource(null, in);
        } catch (IOException e4) {
            Log.e(TAG, "Failed to read lines in last_install", e4);
        }
    }

    public static synchronized String handleAftermath(Context context) {
        String log = null;
        try {
            log = FileUtils.readTextFile(LOG_FILE, -65536, "...\n");
        } catch (FileNotFoundException e) {
            Log.i(TAG, "No recovery log file");
        } catch (IOException e2) {
            Log.e(TAG, "Error reading recovery log", e2);
        }
        if (log != null) {
            parseLastInstallLog(context);
        }
        boolean reservePackage = BLOCK_MAP_FILE.exists();
        if (!reservePackage && UNCRYPT_PACKAGE_FILE.exists()) {
            String filename = null;
            try {
                filename = FileUtils.readTextFile(UNCRYPT_PACKAGE_FILE, 0, null);
            } catch (IOException e3) {
                Log.e(TAG, "Error reading uncrypt file", e3);
            }
            if (filename != null && filename.startsWith("/data")) {
                if (UNCRYPT_PACKAGE_FILE.delete()) {
                    Log.i(TAG, "Deleted: " + filename);
                } else {
                    Log.e(TAG, "Can't delete: " + filename);
                }
            }
        }
        String[] names = RECOVERY_DIR.list();
        for (int i = 0; names != null && i < names.length; i++) {
            if (!names[i].startsWith(LAST_PREFIX) && ((!reservePackage || !names[i].equals(BLOCK_MAP_FILE.getName())) && (!reservePackage || !names[i].equals(UNCRYPT_PACKAGE_FILE.getName())))) {
                recursiveDelete(new File(RECOVERY_DIR, names[i]));
            }
        }
        return log;
    }

    private static synchronized void recursiveDelete(File name) {
        if (name.isDirectory()) {
            String[] files = name.list();
            for (int i = 0; files != null && i < files.length; i++) {
                File f = new File(name, files[i]);
                recursiveDelete(f);
            }
        }
        if (!name.delete()) {
            Log.e(TAG, "Can't delete: " + name);
            return;
        }
        Log.i(TAG, "Deleted: " + name);
    }

    private synchronized boolean uncrypt(String packageFile, IRecoverySystemProgressListener listener) {
        try {
            return this.mService.uncrypt(packageFile, listener);
        } catch (RemoteException e) {
            return false;
        }
    }

    private synchronized boolean setupBcb(String command) {
        try {
            return this.mService.setupBcb(command);
        } catch (RemoteException e) {
            return false;
        }
    }

    private synchronized boolean clearBcb() {
        try {
            return this.mService.clearBcb();
        } catch (RemoteException e) {
            return false;
        }
    }

    private synchronized void rebootRecoveryWithCommand(String command) {
        try {
            this.mService.rebootRecoveryWithCommand(command);
        } catch (RemoteException e) {
        }
    }

    private static synchronized String sanitizeArg(String arg) {
        return arg.replace((char) 0, '?').replace('\n', '?');
    }

    private protected RecoverySystem() {
        this.mService = null;
    }

    public synchronized RecoverySystem(IRecoverySystem service) {
        this.mService = service;
    }
}
