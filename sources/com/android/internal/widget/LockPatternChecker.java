package com.android.internal.widget;

import android.annotation.UnsupportedAppUsage;
import android.os.AsyncTask;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockPatternView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* loaded from: classes3.dex */
public final class LockPatternChecker {

    /* loaded from: classes3.dex */
    public interface OnVerifyCallback {
        void onVerified(byte[] bArr, int i);
    }

    /* loaded from: classes3.dex */
    public interface OnCheckCallback {
        void onChecked(boolean z, int i);

        default void onEarlyMatched() {
        }

        default void onCancelled() {
        }
    }

    public static AsyncTask<?, ?, ?> verifyPattern(final LockPatternUtils utils, final List<LockPatternView.Cell> pattern, final long challenge, final int userId, final OnVerifyCallback callback) {
        AsyncTask<Void, Void, byte[]> task = new AsyncTask<Void, Void, byte[]>() { // from class: com.android.internal.widget.LockPatternChecker.1
            private int mThrottleTimeout;
            private List<LockPatternView.Cell> patternCopy;

            @Override // android.os.AsyncTask
            protected void onPreExecute() {
                this.patternCopy = new ArrayList(pattern);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public byte[] doInBackground(Void... args) {
                try {
                    return utils.verifyPattern(this.patternCopy, challenge, userId);
                } catch (LockPatternUtils.RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return null;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(byte[] result) {
                callback.onVerified(result, this.mThrottleTimeout);
            }
        };
        task.execute(new Void[0]);
        return task;
    }

    public static AsyncTask<?, ?, ?> checkPattern(final LockPatternUtils utils, final List<LockPatternView.Cell> pattern, final int userId, final OnCheckCallback callback) {
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() { // from class: com.android.internal.widget.LockPatternChecker.2
            private int mThrottleTimeout;
            private List<LockPatternView.Cell> patternCopy;

            @Override // android.os.AsyncTask
            protected void onPreExecute() {
                this.patternCopy = new ArrayList(pattern);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Boolean doInBackground(Void... args) {
                try {
                    LockPatternUtils lockPatternUtils = utils;
                    List<LockPatternView.Cell> list = this.patternCopy;
                    int i = userId;
                    OnCheckCallback onCheckCallback = callback;
                    Objects.requireNonNull(onCheckCallback);
                    return Boolean.valueOf(lockPatternUtils.checkPattern(list, i, new $$Lambda$TTC7hNz7BTsLwhNRb2L5kl7mdU(onCheckCallback)));
                } catch (LockPatternUtils.RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return false;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Boolean result) {
                callback.onChecked(result.booleanValue(), this.mThrottleTimeout);
            }

            @Override // android.os.AsyncTask
            protected void onCancelled() {
                callback.onCancelled();
            }
        };
        task.execute(new Void[0]);
        return task;
    }

    @Deprecated
    public static AsyncTask<?, ?, ?> verifyPassword(LockPatternUtils utils, String password, long challenge, int userId, OnVerifyCallback callback) {
        byte[] passwordBytes = password != null ? password.getBytes() : null;
        return verifyPassword(utils, passwordBytes, challenge, userId, callback);
    }

    public static AsyncTask<?, ?, ?> verifyPassword(final LockPatternUtils utils, final byte[] password, final long challenge, final int userId, final OnVerifyCallback callback) {
        AsyncTask<Void, Void, byte[]> task = new AsyncTask<Void, Void, byte[]>() { // from class: com.android.internal.widget.LockPatternChecker.3
            private int mThrottleTimeout;

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public byte[] doInBackground(Void... args) {
                try {
                    return LockPatternUtils.this.verifyPassword(password, challenge, userId);
                } catch (LockPatternUtils.RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return null;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(byte[] result) {
                callback.onVerified(result, this.mThrottleTimeout);
            }
        };
        task.execute(new Void[0]);
        return task;
    }

    public static AsyncTask<?, ?, ?> verifyTiedProfileChallenge(final LockPatternUtils utils, final byte[] password, final boolean isPattern, final long challenge, final int userId, final OnVerifyCallback callback) {
        AsyncTask<Void, Void, byte[]> task = new AsyncTask<Void, Void, byte[]>() { // from class: com.android.internal.widget.LockPatternChecker.4
            private int mThrottleTimeout;

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public byte[] doInBackground(Void... args) {
                try {
                    return LockPatternUtils.this.verifyTiedProfileChallenge(password, isPattern, challenge, userId);
                } catch (LockPatternUtils.RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return null;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(byte[] result) {
                callback.onVerified(result, this.mThrottleTimeout);
            }
        };
        task.execute(new Void[0]);
        return task;
    }

    @UnsupportedAppUsage
    @Deprecated
    public static AsyncTask<?, ?, ?> checkPassword(LockPatternUtils utils, String password, int userId, OnCheckCallback callback) {
        byte[] passwordBytes = password != null ? password.getBytes() : null;
        return checkPassword(utils, passwordBytes, userId, callback);
    }

    public static AsyncTask<?, ?, ?> checkPassword(final LockPatternUtils utils, final byte[] passwordBytes, final int userId, final OnCheckCallback callback) {
        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() { // from class: com.android.internal.widget.LockPatternChecker.5
            private int mThrottleTimeout;

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Boolean doInBackground(Void... args) {
                try {
                    LockPatternUtils lockPatternUtils = LockPatternUtils.this;
                    byte[] bArr = passwordBytes;
                    int i = userId;
                    OnCheckCallback onCheckCallback = callback;
                    Objects.requireNonNull(onCheckCallback);
                    return Boolean.valueOf(lockPatternUtils.checkPassword(bArr, i, new $$Lambda$TTC7hNz7BTsLwhNRb2L5kl7mdU(onCheckCallback)));
                } catch (LockPatternUtils.RequestThrottledException ex) {
                    this.mThrottleTimeout = ex.getTimeoutMs();
                    return false;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Boolean result) {
                callback.onChecked(result.booleanValue(), this.mThrottleTimeout);
            }

            @Override // android.os.AsyncTask
            protected void onCancelled() {
                callback.onCancelled();
            }
        };
        task.execute(new Void[0]);
        return task;
    }
}
