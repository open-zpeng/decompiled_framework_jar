package com.xiaopeng.xuimanager.karaoke;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.xiaopeng.xuimanager.karaoke.IKaraokeEventListener;
/* loaded from: classes3.dex */
public interface IKaraoke extends IInterface {
    int XMS_Create(String str, int i, String str2, IBinder iBinder) throws RemoteException;

    int XMS_Distroy(String str) throws RemoteException;

    int XMS_GetHandShakeStatus(String str) throws RemoteException;

    int XMS_GetMicPowerStatus(String str) throws RemoteException;

    int XMS_GetMicStatus(String str) throws RemoteException;

    int XMS_GetToken(String str) throws RemoteException;

    int XMS_GetVolume(String str, int i) throws RemoteException;

    int XMS_MicCreate(String str, int i, int i2, int i3) throws RemoteException;

    int XMS_MicDestroy(String str) throws RemoteException;

    int XMS_MicGetAvail(String str) throws RemoteException;

    int XMS_MicGetMinBuf(String str, int i, int i2) throws RemoteException;

    int XMS_MicRead(String str, byte[] bArr, int i) throws RemoteException;

    int XMS_Pause(String str) throws RemoteException;

    int XMS_PausePlay(String str) throws RemoteException;

    int XMS_Read(String str, byte[] bArr, int i) throws RemoteException;

    int XMS_RecCreate(String str, int i, int i2, int i3) throws RemoteException;

    int XMS_RecDestroy(String str) throws RemoteException;

    int XMS_RecGetAvail(String str) throws RemoteException;

    int XMS_RecGetMinBuf(String str, int i, int i2) throws RemoteException;

    int XMS_RecStart(String str) throws RemoteException;

    int XMS_RecStop(String str) throws RemoteException;

    void XMS_RegisterCallback(String str, IKaraokeEventListener iKaraokeEventListener) throws RemoteException;

    int XMS_Resume(String str) throws RemoteException;

    int XMS_ResumePlay(String str) throws RemoteException;

    int XMS_SaveRec(String str, int i, String str2, String str3) throws RemoteException;

    int XMS_SetRecMode(String str, int i) throws RemoteException;

    int XMS_SetSignedToken(String str, String str2) throws RemoteException;

    int XMS_SetVolume(String str, int i, int i2) throws RemoteException;

    void XMS_ShowToast(String str) throws RemoteException;

    int XMS_StopSaveRec(String str) throws RemoteException;

    int XMS_TrackCreate(String str, int i, int i2, int i3) throws RemoteException;

    int XMS_TrackDestroy(String str) throws RemoteException;

    int XMS_TrackGetAvail(String str) throws RemoteException;

    int XMS_TrackGetLatency(String str) throws RemoteException;

    int XMS_TrackGetMinBuf(String str, int i, int i2) throws RemoteException;

    void XMS_UnRegisterCallback(String str, IKaraokeEventListener iKaraokeEventListener) throws RemoteException;

    int XMS_Write(String str, byte[] bArr, int i, int i2) throws RemoteException;

    int XMS_aiWakeUp() throws RemoteException;

    int XMS_atlSwitch(boolean z) throws RemoteException;

    void XMS_connectMicFlow() throws RemoteException;

    String XMS_getBuyMicUrl() throws RemoteException;

    String XMS_getMicName() throws RemoteException;

    boolean XMS_hasAtl() throws RemoteException;

    boolean XMS_isAtlEnabled() throws RemoteException;

    void XMS_setAtlEnable(boolean z) throws RemoteException;

    int XMS_setMusicInfo(String[] strArr) throws RemoteException;

