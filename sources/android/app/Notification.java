package android.app;

import android.Manifest;
import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.AudioAttributes;
import android.media.PlayerBase;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.BidiFormatter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import android.util.proto.ProtoOutputStream;
import android.widget.RemoteViews;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.NotificationColorUtil;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class Notification implements Parcelable {
    public static final AudioAttributes AUDIO_ATTRIBUTES_DEFAULT;
    public static final int BADGE_ICON_LARGE = 2;
    public static final int BADGE_ICON_NONE = 0;
    public static final int BADGE_ICON_SMALL = 1;
    public static final String CATEGORY_ALARM = "alarm";
    public static final String CATEGORY_CALL = "call";
    @SystemApi
    public static final String CATEGORY_CAR_EMERGENCY = "car_emergency";
    @SystemApi
    public static final String CATEGORY_CAR_INFORMATION = "car_information";
    @SystemApi
    public static final String CATEGORY_CAR_WARNING = "car_warning";
    public static final String CATEGORY_EMAIL = "email";
    public static final String CATEGORY_ERROR = "err";
    public static final String CATEGORY_EVENT = "event";
    public static final String CATEGORY_MESSAGE = "msg";
    public static final String CATEGORY_NAVIGATION = "navigation";
    public static final String CATEGORY_PROGRESS = "progress";
    public static final String CATEGORY_PROMO = "promo";
    public static final String CATEGORY_RECOMMENDATION = "recommendation";
    public static final String CATEGORY_REMINDER = "reminder";
    public static final String CATEGORY_SERVICE = "service";
    public static final String CATEGORY_SOCIAL = "social";
    public static final String CATEGORY_STATUS = "status";
    public static final String CATEGORY_SYSTEM = "sys";
    public static final String CATEGORY_TRANSPORT = "transport";
    public static final int COLOR_DEFAULT = 0;
    public static final int COLOR_INVALID = 1;
    public static final Parcelable.Creator<Notification> CREATOR;
    public static final int DEFAULT_ALL = -1;
    public static final int DEFAULT_LIGHTS = 4;
    public static final int DEFAULT_SOUND = 1;
    public static final int DEFAULT_VIBRATE = 2;
    @SystemApi
    public static final String EXTRA_ALLOW_DURING_SETUP = "android.allowDuringSetup";
    public static final String EXTRA_AUDIO_CONTENTS_URI = "android.audioContents";
    public static final String EXTRA_BACKGROUND_IMAGE_URI = "android.backgroundImageUri";
    public static final String EXTRA_BIG_TEXT = "android.bigText";
    public static final String EXTRA_BUILDER_APPLICATION_INFO = "android.appInfo";
    public static final String EXTRA_CHANNEL_GROUP_ID = "android.intent.extra.CHANNEL_GROUP_ID";
    public static final String EXTRA_CHANNEL_ID = "android.intent.extra.CHANNEL_ID";
    public static final String EXTRA_CHRONOMETER_COUNT_DOWN = "android.chronometerCountDown";
    public static final String EXTRA_CLEAR_FLAG = "android.clearFlag";
    public static final String EXTRA_COLORIZED = "android.colorized";
    public static final String EXTRA_COMPACT_ACTIONS = "android.compactActions";
    public static final String EXTRA_CONTAINS_CUSTOM_VIEW = "android.contains.customView";
    public static final String EXTRA_CONVERSATION_TITLE = "android.conversationTitle";
    public static final String EXTRA_DETAIL = "android.details";
    public static final String EXTRA_DISPLAY_FLAG = "android.displayFlag";
    public static final String EXTRA_FOREGROUND_APPS = "android.foregroundApps";
    public static final String EXTRA_HIDE_SMART_REPLIES = "android.hideSmartReplies";
    public static final String EXTRA_HISTORIC_MESSAGES = "android.messages.historic";
    public static final String EXTRA_INFO_TEXT = "android.infoText";
    public static final String EXTRA_IS_GROUP_CONVERSATION = "android.isGroupConversation";
    @Deprecated
    public static final String EXTRA_LARGE_ICON = "android.largeIcon";
    public static final String EXTRA_LARGE_ICON_BIG = "android.largeIcon.big";
    public static final String EXTRA_MAJOR_GROUP = "android.majorGroup";
    public static final String EXTRA_MAJOR_PRIORITY = "android.majorPriority";
    public static final String EXTRA_MEDIA_SESSION = "android.mediaSession";
    public static final String EXTRA_MESSAGES = "android.messages";
    public static final String EXTRA_MESSAGING_PERSON = "android.messagingUser";
    public static final String EXTRA_NOTIFICATION_ID = "android.intent.extra.NOTIFICATION_ID";
    public static final String EXTRA_NOTIFICATION_TAG = "android.intent.extra.NOTIFICATION_TAG";
    public static final String EXTRA_PEOPLE = "android.people";
    public static final String EXTRA_PEOPLE_LIST = "android.people.list";
    public static final String EXTRA_PICTURE = "android.picture";
    public static final String EXTRA_PROGRESS = "android.progress";
    public static final String EXTRA_PROGRESS_INDETERMINATE = "android.progressIndeterminate";
    public static final String EXTRA_PROGRESS_MAX = "android.progressMax";
    public static final String EXTRA_REDUCED_IMAGES = "android.reduced.images";
    public static final String EXTRA_REMOTE_INPUT_DRAFT = "android.remoteInputDraft";
    public static final String EXTRA_REMOTE_INPUT_HISTORY = "android.remoteInputHistory";
    public static final String EXTRA_SELF_DISPLAY_NAME = "android.selfDisplayName";
    public static final String EXTRA_SHOW_CHRONOMETER = "android.showChronometer";
    public static final String EXTRA_SHOW_REMOTE_INPUT_SPINNER = "android.remoteInputSpinner";
    public static final String EXTRA_SHOW_WHEN = "android.showWhen";
    @Deprecated
    public static final String EXTRA_SMALL_ICON = "android.icon";
    @SystemApi
    public static final String EXTRA_SUBSTITUTE_APP_NAME = "android.substName";
    public static final String EXTRA_SUB_TEXT = "android.subText";
    public static final String EXTRA_SUMMARY_TEXT = "android.summaryText";
    public static final String EXTRA_TEMPLATE = "android.template";
    public static final String EXTRA_TEXT = "android.text";
    public static final String EXTRA_TEXT_LINES = "android.textLines";
    public static final String EXTRA_TITLE = "android.title";
    public static final String EXTRA_TITLE_BIG = "android.title.big";
    @SystemApi
    public static final int FLAG_AUTOGROUP_SUMMARY = 1024;
    public static final int FLAG_AUTO_CANCEL = 16;
    public static final int FLAG_CAN_COLORIZE = 2048;
    public static final int FLAG_CLEAR_AUTO = 2;
    public static final int FLAG_CLEAR_MASK = 0;
    public static final int FLAG_CLEAR_USER = 1;
    public static final int FLAG_DISPLAY_ALL = 1;
    public static final int FLAG_DISPLAY_BADGE = 8;
    public static final int FLAG_DISPLAY_DIALOG = 16;
    public static final int FLAG_DISPLAY_DOWNLOAD = 64;
    public static final int FLAG_DISPLAY_INFOFLOW = 32;
    public static final int FLAG_DISPLAY_LIST = 4;
    public static final int FLAG_DISPLAY_MASK = 0;
    public static final int FLAG_DISPLAY_NONE = 0;
    public static final int FLAG_DISPLAY_OSD = 2;
    public static final int FLAG_DISPLAY_STATUSBAR = 128;
    public static final int FLAG_FOREGROUND_SERVICE = 64;
    public static final int FLAG_GROUP_SUMMARY = 512;
    @Deprecated
    public static final int FLAG_HIGH_PRIORITY = 128;
    public static final int FLAG_INSISTENT = 4;
    public static final int FLAG_LOCAL_ONLY = 256;
    public static final int FLAG_NO_CLEAR = 32;
    public static final int FLAG_ONGOING_EVENT = 2;
    public static final int FLAG_ONLY_ALERT_ONCE = 8;
    @Deprecated
    public static final int FLAG_SHOW_LIGHTS = 1;
    public static final int GROUP_ALERT_ALL = 0;
    public static final int GROUP_ALERT_CHILDREN = 2;
    public static final int GROUP_ALERT_SUMMARY = 1;
    public static final String INTENT_CATEGORY_NOTIFICATION_PREFERENCES = "android.intent.category.NOTIFICATION_PREFERENCES";
    public static final int MAJOR_GROUP_G0 = 0;
    public static final int MAJOR_GROUP_G1 = 100;
    public static final int MAJOR_GROUP_G2 = 200;
    public static final int MAJOR_GROUP_G3 = 300;
    public static final int MAJOR_GROUP_G4 = 400;
    private static final int MAX_CHARSEQUENCE_LENGTH = 5120;
    private static final int MAX_REPLY_HISTORY = 5;
    @Deprecated
    public static final int PRIORITY_DEFAULT = 0;
    @Deprecated
    public static final int PRIORITY_HIGH = 1;
    @Deprecated
    public static final int PRIORITY_LOW = -1;
    @Deprecated
    public static final int PRIORITY_MAX = 2;
    @Deprecated
    public static final int PRIORITY_MIN = -2;
    public static final int PRIORITY_P0 = 0;
    public static final int PRIORITY_P1 = 1;
    public static final int PRIORITY_P2 = 2;
    private static final ArraySet<Integer> STANDARD_LAYOUTS = new ArraySet<>();
    @Deprecated
    public static final int STREAM_DEFAULT = -1;
    private static final String TAG = "Notification";
    public static final int VISIBILITY_PRIVATE = 0;
    public static final int VISIBILITY_PUBLIC = 1;
    public static final int VISIBILITY_SECRET = -1;
    public static IBinder processWhitelistToken;
    public Action[] actions;
    private protected ArraySet<PendingIntent> allPendingIntents;
    @Deprecated
    public AudioAttributes audioAttributes;
    @Deprecated
    public int audioStreamType;
    @Deprecated
    public RemoteViews bigContentView;
    public String category;
    public int clearFlag;
    public int color;
    public PendingIntent contentIntent;
    @Deprecated
    public RemoteViews contentView;
    public long creationTime;
    @Deprecated
    public int defaults;
    public PendingIntent deleteIntent;
    public int displayFlag;
    public Bundle extras;
    public int flags;
    public PendingIntent fullScreenIntent;
    @Deprecated
    public RemoteViews headsUpContentView;
    @Deprecated
    public int icon;
    public int iconLevel;
    @Deprecated
    public Bitmap largeIcon;
    @Deprecated
    public int ledARGB;
    @Deprecated
    public int ledOffMS;
    @Deprecated
    public int ledOnMS;
    private int mBadgeIcon;
    public protected String mChannelId;
    private int mGroupAlertBehavior;
    public protected String mGroupKey;
    public protected Icon mLargeIcon;
    private CharSequence mSettingsText;
    private String mShortcutId;
    public protected Icon mSmallIcon;
    private String mSortKey;
    private long mTimeout;
    private boolean mUsesStandardHeader;
    private IBinder mWhitelistToken;
    public int majorPriority;
    public int number;
    @Deprecated
    public int priority;
    public Notification publicVersion;
    @Deprecated
    public Uri sound;
    public CharSequence tickerText;
    @Deprecated
    public RemoteViews tickerView;
    @Deprecated
    public long[] vibrate;
    public int visibility;
    public long when;

    /* loaded from: classes.dex */
    public interface Extender {
        Builder extend(Builder builder);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface GroupAlertBehavior {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Priority {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Visibility {
    }

    static {
        STANDARD_LAYOUTS.add(17367189);
        STANDARD_LAYOUTS.add(Integer.valueOf((int) R.layout.notification_template_material_big_base));
        STANDARD_LAYOUTS.add(Integer.valueOf((int) R.layout.notification_template_material_big_picture));
        STANDARD_LAYOUTS.add(Integer.valueOf((int) R.layout.notification_template_material_big_text));
        STANDARD_LAYOUTS.add(Integer.valueOf((int) R.layout.notification_template_material_inbox));
        STANDARD_LAYOUTS.add(Integer.valueOf((int) R.layout.notification_template_material_messaging));
        STANDARD_LAYOUTS.add(Integer.valueOf((int) R.layout.notification_template_material_media));
        STANDARD_LAYOUTS.add(Integer.valueOf((int) R.layout.notification_template_material_big_media));
        STANDARD_LAYOUTS.add(Integer.valueOf((int) R.layout.notification_template_ambient_header));
        STANDARD_LAYOUTS.add(Integer.valueOf((int) R.layout.notification_template_header));
        STANDARD_LAYOUTS.add(Integer.valueOf((int) R.layout.notification_template_material_ambient));
        AUDIO_ATTRIBUTES_DEFAULT = new AudioAttributes.Builder().setContentType(4).setUsage(5).build();
        CREATOR = new Parcelable.Creator<Notification>() { // from class: android.app.Notification.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Notification createFromParcel(Parcel parcel) {
                return new Notification(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Notification[] newArray(int size) {
                return new Notification[size];
            }
        };
    }

    public String getGroup() {
        return this.mGroupKey;
    }

    public String getSortKey() {
        return this.mSortKey;
    }

    /* loaded from: classes.dex */
    public static class Action implements Parcelable {
        public static final Parcelable.Creator<Action> CREATOR = new Parcelable.Creator<Action>() { // from class: android.app.Notification.Action.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Action createFromParcel(Parcel in) {
                return new Action(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Action[] newArray(int size) {
                return new Action[size];
            }
        };
        private static final String EXTRA_DATA_ONLY_INPUTS = "android.extra.DATA_ONLY_INPUTS";
        public static final int SEMANTIC_ACTION_ARCHIVE = 5;
        public static final int SEMANTIC_ACTION_CALL = 10;
        public static final int SEMANTIC_ACTION_DELETE = 4;
        public static final int SEMANTIC_ACTION_MARK_AS_READ = 2;
        public static final int SEMANTIC_ACTION_MARK_AS_UNREAD = 3;
        public static final int SEMANTIC_ACTION_MUTE = 6;
        public static final int SEMANTIC_ACTION_NONE = 0;
        public static final int SEMANTIC_ACTION_REPLY = 1;
        public static final int SEMANTIC_ACTION_THUMBS_DOWN = 9;
        public static final int SEMANTIC_ACTION_THUMBS_UP = 8;
        public static final int SEMANTIC_ACTION_UNMUTE = 7;
        public PendingIntent actionIntent;
        @Deprecated
        public int icon;
        private boolean mAllowGeneratedReplies;
        private final Bundle mExtras;
        public protected Icon mIcon;
        private final RemoteInput[] mRemoteInputs;
        private final int mSemanticAction;
        public CharSequence title;

        /* loaded from: classes.dex */
        public interface Extender {
            Builder extend(Builder builder);
        }

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes.dex */
        public @interface SemanticAction {
        }

        private synchronized Action(Parcel in) {
            this.mAllowGeneratedReplies = true;
            if (in.readInt() != 0) {
                this.mIcon = Icon.CREATOR.createFromParcel(in);
                if (this.mIcon.getType() == 2) {
                    this.icon = this.mIcon.getResId();
                }
            }
            this.title = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            if (in.readInt() == 1) {
                this.actionIntent = PendingIntent.CREATOR.createFromParcel(in);
            }
            this.mExtras = Bundle.setDefusable(in.readBundle(), true);
            this.mRemoteInputs = (RemoteInput[]) in.createTypedArray(RemoteInput.CREATOR);
            this.mAllowGeneratedReplies = in.readInt() == 1;
            this.mSemanticAction = in.readInt();
        }

        @Deprecated
        public Action(int icon, CharSequence title, PendingIntent intent) {
            this(Icon.createWithResource("", icon), title, intent, new Bundle(), null, true, 0);
        }

        private synchronized Action(Icon icon, CharSequence title, PendingIntent intent, Bundle extras, RemoteInput[] remoteInputs, boolean allowGeneratedReplies, int semanticAction) {
            this.mAllowGeneratedReplies = true;
            this.mIcon = icon;
            if (icon != null && icon.getType() == 2) {
                this.icon = icon.getResId();
            }
            this.title = title;
            this.actionIntent = intent;
            this.mExtras = extras != null ? extras : new Bundle();
            this.mRemoteInputs = remoteInputs;
            this.mAllowGeneratedReplies = allowGeneratedReplies;
            this.mSemanticAction = semanticAction;
        }

        public Icon getIcon() {
            if (this.mIcon == null && this.icon != 0) {
                this.mIcon = Icon.createWithResource("", this.icon);
            }
            return this.mIcon;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public boolean getAllowGeneratedReplies() {
            return this.mAllowGeneratedReplies;
        }

        public RemoteInput[] getRemoteInputs() {
            return this.mRemoteInputs;
        }

        public int getSemanticAction() {
            return this.mSemanticAction;
        }

        public RemoteInput[] getDataOnlyRemoteInputs() {
            return (RemoteInput[]) this.mExtras.getParcelableArray(EXTRA_DATA_ONLY_INPUTS);
        }

        /* loaded from: classes.dex */
        public static final class Builder {
            private boolean mAllowGeneratedReplies;
            private final Bundle mExtras;
            private final Icon mIcon;
            private final PendingIntent mIntent;
            private ArrayList<RemoteInput> mRemoteInputs;
            private int mSemanticAction;
            private final CharSequence mTitle;

            @Deprecated
            public Builder(int icon, CharSequence title, PendingIntent intent) {
                this(Icon.createWithResource("", icon), title, intent);
            }

            public Builder(Icon icon, CharSequence title, PendingIntent intent) {
                this(icon, title, intent, new Bundle(), null, true, 0);
            }

            public Builder(Action action) {
                this(action.getIcon(), action.title, action.actionIntent, new Bundle(action.mExtras), action.getRemoteInputs(), action.getAllowGeneratedReplies(), action.getSemanticAction());
            }

            private synchronized Builder(Icon icon, CharSequence title, PendingIntent intent, Bundle extras, RemoteInput[] remoteInputs, boolean allowGeneratedReplies, int semanticAction) {
                this.mAllowGeneratedReplies = true;
                this.mIcon = icon;
                this.mTitle = title;
                this.mIntent = intent;
                this.mExtras = extras;
                if (remoteInputs != null) {
                    this.mRemoteInputs = new ArrayList<>(remoteInputs.length);
                    Collections.addAll(this.mRemoteInputs, remoteInputs);
                }
                this.mAllowGeneratedReplies = allowGeneratedReplies;
                this.mSemanticAction = semanticAction;
            }

            public Builder addExtras(Bundle extras) {
                if (extras != null) {
                    this.mExtras.putAll(extras);
                }
                return this;
            }

            public Bundle getExtras() {
                return this.mExtras;
            }

            public Builder addRemoteInput(RemoteInput remoteInput) {
                if (this.mRemoteInputs == null) {
                    this.mRemoteInputs = new ArrayList<>();
                }
                this.mRemoteInputs.add(remoteInput);
                return this;
            }

            public Builder setAllowGeneratedReplies(boolean allowGeneratedReplies) {
                this.mAllowGeneratedReplies = allowGeneratedReplies;
                return this;
            }

            public Builder setSemanticAction(int semanticAction) {
                this.mSemanticAction = semanticAction;
                return this;
            }

            public Builder extend(Extender extender) {
                extender.extend(this);
                return this;
            }

            public Action build() {
                ArrayList<RemoteInput> dataOnlyInputs = new ArrayList<>();
                RemoteInput[] previousDataInputs = (RemoteInput[]) this.mExtras.getParcelableArray(Action.EXTRA_DATA_ONLY_INPUTS);
                if (previousDataInputs != null) {
                    for (RemoteInput input : previousDataInputs) {
                        dataOnlyInputs.add(input);
                    }
                }
                List<RemoteInput> textInputs = new ArrayList<>();
                if (this.mRemoteInputs != null) {
                    Iterator<RemoteInput> it = this.mRemoteInputs.iterator();
                    while (it.hasNext()) {
                        RemoteInput input2 = it.next();
                        if (input2.isDataOnly()) {
                            dataOnlyInputs.add(input2);
                        } else {
                            textInputs.add(input2);
                        }
                    }
                }
                if (!dataOnlyInputs.isEmpty()) {
                    RemoteInput[] dataInputsArr = (RemoteInput[]) dataOnlyInputs.toArray(new RemoteInput[dataOnlyInputs.size()]);
                    this.mExtras.putParcelableArray(Action.EXTRA_DATA_ONLY_INPUTS, dataInputsArr);
                }
                RemoteInput[] textInputsArr = textInputs.isEmpty() ? null : (RemoteInput[]) textInputs.toArray(new RemoteInput[textInputs.size()]);
                return new Action(this.mIcon, this.mTitle, this.mIntent, this.mExtras, textInputsArr, this.mAllowGeneratedReplies, this.mSemanticAction);
            }
        }

        /* renamed from: clone */
        public Action m9clone() {
            return new Action(getIcon(), this.title, this.actionIntent, this.mExtras == null ? new Bundle() : new Bundle(this.mExtras), getRemoteInputs(), getAllowGeneratedReplies(), getSemanticAction());
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            Icon ic = getIcon();
            if (ic != null) {
                out.writeInt(1);
                ic.writeToParcel(out, 0);
            } else {
                out.writeInt(0);
            }
            TextUtils.writeToParcel(this.title, out, flags);
            if (this.actionIntent != null) {
                out.writeInt(1);
                this.actionIntent.writeToParcel(out, flags);
            } else {
                out.writeInt(0);
            }
            out.writeBundle(this.mExtras);
            out.writeTypedArray(this.mRemoteInputs, flags);
            out.writeInt(this.mAllowGeneratedReplies ? 1 : 0);
            out.writeInt(this.mSemanticAction);
        }

        /* loaded from: classes.dex */
        public static final class WearableExtender implements Extender {
            private static final int DEFAULT_FLAGS = 1;
            private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
            private static final int FLAG_AVAILABLE_OFFLINE = 1;
            private static final int FLAG_HINT_DISPLAY_INLINE = 4;
            private static final int FLAG_HINT_LAUNCHES_ACTIVITY = 2;
            private static final String KEY_CANCEL_LABEL = "cancelLabel";
            private static final String KEY_CONFIRM_LABEL = "confirmLabel";
            private static final String KEY_FLAGS = "flags";
            private static final String KEY_IN_PROGRESS_LABEL = "inProgressLabel";
            private CharSequence mCancelLabel;
            private CharSequence mConfirmLabel;
            private int mFlags;
            private CharSequence mInProgressLabel;

            public WearableExtender() {
                this.mFlags = 1;
            }

            public WearableExtender(Action action) {
                this.mFlags = 1;
                Bundle wearableBundle = action.getExtras().getBundle(EXTRA_WEARABLE_EXTENSIONS);
                if (wearableBundle != null) {
                    this.mFlags = wearableBundle.getInt("flags", 1);
                    this.mInProgressLabel = wearableBundle.getCharSequence(KEY_IN_PROGRESS_LABEL);
                    this.mConfirmLabel = wearableBundle.getCharSequence(KEY_CONFIRM_LABEL);
                    this.mCancelLabel = wearableBundle.getCharSequence(KEY_CANCEL_LABEL);
                }
            }

            @Override // android.app.Notification.Action.Extender
            public Builder extend(Builder builder) {
                Bundle wearableBundle = new Bundle();
                if (this.mFlags != 1) {
                    wearableBundle.putInt("flags", this.mFlags);
                }
                if (this.mInProgressLabel != null) {
                    wearableBundle.putCharSequence(KEY_IN_PROGRESS_LABEL, this.mInProgressLabel);
                }
                if (this.mConfirmLabel != null) {
                    wearableBundle.putCharSequence(KEY_CONFIRM_LABEL, this.mConfirmLabel);
                }
                if (this.mCancelLabel != null) {
                    wearableBundle.putCharSequence(KEY_CANCEL_LABEL, this.mCancelLabel);
                }
                builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, wearableBundle);
                return builder;
            }

            /* renamed from: clone */
            public WearableExtender m10clone() {
                WearableExtender that = new WearableExtender();
                that.mFlags = this.mFlags;
                that.mInProgressLabel = this.mInProgressLabel;
                that.mConfirmLabel = this.mConfirmLabel;
                that.mCancelLabel = this.mCancelLabel;
                return that;
            }

            public WearableExtender setAvailableOffline(boolean availableOffline) {
                setFlag(1, availableOffline);
                return this;
            }

            public boolean isAvailableOffline() {
                return (this.mFlags & 1) != 0;
            }

            private synchronized void setFlag(int mask, boolean value) {
                if (value) {
                    this.mFlags |= mask;
                } else {
                    this.mFlags &= ~mask;
                }
            }

            @Deprecated
            public WearableExtender setInProgressLabel(CharSequence label) {
                this.mInProgressLabel = label;
                return this;
            }

            @Deprecated
            public CharSequence getInProgressLabel() {
                return this.mInProgressLabel;
            }

            @Deprecated
            public WearableExtender setConfirmLabel(CharSequence label) {
                this.mConfirmLabel = label;
                return this;
            }

            @Deprecated
            public CharSequence getConfirmLabel() {
                return this.mConfirmLabel;
            }

            @Deprecated
            public WearableExtender setCancelLabel(CharSequence label) {
                this.mCancelLabel = label;
                return this;
            }

            @Deprecated
            public CharSequence getCancelLabel() {
                return this.mCancelLabel;
            }

            public WearableExtender setHintLaunchesActivity(boolean hintLaunchesActivity) {
                setFlag(2, hintLaunchesActivity);
                return this;
            }

            public boolean getHintLaunchesActivity() {
                return (this.mFlags & 2) != 0;
            }

            public WearableExtender setHintDisplayActionInline(boolean hintDisplayInline) {
                setFlag(4, hintDisplayInline);
                return this;
            }

            public boolean getHintDisplayActionInline() {
                return (this.mFlags & 4) != 0;
            }
        }
    }

    public Notification() {
        this.number = 0;
        this.audioStreamType = -1;
        this.audioAttributes = AUDIO_ATTRIBUTES_DEFAULT;
        this.color = 0;
        this.extras = new Bundle();
        this.mGroupAlertBehavior = 0;
        this.mBadgeIcon = 0;
        this.majorPriority = -1;
        this.displayFlag = -1;
        this.clearFlag = -1;
        this.when = System.currentTimeMillis();
        this.creationTime = System.currentTimeMillis();
        this.priority = 0;
    }

    private protected Notification(Context context, int icon, CharSequence tickerText, long when, CharSequence contentTitle, CharSequence contentText, Intent contentIntent) {
        this.number = 0;
        this.audioStreamType = -1;
        this.audioAttributes = AUDIO_ATTRIBUTES_DEFAULT;
        this.color = 0;
        this.extras = new Bundle();
        this.mGroupAlertBehavior = 0;
        this.mBadgeIcon = 0;
        this.majorPriority = -1;
        this.displayFlag = -1;
        this.clearFlag = -1;
        new Builder(context).setWhen(when).setSmallIcon(icon).setTicker(tickerText).setContentTitle(contentTitle).setContentText(contentText).setContentIntent(PendingIntent.getActivity(context, 0, contentIntent, 0)).buildInto(this);
    }

    @Deprecated
    public Notification(int icon, CharSequence tickerText, long when) {
        this.number = 0;
        this.audioStreamType = -1;
        this.audioAttributes = AUDIO_ATTRIBUTES_DEFAULT;
        this.color = 0;
        this.extras = new Bundle();
        this.mGroupAlertBehavior = 0;
        this.mBadgeIcon = 0;
        this.majorPriority = -1;
        this.displayFlag = -1;
        this.clearFlag = -1;
        this.icon = icon;
        this.tickerText = tickerText;
        this.when = when;
        this.creationTime = System.currentTimeMillis();
    }

    public Notification(Parcel parcel) {
        this.number = 0;
        this.audioStreamType = -1;
        this.audioAttributes = AUDIO_ATTRIBUTES_DEFAULT;
        this.color = 0;
        this.extras = new Bundle();
        this.mGroupAlertBehavior = 0;
        this.mBadgeIcon = 0;
        this.majorPriority = -1;
        this.displayFlag = -1;
        this.clearFlag = -1;
        readFromParcelImpl(parcel);
        this.allPendingIntents = parcel.readArraySet(null);
    }

    private synchronized void readFromParcelImpl(Parcel parcel) {
        parcel.readInt();
        this.mWhitelistToken = parcel.readStrongBinder();
        if (this.mWhitelistToken == null) {
            this.mWhitelistToken = processWhitelistToken;
        }
        parcel.setClassCookie(PendingIntent.class, this.mWhitelistToken);
        this.when = parcel.readLong();
        this.creationTime = parcel.readLong();
        if (parcel.readInt() != 0) {
            this.mSmallIcon = Icon.CREATOR.createFromParcel(parcel);
            if (this.mSmallIcon.getType() == 2) {
                this.icon = this.mSmallIcon.getResId();
            }
        }
        this.number = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.contentIntent = PendingIntent.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.deleteIntent = PendingIntent.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.tickerText = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.tickerView = RemoteViews.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.contentView = RemoteViews.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.mLargeIcon = Icon.CREATOR.createFromParcel(parcel);
        }
        this.defaults = parcel.readInt();
        this.flags = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.sound = Uri.CREATOR.createFromParcel(parcel);
        }
        this.audioStreamType = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.audioAttributes = AudioAttributes.CREATOR.createFromParcel(parcel);
        }
        this.vibrate = parcel.createLongArray();
        this.ledARGB = parcel.readInt();
        this.ledOnMS = parcel.readInt();
        this.ledOffMS = parcel.readInt();
        this.iconLevel = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.fullScreenIntent = PendingIntent.CREATOR.createFromParcel(parcel);
        }
        this.priority = parcel.readInt();
        this.category = parcel.readString();
        this.mGroupKey = parcel.readString();
        this.mSortKey = parcel.readString();
        this.extras = Bundle.setDefusable(parcel.readBundle(), true);
        fixDuplicateExtras();
        this.actions = (Action[]) parcel.createTypedArray(Action.CREATOR);
        if (parcel.readInt() != 0) {
            this.bigContentView = RemoteViews.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.headsUpContentView = RemoteViews.CREATOR.createFromParcel(parcel);
        }
        this.visibility = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.publicVersion = CREATOR.createFromParcel(parcel);
        }
        this.color = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.mChannelId = parcel.readString();
        }
        this.mTimeout = parcel.readLong();
        if (parcel.readInt() != 0) {
            this.mShortcutId = parcel.readString();
        }
        this.mBadgeIcon = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.mSettingsText = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        }
        this.mGroupAlertBehavior = parcel.readInt();
        this.majorPriority = parcel.readInt();
        this.displayFlag = parcel.readInt();
        this.clearFlag = parcel.readInt();
    }

    /* renamed from: clone */
    public Notification m8clone() {
        Notification that = new Notification();
        cloneInto(that, true);
        return that;
    }

    public synchronized void cloneInto(Notification that, boolean heavy) {
        that.mWhitelistToken = this.mWhitelistToken;
        that.when = this.when;
        that.creationTime = this.creationTime;
        that.mSmallIcon = this.mSmallIcon;
        that.number = this.number;
        that.contentIntent = this.contentIntent;
        that.deleteIntent = this.deleteIntent;
        that.fullScreenIntent = this.fullScreenIntent;
        if (this.tickerText != null) {
            that.tickerText = this.tickerText.toString();
        }
        if (heavy && this.tickerView != null) {
            that.tickerView = this.tickerView.mo11clone();
        }
        if (heavy && this.contentView != null) {
            that.contentView = this.contentView.mo11clone();
        }
        if (heavy && this.mLargeIcon != null) {
            that.mLargeIcon = this.mLargeIcon;
        }
        that.iconLevel = this.iconLevel;
        that.sound = this.sound;
        that.audioStreamType = this.audioStreamType;
        if (this.audioAttributes != null) {
            that.audioAttributes = new AudioAttributes.Builder(this.audioAttributes).build();
        }
        long[] vibrate = this.vibrate;
        if (vibrate != null) {
            int N = vibrate.length;
            long[] vib = new long[N];
            that.vibrate = vib;
            System.arraycopy(vibrate, 0, vib, 0, N);
        }
        that.ledARGB = this.ledARGB;
        that.ledOnMS = this.ledOnMS;
        that.ledOffMS = this.ledOffMS;
        that.defaults = this.defaults;
        that.flags = this.flags;
        that.priority = this.priority;
        that.category = this.category;
        that.mGroupKey = this.mGroupKey;
        that.mSortKey = this.mSortKey;
        if (this.extras != null) {
            try {
                that.extras = new Bundle(this.extras);
                that.extras.size();
            } catch (BadParcelableException e) {
                Log.e(TAG, "could not unparcel extras from notification: " + this, e);
                that.extras = null;
            }
        }
        if (!ArrayUtils.isEmpty(this.allPendingIntents)) {
            that.allPendingIntents = new ArraySet<>(this.allPendingIntents);
        }
        if (this.actions != null) {
            that.actions = new Action[this.actions.length];
            for (int i = 0; i < this.actions.length; i++) {
                if (this.actions[i] != null) {
                    that.actions[i] = this.actions[i].m9clone();
                }
            }
        }
        if (heavy && this.bigContentView != null) {
            that.bigContentView = this.bigContentView.mo11clone();
        }
        if (heavy && this.headsUpContentView != null) {
            that.headsUpContentView = this.headsUpContentView.mo11clone();
        }
        that.visibility = this.visibility;
        if (this.publicVersion != null) {
            that.publicVersion = new Notification();
            this.publicVersion.cloneInto(that.publicVersion, heavy);
        }
        that.color = this.color;
        that.mChannelId = this.mChannelId;
        that.mTimeout = this.mTimeout;
        that.mShortcutId = this.mShortcutId;
        that.mBadgeIcon = this.mBadgeIcon;
        that.mSettingsText = this.mSettingsText;
        that.mGroupAlertBehavior = this.mGroupAlertBehavior;
        if (!heavy) {
            that.lightenPayload();
        }
        that.majorPriority = this.majorPriority;
        that.displayFlag = this.displayFlag;
        that.clearFlag = this.clearFlag;
    }

    public synchronized void visitUris(Consumer<Uri> visitor) {
        visitor.accept(this.sound);
        if (this.tickerView != null) {
            this.tickerView.visitUris(visitor);
        }
        if (this.contentView != null) {
            this.contentView.visitUris(visitor);
        }
        if (this.bigContentView != null) {
            this.bigContentView.visitUris(visitor);
        }
        if (this.headsUpContentView != null) {
            this.headsUpContentView.visitUris(visitor);
        }
        if (this.extras != null) {
            visitor.accept((Uri) this.extras.getParcelable(EXTRA_AUDIO_CONTENTS_URI));
            visitor.accept((Uri) this.extras.getParcelable(EXTRA_BACKGROUND_IMAGE_URI));
        }
        if (MessagingStyle.class.equals(getNotificationStyle()) && this.extras != null) {
            Parcelable[] messages = this.extras.getParcelableArray(EXTRA_MESSAGES);
            if (!ArrayUtils.isEmpty(messages)) {
                for (MessagingStyle.Message message : MessagingStyle.Message.getMessagesFromBundleArray(messages)) {
                    visitor.accept(message.getDataUri());
                }
            }
            Parcelable[] historic = this.extras.getParcelableArray(EXTRA_HISTORIC_MESSAGES);
            if (!ArrayUtils.isEmpty(historic)) {
                for (MessagingStyle.Message message2 : MessagingStyle.Message.getMessagesFromBundleArray(historic)) {
                    visitor.accept(message2.getDataUri());
                }
            }
        }
    }

    public final synchronized void lightenPayload() {
        Object obj;
        this.tickerView = null;
        this.contentView = null;
        this.bigContentView = null;
        this.headsUpContentView = null;
        this.mLargeIcon = null;
        if (this.extras != null && !this.extras.isEmpty()) {
            Set<String> keyset = this.extras.keySet();
            int N = keyset.size();
            String[] keys = (String[]) keyset.toArray(new String[N]);
            for (int i = 0; i < N; i++) {
                String key = keys[i];
                if (!"android.tv.EXTENSIONS".equals(key) && (obj = this.extras.get(key)) != null && ((obj instanceof Parcelable) || (obj instanceof Parcelable[]) || (obj instanceof SparseArray) || (obj instanceof ArrayList))) {
                    this.extras.remove(key);
                }
            }
        }
    }

    public static synchronized CharSequence safeCharSequence(CharSequence cs) {
        if (cs == null) {
            return cs;
        }
        if (cs.length() > 5120) {
            cs = cs.subSequence(0, 5120);
        }
        if (cs instanceof Parcelable) {
            Log.e(TAG, "warning: " + cs.getClass().getCanonicalName() + " instance is a custom Parcelable and not allowed in Notification");
            return cs.toString();
        }
        return removeTextSizeSpans(cs);
    }

    private static synchronized CharSequence removeTextSizeSpans(CharSequence charSequence) {
        Object resultSpan;
        if (charSequence instanceof Spanned) {
            Spanned ss = (Spanned) charSequence;
            Object[] spans = ss.getSpans(0, ss.length(), Object.class);
            SpannableStringBuilder builder = new SpannableStringBuilder(ss.toString());
            for (Object span : spans) {
                Object resultSpan2 = span;
                if (resultSpan2 instanceof CharacterStyle) {
                    resultSpan2 = ((CharacterStyle) span).getUnderlying();
                }
                if (!(resultSpan2 instanceof TextAppearanceSpan)) {
                    if (!(resultSpan2 instanceof RelativeSizeSpan) && !(resultSpan2 instanceof AbsoluteSizeSpan)) {
                        resultSpan = span;
                    }
                } else {
                    TextAppearanceSpan originalSpan = (TextAppearanceSpan) resultSpan2;
                    resultSpan = new TextAppearanceSpan(originalSpan.getFamily(), originalSpan.getTextStyle(), -1, originalSpan.getTextColor(), originalSpan.getLinkTextColor());
                }
                builder.setSpan(resultSpan, ss.getSpanStart(span), ss.getSpanEnd(span), ss.getSpanFlags(span));
            }
            return builder;
        }
        return charSequence;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(final Parcel parcel, int flags) {
        boolean collectPendingIntents = this.allPendingIntents == null;
        if (collectPendingIntents) {
            PendingIntent.setOnMarshaledListener(new PendingIntent.OnMarshaledListener() { // from class: android.app.-$$Lambda$Notification$hOCsSZH8tWalFSbIzQ9x9IcPa9M
                @Override // android.app.PendingIntent.OnMarshaledListener
                public final void onMarshaled(PendingIntent pendingIntent, Parcel parcel2, int i) {
                    Notification.lambda$writeToParcel$0(Notification.this, parcel, pendingIntent, parcel2, i);
                }
            });
        }
        try {
            writeToParcelImpl(parcel, flags);
            parcel.writeArraySet(this.allPendingIntents);
        } finally {
            if (collectPendingIntents) {
                PendingIntent.setOnMarshaledListener(null);
            }
        }
    }

    public static /* synthetic */ void lambda$writeToParcel$0(Notification notification, Parcel parcel, PendingIntent intent, Parcel out, int outFlags) {
        if (parcel == out) {
            if (notification.allPendingIntents == null) {
                notification.allPendingIntents = new ArraySet<>();
            }
            notification.allPendingIntents.add(intent);
        }
    }

    private synchronized void writeToParcelImpl(Parcel parcel, int flags) {
        parcel.writeInt(1);
        parcel.writeStrongBinder(this.mWhitelistToken);
        parcel.writeLong(this.when);
        parcel.writeLong(this.creationTime);
        if (this.mSmallIcon == null && this.icon != 0) {
            this.mSmallIcon = Icon.createWithResource("", this.icon);
        }
        if (this.mSmallIcon != null) {
            parcel.writeInt(1);
            this.mSmallIcon.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.number);
        if (this.contentIntent != null) {
            parcel.writeInt(1);
            this.contentIntent.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        if (this.deleteIntent != null) {
            parcel.writeInt(1);
            this.deleteIntent.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        if (this.tickerText != null) {
            parcel.writeInt(1);
            TextUtils.writeToParcel(this.tickerText, parcel, flags);
        } else {
            parcel.writeInt(0);
        }
        if (this.tickerView != null) {
            parcel.writeInt(1);
            this.tickerView.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        if (this.contentView != null) {
            parcel.writeInt(1);
            this.contentView.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        if (this.mLargeIcon == null && this.largeIcon != null) {
            this.mLargeIcon = Icon.createWithBitmap(this.largeIcon);
        }
        if (this.mLargeIcon != null) {
            parcel.writeInt(1);
            this.mLargeIcon.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.defaults);
        parcel.writeInt(this.flags);
        if (this.sound != null) {
            parcel.writeInt(1);
            this.sound.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.audioStreamType);
        if (this.audioAttributes != null) {
            parcel.writeInt(1);
            this.audioAttributes.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeLongArray(this.vibrate);
        parcel.writeInt(this.ledARGB);
        parcel.writeInt(this.ledOnMS);
        parcel.writeInt(this.ledOffMS);
        parcel.writeInt(this.iconLevel);
        if (this.fullScreenIntent != null) {
            parcel.writeInt(1);
            this.fullScreenIntent.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.priority);
        parcel.writeString(this.category);
        parcel.writeString(this.mGroupKey);
        parcel.writeString(this.mSortKey);
        parcel.writeBundle(this.extras);
        parcel.writeTypedArray(this.actions, 0);
        if (this.bigContentView != null) {
            parcel.writeInt(1);
            this.bigContentView.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        if (this.headsUpContentView != null) {
            parcel.writeInt(1);
            this.headsUpContentView.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.visibility);
        if (this.publicVersion != null) {
            parcel.writeInt(1);
            this.publicVersion.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.color);
        if (this.mChannelId != null) {
            parcel.writeInt(1);
            parcel.writeString(this.mChannelId);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeLong(this.mTimeout);
        if (this.mShortcutId != null) {
            parcel.writeInt(1);
            parcel.writeString(this.mShortcutId);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.mBadgeIcon);
        if (this.mSettingsText != null) {
            parcel.writeInt(1);
            TextUtils.writeToParcel(this.mSettingsText, parcel, flags);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.mGroupAlertBehavior);
        parcel.writeInt(this.majorPriority);
        parcel.writeInt(this.displayFlag);
        parcel.writeInt(this.clearFlag);
    }

    public static synchronized boolean areActionsVisiblyDifferent(Notification first, Notification second) {
        Action[] firstAs = first.actions;
        Action[] secondAs = second.actions;
        if ((firstAs == null && secondAs != null) || (firstAs != null && secondAs == null)) {
            return true;
        }
        if (firstAs != null && secondAs != null) {
            if (firstAs.length != secondAs.length) {
                return true;
            }
            for (int i = 0; i < firstAs.length; i++) {
                if (!Objects.equals(String.valueOf(firstAs[i].title), String.valueOf(secondAs[i].title))) {
                    return true;
                }
                RemoteInput[] firstRs = firstAs[i].getRemoteInputs();
                RemoteInput[] secondRs = secondAs[i].getRemoteInputs();
                if (firstRs == null) {
                    firstRs = new RemoteInput[0];
                }
                if (secondRs == null) {
                    secondRs = new RemoteInput[0];
                }
                if (firstRs.length != secondRs.length) {
                    return true;
                }
                for (int j = 0; j < firstRs.length; j++) {
                    if (!Objects.equals(String.valueOf(firstRs[j].getLabel()), String.valueOf(secondRs[j].getLabel()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static synchronized boolean areStyledNotificationsVisiblyDifferent(Builder first, Builder second) {
        if (first.getStyle() == null) {
            return second.getStyle() != null;
        } else if (second.getStyle() == null) {
            return true;
        } else {
            return first.getStyle().areNotificationsVisiblyDifferent(second.getStyle());
        }
    }

    public static synchronized boolean areRemoteViewsChanged(Builder first, Builder second) {
        return !Objects.equals(Boolean.valueOf(first.usesStandardHeader()), Boolean.valueOf(second.usesStandardHeader())) || areRemoteViewsChanged(first.mN.contentView, second.mN.contentView) || areRemoteViewsChanged(first.mN.bigContentView, second.mN.bigContentView) || areRemoteViewsChanged(first.mN.headsUpContentView, second.mN.headsUpContentView);
    }

    private static synchronized boolean areRemoteViewsChanged(RemoteViews first, RemoteViews second) {
        if (first == null && second == null) {
            return false;
        }
        if ((first != null || second == null) && ((first == null || second != null) && Objects.equals(Integer.valueOf(first.getLayoutId()), Integer.valueOf(second.getLayoutId())) && Objects.equals(Integer.valueOf(first.getSequenceNumber()), Integer.valueOf(second.getSequenceNumber())))) {
            return false;
        }
        return true;
    }

    private synchronized void fixDuplicateExtras() {
        if (this.extras != null) {
            fixDuplicateExtra(this.mLargeIcon, EXTRA_LARGE_ICON);
        }
    }

    private synchronized void fixDuplicateExtra(Parcelable original, String extraName) {
        if (original != null && this.extras.getParcelable(extraName) != null) {
            this.extras.putParcelable(extraName, original);
        }
    }

    @Deprecated
    private protected void setLatestEventInfo(Context context, CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent) {
        if (context.getApplicationInfo().targetSdkVersion > 22) {
            Log.e(TAG, "setLatestEventInfo() is deprecated and you should feel deprecated.", new Throwable());
        }
        if (context.getApplicationInfo().targetSdkVersion < 24) {
            this.extras.putBoolean(EXTRA_SHOW_WHEN, true);
        }
        Builder builder = new Builder(context, this);
        if (contentTitle != null) {
            builder.setContentTitle(contentTitle);
        }
        if (contentText != null) {
            builder.setContentText(contentText);
        }
        builder.setContentIntent(contentIntent);
        builder.build();
    }

    public static synchronized void addFieldsFromContext(Context context, Notification notification) {
        addFieldsFromContext(context.getApplicationInfo(), notification);
    }

    public static synchronized void addFieldsFromContext(ApplicationInfo ai, Notification notification) {
        notification.extras.putParcelable(EXTRA_BUILDER_APPLICATION_INFO, ai);
    }

    public synchronized void writeToProto(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1138166333441L, getChannelId());
        proto.write(1133871366146L, this.tickerText != null);
        proto.write(1120986464259L, this.flags);
        proto.write(1120986464260L, this.color);
        proto.write(1138166333445L, this.category);
        proto.write(1138166333446L, this.mGroupKey);
        proto.write(1138166333447L, this.mSortKey);
        if (this.actions != null) {
            proto.write(1120986464264L, this.actions.length);
        }
        if (this.visibility >= -1 && this.visibility <= 1) {
            proto.write(1159641169929L, this.visibility);
        }
        if (this.publicVersion != null) {
            this.publicVersion.writeToProto(proto, 1146756268042L);
        }
        proto.end(token);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Notification(channel=");
        sb.append(getChannelId());
        sb.append(" pri=");
        sb.append(this.priority);
        sb.append(" contentView=");
        if (this.contentView != null) {
            sb.append(this.contentView.getPackage());
            sb.append("/0x");
            sb.append(Integer.toHexString(this.contentView.getLayoutId()));
        } else {
            sb.append("null");
        }
        sb.append(" vibrate=");
        if ((this.defaults & 2) != 0) {
            sb.append("default");
        } else if (this.vibrate != null) {
            int N = this.vibrate.length - 1;
            sb.append("[");
            for (int i = 0; i < N; i++) {
                sb.append(this.vibrate[i]);
                sb.append(',');
            }
            if (N != -1) {
                sb.append(this.vibrate[N]);
            }
            sb.append("]");
        } else {
            sb.append("null");
        }
        sb.append(" sound=");
        if ((this.defaults & 1) != 0) {
            sb.append("default");
        } else if (this.sound != null) {
            sb.append(this.sound.toString());
        } else {
            sb.append("null");
        }
        if (this.tickerText != null) {
            sb.append(" tick");
        }
        sb.append(" defaults=0x");
        sb.append(Integer.toHexString(this.defaults));
        sb.append(" flags=0x");
        sb.append(Integer.toHexString(this.flags));
        sb.append(String.format(" color=0x%08x", Integer.valueOf(this.color)));
        if (this.category != null) {
            sb.append(" category=");
            sb.append(this.category);
        }
        if (this.mGroupKey != null) {
            sb.append(" groupKey=");
            sb.append(this.mGroupKey);
        }
        if (this.mSortKey != null) {
            sb.append(" sortKey=");
            sb.append(this.mSortKey);
        }
        if (this.actions != null) {
            sb.append(" actions=");
            sb.append(this.actions.length);
        }
        sb.append(" vis=");
        sb.append(visibilityToString(this.visibility));
        if (this.publicVersion != null) {
            sb.append(" publicVersion=");
            sb.append(this.publicVersion.toString());
        }
        sb.append(" majorPriority=0x");
        sb.append(Integer.toHexString(this.majorPriority));
        sb.append(" displayFlag=0x");
        sb.append(Integer.toHexString(this.displayFlag));
        sb.append(" clearFlag=0x");
        sb.append(Integer.toHexString(this.clearFlag));
        sb.append(" number=");
        sb.append(Integer.toHexString(this.number));
        sb.append(")");
        return sb.toString();
    }

    public static synchronized String visibilityToString(int vis) {
        switch (vis) {
            case -1:
                return "SECRET";
            case 0:
                return "PRIVATE";
            case 1:
                return "PUBLIC";
            default:
                return "UNKNOWN(" + String.valueOf(vis) + ")";
        }
    }

    public static synchronized String priorityToString(int pri) {
        switch (pri) {
            case -2:
                return "MIN";
            case -1:
                return "LOW";
            case 0:
                return "DEFAULT";
            case 1:
                return "HIGH";
            case 2:
                return "MAX";
            default:
                return "UNKNOWN(" + String.valueOf(pri) + ")";
        }
    }

    public synchronized boolean hasCompletedProgress() {
        return this.extras.containsKey(EXTRA_PROGRESS) && this.extras.containsKey(EXTRA_PROGRESS_MAX) && this.extras.getInt(EXTRA_PROGRESS_MAX) != 0 && this.extras.getInt(EXTRA_PROGRESS) == this.extras.getInt(EXTRA_PROGRESS_MAX);
    }

    @Deprecated
    private protected String getChannel() {
        return this.mChannelId;
    }

    public String getChannelId() {
        return this.mChannelId;
    }

    @Deprecated
    private protected long getTimeout() {
        return this.mTimeout;
    }

    public long getTimeoutAfter() {
        return this.mTimeout;
    }

    public int getBadgeIconType() {
        return this.mBadgeIcon;
    }

    public String getShortcutId() {
        return this.mShortcutId;
    }

    public CharSequence getSettingsText() {
        return this.mSettingsText;
    }

    public int getGroupAlertBehavior() {
        return this.mGroupAlertBehavior;
    }

    public Icon getSmallIcon() {
        return this.mSmallIcon;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSmallIcon(Icon icon) {
        this.mSmallIcon = icon;
    }

    public Icon getLargeIcon() {
        return this.mLargeIcon;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isGroupSummary() {
        return (this.mGroupKey == null || (this.flags & 512) == 0) ? false : true;
    }

    private protected boolean isGroupChild() {
        return this.mGroupKey != null && (this.flags & 512) == 0;
    }

    public synchronized boolean suppressAlertingDueToGrouping() {
        if (isGroupSummary() && getGroupAlertBehavior() == 2) {
            return true;
        }
        return isGroupChild() && getGroupAlertBehavior() == 1;
    }

    public boolean isUnread() {
        return this.number > 0;
    }

    public boolean hasDisplayFlag(int flag) {
        return this.displayFlag > 0 && (this.displayFlag & flag) == flag;
    }

    public CharSequence getDetails() {
        return this.extras.getCharSequence(EXTRA_DETAIL);
    }

    /* loaded from: classes.dex */
    public static class Details {
        public static final int ALIGN_CENTER = 1;
        public static final int ALIGN_LEFT = 0;
        public static final int ALIGN_RIGHT = 2;
        public static final String KEY_ACTION = "action";
        public static final String KEY_ALIGN = "align";
        public static final String KEY_CONTENT = "content";
        public static final String KEY_DETAILS = "details";
        public static final String KEY_DETAILS_TYPE = "detailsType";
        public static final String KEY_DETAILS_VERSION = "detailsVersion";
        public static final String KEY_HEIGHT = "height";
        public static final String KEY_SUBTYPE = "subType";
        public static final String KEY_TEXTCOLOR = "textColor";
        public static final String KEY_TEXTSIZE = "textSize";
        public static final String KEY_TYPE = "type";
        public static final String KEY_URL = "url";
        public static final String KEY_VERISON = "version";
        public static final String KEY_WIDTH = "width";
        public static final int TYPE_AUDIO = 3;
        public static final int TYPE_BUTTON = 5;
        public static final int TYPE_IMAGE = 1;
        public static final int TYPE_MAX = 5;
        public static final int TYPE_MIN = 0;
        public static final int TYPE_PHONE = 4;
        public static final int TYPE_TEXT = 0;
        public static final int TYPE_VIDEO = 2;

        public static int getDetailsType(String jsonContent) {
            if (!TextUtils.isEmpty(jsonContent)) {
                try {
                    JSONObject object = new JSONObject(jsonContent);
                    return object.getInt(KEY_DETAILS_TYPE);
                } catch (Exception e) {
                    return -1;
                }
            }
            return -1;
        }

        public static String getDetailsVersion(String jsonContent) {
            if (!TextUtils.isEmpty(jsonContent)) {
                try {
                    JSONObject object = new JSONObject(jsonContent);
                    return object.getString(KEY_DETAILS_VERSION);
                } catch (Exception e) {
                    return "";
                }
            }
            return "";
        }

        public static List<Map<String, Object>> getDetailsList(String jsonContent) {
            List<Map<String, Object>> list = new ArrayList<>();
            if (!TextUtils.isEmpty(jsonContent)) {
                try {
                    JSONObject object = new JSONObject(jsonContent);
                    JSONArray array = object.getJSONArray(KEY_DETAILS);
                    if (array != null) {
                        for (int i = 0; i < array.length(); i++) {
                            Map<String, Object> map = new HashMap<>();
                            JSONObject jo = array.getJSONObject(i);
                            if (hasJsonObject(jo, "type")) {
                                Iterator<String> keys = jo.keys();
                                while (keys.hasNext()) {
                                    try {
                                        String key = keys.next().trim();
                                        if ((object.get(key) instanceof String) || (object.get(key) instanceof Float) || (object.get(key) instanceof Integer) || (object.get(key) instanceof Boolean) || (object.get(key) instanceof Double) || (object.get(key) instanceof Long) || (object.get(key) instanceof Integer)) {
                                            map.put(key, jo.get(key));
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }
                            list.add(map);
                        }
                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
            return list;
        }

        public static String getDetailsJson(List<Map<String, Object>> details, int detailsType, String detailsVersion) {
            JSONArray array = new JSONArray();
            if (details != null && !details.isEmpty()) {
                for (Map<String, Object> map : details) {
                    if (map != null && !map.isEmpty() && map.containsKey("type")) {
                        try {
                            int type = Integer.parseInt(map.get("type").toString());
                            if (type >= 0 && type <= 5) {
                                array.put(new JSONObject(map));
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
            try {
                JSONObject object = new JSONObject();
                object.put(KEY_DETAILS_TYPE, detailsType);
                object.put(KEY_DETAILS_VERSION, detailsVersion);
                object.put(KEY_DETAILS, array);
                return object.toString();
            } catch (JSONException e2) {
                e2.printStackTrace();
                return null;
            }
        }

        private static boolean hasJsonObject(JSONObject object, String key) {
            if (object != null) {
                try {
                    if (TextUtils.isEmpty(key) || !object.has(key)) {
                        return false;
                    }
                    if (!TextUtils.isEmpty(object.get(key).toString())) {
                        return true;
                    }
                    return false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        }

        private static String getJsonObject(JSONObject object, String key) {
            if (hasJsonObject(object, key)) {
                try {
                    return object.get(key).toString();
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static class DetailsBuilder {
        private List<Map<String, Object>> details;
        private int detailsType;
        private String detailsVersion;

        public DetailsBuilder() {
            this(-1);
        }

        public DetailsBuilder(int detailsType) {
            this(detailsType, "");
        }

        public DetailsBuilder(int detailsType, String detailsVersion) {
            this.details = new ArrayList();
            this.details.clear();
        }

        public DetailsBuilder add(Map<String, Object> detail) {
            if (!detail.isEmpty()) {
                this.details.add(detail);
            }
            return this;
        }

        public String toString() {
            return Details.getDetailsJson(this.details, this.detailsType, this.detailsVersion);
        }
    }

    /* loaded from: classes.dex */
    public static class Builder {
        public static final String EXTRA_REBUILD_BIG_CONTENT_VIEW_ACTION_COUNT = "android.rebuild.bigViewActionCount";
        public static final String EXTRA_REBUILD_CONTENT_VIEW_ACTION_COUNT = "android.rebuild.contentViewActionCount";
        public static final String EXTRA_REBUILD_HEADS_UP_CONTENT_VIEW_ACTION_COUNT = "android.rebuild.hudViewActionCount";
        private static final int LIGHTNESS_TEXT_DIFFERENCE_DARK = -10;
        private static final int LIGHTNESS_TEXT_DIFFERENCE_LIGHT = 20;
        private static final int MAX_ACTION_BUTTONS = 3;
        private static final boolean USE_ONLY_TITLE_IN_LOW_PRIORITY_SUMMARY = SystemProperties.getBoolean("notifications.only_title", true);
        public protected ArrayList<Action> mActions;
        private int mBackgroundColor;
        private int mCachedAmbientColor;
        private int mCachedAmbientColorIsFor;
        private int mCachedContrastColor;
        private int mCachedContrastColorIsFor;
        private NotificationColorUtil mColorUtil;
        private Context mContext;
        private int mForegroundColor;
        private boolean mInNightMode;
        private boolean mIsLegacy;
        private boolean mIsLegacyInitialized;
        private Notification mN;
        private int mNeutralColor;
        private ArrayList<Action> mOriginalActions;
        StandardTemplateParams mParams;
        private ArrayList<Person> mPersonList;
        private int mPrimaryTextColor;
        private boolean mRebuildStyledRemoteViews;
        private int mSecondaryTextColor;
        private Style mStyle;
        private int mTextColorsAreForBackground;
        private boolean mTintActionButtons;
        private Bundle mUserExtras;

        public Builder(Context context, String channelId) {
            this(context, (Notification) null);
            this.mN.mChannelId = channelId;
        }

        @Deprecated
        public Builder(Context context) {
            this(context, (Notification) null);
        }

        public synchronized Builder(Context context, Notification toAdopt) {
            this.mUserExtras = new Bundle();
            this.mActions = new ArrayList<>(3);
            this.mPersonList = new ArrayList<>();
            this.mCachedContrastColor = 1;
            this.mCachedContrastColorIsFor = 1;
            this.mCachedAmbientColor = 1;
            this.mCachedAmbientColorIsFor = 1;
            this.mNeutralColor = 1;
            this.mParams = new StandardTemplateParams();
            this.mTextColorsAreForBackground = 1;
            this.mPrimaryTextColor = 1;
            this.mSecondaryTextColor = 1;
            this.mBackgroundColor = 1;
            this.mForegroundColor = 1;
            this.mContext = context;
            Resources res = this.mContext.getResources();
            this.mTintActionButtons = res.getBoolean(R.bool.config_tintNotificationActionButtons);
            if (res.getBoolean(R.bool.config_enableNightMode)) {
                Configuration currentConfig = res.getConfiguration();
                this.mInNightMode = (currentConfig.uiMode & 48) == 32;
            }
            if (toAdopt == null) {
                this.mN = new Notification();
                if (context.getApplicationInfo().targetSdkVersion < 24) {
                    this.mN.extras.putBoolean(Notification.EXTRA_SHOW_WHEN, true);
                }
                this.mN.priority = 0;
                this.mN.visibility = 0;
                return;
            }
            this.mN = toAdopt;
            if (this.mN.actions != null) {
                Collections.addAll(this.mActions, this.mN.actions);
            }
            if (this.mN.extras.containsKey(Notification.EXTRA_PEOPLE_LIST)) {
                ArrayList<Person> people = this.mN.extras.getParcelableArrayList(Notification.EXTRA_PEOPLE_LIST);
                this.mPersonList.addAll(people);
            }
            if (this.mN.getSmallIcon() == null && this.mN.icon != 0) {
                setSmallIcon(this.mN.icon);
            }
            if (this.mN.getLargeIcon() == null && this.mN.largeIcon != null) {
                setLargeIcon(this.mN.largeIcon);
            }
            String templateClass = this.mN.extras.getString(Notification.EXTRA_TEMPLATE);
            if (!TextUtils.isEmpty(templateClass)) {
                Class<? extends Style> styleClass = Notification.getNotificationStyleClass(templateClass);
                if (styleClass == null) {
                    Log.d(Notification.TAG, "Unknown style class: " + templateClass);
                    return;
                }
                try {
                    Constructor<? extends Style> ctor = styleClass.getDeclaredConstructor(new Class[0]);
                    ctor.setAccessible(true);
                    Style style = ctor.newInstance(new Object[0]);
                    style.restoreFromExtras(this.mN.extras);
                    if (style != null) {
                        setStyle(style);
                    }
                } catch (Throwable t) {
                    Log.e(Notification.TAG, "Could not create Style", t);
                }
            }
        }

        public Builder setDetails(CharSequence text) {
            this.mN.extras.putCharSequence(Notification.EXTRA_DETAIL, Notification.safeCharSequence(text));
            return this;
        }

        public Builder setMajorPriority(int majorPriority) {
            this.mN.majorPriority = majorPriority;
            return this;
        }

        public Builder setDisplayFlag(int displayFlag) {
            this.mN.displayFlag = displayFlag;
            return this;
        }

        public Builder setClearFlag(int clearFlag) {
            this.mN.clearFlag = clearFlag;
            return this;
        }

        private synchronized NotificationColorUtil getColorUtil() {
            if (this.mColorUtil == null) {
                this.mColorUtil = NotificationColorUtil.getInstance(this.mContext);
            }
            return this.mColorUtil;
        }

        public Builder setShortcutId(String shortcutId) {
            this.mN.mShortcutId = shortcutId;
            return this;
        }

        public Builder setBadgeIconType(int icon) {
            this.mN.mBadgeIcon = icon;
            return this;
        }

        public Builder setGroupAlertBehavior(int groupAlertBehavior) {
            this.mN.mGroupAlertBehavior = groupAlertBehavior;
            return this;
        }

        @Deprecated
        private protected Builder setChannel(String channelId) {
            this.mN.mChannelId = channelId;
            return this;
        }

        public Builder setChannelId(String channelId) {
            this.mN.mChannelId = channelId;
            return this;
        }

        @Deprecated
        private protected Builder setTimeout(long durationMs) {
            this.mN.mTimeout = durationMs;
            return this;
        }

        public Builder setTimeoutAfter(long durationMs) {
            this.mN.mTimeout = durationMs;
            return this;
        }

        public Builder setWhen(long when) {
            this.mN.when = when;
            return this;
        }

        public Builder setShowWhen(boolean show) {
            this.mN.extras.putBoolean(Notification.EXTRA_SHOW_WHEN, show);
            return this;
        }

        public Builder setUsesChronometer(boolean b) {
            this.mN.extras.putBoolean(Notification.EXTRA_SHOW_CHRONOMETER, b);
            return this;
        }

        public Builder setChronometerCountDown(boolean countDown) {
            this.mN.extras.putBoolean(Notification.EXTRA_CHRONOMETER_COUNT_DOWN, countDown);
            return this;
        }

        public Builder setSmallIcon(int icon) {
            Icon icon2;
            if (icon != 0) {
                icon2 = Icon.createWithResource(this.mContext, icon);
            } else {
                icon2 = null;
            }
            return setSmallIcon(icon2);
        }

        public Builder setSmallIcon(int icon, int level) {
            this.mN.iconLevel = level;
            return setSmallIcon(icon);
        }

        public Builder setSmallIcon(Icon icon) {
            this.mN.setSmallIcon(icon);
            if (icon != null && icon.getType() == 2) {
                this.mN.icon = icon.getResId();
            }
            return this;
        }

        public Builder setContentTitle(CharSequence title) {
            this.mN.extras.putCharSequence(Notification.EXTRA_TITLE, Notification.safeCharSequence(title));
            return this;
        }

        public Builder setContentText(CharSequence text) {
            this.mN.extras.putCharSequence(Notification.EXTRA_TEXT, Notification.safeCharSequence(text));
            return this;
        }

        public Builder setSubText(CharSequence text) {
            this.mN.extras.putCharSequence(Notification.EXTRA_SUB_TEXT, Notification.safeCharSequence(text));
            return this;
        }

        public Builder setSettingsText(CharSequence text) {
            this.mN.mSettingsText = Notification.safeCharSequence(text);
            return this;
        }

        public Builder setRemoteInputHistory(CharSequence[] text) {
            if (text == null) {
                this.mN.extras.putCharSequenceArray(Notification.EXTRA_REMOTE_INPUT_HISTORY, null);
            } else {
                int N = Math.min(5, text.length);
                CharSequence[] safe = new CharSequence[N];
                for (int i = 0; i < N; i++) {
                    safe[i] = Notification.safeCharSequence(text[i]);
                }
                this.mN.extras.putCharSequenceArray(Notification.EXTRA_REMOTE_INPUT_HISTORY, safe);
            }
            return this;
        }

        public synchronized Builder setShowRemoteInputSpinner(boolean showSpinner) {
            this.mN.extras.putBoolean(Notification.EXTRA_SHOW_REMOTE_INPUT_SPINNER, showSpinner);
            return this;
        }

        public synchronized Builder setHideSmartReplies(boolean hideSmartReplies) {
            this.mN.extras.putBoolean(Notification.EXTRA_HIDE_SMART_REPLIES, hideSmartReplies);
            return this;
        }

        public Builder setNumber(int number) {
            this.mN.number = number;
            return this;
        }

        @Deprecated
        public Builder setContentInfo(CharSequence info) {
            this.mN.extras.putCharSequence(Notification.EXTRA_INFO_TEXT, Notification.safeCharSequence(info));
            return this;
        }

        public Builder setProgress(int max, int progress, boolean indeterminate) {
            this.mN.extras.putInt(Notification.EXTRA_PROGRESS, progress);
            this.mN.extras.putInt(Notification.EXTRA_PROGRESS_MAX, max);
            this.mN.extras.putBoolean(Notification.EXTRA_PROGRESS_INDETERMINATE, indeterminate);
            return this;
        }

        @Deprecated
        public Builder setContent(RemoteViews views) {
            return setCustomContentView(views);
        }

        public Builder setCustomContentView(RemoteViews contentView) {
            this.mN.contentView = contentView;
            return this;
        }

        public Builder setCustomBigContentView(RemoteViews contentView) {
            this.mN.bigContentView = contentView;
            return this;
        }

        public Builder setCustomHeadsUpContentView(RemoteViews contentView) {
            this.mN.headsUpContentView = contentView;
            return this;
        }

        public Builder setContentIntent(PendingIntent intent) {
            this.mN.contentIntent = intent;
            return this;
        }

        public Builder setDeleteIntent(PendingIntent intent) {
            this.mN.deleteIntent = intent;
            return this;
        }

        public Builder setFullScreenIntent(PendingIntent intent, boolean highPriority) {
            this.mN.fullScreenIntent = intent;
            setFlag(128, highPriority);
            return this;
        }

        public Builder setTicker(CharSequence tickerText) {
            this.mN.tickerText = Notification.safeCharSequence(tickerText);
            return this;
        }

        @Deprecated
        public Builder setTicker(CharSequence tickerText, RemoteViews views) {
            setTicker(tickerText);
            return this;
        }

        public Builder setLargeIcon(Bitmap b) {
            return setLargeIcon(b != null ? Icon.createWithBitmap(b) : null);
        }

        public Builder setLargeIcon(Icon icon) {
            this.mN.mLargeIcon = icon;
            this.mN.extras.putParcelable(Notification.EXTRA_LARGE_ICON, icon);
            return this;
        }

        @Deprecated
        public Builder setSound(Uri sound) {
            this.mN.sound = sound;
            this.mN.audioAttributes = Notification.AUDIO_ATTRIBUTES_DEFAULT;
            return this;
        }

        @Deprecated
        public Builder setSound(Uri sound, int streamType) {
            PlayerBase.deprecateStreamTypeForPlayback(streamType, Notification.TAG, "setSound()");
            this.mN.sound = sound;
            this.mN.audioStreamType = streamType;
            return this;
        }

        @Deprecated
        public Builder setSound(Uri sound, AudioAttributes audioAttributes) {
            this.mN.sound = sound;
            this.mN.audioAttributes = audioAttributes;
            return this;
        }

        @Deprecated
        public Builder setVibrate(long[] pattern) {
            this.mN.vibrate = pattern;
            return this;
        }

        @Deprecated
        public Builder setLights(int argb, int onMs, int offMs) {
            this.mN.ledARGB = argb;
            this.mN.ledOnMS = onMs;
            this.mN.ledOffMS = offMs;
            if (onMs != 0 || offMs != 0) {
                this.mN.flags |= 1;
            }
            return this;
        }

        public Builder setOngoing(boolean ongoing) {
            setFlag(2, ongoing);
            return this;
        }

        public Builder setColorized(boolean colorize) {
            this.mN.extras.putBoolean(Notification.EXTRA_COLORIZED, colorize);
            return this;
        }

        public Builder setOnlyAlertOnce(boolean onlyAlertOnce) {
            setFlag(8, onlyAlertOnce);
            return this;
        }

        public Builder setAutoCancel(boolean autoCancel) {
            setFlag(16, autoCancel);
            return this;
        }

        public Builder setLocalOnly(boolean localOnly) {
            setFlag(256, localOnly);
            return this;
        }

        @Deprecated
        public Builder setDefaults(int defaults) {
            this.mN.defaults = defaults;
            return this;
        }

        @Deprecated
        public Builder setPriority(int pri) {
            this.mN.priority = pri;
            return this;
        }

        public Builder setCategory(String category) {
            this.mN.category = category;
            return this;
        }

        public Builder addPerson(String uri) {
            addPerson(new Person.Builder().setUri(uri).build());
            return this;
        }

        public Builder addPerson(Person person) {
            this.mPersonList.add(person);
            return this;
        }

        public Builder setGroup(String groupKey) {
            this.mN.mGroupKey = groupKey;
            return this;
        }

        public Builder setGroupSummary(boolean isGroupSummary) {
            setFlag(512, isGroupSummary);
            return this;
        }

        public Builder setSortKey(String sortKey) {
            this.mN.mSortKey = sortKey;
            return this;
        }

        public Builder addExtras(Bundle extras) {
            if (extras != null) {
                this.mUserExtras.putAll(extras);
            }
            return this;
        }

        public Builder setExtras(Bundle extras) {
            if (extras != null) {
                this.mUserExtras = extras;
            }
            return this;
        }

        public Bundle getExtras() {
            return this.mUserExtras;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Bundle getAllExtras() {
            Bundle saveExtras = (Bundle) this.mUserExtras.clone();
            saveExtras.putAll(this.mN.extras);
            return saveExtras;
        }

        @Deprecated
        public Builder addAction(int icon, CharSequence title, PendingIntent intent) {
            this.mActions.add(new Action(icon, Notification.safeCharSequence(title), intent));
            return this;
        }

        public Builder addAction(Action action) {
            if (action != null) {
                this.mActions.add(action);
            }
            return this;
        }

        public Builder setActions(Action... actions) {
            this.mActions.clear();
            for (int i = 0; i < actions.length; i++) {
                if (actions[i] != null) {
                    this.mActions.add(actions[i]);
                }
            }
            return this;
        }

        public Builder setStyle(Style style) {
            if (this.mStyle != style) {
                this.mStyle = style;
                if (this.mStyle != null) {
                    this.mStyle.setBuilder(this);
                    this.mN.extras.putString(Notification.EXTRA_TEMPLATE, style.getClass().getName());
                } else {
                    this.mN.extras.remove(Notification.EXTRA_TEMPLATE);
                }
            }
            return this;
        }

        public Style getStyle() {
            return this.mStyle;
        }

        public Builder setVisibility(int visibility) {
            this.mN.visibility = visibility;
            return this;
        }

        public Builder setPublicVersion(Notification n) {
            if (n != null) {
                this.mN.publicVersion = new Notification();
                n.cloneInto(this.mN.publicVersion, true);
            } else {
                this.mN.publicVersion = null;
            }
            return this;
        }

        public Builder extend(Extender extender) {
            extender.extend(this);
            return this;
        }

        public synchronized Builder setFlag(int mask, boolean value) {
            if (value) {
                this.mN.flags |= mask;
            } else {
                this.mN.flags &= ~mask;
            }
            return this;
        }

        public Builder setColor(int argb) {
            this.mN.color = argb;
            sanitizeColor();
            return this;
        }

        private synchronized Drawable getProfileBadgeDrawable() {
            if (this.mContext.getUserId() == 0) {
                return null;
            }
            return this.mContext.getPackageManager().getUserBadgeForDensityNoBackground(new UserHandle(this.mContext.getUserId()), 0);
        }

        private synchronized Bitmap getProfileBadge() {
            Drawable badge = getProfileBadgeDrawable();
            if (badge == null) {
                return null;
            }
            int size = this.mContext.getResources().getDimensionPixelSize(R.dimen.notification_badge_size);
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            badge.setBounds(0, 0, size, size);
            badge.draw(canvas);
            return bitmap;
        }

        private synchronized void bindProfileBadge(RemoteViews contentView) {
            Bitmap profileBadge = getProfileBadge();
            if (profileBadge != null) {
                contentView.setImageViewBitmap(R.id.profile_badge, profileBadge);
                contentView.setViewVisibility(R.id.profile_badge, 0);
                if (isColorized()) {
                    contentView.setDrawableTint(R.id.profile_badge, false, getPrimaryTextColor(), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }

        public synchronized boolean usesStandardHeader() {
            if (this.mN.mUsesStandardHeader) {
                return true;
            }
            if (this.mContext.getApplicationInfo().targetSdkVersion >= 24 && this.mN.contentView == null && this.mN.bigContentView == null) {
                return true;
            }
            boolean contentViewUsesHeader = this.mN.contentView == null || Notification.STANDARD_LAYOUTS.contains(Integer.valueOf(this.mN.contentView.getLayoutId()));
            boolean bigContentViewUsesHeader = this.mN.bigContentView == null || Notification.STANDARD_LAYOUTS.contains(Integer.valueOf(this.mN.bigContentView.getLayoutId()));
            return contentViewUsesHeader && bigContentViewUsesHeader;
        }

        private synchronized void resetStandardTemplate(RemoteViews contentView) {
            resetNotificationHeader(contentView);
            contentView.setViewVisibility(16909334, 8);
            contentView.setViewVisibility(android.R.id.title, 8);
            contentView.setTextViewText(android.R.id.title, null);
            contentView.setViewVisibility(16909469, 8);
            contentView.setTextViewText(16909469, null);
            contentView.setViewVisibility(R.id.text_line_1, 8);
            contentView.setTextViewText(R.id.text_line_1, null);
        }

        private synchronized void resetNotificationHeader(RemoteViews contentView) {
            contentView.setBoolean(16909212, "setExpanded", false);
            contentView.setTextViewText(R.id.app_name_text, null);
            contentView.setViewVisibility(R.id.chronometer, 8);
            contentView.setViewVisibility(R.id.header_text, 8);
            contentView.setTextViewText(R.id.header_text, null);
            contentView.setViewVisibility(R.id.header_text_secondary, 8);
            contentView.setTextViewText(R.id.header_text_secondary, null);
            contentView.setViewVisibility(R.id.header_text_divider, 8);
            contentView.setViewVisibility(R.id.header_text_secondary_divider, 8);
            contentView.setViewVisibility(R.id.time_divider, 8);
            contentView.setViewVisibility(16909500, 8);
            contentView.setImageViewIcon(R.id.profile_badge, null);
            contentView.setViewVisibility(R.id.profile_badge, 8);
            this.mN.mUsesStandardHeader = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized RemoteViews applyStandardTemplate(int resId, TemplateBindResult result) {
            return applyStandardTemplate(resId, this.mParams.reset().fillTextsFrom(this), result);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized RemoteViews applyStandardTemplate(int resId, boolean hasProgress, TemplateBindResult result) {
            return applyStandardTemplate(resId, this.mParams.reset().hasProgress(hasProgress).fillTextsFrom(this), result);
        }

        private synchronized RemoteViews applyStandardTemplate(int resId, StandardTemplateParams p, TemplateBindResult result) {
            int i;
            RemoteViews contentView = new BuilderRemoteViews(this.mContext.getApplicationInfo(), resId);
            resetStandardTemplate(contentView);
            Bundle ex = this.mN.extras;
            updateBackgroundColor(contentView);
            bindNotificationHeader(contentView, p.ambient, p.headerTextSecondary);
            bindLargeIconAndReply(contentView, p, result);
            boolean showProgress = handleProgressBar(p.hasProgress, contentView, ex);
            boolean z = false;
            if (p.title != null) {
                contentView.setViewVisibility(android.R.id.title, 0);
                contentView.setTextViewText(android.R.id.title, processTextSpans(p.title));
                if (!p.ambient) {
                    setTextViewColorPrimary(contentView, android.R.id.title);
                }
                if (showProgress) {
                    i = -2;
                } else {
                    i = -1;
                }
                contentView.setViewLayoutWidth(android.R.id.title, i);
            }
            if (p.text != null) {
                int textId = showProgress ? R.id.text_line_1 : 16909469;
                contentView.setTextViewText(textId, processTextSpans(p.text));
                if (!p.ambient) {
                    setTextViewColorSecondary(contentView, textId);
                }
                contentView.setViewVisibility(textId, 0);
            }
            setContentMinHeight(contentView, (showProgress || this.mN.hasLargeIcon()) ? true : true);
            return contentView;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized CharSequence processTextSpans(CharSequence text) {
            if (hasForegroundColor()) {
                return NotificationColorUtil.clearColorSpans(text);
            }
            return text;
        }

        private synchronized void setTextViewColorPrimary(RemoteViews contentView, int id) {
            ensureColors();
            contentView.setTextColor(id, this.mPrimaryTextColor);
        }

        private synchronized boolean hasForegroundColor() {
            return this.mForegroundColor != 1;
        }

        @VisibleForTesting
        public synchronized int getPrimaryTextColor() {
            ensureColors();
            return this.mPrimaryTextColor;
        }

        @VisibleForTesting
        public synchronized int getSecondaryTextColor() {
            ensureColors();
            return this.mSecondaryTextColor;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void setTextViewColorSecondary(RemoteViews contentView, int id) {
            ensureColors();
            contentView.setTextColor(id, this.mSecondaryTextColor);
        }

        private synchronized void ensureColors() {
            int backgroundColor = getBackgroundColor();
            if (this.mPrimaryTextColor == 1 || this.mSecondaryTextColor == 1 || this.mTextColorsAreForBackground != backgroundColor) {
                this.mTextColorsAreForBackground = backgroundColor;
                if (!hasForegroundColor() || !isColorized()) {
                    this.mPrimaryTextColor = NotificationColorUtil.resolvePrimaryColor(this.mContext, backgroundColor);
                    this.mSecondaryTextColor = NotificationColorUtil.resolveSecondaryColor(this.mContext, backgroundColor);
                    if (backgroundColor != 0 && isColorized()) {
                        this.mPrimaryTextColor = NotificationColorUtil.findAlphaToMeetContrast(this.mPrimaryTextColor, backgroundColor, 4.5d);
                        this.mSecondaryTextColor = NotificationColorUtil.findAlphaToMeetContrast(this.mSecondaryTextColor, backgroundColor, 4.5d);
                        return;
                    }
                    return;
                }
                double backLum = NotificationColorUtil.calculateLuminance(backgroundColor);
                double textLum = NotificationColorUtil.calculateLuminance(this.mForegroundColor);
                double contrast = NotificationColorUtil.calculateContrast(this.mForegroundColor, backgroundColor);
                boolean backgroundLight = (backLum > textLum && NotificationColorUtil.satisfiesTextContrast(backgroundColor, -16777216)) || (backLum <= textLum && !NotificationColorUtil.satisfiesTextContrast(backgroundColor, -1));
                if (contrast >= 4.5d) {
                    this.mPrimaryTextColor = this.mForegroundColor;
                    this.mSecondaryTextColor = NotificationColorUtil.changeColorLightness(this.mPrimaryTextColor, backgroundLight ? 20 : -10);
                    if (NotificationColorUtil.calculateContrast(this.mSecondaryTextColor, backgroundColor) < 4.5d) {
                        if (backgroundLight) {
                            this.mSecondaryTextColor = NotificationColorUtil.findContrastColor(this.mSecondaryTextColor, backgroundColor, true, 4.5d);
                        } else {
                            this.mSecondaryTextColor = NotificationColorUtil.findContrastColorAgainstDark(this.mSecondaryTextColor, backgroundColor, true, 4.5d);
                        }
                        this.mPrimaryTextColor = NotificationColorUtil.changeColorLightness(this.mSecondaryTextColor, backgroundLight ? -20 : 10);
                    }
                } else if (backgroundLight) {
                    this.mSecondaryTextColor = NotificationColorUtil.findContrastColor(this.mForegroundColor, backgroundColor, true, 4.5d);
                    this.mPrimaryTextColor = NotificationColorUtil.changeColorLightness(this.mSecondaryTextColor, -20);
                } else {
                    this.mSecondaryTextColor = NotificationColorUtil.findContrastColorAgainstDark(this.mForegroundColor, backgroundColor, true, 4.5d);
                    this.mPrimaryTextColor = NotificationColorUtil.changeColorLightness(this.mSecondaryTextColor, 10);
                }
            }
        }

        private synchronized void updateBackgroundColor(RemoteViews contentView) {
            if (isColorized()) {
                contentView.setInt(16909445, "setBackgroundColor", getBackgroundColor());
            } else {
                contentView.setInt(16909445, "setBackgroundResource", 0);
            }
        }

        synchronized void setContentMinHeight(RemoteViews remoteView, boolean hasMinHeight) {
            int minHeight = 0;
            if (hasMinHeight) {
                minHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.notification_min_content_height);
            }
            remoteView.setInt(R.id.notification_main_column, "setMinimumHeight", minHeight);
        }

        private synchronized boolean handleProgressBar(boolean hasProgress, RemoteViews contentView, Bundle ex) {
            int max = ex.getInt(Notification.EXTRA_PROGRESS_MAX, 0);
            int progress = ex.getInt(Notification.EXTRA_PROGRESS, 0);
            boolean ind = ex.getBoolean(Notification.EXTRA_PROGRESS_INDETERMINATE);
            if (!hasProgress || (max == 0 && !ind)) {
                contentView.setViewVisibility(android.R.id.progress, 8);
                return false;
            }
            contentView.setViewVisibility(android.R.id.progress, 0);
            contentView.setProgressBar(android.R.id.progress, max, progress, ind);
            contentView.setProgressBackgroundTintList(android.R.id.progress, ColorStateList.valueOf(this.mContext.getColor(R.color.notification_progress_background_color)));
            if (this.mN.color != 0) {
                ColorStateList colorStateList = ColorStateList.valueOf(resolveContrastColor());
                contentView.setProgressTintList(android.R.id.progress, colorStateList);
                contentView.setProgressIndeterminateTintList(android.R.id.progress, colorStateList);
                return true;
            }
            return true;
        }

        private synchronized void bindLargeIconAndReply(RemoteViews contentView, StandardTemplateParams p, TemplateBindResult result) {
            boolean z = true;
            int i = 0;
            boolean largeIconShown = bindLargeIcon(contentView, p.hideLargeIcon || p.ambient);
            if (!p.hideReplyIcon && !p.ambient) {
                z = false;
            }
            boolean replyIconShown = bindReplyIcon(contentView, z);
            if (!largeIconShown && !replyIconShown) {
                i = 8;
            }
            contentView.setViewVisibility(R.id.right_icon_container, i);
            int marginEnd = calculateMarginEnd(largeIconShown, replyIconShown);
            contentView.setViewLayoutMarginEnd(16909122, marginEnd);
            contentView.setViewLayoutMarginEnd(16909469, marginEnd);
            contentView.setViewLayoutMarginEnd(android.R.id.progress, marginEnd);
            if (result != null) {
                result.setIconMarginEnd(marginEnd);
            }
        }

        private synchronized int calculateMarginEnd(boolean largeIconShown, boolean replyIconShown) {
            int marginEnd = 0;
            int contentMargin = this.mContext.getResources().getDimensionPixelSize(R.dimen.notification_content_margin_end);
            int iconSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.notification_right_icon_size);
            if (replyIconShown) {
                int marginEnd2 = 0 + iconSize;
                int replyInset = this.mContext.getResources().getDimensionPixelSize(R.dimen.notification_reply_inset);
                marginEnd = marginEnd2 - (replyInset * 2);
            }
            if (largeIconShown) {
                marginEnd += iconSize;
                if (replyIconShown) {
                    marginEnd += contentMargin;
                }
            }
            if (replyIconShown || largeIconShown) {
                return marginEnd + contentMargin;
            }
            return marginEnd;
        }

        private synchronized boolean bindLargeIcon(RemoteViews contentView, boolean hideLargeIcon) {
            if (this.mN.mLargeIcon == null && this.mN.largeIcon != null) {
                this.mN.mLargeIcon = Icon.createWithBitmap(this.mN.largeIcon);
            }
            boolean showLargeIcon = (this.mN.mLargeIcon == null || hideLargeIcon) ? false : true;
            if (showLargeIcon) {
                contentView.setViewVisibility(16909334, 0);
                contentView.setImageViewIcon(16909334, this.mN.mLargeIcon);
                processLargeLegacyIcon(this.mN.mLargeIcon, contentView);
            }
            return showLargeIcon;
        }

        private synchronized boolean bindReplyIcon(RemoteViews contentView, boolean hideReplyIcon) {
            boolean actionVisible = !hideReplyIcon;
            Action action = null;
            if (actionVisible) {
                action = findReplyAction();
                actionVisible = action != null;
            }
            if (!actionVisible) {
                contentView.setRemoteInputs(R.id.reply_icon_action, null);
            } else {
                contentView.setViewVisibility(R.id.reply_icon_action, 0);
                contentView.setDrawableTint(R.id.reply_icon_action, false, getNeutralColor(), PorterDuff.Mode.SRC_ATOP);
                contentView.setOnClickPendingIntent(R.id.reply_icon_action, action.actionIntent);
                contentView.setRemoteInputs(R.id.reply_icon_action, action.mRemoteInputs);
            }
            contentView.setViewVisibility(R.id.reply_icon_action, actionVisible ? 0 : 8);
            return actionVisible;
        }

        private synchronized Action findReplyAction() {
            ArrayList<Action> actions = this.mActions;
            if (this.mOriginalActions != null) {
                actions = this.mOriginalActions;
            }
            int numActions = actions.size();
            for (int i = 0; i < numActions; i++) {
                Action action = actions.get(i);
                if (hasValidRemoteInput(action)) {
                    return action;
                }
            }
            return null;
        }

        private synchronized void bindNotificationHeader(RemoteViews contentView, boolean ambient, CharSequence secondaryHeaderText) {
            bindSmallIcon(contentView, ambient);
            bindHeaderAppName(contentView, ambient);
            if (!ambient) {
                bindHeaderText(contentView);
                bindHeaderTextSecondary(contentView, secondaryHeaderText);
                bindHeaderChronometerAndTime(contentView);
                bindProfileBadge(contentView);
            }
            bindActivePermissions(contentView, ambient);
            bindExpandButton(contentView);
            this.mN.mUsesStandardHeader = true;
        }

        private synchronized void bindActivePermissions(RemoteViews contentView, boolean ambient) {
            int color = ambient ? resolveAmbientColor() : getNeutralColor();
            contentView.setDrawableTint(16908872, false, color, PorterDuff.Mode.SRC_ATOP);
            contentView.setDrawableTint(R.id.mic, false, color, PorterDuff.Mode.SRC_ATOP);
            contentView.setDrawableTint(16909248, false, color, PorterDuff.Mode.SRC_ATOP);
        }

        private synchronized void bindExpandButton(RemoteViews contentView) {
            int color = isColorized() ? getPrimaryTextColor() : getSecondaryTextColor();
            contentView.setDrawableTint(R.id.expand_button, false, color, PorterDuff.Mode.SRC_ATOP);
            contentView.setInt(16909212, "setOriginalNotificationColor", color);
        }

        private synchronized void bindHeaderChronometerAndTime(RemoteViews contentView) {
            if (showsTimeOrChronometer()) {
                contentView.setViewVisibility(R.id.time_divider, 0);
                setTextViewColorSecondary(contentView, R.id.time_divider);
                if (this.mN.extras.getBoolean(Notification.EXTRA_SHOW_CHRONOMETER)) {
                    contentView.setViewVisibility(R.id.chronometer, 0);
                    contentView.setLong(R.id.chronometer, "setBase", this.mN.when + (SystemClock.elapsedRealtime() - System.currentTimeMillis()));
                    contentView.setBoolean(R.id.chronometer, "setStarted", true);
                    boolean countsDown = this.mN.extras.getBoolean(Notification.EXTRA_CHRONOMETER_COUNT_DOWN);
                    contentView.setChronometerCountDown(R.id.chronometer, countsDown);
                    setTextViewColorSecondary(contentView, R.id.chronometer);
                    return;
                }
                contentView.setViewVisibility(16909500, 0);
                contentView.setLong(16909500, "setTime", this.mN.when);
                setTextViewColorSecondary(contentView, 16909500);
                return;
            }
            contentView.setLong(16909500, "setTime", this.mN.when != 0 ? this.mN.when : this.mN.creationTime);
        }

        private synchronized void bindHeaderText(RemoteViews contentView) {
            CharSequence headerText = this.mN.extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
            if (headerText == null && this.mStyle != null && this.mStyle.mSummaryTextSet && this.mStyle.hasSummaryInHeader()) {
                headerText = this.mStyle.mSummaryText;
            }
            if (headerText == null && this.mContext.getApplicationInfo().targetSdkVersion < 24 && this.mN.extras.getCharSequence(Notification.EXTRA_INFO_TEXT) != null) {
                headerText = this.mN.extras.getCharSequence(Notification.EXTRA_INFO_TEXT);
            }
            if (headerText != null) {
                contentView.setTextViewText(R.id.header_text, processTextSpans(processLegacyText(headerText)));
                setTextViewColorSecondary(contentView, R.id.header_text);
                contentView.setViewVisibility(R.id.header_text, 0);
                contentView.setViewVisibility(R.id.header_text_divider, 0);
                setTextViewColorSecondary(contentView, R.id.header_text_divider);
            }
        }

        private synchronized void bindHeaderTextSecondary(RemoteViews contentView, CharSequence secondaryText) {
            if (!TextUtils.isEmpty(secondaryText)) {
                contentView.setTextViewText(R.id.header_text_secondary, processTextSpans(processLegacyText(secondaryText)));
                setTextViewColorSecondary(contentView, R.id.header_text_secondary);
                contentView.setViewVisibility(R.id.header_text_secondary, 0);
                contentView.setViewVisibility(R.id.header_text_secondary_divider, 0);
                setTextViewColorSecondary(contentView, R.id.header_text_secondary_divider);
            }
        }

        private protected String loadHeaderAppName() {
            CharSequence name = null;
            PackageManager pm = this.mContext.getPackageManager();
            if (this.mN.extras.containsKey(Notification.EXTRA_SUBSTITUTE_APP_NAME)) {
                String pkg = this.mContext.getPackageName();
                String subName = this.mN.extras.getString(Notification.EXTRA_SUBSTITUTE_APP_NAME);
                if (pm.checkPermission(Manifest.permission.SUBSTITUTE_NOTIFICATION_APP_NAME, pkg) == 0) {
                    name = subName;
                } else {
                    Log.w(Notification.TAG, "warning: pkg " + pkg + " attempting to substitute app name '" + subName + "' without holding perm " + Manifest.permission.SUBSTITUTE_NOTIFICATION_APP_NAME);
                }
            }
            if (TextUtils.isEmpty(name)) {
                name = pm.getApplicationLabel(this.mContext.getApplicationInfo());
            }
            if (TextUtils.isEmpty(name)) {
                return null;
            }
            return String.valueOf(name);
        }

        private synchronized void bindHeaderAppName(RemoteViews contentView, boolean ambient) {
            contentView.setTextViewText(R.id.app_name_text, loadHeaderAppName());
            if (isColorized() && !ambient) {
                setTextViewColorPrimary(contentView, R.id.app_name_text);
            } else {
                contentView.setTextColor(R.id.app_name_text, ambient ? resolveAmbientColor() : getSecondaryTextColor());
            }
        }

        private synchronized void bindSmallIcon(RemoteViews contentView, boolean ambient) {
            if (this.mN.mSmallIcon == null && this.mN.icon != 0) {
                this.mN.mSmallIcon = Icon.createWithResource(this.mContext, this.mN.icon);
            }
            contentView.setImageViewIcon(android.R.id.icon, this.mN.mSmallIcon);
            contentView.setInt(android.R.id.icon, "setImageLevel", this.mN.iconLevel);
            processSmallIconColor(this.mN.mSmallIcon, contentView, ambient);
        }

        private synchronized boolean showsTimeOrChronometer() {
            return this.mN.showsTime() || this.mN.showsChronometer();
        }

        private synchronized void resetStandardTemplateWithActions(RemoteViews big) {
            big.setViewVisibility(R.id.actions, 8);
            big.removeAllViews(R.id.actions);
            big.setViewVisibility(R.id.notification_material_reply_container, 8);
            big.setTextViewText(R.id.notification_material_reply_text_1, null);
            big.setViewVisibility(R.id.notification_material_reply_text_1_container, 8);
            big.setViewVisibility(R.id.notification_material_reply_progress, 8);
            big.setViewVisibility(R.id.notification_material_reply_text_2, 8);
            big.setTextViewText(R.id.notification_material_reply_text_2, null);
            big.setViewVisibility(R.id.notification_material_reply_text_3, 8);
            big.setTextViewText(R.id.notification_material_reply_text_3, null);
            big.setViewLayoutMarginBottomDimen(R.id.notification_action_list_margin_target, R.dimen.notification_content_margin);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized RemoteViews applyStandardTemplateWithActions(int layoutId, TemplateBindResult result) {
            return applyStandardTemplateWithActions(layoutId, this.mParams.reset().fillTextsFrom(this), result);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized RemoteViews applyStandardTemplateWithActions(int layoutId, StandardTemplateParams p, TemplateBindResult result) {
            boolean validRemoteInput;
            RemoteViews big = applyStandardTemplate(layoutId, p, result);
            resetStandardTemplateWithActions(big);
            int N = this.mActions.size();
            boolean emphazisedMode = (this.mN.fullScreenIntent == null || p.ambient) ? false : true;
            big.setBoolean(R.id.actions, "setEmphasizedMode", emphazisedMode);
            int i = 8;
            if (N > 0) {
                big.setViewVisibility(R.id.actions_container, 0);
                big.setViewVisibility(R.id.actions, 0);
                big.setViewLayoutMarginBottomDimen(R.id.notification_action_list_margin_target, 0);
                if (N > 3) {
                    N = 3;
                }
                validRemoteInput = false;
                for (int i2 = 0; i2 < N; i2++) {
                    Action action = this.mActions.get(i2);
                    boolean actionHasValidInput = hasValidRemoteInput(action);
                    validRemoteInput |= actionHasValidInput;
                    RemoteViews button = generateActionButton(action, emphazisedMode, p.ambient);
                    if (actionHasValidInput && !emphazisedMode) {
                        button.setInt(R.id.action0, "setBackgroundResource", 0);
                    }
                    big.addView(R.id.actions, button);
                }
            } else {
                big.setViewVisibility(R.id.actions_container, 8);
                validRemoteInput = false;
            }
            CharSequence[] replyText = this.mN.extras.getCharSequenceArray(Notification.EXTRA_REMOTE_INPUT_HISTORY);
            if (!p.ambient && validRemoteInput && replyText != null && replyText.length > 0 && !TextUtils.isEmpty(replyText[0]) && p.maxRemoteInputHistory > 0) {
                boolean showSpinner = this.mN.extras.getBoolean(Notification.EXTRA_SHOW_REMOTE_INPUT_SPINNER);
                big.setViewVisibility(R.id.notification_material_reply_container, 0);
                big.setViewVisibility(R.id.notification_material_reply_text_1_container, 0);
                big.setTextViewText(R.id.notification_material_reply_text_1, processTextSpans(replyText[0]));
                setTextViewColorSecondary(big, R.id.notification_material_reply_text_1);
                if (showSpinner) {
                    i = 0;
                }
                big.setViewVisibility(R.id.notification_material_reply_progress, i);
                big.setProgressIndeterminateTintList(R.id.notification_material_reply_progress, ColorStateList.valueOf(isColorized() ? getPrimaryTextColor() : resolveContrastColor()));
                if (replyText.length > 1 && !TextUtils.isEmpty(replyText[1]) && p.maxRemoteInputHistory > 1) {
                    big.setViewVisibility(R.id.notification_material_reply_text_2, 0);
                    big.setTextViewText(R.id.notification_material_reply_text_2, processTextSpans(replyText[1]));
                    setTextViewColorSecondary(big, R.id.notification_material_reply_text_2);
                    if (replyText.length > 2 && !TextUtils.isEmpty(replyText[2]) && p.maxRemoteInputHistory > 2) {
                        big.setViewVisibility(R.id.notification_material_reply_text_3, 0);
                        big.setTextViewText(R.id.notification_material_reply_text_3, processTextSpans(replyText[2]));
                        setTextViewColorSecondary(big, R.id.notification_material_reply_text_3);
                    }
                }
            }
            return big;
        }

        private synchronized boolean hasValidRemoteInput(Action action) {
            RemoteInput[] remoteInputs;
            if (TextUtils.isEmpty(action.title) || action.actionIntent == null || (remoteInputs = action.getRemoteInputs()) == null) {
                return false;
            }
            for (RemoteInput r : remoteInputs) {
                CharSequence[] choices = r.getChoices();
                if (r.getAllowFreeFormInput()) {
                    return true;
                }
                if (choices != null && choices.length != 0) {
                    return true;
                }
            }
            return false;
        }

        public RemoteViews createContentView() {
            return createContentView(false);
        }

        public synchronized RemoteViews createContentView(boolean increasedHeight) {
            RemoteViews styleView;
            if (this.mN.contentView != null && useExistingRemoteView()) {
                return this.mN.contentView;
            }
            if (this.mStyle != null && (styleView = this.mStyle.makeContentView(increasedHeight)) != null) {
                return styleView;
            }
            return applyStandardTemplate(getBaseLayoutResource(), null);
        }

        private synchronized boolean useExistingRemoteView() {
            return this.mStyle == null || !(this.mStyle.displayCustomViewInline() || this.mRebuildStyledRemoteViews);
        }

        public RemoteViews createBigContentView() {
            RemoteViews result = null;
            if (this.mN.bigContentView != null && useExistingRemoteView()) {
                return this.mN.bigContentView;
            }
            if (this.mStyle != null) {
                result = this.mStyle.makeBigContentView();
                hideLine1Text(result);
            } else if (this.mActions.size() != 0) {
                result = applyStandardTemplateWithActions(getBigBaseLayoutResource(), null);
            }
            makeHeaderExpanded(result);
            return result;
        }

        public synchronized RemoteViews makeNotificationHeader(boolean ambient) {
            Boolean colorized = (Boolean) this.mN.extras.get(Notification.EXTRA_COLORIZED);
            this.mN.extras.putBoolean(Notification.EXTRA_COLORIZED, false);
            RemoteViews header = new BuilderRemoteViews(this.mContext.getApplicationInfo(), ambient ? R.layout.notification_template_ambient_header : R.layout.notification_template_header);
            resetNotificationHeader(header);
            bindNotificationHeader(header, ambient, null);
            if (colorized != null) {
                this.mN.extras.putBoolean(Notification.EXTRA_COLORIZED, colorized.booleanValue());
            } else {
                this.mN.extras.remove(Notification.EXTRA_COLORIZED);
            }
            return header;
        }

        public synchronized RemoteViews makeAmbientNotification() {
            RemoteViews ambient = applyStandardTemplateWithActions(R.layout.notification_template_material_ambient, this.mParams.reset().ambient(true).fillTextsFrom(this).hasProgress(false), null);
            return ambient;
        }

        private synchronized void hideLine1Text(RemoteViews result) {
            if (result != null) {
                result.setViewVisibility(R.id.text_line_1, 8);
            }
        }

        public static synchronized void makeHeaderExpanded(RemoteViews result) {
            if (result != null) {
                result.setBoolean(16909212, "setExpanded", true);
            }
        }

        public synchronized RemoteViews createHeadsUpContentView(boolean increasedHeight) {
            if (this.mN.headsUpContentView != null && useExistingRemoteView()) {
                return this.mN.headsUpContentView;
            }
            if (this.mStyle != null) {
                RemoteViews styleView = this.mStyle.makeHeadsUpContentView(increasedHeight);
                if (styleView != null) {
                    return styleView;
                }
            } else if (this.mActions.size() == 0) {
                return null;
            }
            StandardTemplateParams p = this.mParams.reset().fillTextsFrom(this).setMaxRemoteInputHistory(1);
            return applyStandardTemplateWithActions(getBigBaseLayoutResource(), p, null);
        }

        public RemoteViews createHeadsUpContentView() {
            return createHeadsUpContentView(false);
        }

        private protected RemoteViews makePublicContentView() {
            return makePublicView(false);
        }

        public synchronized RemoteViews makePublicAmbientNotification() {
            return makePublicView(true);
        }

        private synchronized RemoteViews makePublicView(boolean ambient) {
            RemoteViews view;
            if (this.mN.publicVersion != null) {
                Builder builder = recoverBuilder(this.mContext, this.mN.publicVersion);
                return ambient ? builder.makeAmbientNotification() : builder.createContentView();
            }
            Bundle savedBundle = this.mN.extras;
            Style style = this.mStyle;
            this.mStyle = null;
            Icon largeIcon = this.mN.mLargeIcon;
            this.mN.mLargeIcon = null;
            Bitmap largeIconLegacy = this.mN.largeIcon;
            this.mN.largeIcon = null;
            ArrayList<Action> actions = this.mActions;
            this.mActions = new ArrayList<>();
            Bundle publicExtras = new Bundle();
            publicExtras.putBoolean(Notification.EXTRA_SHOW_WHEN, savedBundle.getBoolean(Notification.EXTRA_SHOW_WHEN));
            publicExtras.putBoolean(Notification.EXTRA_SHOW_CHRONOMETER, savedBundle.getBoolean(Notification.EXTRA_SHOW_CHRONOMETER));
            publicExtras.putBoolean(Notification.EXTRA_CHRONOMETER_COUNT_DOWN, savedBundle.getBoolean(Notification.EXTRA_CHRONOMETER_COUNT_DOWN));
            String appName = savedBundle.getString(Notification.EXTRA_SUBSTITUTE_APP_NAME);
            if (appName != null) {
                publicExtras.putString(Notification.EXTRA_SUBSTITUTE_APP_NAME, appName);
            }
            this.mN.extras = publicExtras;
            if (ambient) {
                publicExtras.putCharSequence(Notification.EXTRA_TITLE, this.mContext.getString(R.string.notification_hidden_text));
                view = makeAmbientNotification();
            } else {
                view = makeNotificationHeader(false);
                view.setBoolean(16909212, "setExpandOnlyOnButton", true);
            }
            this.mN.extras = savedBundle;
            this.mN.mLargeIcon = largeIcon;
            this.mN.largeIcon = largeIconLegacy;
            this.mActions = actions;
            this.mStyle = style;
            return view;
        }

        public synchronized RemoteViews makeLowPriorityContentView(boolean useRegularSubtext) {
            int color = this.mN.color;
            this.mN.color = 0;
            CharSequence summary = this.mN.extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
            if (!useRegularSubtext || TextUtils.isEmpty(summary)) {
                CharSequence newSummary = createSummaryText();
                if (!TextUtils.isEmpty(newSummary)) {
                    this.mN.extras.putCharSequence(Notification.EXTRA_SUB_TEXT, newSummary);
                }
            }
            RemoteViews header = makeNotificationHeader(false);
            header.setBoolean(16909212, "setAcceptAllTouches", true);
            if (summary != null) {
                this.mN.extras.putCharSequence(Notification.EXTRA_SUB_TEXT, summary);
            } else {
                this.mN.extras.remove(Notification.EXTRA_SUB_TEXT);
            }
            this.mN.color = color;
            return header;
        }

        private synchronized CharSequence createSummaryText() {
            CharSequence titleText = this.mN.extras.getCharSequence(Notification.EXTRA_TITLE);
            if (USE_ONLY_TITLE_IN_LOW_PRIORITY_SUMMARY) {
                return titleText;
            }
            SpannableStringBuilder summary = new SpannableStringBuilder();
            if (titleText == null) {
                titleText = this.mN.extras.getCharSequence(Notification.EXTRA_TITLE_BIG);
            }
            BidiFormatter bidi = BidiFormatter.getInstance();
            if (titleText != null) {
                summary.append(bidi.unicodeWrap(titleText));
            }
            CharSequence contentText = this.mN.extras.getCharSequence(Notification.EXTRA_TEXT);
            if (titleText != null && contentText != null) {
                summary.append(bidi.unicodeWrap(this.mContext.getText(R.string.notification_header_divider_symbol_with_spaces)));
            }
            if (contentText != null) {
                summary.append(bidi.unicodeWrap(contentText));
            }
            return summary;
        }

        private synchronized RemoteViews generateActionButton(Action action, boolean emphazisedMode, boolean ambient) {
            int actionTombstoneLayoutResource;
            CharSequence title;
            int textColor;
            boolean tombstone = action.actionIntent == null;
            ApplicationInfo applicationInfo = this.mContext.getApplicationInfo();
            if (emphazisedMode) {
                actionTombstoneLayoutResource = getEmphasizedActionLayoutResource();
            } else {
                actionTombstoneLayoutResource = tombstone ? getActionTombstoneLayoutResource() : getActionLayoutResource();
            }
            RemoteViews button = new BuilderRemoteViews(applicationInfo, actionTombstoneLayoutResource);
            if (!tombstone) {
                button.setOnClickPendingIntent(R.id.action0, action.actionIntent);
            }
            button.setContentDescription(R.id.action0, action.title);
            if (action.mRemoteInputs != null) {
                button.setRemoteInputs(R.id.action0, action.mRemoteInputs);
            }
            if (!emphazisedMode) {
                button.setTextViewText(R.id.action0, processTextSpans(processLegacyText(action.title)));
                if (isColorized() && !ambient) {
                    setTextViewColorPrimary(button, R.id.action0);
                } else if (this.mN.color != 0 && this.mTintActionButtons) {
                    button.setTextColor(R.id.action0, ambient ? resolveAmbientColor() : resolveContrastColor());
                }
            } else {
                CharSequence title2 = action.title;
                ColorStateList[] outResultColor = null;
                int background = resolveBackgroundColor();
                if (isLegacy()) {
                    title = NotificationColorUtil.clearColorSpans(title2);
                } else {
                    outResultColor = new ColorStateList[1];
                    title = ensureColorSpanContrast(title2, background, outResultColor);
                }
                button.setTextViewText(R.id.action0, processTextSpans(title));
                setTextViewColorPrimary(button, R.id.action0);
                boolean hasColorOverride = (outResultColor == null || outResultColor[0] == null) ? false : true;
                if (hasColorOverride) {
                    background = outResultColor[0].getDefaultColor();
                    textColor = NotificationColorUtil.resolvePrimaryColor(this.mContext, background);
                    button.setTextColor(R.id.action0, textColor);
                } else if (this.mN.color != 0 && !isColorized() && this.mTintActionButtons) {
                    textColor = resolveContrastColor();
                    button.setTextColor(R.id.action0, textColor);
                } else {
                    textColor = getPrimaryTextColor();
                }
                int rippleColor = (16777215 & textColor) | 855638016;
                button.setColorStateList(R.id.action0, "setRippleColor", ColorStateList.valueOf(rippleColor));
                button.setColorStateList(R.id.action0, "setButtonBackground", ColorStateList.valueOf(background));
                button.setBoolean(R.id.action0, "setHasStroke", hasColorOverride ? false : true);
            }
            return button;
        }

        private synchronized CharSequence ensureColorSpanContrast(CharSequence charSequence, int background, ColorStateList[] outResultColor) {
            Object[] spans;
            int i;
            int i2;
            boolean z;
            if (charSequence instanceof Spanned) {
                Spanned ss = (Spanned) charSequence;
                boolean z2 = false;
                Object[] spans2 = ss.getSpans(0, ss.length(), Object.class);
                SpannableStringBuilder builder = new SpannableStringBuilder(ss.toString());
                int length = spans2.length;
                int i3 = 0;
                while (i3 < length) {
                    Object span = spans2[i3];
                    Object resultSpan = span;
                    int spanStart = ss.getSpanStart(span);
                    int spanEnd = ss.getSpanEnd(span);
                    boolean fullLength = spanEnd - spanStart == charSequence.length() ? true : z2;
                    if (resultSpan instanceof CharacterStyle) {
                        resultSpan = ((CharacterStyle) span).getUnderlying();
                    }
                    if (resultSpan instanceof TextAppearanceSpan) {
                        TextAppearanceSpan originalSpan = (TextAppearanceSpan) resultSpan;
                        ColorStateList textColor = originalSpan.getTextColor();
                        if (textColor != null) {
                            spans = spans2;
                            int[] colors = textColor.getColors();
                            i = length;
                            int[] newColors = new int[colors.length];
                            int i4 = 0;
                            while (true) {
                                int i5 = i4;
                                i2 = i3;
                                if (i5 >= newColors.length) {
                                    break;
                                }
                                newColors[i5] = NotificationColorUtil.ensureLargeTextContrast(colors[i5], background, this.mInNightMode);
                                i4 = i5 + 1;
                                i3 = i2;
                                colors = colors;
                            }
                            ColorStateList textColor2 = new ColorStateList((int[][]) textColor.getStates().clone(), newColors);
                            if (fullLength) {
                                outResultColor[0] = textColor2;
                                textColor2 = null;
                            }
                            resultSpan = new TextAppearanceSpan(originalSpan.getFamily(), originalSpan.getTextStyle(), originalSpan.getTextSize(), textColor2, originalSpan.getLinkTextColor());
                        } else {
                            spans = spans2;
                            i = length;
                            i2 = i3;
                        }
                        z = false;
                    } else {
                        spans = spans2;
                        i = length;
                        i2 = i3;
                        if (resultSpan instanceof ForegroundColorSpan) {
                            int foregroundColor = NotificationColorUtil.ensureLargeTextContrast(((ForegroundColorSpan) resultSpan).getForegroundColor(), background, this.mInNightMode);
                            if (fullLength) {
                                z = false;
                                outResultColor[0] = ColorStateList.valueOf(foregroundColor);
                                resultSpan = null;
                            } else {
                                z = false;
                                resultSpan = new ForegroundColorSpan(foregroundColor);
                            }
                        } else {
                            z = false;
                            resultSpan = span;
                        }
                    }
                    if (resultSpan != null) {
                        builder.setSpan(resultSpan, spanStart, spanEnd, ss.getSpanFlags(span));
                    }
                    i3 = i2 + 1;
                    z2 = z;
                    spans2 = spans;
                    length = i;
                }
                return builder;
            }
            return charSequence;
        }

        private synchronized boolean isLegacy() {
            if (!this.mIsLegacyInitialized) {
                this.mIsLegacy = this.mContext.getApplicationInfo().targetSdkVersion < 21;
                this.mIsLegacyInitialized = true;
            }
            return this.mIsLegacy;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized CharSequence processLegacyText(CharSequence charSequence) {
            return processLegacyText(charSequence, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized CharSequence processLegacyText(CharSequence charSequence, boolean ambient) {
            boolean isAlreadyLightText = isLegacy() || textColorsNeedInversion();
            if (isAlreadyLightText != ambient) {
                return getColorUtil().invertCharSequenceColors(charSequence);
            }
            return charSequence;
        }

        private synchronized void processSmallIconColor(Icon smallIcon, RemoteViews contentView, boolean ambient) {
            int color;
            boolean colorable = !isLegacy() || getColorUtil().isGrayscaleIcon(this.mContext, smallIcon);
            if (ambient) {
                color = resolveAmbientColor();
            } else if (isColorized()) {
                color = getPrimaryTextColor();
            } else {
                color = resolveContrastColor();
            }
            if (colorable) {
                contentView.setDrawableTint(android.R.id.icon, false, color, PorterDuff.Mode.SRC_ATOP);
            }
            contentView.setInt(16909212, "setOriginalIconColor", colorable ? color : 1);
        }

        private synchronized void processLargeLegacyIcon(Icon largeIcon, RemoteViews contentView) {
            if (largeIcon != null && isLegacy() && getColorUtil().isGrayscaleIcon(this.mContext, largeIcon)) {
                contentView.setDrawableTint(android.R.id.icon, false, resolveContrastColor(), PorterDuff.Mode.SRC_ATOP);
            }
        }

        private synchronized void sanitizeColor() {
            if (this.mN.color != 0) {
                this.mN.color |= -16777216;
            }
        }

        synchronized int resolveContrastColor() {
            int color;
            if (this.mCachedContrastColorIsFor == this.mN.color && this.mCachedContrastColor != 1) {
                return this.mCachedContrastColor;
            }
            int background = this.mContext.getColor(R.color.notification_material_background_color);
            if (this.mN.color == 0) {
                ensureColors();
                color = NotificationColorUtil.resolveDefaultColor(this.mContext, background);
            } else {
                color = NotificationColorUtil.resolveContrastColor(this.mContext, this.mN.color, background, this.mInNightMode);
            }
            if (Color.alpha(color) < 255) {
                color = NotificationColorUtil.compositeColors(color, background);
            }
            this.mCachedContrastColorIsFor = this.mN.color;
            this.mCachedContrastColor = color;
            return color;
        }

        synchronized int resolveNeutralColor() {
            if (this.mNeutralColor != 1) {
                return this.mNeutralColor;
            }
            int background = this.mContext.getColor(R.color.notification_material_background_color);
            this.mNeutralColor = NotificationColorUtil.resolveDefaultColor(this.mContext, background);
            if (Color.alpha(this.mNeutralColor) < 255) {
                this.mNeutralColor = NotificationColorUtil.compositeColors(this.mNeutralColor, background);
            }
            return this.mNeutralColor;
        }

        synchronized int resolveAmbientColor() {
            if (this.mCachedAmbientColorIsFor == this.mN.color && this.mCachedAmbientColorIsFor != 1) {
                return this.mCachedAmbientColor;
            }
            int contrasted = NotificationColorUtil.resolveAmbientColor(this.mContext, this.mN.color);
            this.mCachedAmbientColorIsFor = this.mN.color;
            this.mCachedAmbientColor = contrasted;
            return contrasted;
        }

        public synchronized Notification buildUnstyled() {
            if (this.mActions.size() > 0) {
                this.mN.actions = new Action[this.mActions.size()];
                this.mActions.toArray(this.mN.actions);
            }
            if (!this.mPersonList.isEmpty()) {
                this.mN.extras.putParcelableArrayList(Notification.EXTRA_PEOPLE_LIST, this.mPersonList);
            }
            if (this.mN.bigContentView != null || this.mN.contentView != null || this.mN.headsUpContentView != null) {
                this.mN.extras.putBoolean(Notification.EXTRA_CONTAINS_CUSTOM_VIEW, true);
            }
            return this.mN;
        }

        public static Builder recoverBuilder(Context context, Notification n) {
            Context builderContext;
            ApplicationInfo applicationInfo = (ApplicationInfo) n.extras.getParcelable(Notification.EXTRA_BUILDER_APPLICATION_INFO);
            if (applicationInfo != null) {
                try {
                    builderContext = context.createApplicationContext(applicationInfo, 4);
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(Notification.TAG, "ApplicationInfo " + applicationInfo + " not found");
                    builderContext = context;
                }
            } else {
                builderContext = context;
            }
            return new Builder(builderContext, n);
        }

        @Deprecated
        public Notification getNotification() {
            return build();
        }

        public Notification build() {
            if (this.mUserExtras != null) {
                this.mN.extras = getAllExtras();
            }
            this.mN.creationTime = System.currentTimeMillis();
            Notification.addFieldsFromContext(this.mContext, this.mN);
            buildUnstyled();
            if (this.mStyle != null) {
                this.mStyle.reduceImageSizes(this.mContext);
                this.mStyle.purgeResources();
                this.mStyle.validate(this.mContext);
                this.mStyle.buildStyled(this.mN);
            }
            this.mN.reduceImageSizes(this.mContext);
            if (this.mContext.getApplicationInfo().targetSdkVersion < 24 && useExistingRemoteView()) {
                if (this.mN.contentView == null) {
                    this.mN.contentView = createContentView();
                    this.mN.extras.putInt(EXTRA_REBUILD_CONTENT_VIEW_ACTION_COUNT, this.mN.contentView.getSequenceNumber());
                }
                if (this.mN.bigContentView == null) {
                    this.mN.bigContentView = createBigContentView();
                    if (this.mN.bigContentView != null) {
                        this.mN.extras.putInt(EXTRA_REBUILD_BIG_CONTENT_VIEW_ACTION_COUNT, this.mN.bigContentView.getSequenceNumber());
                    }
                }
                if (this.mN.headsUpContentView == null) {
                    this.mN.headsUpContentView = createHeadsUpContentView();
                    if (this.mN.headsUpContentView != null) {
                        this.mN.extras.putInt(EXTRA_REBUILD_HEADS_UP_CONTENT_VIEW_ACTION_COUNT, this.mN.headsUpContentView.getSequenceNumber());
                    }
                }
            }
            if ((this.mN.defaults & 4) != 0) {
                this.mN.flags |= 1;
            }
            this.mN.allPendingIntents = null;
            return this.mN;
        }

        public synchronized Notification buildInto(Notification n) {
            build().cloneInto(n, true);
            return n;
        }

        public static synchronized Notification maybeCloneStrippedForDelivery(Notification n, boolean isLowRam, Context context) {
            String templateClass = n.extras.getString(Notification.EXTRA_TEMPLATE);
            if (!isLowRam && !TextUtils.isEmpty(templateClass) && Notification.getNotificationStyleClass(templateClass) == null) {
                return n;
            }
            boolean stripHeadsUpContentView = false;
            boolean stripContentView = (n.contentView instanceof BuilderRemoteViews) && n.extras.getInt(EXTRA_REBUILD_CONTENT_VIEW_ACTION_COUNT, -1) == n.contentView.getSequenceNumber();
            boolean stripBigContentView = (n.bigContentView instanceof BuilderRemoteViews) && n.extras.getInt(EXTRA_REBUILD_BIG_CONTENT_VIEW_ACTION_COUNT, -1) == n.bigContentView.getSequenceNumber();
            if ((n.headsUpContentView instanceof BuilderRemoteViews) && n.extras.getInt(EXTRA_REBUILD_HEADS_UP_CONTENT_VIEW_ACTION_COUNT, -1) == n.headsUpContentView.getSequenceNumber()) {
                stripHeadsUpContentView = true;
            }
            if (!isLowRam && !stripContentView && !stripBigContentView && !stripHeadsUpContentView) {
                return n;
            }
            Notification clone = n.m8clone();
            if (stripContentView) {
                clone.contentView = null;
                clone.extras.remove(EXTRA_REBUILD_CONTENT_VIEW_ACTION_COUNT);
            }
            if (stripBigContentView) {
                clone.bigContentView = null;
                clone.extras.remove(EXTRA_REBUILD_BIG_CONTENT_VIEW_ACTION_COUNT);
            }
            if (stripHeadsUpContentView) {
                clone.headsUpContentView = null;
                clone.extras.remove(EXTRA_REBUILD_HEADS_UP_CONTENT_VIEW_ACTION_COUNT);
            }
            if (isLowRam) {
                String[] allowedServices = context.getResources().getStringArray(R.array.config_allowedManagedServicesOnLowRamDevices);
                if (allowedServices.length == 0) {
                    clone.extras.remove("android.tv.EXTENSIONS");
                    clone.extras.remove("android.wearable.EXTENSIONS");
                    clone.extras.remove("android.car.EXTENSIONS");
                }
            }
            return clone;
        }

        /* JADX INFO: Access modifiers changed from: public */
        public int getBaseLayoutResource() {
            return 17367189;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized int getBigBaseLayoutResource() {
            return R.layout.notification_template_material_big_base;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized int getBigPictureLayoutResource() {
            return R.layout.notification_template_material_big_picture;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized int getBigTextLayoutResource() {
            return R.layout.notification_template_material_big_text;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized int getInboxLayoutResource() {
            return R.layout.notification_template_material_inbox;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized int getMessagingLayoutResource() {
            return R.layout.notification_template_material_messaging;
        }

        private synchronized int getActionLayoutResource() {
            return R.layout.notification_material_action;
        }

        private synchronized int getEmphasizedActionLayoutResource() {
            return R.layout.notification_material_action_emphasized;
        }

        private synchronized int getActionTombstoneLayoutResource() {
            return R.layout.notification_material_action_tombstone;
        }

        private synchronized int getBackgroundColor() {
            if (isColorized()) {
                return this.mBackgroundColor != 1 ? this.mBackgroundColor : this.mN.color;
            }
            return 0;
        }

        private synchronized int getNeutralColor() {
            if (isColorized()) {
                return getSecondaryTextColor();
            }
            return resolveNeutralColor();
        }

        private synchronized int resolveBackgroundColor() {
            int backgroundColor = getBackgroundColor();
            if (backgroundColor == 0) {
                return this.mContext.getColor(R.color.notification_material_background_color);
            }
            return backgroundColor;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean isColorized() {
            return this.mN.isColorized();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean shouldTintActionButtons() {
            return this.mTintActionButtons;
        }

        private synchronized boolean textColorsNeedInversion() {
            int targetSdkVersion;
            return this.mStyle != null && MediaStyle.class.equals(this.mStyle.getClass()) && (targetSdkVersion = this.mContext.getApplicationInfo().targetSdkVersion) > 23 && targetSdkVersion < 26;
        }

        public synchronized void setColorPalette(int backgroundColor, int foregroundColor) {
            this.mBackgroundColor = backgroundColor;
            this.mForegroundColor = foregroundColor;
            this.mTextColorsAreForBackground = 1;
            ensureColors();
        }

        public synchronized void setRebuildStyledRemoteViews(boolean rebuild) {
            this.mRebuildStyledRemoteViews = rebuild;
        }

        public synchronized CharSequence getHeadsUpStatusBarText(boolean publicMode) {
            if (this.mStyle != null && !publicMode) {
                CharSequence text = this.mStyle.getHeadsUpStatusBarText();
                if (!TextUtils.isEmpty(text)) {
                    return text;
                }
            }
            return loadHeaderAppName();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void reduceImageSizes(Context context) {
        int i;
        int i2;
        int i3;
        if (this.extras.getBoolean(EXTRA_REDUCED_IMAGES)) {
            return;
        }
        boolean isLowRam = ActivityManager.isLowRamDeviceStatic();
        if (this.mLargeIcon != null || this.largeIcon != null) {
            Resources resources = context.getResources();
            Class<? extends Style> style = getNotificationStyle();
            if (isLowRam) {
                i = R.dimen.notification_right_icon_size_low_ram;
            } else {
                i = R.dimen.notification_right_icon_size;
            }
            int maxWidth = resources.getDimensionPixelSize(i);
            int maxHeight = maxWidth;
            if (MediaStyle.class.equals(style) || DecoratedMediaCustomViewStyle.class.equals(style)) {
                if (isLowRam) {
                    i2 = R.dimen.notification_media_image_max_height_low_ram;
                } else {
                    i2 = R.dimen.notification_media_image_max_height;
                }
                maxHeight = resources.getDimensionPixelSize(i2);
                if (isLowRam) {
                    i3 = R.dimen.notification_media_image_max_width_low_ram;
                } else {
                    i3 = R.dimen.notification_media_image_max_width;
                }
                maxWidth = resources.getDimensionPixelSize(i3);
            }
            if (this.mLargeIcon != null) {
                this.mLargeIcon.scaleDownIfNecessary(maxWidth, maxHeight);
            }
            if (this.largeIcon != null) {
                this.largeIcon = Icon.scaleDownIfNecessary(this.largeIcon, maxWidth, maxHeight);
            }
        }
        reduceImageSizesForRemoteView(this.contentView, context, isLowRam);
        reduceImageSizesForRemoteView(this.headsUpContentView, context, isLowRam);
        reduceImageSizesForRemoteView(this.bigContentView, context, isLowRam);
        this.extras.putBoolean(EXTRA_REDUCED_IMAGES, true);
    }

    private synchronized void reduceImageSizesForRemoteView(RemoteViews remoteView, Context context, boolean isLowRam) {
        int i;
        int i2;
        if (remoteView != null) {
            Resources resources = context.getResources();
            if (isLowRam) {
                i = R.dimen.notification_custom_view_max_image_width_low_ram;
            } else {
                i = R.dimen.notification_custom_view_max_image_width;
            }
            int maxWidth = resources.getDimensionPixelSize(i);
            if (isLowRam) {
                i2 = R.dimen.notification_custom_view_max_image_height_low_ram;
            } else {
                i2 = R.dimen.notification_custom_view_max_image_height;
            }
            int maxHeight = resources.getDimensionPixelSize(i2);
            remoteView.reduceImageSizes(maxWidth, maxHeight);
        }
    }

    private synchronized boolean isForegroundService() {
        return (this.flags & 64) != 0;
    }

    public synchronized boolean hasMediaSession() {
        return this.extras.getParcelable(EXTRA_MEDIA_SESSION) != null;
    }

    public synchronized Class<? extends Style> getNotificationStyle() {
        String templateClass = this.extras.getString(EXTRA_TEMPLATE);
        if (!TextUtils.isEmpty(templateClass)) {
            return getNotificationStyleClass(templateClass);
        }
        return null;
    }

    public synchronized boolean isColorized() {
        if (isColorizedMedia()) {
            return true;
        }
        return this.extras.getBoolean(EXTRA_COLORIZED) && (hasColorizedPermission() || isForegroundService());
    }

    private synchronized boolean hasColorizedPermission() {
        return (this.flags & 2048) != 0;
    }

    public synchronized boolean isColorizedMedia() {
        Class<? extends Style> style = getNotificationStyle();
        if (!MediaStyle.class.equals(style)) {
            return DecoratedMediaCustomViewStyle.class.equals(style) && this.extras.getBoolean(EXTRA_COLORIZED) && hasMediaSession();
        }
        Boolean colorized = (Boolean) this.extras.get(EXTRA_COLORIZED);
        return (colorized == null || colorized.booleanValue()) && hasMediaSession();
    }

    public synchronized boolean isMediaNotification() {
        Class<? extends Style> style = getNotificationStyle();
        return MediaStyle.class.equals(style) || DecoratedMediaCustomViewStyle.class.equals(style);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean hasLargeIcon() {
        return (this.mLargeIcon == null && this.largeIcon == null) ? false : true;
    }

    public synchronized boolean showsTime() {
        return this.when != 0 && this.extras.getBoolean(EXTRA_SHOW_WHEN);
    }

    public synchronized boolean showsChronometer() {
        return this.when != 0 && this.extras.getBoolean(EXTRA_SHOW_CHRONOMETER);
    }

    @SystemApi
    private protected static Class<? extends Style> getNotificationStyleClass(String templateClass) {
        Class<? extends Style>[] classes = {BigTextStyle.class, BigPictureStyle.class, InboxStyle.class, MediaStyle.class, DecoratedCustomViewStyle.class, DecoratedMediaCustomViewStyle.class, MessagingStyle.class};
        for (Class<? extends Style> innerClass : classes) {
            if (templateClass.equals(innerClass.getName())) {
                return innerClass;
            }
        }
        return null;
    }

    /* loaded from: classes.dex */
    public static abstract class Style {
        static final int MAX_REMOTE_INPUT_HISTORY_LINES = 3;
        private CharSequence mBigContentTitle;
        protected Builder mBuilder;
        protected CharSequence mSummaryText = null;
        protected boolean mSummaryTextSet = false;

        public abstract synchronized boolean areNotificationsVisiblyDifferent(Style style);

        protected void internalSetBigContentTitle(CharSequence title) {
            this.mBigContentTitle = title;
        }

        protected void internalSetSummaryText(CharSequence cs) {
            this.mSummaryText = cs;
            this.mSummaryTextSet = true;
        }

        public void setBuilder(Builder builder) {
            if (this.mBuilder != builder) {
                this.mBuilder = builder;
                if (this.mBuilder != null) {
                    this.mBuilder.setStyle(this);
                }
            }
        }

        protected void checkBuilder() {
            if (this.mBuilder == null) {
                throw new IllegalArgumentException("Style requires a valid Builder object");
            }
        }

        protected RemoteViews getStandardView(int layoutId) {
            return getStandardView(layoutId, null);
        }

        protected synchronized RemoteViews getStandardView(int layoutId, TemplateBindResult result) {
            checkBuilder();
            CharSequence oldBuilderContentTitle = this.mBuilder.getAllExtras().getCharSequence(Notification.EXTRA_TITLE);
            if (this.mBigContentTitle != null) {
                this.mBuilder.setContentTitle(this.mBigContentTitle);
            }
            RemoteViews contentView = this.mBuilder.applyStandardTemplateWithActions(layoutId, result);
            this.mBuilder.getAllExtras().putCharSequence(Notification.EXTRA_TITLE, oldBuilderContentTitle);
            if (this.mBigContentTitle != null && this.mBigContentTitle.equals("")) {
                contentView.setViewVisibility(16909122, 8);
            } else {
                contentView.setViewVisibility(16909122, 0);
            }
            return contentView;
        }

        public synchronized RemoteViews makeContentView(boolean increasedHeight) {
            return null;
        }

        public synchronized RemoteViews makeBigContentView() {
            return null;
        }

        public synchronized RemoteViews makeHeadsUpContentView(boolean increasedHeight) {
            return null;
        }

        public synchronized void addExtras(Bundle extras) {
            if (this.mSummaryTextSet) {
                extras.putCharSequence(Notification.EXTRA_SUMMARY_TEXT, this.mSummaryText);
            }
            if (this.mBigContentTitle != null) {
                extras.putCharSequence(Notification.EXTRA_TITLE_BIG, this.mBigContentTitle);
            }
            extras.putString(Notification.EXTRA_TEMPLATE, getClass().getName());
        }

        protected synchronized void restoreFromExtras(Bundle extras) {
            if (extras.containsKey(Notification.EXTRA_SUMMARY_TEXT)) {
                this.mSummaryText = extras.getCharSequence(Notification.EXTRA_SUMMARY_TEXT);
                this.mSummaryTextSet = true;
            }
            if (extras.containsKey(Notification.EXTRA_TITLE_BIG)) {
                this.mBigContentTitle = extras.getCharSequence(Notification.EXTRA_TITLE_BIG);
            }
        }

        public synchronized Notification buildStyled(Notification wip) {
            addExtras(wip.extras);
            return wip;
        }

        public synchronized void purgeResources() {
        }

        public Notification build() {
            checkBuilder();
            return this.mBuilder.build();
        }

        protected synchronized boolean hasProgress() {
            return true;
        }

        public synchronized boolean hasSummaryInHeader() {
            return true;
        }

        public synchronized boolean displayCustomViewInline() {
            return false;
        }

        public synchronized void reduceImageSizes(Context context) {
        }

        public synchronized void validate(Context context) {
        }

        public synchronized CharSequence getHeadsUpStatusBarText() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static class BigPictureStyle extends Style {
        public static final int MIN_ASHMEM_BITMAP_SIZE = 131072;
        private Icon mBigLargeIcon;
        private boolean mBigLargeIconSet = false;
        private Bitmap mPicture;

        public BigPictureStyle() {
        }

        @Deprecated
        public BigPictureStyle(Builder builder) {
            setBuilder(builder);
        }

        public BigPictureStyle setBigContentTitle(CharSequence title) {
            internalSetBigContentTitle(Notification.safeCharSequence(title));
            return this;
        }

        public BigPictureStyle setSummaryText(CharSequence cs) {
            internalSetSummaryText(Notification.safeCharSequence(cs));
            return this;
        }

        public synchronized Bitmap getBigPicture() {
            return this.mPicture;
        }

        public BigPictureStyle bigPicture(Bitmap b) {
            this.mPicture = b;
            return this;
        }

        public BigPictureStyle bigLargeIcon(Bitmap b) {
            return bigLargeIcon(b != null ? Icon.createWithBitmap(b) : null);
        }

        public BigPictureStyle bigLargeIcon(Icon icon) {
            this.mBigLargeIconSet = true;
            this.mBigLargeIcon = icon;
            return this;
        }

        @Override // android.app.Notification.Style
        public synchronized void purgeResources() {
            super.purgeResources();
            if (this.mPicture != null && this.mPicture.isMutable() && this.mPicture.getAllocationByteCount() >= 131072) {
                this.mPicture = this.mPicture.createAshmemBitmap();
            }
            if (this.mBigLargeIcon != null) {
                this.mBigLargeIcon.convertToAshmem();
            }
        }

        @Override // android.app.Notification.Style
        public synchronized void reduceImageSizes(Context context) {
            int i;
            int i2;
            int i3;
            super.reduceImageSizes(context);
            Resources resources = context.getResources();
            boolean isLowRam = ActivityManager.isLowRamDeviceStatic();
            if (this.mPicture != null) {
                if (isLowRam) {
                    i2 = R.dimen.notification_big_picture_max_height_low_ram;
                } else {
                    i2 = R.dimen.notification_big_picture_max_height;
                }
                int maxPictureWidth = resources.getDimensionPixelSize(i2);
                if (isLowRam) {
                    i3 = R.dimen.notification_big_picture_max_width_low_ram;
                } else {
                    i3 = R.dimen.notification_big_picture_max_width;
                }
                int maxPictureHeight = resources.getDimensionPixelSize(i3);
                this.mPicture = Icon.scaleDownIfNecessary(this.mPicture, maxPictureWidth, maxPictureHeight);
            }
            if (this.mBigLargeIcon != null) {
                if (isLowRam) {
                    i = R.dimen.notification_right_icon_size_low_ram;
                } else {
                    i = R.dimen.notification_right_icon_size;
                }
                int rightIconSize = resources.getDimensionPixelSize(i);
                this.mBigLargeIcon.scaleDownIfNecessary(rightIconSize, rightIconSize);
            }
        }

        @Override // android.app.Notification.Style
        public synchronized RemoteViews makeBigContentView() {
            Icon oldLargeIcon = null;
            Bitmap largeIconLegacy = null;
            if (this.mBigLargeIconSet) {
                oldLargeIcon = this.mBuilder.mN.mLargeIcon;
                this.mBuilder.mN.mLargeIcon = this.mBigLargeIcon;
                largeIconLegacy = this.mBuilder.mN.largeIcon;
                this.mBuilder.mN.largeIcon = null;
            }
            RemoteViews contentView = getStandardView(this.mBuilder.getBigPictureLayoutResource(), null);
            if (this.mSummaryTextSet) {
                contentView.setTextViewText(16909469, this.mBuilder.processTextSpans(this.mBuilder.processLegacyText(this.mSummaryText)));
                this.mBuilder.setTextViewColorSecondary(contentView, 16909469);
                contentView.setViewVisibility(16909469, 0);
            }
            this.mBuilder.setContentMinHeight(contentView, this.mBuilder.mN.hasLargeIcon());
            if (this.mBigLargeIconSet) {
                this.mBuilder.mN.mLargeIcon = oldLargeIcon;
                this.mBuilder.mN.largeIcon = largeIconLegacy;
            }
            contentView.setImageViewBitmap(R.id.big_picture, this.mPicture);
            return contentView;
        }

        @Override // android.app.Notification.Style
        public synchronized void addExtras(Bundle extras) {
            super.addExtras(extras);
            if (this.mBigLargeIconSet) {
                extras.putParcelable(Notification.EXTRA_LARGE_ICON_BIG, this.mBigLargeIcon);
            }
            extras.putParcelable(Notification.EXTRA_PICTURE, this.mPicture);
        }

        @Override // android.app.Notification.Style
        protected synchronized void restoreFromExtras(Bundle extras) {
            super.restoreFromExtras(extras);
            if (extras.containsKey(Notification.EXTRA_LARGE_ICON_BIG)) {
                this.mBigLargeIconSet = true;
                this.mBigLargeIcon = (Icon) extras.getParcelable(Notification.EXTRA_LARGE_ICON_BIG);
            }
            this.mPicture = (Bitmap) extras.getParcelable(Notification.EXTRA_PICTURE);
        }

        @Override // android.app.Notification.Style
        public synchronized boolean hasSummaryInHeader() {
            return false;
        }

        @Override // android.app.Notification.Style
        public synchronized boolean areNotificationsVisiblyDifferent(Style other) {
            if (other == null || getClass() != other.getClass()) {
                return true;
            }
            BigPictureStyle otherS = (BigPictureStyle) other;
            return areBitmapsObviouslyDifferent(getBigPicture(), otherS.getBigPicture());
        }

        private static synchronized boolean areBitmapsObviouslyDifferent(Bitmap a, Bitmap b) {
            if (a == b) {
                return false;
            }
            if (a == null || b == null) {
                return true;
            }
            if (a.getWidth() == b.getWidth() && a.getHeight() == b.getHeight() && a.getConfig() == b.getConfig() && a.getGenerationId() == b.getGenerationId()) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: classes.dex */
    public static class BigTextStyle extends Style {
        private CharSequence mBigText;

        public BigTextStyle() {
        }

        @Deprecated
        public BigTextStyle(Builder builder) {
            setBuilder(builder);
        }

        public BigTextStyle setBigContentTitle(CharSequence title) {
            internalSetBigContentTitle(Notification.safeCharSequence(title));
            return this;
        }

        public BigTextStyle setSummaryText(CharSequence cs) {
            internalSetSummaryText(Notification.safeCharSequence(cs));
            return this;
        }

        public BigTextStyle bigText(CharSequence cs) {
            this.mBigText = Notification.safeCharSequence(cs);
            return this;
        }

        public synchronized CharSequence getBigText() {
            return this.mBigText;
        }

        @Override // android.app.Notification.Style
        public synchronized void addExtras(Bundle extras) {
            super.addExtras(extras);
            extras.putCharSequence(Notification.EXTRA_BIG_TEXT, this.mBigText);
        }

        @Override // android.app.Notification.Style
        protected synchronized void restoreFromExtras(Bundle extras) {
            super.restoreFromExtras(extras);
            this.mBigText = extras.getCharSequence(Notification.EXTRA_BIG_TEXT);
        }

        @Override // android.app.Notification.Style
        public synchronized RemoteViews makeContentView(boolean increasedHeight) {
            if (increasedHeight) {
                this.mBuilder.mOriginalActions = this.mBuilder.mActions;
                this.mBuilder.mActions = new ArrayList();
                RemoteViews remoteViews = makeBigContentView();
                this.mBuilder.mActions = this.mBuilder.mOriginalActions;
                this.mBuilder.mOriginalActions = null;
                return remoteViews;
            }
            RemoteViews remoteViews2 = super.makeContentView(increasedHeight);
            return remoteViews2;
        }

        @Override // android.app.Notification.Style
        public synchronized RemoteViews makeHeadsUpContentView(boolean increasedHeight) {
            if (increasedHeight && this.mBuilder.mActions.size() > 0) {
                return makeBigContentView();
            }
            return super.makeHeadsUpContentView(increasedHeight);
        }

        @Override // android.app.Notification.Style
        public synchronized RemoteViews makeBigContentView() {
            CharSequence text = this.mBuilder.getAllExtras().getCharSequence(Notification.EXTRA_TEXT);
            this.mBuilder.getAllExtras().putCharSequence(Notification.EXTRA_TEXT, null);
            TemplateBindResult result = new TemplateBindResult();
            RemoteViews contentView = getStandardView(this.mBuilder.getBigTextLayoutResource(), result);
            contentView.setInt(R.id.big_text, "setImageEndMargin", result.getIconMarginEnd());
            this.mBuilder.getAllExtras().putCharSequence(Notification.EXTRA_TEXT, text);
            CharSequence bigTextText = this.mBuilder.processLegacyText(this.mBigText);
            if (TextUtils.isEmpty(bigTextText)) {
                bigTextText = this.mBuilder.processLegacyText(text);
            }
            applyBigTextContentView(this.mBuilder, contentView, bigTextText);
            return contentView;
        }

        @Override // android.app.Notification.Style
        public synchronized boolean areNotificationsVisiblyDifferent(Style other) {
            if (other == null || getClass() != other.getClass()) {
                return true;
            }
            BigTextStyle newS = (BigTextStyle) other;
            return true ^ Objects.equals(String.valueOf(getBigText()), String.valueOf(newS.getBigText()));
        }

        static synchronized void applyBigTextContentView(Builder builder, RemoteViews contentView, CharSequence bigTextText) {
            contentView.setTextViewText(R.id.big_text, builder.processTextSpans(bigTextText));
            builder.setTextViewColorSecondary(contentView, R.id.big_text);
            contentView.setViewVisibility(R.id.big_text, TextUtils.isEmpty(bigTextText) ? 8 : 0);
            contentView.setBoolean(R.id.big_text, "setHasImage", builder.mN.hasLargeIcon());
        }
    }

    /* loaded from: classes.dex */
    public static class MessagingStyle extends Style {
        public static final int MAXIMUM_RETAINED_MESSAGES = 25;
        CharSequence mConversationTitle;
        List<Message> mHistoricMessages;
        boolean mIsGroupConversation;
        List<Message> mMessages;
        Person mUser;

        synchronized MessagingStyle() {
            this.mMessages = new ArrayList();
            this.mHistoricMessages = new ArrayList();
        }

        public MessagingStyle(CharSequence userDisplayName) {
            this(new Person.Builder().setName(userDisplayName).build());
        }

        public MessagingStyle(Person user) {
            this.mMessages = new ArrayList();
            this.mHistoricMessages = new ArrayList();
            this.mUser = user;
        }

        @Override // android.app.Notification.Style
        public synchronized void validate(Context context) {
            super.validate(context);
            if (context.getApplicationInfo().targetSdkVersion >= 28) {
                if (this.mUser == null || this.mUser.getName() == null) {
                    throw new RuntimeException("User must be valid and have a name.");
                }
            }
        }

        @Override // android.app.Notification.Style
        public synchronized CharSequence getHeadsUpStatusBarText() {
            CharSequence conversationTitle;
            if (!TextUtils.isEmpty(((Style) this).mBigContentTitle)) {
                conversationTitle = ((Style) this).mBigContentTitle;
            } else {
                conversationTitle = this.mConversationTitle;
            }
            if (!TextUtils.isEmpty(conversationTitle) && !hasOnlyWhiteSpaceSenders()) {
                return conversationTitle;
            }
            return null;
        }

        public Person getUser() {
            return this.mUser;
        }

        public CharSequence getUserDisplayName() {
            return this.mUser.getName();
        }

        public MessagingStyle setConversationTitle(CharSequence conversationTitle) {
            this.mConversationTitle = conversationTitle;
            return this;
        }

        public CharSequence getConversationTitle() {
            return this.mConversationTitle;
        }

        public MessagingStyle addMessage(CharSequence text, long timestamp, CharSequence sender) {
            return addMessage(text, timestamp, sender == null ? null : new Person.Builder().setName(sender).build());
        }

        public MessagingStyle addMessage(CharSequence text, long timestamp, Person sender) {
            return addMessage(new Message(text, timestamp, sender));
        }

        public MessagingStyle addMessage(Message message) {
            this.mMessages.add(message);
            if (this.mMessages.size() > 25) {
                this.mMessages.remove(0);
            }
            return this;
        }

        public MessagingStyle addHistoricMessage(Message message) {
            this.mHistoricMessages.add(message);
            if (this.mHistoricMessages.size() > 25) {
                this.mHistoricMessages.remove(0);
            }
            return this;
        }

        public List<Message> getMessages() {
            return this.mMessages;
        }

        public List<Message> getHistoricMessages() {
            return this.mHistoricMessages;
        }

        public MessagingStyle setGroupConversation(boolean isGroupConversation) {
            this.mIsGroupConversation = isGroupConversation;
            return this;
        }

        public boolean isGroupConversation() {
            if (this.mBuilder == null || this.mBuilder.mContext.getApplicationInfo().targetSdkVersion >= 28) {
                return this.mIsGroupConversation;
            }
            return this.mConversationTitle != null;
        }

        @Override // android.app.Notification.Style
        public synchronized void addExtras(Bundle extras) {
            super.addExtras(extras);
            if (this.mUser != null) {
                extras.putCharSequence(Notification.EXTRA_SELF_DISPLAY_NAME, this.mUser.getName());
                extras.putParcelable(Notification.EXTRA_MESSAGING_PERSON, this.mUser);
            }
            if (this.mConversationTitle != null) {
                extras.putCharSequence(Notification.EXTRA_CONVERSATION_TITLE, this.mConversationTitle);
            }
            if (!this.mMessages.isEmpty()) {
                extras.putParcelableArray(Notification.EXTRA_MESSAGES, Message.getBundleArrayForMessages(this.mMessages));
            }
            if (!this.mHistoricMessages.isEmpty()) {
                extras.putParcelableArray(Notification.EXTRA_HISTORIC_MESSAGES, Message.getBundleArrayForMessages(this.mHistoricMessages));
            }
            fixTitleAndTextExtras(extras);
            extras.putBoolean(Notification.EXTRA_IS_GROUP_CONVERSATION, this.mIsGroupConversation);
        }

        private synchronized void fixTitleAndTextExtras(Bundle extras) {
            CharSequence text;
            CharSequence title;
            Message m = findLatestIncomingMessage();
            CharSequence sender = null;
            if (m == null) {
                text = null;
            } else {
                text = m.mText;
            }
            if (m != null) {
                sender = ((m.mSender == null || TextUtils.isEmpty(m.mSender.getName())) ? this.mUser : m.mSender).getName();
            }
            if (!TextUtils.isEmpty(this.mConversationTitle)) {
                if (!TextUtils.isEmpty(sender)) {
                    BidiFormatter bidi = BidiFormatter.getInstance();
                    title = this.mBuilder.mContext.getString(R.string.notification_messaging_title_template, bidi.unicodeWrap(this.mConversationTitle), bidi.unicodeWrap(sender));
                } else {
                    title = this.mConversationTitle;
                }
            } else {
                title = sender;
            }
            if (title != null) {
                extras.putCharSequence(Notification.EXTRA_TITLE, title);
            }
            if (text != null) {
                extras.putCharSequence(Notification.EXTRA_TEXT, text);
            }
        }

        @Override // android.app.Notification.Style
        protected synchronized void restoreFromExtras(Bundle extras) {
            super.restoreFromExtras(extras);
            this.mUser = (Person) extras.getParcelable(Notification.EXTRA_MESSAGING_PERSON);
            if (this.mUser == null) {
                CharSequence displayName = extras.getCharSequence(Notification.EXTRA_SELF_DISPLAY_NAME);
                this.mUser = new Person.Builder().setName(displayName).build();
            }
            this.mConversationTitle = extras.getCharSequence(Notification.EXTRA_CONVERSATION_TITLE);
            Parcelable[] messages = extras.getParcelableArray(Notification.EXTRA_MESSAGES);
            this.mMessages = Message.getMessagesFromBundleArray(messages);
            Parcelable[] histMessages = extras.getParcelableArray(Notification.EXTRA_HISTORIC_MESSAGES);
            this.mHistoricMessages = Message.getMessagesFromBundleArray(histMessages);
            this.mIsGroupConversation = extras.getBoolean(Notification.EXTRA_IS_GROUP_CONVERSATION);
        }

        @Override // android.app.Notification.Style
        public synchronized RemoteViews makeContentView(boolean increasedHeight) {
            this.mBuilder.mOriginalActions = this.mBuilder.mActions;
            this.mBuilder.mActions = new ArrayList();
            RemoteViews remoteViews = makeMessagingView(true, false);
            this.mBuilder.mActions = this.mBuilder.mOriginalActions;
            this.mBuilder.mOriginalActions = null;
            return remoteViews;
        }

        @Override // android.app.Notification.Style
        public synchronized boolean areNotificationsVisiblyDifferent(Style other) {
            CharSequence name;
            CharSequence name2;
            if (other == null || getClass() != other.getClass()) {
                return true;
            }
            MessagingStyle newS = (MessagingStyle) other;
            List<Message> oldMs = getMessages();
            List<Message> newMs = newS.getMessages();
            if (oldMs == null || newMs == null) {
                newMs = new ArrayList();
            }
            int n = oldMs.size();
            if (n != newMs.size()) {
                return true;
            }
            for (int i = 0; i < n; i++) {
                Message oldM = oldMs.get(i);
                Message newM = newMs.get(i);
                if (!Objects.equals(String.valueOf(oldM.getText()), String.valueOf(newM.getText())) || !Objects.equals(oldM.getDataUri(), newM.getDataUri())) {
                    return true;
                }
                if (oldM.getSenderPerson() == null) {
                    name = oldM.getSender();
                } else {
                    name = oldM.getSenderPerson().getName();
                }
                String oldSender = String.valueOf(name);
                if (newM.getSenderPerson() == null) {
                    name2 = newM.getSender();
                } else {
                    name2 = newM.getSenderPerson().getName();
                }
                String newSender = String.valueOf(name2);
                if (!Objects.equals(oldSender, newSender)) {
                    return true;
                }
                String oldKey = oldM.getSenderPerson() == null ? null : oldM.getSenderPerson().getKey();
                String newKey = newM.getSenderPerson() != null ? newM.getSenderPerson().getKey() : null;
                if (!Objects.equals(oldKey, newKey)) {
                    return true;
                }
            }
            return false;
        }

        private synchronized Message findLatestIncomingMessage() {
            return findLatestIncomingMessage(this.mMessages);
        }

        public static synchronized Message findLatestIncomingMessage(List<Message> messages) {
            for (int i = messages.size() - 1; i >= 0; i--) {
                Message m = messages.get(i);
                if (m.mSender != null && !TextUtils.isEmpty(m.mSender.getName())) {
                    return m;
                }
            }
            if (!messages.isEmpty()) {
                return messages.get(messages.size() - 1);
            }
            return null;
        }

        @Override // android.app.Notification.Style
        public synchronized RemoteViews makeBigContentView() {
            return makeMessagingView(false, true);
        }

        private synchronized RemoteViews makeMessagingView(boolean displayImagesAtEnd, boolean hideRightIcons) {
            CharSequence conversationTitle;
            boolean isOneToOne;
            if (!TextUtils.isEmpty(((Style) this).mBigContentTitle)) {
                conversationTitle = ((Style) this).mBigContentTitle;
            } else {
                conversationTitle = this.mConversationTitle;
            }
            boolean z = false;
            boolean atLeastP = this.mBuilder.mContext.getApplicationInfo().targetSdkVersion >= 28;
            CharSequence nameReplacement = null;
            Icon avatarReplacement = null;
            if (!atLeastP) {
                isOneToOne = TextUtils.isEmpty(conversationTitle);
                avatarReplacement = this.mBuilder.mN.mLargeIcon;
                if (hasOnlyWhiteSpaceSenders()) {
                    isOneToOne = true;
                    nameReplacement = conversationTitle;
                    conversationTitle = null;
                }
            } else {
                boolean isOneToOne2 = isGroupConversation();
                isOneToOne = !isOneToOne2;
            }
            TemplateBindResult bindResult = new TemplateBindResult();
            Builder builder = this.mBuilder;
            int messagingLayoutResource = this.mBuilder.getMessagingLayoutResource();
            StandardTemplateParams text = this.mBuilder.mParams.reset().hasProgress(false).title(conversationTitle).text(null);
            if (hideRightIcons || isOneToOne) {
                z = true;
            }
            RemoteViews contentView = builder.applyStandardTemplateWithActions(messagingLayoutResource, text.hideLargeIcon(z).hideReplyIcon(hideRightIcons).headerTextSecondary(conversationTitle), bindResult);
            addExtras(this.mBuilder.mN.extras);
            contentView.setViewLayoutMarginEnd(R.id.notification_messaging, bindResult.getIconMarginEnd());
            contentView.setInt(16909445, "setLayoutColor", this.mBuilder.isColorized() ? this.mBuilder.getPrimaryTextColor() : this.mBuilder.resolveContrastColor());
            contentView.setInt(16909445, "setSenderTextColor", this.mBuilder.getPrimaryTextColor());
            contentView.setInt(16909445, "setMessageTextColor", this.mBuilder.getSecondaryTextColor());
            contentView.setBoolean(16909445, "setDisplayImagesAtEnd", displayImagesAtEnd);
            contentView.setIcon(16909445, "setAvatarReplacement", avatarReplacement);
            contentView.setCharSequence(16909445, "setNameReplacement", nameReplacement);
            contentView.setBoolean(16909445, "setIsOneToOne", isOneToOne);
            contentView.setBundle(16909445, "setData", this.mBuilder.mN.extras);
            return contentView;
        }

        private synchronized boolean hasOnlyWhiteSpaceSenders() {
            for (int i = 0; i < this.mMessages.size(); i++) {
                Message m = this.mMessages.get(i);
                Person sender = m.getSenderPerson();
                if (sender != null && !isWhiteSpace(sender.getName())) {
                    return false;
                }
            }
            return true;
        }

        private synchronized boolean isWhiteSpace(CharSequence sender) {
            if (TextUtils.isEmpty(sender) || sender.toString().matches("^\\s*$")) {
                return true;
            }
            for (int i = 0; i < sender.length(); i++) {
                char c = sender.charAt(i);
                if (c != 8203) {
                    return false;
                }
            }
            return true;
        }

        private synchronized CharSequence createConversationTitleFromMessages() {
            ArraySet<CharSequence> names = new ArraySet<>();
            for (int i = 0; i < this.mMessages.size(); i++) {
                Message m = this.mMessages.get(i);
                Person sender = m.getSenderPerson();
                if (sender != null) {
                    names.add(sender.getName());
                }
            }
            SpannableStringBuilder title = new SpannableStringBuilder();
            int size = names.size();
            for (int i2 = 0; i2 < size; i2++) {
                CharSequence name = names.valueAt(i2);
                if (!TextUtils.isEmpty(title)) {
                    title.append((CharSequence) ", ");
                }
                title.append(BidiFormatter.getInstance().unicodeWrap(name));
            }
            return title;
        }

        @Override // android.app.Notification.Style
        public synchronized RemoteViews makeHeadsUpContentView(boolean increasedHeight) {
            RemoteViews remoteViews = makeMessagingView(true, true);
            remoteViews.setInt(R.id.notification_messaging, "setMaxDisplayedLines", 1);
            return remoteViews;
        }

        private static synchronized TextAppearanceSpan makeFontColorSpan(int color) {
            return new TextAppearanceSpan(null, 0, 0, ColorStateList.valueOf(color), null);
        }

        /* loaded from: classes.dex */
        public static final class Message {
            static final String KEY_DATA_MIME_TYPE = "type";
            static final String KEY_DATA_URI = "uri";
            static final String KEY_EXTRAS_BUNDLE = "extras";
            static final String KEY_REMOTE_INPUT_HISTORY = "remote_input_history";
            static final String KEY_SENDER = "sender";
            static final String KEY_SENDER_PERSON = "sender_person";
            static final String KEY_TEXT = "text";
            static final String KEY_TIMESTAMP = "time";
            private String mDataMimeType;
            private Uri mDataUri;
            private Bundle mExtras;
            private final boolean mRemoteInputHistory;
            private final Person mSender;
            private final CharSequence mText;
            private final long mTimestamp;

            public Message(CharSequence text, long timestamp, CharSequence sender) {
                this(text, timestamp, sender == null ? null : new Person.Builder().setName(sender).build());
            }

            public Message(CharSequence text, long timestamp, Person sender) {
                this(text, timestamp, sender, false);
            }

            public synchronized Message(CharSequence text, long timestamp, Person sender, boolean remoteInputHistory) {
                this.mExtras = new Bundle();
                this.mText = text;
                this.mTimestamp = timestamp;
                this.mSender = sender;
                this.mRemoteInputHistory = remoteInputHistory;
            }

            public Message setData(String dataMimeType, Uri dataUri) {
                this.mDataMimeType = dataMimeType;
                this.mDataUri = dataUri;
                return this;
            }

            public CharSequence getText() {
                return this.mText;
            }

            public long getTimestamp() {
                return this.mTimestamp;
            }

            public Bundle getExtras() {
                return this.mExtras;
            }

            public CharSequence getSender() {
                if (this.mSender == null) {
                    return null;
                }
                return this.mSender.getName();
            }

            public Person getSenderPerson() {
                return this.mSender;
            }

            public String getDataMimeType() {
                return this.mDataMimeType;
            }

            public Uri getDataUri() {
                return this.mDataUri;
            }

            public synchronized boolean isRemoteInputHistory() {
                return this.mRemoteInputHistory;
            }

            private synchronized Bundle toBundle() {
                Bundle bundle = new Bundle();
                if (this.mText != null) {
                    bundle.putCharSequence("text", this.mText);
                }
                bundle.putLong("time", this.mTimestamp);
                if (this.mSender != null) {
                    bundle.putCharSequence("sender", this.mSender.getName());
                    bundle.putParcelable(KEY_SENDER_PERSON, this.mSender);
                }
                if (this.mDataMimeType != null) {
                    bundle.putString("type", this.mDataMimeType);
                }
                if (this.mDataUri != null) {
                    bundle.putParcelable("uri", this.mDataUri);
                }
                if (this.mExtras != null) {
                    bundle.putBundle(KEY_EXTRAS_BUNDLE, this.mExtras);
                }
                if (this.mRemoteInputHistory) {
                    bundle.putBoolean(KEY_REMOTE_INPUT_HISTORY, this.mRemoteInputHistory);
                }
                return bundle;
            }

            static synchronized Bundle[] getBundleArrayForMessages(List<Message> messages) {
                Bundle[] bundles = new Bundle[messages.size()];
                int N = messages.size();
                for (int i = 0; i < N; i++) {
                    bundles[i] = messages.get(i).toBundle();
                }
                return bundles;
            }

            public static synchronized List<Message> getMessagesFromBundleArray(Parcelable[] bundles) {
                Message message;
                if (bundles == null) {
                    return new ArrayList();
                }
                List<Message> messages = new ArrayList<>(bundles.length);
                for (int i = 0; i < bundles.length; i++) {
                    if ((bundles[i] instanceof Bundle) && (message = getMessageFromBundle((Bundle) bundles[i])) != null) {
                        messages.add(message);
                    }
                }
                return messages;
            }

            public static synchronized Message getMessageFromBundle(Bundle bundle) {
                CharSequence senderName;
                try {
                    if (bundle.containsKey("text") && bundle.containsKey("time")) {
                        Person senderPerson = (Person) bundle.getParcelable(KEY_SENDER_PERSON);
                        if (senderPerson == null && (senderName = bundle.getCharSequence("sender")) != null) {
                            senderPerson = new Person.Builder().setName(senderName).build();
                        }
                        Message message = new Message(bundle.getCharSequence("text"), bundle.getLong("time"), senderPerson, bundle.getBoolean(KEY_REMOTE_INPUT_HISTORY, false));
                        if (bundle.containsKey("type") && bundle.containsKey("uri")) {
                            message.setData(bundle.getString("type"), (Uri) bundle.getParcelable("uri"));
                        }
                        if (bundle.containsKey(KEY_EXTRAS_BUNDLE)) {
                            message.getExtras().putAll(bundle.getBundle(KEY_EXTRAS_BUNDLE));
                        }
                        return message;
                    }
                    return null;
                } catch (ClassCastException e) {
                    return null;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class InboxStyle extends Style {
        private static final int NUMBER_OF_HISTORY_ALLOWED_UNTIL_REDUCTION = 1;
        private ArrayList<CharSequence> mTexts = new ArrayList<>(5);

        public InboxStyle() {
        }

        @Deprecated
        public InboxStyle(Builder builder) {
            setBuilder(builder);
        }

        public InboxStyle setBigContentTitle(CharSequence title) {
            internalSetBigContentTitle(Notification.safeCharSequence(title));
            return this;
        }

        public InboxStyle setSummaryText(CharSequence cs) {
            internalSetSummaryText(Notification.safeCharSequence(cs));
            return this;
        }

        public InboxStyle addLine(CharSequence cs) {
            this.mTexts.add(Notification.safeCharSequence(cs));
            return this;
        }

        public synchronized ArrayList<CharSequence> getLines() {
            return this.mTexts;
        }

        @Override // android.app.Notification.Style
        public synchronized void addExtras(Bundle extras) {
            super.addExtras(extras);
            CharSequence[] a = new CharSequence[this.mTexts.size()];
            extras.putCharSequenceArray(Notification.EXTRA_TEXT_LINES, (CharSequence[]) this.mTexts.toArray(a));
        }

        @Override // android.app.Notification.Style
        protected synchronized void restoreFromExtras(Bundle extras) {
            super.restoreFromExtras(extras);
            this.mTexts.clear();
            if (extras.containsKey(Notification.EXTRA_TEXT_LINES)) {
                Collections.addAll(this.mTexts, extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES));
            }
        }

        @Override // android.app.Notification.Style
        public synchronized RemoteViews makeBigContentView() {
            int onlyViewId;
            CharSequence oldBuilderContentText = this.mBuilder.mN.extras.getCharSequence(Notification.EXTRA_TEXT);
            this.mBuilder.getAllExtras().putCharSequence(Notification.EXTRA_TEXT, null);
            TemplateBindResult result = new TemplateBindResult();
            RemoteViews contentView = getStandardView(this.mBuilder.getInboxLayoutResource(), result);
            this.mBuilder.getAllExtras().putCharSequence(Notification.EXTRA_TEXT, oldBuilderContentText);
            int[] rowIds = {R.id.inbox_text0, R.id.inbox_text1, R.id.inbox_text2, R.id.inbox_text3, R.id.inbox_text4, R.id.inbox_text5, R.id.inbox_text6};
            int i = 0;
            for (int rowId : rowIds) {
                contentView.setViewVisibility(rowId, 8);
            }
            int i2 = 0;
            int topPadding = this.mBuilder.mContext.getResources().getDimensionPixelSize(R.dimen.notification_inbox_item_top_padding);
            int maxRows = rowIds.length;
            if (this.mBuilder.mActions.size() > 0) {
                maxRows--;
            }
            CharSequence[] remoteInputHistory = this.mBuilder.mN.extras.getCharSequenceArray(Notification.EXTRA_REMOTE_INPUT_HISTORY);
            if (remoteInputHistory != null && remoteInputHistory.length > 1) {
                int numRemoteInputs = Math.min(remoteInputHistory.length, 3);
                int totalNumRows = (this.mTexts.size() + numRemoteInputs) - 1;
                if (totalNumRows > maxRows) {
                    int overflow = totalNumRows - maxRows;
                    if (this.mTexts.size() > maxRows) {
                        maxRows -= overflow;
                    } else {
                        i2 = overflow;
                    }
                }
            }
            int i3 = i2;
            boolean first = true;
            int onlyViewId2 = 0;
            int maxRows2 = maxRows;
            while (i3 < this.mTexts.size() && i3 < maxRows2) {
                CharSequence str = this.mTexts.get(i3);
                if (!TextUtils.isEmpty(str)) {
                    contentView.setViewVisibility(rowIds[i3], i);
                    contentView.setTextViewText(rowIds[i3], this.mBuilder.processTextSpans(this.mBuilder.processLegacyText(str)));
                    this.mBuilder.setTextViewColorSecondary(contentView, rowIds[i3]);
                    boolean first2 = first;
                    contentView.setViewPadding(rowIds[i3], 0, topPadding, 0, 0);
                    handleInboxImageMargin(contentView, rowIds[i3], first2, result.getIconMarginEnd());
                    if (first2) {
                        onlyViewId = rowIds[i3];
                    } else {
                        onlyViewId = 0;
                    }
                    onlyViewId2 = onlyViewId;
                    first = false;
                }
                i3++;
                i = 0;
            }
            if (onlyViewId2 != 0) {
                int topPadding2 = this.mBuilder.mContext.getResources().getDimensionPixelSize(R.dimen.notification_text_margin_top);
                contentView.setViewPadding(onlyViewId2, 0, topPadding2, 0, 0);
            }
            return contentView;
        }

        @Override // android.app.Notification.Style
        public synchronized boolean areNotificationsVisiblyDifferent(Style other) {
            if (other == null || getClass() != other.getClass()) {
                return true;
            }
            InboxStyle newS = (InboxStyle) other;
            ArrayList<CharSequence> myLines = getLines();
            ArrayList<CharSequence> newLines = newS.getLines();
            int n = myLines.size();
            if (n != newLines.size()) {
                return true;
            }
            for (int i = 0; i < n; i++) {
                if (!Objects.equals(String.valueOf(myLines.get(i)), String.valueOf(newLines.get(i)))) {
                    return true;
                }
            }
            return false;
        }

        private synchronized void handleInboxImageMargin(RemoteViews contentView, int id, boolean first, int marginEndValue) {
            int endMargin = 0;
            if (first) {
                boolean hasProgress = false;
                int max = this.mBuilder.mN.extras.getInt(Notification.EXTRA_PROGRESS_MAX, 0);
                boolean ind = this.mBuilder.mN.extras.getBoolean(Notification.EXTRA_PROGRESS_INDETERMINATE);
                hasProgress = (max != 0 || ind) ? true : true;
                if (!hasProgress) {
                    endMargin = marginEndValue;
                }
            }
            contentView.setViewLayoutMarginEnd(id, endMargin);
        }
    }

    /* loaded from: classes.dex */
    public static class MediaStyle extends Style {
        static final int MAX_MEDIA_BUTTONS = 5;
        static final int MAX_MEDIA_BUTTONS_IN_COMPACT = 3;
        private int[] mActionsToShowInCompact = null;
        private MediaSession.Token mToken;

        public MediaStyle() {
        }

        @Deprecated
        public MediaStyle(Builder builder) {
            setBuilder(builder);
        }

        public MediaStyle setShowActionsInCompactView(int... actions) {
            this.mActionsToShowInCompact = actions;
            return this;
        }

        public MediaStyle setMediaSession(MediaSession.Token token) {
            this.mToken = token;
            return this;
        }

        private protected Notification buildStyled(Notification wip) {
            super.buildStyled(wip);
            if (wip.category == null) {
                wip.category = Notification.CATEGORY_TRANSPORT;
            }
            return wip;
        }

        @Override // android.app.Notification.Style
        public synchronized RemoteViews makeContentView(boolean increasedHeight) {
            return makeMediaContentView();
        }

        @Override // android.app.Notification.Style
        public synchronized RemoteViews makeBigContentView() {
            return makeMediaBigContentView();
        }

        @Override // android.app.Notification.Style
        public synchronized RemoteViews makeHeadsUpContentView(boolean increasedHeight) {
            RemoteViews expanded = makeMediaBigContentView();
            return expanded != null ? expanded : makeMediaContentView();
        }

        @Override // android.app.Notification.Style
        public synchronized void addExtras(Bundle extras) {
            super.addExtras(extras);
            if (this.mToken != null) {
                extras.putParcelable(Notification.EXTRA_MEDIA_SESSION, this.mToken);
            }
            if (this.mActionsToShowInCompact != null) {
                extras.putIntArray(Notification.EXTRA_COMPACT_ACTIONS, this.mActionsToShowInCompact);
            }
        }

        @Override // android.app.Notification.Style
        protected synchronized void restoreFromExtras(Bundle extras) {
            super.restoreFromExtras(extras);
            if (extras.containsKey(Notification.EXTRA_MEDIA_SESSION)) {
                this.mToken = (MediaSession.Token) extras.getParcelable(Notification.EXTRA_MEDIA_SESSION);
            }
            if (extras.containsKey(Notification.EXTRA_COMPACT_ACTIONS)) {
                this.mActionsToShowInCompact = extras.getIntArray(Notification.EXTRA_COMPACT_ACTIONS);
            }
        }

        @Override // android.app.Notification.Style
        public synchronized boolean areNotificationsVisiblyDifferent(Style other) {
            if (other == null || getClass() != other.getClass()) {
                return true;
            }
            return false;
        }

        private synchronized RemoteViews generateMediaActionButton(Action action, int color) {
            int tintColor;
            boolean tombstone = action.actionIntent == null;
            RemoteViews button = new BuilderRemoteViews(this.mBuilder.mContext.getApplicationInfo(), R.layout.notification_material_media_action);
            button.setImageViewIcon(R.id.action0, action.getIcon());
            if (!this.mBuilder.shouldTintActionButtons() && !this.mBuilder.isColorized()) {
                tintColor = NotificationColorUtil.resolveColor(this.mBuilder.mContext, 0);
            } else {
                tintColor = color;
            }
            button.setDrawableTint(R.id.action0, false, tintColor, PorterDuff.Mode.SRC_ATOP);
            if (!tombstone) {
                button.setOnClickPendingIntent(R.id.action0, action.actionIntent);
            }
            button.setContentDescription(R.id.action0, action.title);
            return button;
        }

        private synchronized RemoteViews makeMediaContentView() {
            RemoteViews view = this.mBuilder.applyStandardTemplate((int) R.layout.notification_template_material_media, false, (TemplateBindResult) null);
            int numActions = this.mBuilder.mActions.size();
            int N = this.mActionsToShowInCompact == null ? 0 : Math.min(this.mActionsToShowInCompact.length, 3);
            if (N > 0) {
                view.removeAllViews(16909147);
                for (int i = 0; i < N; i++) {
                    if (i < numActions) {
                        Action action = (Action) this.mBuilder.mActions.get(this.mActionsToShowInCompact[i]);
                        RemoteViews button = generateMediaActionButton(action, getActionColor());
                        view.addView(16909147, button);
                    } else {
                        throw new IllegalArgumentException(String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", Integer.valueOf(i), Integer.valueOf(numActions - 1)));
                    }
                }
            }
            handleImage(view);
            int endMargin = R.dimen.notification_content_margin_end;
            if (this.mBuilder.mN.hasLargeIcon()) {
                endMargin = R.dimen.notification_media_image_margin_end;
            }
            view.setViewLayoutMarginEndDimen(R.id.notification_main_column, endMargin);
            return view;
        }

        private synchronized int getActionColor() {
            return this.mBuilder.isColorized() ? this.mBuilder.getPrimaryTextColor() : this.mBuilder.resolveContrastColor();
        }

        private synchronized RemoteViews makeMediaBigContentView() {
            int actionCount = Math.min(this.mBuilder.mActions.size(), 5);
            int actionsInCompact = this.mActionsToShowInCompact == null ? 0 : Math.min(this.mActionsToShowInCompact.length, 3);
            if (this.mBuilder.mN.hasLargeIcon() || actionCount > actionsInCompact) {
                RemoteViews big = this.mBuilder.applyStandardTemplate((int) R.layout.notification_template_material_big_media, false, (TemplateBindResult) null);
                if (actionCount > 0) {
                    big.removeAllViews(16909147);
                    for (int i = 0; i < actionCount; i++) {
                        RemoteViews button = generateMediaActionButton((Action) this.mBuilder.mActions.get(i), getActionColor());
                        big.addView(16909147, button);
                    }
                }
                handleImage(big);
                return big;
            }
            return null;
        }

        private synchronized void handleImage(RemoteViews contentView) {
            if (this.mBuilder.mN.hasLargeIcon()) {
                contentView.setViewLayoutMarginEndDimen(16909122, 0);
                contentView.setViewLayoutMarginEndDimen(16909469, 0);
            }
        }

        @Override // android.app.Notification.Style
        protected synchronized boolean hasProgress() {
            return false;
        }
    }

    /* loaded from: classes.dex */
    public static class DecoratedCustomViewStyle extends Style {
        @Override // android.app.Notification.Style
        public synchronized boolean displayCustomViewInline() {
            return true;
        }

        @Override // android.app.Notification.Style
        public synchronized RemoteViews makeContentView(boolean increasedHeight) {
            return makeStandardTemplateWithCustomContent(this.mBuilder.mN.contentView);
        }

        @Override // android.app.Notification.Style
        public synchronized RemoteViews makeBigContentView() {
            return makeDecoratedBigContentView();
        }

        @Override // android.app.Notification.Style
        public synchronized RemoteViews makeHeadsUpContentView(boolean increasedHeight) {
            return makeDecoratedHeadsUpContentView();
        }

        private synchronized RemoteViews makeDecoratedHeadsUpContentView() {
            RemoteViews headsUpContentView = this.mBuilder.mN.headsUpContentView == null ? this.mBuilder.mN.contentView : this.mBuilder.mN.headsUpContentView;
            if (this.mBuilder.mActions.size() == 0) {
                return makeStandardTemplateWithCustomContent(headsUpContentView);
            }
            TemplateBindResult result = new TemplateBindResult();
            RemoteViews remoteViews = this.mBuilder.applyStandardTemplateWithActions(this.mBuilder.getBigBaseLayoutResource(), result);
            buildIntoRemoteViewContent(remoteViews, headsUpContentView, result);
            return remoteViews;
        }

        private synchronized RemoteViews makeStandardTemplateWithCustomContent(RemoteViews customContent) {
            TemplateBindResult result = new TemplateBindResult();
            RemoteViews remoteViews = this.mBuilder.applyStandardTemplate(this.mBuilder.getBaseLayoutResource(), result);
            buildIntoRemoteViewContent(remoteViews, customContent, result);
            return remoteViews;
        }

        private synchronized RemoteViews makeDecoratedBigContentView() {
            RemoteViews bigContentView = this.mBuilder.mN.bigContentView == null ? this.mBuilder.mN.contentView : this.mBuilder.mN.bigContentView;
            if (this.mBuilder.mActions.size() == 0) {
                return makeStandardTemplateWithCustomContent(bigContentView);
            }
            TemplateBindResult result = new TemplateBindResult();
            RemoteViews remoteViews = this.mBuilder.applyStandardTemplateWithActions(this.mBuilder.getBigBaseLayoutResource(), result);
            buildIntoRemoteViewContent(remoteViews, bigContentView, result);
            return remoteViews;
        }

        private synchronized void buildIntoRemoteViewContent(RemoteViews remoteViews, RemoteViews customContent, TemplateBindResult result) {
            if (customContent != null) {
                RemoteViews customContent2 = customContent.mo11clone();
                remoteViews.removeAllViewsExceptId(R.id.notification_main_column, android.R.id.progress);
                remoteViews.addView(R.id.notification_main_column, customContent2, 0);
                remoteViews.setReapplyDisallowed();
            }
            Resources resources = this.mBuilder.mContext.getResources();
            int endMargin = resources.getDimensionPixelSize(R.dimen.notification_content_margin_end) + result.getIconMarginEnd();
            remoteViews.setViewLayoutMarginEnd(R.id.notification_main_column, endMargin);
        }

        @Override // android.app.Notification.Style
        public synchronized boolean areNotificationsVisiblyDifferent(Style other) {
            if (other == null || getClass() != other.getClass()) {
                return true;
            }
            return false;
        }
    }

    /* loaded from: classes.dex */
    public static class DecoratedMediaCustomViewStyle extends MediaStyle {
        @Override // android.app.Notification.Style
        public synchronized boolean displayCustomViewInline() {
            return true;
        }

        @Override // android.app.Notification.MediaStyle, android.app.Notification.Style
        public synchronized RemoteViews makeContentView(boolean increasedHeight) {
            RemoteViews remoteViews = super.makeContentView(false);
            return buildIntoRemoteView(remoteViews, R.id.notification_content_container, this.mBuilder.mN.contentView);
        }

        @Override // android.app.Notification.MediaStyle, android.app.Notification.Style
        public synchronized RemoteViews makeBigContentView() {
            RemoteViews customRemoteView = this.mBuilder.mN.bigContentView != null ? this.mBuilder.mN.bigContentView : this.mBuilder.mN.contentView;
            return makeBigContentViewWithCustomContent(customRemoteView);
        }

        private synchronized RemoteViews makeBigContentViewWithCustomContent(RemoteViews customRemoteView) {
            RemoteViews remoteViews = super.makeBigContentView();
            if (remoteViews == null) {
                if (customRemoteView != this.mBuilder.mN.contentView) {
                    return buildIntoRemoteView(super.makeContentView(false), R.id.notification_content_container, customRemoteView);
                }
                return null;
            }
            return buildIntoRemoteView(remoteViews, R.id.notification_main_column, customRemoteView);
        }

        @Override // android.app.Notification.MediaStyle, android.app.Notification.Style
        public synchronized RemoteViews makeHeadsUpContentView(boolean increasedHeight) {
            RemoteViews customRemoteView = this.mBuilder.mN.headsUpContentView != null ? this.mBuilder.mN.headsUpContentView : this.mBuilder.mN.contentView;
            return makeBigContentViewWithCustomContent(customRemoteView);
        }

        @Override // android.app.Notification.MediaStyle, android.app.Notification.Style
        public synchronized boolean areNotificationsVisiblyDifferent(Style other) {
            if (other == null || getClass() != other.getClass()) {
                return true;
            }
            return false;
        }

        private synchronized RemoteViews buildIntoRemoteView(RemoteViews remoteViews, int id, RemoteViews customContent) {
            if (customContent != null) {
                RemoteViews customContent2 = customContent.mo11clone();
                customContent2.overrideTextColors(this.mBuilder.getPrimaryTextColor());
                remoteViews.removeAllViews(id);
                remoteViews.addView(id, customContent2);
                remoteViews.setReapplyDisallowed();
            }
            return remoteViews;
        }
    }

    /* loaded from: classes.dex */
    public static final class WearableExtender implements Extender {
        private static final int DEFAULT_CONTENT_ICON_GRAVITY = 8388613;
        private static final int DEFAULT_FLAGS = 1;
        private static final int DEFAULT_GRAVITY = 80;
        private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
        private static final int FLAG_BIG_PICTURE_AMBIENT = 32;
        private static final int FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE = 1;
        private static final int FLAG_HINT_AVOID_BACKGROUND_CLIPPING = 16;
        private static final int FLAG_HINT_CONTENT_INTENT_LAUNCHES_ACTIVITY = 64;
        private static final int FLAG_HINT_HIDE_ICON = 2;
        private static final int FLAG_HINT_SHOW_BACKGROUND_ONLY = 4;
        private static final int FLAG_START_SCROLL_BOTTOM = 8;
        private static final String KEY_ACTIONS = "actions";
        private static final String KEY_BACKGROUND = "background";
        private static final String KEY_BRIDGE_TAG = "bridgeTag";
        private static final String KEY_CONTENT_ACTION_INDEX = "contentActionIndex";
        private static final String KEY_CONTENT_ICON = "contentIcon";
        private static final String KEY_CONTENT_ICON_GRAVITY = "contentIconGravity";
        private static final String KEY_CUSTOM_CONTENT_HEIGHT = "customContentHeight";
        private static final String KEY_CUSTOM_SIZE_PRESET = "customSizePreset";
        private static final String KEY_DISMISSAL_ID = "dismissalId";
        private static final String KEY_DISPLAY_INTENT = "displayIntent";
        private static final String KEY_FLAGS = "flags";
        private static final String KEY_GRAVITY = "gravity";
        private static final String KEY_HINT_SCREEN_TIMEOUT = "hintScreenTimeout";
        private static final String KEY_PAGES = "pages";
        public static final int SCREEN_TIMEOUT_LONG = -1;
        public static final int SCREEN_TIMEOUT_SHORT = 0;
        public static final int SIZE_DEFAULT = 0;
        public static final int SIZE_FULL_SCREEN = 5;
        public static final int SIZE_LARGE = 4;
        public static final int SIZE_MEDIUM = 3;
        public static final int SIZE_SMALL = 2;
        public static final int SIZE_XSMALL = 1;
        public static final int UNSET_ACTION_INDEX = -1;
        private ArrayList<Action> mActions;
        private Bitmap mBackground;
        private String mBridgeTag;
        private int mContentActionIndex;
        private int mContentIcon;
        private int mContentIconGravity;
        private int mCustomContentHeight;
        private int mCustomSizePreset;
        private String mDismissalId;
        private PendingIntent mDisplayIntent;
        private int mFlags;
        private int mGravity;
        private int mHintScreenTimeout;
        private ArrayList<Notification> mPages;

        public WearableExtender() {
            this.mActions = new ArrayList<>();
            this.mFlags = 1;
            this.mPages = new ArrayList<>();
            this.mContentIconGravity = 8388613;
            this.mContentActionIndex = -1;
            this.mCustomSizePreset = 0;
            this.mGravity = 80;
        }

        public WearableExtender(Notification notif) {
            this.mActions = new ArrayList<>();
            this.mFlags = 1;
            this.mPages = new ArrayList<>();
            this.mContentIconGravity = 8388613;
            this.mContentActionIndex = -1;
            this.mCustomSizePreset = 0;
            this.mGravity = 80;
            Bundle wearableBundle = notif.extras.getBundle(EXTRA_WEARABLE_EXTENSIONS);
            if (wearableBundle != null) {
                List<Action> actions = wearableBundle.getParcelableArrayList("actions");
                if (actions != null) {
                    this.mActions.addAll(actions);
                }
                this.mFlags = wearableBundle.getInt("flags", 1);
                this.mDisplayIntent = (PendingIntent) wearableBundle.getParcelable(KEY_DISPLAY_INTENT);
                Notification[] pages = Notification.getNotificationArrayFromBundle(wearableBundle, KEY_PAGES);
                if (pages != null) {
                    Collections.addAll(this.mPages, pages);
                }
                this.mBackground = (Bitmap) wearableBundle.getParcelable(KEY_BACKGROUND);
                this.mContentIcon = wearableBundle.getInt(KEY_CONTENT_ICON);
                this.mContentIconGravity = wearableBundle.getInt(KEY_CONTENT_ICON_GRAVITY, 8388613);
                this.mContentActionIndex = wearableBundle.getInt(KEY_CONTENT_ACTION_INDEX, -1);
                this.mCustomSizePreset = wearableBundle.getInt(KEY_CUSTOM_SIZE_PRESET, 0);
                this.mCustomContentHeight = wearableBundle.getInt(KEY_CUSTOM_CONTENT_HEIGHT);
                this.mGravity = wearableBundle.getInt(KEY_GRAVITY, 80);
                this.mHintScreenTimeout = wearableBundle.getInt(KEY_HINT_SCREEN_TIMEOUT);
                this.mDismissalId = wearableBundle.getString(KEY_DISMISSAL_ID);
                this.mBridgeTag = wearableBundle.getString(KEY_BRIDGE_TAG);
            }
        }

        @Override // android.app.Notification.Extender
        public Builder extend(Builder builder) {
            Bundle wearableBundle = new Bundle();
            if (!this.mActions.isEmpty()) {
                wearableBundle.putParcelableArrayList("actions", this.mActions);
            }
            if (this.mFlags != 1) {
                wearableBundle.putInt("flags", this.mFlags);
            }
            if (this.mDisplayIntent != null) {
                wearableBundle.putParcelable(KEY_DISPLAY_INTENT, this.mDisplayIntent);
            }
            if (!this.mPages.isEmpty()) {
                wearableBundle.putParcelableArray(KEY_PAGES, (Parcelable[]) this.mPages.toArray(new Notification[this.mPages.size()]));
            }
            if (this.mBackground != null) {
                wearableBundle.putParcelable(KEY_BACKGROUND, this.mBackground);
            }
            if (this.mContentIcon != 0) {
                wearableBundle.putInt(KEY_CONTENT_ICON, this.mContentIcon);
            }
            if (this.mContentIconGravity != 8388613) {
                wearableBundle.putInt(KEY_CONTENT_ICON_GRAVITY, this.mContentIconGravity);
            }
            if (this.mContentActionIndex != -1) {
                wearableBundle.putInt(KEY_CONTENT_ACTION_INDEX, this.mContentActionIndex);
            }
            if (this.mCustomSizePreset != 0) {
                wearableBundle.putInt(KEY_CUSTOM_SIZE_PRESET, this.mCustomSizePreset);
            }
            if (this.mCustomContentHeight != 0) {
                wearableBundle.putInt(KEY_CUSTOM_CONTENT_HEIGHT, this.mCustomContentHeight);
            }
            if (this.mGravity != 80) {
                wearableBundle.putInt(KEY_GRAVITY, this.mGravity);
            }
            if (this.mHintScreenTimeout != 0) {
                wearableBundle.putInt(KEY_HINT_SCREEN_TIMEOUT, this.mHintScreenTimeout);
            }
            if (this.mDismissalId != null) {
                wearableBundle.putString(KEY_DISMISSAL_ID, this.mDismissalId);
            }
            if (this.mBridgeTag != null) {
                wearableBundle.putString(KEY_BRIDGE_TAG, this.mBridgeTag);
            }
            builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, wearableBundle);
            return builder;
        }

        /* renamed from: clone */
        public WearableExtender m12clone() {
            WearableExtender that = new WearableExtender();
            that.mActions = new ArrayList<>(this.mActions);
            that.mFlags = this.mFlags;
            that.mDisplayIntent = this.mDisplayIntent;
            that.mPages = new ArrayList<>(this.mPages);
            that.mBackground = this.mBackground;
            that.mContentIcon = this.mContentIcon;
            that.mContentIconGravity = this.mContentIconGravity;
            that.mContentActionIndex = this.mContentActionIndex;
            that.mCustomSizePreset = this.mCustomSizePreset;
            that.mCustomContentHeight = this.mCustomContentHeight;
            that.mGravity = this.mGravity;
            that.mHintScreenTimeout = this.mHintScreenTimeout;
            that.mDismissalId = this.mDismissalId;
            that.mBridgeTag = this.mBridgeTag;
            return that;
        }

        public WearableExtender addAction(Action action) {
            this.mActions.add(action);
            return this;
        }

        public WearableExtender addActions(List<Action> actions) {
            this.mActions.addAll(actions);
            return this;
        }

        public WearableExtender clearActions() {
            this.mActions.clear();
            return this;
        }

        public List<Action> getActions() {
            return this.mActions;
        }

        public WearableExtender setDisplayIntent(PendingIntent intent) {
            this.mDisplayIntent = intent;
            return this;
        }

        public PendingIntent getDisplayIntent() {
            return this.mDisplayIntent;
        }

        public WearableExtender addPage(Notification page) {
            this.mPages.add(page);
            return this;
        }

        public WearableExtender addPages(List<Notification> pages) {
            this.mPages.addAll(pages);
            return this;
        }

        public WearableExtender clearPages() {
            this.mPages.clear();
            return this;
        }

        public List<Notification> getPages() {
            return this.mPages;
        }

        public WearableExtender setBackground(Bitmap background) {
            this.mBackground = background;
            return this;
        }

        public Bitmap getBackground() {
            return this.mBackground;
        }

        @Deprecated
        public WearableExtender setContentIcon(int icon) {
            this.mContentIcon = icon;
            return this;
        }

        @Deprecated
        public int getContentIcon() {
            return this.mContentIcon;
        }

        @Deprecated
        public WearableExtender setContentIconGravity(int contentIconGravity) {
            this.mContentIconGravity = contentIconGravity;
            return this;
        }

        @Deprecated
        public int getContentIconGravity() {
            return this.mContentIconGravity;
        }

        public WearableExtender setContentAction(int actionIndex) {
            this.mContentActionIndex = actionIndex;
            return this;
        }

        public int getContentAction() {
            return this.mContentActionIndex;
        }

        @Deprecated
        public WearableExtender setGravity(int gravity) {
            this.mGravity = gravity;
            return this;
        }

        @Deprecated
        public int getGravity() {
            return this.mGravity;
        }

        @Deprecated
        public WearableExtender setCustomSizePreset(int sizePreset) {
            this.mCustomSizePreset = sizePreset;
            return this;
        }

        @Deprecated
        public int getCustomSizePreset() {
            return this.mCustomSizePreset;
        }

        @Deprecated
        public WearableExtender setCustomContentHeight(int height) {
            this.mCustomContentHeight = height;
            return this;
        }

        @Deprecated
        public int getCustomContentHeight() {
            return this.mCustomContentHeight;
        }

        public WearableExtender setStartScrollBottom(boolean startScrollBottom) {
            setFlag(8, startScrollBottom);
            return this;
        }

        public boolean getStartScrollBottom() {
            return (this.mFlags & 8) != 0;
        }

        public WearableExtender setContentIntentAvailableOffline(boolean contentIntentAvailableOffline) {
            setFlag(1, contentIntentAvailableOffline);
            return this;
        }

        public boolean getContentIntentAvailableOffline() {
            return (this.mFlags & 1) != 0;
        }

        @Deprecated
        public WearableExtender setHintHideIcon(boolean hintHideIcon) {
            setFlag(2, hintHideIcon);
            return this;
        }

        @Deprecated
        public boolean getHintHideIcon() {
            return (this.mFlags & 2) != 0;
        }

        @Deprecated
        public WearableExtender setHintShowBackgroundOnly(boolean hintShowBackgroundOnly) {
            setFlag(4, hintShowBackgroundOnly);
            return this;
        }

        @Deprecated
        public boolean getHintShowBackgroundOnly() {
            return (this.mFlags & 4) != 0;
        }

        @Deprecated
        public WearableExtender setHintAvoidBackgroundClipping(boolean hintAvoidBackgroundClipping) {
            setFlag(16, hintAvoidBackgroundClipping);
            return this;
        }

        @Deprecated
        public boolean getHintAvoidBackgroundClipping() {
            return (this.mFlags & 16) != 0;
        }

        @Deprecated
        public WearableExtender setHintScreenTimeout(int timeout) {
            this.mHintScreenTimeout = timeout;
            return this;
        }

        @Deprecated
        public int getHintScreenTimeout() {
            return this.mHintScreenTimeout;
        }

        public WearableExtender setHintAmbientBigPicture(boolean hintAmbientBigPicture) {
            setFlag(32, hintAmbientBigPicture);
            return this;
        }

        public boolean getHintAmbientBigPicture() {
            return (this.mFlags & 32) != 0;
        }

        public WearableExtender setHintContentIntentLaunchesActivity(boolean hintContentIntentLaunchesActivity) {
            setFlag(64, hintContentIntentLaunchesActivity);
            return this;
        }

        public boolean getHintContentIntentLaunchesActivity() {
            return (this.mFlags & 64) != 0;
        }

        public WearableExtender setDismissalId(String dismissalId) {
            this.mDismissalId = dismissalId;
            return this;
        }

        public String getDismissalId() {
            return this.mDismissalId;
        }

        public WearableExtender setBridgeTag(String bridgeTag) {
            this.mBridgeTag = bridgeTag;
            return this;
        }

        public String getBridgeTag() {
            return this.mBridgeTag;
        }

        private synchronized void setFlag(int mask, boolean value) {
            if (value) {
                this.mFlags |= mask;
            } else {
                this.mFlags &= ~mask;
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class CarExtender implements Extender {
        private static final String EXTRA_CAR_EXTENDER = "android.car.EXTENSIONS";
        private static final String EXTRA_COLOR = "app_color";
        private static final String EXTRA_CONVERSATION = "car_conversation";
        private static final String EXTRA_LARGE_ICON = "large_icon";
        private static final String TAG = "CarExtender";
        private int mColor;
        private Bitmap mLargeIcon;
        private UnreadConversation mUnreadConversation;

        public CarExtender() {
            this.mColor = 0;
        }

        public CarExtender(Notification notif) {
            this.mColor = 0;
            Bundle carBundle = notif.extras == null ? null : notif.extras.getBundle(EXTRA_CAR_EXTENDER);
            if (carBundle != null) {
                this.mLargeIcon = (Bitmap) carBundle.getParcelable(EXTRA_LARGE_ICON);
                this.mColor = carBundle.getInt(EXTRA_COLOR, 0);
                Bundle b = carBundle.getBundle(EXTRA_CONVERSATION);
                this.mUnreadConversation = UnreadConversation.getUnreadConversationFromBundle(b);
            }
        }

        @Override // android.app.Notification.Extender
        public Builder extend(Builder builder) {
            Bundle carExtensions = new Bundle();
            if (this.mLargeIcon != null) {
                carExtensions.putParcelable(EXTRA_LARGE_ICON, this.mLargeIcon);
            }
            if (this.mColor != 0) {
                carExtensions.putInt(EXTRA_COLOR, this.mColor);
            }
            if (this.mUnreadConversation != null) {
                Bundle b = this.mUnreadConversation.getBundleForUnreadConversation();
                carExtensions.putBundle(EXTRA_CONVERSATION, b);
            }
            Bundle b2 = builder.getExtras();
            b2.putBundle(EXTRA_CAR_EXTENDER, carExtensions);
            return builder;
        }

        public CarExtender setColor(int color) {
            this.mColor = color;
            return this;
        }

        public int getColor() {
            return this.mColor;
        }

        public CarExtender setLargeIcon(Bitmap largeIcon) {
            this.mLargeIcon = largeIcon;
            return this;
        }

        public Bitmap getLargeIcon() {
            return this.mLargeIcon;
        }

        public CarExtender setUnreadConversation(UnreadConversation unreadConversation) {
            this.mUnreadConversation = unreadConversation;
            return this;
        }

        public UnreadConversation getUnreadConversation() {
            return this.mUnreadConversation;
        }

        /* loaded from: classes.dex */
        public static class UnreadConversation {
            private static final String KEY_AUTHOR = "author";
            private static final String KEY_MESSAGES = "messages";
            private static final String KEY_ON_READ = "on_read";
            private static final String KEY_ON_REPLY = "on_reply";
            private static final String KEY_PARTICIPANTS = "participants";
            private static final String KEY_REMOTE_INPUT = "remote_input";
            private static final String KEY_TEXT = "text";
            private static final String KEY_TIMESTAMP = "timestamp";
            private final long mLatestTimestamp;
            private final String[] mMessages;
            private final String[] mParticipants;
            private final PendingIntent mReadPendingIntent;
            private final RemoteInput mRemoteInput;
            private final PendingIntent mReplyPendingIntent;

            synchronized UnreadConversation(String[] messages, RemoteInput remoteInput, PendingIntent replyPendingIntent, PendingIntent readPendingIntent, String[] participants, long latestTimestamp) {
                this.mMessages = messages;
                this.mRemoteInput = remoteInput;
                this.mReadPendingIntent = readPendingIntent;
                this.mReplyPendingIntent = replyPendingIntent;
                this.mParticipants = participants;
                this.mLatestTimestamp = latestTimestamp;
            }

            public String[] getMessages() {
                return this.mMessages;
            }

            public RemoteInput getRemoteInput() {
                return this.mRemoteInput;
            }

            public PendingIntent getReplyPendingIntent() {
                return this.mReplyPendingIntent;
            }

            public PendingIntent getReadPendingIntent() {
                return this.mReadPendingIntent;
            }

            public String[] getParticipants() {
                return this.mParticipants;
            }

            public String getParticipant() {
                if (this.mParticipants.length > 0) {
                    return this.mParticipants[0];
                }
                return null;
            }

            public long getLatestTimestamp() {
                return this.mLatestTimestamp;
            }

            synchronized Bundle getBundleForUnreadConversation() {
                Bundle b = new Bundle();
                String author = null;
                if (this.mParticipants != null && this.mParticipants.length > 1) {
                    author = this.mParticipants[0];
                }
                Parcelable[] messages = new Parcelable[this.mMessages.length];
                for (int i = 0; i < messages.length; i++) {
                    Bundle m = new Bundle();
                    m.putString("text", this.mMessages[i]);
                    m.putString(KEY_AUTHOR, author);
                    messages[i] = m;
                }
                b.putParcelableArray(KEY_MESSAGES, messages);
                if (this.mRemoteInput != null) {
                    b.putParcelable(KEY_REMOTE_INPUT, this.mRemoteInput);
                }
                b.putParcelable(KEY_ON_REPLY, this.mReplyPendingIntent);
                b.putParcelable(KEY_ON_READ, this.mReadPendingIntent);
                b.putStringArray(KEY_PARTICIPANTS, this.mParticipants);
                b.putLong(KEY_TIMESTAMP, this.mLatestTimestamp);
                return b;
            }

            static synchronized UnreadConversation getUnreadConversationFromBundle(Bundle b) {
                if (b == null) {
                    return null;
                }
                Parcelable[] parcelableMessages = b.getParcelableArray(KEY_MESSAGES);
                String[] messages = null;
                if (parcelableMessages != null) {
                    String[] tmp = new String[parcelableMessages.length];
                    boolean success = true;
                    int i = 0;
                    while (true) {
                        if (i >= tmp.length) {
                            break;
                        } else if (!(parcelableMessages[i] instanceof Bundle)) {
                            success = false;
                            break;
                        } else {
                            tmp[i] = ((Bundle) parcelableMessages[i]).getString("text");
                            if (tmp[i] != null) {
                                i++;
                            } else {
                                success = false;
                                break;
                            }
                        }
                    }
                    if (!success) {
                        return null;
                    }
                    messages = tmp;
                }
                PendingIntent onRead = (PendingIntent) b.getParcelable(KEY_ON_READ);
                PendingIntent onReply = (PendingIntent) b.getParcelable(KEY_ON_REPLY);
                RemoteInput remoteInput = (RemoteInput) b.getParcelable(KEY_REMOTE_INPUT);
                String[] participants = b.getStringArray(KEY_PARTICIPANTS);
                if (participants == null || participants.length != 1) {
                    return null;
                }
                return new UnreadConversation(messages, remoteInput, onReply, onRead, participants, b.getLong(KEY_TIMESTAMP));
            }
        }

        /* loaded from: classes.dex */
        public static class Builder {
            private long mLatestTimestamp;
            private final List<String> mMessages = new ArrayList();
            private final String mParticipant;
            private PendingIntent mReadPendingIntent;
            private RemoteInput mRemoteInput;
            private PendingIntent mReplyPendingIntent;

            public Builder(String name) {
                this.mParticipant = name;
            }

            public Builder addMessage(String message) {
                this.mMessages.add(message);
                return this;
            }

            public Builder setReplyAction(PendingIntent pendingIntent, RemoteInput remoteInput) {
                this.mRemoteInput = remoteInput;
                this.mReplyPendingIntent = pendingIntent;
                return this;
            }

            public Builder setReadPendingIntent(PendingIntent pendingIntent) {
                this.mReadPendingIntent = pendingIntent;
                return this;
            }

            public Builder setLatestTimestamp(long timestamp) {
                this.mLatestTimestamp = timestamp;
                return this;
            }

            public UnreadConversation build() {
                String[] messages = (String[]) this.mMessages.toArray(new String[this.mMessages.size()]);
                String[] participants = {this.mParticipant};
                return new UnreadConversation(messages, this.mRemoteInput, this.mReplyPendingIntent, this.mReadPendingIntent, participants, this.mLatestTimestamp);
            }
        }
    }

    @SystemApi
    /* loaded from: classes.dex */
    public static final class TvExtender implements Extender {
        private static final String EXTRA_CHANNEL_ID = "channel_id";
        private static final String EXTRA_CONTENT_INTENT = "content_intent";
        private static final String EXTRA_DELETE_INTENT = "delete_intent";
        private static final String EXTRA_FLAGS = "flags";
        private static final String EXTRA_SUPPRESS_SHOW_OVER_APPS = "suppressShowOverApps";
        private static final String EXTRA_TV_EXTENDER = "android.tv.EXTENSIONS";
        private static final int FLAG_AVAILABLE_ON_TV = 1;
        private static final String TAG = "TvExtender";
        private String mChannelId;
        private PendingIntent mContentIntent;
        private PendingIntent mDeleteIntent;
        private int mFlags;
        private boolean mSuppressShowOverApps;

        public TvExtender() {
            this.mFlags = 1;
        }

        public TvExtender(Notification notif) {
            Bundle bundle = notif.extras == null ? null : notif.extras.getBundle(EXTRA_TV_EXTENDER);
            if (bundle != null) {
                this.mFlags = bundle.getInt("flags");
                this.mChannelId = bundle.getString("channel_id");
                this.mSuppressShowOverApps = bundle.getBoolean(EXTRA_SUPPRESS_SHOW_OVER_APPS);
                this.mContentIntent = (PendingIntent) bundle.getParcelable(EXTRA_CONTENT_INTENT);
                this.mDeleteIntent = (PendingIntent) bundle.getParcelable(EXTRA_DELETE_INTENT);
            }
        }

        @Override // android.app.Notification.Extender
        public Builder extend(Builder builder) {
            Bundle bundle = new Bundle();
            bundle.putInt("flags", this.mFlags);
            bundle.putString("channel_id", this.mChannelId);
            bundle.putBoolean(EXTRA_SUPPRESS_SHOW_OVER_APPS, this.mSuppressShowOverApps);
            if (this.mContentIntent != null) {
                bundle.putParcelable(EXTRA_CONTENT_INTENT, this.mContentIntent);
            }
            if (this.mDeleteIntent != null) {
                bundle.putParcelable(EXTRA_DELETE_INTENT, this.mDeleteIntent);
            }
            builder.getExtras().putBundle(EXTRA_TV_EXTENDER, bundle);
            return builder;
        }

        public boolean isAvailableOnTv() {
            return (this.mFlags & 1) != 0;
        }

        public TvExtender setChannel(String channelId) {
            this.mChannelId = channelId;
            return this;
        }

        public TvExtender setChannelId(String channelId) {
            this.mChannelId = channelId;
            return this;
        }

        @Deprecated
        private protected String getChannel() {
            return this.mChannelId;
        }

        public String getChannelId() {
            return this.mChannelId;
        }

        public TvExtender setContentIntent(PendingIntent intent) {
            this.mContentIntent = intent;
            return this;
        }

        public PendingIntent getContentIntent() {
            return this.mContentIntent;
        }

        public TvExtender setDeleteIntent(PendingIntent intent) {
            this.mDeleteIntent = intent;
            return this;
        }

        public PendingIntent getDeleteIntent() {
            return this.mDeleteIntent;
        }

        public TvExtender setSuppressShowOverApps(boolean suppress) {
            this.mSuppressShowOverApps = suppress;
            return this;
        }

        public boolean getSuppressShowOverApps() {
            return this.mSuppressShowOverApps;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized Notification[] getNotificationArrayFromBundle(Bundle bundle, String key) {
        Parcelable[] array = bundle.getParcelableArray(key);
        if ((array instanceof Notification[]) || array == null) {
            return (Notification[]) array;
        }
        Notification[] typedArray = (Notification[]) Arrays.copyOf(array, array.length, Notification[].class);
        bundle.putParcelableArray(key, typedArray);
        return typedArray;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class BuilderRemoteViews extends RemoteViews {
        public synchronized BuilderRemoteViews(Parcel parcel) {
            super(parcel);
        }

        public synchronized BuilderRemoteViews(ApplicationInfo appInfo, int layoutId) {
            super(appInfo, layoutId);
        }

        @Override // android.widget.RemoteViews
        /* renamed from: clone */
        public BuilderRemoteViews mo11clone() {
            Parcel p = Parcel.obtain();
            writeToParcel(p, 0);
            p.setDataPosition(0);
            BuilderRemoteViews brv = new BuilderRemoteViews(p);
            p.recycle();
            return brv;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TemplateBindResult {
        int mIconMarginEnd;

        private synchronized TemplateBindResult() {
        }

        public synchronized int getIconMarginEnd() {
            return this.mIconMarginEnd;
        }

        public synchronized void setIconMarginEnd(int iconMarginEnd) {
            this.mIconMarginEnd = iconMarginEnd;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class StandardTemplateParams {
        boolean ambient;
        boolean hasProgress;
        CharSequence headerTextSecondary;
        boolean hideLargeIcon;
        boolean hideReplyIcon;
        int maxRemoteInputHistory;
        CharSequence text;
        CharSequence title;

        private synchronized StandardTemplateParams() {
            this.hasProgress = true;
            this.ambient = false;
            this.maxRemoteInputHistory = 3;
        }

        final synchronized StandardTemplateParams reset() {
            this.hasProgress = true;
            this.ambient = false;
            this.title = null;
            this.text = null;
            this.headerTextSecondary = null;
            this.maxRemoteInputHistory = 3;
            return this;
        }

        final synchronized StandardTemplateParams hasProgress(boolean hasProgress) {
            this.hasProgress = hasProgress;
            return this;
        }

        final synchronized StandardTemplateParams title(CharSequence title) {
            this.title = title;
            return this;
        }

        final synchronized StandardTemplateParams text(CharSequence text) {
            this.text = text;
            return this;
        }

        final synchronized StandardTemplateParams headerTextSecondary(CharSequence text) {
            this.headerTextSecondary = text;
            return this;
        }

        final synchronized StandardTemplateParams hideLargeIcon(boolean hideLargeIcon) {
            this.hideLargeIcon = hideLargeIcon;
            return this;
        }

        final synchronized StandardTemplateParams hideReplyIcon(boolean hideReplyIcon) {
            this.hideReplyIcon = hideReplyIcon;
            return this;
        }

        final synchronized StandardTemplateParams ambient(boolean ambient) {
            Preconditions.checkState(this.title == null && this.text == null, "must set ambient before text");
            this.ambient = ambient;
            return this;
        }

        final synchronized StandardTemplateParams fillTextsFrom(Builder b) {
            Bundle extras = b.mN.extras;
            this.title = b.processLegacyText(extras.getCharSequence(Notification.EXTRA_TITLE), this.ambient);
            CharSequence text = extras.getCharSequence(Notification.EXTRA_BIG_TEXT);
            if (!this.ambient || TextUtils.isEmpty(text)) {
                text = extras.getCharSequence(Notification.EXTRA_TEXT);
            }
            this.text = b.processLegacyText(text, this.ambient);
            return this;
        }

        public synchronized StandardTemplateParams setMaxRemoteInputHistory(int maxRemoteInputHistory) {
            this.maxRemoteInputHistory = maxRemoteInputHistory;
            return this;
        }
    }
}
