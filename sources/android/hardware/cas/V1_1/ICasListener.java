package android.hardware.cas.V1_1;

import android.bluetooth.BluetoothHidDevice;
import android.internal.hidl.base.V1_0.DebugInfo;
import android.internal.hidl.base.V1_0.IBase;
import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwInterface;
import android.os.NativeHandle;
import android.os.RemoteException;
import com.android.internal.midi.MidiConstants;
import com.android.internal.telephony.GsmAlphabet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/* loaded from: classes.dex */
public interface ICasListener extends android.hardware.cas.V1_0.ICasListener {
    public static final String kInterfaceName = "android.hardware.cas@1.1::ICasListener";

    @Override // android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase, android.os.IHwInterface
    IHwBinder asBinder();

    @Override // android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
    void debug(NativeHandle nativeHandle, ArrayList<String> arrayList) throws RemoteException;

    @Override // android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
    DebugInfo getDebugInfo() throws RemoteException;

    @Override // android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
    ArrayList<byte[]> getHashChain() throws RemoteException;

    @Override // android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
    ArrayList<String> interfaceChain() throws RemoteException;

    @Override // android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
    String interfaceDescriptor() throws RemoteException;

    @Override // android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    @Override // android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
    void notifySyspropsChanged() throws RemoteException;

    void onSessionEvent(ArrayList<Byte> arrayList, int i, int i2, ArrayList<Byte> arrayList2) throws RemoteException;

    @Override // android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
    void ping() throws RemoteException;

    @Override // android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
    void setHALInstrumentation() throws RemoteException;

    @Override // android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
    boolean unlinkToDeath(IHwBinder.DeathRecipient deathRecipient) throws RemoteException;

    static ICasListener asInterface(IHwBinder binder) {
        if (binder == null) {
            return null;
        }
        IHwInterface iface = binder.queryLocalInterface(kInterfaceName);
        if (iface != null && (iface instanceof ICasListener)) {
            return (ICasListener) iface;
        }
        ICasListener proxy = new Proxy(binder);
        try {
            Iterator<String> it = proxy.interfaceChain().iterator();
            while (it.hasNext()) {
                String descriptor = it.next();
                if (descriptor.equals(kInterfaceName)) {
                    return proxy;
                }
            }
        } catch (RemoteException e) {
        }
        return null;
    }

    static ICasListener castFrom(IHwInterface iface) {
        if (iface == null) {
            return null;
        }
        return asInterface(iface.asBinder());
    }

