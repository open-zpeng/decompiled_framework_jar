package android.service.voice;

import android.Manifest;
import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import com.android.internal.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes2.dex */
public class VoiceInteractionServiceInfo {
    static final String TAG = "VoiceInteractionServiceInfo";
    private String mParseError;
    private String mRecognitionService;
    private ServiceInfo mServiceInfo;
    private String mSessionService;
    private String mSettingsActivity;
    private boolean mSupportsAssist;
    private boolean mSupportsLaunchFromKeyguard;
    private boolean mSupportsLocalInteraction;

    public synchronized VoiceInteractionServiceInfo(PackageManager pm, ComponentName comp) throws PackageManager.NameNotFoundException {
        this(pm, pm.getServiceInfo(comp, 128));
    }

    public synchronized VoiceInteractionServiceInfo(PackageManager pm, ComponentName comp, int userHandle) throws PackageManager.NameNotFoundException {
        this(pm, getServiceInfoOrThrow(comp, userHandle));
    }

    static synchronized ServiceInfo getServiceInfoOrThrow(ComponentName comp, int userHandle) throws PackageManager.NameNotFoundException {
        try {
            ServiceInfo si = AppGlobals.getPackageManager().getServiceInfo(comp, 269222016, userHandle);
            if (si != null) {
                return si;
            }
        } catch (RemoteException e) {
        }
        throw new PackageManager.NameNotFoundException(comp.toString());
    }

    public synchronized VoiceInteractionServiceInfo(PackageManager pm, ServiceInfo si) {
        if (si == null) {
            this.mParseError = "Service not available";
        } else if (!Manifest.permission.BIND_VOICE_INTERACTION.equals(si.permission)) {
            this.mParseError = "Service does not require permission android.permission.BIND_VOICE_INTERACTION";
        } else {
            XmlResourceParser parser = null;
            try {
                try {
                    try {
                        try {
                            XmlResourceParser parser2 = si.loadXmlMetaData(pm, VoiceInteractionService.SERVICE_META_DATA);
                            if (parser2 == null) {
                                this.mParseError = "No android.voice_interaction meta-data for " + si.packageName;
                                if (parser2 != null) {
                                    parser2.close();
                                    return;
                                }
                                return;
                            }
                            Resources res = pm.getResourcesForApplication(si.applicationInfo);
                            AttributeSet attrs = Xml.asAttributeSet(parser2);
                            while (true) {
                                int type = parser2.next();
                                if (type == 1 || type == 2) {
                                    break;
                                }
                            }
                            String nodeName = parser2.getName();
                            if (!"voice-interaction-service".equals(nodeName)) {
                                this.mParseError = "Meta-data does not start with voice-interaction-service tag";
                                if (parser2 != null) {
                                    parser2.close();
                                    return;
                                }
                                return;
                            }
                            TypedArray array = res.obtainAttributes(attrs, R.styleable.VoiceInteractionService);
                            this.mSessionService = array.getString(1);
                            this.mRecognitionService = array.getString(2);
                            this.mSettingsActivity = array.getString(0);
                            this.mSupportsAssist = array.getBoolean(3, false);
                            this.mSupportsLaunchFromKeyguard = array.getBoolean(4, false);
                            this.mSupportsLocalInteraction = array.getBoolean(5, false);
                            array.recycle();
                            if (this.mSessionService == null) {
                                this.mParseError = "No sessionService specified";
                                if (parser2 != null) {
                                    parser2.close();
                                }
                            } else if (this.mRecognitionService != null) {
                                if (parser2 != null) {
                                    parser2.close();
                                }
                                this.mServiceInfo = si;
                            } else {
                                this.mParseError = "No recognitionService specified";
                                if (parser2 != null) {
                                    parser2.close();
                                }
                            }
                        } catch (IOException e) {
                            this.mParseError = "Error parsing voice interation service meta-data: " + e;
                            Log.w(TAG, "error parsing voice interaction service meta-data", e);
                            if (0 != 0) {
                                parser.close();
                            }
                        }
                    } catch (PackageManager.NameNotFoundException e2) {
                        this.mParseError = "Error parsing voice interation service meta-data: " + e2;
                        Log.w(TAG, "error parsing voice interaction service meta-data", e2);
                        if (0 != 0) {
                            parser.close();
                        }
                    }
                } catch (XmlPullParserException e3) {
                    this.mParseError = "Error parsing voice interation service meta-data: " + e3;
                    Log.w(TAG, "error parsing voice interaction service meta-data", e3);
                    if (0 != 0) {
                        parser.close();
                    }
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    parser.close();
                }
                throw th;
            }
        }
    }

    public synchronized String getParseError() {
        return this.mParseError;
    }

    public synchronized ServiceInfo getServiceInfo() {
        return this.mServiceInfo;
    }

    public synchronized String getSessionService() {
        return this.mSessionService;
    }

    public synchronized String getRecognitionService() {
        return this.mRecognitionService;
    }

    public synchronized String getSettingsActivity() {
        return this.mSettingsActivity;
    }

    public synchronized boolean getSupportsAssist() {
        return this.mSupportsAssist;
    }

    public synchronized boolean getSupportsLaunchFromKeyguard() {
        return this.mSupportsLaunchFromKeyguard;
    }

    public synchronized boolean getSupportsLocalInteraction() {
        return this.mSupportsLocalInteraction;
    }
}
