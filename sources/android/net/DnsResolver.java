package android.net;

import android.net.DnsPacket;
import android.net.DnsResolver;
import android.net.util.DnsUtils;
import android.os.CancellationSignal;
import android.os.Looper;
import android.os.MessageQueue;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.util.Log;
import java.io.FileDescriptor;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public final class DnsResolver {
    public static final int CLASS_IN = 1;
    public static final int ERROR_PARSE = 0;
    public static final int ERROR_SYSTEM = 1;
    private static final int FD_EVENTS = 5;
    public static final int FLAG_EMPTY = 0;
    public static final int FLAG_NO_CACHE_LOOKUP = 4;
    public static final int FLAG_NO_CACHE_STORE = 2;
    public static final int FLAG_NO_RETRY = 1;
    private static final int MAXPACKET = 8192;
    private static final int NETID_UNSET = 0;
    private static final int SLEEP_TIME_MS = 2;
    private static final String TAG = "DnsResolver";
    public static final int TYPE_A = 1;
    public static final int TYPE_AAAA = 28;
    private static final DnsResolver sInstance = new DnsResolver();

    /* loaded from: classes2.dex */
    public interface Callback<T> {
        void onAnswer(T t, int i);

        void onError(DnsException dnsException);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    @interface DnsError {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    @interface QueryClass {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    @interface QueryFlag {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    @interface QueryType {
    }

    public static DnsResolver getInstance() {
        return sInstance;
    }

    private DnsResolver() {
    }

    /* loaded from: classes2.dex */
    public static class DnsException extends Exception {
        public final int code;

        DnsException(int code, Throwable cause) {
            super(cause);
            this.code = code;
        }
    }

    public void rawQuery(Network network, byte[] query, int flags, Executor executor, CancellationSignal cancellationSignal, final Callback<? super byte[]> callback) {
        int netIdForResolv;
        if (cancellationSignal != null && cancellationSignal.isCanceled()) {
            return;
        }
        Object lock = new Object();
        if (network != null) {
            try {
                netIdForResolv = network.getNetIdForResolv();
            } catch (ErrnoException e) {
                executor.execute(new Runnable() { // from class: android.net.-$$Lambda$DnsResolver$h2SsAzA5_rVr-mzxppK8PJLQe98
                    @Override // java.lang.Runnable
                    public final void run() {
                        DnsResolver.Callback.this.onError(new DnsResolver.DnsException(1, e));
                    }
                });
                return;
            }
        } else {
            netIdForResolv = 0;
        }
        FileDescriptor queryfd = NetworkUtils.resNetworkSend(netIdForResolv, query, query.length, flags);
        synchronized (lock) {
            registerFDListener(executor, queryfd, callback, cancellationSignal, lock);
            if (cancellationSignal == null) {
                return;
            }
            addCancellationSignal(cancellationSignal, queryfd, lock);
        }
    }

    public void rawQuery(Network network, String domain, int nsClass, int nsType, int flags, Executor executor, CancellationSignal cancellationSignal, final Callback<? super byte[]> callback) {
        int netIdForResolv;
        if (cancellationSignal != null && cancellationSignal.isCanceled()) {
            return;
        }
        Object lock = new Object();
        if (network != null) {
            try {
                netIdForResolv = network.getNetIdForResolv();
            } catch (ErrnoException e) {
                e = e;
                executor.execute(new Runnable() { // from class: android.net.-$$Lambda$DnsResolver$GTAgQiExADAzbCx0WiV_97W72-g
                    @Override // java.lang.Runnable
                    public final void run() {
                        DnsResolver.Callback.this.onError(new DnsResolver.DnsException(1, e));
                    }
                });
            }
        } else {
            netIdForResolv = 0;
        }
        try {
            FileDescriptor queryfd = NetworkUtils.resNetworkQuery(netIdForResolv, domain, nsClass, nsType, flags);
            synchronized (lock) {
                try {
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    registerFDListener(executor, queryfd, callback, cancellationSignal, lock);
                    if (cancellationSignal == null) {
                        return;
                    }
                    addCancellationSignal(cancellationSignal, queryfd, lock);
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        } catch (ErrnoException e2) {
            e = e2;
            executor.execute(new Runnable() { // from class: android.net.-$$Lambda$DnsResolver$GTAgQiExADAzbCx0WiV_97W72-g
                @Override // java.lang.Runnable
                public final void run() {
                    DnsResolver.Callback.this.onError(new DnsResolver.DnsException(1, e));
                }
            });
        }
    }

    /* loaded from: classes2.dex */
    private class InetAddressAnswerAccumulator implements Callback<byte[]> {
        private DnsException mDnsException;
        private final Network mNetwork;
        private int mRcode;
        private final int mTargetAnswerCount;
        private final Callback<? super List<InetAddress>> mUserCallback;
        private int mReceivedAnswerCount = 0;
        private final List<InetAddress> mAllAnswers = new ArrayList();

        InetAddressAnswerAccumulator(Network network, int size, Callback<? super List<InetAddress>> callback) {
            this.mNetwork = network;
            this.mTargetAnswerCount = size;
            this.mUserCallback = callback;
        }

        private boolean maybeReportError() {
            int i = this.mRcode;
            if (i != 0) {
                this.mUserCallback.onAnswer(this.mAllAnswers, i);
                return true;
            }
            DnsException dnsException = this.mDnsException;
            if (dnsException != null) {
                this.mUserCallback.onError(dnsException);
                return true;
            }
            return false;
        }

        private void maybeReportAnswer() {
            int i = this.mReceivedAnswerCount + 1;
            this.mReceivedAnswerCount = i;
            if (i != this.mTargetAnswerCount) {
                return;
            }
            if (this.mAllAnswers.isEmpty() && maybeReportError()) {
                return;
            }
            this.mUserCallback.onAnswer(DnsUtils.rfc6724Sort(this.mNetwork, this.mAllAnswers), this.mRcode);
        }

        @Override // android.net.DnsResolver.Callback
        public void onAnswer(byte[] answer, int rcode) {
            if (this.mReceivedAnswerCount == 0 || rcode == 0) {
                this.mRcode = rcode;
            }
            try {
                this.mAllAnswers.addAll(new DnsAddressAnswer(answer).getAddresses());
            } catch (ParseException e) {
                this.mDnsException = new DnsException(0, e);
            }
            maybeReportAnswer();
        }

        @Override // android.net.DnsResolver.Callback
        public void onError(DnsException error) {
            this.mDnsException = error;
            maybeReportAnswer();
        }
    }

    public void query(Network network, String domain, int flags, Executor executor, CancellationSignal cancellationSignal, final Callback<? super List<InetAddress>> callback) {
        Network dnsNetwork;
        FileDescriptor v6fd;
        FileDescriptor v4fd;
        int queryCount;
        if (cancellationSignal != null && cancellationSignal.isCanceled()) {
            return;
        }
        final Object lock = new Object();
        if (network != null) {
            dnsNetwork = network;
        } else {
            try {
                dnsNetwork = NetworkUtils.getDnsNetwork();
            } catch (ErrnoException e) {
                executor.execute(new Runnable() { // from class: android.net.-$$Lambda$DnsResolver$vvKhya16sREGcN1Gxnqgw-LBoV4
                    @Override // java.lang.Runnable
                    public final void run() {
                        DnsResolver.Callback.this.onError(new DnsResolver.DnsException(1, e));
                    }
                });
                return;
            }
        }
        Network queryNetwork = dnsNetwork;
        final boolean queryIpv6 = DnsUtils.haveIpv6(queryNetwork);
        final boolean queryIpv4 = DnsUtils.haveIpv4(queryNetwork);
        if (!queryIpv6 && !queryIpv4) {
            executor.execute(new Runnable() { // from class: android.net.-$$Lambda$DnsResolver$kjq9c3feWPGKUPV3AzJBFi1GUvw
                @Override // java.lang.Runnable
                public final void run() {
                    DnsResolver.Callback.this.onError(new DnsResolver.DnsException(1, new ErrnoException("resNetworkQuery", OsConstants.ENONET)));
                }
            });
            return;
        }
        int queryCount2 = 0;
        if (queryIpv6) {
            try {
                FileDescriptor v6fd2 = NetworkUtils.resNetworkQuery(queryNetwork.getNetIdForResolv(), domain, 1, 28, flags);
                queryCount2 = 0 + 1;
                v6fd = v6fd2;
            } catch (ErrnoException e2) {
                executor.execute(new Runnable() { // from class: android.net.-$$Lambda$DnsResolver$uxb9gSgrd6Qyj9SLhCAtOvpxa3I
                    @Override // java.lang.Runnable
                    public final void run() {
                        DnsResolver.Callback.this.onError(new DnsResolver.DnsException(1, e2));
                    }
                });
                return;
            }
        } else {
            v6fd = null;
        }
        try {
            Thread.sleep(2L);
        } catch (InterruptedException e3) {
            Thread.currentThread().interrupt();
        }
        if (queryIpv4) {
            try {
                FileDescriptor v4fd2 = NetworkUtils.resNetworkQuery(queryNetwork.getNetIdForResolv(), domain, 1, 1, flags);
                v4fd = v4fd2;
                queryCount = queryCount2 + 1;
            } catch (ErrnoException e4) {
                if (queryIpv6) {
                    NetworkUtils.resNetworkCancel(v6fd);
                }
                executor.execute(new Runnable() { // from class: android.net.-$$Lambda$DnsResolver$t5xp-fS_zTQ564hG-PIaWJdBP8c
                    @Override // java.lang.Runnable
                    public final void run() {
                        DnsResolver.Callback.this.onError(new DnsResolver.DnsException(1, e4));
                    }
                });
                return;
            }
        } else {
            v4fd = null;
            queryCount = queryCount2;
        }
        InetAddressAnswerAccumulator accumulator = new InetAddressAnswerAccumulator(queryNetwork, queryCount, callback);
        synchronized (lock) {
            try {
                if (queryIpv6) {
                    try {
                        registerFDListener(executor, v6fd, accumulator, cancellationSignal, lock);
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                }
                if (queryIpv4) {
                    registerFDListener(executor, v4fd, accumulator, cancellationSignal, lock);
                }
                if (cancellationSignal == null) {
                    return;
                }
                final FileDescriptor fileDescriptor = v4fd;
                final FileDescriptor fileDescriptor2 = v6fd;
                cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() { // from class: android.net.-$$Lambda$DnsResolver$DW9jYL2ZOH6BjebIVPhZIrrhoD8
                    @Override // android.os.CancellationSignal.OnCancelListener
                    public final void onCancel() {
                        DnsResolver.this.lambda$query$6$DnsResolver(lock, queryIpv4, fileDescriptor, queryIpv6, fileDescriptor2);
                    }
                });
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public /* synthetic */ void lambda$query$6$DnsResolver(Object lock, boolean queryIpv4, FileDescriptor v4fd, boolean queryIpv6, FileDescriptor v6fd) {
        synchronized (lock) {
            if (queryIpv4) {
                try {
                    cancelQuery(v4fd);
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (queryIpv6) {
                cancelQuery(v6fd);
            }
        }
    }

    public void query(Network network, String domain, int nsType, int flags, Executor executor, CancellationSignal cancellationSignal, final Callback<? super List<InetAddress>> callback) {
        Network dnsNetwork;
        if (cancellationSignal != null && cancellationSignal.isCanceled()) {
            return;
        }
        Object lock = new Object();
        if (network != null) {
            dnsNetwork = network;
        } else {
            try {
                dnsNetwork = NetworkUtils.getDnsNetwork();
            } catch (ErrnoException e) {
                e = e;
                executor.execute(new Runnable() { // from class: android.net.-$$Lambda$DnsResolver$wc3_cnx2aezlAHvMEbQVFaTPAcE
                    @Override // java.lang.Runnable
                    public final void run() {
                        DnsResolver.Callback.this.onError(new DnsResolver.DnsException(1, e));
                    }
                });
            }
        }
        Network queryNetwork = dnsNetwork;
        try {
            FileDescriptor queryfd = NetworkUtils.resNetworkQuery(queryNetwork.getNetIdForResolv(), domain, 1, nsType, flags);
            InetAddressAnswerAccumulator accumulator = new InetAddressAnswerAccumulator(queryNetwork, 1, callback);
            synchronized (lock) {
                registerFDListener(executor, queryfd, accumulator, cancellationSignal, lock);
                if (cancellationSignal == null) {
                    return;
                }
                addCancellationSignal(cancellationSignal, queryfd, lock);
            }
        } catch (ErrnoException e2) {
            e = e2;
            executor.execute(new Runnable() { // from class: android.net.-$$Lambda$DnsResolver$wc3_cnx2aezlAHvMEbQVFaTPAcE
                @Override // java.lang.Runnable
                public final void run() {
                    DnsResolver.Callback.this.onError(new DnsResolver.DnsException(1, e));
                }
            });
        }
    }

    /* loaded from: classes2.dex */
    public static final class DnsResponse {
        public final byte[] answerbuf;
        public final int rcode;

        public DnsResponse(byte[] answerbuf, int rcode) {
            this.answerbuf = answerbuf;
            this.rcode = rcode;
        }
    }

    private void registerFDListener(final Executor executor, FileDescriptor queryfd, final Callback<? super byte[]> answerCallback, final CancellationSignal cancellationSignal, final Object lock) {
        final MessageQueue mainThreadMessageQueue = Looper.getMainLooper().getQueue();
        mainThreadMessageQueue.addOnFileDescriptorEventListener(queryfd, 5, new MessageQueue.OnFileDescriptorEventListener() { // from class: android.net.-$$Lambda$DnsResolver$kxKi6qjPYeR_SIipxW4tYpxyM50
            @Override // android.os.MessageQueue.OnFileDescriptorEventListener
            public final int onFileDescriptorEvents(FileDescriptor fileDescriptor, int i) {
                return DnsResolver.lambda$registerFDListener$9(MessageQueue.this, executor, lock, cancellationSignal, answerCallback, fileDescriptor, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$registerFDListener$9(MessageQueue mainThreadMessageQueue, Executor executor, final Object lock, final CancellationSignal cancellationSignal, final Callback answerCallback, final FileDescriptor fd, int events) {
        mainThreadMessageQueue.removeOnFileDescriptorEventListener(fd);
        executor.execute(new Runnable() { // from class: android.net.-$$Lambda$DnsResolver$hIO7FFv0AXN6Nj0Dzka-LD8S870
            @Override // java.lang.Runnable
            public final void run() {
                DnsResolver.lambda$registerFDListener$8(lock, cancellationSignal, fd, answerCallback);
            }
        });
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$registerFDListener$8(Object lock, CancellationSignal cancellationSignal, FileDescriptor fd, Callback answerCallback) {
        DnsResponse resp = null;
        ErrnoException exception = null;
        synchronized (lock) {
            if (cancellationSignal != null) {
                if (cancellationSignal.isCanceled()) {
                    return;
                }
            }
            try {
                resp = NetworkUtils.resNetworkResult(fd);
            } catch (ErrnoException e) {
                Log.e(TAG, "resNetworkResult:" + e.toString());
                exception = e;
            }
            if (exception != null) {
                answerCallback.onError(new DnsException(1, exception));
            } else {
                answerCallback.onAnswer(resp.answerbuf, resp.rcode);
            }
        }
    }

    private void cancelQuery(FileDescriptor queryfd) {
        if (queryfd.valid()) {
            Looper.getMainLooper().getQueue().removeOnFileDescriptorEventListener(queryfd);
            NetworkUtils.resNetworkCancel(queryfd);
        }
    }

    private void addCancellationSignal(CancellationSignal cancellationSignal, final FileDescriptor queryfd, final Object lock) {
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() { // from class: android.net.-$$Lambda$DnsResolver$05nTktlCCI7FQsULCMbgIrjmrGc
            @Override // android.os.CancellationSignal.OnCancelListener
            public final void onCancel() {
                DnsResolver.this.lambda$addCancellationSignal$10$DnsResolver(lock, queryfd);
            }
        });
    }

    public /* synthetic */ void lambda$addCancellationSignal$10$DnsResolver(Object lock, FileDescriptor queryfd) {
        synchronized (lock) {
            cancelQuery(queryfd);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class DnsAddressAnswer extends DnsPacket {
        private static final boolean DBG = false;
        private static final String TAG = "DnsResolver.DnsAddressAnswer";
        private final int mQueryType;

        DnsAddressAnswer(byte[] data) throws ParseException {
            super(data);
            if ((this.mHeader.flags & 32768) == 0) {
                throw new ParseException("Not an answer packet");
            }
            if (this.mHeader.getRecordCount(0) == 0) {
                throw new ParseException("No question found");
            }
            this.mQueryType = this.mRecords[0].get(0).nsType;
        }

        public List<InetAddress> getAddresses() {
            List<InetAddress> results = new ArrayList<>();
            if (this.mHeader.getRecordCount(1) == 0) {
                return results;
            }
            for (DnsPacket.DnsRecord ansSec : this.mRecords[1]) {
                int nsType = ansSec.nsType;
                if (nsType == this.mQueryType && (nsType == 1 || nsType == 28)) {
                    try {
                        results.add(InetAddress.getByAddress(ansSec.getRR()));
                    } catch (UnknownHostException e) {
                    }
                }
            }
            return results;
        }
    }
}
