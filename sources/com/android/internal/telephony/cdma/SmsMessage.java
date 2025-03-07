package com.android.internal.telephony.cdma;

import android.content.res.Resources;
import android.os.SystemProperties;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.SmsCbLocation;
import android.telephony.SmsCbMessage;
import android.telephony.cdma.CdmaSmsCbProgramData;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.R;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.Sms7BitEncodingTranslator;
import com.android.internal.telephony.SmsAddress;
import com.android.internal.telephony.SmsConstants;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.TelephonyProperties;
import com.android.internal.telephony.cdma.sms.BearerData;
import com.android.internal.telephony.cdma.sms.CdmaSmsAddress;
import com.android.internal.telephony.cdma.sms.CdmaSmsSubaddress;
import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.util.BitwiseInputStream;
import com.android.internal.util.HexDump;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/* loaded from: classes3.dex */
public class SmsMessage extends SmsMessageBase {
    private static final byte BEARER_DATA = 8;
    private static final byte BEARER_REPLY_OPTION = 6;
    private static final byte CAUSE_CODES = 7;
    private static final byte DESTINATION_ADDRESS = 4;
    private static final byte DESTINATION_SUB_ADDRESS = 5;
    private static final String LOGGABLE_TAG = "CDMA:SMS";
    static final String LOG_TAG = "SmsMessage";
    private static final byte ORIGINATING_ADDRESS = 2;
    private static final byte ORIGINATING_SUB_ADDRESS = 3;
    private static final int PRIORITY_EMERGENCY = 3;
    private static final int PRIORITY_INTERACTIVE = 1;
    private static final int PRIORITY_NORMAL = 0;
    private static final int PRIORITY_URGENT = 2;
    private static final int RETURN_ACK = 1;
    private static final int RETURN_NO_ACK = 0;
    private static final byte SERVICE_CATEGORY = 1;
    private static final byte TELESERVICE_IDENTIFIER = 0;
    private static final boolean VDBG = false;
    private BearerData mBearerData;
    private SmsEnvelope mEnvelope;
    private int status;

    /* loaded from: classes3.dex */
    public static class SubmitPdu extends SmsMessageBase.SubmitPduBase {
    }

    public SmsMessage(SmsAddress addr, SmsEnvelope env) {
        this.mOriginatingAddress = addr;
        this.mEnvelope = env;
        createPdu();
    }

    public SmsMessage() {
    }

    public static SmsMessage createFromPdu(byte[] pdu) {
        SmsMessage msg = new SmsMessage();
        try {
            msg.parsePdu(pdu);
            return msg;
        } catch (OutOfMemoryError e) {
            Log.e(LOG_TAG, "SMS PDU parsing failed with out of memory: ", e);
            return null;
        } catch (RuntimeException ex) {
            Rlog.e(LOG_TAG, "SMS PDU parsing failed: ", ex);
            return null;
        }
    }

    public static SmsMessage createFromEfRecord(int index, byte[] data) {
        try {
            SmsMessage msg = new SmsMessage();
            msg.mIndexOnIcc = index;
            if ((data[0] & 1) == 0) {
                Rlog.w(LOG_TAG, "SMS parsing failed: Trying to parse a free record");
                return null;
            }
            msg.mStatusOnIcc = data[0] & 7;
            int size = data[1] & 255;
            byte[] pdu = new byte[size];
            System.arraycopy(data, 2, pdu, 0, size);
            msg.parsePduFromEfRecord(pdu);
            return msg;
        } catch (RuntimeException ex) {
            Rlog.e(LOG_TAG, "SMS PDU parsing failed: ", ex);
            return null;
        }
    }

    public static int getTPLayerLengthForPDU(String pdu) {
        Rlog.w(LOG_TAG, "getTPLayerLengthForPDU: is not supported in CDMA mode.");
        return 0;
    }

    public static SubmitPdu getSubmitPdu(String scAddr, String destAddr, String message, boolean statusReportRequested, SmsHeader smsHeader) {
        return getSubmitPdu(scAddr, destAddr, message, statusReportRequested, smsHeader, -1);
    }

    public static SubmitPdu getSubmitPdu(String scAddr, String destAddr, String message, boolean statusReportRequested, SmsHeader smsHeader, int priority) {
        if (message == null || destAddr == null) {
            return null;
        }
        UserData uData = new UserData();
        uData.payloadStr = message;
        uData.userDataHeader = smsHeader;
        return privateGetSubmitPdu(destAddr, statusReportRequested, uData, priority);
    }