    int XMS_setMusicLyric(String[] strArr) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IKaraoke {
        private static final String DESCRIPTOR = "com.xiaopeng.xuimanager.karaoke.IKaraoke";
        static final int TRANSACTION_XMS_Create = 1;
        static final int TRANSACTION_XMS_Distroy = 4;
        static final int TRANSACTION_XMS_GetHandShakeStatus = 7;
        static final int TRANSACTION_XMS_GetMicPowerStatus = 9;
        static final int TRANSACTION_XMS_GetMicStatus = 8;
        static final int TRANSACTION_XMS_GetToken = 5;
        static final int TRANSACTION_XMS_GetVolume = 11;
        static final int TRANSACTION_XMS_MicCreate = 33;
        static final int TRANSACTION_XMS_MicDestroy = 36;
        static final int TRANSACTION_XMS_MicGetAvail = 34;
        static final int TRANSACTION_XMS_MicGetMinBuf = 32;
        static final int TRANSACTION_XMS_MicRead = 35;
        static final int TRANSACTION_XMS_Pause = 18;
        static final int TRANSACTION_XMS_PausePlay = 23;
        static final int TRANSACTION_XMS_Read = 30;
        static final int TRANSACTION_XMS_RecCreate = 27;
        static final int TRANSACTION_XMS_RecDestroy = 31;
        static final int TRANSACTION_XMS_RecGetAvail = 28;
        static final int TRANSACTION_XMS_RecGetMinBuf = 26;
        static final int TRANSACTION_XMS_RecStart = 20;
        static final int TRANSACTION_XMS_RecStop = 19;
        static final int TRANSACTION_XMS_RegisterCallback = 2;
        static final int TRANSACTION_XMS_Resume = 21;
        static final int TRANSACTION_XMS_ResumePlay = 22;
        static final int TRANSACTION_XMS_SaveRec = 24;
        static final int TRANSACTION_XMS_SetRecMode = 29;
        static final int TRANSACTION_XMS_SetSignedToken = 6;
        static final int TRANSACTION_XMS_SetVolume = 10;
        static final int TRANSACTION_XMS_ShowToast = 45;
        static final int TRANSACTION_XMS_StopSaveRec = 25;
        static final int TRANSACTION_XMS_TrackCreate = 13;
        static final int TRANSACTION_XMS_TrackDestroy = 17;
        static final int TRANSACTION_XMS_TrackGetAvail = 16;
        static final int TRANSACTION_XMS_TrackGetLatency = 14;
        static final int TRANSACTION_XMS_TrackGetMinBuf = 12;
        static final int TRANSACTION_XMS_UnRegisterCallback = 3;
        static final int TRANSACTION_XMS_Write = 15;
        static final int TRANSACTION_XMS_aiWakeUp = 38;
        static final int TRANSACTION_XMS_atlSwitch = 37;
        static final int TRANSACTION_XMS_connectMicFlow = 44;
        static final int TRANSACTION_XMS_getBuyMicUrl = 43;
        static final int TRANSACTION_XMS_getMicName = 40;
        static final int TRANSACTION_XMS_hasAtl = 39;
        static final int TRANSACTION_XMS_isAtlEnabled = 42;
        static final int TRANSACTION_XMS_setAtlEnable = 41;
        static final int TRANSACTION_XMS_setMusicInfo = 46;
        static final int TRANSACTION_XMS_setMusicLyric = 47;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IKaraoke asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IKaraoke)) {
                return (IKaraoke) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    int _arg1 = data.readInt();
                    String _arg2 = data.readString();
                    IBinder _arg3 = data.readStrongBinder();
                    int _result = XMS_Create(_arg02, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    IKaraokeEventListener _arg12 = IKaraokeEventListener.Stub.asInterface(data.readStrongBinder());
                    XMS_RegisterCallback(_arg03, _arg12);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    IKaraokeEventListener _arg13 = IKaraokeEventListener.Stub.asInterface(data.readStrongBinder());
                    XMS_UnRegisterCallback(_arg04, _arg13);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _result2 = XMS_Distroy(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _result3 = XMS_GetToken(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    String _arg14 = data.readString();
                    int _result4 = XMS_SetSignedToken(_arg05, _arg14);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _result5 = XMS_GetHandShakeStatus(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _result6 = XMS_GetMicStatus(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _result7 = XMS_GetMicPowerStatus(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    int _arg15 = data.readInt();
                    int _arg22 = data.readInt();
                    int _result8 = XMS_SetVolume(_arg06, _arg15, _arg22);
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    int _arg16 = data.readInt();
                    int _result9 = XMS_GetVolume(_arg07, _arg16);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    int _arg17 = data.readInt();
                    int _arg23 = data.readInt();
                    int _result10 = XMS_TrackGetMinBuf(_arg08, _arg17, _arg23);
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    int _arg18 = data.readInt();
                    int _arg24 = data.readInt();
                    int _arg32 = data.readInt();
                    int _result11 = XMS_TrackCreate(_arg09, _arg18, _arg24, _arg32);
                    reply.writeNoException();
                    reply.writeInt(_result11);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    int _result12 = XMS_TrackGetLatency(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result12);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    byte[] _arg19 = data.createByteArray();
                    int _arg25 = data.readInt();
                    int _arg33 = data.readInt();
                    int _result13 = XMS_Write(_arg010, _arg19, _arg25, _arg33);
                    reply.writeNoException();
                    reply.writeInt(_result13);
                    reply.writeByteArray(_arg19);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    int _result14 = XMS_TrackGetAvail(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result14);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _result15 = XMS_TrackDestroy(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result15);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    int _result16 = XMS_Pause(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result16);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    int _result17 = XMS_RecStop(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result17);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    int _result18 = XMS_RecStart(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result18);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    int _result19 = XMS_Resume(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result19);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    int _result20 = XMS_ResumePlay(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    int _result21 = XMS_PausePlay(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result21);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    int _arg110 = data.readInt();
                    String _arg26 = data.readString();
                    String _arg34 = data.readString();
                    int _result22 = XMS_SaveRec(_arg011, _arg110, _arg26, _arg34);
                    reply.writeNoException();
                    reply.writeInt(_result22);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    int _result23 = XMS_StopSaveRec(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result23);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    int _arg111 = data.readInt();
                    int _arg27 = data.readInt();
                    int _result24 = XMS_RecGetMinBuf(_arg012, _arg111, _arg27);
                    reply.writeNoException();
                    reply.writeInt(_result24);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    int _arg112 = data.readInt();
                    int _arg28 = data.readInt();
                    int _arg35 = data.readInt();
                    int _result25 = XMS_RecCreate(_arg013, _arg112, _arg28, _arg35);
                    reply.writeNoException();
                    reply.writeInt(_result25);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    int _result26 = XMS_RecGetAvail(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result26);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    int _arg113 = data.readInt();
                    int _result27 = XMS_SetRecMode(_arg014, _arg113);
                    reply.writeNoException();
                    reply.writeInt(_result27);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    byte[] _arg114 = data.createByteArray();
                    int _arg29 = data.readInt();
                    int _result28 = XMS_Read(_arg015, _arg114, _arg29);
                    reply.writeNoException();
                    reply.writeInt(_result28);
                    reply.writeByteArray(_arg114);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    int _result29 = XMS_RecDestroy(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result29);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    int _arg115 = data.readInt();
                    int _arg210 = data.readInt();
                    int _result30 = XMS_MicGetMinBuf(_arg016, _arg115, _arg210);
                    reply.writeNoException();
                    reply.writeInt(_result30);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg017 = data.readString();
                    int _arg116 = data.readInt();
                    int _arg211 = data.readInt();
                    int _arg36 = data.readInt();
                    int _result31 = XMS_MicCreate(_arg017, _arg116, _arg211, _arg36);
                    reply.writeNoException();
                    reply.writeInt(_result31);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    int _result32 = XMS_MicGetAvail(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result32);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    byte[] _arg117 = data.createByteArray();
                    int _arg212 = data.readInt();
                    int _result33 = XMS_MicRead(_arg018, _arg117, _arg212);
                    reply.writeNoException();
                    reply.writeInt(_result33);
                    reply.writeByteArray(_arg117);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    int _result34 = XMS_MicDestroy(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result34);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    int _result35 = XMS_atlSwitch(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result35);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    int _result36 = XMS_aiWakeUp();
                    reply.writeNoException();
                    reply.writeInt(_result36);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    boolean XMS_hasAtl = XMS_hasAtl();
                    reply.writeNoException();
                    reply.writeInt(XMS_hasAtl ? 1 : 0);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    String _result37 = XMS_getMicName();
                    reply.writeNoException();
                    reply.writeString(_result37);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    XMS_setAtlEnable(_arg0);
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    boolean XMS_isAtlEnabled = XMS_isAtlEnabled();
                    reply.writeNoException();
                    reply.writeInt(XMS_isAtlEnabled ? 1 : 0);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    String _result38 = XMS_getBuyMicUrl();
                    reply.writeNoException();
                    reply.writeString(_result38);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    XMS_connectMicFlow();
                    reply.writeNoException();
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    XMS_ShowToast(data.readString());
                    reply.writeNoException();
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    int _result39 = XMS_setMusicInfo(data.createStringArray());
                    reply.writeNoException();
                    reply.writeInt(_result39);
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    int _result40 = XMS_setMusicLyric(data.createStringArray());
                    reply.writeNoException();
                    reply.writeInt(_result40);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IKaraoke {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_Create(String pkgName, int mic, String midware, IBinder cbt) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(mic);
                    _data.writeString(midware);
                    _data.writeStrongBinder(cbt);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public void XMS_RegisterCallback(String pkgName, IKaraokeEventListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public void XMS_UnRegisterCallback(String pkgName, IKaraokeEventListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_Distroy(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_GetToken(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_SetSignedToken(String pkgName, String sToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeString(sToken);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_GetHandShakeStatus(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_GetMicStatus(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_GetMicPowerStatus(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_SetVolume(String pkgName, int type, int vol) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(type);
                    _data.writeInt(vol);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_GetVolume(String pkgName, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(type);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_TrackGetMinBuf(String pkgName, int sampleRate, int channel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(sampleRate);
                    _data.writeInt(channel);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_TrackCreate(String pkgName, int sampleRate, int channel, int bufSize) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(sampleRate);
                    _data.writeInt(channel);
                    _data.writeInt(bufSize);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_TrackGetLatency(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_Write(String pkgName, byte[] data, int off, int size) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeByteArray(data);
                    _data.writeInt(off);
                    _data.writeInt(size);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(data);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_TrackGetAvail(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_TrackDestroy(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_Pause(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_RecStop(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_RecStart(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_Resume(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_ResumePlay(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_PausePlay(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_SaveRec(String pkgName, int mode, String micPath, String mixPath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(mode);
                    _data.writeString(micPath);
                    _data.writeString(mixPath);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_StopSaveRec(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_RecGetMinBuf(String pkgName, int sampleRate, int channel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(sampleRate);
                    _data.writeInt(channel);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_RecCreate(String pkgName, int sampleRate, int channel, int bufSize) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(sampleRate);
                    _data.writeInt(channel);
                    _data.writeInt(bufSize);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_RecGetAvail(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_SetRecMode(String pkgName, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(mode);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_Read(String pkgName, byte[] data, int size) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeByteArray(data);
                    _data.writeInt(size);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(data);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_RecDestroy(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_MicGetMinBuf(String pkgName, int sampleRate, int channel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(sampleRate);
                    _data.writeInt(channel);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_MicCreate(String pkgName, int sampleRate, int channel, int bufSize) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeInt(sampleRate);
                    _data.writeInt(channel);
                    _data.writeInt(bufSize);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_MicGetAvail(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_MicRead(String pkgName, byte[] data, int size) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    _data.writeByteArray(data);
                    _data.writeInt(size);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(data);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_MicDestroy(String pkgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkgName);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_atlSwitch(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_aiWakeUp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public boolean XMS_hasAtl() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public String XMS_getMicName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public void XMS_setAtlEnable(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public boolean XMS_isAtlEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public String XMS_getBuyMicUrl() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public void XMS_connectMicFlow() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public void XMS_ShowToast(String toast) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(toast);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_setMusicInfo(String[] musicInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(musicInfo);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xuimanager.karaoke.IKaraoke
            public int XMS_setMusicLyric(String[] lyric) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(lyric);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
