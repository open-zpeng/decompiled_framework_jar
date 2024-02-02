package com.android.internal.telephony;
/* loaded from: classes3.dex */
public class EncodeException extends Exception {
    public synchronized EncodeException() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public EncodeException(String s) {
        super(s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public EncodeException(char c) {
        super("Unencodable char: '" + c + "'");
    }
}
