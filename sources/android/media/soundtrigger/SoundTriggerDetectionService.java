package android.media.soundtrigger;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.soundtrigger.SoundTrigger;
import android.media.soundtrigger.ISoundTriggerDetectionService;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.function.QuadConsumer;
import com.android.internal.util.function.QuintConsumer;
import com.android.internal.util.function.TriConsumer;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.UUID;

@SystemApi
/* loaded from: classes2.dex */
public abstract class SoundTriggerDetectionService extends Service {
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = SoundTriggerDetectionService.class.getSimpleName();
    private Handler mHandler;
    private final Object mLock = new Object();
    @GuardedBy({"mLock"})
    private final ArrayMap<UUID, ISoundTriggerDetectionServiceClient> mClients = new ArrayMap<>();

    public abstract void onStopOperation(UUID uuid, Bundle bundle, int i);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.content.ContextWrapper
    public final void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.mHandler = new Handler(base.getMainLooper());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setClient(UUID uuid, Bundle params, ISoundTriggerDetectionServiceClient client) {
        synchronized (this.mLock) {
            this.mClients.put(uuid, client);
        }
        onConnected(uuid, params);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeClient(UUID uuid, Bundle params) {
        synchronized (this.mLock) {
            this.mClients.remove(uuid);
        }
        onDisconnected(uuid, params);
    }

    public void onConnected(UUID uuid, Bundle params) {
    }

    public void onDisconnected(UUID uuid, Bundle params) {
    }

    public void onGenericRecognitionEvent(UUID uuid, Bundle params, int opId, SoundTrigger.RecognitionEvent event) {
        operationFinished(uuid, opId);
    }

    public void onError(UUID uuid, Bundle params, int opId, int status) {
        operationFinished(uuid, opId);
    }

    public final void operationFinished(UUID uuid, int opId) {
        try {
            synchronized (this.mLock) {
                ISoundTriggerDetectionServiceClient client = this.mClients.get(uuid);
                if (client == null) {
                    String str = LOG_TAG;
                    Log.w(str, "operationFinished called, but no client for " + uuid + ". Was this called after onDisconnected?");
                    return;
                }
                client.onOpFinished(opId);
            }
        } catch (RemoteException e) {
            String str2 = LOG_TAG;
            Log.e(str2, "operationFinished, remote exception for client " + uuid, e);
        }
    }

    /* renamed from: android.media.soundtrigger.SoundTriggerDetectionService$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends ISoundTriggerDetectionService.Stub {
        private final Object mBinderLock = new Object();
        @GuardedBy({"mBinderLock"})
        public final ArrayMap<UUID, Bundle> mParams = new ArrayMap<>();

        AnonymousClass1() {
        }

        @Override // android.media.soundtrigger.ISoundTriggerDetectionService
        public void setClient(ParcelUuid puuid, Bundle params, ISoundTriggerDetectionServiceClient client) {
            UUID uuid = puuid.getUuid();
            synchronized (this.mBinderLock) {
                this.mParams.put(uuid, params);
            }
            SoundTriggerDetectionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new QuadConsumer() { // from class: android.media.soundtrigger.-$$Lambda$SoundTriggerDetectionService$1$LlOo7TiZplZCgGhS07DqYHocFcw
                @Override // com.android.internal.util.function.QuadConsumer
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4) {
                    ((SoundTriggerDetectionService) obj).setClient((UUID) obj2, (Bundle) obj3, (ISoundTriggerDetectionServiceClient) obj4);
                }
            }, SoundTriggerDetectionService.this, uuid, params, client));
        }

        @Override // android.media.soundtrigger.ISoundTriggerDetectionService
        public void removeClient(ParcelUuid puuid) {
            Bundle params;
            UUID uuid = puuid.getUuid();
            synchronized (this.mBinderLock) {
                params = this.mParams.remove(uuid);
            }
            SoundTriggerDetectionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new TriConsumer() { // from class: android.media.soundtrigger.-$$Lambda$SoundTriggerDetectionService$1$pKR4r0FzOzoVczcnvLQIZNjkZZw
                @Override // com.android.internal.util.function.TriConsumer
                public final void accept(Object obj, Object obj2, Object obj3) {
                    ((SoundTriggerDetectionService) obj).removeClient((UUID) obj2, (Bundle) obj3);
                }
            }, SoundTriggerDetectionService.this, uuid, params));
        }

        @Override // android.media.soundtrigger.ISoundTriggerDetectionService
        public void onGenericRecognitionEvent(ParcelUuid puuid, int opId, SoundTrigger.GenericRecognitionEvent event) {
            Bundle params;
            UUID uuid = puuid.getUuid();
            synchronized (this.mBinderLock) {
                params = this.mParams.get(uuid);
            }
            SoundTriggerDetectionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new QuintConsumer() { // from class: android.media.soundtrigger.-$$Lambda$ISQYIYPBRBIOLBUJy7rrJW-SiJg
                @Override // com.android.internal.util.function.QuintConsumer
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                    ((SoundTriggerDetectionService) obj).onGenericRecognitionEvent((UUID) obj2, (Bundle) obj3, ((Integer) obj4).intValue(), (SoundTrigger.GenericRecognitionEvent) obj5);
                }
            }, SoundTriggerDetectionService.this, uuid, params, Integer.valueOf(opId), event));
        }

        @Override // android.media.soundtrigger.ISoundTriggerDetectionService
        public void onError(ParcelUuid puuid, int opId, int status) {
            Bundle params;
            UUID uuid = puuid.getUuid();
            synchronized (this.mBinderLock) {
                params = this.mParams.get(uuid);
            }
            SoundTriggerDetectionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new QuintConsumer() { // from class: android.media.soundtrigger.-$$Lambda$oNgT3sYhSGVWlnU92bECo_ULGeY
                @Override // com.android.internal.util.function.QuintConsumer
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                    ((SoundTriggerDetectionService) obj).onError((UUID) obj2, (Bundle) obj3, ((Integer) obj4).intValue(), ((Integer) obj5).intValue());
                }
            }, SoundTriggerDetectionService.this, uuid, params, Integer.valueOf(opId), Integer.valueOf(status)));
        }

        @Override // android.media.soundtrigger.ISoundTriggerDetectionService
        public void onStopOperation(ParcelUuid puuid, int opId) {
            Bundle params;
            UUID uuid = puuid.getUuid();
            synchronized (this.mBinderLock) {
                params = this.mParams.get(uuid);
            }
            SoundTriggerDetectionService.this.mHandler.sendMessage(PooledLambda.obtainMessage(new QuadConsumer() { // from class: android.media.soundtrigger.-$$Lambda$bPGNpvkCtpPW14oaI3pxn1e6JtQ
                @Override // com.android.internal.util.function.QuadConsumer
                public final void accept(Object obj, Object obj2, Object obj3, Object obj4) {
                    ((SoundTriggerDetectionService) obj).onStopOperation((UUID) obj2, (Bundle) obj3, ((Integer) obj4).intValue());
                }
            }, SoundTriggerDetectionService.this, uuid, params, Integer.valueOf(opId)));
        }
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        return new AnonymousClass1();
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        this.mClients.clear();
        return false;
    }
}
