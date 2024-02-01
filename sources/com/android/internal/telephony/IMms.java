package com.android.internal.telephony;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes3.dex */
public interface IMms extends IInterface {
    synchronized Uri addMultimediaMessageDraft(String str, Uri uri) throws RemoteException;

    synchronized Uri addTextMessageDraft(String str, String str2, String str3) throws RemoteException;

    synchronized boolean archiveStoredConversation(String str, long j, boolean z) throws RemoteException;

    synchronized boolean deleteStoredConversation(String str, long j) throws RemoteException;

    synchronized boolean deleteStoredMessage(String str, Uri uri) throws RemoteException;

    synchronized void downloadMessage(int i, String str, String str2, Uri uri, Bundle bundle, PendingIntent pendingIntent) throws RemoteException;

    synchronized boolean getAutoPersisting() throws RemoteException;

    synchronized Bundle getCarrierConfigValues(int i) throws RemoteException;

    synchronized Uri importMultimediaMessage(String str, Uri uri, String str2, long j, boolean z, boolean z2) throws RemoteException;

    synchronized Uri importTextMessage(String str, String str2, int i, String str3, long j, boolean z, boolean z2) throws RemoteException;

    synchronized void sendMessage(int i, String str, Uri uri, String str2, Bundle bundle, PendingIntent pendingIntent) throws RemoteException;

    synchronized void sendStoredMessage(int i, String str, Uri uri, Bundle bundle, PendingIntent pendingIntent) throws RemoteException;

    synchronized void setAutoPersisting(String str, boolean z) throws RemoteException;

