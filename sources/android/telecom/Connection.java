package android.telecom;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.bluetooth.BluetoothDevice;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.telecom.Conference;
import android.telecom.Connection;
import android.telecom.VideoProfile;
import android.telephony.ServiceState;
import android.util.ArraySet;
import android.view.Surface;
import com.android.internal.os.SomeArgs;
import com.android.internal.telecom.IVideoCallback;
import com.android.internal.telecom.IVideoProvider;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.transition.EpicenterTranslateClipReveal;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/* loaded from: classes2.dex */
public abstract class Connection extends Conferenceable {
    public static final int CAPABILITY_CANNOT_DOWNGRADE_VIDEO_TO_AUDIO = 8388608;
    public static final int CAPABILITY_CAN_PAUSE_VIDEO = 1048576;
    public static final int CAPABILITY_CAN_PULL_CALL = 16777216;
    public static final int CAPABILITY_CAN_SEND_RESPONSE_VIA_CONNECTION = 4194304;
    public static final int CAPABILITY_CAN_UPGRADE_TO_VIDEO = 524288;
    public static final int CAPABILITY_CONFERENCE_HAS_NO_CHILDREN = 2097152;
    public static final int CAPABILITY_DISCONNECT_FROM_CONFERENCE = 8192;
    public static final int CAPABILITY_HOLD = 1;
    public static final int CAPABILITY_MANAGE_CONFERENCE = 128;
    public static final int CAPABILITY_MERGE_CONFERENCE = 4;
    public static final int CAPABILITY_MUTE = 64;
    public static final int CAPABILITY_RESPOND_VIA_TEXT = 32;
    public static final int CAPABILITY_SEPARATE_FROM_CONFERENCE = 4096;
    public static final int CAPABILITY_SPEED_UP_MT_AUDIO = 262144;
    public static final int CAPABILITY_SUPPORTS_VT_LOCAL_BIDIRECTIONAL = 768;
    public static final int CAPABILITY_SUPPORTS_VT_LOCAL_RX = 256;
    public static final int CAPABILITY_SUPPORTS_VT_LOCAL_TX = 512;
    public static final int CAPABILITY_SUPPORTS_VT_REMOTE_BIDIRECTIONAL = 3072;
    public static final int CAPABILITY_SUPPORTS_VT_REMOTE_RX = 1024;
    public static final int CAPABILITY_SUPPORTS_VT_REMOTE_TX = 2048;
    public static final int CAPABILITY_SUPPORT_DEFLECT = 33554432;
    public static final int CAPABILITY_SUPPORT_HOLD = 2;
    public static final int CAPABILITY_SWAP_CONFERENCE = 8;
    public static final int CAPABILITY_UNUSED = 16;
    public static final int CAPABILITY_UNUSED_2 = 16384;
    public static final int CAPABILITY_UNUSED_3 = 32768;
    public static final int CAPABILITY_UNUSED_4 = 65536;
    public static final int CAPABILITY_UNUSED_5 = 131072;
    public static final String EVENT_CALL_HOLD_FAILED = "android.telecom.event.CALL_HOLD_FAILED";
    public static final String EVENT_CALL_MERGE_FAILED = "android.telecom.event.CALL_MERGE_FAILED";
    public static final String EVENT_CALL_PULL_FAILED = "android.telecom.event.CALL_PULL_FAILED";
    public static final String EVENT_CALL_REMOTELY_HELD = "android.telecom.event.CALL_REMOTELY_HELD";
    public static final String EVENT_CALL_REMOTELY_UNHELD = "android.telecom.event.CALL_REMOTELY_UNHELD";
    public static final String EVENT_HANDOVER_COMPLETE = "android.telecom.event.HANDOVER_COMPLETE";
    public static final String EVENT_HANDOVER_FAILED = "android.telecom.event.HANDOVER_FAILED";
    public static final String EVENT_MERGE_COMPLETE = "android.telecom.event.MERGE_COMPLETE";
    public static final String EVENT_MERGE_START = "android.telecom.event.MERGE_START";
    public static final String EVENT_ON_HOLD_TONE_END = "android.telecom.event.ON_HOLD_TONE_END";
    public static final String EVENT_ON_HOLD_TONE_START = "android.telecom.event.ON_HOLD_TONE_START";
    public static final String EVENT_RTT_AUDIO_INDICATION_CHANGED = "android.telecom.event.RTT_AUDIO_INDICATION_CHANGED";
    public static final String EXTRA_ANSWERING_DROPS_FG_CALL = "android.telecom.extra.ANSWERING_DROPS_FG_CALL";
    public static final String EXTRA_ANSWERING_DROPS_FG_CALL_APP_NAME = "android.telecom.extra.ANSWERING_DROPS_FG_CALL_APP_NAME";
    public static final String EXTRA_CALL_SUBJECT = "android.telecom.extra.CALL_SUBJECT";
    public static final String EXTRA_CHILD_ADDRESS = "android.telecom.extra.CHILD_ADDRESS";
    public static final String EXTRA_DISABLE_ADD_CALL = "android.telecom.extra.DISABLE_ADD_CALL";
    public static final String EXTRA_IS_RTT_AUDIO_PRESENT = "android.telecom.extra.IS_RTT_AUDIO_PRESENT";
    public static final String EXTRA_LAST_FORWARDED_NUMBER = "android.telecom.extra.LAST_FORWARDED_NUMBER";
    public static final String EXTRA_ORIGINAL_CONNECTION_ID = "android.telecom.extra.ORIGINAL_CONNECTION_ID";
    public static final String EXTRA_SIP_INVITE = "android.telecom.extra.SIP_INVITE";
    private static final boolean PII_DEBUG = Log.isLoggable(3);
    public static final int PROPERTY_ASSISTED_DIALING_USED = 512;
    public static final int PROPERTY_EMERGENCY_CALLBACK_MODE = 1;
    public static final int PROPERTY_GENERIC_CONFERENCE = 2;
    public static final int PROPERTY_HAS_CDMA_VOICE_PRIVACY = 32;
    public static final int PROPERTY_HIGH_DEF_AUDIO = 4;
    public static final int PROPERTY_IS_DOWNGRADED_CONFERENCE = 64;
    public static final int PROPERTY_IS_EXTERNAL_CALL = 16;
    public static final int PROPERTY_IS_RTT = 256;
    public static final int PROPERTY_NETWORK_IDENTIFIED_EMERGENCY_CALL = 1024;
    public static final int PROPERTY_REMOTELY_HOSTED = 2048;
    public static final int PROPERTY_SELF_MANAGED = 128;
    public static final int PROPERTY_WIFI = 8;
    public static final int STATE_ACTIVE = 4;
    public static final int STATE_DIALING = 3;
    public static final int STATE_DISCONNECTED = 6;
    public static final int STATE_HOLDING = 5;
    public static final int STATE_INITIALIZING = 0;
    public static final int STATE_NEW = 1;
    public static final int STATE_PULLING_CALL = 7;
    public static final int STATE_RINGING = 2;
    private Uri mAddress;
    private int mAddressPresentation;
    private boolean mAudioModeIsVoip;
    private CallAudioState mCallAudioState;
    private String mCallerDisplayName;
    private int mCallerDisplayNamePresentation;
    private Conference mConference;
    private int mConnectionCapabilities;
    private int mConnectionProperties;
    private ConnectionService mConnectionService;
    private DisconnectCause mDisconnectCause;
    private Bundle mExtras;
    private PhoneAccountHandle mPhoneAccountHandle;
    private Set<String> mPreviousExtraKeys;
    private StatusHints mStatusHints;
    private String mTelecomCallId;
    private VideoProvider mVideoProvider;
    private int mVideoState;
    private final Listener mConnectionDeathListener = new Listener() { // from class: android.telecom.Connection.1
        @Override // android.telecom.Connection.Listener
        public void onDestroyed(Connection c) {
            if (Connection.this.mConferenceables.remove(c)) {
                Connection.this.fireOnConferenceableConnectionsChanged();
            }
        }
    };
    private final Conference.Listener mConferenceDeathListener = new Conference.Listener() { // from class: android.telecom.Connection.2
        @Override // android.telecom.Conference.Listener
        public void onDestroyed(Conference c) {
            if (Connection.this.mConferenceables.remove(c)) {
                Connection.this.fireOnConferenceableConnectionsChanged();
            }
        }
    };
    private final Set<Listener> mListeners = Collections.newSetFromMap(new ConcurrentHashMap(8, 0.9f, 1));
    private final List<Conferenceable> mConferenceables = new ArrayList();
    private final List<Conferenceable> mUnmodifiableConferenceables = Collections.unmodifiableList(this.mConferenceables);
    private int mState = 1;
    private boolean mRingbackRequested = false;
    private int mSupportedAudioRoutes = 15;
    private long mConnectTimeMillis = 0;
    private long mConnectElapsedTimeMillis = 0;
    private final Object mExtrasLock = new Object();
    private int mCallDirection = -1;

