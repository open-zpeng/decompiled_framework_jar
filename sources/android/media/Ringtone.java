package android.media;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.IAudioService;
import android.media.MediaPlayer;
import android.media.VolumeShaper;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.R;
import java.io.IOException;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class Ringtone {
    private static final boolean LOGD = true;
    private static final String MEDIA_SELECTION = "mime_type LIKE 'audio/%' OR mime_type IN ('application/ogg', 'application/x-flac')";
    private static final String TAG = "Ringtone";
    private static IAudioService sService;
    private final boolean mAllowRemote;
    private final AudioManager mAudioManager;
    private final Context mContext;
    @UnsupportedAppUsage
    private MediaPlayer mLocalPlayer;
    private final IRingtonePlayer mRemotePlayer;
    private final Binder mRemoteToken;
    private String mTitle;
    @UnsupportedAppUsage
    private Uri mUri;
    private VolumeShaper mVolumeShaper;
    private VolumeShaper.Configuration mVolumeShaperConfig;
    private static final String[] MEDIA_COLUMNS = {"_id", "title"};
    private static final ArrayList<Ringtone> sActiveRingtones = new ArrayList<>();
    private final MyOnCompletionListener mCompletionListener = new MyOnCompletionListener();
    private AudioAttributes mAudioAttributes = new AudioAttributes.Builder().setUsage(6).setContentType(4).build();
    private boolean mIsLooping = false;
    private float mVolume = 1.0f;
    private final Object mPlaybackSettingsLock = new Object();

    @UnsupportedAppUsage
    public Ringtone(Context context, boolean allowRemote) {
        this.mContext = context;
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        this.mAllowRemote = allowRemote;
        this.mRemotePlayer = allowRemote ? this.mAudioManager.getRingtonePlayer() : null;
        this.mRemoteToken = allowRemote ? new Binder() : null;
    }

    @Deprecated
    public void setStreamType(int streamType) {
        PlayerBase.deprecateStreamTypeForPlayback(streamType, TAG, "setStreamType()");
        setAudioAttributes(new AudioAttributes.Builder().setInternalLegacyStreamType(streamType).build());
    }

    @Deprecated
    public int getStreamType() {
        return AudioAttributes.toLegacyStreamType(this.mAudioAttributes);
    }

    public void setAudioAttributes(AudioAttributes attributes) throws IllegalArgumentException {
        if (attributes == null) {
            throw new IllegalArgumentException("Invalid null AudioAttributes for Ringtone");
        }
        this.mAudioAttributes = attributes;
        setUri(this.mUri, this.mVolumeShaperConfig);
    }

    public AudioAttributes getAudioAttributes() {
        return this.mAudioAttributes;
    }

    public void setLooping(boolean looping) {
        synchronized (this.mPlaybackSettingsLock) {
            this.mIsLooping = looping;
            applyPlaybackProperties_sync();
        }
    }

    public boolean isLooping() {
        boolean z;
        synchronized (this.mPlaybackSettingsLock) {
            z = this.mIsLooping;
        }
        return z;
    }

    public void setVolume(float volume) {
        synchronized (this.mPlaybackSettingsLock) {
            if (volume < 0.0f) {
                volume = 0.0f;
            }
            if (volume > 1.0f) {
                volume = 1.0f;
            }
            this.mVolume = volume;
            applyPlaybackProperties_sync();
        }
    }

    public float getVolume() {
        float f;
        synchronized (this.mPlaybackSettingsLock) {
            f = this.mVolume;
        }
        return f;
    }

    private void applyPlaybackProperties_sync() {
        IRingtonePlayer iRingtonePlayer;
        MediaPlayer mediaPlayer = this.mLocalPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(this.mVolume);
            this.mLocalPlayer.setLooping(this.mIsLooping);
        } else if (this.mAllowRemote && (iRingtonePlayer = this.mRemotePlayer) != null) {
            try {
                iRingtonePlayer.setPlaybackProperties(this.mRemoteToken, this.mVolume, this.mIsLooping);
            } catch (RemoteException e) {
                Log.w(TAG, "Problem setting playback properties: ", e);
            }
        } else {
            Log.w(TAG, "Neither local nor remote player available when applying playback properties");
        }
    }

    public String getTitle(Context context) {
        String str = this.mTitle;
        if (str != null) {
            return str;
        }
        String title = getTitle(context, this.mUri, true, this.mAllowRemote);
        this.mTitle = title;
        return title;
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x0092, code lost:
        if (r10 == null) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0095, code lost:
        if (r7 != null) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0097, code lost:
        r7 = r12.getLastPathSegment();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String getTitle(android.content.Context r11, android.net.Uri r12, boolean r13, boolean r14) {
        /*
            android.content.ContentResolver r6 = r11.getContentResolver()
            r7 = 0
            if (r12 == 0) goto L9c
            java.lang.String r0 = r12.getAuthority()
            java.lang.String r8 = android.content.ContentProvider.getAuthorityWithoutUserId(r0)
            java.lang.String r0 = "settings"
            boolean r0 = r0.equals(r8)
            r9 = 1
            if (r0 == 0) goto L36
            if (r13 == 0) goto L9b
        L1c:
            int r0 = android.media.RingtoneManager.getDefaultType(r12)
            android.net.Uri r0 = android.media.RingtoneManager.getActualDefaultRingtoneUri(r11, r0)
            r1 = 0
            java.lang.String r2 = getTitle(r11, r0, r1, r14)
            r3 = 17040952(0x1040638, float:2.4249033E-38)
            java.lang.Object[] r4 = new java.lang.Object[r9]
            r4[r1] = r2
            java.lang.String r7 = r11.getString(r3, r4)
            goto L9b
        L36:
            r10 = 0
            java.lang.String r0 = "media"
            boolean r0 = r0.equals(r8)     // Catch: java.lang.Throwable -> L6f java.lang.SecurityException -> L71
            if (r0 == 0) goto L68
            if (r14 == 0) goto L44
            r0 = 0
            goto L47
        L44:
            java.lang.String r0 = "mime_type LIKE 'audio/%' OR mime_type IN ('application/ogg', 'application/x-flac')"
        L47:
            r3 = r0
            java.lang.String[] r2 = android.media.Ringtone.MEDIA_COLUMNS     // Catch: java.lang.Throwable -> L6f java.lang.SecurityException -> L71
            r4 = 0
            r5 = 0
            r0 = r6
            r1 = r12
            android.database.Cursor r0 = r0.query(r1, r2, r3, r4, r5)     // Catch: java.lang.Throwable -> L6f java.lang.SecurityException -> L71
            r10 = r0
            if (r10 == 0) goto L68
            int r0 = r10.getCount()     // Catch: java.lang.Throwable -> L6f java.lang.SecurityException -> L71
            if (r0 != r9) goto L68
            r10.moveToFirst()     // Catch: java.lang.Throwable -> L6f java.lang.SecurityException -> L71
            java.lang.String r0 = r10.getString(r9)     // Catch: java.lang.Throwable -> L6f java.lang.SecurityException -> L71
            r10.close()
            r1 = 0
            return r0
        L68:
            if (r10 == 0) goto L6d
        L6a:
            r10.close()
        L6d:
            r0 = 0
            goto L95
        L6f:
            r0 = move-exception
            goto L8a
        L71:
            r0 = move-exception
            r1 = 0
            if (r14 == 0) goto L82
            java.lang.String r2 = "audio"
            java.lang.Object r2 = r11.getSystemService(r2)     // Catch: java.lang.Throwable -> L6f
            android.media.AudioManager r2 = (android.media.AudioManager) r2     // Catch: java.lang.Throwable -> L6f
            android.media.IRingtonePlayer r3 = r2.getRingtonePlayer()     // Catch: java.lang.Throwable -> L6f
            r1 = r3
        L82:
            if (r1 == 0) goto L92
            java.lang.String r2 = r1.getTitle(r12)     // Catch: java.lang.Throwable -> L6f android.os.RemoteException -> L91
            r7 = r2
            goto L92
        L8a:
            if (r10 == 0) goto L8f
            r10.close()
        L8f:
            r1 = 0
            throw r0
        L91:
            r2 = move-exception
        L92:
            if (r10 == 0) goto L6d
            goto L6a
        L95:
            if (r7 != 0) goto L9b
            java.lang.String r7 = r12.getLastPathSegment()
        L9b:
            goto La3
        L9c:
            r0 = 17040956(0x104063c, float:2.4249044E-38)
            java.lang.String r7 = r11.getString(r0)
        La3:
            if (r7 != 0) goto Lb0
            r0 = 17040957(0x104063d, float:2.4249047E-38)
            java.lang.String r7 = r11.getString(r0)
            if (r7 != 0) goto Lb0
            java.lang.String r7 = ""
        Lb0:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.Ringtone.getTitle(android.content.Context, android.net.Uri, boolean, boolean):java.lang.String");
    }

    @UnsupportedAppUsage
    public void setUri(Uri uri) {
        setUri(uri, null);
    }

    public void setUri(Uri uri, VolumeShaper.Configuration volumeShaperConfig) {
        this.mVolumeShaperConfig = volumeShaperConfig;
        destroyLocalPlayer();
        this.mUri = uri;
        if (this.mUri == null) {
            return;
        }
        this.mLocalPlayer = new MediaPlayer();
        try {
            this.mLocalPlayer.setDataSource(this.mContext, this.mUri);
            this.mLocalPlayer.setAudioAttributes(this.mAudioAttributes);
            synchronized (this.mPlaybackSettingsLock) {
                applyPlaybackProperties_sync();
            }
            if (this.mVolumeShaperConfig != null) {
                this.mVolumeShaper = this.mLocalPlayer.createVolumeShaper(this.mVolumeShaperConfig);
            }
            this.mLocalPlayer.prepare();
        } catch (IOException | SecurityException e) {
            destroyLocalPlayer();
            if (!this.mAllowRemote) {
                Log.w(TAG, "Remote playback not allowed: " + e);
            }
        }
        if (this.mLocalPlayer != null) {
            Log.d(TAG, "Successfully created local player");
        } else {
            Log.d(TAG, "Problem opening; delegating to remote player");
        }
    }

    @UnsupportedAppUsage
    public Uri getUri() {
        return this.mUri;
    }

    public void play() {
        boolean looping;
        float volume;
        if (this.mLocalPlayer != null) {
            if (this.mAudioManager.getStreamVolume(AudioAttributes.toLegacyStreamType(this.mAudioAttributes)) != 0) {
                startLocalPlayer();
            }
        } else if (this.mAllowRemote && this.mRemotePlayer != null) {
            Uri canonicalUri = this.mUri.getCanonicalUri();
            synchronized (this.mPlaybackSettingsLock) {
                looping = this.mIsLooping;
                volume = this.mVolume;
            }
            try {
                this.mRemotePlayer.playWithVolumeShaping(this.mRemoteToken, canonicalUri, this.mAudioAttributes, volume, looping, this.mVolumeShaperConfig);
            } catch (RemoteException e) {
                if (!playFallbackRingtone()) {
                    Log.w(TAG, "Problem playing ringtone: " + e);
                }
            }
        } else if (!playFallbackRingtone()) {
            Log.w(TAG, "Neither local nor remote playback available");
        }
    }

    public void stop() {
        IRingtonePlayer iRingtonePlayer;
        if (this.mLocalPlayer != null) {
            destroyLocalPlayer();
        } else if (this.mAllowRemote && (iRingtonePlayer = this.mRemotePlayer) != null) {
            try {
                iRingtonePlayer.stop(this.mRemoteToken);
            } catch (RemoteException e) {
                Log.w(TAG, "Problem stopping ringtone: " + e);
            }
        }
    }

    private void destroyLocalPlayer() {
        MediaPlayer mediaPlayer = this.mLocalPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(null);
            this.mLocalPlayer.reset();
            this.mLocalPlayer.release();
            this.mLocalPlayer = null;
            this.mVolumeShaper = null;
            synchronized (sActiveRingtones) {
                sActiveRingtones.remove(this);
            }
        }
    }

    private static IAudioService getService() {
        IAudioService iAudioService = sService;
        if (iAudioService != null) {
            return iAudioService;
        }
        IBinder b = ServiceManager.getService("audio");
        sService = IAudioService.Stub.asInterface(b);
        return sService;
    }

    private void startLocalPlayer() {
        if (this.mLocalPlayer == null) {
            return;
        }
        synchronized (sActiveRingtones) {
            sActiveRingtones.add(this);
        }
        this.mLocalPlayer.setOnCompletionListener(this.mCompletionListener);
        int sessionid = this.mLocalPlayer.getAudioSessionId();
        IAudioService service = getService();
        try {
            service.setRingtoneSessionId(getStreamType(), sessionid, this.mContext.getOpPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mLocalPlayer.start();
        VolumeShaper volumeShaper = this.mVolumeShaper;
        if (volumeShaper != null) {
            volumeShaper.apply(VolumeShaper.Operation.PLAY);
        }
    }

    public boolean isPlaying() {
        IRingtonePlayer iRingtonePlayer;
        MediaPlayer mediaPlayer = this.mLocalPlayer;
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        if (this.mAllowRemote && (iRingtonePlayer = this.mRemotePlayer) != null) {
            try {
                return iRingtonePlayer.isPlaying(this.mRemoteToken);
            } catch (RemoteException e) {
                Log.w(TAG, "Problem checking ringtone: " + e);
                return false;
            }
        }
        Log.w(TAG, "Neither local nor remote playback available");
        return false;
    }

    private boolean playFallbackRingtone() {
        if (this.mAudioManager.getStreamVolume(AudioAttributes.toLegacyStreamType(this.mAudioAttributes)) != 0) {
            int ringtoneType = RingtoneManager.getDefaultType(this.mUri);
            if (ringtoneType == -1 || RingtoneManager.getActualDefaultRingtoneUri(this.mContext, ringtoneType) != null) {
                try {
                    AssetFileDescriptor afd = this.mContext.getResources().openRawResourceFd(R.raw.fallbackring);
                    if (afd != null) {
                        this.mLocalPlayer = new MediaPlayer();
                        if (afd.getDeclaredLength() < 0) {
                            this.mLocalPlayer.setDataSource(afd.getFileDescriptor());
                        } else {
                            this.mLocalPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
                        }
                        this.mLocalPlayer.setAudioAttributes(this.mAudioAttributes);
                        synchronized (this.mPlaybackSettingsLock) {
                            applyPlaybackProperties_sync();
                        }
                        if (this.mVolumeShaperConfig != null) {
                            this.mVolumeShaper = this.mLocalPlayer.createVolumeShaper(this.mVolumeShaperConfig);
                        }
                        this.mLocalPlayer.prepare();
                        startLocalPlayer();
                        afd.close();
                        return true;
                    }
                    Log.e(TAG, "Could not load fallback ringtone");
                    return false;
                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Fallback ringtone does not exist");
                    return false;
                } catch (IOException e2) {
                    destroyLocalPlayer();
                    Log.e(TAG, "Failed to open fallback ringtone");
                    return false;
                }
            }
            Log.w(TAG, "not playing fallback for " + this.mUri);
            return false;
        }
        return false;
    }

    void setTitle(String title) {
        this.mTitle = title;
    }

    protected void finalize() {
        MediaPlayer mediaPlayer = this.mLocalPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        MyOnCompletionListener() {
        }

        @Override // android.media.MediaPlayer.OnCompletionListener
        public void onCompletion(MediaPlayer mp) {
            synchronized (Ringtone.sActiveRingtones) {
                Ringtone.sActiveRingtones.remove(Ringtone.this);
            }
            mp.setOnCompletionListener(null);
        }
    }
}
