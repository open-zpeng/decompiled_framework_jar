package com.android.internal.util;

import android.os.RemoteException;
import android.util.ExceptionUtils;
import com.android.internal.util.FunctionalUtils;
import java.util.function.Consumer;
/* loaded from: classes3.dex */
public class FunctionalUtils {

    @FunctionalInterface
    /* loaded from: classes3.dex */
    public interface ThrowingSupplier<T> {
        synchronized T getOrThrow() throws Exception;
    }

    private synchronized FunctionalUtils() {
    }

    public static synchronized <T> Consumer<T> uncheckExceptions(ThrowingConsumer<T> action) {
        return action;
    }

    public static synchronized <T> Consumer<T> ignoreRemoteException(RemoteExceptionIgnoringConsumer<T> action) {
        return action;
    }

    public static synchronized Runnable handleExceptions(final ThrowingRunnable r, final Consumer<Throwable> handler) {
        return new Runnable() { // from class: com.android.internal.util.-$$Lambda$FunctionalUtils$koCSI8D7Nu5vOJTVTEj0m3leo_U
            @Override // java.lang.Runnable
            public final void run() {
                FunctionalUtils.lambda$handleExceptions$0(FunctionalUtils.ThrowingRunnable.this, handler);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$handleExceptions$0(ThrowingRunnable r, Consumer handler) {
        try {
            r.run();
        } catch (Throwable t) {
            handler.accept(t);
        }
    }

    @FunctionalInterface
    /* loaded from: classes3.dex */
    public interface ThrowingRunnable extends Runnable {
        synchronized void runOrThrow() throws Exception;

        @Override // java.lang.Runnable
        default void run() {
            try {
                runOrThrow();
            } catch (Exception ex) {
                throw ExceptionUtils.propagate(ex);
            }
        }
    }

    @FunctionalInterface
    /* loaded from: classes3.dex */
    public interface ThrowingConsumer<T> extends Consumer<T> {
        synchronized void acceptOrThrow(T t) throws Exception;

        @Override // java.util.function.Consumer
        default void accept(T t) {
            try {
                acceptOrThrow(t);
            } catch (Exception ex) {
                throw ExceptionUtils.propagate(ex);
            }
        }
    }

    @FunctionalInterface
    /* loaded from: classes3.dex */
    public interface RemoteExceptionIgnoringConsumer<T> extends Consumer<T> {
        synchronized void acceptOrThrow(T t) throws RemoteException;

        @Override // java.util.function.Consumer
        default void accept(T t) {
            try {
                acceptOrThrow(t);
            } catch (RemoteException e) {
            }
        }
    }
}
