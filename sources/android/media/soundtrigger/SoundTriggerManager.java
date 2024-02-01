package android.media.soundtrigger;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.Context;
import android.hardware.soundtrigger.SoundTrigger;
import android.media.soundtrigger.SoundTriggerDetector;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Slog;
import com.android.internal.app.ISoundTriggerService;
import com.android.internal.util.Preconditions;
import java.util.HashMap;
import java.util.UUID;

@SystemApi
/* loaded from: classes2.dex */
public final class SoundTriggerManager {
    private static final boolean DBG = false;
    public static final String EXTRA_MESSAGE_TYPE = "android.media.soundtrigger.MESSAGE_TYPE";
    public static final String EXTRA_RECOGNITION_EVENT = "android.media.soundtrigger.RECOGNITION_EVENT";
    public static final String EXTRA_STATUS = "android.media.soundtrigger.STATUS";
    public static final int FLAG_MESSAGE_TYPE_RECOGNITION_ERROR = 1;
    public static final int FLAG_MESSAGE_TYPE_RECOGNITION_EVENT = 0;
    public static final int FLAG_MESSAGE_TYPE_RECOGNITION_PAUSED = 2;
    public static final int FLAG_MESSAGE_TYPE_RECOGNITION_RESUMED = 3;
    public static final int FLAG_MESSAGE_TYPE_UNKNOWN = -1;
    private static final String TAG = "SoundTriggerManager";
    private final Context mContext;
    private final HashMap<UUID, SoundTriggerDetector> mReceiverInstanceMap = new HashMap<>();
    private final ISoundTriggerService mSoundTriggerService;

    public SoundTriggerManager(Context context, ISoundTriggerService soundTriggerService) {
        this.mSoundTriggerService = soundTriggerService;
        this.mContext = context;
    }

    public void updateModel(Model model) {
        try {
            this.mSoundTriggerService.updateSoundModel(model.getGenericSoundModel());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Model getModel(UUID soundModelId) {
        try {
            return new Model(this.mSoundTriggerService.getSoundModel(new ParcelUuid(soundModelId)));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void deleteModel(UUID soundModelId) {
        try {
            this.mSoundTriggerService.deleteSoundModel(new ParcelUuid(soundModelId));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public SoundTriggerDetector createSoundTriggerDetector(UUID soundModelId, SoundTriggerDetector.Callback callback, Handler handler) {
        if (soundModelId == null) {
            return null;
        }
        this.mReceiverInstanceMap.get(soundModelId);
        SoundTriggerDetector newInstance = new SoundTriggerDetector(this.mSoundTriggerService, soundModelId, callback, handler);
        this.mReceiverInstanceMap.put(soundModelId, newInstance);
        return newInstance;
    }

    /* loaded from: classes2.dex */
    public static class Model {
        private SoundTrigger.GenericSoundModel mGenericSoundModel;

        Model(SoundTrigger.GenericSoundModel soundTriggerModel) {
            this.mGenericSoundModel = soundTriggerModel;
        }

        public static Model create(UUID modelUuid, UUID vendorUuid, byte[] data) {
            return new Model(new SoundTrigger.GenericSoundModel(modelUuid, vendorUuid, data));
        }

        public UUID getModelUuid() {
            return this.mGenericSoundModel.uuid;
        }

        public UUID getVendorUuid() {
            return this.mGenericSoundModel.vendorUuid;
        }

        public byte[] getModelData() {
            return this.mGenericSoundModel.data;
        }

        SoundTrigger.GenericSoundModel getGenericSoundModel() {
            return this.mGenericSoundModel;
        }
    }

    @UnsupportedAppUsage
    public int loadSoundModel(SoundTrigger.SoundModel soundModel) {
        if (soundModel == null) {
            return Integer.MIN_VALUE;
        }
        try {
            int i = soundModel.type;
            if (i != 0) {
                if (i == 1) {
                    return this.mSoundTriggerService.loadGenericSoundModel((SoundTrigger.GenericSoundModel) soundModel);
                }
                Slog.e(TAG, "Unkown model type");
                return Integer.MIN_VALUE;
            }
            return this.mSoundTriggerService.loadKeyphraseSoundModel((SoundTrigger.KeyphraseSoundModel) soundModel);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public int startRecognition(UUID soundModelId, Bundle params, ComponentName detectionService, SoundTrigger.RecognitionConfig config) {
        Preconditions.checkNotNull(soundModelId);
        Preconditions.checkNotNull(detectionService);
        Preconditions.checkNotNull(config);
        try {
            return this.mSoundTriggerService.startRecognitionForService(new ParcelUuid(soundModelId), params, detectionService, config);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public int stopRecognition(UUID soundModelId) {
        if (soundModelId == null) {
            return Integer.MIN_VALUE;
        }
        try {
            return this.mSoundTriggerService.stopRecognitionForService(new ParcelUuid(soundModelId));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public int unloadSoundModel(UUID soundModelId) {
        if (soundModelId == null) {
            return Integer.MIN_VALUE;
        }
        try {
            return this.mSoundTriggerService.unloadSoundModel(new ParcelUuid(soundModelId));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public boolean isRecognitionActive(UUID soundModelId) {
        if (soundModelId == null) {
            return false;
        }
        try {
            return this.mSoundTriggerService.isRecognitionActive(new ParcelUuid(soundModelId));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getDetectionServiceOperationsTimeout() {
        try {
            return Settings.Global.getInt(this.mContext.getContentResolver(), Settings.Global.SOUND_TRIGGER_DETECTION_SERVICE_OP_TIMEOUT);
        } catch (Settings.SettingNotFoundException e) {
            return Integer.MAX_VALUE;
        }
    }

    @UnsupportedAppUsage
    public int getModelState(UUID soundModelId) {
        if (soundModelId == null) {
            return Integer.MIN_VALUE;
        }
        try {
            return this.mSoundTriggerService.getModelState(new ParcelUuid(soundModelId));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
