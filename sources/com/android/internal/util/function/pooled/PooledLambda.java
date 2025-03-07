package com.android.internal.util.function.pooled;

import android.os.Message;
import com.android.internal.util.function.HeptConsumer;
import com.android.internal.util.function.HeptFunction;
import com.android.internal.util.function.HexConsumer;
import com.android.internal.util.function.HexFunction;
import com.android.internal.util.function.NonaConsumer;
import com.android.internal.util.function.NonaFunction;
import com.android.internal.util.function.OctConsumer;
import com.android.internal.util.function.OctFunction;
import com.android.internal.util.function.QuadConsumer;
import com.android.internal.util.function.QuadFunction;
import com.android.internal.util.function.QuintConsumer;
import com.android.internal.util.function.QuintFunction;
import com.android.internal.util.function.TriConsumer;
import com.android.internal.util.function.TriFunction;
import com.android.internal.util.function.pooled.PooledSupplier;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/* loaded from: classes3.dex */
public interface PooledLambda {
    void recycle();

    PooledLambda recycleOnUse();

    static <R> ArgumentPlaceholder<R> __() {
        return (ArgumentPlaceholder<R>) ArgumentPlaceholder.INSTANCE;
    }

    static <R> ArgumentPlaceholder<R> __(Class<R> typeHint) {
        return __();
    }

    static <R> PooledSupplier<R> obtainSupplier(R value) {
        PooledLambdaImpl r = PooledLambdaImpl.acquireConstSupplier(3);
        r.mFunc = value;
        return r;
    }

    static PooledSupplier.OfInt obtainSupplier(int value) {
        PooledLambdaImpl r = PooledLambdaImpl.acquireConstSupplier(4);
        r.mConstValue = value;
        return r;
    }

    static PooledSupplier.OfLong obtainSupplier(long value) {
        PooledLambdaImpl r = PooledLambdaImpl.acquireConstSupplier(5);
        r.mConstValue = value;
        return r;
    }

    static PooledSupplier.OfDouble obtainSupplier(double value) {
        PooledLambdaImpl r = PooledLambdaImpl.acquireConstSupplier(6);
        r.mConstValue = Double.doubleToRawLongBits(value);
        return r;
    }

    static <A> PooledRunnable obtainRunnable(Consumer<? super A> function, A arg1) {
        return (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 1, 0, 1, arg1, null, null, null, null, null, null, null, null);
    }

