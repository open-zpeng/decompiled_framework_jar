package com.android.internal.telephony;

import android.provider.Telephony;
import android.telephony.SmsMessage;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.SmsConstants;
import java.util.Arrays;
/* loaded from: classes3.dex */
public abstract class SmsMessageBase {
    protected String mEmailBody;
    protected String mEmailFrom;
    protected boolean mIsEmail;
    public private boolean mIsMwi;
    public private String mMessageBody;
    private protected int mMessageRef;
    public private boolean mMwiDontStore;
    public private boolean mMwiSense;
    public private SmsAddress mOriginatingAddress;
    public private byte[] mPdu;
    protected String mPseudoSubject;
    public private String mScAddress;
    protected long mScTimeMillis;
    protected byte[] mUserData;
    public private SmsHeader mUserDataHeader;
    protected int mStatusOnIcc = -1;
    protected int mIndexOnIcc = -1;

    public abstract synchronized SmsConstants.MessageClass getMessageClass();

    /* JADX INFO: Access modifiers changed from: private */
    public abstract int getProtocolIdentifier();

    /* JADX INFO: Access modifiers changed from: private */
    public abstract int getStatus();

    public abstract synchronized boolean isCphsMwiMessage();

    public abstract synchronized boolean isMWIClearMessage();

    public abstract synchronized boolean isMWISetMessage();

    public abstract synchronized boolean isMwiDontStore();

    /* JADX INFO: Access modifiers changed from: private */
    public abstract boolean isReplace();

    /* JADX INFO: Access modifiers changed from: private */
    public abstract boolean isReplyPathPresent();

    /* JADX INFO: Access modifiers changed from: private */
    public abstract boolean isStatusReportMessage();

    /* loaded from: classes3.dex */
    public static abstract class SubmitPduBase {
        private protected byte[] encodedMessage;
        private protected byte[] encodedScAddress;

        public String toString() {
            return "SubmitPdu: encodedScAddress = " + Arrays.toString(this.encodedScAddress) + ", encodedMessage = " + Arrays.toString(this.encodedMessage);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getServiceCenterAddress() {
        return this.mScAddress;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getOriginatingAddress() {
        if (this.mOriginatingAddress == null) {
            return null;
        }
        return this.mOriginatingAddress.getAddressString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDisplayOriginatingAddress() {
        if (this.mIsEmail) {
            return this.mEmailFrom;
        }
        return getOriginatingAddress();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getMessageBody() {
        return this.mMessageBody;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDisplayMessageBody() {
        if (this.mIsEmail) {
            return this.mEmailBody;
        }
        return getMessageBody();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getPseudoSubject() {
        return this.mPseudoSubject == null ? "" : this.mPseudoSubject;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getTimestampMillis() {
        return this.mScTimeMillis;
    }

    public synchronized boolean isEmail() {
        return this.mIsEmail;
    }

    public synchronized String getEmailBody() {
        return this.mEmailBody;
    }

    public synchronized String getEmailFrom() {
        return this.mEmailFrom;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] getUserData() {
        return this.mUserData;
    }

    private protected SmsHeader getUserDataHeader() {
        return this.mUserDataHeader;
    }

    public synchronized byte[] getPdu() {
        return this.mPdu;
    }

    public synchronized int getStatusOnIcc() {
        return this.mStatusOnIcc;
    }

    public synchronized int getIndexOnIcc() {
        return this.mIndexOnIcc;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void parseMessageBody() {
        if (this.mOriginatingAddress != null && this.mOriginatingAddress.couldBeEmailGateway()) {
            extractEmailAddressFromMessageBody();
        }
    }

    protected synchronized void extractEmailAddressFromMessageBody() {
        String[] parts = this.mMessageBody.split("( /)|( )", 2);
        if (parts.length < 2) {
            return;
        }
        this.mEmailFrom = parts[0];
        this.mEmailBody = parts[1];
        this.mIsEmail = Telephony.Mms.isEmailAddress(this.mEmailFrom);
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:?, code lost:
        return r2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static synchronized int findNextUnicodePosition(int r4, int r5, java.lang.CharSequence r6) {
        /*
            int r0 = r5 / 2
            int r0 = r0 + r4
            int r1 = r6.length()
            int r0 = java.lang.Math.min(r0, r1)
            int r1 = r6.length()
            if (r0 >= r1) goto L55
            java.text.BreakIterator r1 = java.text.BreakIterator.getCharacterInstance()
            java.lang.String r2 = r6.toString()
            r1.setText(r2)
            boolean r2 = r1.isBoundary(r0)
            if (r2 != 0) goto L55
            int r2 = r1.preceding(r0)
        L26:
            int r3 = r2 + 4
            if (r3 > r0) goto L43
            int r3 = java.lang.Character.codePointAt(r6, r2)
            boolean r3 = android.text.Emoji.isRegionalIndicatorSymbol(r3)
            if (r3 == 0) goto L43
            int r3 = r2 + 2
            int r3 = java.lang.Character.codePointAt(r6, r3)
            boolean r3 = android.text.Emoji.isRegionalIndicatorSymbol(r3)
            if (r3 == 0) goto L43
            int r2 = r2 + 4
            goto L26
        L43:
            if (r2 <= r4) goto L47
            r0 = r2
            goto L55
        L47:
            int r3 = r0 + (-1)
            char r3 = r6.charAt(r3)
            boolean r3 = java.lang.Character.isHighSurrogate(r3)
            if (r3 == 0) goto L55
            int r0 = r0 + (-1)
        L55:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.telephony.SmsMessageBase.findNextUnicodePosition(int, int, java.lang.CharSequence):int");
    }

    public static synchronized GsmAlphabet.TextEncodingDetails calcUnicodeEncodingDetails(CharSequence msgBody) {
        GsmAlphabet.TextEncodingDetails ted = new GsmAlphabet.TextEncodingDetails();
        int octets = msgBody.length() * 2;
        ted.codeUnitSize = 3;
        ted.codeUnitCount = msgBody.length();
        if (octets > 140) {
            int maxUserDataBytesWithHeader = 134;
            if (!SmsMessage.hasEmsSupport() && octets <= 9 * (134 - 2)) {
                maxUserDataBytesWithHeader = 134 - 2;
            }
            int pos = 0;
            int msgCount = 0;
            while (pos < msgBody.length()) {
                int nextPos = findNextUnicodePosition(pos, maxUserDataBytesWithHeader, msgBody);
                if (nextPos == msgBody.length()) {
                    ted.codeUnitsRemaining = ((maxUserDataBytesWithHeader / 2) + pos) - msgBody.length();
                }
                pos = nextPos;
                msgCount++;
            }
            ted.msgCount = msgCount;
        } else {
            ted.msgCount = 1;
            ted.codeUnitsRemaining = (140 - octets) / 2;
        }
        return ted;
    }
}
