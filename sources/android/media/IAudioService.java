package android.media;

import android.annotation.UnsupportedAppUsage;
import android.bluetooth.BluetoothDevice;
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
import android.media.audiopolicy.AudioProductStrategy;
import android.media.audiopolicy.AudioVolumeGroup;
import android.media.audiopolicy.IAudioPolicyCallback;
import android.media.projection.IMediaProjection;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.xiaopeng.audio.xpAudioSessionInfo;
import com.xiaopeng.audio.xuiKaraoke.IKaraoke;
import java.util.List;

/* loaded from: classes2.dex */
public interface IAudioService extends IInterface {
    void ChangeChannelByTrack(int i, int i2, boolean z) throws RemoteException;

    int abandonAudioFocus(IAudioFocusDispatcher iAudioFocusDispatcher, String str, AudioAttributes audioAttributes, String str2) throws RemoteException;

    int addMixForPolicy(AudioPolicyConfig audioPolicyConfig, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    void adjustStreamVolume(int i, int i2, int i3, String str) throws RemoteException;

    void adjustSuggestedStreamVolume(int i, int i2, int i3, String str, String str2) throws RemoteException;

    void applyAlarmId(int i, int i2) throws RemoteException;

    void audioThreadProcess(int i, int i2, int i3, int i4, String str) throws RemoteException;

    void avrcpSupportsAbsoluteVolume(String str, boolean z) throws RemoteException;

    void changeAudioFocusPosition(String str, int i) throws RemoteException;

    void checkAlarmVolume() throws RemoteException;

    boolean checkPlayingRouteByPackage(int i, String str) throws RemoteException;

    boolean checkStreamActive(int i) throws RemoteException;

    int checkStreamCanPlay(int i) throws RemoteException;

    void disableRingtoneSync(int i) throws RemoteException;

    void disableSafeMediaVolume(String str) throws RemoteException;

    void disableSystemSound() throws RemoteException;

    int dispatchFocusChange(AudioFocusInfo audioFocusInfo, int i, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    void doZenVolumeProcess(boolean z, String str) throws RemoteException;

    void enableSystemSound() throws RemoteException;

    void flushXpCustomizeEffects(int[] iArr) throws RemoteException;

    void forceChangeToAmpChannel(int i, int i2, int i3, boolean z) throws RemoteException;

    void forceRemoteSubmixFullVolume(boolean z, IBinder iBinder) throws RemoteException;

    void forceVolumeControlStream(int i, IBinder iBinder) throws RemoteException;

    List<AudioPlaybackConfiguration> getActivePlaybackConfigurations() throws RemoteException;

    List<AudioRecordingConfiguration> getActiveRecordingConfigurations() throws RemoteException;

    List<xpAudioSessionInfo> getActiveSessionList() throws RemoteException;

    int getAllowedCapturePolicy() throws RemoteException;

    List<String> getAudioFocusPackageNameList() throws RemoteException;

    List<String> getAudioFocusPackageNameListByPosition(int i) throws RemoteException;

    List<AudioProductStrategy> getAudioProductStrategies() throws RemoteException;

    List<AudioVolumeGroup> getAudioVolumeGroups() throws RemoteException;

    int getBanVolumeChangeMode(int i) throws RemoteException;

    int getBtCallMode() throws RemoteException;

    int getBtCallOnFlag() throws RemoteException;

    int getCurrentAudioFocus() throws RemoteException;

    AudioAttributes getCurrentAudioFocusAttributes() throws RemoteException;

    String getCurrentAudioFocusPackageName() throws RemoteException;

    int getDangerousTtsStatus() throws RemoteException;

    int getDangerousTtsVolLevel() throws RemoteException;

    int getDyn3dEffectLevel() throws RemoteException;

    int getFocusRampTimeMs(int i, AudioAttributes audioAttributes) throws RemoteException;

    int getLastAudibleStreamVolume(int i) throws RemoteException;

    String getLastAudioFocusPackageName() throws RemoteException;

    int getMainDriverMode() throws RemoteException;

    int getMaxVolumeIndexForAttributes(AudioAttributes audioAttributes) throws RemoteException;

    int getMinVolumeIndexForAttributes(AudioAttributes audioAttributes) throws RemoteException;

    boolean getMmapToAvasEnable() throws RemoteException;

    int getMode() throws RemoteException;

    int getMusicSeatEffect() throws RemoteException;

    boolean getMusicSeatEnable() throws RemoteException;

    boolean getNavVolDecreaseEnable() throws RemoteException;

    String getOtherMusicPlayingPkgs() throws RemoteException;

    int getRingerModeExternal() throws RemoteException;

    int getRingerModeInternal() throws RemoteException;

    IRingtonePlayer getRingtonePlayer() throws RemoteException;

    int getSoundEffectMode() throws RemoteException;

    SoundEffectParms getSoundEffectParms(int i, int i2) throws RemoteException;

    int getSoundEffectScene(int i) throws RemoteException;

    int getSoundEffectType(int i) throws RemoteException;

    SoundField getSoundField(int i) throws RemoteException;

    boolean getSoundPositionEnable() throws RemoteException;

    int getSoundSpeedLinkLevel() throws RemoteException;

    @UnsupportedAppUsage
    int getStreamMaxVolume(int i) throws RemoteException;

    int getStreamMinVolume(int i) throws RemoteException;

    @UnsupportedAppUsage
    int getStreamVolume(int i) throws RemoteException;

    int getUiSoundsStreamType() throws RemoteException;

    int getVibrateSetting(int i) throws RemoteException;

    int getVoicePosition() throws RemoteException;

    int getVoiceStatus() throws RemoteException;

    int getVolumeIndexForAttributes(AudioAttributes audioAttributes) throws RemoteException;

    IKaraoke getXMicService() throws RemoteException;

    int getXpCustomizeEffect(int i) throws RemoteException;

    void handleBluetoothA2dpDeviceConfigChange(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean hasHapticChannels(Uri uri) throws RemoteException;

    boolean hasRegisteredDynamicPolicy() throws RemoteException;

    boolean isAnyStreamActive() throws RemoteException;

    boolean isAudioServerRunning() throws RemoteException;

    boolean isBluetoothA2dpOn() throws RemoteException;

    boolean isBluetoothScoOn() throws RemoteException;

    boolean isBtCallOn() throws RemoteException;

    boolean isBtHeadPhoneOn() throws RemoteException;

    boolean isCameraSoundForced() throws RemoteException;

    boolean isFixedVolume(int i) throws RemoteException;

    boolean isHdmiSystemAudioSupported() throws RemoteException;

    boolean isKaraokeOn() throws RemoteException;

    boolean isMainDriverOn() throws RemoteException;

    boolean isMasterMute() throws RemoteException;

    boolean isMusicLimitMode() throws RemoteException;

    boolean isOtherSessionOn() throws RemoteException;

    boolean isSpeakerphoneOn() throws RemoteException;

    boolean isSpeechSurroundOn() throws RemoteException;

    boolean isStereoAlarmOn() throws RemoteException;

    boolean isStreamAffectedByMute(int i) throws RemoteException;

    boolean isStreamAffectedByRingerMode(int i) throws RemoteException;

    boolean isStreamMute(int i) throws RemoteException;

    boolean isSystemSoundEnabled() throws RemoteException;

    boolean isUsageActive(int i) throws RemoteException;

    boolean isValidRingerMode(int i) throws RemoteException;

    boolean isZenVolume() throws RemoteException;

    boolean loadSoundEffects() throws RemoteException;

    int lockActiveStream(boolean z) throws RemoteException;

    void notifyVolumeControllerVisible(IVolumeController iVolumeController, boolean z) throws RemoteException;

    void playAvasSound(int i, String str) throws RemoteException;

    void playSoundEffect(int i) throws RemoteException;

    void playSoundEffectVolume(int i, float f) throws RemoteException;

    void playbackControl(int i, int i2) throws RemoteException;

    void playerAttributes(int i, AudioAttributes audioAttributes) throws RemoteException;

    void playerEvent(int i, int i2) throws RemoteException;

    void playerHasOpPlayAudio(int i, boolean z) throws RemoteException;

    void recorderEvent(int i, int i2) throws RemoteException;

    String registerAudioPolicy(AudioPolicyConfig audioPolicyConfig, IAudioPolicyCallback iAudioPolicyCallback, boolean z, boolean z2, boolean z3, boolean z4, IMediaProjection iMediaProjection) throws RemoteException;

    void registerAudioServerStateDispatcher(IAudioServerStateDispatcher iAudioServerStateDispatcher) throws RemoteException;

    void registerCallback(String str, IAudioEventListener iAudioEventListener) throws RemoteException;

    void registerPlaybackCallback(IPlaybackConfigDispatcher iPlaybackConfigDispatcher) throws RemoteException;

    void registerRecordingCallback(IRecordingConfigDispatcher iRecordingConfigDispatcher) throws RemoteException;

    void releasePlayer(int i) throws RemoteException;

    void releaseRecorder(int i) throws RemoteException;

    void reloadAudioSettings() throws RemoteException;

    int removeMixForPolicy(AudioPolicyConfig audioPolicyConfig, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    int removeUidDeviceAffinity(IAudioPolicyCallback iAudioPolicyCallback, int i) throws RemoteException;

    int requestAudioFocus(AudioAttributes audioAttributes, int i, IBinder iBinder, IAudioFocusDispatcher iAudioFocusDispatcher, String str, String str2, int i2, IAudioPolicyCallback iAudioPolicyCallback, int i3) throws RemoteException;

    int requestAudioFocusPosition(AudioAttributes audioAttributes, int i, IBinder iBinder, IAudioFocusDispatcher iAudioFocusDispatcher, String str, int i2, int i3, IAudioPolicyCallback iAudioPolicyCallback, int i4) throws RemoteException;

    void restoreMusicVolume(String str) throws RemoteException;

    int selectAlarmChannels(int i, int i2, int i3) throws RemoteException;

    int setAllowedCapturePolicy(int i) throws RemoteException;

    void setAudioPathWhiteList(int i, String str) throws RemoteException;

    void setBanVolumeChangeMode(int i, int i2, String str) throws RemoteException;

    void setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(BluetoothDevice bluetoothDevice, int i, int i2, boolean z, int i3) throws RemoteException;

    void setBluetoothA2dpOn(boolean z) throws RemoteException;

    void setBluetoothHearingAidDeviceConnectionState(BluetoothDevice bluetoothDevice, int i, boolean z, int i2) throws RemoteException;

    void setBluetoothScoOn(boolean z) throws RemoteException;

    void setBtCallMode(int i) throws RemoteException;

    void setBtCallOn(boolean z) throws RemoteException;

    void setBtCallOnFlag(int i) throws RemoteException;

    void setBtHeadPhone(boolean z) throws RemoteException;

    void setDangerousTtsStatus(int i) throws RemoteException;

    void setDangerousTtsVolLevel(int i) throws RemoteException;

    void setDyn3dEffectLevel(int i) throws RemoteException;

    boolean setFixedVolume(boolean z, int i, int i2, String str) throws RemoteException;

    int setFocusPropertiesForPolicy(int i, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    void setFocusRequestResultFromExtPolicy(AudioFocusInfo audioFocusInfo, int i, IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    int setHdmiSystemAudioSupported(boolean z) throws RemoteException;

    void setKaraokeOn(boolean z) throws RemoteException;

    void setMainDriver(boolean z) throws RemoteException;

    void setMainDriverMode(int i) throws RemoteException;

    void setMassageSeatLevel(List<String> list) throws RemoteException;

    void setMasterMute(boolean z, int i, String str, int i2) throws RemoteException;

    void setMicrophoneMute(boolean z, String str, int i) throws RemoteException;

    void setMmapToAvasEnable(boolean z) throws RemoteException;

    void setMode(int i, IBinder iBinder, String str) throws RemoteException;

    void setMusicLimitMode(boolean z) throws RemoteException;

    void setMusicSeatEffect(int i) throws RemoteException;

    void setMusicSeatEnable(boolean z) throws RemoteException;

    void setMusicSeatRythmPause(boolean z) throws RemoteException;

    void setNavVolDecreaseEnable(boolean z) throws RemoteException;

    void setNetEcallEnable(boolean z) throws RemoteException;

    void setRingerModeExternal(int i, String str) throws RemoteException;

    void setRingerModeInternal(int i, String str) throws RemoteException;

    void setRingtonePlayer(IRingtonePlayer iRingtonePlayer) throws RemoteException;

    void setRingtoneSessionId(int i, int i2, String str) throws RemoteException;

    void setSessionIdStatus(int i, int i2, int i3) throws RemoteException;

    void setSoftTypeVolumeMute(int i, boolean z) throws RemoteException;

    void setSoundEffectMode(int i) throws RemoteException;

    void setSoundEffectParms(int i, int i2, int i3, int i4) throws RemoteException;

    void setSoundEffectScene(int i, int i2) throws RemoteException;

    void setSoundEffectType(int i, int i2) throws RemoteException;

    void setSoundField(int i, int i2, int i3) throws RemoteException;

    void setSoundPositionEnable(boolean z) throws RemoteException;

    void setSoundSpeedLinkLevel(int i) throws RemoteException;

    void setSpeakerphoneOn(boolean z) throws RemoteException;

    void setSpecialOutputId(int i, int i2, boolean z) throws RemoteException;

    void setSpeechSurround(boolean z) throws RemoteException;

    void setStereoAlarm(boolean z) throws RemoteException;

    void setStreamPosition(int i, String str, int i2, int i3) throws RemoteException;

    @UnsupportedAppUsage
    void setStreamVolume(int i, int i2, int i3, String str) throws RemoteException;

    int setUidDeviceAffinity(IAudioPolicyCallback iAudioPolicyCallback, int i, int[] iArr, String[] strArr) throws RemoteException;

    void setVibrateSetting(int i, int i2) throws RemoteException;

    void setVoicePosition(int i, int i2, String str) throws RemoteException;

    void setVoiceStatus(int i) throws RemoteException;

    void setVolumeController(IVolumeController iVolumeController) throws RemoteException;

    void setVolumeFaded(int i, int i2, int i3, String str) throws RemoteException;

    void setVolumeIndexForAttributes(AudioAttributes audioAttributes, int i, int i2, String str) throws RemoteException;

    void setVolumePolicy(VolumePolicy volumePolicy) throws RemoteException;

    void setVolumeToHal(int i) throws RemoteException;

    void setWiredDeviceConnectionState(int i, int i2, String str, String str2, String str3) throws RemoteException;

    void setXpCustomizeEffect(int i, int i2) throws RemoteException;

    boolean shouldVibrate(int i) throws RemoteException;

    void startAudioCapture(int i, int i2) throws RemoteException;

    void startBluetoothSco(IBinder iBinder, int i) throws RemoteException;

    void startBluetoothScoVirtualCall(IBinder iBinder) throws RemoteException;

    void startSpeechEffect(int i) throws RemoteException;

    @UnsupportedAppUsage
    AudioRoutesInfo startWatchingRoutes(IAudioRoutesObserver iAudioRoutesObserver) throws RemoteException;

    void stopAudioCapture(int i, int i2) throws RemoteException;

    void stopAvasSound(String str) throws RemoteException;

    void stopBluetoothSco(IBinder iBinder) throws RemoteException;

    void stopSpeechEffect(int i) throws RemoteException;

    void temporaryChangeVolumeDown(int i, int i2, boolean z, int i3, String str) throws RemoteException;

    int trackPlayer(PlayerBase.PlayerIdCard playerIdCard) throws RemoteException;

    int trackRecorder(IBinder iBinder) throws RemoteException;

    void unloadSoundEffects() throws RemoteException;

    void unregisterAudioFocusClient(String str) throws RemoteException;

    void unregisterAudioPolicy(IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    void unregisterAudioPolicyAsync(IAudioPolicyCallback iAudioPolicyCallback) throws RemoteException;

    void unregisterAudioServerStateDispatcher(IAudioServerStateDispatcher iAudioServerStateDispatcher) throws RemoteException;

    void unregisterCallback(String str, IAudioEventListener iAudioEventListener) throws RemoteException;

    void unregisterPlaybackCallback(IPlaybackConfigDispatcher iPlaybackConfigDispatcher) throws RemoteException;

    void unregisterRecordingCallback(IRecordingConfigDispatcher iRecordingConfigDispatcher) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IAudioService {
        @Override // android.media.IAudioService
        public int trackPlayer(PlayerBase.PlayerIdCard pic) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void playerAttributes(int piid, AudioAttributes attr) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void playerEvent(int piid, int event) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void releasePlayer(int piid) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int trackRecorder(IBinder recorder) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void recorderEvent(int riid, int event) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void releaseRecorder(int riid) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void adjustSuggestedStreamVolume(int direction, int suggestedStreamType, int flags, String callingPackage, String caller) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void adjustStreamVolume(int streamType, int direction, int flags, String callingPackage) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setStreamVolume(int streamType, int index, int flags, String callingPackage) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setVolumeToHal(int streamType) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean checkStreamActive(int streamType) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public boolean isAnyStreamActive() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public int checkStreamCanPlay(int streamType) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setMusicLimitMode(boolean mode) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isMusicLimitMode() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public boolean isStreamMute(int streamType) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void forceRemoteSubmixFullVolume(boolean startForcing, IBinder cb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isMasterMute() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setMasterMute(boolean mute, int flags, String callingPackage, int userId) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getStreamVolume(int streamType) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int getStreamMinVolume(int streamType) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int getStreamMaxVolume(int streamType) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public List<AudioVolumeGroup> getAudioVolumeGroups() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public void setVolumeIndexForAttributes(AudioAttributes aa, int index, int flags, String callingPackage) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int getMaxVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int getMinVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int getLastAudibleStreamVolume(int streamType) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public List<AudioProductStrategy> getAudioProductStrategies() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public void setMicrophoneMute(boolean on, String callingPackage, int userId) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setRingerModeExternal(int ringerMode, String caller) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setRingerModeInternal(int ringerMode, String caller) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getRingerModeExternal() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int getRingerModeInternal() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public boolean isValidRingerMode(int ringerMode) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setVibrateSetting(int vibrateType, int vibrateSetting) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getVibrateSetting(int vibrateType) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public boolean shouldVibrate(int vibrateType) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setMode(int mode, IBinder cb, String callingPackage) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getMode() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void playSoundEffect(int effectType) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void playSoundEffectVolume(int effectType, float volume) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean loadSoundEffects() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void unloadSoundEffects() throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void reloadAudioSettings() throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void avrcpSupportsAbsoluteVolume(String address, boolean support) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setSpeakerphoneOn(boolean on) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isSpeakerphoneOn() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setBluetoothScoOn(boolean on) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isBluetoothScoOn() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setBluetoothA2dpOn(boolean on) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isBluetoothA2dpOn() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public int requestAudioFocus(AudioAttributes aa, int durationHint, IBinder cb, IAudioFocusDispatcher fd, String clientId, String callingPackageName, int flags, IAudioPolicyCallback pcb, int sdk) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int abandonAudioFocus(IAudioFocusDispatcher fd, String clientId, AudioAttributes aa, String callingPackageName) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void unregisterAudioFocusClient(String clientId) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getCurrentAudioFocus() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public AudioAttributes getCurrentAudioFocusAttributes() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public String getCurrentAudioFocusPackageName() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public String getLastAudioFocusPackageName() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public void startAudioCapture(int audioSession, int usage) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void stopAudioCapture(int audioSession, int usage) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void startBluetoothSco(IBinder cb, int targetSdkVersion) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void startBluetoothScoVirtualCall(IBinder cb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void stopBluetoothSco(IBinder cb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void forceVolumeControlStream(int streamType, IBinder cb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setRingtonePlayer(IRingtonePlayer player) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public IRingtonePlayer getRingtonePlayer() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public int getUiSoundsStreamType() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setWiredDeviceConnectionState(int type, int state, String address, String name, String caller) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void handleBluetoothA2dpDeviceConfigChange(BluetoothDevice device) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public AudioRoutesInfo startWatchingRoutes(IAudioRoutesObserver observer) throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public boolean isCameraSoundForced() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setVolumeController(IVolumeController controller) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void notifyVolumeControllerVisible(IVolumeController controller, boolean visible) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isStreamAffectedByRingerMode(int streamType) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public boolean isStreamAffectedByMute(int streamType) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void disableSafeMediaVolume(String callingPackage) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int setHdmiSystemAudioSupported(boolean on) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public boolean isHdmiSystemAudioSupported() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public String registerAudioPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb, boolean hasFocusListener, boolean isFocusPolicy, boolean isTestFocusPolicy, boolean isVolumeController, IMediaProjection projection) throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public void unregisterAudioPolicyAsync(IAudioPolicyCallback pcb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void unregisterAudioPolicy(IAudioPolicyCallback pcb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int addMixForPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int removeMixForPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int setFocusPropertiesForPolicy(int duckingBehavior, IAudioPolicyCallback pcb) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setVolumePolicy(VolumePolicy policy) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean hasRegisteredDynamicPolicy() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void registerRecordingCallback(IRecordingConfigDispatcher rcdb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void unregisterRecordingCallback(IRecordingConfigDispatcher rcdb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public List<AudioRecordingConfiguration> getActiveRecordingConfigurations() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public void registerPlaybackCallback(IPlaybackConfigDispatcher pcdb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void unregisterPlaybackCallback(IPlaybackConfigDispatcher pcdb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public List<AudioPlaybackConfiguration> getActivePlaybackConfigurations() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public void disableRingtoneSync(int userId) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getFocusRampTimeMs(int focusGain, AudioAttributes attr) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int dispatchFocusChange(AudioFocusInfo afi, int focusChange, IAudioPolicyCallback pcb) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void playerHasOpPlayAudio(int piid, boolean hasOpPlayAudio) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setBluetoothHearingAidDeviceConnectionState(BluetoothDevice device, int state, boolean suppressNoisyIntent, int musicDevice) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(BluetoothDevice device, int state, int profile, boolean suppressNoisyIntent, int a2dpVolume) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setFocusRequestResultFromExtPolicy(AudioFocusInfo afi, int requestResult, IAudioPolicyCallback pcb) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void registerAudioServerStateDispatcher(IAudioServerStateDispatcher asd) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void unregisterAudioServerStateDispatcher(IAudioServerStateDispatcher asd) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isAudioServerRunning() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public int setUidDeviceAffinity(IAudioPolicyCallback pcb, int uid, int[] deviceTypes, String[] deviceAddresses) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int removeUidDeviceAffinity(IAudioPolicyCallback pcb, int uid) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public boolean hasHapticChannels(Uri uri) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public int setAllowedCapturePolicy(int capturePolicy) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public int getAllowedCapturePolicy() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setSoundField(int mode, int x, int y) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public SoundField getSoundField(int mode) throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public int getSoundEffectMode() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setSoundEffectMode(int mode) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setSoundEffectType(int mode, int type) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getSoundEffectType(int mode) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setSoundEffectScene(int mode, int type) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getSoundEffectScene(int mode) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setSoundEffectParms(int effectType, int nativeValue, int softValue, int innervationValue) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public SoundEffectParms getSoundEffectParms(int effectType, int modeType) throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public void setSoundSpeedLinkLevel(int level) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getSoundSpeedLinkLevel() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setDyn3dEffectLevel(int level) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getDyn3dEffectLevel() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setNavVolDecreaseEnable(boolean enable) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean getNavVolDecreaseEnable() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setXpCustomizeEffect(int type, int value) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getXpCustomizeEffect(int type) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void flushXpCustomizeEffects(int[] values) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void enableSystemSound() throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void disableSystemSound() throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isSystemSoundEnabled() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public boolean isUsageActive(int usage) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setStereoAlarm(boolean enable) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setSpeechSurround(boolean enable) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setMainDriver(boolean enable) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setBtHeadPhone(boolean enable) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isStereoAlarmOn() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public boolean isSpeechSurroundOn() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public boolean isMainDriverOn() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public boolean isBtHeadPhoneOn() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public int selectAlarmChannels(int location, int fadeTimeMs, int soundid) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void checkAlarmVolume() throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setBtCallOn(boolean enable) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isBtCallOn() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setKaraokeOn(boolean on) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isKaraokeOn() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public boolean isOtherSessionOn() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public String getOtherMusicPlayingPkgs() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public void setVoiceStatus(int status) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getVoiceStatus() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setVoicePosition(int position, int flag, String pkgName) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getVoicePosition() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public boolean setFixedVolume(boolean enable, int vol, int streamType, String callingPackage) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public boolean isFixedVolume(int streamType) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void doZenVolumeProcess(boolean enable, String callingPackage) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean isZenVolume() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public int lockActiveStream(boolean lock) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setVolumeFaded(int StreamType, int vol, int fadetime, String callingPackage) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void startSpeechEffect(int audioSession) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void stopSpeechEffect(int audioSession) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void forceChangeToAmpChannel(int channelBits, int activeBits, int volume, boolean stop) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void restoreMusicVolume(String callingPackage) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setMainDriverMode(int mode) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getMainDriverMode() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setRingtoneSessionId(int streamType, int sessionid, String pkgName) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setBanVolumeChangeMode(int streamType, int mode, String pkgName) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getBanVolumeChangeMode(int streamType) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setBtCallMode(int mode) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getBtCallMode() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void playbackControl(int cmd, int param) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setBtCallOnFlag(int flag) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setNetEcallEnable(boolean enable) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void registerCallback(String pkgName, IAudioEventListener callBackFunc) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void unregisterCallback(String pkgName, IAudioEventListener callBackFunc) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getBtCallOnFlag() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setDangerousTtsStatus(int on) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getDangerousTtsStatus() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setDangerousTtsVolLevel(int level) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getDangerousTtsVolLevel() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void ChangeChannelByTrack(int usage, int id, boolean start) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void temporaryChangeVolumeDown(int StreamType, int dstVol, boolean restoreVol, int flag, String packageName) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void applyAlarmId(int usage, int id) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public List<String> getAudioFocusPackageNameList() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public List<String> getAudioFocusPackageNameListByPosition(int position) throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public void audioThreadProcess(int type, int usage, int streamType, int Ppid, String pkgName) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setSessionIdStatus(int sessionId, int position, int status) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int requestAudioFocusPosition(AudioAttributes aa, int durationHint, IBinder cb, IAudioFocusDispatcher fd, String clientId, int position, int flags, IAudioPolicyCallback pcb, int sdk) throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void changeAudioFocusPosition(String clientId, int position) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setStreamPosition(int streamType, String pkgName, int position, int id) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setSoundPositionEnable(boolean enable) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean getSoundPositionEnable() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setMassageSeatLevel(List<String> levelList) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setMusicSeatEnable(boolean enable) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean getMusicSeatEnable() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setMusicSeatRythmPause(boolean pause) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setMusicSeatEffect(int index) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public int getMusicSeatEffect() throws RemoteException {
            return 0;
        }

        @Override // android.media.IAudioService
        public void setMmapToAvasEnable(boolean enable) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean getMmapToAvasEnable() throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setSpecialOutputId(int outType, int sessionId, boolean enable) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void playAvasSound(int position, String path) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void stopAvasSound(String path) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public void setSoftTypeVolumeMute(int type, boolean enable) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public boolean checkPlayingRouteByPackage(int type, String pkgName) throws RemoteException {
            return false;
        }

        @Override // android.media.IAudioService
        public void setAudioPathWhiteList(int type, String writeList) throws RemoteException {
        }

        @Override // android.media.IAudioService
        public IKaraoke getXMicService() throws RemoteException {
            return null;
        }

        @Override // android.media.IAudioService
        public List<xpAudioSessionInfo> getActiveSessionList() throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IAudioService {
        private static final String DESCRIPTOR = "android.media.IAudioService";
        static final int TRANSACTION_ChangeChannelByTrack = 180;
        static final int TRANSACTION_abandonAudioFocus = 55;
        static final int TRANSACTION_addMixForPolicy = 84;
        static final int TRANSACTION_adjustStreamVolume = 9;
        static final int TRANSACTION_adjustSuggestedStreamVolume = 8;
        static final int TRANSACTION_applyAlarmId = 182;
        static final int TRANSACTION_audioThreadProcess = 185;
        static final int TRANSACTION_avrcpSupportsAbsoluteVolume = 47;
        static final int TRANSACTION_changeAudioFocusPosition = 188;
        static final int TRANSACTION_checkAlarmVolume = 142;
        static final int TRANSACTION_checkPlayingRouteByPackage = 204;
        static final int TRANSACTION_checkStreamActive = 12;
        static final int TRANSACTION_checkStreamCanPlay = 14;
        static final int TRANSACTION_disableRingtoneSync = 95;
        static final int TRANSACTION_disableSafeMediaVolume = 78;
        static final int TRANSACTION_disableSystemSound = 130;
        static final int TRANSACTION_dispatchFocusChange = 97;
        static final int TRANSACTION_doZenVolumeProcess = 155;
        static final int TRANSACTION_enableSystemSound = 129;
        static final int TRANSACTION_flushXpCustomizeEffects = 128;
        static final int TRANSACTION_forceChangeToAmpChannel = 161;
        static final int TRANSACTION_forceRemoteSubmixFullVolume = 18;
        static final int TRANSACTION_forceVolumeControlStream = 66;
        static final int TRANSACTION_getActivePlaybackConfigurations = 94;
        static final int TRANSACTION_getActiveRecordingConfigurations = 91;
        static final int TRANSACTION_getActiveSessionList = 207;
        static final int TRANSACTION_getAllowedCapturePolicy = 109;
        static final int TRANSACTION_getAudioFocusPackageNameList = 183;
        static final int TRANSACTION_getAudioFocusPackageNameListByPosition = 184;
        static final int TRANSACTION_getAudioProductStrategies = 30;
        static final int TRANSACTION_getAudioVolumeGroups = 24;
        static final int TRANSACTION_getBanVolumeChangeMode = 167;
        static final int TRANSACTION_getBtCallMode = 169;
        static final int TRANSACTION_getBtCallOnFlag = 175;
        static final int TRANSACTION_getCurrentAudioFocus = 57;
        static final int TRANSACTION_getCurrentAudioFocusAttributes = 58;
        static final int TRANSACTION_getCurrentAudioFocusPackageName = 59;
        static final int TRANSACTION_getDangerousTtsStatus = 177;
        static final int TRANSACTION_getDangerousTtsVolLevel = 179;
        static final int TRANSACTION_getDyn3dEffectLevel = 123;
        static final int TRANSACTION_getFocusRampTimeMs = 96;
        static final int TRANSACTION_getLastAudibleStreamVolume = 29;
        static final int TRANSACTION_getLastAudioFocusPackageName = 60;
        static final int TRANSACTION_getMainDriverMode = 164;
        static final int TRANSACTION_getMaxVolumeIndexForAttributes = 27;
        static final int TRANSACTION_getMinVolumeIndexForAttributes = 28;
        static final int TRANSACTION_getMmapToAvasEnable = 199;
        static final int TRANSACTION_getMode = 41;
        static final int TRANSACTION_getMusicSeatEffect = 197;
        static final int TRANSACTION_getMusicSeatEnable = 194;
        static final int TRANSACTION_getNavVolDecreaseEnable = 125;
        static final int TRANSACTION_getOtherMusicPlayingPkgs = 148;
        static final int TRANSACTION_getRingerModeExternal = 34;
        static final int TRANSACTION_getRingerModeInternal = 35;
        static final int TRANSACTION_getRingtonePlayer = 68;
        static final int TRANSACTION_getSoundEffectMode = 112;
        static final int TRANSACTION_getSoundEffectParms = 119;
        static final int TRANSACTION_getSoundEffectScene = 117;
        static final int TRANSACTION_getSoundEffectType = 115;
        static final int TRANSACTION_getSoundField = 111;
        static final int TRANSACTION_getSoundPositionEnable = 191;
        static final int TRANSACTION_getSoundSpeedLinkLevel = 121;
        static final int TRANSACTION_getStreamMaxVolume = 23;
        static final int TRANSACTION_getStreamMinVolume = 22;
        static final int TRANSACTION_getStreamVolume = 21;
        static final int TRANSACTION_getUiSoundsStreamType = 69;
        static final int TRANSACTION_getVibrateSetting = 38;
        static final int TRANSACTION_getVoicePosition = 152;
        static final int TRANSACTION_getVoiceStatus = 150;
        static final int TRANSACTION_getVolumeIndexForAttributes = 26;
        static final int TRANSACTION_getXMicService = 206;
        static final int TRANSACTION_getXpCustomizeEffect = 127;
        static final int TRANSACTION_handleBluetoothA2dpDeviceConfigChange = 71;
        static final int TRANSACTION_hasHapticChannels = 107;
        static final int TRANSACTION_hasRegisteredDynamicPolicy = 88;
        static final int TRANSACTION_isAnyStreamActive = 13;
        static final int TRANSACTION_isAudioServerRunning = 104;
        static final int TRANSACTION_isBluetoothA2dpOn = 53;
        static final int TRANSACTION_isBluetoothScoOn = 51;
        static final int TRANSACTION_isBtCallOn = 144;
        static final int TRANSACTION_isBtHeadPhoneOn = 140;
        static final int TRANSACTION_isCameraSoundForced = 73;
        static final int TRANSACTION_isFixedVolume = 154;
        static final int TRANSACTION_isHdmiSystemAudioSupported = 80;
        static final int TRANSACTION_isKaraokeOn = 146;
        static final int TRANSACTION_isMainDriverOn = 139;
        static final int TRANSACTION_isMasterMute = 19;
        static final int TRANSACTION_isMusicLimitMode = 16;
        static final int TRANSACTION_isOtherSessionOn = 147;
        static final int TRANSACTION_isSpeakerphoneOn = 49;
        static final int TRANSACTION_isSpeechSurroundOn = 138;
        static final int TRANSACTION_isStereoAlarmOn = 137;
        static final int TRANSACTION_isStreamAffectedByMute = 77;
        static final int TRANSACTION_isStreamAffectedByRingerMode = 76;
        static final int TRANSACTION_isStreamMute = 17;
        static final int TRANSACTION_isSystemSoundEnabled = 131;
        static final int TRANSACTION_isUsageActive = 132;
        static final int TRANSACTION_isValidRingerMode = 36;
        static final int TRANSACTION_isZenVolume = 156;
        static final int TRANSACTION_loadSoundEffects = 44;
        static final int TRANSACTION_lockActiveStream = 157;
        static final int TRANSACTION_notifyVolumeControllerVisible = 75;
        static final int TRANSACTION_playAvasSound = 201;
        static final int TRANSACTION_playSoundEffect = 42;
        static final int TRANSACTION_playSoundEffectVolume = 43;
        static final int TRANSACTION_playbackControl = 170;
        static final int TRANSACTION_playerAttributes = 2;
        static final int TRANSACTION_playerEvent = 3;
        static final int TRANSACTION_playerHasOpPlayAudio = 98;
        static final int TRANSACTION_recorderEvent = 6;
        static final int TRANSACTION_registerAudioPolicy = 81;
        static final int TRANSACTION_registerAudioServerStateDispatcher = 102;
        static final int TRANSACTION_registerCallback = 173;
        static final int TRANSACTION_registerPlaybackCallback = 92;
        static final int TRANSACTION_registerRecordingCallback = 89;
        static final int TRANSACTION_releasePlayer = 4;
        static final int TRANSACTION_releaseRecorder = 7;
        static final int TRANSACTION_reloadAudioSettings = 46;
        static final int TRANSACTION_removeMixForPolicy = 85;
        static final int TRANSACTION_removeUidDeviceAffinity = 106;
        static final int TRANSACTION_requestAudioFocus = 54;
        static final int TRANSACTION_requestAudioFocusPosition = 187;
        static final int TRANSACTION_restoreMusicVolume = 162;
        static final int TRANSACTION_selectAlarmChannels = 141;
        static final int TRANSACTION_setAllowedCapturePolicy = 108;
        static final int TRANSACTION_setAudioPathWhiteList = 205;
        static final int TRANSACTION_setBanVolumeChangeMode = 166;
        static final int TRANSACTION_setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent = 100;
        static final int TRANSACTION_setBluetoothA2dpOn = 52;
        static final int TRANSACTION_setBluetoothHearingAidDeviceConnectionState = 99;
        static final int TRANSACTION_setBluetoothScoOn = 50;
        static final int TRANSACTION_setBtCallMode = 168;
        static final int TRANSACTION_setBtCallOn = 143;
        static final int TRANSACTION_setBtCallOnFlag = 171;
        static final int TRANSACTION_setBtHeadPhone = 136;
        static final int TRANSACTION_setDangerousTtsStatus = 176;
        static final int TRANSACTION_setDangerousTtsVolLevel = 178;
        static final int TRANSACTION_setDyn3dEffectLevel = 122;
        static final int TRANSACTION_setFixedVolume = 153;
        static final int TRANSACTION_setFocusPropertiesForPolicy = 86;
        static final int TRANSACTION_setFocusRequestResultFromExtPolicy = 101;
        static final int TRANSACTION_setHdmiSystemAudioSupported = 79;
        static final int TRANSACTION_setKaraokeOn = 145;
        static final int TRANSACTION_setMainDriver = 135;
        static final int TRANSACTION_setMainDriverMode = 163;
        static final int TRANSACTION_setMassageSeatLevel = 192;
        static final int TRANSACTION_setMasterMute = 20;
        static final int TRANSACTION_setMicrophoneMute = 31;
        static final int TRANSACTION_setMmapToAvasEnable = 198;
        static final int TRANSACTION_setMode = 40;
        static final int TRANSACTION_setMusicLimitMode = 15;
        static final int TRANSACTION_setMusicSeatEffect = 196;
        static final int TRANSACTION_setMusicSeatEnable = 193;
        static final int TRANSACTION_setMusicSeatRythmPause = 195;
        static final int TRANSACTION_setNavVolDecreaseEnable = 124;
        static final int TRANSACTION_setNetEcallEnable = 172;
        static final int TRANSACTION_setRingerModeExternal = 32;
        static final int TRANSACTION_setRingerModeInternal = 33;
        static final int TRANSACTION_setRingtonePlayer = 67;
        static final int TRANSACTION_setRingtoneSessionId = 165;
        static final int TRANSACTION_setSessionIdStatus = 186;
        static final int TRANSACTION_setSoftTypeVolumeMute = 203;
        static final int TRANSACTION_setSoundEffectMode = 113;
        static final int TRANSACTION_setSoundEffectParms = 118;
        static final int TRANSACTION_setSoundEffectScene = 116;
        static final int TRANSACTION_setSoundEffectType = 114;
        static final int TRANSACTION_setSoundField = 110;
        static final int TRANSACTION_setSoundPositionEnable = 190;
        static final int TRANSACTION_setSoundSpeedLinkLevel = 120;
        static final int TRANSACTION_setSpeakerphoneOn = 48;
        static final int TRANSACTION_setSpecialOutputId = 200;
        static final int TRANSACTION_setSpeechSurround = 134;
        static final int TRANSACTION_setStereoAlarm = 133;
        static final int TRANSACTION_setStreamPosition = 189;
        static final int TRANSACTION_setStreamVolume = 10;
        static final int TRANSACTION_setUidDeviceAffinity = 105;
        static final int TRANSACTION_setVibrateSetting = 37;
        static final int TRANSACTION_setVoicePosition = 151;
        static final int TRANSACTION_setVoiceStatus = 149;
        static final int TRANSACTION_setVolumeController = 74;
        static final int TRANSACTION_setVolumeFaded = 158;
        static final int TRANSACTION_setVolumeIndexForAttributes = 25;
        static final int TRANSACTION_setVolumePolicy = 87;
        static final int TRANSACTION_setVolumeToHal = 11;
        static final int TRANSACTION_setWiredDeviceConnectionState = 70;
        static final int TRANSACTION_setXpCustomizeEffect = 126;
        static final int TRANSACTION_shouldVibrate = 39;
        static final int TRANSACTION_startAudioCapture = 61;
        static final int TRANSACTION_startBluetoothSco = 63;
        static final int TRANSACTION_startBluetoothScoVirtualCall = 64;
        static final int TRANSACTION_startSpeechEffect = 159;
        static final int TRANSACTION_startWatchingRoutes = 72;
        static final int TRANSACTION_stopAudioCapture = 62;
        static final int TRANSACTION_stopAvasSound = 202;
        static final int TRANSACTION_stopBluetoothSco = 65;
        static final int TRANSACTION_stopSpeechEffect = 160;
        static final int TRANSACTION_temporaryChangeVolumeDown = 181;
        static final int TRANSACTION_trackPlayer = 1;
        static final int TRANSACTION_trackRecorder = 5;
        static final int TRANSACTION_unloadSoundEffects = 45;
        static final int TRANSACTION_unregisterAudioFocusClient = 56;
        static final int TRANSACTION_unregisterAudioPolicy = 83;
        static final int TRANSACTION_unregisterAudioPolicyAsync = 82;
        static final int TRANSACTION_unregisterAudioServerStateDispatcher = 103;
        static final int TRANSACTION_unregisterCallback = 174;
        static final int TRANSACTION_unregisterPlaybackCallback = 93;
        static final int TRANSACTION_unregisterRecordingCallback = 90;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

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

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "trackPlayer";
                case 2:
                    return "playerAttributes";
                case 3:
                    return "playerEvent";
                case 4:
                    return "releasePlayer";
                case 5:
                    return "trackRecorder";
                case 6:
                    return "recorderEvent";
                case 7:
                    return "releaseRecorder";
                case 8:
                    return "adjustSuggestedStreamVolume";
                case 9:
                    return "adjustStreamVolume";
                case 10:
                    return "setStreamVolume";
                case 11:
                    return "setVolumeToHal";
                case 12:
                    return "checkStreamActive";
                case 13:
                    return "isAnyStreamActive";
                case 14:
                    return "checkStreamCanPlay";
                case 15:
                    return "setMusicLimitMode";
                case 16:
                    return "isMusicLimitMode";
                case 17:
                    return "isStreamMute";
                case 18:
                    return "forceRemoteSubmixFullVolume";
                case 19:
                    return "isMasterMute";
                case 20:
                    return "setMasterMute";
                case 21:
                    return "getStreamVolume";
                case 22:
                    return "getStreamMinVolume";
                case 23:
                    return "getStreamMaxVolume";
                case 24:
                    return "getAudioVolumeGroups";
                case 25:
                    return "setVolumeIndexForAttributes";
                case 26:
                    return "getVolumeIndexForAttributes";
                case 27:
                    return "getMaxVolumeIndexForAttributes";
                case 28:
                    return "getMinVolumeIndexForAttributes";
                case 29:
                    return "getLastAudibleStreamVolume";
                case 30:
                    return "getAudioProductStrategies";
                case 31:
                    return "setMicrophoneMute";
                case 32:
                    return "setRingerModeExternal";
                case 33:
                    return "setRingerModeInternal";
                case 34:
                    return "getRingerModeExternal";
                case 35:
                    return "getRingerModeInternal";
                case 36:
                    return "isValidRingerMode";
                case 37:
                    return "setVibrateSetting";
                case 38:
                    return "getVibrateSetting";
                case 39:
                    return "shouldVibrate";
                case 40:
                    return "setMode";
                case 41:
                    return "getMode";
                case 42:
                    return "playSoundEffect";
                case 43:
                    return "playSoundEffectVolume";
                case 44:
                    return "loadSoundEffects";
                case 45:
                    return "unloadSoundEffects";
                case 46:
                    return "reloadAudioSettings";
                case 47:
                    return "avrcpSupportsAbsoluteVolume";
                case 48:
                    return "setSpeakerphoneOn";
                case 49:
                    return "isSpeakerphoneOn";
                case 50:
                    return "setBluetoothScoOn";
                case 51:
                    return "isBluetoothScoOn";
                case 52:
                    return "setBluetoothA2dpOn";
                case 53:
                    return "isBluetoothA2dpOn";
                case 54:
                    return "requestAudioFocus";
                case 55:
                    return "abandonAudioFocus";
                case 56:
                    return "unregisterAudioFocusClient";
                case 57:
                    return "getCurrentAudioFocus";
                case 58:
                    return "getCurrentAudioFocusAttributes";
                case 59:
                    return "getCurrentAudioFocusPackageName";
                case 60:
                    return "getLastAudioFocusPackageName";
                case 61:
                    return "startAudioCapture";
                case 62:
                    return "stopAudioCapture";
                case 63:
                    return "startBluetoothSco";
                case 64:
                    return "startBluetoothScoVirtualCall";
                case 65:
                    return "stopBluetoothSco";
                case 66:
                    return "forceVolumeControlStream";
                case 67:
                    return "setRingtonePlayer";
                case 68:
                    return "getRingtonePlayer";
                case 69:
                    return "getUiSoundsStreamType";
                case 70:
                    return "setWiredDeviceConnectionState";
                case 71:
                    return "handleBluetoothA2dpDeviceConfigChange";
                case 72:
                    return "startWatchingRoutes";
                case 73:
                    return "isCameraSoundForced";
                case 74:
                    return "setVolumeController";
                case 75:
                    return "notifyVolumeControllerVisible";
                case 76:
                    return "isStreamAffectedByRingerMode";
                case 77:
                    return "isStreamAffectedByMute";
                case 78:
                    return "disableSafeMediaVolume";
                case 79:
                    return "setHdmiSystemAudioSupported";
                case 80:
                    return "isHdmiSystemAudioSupported";
                case 81:
                    return "registerAudioPolicy";
                case 82:
                    return "unregisterAudioPolicyAsync";
                case 83:
                    return "unregisterAudioPolicy";
                case 84:
                    return "addMixForPolicy";
                case 85:
                    return "removeMixForPolicy";
                case 86:
                    return "setFocusPropertiesForPolicy";
                case 87:
                    return "setVolumePolicy";
                case 88:
                    return "hasRegisteredDynamicPolicy";
                case 89:
                    return "registerRecordingCallback";
                case 90:
                    return "unregisterRecordingCallback";
                case 91:
                    return "getActiveRecordingConfigurations";
                case 92:
                    return "registerPlaybackCallback";
                case 93:
                    return "unregisterPlaybackCallback";
                case 94:
                    return "getActivePlaybackConfigurations";
                case 95:
                    return "disableRingtoneSync";
                case 96:
                    return "getFocusRampTimeMs";
                case 97:
                    return "dispatchFocusChange";
                case 98:
                    return "playerHasOpPlayAudio";
                case 99:
                    return "setBluetoothHearingAidDeviceConnectionState";
                case 100:
                    return "setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent";
                case 101:
                    return "setFocusRequestResultFromExtPolicy";
                case 102:
                    return "registerAudioServerStateDispatcher";
                case 103:
                    return "unregisterAudioServerStateDispatcher";
                case 104:
                    return "isAudioServerRunning";
                case 105:
                    return "setUidDeviceAffinity";
                case 106:
                    return "removeUidDeviceAffinity";
                case 107:
                    return "hasHapticChannels";
                case 108:
                    return "setAllowedCapturePolicy";
                case 109:
                    return "getAllowedCapturePolicy";
                case 110:
                    return "setSoundField";
                case 111:
                    return "getSoundField";
                case 112:
                    return "getSoundEffectMode";
                case 113:
                    return "setSoundEffectMode";
                case 114:
                    return "setSoundEffectType";
                case 115:
                    return "getSoundEffectType";
                case 116:
                    return "setSoundEffectScene";
                case 117:
                    return "getSoundEffectScene";
                case 118:
                    return "setSoundEffectParms";
                case 119:
                    return "getSoundEffectParms";
                case 120:
                    return "setSoundSpeedLinkLevel";
                case 121:
                    return "getSoundSpeedLinkLevel";
                case 122:
                    return "setDyn3dEffectLevel";
                case 123:
                    return "getDyn3dEffectLevel";
                case 124:
                    return "setNavVolDecreaseEnable";
                case 125:
                    return "getNavVolDecreaseEnable";
                case 126:
                    return "setXpCustomizeEffect";
                case 127:
                    return "getXpCustomizeEffect";
                case 128:
                    return "flushXpCustomizeEffects";
                case 129:
                    return "enableSystemSound";
                case 130:
                    return "disableSystemSound";
                case 131:
                    return "isSystemSoundEnabled";
                case 132:
                    return "isUsageActive";
                case 133:
                    return "setStereoAlarm";
                case 134:
                    return "setSpeechSurround";
                case 135:
                    return "setMainDriver";
                case 136:
                    return "setBtHeadPhone";
                case 137:
                    return "isStereoAlarmOn";
                case 138:
                    return "isSpeechSurroundOn";
                case 139:
                    return "isMainDriverOn";
                case 140:
                    return "isBtHeadPhoneOn";
                case 141:
                    return "selectAlarmChannels";
                case 142:
                    return "checkAlarmVolume";
                case 143:
                    return "setBtCallOn";
                case 144:
                    return "isBtCallOn";
                case 145:
                    return "setKaraokeOn";
                case 146:
                    return "isKaraokeOn";
                case 147:
                    return "isOtherSessionOn";
                case 148:
                    return "getOtherMusicPlayingPkgs";
                case 149:
                    return "setVoiceStatus";
                case 150:
                    return "getVoiceStatus";
                case 151:
                    return "setVoicePosition";
                case 152:
                    return "getVoicePosition";
                case 153:
                    return "setFixedVolume";
                case 154:
                    return "isFixedVolume";
                case 155:
                    return "doZenVolumeProcess";
                case 156:
                    return "isZenVolume";
                case 157:
                    return "lockActiveStream";
                case 158:
                    return "setVolumeFaded";
                case 159:
                    return "startSpeechEffect";
                case 160:
                    return "stopSpeechEffect";
                case 161:
                    return "forceChangeToAmpChannel";
                case 162:
                    return "restoreMusicVolume";
                case 163:
                    return "setMainDriverMode";
                case 164:
                    return "getMainDriverMode";
                case 165:
                    return "setRingtoneSessionId";
                case 166:
                    return "setBanVolumeChangeMode";
                case 167:
                    return "getBanVolumeChangeMode";
                case 168:
                    return "setBtCallMode";
                case 169:
                    return "getBtCallMode";
                case 170:
                    return "playbackControl";
                case 171:
                    return "setBtCallOnFlag";
                case 172:
                    return "setNetEcallEnable";
                case 173:
                    return "registerCallback";
                case 174:
                    return "unregisterCallback";
                case 175:
                    return "getBtCallOnFlag";
                case 176:
                    return "setDangerousTtsStatus";
                case 177:
                    return "getDangerousTtsStatus";
                case 178:
                    return "setDangerousTtsVolLevel";
                case 179:
                    return "getDangerousTtsVolLevel";
                case 180:
                    return "ChangeChannelByTrack";
                case 181:
                    return "temporaryChangeVolumeDown";
                case 182:
                    return "applyAlarmId";
                case 183:
                    return "getAudioFocusPackageNameList";
                case 184:
                    return "getAudioFocusPackageNameListByPosition";
                case 185:
                    return "audioThreadProcess";
                case 186:
                    return "setSessionIdStatus";
                case 187:
                    return "requestAudioFocusPosition";
                case 188:
                    return "changeAudioFocusPosition";
                case 189:
                    return "setStreamPosition";
                case 190:
                    return "setSoundPositionEnable";
                case 191:
                    return "getSoundPositionEnable";
                case 192:
                    return "setMassageSeatLevel";
                case 193:
                    return "setMusicSeatEnable";
                case 194:
                    return "getMusicSeatEnable";
                case 195:
                    return "setMusicSeatRythmPause";
                case 196:
                    return "setMusicSeatEffect";
                case 197:
                    return "getMusicSeatEffect";
                case 198:
                    return "setMmapToAvasEnable";
                case 199:
                    return "getMmapToAvasEnable";
                case 200:
                    return "setSpecialOutputId";
                case 201:
                    return "playAvasSound";
                case 202:
                    return "stopAvasSound";
                case 203:
                    return "setSoftTypeVolumeMute";
                case 204:
                    return "checkPlayingRouteByPackage";
                case 205:
                    return "setAudioPathWhiteList";
                case 206:
                    return "getXMicService";
                case 207:
                    return "getActiveSessionList";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            PlayerBase.PlayerIdCard _arg0;
            AudioAttributes _arg1;
            boolean _arg12;
            AudioAttributes _arg02;
            AudioAttributes _arg03;
            AudioAttributes _arg04;
            AudioAttributes _arg05;
            AudioAttributes _arg06;
            AudioAttributes _arg2;
            BluetoothDevice _arg07;
            AudioPolicyConfig _arg08;
            AudioPolicyConfig _arg09;
            AudioPolicyConfig _arg010;
            VolumePolicy _arg011;
            AudioAttributes _arg13;
            AudioFocusInfo _arg012;
            BluetoothDevice _arg013;
            BluetoothDevice _arg014;
            AudioFocusInfo _arg015;
            Uri _arg016;
            AudioAttributes _arg017;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = PlayerBase.PlayerIdCard.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    int _result = trackPlayer(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg018 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = AudioAttributes.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    playerAttributes(_arg018, _arg1);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg019 = data.readInt();
                    playerEvent(_arg019, data.readInt());
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg020 = data.readInt();
                    releasePlayer(_arg020);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg021 = data.readStrongBinder();
                    int _result2 = trackRecorder(_arg021);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg022 = data.readInt();
                    recorderEvent(_arg022, data.readInt());
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg023 = data.readInt();
                    releaseRecorder(_arg023);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg024 = data.readInt();
                    int _arg14 = data.readInt();
                    int _arg22 = data.readInt();
                    String _arg3 = data.readString();
                    String _arg4 = data.readString();
                    adjustSuggestedStreamVolume(_arg024, _arg14, _arg22, _arg3, _arg4);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg025 = data.readInt();
                    int _arg15 = data.readInt();
                    int _arg23 = data.readInt();
                    String _arg32 = data.readString();
                    adjustStreamVolume(_arg025, _arg15, _arg23, _arg32);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg026 = data.readInt();
                    int _arg16 = data.readInt();
                    int _arg24 = data.readInt();
                    String _arg33 = data.readString();
                    setStreamVolume(_arg026, _arg16, _arg24, _arg33);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg027 = data.readInt();
                    setVolumeToHal(_arg027);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg028 = data.readInt();
                    boolean checkStreamActive = checkStreamActive(_arg028);
                    reply.writeNoException();
                    reply.writeInt(checkStreamActive ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isAnyStreamActive = isAnyStreamActive();
                    reply.writeNoException();
                    reply.writeInt(isAnyStreamActive ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg029 = data.readInt();
                    int _result3 = checkStreamCanPlay(_arg029);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg030 = _arg12;
                    setMusicLimitMode(_arg030);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isMusicLimitMode = isMusicLimitMode();
                    reply.writeNoException();
                    reply.writeInt(isMusicLimitMode ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg031 = data.readInt();
                    boolean isStreamMute = isStreamMute(_arg031);
                    reply.writeNoException();
                    reply.writeInt(isStreamMute ? 1 : 0);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg032 = _arg12;
                    forceRemoteSubmixFullVolume(_arg032, data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isMasterMute = isMasterMute();
                    reply.writeNoException();
                    reply.writeInt(isMasterMute ? 1 : 0);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg033 = _arg12;
                    int _arg17 = data.readInt();
                    String _arg25 = data.readString();
                    int _arg34 = data.readInt();
                    setMasterMute(_arg033, _arg17, _arg25, _arg34);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg034 = data.readInt();
                    int _result4 = getStreamVolume(_arg034);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg035 = data.readInt();
                    int _result5 = getStreamMinVolume(_arg035);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg036 = data.readInt();
                    int _result6 = getStreamMaxVolume(_arg036);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    List<AudioVolumeGroup> _result7 = getAudioVolumeGroups();
                    reply.writeNoException();
                    reply.writeTypedList(_result7);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = AudioAttributes.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    int _arg18 = data.readInt();
                    int _arg26 = data.readInt();
                    String _arg35 = data.readString();
                    setVolumeIndexForAttributes(_arg02, _arg18, _arg26, _arg35);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = AudioAttributes.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    int _result8 = getVolumeIndexForAttributes(_arg03);
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = AudioAttributes.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    int _result9 = getMaxVolumeIndexForAttributes(_arg04);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = AudioAttributes.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    int _result10 = getMinVolumeIndexForAttributes(_arg05);
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg037 = data.readInt();
                    int _result11 = getLastAudibleStreamVolume(_arg037);
                    reply.writeNoException();
                    reply.writeInt(_result11);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    List<AudioProductStrategy> _result12 = getAudioProductStrategies();
                    reply.writeNoException();
                    reply.writeTypedList(_result12);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg038 = _arg12;
                    String _arg19 = data.readString();
                    int _arg27 = data.readInt();
                    setMicrophoneMute(_arg038, _arg19, _arg27);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg039 = data.readInt();
                    setRingerModeExternal(_arg039, data.readString());
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg040 = data.readInt();
                    setRingerModeInternal(_arg040, data.readString());
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    int _result13 = getRingerModeExternal();
                    reply.writeNoException();
                    reply.writeInt(_result13);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    int _result14 = getRingerModeInternal();
                    reply.writeNoException();
                    reply.writeInt(_result14);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg041 = data.readInt();
                    boolean isValidRingerMode = isValidRingerMode(_arg041);
                    reply.writeNoException();
                    reply.writeInt(isValidRingerMode ? 1 : 0);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg042 = data.readInt();
                    setVibrateSetting(_arg042, data.readInt());
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg043 = data.readInt();
                    int _result15 = getVibrateSetting(_arg043);
                    reply.writeNoException();
                    reply.writeInt(_result15);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg044 = data.readInt();
                    boolean shouldVibrate = shouldVibrate(_arg044);
                    reply.writeNoException();
                    reply.writeInt(shouldVibrate ? 1 : 0);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg045 = data.readInt();
                    IBinder _arg110 = data.readStrongBinder();
                    String _arg28 = data.readString();
                    setMode(_arg045, _arg110, _arg28);
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    int _result16 = getMode();
                    reply.writeNoException();
                    reply.writeInt(_result16);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg046 = data.readInt();
                    playSoundEffect(_arg046);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg047 = data.readInt();
                    playSoundEffectVolume(_arg047, data.readFloat());
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    boolean loadSoundEffects = loadSoundEffects();
                    reply.writeNoException();
                    reply.writeInt(loadSoundEffects ? 1 : 0);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    unloadSoundEffects();
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    reloadAudioSettings();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg048 = data.readString();
                    _arg12 = data.readInt() != 0;
                    avrcpSupportsAbsoluteVolume(_arg048, _arg12);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg049 = _arg12;
                    setSpeakerphoneOn(_arg049);
                    reply.writeNoException();
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isSpeakerphoneOn = isSpeakerphoneOn();
                    reply.writeNoException();
                    reply.writeInt(isSpeakerphoneOn ? 1 : 0);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg050 = _arg12;
                    setBluetoothScoOn(_arg050);
                    reply.writeNoException();
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isBluetoothScoOn = isBluetoothScoOn();
                    reply.writeNoException();
                    reply.writeInt(isBluetoothScoOn ? 1 : 0);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg051 = _arg12;
                    setBluetoothA2dpOn(_arg051);
                    reply.writeNoException();
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isBluetoothA2dpOn = isBluetoothA2dpOn();
                    reply.writeNoException();
                    reply.writeInt(isBluetoothA2dpOn ? 1 : 0);
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = AudioAttributes.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    int _arg111 = data.readInt();
                    IBinder _arg29 = data.readStrongBinder();
                    IAudioFocusDispatcher _arg36 = IAudioFocusDispatcher.Stub.asInterface(data.readStrongBinder());
                    String _arg42 = data.readString();
                    String _arg5 = data.readString();
                    int _arg6 = data.readInt();
                    IAudioPolicyCallback _arg7 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg8 = data.readInt();
                    int _result17 = requestAudioFocus(_arg06, _arg111, _arg29, _arg36, _arg42, _arg5, _arg6, _arg7, _arg8);
                    reply.writeNoException();
                    reply.writeInt(_result17);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    IAudioFocusDispatcher _arg052 = IAudioFocusDispatcher.Stub.asInterface(data.readStrongBinder());
                    String _arg112 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = AudioAttributes.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    String _arg37 = data.readString();
                    int _result18 = abandonAudioFocus(_arg052, _arg112, _arg2, _arg37);
                    reply.writeNoException();
                    reply.writeInt(_result18);
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg053 = data.readString();
                    unregisterAudioFocusClient(_arg053);
                    reply.writeNoException();
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    int _result19 = getCurrentAudioFocus();
                    reply.writeNoException();
                    reply.writeInt(_result19);
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    AudioAttributes _result20 = getCurrentAudioFocusAttributes();
                    reply.writeNoException();
                    if (_result20 != null) {
                        reply.writeInt(1);
                        _result20.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    String _result21 = getCurrentAudioFocusPackageName();
                    reply.writeNoException();
                    reply.writeString(_result21);
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    String _result22 = getLastAudioFocusPackageName();
                    reply.writeNoException();
                    reply.writeString(_result22);
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg054 = data.readInt();
                    startAudioCapture(_arg054, data.readInt());
                    reply.writeNoException();
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg055 = data.readInt();
                    stopAudioCapture(_arg055, data.readInt());
                    reply.writeNoException();
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg056 = data.readStrongBinder();
                    startBluetoothSco(_arg056, data.readInt());
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg057 = data.readStrongBinder();
                    startBluetoothScoVirtualCall(_arg057);
                    reply.writeNoException();
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg058 = data.readStrongBinder();
                    stopBluetoothSco(_arg058);
                    reply.writeNoException();
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg059 = data.readInt();
                    forceVolumeControlStream(_arg059, data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    IRingtonePlayer _arg060 = IRingtonePlayer.Stub.asInterface(data.readStrongBinder());
                    setRingtonePlayer(_arg060);
                    reply.writeNoException();
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    IRingtonePlayer _result23 = getRingtonePlayer();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result23 != null ? _result23.asBinder() : null);
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    int _result24 = getUiSoundsStreamType();
                    reply.writeNoException();
                    reply.writeInt(_result24);
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg061 = data.readInt();
                    int _arg113 = data.readInt();
                    String _arg210 = data.readString();
                    String _arg38 = data.readString();
                    String _arg43 = data.readString();
                    setWiredDeviceConnectionState(_arg061, _arg113, _arg210, _arg38, _arg43);
                    reply.writeNoException();
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg07 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg07 = null;
                    }
                    handleBluetoothA2dpDeviceConfigChange(_arg07);
                    reply.writeNoException();
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    IAudioRoutesObserver _arg062 = IAudioRoutesObserver.Stub.asInterface(data.readStrongBinder());
                    AudioRoutesInfo _result25 = startWatchingRoutes(_arg062);
                    reply.writeNoException();
                    if (_result25 != null) {
                        reply.writeInt(1);
                        _result25.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isCameraSoundForced = isCameraSoundForced();
                    reply.writeNoException();
                    reply.writeInt(isCameraSoundForced ? 1 : 0);
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    IVolumeController _arg063 = IVolumeController.Stub.asInterface(data.readStrongBinder());
                    setVolumeController(_arg063);
                    reply.writeNoException();
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    IVolumeController _arg064 = IVolumeController.Stub.asInterface(data.readStrongBinder());
                    _arg12 = data.readInt() != 0;
                    notifyVolumeControllerVisible(_arg064, _arg12);
                    reply.writeNoException();
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg065 = data.readInt();
                    boolean isStreamAffectedByRingerMode = isStreamAffectedByRingerMode(_arg065);
                    reply.writeNoException();
                    reply.writeInt(isStreamAffectedByRingerMode ? 1 : 0);
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg066 = data.readInt();
                    boolean isStreamAffectedByMute = isStreamAffectedByMute(_arg066);
                    reply.writeNoException();
                    reply.writeInt(isStreamAffectedByMute ? 1 : 0);
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg067 = data.readString();
                    disableSafeMediaVolume(_arg067);
                    reply.writeNoException();
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg068 = _arg12;
                    int _result26 = setHdmiSystemAudioSupported(_arg068);
                    reply.writeNoException();
                    reply.writeInt(_result26);
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isHdmiSystemAudioSupported = isHdmiSystemAudioSupported();
                    reply.writeNoException();
                    reply.writeInt(isHdmiSystemAudioSupported ? 1 : 0);
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg08 = AudioPolicyConfig.CREATOR.createFromParcel(data);
                    } else {
                        _arg08 = null;
                    }
                    IAudioPolicyCallback _arg114 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                    boolean _arg211 = data.readInt() != 0;
                    boolean _arg39 = data.readInt() != 0;
                    boolean _arg44 = data.readInt() != 0;
                    boolean _arg52 = data.readInt() != 0;
                    IMediaProjection _arg62 = IMediaProjection.Stub.asInterface(data.readStrongBinder());
                    String _result27 = registerAudioPolicy(_arg08, _arg114, _arg211, _arg39, _arg44, _arg52, _arg62);
                    reply.writeNoException();
                    reply.writeString(_result27);
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    IAudioPolicyCallback _arg069 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                    unregisterAudioPolicyAsync(_arg069);
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    IAudioPolicyCallback _arg070 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                    unregisterAudioPolicy(_arg070);
                    reply.writeNoException();
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg09 = AudioPolicyConfig.CREATOR.createFromParcel(data);
                    } else {
                        _arg09 = null;
                    }
                    int _result28 = addMixForPolicy(_arg09, IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result28);
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg010 = AudioPolicyConfig.CREATOR.createFromParcel(data);
                    } else {
                        _arg010 = null;
                    }
                    int _result29 = removeMixForPolicy(_arg010, IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result29);
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg071 = data.readInt();
                    int _result30 = setFocusPropertiesForPolicy(_arg071, IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(_result30);
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg011 = VolumePolicy.CREATOR.createFromParcel(data);
                    } else {
                        _arg011 = null;
                    }
                    setVolumePolicy(_arg011);
                    reply.writeNoException();
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hasRegisteredDynamicPolicy = hasRegisteredDynamicPolicy();
                    reply.writeNoException();
                    reply.writeInt(hasRegisteredDynamicPolicy ? 1 : 0);
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    IRecordingConfigDispatcher _arg072 = IRecordingConfigDispatcher.Stub.asInterface(data.readStrongBinder());
                    registerRecordingCallback(_arg072);
                    reply.writeNoException();
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    IRecordingConfigDispatcher _arg073 = IRecordingConfigDispatcher.Stub.asInterface(data.readStrongBinder());
                    unregisterRecordingCallback(_arg073);
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    List<AudioRecordingConfiguration> _result31 = getActiveRecordingConfigurations();
                    reply.writeNoException();
                    reply.writeTypedList(_result31);
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    IPlaybackConfigDispatcher _arg074 = IPlaybackConfigDispatcher.Stub.asInterface(data.readStrongBinder());
                    registerPlaybackCallback(_arg074);
                    reply.writeNoException();
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    IPlaybackConfigDispatcher _arg075 = IPlaybackConfigDispatcher.Stub.asInterface(data.readStrongBinder());
                    unregisterPlaybackCallback(_arg075);
                    return true;
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    List<AudioPlaybackConfiguration> _result32 = getActivePlaybackConfigurations();
                    reply.writeNoException();
                    reply.writeTypedList(_result32);
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg076 = data.readInt();
                    disableRingtoneSync(_arg076);
                    reply.writeNoException();
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg077 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg13 = AudioAttributes.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    int _result33 = getFocusRampTimeMs(_arg077, _arg13);
                    reply.writeNoException();
                    reply.writeInt(_result33);
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg012 = AudioFocusInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg012 = null;
                    }
                    int _arg115 = data.readInt();
                    IAudioPolicyCallback _arg212 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                    int _result34 = dispatchFocusChange(_arg012, _arg115, _arg212);
                    reply.writeNoException();
                    reply.writeInt(_result34);
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg078 = data.readInt();
                    _arg12 = data.readInt() != 0;
                    playerHasOpPlayAudio(_arg078, _arg12);
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg013 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg013 = null;
                    }
                    int _arg116 = data.readInt();
                    _arg12 = data.readInt() != 0;
                    int _arg310 = data.readInt();
                    setBluetoothHearingAidDeviceConnectionState(_arg013, _arg116, _arg12, _arg310);
                    reply.writeNoException();
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg014 = BluetoothDevice.CREATOR.createFromParcel(data);
                    } else {
                        _arg014 = null;
                    }
                    int _arg117 = data.readInt();
                    int _arg213 = data.readInt();
                    boolean _arg311 = data.readInt() != 0;
                    int _arg45 = data.readInt();
                    setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(_arg014, _arg117, _arg213, _arg311, _arg45);
                    reply.writeNoException();
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg015 = AudioFocusInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg015 = null;
                    }
                    int _arg118 = data.readInt();
                    IAudioPolicyCallback _arg214 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                    setFocusRequestResultFromExtPolicy(_arg015, _arg118, _arg214);
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    IAudioServerStateDispatcher _arg079 = IAudioServerStateDispatcher.Stub.asInterface(data.readStrongBinder());
                    registerAudioServerStateDispatcher(_arg079);
                    reply.writeNoException();
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    IAudioServerStateDispatcher _arg080 = IAudioServerStateDispatcher.Stub.asInterface(data.readStrongBinder());
                    unregisterAudioServerStateDispatcher(_arg080);
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isAudioServerRunning = isAudioServerRunning();
                    reply.writeNoException();
                    reply.writeInt(isAudioServerRunning ? 1 : 0);
                    return true;
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    IAudioPolicyCallback _arg081 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg119 = data.readInt();
                    int[] _arg215 = data.createIntArray();
                    String[] _arg312 = data.createStringArray();
                    int _result35 = setUidDeviceAffinity(_arg081, _arg119, _arg215, _arg312);
                    reply.writeNoException();
                    reply.writeInt(_result35);
                    return true;
                case 106:
                    data.enforceInterface(DESCRIPTOR);
                    IAudioPolicyCallback _arg082 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                    int _result36 = removeUidDeviceAffinity(_arg082, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result36);
                    return true;
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg016 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg016 = null;
                    }
                    boolean hasHapticChannels = hasHapticChannels(_arg016);
                    reply.writeNoException();
                    reply.writeInt(hasHapticChannels ? 1 : 0);
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg083 = data.readInt();
                    int _result37 = setAllowedCapturePolicy(_arg083);
                    reply.writeNoException();
                    reply.writeInt(_result37);
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    int _result38 = getAllowedCapturePolicy();
                    reply.writeNoException();
                    reply.writeInt(_result38);
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg084 = data.readInt();
                    int _arg120 = data.readInt();
                    int _arg216 = data.readInt();
                    setSoundField(_arg084, _arg120, _arg216);
                    reply.writeNoException();
                    return true;
                case 111:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg085 = data.readInt();
                    SoundField _result39 = getSoundField(_arg085);
                    reply.writeNoException();
                    if (_result39 != null) {
                        reply.writeInt(1);
                        _result39.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 112:
                    data.enforceInterface(DESCRIPTOR);
                    int _result40 = getSoundEffectMode();
                    reply.writeNoException();
                    reply.writeInt(_result40);
                    return true;
                case 113:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg086 = data.readInt();
                    setSoundEffectMode(_arg086);
                    reply.writeNoException();
                    return true;
                case 114:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg087 = data.readInt();
                    setSoundEffectType(_arg087, data.readInt());
                    reply.writeNoException();
                    return true;
                case 115:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg088 = data.readInt();
                    int _result41 = getSoundEffectType(_arg088);
                    reply.writeNoException();
                    reply.writeInt(_result41);
                    return true;
                case 116:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg089 = data.readInt();
                    setSoundEffectScene(_arg089, data.readInt());
                    reply.writeNoException();
                    return true;
                case 117:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg090 = data.readInt();
                    int _result42 = getSoundEffectScene(_arg090);
                    reply.writeNoException();
                    reply.writeInt(_result42);
                    return true;
                case 118:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg091 = data.readInt();
                    int _arg121 = data.readInt();
                    int _arg217 = data.readInt();
                    int _arg313 = data.readInt();
                    setSoundEffectParms(_arg091, _arg121, _arg217, _arg313);
                    reply.writeNoException();
                    return true;
                case 119:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg092 = data.readInt();
                    SoundEffectParms _result43 = getSoundEffectParms(_arg092, data.readInt());
                    reply.writeNoException();
                    if (_result43 != null) {
                        reply.writeInt(1);
                        _result43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 120:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg093 = data.readInt();
                    setSoundSpeedLinkLevel(_arg093);
                    reply.writeNoException();
                    return true;
                case 121:
                    data.enforceInterface(DESCRIPTOR);
                    int _result44 = getSoundSpeedLinkLevel();
                    reply.writeNoException();
                    reply.writeInt(_result44);
                    return true;
                case 122:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg094 = data.readInt();
                    setDyn3dEffectLevel(_arg094);
                    reply.writeNoException();
                    return true;
                case 123:
                    data.enforceInterface(DESCRIPTOR);
                    int _result45 = getDyn3dEffectLevel();
                    reply.writeNoException();
                    reply.writeInt(_result45);
                    return true;
                case 124:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg095 = _arg12;
                    setNavVolDecreaseEnable(_arg095);
                    reply.writeNoException();
                    return true;
                case 125:
                    data.enforceInterface(DESCRIPTOR);
                    boolean navVolDecreaseEnable = getNavVolDecreaseEnable();
                    reply.writeNoException();
                    reply.writeInt(navVolDecreaseEnable ? 1 : 0);
                    return true;
                case 126:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg096 = data.readInt();
                    setXpCustomizeEffect(_arg096, data.readInt());
                    reply.writeNoException();
                    return true;
                case 127:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg097 = data.readInt();
                    int _result46 = getXpCustomizeEffect(_arg097);
                    reply.writeNoException();
                    reply.writeInt(_result46);
                    return true;
                case 128:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg098 = data.createIntArray();
                    flushXpCustomizeEffects(_arg098);
                    reply.writeNoException();
                    return true;
                case 129:
                    data.enforceInterface(DESCRIPTOR);
                    enableSystemSound();
                    reply.writeNoException();
                    return true;
                case 130:
                    data.enforceInterface(DESCRIPTOR);
                    disableSystemSound();
                    reply.writeNoException();
                    return true;
                case 131:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isSystemSoundEnabled = isSystemSoundEnabled();
                    reply.writeNoException();
                    reply.writeInt(isSystemSoundEnabled ? 1 : 0);
                    return true;
                case 132:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg099 = data.readInt();
                    boolean isUsageActive = isUsageActive(_arg099);
                    reply.writeNoException();
                    reply.writeInt(isUsageActive ? 1 : 0);
                    return true;
                case 133:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg0100 = _arg12;
                    setStereoAlarm(_arg0100);
                    reply.writeNoException();
                    return true;
                case 134:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg0101 = _arg12;
                    setSpeechSurround(_arg0101);
                    reply.writeNoException();
                    return true;
                case 135:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg0102 = _arg12;
                    setMainDriver(_arg0102);
                    reply.writeNoException();
                    return true;
                case 136:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg0103 = _arg12;
                    setBtHeadPhone(_arg0103);
                    reply.writeNoException();
                    return true;
                case 137:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isStereoAlarmOn = isStereoAlarmOn();
                    reply.writeNoException();
                    reply.writeInt(isStereoAlarmOn ? 1 : 0);
                    return true;
                case 138:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isSpeechSurroundOn = isSpeechSurroundOn();
                    reply.writeNoException();
                    reply.writeInt(isSpeechSurroundOn ? 1 : 0);
                    return true;
                case 139:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isMainDriverOn = isMainDriverOn();
                    reply.writeNoException();
                    reply.writeInt(isMainDriverOn ? 1 : 0);
                    return true;
                case 140:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isBtHeadPhoneOn = isBtHeadPhoneOn();
                    reply.writeNoException();
                    reply.writeInt(isBtHeadPhoneOn ? 1 : 0);
                    return true;
                case 141:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0104 = data.readInt();
                    int _arg122 = data.readInt();
                    int _arg218 = data.readInt();
                    int _result47 = selectAlarmChannels(_arg0104, _arg122, _arg218);
                    reply.writeNoException();
                    reply.writeInt(_result47);
                    return true;
                case 142:
                    data.enforceInterface(DESCRIPTOR);
                    checkAlarmVolume();
                    reply.writeNoException();
                    return true;
                case 143:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg0105 = _arg12;
                    setBtCallOn(_arg0105);
                    reply.writeNoException();
                    return true;
                case 144:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isBtCallOn = isBtCallOn();
                    reply.writeNoException();
                    reply.writeInt(isBtCallOn ? 1 : 0);
                    return true;
                case 145:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg0106 = _arg12;
                    setKaraokeOn(_arg0106);
                    reply.writeNoException();
                    return true;
                case 146:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isKaraokeOn = isKaraokeOn();
                    reply.writeNoException();
                    reply.writeInt(isKaraokeOn ? 1 : 0);
                    return true;
                case 147:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isOtherSessionOn = isOtherSessionOn();
                    reply.writeNoException();
                    reply.writeInt(isOtherSessionOn ? 1 : 0);
                    return true;
                case 148:
                    data.enforceInterface(DESCRIPTOR);
                    String _result48 = getOtherMusicPlayingPkgs();
                    reply.writeNoException();
                    reply.writeString(_result48);
                    return true;
                case 149:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0107 = data.readInt();
                    setVoiceStatus(_arg0107);
                    reply.writeNoException();
                    return true;
                case 150:
                    data.enforceInterface(DESCRIPTOR);
                    int _result49 = getVoiceStatus();
                    reply.writeNoException();
                    reply.writeInt(_result49);
                    return true;
                case 151:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0108 = data.readInt();
                    int _arg123 = data.readInt();
                    String _arg219 = data.readString();
                    setVoicePosition(_arg0108, _arg123, _arg219);
                    reply.writeNoException();
                    return true;
                case 152:
                    data.enforceInterface(DESCRIPTOR);
                    int _result50 = getVoicePosition();
                    reply.writeNoException();
                    reply.writeInt(_result50);
                    return true;
                case 153:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg0109 = _arg12;
                    int _arg124 = data.readInt();
                    int _arg220 = data.readInt();
                    String _arg314 = data.readString();
                    boolean fixedVolume = setFixedVolume(_arg0109, _arg124, _arg220, _arg314);
                    reply.writeNoException();
                    reply.writeInt(fixedVolume ? 1 : 0);
                    return true;
                case 154:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0110 = data.readInt();
                    boolean isFixedVolume = isFixedVolume(_arg0110);
                    reply.writeNoException();
                    reply.writeInt(isFixedVolume ? 1 : 0);
                    return true;
                case 155:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg0111 = _arg12;
                    doZenVolumeProcess(_arg0111, data.readString());
                    reply.writeNoException();
                    return true;
                case 156:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isZenVolume = isZenVolume();
                    reply.writeNoException();
                    reply.writeInt(isZenVolume ? 1 : 0);
                    return true;
                case 157:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg0112 = _arg12;
                    int _result51 = lockActiveStream(_arg0112);
                    reply.writeNoException();
                    reply.writeInt(_result51);
                    return true;
                case 158:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0113 = data.readInt();
                    int _arg125 = data.readInt();
                    int _arg221 = data.readInt();
                    String _arg315 = data.readString();
                    setVolumeFaded(_arg0113, _arg125, _arg221, _arg315);
                    reply.writeNoException();
                    return true;
                case 159:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0114 = data.readInt();
                    startSpeechEffect(_arg0114);
                    reply.writeNoException();
                    return true;
                case 160:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0115 = data.readInt();
                    stopSpeechEffect(_arg0115);
                    reply.writeNoException();
                    return true;
                case 161:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0116 = data.readInt();
                    int _arg126 = data.readInt();
                    int _arg222 = data.readInt();
                    _arg12 = data.readInt() != 0;
                    forceChangeToAmpChannel(_arg0116, _arg126, _arg222, _arg12);
                    reply.writeNoException();
                    return true;
                case 162:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0117 = data.readString();
                    restoreMusicVolume(_arg0117);
                    reply.writeNoException();
                    return true;
                case 163:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0118 = data.readInt();
                    setMainDriverMode(_arg0118);
                    reply.writeNoException();
                    return true;
                case 164:
                    data.enforceInterface(DESCRIPTOR);
                    int _result52 = getMainDriverMode();
                    reply.writeNoException();
                    reply.writeInt(_result52);
                    return true;
                case 165:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0119 = data.readInt();
                    int _arg127 = data.readInt();
                    String _arg223 = data.readString();
                    setRingtoneSessionId(_arg0119, _arg127, _arg223);
                    reply.writeNoException();
                    return true;
                case 166:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0120 = data.readInt();
                    int _arg128 = data.readInt();
                    String _arg224 = data.readString();
                    setBanVolumeChangeMode(_arg0120, _arg128, _arg224);
                    reply.writeNoException();
                    return true;
                case 167:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0121 = data.readInt();
                    int _result53 = getBanVolumeChangeMode(_arg0121);
                    reply.writeNoException();
                    reply.writeInt(_result53);
                    return true;
                case 168:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0122 = data.readInt();
                    setBtCallMode(_arg0122);
                    reply.writeNoException();
                    return true;
                case 169:
                    data.enforceInterface(DESCRIPTOR);
                    int _result54 = getBtCallMode();
                    reply.writeNoException();
                    reply.writeInt(_result54);
                    return true;
                case 170:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0123 = data.readInt();
                    playbackControl(_arg0123, data.readInt());
                    reply.writeNoException();
                    return true;
                case 171:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0124 = data.readInt();
                    setBtCallOnFlag(_arg0124);
                    reply.writeNoException();
                    return true;
                case 172:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg0125 = _arg12;
                    setNetEcallEnable(_arg0125);
                    reply.writeNoException();
                    return true;
                case 173:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0126 = data.readString();
                    registerCallback(_arg0126, IAudioEventListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 174:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0127 = data.readString();
                    unregisterCallback(_arg0127, IAudioEventListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 175:
                    data.enforceInterface(DESCRIPTOR);
                    int _result55 = getBtCallOnFlag();
                    reply.writeNoException();
                    reply.writeInt(_result55);
                    return true;
                case 176:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0128 = data.readInt();
                    setDangerousTtsStatus(_arg0128);
                    reply.writeNoException();
                    return true;
                case 177:
                    data.enforceInterface(DESCRIPTOR);
                    int _result56 = getDangerousTtsStatus();
                    reply.writeNoException();
                    reply.writeInt(_result56);
                    return true;
                case 178:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0129 = data.readInt();
                    setDangerousTtsVolLevel(_arg0129);
                    reply.writeNoException();
                    return true;
                case 179:
                    data.enforceInterface(DESCRIPTOR);
                    int _result57 = getDangerousTtsVolLevel();
                    reply.writeNoException();
                    reply.writeInt(_result57);
                    return true;
                case 180:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0130 = data.readInt();
                    int _arg129 = data.readInt();
                    _arg12 = data.readInt() != 0;
                    ChangeChannelByTrack(_arg0130, _arg129, _arg12);
                    reply.writeNoException();
                    return true;
                case 181:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0131 = data.readInt();
                    int _arg130 = data.readInt();
                    boolean _arg225 = data.readInt() != 0;
                    int _arg316 = data.readInt();
                    String _arg46 = data.readString();
                    temporaryChangeVolumeDown(_arg0131, _arg130, _arg225, _arg316, _arg46);
                    reply.writeNoException();
                    return true;
                case 182:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0132 = data.readInt();
                    applyAlarmId(_arg0132, data.readInt());
                    reply.writeNoException();
                    return true;
                case 183:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _result58 = getAudioFocusPackageNameList();
                    reply.writeNoException();
                    reply.writeStringList(_result58);
                    return true;
                case 184:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0133 = data.readInt();
                    List<String> _result59 = getAudioFocusPackageNameListByPosition(_arg0133);
                    reply.writeNoException();
                    reply.writeStringList(_result59);
                    return true;
                case 185:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0134 = data.readInt();
                    int _arg131 = data.readInt();
                    int _arg226 = data.readInt();
                    int _arg317 = data.readInt();
                    String _arg47 = data.readString();
                    audioThreadProcess(_arg0134, _arg131, _arg226, _arg317, _arg47);
                    reply.writeNoException();
                    return true;
                case 186:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0135 = data.readInt();
                    int _arg132 = data.readInt();
                    int _arg227 = data.readInt();
                    setSessionIdStatus(_arg0135, _arg132, _arg227);
                    reply.writeNoException();
                    return true;
                case 187:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg017 = AudioAttributes.CREATOR.createFromParcel(data);
                    } else {
                        _arg017 = null;
                    }
                    int _arg133 = data.readInt();
                    IBinder _arg228 = data.readStrongBinder();
                    IAudioFocusDispatcher _arg318 = IAudioFocusDispatcher.Stub.asInterface(data.readStrongBinder());
                    String _arg48 = data.readString();
                    int _arg53 = data.readInt();
                    int _arg63 = data.readInt();
                    IAudioPolicyCallback _arg72 = IAudioPolicyCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg82 = data.readInt();
                    int _result60 = requestAudioFocusPosition(_arg017, _arg133, _arg228, _arg318, _arg48, _arg53, _arg63, _arg72, _arg82);
                    reply.writeNoException();
                    reply.writeInt(_result60);
                    return true;
                case 188:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0136 = data.readString();
                    changeAudioFocusPosition(_arg0136, data.readInt());
                    reply.writeNoException();
                    return true;
                case 189:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0137 = data.readInt();
                    String _arg134 = data.readString();
                    int _arg229 = data.readInt();
                    int _arg319 = data.readInt();
                    setStreamPosition(_arg0137, _arg134, _arg229, _arg319);
                    reply.writeNoException();
                    return true;
                case 190:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg0138 = _arg12;
                    setSoundPositionEnable(_arg0138);
                    reply.writeNoException();
                    return true;
                case 191:
                    data.enforceInterface(DESCRIPTOR);
                    boolean soundPositionEnable = getSoundPositionEnable();
                    reply.writeNoException();
                    reply.writeInt(soundPositionEnable ? 1 : 0);
                    return true;
                case 192:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _arg0139 = data.createStringArrayList();
                    setMassageSeatLevel(_arg0139);
                    reply.writeNoException();
                    return true;
                case 193:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg0140 = _arg12;
                    setMusicSeatEnable(_arg0140);
                    reply.writeNoException();
                    return true;
                case 194:
                    data.enforceInterface(DESCRIPTOR);
                    boolean musicSeatEnable = getMusicSeatEnable();
                    reply.writeNoException();
                    reply.writeInt(musicSeatEnable ? 1 : 0);
                    return true;
                case 195:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg0141 = _arg12;
                    setMusicSeatRythmPause(_arg0141);
                    reply.writeNoException();
                    return true;
                case 196:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0142 = data.readInt();
                    setMusicSeatEffect(_arg0142);
                    reply.writeNoException();
                    return true;
                case 197:
                    data.enforceInterface(DESCRIPTOR);
                    int _result61 = getMusicSeatEffect();
                    reply.writeNoException();
                    reply.writeInt(_result61);
                    return true;
                case 198:
                    data.enforceInterface(DESCRIPTOR);
                    _arg12 = data.readInt() != 0;
                    boolean _arg0143 = _arg12;
                    setMmapToAvasEnable(_arg0143);
                    reply.writeNoException();
                    return true;
                case 199:
                    data.enforceInterface(DESCRIPTOR);
                    boolean mmapToAvasEnable = getMmapToAvasEnable();
                    reply.writeNoException();
                    reply.writeInt(mmapToAvasEnable ? 1 : 0);
                    return true;
                case 200:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0144 = data.readInt();
                    int _arg135 = data.readInt();
                    _arg12 = data.readInt() != 0;
                    setSpecialOutputId(_arg0144, _arg135, _arg12);
                    reply.writeNoException();
                    return true;
                case 201:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0145 = data.readInt();
                    playAvasSound(_arg0145, data.readString());
                    reply.writeNoException();
                    return true;
                case 202:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0146 = data.readString();
                    stopAvasSound(_arg0146);
                    reply.writeNoException();
                    return true;
                case 203:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0147 = data.readInt();
                    _arg12 = data.readInt() != 0;
                    setSoftTypeVolumeMute(_arg0147, _arg12);
                    reply.writeNoException();
                    return true;
                case 204:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0148 = data.readInt();
                    boolean checkPlayingRouteByPackage = checkPlayingRouteByPackage(_arg0148, data.readString());
                    reply.writeNoException();
                    reply.writeInt(checkPlayingRouteByPackage ? 1 : 0);
                    return true;
                case 205:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0149 = data.readInt();
                    setAudioPathWhiteList(_arg0149, data.readString());
                    reply.writeNoException();
                    return true;
                case 206:
                    data.enforceInterface(DESCRIPTOR);
                    IKaraoke _result62 = getXMicService();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result62 != null ? _result62.asBinder() : null);
                    return true;
                case 207:
                    data.enforceInterface(DESCRIPTOR);
                    List<xpAudioSessionInfo> _result63 = getActiveSessionList();
                    reply.writeNoException();
                    reply.writeTypedList(_result63);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IAudioService {
            public static IAudioService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.media.IAudioService
            public int trackPlayer(PlayerBase.PlayerIdCard pic) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().trackPlayer(pic);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void playerAttributes(int piid, AudioAttributes attr) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().playerAttributes(piid, attr);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void playerEvent(int piid, int event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piid);
                    _data.writeInt(event);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().playerEvent(piid, event);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void releasePlayer(int piid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piid);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().releasePlayer(piid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int trackRecorder(IBinder recorder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(recorder);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().trackRecorder(recorder);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void recorderEvent(int riid, int event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(riid);
                    _data.writeInt(event);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().recorderEvent(riid, event);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void releaseRecorder(int riid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(riid);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().releaseRecorder(riid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void adjustSuggestedStreamVolume(int direction, int suggestedStreamType, int flags, String callingPackage, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(direction);
                    _data.writeInt(suggestedStreamType);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    _data.writeString(caller);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().adjustSuggestedStreamVolume(direction, suggestedStreamType, flags, callingPackage, caller);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void adjustStreamVolume(int streamType, int direction, int flags, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    _data.writeInt(direction);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().adjustStreamVolume(streamType, direction, flags, callingPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setStreamVolume(int streamType, int index, int flags, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    _data.writeInt(index);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setStreamVolume(streamType, index, flags, callingPackage);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVolumeToHal(streamType);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkStreamActive(streamType);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
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
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAnyStreamActive();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
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
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkStreamCanPlay(streamType);
                    }
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
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMusicLimitMode(mode);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isMusicLimitMode();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isStreamMute(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isStreamMute(streamType);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void forceRemoteSubmixFullVolume(boolean startForcing, IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(startForcing ? 1 : 0);
                    _data.writeStrongBinder(cb);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().forceRemoteSubmixFullVolume(startForcing, cb);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isMasterMute() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isMasterMute();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setMasterMute(boolean mute, int flags, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mute ? 1 : 0);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMasterMute(mute, flags, callingPackage, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getStreamVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStreamVolume(streamType);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getStreamMinVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStreamMinVolume(streamType);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getStreamMaxVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStreamMaxVolume(streamType);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public List<AudioVolumeGroup> getAudioVolumeGroups() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAudioVolumeGroups();
                    }
                    _reply.readException();
                    List<AudioVolumeGroup> _result = _reply.createTypedArrayList(AudioVolumeGroup.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setVolumeIndexForAttributes(AudioAttributes aa, int index, int flags, String callingPackage) throws RemoteException {
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
                    _data.writeInt(index);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVolumeIndexForAttributes(aa, index, flags, callingPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVolumeIndexForAttributes(aa);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getMaxVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMaxVolumeIndexForAttributes(aa);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getMinVolumeIndexForAttributes(AudioAttributes aa) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMinVolumeIndexForAttributes(aa);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getLastAudibleStreamVolume(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLastAudibleStreamVolume(streamType);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public List<AudioProductStrategy> getAudioProductStrategies() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAudioProductStrategies();
                    }
                    _reply.readException();
                    List<AudioProductStrategy> _result = _reply.createTypedArrayList(AudioProductStrategy.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setMicrophoneMute(boolean on, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMicrophoneMute(on, callingPackage, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setRingerModeExternal(int ringerMode, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ringerMode);
                    _data.writeString(caller);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRingerModeExternal(ringerMode, caller);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setRingerModeInternal(int ringerMode, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ringerMode);
                    _data.writeString(caller);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRingerModeInternal(ringerMode, caller);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getRingerModeExternal() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRingerModeExternal();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getRingerModeInternal() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRingerModeInternal();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isValidRingerMode(int ringerMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(ringerMode);
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isValidRingerMode(ringerMode);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setVibrateSetting(int vibrateType, int vibrateSetting) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(vibrateType);
                    _data.writeInt(vibrateSetting);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVibrateSetting(vibrateType, vibrateSetting);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getVibrateSetting(int vibrateType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(vibrateType);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVibrateSetting(vibrateType);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean shouldVibrate(int vibrateType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(vibrateType);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().shouldVibrate(vibrateType);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setMode(int mode, IBinder cb, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeStrongBinder(cb);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMode(mode, cb, callingPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMode();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void playSoundEffect(int effectType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(effectType);
                    boolean _status = this.mRemote.transact(42, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().playSoundEffect(effectType);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void playSoundEffectVolume(int effectType, float volume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(effectType);
                    _data.writeFloat(volume);
                    boolean _status = this.mRemote.transact(43, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().playSoundEffectVolume(effectType, volume);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean loadSoundEffects() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().loadSoundEffects();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unloadSoundEffects() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(45, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unloadSoundEffects();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void reloadAudioSettings() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(46, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reloadAudioSettings();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void avrcpSupportsAbsoluteVolume(String address, boolean support) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(support ? 1 : 0);
                    boolean _status = this.mRemote.transact(47, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().avrcpSupportsAbsoluteVolume(address, support);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setSpeakerphoneOn(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    boolean _status = this.mRemote.transact(48, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSpeakerphoneOn(on);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isSpeakerphoneOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isSpeakerphoneOn();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setBluetoothScoOn(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBluetoothScoOn(on);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isBluetoothScoOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(51, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isBluetoothScoOn();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setBluetoothA2dpOn(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    boolean _status = this.mRemote.transact(52, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBluetoothA2dpOn(on);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isBluetoothA2dpOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(53, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isBluetoothA2dpOn();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int requestAudioFocus(AudioAttributes aa, int durationHint, IBinder cb, IAudioFocusDispatcher fd, String clientId, String callingPackageName, int flags, IAudioPolicyCallback pcb, int sdk) throws RemoteException {
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
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(durationHint);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeStrongBinder(cb);
                    _data.writeStrongBinder(fd != null ? fd.asBinder() : null);
                    _data.writeString(clientId);
                    _data.writeString(callingPackageName);
                    _data.writeInt(flags);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    _data.writeInt(sdk);
                    boolean _status = this.mRemote.transact(54, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int requestAudioFocus = Stub.getDefaultImpl().requestAudioFocus(aa, durationHint, cb, fd, clientId, callingPackageName, flags, pcb, sdk);
                        _reply.recycle();
                        _data.recycle();
                        return requestAudioFocus;
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.media.IAudioService
            public int abandonAudioFocus(IAudioFocusDispatcher fd, String clientId, AudioAttributes aa, String callingPackageName) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(55, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().abandonAudioFocus(fd, clientId, aa, callingPackageName);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unregisterAudioFocusClient(String clientId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(clientId);
                    boolean _status = this.mRemote.transact(56, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterAudioFocusClient(clientId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getCurrentAudioFocus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(57, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentAudioFocus();
                    }
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
                    boolean _status = this.mRemote.transact(58, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentAudioFocusAttributes();
                    }
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
                    boolean _status = this.mRemote.transact(59, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentAudioFocusPackageName();
                    }
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
                    boolean _status = this.mRemote.transact(60, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLastAudioFocusPackageName();
                    }
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
                    boolean _status = this.mRemote.transact(61, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startAudioCapture(audioSession, usage);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(62, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopAudioCapture(audioSession, usage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void startBluetoothSco(IBinder cb, int targetSdkVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb);
                    _data.writeInt(targetSdkVersion);
                    boolean _status = this.mRemote.transact(63, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startBluetoothSco(cb, targetSdkVersion);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void startBluetoothScoVirtualCall(IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb);
                    boolean _status = this.mRemote.transact(64, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startBluetoothScoVirtualCall(cb);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void stopBluetoothSco(IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb);
                    boolean _status = this.mRemote.transact(65, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopBluetoothSco(cb);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void forceVolumeControlStream(int streamType, IBinder cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    _data.writeStrongBinder(cb);
                    boolean _status = this.mRemote.transact(66, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().forceVolumeControlStream(streamType, cb);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setRingtonePlayer(IRingtonePlayer player) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(player != null ? player.asBinder() : null);
                    boolean _status = this.mRemote.transact(67, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRingtonePlayer(player);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public IRingtonePlayer getRingtonePlayer() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(68, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRingtonePlayer();
                    }
                    _reply.readException();
                    IRingtonePlayer _result = IRingtonePlayer.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getUiSoundsStreamType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(69, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUiSoundsStreamType();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setWiredDeviceConnectionState(int type, int state, String address, String name, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(state);
                    _data.writeString(address);
                    _data.writeString(name);
                    _data.writeString(caller);
                    boolean _status = this.mRemote.transact(70, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setWiredDeviceConnectionState(type, state, address, name, caller);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void handleBluetoothA2dpDeviceConfigChange(BluetoothDevice device) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(71, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().handleBluetoothA2dpDeviceConfigChange(device);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public AudioRoutesInfo startWatchingRoutes(IAudioRoutesObserver observer) throws RemoteException {
                AudioRoutesInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    boolean _status = this.mRemote.transact(72, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startWatchingRoutes(observer);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = AudioRoutesInfo.CREATOR.createFromParcel(_reply);
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
            public boolean isCameraSoundForced() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(73, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCameraSoundForced();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setVolumeController(IVolumeController controller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(controller != null ? controller.asBinder() : null);
                    boolean _status = this.mRemote.transact(74, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVolumeController(controller);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void notifyVolumeControllerVisible(IVolumeController controller, boolean visible) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(controller != null ? controller.asBinder() : null);
                    _data.writeInt(visible ? 1 : 0);
                    boolean _status = this.mRemote.transact(75, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyVolumeControllerVisible(controller, visible);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isStreamAffectedByRingerMode(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean _status = this.mRemote.transact(76, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isStreamAffectedByRingerMode(streamType);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isStreamAffectedByMute(int streamType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    boolean _status = this.mRemote.transact(77, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isStreamAffectedByMute(streamType);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void disableSafeMediaVolume(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _status = this.mRemote.transact(78, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().disableSafeMediaVolume(callingPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int setHdmiSystemAudioSupported(boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(on ? 1 : 0);
                    boolean _status = this.mRemote.transact(79, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setHdmiSystemAudioSupported(on);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isHdmiSystemAudioSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(80, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isHdmiSystemAudioSupported();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public String registerAudioPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb, boolean hasFocusListener, boolean isFocusPolicy, boolean isTestFocusPolicy, boolean isVolumeController, IMediaProjection projection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (policyConfig != null) {
                        _data.writeInt(1);
                        policyConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    _data.writeInt(hasFocusListener ? 1 : 0);
                    _data.writeInt(isFocusPolicy ? 1 : 0);
                    _data.writeInt(isTestFocusPolicy ? 1 : 0);
                    if (!isVolumeController) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeStrongBinder(projection != null ? projection.asBinder() : null);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    boolean _status = this.mRemote.transact(81, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        String registerAudioPolicy = Stub.getDefaultImpl().registerAudioPolicy(policyConfig, pcb, hasFocusListener, isFocusPolicy, isTestFocusPolicy, isVolumeController, projection);
                        _reply.recycle();
                        _data.recycle();
                        return registerAudioPolicy;
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.media.IAudioService
            public void unregisterAudioPolicyAsync(IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    boolean _status = this.mRemote.transact(82, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterAudioPolicyAsync(pcb);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unregisterAudioPolicy(IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    boolean _status = this.mRemote.transact(83, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterAudioPolicy(pcb);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int addMixForPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(84, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().addMixForPolicy(policyConfig, pcb);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int removeMixForPolicy(AudioPolicyConfig policyConfig, IAudioPolicyCallback pcb) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(85, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeMixForPolicy(policyConfig, pcb);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int setFocusPropertiesForPolicy(int duckingBehavior, IAudioPolicyCallback pcb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(duckingBehavior);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    boolean _status = this.mRemote.transact(86, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setFocusPropertiesForPolicy(duckingBehavior, pcb);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setVolumePolicy(VolumePolicy policy) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(87, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVolumePolicy(policy);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean hasRegisteredDynamicPolicy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(88, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasRegisteredDynamicPolicy();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void registerRecordingCallback(IRecordingConfigDispatcher rcdb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(rcdb != null ? rcdb.asBinder() : null);
                    boolean _status = this.mRemote.transact(89, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerRecordingCallback(rcdb);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unregisterRecordingCallback(IRecordingConfigDispatcher rcdb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(rcdb != null ? rcdb.asBinder() : null);
                    boolean _status = this.mRemote.transact(90, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterRecordingCallback(rcdb);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public List<AudioRecordingConfiguration> getActiveRecordingConfigurations() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(91, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActiveRecordingConfigurations();
                    }
                    _reply.readException();
                    List<AudioRecordingConfiguration> _result = _reply.createTypedArrayList(AudioRecordingConfiguration.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void registerPlaybackCallback(IPlaybackConfigDispatcher pcdb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcdb != null ? pcdb.asBinder() : null);
                    boolean _status = this.mRemote.transact(92, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerPlaybackCallback(pcdb);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unregisterPlaybackCallback(IPlaybackConfigDispatcher pcdb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcdb != null ? pcdb.asBinder() : null);
                    boolean _status = this.mRemote.transact(93, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterPlaybackCallback(pcdb);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public List<AudioPlaybackConfiguration> getActivePlaybackConfigurations() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(94, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActivePlaybackConfigurations();
                    }
                    _reply.readException();
                    List<AudioPlaybackConfiguration> _result = _reply.createTypedArrayList(AudioPlaybackConfiguration.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void disableRingtoneSync(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(95, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().disableRingtoneSync(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getFocusRampTimeMs(int focusGain, AudioAttributes attr) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(96, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getFocusRampTimeMs(focusGain, attr);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int dispatchFocusChange(AudioFocusInfo afi, int focusChange, IAudioPolicyCallback pcb) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(97, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().dispatchFocusChange(afi, focusChange, pcb);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void playerHasOpPlayAudio(int piid, boolean hasOpPlayAudio) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(piid);
                    _data.writeInt(hasOpPlayAudio ? 1 : 0);
                    boolean _status = this.mRemote.transact(98, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().playerHasOpPlayAudio(piid, hasOpPlayAudio);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setBluetoothHearingAidDeviceConnectionState(BluetoothDevice device, int state, boolean suppressNoisyIntent, int musicDevice) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(state);
                    if (!suppressNoisyIntent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(musicDevice);
                    boolean _status = this.mRemote.transact(99, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBluetoothHearingAidDeviceConnectionState(device, state, suppressNoisyIntent, musicDevice);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(BluetoothDevice device, int state, int profile, boolean suppressNoisyIntent, int a2dpVolume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(state);
                    _data.writeInt(profile);
                    if (!suppressNoisyIntent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(a2dpVolume);
                    boolean _status = this.mRemote.transact(100, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(device, state, profile, suppressNoisyIntent, a2dpVolume);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setFocusRequestResultFromExtPolicy(AudioFocusInfo afi, int requestResult, IAudioPolicyCallback pcb) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(101, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setFocusRequestResultFromExtPolicy(afi, requestResult, pcb);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void registerAudioServerStateDispatcher(IAudioServerStateDispatcher asd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(asd != null ? asd.asBinder() : null);
                    boolean _status = this.mRemote.transact(102, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerAudioServerStateDispatcher(asd);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void unregisterAudioServerStateDispatcher(IAudioServerStateDispatcher asd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(asd != null ? asd.asBinder() : null);
                    boolean _status = this.mRemote.transact(103, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterAudioServerStateDispatcher(asd);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean isAudioServerRunning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(104, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAudioServerRunning();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int setUidDeviceAffinity(IAudioPolicyCallback pcb, int uid, int[] deviceTypes, String[] deviceAddresses) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    _data.writeInt(uid);
                    _data.writeIntArray(deviceTypes);
                    _data.writeStringArray(deviceAddresses);
                    boolean _status = this.mRemote.transact(105, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setUidDeviceAffinity(pcb, uid, deviceTypes, deviceAddresses);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int removeUidDeviceAffinity(IAudioPolicyCallback pcb, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(106, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeUidDeviceAffinity(pcb, uid);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean hasHapticChannels(Uri uri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(107, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasHapticChannels(uri);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int setAllowedCapturePolicy(int capturePolicy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(capturePolicy);
                    boolean _status = this.mRemote.transact(108, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setAllowedCapturePolicy(capturePolicy);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getAllowedCapturePolicy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(109, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAllowedCapturePolicy();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
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
                    boolean _status = this.mRemote.transact(110, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSoundField(mode, x, y);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(111, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSoundField(mode);
                    }
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
                    boolean _status = this.mRemote.transact(112, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSoundEffectMode();
                    }
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
                    boolean _status = this.mRemote.transact(113, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSoundEffectMode(mode);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(114, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSoundEffectType(mode, type);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(115, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSoundEffectType(mode);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
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
                    boolean _status = this.mRemote.transact(116, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSoundEffectScene(mode, type);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(117, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSoundEffectScene(mode);
                    }
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
                    boolean _status = this.mRemote.transact(118, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSoundEffectParms(effectType, nativeValue, softValue, innervationValue);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(119, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSoundEffectParms(effectType, modeType);
                    }
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
            public void setSoundSpeedLinkLevel(int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(level);
                    boolean _status = this.mRemote.transact(120, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSoundSpeedLinkLevel(level);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getSoundSpeedLinkLevel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(121, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSoundSpeedLinkLevel();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setDyn3dEffectLevel(int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(level);
                    boolean _status = this.mRemote.transact(122, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDyn3dEffectLevel(level);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getDyn3dEffectLevel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(123, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDyn3dEffectLevel();
                    }
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
                    boolean _status = this.mRemote.transact(124, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setNavVolDecreaseEnable(enable);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(125, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getNavVolDecreaseEnable();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
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
                    boolean _status = this.mRemote.transact(126, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setXpCustomizeEffect(type, value);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(127, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getXpCustomizeEffect(type);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void flushXpCustomizeEffects(int[] values) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(values);
                    boolean _status = this.mRemote.transact(128, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().flushXpCustomizeEffects(values);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(129, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enableSystemSound();
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(130, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().disableSystemSound();
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(131, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isSystemSoundEnabled();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
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
                    boolean _status = this.mRemote.transact(132, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUsageActive(usage);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
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
                    boolean _status = this.mRemote.transact(133, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setStereoAlarm(enable);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(134, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSpeechSurround(enable);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(135, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMainDriver(enable);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(136, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBtHeadPhone(enable);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(137, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isStereoAlarmOn();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
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
                    boolean _status = this.mRemote.transact(138, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isSpeechSurroundOn();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
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
                    boolean _status = this.mRemote.transact(139, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isMainDriverOn();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
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
                    boolean _status = this.mRemote.transact(140, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isBtHeadPhoneOn();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
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
                    boolean _status = this.mRemote.transact(141, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().selectAlarmChannels(location, fadeTimeMs, soundid);
                    }
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
                    boolean _status = this.mRemote.transact(142, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().checkAlarmVolume();
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(143, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBtCallOn(enable);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(144, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isBtCallOn();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
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
                    boolean _status = this.mRemote.transact(145, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setKaraokeOn(on);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(146, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isKaraokeOn();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
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
                    boolean _status = this.mRemote.transact(147, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isOtherSessionOn();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
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
                    boolean _status = this.mRemote.transact(148, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOtherMusicPlayingPkgs();
                    }
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
                    boolean _status = this.mRemote.transact(149, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVoiceStatus(status);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(150, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVoiceStatus();
                    }
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
                    boolean _status = this.mRemote.transact(151, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVoicePosition(position, flag, pkgName);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(152, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVoicePosition();
                    }
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
                    boolean _status = this.mRemote.transact(153, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setFixedVolume(enable, vol, streamType, callingPackage);
                    }
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
                    boolean _status = this.mRemote.transact(154, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isFixedVolume(streamType);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
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
                    boolean _status = this.mRemote.transact(155, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().doZenVolumeProcess(enable, callingPackage);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(156, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isZenVolume();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
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
                    boolean _status = this.mRemote.transact(157, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().lockActiveStream(lock);
                    }
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
                    boolean _status = this.mRemote.transact(158, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setVolumeFaded(StreamType, vol, fadetime, callingPackage);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(159, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startSpeechEffect(audioSession);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(160, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopSpeechEffect(audioSession);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(161, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().forceChangeToAmpChannel(channelBits, activeBits, volume, stop);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(162, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().restoreMusicVolume(callingPackage);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(163, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMainDriverMode(mode);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(164, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMainDriverMode();
                    }
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
                    boolean _status = this.mRemote.transact(165, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRingtoneSessionId(streamType, sessionid, pkgName);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(166, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBanVolumeChangeMode(streamType, mode, pkgName);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(167, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBanVolumeChangeMode(streamType);
                    }
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
                    boolean _status = this.mRemote.transact(168, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBtCallMode(mode);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(169, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBtCallMode();
                    }
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
                    boolean _status = this.mRemote.transact(170, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().playbackControl(cmd, param);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(171, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBtCallOnFlag(flag);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setNetEcallEnable(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    boolean _status = this.mRemote.transact(172, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setNetEcallEnable(enable);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(173, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerCallback(pkgName, callBackFunc);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(174, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterCallback(pkgName, callBackFunc);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(175, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBtCallOnFlag();
                    }
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
                    boolean _status = this.mRemote.transact(176, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDangerousTtsStatus(on);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(177, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDangerousTtsStatus();
                    }
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
                    boolean _status = this.mRemote.transact(178, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDangerousTtsVolLevel(level);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(179, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDangerousTtsVolLevel();
                    }
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
                    boolean _status = this.mRemote.transact(180, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().ChangeChannelByTrack(usage, id, start);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(181, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().temporaryChangeVolumeDown(StreamType, dstVol, restoreVol, flag, packageName);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(182, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().applyAlarmId(usage, id);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(183, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAudioFocusPackageNameList();
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public List<String> getAudioFocusPackageNameListByPosition(int position) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(position);
                    boolean _status = this.mRemote.transact(184, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAudioFocusPackageNameListByPosition(position);
                    }
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
                    boolean _status = this.mRemote.transact(185, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().audioThreadProcess(type, usage, streamType, Ppid, pkgName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setSessionIdStatus(int sessionId, int position, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionId);
                    _data.writeInt(position);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(186, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSessionIdStatus(sessionId, position, status);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int requestAudioFocusPosition(AudioAttributes aa, int durationHint, IBinder cb, IAudioFocusDispatcher fd, String clientId, int position, int flags, IAudioPolicyCallback pcb, int sdk) throws RemoteException {
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
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(durationHint);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeStrongBinder(cb);
                    _data.writeStrongBinder(fd != null ? fd.asBinder() : null);
                    _data.writeString(clientId);
                    _data.writeInt(position);
                    _data.writeInt(flags);
                    _data.writeStrongBinder(pcb != null ? pcb.asBinder() : null);
                    _data.writeInt(sdk);
                    boolean _status = this.mRemote.transact(187, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        int requestAudioFocusPosition = Stub.getDefaultImpl().requestAudioFocusPosition(aa, durationHint, cb, fd, clientId, position, flags, pcb, sdk);
                        _reply.recycle();
                        _data.recycle();
                        return requestAudioFocusPosition;
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.media.IAudioService
            public void changeAudioFocusPosition(String clientId, int position) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(clientId);
                    _data.writeInt(position);
                    boolean _status = this.mRemote.transact(188, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().changeAudioFocusPosition(clientId, position);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setStreamPosition(int streamType, String pkgName, int position, int id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(streamType);
                    _data.writeString(pkgName);
                    _data.writeInt(position);
                    _data.writeInt(id);
                    boolean _status = this.mRemote.transact(189, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setStreamPosition(streamType, pkgName, position, id);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setSoundPositionEnable(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    boolean _status = this.mRemote.transact(190, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSoundPositionEnable(enable);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean getSoundPositionEnable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(191, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSoundPositionEnable();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setMassageSeatLevel(List<String> levelList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(levelList);
                    boolean _status = this.mRemote.transact(192, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMassageSeatLevel(levelList);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setMusicSeatEnable(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    boolean _status = this.mRemote.transact(193, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMusicSeatEnable(enable);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean getMusicSeatEnable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(194, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMusicSeatEnable();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setMusicSeatRythmPause(boolean pause) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pause ? 1 : 0);
                    boolean _status = this.mRemote.transact(195, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMusicSeatRythmPause(pause);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setMusicSeatEffect(int index) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(index);
                    boolean _status = this.mRemote.transact(196, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMusicSeatEffect(index);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public int getMusicSeatEffect() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(197, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMusicSeatEffect();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setMmapToAvasEnable(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    boolean _status = this.mRemote.transact(198, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMmapToAvasEnable(enable);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean getMmapToAvasEnable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(199, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMmapToAvasEnable();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setSpecialOutputId(int outType, int sessionId, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(outType);
                    _data.writeInt(sessionId);
                    _data.writeInt(enable ? 1 : 0);
                    boolean _status = this.mRemote.transact(200, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSpecialOutputId(outType, sessionId, enable);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void playAvasSound(int position, String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(position);
                    _data.writeString(path);
                    boolean _status = this.mRemote.transact(201, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().playAvasSound(position, path);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void stopAvasSound(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    boolean _status = this.mRemote.transact(202, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopAvasSound(path);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setSoftTypeVolumeMute(int type, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeInt(enable ? 1 : 0);
                    boolean _status = this.mRemote.transact(203, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSoftTypeVolumeMute(type, enable);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public boolean checkPlayingRouteByPackage(int type, String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(pkgName);
                    boolean _status = this.mRemote.transact(204, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkPlayingRouteByPackage(type, pkgName);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.media.IAudioService
            public void setAudioPathWhiteList(int type, String writeList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(writeList);
                    boolean _status = this.mRemote.transact(205, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAudioPathWhiteList(type, writeList);
                    } else {
                        _reply.readException();
                    }
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
                    boolean _status = this.mRemote.transact(206, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getXMicService();
                    }
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
                    boolean _status = this.mRemote.transact(207, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActiveSessionList();
                    }
                    _reply.readException();
                    List<xpAudioSessionInfo> _result = _reply.createTypedArrayList(xpAudioSessionInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IAudioService impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IAudioService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
