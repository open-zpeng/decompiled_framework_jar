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
import android.os.IVold;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.euicc.EuiccManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.WindowManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;

/* loaded from: classes2.dex */
public class RecoverySystem {
    private static final String ACTION_EUICC_FACTORY_RESET = "com.android.internal.action.EUICC_FACTORY_RESET";
    private static final String ACTION_EUICC_REMOVE_INVISIBLE_SUBSCRIPTIONS = "com.android.internal.action.EUICC_REMOVE_INVISIBLE_SUBSCRIPTIONS";
    private static final long DEFAULT_EUICC_FACTORY_RESET_TIMEOUT_MILLIS = 30000;
    private static final long DEFAULT_EUICC_REMOVING_INVISIBLE_PROFILES_TIMEOUT_MILLIS = 45000;
    private static final String LAST_INSTALL_PATH = "last_install";
    private static final String LAST_PREFIX = "last_";
    private static final int LOG_FILE_MAX_LENGTH = 65536;
    private static final long MAX_EUICC_FACTORY_RESET_TIMEOUT_MILLIS = 60000;
    private static final long MAX_EUICC_REMOVING_INVISIBLE_PROFILES_TIMEOUT_MILLIS = 90000;
    private static final long MIN_EUICC_FACTORY_RESET_TIMEOUT_MILLIS = 5000;
    private static final long MIN_EUICC_REMOVING_INVISIBLE_PROFILES_TIMEOUT_MILLIS = 15000;
    private static final String PACKAGE_NAME_EUICC_DATA_MANAGEMENT_CALLBACK = "android";
    private static final long PUBLISH_PROGRESS_INTERVAL_MS = 500;
    private static final String TAG = "RecoverySystem";
    private final IRecoverySystem mService;
    private static final File DEFAULT_KEYSTORE = new File("/system/etc/security/otacerts.zip");
    private static final File RECOVERY_DIR = new File("/cache/recovery");
    private static final File LOG_FILE = new File(RECOVERY_DIR, "log");
    public static final File BLOCK_MAP_FILE = new File(RECOVERY_DIR, "block.map");
    public static final File UNCRYPT_PACKAGE_FILE = new File(RECOVERY_DIR, "uncrypt_file");
    public static final File UNCRYPT_STATUS_FILE = new File(RECOVERY_DIR, "uncrypt_status");
    private static final Object sRequestLock = new Object();

    /* loaded from: classes2.dex */
    public interface ProgressListener {
        void onProgress(int i);
    }

