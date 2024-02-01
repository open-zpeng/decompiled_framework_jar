package android.hardware.camera2.impl;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.MarshalRegistry;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.marshal.impl.MarshalQueryableArray;
import android.hardware.camera2.marshal.impl.MarshalQueryableBlackLevelPattern;
import android.hardware.camera2.marshal.impl.MarshalQueryableBoolean;
import android.hardware.camera2.marshal.impl.MarshalQueryableColorSpaceTransform;
import android.hardware.camera2.marshal.impl.MarshalQueryableEnum;
import android.hardware.camera2.marshal.impl.MarshalQueryableHighSpeedVideoConfiguration;
import android.hardware.camera2.marshal.impl.MarshalQueryableMeteringRectangle;
import android.hardware.camera2.marshal.impl.MarshalQueryableNativeByteToInteger;
import android.hardware.camera2.marshal.impl.MarshalQueryablePair;
import android.hardware.camera2.marshal.impl.MarshalQueryableParcelable;
import android.hardware.camera2.marshal.impl.MarshalQueryablePrimitive;
import android.hardware.camera2.marshal.impl.MarshalQueryableRange;
import android.hardware.camera2.marshal.impl.MarshalQueryableRecommendedStreamConfiguration;
import android.hardware.camera2.marshal.impl.MarshalQueryableRect;
import android.hardware.camera2.marshal.impl.MarshalQueryableReprocessFormatsMap;
import android.hardware.camera2.marshal.impl.MarshalQueryableRggbChannelVector;
import android.hardware.camera2.marshal.impl.MarshalQueryableSize;
import android.hardware.camera2.marshal.impl.MarshalQueryableSizeF;
import android.hardware.camera2.marshal.impl.MarshalQueryableStreamConfiguration;
import android.hardware.camera2.marshal.impl.MarshalQueryableStreamConfigurationDuration;
import android.hardware.camera2.marshal.impl.MarshalQueryableString;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.HighSpeedVideoConfiguration;
import android.hardware.camera2.params.LensShadingMap;
import android.hardware.camera2.params.MandatoryStreamCombination;
import android.hardware.camera2.params.OisSample;
import android.hardware.camera2.params.RecommendedStreamConfiguration;
import android.hardware.camera2.params.RecommendedStreamConfigurationMap;
import android.hardware.camera2.params.ReprocessFormatsMap;
import android.hardware.camera2.params.StreamConfiguration;
import android.hardware.camera2.params.StreamConfigurationDuration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.params.TonemapCurve;
import android.hardware.camera2.utils.TypeReference;
import android.location.Location;
import android.location.LocationManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ServiceSpecificException;
import android.util.Log;
import android.util.Size;
import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class CameraMetadataNative implements Parcelable {
    private static final String CELLID_PROCESS = "CELLID";
    private static final boolean DEBUG = false;
    private static final int FACE_LANDMARK_SIZE = 6;
    private static final String GPS_PROCESS = "GPS";
    public static final int NATIVE_JPEG_FORMAT = 33;
    public static final int NUM_TYPES = 6;
    private static final String TAG = "CameraMetadataJV";
    public static final int TYPE_BYTE = 0;
    public static final int TYPE_DOUBLE = 4;
    public static final int TYPE_FLOAT = 2;
    public static final int TYPE_INT32 = 1;
    public static final int TYPE_INT64 = 3;
    public static final int TYPE_RATIONAL = 5;
    private static final HashMap<Key<?>, SetCommand> sSetCommandMap;
    private int mCameraId;
    private Size mDisplaySize;
    @UnsupportedAppUsage
    private long mMetadataPtr;
    public static final Parcelable.Creator<CameraMetadataNative> CREATOR = new Parcelable.Creator<CameraMetadataNative>() { // from class: android.hardware.camera2.impl.CameraMetadataNative.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CameraMetadataNative createFromParcel(Parcel in) {
            CameraMetadataNative metadata = new CameraMetadataNative();
            metadata.readFromParcel(in);
            return metadata;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CameraMetadataNative[] newArray(int size) {
            return new CameraMetadataNative[size];
        }
    };
    private static final HashMap<Key<?>, GetCommand> sGetCommandMap = new HashMap<>();

    private native long nativeAllocate();

    private native long nativeAllocateCopy(CameraMetadataNative cameraMetadataNative) throws NullPointerException;

    private native synchronized void nativeClose();

    private native synchronized void nativeDump() throws IOException;

    private native synchronized ArrayList nativeGetAllVendorKeys(Class cls);

    private native synchronized int nativeGetEntryCount();

    private static native int nativeGetTagFromKey(String str, long j) throws IllegalArgumentException;

    @UnsupportedAppUsage
    private native synchronized int nativeGetTagFromKeyLocal(String str) throws IllegalArgumentException;

    private static native int nativeGetTypeFromTag(int i, long j) throws IllegalArgumentException;

    @UnsupportedAppUsage
    private native synchronized int nativeGetTypeFromTagLocal(int i) throws IllegalArgumentException;

    private native synchronized boolean nativeIsEmpty();

    private native synchronized void nativeReadFromParcel(Parcel parcel);

    @UnsupportedAppUsage
    private native synchronized byte[] nativeReadValues(int i);

    private static native int nativeSetupGlobalVendorTagDescriptor();

    private native synchronized void nativeSwap(CameraMetadataNative cameraMetadataNative) throws NullPointerException;

    private native synchronized void nativeWriteToParcel(Parcel parcel);

    private native synchronized void nativeWriteValues(int i, byte[] bArr);

    /* loaded from: classes.dex */
    public static class Key<T> {
        private final String mFallbackName;
        private boolean mHasTag;
        private final int mHash;
        private final String mName;
        private int mTag;
        private final Class<T> mType;
        private final TypeReference<T> mTypeReference;
        private long mVendorId;

        public Key(String name, Class<T> type, long vendorId) {
            this.mVendorId = Long.MAX_VALUE;
            if (name == null) {
                throw new NullPointerException("Key needs a valid name");
            }
            if (type == null) {
                throw new NullPointerException("Type needs to be non-null");
            }
            this.mName = name;
            this.mFallbackName = null;
            this.mType = type;
            this.mVendorId = vendorId;
            this.mTypeReference = TypeReference.createSpecializedTypeReference((Class) type);
            this.mHash = this.mName.hashCode() ^ this.mTypeReference.hashCode();
        }

        public Key(String name, String fallbackName, Class<T> type) {
            this.mVendorId = Long.MAX_VALUE;
            if (name == null) {
                throw new NullPointerException("Key needs a valid name");
            }
            if (type == null) {
                throw new NullPointerException("Type needs to be non-null");
            }
            this.mName = name;
            this.mFallbackName = fallbackName;
            this.mType = type;
            this.mTypeReference = TypeReference.createSpecializedTypeReference((Class) type);
            this.mHash = this.mName.hashCode() ^ this.mTypeReference.hashCode();
        }

        public Key(String name, Class<T> type) {
            this.mVendorId = Long.MAX_VALUE;
            if (name == null) {
                throw new NullPointerException("Key needs a valid name");
            }
            if (type == null) {
                throw new NullPointerException("Type needs to be non-null");
            }
            this.mName = name;
            this.mFallbackName = null;
            this.mType = type;
            this.mTypeReference = TypeReference.createSpecializedTypeReference((Class) type);
            this.mHash = this.mName.hashCode() ^ this.mTypeReference.hashCode();
        }

        public Key(String name, TypeReference<T> typeReference) {
            this.mVendorId = Long.MAX_VALUE;
            if (name == null) {
                throw new NullPointerException("Key needs a valid name");
            }
            if (typeReference == null) {
                throw new NullPointerException("TypeReference needs to be non-null");
            }
            this.mName = name;
            this.mFallbackName = null;
            this.mType = (Class<? super T>) typeReference.getRawType();
            this.mTypeReference = typeReference;
            this.mHash = this.mName.hashCode() ^ this.mTypeReference.hashCode();
        }

        public final String getName() {
            return this.mName;
        }

        public final int hashCode() {
            return this.mHash;
        }

        public final boolean equals(Object o) {
            Key<?> lhs;
            if (this == o) {
                return true;
            }
            if (o == null || hashCode() != o.hashCode()) {
                return false;
            }
            if (o instanceof CaptureResult.Key) {
                lhs = ((CaptureResult.Key) o).getNativeKey();
            } else if (o instanceof CaptureRequest.Key) {
                lhs = ((CaptureRequest.Key) o).getNativeKey();
            } else if (o instanceof CameraCharacteristics.Key) {
                lhs = ((CameraCharacteristics.Key) o).getNativeKey();
            } else if (!(o instanceof Key)) {
                return false;
            } else {
                lhs = (Key) o;
            }
            if (this.mName.equals(lhs.mName) && this.mTypeReference.equals(lhs.mTypeReference)) {
                return true;
            }
            return false;
        }

        @UnsupportedAppUsage
        public final int getTag() {
            if (!this.mHasTag) {
                this.mTag = CameraMetadataNative.getTag(this.mName, this.mVendorId);
                this.mHasTag = true;
            }
            return this.mTag;
        }

        public final Class<T> getType() {
            return this.mType;
        }

        public final long getVendorId() {
            return this.mVendorId;
        }

        public final TypeReference<T> getTypeReference() {
            return this.mTypeReference;
        }
    }

    private static String translateLocationProviderToProcess(String provider) {
        if (provider == null) {
            return null;
        }
        char c = 65535;
        int hashCode = provider.hashCode();
        if (hashCode != 102570) {
            if (hashCode == 1843485230 && provider.equals(LocationManager.NETWORK_PROVIDER)) {
                c = 1;
            }
        } else if (provider.equals(LocationManager.GPS_PROVIDER)) {
            c = 0;
        }
        if (c != 0) {
            if (c != 1) {
                return null;
            }
            return CELLID_PROCESS;
        }
        return GPS_PROCESS;
    }

    private static String translateProcessToLocationProvider(String process) {
        if (process == null) {
            return null;
        }
        char c = 65535;
        int hashCode = process.hashCode();
        if (hashCode != 70794) {
            if (hashCode == 1984215549 && process.equals(CELLID_PROCESS)) {
                c = 1;
            }
        } else if (process.equals(GPS_PROCESS)) {
            c = 0;
        }
        if (c != 0) {
            if (c != 1) {
                return null;
            }
            return LocationManager.NETWORK_PROVIDER;
        }
        return LocationManager.GPS_PROVIDER;
    }

    public CameraMetadataNative() {
        this.mCameraId = -1;
        this.mDisplaySize = new Size(0, 0);
        this.mMetadataPtr = nativeAllocate();
        if (this.mMetadataPtr == 0) {
            throw new OutOfMemoryError("Failed to allocate native CameraMetadata");
        }
    }

    public CameraMetadataNative(CameraMetadataNative other) {
        this.mCameraId = -1;
        this.mDisplaySize = new Size(0, 0);
        this.mMetadataPtr = nativeAllocateCopy(other);
        if (this.mMetadataPtr == 0) {
            throw new OutOfMemoryError("Failed to allocate native CameraMetadata");
        }
    }

    public static CameraMetadataNative move(CameraMetadataNative other) {
        CameraMetadataNative newObject = new CameraMetadataNative();
        newObject.swap(other);
        return newObject;
    }

    static {
        sGetCommandMap.put(CameraCharacteristics.SCALER_AVAILABLE_FORMATS.getNativeKey(), new GetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.2
            @Override // android.hardware.camera2.impl.GetCommand
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return (T) metadata.getAvailableFormats();
            }
        });
        sGetCommandMap.put(CaptureResult.STATISTICS_FACES.getNativeKey(), new GetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.3
            @Override // android.hardware.camera2.impl.GetCommand
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return (T) metadata.getFaces();
            }
        });
        sGetCommandMap.put(CaptureResult.STATISTICS_FACE_RECTANGLES.getNativeKey(), new GetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.4
            @Override // android.hardware.camera2.impl.GetCommand
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return (T) metadata.getFaceRectangles();
            }
        });
        sGetCommandMap.put(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP.getNativeKey(), new GetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.5
            @Override // android.hardware.camera2.impl.GetCommand
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return (T) metadata.getStreamConfigurationMap();
            }
        });
        sGetCommandMap.put(CameraCharacteristics.SCALER_MANDATORY_STREAM_COMBINATIONS.getNativeKey(), new GetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.6
            @Override // android.hardware.camera2.impl.GetCommand
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return (T) metadata.getMandatoryStreamCombinations();
            }
        });
        sGetCommandMap.put(CameraCharacteristics.CONTROL_MAX_REGIONS_AE.getNativeKey(), new GetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.7
            @Override // android.hardware.camera2.impl.GetCommand
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return (T) metadata.getMaxRegions(key);
            }
        });
        sGetCommandMap.put(CameraCharacteristics.CONTROL_MAX_REGIONS_AWB.getNativeKey(), new GetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.8
            @Override // android.hardware.camera2.impl.GetCommand
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return (T) metadata.getMaxRegions(key);
            }
        });
        sGetCommandMap.put(CameraCharacteristics.CONTROL_MAX_REGIONS_AF.getNativeKey(), new GetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.9
            @Override // android.hardware.camera2.impl.GetCommand
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return (T) metadata.getMaxRegions(key);
            }
        });
        sGetCommandMap.put(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_RAW.getNativeKey(), new GetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.10
            @Override // android.hardware.camera2.impl.GetCommand
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return (T) metadata.getMaxNumOutputs(key);
            }
        });
        sGetCommandMap.put(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC.getNativeKey(), new GetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.11
            @Override // android.hardware.camera2.impl.GetCommand
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return (T) metadata.getMaxNumOutputs(key);
            }
        });
        sGetCommandMap.put(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC_STALLING.getNativeKey(), new GetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.12
            @Override // android.hardware.camera2.impl.GetCommand
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return (T) metadata.getMaxNumOutputs(key);
            }
        });
        sGetCommandMap.put(CaptureRequest.TONEMAP_CURVE.getNativeKey(), new GetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.13
            @Override // android.hardware.camera2.impl.GetCommand
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return (T) metadata.getTonemapCurve();
            }
        });
        sGetCommandMap.put(CaptureResult.JPEG_GPS_LOCATION.getNativeKey(), new GetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.14
            @Override // android.hardware.camera2.impl.GetCommand
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return (T) metadata.getGpsLocation();
            }
        });
        sGetCommandMap.put(CaptureResult.STATISTICS_LENS_SHADING_CORRECTION_MAP.getNativeKey(), new GetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.15
            @Override // android.hardware.camera2.impl.GetCommand
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return (T) metadata.getLensShadingMap();
            }
        });
        sGetCommandMap.put(CaptureResult.STATISTICS_OIS_SAMPLES.getNativeKey(), new GetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.16
            @Override // android.hardware.camera2.impl.GetCommand
            public <T> T getValue(CameraMetadataNative metadata, Key<T> key) {
                return (T) metadata.getOisSamples();
            }
        });
        sSetCommandMap = new HashMap<>();
        sSetCommandMap.put(CameraCharacteristics.SCALER_AVAILABLE_FORMATS.getNativeKey(), new SetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.17
            @Override // android.hardware.camera2.impl.SetCommand
            public <T> void setValue(CameraMetadataNative metadata, T value) {
                metadata.setAvailableFormats((int[]) value);
            }
        });
        sSetCommandMap.put(CaptureResult.STATISTICS_FACE_RECTANGLES.getNativeKey(), new SetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.18
            @Override // android.hardware.camera2.impl.SetCommand
            public <T> void setValue(CameraMetadataNative metadata, T value) {
                metadata.setFaceRectangles((Rect[]) value);
            }
        });
        sSetCommandMap.put(CaptureResult.STATISTICS_FACES.getNativeKey(), new SetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.19
            @Override // android.hardware.camera2.impl.SetCommand
            public <T> void setValue(CameraMetadataNative metadata, T value) {
                metadata.setFaces((Face[]) value);
            }
        });
        sSetCommandMap.put(CaptureRequest.TONEMAP_CURVE.getNativeKey(), new SetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.20
            @Override // android.hardware.camera2.impl.SetCommand
            public <T> void setValue(CameraMetadataNative metadata, T value) {
                metadata.setTonemapCurve((TonemapCurve) value);
            }
        });
        sSetCommandMap.put(CaptureResult.JPEG_GPS_LOCATION.getNativeKey(), new SetCommand() { // from class: android.hardware.camera2.impl.CameraMetadataNative.21
            @Override // android.hardware.camera2.impl.SetCommand
            public <T> void setValue(CameraMetadataNative metadata, T value) {
                metadata.setGpsLocation((Location) value);
            }
        });
        registerAllMarshalers();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        nativeWriteToParcel(dest);
    }

    public <T> T get(CameraCharacteristics.Key<T> key) {
        return (T) get(key.getNativeKey());
    }

    public <T> T get(CaptureResult.Key<T> key) {
        return (T) get(key.getNativeKey());
    }

    public <T> T get(CaptureRequest.Key<T> key) {
        return (T) get(key.getNativeKey());
    }

    public <T> T get(Key<T> key) {
        Preconditions.checkNotNull(key, "key must not be null");
        GetCommand g = sGetCommandMap.get(key);
        if (g != null) {
            return (T) g.getValue(this, key);
        }
        return (T) getBase(key);
    }

    public void readFromParcel(Parcel in) {
        nativeReadFromParcel(in);
    }

    public static void setupGlobalVendorTagDescriptor() throws ServiceSpecificException {
        int err = nativeSetupGlobalVendorTagDescriptor();
        if (err != 0) {
            throw new ServiceSpecificException(err, "Failure to set up global vendor tags");
        }
    }

    public <T> void set(Key<T> key, T value) {
        SetCommand s = sSetCommandMap.get(key);
        if (s != null) {
            s.setValue(this, value);
        } else {
            setBase((Key<Key<T>>) key, (Key<T>) value);
        }
    }

    public <T> void set(CaptureRequest.Key<T> key, T value) {
        set((Key<Key<T>>) key.getNativeKey(), (Key<T>) value);
    }

    public <T> void set(CaptureResult.Key<T> key, T value) {
        set((Key<Key<T>>) key.getNativeKey(), (Key<T>) value);
    }

    public <T> void set(CameraCharacteristics.Key<T> key, T value) {
        set((Key<Key<T>>) key.getNativeKey(), (Key<T>) value);
    }

    private void close() {
        nativeClose();
        this.mMetadataPtr = 0L;
    }

    private <T> T getBase(CameraCharacteristics.Key<T> key) {
        return (T) getBase(key.getNativeKey());
    }

    private <T> T getBase(CaptureResult.Key<T> key) {
        return (T) getBase(key.getNativeKey());
    }

    private <T> T getBase(CaptureRequest.Key<T> key) {
        return (T) getBase(key.getNativeKey());
    }

    private <T> T getBase(Key<T> key) {
        int tag = nativeGetTagFromKeyLocal(key.getName());
        byte[] values = readValues(tag);
        if (values == null && (((Key) key).mFallbackName == null || (values = readValues((tag = nativeGetTagFromKeyLocal(((Key) key).mFallbackName)))) == null)) {
            return null;
        }
        int nativeType = nativeGetTypeFromTagLocal(tag);
        Marshaler<T> marshaler = getMarshalerForKey(key, nativeType);
        ByteBuffer buffer = ByteBuffer.wrap(values).order(ByteOrder.nativeOrder());
        return marshaler.unmarshal(buffer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] getAvailableFormats() {
        int[] availableFormats = (int[]) getBase(CameraCharacteristics.SCALER_AVAILABLE_FORMATS);
        if (availableFormats != null) {
            for (int i = 0; i < availableFormats.length; i++) {
                if (availableFormats[i] == 33) {
                    availableFormats[i] = 256;
                }
            }
        }
        return availableFormats;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setFaces(Face[] faces) {
        if (faces == null) {
            return false;
        }
        int numFaces = faces.length;
        boolean fullMode = true;
        int numFaces2 = numFaces;
        for (Face face : faces) {
            if (face == null) {
                numFaces2--;
                Log.w(TAG, "setFaces - null face detected, skipping");
            } else if (face.getId() == -1) {
                fullMode = false;
            }
        }
        Rect[] faceRectangles = new Rect[numFaces2];
        byte[] faceScores = new byte[numFaces2];
        int[] faceIds = null;
        int[] faceLandmarks = null;
        if (fullMode) {
            faceIds = new int[numFaces2];
            faceLandmarks = new int[numFaces2 * 6];
        }
        int i = 0;
        for (Face face2 : faces) {
            if (face2 != null) {
                faceRectangles[i] = face2.getBounds();
                faceScores[i] = (byte) face2.getScore();
                if (fullMode) {
                    faceIds[i] = face2.getId();
                    int j = 0 + 1;
                    faceLandmarks[(i * 6) + 0] = face2.getLeftEyePosition().x;
                    int j2 = j + 1;
                    faceLandmarks[(i * 6) + j] = face2.getLeftEyePosition().y;
                    int j3 = j2 + 1;
                    faceLandmarks[(i * 6) + j2] = face2.getRightEyePosition().x;
                    int j4 = j3 + 1;
                    faceLandmarks[(i * 6) + j3] = face2.getRightEyePosition().y;
                    int j5 = j4 + 1;
                    faceLandmarks[(i * 6) + j4] = face2.getMouthPosition().x;
                    int i2 = j5 + 1;
                    faceLandmarks[(i * 6) + j5] = face2.getMouthPosition().y;
                }
                i++;
            }
        }
        set((CaptureResult.Key<CaptureResult.Key<Rect[]>>) CaptureResult.STATISTICS_FACE_RECTANGLES, (CaptureResult.Key<Rect[]>) faceRectangles);
        set((CaptureResult.Key<CaptureResult.Key<int[]>>) CaptureResult.STATISTICS_FACE_IDS, (CaptureResult.Key<int[]>) faceIds);
        set((CaptureResult.Key<CaptureResult.Key<int[]>>) CaptureResult.STATISTICS_FACE_LANDMARKS, (CaptureResult.Key<int[]>) faceLandmarks);
        set((CaptureResult.Key<CaptureResult.Key<byte[]>>) CaptureResult.STATISTICS_FACE_SCORES, (CaptureResult.Key<byte[]>) faceScores);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Face[] getFaces() {
        Integer faceDetectMode = (Integer) get(CaptureResult.STATISTICS_FACE_DETECT_MODE);
        byte[] faceScores = (byte[]) get(CaptureResult.STATISTICS_FACE_SCORES);
        Rect[] faceRectangles = (Rect[]) get(CaptureResult.STATISTICS_FACE_RECTANGLES);
        int[] faceIds = (int[]) get(CaptureResult.STATISTICS_FACE_IDS);
        int[] faceLandmarks = (int[]) get(CaptureResult.STATISTICS_FACE_LANDMARKS);
        byte b = 1;
        if (areValuesAllNull(faceDetectMode, faceScores, faceRectangles, faceIds, faceLandmarks)) {
            return null;
        }
        if (faceDetectMode == null) {
            Log.w(TAG, "Face detect mode metadata is null, assuming the mode is SIMPLE");
            faceDetectMode = 1;
        } else if (faceDetectMode.intValue() > 2) {
            faceDetectMode = 2;
        } else if (faceDetectMode.intValue() == 0) {
            return new Face[0];
        } else {
            if (faceDetectMode.intValue() != 1 && faceDetectMode.intValue() != 2) {
                Log.w(TAG, "Unknown face detect mode: " + faceDetectMode);
                return new Face[0];
            }
        }
        if (faceScores == null || faceRectangles == null) {
            Log.w(TAG, "Expect face scores and rectangles to be non-null");
            return new Face[0];
        }
        if (faceScores.length != faceRectangles.length) {
            Log.w(TAG, String.format("Face score size(%d) doesn match face rectangle size(%d)!", Integer.valueOf(faceScores.length), Integer.valueOf(faceRectangles.length)));
        }
        int numFaces = Math.min(faceScores.length, faceRectangles.length);
        if (faceDetectMode.intValue() == 2) {
            if (faceIds == null || faceLandmarks == null) {
                Log.w(TAG, "Expect face ids and landmarks to be non-null for FULL mode,fallback to SIMPLE mode");
                faceDetectMode = 1;
            } else {
                if (faceIds.length != numFaces || faceLandmarks.length != numFaces * 6) {
                    Log.w(TAG, String.format("Face id size(%d), or face landmark size(%d) don'tmatch face number(%d)!", Integer.valueOf(faceIds.length), Integer.valueOf(faceLandmarks.length * 6), Integer.valueOf(numFaces)));
                }
                numFaces = Math.min(Math.min(numFaces, faceIds.length), faceLandmarks.length / 6);
            }
        }
        ArrayList<Face> faceList = new ArrayList<>();
        byte b2 = 100;
        if (faceDetectMode.intValue() == 1) {
            for (int i = 0; i < numFaces; i++) {
                if (faceScores[i] <= 100 && faceScores[i] >= 1) {
                    faceList.add(new Face(faceRectangles[i], faceScores[i]));
                }
            }
        } else {
            int i2 = 0;
            while (i2 < numFaces) {
                if (faceScores[i2] <= b2 && faceScores[i2] >= b && faceIds[i2] >= 0) {
                    Point leftEye = new Point(faceLandmarks[i2 * 6], faceLandmarks[(i2 * 6) + 1]);
                    Point rightEye = new Point(faceLandmarks[(i2 * 6) + 2], faceLandmarks[(i2 * 6) + 3]);
                    Point mouth = new Point(faceLandmarks[(i2 * 6) + 4], faceLandmarks[(i2 * 6) + 5]);
                    Face face = new Face(faceRectangles[i2], faceScores[i2], faceIds[i2], leftEye, rightEye, mouth);
                    faceList.add(face);
                }
                i2++;
                b = 1;
                b2 = 100;
            }
        }
        Face[] faces = new Face[faceList.size()];
        faceList.toArray(faces);
        return faces;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rect[] getFaceRectangles() {
        Rect[] faceRectangles = (Rect[]) getBase(CaptureResult.STATISTICS_FACE_RECTANGLES);
        if (faceRectangles == null) {
            return null;
        }
        Rect[] fixedFaceRectangles = new Rect[faceRectangles.length];
        for (int i = 0; i < faceRectangles.length; i++) {
            fixedFaceRectangles[i] = new Rect(faceRectangles[i].left, faceRectangles[i].top, faceRectangles[i].right - faceRectangles[i].left, faceRectangles[i].bottom - faceRectangles[i].top);
        }
        return fixedFaceRectangles;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LensShadingMap getLensShadingMap() {
        float[] lsmArray = (float[]) getBase(CaptureResult.STATISTICS_LENS_SHADING_MAP);
        Size s = (Size) get(CameraCharacteristics.LENS_INFO_SHADING_MAP_SIZE);
        if (lsmArray == null) {
            return null;
        }
        if (s == null) {
            Log.w(TAG, "getLensShadingMap - Lens shading map size was null.");
            return null;
        }
        LensShadingMap map = new LensShadingMap(lsmArray, s.getHeight(), s.getWidth());
        return map;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Location getGpsLocation() {
        String processingMethod = (String) get(CaptureResult.JPEG_GPS_PROCESSING_METHOD);
        double[] coords = (double[]) get(CaptureResult.JPEG_GPS_COORDINATES);
        Long timeStamp = (Long) get(CaptureResult.JPEG_GPS_TIMESTAMP);
        if (areValuesAllNull(processingMethod, coords, timeStamp)) {
            return null;
        }
        Location l = new Location(translateProcessToLocationProvider(processingMethod));
        if (timeStamp == null) {
            Log.w(TAG, "getGpsLocation - No timestamp for GPS location.");
        } else {
            l.setTime(timeStamp.longValue() * 1000);
        }
        if (coords == null) {
            Log.w(TAG, "getGpsLocation - No coordinates for GPS location");
        } else {
            l.setLatitude(coords[0]);
            l.setLongitude(coords[1]);
            l.setAltitude(coords[2]);
        }
        return l;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setGpsLocation(Location l) {
        if (l == null) {
            return false;
        }
        double[] coords = {l.getLatitude(), l.getLongitude(), l.getAltitude()};
        String processMethod = translateLocationProviderToProcess(l.getProvider());
        long timestamp = l.getTime() / 1000;
        set((CaptureRequest.Key<CaptureRequest.Key<Long>>) CaptureRequest.JPEG_GPS_TIMESTAMP, (CaptureRequest.Key<Long>) Long.valueOf(timestamp));
        set((CaptureRequest.Key<CaptureRequest.Key<double[]>>) CaptureRequest.JPEG_GPS_COORDINATES, (CaptureRequest.Key<double[]>) coords);
        if (processMethod == null) {
            Log.w(TAG, "setGpsLocation - No process method, Location is not from a GPS or NETWORKprovider");
        } else {
            setBase((CaptureRequest.Key<CaptureRequest.Key<String>>) CaptureRequest.JPEG_GPS_PROCESSING_METHOD, (CaptureRequest.Key<String>) processMethod);
        }
        return true;
    }

    private void parseRecommendedConfigurations(RecommendedStreamConfiguration[] configurations, StreamConfigurationMap fullMap, boolean isDepth, ArrayList<ArrayList<StreamConfiguration>> streamConfigList, ArrayList<ArrayList<StreamConfigurationDuration>> streamDurationList, ArrayList<ArrayList<StreamConfigurationDuration>> streamStallList, boolean[] supportsPrivate) {
        int i;
        char c;
        int usecaseBitmap;
        Size sz;
        int publicFormat;
        int internalFormat;
        int width;
        int height;
        StreamConfigurationDuration minDurationConfiguration;
        StreamConfigurationDuration minDurationConfiguration2;
        StreamConfigurationDuration stallDurationConfiguration;
        RecommendedStreamConfiguration[] recommendedStreamConfigurationArr = configurations;
        streamConfigList.ensureCapacity(32);
        streamDurationList.ensureCapacity(32);
        streamStallList.ensureCapacity(32);
        for (int i2 = 0; i2 < 32; i2++) {
            streamConfigList.add(new ArrayList<>());
            streamDurationList.add(new ArrayList<>());
            streamStallList.add(new ArrayList<>());
        }
        int publicFormat2 = recommendedStreamConfigurationArr.length;
        boolean z = false;
        int i3 = 0;
        while (i3 < publicFormat2) {
            RecommendedStreamConfiguration c2 = recommendedStreamConfigurationArr[i3];
            int width2 = c2.getWidth();
            int height2 = c2.getHeight();
            int internalFormat2 = c2.getFormat();
            int publicFormat3 = isDepth ? StreamConfigurationMap.depthFormatToPublic(internalFormat2) : StreamConfigurationMap.imageFormatToPublic(internalFormat2);
            Size sz2 = new Size(width2, height2);
            int usecaseBitmap2 = c2.getUsecaseBitmap();
            if (!c2.isInput()) {
                StreamConfiguration streamConfiguration = new StreamConfiguration(internalFormat2, width2, height2, z);
                long minFrameDuration = fullMap.getOutputMinFrameDuration(publicFormat3, sz2);
                if (minFrameDuration <= 0) {
                    i = publicFormat2;
                    usecaseBitmap = usecaseBitmap2;
                    sz = sz2;
                    publicFormat = publicFormat3;
                    internalFormat = internalFormat2;
                    width = width2;
                    height = height2;
                    minDurationConfiguration = null;
                } else {
                    usecaseBitmap = usecaseBitmap2;
                    sz = sz2;
                    i = publicFormat2;
                    publicFormat = publicFormat3;
                    internalFormat = internalFormat2;
                    width = width2;
                    height = height2;
                    StreamConfigurationDuration minDurationConfiguration3 = new StreamConfigurationDuration(internalFormat2, width2, height2, minFrameDuration);
                    minDurationConfiguration = minDurationConfiguration3;
                }
                long stallDuration = fullMap.getOutputStallDuration(publicFormat, sz);
                if (stallDuration <= 0) {
                    minDurationConfiguration2 = minDurationConfiguration;
                    stallDurationConfiguration = null;
                } else {
                    minDurationConfiguration2 = minDurationConfiguration;
                    StreamConfigurationDuration stallDurationConfiguration2 = new StreamConfigurationDuration(internalFormat, width, height, stallDuration);
                    stallDurationConfiguration = stallDurationConfiguration2;
                }
                int i4 = 0;
                while (true) {
                    c = ' ';
                    if (i4 < 32) {
                        if ((usecaseBitmap & (1 << i4)) != 0) {
                            ArrayList<StreamConfiguration> sc = streamConfigList.get(i4);
                            sc.add(streamConfiguration);
                            if (minFrameDuration > 0) {
                                ArrayList<StreamConfigurationDuration> scd = streamDurationList.get(i4);
                                scd.add(minDurationConfiguration2);
                            }
                            if (stallDuration > 0) {
                                ArrayList<StreamConfigurationDuration> scs = streamStallList.get(i4);
                                scs.add(stallDurationConfiguration);
                            }
                            if (supportsPrivate != null && !supportsPrivate[i4] && publicFormat == 34) {
                                supportsPrivate[i4] = true;
                            }
                        }
                        i4++;
                    }
                }
            } else {
                i = publicFormat2;
                c = ' ';
                if (usecaseBitmap2 != 16) {
                    throw new IllegalArgumentException("Recommended input stream configurations should only be advertised in the ZSL use case!");
                }
                ArrayList<StreamConfiguration> sc2 = streamConfigList.get(4);
                sc2.add(new StreamConfiguration(internalFormat2, width2, height2, true));
            }
            i3++;
            recommendedStreamConfigurationArr = configurations;
            publicFormat2 = i;
            z = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class StreamConfigurationData {
        StreamConfigurationDuration[] minDurationArray;
        StreamConfigurationDuration[] stallDurationArray;
        StreamConfiguration[] streamConfigurationArray;

        private StreamConfigurationData() {
            this.streamConfigurationArray = null;
            this.minDurationArray = null;
            this.stallDurationArray = null;
        }
    }

    public void initializeStreamConfigurationData(ArrayList<StreamConfiguration> sc, ArrayList<StreamConfigurationDuration> scd, ArrayList<StreamConfigurationDuration> scs, StreamConfigurationData scData) {
        if (scData == null || sc == null) {
            return;
        }
        scData.streamConfigurationArray = new StreamConfiguration[sc.size()];
        scData.streamConfigurationArray = (StreamConfiguration[]) sc.toArray(scData.streamConfigurationArray);
        if (scd != null && !scd.isEmpty()) {
            scData.minDurationArray = new StreamConfigurationDuration[scd.size()];
            scData.minDurationArray = (StreamConfigurationDuration[]) scd.toArray(scData.minDurationArray);
        } else {
            scData.minDurationArray = new StreamConfigurationDuration[0];
        }
        if (scs != null && !scs.isEmpty()) {
            scData.stallDurationArray = new StreamConfigurationDuration[scs.size()];
            scData.stallDurationArray = (StreamConfigurationDuration[]) scs.toArray(scData.stallDurationArray);
            return;
        }
        scData.stallDurationArray = new StreamConfigurationDuration[0];
    }

    public ArrayList<RecommendedStreamConfigurationMap> getRecommendedStreamConfigurations() {
        String str;
        boolean[] supportsPrivate;
        ArrayList<ArrayList<StreamConfigurationDuration>> streamStallList;
        ArrayList<ArrayList<StreamConfigurationDuration>> streamDurationList;
        ArrayList<ArrayList<StreamConfigurationDuration>> depthStreamStallList;
        ArrayList<ArrayList<StreamConfigurationDuration>> depthStreamDurationList;
        ArrayList<ArrayList<StreamConfiguration>> depthStreamConfigList;
        ArrayList<ArrayList<StreamConfigurationDuration>> streamStallList2;
        ArrayList<ArrayList<StreamConfigurationDuration>> streamDurationList2;
        ArrayList<ArrayList<StreamConfigurationDuration>> streamDurationList3;
        StreamConfigurationMap map;
        RecommendedStreamConfiguration[] configurations = (RecommendedStreamConfiguration[]) getBase(CameraCharacteristics.SCALER_AVAILABLE_RECOMMENDED_STREAM_CONFIGURATIONS);
        RecommendedStreamConfiguration[] depthConfigurations = (RecommendedStreamConfiguration[]) getBase(CameraCharacteristics.DEPTH_AVAILABLE_RECOMMENDED_DEPTH_STREAM_CONFIGURATIONS);
        if (configurations == null && depthConfigurations == null) {
            return null;
        }
        StreamConfigurationMap fullMap = getStreamConfigurationMap();
        ArrayList<RecommendedStreamConfigurationMap> recommendedConfigurations = new ArrayList<>();
        ArrayList<ArrayList<StreamConfiguration>> streamConfigList = new ArrayList<>();
        ArrayList<ArrayList<StreamConfigurationDuration>> streamDurationList4 = new ArrayList<>();
        ArrayList<ArrayList<StreamConfigurationDuration>> streamStallList3 = new ArrayList<>();
        boolean[] supportsPrivate2 = new boolean[32];
        if (configurations != null) {
            str = TAG;
            supportsPrivate = supportsPrivate2;
            streamStallList = streamStallList3;
            streamDurationList = streamDurationList4;
            try {
                parseRecommendedConfigurations(configurations, fullMap, false, streamConfigList, streamDurationList4, streamStallList3, supportsPrivate);
            } catch (IllegalArgumentException e) {
                Log.e(str, "Failed parsing the recommended stream configurations!");
                return null;
            }
        } else {
            str = TAG;
            supportsPrivate = supportsPrivate2;
            streamStallList = streamStallList3;
            streamDurationList = streamDurationList4;
        }
        ArrayList<ArrayList<StreamConfiguration>> depthStreamConfigList2 = new ArrayList<>();
        ArrayList<ArrayList<StreamConfigurationDuration>> depthStreamDurationList2 = new ArrayList<>();
        ArrayList<ArrayList<StreamConfigurationDuration>> depthStreamStallList2 = new ArrayList<>();
        if (depthConfigurations == null) {
            depthStreamStallList = depthStreamStallList2;
            depthStreamDurationList = depthStreamDurationList2;
            depthStreamConfigList = depthStreamConfigList2;
        } else {
            depthStreamStallList = depthStreamStallList2;
            depthStreamDurationList = depthStreamDurationList2;
            depthStreamConfigList = depthStreamConfigList2;
            try {
                parseRecommendedConfigurations(depthConfigurations, fullMap, true, depthStreamConfigList2, depthStreamDurationList2, depthStreamStallList, null);
            } catch (IllegalArgumentException e2) {
                Log.e(str, "Failed parsing the recommended depth stream configurations!");
                return null;
            }
        }
        ReprocessFormatsMap inputOutputFormatsMap = (ReprocessFormatsMap) getBase(CameraCharacteristics.SCALER_AVAILABLE_RECOMMENDED_INPUT_OUTPUT_FORMATS_MAP);
        HighSpeedVideoConfiguration[] highSpeedVideoConfigurations = (HighSpeedVideoConfiguration[]) getBase(CameraCharacteristics.CONTROL_AVAILABLE_HIGH_SPEED_VIDEO_CONFIGURATIONS);
        boolean listHighResolution = isBurstSupported();
        recommendedConfigurations.ensureCapacity(32);
        int i = 0;
        for (int i2 = 32; i < i2; i2 = 32) {
            StreamConfigurationData scData = new StreamConfigurationData();
            if (configurations == null) {
                streamStallList2 = streamStallList;
                streamDurationList2 = streamDurationList;
            } else {
                streamDurationList2 = streamDurationList;
                streamStallList2 = streamStallList;
                initializeStreamConfigurationData(streamConfigList.get(i), streamDurationList2.get(i), streamStallList2.get(i), scData);
            }
            StreamConfigurationData depthScData = new StreamConfigurationData();
            if (depthConfigurations == null) {
                streamDurationList3 = streamDurationList2;
            } else {
                streamDurationList3 = streamDurationList2;
                initializeStreamConfigurationData(depthStreamConfigList.get(i), depthStreamDurationList.get(i), depthStreamStallList.get(i), depthScData);
            }
            if ((scData.streamConfigurationArray == null || scData.streamConfigurationArray.length == 0) && (depthScData.streamConfigurationArray == null || depthScData.streamConfigurationArray.length == 0)) {
                recommendedConfigurations.add(null);
            } else {
                if (i != 0) {
                    if (i == 1) {
                        map = new StreamConfigurationMap(scData.streamConfigurationArray, scData.minDurationArray, scData.stallDurationArray, null, null, null, null, null, null, null, null, null, highSpeedVideoConfigurations, null, listHighResolution, supportsPrivate[i]);
                    } else if (i != 2) {
                        if (i == 4) {
                            map = new StreamConfigurationMap(scData.streamConfigurationArray, scData.minDurationArray, scData.stallDurationArray, depthScData.streamConfigurationArray, depthScData.minDurationArray, depthScData.stallDurationArray, null, null, null, null, null, null, null, inputOutputFormatsMap, listHighResolution, supportsPrivate[i]);
                        } else if (i != 5 && i != 6) {
                            map = new StreamConfigurationMap(scData.streamConfigurationArray, scData.minDurationArray, scData.stallDurationArray, depthScData.streamConfigurationArray, depthScData.minDurationArray, depthScData.stallDurationArray, null, null, null, null, null, null, null, null, listHighResolution, supportsPrivate[i]);
                        }
                    }
                    recommendedConfigurations.add(new RecommendedStreamConfigurationMap(map, i, supportsPrivate[i]));
                }
                map = new StreamConfigurationMap(scData.streamConfigurationArray, scData.minDurationArray, scData.stallDurationArray, null, null, null, null, null, null, null, null, null, null, null, listHighResolution, supportsPrivate[i]);
                recommendedConfigurations.add(new RecommendedStreamConfigurationMap(map, i, supportsPrivate[i]));
            }
            i++;
            streamStallList = streamStallList2;
            streamDurationList = streamDurationList3;
        }
        return recommendedConfigurations;
    }

    private boolean isBurstSupported() {
        int[] capabilities = (int[]) getBase(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
        for (int capability : capabilities) {
            if (capability == 6) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MandatoryStreamCombination[] getMandatoryStreamCombinations() {
        int[] capabilities = (int[]) getBase(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
        ArrayList<Integer> caps = new ArrayList<>();
        caps.ensureCapacity(capabilities.length);
        for (int c : capabilities) {
            caps.add(new Integer(c));
        }
        int hwLevel = ((Integer) getBase(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)).intValue();
        MandatoryStreamCombination.Builder build = new MandatoryStreamCombination.Builder(this.mCameraId, hwLevel, this.mDisplaySize, caps, getStreamConfigurationMap());
        List<MandatoryStreamCombination> combs = build.getAvailableMandatoryStreamCombinations();
        if (combs != null && !combs.isEmpty()) {
            MandatoryStreamCombination[] combArray = new MandatoryStreamCombination[combs.size()];
            return (MandatoryStreamCombination[]) combs.toArray(combArray);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StreamConfigurationMap getStreamConfigurationMap() {
        StreamConfiguration[] configurations = (StreamConfiguration[]) getBase(CameraCharacteristics.SCALER_AVAILABLE_STREAM_CONFIGURATIONS);
        StreamConfigurationDuration[] minFrameDurations = (StreamConfigurationDuration[]) getBase(CameraCharacteristics.SCALER_AVAILABLE_MIN_FRAME_DURATIONS);
        StreamConfigurationDuration[] stallDurations = (StreamConfigurationDuration[]) getBase(CameraCharacteristics.SCALER_AVAILABLE_STALL_DURATIONS);
        StreamConfiguration[] depthConfigurations = (StreamConfiguration[]) getBase(CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_STREAM_CONFIGURATIONS);
        StreamConfigurationDuration[] depthMinFrameDurations = (StreamConfigurationDuration[]) getBase(CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_MIN_FRAME_DURATIONS);
        StreamConfigurationDuration[] depthStallDurations = (StreamConfigurationDuration[]) getBase(CameraCharacteristics.DEPTH_AVAILABLE_DEPTH_STALL_DURATIONS);
        StreamConfiguration[] dynamicDepthConfigurations = (StreamConfiguration[]) getBase(CameraCharacteristics.DEPTH_AVAILABLE_DYNAMIC_DEPTH_STREAM_CONFIGURATIONS);
        StreamConfigurationDuration[] dynamicDepthMinFrameDurations = (StreamConfigurationDuration[]) getBase(CameraCharacteristics.DEPTH_AVAILABLE_DYNAMIC_DEPTH_MIN_FRAME_DURATIONS);
        StreamConfigurationDuration[] dynamicDepthStallDurations = (StreamConfigurationDuration[]) getBase(CameraCharacteristics.DEPTH_AVAILABLE_DYNAMIC_DEPTH_STALL_DURATIONS);
        StreamConfiguration[] heicConfigurations = (StreamConfiguration[]) getBase(CameraCharacteristics.HEIC_AVAILABLE_HEIC_STREAM_CONFIGURATIONS);
        StreamConfigurationDuration[] heicMinFrameDurations = (StreamConfigurationDuration[]) getBase(CameraCharacteristics.HEIC_AVAILABLE_HEIC_MIN_FRAME_DURATIONS);
        StreamConfigurationDuration[] heicStallDurations = (StreamConfigurationDuration[]) getBase(CameraCharacteristics.HEIC_AVAILABLE_HEIC_STALL_DURATIONS);
        HighSpeedVideoConfiguration[] highSpeedVideoConfigurations = (HighSpeedVideoConfiguration[]) getBase(CameraCharacteristics.CONTROL_AVAILABLE_HIGH_SPEED_VIDEO_CONFIGURATIONS);
        ReprocessFormatsMap inputOutputFormatsMap = (ReprocessFormatsMap) getBase(CameraCharacteristics.SCALER_AVAILABLE_INPUT_OUTPUT_FORMATS_MAP);
        boolean listHighResolution = isBurstSupported();
        return new StreamConfigurationMap(configurations, minFrameDurations, stallDurations, depthConfigurations, depthMinFrameDurations, depthStallDurations, dynamicDepthConfigurations, dynamicDepthMinFrameDurations, dynamicDepthStallDurations, heicConfigurations, heicMinFrameDurations, heicStallDurations, highSpeedVideoConfigurations, inputOutputFormatsMap, listHighResolution);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public <T> Integer getMaxRegions(Key<T> key) {
        int[] maxRegions = (int[]) getBase(CameraCharacteristics.CONTROL_MAX_REGIONS);
        if (maxRegions == null) {
            return null;
        }
        if (key.equals(CameraCharacteristics.CONTROL_MAX_REGIONS_AE)) {
            return Integer.valueOf(maxRegions[0]);
        }
        if (key.equals(CameraCharacteristics.CONTROL_MAX_REGIONS_AWB)) {
            return Integer.valueOf(maxRegions[1]);
        }
        if (key.equals(CameraCharacteristics.CONTROL_MAX_REGIONS_AF)) {
            return Integer.valueOf(maxRegions[2]);
        }
        throw new AssertionError("Invalid key " + key);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public <T> Integer getMaxNumOutputs(Key<T> key) {
        int[] maxNumOutputs = (int[]) getBase(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_STREAMS);
        if (maxNumOutputs == null) {
            return null;
        }
        if (key.equals(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_RAW)) {
            return Integer.valueOf(maxNumOutputs[0]);
        }
        if (key.equals(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC)) {
            return Integer.valueOf(maxNumOutputs[1]);
        }
        if (key.equals(CameraCharacteristics.REQUEST_MAX_NUM_OUTPUT_PROC_STALLING)) {
            return Integer.valueOf(maxNumOutputs[2]);
        }
        throw new AssertionError("Invalid key " + key);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public <T> TonemapCurve getTonemapCurve() {
        float[] red = (float[]) getBase(CaptureRequest.TONEMAP_CURVE_RED);
        float[] green = (float[]) getBase(CaptureRequest.TONEMAP_CURVE_GREEN);
        float[] blue = (float[]) getBase(CaptureRequest.TONEMAP_CURVE_BLUE);
        if (areValuesAllNull(red, green, blue)) {
            return null;
        }
        if (red == null || green == null || blue == null) {
            Log.w(TAG, "getTonemapCurve - missing tone curve components");
            return null;
        }
        TonemapCurve tc = new TonemapCurve(red, green, blue);
        return tc;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public OisSample[] getOisSamples() {
        long[] timestamps = (long[]) getBase(CaptureResult.STATISTICS_OIS_TIMESTAMPS);
        float[] xShifts = (float[]) getBase(CaptureResult.STATISTICS_OIS_X_SHIFTS);
        float[] yShifts = (float[]) getBase(CaptureResult.STATISTICS_OIS_Y_SHIFTS);
        if (timestamps == null) {
            if (xShifts != null) {
                throw new AssertionError("timestamps is null but xShifts is not");
            }
            if (yShifts != null) {
                throw new AssertionError("timestamps is null but yShifts is not");
            }
            return null;
        } else if (xShifts == null) {
            throw new AssertionError("timestamps is not null but xShifts is");
        } else {
            if (yShifts == null) {
                throw new AssertionError("timestamps is not null but yShifts is");
            }
            if (xShifts.length != timestamps.length) {
                throw new AssertionError(String.format("timestamps has %d entries but xShifts has %d", Integer.valueOf(timestamps.length), Integer.valueOf(xShifts.length)));
            }
            if (yShifts.length != timestamps.length) {
                throw new AssertionError(String.format("timestamps has %d entries but yShifts has %d", Integer.valueOf(timestamps.length), Integer.valueOf(yShifts.length)));
            }
            OisSample[] samples = new OisSample[timestamps.length];
            for (int i = 0; i < timestamps.length; i++) {
                samples[i] = new OisSample(timestamps[i], xShifts[i], yShifts[i]);
            }
            return samples;
        }
    }

    private <T> void setBase(CameraCharacteristics.Key<T> key, T value) {
        setBase((Key<Key<T>>) key.getNativeKey(), (Key<T>) value);
    }

    private <T> void setBase(CaptureResult.Key<T> key, T value) {
        setBase((Key<Key<T>>) key.getNativeKey(), (Key<T>) value);
    }

    private <T> void setBase(CaptureRequest.Key<T> key, T value) {
        setBase((Key<Key<T>>) key.getNativeKey(), (Key<T>) value);
    }

    private <T> void setBase(Key<T> key, T value) {
        int tag = nativeGetTagFromKeyLocal(key.getName());
        if (value == null) {
            writeValues(tag, null);
            return;
        }
        int nativeType = nativeGetTypeFromTagLocal(tag);
        Marshaler<T> marshaler = getMarshalerForKey(key, nativeType);
        int size = marshaler.calculateMarshalSize(value);
        byte[] values = new byte[size];
        ByteBuffer buffer = ByteBuffer.wrap(values).order(ByteOrder.nativeOrder());
        marshaler.marshal(value, buffer);
        writeValues(tag, values);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setAvailableFormats(int[] value) {
        if (value == null) {
            return false;
        }
        int[] newValues = new int[value.length];
        for (int i = 0; i < value.length; i++) {
            newValues[i] = value[i];
            if (value[i] == 256) {
                newValues[i] = 33;
            }
        }
        setBase((CameraCharacteristics.Key<CameraCharacteristics.Key<int[]>>) CameraCharacteristics.SCALER_AVAILABLE_FORMATS, (CameraCharacteristics.Key<int[]>) newValues);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setFaceRectangles(Rect[] faceRects) {
        if (faceRects == null) {
            return false;
        }
        Rect[] newFaceRects = new Rect[faceRects.length];
        for (int i = 0; i < newFaceRects.length; i++) {
            newFaceRects[i] = new Rect(faceRects[i].left, faceRects[i].top, faceRects[i].right + faceRects[i].left, faceRects[i].bottom + faceRects[i].top);
        }
        setBase((CaptureResult.Key<CaptureResult.Key<Rect[]>>) CaptureResult.STATISTICS_FACE_RECTANGLES, (CaptureResult.Key<Rect[]>) newFaceRects);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public <T> boolean setTonemapCurve(TonemapCurve tc) {
        if (tc == null) {
            return false;
        }
        float[][] curve = new float[3];
        for (int i = 0; i <= 2; i++) {
            int pointCount = tc.getPointCount(i);
            curve[i] = new float[pointCount * 2];
            tc.copyColorCurve(i, curve[i], 0);
        }
        setBase((CaptureRequest.Key<CaptureRequest.Key<float[]>>) CaptureRequest.TONEMAP_CURVE_RED, (CaptureRequest.Key<float[]>) curve[0]);
        setBase((CaptureRequest.Key<CaptureRequest.Key<float[]>>) CaptureRequest.TONEMAP_CURVE_GREEN, (CaptureRequest.Key<float[]>) curve[1]);
        setBase((CaptureRequest.Key<CaptureRequest.Key<float[]>>) CaptureRequest.TONEMAP_CURVE_BLUE, (CaptureRequest.Key<float[]>) curve[2]);
        return true;
    }

    public void setCameraId(int cameraId) {
        this.mCameraId = cameraId;
    }

    public void setDisplaySize(Size displaySize) {
        this.mDisplaySize = displaySize;
    }

    public void swap(CameraMetadataNative other) {
        nativeSwap(other);
        this.mCameraId = other.mCameraId;
        this.mDisplaySize = other.mDisplaySize;
    }

    public int getEntryCount() {
        return nativeGetEntryCount();
    }

    public boolean isEmpty() {
        return nativeIsEmpty();
    }

    public <K> ArrayList<K> getAllVendorKeys(Class<K> keyClass) {
        if (keyClass == null) {
            throw new NullPointerException();
        }
        return nativeGetAllVendorKeys(keyClass);
    }

    public static int getTag(String key) {
        return nativeGetTagFromKey(key, Long.MAX_VALUE);
    }

    public static int getTag(String key, long vendorId) {
        return nativeGetTagFromKey(key, vendorId);
    }

    public static int getNativeType(int tag, long vendorId) {
        return nativeGetTypeFromTag(tag, vendorId);
    }

    public void writeValues(int tag, byte[] src) {
        nativeWriteValues(tag, src);
    }

    public byte[] readValues(int tag) {
        return nativeReadValues(tag);
    }

    public void dumpToLog() {
        try {
            nativeDump();
        } catch (IOException e) {
            Log.wtf(TAG, "Dump logging failed", e);
        }
    }

    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    private static <T> Marshaler<T> getMarshalerForKey(Key<T> key, int nativeType) {
        return MarshalRegistry.getMarshaler(key.getTypeReference(), nativeType);
    }

    private static void registerAllMarshalers() {
        MarshalQueryable[] queryList = {new MarshalQueryablePrimitive(), new MarshalQueryableEnum(), new MarshalQueryableArray(), new MarshalQueryableBoolean(), new MarshalQueryableNativeByteToInteger(), new MarshalQueryableRect(), new MarshalQueryableSize(), new MarshalQueryableSizeF(), new MarshalQueryableString(), new MarshalQueryableReprocessFormatsMap(), new MarshalQueryableRange(), new MarshalQueryablePair(), new MarshalQueryableMeteringRectangle(), new MarshalQueryableColorSpaceTransform(), new MarshalQueryableStreamConfiguration(), new MarshalQueryableStreamConfigurationDuration(), new MarshalQueryableRggbChannelVector(), new MarshalQueryableBlackLevelPattern(), new MarshalQueryableHighSpeedVideoConfiguration(), new MarshalQueryableRecommendedStreamConfiguration(), new MarshalQueryableParcelable()};
        for (MarshalQueryable query : queryList) {
            MarshalRegistry.registerMarshalQueryable(query);
        }
    }

    private static boolean areValuesAllNull(Object... objs) {
        for (Object o : objs) {
            if (o != null) {
                return false;
            }
        }
        return true;
    }
}