    public static SubmitPdu getSubmitPdu(String scAddr, String destAddr, int destPort, byte[] data, boolean statusReportRequested) {
        SmsHeader.PortAddrs portAddrs = new SmsHeader.PortAddrs();
        portAddrs.destPort = destPort;
        portAddrs.origPort = 0;
        portAddrs.areEightBits = false;
        SmsHeader smsHeader = new SmsHeader();
        smsHeader.portAddrs = portAddrs;
        UserData uData = new UserData();
        uData.userDataHeader = smsHeader;
        uData.msgEncoding = 0;
        uData.msgEncodingSet = true;
        uData.payload = data;
        return privateGetSubmitPdu(destAddr, statusReportRequested, uData);
    }

    public static SubmitPdu getSubmitPdu(String destAddr, UserData userData, boolean statusReportRequested) {
        return privateGetSubmitPdu(destAddr, statusReportRequested, userData);
    }

    public static SubmitPdu getSubmitPdu(String destAddr, UserData userData, boolean statusReportRequested, int priority) {
        return privateGetSubmitPdu(destAddr, statusReportRequested, userData, priority);
    }

    @Override // com.android.internal.telephony.SmsMessageBase
    public int getProtocolIdentifier() {
        Rlog.w(LOG_TAG, "getProtocolIdentifier: is not supported in CDMA mode.");
        return 0;
    }

    @Override // com.android.internal.telephony.SmsMessageBase
    public boolean isReplace() {
        Rlog.w(LOG_TAG, "isReplace: is not supported in CDMA mode.");
        return false;
    }

    @Override // com.android.internal.telephony.SmsMessageBase
    public boolean isCphsMwiMessage() {
        Rlog.w(LOG_TAG, "isCphsMwiMessage: is not supported in CDMA mode.");
        return false;
    }

    @Override // com.android.internal.telephony.SmsMessageBase
    public boolean isMWIClearMessage() {
        BearerData bearerData = this.mBearerData;
        return bearerData != null && bearerData.numberOfMessages == 0;
    }

    @Override // com.android.internal.telephony.SmsMessageBase
    public boolean isMWISetMessage() {
        BearerData bearerData = this.mBearerData;
        return bearerData != null && bearerData.numberOfMessages > 0;
    }

    @Override // com.android.internal.telephony.SmsMessageBase
    public boolean isMwiDontStore() {
        BearerData bearerData = this.mBearerData;
        return bearerData != null && bearerData.numberOfMessages > 0 && this.mBearerData.userData == null;
    }

    @Override // com.android.internal.telephony.SmsMessageBase
    public int getStatus() {
        return this.status << 16;
    }

    @Override // com.android.internal.telephony.SmsMessageBase
    public boolean isStatusReportMessage() {
        return this.mBearerData.messageType == 4;
    }

    @Override // com.android.internal.telephony.SmsMessageBase
    public boolean isReplyPathPresent() {
        Rlog.w(LOG_TAG, "isReplyPathPresent: is not supported in CDMA mode.");
        return false;
    }

    public static GsmAlphabet.TextEncodingDetails calculateLength(CharSequence messageBody, boolean use7bitOnly, boolean isEntireMsg) {
        CharSequence newMsgBody = null;
        Resources r = Resources.getSystem();
        if (r.getBoolean(R.bool.config_sms_force_7bit_encoding)) {
            newMsgBody = Sms7BitEncodingTranslator.translate(messageBody, true);
        }
        if (TextUtils.isEmpty(newMsgBody)) {
            newMsgBody = messageBody;
        }
        return BearerData.calcTextEncodingDetails(newMsgBody, use7bitOnly, isEntireMsg);
    }

    public int getTeleService() {
        return this.mEnvelope.teleService;
    }

    public int getMessageType() {
        if (this.mEnvelope.serviceCategory != 0) {
            return 1;
        }
        return 0;
    }

