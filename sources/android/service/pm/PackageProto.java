package android.service.pm;
/* loaded from: classes2.dex */
public final class PackageProto {
    private protected static final long INSTALLER_NAME = 1138166333447L;
    private protected static final long INSTALL_TIME_MS = 1112396529669L;
    private protected static final long NAME = 1138166333441L;
    private protected static final long SPLITS = 2246267895816L;
    private protected static final long UID = 1120986464258L;
    private protected static final long UPDATE_TIME_MS = 1112396529670L;
    private protected static final long USERS = 2246267895817L;
    private protected static final long VERSION_CODE = 1120986464259L;
    private protected static final long VERSION_STRING = 1138166333444L;

    private protected synchronized PackageProto() {
    }

    /* loaded from: classes2.dex */
    public final class SplitProto {
        private protected static final long NAME = 1138166333441L;
        private protected static final long REVISION_CODE = 1120986464258L;

        public SplitProto() {
        }
    }

    /* loaded from: classes2.dex */
    public final class UserInfoProto {
        private protected static final int COMPONENT_ENABLED_STATE_DEFAULT = 0;
        private protected static final int COMPONENT_ENABLED_STATE_DISABLED = 2;
        private protected static final int COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED = 4;
        private protected static final int COMPONENT_ENABLED_STATE_DISABLED_USER = 3;
        private protected static final int COMPONENT_ENABLED_STATE_ENABLED = 1;
        private protected static final long ENABLED_STATE = 1159641169927L;
        private protected static final int FULL_APP_INSTALL = 1;
        private protected static final long ID = 1120986464257L;
        private protected static final long INSTALL_TYPE = 1159641169922L;
        private protected static final int INSTANT_APP_INSTALL = 2;
        private protected static final long IS_HIDDEN = 1133871366147L;
        private protected static final long IS_LAUNCHED = 1133871366150L;
        private protected static final long IS_STOPPED = 1133871366149L;
        private protected static final long IS_SUSPENDED = 1133871366148L;
        private protected static final long LAST_DISABLED_APP_CALLER = 1138166333448L;
        private protected static final int NOT_INSTALLED_FOR_USER = 0;
        private protected static final long SUSPENDING_PACKAGE = 1138166333449L;

        public UserInfoProto() {
        }
    }
}
