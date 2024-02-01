package android.media;

import android.bluetooth.BluetoothDevice;
import android.car.hardware.XpVehicle.IXpVehicle;
import android.media.IAudioEventListener;
import android.media.IAudioFocusDispatcher;
import android.media.IAudioRoutesObserver;
import android.media.IAudioServerStateDispatcher;
import android.media.IPlaybackConfigDispatcher;
import android.media.IRecordingConfigDispatcher;
import android.media.IRingtonePlayer;
import android.media.IVolumeController;
import android.media.PlayerBase;
import android.media.audiopolicy.AudioPolicyConfig;
import android.media.audiopolicy.IAudioPolicyCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.xiaopeng.audio.xpAudioSessionInfo;
import com.xiaopeng.xuimanager.karaoke.IKaraoke;
import java.util.List;
/* loaded from: classes.dex */
public interface IAudioService extends IInterface {
    void ChangeChannelByTrack(int i, int i2, boolean z) throws RemoteException;

    synchronized int abandonAudioFocus(IAudioFocusDispatcher iAudioFocusDispatcher, String str, AudioAttributes audioAttributes, String str2) throws RemoteException;

    synchronized int addMixForPolicy(AudioPolicyConfig audioPolicyConfig, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    synchronized void adjustStreamVolume(int i, int i2, int i3, String str) throws RemoteException;

    synchronized void adjustSuggestedStreamVolume(int i, int i2, int i3, String str, String str2) throws RemoteException;

    void applyAlarmId(int i, int i2) throws RemoteException;

    void audioThreadProcess(int i, int i2, int i3, int i4, String str) throws RemoteException;

    synchronized void avrcpSupportsAbsoluteVolume(String str, boolean z) throws RemoteException;

    void checkAlarmVolume() throws RemoteException;

    boolean checkStreamActive(int i) throws RemoteException;

    int checkStreamCanPlay(int i) throws RemoteException;

    synchronized void disableRingtoneSync(int i) throws RemoteException;

    synchronized void disableSafeMediaVolume(String str) throws RemoteException;

    void disableSystemSound() throws RemoteException;

    synchronized int dispatchFocusChange(AudioFocusInfo audioFocusInfo, int i, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    void doZenVolumeProcess(boolean z, String str) throws RemoteException;

    void enableSystemSound() throws RemoteException;

    void flushXpCustomizeEffects(int[] iArr) throws RemoteException;

    void forceChangeToAmpChannel(int i, int i2, int i3, boolean z) throws RemoteException;

    synchronized void forceRemoteSubmixFullVolume(boolean z, IBinder iBinder) throws RemoteException;

    synchronized void forceVolumeControlStream(int i, IBinder iBinder) throws RemoteException;

    synchronized List<AudioPlaybackConfiguration> getActivePlaybackConfigurations() throws RemoteException;

    synchronized List<AudioRecordingConfiguration> getActiveRecordingConfigurations() throws RemoteException;

    List<xpAudioSessionInfo> getActiveSessionList() throws RemoteException;

    List<String> getAudioFocusPackageNameList() throws RemoteException;

    int getBanVolumeChangeMode(int i) throws RemoteException;

    int getBtCallMode() throws RemoteException;

    int getBtCallOnFlag() throws RemoteException;

    synchronized int getCurrentAudioFocus() throws RemoteException;

    AudioAttributes getCurrentAudioFocusAttributes() throws RemoteException;

    String getCurrentAudioFocusPackageName() throws RemoteException;

    int getDangerousTtsStatus() throws RemoteException;

    int getDangerousTtsVolLevel() throws RemoteException;

    synchronized int getFocusRampTimeMs(int i, AudioAttributes audioAttributes) throws RemoteException;

    synchronized int getLastAudibleStreamVolume(int i) throws RemoteException;

    String getLastAudioFocusPackageName() throws RemoteException;

    int getMainDriverMode() throws RemoteException;

    synchronized int getMode() throws RemoteException;

    boolean getNavVolDecreaseEnable() throws RemoteException;

    String getOtherMusicPlayingPkgs() throws RemoteException;

    synchronized int getRingerModeExternal() throws RemoteException;

    synchronized int getRingerModeInternal() throws RemoteException;

    synchronized IRingtonePlayer getRingtonePlayer() throws RemoteException;

    int getSoundEffectMode() throws RemoteException;

    SoundEffectParms getSoundEffectParms(int i, int i2) throws RemoteException;

    int getSoundEffectScene(int i) throws RemoteException;

    int getSoundEffectType(int i) throws RemoteException;

    SoundField getSoundField(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getStreamMaxVolume(int i) throws RemoteException;

    synchronized int getStreamMinVolume(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getStreamVolume(int i) throws RemoteException;

    synchronized int getUiSoundsStreamType() throws RemoteException;

    synchronized int getVibrateSetting(int i) throws RemoteException;

    int getVoicePosition() throws RemoteException;

    int getVoiceStatus() throws RemoteException;

    IKaraoke getXMicService() throws RemoteException;

    int getXpCustomizeEffect(int i) throws RemoteException;

    IXpVehicle getXpVehicle() throws RemoteException;

    synchronized void handleBluetoothA2dpDeviceConfigChange(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean isAnyStreamActive() throws RemoteException;

    synchronized boolean isAudioServerRunning() throws RemoteException;

    synchronized boolean isBluetoothA2dpOn() throws RemoteException;

    synchronized boolean isBluetoothScoOn() throws RemoteException;

    boolean isBtCallOn() throws RemoteException;

    boolean isBtHeadPhoneOn() throws RemoteException;

    synchronized boolean isCameraSoundForced() throws RemoteException;

    boolean isFixedVolume(int i) throws RemoteException;

    synchronized boolean isHdmiSystemAudioSupported() throws RemoteException;

    boolean isKaraokeOn() throws RemoteException;

    boolean isMainDriverOn() throws RemoteException;

    synchronized boolean isMasterMute() throws RemoteException;

    boolean isMusicLimitMode() throws RemoteException;

    boolean isOtherSessionOn() throws RemoteException;

    synchronized boolean isSpeakerphoneOn() throws RemoteException;

    boolean isSpeechSurroundOn() throws RemoteException;

    boolean isStereoAlarmOn() throws RemoteException;

    synchronized boolean isStreamAffectedByMute(int i) throws RemoteException;

    synchronized boolean isStreamAffectedByRingerMode(int i) throws RemoteException;

    synchronized boolean isStreamMute(int i) throws RemoteException;

    boolean isSystemSoundEnabled() throws RemoteException;

    boolean isUsageActive(int i) throws RemoteException;

    synchronized boolean isValidRingerMode(int i) throws RemoteException;

    boolean isZenVolume() throws RemoteException;

    synchronized boolean loadSoundEffects() throws RemoteException;

    int lockActiveStream(boolean z) throws RemoteException;

    synchronized void notifyVolumeControllerVisible(IVolumeController iVolumeController, boolean z) throws RemoteException;

    synchronized void playSoundEffect(int i) throws RemoteException;

    synchronized void playSoundEffectVolume(int i, float f) throws RemoteException;

    void playbackControl(int i, int i2) throws RemoteException;

    synchronized void playerAttributes(int i, AudioAttributes audioAttributes) throws RemoteException;

    synchronized void playerEvent(int i, int i2) throws RemoteException;

    synchronized void playerHasOpPlayAudio(int i, boolean z) throws RemoteException;

    synchronized String registerAudioPolicy(AudioPolicyConfig audioPolicyConfig, IAudioPolicyCallback iAudioPolicyCallback, boolean z, boolean z2, boolean z3) throws RemoteException;

    synchronized void registerAudioServerStateDispatcher(IAudioServerStateDispatcher iAudioServerStateDispatcher) throws RemoteException;

    void registerCallback(String str, IAudioEventListener iAudioEventListener) throws RemoteException;

    synchronized void registerPlaybackCallback(IPlaybackConfigDispatcher iPlaybackConfigDispatcher) throws RemoteException;

    synchronized void registerRecordingCallback(IRecordingConfigDispatcher iRecordingConfigDispatcher) throws RemoteException;

    synchronized void releasePlayer(int i) throws RemoteException;

    synchronized void reloadAudioSettings() throws RemoteException;

    synchronized int removeMixForPolicy(AudioPolicyConfig audioPolicyConfig, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    synchronized int requestAudioFocus(AudioAttributes audioAttributes, int i, IBinder iBinder, IAudioFocusDispatcher iAudioFocusDispatcher, String str, String str2, int i2, IAudioPolicyCallback iAudioPolicyCallback, int i3) throws RemoteException;

    void restoreMusicVolume(String str) throws RemoteException;

    int selectAlarmChannels(int i, int i2, int i3) throws RemoteException;

    void setBanVolumeChangeMode(int i, int i2, String str) throws RemoteException;

    synchronized int setBluetoothA2dpDeviceConnectionState(BluetoothDevice bluetoothDevice, int i, int i2) throws RemoteException;

    synchronized int setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(BluetoothDevice bluetoothDevice, int i, int i2, boolean z, int i3) throws RemoteException;

    synchronized void setBluetoothA2dpOn(boolean z) throws RemoteException;

    int setBluetoothHearingAidDeviceConnectionState(BluetoothDevice bluetoothDevice, int i, boolean z, int i2) throws RemoteException;

    synchronized void setBluetoothScoOn(boolean z) throws RemoteException;

    void setBtCallMode(int i) throws RemoteException;

    void setBtCallOn(boolean z) throws RemoteException;

    void setBtCallOnFlag(int i) throws RemoteException;

    void setBtHeadPhone(boolean z) throws RemoteException;

    void setDangerousTtsStatus(int i) throws RemoteException;

    void setDangerousTtsVolLevel(int i) throws RemoteException;

    boolean setFixedVolume(boolean z, int i, int i2, String str) throws RemoteException;

    synchronized int setFocusPropertiesForPolicy(int i, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    synchronized void setFocusRequestResultFromExtPolicy(AudioFocusInfo audioFocusInfo, int i, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    synchronized int setHdmiSystemAudioSupported(boolean z) throws RemoteException;

    void setKaraokeOn(boolean z) throws RemoteException;

    void setMainDriver(boolean z) throws RemoteException;

    void setMainDriverMode(int i) throws RemoteException;

    synchronized void setMasterMute(boolean z, int i, String str, int i2) throws RemoteException;

    synchronized void setMicrophoneMute(boolean z, String str, int i) throws RemoteException;

    synchronized void setMode(int i, IBinder iBinder, String str) throws RemoteException;

    void setMusicLimitMode(boolean z) throws RemoteException;

    void setNavVolDecreaseEnable(boolean z) throws RemoteException;

    synchronized void setRingerModeExternal(int i, String str) throws RemoteException;

    synchronized void setRingerModeInternal(int i, String str) throws RemoteException;

    synchronized void setRingtonePlayer(IRingtonePlayer iRingtonePlayer) throws RemoteException;

    void setRingtoneSessionId(int i, int i2, String str) throws RemoteException;

    void setSoundEffectMode(int i) throws RemoteException;

    void setSoundEffectParms(int i, int i2, int i3, int i4) throws RemoteException;

    void setSoundEffectScene(int i, int i2) throws RemoteException;

    void setSoundEffectType(int i, int i2) throws RemoteException;

    void setSoundField(int i, int i2, int i3) throws RemoteException;

    synchronized void setSpeakerphoneOn(boolean z) throws RemoteException;

    void setSpeechSurround(boolean z) throws RemoteException;

    void setStereoAlarm(boolean z) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setStreamVolume(int i, int i2, int i3, String str) throws RemoteException;

    synchronized void setVibrateSetting(int i, int i2) throws RemoteException;

    void setVoicePosition(int i, int i2, String str) throws RemoteException;

    void setVoiceStatus(int i) throws RemoteException;

    synchronized void setVolumeController(IVolumeController iVolumeController) throws RemoteException;

    void setVolumeFaded(int i, int i2, int i3, String str) throws RemoteException;

    synchronized void setVolumePolicy(VolumePolicy volumePolicy) throws RemoteException;

    void setVolumeToHal(int i) throws RemoteException;

    synchronized void setWiredDeviceConnectionState(int i, int i2, String str, String str2, String str3) throws RemoteException;

    void setXpCustomizeEffect(int i, int i2) throws RemoteException;

    synchronized boolean shouldVibrate(int i) throws RemoteException;

    void startAudioCapture(int i, int i2) throws RemoteException;

    synchronized void startBluetoothSco(IBinder iBinder, int i) throws RemoteException;

    synchronized void startBluetoothScoVirtualCall(IBinder iBinder) throws RemoteException;

    void startSpeechEffect(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    AudioRoutesInfo startWatchingRoutes(IAudioRoutesObserver iAudioRoutesObserver) throws RemoteException;

    void stopAudioCapture(int i, int i2) throws RemoteException;

    synchronized void stopBluetoothSco(IBinder iBinder) throws RemoteException;

    void stopSpeechEffect(int i) throws RemoteException;

    void temporaryChangeVolumeDown(int i, int i2, boolean z, int i3, String str) throws RemoteException;

    synchronized int trackPlayer(PlayerBase.PlayerIdCard playerIdCard) throws RemoteException;

    synchronized void unloadSoundEffects() throws RemoteException;

    synchronized void unregisterAudioFocusClient(String str) throws RemoteException;

    synchronized void unregisterAudioPolicyAsync(IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    synchronized void unregisterAudioServerStateDispatcher(IAudioServerStateDispatcher iAudioServerStateDispatcher) throws RemoteException;

    void unregisterCallback(String str, IAudioEventListener iAudioEventListener) throws RemoteException;

    synchronized void unregisterPlaybackCallback(IPlaybackConfigDispatcher iPlaybackConfigDispatcher) throws RemoteException;

    synchronized void unregisterRecordingCallback(IRecordingConfigDispatcher iRecordingConfigDispatcher) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IAudioService {
        private static final String DESCRIPTOR = "android.media.IAudioService";
        static final int TRANSACTION_ChangeChannelByTrack = 161;
        static final int TRANSACTION_abandonAudioFocus = 46;
        static final int TRANSACTION_addMixForPolicy = 75;
        static final int TRANSACTION_adjustStreamVolume = 6;
        static final int TRANSACTION_adjustSuggestedStreamVolume = 5;
        static final int TRANSACTION_applyAlarmId = 163;
        static final int TRANSACTION_audioThreadProcess = 165;
        static final int TRANSACTION_avrcpSupportsAbsoluteVolume = 38;
        static final int TRANSACTION_checkAlarmVolume = 124;
        static final int TRANSACTION_checkStreamActive = 9;
        static final int TRANSACTION_checkStreamCanPlay = 11;
        static final int TRANSACTION_disableRingtoneSync = 85;
        static final int TRANSACTION_disableSafeMediaVolume = 70;
        static final int TRANSACTION_disableSystemSound = 112;
        static final int TRANSACTION_dispatchFocusChange = 87;
        static final int TRANSACTION_doZenVolumeProcess = 137;
        static final int TRANSACTION_enableSystemSound = 111;
        static final int TRANSACTION_flushXpCustomizeEffects = 105;
        static final int TRANSACTION_forceChangeToAmpChannel = 143;
        static final int TRANSACTION_forceRemoteSubmixFullVolume = 15;
        static final int TRANSACTION_forceVolumeControlStream = 57;
        static final int TRANSACTION_getActivePlaybackConfigurations = 84;
        static final int TRANSACTION_getActiveRecordingConfigurations = 81;
        static final int TRANSACTION_getActiveSessionList = 167;
        static final int TRANSACTION_getAudioFocusPackageNameList = 164;
        static final int TRANSACTION_getBanVolumeChangeMode = 149;
        static final int TRANSACTION_getBtCallMode = 151;
        static final int TRANSACTION_getBtCallOnFlag = 156;
        static final int TRANSACTION_getCurrentAudioFocus = 48;
        static final int TRANSACTION_getCurrentAudioFocusAttributes = 49;
        static final int TRANSACTION_getCurrentAudioFocusPackageName = 50;
        static final int TRANSACTION_getDangerousTtsStatus = 158;
        static final int TRANSACTION_getDangerousTtsVolLevel = 160;
        static final int TRANSACTION_getFocusRampTimeMs = 86;
        static final int TRANSACTION_getLastAudibleStreamVolume = 21;
        static final int TRANSACTION_getLastAudioFocusPackageName = 51;
        static final int TRANSACTION_getMainDriverMode = 146;
        static final int TRANSACTION_getMode = 32;
        static final int TRANSACTION_getNavVolDecreaseEnable = 102;
        static final int TRANSACTION_getOtherMusicPlayingPkgs = 130;
        static final int TRANSACTION_getRingerModeExternal = 25;
        static final int TRANSACTION_getRingerModeInternal = 26;
        static final int TRANSACTION_getRingtonePlayer = 59;
        static final int TRANSACTION_getSoundEffectMode = 97;
        static final int TRANSACTION_getSoundEffectParms = 109;
        static final int TRANSACTION_getSoundEffectScene = 107;
        static final int TRANSACTION_getSoundEffectType = 100;
        static final int TRANSACTION_getSoundField = 96;
        static final int TRANSACTION_getStreamMaxVolume = 20;
        static final int TRANSACTION_getStreamMinVolume = 19;
        static final int TRANSACTION_getStreamVolume = 18;
        static final int TRANSACTION_getUiSoundsStreamType = 60;
        static final int TRANSACTION_getVibrateSetting = 29;
        static final int TRANSACTION_getVoicePosition = 134;
        static final int TRANSACTION_getVoiceStatus = 132;
        static final int TRANSACTION_getXMicService = 166;
        static final int TRANSACTION_getXpCustomizeEffect = 104;
        static final int TRANSACTION_getXpVehicle = 110;
        static final int TRANSACTION_handleBluetoothA2dpDeviceConfigChange = 63;
        static final int TRANSACTION_isAnyStreamActive = 10;
        static final int TRANSACTION_isAudioServerRunning = 94;
        static final int TRANSACTION_isBluetoothA2dpOn = 44;
        static final int TRANSACTION_isBluetoothScoOn = 42;
        static final int TRANSACTION_isBtCallOn = 126;
        static final int TRANSACTION_isBtHeadPhoneOn = 122;
        static final int TRANSACTION_isCameraSoundForced = 65;
        static final int TRANSACTION_isFixedVolume = 136;
        static final int TRANSACTION_isHdmiSystemAudioSupported = 72;
        static final int TRANSACTION_isKaraokeOn = 128;
        static final int TRANSACTION_isMainDriverOn = 121;
        static final int TRANSACTION_isMasterMute = 16;
        static final int TRANSACTION_isMusicLimitMode = 13;
        static final int TRANSACTION_isOtherSessionOn = 129;
        static final int TRANSACTION_isSpeakerphoneOn = 40;
        static final int TRANSACTION_isSpeechSurroundOn = 120;
        static final int TRANSACTION_isStereoAlarmOn = 119;
        static final int TRANSACTION_isStreamAffectedByMute = 69;
        static final int TRANSACTION_isStreamAffectedByRingerMode = 68;
        static final int TRANSACTION_isStreamMute = 14;
        static final int TRANSACTION_isSystemSoundEnabled = 113;
        static final int TRANSACTION_isUsageActive = 114;
        static final int TRANSACTION_isValidRingerMode = 27;
        static final int TRANSACTION_isZenVolume = 138;
        static final int TRANSACTION_loadSoundEffects = 35;
        static final int TRANSACTION_lockActiveStream = 139;
        static final int TRANSACTION_notifyVolumeControllerVisible = 67;
        static final int TRANSACTION_playSoundEffect = 33;
        static final int TRANSACTION_playSoundEffectVolume = 34;
        static final int TRANSACTION_playbackControl = 152;
        static final int TRANSACTION_playerAttributes = 2;
        static final int TRANSACTION_playerEvent = 3;
        static final int TRANSACTION_playerHasOpPlayAudio = 88;
        static final int TRANSACTION_registerAudioPolicy = 73;
        static final int TRANSACTION_registerAudioServerStateDispatcher = 92;
        static final int TRANSACTION_registerCallback = 154;
        static final int TRANSACTION_registerPlaybackCallback = 82;
        static final int TRANSACTION_registerRecordingCallback = 79;
        static final int TRANSACTION_releasePlayer = 4;
        static final int TRANSACTION_reloadAudioSettings = 37;
        static final int TRANSACTION_removeMixForPolicy = 76;
        static final int TRANSACTION_requestAudioFocus = 45;
        static final int TRANSACTION_restoreMusicVolume = 144;
        static final int TRANSACTION_selectAlarmChannels = 123;
        static final int TRANSACTION_setBanVolumeChangeMode = 148;
        static final int TRANSACTION_setBluetoothA2dpDeviceConnectionState = 62;
        static final int TRANSACTION_setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent = 90;
        static final int TRANSACTION_setBluetoothA2dpOn = 43;
        static final int TRANSACTION_setBluetoothHearingAidDeviceConnectionState = 89;
        static final int TRANSACTION_setBluetoothScoOn = 41;
        static final int TRANSACTION_setBtCallMode = 150;
        static final int TRANSACTION_setBtCallOn = 125;
        static final int TRANSACTION_setBtCallOnFlag = 153;
        static final int TRANSACTION_setBtHeadPhone = 118;
        static final int TRANSACTION_setDangerousTtsStatus = 157;
        static final int TRANSACTION_setDangerousTtsVolLevel = 159;
        static final int TRANSACTION_setFixedVolume = 135;
        static final int TRANSACTION_setFocusPropertiesForPolicy = 77;
        static final int TRANSACTION_setFocusRequestResultFromExtPolicy = 91;
        static final int TRANSACTION_setHdmiSystemAudioSupported = 71;
        static final int TRANSACTION_setKaraokeOn = 127;
        static final int TRANSACTION_setMainDriver = 117;
        static final int TRANSACTION_setMainDriverMode = 145;
        static final int TRANSACTION_setMasterMute = 17;
        static final int TRANSACTION_setMicrophoneMute = 22;
        static final int TRANSACTION_setMode = 31;
        static final int TRANSACTION_setMusicLimitMode = 12;
        static final int TRANSACTION_setNavVolDecreaseEnable = 101;
        static final int TRANSACTION_setRingerModeExternal = 23;
        static final int TRANSACTION_setRingerModeInternal = 24;
        static final int TRANSACTION_setRingtonePlayer = 58;
        static final int TRANSACTION_setRingtoneSessionId = 147;
        static final int TRANSACTION_setSoundEffectMode = 98;
        static final int TRANSACTION_setSoundEffectParms = 108;
        static final int TRANSACTION_setSoundEffectScene = 106;
        static final int TRANSACTION_setSoundEffectType = 99;
        static final int TRANSACTION_setSoundField = 95;
        static final int TRANSACTION_setSpeakerphoneOn = 39;
        static final int TRANSACTION_setSpeechSurround = 116;
        static final int TRANSACTION_setStereoAlarm = 115;
        static final int TRANSACTION_setStreamVolume = 7;
        static final int TRANSACTION_setVibrateSetting = 28;
        static final int TRANSACTION_setVoicePosition = 133;
        static final int TRANSACTION_setVoiceStatus = 131;
        static final int TRANSACTION_setVolumeController = 66;
        static final int TRANSACTION_setVolumeFaded = 140;
        static final int TRANSACTION_setVolumePolicy = 78;
        static final int TRANSACTION_setVolumeToHal = 8;
        static final int TRANSACTION_setWiredDeviceConnectionState = 61;
        static final int TRANSACTION_setXpCustomizeEffect = 103;
        static final int TRANSACTION_shouldVibrate = 30;
        static final int TRANSACTION_startAudioCapture = 52;
        static final int TRANSACTION_startBluetoothSco = 54;
        static final int TRANSACTION_startBluetoothScoVirtualCall = 55;
        static final int TRANSACTION_startSpeechEffect = 141;
        static final int TRANSACTION_startWatchingRoutes = 64;
        static final int TRANSACTION_stopAudioCapture = 53;
        static final int TRANSACTION_stopBluetoothSco = 56;
        static final int TRANSACTION_stopSpeechEffect = 142;
        static final int TRANSACTION_temporaryChangeVolumeDown = 162;
        static final int TRANSACTION_trackPlayer = 1;
        static final int TRANSACTION_unloadSoundEffects = 36;
        static final int TRANSACTION_unregisterAudioFocusClient = 47;
        static final int TRANSACTION_unregisterAudioPolicyAsync = 74;
        static final int TRANSACTION_unregisterAudioServerStateDispatcher = 93;
        static final int TRANSACTION_unregisterCallback = 155;
        static final int TRANSACTION_unregisterPlaybackCallback = 83;
        static final int TRANSACTION_unregisterRecordingCallback = 80;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IAudioService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAudioService)) {
                return (IAudioService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg2;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    PlayerBase.PlayerIdCard _arg0 = data.readInt() != 0 ? PlayerBase.PlayerIdCard.CREATOR.createFromParcel(data) : null;
                    int _result = trackPlayer(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    AudioAttributes _arg1 = data.readInt() != 0 ? AudioAttributes.CREATOR.createFromParcel(data) : null;
                    playerAttributes(_arg02, _arg1);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    int _arg12 = data.readInt();
                    playerEvent(_arg03, _arg12);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    releasePlayer(_arg04);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    int _arg13 = data.readInt();
                    int _arg22 = data.readInt();
                    String _arg3 = data.readString();
                    String _arg4 = data.readString();
                    adjustSuggestedStreamVolume(_arg05, _arg13, _arg22, _arg3, _arg4);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    int _arg14 = data.readInt();
                    int _arg23 = data.readInt();
                    String _arg32 = data.readString();
                    adjustStreamVolume(_arg06, _arg14, _arg23, _arg32);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    int _arg15 = data.readInt();
                    int _arg24 = data.readInt();
                    String _arg33 = data.readString();
                    setStreamVolume(_arg07, _arg15, _arg24, _arg33);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    setVolumeToHal(_arg08);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    boolean checkStreamActive = checkStreamActive(_arg09);
                    reply.writeNoException();
                    reply.writeInt(checkStreamActive ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isAnyStreamActive = isAnyStreamActive();
                    reply.writeNoException();
                    reply.writeInt(isAnyStreamActive ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg010 = data.readInt();
                    int _result2 = checkStreamCanPlay(_arg010);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg011 = _arg2;
                    setMusicLimitMode(_arg011);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isMusicLimitMode = isMusicLimitMode();
                    reply.writeNoException();
                    reply.writeInt(isMusicLimitMode ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    boolean isStreamMute = isStreamMute(_arg012);
                    reply.writeNoException();
                    reply.writeInt(isStreamMute ? 1 : 0);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg013 = _arg2;
                    IBinder _arg16 = data.readStrongBinder();
                    forceRemoteSubmixFullVolume(_arg013, _arg16);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isMasterMute = isMasterMute();
                    reply.writeNoException();
                    reply.writeInt(isMasterMute ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg014 = _arg2;
                    int _arg17 = data.readInt();
                    String _arg25 = data.readString();
                    int _arg34 = data.readInt();
                    setMasterMute(_arg014, _arg17, _arg25, _arg34);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg015 = data.readInt();
                    int _result3 = getStreamVolume(_arg015);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg016 = data.readInt();
                    int _result4 = getStreamMinVolume(_arg016);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg017 = data.readInt();
                    int _result5 = getStreamMaxVolume(_arg017);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg018 = data.readInt();
                    int _result6 = getLastAudibleStreamVolume(_arg018);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg019 = _arg2;
                    String _arg18 = data.readString();
                    setMicrophoneMute(_arg019, _arg18, data.readInt());
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg020 = data.readInt();
                    String _arg19 = data.readString();
                    setRingerModeExternal(_arg020, _arg19);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg021 = data.readInt();
                    String _arg110 = data.readString();
                    setRingerModeInternal(_arg021, _arg110);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    int _result7 = getRingerModeExternal();
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    int _result8 = getRingerModeInternal();
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg022 = data.readInt();
                    boolean isValidRingerMode = isValidRingerMode(_arg022);
                    reply.writeNoException();
                    reply.writeInt(isValidRingerMode ? 1 : 0);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg023 = data.readInt();
                    int _arg111 = data.readInt();
                    setVibrateSetting(_arg023, _arg111);
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg024 = data.readInt();
                    int _result9 = getVibrateSetting(_arg024);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg025 = data.readInt();
                    boolean shouldVibrate = shouldVibrate(_arg025);
                    reply.writeNoException();
                    reply.writeInt(shouldVibrate ? 1 : 0);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg026 = data.readInt();
                    IBinder _arg112 = data.readStrongBinder();
                    setMode(_arg026, _arg112, data.readString());
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    int _result10 = getMode();
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg027 = data.readInt();
                    playSoundEffect(_arg027);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg028 = data.readInt();
                    float _arg113 = data.readFloat();
                    playSoundEffectVolume(_arg028, _arg113);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    boolean loadSoundEffects = loadSoundEffects();
                    reply.writeNoException();
                    reply.writeInt(loadSoundEffects ? 1 : 0);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    unloadSoundEffects();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    reloadAudioSettings();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg029 = data.readString();
                    _arg2 = data.readInt() != 0;
                    boolean _arg114 = _arg2;
                    avrcpSupportsAbsoluteVolume(_arg029, _arg114);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg030 = _arg2;
                    setSpeakerphoneOn(_arg030);
                    reply.writeNoException();
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isSpeakerphoneOn = isSpeakerphoneOn();
                    reply.writeNoException();
                    reply.writeInt(isSpeakerphoneOn ? 1 : 0);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg031 = _arg2;
                    setBluetoothScoOn(_arg031);
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isBluetoothScoOn = isBluetoothScoOn();
                    reply.writeNoException();
                    reply.writeInt(isBluetoothScoOn ? 1 : 0);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg032 = _arg2;
                    setBluetoothA2dpOn(_arg032);
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isBluetoothA2dpOn = isBluetoothA2dpOn();
                    reply.writeNoException();
                    reply.writeInt(isBluetoothA2dpOn ? 1 : 0);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    AudioAttributes _arg033 = data.readInt() != 0 ? AudioAttributes.CREATOR.createFromParcel(data) : null;
                    int _arg115 = data.readInt();
                    IBinder _arg26 = data.readStrongBinder();
                    IAudioFocusDispatcher _arg35 = IAudioFocusDispatcher.Stub.asInterface(data.readStrongBinder());
                    String _arg42 = data.readString();
                    String _arg5 = data.readString();
                    int _arg6 = data.readInt();
                    IAudioPolicyCallback _arg7 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg8 = data.readInt();
                    int _result11 = requestAudioFocus(_arg033, _arg115, _arg26, _arg35, _arg42, _arg5, _arg6, _arg7, _arg8);
                    reply.writeNoException();
                    reply.writeInt(_result11);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    IAudioFocusDispatcher _arg034 = IAudioFocusDispatcher.Stub.asInterface(data.readStrongBinder());
                    String _arg116 = data.readString();
                    AudioAttributes _arg27 = data.readInt() != 0 ? AudioAttributes.CREATOR.createFromParcel(data) : null;
                    String _arg36 = data.readString();
                    int _result12 = abandonAudioFocus(_arg034, _arg116, _arg27, _arg36);
                    reply.writeNoException();
                    reply.writeInt(_result12);
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg035 = data.readString();
                    unregisterAudioFocusClient(_arg035);
                    reply.writeNoException();
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    int _result13 = getCurrentAudioFocus();
                    reply.writeNoException();
                    reply.writeInt(_result13);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    AudioAttributes _result14 = getCurrentAudioFocusAttributes();
                    reply.writeNoException();
                    if (_result14 != null) {
                        reply.writeInt(1);
                        _result14.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    String _result15 = getCurrentAudioFocusPackageName();
                    reply.writeNoException();
                    reply.writeString(_result15);
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    String _result16 = getLastAudioFocusPackageName();
                    reply.writeNoException();
                    reply.writeString(_result16);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg036 = data.readInt();
                    int _arg117 = data.readInt();
                    startAudioCapture(_arg036, _arg117);
                    reply.writeNoException();
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg037 = data.readInt();
                    int _arg118 = data.readInt();
                    stopAudioCapture(_arg037, _arg118);
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg038 = data.readStrongBinder();
                    int _arg119 = data.readInt();
                    startBluetoothSco(_arg038, _arg119);
                    reply.writeNoException();
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg039 = data.readStrongBinder();
                    startBluetoothScoVirtualCall(_arg039);
                    reply.writeNoException();
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg040 = data.readStrongBinder();
                    stopBluetoothSco(_arg040);
                    reply.writeNoException();
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg041 = data.readInt();
                    IBinder _arg120 = data.readStrongBinder();
                    forceVolumeControlStream(_arg041, _arg120);
                    reply.writeNoException();
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    IRingtonePlayer _arg042 = IRingtonePlayer.Stub.asInterface(data.readStrongBinder());
                    setRingtonePlayer(_arg042);
                    reply.writeNoException();
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    IRingtonePlayer _result17 = getRingtonePlayer();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result17 != null ? _result17.asBinder() : null);
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    int _result18 = getUiSoundsStreamType();
                    reply.writeNoException();
                    reply.writeInt(_result18);
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg043 = data.readInt();
                    int _arg121 = data.readInt();
                    String _arg28 = data.readString();
                    String _arg37 = data.readString();
                    String _arg43 = data.readString();
                    setWiredDeviceConnectionState(_arg043, _arg121, _arg28, _arg37, _arg43);
                    reply.writeNoException();
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothDevice _arg044 = data.readInt() != 0 ? BluetoothDevice.CREATOR.createFromParcel(data) : null;
                    int _arg122 = data.readInt();
                    int _result19 = setBluetoothA2dpDeviceConnectionState(_arg044, _arg122, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result19);
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothDevice _arg045 = data.readInt() != 0 ? BluetoothDevice.CREATOR.createFromParcel(data) : null;
                    handleBluetoothA2dpDeviceConfigChange(_arg045);
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    IAudioRoutesObserver _arg046 = IAudioRoutesObserver.Stub.asInterface(data.readStrongBinder());
                    AudioRoutesInfo _result20 = startWatchingRoutes(_arg046);
                    reply.writeNoException();
                    if (_result20 != null) {
                        reply.writeInt(1);
                        _result20.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isCameraSoundForced = isCameraSoundForced();
                    reply.writeNoException();
                    reply.writeInt(isCameraSoundForced ? 1 : 0);
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    IVolumeController _arg047 = IVolumeController.Stub.asInterface(data.readStrongBinder());
                    setVolumeController(_arg047);
                    reply.writeNoException();
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    IVolumeController _arg048 = IVolumeController.Stub.asInterface(data.readStrongBinder());
                    _arg2 = data.readInt() != 0;
                    boolean _arg123 = _arg2;
                    notifyVolumeControllerVisible(_arg048, _arg123);
                    reply.writeNoException();
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg049 = data.readInt();
                    boolean isStreamAffectedByRingerMode = isStreamAffectedByRingerMode(_arg049);
                    reply.writeNoException();
                    reply.writeInt(isStreamAffectedByRingerMode ? 1 : 0);
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg050 = data.readInt();
                    boolean isStreamAffectedByMute = isStreamAffectedByMute(_arg050);
                    reply.writeNoException();
                    reply.writeInt(isStreamAffectedByMute ? 1 : 0);
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg051 = data.readString();
                    disableSafeMediaVolume(_arg051);
                    reply.writeNoException();
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg052 = _arg2;
                    int _result21 = setHdmiSystemAudioSupported(_arg052);
                    reply.writeNoException();
                    reply.writeInt(_result21);
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isHdmiSystemAudioSupported = isHdmiSystemAudioSupported();
                    reply.writeNoException();
                    reply.writeInt(isHdmiSystemAudioSupported ? 1 : 0);
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    AudioPolicyConfig _arg053 = data.readInt() != 0 ? AudioPolicyConfig.CREATOR.createFromParcel(data) : null;
                    IAudioPolicyCallback _arg124 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                    boolean _arg29 = data.readInt() != 0;
                    boolean _arg38 = data.readInt() != 0;
                    boolean _arg44 = data.readInt() != 0;
                    String _result22 = registerAudioPolicy(_arg053, _arg124, _arg29, _arg38, _arg44);
                    reply.writeNoException();
                    reply.writeString(_result22);
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    IAudioPolicyCallback _arg054 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                    unregisterAudioPolicyAsync(_arg054);
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    AudioPolicyConfig _arg055 = data.readInt() != 0 ? AudioPolicyConfig.CREATOR.createFromParcel(data) : null;
                    IAudioPolicyCallback _arg125 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                    int _result23 = addMixForPolicy(_arg055, _arg125);
                    reply.writeNoException();
                    reply.writeInt(_result23);
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    AudioPolicyConfig _arg056 = data.readInt() != 0 ? AudioPolicyConfig.CREATOR.createFromParcel(data) : null;
                    IAudioPolicyCallback _arg126 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                    int _result24 = removeMixForPolicy(_arg056, _arg126);
                    reply.writeNoException();
                    reply.writeInt(_result24);
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg057 = data.readInt();
                    IAudioPolicyCallback _arg127 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                    int _result25 = setFocusPropertiesForPolicy(_arg057, _arg127);
                    reply.writeNoException();
                    reply.writeInt(_result25);
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    VolumePolicy _arg058 = data.readInt() != 0 ? VolumePolicy.CREATOR.createFromParcel(data) : null;
                    setVolumePolicy(_arg058);
                    reply.writeNoException();
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    IRecordingConfigDispatcher _arg059 = IRecordingConfigDispatcher.Stub.asInterface(data.readStrongBinder());
                    registerRecordingCallback(_arg059);
                    reply.writeNoException();
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    IRecordingConfigDispatcher _arg060 = IRecordingConfigDispatcher.Stub.asInterface(data.readStrongBinder());
                    unregisterRecordingCallback(_arg060);
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    List<AudioRecordingConfiguration> _result26 = getActiveRecordingConfigurations();
                    reply.writeNoException();
                    reply.writeTypedList(_result26);
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    IPlaybackConfigDispatcher _arg061 = IPlaybackConfigDispatcher.Stub.asInterface(data.readStrongBinder());
                    registerPlaybackCallback(_arg061);
                    reply.writeNoException();
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    IPlaybackConfigDispatcher _arg062 = IPlaybackConfigDispatcher.Stub.asInterface(data.readStrongBinder());
                    unregisterPlaybackCallback(_arg062);
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    List<AudioPlaybackConfiguration> _result27 = getActivePlaybackConfigurations();
                    reply.writeNoException();
                    reply.writeTypedList(_result27);
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg063 = data.readInt();
                    disableRingtoneSync(_arg063);
                    reply.writeNoException();
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg064 = data.readInt();
                    AudioAttributes _arg128 = data.readInt() != 0 ? AudioAttributes.CREATOR.createFromParcel(data) : null;
                    int _result28 = getFocusRampTimeMs(_arg064, _arg128);
                    reply.writeNoException();
                    reply.writeInt(_result28);
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    AudioFocusInfo _arg065 = data.readInt() != 0 ? AudioFocusInfo.CREATOR.createFromParcel(data) : null;
                    int _arg129 = data.readInt();
                    int _result29 = dispatchFocusChange(_arg065, _arg129, IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result29);
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg066 = data.readInt();
                    _arg2 = data.readInt() != 0;
                    boolean _arg130 = _arg2;
                    playerHasOpPlayAudio(_arg066, _arg130);
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothDevice _arg067 = data.readInt() != 0 ? BluetoothDevice.CREATOR.createFromParcel(data) : null;
                    int _arg131 = data.readInt();
                    _arg2 = data.readInt() != 0;
                    int _arg39 = data.readInt();
                    int _result30 = setBluetoothHearingAidDeviceConnectionState(_arg067, _arg131, _arg2, _arg39);
                    reply.writeNoException();
                    reply.writeInt(_result30);
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    BluetoothDevice _arg068 = data.readInt() != 0 ? BluetoothDevice.CREATOR.createFromParcel(data) : null;
                    int _arg132 = data.readInt();
                    int _arg210 = data.readInt();
                    boolean _arg310 = data.readInt() != 0;
                    int _arg45 = data.readInt();
                    int _result31 = setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(_arg068, _arg132, _arg210, _arg310, _arg45);
                    reply.writeNoException();
                    reply.writeInt(_result31);
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    AudioFocusInfo _arg069 = data.readInt() != 0 ? AudioFocusInfo.CREATOR.createFromParcel(data) : null;
                    int _arg133 = data.readInt();
                    setFocusRequestResultFromExtPolicy(_arg069, _arg133, IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    IAudioServerStateDispatcher _arg070 = IAudioServerStateDispatcher.Stub.asInterface(data.readStrongBinder());
                    registerAudioServerStateDispatcher(_arg070);
                    reply.writeNoException();
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    IAudioServerStateDispatcher _arg071 = IAudioServerStateDispatcher.Stub.asInterface(data.readStrongBinder());
                    unregisterAudioServerStateDispatcher(_arg071);
                    return true;
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isAudioServerRunning = isAudioServerRunning();
                    reply.writeNoException();
                    reply.writeInt(isAudioServerRunning ? 1 : 0);
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg072 = data.readInt();
                    int _arg134 = data.readInt();
                    setSoundField(_arg072, _arg134, data.readInt());
                    reply.writeNoException();
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg073 = data.readInt();
                    SoundField _result32 = getSoundField(_arg073);
                    reply.writeNoException();
                    if (_result32 != null) {
                        reply.writeInt(1);
                        _result32.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    int _result33 = getSoundEffectMode();
                    reply.writeNoException();
                    reply.writeInt(_result33);
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg074 = data.readInt();
                    setSoundEffectMode(_arg074);
                    reply.writeNoException();
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg075 = data.readInt();
                    int _arg135 = data.readInt();
                    setSoundEffectType(_arg075, _arg135);
                    reply.writeNoException();
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg076 = data.readInt();
                    int _result34 = getSoundEffectType(_arg076);
                    reply.writeNoException();
                    reply.writeInt(_result34);
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg077 = _arg2;
                    setNavVolDecreaseEnable(_arg077);
                    reply.writeNoException();
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    boolean navVolDecreaseEnable = getNavVolDecreaseEnable();
                    reply.writeNoException();
                    reply.writeInt(navVolDecreaseEnable ? 1 : 0);
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg078 = data.readInt();
                    int _arg136 = data.readInt();
                    setXpCustomizeEffect(_arg078, _arg136);
                    reply.writeNoException();
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg079 = data.readInt();
                    int _result35 = getXpCustomizeEffect(_arg079);
                    reply.writeNoException();
                    reply.writeInt(_result35);
                    return true;
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg080 = data.createIntArray();
                    flushXpCustomizeEffects(_arg080);
                    reply.writeNoException();
                    return true;
                case 106:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg081 = data.readInt();
                    int _arg137 = data.readInt();
                    setSoundEffectScene(_arg081, _arg137);
                    reply.writeNoException();
                    return true;
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg082 = data.readInt();
                    int _result36 = getSoundEffectScene(_arg082);
                    reply.writeNoException();
                    reply.writeInt(_result36);
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg083 = data.readInt();
                    int _arg138 = data.readInt();
                    int _arg211 = data.readInt();
                    int _arg311 = data.readInt();
                    setSoundEffectParms(_arg083, _arg138, _arg211, _arg311);
                    reply.writeNoException();
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg084 = data.readInt();
                    int _arg139 = data.readInt();
                    SoundEffectParms _result37 = getSoundEffectParms(_arg084, _arg139);
                    reply.writeNoException();
                    if (_result37 != null) {
                        reply.writeInt(1);
                        _result37.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    IXpVehicle _result38 = getXpVehicle();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result38 != null ? _result38.asBinder() : null);
                    return true;
                case 111:
                    data.enforceInterface(DESCRIPTOR);
                    enableSystemSound();
                    reply.writeNoException();
                    return true;
                case 112:
                    data.enforceInterface(DESCRIPTOR);
                    disableSystemSound();
                    reply.writeNoException();
                    return true;
                case 113:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isSystemSoundEnabled = isSystemSoundEnabled();
                    reply.writeNoException();
                    reply.writeInt(isSystemSoundEnabled ? 1 : 0);
                    return true;
                case 114:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg085 = data.readInt();
                    boolean isUsageActive = isUsageActive(_arg085);
                    reply.writeNoException();
                    reply.writeInt(isUsageActive ? 1 : 0);
                    return true;
                case 115:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg086 = _arg2;
                    setStereoAlarm(_arg086);
                    reply.writeNoException();
                    return true;
                case 116:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg087 = _arg2;
                    setSpeechSurround(_arg087);
                    reply.writeNoException();
                    return true;
                case 117:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg088 = _arg2;
                    setMainDriver(_arg088);
                    reply.writeNoException();
                    return true;
                case 118:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg089 = _arg2;
                    setBtHeadPhone(_arg089);
                    reply.writeNoException();
                    return true;
                case 119:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isStereoAlarmOn = isStereoAlarmOn();
                    reply.writeNoException();
                    reply.writeInt(isStereoAlarmOn ? 1 : 0);
                    return true;
                case 120:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isSpeechSurroundOn = isSpeechSurroundOn();
                    reply.writeNoException();
                    reply.writeInt(isSpeechSurroundOn ? 1 : 0);
                    return true;
                case 121:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isMainDriverOn = isMainDriverOn();
                    reply.writeNoException();
                    reply.writeInt(isMainDriverOn ? 1 : 0);
                    return true;
                case 122:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isBtHeadPhoneOn = isBtHeadPhoneOn();
                    reply.writeNoException();
                    reply.writeInt(isBtHeadPhoneOn ? 1 : 0);
                    return true;
                case 123:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg090 = data.readInt();
                    int _arg140 = data.readInt();
                    int _result39 = selectAlarmChannels(_arg090, _arg140, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result39);
                    return true;
                case 124:
                    data.enforceInterface(DESCRIPTOR);
                    checkAlarmVolume();
                    reply.writeNoException();
                    return true;
                case 125:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg091 = _arg2;
                    setBtCallOn(_arg091);
                    reply.writeNoException();
                    return true;
                case 126:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isBtCallOn = isBtCallOn();
                    reply.writeNoException();
                    reply.writeInt(isBtCallOn ? 1 : 0);
                    return true;
                case 127:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg092 = _arg2;
                    setKaraokeOn(_arg092);
                    reply.writeNoException();
                    return true;
                case 128:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isKaraokeOn = isKaraokeOn();
                    reply.writeNoException();
                    reply.writeInt(isKaraokeOn ? 1 : 0);
                    return true;
                case 129:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isOtherSessionOn = isOtherSessionOn();
                    reply.writeNoException();
                    reply.writeInt(isOtherSessionOn ? 1 : 0);
                    return true;
                case 130:
                    data.enforceInterface(DESCRIPTOR);
                    String _result40 = getOtherMusicPlayingPkgs();
                    reply.writeNoException();
                    reply.writeString(_result40);
                    return true;
                case 131:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg093 = data.readInt();
                    setVoiceStatus(_arg093);
                    reply.writeNoException();
                    return true;
                case 132:
                    data.enforceInterface(DESCRIPTOR);
                    int _result41 = getVoiceStatus();
                    reply.writeNoException();
                    reply.writeInt(_result41);
                    return true;
                case 133:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg094 = data.readInt();
                    int _arg141 = data.readInt();
                    setVoicePosition(_arg094, _arg141, data.readString());
                    reply.writeNoException();
                    return true;
                case 134:
                    data.enforceInterface(DESCRIPTOR);
                    int _result42 = getVoicePosition();
                    reply.writeNoException();
                    reply.writeInt(_result42);
                    return true;
                case 135:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg095 = _arg2;
                    int _arg142 = data.readInt();
                    int _arg212 = data.readInt();
                    String _arg312 = data.readString();
                    boolean fixedVolume = setFixedVolume(_arg095, _arg142, _arg212, _arg312);
                    reply.writeNoException();
                    reply.writeInt(fixedVolume ? 1 : 0);
                    return true;
                case 136:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg096 = data.readInt();
                    boolean isFixedVolume = isFixedVolume(_arg096);
                    reply.writeNoException();
                    reply.writeInt(isFixedVolume ? 1 : 0);
                    return true;
                case 137:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg097 = _arg2;
                    String _arg143 = data.readString();
                    doZenVolumeProcess(_arg097, _arg143);
                    reply.writeNoException();
                    return true;
                case 138:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isZenVolume = isZenVolume();
                    reply.writeNoException();
                    reply.writeInt(isZenVolume ? 1 : 0);
                    return true;
                case 139:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    boolean _arg098 = _arg2;
                    int _result43 = lockActiveStream(_arg098);
                    reply.writeNoException();
                    reply.writeInt(_result43);
                    return true;
                case 140:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg099 = data.readInt();
                    int _arg144 = data.readInt();
                    int _arg213 = data.readInt();
                    String _arg313 = data.readString();
                    setVolumeFaded(_arg099, _arg144, _arg213, _arg313);
                    reply.writeNoException();
                    return true;
                case 141:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0100 = data.readInt();
                    startSpeechEffect(_arg0100);
                    reply.writeNoException();
                    return true;
                case 142:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0101 = data.readInt();
                    stopSpeechEffect(_arg0101);
                    reply.writeNoException();
                    return true;
                case 143:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0102 = data.readInt();
                    int _arg145 = data.readInt();
                    int _arg214 = data.readInt();
                    _arg2 = data.readInt() != 0;
                    forceChangeToAmpChannel(_arg0102, _arg145, _arg214, _arg2);
                    reply.writeNoException();
                    return true;
                case 144:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0103 = data.readString();
                    restoreMusicVolume(_arg0103);
                    reply.writeNoException();
                    return true;
                case 145:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0104 = data.readInt();
                    setMainDriverMode(_arg0104);
                    reply.writeNoException();
                    return true;
                case 146:
                    data.enforceInterface(DESCRIPTOR);
                    int _result44 = getMainDriverMode();
                    reply.writeNoException();
                    reply.writeInt(_result44);
                    return true;
                case 147:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0105 = data.readInt();
                    int _arg146 = data.readInt();
                    setRingtoneSessionId(_arg0105, _arg146, data.readString());
                    reply.writeNoException();
                    return true;
                case 148:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0106 = data.readInt();
                    int _arg147 = data.readInt();
                    setBanVolumeChangeMode(_arg0106, _arg147, data.readString());
                    reply.writeNoException();
                    return true;
                case 149:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0107 = data.readInt();
                    int _result45 = getBanVolumeChangeMode(_arg0107);
                    reply.writeNoException();
                    reply.writeInt(_result45);
                    return true;
                case 150:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0108 = data.readInt();
                    setBtCallMode(_arg0108);
                    reply.writeNoException();
                    return true;
                case 151:
                    data.enforceInterface(DESCRIPTOR);
                    int _result46 = getBtCallMode();
                    reply.writeNoException();
                    reply.writeInt(_result46);
                    return true;
                case 152:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0109 = data.readInt();
                    int _arg148 = data.readInt();
                    playbackControl(_arg0109, _arg148);
                    reply.writeNoException();
                    return true;
                case 153:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0110 = data.readInt();
                    setBtCallOnFlag(_arg0110);
                    reply.writeNoException();
                    return true;
                case 154:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0111 = data.readString();
                    IAudioEventListener _arg149 = IAudioEventListener.Stub.asInterface(data.readStrongBinder());
                    registerCallback(_arg0111, _arg149);
                    reply.writeNoException();
                    return true;
                case 155:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0112 = data.readString();
                    IAudioEventListener _arg150 = IAudioEventListener.Stub.asInterface(data.readStrongBinder());
                    unregisterCallback(_arg0112, _arg150);
                    reply.writeNoException();
                    return true;
                case 156:
                    data.enforceInterface(DESCRIPTOR);
                    int _result47 = getBtCallOnFlag();
                    reply.writeNoException();
                    reply.writeInt(_result47);
                    return true;
                case 157:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0113 = data.readInt();
                    setDangerousTtsStatus(_arg0113);
                    reply.writeNoException();
                    return true;
                case 158:
                    data.enforceInterface(DESCRIPTOR);
                    int _result48 = getDangerousTtsStatus();
                    reply.writeNoException();
                    reply.writeInt(_result48);
                    return true;
                case 159:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0114 = data.readInt();
                    setDangerousTtsVolLevel(_arg0114);
                    reply.writeNoException();
                    return true;
                case 160:
                    data.enforceInterface(DESCRIPTOR);
                    int _result49 = getDangerousTtsVolLevel();
                    reply.writeNoException();
                    reply.writeInt(_result49);
                    return true;
                case 161:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0115 = data.readInt();
                    int _arg151 = data.readInt();
                    _arg2 = data.readInt() != 0;
                    ChangeChannelByTrack(_arg0115, _arg151, _arg2);
                    reply.writeNoException();
                    return true;
                case 162:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0116 = data.readInt();
                    int _arg152 = data.readInt();
                    boolean _arg215 = data.readInt() != 0;
                    int _arg314 = data.readInt();
                    String _arg46 = data.readString();
                    temporaryChangeVolumeDown(_arg0116, _arg152, _arg215, _arg314, _arg46);
                    reply.writeNoException();
                    return true;
                case 163:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0117 = data.readInt();
                    int _arg153 = data.readInt();
                    applyAlarmId(_arg0117, _arg153);
                    reply.writeNoException();
                    return true;
                case 164:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _result50 = getAudioFocusPackageNameList();
                    reply.writeNoException();
                    reply.writeStringList(_result50);
                    return true;
                case 165:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0118 = data.readInt();
                    int _arg154 = data.readInt();
                    int _arg216 = data.readInt();
                    int _arg315 = data.readInt();
                    String _arg47 = data.readString();
                    audioThreadProcess(_arg0118, _arg154, _arg216, _arg315, _arg47);
                    reply.writeNoException();
                    return true;
                case 166:
                    data.enforceInterface(DESCRIPTOR);
                    IKaraoke _result51 = getXMicService();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result51 != null ? _result51.asBinder() : null);
                    return true;
                case 167:
                    data.enforceInterface(DESCRIPTOR);
                    List<xpAudioSessionInfo> _result52 = getActiveSessionList();
                    reply.writeNoException();
                    reply.writeTypedList(_result52);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IAudioService {
            private IBinder mRemote;

            public private protected Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.media.IAudioService
            public synchronized int trackPlayer(PlayerBase.PlayerIdCard pic) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (pic != null) {
                        _data.writeInt(1);
                        pic.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void playerAttributes(int piid, AudioAttributes attr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piid);
                    if (attr != null) {
                        _data.writeInt(1);
                        attr.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void playerEvent(int piid, int event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piid);
                    _data.writeInt(event);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void releasePlayer(int piid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piid);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void adjustSuggestedStreamVolume(int direction, int suggestedStreamType, int flags, String callingPackage, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(direction);
                    _data.writeInt(suggestedStreamType);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    _data.writeString(caller);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void adjustStreamVolume(int streamType, int direction, int flags, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    _data.writeInt(direction);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setStreamVolume(int streamType, int index, int flags, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    _data.writeInt(index);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setVolumeToHal(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean checkStreamActive(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isAnyStreamActive() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int checkStreamCanPlay(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setMusicLimitMode(boolean mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode ? 1 : 0);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isMusicLimitMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized boolean isStreamMute(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void forceRemoteSubmixFullVolume(boolean startForcing, IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(startForcing ? 1 : 0);
                    _data.writeStrongBinder(cb);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized boolean isMasterMute() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void setMasterMute(boolean mute, int flags, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mute ? 1 : 0);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getStreamVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int getStreamMinVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getStreamMaxVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int getLastAudibleStreamVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void setMicrophoneMute(boolean on, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void setRingerModeExternal(int ringerMode, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ringerMode);
                    _data.writeString(caller);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void setRingerModeInternal(int ringerMode, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ringerMode);
                    _data.writeString(caller);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int getRingerModeExternal() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int getRingerModeInternal() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized boolean isValidRingerMode(int ringerMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ringerMode);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void setVibrateSetting(int vibrateType, int vibrateSetting) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(vibrateType);
                    _data.writeInt(vibrateSetting);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int getVibrateSetting(int vibrateType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(vibrateType);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized boolean shouldVibrate(int vibrateType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(vibrateType);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void setMode(int mode, IBinder cb, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeStrongBinder(cb);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int getMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void playSoundEffect(int effectType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(effectType);
                    this.mRemote.transact(33, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void playSoundEffectVolume(int effectType, float volume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(effectType);
                    _data.writeFloat(volume);
                    this.mRemote.transact(34, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized boolean loadSoundEffects() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void unloadSoundEffects() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(36, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void reloadAudioSettings() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void avrcpSupportsAbsoluteVolume(String address, boolean support) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(support ? 1 : 0);
                    this.mRemote.transact(38, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void setSpeakerphoneOn(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized boolean isSpeakerphoneOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void setBluetoothScoOn(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized boolean isBluetoothScoOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void setBluetoothA2dpOn(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized boolean isBluetoothA2dpOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int requestAudioFocus(AudioAttributes aa, int durationHint, IBinder cb, IAudioFocusDispatcher fd, String clientId, String callingPackageName, int flags, IAudioPolicyCallback pcb, int sdk) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(durationHint);
                    _data.writeStrongBinder(cb);
                    _data.writeStrongBinder(fd != null ? fd.asBinder() : null);
                    _data.writeString(clientId);
                    _data.writeString(callingPackageName);
                    _data.writeInt(flags);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    _data.writeInt(sdk);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int abandonAudioFocus(IAudioFocusDispatcher fd, String clientId, AudioAttributes aa, String callingPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(fd != null ? fd.asBinder() : null);
                    _data.writeString(clientId);
                    if (aa != null) {
                        _data.writeInt(1);
                        aa.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackageName);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void unregisterAudioFocusClient(String clientId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(clientId);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int getCurrentAudioFocus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public AudioAttributes getCurrentAudioFocusAttributes() throws RemoteException {
                AudioAttributes _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = AudioAttributes.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public String getCurrentAudioFocusPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public String getLastAudioFocusPackageName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void startAudioCapture(int audioSession, int usage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(audioSession);
                    _data.writeInt(usage);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void stopAudioCapture(int audioSession, int usage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(audioSession);
                    _data.writeInt(usage);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void startBluetoothSco(IBinder cb, int targetSdkVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb);
                    _data.writeInt(targetSdkVersion);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void startBluetoothScoVirtualCall(IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void stopBluetoothSco(IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void forceVolumeControlStream(int streamType, IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    _data.writeStrongBinder(cb);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void setRingtonePlayer(IRingtonePlayer player) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(player != null ? player.asBinder() : null);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized IRingtonePlayer getRingtonePlayer() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                    IRingtonePlayer _result = IRingtonePlayer.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int getUiSoundsStreamType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void setWiredDeviceConnectionState(int type, int state, String address, String name, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(state);
                    _data.writeString(address);
                    _data.writeString(name);
                    _data.writeString(caller);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int setBluetoothA2dpDeviceConnectionState(BluetoothDevice device, int state, int profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(state);
                    _data.writeInt(profile);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void handleBluetoothA2dpDeviceConfigChange(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized AudioRoutesInfo startWatchingRoutes(IAudioRoutesObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    AudioRoutesInfo _result = null;
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = AudioRoutesInfo.CREATOR.createFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized boolean isCameraSoundForced() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void setVolumeController(IVolumeController controller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(controller != null ? controller.asBinder() : null);
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void notifyVolumeControllerVisible(IVolumeController controller, boolean visible) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(controller != null ? controller.asBinder() : null);
                    _data.writeInt(visible ? 1 : 0);
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized boolean isStreamAffectedByRingerMode(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized boolean isStreamAffectedByMute(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void disableSafeMediaVolume(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int setHdmiSystemAudioSupported(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized boolean isHdmiSystemAudioSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized String registerAudioPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb, boolean hasFocusListener, boolean isFocusPolicy, boolean isVolumeController) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyConfig != null) {
                        _data.writeInt(1);
                        policyConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    _data.writeInt(hasFocusListener ? 1 : 0);
                    _data.writeInt(isFocusPolicy ? 1 : 0);
                    _data.writeInt(isVolumeController ? 1 : 0);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void unregisterAudioPolicyAsync(IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    this.mRemote.transact(74, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int addMixForPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyConfig != null) {
                        _data.writeInt(1);
                        policyConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int removeMixForPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyConfig != null) {
                        _data.writeInt(1);
                        policyConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int setFocusPropertiesForPolicy(int duckingBehavior, IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(duckingBehavior);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void setVolumePolicy(VolumePolicy policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policy != null) {
                        _data.writeInt(1);
                        policy.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void registerRecordingCallback(IRecordingConfigDispatcher rcdb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(rcdb != null ? rcdb.asBinder() : null);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void unregisterRecordingCallback(IRecordingConfigDispatcher rcdb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(rcdb != null ? rcdb.asBinder() : null);
                    this.mRemote.transact(80, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized List<AudioRecordingConfiguration> getActiveRecordingConfigurations() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                    List<AudioRecordingConfiguration> _result = _reply.createTypedArrayList(AudioRecordingConfiguration.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void registerPlaybackCallback(IPlaybackConfigDispatcher pcdb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcdb != null ? pcdb.asBinder() : null);
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void unregisterPlaybackCallback(IPlaybackConfigDispatcher pcdb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcdb != null ? pcdb.asBinder() : null);
                    this.mRemote.transact(83, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized List<AudioPlaybackConfiguration> getActivePlaybackConfigurations() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                    List<AudioPlaybackConfiguration> _result = _reply.createTypedArrayList(AudioPlaybackConfiguration.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void disableRingtoneSync(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(85, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int getFocusRampTimeMs(int focusGain, AudioAttributes attr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(focusGain);
                    if (attr != null) {
                        _data.writeInt(1);
                        attr.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(86, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int dispatchFocusChange(AudioFocusInfo afi, int focusChange, IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (afi != null) {
                        _data.writeInt(1);
                        afi.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(focusChange);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    this.mRemote.transact(87, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void playerHasOpPlayAudio(int piid, boolean hasOpPlayAudio) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piid);
                    _data.writeInt(hasOpPlayAudio ? 1 : 0);
                    this.mRemote.transact(88, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int setBluetoothHearingAidDeviceConnectionState(BluetoothDevice device, int state, boolean suppressNoisyIntent, int musicDevice) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(state);
                    _data.writeInt(suppressNoisyIntent ? 1 : 0);
                    _data.writeInt(musicDevice);
                    this.mRemote.transact(89, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized int setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(BluetoothDevice device, int state, int profile, boolean suppressNoisyIntent, int a2dpVolume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(state);
                    _data.writeInt(profile);
                    _data.writeInt(suppressNoisyIntent ? 1 : 0);
                    _data.writeInt(a2dpVolume);
                    this.mRemote.transact(90, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void setFocusRequestResultFromExtPolicy(AudioFocusInfo afi, int requestResult, IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (afi != null) {
                        _data.writeInt(1);
                        afi.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(requestResult);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    this.mRemote.transact(91, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void registerAudioServerStateDispatcher(IAudioServerStateDispatcher asd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(asd != null ? asd.asBinder() : null);
                    this.mRemote.transact(92, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized void unregisterAudioServerStateDispatcher(IAudioServerStateDispatcher asd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(asd != null ? asd.asBinder() : null);
                    this.mRemote.transact(93, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public synchronized boolean isAudioServerRunning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(94, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setSoundField(int mode, int x, int y) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeInt(x);
                    _data.writeInt(y);
                    this.mRemote.transact(95, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public SoundField getSoundField(int mode) throws RemoteException {
                SoundField _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(96, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = SoundField.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getSoundEffectMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setSoundEffectMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(98, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setSoundEffectType(int mode, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeInt(type);
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getSoundEffectType(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(100, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setNavVolDecreaseEnable(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(101, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean getNavVolDecreaseEnable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(102, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setXpCustomizeEffect(int type, int value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(value);
                    this.mRemote.transact(103, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getXpCustomizeEffect(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(104, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void flushXpCustomizeEffects(int[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(value);
                    this.mRemote.transact(105, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setSoundEffectScene(int mode, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeInt(type);
                    this.mRemote.transact(106, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getSoundEffectScene(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(107, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setSoundEffectParms(int effectType, int nativeValue, int softValue, int innervationValue) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(effectType);
                    _data.writeInt(nativeValue);
                    _data.writeInt(softValue);
                    _data.writeInt(innervationValue);
                    this.mRemote.transact(108, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public SoundEffectParms getSoundEffectParms(int effectType, int modeType) throws RemoteException {
                SoundEffectParms _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(effectType);
                    _data.writeInt(modeType);
                    this.mRemote.transact(109, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = SoundEffectParms.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public IXpVehicle getXpVehicle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(110, _data, _reply, 0);
                    _reply.readException();
                    IXpVehicle _result = IXpVehicle.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void enableSystemSound() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(111, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void disableSystemSound() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(112, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isSystemSoundEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(113, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isUsageActive(int usage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(usage);
                    this.mRemote.transact(114, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setStereoAlarm(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(115, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setSpeechSurround(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(116, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setMainDriver(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(117, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setBtHeadPhone(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(118, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isStereoAlarmOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(119, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isSpeechSurroundOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(120, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isMainDriverOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(121, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isBtHeadPhoneOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(122, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int selectAlarmChannels(int location, int fadeTimeMs, int soundid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(location);
                    _data.writeInt(fadeTimeMs);
                    _data.writeInt(soundid);
                    this.mRemote.transact(123, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void checkAlarmVolume() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(124, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setBtCallOn(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(125, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isBtCallOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(126, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setKaraokeOn(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    this.mRemote.transact(127, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isKaraokeOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(128, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isOtherSessionOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(129, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public String getOtherMusicPlayingPkgs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(130, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setVoiceStatus(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    this.mRemote.transact(131, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getVoiceStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(132, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setVoicePosition(int position, int flag, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(position);
                    _data.writeInt(flag);
                    _data.writeString(pkgName);
                    this.mRemote.transact(133, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getVoicePosition() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(134, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean setFixedVolume(boolean enable, int vol, int streamType, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeInt(vol);
                    _data.writeInt(streamType);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(135, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isFixedVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    this.mRemote.transact(136, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void doZenVolumeProcess(boolean enable, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(137, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isZenVolume() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(138, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int lockActiveStream(boolean lock) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(lock ? 1 : 0);
                    this.mRemote.transact(139, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setVolumeFaded(int StreamType, int vol, int fadetime, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(StreamType);
                    _data.writeInt(vol);
                    _data.writeInt(fadetime);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(140, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void startSpeechEffect(int audioSession) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(audioSession);
                    this.mRemote.transact(141, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void stopSpeechEffect(int audioSession) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(audioSession);
                    this.mRemote.transact(142, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void forceChangeToAmpChannel(int channelBits, int activeBits, int volume, boolean stop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(channelBits);
                    _data.writeInt(activeBits);
                    _data.writeInt(volume);
                    _data.writeInt(stop ? 1 : 0);
                    this.mRemote.transact(143, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void restoreMusicVolume(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(144, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setMainDriverMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(145, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getMainDriverMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(146, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setRingtoneSessionId(int streamType, int sessionid, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    _data.writeInt(sessionid);
                    _data.writeString(pkgName);
                    this.mRemote.transact(147, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setBanVolumeChangeMode(int streamType, int mode, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    _data.writeInt(mode);
                    _data.writeString(pkgName);
                    this.mRemote.transact(148, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getBanVolumeChangeMode(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    this.mRemote.transact(149, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setBtCallMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(150, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getBtCallMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(151, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void playbackControl(int cmd, int param) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cmd);
                    _data.writeInt(param);
                    this.mRemote.transact(152, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setBtCallOnFlag(int flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flag);
                    this.mRemote.transact(153, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void registerCallback(String pkgName, IAudioEventListener callBackFunc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeStrongBinder(callBackFunc != null ? callBackFunc.asBinder() : null);
                    this.mRemote.transact(154, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unregisterCallback(String pkgName, IAudioEventListener callBackFunc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeStrongBinder(callBackFunc != null ? callBackFunc.asBinder() : null);
                    this.mRemote.transact(155, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getBtCallOnFlag() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(156, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setDangerousTtsStatus(int on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on);
                    this.mRemote.transact(157, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getDangerousTtsStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(158, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setDangerousTtsVolLevel(int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(level);
                    this.mRemote.transact(159, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getDangerousTtsVolLevel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(160, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void ChangeChannelByTrack(int usage, int id, boolean start) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(usage);
                    _data.writeInt(id);
                    _data.writeInt(start ? 1 : 0);
                    this.mRemote.transact(161, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void temporaryChangeVolumeDown(int StreamType, int dstVol, boolean restoreVol, int flag, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(StreamType);
                    _data.writeInt(dstVol);
                    _data.writeInt(restoreVol ? 1 : 0);
                    _data.writeInt(flag);
                    _data.writeString(packageName);
                    this.mRemote.transact(162, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void applyAlarmId(int usage, int id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(usage);
                    _data.writeInt(id);
                    this.mRemote.transact(163, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public List<String> getAudioFocusPackageNameList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(164, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void audioThreadProcess(int type, int usage, int streamType, int Ppid, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(usage);
                    _data.writeInt(streamType);
                    _data.writeInt(Ppid);
                    _data.writeString(pkgName);
                    this.mRemote.transact(165, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public IKaraoke getXMicService() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(166, _data, _reply, 0);
                    _reply.readException();
                    IKaraoke _result = IKaraoke.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public List<xpAudioSessionInfo> getActiveSessionList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(167, _data, _reply, 0);
                    _reply.readException();
                    List<xpAudioSessionInfo> _result = _reply.createTypedArrayList(xpAudioSessionInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
