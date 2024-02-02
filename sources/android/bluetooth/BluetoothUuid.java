package android.bluetooth;

import android.media.session.PlaybackState;
import android.os.ParcelUuid;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;
/* loaded from: classes.dex */
public final class BluetoothUuid {
    public static final int UUID_BYTES_128_BIT = 16;
    public static final int UUID_BYTES_16_BIT = 2;
    public static final int UUID_BYTES_32_BIT = 4;
    private protected static final ParcelUuid AudioSink = ParcelUuid.fromString("0000110B-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid AudioSource = ParcelUuid.fromString("0000110A-0000-1000-8000-00805F9B34FB");
    private protected static final ParcelUuid AdvAudioDist = ParcelUuid.fromString("0000110D-0000-1000-8000-00805F9B34FB");
    private protected static final ParcelUuid HSP = ParcelUuid.fromString("00001108-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid HSP_AG = ParcelUuid.fromString("00001112-0000-1000-8000-00805F9B34FB");
    private protected static final ParcelUuid Handsfree = ParcelUuid.fromString("0000111E-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid Handsfree_AG = ParcelUuid.fromString("0000111F-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid AvrcpController = ParcelUuid.fromString("0000110E-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid AvrcpTarget = ParcelUuid.fromString("0000110C-0000-1000-8000-00805F9B34FB");
    private protected static final ParcelUuid ObexObjectPush = ParcelUuid.fromString("00001105-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid Hid = ParcelUuid.fromString("00001124-0000-1000-8000-00805f9b34fb");
    private protected static final ParcelUuid Hogp = ParcelUuid.fromString("00001812-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid PANU = ParcelUuid.fromString("00001115-0000-1000-8000-00805F9B34FB");
    private protected static final ParcelUuid NAP = ParcelUuid.fromString("00001116-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid BNEP = ParcelUuid.fromString("0000000f-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid PBAP_PCE = ParcelUuid.fromString("0000112e-0000-1000-8000-00805F9B34FB");
    private protected static final ParcelUuid PBAP_PSE = ParcelUuid.fromString("0000112f-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid MAP = ParcelUuid.fromString("00001134-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid MNS = ParcelUuid.fromString("00001133-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid MAS = ParcelUuid.fromString("00001132-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid SAP = ParcelUuid.fromString("0000112D-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid HearingAid = ParcelUuid.fromString("0000FDF0-0000-1000-8000-00805f9b34fb");
    public static final ParcelUuid DIP = ParcelUuid.fromString("00001200-0000-1000-8000-00805F9B34FB");
    public static final ParcelUuid BASE_UUID = ParcelUuid.fromString("00000000-0000-1000-8000-00805F9B34FB");
    private protected static final ParcelUuid[] RESERVED_UUIDS = {AudioSink, AudioSource, AdvAudioDist, HSP, Handsfree, AvrcpController, AvrcpTarget, ObexObjectPush, PANU, NAP, MAP, MNS, MAS, SAP};

    private protected static boolean isAudioSource(ParcelUuid uuid) {
        return uuid.equals(AudioSource);
    }

    public static synchronized boolean isAudioSink(ParcelUuid uuid) {
        return uuid.equals(AudioSink);
    }

    private protected static boolean isAdvAudioDist(ParcelUuid uuid) {
        return uuid.equals(AdvAudioDist);
    }

    public static synchronized boolean isHandsfree(ParcelUuid uuid) {
        return uuid.equals(Handsfree);
    }

    public static synchronized boolean isHeadset(ParcelUuid uuid) {
        return uuid.equals(HSP);
    }

    public static synchronized boolean isAvrcpController(ParcelUuid uuid) {
        return uuid.equals(AvrcpController);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isAvrcpTarget(ParcelUuid uuid) {
        return uuid.equals(AvrcpTarget);
    }

    public static synchronized boolean isInputDevice(ParcelUuid uuid) {
        return uuid.equals(Hid);
    }

    public static synchronized boolean isPanu(ParcelUuid uuid) {
        return uuid.equals(PANU);
    }

    public static synchronized boolean isNap(ParcelUuid uuid) {
        return uuid.equals(NAP);
    }

    public static synchronized boolean isBnep(ParcelUuid uuid) {
        return uuid.equals(BNEP);
    }

    public static synchronized boolean isMap(ParcelUuid uuid) {
        return uuid.equals(MAP);
    }

    public static synchronized boolean isMns(ParcelUuid uuid) {
        return uuid.equals(MNS);
    }

    public static synchronized boolean isMas(ParcelUuid uuid) {
        return uuid.equals(MAS);
    }

    public static synchronized boolean isSap(ParcelUuid uuid) {
        return uuid.equals(SAP);
    }

    private protected static boolean isUuidPresent(ParcelUuid[] uuidArray, ParcelUuid uuid) {
        if ((uuidArray == null || uuidArray.length == 0) && uuid == null) {
            return true;
        }
        if (uuidArray == null) {
            return false;
        }
        for (ParcelUuid element : uuidArray) {
            if (element.equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    private protected static boolean containsAnyUuid(ParcelUuid[] uuidA, ParcelUuid[] uuidB) {
        if (uuidA == null && uuidB == null) {
            return true;
        }
        if (uuidA == null) {
            if (uuidB.length == 0) {
                return true;
            }
            return false;
        } else if (uuidB == null) {
            if (uuidA.length == 0) {
                return true;
            }
            return false;
        } else {
            HashSet<ParcelUuid> uuidSet = new HashSet<>(Arrays.asList(uuidA));
            for (ParcelUuid uuid : uuidB) {
                if (uuidSet.contains(uuid)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static synchronized boolean containsAllUuids(ParcelUuid[] uuidA, ParcelUuid[] uuidB) {
        if (uuidA == null && uuidB == null) {
            return true;
        }
        if (uuidA == null) {
            if (uuidB.length == 0) {
                return true;
            }
            return false;
        } else if (uuidB == null) {
            return true;
        } else {
            HashSet<ParcelUuid> uuidSet = new HashSet<>(Arrays.asList(uuidA));
            for (ParcelUuid uuid : uuidB) {
                if (!uuidSet.contains(uuid)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static synchronized int getServiceIdentifierFromParcelUuid(ParcelUuid parcelUuid) {
        UUID uuid = parcelUuid.getUuid();
        long value = (uuid.getMostSignificantBits() & (-4294967296L)) >>> 32;
        return (int) value;
    }

    public static synchronized ParcelUuid parseUuidFrom(byte[] uuidBytes) {
        long shortUuid;
        if (uuidBytes == null) {
            throw new IllegalArgumentException("uuidBytes cannot be null");
        }
        int length = uuidBytes.length;
        if (length == 2 || length == 4 || length == 16) {
            if (length == 16) {
                ByteBuffer buf = ByteBuffer.wrap(uuidBytes).order(ByteOrder.LITTLE_ENDIAN);
                long msb = buf.getLong(8);
                long lsb = buf.getLong(0);
                return new ParcelUuid(new UUID(msb, lsb));
            }
            if (length == 2) {
                long shortUuid2 = uuidBytes[0] & 255;
                shortUuid = shortUuid2 + ((uuidBytes[1] & 255) << 8);
            } else {
                long shortUuid3 = uuidBytes[0] & 255;
                shortUuid = ((uuidBytes[3] & 255) << 24) + shortUuid3 + ((uuidBytes[1] & 255) << 8) + ((uuidBytes[2] & 255) << 16);
            }
            long msb2 = BASE_UUID.getUuid().getMostSignificantBits() + (shortUuid << 32);
            long lsb2 = BASE_UUID.getUuid().getLeastSignificantBits();
            return new ParcelUuid(new UUID(msb2, lsb2));
        }
        throw new IllegalArgumentException("uuidBytes length invalid - " + length);
    }

    public static synchronized byte[] uuidToBytes(ParcelUuid uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("uuid cannot be null");
        }
        if (is16BitUuid(uuid)) {
            int uuidVal = getServiceIdentifierFromParcelUuid(uuid);
            return new byte[]{(byte) (uuidVal & 255), (byte) ((65280 & uuidVal) >> 8)};
        } else if (is32BitUuid(uuid)) {
            int uuidVal2 = getServiceIdentifierFromParcelUuid(uuid);
            return new byte[]{(byte) (uuidVal2 & 255), (byte) ((65280 & uuidVal2) >> 8), (byte) ((16711680 & uuidVal2) >> 16), (byte) (((-16777216) & uuidVal2) >> 24)};
        } else {
            long msb = uuid.getUuid().getMostSignificantBits();
            long lsb = uuid.getUuid().getLeastSignificantBits();
            byte[] uuidBytes = new byte[16];
            ByteBuffer buf = ByteBuffer.wrap(uuidBytes).order(ByteOrder.LITTLE_ENDIAN);
            buf.putLong(8, msb);
            buf.putLong(0, lsb);
            return uuidBytes;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean is16BitUuid(ParcelUuid parcelUuid) {
        UUID uuid = parcelUuid.getUuid();
        return uuid.getLeastSignificantBits() == BASE_UUID.getUuid().getLeastSignificantBits() && (uuid.getMostSignificantBits() & (-281470681743361L)) == PlaybackState.ACTION_SKIP_TO_QUEUE_ITEM;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean is32BitUuid(ParcelUuid parcelUuid) {
        UUID uuid = parcelUuid.getUuid();
        return uuid.getLeastSignificantBits() == BASE_UUID.getUuid().getLeastSignificantBits() && !is16BitUuid(parcelUuid) && (uuid.getMostSignificantBits() & 4294967295L) == PlaybackState.ACTION_SKIP_TO_QUEUE_ITEM;
    }
}