    public static boolean can(int capabilities, int capability) {
        return (capabilities & capability) == capability;
    }

    public boolean can(int capability) {
        return can(this.mConnectionCapabilities, capability);
    }

    public void removeCapability(int capability) {
        this.mConnectionCapabilities &= ~capability;
    }

    public void addCapability(int capability) {
        this.mConnectionCapabilities |= capability;
    }

    public static String capabilitiesToString(int capabilities) {
        return capabilitiesToStringInternal(capabilities, true);
    }

    public static String capabilitiesToStringShort(int capabilities) {
        return capabilitiesToStringInternal(capabilities, false);
    }

    private static String capabilitiesToStringInternal(int capabilities, boolean isLong) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if (isLong) {
            builder.append("Capabilities:");
        }
        if (can(capabilities, 1)) {
            builder.append(isLong ? " CAPABILITY_HOLD" : " hld");
        }
        if (can(capabilities, 2)) {
            builder.append(isLong ? " CAPABILITY_SUPPORT_HOLD" : " sup_hld");
        }
        if (can(capabilities, 4)) {
            builder.append(isLong ? " CAPABILITY_MERGE_CONFERENCE" : " mrg_cnf");
        }
        if (can(capabilities, 8)) {
            builder.append(isLong ? " CAPABILITY_SWAP_CONFERENCE" : " swp_cnf");
        }
        if (can(capabilities, 32)) {
            builder.append(isLong ? " CAPABILITY_RESPOND_VIA_TEXT" : " txt");
        }
        if (can(capabilities, 64)) {
            builder.append(isLong ? " CAPABILITY_MUTE" : " mut");
        }
        if (can(capabilities, 128)) {
            builder.append(isLong ? " CAPABILITY_MANAGE_CONFERENCE" : " mng_cnf");
        }
        if (can(capabilities, 256)) {
            builder.append(isLong ? " CAPABILITY_SUPPORTS_VT_LOCAL_RX" : " VTlrx");
        }
        if (can(capabilities, 512)) {
            builder.append(isLong ? " CAPABILITY_SUPPORTS_VT_LOCAL_TX" : " VTltx");
        }
        if (can(capabilities, 768)) {
            builder.append(isLong ? " CAPABILITY_SUPPORTS_VT_LOCAL_BIDIRECTIONAL" : " VTlbi");
        }
        if (can(capabilities, 1024)) {
            builder.append(isLong ? " CAPABILITY_SUPPORTS_VT_REMOTE_RX" : " VTrrx");
        }
        if (can(capabilities, 2048)) {
            builder.append(isLong ? " CAPABILITY_SUPPORTS_VT_REMOTE_TX" : " VTrtx");
        }
        if (can(capabilities, 3072)) {
            builder.append(isLong ? " CAPABILITY_SUPPORTS_VT_REMOTE_BIDIRECTIONAL" : " VTrbi");
        }
        if (can(capabilities, 8388608)) {
            builder.append(isLong ? " CAPABILITY_CANNOT_DOWNGRADE_VIDEO_TO_AUDIO" : " !v2a");
        }
        if (can(capabilities, 262144)) {
            builder.append(isLong ? " CAPABILITY_SPEED_UP_MT_AUDIO" : " spd_aud");
        }
        if (can(capabilities, 524288)) {
            builder.append(isLong ? " CAPABILITY_CAN_UPGRADE_TO_VIDEO" : " a2v");
        }
        if (can(capabilities, 1048576)) {
            builder.append(isLong ? " CAPABILITY_CAN_PAUSE_VIDEO" : " paus_VT");
        }
        if (can(capabilities, 2097152)) {
            builder.append(isLong ? " CAPABILITY_SINGLE_PARTY_CONFERENCE" : " 1p_cnf");
        }
        if (can(capabilities, 4194304)) {
            builder.append(isLong ? " CAPABILITY_CAN_SEND_RESPONSE_VIA_CONNECTION" : " rsp_by_con");
        }
        if (can(capabilities, 16777216)) {
            builder.append(isLong ? " CAPABILITY_CAN_PULL_CALL" : " pull");
        }
        if (can(capabilities, 33554432)) {
            builder.append(isLong ? " CAPABILITY_SUPPORT_DEFLECT" : " sup_def");
        }
        builder.append("]");
        return builder.toString();
    }

    public static String propertiesToString(int properties) {
        return propertiesToStringInternal(properties, true);
    }

    public static String propertiesToStringShort(int properties) {
        return propertiesToStringInternal(properties, false);
    }

    private static String propertiesToStringInternal(int properties, boolean isLong) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if (isLong) {
            builder.append("Properties:");
        }
        if (can(properties, 128)) {
            builder.append(isLong ? " PROPERTY_SELF_MANAGED" : " self_mng");
        }
        if (can(properties, 1)) {
            builder.append(isLong ? " PROPERTY_EMERGENCY_CALLBACK_MODE" : " ecbm");
        }
        if (can(properties, 4)) {
            builder.append(isLong ? " PROPERTY_HIGH_DEF_AUDIO" : " HD");
        }
        if (can(properties, 8)) {
            builder.append(isLong ? " PROPERTY_WIFI" : " wifi");
        }
        if (can(properties, 2)) {
            builder.append(isLong ? " PROPERTY_GENERIC_CONFERENCE" : " gen_conf");
        }
        if (can(properties, 16)) {
            builder.append(isLong ? " PROPERTY_IS_EXTERNAL_CALL" : " xtrnl");
        }
        if (can(properties, 32)) {
            builder.append(isLong ? " PROPERTY_HAS_CDMA_VOICE_PRIVACY" : " priv");
        }
        if (can(properties, 256)) {
            builder.append(isLong ? " PROPERTY_IS_RTT" : " rtt");
        }
        if (can(properties, 1024)) {
            builder.append(isLong ? " PROPERTY_NETWORK_IDENTIFIED_EMERGENCY_CALL" : " ecall");
        }
        if (can(properties, 2048)) {
            builder.append(isLong ? " PROPERTY_REMOTELY_HOSTED" : " remote_hst");
        }
        builder.append("]");
        return builder.toString();
    }

    /* loaded from: classes2.dex */
    public static abstract class Listener {
        public void onStateChanged(Connection c, int state) {
        }

        public void onAddressChanged(Connection c, Uri newAddress, int presentation) {
        }

        public void onCallerDisplayNameChanged(Connection c, String callerDisplayName, int presentation) {
        }

        public void onVideoStateChanged(Connection c, int videoState) {
        }

        public void onDisconnected(Connection c, DisconnectCause disconnectCause) {
        }

        public void onPostDialWait(Connection c, String remaining) {
        }

        public void onPostDialChar(Connection c, char nextChar) {
        }

        public void onRingbackRequested(Connection c, boolean ringback) {
        }

        public void onDestroyed(Connection c) {
        }

        public void onConnectionCapabilitiesChanged(Connection c, int capabilities) {
        }

        public void onConnectionPropertiesChanged(Connection c, int properties) {
        }

        public void onSupportedAudioRoutesChanged(Connection c, int supportedAudioRoutes) {
        }

        public void onVideoProviderChanged(Connection c, VideoProvider videoProvider) {
        }

        public void onAudioModeIsVoipChanged(Connection c, boolean isVoip) {
        }

        public void onStatusHintsChanged(Connection c, StatusHints statusHints) {
        }

        public void onConferenceablesChanged(Connection c, List<Conferenceable> conferenceables) {
        }

        public void onConferenceChanged(Connection c, Conference conference) {
        }

        public void onConferenceParticipantsChanged(Connection c, List<ConferenceParticipant> participants) {
        }

        public void onConferenceStarted() {
        }

        public void onConferenceMergeFailed(Connection c) {
        }

        public void onExtrasChanged(Connection c, Bundle extras) {
        }

        public void onExtrasRemoved(Connection c, List<String> keys) {
        }

        public void onConnectionEvent(Connection c, String event, Bundle extras) {
        }

        public void onConferenceSupportedChanged(Connection c, boolean isConferenceSupported) {
        }

        public void onAudioRouteChanged(Connection c, int audioRoute, String bluetoothAddress) {
        }

        public void onRttInitiationSuccess(Connection c) {
        }

        public void onRttInitiationFailure(Connection c, int reason) {
        }

        public void onRttSessionRemotelyTerminated(Connection c) {
        }

        public void onRemoteRttRequest(Connection c) {
        }

        public void onPhoneAccountChanged(Connection c, PhoneAccountHandle pHandle) {
        }

        public void onConnectionTimeReset(Connection c) {
        }
    }

    /* loaded from: classes2.dex */
    public static final class RttTextStream {
        private static final int READ_BUFFER_SIZE = 1000;
        private final ParcelFileDescriptor mFdFromInCall;
        private final ParcelFileDescriptor mFdToInCall;
        private final FileInputStream mFromInCallFileInputStream;
        private final InputStreamReader mPipeFromInCall;
        private final OutputStreamWriter mPipeToInCall;
        private char[] mReadBuffer = new char[1000];

        public RttTextStream(ParcelFileDescriptor toInCall, ParcelFileDescriptor fromInCall) {
            this.mFdFromInCall = fromInCall;
            this.mFdToInCall = toInCall;
            this.mFromInCallFileInputStream = new FileInputStream(fromInCall.getFileDescriptor());
            this.mPipeFromInCall = new InputStreamReader(Channels.newInputStream(Channels.newChannel(this.mFromInCallFileInputStream)));
            this.mPipeToInCall = new OutputStreamWriter(new FileOutputStream(toInCall.getFileDescriptor()));
        }

        public void write(String input) throws IOException {
            this.mPipeToInCall.write(input);
            this.mPipeToInCall.flush();
        }

        public String read() throws IOException {
            int numRead = this.mPipeFromInCall.read(this.mReadBuffer, 0, 1000);
            if (numRead < 0) {
                return null;
            }
            return new String(this.mReadBuffer, 0, numRead);
        }

        public String readImmediately() throws IOException {
            if (this.mFromInCallFileInputStream.available() > 0) {
                return read();
            }
            return null;
        }

        public ParcelFileDescriptor getFdFromInCall() {
            return this.mFdFromInCall;
        }

        public ParcelFileDescriptor getFdToInCall() {
            return this.mFdToInCall;
        }
    }

    /* loaded from: classes2.dex */
    public static final class RttModifyStatus {
        public static final int SESSION_MODIFY_REQUEST_FAIL = 2;
        public static final int SESSION_MODIFY_REQUEST_INVALID = 3;
        public static final int SESSION_MODIFY_REQUEST_REJECTED_BY_REMOTE = 5;
        public static final int SESSION_MODIFY_REQUEST_SUCCESS = 1;
        public static final int SESSION_MODIFY_REQUEST_TIMED_OUT = 4;

        private RttModifyStatus() {
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class VideoProvider {
        private static final int MSG_ADD_VIDEO_CALLBACK = 1;
        private static final int MSG_REMOVE_VIDEO_CALLBACK = 12;
        private static final int MSG_REQUEST_CAMERA_CAPABILITIES = 9;
        private static final int MSG_REQUEST_CONNECTION_DATA_USAGE = 10;
        private static final int MSG_SEND_SESSION_MODIFY_REQUEST = 7;
        private static final int MSG_SEND_SESSION_MODIFY_RESPONSE = 8;
        private static final int MSG_SET_CAMERA = 2;
        private static final int MSG_SET_DEVICE_ORIENTATION = 5;
        private static final int MSG_SET_DISPLAY_SURFACE = 4;
        private static final int MSG_SET_PAUSE_IMAGE = 11;
        private static final int MSG_SET_PREVIEW_SURFACE = 3;
        private static final int MSG_SET_ZOOM = 6;
        public static final int SESSION_EVENT_CAMERA_FAILURE = 5;
        private static final String SESSION_EVENT_CAMERA_FAILURE_STR = "CAMERA_FAIL";
        public static final int SESSION_EVENT_CAMERA_PERMISSION_ERROR = 7;
        private static final String SESSION_EVENT_CAMERA_PERMISSION_ERROR_STR = "CAMERA_PERMISSION_ERROR";
        public static final int SESSION_EVENT_CAMERA_READY = 6;
        private static final String SESSION_EVENT_CAMERA_READY_STR = "CAMERA_READY";
        public static final int SESSION_EVENT_RX_PAUSE = 1;
        private static final String SESSION_EVENT_RX_PAUSE_STR = "RX_PAUSE";
        public static final int SESSION_EVENT_RX_RESUME = 2;
        private static final String SESSION_EVENT_RX_RESUME_STR = "RX_RESUME";
        public static final int SESSION_EVENT_TX_START = 3;
        private static final String SESSION_EVENT_TX_START_STR = "TX_START";
        public static final int SESSION_EVENT_TX_STOP = 4;
        private static final String SESSION_EVENT_TX_STOP_STR = "TX_STOP";
        private static final String SESSION_EVENT_UNKNOWN_STR = "UNKNOWN";
        public static final int SESSION_MODIFY_REQUEST_FAIL = 2;
        public static final int SESSION_MODIFY_REQUEST_INVALID = 3;
        public static final int SESSION_MODIFY_REQUEST_REJECTED_BY_REMOTE = 5;
        public static final int SESSION_MODIFY_REQUEST_SUCCESS = 1;
        public static final int SESSION_MODIFY_REQUEST_TIMED_OUT = 4;
        private final VideoProviderBinder mBinder;
        private VideoProviderHandler mMessageHandler;
        private ConcurrentHashMap<IBinder, IVideoCallback> mVideoCallbacks;

        public abstract void onRequestCameraCapabilities();

        public abstract void onRequestConnectionDataUsage();

        public abstract void onSendSessionModifyRequest(VideoProfile videoProfile, VideoProfile videoProfile2);

        public abstract void onSendSessionModifyResponse(VideoProfile videoProfile);

        public abstract void onSetCamera(String str);

        public abstract void onSetDeviceOrientation(int i);

        public abstract void onSetDisplaySurface(Surface surface);

        public abstract void onSetPauseImage(Uri uri);

        public abstract void onSetPreviewSurface(Surface surface);

        public abstract void onSetZoom(float f);

        /* loaded from: classes2.dex */
        private final class VideoProviderHandler extends Handler {
            public VideoProviderHandler() {
            }

            public VideoProviderHandler(Looper looper) {
                super(looper);
            }

            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                SomeArgs args;
                switch (msg.what) {
                    case 1:
                        IBinder binder = (IBinder) msg.obj;
                        IVideoCallback callback = IVideoCallback.Stub.asInterface((IBinder) msg.obj);
                        if (callback != null) {
                            if (!VideoProvider.this.mVideoCallbacks.containsKey(binder)) {
                                VideoProvider.this.mVideoCallbacks.put(binder, callback);
                                return;
                            } else {
                                Log.i(this, "addVideoProvider - skipped; already present.", new Object[0]);
                                return;
                            }
                        }
                        Log.w(this, "addVideoProvider - skipped; callback is null.", new Object[0]);
                        return;
                    case 2:
                        args = (SomeArgs) msg.obj;
                        try {
                            VideoProvider.this.onSetCamera((String) args.arg1);
                            VideoProvider.this.onSetCamera((String) args.arg1, (String) args.arg2, args.argi1, args.argi2, args.argi3);
                            return;
                        } finally {
                        }
                    case 3:
                        VideoProvider.this.onSetPreviewSurface((Surface) msg.obj);
                        return;
                    case 4:
                        VideoProvider.this.onSetDisplaySurface((Surface) msg.obj);
                        return;
                    case 5:
                        VideoProvider.this.onSetDeviceOrientation(msg.arg1);
                        return;
                    case 6:
                        VideoProvider.this.onSetZoom(((Float) msg.obj).floatValue());
                        return;
                    case 7:
                        args = (SomeArgs) msg.obj;
                        try {
                            VideoProvider.this.onSendSessionModifyRequest((VideoProfile) args.arg1, (VideoProfile) args.arg2);
                            return;
                        } finally {
                        }
                    case 8:
                        VideoProvider.this.onSendSessionModifyResponse((VideoProfile) msg.obj);
                        return;
                    case 9:
                        VideoProvider.this.onRequestCameraCapabilities();
                        return;
                    case 10:
                        VideoProvider.this.onRequestConnectionDataUsage();
                        return;
                    case 11:
                        VideoProvider.this.onSetPauseImage((Uri) msg.obj);
                        return;
                    case 12:
                        IBinder binder2 = (IBinder) msg.obj;
                        IVideoCallback.Stub.asInterface((IBinder) msg.obj);
                        if (VideoProvider.this.mVideoCallbacks.containsKey(binder2)) {
                            VideoProvider.this.mVideoCallbacks.remove(binder2);
                            return;
                        } else {
                            Log.i(this, "removeVideoProvider - skipped; not present.", new Object[0]);
                            return;
                        }
                    default:
                        return;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public final class VideoProviderBinder extends IVideoProvider.Stub {
            private VideoProviderBinder() {
            }

            @Override // com.android.internal.telecom.IVideoProvider
            public void addVideoCallback(IBinder videoCallbackBinder) {
                VideoProvider.this.mMessageHandler.obtainMessage(1, videoCallbackBinder).sendToTarget();
            }

            @Override // com.android.internal.telecom.IVideoProvider
            public void removeVideoCallback(IBinder videoCallbackBinder) {
                VideoProvider.this.mMessageHandler.obtainMessage(12, videoCallbackBinder).sendToTarget();
            }

            @Override // com.android.internal.telecom.IVideoProvider
            public void setCamera(String cameraId, String callingPackageName, int targetSdkVersion) {
                SomeArgs args = SomeArgs.obtain();
                args.arg1 = cameraId;
                args.arg2 = callingPackageName;
                args.argi1 = Binder.getCallingUid();
                args.argi2 = Binder.getCallingPid();
                args.argi3 = targetSdkVersion;
                VideoProvider.this.mMessageHandler.obtainMessage(2, args).sendToTarget();
            }

            @Override // com.android.internal.telecom.IVideoProvider
            public void setPreviewSurface(Surface surface) {
                VideoProvider.this.mMessageHandler.obtainMessage(3, surface).sendToTarget();
            }

            @Override // com.android.internal.telecom.IVideoProvider
            public void setDisplaySurface(Surface surface) {
                VideoProvider.this.mMessageHandler.obtainMessage(4, surface).sendToTarget();
            }

            @Override // com.android.internal.telecom.IVideoProvider
            public void setDeviceOrientation(int rotation) {
                VideoProvider.this.mMessageHandler.obtainMessage(5, rotation, 0).sendToTarget();
            }

            @Override // com.android.internal.telecom.IVideoProvider
            public void setZoom(float value) {
                VideoProvider.this.mMessageHandler.obtainMessage(6, Float.valueOf(value)).sendToTarget();
            }

            @Override // com.android.internal.telecom.IVideoProvider
            public void sendSessionModifyRequest(VideoProfile fromProfile, VideoProfile toProfile) {
                SomeArgs args = SomeArgs.obtain();
                args.arg1 = fromProfile;
                args.arg2 = toProfile;
                VideoProvider.this.mMessageHandler.obtainMessage(7, args).sendToTarget();
            }

            @Override // com.android.internal.telecom.IVideoProvider
            public void sendSessionModifyResponse(VideoProfile responseProfile) {
                VideoProvider.this.mMessageHandler.obtainMessage(8, responseProfile).sendToTarget();
            }

            @Override // com.android.internal.telecom.IVideoProvider
            public void requestCameraCapabilities() {
                VideoProvider.this.mMessageHandler.obtainMessage(9).sendToTarget();
            }

            @Override // com.android.internal.telecom.IVideoProvider
            public void requestCallDataUsage() {
                VideoProvider.this.mMessageHandler.obtainMessage(10).sendToTarget();
            }

            @Override // com.android.internal.telecom.IVideoProvider
            public void setPauseImage(Uri uri) {
                VideoProvider.this.mMessageHandler.obtainMessage(11, uri).sendToTarget();
            }
        }

        public VideoProvider() {
            this.mVideoCallbacks = new ConcurrentHashMap<>(8, 0.9f, 1);
            this.mBinder = new VideoProviderBinder();
            this.mMessageHandler = new VideoProviderHandler(Looper.getMainLooper());
        }

        @UnsupportedAppUsage
        public VideoProvider(Looper looper) {
            this.mVideoCallbacks = new ConcurrentHashMap<>(8, 0.9f, 1);
            this.mBinder = new VideoProviderBinder();
            this.mMessageHandler = new VideoProviderHandler(looper);
        }

        public final IVideoProvider getInterface() {
            return this.mBinder;
        }

        public void onSetCamera(String cameraId, String callingPackageName, int callingUid, int callingPid, int targetSdkVersion) {
        }

        public void receiveSessionModifyRequest(VideoProfile videoProfile) {
            ConcurrentHashMap<IBinder, IVideoCallback> concurrentHashMap = this.mVideoCallbacks;
            if (concurrentHashMap != null) {
                for (IVideoCallback callback : concurrentHashMap.values()) {
                    try {
                        callback.receiveSessionModifyRequest(videoProfile);
                    } catch (RemoteException ignored) {
                        Log.w(this, "receiveSessionModifyRequest callback failed", ignored);
                    }
                }
            }
        }

        public void receiveSessionModifyResponse(int status, VideoProfile requestedProfile, VideoProfile responseProfile) {
            ConcurrentHashMap<IBinder, IVideoCallback> concurrentHashMap = this.mVideoCallbacks;
            if (concurrentHashMap != null) {
                for (IVideoCallback callback : concurrentHashMap.values()) {
                    try {
                        callback.receiveSessionModifyResponse(status, requestedProfile, responseProfile);
                    } catch (RemoteException ignored) {
                        Log.w(this, "receiveSessionModifyResponse callback failed", ignored);
                    }
                }
            }
        }

        public void handleCallSessionEvent(int event) {
            ConcurrentHashMap<IBinder, IVideoCallback> concurrentHashMap = this.mVideoCallbacks;
            if (concurrentHashMap != null) {
                for (IVideoCallback callback : concurrentHashMap.values()) {
                    try {
                        callback.handleCallSessionEvent(event);
                    } catch (RemoteException ignored) {
                        Log.w(this, "handleCallSessionEvent callback failed", ignored);
                    }
                }
            }
        }

        public void changePeerDimensions(int width, int height) {
            ConcurrentHashMap<IBinder, IVideoCallback> concurrentHashMap = this.mVideoCallbacks;
            if (concurrentHashMap != null) {
                for (IVideoCallback callback : concurrentHashMap.values()) {
                    try {
                        callback.changePeerDimensions(width, height);
                    } catch (RemoteException ignored) {
                        Log.w(this, "changePeerDimensions callback failed", ignored);
                    }
                }
            }
        }

        public void setCallDataUsage(long dataUsage) {
            ConcurrentHashMap<IBinder, IVideoCallback> concurrentHashMap = this.mVideoCallbacks;
            if (concurrentHashMap != null) {
                for (IVideoCallback callback : concurrentHashMap.values()) {
                    try {
                        callback.changeCallDataUsage(dataUsage);
                    } catch (RemoteException ignored) {
                        Log.w(this, "setCallDataUsage callback failed", ignored);
                    }
                }
            }
        }

        public void changeCallDataUsage(long dataUsage) {
            setCallDataUsage(dataUsage);
        }

        public void changeCameraCapabilities(VideoProfile.CameraCapabilities cameraCapabilities) {
            ConcurrentHashMap<IBinder, IVideoCallback> concurrentHashMap = this.mVideoCallbacks;
            if (concurrentHashMap != null) {
                for (IVideoCallback callback : concurrentHashMap.values()) {
                    try {
                        callback.changeCameraCapabilities(cameraCapabilities);
                    } catch (RemoteException ignored) {
                        Log.w(this, "changeCameraCapabilities callback failed", ignored);
                    }
                }
            }
        }

        public void changeVideoQuality(int videoQuality) {
            ConcurrentHashMap<IBinder, IVideoCallback> concurrentHashMap = this.mVideoCallbacks;
            if (concurrentHashMap != null) {
                for (IVideoCallback callback : concurrentHashMap.values()) {
                    try {
                        callback.changeVideoQuality(videoQuality);
                    } catch (RemoteException ignored) {
                        Log.w(this, "changeVideoQuality callback failed", ignored);
                    }
                }
            }
        }

        public static String sessionEventToString(int event) {
            switch (event) {
                case 1:
                    return SESSION_EVENT_RX_PAUSE_STR;
                case 2:
                    return SESSION_EVENT_RX_RESUME_STR;
                case 3:
                    return SESSION_EVENT_TX_START_STR;
                case 4:
                    return SESSION_EVENT_TX_STOP_STR;
                case 5:
                    return SESSION_EVENT_CAMERA_FAILURE_STR;
                case 6:
                    return SESSION_EVENT_CAMERA_READY_STR;
                case 7:
                    return SESSION_EVENT_CAMERA_PERMISSION_ERROR_STR;
                default:
                    return "UNKNOWN " + event;
            }
        }
    }

    public final String getTelecomCallId() {
        return this.mTelecomCallId;
    }

    public final Uri getAddress() {
        return this.mAddress;
    }

    public final int getAddressPresentation() {
        return this.mAddressPresentation;
    }

    public final String getCallerDisplayName() {
        return this.mCallerDisplayName;
    }

    public final int getCallerDisplayNamePresentation() {
        return this.mCallerDisplayNamePresentation;
    }

    public final int getState() {
        return this.mState;
    }

    public final int getVideoState() {
        return this.mVideoState;
    }

    @SystemApi
    @Deprecated
    public final AudioState getAudioState() {
        CallAudioState callAudioState = this.mCallAudioState;
        if (callAudioState == null) {
            return null;
        }
        return new AudioState(callAudioState);
    }

    public final CallAudioState getCallAudioState() {
        return this.mCallAudioState;
    }

    public final Conference getConference() {
        return this.mConference;
    }

    public final boolean isRingbackRequested() {
        return this.mRingbackRequested;
    }

    public final boolean getAudioModeIsVoip() {
        return this.mAudioModeIsVoip;
    }

    public final long getConnectTimeMillis() {
        return this.mConnectTimeMillis;
    }

    public final long getConnectElapsedTimeMillis() {
        return this.mConnectElapsedTimeMillis;
    }

    public final int getCallRadioTech() {
        int voiceNetworkType = 0;
        Bundle extras = getExtras();
        if (extras != null) {
            voiceNetworkType = extras.getInt(TelecomManager.EXTRA_CALL_NETWORK_TYPE, 0);
        }
        return ServiceState.networkTypeToRilRadioTechnology(voiceNetworkType);
    }

    public final StatusHints getStatusHints() {
        return this.mStatusHints;
    }

    public final Bundle getExtras() {
        Bundle extras = null;
        synchronized (this.mExtrasLock) {
            if (this.mExtras != null) {
                extras = new Bundle(this.mExtras);
            }
        }
        return extras;
    }

    public final Connection addConnectionListener(Listener l) {
        this.mListeners.add(l);
        return this;
    }

    public final Connection removeConnectionListener(Listener l) {
        if (l != null) {
            this.mListeners.remove(l);
        }
        return this;
    }

    public final DisconnectCause getDisconnectCause() {
        return this.mDisconnectCause;
    }

    public void setTelecomCallId(String callId) {
        this.mTelecomCallId = callId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void setCallAudioState(CallAudioState state) {
        checkImmutable();
        Log.d(this, "setAudioState %s", state);
        this.mCallAudioState = state;
        onAudioStateChanged(getAudioState());
        onCallAudioStateChanged(state);
    }

    public static String stateToString(int state) {
        switch (state) {
            case 0:
                return "INITIALIZING";
            case 1:
                return "NEW";
            case 2:
                return "RINGING";
            case 3:
                return "DIALING";
            case 4:
                return "ACTIVE";
            case 5:
                return "HOLDING";
            case 6:
                return "DISCONNECTED";
            case 7:
                return "PULLING_CALL";
            default:
                Log.wtf(Connection.class, "Unknown state %d", Integer.valueOf(state));
                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
    }

    public final int getConnectionCapabilities() {
        return this.mConnectionCapabilities;
    }

    public final int getConnectionProperties() {
        return this.mConnectionProperties;
    }

    public final int getSupportedAudioRoutes() {
        return this.mSupportedAudioRoutes;
    }

    public final void setAddress(Uri address, int presentation) {
        checkImmutable();
        Log.d(this, "setAddress %s", address);
        this.mAddress = address;
        this.mAddressPresentation = presentation;
        for (Listener l : this.mListeners) {
            l.onAddressChanged(this, address, presentation);
        }
    }

    public final void setCallerDisplayName(String callerDisplayName, int presentation) {
        checkImmutable();
        Log.d(this, "setCallerDisplayName %s", callerDisplayName);
        this.mCallerDisplayName = callerDisplayName;
        this.mCallerDisplayNamePresentation = presentation;
        for (Listener l : this.mListeners) {
            l.onCallerDisplayNameChanged(this, callerDisplayName, presentation);
        }
    }

    public final void setVideoState(int videoState) {
        checkImmutable();
        Log.d(this, "setVideoState %d", Integer.valueOf(videoState));
        this.mVideoState = videoState;
        for (Listener l : this.mListeners) {
            l.onVideoStateChanged(this, this.mVideoState);
        }
    }

    public final void setActive() {
        checkImmutable();
        setRingbackRequested(false);
        setState(4);
    }

    public final void setRinging() {
        checkImmutable();
        setState(2);
    }

    public final void setInitializing() {
        checkImmutable();
        setState(0);
    }

    public final void setInitialized() {
        checkImmutable();
        setState(1);
    }

    public final void setDialing() {
        checkImmutable();
        setState(3);
    }

    public final void setPulling() {
        checkImmutable();
        setState(7);
    }

    public final void setOnHold() {
        checkImmutable();
        setState(5);
    }

    public final void setVideoProvider(VideoProvider videoProvider) {
        checkImmutable();
        this.mVideoProvider = videoProvider;
        for (Listener l : this.mListeners) {
            l.onVideoProviderChanged(this, videoProvider);
        }
    }

    public final VideoProvider getVideoProvider() {
        return this.mVideoProvider;
    }

    public final void setDisconnected(DisconnectCause disconnectCause) {
        checkImmutable();
        this.mDisconnectCause = disconnectCause;
        setState(6);
        Log.d(this, "Disconnected with cause %s", disconnectCause);
        for (Listener l : this.mListeners) {
            l.onDisconnected(this, disconnectCause);
        }
    }

    public final void setPostDialWait(String remaining) {
        checkImmutable();
        for (Listener l : this.mListeners) {
            l.onPostDialWait(this, remaining);
        }
    }

    public final void setNextPostDialChar(char nextChar) {
        checkImmutable();
        for (Listener l : this.mListeners) {
            l.onPostDialChar(this, nextChar);
        }
    }

    public final void setRingbackRequested(boolean ringback) {
        checkImmutable();
        if (this.mRingbackRequested != ringback) {
            this.mRingbackRequested = ringback;
            for (Listener l : this.mListeners) {
                l.onRingbackRequested(this, ringback);
            }
        }
    }

    public final void setConnectionCapabilities(int connectionCapabilities) {
        checkImmutable();
        if (this.mConnectionCapabilities != connectionCapabilities) {
            this.mConnectionCapabilities = connectionCapabilities;
            for (Listener l : this.mListeners) {
                l.onConnectionCapabilitiesChanged(this, this.mConnectionCapabilities);
            }
        }
    }

    public final void setConnectionProperties(int connectionProperties) {
        checkImmutable();
        if (this.mConnectionProperties != connectionProperties) {
            this.mConnectionProperties = connectionProperties;
            for (Listener l : this.mListeners) {
                l.onConnectionPropertiesChanged(this, this.mConnectionProperties);
            }
        }
    }

    public final void setSupportedAudioRoutes(int supportedAudioRoutes) {
        if ((supportedAudioRoutes & 9) == 0) {
            throw new IllegalArgumentException("supported audio routes must include either speaker or earpiece");
        }
        if (this.mSupportedAudioRoutes != supportedAudioRoutes) {
            this.mSupportedAudioRoutes = supportedAudioRoutes;
            for (Listener l : this.mListeners) {
                l.onSupportedAudioRoutesChanged(this, this.mSupportedAudioRoutes);
            }
        }
    }

    public final void destroy() {
        for (Listener l : this.mListeners) {
            l.onDestroyed(this);
        }
    }

    public final void setAudioModeIsVoip(boolean isVoip) {
        checkImmutable();
        this.mAudioModeIsVoip = isVoip;
        for (Listener l : this.mListeners) {
            l.onAudioModeIsVoipChanged(this, isVoip);
        }
    }

    public final void setConnectTimeMillis(long connectTimeMillis) {
        this.mConnectTimeMillis = connectTimeMillis;
    }

    public final void setConnectionStartElapsedRealTime(long connectElapsedTimeMillis) {
        this.mConnectElapsedTimeMillis = connectElapsedTimeMillis;
    }

    public final void setCallRadioTech(int vrat) {
        putExtra(TelecomManager.EXTRA_CALL_NETWORK_TYPE, ServiceState.rilRadioTechnologyToNetworkType(vrat));
        if (getConference() != null) {
            getConference().setCallRadioTech(vrat);
        }
    }

    public final void setStatusHints(StatusHints statusHints) {
        checkImmutable();
        this.mStatusHints = statusHints;
        for (Listener l : this.mListeners) {
            l.onStatusHintsChanged(this, statusHints);
        }
    }

    public final void setConferenceableConnections(List<Connection> conferenceableConnections) {
        checkImmutable();
        clearConferenceableList();
        for (Connection c : conferenceableConnections) {
            if (!this.mConferenceables.contains(c)) {
                c.addConnectionListener(this.mConnectionDeathListener);
                this.mConferenceables.add(c);
            }
        }
        fireOnConferenceableConnectionsChanged();
    }

    public final void setConferenceables(List<Conferenceable> conferenceables) {
        clearConferenceableList();
        for (Conferenceable c : conferenceables) {
            if (!this.mConferenceables.contains(c)) {
                if (c instanceof Connection) {
                    Connection connection = (Connection) c;
                    connection.addConnectionListener(this.mConnectionDeathListener);
                } else if (c instanceof Conference) {
                    Conference conference = (Conference) c;
                    conference.addListener(this.mConferenceDeathListener);
                }
                this.mConferenceables.add(c);
            }
        }
        fireOnConferenceableConnectionsChanged();
    }

    public final void resetConnectionTime() {
        for (Listener l : this.mListeners) {
            l.onConnectionTimeReset(this);
        }
    }

    public final List<Conferenceable> getConferenceables() {
        return this.mUnmodifiableConferenceables;
    }

    public final void setConnectionService(ConnectionService connectionService) {
        checkImmutable();
        if (this.mConnectionService != null) {
            Log.e(this, new Exception(), "Trying to set ConnectionService on a connection which is already associated with another ConnectionService.", new Object[0]);
        } else {
            this.mConnectionService = connectionService;
        }
    }

    public final void unsetConnectionService(ConnectionService connectionService) {
        if (this.mConnectionService != connectionService) {
            Log.e(this, new Exception(), "Trying to remove ConnectionService from a Connection that does not belong to the ConnectionService.", new Object[0]);
        } else {
            this.mConnectionService = null;
        }
    }

    public final ConnectionService getConnectionService() {
        return this.mConnectionService;
    }

    public final boolean setConference(Conference conference) {
        checkImmutable();
        if (this.mConference == null) {
            this.mConference = conference;
            ConnectionService connectionService = this.mConnectionService;
            if (connectionService != null && connectionService.containsConference(conference)) {
                fireConferenceChanged();
                return true;
            }
            return true;
        }
        return false;
    }

    public final void resetConference() {
        if (this.mConference != null) {
            Log.d(this, "Conference reset", new Object[0]);
            this.mConference = null;
            fireConferenceChanged();
        }
    }

    public final void setExtras(Bundle extras) {
        checkImmutable();
        putExtras(extras);
        if (this.mPreviousExtraKeys != null) {
            List<String> toRemove = new ArrayList<>();
            for (String oldKey : this.mPreviousExtraKeys) {
                if (extras == null || !extras.containsKey(oldKey)) {
                    toRemove.add(oldKey);
                }
            }
            if (!toRemove.isEmpty()) {
                removeExtras(toRemove);
            }
        }
        if (this.mPreviousExtraKeys == null) {
            this.mPreviousExtraKeys = new ArraySet();
        }
        this.mPreviousExtraKeys.clear();
        if (extras != null) {
            this.mPreviousExtraKeys.addAll(extras.keySet());
        }
    }

    public final void putExtras(Bundle extras) {
        Bundle listenerExtras;
        checkImmutable();
        if (extras == null) {
            return;
        }
        synchronized (this.mExtrasLock) {
            if (this.mExtras == null) {
                this.mExtras = new Bundle();
            }
            this.mExtras.putAll(extras);
            listenerExtras = new Bundle(this.mExtras);
        }
        for (Listener l : this.mListeners) {
            l.onExtrasChanged(this, new Bundle(listenerExtras));
        }
    }

    public final void putExtra(String key, boolean value) {
        Bundle newExtras = new Bundle();
        newExtras.putBoolean(key, value);
        putExtras(newExtras);
    }

    public final void putExtra(String key, int value) {
        Bundle newExtras = new Bundle();
        newExtras.putInt(key, value);
        putExtras(newExtras);
    }

    public final void putExtra(String key, String value) {
        Bundle newExtras = new Bundle();
        newExtras.putString(key, value);
        putExtras(newExtras);
    }

    public final void removeExtras(List<String> keys) {
        synchronized (this.mExtrasLock) {
            if (this.mExtras != null) {
                for (String key : keys) {
                    this.mExtras.remove(key);
                }
            }
        }
        List<String> unmodifiableKeys = Collections.unmodifiableList(keys);
        for (Listener l : this.mListeners) {
            l.onExtrasRemoved(this, unmodifiableKeys);
        }
    }

    public final void removeExtras(String... keys) {
        removeExtras(Arrays.asList(keys));
    }

    public final void setAudioRoute(int route) {
        for (Listener l : this.mListeners) {
            l.onAudioRouteChanged(this, route, null);
        }
    }

    public void requestBluetoothAudio(BluetoothDevice bluetoothDevice) {
        for (Listener l : this.mListeners) {
            l.onAudioRouteChanged(this, 2, bluetoothDevice.getAddress());
        }
    }

    public /* synthetic */ void lambda$sendRttInitiationSuccess$0$Connection(Listener l) {
        l.onRttInitiationSuccess(this);
    }

    public final void sendRttInitiationSuccess() {
        this.mListeners.forEach(new Consumer() { // from class: android.telecom.-$$Lambda$Connection$8xeoCKtoHEwnDqv6gbuSfOMODH0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Connection.this.lambda$sendRttInitiationSuccess$0$Connection((Connection.Listener) obj);
            }
        });
    }

    public /* synthetic */ void lambda$sendRttInitiationFailure$1$Connection(int reason, Listener l) {
        l.onRttInitiationFailure(this, reason);
    }

    public final void sendRttInitiationFailure(final int reason) {
        this.mListeners.forEach(new Consumer() { // from class: android.telecom.-$$Lambda$Connection$noXZvls4rxmO_SOjgkFMZLLrfSg
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Connection.this.lambda$sendRttInitiationFailure$1$Connection(reason, (Connection.Listener) obj);
            }
        });
    }

    public /* synthetic */ void lambda$sendRttSessionRemotelyTerminated$2$Connection(Listener l) {
        l.onRttSessionRemotelyTerminated(this);
    }

    public final void sendRttSessionRemotelyTerminated() {
        this.mListeners.forEach(new Consumer() { // from class: android.telecom.-$$Lambda$Connection$SYsjtKchY2AYvOeGveCrqxSfKTU
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Connection.this.lambda$sendRttSessionRemotelyTerminated$2$Connection((Connection.Listener) obj);
            }
        });
    }

    public /* synthetic */ void lambda$sendRemoteRttRequest$3$Connection(Listener l) {
        l.onRemoteRttRequest(this);
    }

    public final void sendRemoteRttRequest() {
        this.mListeners.forEach(new Consumer() { // from class: android.telecom.-$$Lambda$Connection$lnfFNF0t9fPLEf01JE291g4chSk
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Connection.this.lambda$sendRemoteRttRequest$3$Connection((Connection.Listener) obj);
            }
        });
    }

    @SystemApi
    @Deprecated
    public void onAudioStateChanged(AudioState state) {
    }

    public void onCallAudioStateChanged(CallAudioState state) {
    }

    public void onStateChanged(int state) {
    }

    public void onPlayDtmfTone(char c) {
    }

    public void onStopDtmfTone() {
    }

    public void onDisconnect() {
    }

    public void onDisconnectConferenceParticipant(Uri endpoint) {
    }

    public void onSeparate() {
    }

    public void onAbort() {
    }

    public void onHold() {
    }

    public void onUnhold() {
    }

    public void onAnswer(int videoState) {
    }

    public void onAnswer() {
        onAnswer(0);
    }

    public void onDeflect(Uri address) {
    }

    public void onReject() {
    }

    public void onReject(String replyMessage) {
    }

    public void onSilence() {
    }

    public void onPostDialContinue(boolean proceed) {
    }

    public void onPullExternalCall() {
    }

    public void onCallEvent(String event, Bundle extras) {
    }

    public void onHandoverComplete() {
    }

    public void onExtrasChanged(Bundle extras) {
    }

    public void onShowIncomingCallUi() {
    }

    public void onStartRtt(RttTextStream rttTextStream) {
    }

    public void onStopRtt() {
    }

    public void handleRttUpgradeResponse(RttTextStream rttTextStream) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String toLogSafePhoneNumber(String number) {
        if (number == null) {
            return "";
        }
        if (PII_DEBUG) {
            return number;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < number.length(); i++) {
            char c = number.charAt(i);
            if (c == '-' || c == '@' || c == '.') {
                builder.append(c);
            } else {
                builder.append(EpicenterTranslateClipReveal.StateProperty.TARGET_X);
            }
        }
        return builder.toString();
    }

    private void setState(int state) {
        checkImmutable();
        int i = this.mState;
        if (i == 6 && i != state) {
            Log.d(this, "Connection already DISCONNECTED; cannot transition out of this state.", new Object[0]);
        } else if (this.mState != state) {
            Log.d(this, "setState: %s", stateToString(state));
            this.mState = state;
            onStateChanged(state);
            for (Listener l : this.mListeners) {
                l.onStateChanged(this, state);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class FailureSignalingConnection extends Connection {
        private boolean mImmutable;

        public FailureSignalingConnection(DisconnectCause disconnectCause) {
            this.mImmutable = false;
            setDisconnected(disconnectCause);
            this.mImmutable = true;
        }

        @Override // android.telecom.Connection
        public void checkImmutable() {
            if (this.mImmutable) {
                throw new UnsupportedOperationException("Connection is immutable");
            }
        }
    }

    public static Connection createFailedConnection(DisconnectCause disconnectCause) {
        return new FailureSignalingConnection(disconnectCause);
    }

    public void checkImmutable() {
    }

    public static Connection createCanceledConnection() {
        return new FailureSignalingConnection(new DisconnectCause(4));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void fireOnConferenceableConnectionsChanged() {
        for (Listener l : this.mListeners) {
            l.onConferenceablesChanged(this, getConferenceables());
        }
    }

    private final void fireConferenceChanged() {
        for (Listener l : this.mListeners) {
            l.onConferenceChanged(this, this.mConference);
        }
    }

    private final void clearConferenceableList() {
        for (Conferenceable c : this.mConferenceables) {
            if (c instanceof Connection) {
                Connection connection = (Connection) c;
                connection.removeConnectionListener(this.mConnectionDeathListener);
            } else if (c instanceof Conference) {
                Conference conference = (Conference) c;
                conference.removeListener(this.mConferenceDeathListener);
            }
        }
        this.mConferenceables.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void handleExtrasChanged(Bundle extras) {
        Bundle b = null;
        synchronized (this.mExtrasLock) {
            this.mExtras = extras;
            if (this.mExtras != null) {
                b = new Bundle(this.mExtras);
            }
        }
        onExtrasChanged(b);
    }

    protected final void notifyConferenceMergeFailed() {
        for (Listener l : this.mListeners) {
            l.onConferenceMergeFailed(this);
        }
    }

    protected final void updateConferenceParticipants(List<ConferenceParticipant> conferenceParticipants) {
        for (Listener l : this.mListeners) {
            l.onConferenceParticipantsChanged(this, conferenceParticipants);
        }
    }

    protected void notifyConferenceStarted() {
        for (Listener l : this.mListeners) {
            l.onConferenceStarted();
        }
    }

    protected void notifyConferenceSupportedChanged(boolean isConferenceSupported) {
        for (Listener l : this.mListeners) {
            l.onConferenceSupportedChanged(this, isConferenceSupported);
        }
    }

    public void notifyPhoneAccountChanged(PhoneAccountHandle pHandle) {
        for (Listener l : this.mListeners) {
            l.onPhoneAccountChanged(this, pHandle);
        }
    }

    public void setPhoneAccountHandle(PhoneAccountHandle phoneAccountHandle) {
        if (this.mPhoneAccountHandle != phoneAccountHandle) {
            this.mPhoneAccountHandle = phoneAccountHandle;
            notifyPhoneAccountChanged(phoneAccountHandle);
        }
    }

    public PhoneAccountHandle getPhoneAccountHandle() {
        return this.mPhoneAccountHandle;
    }

    public void sendConnectionEvent(String event, Bundle extras) {
        for (Listener l : this.mListeners) {
            l.onConnectionEvent(this, event, extras);
        }
    }

    public final int getCallDirection() {
        return this.mCallDirection;
    }

    public void setCallDirection(int callDirection) {
        this.mCallDirection = callDirection;
    }
}
