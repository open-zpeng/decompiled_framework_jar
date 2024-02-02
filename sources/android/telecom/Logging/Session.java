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
    private protected static final String CONTINUE_SUBSESSION = "CONTINUE_SUBSESSION";
    private protected static final String CREATE_SUBSESSION = "CREATE_SUBSESSION";
    private protected static final String END_SESSION = "END_SESSION";
    private protected static final String END_SUBSESSION = "END_SUBSESSION";
    private protected static final String EXTERNAL_INDICATOR = "E-";
    private protected static final String SESSION_SEPARATION_CHAR_CHILD = "_";
    private protected static final String START_EXTERNAL_SESSION = "START_EXTERNAL_SESSION";
    private protected static final String START_SESSION = "START_SESSION";
    private protected static final String SUBSESSION_SEPARATION_CHAR = "->";
    private protected static final String TRUNCATE_STRING = "...";
    private protected static final int UNDEFINED = -1;
    public protected ArrayList<Session> mChildSessions;
    public protected long mExecutionStartTimeMs;
    public protected String mFullMethodPathCache;
    public protected boolean mIsStartedFromActiveSession;
    public protected String mOwnerInfo;
    public protected Session mParentSession;
    public protected String mSessionId;
    public protected String mShortMethodName;
    public protected long mExecutionEndTimeMs = -1;
    public protected boolean mIsCompleted = false;
    public protected boolean mIsExternal = false;
    public protected int mChildCounter = 0;

    /* loaded from: classes2.dex */
    public static class Info implements Parcelable {
        private protected static final Parcelable.Creator<Info> CREATOR = new Parcelable.Creator<Info>() { // from class: android.telecom.Logging.Session.Info.1
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
        private protected final String methodPath;
        private protected final String sessionId;

        public protected synchronized Info(String id, String path) {
            this.sessionId = id;
            this.methodPath = path;
        }

        private protected static synchronized Info getInfo(Session s) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Session(String sessionId, String shortMethodName, long startTimeMs, boolean isStartedFromActiveSession, String ownerInfo) {
        this.mIsStartedFromActiveSession = false;
        setSessionId(sessionId);
        setShortMethodName(shortMethodName);
        this.mExecutionStartTimeMs = startTimeMs;
        this.mParentSession = null;
        this.mChildSessions = new ArrayList<>(5);
        this.mIsStartedFromActiveSession = isStartedFromActiveSession;
        this.mOwnerInfo = ownerInfo;
    }

    private protected synchronized void setSessionId(String sessionId) {
        if (sessionId == null) {
            this.mSessionId = "?";
        }
        this.mSessionId = sessionId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized String getShortMethodName() {
        return this.mShortMethodName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setShortMethodName(String shortMethodName) {
        if (shortMethodName == null) {
            shortMethodName = "";
        }
        this.mShortMethodName = shortMethodName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setIsExternal(boolean isExternal) {
        this.mIsExternal = isExternal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean isExternal() {
        return this.mIsExternal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setParentSession(Session parentSession) {
        this.mParentSession = parentSession;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void addChild(Session childSession) {
        if (childSession != null) {
            this.mChildSessions.add(childSession);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void removeChild(Session child) {
        if (child != null) {
            this.mChildSessions.remove(child);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized long getExecutionStartTimeMilliseconds() {
        return this.mExecutionStartTimeMs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setExecutionStartTimeMs(long startTimeMs) {
        this.mExecutionStartTimeMs = startTimeMs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Session getParentSession() {
        return this.mParentSession;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized ArrayList<Session> getChildSessions() {
        return this.mChildSessions;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean isSessionCompleted() {
        return this.mIsCompleted;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean isStartedFromActiveSession() {
        return this.mIsStartedFromActiveSession;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Info getInfo() {
        return Info.getInfo(this);
    }

    @VisibleForTesting
    private protected synchronized String getSessionId() {
        return this.mSessionId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void markSessionCompleted(long executionEndTimeMs) {
        this.mExecutionEndTimeMs = executionEndTimeMs;
        this.mIsCompleted = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized long getLocalExecutionTime() {
        if (this.mExecutionEndTimeMs == -1) {
            return -1L;
        }
        return this.mExecutionEndTimeMs - this.mExecutionStartTimeMs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized String getNextChildId() {
        int i;
        i = this.mChildCounter;
        this.mChildCounter = i + 1;
        return String.valueOf(i);
    }

    /* JADX INFO: Access modifiers changed from: public */
    public synchronized String getFullSessionId() {
        Session parentSession = this.mParentSession;
        if (parentSession == null) {
            return this.mSessionId;
        }
        if (Log.VERBOSE) {
            return parentSession.getFullSessionId() + SESSION_SEPARATION_CHAR_CHILD + this.mSessionId;
        }
        return parentSession.getFullSessionId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized String printFullSessionTree() {
        Session topNode = this;
        while (topNode.getParentSession() != null) {
            topNode = topNode.getParentSession();
        }
        return topNode.printSessionTree();
    }

    private protected synchronized String printSessionTree() {
        StringBuilder sb = new StringBuilder();
        printSessionTree(0, sb);
        return sb.toString();
    }

    public protected synchronized void printSessionTree(int tabI, StringBuilder sb) {
        sb.append(toString());
        Iterator<Session> it = this.mChildSessions.iterator();
        while (it.hasNext()) {
            Session child = it.next();
            sb.append("\n");
            for (int i = 0; i <= tabI; i++) {
                sb.append("\t");
            }
            int i2 = tabI + 1;
            child.printSessionTree(i2, sb);
        }
    }

    private protected synchronized String getFullMethodPath(boolean truncatePath) {
        StringBuilder sb = new StringBuilder();
        getFullMethodPath(sb, truncatePath);
        return sb.toString();
    }

    public protected synchronized void getFullMethodPath(StringBuilder sb, boolean truncatePath) {
        if (!TextUtils.isEmpty(this.mFullMethodPathCache) && !truncatePath) {
            sb.append(this.mFullMethodPathCache);
            return;
        }
        Session parentSession = getParentSession();
        boolean isSessionStarted = false;
        if (parentSession != null) {
            isSessionStarted = !this.mShortMethodName.equals(parentSession.mShortMethodName);
            parentSession.getFullMethodPath(sb, truncatePath);
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

    /* JADX INFO: Access modifiers changed from: public */
    public synchronized boolean isSessionExternal() {
        if (getParentSession() == null) {
            return isExternal();
        }
        return getParentSession().isSessionExternal();
    }

    public int hashCode() {
        int result = this.mSessionId != null ? this.mSessionId.hashCode() : 0;
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.mShortMethodName != null ? this.mShortMethodName.hashCode() : 0))) + ((int) (this.mExecutionStartTimeMs ^ (this.mExecutionStartTimeMs >>> 32))))) + ((int) (this.mExecutionEndTimeMs ^ (this.mExecutionEndTimeMs >>> 32))))) + (this.mParentSession != null ? this.mParentSession.hashCode() : 0))) + (this.mChildSessions != null ? this.mChildSessions.hashCode() : 0))) + (this.mIsCompleted ? 1 : 0))) + this.mChildCounter)) + (this.mIsStartedFromActiveSession ? 1 : 0))) + (this.mOwnerInfo != null ? this.mOwnerInfo.hashCode() : 0);
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
        if (this.mSessionId == null ? session.mSessionId != null : !this.mSessionId.equals(session.mSessionId)) {
            return false;
        }
        if (this.mShortMethodName == null ? session.mShortMethodName != null : !this.mShortMethodName.equals(session.mShortMethodName)) {
            return false;
        }
        if (this.mParentSession == null ? session.mParentSession != null : !this.mParentSession.equals(session.mParentSession)) {
            return false;
        }
        if (this.mChildSessions == null ? session.mChildSessions != null : !this.mChildSessions.equals(session.mChildSessions)) {
            return false;
        }
        if (this.mOwnerInfo != null) {
            return this.mOwnerInfo.equals(session.mOwnerInfo);
        }
        if (session.mOwnerInfo == null) {
            return true;
        }
        return false;
    }

    public String toString() {
        if (this.mParentSession != null && this.mIsStartedFromActiveSession) {
            return this.mParentSession.toString();
        }
        StringBuilder methodName = new StringBuilder();
        methodName.append(getFullMethodPath(false));
        if (this.mOwnerInfo != null && !this.mOwnerInfo.isEmpty()) {
            methodName.append("(InCall package: ");
            methodName.append(this.mOwnerInfo);
            methodName.append(")");
        }
        return methodName.toString() + "@" + getFullSessionId();
    }
}