    static ICasListener getService(String serviceName, boolean retry) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, serviceName, retry));
    }

    static ICasListener getService(boolean retry) throws RemoteException {
        return getService("default", retry);
    }

    static ICasListener getService(String serviceName) throws RemoteException {
        return asInterface(HwBinder.getService(kInterfaceName, serviceName));
    }

    static ICasListener getService() throws RemoteException {
        return getService("default");
    }

    /* loaded from: classes.dex */
    public static final class Proxy implements ICasListener {
        private IHwBinder mRemote;

        public Proxy(IHwBinder remote) {
            this.mRemote = (IHwBinder) Objects.requireNonNull(remote);
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase, android.os.IHwInterface
        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException e) {
                return "[class or subclass of android.hardware.cas@1.1::ICasListener]@Proxy";
            }
        }

        public final boolean equals(Object other) {
            return HidlSupport.interfacesEqual(this, other);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // android.hardware.cas.V1_0.ICasListener
        public void onEvent(int event, int arg, ArrayList<Byte> data) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(android.hardware.cas.V1_0.ICasListener.kInterfaceName);
            _hidl_request.writeInt32(event);
            _hidl_request.writeInt32(arg);
            _hidl_request.writeInt8Vector(data);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(1, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.cas.V1_1.ICasListener
        public void onSessionEvent(ArrayList<Byte> sessionId, int event, int arg, ArrayList<Byte> data) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(ICasListener.kInterfaceName);
            _hidl_request.writeInt8Vector(sessionId);
            _hidl_request.writeInt32(event);
            _hidl_request.writeInt32(arg);
            _hidl_request.writeInt8Vector(data);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(2, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public ArrayList<String> interfaceChain() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256067662, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                ArrayList<String> _hidl_out_descriptors = _hidl_reply.readStringVector();
                return _hidl_out_descriptors;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public void debug(NativeHandle fd, ArrayList<String> options) throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            _hidl_request.writeNativeHandle(fd);
            _hidl_request.writeStringVector(options);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256131655, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public String interfaceDescriptor() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256136003, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                String _hidl_out_descriptor = _hidl_reply.readString();
                return _hidl_out_descriptor;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public ArrayList<byte[]> getHashChain() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256398152, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                ArrayList<byte[]> _hidl_out_hashchain = new ArrayList<>();
                HwBlob _hidl_blob = _hidl_reply.readBuffer(16L);
                int _hidl_vec_size = _hidl_blob.getInt32(8L);
                HwBlob childBlob = _hidl_reply.readEmbeddedBuffer(_hidl_vec_size * 32, _hidl_blob.handle(), 0L, true);
                _hidl_out_hashchain.clear();
                for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                    byte[] _hidl_vec_element = new byte[32];
                    long _hidl_array_offset_1 = _hidl_index_0 * 32;
                    childBlob.copyToInt8Array(_hidl_array_offset_1, _hidl_vec_element, 32);
                    _hidl_out_hashchain.add(_hidl_vec_element);
                }
                return _hidl_out_hashchain;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public void setHALInstrumentation() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256462420, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public boolean linkToDeath(IHwBinder.DeathRecipient recipient, long cookie) throws RemoteException {
            return this.mRemote.linkToDeath(recipient, cookie);
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public void ping() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(256921159, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public DebugInfo getDebugInfo() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(257049926, _hidl_request, _hidl_reply, 0);
                _hidl_reply.verifySuccess();
                _hidl_request.releaseTemporaryStorage();
                DebugInfo _hidl_out_info = new DebugInfo();
                _hidl_out_info.readFromParcel(_hidl_reply);
                return _hidl_out_info;
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public void notifySyspropsChanged() throws RemoteException {
            HwParcel _hidl_request = new HwParcel();
            _hidl_request.writeInterfaceToken(IBase.kInterfaceName);
            HwParcel _hidl_reply = new HwParcel();
            try {
                this.mRemote.transact(257120595, _hidl_request, _hidl_reply, 1);
                _hidl_request.releaseTemporaryStorage();
            } finally {
                _hidl_reply.release();
            }
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public boolean unlinkToDeath(IHwBinder.DeathRecipient recipient) throws RemoteException {
            return this.mRemote.unlinkToDeath(recipient);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends HwBinder implements ICasListener {
        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase, android.os.IHwInterface
        public IHwBinder asBinder() {
            return this;
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public final ArrayList<String> interfaceChain() {
            return new ArrayList<>(Arrays.asList(ICasListener.kInterfaceName, android.hardware.cas.V1_0.ICasListener.kInterfaceName, IBase.kInterfaceName));
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public void debug(NativeHandle fd, ArrayList<String> options) {
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public final String interfaceDescriptor() {
            return ICasListener.kInterfaceName;
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public final ArrayList<byte[]> getHashChain() {
            return new ArrayList<>(Arrays.asList(new byte[]{37, 1, 45, 23, 120, -9, 57, 111, -106, 123, -68, 2, 49, 57, 125, 84, 75, -34, 66, GsmAlphabet.GSM_EXTENDED_ESCAPE, -91, -71, -121, 6, -55, -28, -118, -57, MidiConstants.STATUS_NOTE_ON, 97, 38, -125}, new byte[]{-72, BluetoothHidDevice.ERROR_RSP_UNKNOWN, 20, 86, -72, 31, Byte.MIN_VALUE, 3, 45, 13, -25, -53, 69, 101, 42, -63, 90, MidiConstants.STATUS_MIDI_TIME_CODE, 30, 116, 116, -43, 32, -41, 87, 72, 30, -54, -83, 121, 109, -1}, new byte[]{-20, Byte.MAX_VALUE, -41, -98, MidiConstants.STATUS_CHANNEL_PRESSURE, 45, -6, -123, -68, 73, -108, 38, -83, -82, 62, -66, 35, -17, 5, 36, MidiConstants.STATUS_SONG_SELECT, -51, 105, 87, 19, -109, 36, -72, 59, 24, -54, 76}));
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public final void setHALInstrumentation() {
        }

        @Override // android.os.IHwBinder, android.hardware.cas.V1_0.ICas, android.internal.hidl.base.V1_0.IBase
        public final boolean linkToDeath(IHwBinder.DeathRecipient recipient, long cookie) {
            return true;
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public final void ping() {
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public final DebugInfo getDebugInfo() {
            DebugInfo info = new DebugInfo();
            info.pid = HidlSupport.getPidIfSharable();
            info.ptr = 0L;
            info.arch = 0;
            return info;
        }

        @Override // android.hardware.cas.V1_1.ICasListener, android.hardware.cas.V1_0.ICasListener, android.internal.hidl.base.V1_0.IBase
        public final void notifySyspropsChanged() {
            HwBinder.enableInstrumentation();
        }

        @Override // android.os.IHwBinder, android.hardware.cas.V1_0.ICas, android.internal.hidl.base.V1_0.IBase
        public final boolean unlinkToDeath(IHwBinder.DeathRecipient recipient) {
            return true;
        }

        @Override // android.os.IHwBinder
        public IHwInterface queryLocalInterface(String descriptor) {
            if (ICasListener.kInterfaceName.equals(descriptor)) {
                return this;
            }
            return null;
        }

        public void registerAsService(String serviceName) throws RemoteException {
            registerService(serviceName);
        }

        public String toString() {
            return interfaceDescriptor() + "@Stub";
        }

        @Override // android.os.HwBinder
        public void onTransact(int _hidl_code, HwParcel _hidl_request, HwParcel _hidl_reply, int _hidl_flags) throws RemoteException {
            boolean _hidl_is_oneway;
            if (_hidl_code == 1) {
                int event = _hidl_flags & 1;
                _hidl_is_oneway = event != 0;
                if (_hidl_is_oneway) {
                    _hidl_reply.writeStatus(Integer.MIN_VALUE);
                    _hidl_reply.send();
                    return;
                }
                _hidl_request.enforceInterface(android.hardware.cas.V1_0.ICasListener.kInterfaceName);
                int event2 = _hidl_request.readInt32();
                int arg = _hidl_request.readInt32();
                ArrayList<Byte> data = _hidl_request.readInt8Vector();
                onEvent(event2, arg, data);
                _hidl_reply.writeStatus(0);
                _hidl_reply.send();
            } else if (_hidl_code == 2) {
                _hidl_is_oneway = (_hidl_flags & 1) != 0;
                if (_hidl_is_oneway) {
                    _hidl_reply.writeStatus(Integer.MIN_VALUE);
                    _hidl_reply.send();
                    return;
                }
                _hidl_request.enforceInterface(ICasListener.kInterfaceName);
                ArrayList<Byte> sessionId = _hidl_request.readInt8Vector();
                int event3 = _hidl_request.readInt32();
                int arg2 = _hidl_request.readInt32();
                ArrayList<Byte> data2 = _hidl_request.readInt8Vector();
                onSessionEvent(sessionId, event3, arg2, data2);
                _hidl_reply.writeStatus(0);
                _hidl_reply.send();
            } else {
                switch (_hidl_code) {
                    case 256067662:
                        if ((_hidl_flags & 1) != 0) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        _hidl_request.enforceInterface(IBase.kInterfaceName);
                        ArrayList<String> _hidl_out_descriptors = interfaceChain();
                        _hidl_reply.writeStatus(0);
                        _hidl_reply.writeStringVector(_hidl_out_descriptors);
                        _hidl_reply.send();
                        return;
                    case 256131655:
                        if ((_hidl_flags & 1) != 0) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        _hidl_request.enforceInterface(IBase.kInterfaceName);
                        NativeHandle fd = _hidl_request.readNativeHandle();
                        ArrayList<String> options = _hidl_request.readStringVector();
                        debug(fd, options);
                        _hidl_reply.writeStatus(0);
                        _hidl_reply.send();
                        return;
                    case 256136003:
                        if ((_hidl_flags & 1) != 0) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        _hidl_request.enforceInterface(IBase.kInterfaceName);
                        String _hidl_out_descriptor = interfaceDescriptor();
                        _hidl_reply.writeStatus(0);
                        _hidl_reply.writeString(_hidl_out_descriptor);
                        _hidl_reply.send();
                        return;
                    case 256398152:
                        if ((_hidl_flags & 1) != 0) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        _hidl_request.enforceInterface(IBase.kInterfaceName);
                        ArrayList<byte[]> _hidl_out_hashchain = getHashChain();
                        _hidl_reply.writeStatus(0);
                        HwBlob _hidl_blob = new HwBlob(16);
                        int _hidl_vec_size = _hidl_out_hashchain.size();
                        _hidl_blob.putInt32(8L, _hidl_vec_size);
                        _hidl_blob.putBool(12L, false);
                        HwBlob childBlob = new HwBlob(_hidl_vec_size * 32);
                        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
                            long _hidl_array_offset_1 = _hidl_index_0 * 32;
                            byte[] _hidl_array_item_1 = _hidl_out_hashchain.get(_hidl_index_0);
                            if (_hidl_array_item_1 == null || _hidl_array_item_1.length != 32) {
                                throw new IllegalArgumentException("Array element is not of the expected length");
                            }
                            childBlob.putInt8Array(_hidl_array_offset_1, _hidl_array_item_1);
                        }
                        _hidl_blob.putBlob(0L, childBlob);
                        _hidl_reply.writeBuffer(_hidl_blob);
                        _hidl_reply.send();
                        return;
                    case 256462420:
                        if (!((_hidl_flags & 1) != 0)) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        _hidl_request.enforceInterface(IBase.kInterfaceName);
                        setHALInstrumentation();
                        return;
                    case 256660548:
                        if ((_hidl_flags & 1) != 0) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        return;
                    case 256921159:
                        if ((_hidl_flags & 1) != 0) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        _hidl_request.enforceInterface(IBase.kInterfaceName);
                        ping();
                        _hidl_reply.writeStatus(0);
                        _hidl_reply.send();
                        return;
                    case 257049926:
                        if ((_hidl_flags & 1) != 0) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        _hidl_request.enforceInterface(IBase.kInterfaceName);
                        DebugInfo _hidl_out_info = getDebugInfo();
                        _hidl_reply.writeStatus(0);
                        _hidl_out_info.writeToParcel(_hidl_reply);
                        _hidl_reply.send();
                        return;
                    case 257120595:
                        if (!((_hidl_flags & 1) != 0)) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        _hidl_request.enforceInterface(IBase.kInterfaceName);
                        notifySyspropsChanged();
                        return;
                    case 257250372:
                        if ((_hidl_flags & 1) != 0) {
                            _hidl_reply.writeStatus(Integer.MIN_VALUE);
                            _hidl_reply.send();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }
}
