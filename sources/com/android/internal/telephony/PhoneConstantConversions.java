package com.android.internal.telephony;

import com.android.internal.telephony.PhoneConstants;

/* loaded from: classes3.dex */
public class PhoneConstantConversions {
    public static int convertCallState(PhoneConstants.State state) {
        int i = AnonymousClass1.$SwitchMap$com$android$internal$telephony$PhoneConstants$State[state.ordinal()];
        if (i != 1) {
            return i != 2 ? 0 : 2;
        }
        return 1;
    }

    public static PhoneConstants.State convertCallState(int state) {
        if (state != 1) {
            if (state == 2) {
                return PhoneConstants.State.OFFHOOK;
            }
            return PhoneConstants.State.IDLE;
        }
        return PhoneConstants.State.RINGING;
    }

    /* renamed from: com.android.internal.telephony.PhoneConstantConversions$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$PhoneConstants$DataState = new int[PhoneConstants.DataState.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$PhoneConstants$State;

        static {
            try {
                $SwitchMap$com$android$internal$telephony$PhoneConstants$DataState[PhoneConstants.DataState.CONNECTING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$PhoneConstants$DataState[PhoneConstants.DataState.CONNECTED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$PhoneConstants$DataState[PhoneConstants.DataState.SUSPENDED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SwitchMap$com$android$internal$telephony$PhoneConstants$State = new int[PhoneConstants.State.values().length];
            try {
                $SwitchMap$com$android$internal$telephony$PhoneConstants$State[PhoneConstants.State.RINGING.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$PhoneConstants$State[PhoneConstants.State.OFFHOOK.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public static int convertDataState(PhoneConstants.DataState state) {
        int i = AnonymousClass1.$SwitchMap$com$android$internal$telephony$PhoneConstants$DataState[state.ordinal()];
        if (i != 1) {
            if (i != 2) {
                return i != 3 ? 0 : 3;
            }
            return 2;
        }
        return 1;
    }

    public static PhoneConstants.DataState convertDataState(int state) {
        if (state != 1) {
            if (state != 2) {
                if (state == 3) {
                    return PhoneConstants.DataState.SUSPENDED;
                }
                return PhoneConstants.DataState.DISCONNECTED;
            }
            return PhoneConstants.DataState.CONNECTED;
        }
        return PhoneConstants.DataState.CONNECTING;
    }
}