    private void parsePdu(byte[] pdu) {
        int length;
        ByteArrayInputStream bais = new ByteArrayInputStream(pdu);
        DataInputStream dis = new DataInputStream(bais);
        SmsEnvelope env = new SmsEnvelope();
        CdmaSmsAddress addr = new CdmaSmsAddress();
        CdmaSmsSubaddress subaddr = new CdmaSmsSubaddress();
        try {
            env.messageType = dis.readInt();
            env.teleService = dis.readInt();
            env.serviceCategory = dis.readInt();
            addr.digitMode = dis.readByte();
            addr.numberMode = dis.readByte();
            addr.ton = dis.readByte();
            addr.numberPlan = dis.readByte();
            length = dis.readUnsignedByte();
            addr.numberOfDigits = length;
        } catch (IOException ex) {
            throw new RuntimeException("createFromPdu: conversion from byte array to object failed: " + ex, ex);
        } catch (Exception ex2) {
            Rlog.e(LOG_TAG, "createFromPdu: conversion from byte array to object failed: " + ex2);
        }
        if (length > pdu.length) {
            throw new RuntimeException("createFromPdu: Invalid pdu, addr.numberOfDigits " + length + " > pdu len " + pdu.length);
        }
        addr.origBytes = new byte[length];
        dis.read(addr.origBytes, 0, length);
        env.bearerReply = dis.readInt();
        env.replySeqNo = dis.readByte();
        env.errorClass = dis.readByte();
        env.causeCode = dis.readByte();
        int bearerDataLength = dis.readInt();
        if (bearerDataLength > pdu.length) {
            throw new RuntimeException("createFromPdu: Invalid pdu, bearerDataLength " + bearerDataLength + " > pdu len " + pdu.length);
        }
        env.bearerData = new byte[bearerDataLength];
        dis.read(env.bearerData, 0, bearerDataLength);
        dis.close();
        this.mOriginatingAddress = addr;
        env.origAddress = addr;
        env.origSubaddress = subaddr;
        this.mEnvelope = env;
        this.mPdu = pdu;
        parseSms();
    }