    private static HashSet<X509Certificate> getTrustedCerts(File keystore) throws IOException, GeneralSecurityException {
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

    public static void verifyPackage(File packageFile, final ProgressListener listener, File deviceCertsZipFile) throws IOException, GeneralSecurityException {
        boolean verified;
        final long fileLen = packageFile.length();
        final RandomAccessFile raf = new RandomAccessFile(packageFile, "r");
        try {
            final long startTimeMillis = System.currentTimeMillis();
            if (listener != null) {
                listener.onProgress(0);
            }
            raf.seek(fileLen - 6);
            byte[] footer = new byte[6];
            raf.readFully(footer);
            try {
                if (footer[2] == -1) {
                    if (footer[3] == -1) {
                        final int commentSize = ((footer[5] & 255) << 8) | (footer[4] & 255);
                        int signatureStart = (footer[0] & 255) | ((footer[1] & 255) << 8);
                        byte[] eocd = new byte[commentSize + 22];
                        try {
                            raf.seek(fileLen - (commentSize + 22));
                            raf.readFully(eocd);
                            if (eocd[0] != 80 || eocd[1] != 75 || eocd[2] != 5 || eocd[3] != 6) {
                                throw new SignatureException("no signature in file (bad footer)");
                            }
                            int i = 4;
                            for (int i2 = 3; i < eocd.length - i2; i2 = 3) {
                                if (eocd[i] == 80 && eocd[i + 1] == 75 && eocd[i + 2] == 5) {
                                    if (eocd[i + 3] == 6) {
                                        throw new SignatureException("EOCD marker found after start of EOCD");
                                    }
                                }
                                i++;
                            }
                            PKCS7 block = new PKCS7(new ByteArrayInputStream(eocd, (commentSize + 22) - signatureStart, signatureStart));
                            X509Certificate[] certificates = block.getCertificates();
                            if (certificates == null || certificates.length == 0) {
                                throw new SignatureException("signature contains no certificates");
                            }
                            X509Certificate cert = certificates[0];
                            PublicKey signatureKey = cert.getPublicKey();
                            SignerInfo[] signerInfos = block.getSignerInfos();
                            if (signerInfos == null || signerInfos.length == 0) {
                                throw new SignatureException("signature contains no signedData");
                            }
                            SignerInfo signerInfo = signerInfos[0];
                            HashSet<X509Certificate> trusted = getTrustedCerts(deviceCertsZipFile == null ? DEFAULT_KEYSTORE : deviceCertsZipFile);
                            Iterator<X509Certificate> it = trusted.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    verified = false;
                                    break;
                                }
                                X509Certificate c = it.next();
                                SignerInfo[] signerInfos2 = signerInfos;
                                if (c.getPublicKey().equals(signatureKey)) {
                                    verified = true;
                                    break;
                                }
                                signerInfos = signerInfos2;
                            }
                            if (!verified) {
                                throw new SignatureException("signature doesn't match any trusted key");
                            }
                            raf.seek(0L);
                            SignerInfo verifyResult = block.verify(signerInfo, new InputStream() { // from class: android.os.RecoverySystem.1
                                long lastPublishTime;
                                long toRead;
                                long soFar = 0;
                                int lastPercent = 0;

                                {
                                    this.toRead = (fileLen - commentSize) - 2;
                                    this.lastPublishTime = startTimeMillis;
                                }

                                @Override // java.io.InputStream
                                public int read() throws IOException {
                                    throw new UnsupportedOperationException();
                                }

                                @Override // java.io.InputStream
                                public int read(byte[] b, int off, int len) throws IOException {
                                    if (this.soFar < this.toRead && !Thread.currentThread().isInterrupted()) {
                                        int size = len;
                                        long j = this.soFar;
                                        long j2 = this.toRead;
                                        if (size + j > j2) {
                                            size = (int) (j2 - j);
                                        }
                                        int read = raf.read(b, off, size);
                                        this.soFar += read;
                                        if (listener != null) {
                                            long now = System.currentTimeMillis();
                                            int p = (int) ((this.soFar * 100) / this.toRead);
                                            if (p > this.lastPercent && now - this.lastPublishTime > 500) {
                                                this.lastPercent = p;
                                                this.lastPublishTime = now;
                                                listener.onProgress(this.lastPercent);
                                            }
                                        }
                                        return read;
                                    }
                                    return -1;
                                }
                            });
                            boolean interrupted = Thread.interrupted();
                            if (listener != null) {
                                listener.onProgress(100);
                            }
                            if (interrupted) {
                                throw new SignatureException("verification was interrupted");
                            }
                            if (verifyResult == null) {
                                throw new SignatureException("signature digest verification failed");
                            }
                            raf.close();
                            if (!readAndVerifyPackageCompatibilityEntry(packageFile)) {
                                throw new SignatureException("package compatibility verification failed");
                            }
                            return;
                        } catch (Throwable th) {
                            th = th;
                            raf.close();
                            throw th;
                        }
                    }
                }
                throw new SignatureException("no signature in file (no footer)");
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x004f, code lost:
        throw new java.io.IOException("invalid entry size (" + r4 + ") in the compatibility file");
     */
    @android.annotation.UnsupportedAppUsage
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean verifyPackageCompatibility(java.io.InputStream r8) throws java.io.IOException {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.util.zip.ZipInputStream r1 = new java.util.zip.ZipInputStream
            r1.<init>(r8)
        La:
            java.util.zip.ZipEntry r2 = r1.getNextEntry()
            r3 = r2
            if (r2 == 0) goto L50
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
        L50:
            boolean r2 = r0.isEmpty()
            if (r2 != 0) goto L6c
            int r2 = r0.size()
            java.lang.String[] r2 = new java.lang.String[r2]
            java.lang.Object[] r2 = r0.toArray(r2)
            java.lang.String[] r2 = (java.lang.String[]) r2
            int r2 = android.os.VintfObject.verify(r2)
            if (r2 != 0) goto L6a
            r2 = 1
            goto L6b
        L6a:
            r2 = 0
        L6b:
            return r2
        L6c:
            java.io.IOException r2 = new java.io.IOException
            java.lang.String r4 = "no entries found in the compatibility file"
            r2.<init>(r4)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.RecoverySystem.verifyPackageCompatibility(java.io.InputStream):boolean");
    }

    private static boolean readAndVerifyPackageCompatibilityEntry(File packageFile) throws IOException {
        ZipFile zip = new ZipFile(packageFile);
        try {
            ZipEntry entry = zip.getEntry("compatibility.zip");
            if (entry != null) {
                InputStream inputStream = zip.getInputStream(entry);
                boolean verifyPackageCompatibility = verifyPackageCompatibility(inputStream);
                $closeResource(null, zip);
                return verifyPackageCompatibility;
            }
            $closeResource(null, zip);
            return true;
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
                        AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                        anonymousClass2.lastProgress = progress;
                        anonymousClass2.lastPublishTime = now;
                        anonymousClass2.val$listener.onProgress(progress);
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

    public static void rebootWipeUserData(Context context, String reason) throws IOException {
        rebootWipeUserData(context, false, reason, false, false);
    }

    public static void rebootWipeUserData(Context context, boolean shutdown) throws IOException {
        rebootWipeUserData(context, shutdown, context.getPackageName(), false, false);
    }

    public static void rebootWipeUserData(Context context, boolean shutdown, String reason, boolean force) throws IOException {
        rebootWipeUserData(context, shutdown, reason, force, false);
    }

    public static void rebootWipeUserData(Context context, boolean shutdown, String reason, boolean force, boolean wipeEuicc) throws IOException {
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
        EuiccManager euiccManager = (EuiccManager) context.getSystemService(EuiccManager.class);
        if (wipeEuicc) {
            wipeEuiccData(context, "android");
        } else {
            removeEuiccInvisibleSubs(context, euiccManager);
        }
        String shutdownArg = null;
        if (shutdown) {
            shutdownArg = "--shutdown_after";
        }
        String reasonArg = null;
        if (!TextUtils.isEmpty(reason)) {
            String timeStamp = DateFormat.format("yyyy-MM-ddTHH:mm:ssZ", System.currentTimeMillis()).toString();
            StringBuilder sb = new StringBuilder();
            sb.append("--reason=");
            sb.append(sanitizeArg(reason + SmsManager.REGEX_PREFIX_DELIMITER + timeStamp));
            reasonArg = sb.toString();
        }
        String localeArg = "--locale=" + Locale.getDefault().toLanguageTag();
        bootCommand(context, shutdownArg, "--wipe_data", reasonArg, localeArg);
    }

    public static boolean wipeEuiccData(Context context, String packageName) {
        long waitingTimeMillis;
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
            waitingTimeMillis = Settings.Global.getLong(context.getContentResolver(), Settings.Global.EUICC_FACTORY_RESET_TIMEOUT_MILLIS, 30000L);
            if (waitingTimeMillis < 5000) {
                waitingTimeMillis = 5000;
            } else if (waitingTimeMillis > 60000) {
                waitingTimeMillis = 60000;
            }
        } catch (InterruptedException e2) {
            e = e2;
        } catch (Throwable th2) {
            e = th2;
            context.getApplicationContext().unregisterReceiver(euiccWipeFinishReceiver);
            throw e;
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
            } catch (Throwable th3) {
                e = th3;
                context.getApplicationContext().unregisterReceiver(euiccWipeFinishReceiver);
                throw e;
            }
        } catch (InterruptedException e3) {
            e = e3;
            Thread.currentThread().interrupt();
            Log.e(TAG, "Wiping eUICC data interrupted", e);
            context.getApplicationContext().unregisterReceiver(euiccWipeFinishReceiver);
            return false;
        }
    }

    private static void removeEuiccInvisibleSubs(Context context, EuiccManager euiccManager) {
        ContentResolver cr = context.getContentResolver();
        if (Settings.Global.getInt(cr, Settings.Global.EUICC_PROVISIONED, 0) == 0) {
            Log.i(TAG, "Skip removing eUICC invisible profiles as it is not provisioned.");
        } else if (euiccManager == null || !euiccManager.isEnabled()) {
            Log.i(TAG, "Skip removing eUICC invisible profiles as eUICC manager is not available.");
        } else {
            SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
            List<SubscriptionInfo> availableSubs = subscriptionManager.getAvailableSubscriptionInfoList();
            if (availableSubs == null || availableSubs.isEmpty()) {
                Log.i(TAG, "Skip removing eUICC invisible profiles as no available profiles found.");
                return;
            }
            List<SubscriptionInfo> invisibleSubs = new ArrayList<>();
            for (SubscriptionInfo sub : availableSubs) {
                if (sub.isEmbedded() && !subscriptionManager.lambda$getActiveSubscriptionInfoList$0$SubscriptionManager(sub)) {
                    invisibleSubs.add(sub);
                }
            }
            removeEuiccInvisibleSubs(context, invisibleSubs, euiccManager);
        }
    }

    private static boolean removeEuiccInvisibleSubs(Context context, List<SubscriptionInfo> subscriptionInfos, EuiccManager euiccManager) {
        if (subscriptionInfos != null && !subscriptionInfos.isEmpty()) {
            final CountDownLatch removeSubsLatch = new CountDownLatch(subscriptionInfos.size());
            final AtomicInteger removedSubsCount = new AtomicInteger(0);
            BroadcastReceiver removeEuiccSubsReceiver = new BroadcastReceiver() { // from class: android.os.RecoverySystem.5
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context2, Intent intent) {
                    if (RecoverySystem.ACTION_EUICC_REMOVE_INVISIBLE_SUBSCRIPTIONS.equals(intent.getAction())) {
                        if (getResultCode() != 0) {
                            int detailedCode = intent.getIntExtra(EuiccManager.EXTRA_EMBEDDED_SUBSCRIPTION_DETAILED_CODE, 0);
                            Log.e(RecoverySystem.TAG, "Error removing euicc opportunistic profile, Detailed code = " + detailedCode);
                        } else {
                            Log.e(RecoverySystem.TAG, "Successfully remove euicc opportunistic profile.");
                            removedSubsCount.incrementAndGet();
                        }
                        removeSubsLatch.countDown();
                    }
                }
            };
            Intent intent = new Intent(ACTION_EUICC_REMOVE_INVISIBLE_SUBSCRIPTIONS);
            intent.setPackage("android");
            PendingIntent callbackIntent = PendingIntent.getBroadcastAsUser(context, 0, intent, 134217728, UserHandle.SYSTEM);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_EUICC_REMOVE_INVISIBLE_SUBSCRIPTIONS);
            HandlerThread euiccHandlerThread = new HandlerThread("euiccRemovingSubsReceiverThread");
            euiccHandlerThread.start();
            Handler euiccHandler = new Handler(euiccHandlerThread.getLooper());
            context.getApplicationContext().registerReceiver(removeEuiccSubsReceiver, intentFilter, null, euiccHandler);
            for (SubscriptionInfo subscriptionInfo : subscriptionInfos) {
                Log.i(TAG, "Remove invisible subscription " + subscriptionInfo.getSubscriptionId() + " from card " + subscriptionInfo.getCardId());
                euiccManager.createForCardId(subscriptionInfo.getCardId()).deleteSubscription(subscriptionInfo.getSubscriptionId(), callbackIntent);
            }
            try {
                long waitingTimeMillis = Settings.Global.getLong(context.getContentResolver(), Settings.Global.EUICC_REMOVING_INVISIBLE_PROFILES_TIMEOUT_MILLIS, DEFAULT_EUICC_REMOVING_INVISIBLE_PROFILES_TIMEOUT_MILLIS);
                if (waitingTimeMillis < MIN_EUICC_REMOVING_INVISIBLE_PROFILES_TIMEOUT_MILLIS) {
                    waitingTimeMillis = MIN_EUICC_REMOVING_INVISIBLE_PROFILES_TIMEOUT_MILLIS;
                } else if (waitingTimeMillis > MAX_EUICC_REMOVING_INVISIBLE_PROFILES_TIMEOUT_MILLIS) {
                    waitingTimeMillis = MAX_EUICC_REMOVING_INVISIBLE_PROFILES_TIMEOUT_MILLIS;
                }
                if (!removeSubsLatch.await(waitingTimeMillis, TimeUnit.MILLISECONDS)) {
                    Log.e(TAG, "Timeout removing invisible euicc profiles.");
                    return false;
                }
                context.getApplicationContext().unregisterReceiver(removeEuiccSubsReceiver);
                euiccHandlerThread.quit();
                return removedSubsCount.get() == subscriptionInfos.size();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Log.e(TAG, "Removing invisible euicc profiles interrupted", e);
                return false;
            } finally {
                context.getApplicationContext().unregisterReceiver(removeEuiccSubsReceiver);
                euiccHandlerThread.quit();
            }
        }
        Log.i(TAG, "There are no eUICC invisible profiles needed to be removed.");
        return true;
    }

