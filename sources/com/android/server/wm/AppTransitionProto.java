package com.android.server.wm;
/* loaded from: classes3.dex */
public final class AppTransitionProto {
    private protected static final int APP_STATE_IDLE = 0;
    private protected static final int APP_STATE_READY = 1;
    private protected static final int APP_STATE_RUNNING = 2;
    private protected static final int APP_STATE_TIMEOUT = 3;
    private protected static final long APP_TRANSITION_STATE = 1159641169921L;
    private protected static final long LAST_USED_APP_TRANSITION = 1159641169922L;
    private protected static final int TRANSIT_ACTIVITY_CLOSE = 7;
    private protected static final int TRANSIT_ACTIVITY_OPEN = 6;
    private protected static final int TRANSIT_ACTIVITY_RELAUNCH = 18;
    private protected static final int TRANSIT_DOCK_TASK_FROM_RECENTS = 19;
    private protected static final int TRANSIT_KEYGUARD_GOING_AWAY = 20;
    private protected static final int TRANSIT_KEYGUARD_GOING_AWAY_ON_WALLPAPER = 21;
    private protected static final int TRANSIT_KEYGUARD_OCCLUDE = 22;
    private protected static final int TRANSIT_KEYGUARD_UNOCCLUDE = 23;
    private protected static final int TRANSIT_NONE = 0;
    private protected static final int TRANSIT_TASK_CLOSE = 9;
    private protected static final int TRANSIT_TASK_IN_PLACE = 17;
    private protected static final int TRANSIT_TASK_OPEN = 8;
    private protected static final int TRANSIT_TASK_OPEN_BEHIND = 16;
    private protected static final int TRANSIT_TASK_TO_BACK = 11;
    private protected static final int TRANSIT_TASK_TO_FRONT = 10;
    private protected static final int TRANSIT_TRANSLUCENT_ACTIVITY_CLOSE = 25;
    private protected static final int TRANSIT_TRANSLUCENT_ACTIVITY_OPEN = 24;
    private protected static final int TRANSIT_UNSET = -1;
    private protected static final int TRANSIT_WALLPAPER_CLOSE = 12;
    private protected static final int TRANSIT_WALLPAPER_INTRA_CLOSE = 15;
    private protected static final int TRANSIT_WALLPAPER_INTRA_OPEN = 14;
    private protected static final int TRANSIT_WALLPAPER_OPEN = 13;

    private protected synchronized AppTransitionProto() {
    }
}
