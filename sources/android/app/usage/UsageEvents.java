package android.app.usage;

import android.annotation.SystemApi;
import android.content.res.Configuration;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public final class UsageEvents implements Parcelable {
    public static final Parcelable.Creator<UsageEvents> CREATOR = new Parcelable.Creator<UsageEvents>() { // from class: android.app.usage.UsageEvents.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UsageEvents createFromParcel(Parcel source) {
            return new UsageEvents(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UsageEvents[] newArray(int size) {
            return new UsageEvents[size];
        }
    };
    public static final String INSTANT_APP_CLASS_NAME = "android.instant_class";
    public static final String INSTANT_APP_PACKAGE_NAME = "android.instant_app";
    public protected final int mEventCount;
    public protected List<Event> mEventsToWrite;
    public protected int mIndex;
    public protected Parcel mParcel;
    public protected String[] mStringPool;

    /* loaded from: classes.dex */
    public static final class Event {
        public static final int CHOOSER_ACTION = 9;
        public static final int CONFIGURATION_CHANGE = 5;
        public static final int CONTINUE_PREVIOUS_DAY = 4;
        public static final int END_OF_DAY = 3;
        public static final int FLAG_IS_PACKAGE_INSTANT_APP = 1;
        public static final int KEYGUARD_HIDDEN = 18;
        public static final int KEYGUARD_SHOWN = 17;
        public static final int MOVE_TO_BACKGROUND = 2;
        public static final int MOVE_TO_FOREGROUND = 1;
        public static final int NONE = 0;
        @SystemApi
        public static final int NOTIFICATION_INTERRUPTION = 12;
        @SystemApi
        public static final int NOTIFICATION_SEEN = 10;
        public static final int SCREEN_INTERACTIVE = 15;
        public static final int SCREEN_NON_INTERACTIVE = 16;
        public static final int SHORTCUT_INVOCATION = 8;
        @SystemApi
        public static final int SLICE_PINNED = 14;
        @SystemApi
        public static final int SLICE_PINNED_PRIV = 13;
        public static final int STANDBY_BUCKET_CHANGED = 11;
        @SystemApi
        public static final int SYSTEM_INTERACTION = 6;
        public static final int USER_INTERACTION = 7;
        public String mAction;
        public int mBucketAndReason;
        private protected String mClass;
        private protected Configuration mConfiguration;
        public String[] mContentAnnotations;
        public String mContentType;
        private protected int mEventType;
        public int mFlags;
        public String mNotificationChannelId;
        private protected String mPackage;
        public String mShortcutId;
        private protected long mTimeStamp;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes.dex */
        public @interface EventFlags {
        }

        public Event() {
        }

        public synchronized Event(Event orig) {
            this.mPackage = orig.mPackage;
            this.mClass = orig.mClass;
            this.mTimeStamp = orig.mTimeStamp;
            this.mEventType = orig.mEventType;
            this.mConfiguration = orig.mConfiguration;
            this.mShortcutId = orig.mShortcutId;
            this.mAction = orig.mAction;
            this.mContentType = orig.mContentType;
            this.mContentAnnotations = orig.mContentAnnotations;
            this.mFlags = orig.mFlags;
            this.mBucketAndReason = orig.mBucketAndReason;
            this.mNotificationChannelId = orig.mNotificationChannelId;
        }

        public String getPackageName() {
            return this.mPackage;
        }

        public String getClassName() {
            return this.mClass;
        }

        public long getTimeStamp() {
            return this.mTimeStamp;
        }

        public int getEventType() {
            return this.mEventType;
        }

        public Configuration getConfiguration() {
            return this.mConfiguration;
        }

        public String getShortcutId() {
            return this.mShortcutId;
        }

        public synchronized int getStandbyBucket() {
            return (this.mBucketAndReason & (-65536)) >>> 16;
        }

        public int getAppStandbyBucket() {
            return (this.mBucketAndReason & (-65536)) >>> 16;
        }

        public synchronized int getStandbyReason() {
            return this.mBucketAndReason & 65535;
        }

        @SystemApi
        public String getNotificationChannelId() {
            return this.mNotificationChannelId;
        }

        public synchronized Event getObfuscatedIfInstantApp() {
            if ((this.mFlags & 1) == 0) {
                return this;
            }
            Event ret = new Event(this);
            ret.mPackage = UsageEvents.INSTANT_APP_PACKAGE_NAME;
            ret.mClass = UsageEvents.INSTANT_APP_CLASS_NAME;
            return ret;
        }
    }

    private protected UsageEvents(Parcel in) {
        this.mEventsToWrite = null;
        this.mParcel = null;
        this.mIndex = 0;
        byte[] bytes = in.readBlob();
        Parcel data = Parcel.obtain();
        data.unmarshall(bytes, 0, bytes.length);
        data.setDataPosition(0);
        this.mEventCount = data.readInt();
        this.mIndex = data.readInt();
        if (this.mEventCount > 0) {
            this.mStringPool = data.createStringArray();
            int listByteLength = data.readInt();
            int positionInParcel = data.readInt();
            this.mParcel = Parcel.obtain();
            this.mParcel.setDataPosition(0);
            this.mParcel.appendFrom(data, data.dataPosition(), listByteLength);
            this.mParcel.setDataSize(this.mParcel.dataPosition());
            this.mParcel.setDataPosition(positionInParcel);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized UsageEvents() {
        this.mEventsToWrite = null;
        this.mParcel = null;
        this.mIndex = 0;
        this.mEventCount = 0;
    }

    public synchronized UsageEvents(List<Event> events, String[] stringPool) {
        this.mEventsToWrite = null;
        this.mParcel = null;
        this.mIndex = 0;
        this.mStringPool = stringPool;
        this.mEventCount = events.size();
        this.mEventsToWrite = events;
    }

    public boolean hasNextEvent() {
        return this.mIndex < this.mEventCount;
    }

    public boolean getNextEvent(Event eventOut) {
        if (this.mIndex >= this.mEventCount) {
            return false;
        }
        readEventFromParcel(this.mParcel, eventOut);
        this.mIndex++;
        if (this.mIndex >= this.mEventCount) {
            this.mParcel.recycle();
            this.mParcel = null;
        }
        return true;
    }

    public synchronized void resetToStart() {
        this.mIndex = 0;
        if (this.mParcel != null) {
            this.mParcel.setDataPosition(0);
        }
    }

    public protected int findStringIndex(String str) {
        int index = Arrays.binarySearch(this.mStringPool, str);
        if (index < 0) {
            throw new IllegalStateException("String '" + str + "' is not in the string pool");
        }
        return index;
    }

    public protected void writeEventToParcel(Event event, Parcel p, int flags) {
        int packageIndex;
        if (event.mPackage != null) {
            packageIndex = findStringIndex(event.mPackage);
        } else {
            packageIndex = -1;
        }
        int classIndex = event.mClass != null ? findStringIndex(event.mClass) : -1;
        p.writeInt(packageIndex);
        p.writeInt(classIndex);
        p.writeInt(event.mEventType);
        p.writeLong(event.mTimeStamp);
        switch (event.mEventType) {
            case 5:
                event.mConfiguration.writeToParcel(p, flags);
                return;
            case 6:
            case 7:
            case 10:
            default:
                return;
            case 8:
                p.writeString(event.mShortcutId);
                return;
            case 9:
                p.writeString(event.mAction);
                p.writeString(event.mContentType);
                p.writeStringArray(event.mContentAnnotations);
                return;
            case 11:
                p.writeInt(event.mBucketAndReason);
                return;
            case 12:
                p.writeString(event.mNotificationChannelId);
                return;
        }
    }

    public protected void readEventFromParcel(Parcel p, Event eventOut) {
        int packageIndex = p.readInt();
        if (packageIndex >= 0) {
            eventOut.mPackage = this.mStringPool[packageIndex];
        } else {
            eventOut.mPackage = null;
        }
        int classIndex = p.readInt();
        if (classIndex >= 0) {
            eventOut.mClass = this.mStringPool[classIndex];
        } else {
            eventOut.mClass = null;
        }
        eventOut.mEventType = p.readInt();
        eventOut.mTimeStamp = p.readLong();
        eventOut.mConfiguration = null;
        eventOut.mShortcutId = null;
        eventOut.mAction = null;
        eventOut.mContentType = null;
        eventOut.mContentAnnotations = null;
        eventOut.mNotificationChannelId = null;
        switch (eventOut.mEventType) {
            case 5:
                eventOut.mConfiguration = Configuration.CREATOR.createFromParcel(p);
                return;
            case 6:
            case 7:
            case 10:
            default:
                return;
            case 8:
                eventOut.mShortcutId = p.readString();
                return;
            case 9:
                eventOut.mAction = p.readString();
                eventOut.mContentType = p.readString();
                eventOut.mContentAnnotations = p.createStringArray();
                return;
            case 11:
                eventOut.mBucketAndReason = p.readInt();
                return;
            case 12:
                eventOut.mNotificationChannelId = p.readString();
                return;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        Parcel data = Parcel.obtain();
        data.writeInt(this.mEventCount);
        data.writeInt(this.mIndex);
        if (this.mEventCount > 0) {
            data.writeStringArray(this.mStringPool);
            if (this.mEventsToWrite != null) {
                Parcel p = Parcel.obtain();
                try {
                    p.setDataPosition(0);
                    for (int i = 0; i < this.mEventCount; i++) {
                        Event event = this.mEventsToWrite.get(i);
                        writeEventToParcel(event, p, flags);
                    }
                    int listByteLength = p.dataPosition();
                    data.writeInt(listByteLength);
                    data.writeInt(0);
                    data.appendFrom(p, 0, listByteLength);
                } finally {
                    p.recycle();
                }
            } else if (this.mParcel != null) {
                data.writeInt(this.mParcel.dataSize());
                data.writeInt(this.mParcel.dataPosition());
                data.appendFrom(this.mParcel, 0, this.mParcel.dataSize());
            } else {
                throw new IllegalStateException("Either mParcel or mEventsToWrite must not be null");
            }
        }
        dest.writeBlob(data.marshall());
    }
}
