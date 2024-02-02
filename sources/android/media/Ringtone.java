package android.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.IAudioService;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.R;
import java.io.IOException;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class Ringtone {
    private static final boolean LOGD = true;
    private static final String MEDIA_SELECTION = "mime_type LIKE 'audio/%' OR mime_type IN ('application/ogg', 'application/x-flac')";
    private static final String TAG = "Ringtone";
    private static IAudioService sService;
    private final boolean mAllowRemote;
    private final AudioManager mAudioManager;
    private final Context mContext;
    public protected MediaPlayer mLocalPlayer;
    private final IRingtonePlayer mRemotePlayer;
    private final Binder mRemoteToken;
    private String mTitle;
    public protected Uri mUri;
    private static final String[] MEDIA_COLUMNS = {"_id", "_data", "title"};
    private static final ArrayList<Ringtone> sActiveRingtones = new ArrayList<>();
    private final MyOnCompletionListener mCompletionListener = new MyOnCompletionListener();
    private AudioAttributes mAudioAttributes = new AudioAttributes.Builder().setUsage(6).setContentType(4).build();
    private boolean mIsLooping = false;
    private float mVolume = 1.0f;
    private final Object mPlaybackSettingsLock = new Object();

    /* JADX INFO: Access modifiers changed from: private */
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
        setUri(this.mUri);
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

    private synchronized void applyPlaybackProperties_sync() {
        if (this.mLocalPlayer != null) {
            this.mLocalPlayer.setVolume(this.mVolume);
            this.mLocalPlayer.setLooping(this.mIsLooping);
        } else if (this.mAllowRemote && this.mRemotePlayer != null) {
            try {
                this.mRemotePlayer.setPlaybackProperties(this.mRemoteToken, this.mVolume, this.mIsLooping);
            } catch (RemoteException e) {
                Log.w(TAG, "Problem setting playback properties: ", e);
            }
        } else {
            Log.w(TAG, "Neither local nor remote player available when applying playback properties");
        }
    }

    public String getTitle(Context context) {
        if (this.mTitle != null) {
            return this.mTitle;
        }
        String title = getTitle(context, this.mUri, true, this.mAllowRemote);
        this.mTitle = title;
        return title;
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0069, code lost:
        if (r10 != null) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x006b, code lost:
        r10.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0093, code lost:
        if (r10 == null) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0096, code lost:
        if (r7 != null) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0098, code lost:
        r7 = r12.getLastPathSegment();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static synchronized java.lang.String getTitle(android.content.Context r11, android.net.Uri r12, boolean r13, boolean r14) {
        /*
            android.content.ContentResolver r6 = r11.getContentResolver()
            r7 = 0
            if (r12 == 0) goto L9d
            java.lang.String r0 = r12.getAuthority()
            java.lang.String r8 = android.content.ContentProvider.getAuthorityWithoutUserId(r0)
            java.lang.String r0 = "settings"
            boolean r0 = r0.equals(r8)
            r9 = 1
            if (r0 == 0) goto L36
            if (r13 == 0) goto L9c
        L1c:
            int r0 = android.media.RingtoneManager.getDefaultType(r12)
            android.net.Uri r0 = android.media.RingtoneManager.getActualDefaultRingtoneUri(r11, r0)
            r1 = 0
            java.lang.String r2 = getTitle(r11, r0, r1, r14)
            r3 = 17040795(0x104059b, float:2.4248593E-38)
            java.lang.Object[] r4 = new java.lang.Object[r9]
            r4[r1] = r2
            java.lang.String r7 = r11.getString(r3, r4)
            goto L9c
        L36:
            r0 = 0
            r10 = r0
            java.lang.String r1 = "media"
            boolean r1 = r1.equals(r8)     // Catch: java.lang.Throwable -> L70 java.lang.SecurityException -> L72
            if (r1 == 0) goto L69
            if (r14 == 0) goto L44
        L42:
            r3 = r0
            goto L47
        L44:
            java.lang.String r0 = "mime_type LIKE 'audio/%' OR mime_type IN ('application/ogg', 'application/x-flac')"
            goto L42
        L47:
            java.lang.String[] r2 = android.media.Ringtone.MEDIA_COLUMNS     // Catch: java.lang.Throwable -> L70 java.lang.SecurityException -> L72
            r4 = 0
            r5 = 0
            r0 = r6
            r1 = r12
            android.database.Cursor r0 = r0.query(r1, r2, r3, r4, r5)     // Catch: java.lang.Throwable -> L70 java.lang.SecurityException -> L72
            r10 = r0
            if (r10 == 0) goto L69
            int r0 = r10.getCount()     // Catch: java.lang.Throwable -> L70 java.lang.SecurityException -> L72
            if (r0 != r9) goto L69
            r10.moveToFirst()     // Catch: java.lang.Throwable -> L70 java.lang.SecurityException -> L72
            r0 = 2
            java.lang.String r0 = r10.getString(r0)     // Catch: java.lang.Throwable -> L70 java.lang.SecurityException -> L72
            if (r10 == 0) goto L67
            r10.close()
        L67:
            r1 = 0
            return r0
        L69:
            if (r10 == 0) goto L6e
        L6b:
            r10.close()
        L6e:
            r0 = 0
            goto L96
        L70:
            r0 = move-exception
            goto L8b
        L72:
            r0 = move-exception
            r1 = 0
            if (r14 == 0) goto L83
            java.lang.String r2 = "audio"
            java.lang.Object r2 = r11.getSystemService(r2)     // Catch: java.lang.Throwable -> L70
            android.media.AudioManager r2 = (android.media.AudioManager) r2     // Catch: java.lang.Throwable -> L70
            android.media.IRingtonePlayer r3 = r2.getRingtonePlayer()     // Catch: java.lang.Throwable -> L70
            r1 = r3
        L83:
            if (r1 == 0) goto L93
            java.lang.String r2 = r1.getTitle(r12)     // Catch: java.lang.Throwable -> L70 android.os.RemoteException -> L92
            r7 = r2
            goto L93
        L8b:
            if (r10 == 0) goto L90
            r10.close()
        L90:
            r1 = 0
            throw r0
        L92:
            r2 = move-exception
        L93:
            if (r10 == 0) goto L6e
            goto L6b
        L96:
            if (r7 != 0) goto L9c
            java.lang.String r7 = r12.getLastPathSegment()
        L9c:
            goto La4
        L9d:
            r0 = 17040799(0x104059f, float:2.4248604E-38)
            java.lang.String r7 = r11.getString(r0)
        La4:
            if (r7 != 0) goto Lb1
            r0 = 17040800(0x10405a0, float:2.4248607E-38)
            java.lang.String r7 = r11.getString(r0)
            if (r7 != 0) goto Lb1
            java.lang.String r7 = ""
        Lb1:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.Ringtone.getTitle(android.content.Context, android.net.Uri, boolean, boolean):java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUri(Uri uri) {
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

    private protected Uri getUri() {
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
                this.mRemotePlayer.play(this.mRemoteToken, canonicalUri, this.mAudioAttributes, volume, looping);
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
        if (this.mLocalPlayer != null) {
            destroyLocalPlayer();
        } else if (this.mAllowRemote && this.mRemotePlayer != null) {
            try {
                this.mRemotePlayer.stop(this.mRemoteToken);
            } catch (RemoteException e) {
                Log.w(TAG, "Problem stopping ringtone: " + e);
            }
        }
    }

    private synchronized void destroyLocalPlayer() {
        if (this.mLocalPlayer != null) {
            this.mLocalPlayer.setOnCompletionListener(null);
            this.mLocalPlayer.reset();
            this.mLocalPlayer.release();
            this.mLocalPlayer = null;
            synchronized (sActiveRingtones) {
                sActiveRingtones.remove(this);
            }
        }
    }

    private static IAudioService getService() {
        if (sService != null) {
            return sService;
        }
        IBinder b = ServiceManager.getService("audio");
        sService = IAudioService.Stub.asInterface(b);
        return sService;
    }

    private synchronized void startLocalPlayer() {
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
    }

    public boolean isPlaying() {
        if (this.mLocalPlayer != null) {
            return this.mLocalPlayer.isPlaying();
        }
        if (this.mAllowRemote && this.mRemotePlayer != null) {
            try {
                return this.mRemotePlayer.isPlaying(this.mRemoteToken);
            } catch (RemoteException e) {
                Log.w(TAG, "Problem checking ringtone: " + e);
                return false;
            }
        }
        Log.w(TAG, "Neither local nor remote playback available");
        return false;
    }

    private synchronized boolean playFallbackRingtone() {
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

    synchronized void setTitle(String title) {
        this.mTitle = title;
    }

    protected void finalize() {
        if (this.mLocalPlayer != null) {
            this.mLocalPlayer.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
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
