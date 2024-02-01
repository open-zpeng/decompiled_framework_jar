package com.android.internal.telephony;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes3.dex */
public class EncodeException extends Exception {
    public static final int ERROR_EXCEED_SIZE = 1;
    public static final int ERROR_UNENCODABLE = 0;
    private int mError;

    public EncodeException() {
        this.mError = 0;
    }

    @UnsupportedAppUsage
    public EncodeException(String s) {
        super(s);
        this.mError = 0;
    }

    public EncodeException(String s, int error) {
        super(s);
        this.mError = 0;
        this.mError = error;
    }

    @UnsupportedAppUsage
    public EncodeException(char c) {
        super("Unencodable char: '" + c + "'");
        this.mError = 0;
    }

    public int getError() {
        return this.mError;
    }
}