    public static void rebootPromptAndWipeUserData(Context context, String reason) throws IOException {
        boolean checkpointing = false;
        IVold vold = null;
        try {
            vold = IVold.Stub.asInterface(ServiceManager.checkService("vold"));
            if (vold != null) {
                checkpointing = vold.needsCheckpoint();
            } else {
                Log.w(TAG, "Failed to get vold");
            }
        } catch (Exception e) {
            Log.w(TAG, "Failed to check for checkpointing");
        }
        if (checkpointing) {
            try {
                vold.abortChanges("rescueparty", false);
                Log.i(TAG, "Rescue Party requested wipe. Aborting update");
                return;
            } catch (Exception e2) {
                Log.i(TAG, "Rescue Party requested wipe. Rebooting instead.");
                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                pm.reboot("rescueparty");
                return;
            }
        }
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

    public static void rebootWipeCache(Context context, String reason) throws IOException {
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

    public static String handleAftermath(Context context) {
        String log = null;
        try {
            log = FileUtils.readTextFile(LOG_FILE, -65536, "...\n");
        } catch (FileNotFoundException e) {
            Log.i(TAG, "No recovery log file");
        } catch (IOException e2) {
            Log.e(TAG, "Error reading recovery log", e2);
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
            if (!names[i].startsWith(LAST_PREFIX) && !names[i].equals(LAST_INSTALL_PATH) && ((!reservePackage || !names[i].equals(BLOCK_MAP_FILE.getName())) && (!reservePackage || !names[i].equals(UNCRYPT_PACKAGE_FILE.getName())))) {
                recursiveDelete(new File(RECOVERY_DIR, names[i]));
            }
        }
        return log;
    }

    private static void recursiveDelete(File name) {
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

    private boolean uncrypt(String packageFile, IRecoverySystemProgressListener listener) {
        try {
            return this.mService.uncrypt(packageFile, listener);
        } catch (RemoteException e) {
            return false;
        }
    }

    private boolean setupBcb(String command) {
        try {
            return this.mService.setupBcb(command);
        } catch (RemoteException e) {
            return false;
        }
    }

    private boolean clearBcb() {
        try {
            return this.mService.clearBcb();
        } catch (RemoteException e) {
            return false;
        }
    }

    private void rebootRecoveryWithCommand(String command) {
        try {
            this.mService.rebootRecoveryWithCommand(command);
        } catch (RemoteException e) {
        }
    }

    private static String sanitizeArg(String arg) {
        return arg.replace((char) 0, '?').replace('\n', '?');
    }

    public RecoverySystem() {
        this.mService = null;
    }

    public RecoverySystem(IRecoverySystem service) {
        this.mService = service;
    }
}