    private void parsePduFromEfRecord(byte[] pdu) {
        ByteArrayInputStream bais = new ByteArrayInputStream(pdu);
        DataInputStream dis = new DataInputStream(bais);
        SmsEnvelope env = new SmsEnvelope();
        CdmaSmsAddress addr = new CdmaSmsAddress();
        CdmaSmsSubaddress subAddr = new CdmaSmsSubaddress();
        try {
            env.messageType = dis.readByte();
            while (dis.available() > 0) {
                int parameterId = dis.readByte();
                int parameterLen = dis.readUnsignedByte();
                byte[] parameterData = new byte[parameterLen];
                switch (parameterId) {
                    case 0:
                        env.teleService = dis.readUnsignedShort();
                        Rlog.i(LOG_TAG, "teleservice = " + env.teleService);
                        break;
                    case 1:
                        env.serviceCategory = dis.readUnsignedShort();
                        break;
                    case 2:
                    case 4:
                        dis.read(parameterData, 0, parameterLen);
                        BitwiseInputStream addrBis = new BitwiseInputStream(parameterData);
                        addr.digitMode = addrBis.read(1);
                        addr.numberMode = addrBis.read(1);
                        int numberType = 0;
                        if (addr.digitMode == 1) {
                            numberType = addrBis.read(3);
                            addr.ton = numberType;
                            if (addr.numberMode == 0) {
                                addr.numberPlan = addrBis.read(4);
                            }
                        }
                        addr.numberOfDigits = addrBis.read(8);
                        byte[] data = new byte[addr.numberOfDigits];
                        if (addr.digitMode == 0) {
                            int index = 0;
                            while (true) {
                                int parameterLen2 = parameterLen;
                                if (index < addr.numberOfDigits) {
                                    byte b = (byte) (addrBis.read(4) & 15);
                                    data[index] = convertDtmfToAscii(b);
                                    index++;
                                    parameterLen = parameterLen2;
                                }
                            }
                        } else if (addr.digitMode != 1) {
                            Rlog.e(LOG_TAG, "Incorrect Digit mode");
                        } else if (addr.numberMode == 0) {
                            int index2 = 0;
                            while (index2 < addr.numberOfDigits) {
                                byte b2 = (byte) (addrBis.read(8) & 255);
                                data[index2] = b2;
                                index2++;
                                parameterData = parameterData;
                            }
                        } else if (addr.numberMode == 1) {
                            if (numberType == 2) {
                                Rlog.e(LOG_TAG, "TODO: Addr is email id");
                            } else {
                                Rlog.e(LOG_TAG, "TODO: Addr is data network address");
                            }
                        } else {
                            Rlog.e(LOG_TAG, "Addr is of incorrect type");
                        }
                        addr.origBytes = data;
                        Rlog.pii(LOG_TAG, "Addr=" + addr.toString());
                        this.mOriginatingAddress = addr;
                        if (parameterId != 4) {
                            break;
                        } else {
                            this.mRecipientAddress = addr;
                            break;
                        }
                    case 3:
                    case 5:
                        dis.read(parameterData, 0, parameterLen);
                        BitwiseInputStream subAddrBis = new BitwiseInputStream(parameterData);
                        subAddr.type = subAddrBis.read(3);
                        subAddr.odd = subAddrBis.readByteArray(1)[0];
                        int subAddrLen = subAddrBis.read(8);
                        byte[] subdata = new byte[subAddrLen];
                        int index3 = 0;
                        while (index3 < subAddrLen) {
                            int subAddrLen2 = subAddrLen;
                            byte b3 = (byte) (subAddrBis.read(4) & 255);
                            subdata[index3] = convertDtmfToAscii(b3);
                            index3++;
                            subAddrLen = subAddrLen2;
                        }
                        subAddr.origBytes = subdata;
                        break;
                    case 6:
                        dis.read(parameterData, 0, parameterLen);
                        BitwiseInputStream replyOptBis = new BitwiseInputStream(parameterData);
                        env.bearerReply = replyOptBis.read(6);
                        break;
                    case 7:
                        dis.read(parameterData, 0, parameterLen);
                        BitwiseInputStream ccBis = new BitwiseInputStream(parameterData);
                        env.replySeqNo = ccBis.readByteArray(6)[0];
                        env.errorClass = ccBis.readByteArray(2)[0];
                        if (env.errorClass == 0) {
                            break;
                        } else {
                            env.causeCode = ccBis.readByteArray(8)[0];
                            break;
                        }
                    case 8:
                        dis.read(parameterData, 0, parameterLen);
                        env.bearerData = parameterData;
                        break;
                    default:
                        throw new Exception("unsupported parameterId (" + parameterId + ")");
                }
            }
            bais.close();
            dis.close();
        } catch (Exception ex) {
            Rlog.e(LOG_TAG, "parsePduFromEfRecord: conversion from pdu to SmsMessage failed" + ex);
        }
        this.mOriginatingAddress = addr;
        env.origAddress = addr;
        env.origSubaddress = subAddr;
        this.mEnvelope = env;
        this.mPdu = pdu;
        parseSms();
    }