    static <A> PooledSupplier<Boolean> obtainSupplier(Predicate<? super A> function, A arg1) {
        return (PooledSupplier) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 1, 0, 2, arg1, null, null, null, null, null, null, null, null);
    }

    static <A, R> PooledSupplier<R> obtainSupplier(Function<? super A, ? extends R> function, A arg1) {
        return (PooledSupplier) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 1, 0, 3, arg1, null, null, null, null, null, null, null, null);
    }

    static <A> Message obtainMessage(Consumer<? super A> function, A arg1) {
        Message callback;
        synchronized (Message.sPoolSync) {
            PooledRunnable callback2 = (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sMessageCallbacksPool, function, 1, 0, 1, arg1, null, null, null, null, null, null, null, null);
            callback = Message.obtain().setCallback(callback2.recycleOnUse());
        }
        return callback;
    }

    static <A, B> PooledRunnable obtainRunnable(BiConsumer<? super A, ? super B> function, A arg1, B arg2) {
        return (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 2, 0, 1, arg1, arg2, null, null, null, null, null, null, null);
    }

    static <A, B> PooledSupplier<Boolean> obtainSupplier(BiPredicate<? super A, ? super B> function, A arg1, B arg2) {
        return (PooledSupplier) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 2, 0, 2, arg1, arg2, null, null, null, null, null, null, null);
    }

    static <A, B, R> PooledSupplier<R> obtainSupplier(BiFunction<? super A, ? super B, ? extends R> function, A arg1, B arg2) {
        return (PooledSupplier) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 2, 0, 3, arg1, arg2, null, null, null, null, null, null, null);
    }

    static <A, B> PooledConsumer<A> obtainConsumer(BiConsumer<? super A, ? super B> function, ArgumentPlaceholder<A> arg1, B arg2) {
        return (PooledConsumer) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 2, 1, 1, arg1, arg2, null, null, null, null, null, null, null);
    }

    static <A, B> PooledPredicate<A> obtainPredicate(BiPredicate<? super A, ? super B> function, ArgumentPlaceholder<A> arg1, B arg2) {
        return (PooledPredicate) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 2, 1, 2, arg1, arg2, null, null, null, null, null, null, null);
    }

    static <A, B, R> PooledFunction<A, R> obtainFunction(BiFunction<? super A, ? super B, ? extends R> function, ArgumentPlaceholder<A> arg1, B arg2) {
        return (PooledFunction) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 2, 1, 3, arg1, arg2, null, null, null, null, null, null, null);
    }

    static <A, B> PooledConsumer<B> obtainConsumer(BiConsumer<? super A, ? super B> function, A arg1, ArgumentPlaceholder<B> arg2) {
        return (PooledConsumer) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 2, 1, 1, arg1, arg2, null, null, null, null, null, null, null);
    }

    static <A, B> PooledPredicate<B> obtainPredicate(BiPredicate<? super A, ? super B> function, A arg1, ArgumentPlaceholder<B> arg2) {
        return (PooledPredicate) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 2, 1, 2, arg1, arg2, null, null, null, null, null, null, null);
    }

    static <A, B, R> PooledFunction<B, R> obtainFunction(BiFunction<? super A, ? super B, ? extends R> function, A arg1, ArgumentPlaceholder<B> arg2) {
        return (PooledFunction) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 2, 1, 3, arg1, arg2, null, null, null, null, null, null, null);
    }

    static <A, B> Message obtainMessage(BiConsumer<? super A, ? super B> function, A arg1, B arg2) {
        Message callback;
        synchronized (Message.sPoolSync) {
            PooledRunnable callback2 = (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sMessageCallbacksPool, function, 2, 0, 1, arg1, arg2, null, null, null, null, null, null, null);
            callback = Message.obtain().setCallback(callback2.recycleOnUse());
        }
        return callback;
    }

    static <A, B, C> PooledRunnable obtainRunnable(TriConsumer<? super A, ? super B, ? super C> function, A arg1, B arg2, C arg3) {
        return (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 3, 0, 1, arg1, arg2, arg3, null, null, null, null, null, null);
    }

    static <A, B, C, R> PooledSupplier<R> obtainSupplier(TriFunction<? super A, ? super B, ? super C, ? extends R> function, A arg1, B arg2, C arg3) {
        return (PooledSupplier) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 3, 0, 3, arg1, arg2, arg3, null, null, null, null, null, null);
    }

    static <A, B, C> PooledConsumer<A> obtainConsumer(TriConsumer<? super A, ? super B, ? super C> function, ArgumentPlaceholder<A> arg1, B arg2, C arg3) {
        return (PooledConsumer) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 3, 1, 1, arg1, arg2, arg3, null, null, null, null, null, null);
    }

    static <A, B, C, R> PooledFunction<A, R> obtainFunction(TriFunction<? super A, ? super B, ? super C, ? extends R> function, ArgumentPlaceholder<A> arg1, B arg2, C arg3) {
        return (PooledFunction) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 3, 1, 3, arg1, arg2, arg3, null, null, null, null, null, null);
    }

    static <A, B, C> PooledConsumer<B> obtainConsumer(TriConsumer<? super A, ? super B, ? super C> function, A arg1, ArgumentPlaceholder<B> arg2, C arg3) {
        return (PooledConsumer) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 3, 1, 1, arg1, arg2, arg3, null, null, null, null, null, null);
    }

    static <A, B, C, R> PooledFunction<B, R> obtainFunction(TriFunction<? super A, ? super B, ? super C, ? extends R> function, A arg1, ArgumentPlaceholder<B> arg2, C arg3) {
        return (PooledFunction) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 3, 1, 3, arg1, arg2, arg3, null, null, null, null, null, null);
    }

    static <A, B, C> PooledConsumer<C> obtainConsumer(TriConsumer<? super A, ? super B, ? super C> function, A arg1, B arg2, ArgumentPlaceholder<C> arg3) {
        return (PooledConsumer) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 3, 1, 1, arg1, arg2, arg3, null, null, null, null, null, null);
    }

    static <A, B, C, R> PooledFunction<C, R> obtainFunction(TriFunction<? super A, ? super B, ? super C, ? extends R> function, A arg1, B arg2, ArgumentPlaceholder<C> arg3) {
        return (PooledFunction) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 3, 1, 3, arg1, arg2, arg3, null, null, null, null, null, null);
    }

    static <A, B, C> Message obtainMessage(TriConsumer<? super A, ? super B, ? super C> function, A arg1, B arg2, C arg3) {
        Message callback;
        synchronized (Message.sPoolSync) {
            PooledRunnable callback2 = (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sMessageCallbacksPool, function, 3, 0, 1, arg1, arg2, arg3, null, null, null, null, null, null);
            callback = Message.obtain().setCallback(callback2.recycleOnUse());
        }
        return callback;
    }

    static <A, B, C, D> PooledRunnable obtainRunnable(QuadConsumer<? super A, ? super B, ? super C, ? super D> function, A arg1, B arg2, C arg3, D arg4) {
        return (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 4, 0, 1, arg1, arg2, arg3, arg4, null, null, null, null, null);
    }

    static <A, B, C, D, R> PooledSupplier<R> obtainSupplier(QuadFunction<? super A, ? super B, ? super C, ? super D, ? extends R> function, A arg1, B arg2, C arg3, D arg4) {
        return (PooledSupplier) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 4, 0, 3, arg1, arg2, arg3, arg4, null, null, null, null, null);
    }

    static <A, B, C, D> PooledConsumer<A> obtainConsumer(QuadConsumer<? super A, ? super B, ? super C, ? super D> function, ArgumentPlaceholder<A> arg1, B arg2, C arg3, D arg4) {
        return (PooledConsumer) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 4, 1, 1, arg1, arg2, arg3, arg4, null, null, null, null, null);
    }

    static <A, B, C, D, R> PooledFunction<A, R> obtainFunction(QuadFunction<? super A, ? super B, ? super C, ? super D, ? extends R> function, ArgumentPlaceholder<A> arg1, B arg2, C arg3, D arg4) {
        return (PooledFunction) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 4, 1, 3, arg1, arg2, arg3, arg4, null, null, null, null, null);
    }

    static <A, B, C, D> PooledConsumer<B> obtainConsumer(QuadConsumer<? super A, ? super B, ? super C, ? super D> function, A arg1, ArgumentPlaceholder<B> arg2, C arg3, D arg4) {
        return (PooledConsumer) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 4, 1, 1, arg1, arg2, arg3, arg4, null, null, null, null, null);
    }

    static <A, B, C, D, R> PooledFunction<B, R> obtainFunction(QuadFunction<? super A, ? super B, ? super C, ? super D, ? extends R> function, A arg1, ArgumentPlaceholder<B> arg2, C arg3, D arg4) {
        return (PooledFunction) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 4, 1, 3, arg1, arg2, arg3, arg4, null, null, null, null, null);
    }

    static <A, B, C, D> PooledConsumer<C> obtainConsumer(QuadConsumer<? super A, ? super B, ? super C, ? super D> function, A arg1, B arg2, ArgumentPlaceholder<C> arg3, D arg4) {
        return (PooledConsumer) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 4, 1, 1, arg1, arg2, arg3, arg4, null, null, null, null, null);
    }

    static <A, B, C, D, R> PooledFunction<C, R> obtainFunction(QuadFunction<? super A, ? super B, ? super C, ? super D, ? extends R> function, A arg1, B arg2, ArgumentPlaceholder<C> arg3, D arg4) {
        return (PooledFunction) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 4, 1, 3, arg1, arg2, arg3, arg4, null, null, null, null, null);
    }

    static <A, B, C, D> PooledConsumer<D> obtainConsumer(QuadConsumer<? super A, ? super B, ? super C, ? super D> function, A arg1, B arg2, C arg3, ArgumentPlaceholder<D> arg4) {
        return (PooledConsumer) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 4, 1, 1, arg1, arg2, arg3, arg4, null, null, null, null, null);
    }

    static <A, B, C, D, R> PooledFunction<D, R> obtainFunction(QuadFunction<? super A, ? super B, ? super C, ? super D, ? extends R> function, A arg1, B arg2, C arg3, ArgumentPlaceholder<D> arg4) {
        return (PooledFunction) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 4, 1, 3, arg1, arg2, arg3, arg4, null, null, null, null, null);
    }

    static <A, B, C, D> Message obtainMessage(QuadConsumer<? super A, ? super B, ? super C, ? super D> function, A arg1, B arg2, C arg3, D arg4) {
        Message callback;
        synchronized (Message.sPoolSync) {
            PooledRunnable callback2 = (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sMessageCallbacksPool, function, 4, 0, 1, arg1, arg2, arg3, arg4, null, null, null, null, null);
            callback = Message.obtain().setCallback(callback2.recycleOnUse());
        }
        return callback;
    }

    static <A, B, C, D, E> PooledRunnable obtainRunnable(QuintConsumer<? super A, ? super B, ? super C, ? super D, ? super E> function, A arg1, B arg2, C arg3, D arg4, E arg5) {
        return (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 5, 0, 1, arg1, arg2, arg3, arg4, arg5, null, null, null, null);
    }

    static <A, B, C, D, E, R> PooledSupplier<R> obtainSupplier(QuintFunction<? super A, ? super B, ? super C, ? super D, ? super E, ? extends R> function, A arg1, B arg2, C arg3, D arg4, E arg5) {
        return (PooledSupplier) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 5, 0, 3, arg1, arg2, arg3, arg4, arg5, null, null, null, null);
    }

    static <A, B, C, D, E> Message obtainMessage(QuintConsumer<? super A, ? super B, ? super C, ? super D, ? super E> function, A arg1, B arg2, C arg3, D arg4, E arg5) {
        Message callback;
        synchronized (Message.sPoolSync) {
            PooledRunnable callback2 = (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sMessageCallbacksPool, function, 5, 0, 1, arg1, arg2, arg3, arg4, arg5, null, null, null, null);
            callback = Message.obtain().setCallback(callback2.recycleOnUse());
        }
        return callback;
    }

    static <A, B, C, D, E, F> PooledRunnable obtainRunnable(HexConsumer<? super A, ? super B, ? super C, ? super D, ? super E, ? super F> function, A arg1, B arg2, C arg3, D arg4, E arg5, F arg6) {
        return (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 6, 0, 1, arg1, arg2, arg3, arg4, arg5, arg6, null, null, null);
    }

    static <A, B, C, D, E, F, R> PooledSupplier<R> obtainSupplier(HexFunction<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? extends R> function, A arg1, B arg2, C arg3, D arg4, E arg5, F arg6) {
        return (PooledSupplier) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 6, 0, 3, arg1, arg2, arg3, arg4, arg5, arg6, null, null, null);
    }

    static <A, B, C, D, E, F> Message obtainMessage(HexConsumer<? super A, ? super B, ? super C, ? super D, ? super E, ? super F> function, A arg1, B arg2, C arg3, D arg4, E arg5, F arg6) {
        Message callback;
        synchronized (Message.sPoolSync) {
            PooledRunnable callback2 = (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sMessageCallbacksPool, function, 6, 0, 1, arg1, arg2, arg3, arg4, arg5, arg6, null, null, null);
            callback = Message.obtain().setCallback(callback2.recycleOnUse());
        }
        return callback;
    }

    static <A, B, C, D, E, F, G> PooledRunnable obtainRunnable(HeptConsumer<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G> function, A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7) {
        return (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 7, 0, 1, arg1, arg2, arg3, arg4, arg5, arg6, arg7, null, null);
    }

    static <A, B, C, D, E, F, G, R> PooledSupplier<R> obtainSupplier(HeptFunction<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends R> function, A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7) {
        return (PooledSupplier) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 7, 0, 3, arg1, arg2, arg3, arg4, arg5, arg6, arg7, null, null);
    }

    static <A, B, C, D, E, F, G> Message obtainMessage(HeptConsumer<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G> function, A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7) {
        Message callback;
        synchronized (Message.sPoolSync) {
            PooledRunnable callback2 = (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sMessageCallbacksPool, function, 7, 0, 1, arg1, arg2, arg3, arg4, arg5, arg6, arg7, null, null);
            callback = Message.obtain().setCallback(callback2.recycleOnUse());
        }
        return callback;
    }

    static <A, B, C, D, E, F, G, H> PooledRunnable obtainRunnable(OctConsumer<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H> function, A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8) {
        return (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 8, 0, 1, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, null);
    }

    static <A, B, C, D, E, F, G, H, R> PooledSupplier<R> obtainSupplier(OctFunction<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? extends R> function, A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8) {
        return (PooledSupplier) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 8, 0, 3, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, null);
    }

    static <A, B, C, D, E, F, G, H> Message obtainMessage(OctConsumer<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H> function, A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8) {
        Message callback;
        synchronized (Message.sPoolSync) {
            PooledRunnable callback2 = (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sMessageCallbacksPool, function, 8, 0, 1, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, null);
            callback = Message.obtain().setCallback(callback2.recycleOnUse());
        }
        return callback;
    }

    static <A, B, C, D, E, F, G, H, I> PooledRunnable obtainRunnable(NonaConsumer<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I> function, A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8, I arg9) {
        return (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 9, 0, 1, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    }

    static <A, B, C, D, E, F, G, H, I, R> PooledSupplier<R> obtainSupplier(NonaFunction<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I, ? extends R> function, A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8, I arg9) {
        return (PooledSupplier) PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, function, 9, 0, 3, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    }

    static <A, B, C, D, E, F, G, H, I> Message obtainMessage(NonaConsumer<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? super H, ? super I> function, A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8, I arg9) {
        Message callback;
        synchronized (Message.sPoolSync) {
            PooledRunnable callback2 = (PooledRunnable) PooledLambdaImpl.acquire(PooledLambdaImpl.sMessageCallbacksPool, function, 9, 0, 1, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
            callback = Message.obtain().setCallback(callback2.recycleOnUse());
        }
        return callback;
    }
}
