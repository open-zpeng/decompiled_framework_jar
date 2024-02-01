package android.telecom.Logging;

import android.os.Parcel;
import android.os.Parcelable;
import android.telecom.Log;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class Session {
    public static final String CONTINUE_SUBSESSION = "CONTINUE_SUBSESSION";
    public static final String CREATE_SUBSESSION = "CREATE_SUBSESSION";
    public static final String END_SESSION = "END_SESSION";
    public static final String END_SUBSESSION = "END_SUBSESSION";
    public static final String EXTERNAL_INDICATOR = "E-";
    public static final String LOG_TAG = "Session";
    private static final int SESSION_RECURSION_LIMIT = 25;
    public static final String SESSION_SEPARATION_CHAR_CHILD = "_";
    public static final String START_EXTERNAL_SESSION = "START_EXTERNAL_SESSION";
    public static final String START_SESSION = "START_SESSION";
    public static final String SUBSESSION_SEPARATION_CHAR = "->";
    public static final String TRUNCATE_STRING = "...";
    public static final int UNDEFINED = -1;
    private ArrayList<Session> mChildSessions;
    private long mExecutionStartTimeMs;
    private String mFullMethodPathCache;
    private boolean mIsStartedFromActiveSession;
    private String mOwnerInfo;
    private Session mParentSession;
    private String mSessionId;
    private String mShortMethodName;
    private long mExecutionEndTimeMs = -1;
    private boolean mIsCompleted = false;
    private boolean mIsExternal = false;
    private int mChildCounter = 0;

    /* loaded from: classes2.dex */
    public static class Info implements Parcelable {
        public static final Parcelable.Creator<Info> CREATOR = new Parcelable.Creator<Info>() { // from class: android.telecom.Logging.Session.Info.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Info createFromParcel(Parcel source) {
                String id = source.readString();
                String methodName = source.readString();
                return new Info(id, methodName);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Info[] newArray(int size) {
                return new Info[size];
            }
        };
        public final String methodPath;
        public final String sessionId;

        private Info(String id, String path) {
            this.sessionId = id;
            this.methodPath = path;
        }

        public static Info getInfo(Session s) {
            return new Info(s.getFullSessionId(), s.getFullMethodPath(!Log.DEBUG && s.isSessionExternal()));
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel destination, int flags) {
            destination.writeString(this.sessionId);
            destination.writeString(this.methodPath);
        }
    }

    public Session(String sessionId, String shortMethodName, long startTimeMs, boolean isStartedFromActiveSession, String ownerInfo) {
        this.mIsStartedFromActiveSession = false;
        setSessionId(sessionId);
        setShortMethodName(shortMethodName);
        this.mExecutionStartTimeMs = startTimeMs;
        this.mParentSession = null;
        this.mChildSessions = new ArrayList<>(5);
        this.mIsStartedFromActiveSession = isStartedFromActiveSession;
        this.mOwnerInfo = ownerInfo;
    }

    public void setSessionId(String sessionId) {
        if (sessionId == null) {
            this.mSessionId = "?";
        }
        this.mSessionId = sessionId;
    }

    public String getShortMethodName() {
        return this.mShortMethodName;
    }

    public void setShortMethodName(String shortMethodName) {
        if (shortMethodName == null) {
            shortMethodName = "";
        }
        this.mShortMethodName = shortMethodName;
    }

    public void setIsExternal(boolean isExternal) {
        this.mIsExternal = isExternal;
    }

    public boolean isExternal() {
        return this.mIsExternal;
    }

    public void setParentSession(Session parentSession) {
        this.mParentSession = parentSession;
    }

    public void addChild(Session childSession) {
        if (childSession != null) {
            this.mChildSessions.add(childSession);
        }
    }

    public void removeChild(Session child) {
        if (child != null) {
            this.mChildSessions.remove(child);
        }
    }

    public long getExecutionStartTimeMilliseconds() {
        return this.mExecutionStartTimeMs;
    }

    public void setExecutionStartTimeMs(long startTimeMs) {
        this.mExecutionStartTimeMs = startTimeMs;
    }

    public Session getParentSession() {
        return this.mParentSession;
    }

    public ArrayList<Session> getChildSessions() {
        return this.mChildSessions;
    }

    public boolean isSessionCompleted() {
        return this.mIsCompleted;
    }

    public boolean isStartedFromActiveSession() {
        return this.mIsStartedFromActiveSession;
    }

    public Info getInfo() {
        return Info.getInfo(this);
    }

    @VisibleForTesting
    public String getSessionId() {
        return this.mSessionId;
    }

    public void markSessionCompleted(long executionEndTimeMs) {
        this.mExecutionEndTimeMs = executionEndTimeMs;
        this.mIsCompleted = true;
    }

    public long getLocalExecutionTime() {
        long j = this.mExecutionEndTimeMs;
        if (j == -1) {
            return -1L;
        }
        return j - this.mExecutionStartTimeMs;
    }

    public synchronized String getNextChildId() {
        int i;
        i = this.mChildCounter;
        this.mChildCounter = i + 1;
        return String.valueOf(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getFullSessionId() {
        return getFullSessionId(0);
    }

    private String getFullSessionId(int parentCount) {
        if (parentCount >= 25) {
            Log.w(LOG_TAG, "getFullSessionId: Hit recursion limit!", new Object[0]);
            return TRUNCATE_STRING + this.mSessionId;
        }
        Session parentSession = this.mParentSession;
        if (parentSession == null) {
            return this.mSessionId;
        }
        if (Log.VERBOSE) {
            return parentSession.getFullSessionId(parentCount + 1) + SESSION_SEPARATION_CHAR_CHILD + this.mSessionId;
        }
        return parentSession.getFullSessionId(parentCount + 1);
    }

    private Session getRootSession(String callingMethod) {
        int currParentCount = 0;
        Session topNode = this;
        while (true) {
            if (topNode.getParentSession() == null) {
                break;
            } else if (currParentCount >= 25) {
                Log.w(LOG_TAG, "getRootSession: Hit recursion limit from " + callingMethod, new Object[0]);
                break;
            } else {
                topNode = topNode.getParentSession();
                currParentCount++;
            }
        }
        return topNode;
    }

    public String printFullSessionTree() {
        return getRootSession("printFullSessionTree").printSessionTree();
    }

    private String printSessionTree() {
        StringBuilder sb = new StringBuilder();
        printSessionTree(0, sb, 0);
        return sb.toString();
    }

    private void printSessionTree(int tabI, StringBuilder sb, int currChildCount) {
        if (currChildCount >= 25) {
            Log.w(LOG_TAG, "printSessionTree: Hit recursion limit!", new Object[0]);
            sb.append(TRUNCATE_STRING);
            return;
        }
        sb.append(toString());
        Iterator<Session> it = this.mChildSessions.iterator();
        while (it.hasNext()) {
            Session child = it.next();
            sb.append("\n");
            for (int i = 0; i <= tabI; i++) {
                sb.append("\t");
            }
            int i2 = tabI + 1;
            child.printSessionTree(i2, sb, currChildCount + 1);
        }
    }

    public String getFullMethodPath(boolean truncatePath) {
        StringBuilder sb = new StringBuilder();
        getFullMethodPath(sb, truncatePath, 0);
        return sb.toString();
    }

    private synchronized void getFullMethodPath(StringBuilder sb, boolean truncatePath, int parentCount) {
        if (parentCount >= 25) {
            Log.w(LOG_TAG, "getFullMethodPath: Hit recursion limit!", new Object[0]);
            sb.append(TRUNCATE_STRING);
        } else if (!TextUtils.isEmpty(this.mFullMethodPathCache) && !truncatePath) {
            sb.append(this.mFullMethodPathCache);
        } else {
            Session parentSession = getParentSession();
            boolean isSessionStarted = false;
            if (parentSession != null) {
                isSessionStarted = this.mShortMethodName.equals(parentSession.mShortMethodName) ? false : true;
                parentSession.getFullMethodPath(sb, truncatePath, parentCount + 1);
                sb.append(SUBSESSION_SEPARATION_CHAR);
            }
            if (isExternal()) {
                if (truncatePath) {
                    sb.append(TRUNCATE_STRING);
                } else {
                    sb.append("(");
                    sb.append(this.mShortMethodName);
                    sb.append(")");
                }
            } else {
                sb.append(this.mShortMethodName);
            }
            if (isSessionStarted && !truncatePath) {
                this.mFullMethodPathCache = sb.toString();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSessionExternal() {
        return getRootSession("isSessionExternal").isExternal();
    }

    public int hashCode() {
        String str = this.mSessionId;
        int result = str != null ? str.hashCode() : 0;
        int i = result * 31;
        String str2 = this.mShortMethodName;
        int result2 = i + (str2 != null ? str2.hashCode() : 0);
        long j = this.mExecutionStartTimeMs;
        long j2 = this.mExecutionEndTimeMs;
        int result3 = ((((result2 * 31) + ((int) (j ^ (j >>> 32)))) * 31) + ((int) (j2 ^ (j2 >>> 32)))) * 31;
        Session session = this.mParentSession;
        int result4 = (result3 + (session != null ? session.hashCode() : 0)) * 31;
        ArrayList<Session> arrayList = this.mChildSessions;
        int result5 = (((((((result4 + (arrayList != null ? arrayList.hashCode() : 0)) * 31) + (this.mIsCompleted ? 1 : 0)) * 31) + this.mChildCounter) * 31) + (this.mIsStartedFromActiveSession ? 1 : 0)) * 31;
        String str3 = this.mOwnerInfo;
        return result5 + (str3 != null ? str3.hashCode() : 0);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        if (this.mExecutionStartTimeMs != session.mExecutionStartTimeMs || this.mExecutionEndTimeMs != session.mExecutionEndTimeMs || this.mIsCompleted != session.mIsCompleted || this.mChildCounter != session.mChildCounter || this.mIsStartedFromActiveSession != session.mIsStartedFromActiveSession) {
            return false;
        }
        String str = this.mSessionId;
        if (str == null ? session.mSessionId != null : !str.equals(session.mSessionId)) {
            return false;
        }
        String str2 = this.mShortMethodName;
        if (str2 == null ? session.mShortMethodName != null : !str2.equals(session.mShortMethodName)) {
            return false;
        }
        Session session2 = this.mParentSession;
        if (session2 == null ? session.mParentSession != null : !session2.equals(session.mParentSession)) {
            return false;
        }
        ArrayList<Session> arrayList = this.mChildSessions;
        if (arrayList == null ? session.mChildSessions != null : !arrayList.equals(session.mChildSessions)) {
            return false;
        }
        String str3 = this.mOwnerInfo;
        if (str3 != null) {
            return str3.equals(session.mOwnerInfo);
        }
        if (session.mOwnerInfo == null) {
            return true;
        }
        return false;
    }

    public String toString() {
        Session session = this.mParentSession;
        if (session != null && this.mIsStartedFromActiveSession) {
            return session.toString();
        }
        StringBuilder methodName = new StringBuilder();
        methodName.append(getFullMethodPath(false));
        String str = this.mOwnerInfo;
        if (str != null && !str.isEmpty()) {
            methodName.append("(InCall package: ");
            methodName.append(this.mOwnerInfo);
            methodName.append(")");
        }
        return methodName.toString() + "@" + getFullSessionId();
    }
}