    public void parseSms() {
        if (this.mEnvelope.teleService == 262144) {
            this.mBearerData = new BearerData();
            if (this.mEnvelope.bearerData != null) {
                this.mBearerData.numberOfMessages = this.mEnvelope.bearerData[0] & 255;
                return;
            }
            return;
        }
        this.mBearerData = BearerData.decode(this.mEnvelope.bearerData);
        if (Rlog.isLoggable(LOGGABLE_TAG, 2)) {
            Rlog.d(LOG_TAG, "MT raw BearerData = '" + HexDump.toHexString(this.mEnvelope.bearerData) + "'");
            StringBuilder sb = new StringBuilder();
            sb.append("MT (decoded) BearerData = ");
            sb.append(this.mBearerData);
            Rlog.d(LOG_TAG, sb.toString());
        }
        this.mMessageRef = this.mBearerData.messageId;
        if (this.mBearerData.userData != null) {
            this.mUserData = this.mBearerData.userData.payload;
            this.mUserDataHeader = this.mBearerData.userData.userDataHeader;
            this.mMessageBody = this.mBearerData.userData.payloadStr;
        }
        if (this.mOriginatingAddress != null) {
            decodeSmsDisplayAddress(this.mOriginatingAddress);
        }
        if (this.mRecipientAddress != null) {
            decodeSmsDisplayAddress(this.mRecipientAddress);
        }
        if (this.mBearerData.msgCenterTimeStamp != null) {
            this.mScTimeMillis = this.mBearerData.msgCenterTimeStamp.toMillis(true);
        }
        if (this.mBearerData.messageType == 4) {
            if (!this.mBearerData.messageStatusSet) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("DELIVERY_ACK message without msgStatus (");
                sb2.append(this.mUserData == null ? "also missing" : "does have");
                sb2.append(" userData).");
                Rlog.d(LOG_TAG, sb2.toString());
                this.status = 0;
            } else {
                this.status = this.mBearerData.errorClass << 8;
                this.status |= this.mBearerData.messageStatus;
            }
        } else if (this.mBearerData.messageType != 1 && this.mBearerData.messageType != 2) {
            throw new RuntimeException("Unsupported message type: " + this.mBearerData.messageType);
        }
        if (this.mMessageBody != null) {
            parseMessageBody();
        } else {
            byte[] bArr = this.mUserData;
        }
    }

    private void decodeSmsDisplayAddress(SmsAddress addr) {
        addr.address = new String(addr.origBytes);
        if (addr.ton == 1 && addr.address.charAt(0) != '+') {
            addr.address = "+" + addr.address;
        }
        Rlog.pii(LOG_TAG, " decodeSmsDisplayAddress = " + addr.address);
    }

    public SmsCbMessage parseBroadcastSms(String plmn) {
        BearerData bData = BearerData.decode(this.mEnvelope.bearerData, this.mEnvelope.serviceCategory);
        if (bData == null) {
            Rlog.w(LOG_TAG, "BearerData.decode() returned null");
            return null;
        }
        if (Rlog.isLoggable(LOGGABLE_TAG, 2)) {
            Rlog.d(LOG_TAG, "MT raw BearerData = " + HexDump.toHexString(this.mEnvelope.bearerData));
        }
        SmsCbLocation location = new SmsCbLocation(plmn);
        return new SmsCbMessage(2, 1, bData.messageId, location, this.mEnvelope.serviceCategory, bData.getLanguage(), bData.userData.payloadStr, bData.priority, null, bData.cmasWarningInfo);
    }

    @Override // com.android.internal.telephony.SmsMessageBase
    public SmsConstants.MessageClass getMessageClass() {
        if (this.mBearerData.displayMode == 0) {
            return SmsConstants.MessageClass.CLASS_0;
        }
        return SmsConstants.MessageClass.UNKNOWN;
    }

    public static synchronized int getNextMessageId() {
        int msgId;
        synchronized (SmsMessage.class) {
            msgId = SystemProperties.getInt(TelephonyProperties.PROPERTY_CDMA_MSG_ID, 1);
            String nextMsgId = Integer.toString((msgId % 65535) + 1);
            try {
                SystemProperties.set(TelephonyProperties.PROPERTY_CDMA_MSG_ID, nextMsgId);
                if (Rlog.isLoggable(LOGGABLE_TAG, 2)) {
                    Rlog.d(LOG_TAG, "next persist.radio.cdma.msgid = " + nextMsgId);
                    Rlog.d(LOG_TAG, "readback gets " + SystemProperties.get(TelephonyProperties.PROPERTY_CDMA_MSG_ID));
                }
            } catch (RuntimeException ex) {
                Rlog.e(LOG_TAG, "set nextMessage ID failed: " + ex);
            }
        }
        return msgId;
    }

    private static SubmitPdu privateGetSubmitPdu(String destAddrStr, boolean statusReportRequested, UserData userData) {
        return privateGetSubmitPdu(destAddrStr, statusReportRequested, userData, -1);
    }

    private static SubmitPdu privateGetSubmitPdu(String destAddrStr, boolean statusReportRequested, UserData userData, int priority) {
        CdmaSmsAddress destAddr = CdmaSmsAddress.parse(PhoneNumberUtils.cdmaCheckAndProcessPlusCodeForSms(destAddrStr));
        if (destAddr == null) {
            return null;
        }
        BearerData bearerData = new BearerData();
        bearerData.messageType = 2;
        bearerData.messageId = getNextMessageId();
        bearerData.deliveryAckReq = statusReportRequested;
        bearerData.userAckReq = false;
        bearerData.readAckReq = false;
        bearerData.reportReq = false;
        if (priority >= 0 && priority <= 3) {
            bearerData.priorityIndicatorSet = true;
            bearerData.priority = priority;
        }
        bearerData.userData = userData;
        byte[] encodedBearerData = BearerData.encode(bearerData);
        if (encodedBearerData == null) {
            return null;
        }
        if (Rlog.isLoggable(LOGGABLE_TAG, 2)) {
            Rlog.d(LOG_TAG, "MO (encoded) BearerData = " + bearerData);
            Rlog.d(LOG_TAG, "MO raw BearerData = '" + HexDump.toHexString(encodedBearerData) + "'");
        }
        int teleservice = (!bearerData.hasUserDataHeader || userData.msgEncoding == 2) ? 4098 : 4101;
        SmsEnvelope envelope = new SmsEnvelope();
        envelope.messageType = 0;
        envelope.teleService = teleservice;
        envelope.destAddress = destAddr;
        envelope.bearerReply = 1;
        envelope.bearerData = encodedBearerData;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(envelope.teleService);
            dos.writeInt(0);
            dos.writeInt(0);
            dos.write(destAddr.digitMode);
            dos.write(destAddr.numberMode);
            dos.write(destAddr.ton);
            dos.write(destAddr.numberPlan);
            dos.write(destAddr.numberOfDigits);
            dos.write(destAddr.origBytes, 0, destAddr.origBytes.length);
            dos.write(0);
            dos.write(0);
            dos.write(0);
            dos.write(encodedBearerData.length);
            dos.write(encodedBearerData, 0, encodedBearerData.length);
            dos.close();
            SubmitPdu pdu = new SubmitPdu();
            pdu.encodedMessage = baos.toByteArray();
            pdu.encodedScAddress = null;
            return pdu;
        } catch (IOException ex) {
            Rlog.e(LOG_TAG, "creating SubmitPdu failed: " + ex);
            return null;
        }
    }

    public void createPdu() {
        SmsEnvelope env = this.mEnvelope;
        CdmaSmsAddress addr = env.origAddress;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(baos));
        try {
            dos.writeInt(env.messageType);
            dos.writeInt(env.teleService);
            dos.writeInt(env.serviceCategory);
            dos.writeByte(addr.digitMode);
            dos.writeByte(addr.numberMode);
            dos.writeByte(addr.ton);
            dos.writeByte(addr.numberPlan);
            dos.writeByte(addr.numberOfDigits);
            dos.write(addr.origBytes, 0, addr.origBytes.length);
            dos.writeInt(env.bearerReply);
            dos.writeByte(env.replySeqNo);
            dos.writeByte(env.errorClass);
            dos.writeByte(env.causeCode);
            dos.writeInt(env.bearerData.length);
            dos.write(env.bearerData, 0, env.bearerData.length);
            dos.close();
            this.mPdu = baos.toByteArray();
        } catch (IOException ex) {
            Rlog.e(LOG_TAG, "createPdu: conversion from object to byte array failed: " + ex);
        }
    }

    public static byte convertDtmfToAscii(byte dtmfDigit) {
        switch (dtmfDigit) {
            case 0:
                return (byte) 68;
            case 1:
                return (byte) 49;
            case 2:
                return (byte) 50;
            case 3:
                return (byte) 51;
            case 4:
                return (byte) 52;
            case 5:
                return (byte) 53;
            case 6:
                return (byte) 54;
            case 7:
                return (byte) 55;
            case 8:
                return (byte) 56;
            case 9:
                return (byte) 57;
            case 10:
                return (byte) 48;
            case 11:
                return (byte) 42;
            case 12:
                return (byte) 35;
            case 13:
                return (byte) 65;
            case 14:
                return (byte) 66;
            case 15:
                return (byte) 67;
            default:
                return (byte) 32;
        }
    }

    public int getNumOfVoicemails() {
        return this.mBearerData.numberOfMessages;
    }

    public byte[] getIncomingSmsFingerprint() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(this.mEnvelope.serviceCategory);
        output.write(this.mEnvelope.teleService);
        output.write(this.mEnvelope.origAddress.origBytes, 0, this.mEnvelope.origAddress.origBytes.length);
        output.write(this.mEnvelope.bearerData, 0, this.mEnvelope.bearerData.length);
        if (this.mEnvelope.origSubaddress != null && this.mEnvelope.origSubaddress.origBytes != null) {
            output.write(this.mEnvelope.origSubaddress.origBytes, 0, this.mEnvelope.origSubaddress.origBytes.length);
        }
        return output.toByteArray();
    }

    public ArrayList<CdmaSmsCbProgramData> getSmsCbProgramData() {
        return this.mBearerData.serviceCategoryProgramData;
    }
}