    synchronized boolean updateStoredMessageStatus(String str, Uri uri, ContentValues contentValues) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IMms {
        private static final String DESCRIPTOR = "com.android.internal.telephony.IMms";
        static final int TRANSACTION_addMultimediaMessageDraft = 11;
        static final int TRANSACTION_addTextMessageDraft = 10;
        static final int TRANSACTION_archiveStoredConversation = 9;
        static final int TRANSACTION_deleteStoredConversation = 7;
        static final int TRANSACTION_deleteStoredMessage = 6;
        static final int TRANSACTION_downloadMessage = 2;
        static final int TRANSACTION_getAutoPersisting = 14;
        static final int TRANSACTION_getCarrierConfigValues = 3;
        static final int TRANSACTION_importMultimediaMessage = 5;
        static final int TRANSACTION_importTextMessage = 4;
        static final int TRANSACTION_sendMessage = 1;
        static final int TRANSACTION_sendStoredMessage = 12;
        static final int TRANSACTION_setAutoPersisting = 13;
        static final int TRANSACTION_updateStoredMessageStatus = 8;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IMms asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IMms)) {
                return (IMms) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Uri _arg2;
            Bundle _arg4;
            Uri _arg3;
            Bundle _arg42;
            Uri _arg1;
            Uri _arg22;
            Bundle _arg32;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    String _arg12 = data.readString();
                    if (data.readInt() != 0) {
                        Uri _arg23 = Uri.CREATOR.createFromParcel(data);
                        _arg2 = _arg23;
                    } else {
                        _arg2 = null;
                    }
                    String _arg33 = data.readString();
                    if (data.readInt() != 0) {
                        Bundle _arg43 = Bundle.CREATOR.createFromParcel(data);
                        _arg4 = _arg43;
                    } else {
                        _arg4 = null;
                    }
                    PendingIntent _arg5 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    sendMessage(_arg0, _arg12, _arg2, _arg33, _arg4, _arg5);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    String _arg13 = data.readString();
                    String _arg24 = data.readString();
                    if (data.readInt() != 0) {
                        Uri _arg34 = Uri.CREATOR.createFromParcel(data);
                        _arg3 = _arg34;
                    } else {
                        _arg3 = null;
                    }
                    if (data.readInt() != 0) {
                        Bundle _arg44 = Bundle.CREATOR.createFromParcel(data);
                        _arg42 = _arg44;
                    } else {
                        _arg42 = null;
                    }
                    PendingIntent _arg52 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    downloadMessage(_arg02, _arg13, _arg24, _arg3, _arg42, _arg52);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    Bundle _result = getCarrierConfigValues(_arg03);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    String _arg14 = data.readString();
                    int _arg25 = data.readInt();
                    String _arg35 = data.readString();
                    long _arg45 = data.readLong();
                    boolean _arg53 = data.readInt() != 0;
                    boolean _arg6 = data.readInt() != 0;
                    Uri _result2 = importTextMessage(_arg04, _arg14, _arg25, _arg35, _arg45, _arg53, _arg6);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    Uri _arg15 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    String _arg26 = data.readString();
                    long _arg36 = data.readLong();
                    boolean _arg46 = data.readInt() != 0;
                    boolean _arg54 = data.readInt() != 0;
                    Uri _result3 = importMultimediaMessage(_arg05, _arg15, _arg26, _arg36, _arg46, _arg54);
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    Uri _arg16 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    boolean deleteStoredMessage = deleteStoredMessage(_arg06, _arg16);
                    reply.writeNoException();
                    reply.writeInt(deleteStoredMessage ? 1 : 0);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    long _arg17 = data.readLong();
                    boolean deleteStoredConversation = deleteStoredConversation(_arg07, _arg17);
                    reply.writeNoException();
                    reply.writeInt(deleteStoredConversation ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    ContentValues _arg27 = data.readInt() != 0 ? ContentValues.CREATOR.createFromParcel(data) : null;
                    boolean updateStoredMessageStatus = updateStoredMessageStatus(_arg08, _arg1, _arg27);
                    reply.writeNoException();
                    reply.writeInt(updateStoredMessageStatus ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    long _arg18 = data.readLong();
                    boolean _arg28 = data.readInt() != 0;
                    boolean archiveStoredConversation = archiveStoredConversation(_arg09, _arg18, _arg28);
                    reply.writeNoException();
                    reply.writeInt(archiveStoredConversation ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    String _arg19 = data.readString();
                    String _arg29 = data.readString();
                    Uri _result4 = addTextMessageDraft(_arg010, _arg19, _arg29);
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    Uri _arg110 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    Uri _result5 = addMultimediaMessageDraft(_arg011, _arg110);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    String _arg111 = data.readString();
                    if (data.readInt() != 0) {
                        Uri _arg210 = Uri.CREATOR.createFromParcel(data);
                        _arg22 = _arg210;
                    } else {
                        _arg22 = null;
                    }
                    if (data.readInt() != 0) {
                        Bundle _arg37 = Bundle.CREATOR.createFromParcel(data);
                        _arg32 = _arg37;
                    } else {
                        _arg32 = null;
                    }
                    PendingIntent _arg47 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    sendStoredMessage(_arg012, _arg111, _arg22, _arg32, _arg47);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    boolean _arg112 = data.readInt() != 0;
                    setAutoPersisting(_arg013, _arg112);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    boolean autoPersisting = getAutoPersisting();
                    reply.writeNoException();
                    reply.writeInt(autoPersisting ? 1 : 0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IMms {
            private IBinder mRemote;

            synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.android.internal.telephony.IMms
            public synchronized void sendMessage(int subId, String callingPkg, Uri contentUri, String locationUrl, Bundle configOverrides, PendingIntent sentIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    if (contentUri != null) {
                        _data.writeInt(1);
                        contentUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(locationUrl);
                    if (configOverrides != null) {
                        _data.writeInt(1);
                        configOverrides.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IMms
            public synchronized void downloadMessage(int subId, String callingPkg, String locationUrl, Uri contentUri, Bundle configOverrides, PendingIntent downloadedIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(locationUrl);
                    if (contentUri != null) {
                        _data.writeInt(1);
                        contentUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (configOverrides != null) {
                        _data.writeInt(1);
                        configOverrides.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (downloadedIntent != null) {
                        _data.writeInt(1);
                        downloadedIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IMms
            public synchronized Bundle getCarrierConfigValues(int subId) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IMms
            public synchronized Uri importTextMessage(String callingPkg, String address, int type, String text, long timestampMillis, boolean seen, boolean read) throws RemoteException {
                Uri _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(address);
                    _data.writeInt(type);
                    _data.writeString(text);
                    _data.writeLong(timestampMillis);
                    _data.writeInt(seen ? 1 : 0);
                    _data.writeInt(read ? 1 : 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Uri.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IMms
            public synchronized Uri importMultimediaMessage(String callingPkg, Uri contentUri, String messageId, long timestampSecs, boolean seen, boolean read) throws RemoteException {
                Uri _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    if (contentUri != null) {
                        _data.writeInt(1);
                        contentUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(messageId);
                    _data.writeLong(timestampSecs);
                    _data.writeInt(seen ? 1 : 0);
                    _data.writeInt(read ? 1 : 0);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Uri.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IMms
            public synchronized boolean deleteStoredMessage(String callingPkg, Uri messageUri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    if (messageUri != null) {
                        _data.writeInt(1);
                        messageUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IMms
            public synchronized boolean deleteStoredConversation(String callingPkg, long conversationId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeLong(conversationId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IMms
            public synchronized boolean updateStoredMessageStatus(String callingPkg, Uri messageUri, ContentValues statusValues) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    if (messageUri != null) {
                        _data.writeInt(1);
                        messageUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (statusValues != null) {
                        _data.writeInt(1);
                        statusValues.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IMms
            public synchronized boolean archiveStoredConversation(String callingPkg, long conversationId, boolean archived) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeLong(conversationId);
                    _data.writeInt(archived ? 1 : 0);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IMms
            public synchronized Uri addTextMessageDraft(String callingPkg, String address, String text) throws RemoteException {
                Uri _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(address);
                    _data.writeString(text);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Uri.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IMms
            public synchronized Uri addMultimediaMessageDraft(String callingPkg, Uri contentUri) throws RemoteException {
                Uri _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    if (contentUri != null) {
                        _data.writeInt(1);
                        contentUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Uri.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IMms
            public synchronized void sendStoredMessage(int subId, String callingPkg, Uri messageUri, Bundle configOverrides, PendingIntent sentIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    if (messageUri != null) {
                        _data.writeInt(1);
                        messageUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (configOverrides != null) {
                        _data.writeInt(1);
                        configOverrides.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IMms
            public synchronized void setAutoPersisting(String callingPkg, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IMms
            public synchronized boolean getAutoPersisting() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
