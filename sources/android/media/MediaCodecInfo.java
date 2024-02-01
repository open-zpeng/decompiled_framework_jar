package android.media;

import android.bluetooth.BluetoothHealth;
import android.util.Log;
import android.util.Pair;
import android.util.Range;
import android.util.Rational;
import android.util.Size;
import com.android.internal.content.NativeLibraryHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public final class MediaCodecInfo {
    private static final int DEFAULT_MAX_SUPPORTED_INSTANCES = 32;
    private static final int ERROR_NONE_SUPPORTED = 4;
    private static final int ERROR_UNRECOGNIZED = 1;
    private static final int ERROR_UNSUPPORTED = 2;
    private static final int MAX_SUPPORTED_INSTANCES_LIMIT = 256;
    private Map<String, CodecCapabilities> mCaps = new HashMap();
    private boolean mIsEncoder;
    private String mName;
    private static final Range<Integer> POSITIVE_INTEGERS = Range.create(1, Integer.MAX_VALUE);
    private static final Range<Long> POSITIVE_LONGS = Range.create(1L, Long.MAX_VALUE);
    private static final Range<Rational> POSITIVE_RATIONALS = Range.create(new Rational(1, Integer.MAX_VALUE), new Rational(Integer.MAX_VALUE, 1));
    private static final Range<Integer> SIZE_RANGE = Range.create(1, 32768);
    private static final Range<Integer> FRAME_RATE_RANGE = Range.create(0, 960);
    private static final Range<Integer> BITRATE_RANGE = Range.create(0, 500000000);

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized MediaCodecInfo(String name, boolean isEncoder, CodecCapabilities[] caps) {
        this.mName = name;
        this.mIsEncoder = isEncoder;
        for (CodecCapabilities c : caps) {
            this.mCaps.put(c.getMimeType(), c);
        }
    }

    public final String getName() {
        return this.mName;
    }

    public final boolean isEncoder() {
        return this.mIsEncoder;
    }

    public final String[] getSupportedTypes() {
        Set<String> typeSet = this.mCaps.keySet();
        String[] types = (String[]) typeSet.toArray(new String[typeSet.size()]);
        Arrays.sort(types);
        return types;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized int checkPowerOfTwo(int value, String message) {
        if (((value - 1) & value) != 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Feature {
        public boolean mDefault;
        public String mName;
        public int mValue;

        public synchronized Feature(String name, int value, boolean def) {
            this.mName = name;
            this.mValue = value;
            this.mDefault = def;
        }
    }

    /* loaded from: classes.dex */
    public static final class CodecCapabilities {
        public static final int COLOR_Format12bitRGB444 = 3;
        public static final int COLOR_Format16bitARGB1555 = 5;
        public static final int COLOR_Format16bitARGB4444 = 4;
        public static final int COLOR_Format16bitBGR565 = 7;
        public static final int COLOR_Format16bitRGB565 = 6;
        public static final int COLOR_Format18BitBGR666 = 41;
        public static final int COLOR_Format18bitARGB1665 = 9;
        public static final int COLOR_Format18bitRGB666 = 8;
        public static final int COLOR_Format19bitARGB1666 = 10;
        public static final int COLOR_Format24BitABGR6666 = 43;
        public static final int COLOR_Format24BitARGB6666 = 42;
        public static final int COLOR_Format24bitARGB1887 = 13;
        public static final int COLOR_Format24bitBGR888 = 12;
        public static final int COLOR_Format24bitRGB888 = 11;
        public static final int COLOR_Format25bitARGB1888 = 14;
        public static final int COLOR_Format32bitABGR8888 = 2130747392;
        public static final int COLOR_Format32bitARGB8888 = 16;
        public static final int COLOR_Format32bitBGRA8888 = 15;
        public static final int COLOR_Format8bitRGB332 = 2;
        public static final int COLOR_FormatCbYCrY = 27;
        public static final int COLOR_FormatCrYCbY = 28;
        public static final int COLOR_FormatL16 = 36;
        public static final int COLOR_FormatL2 = 33;
        public static final int COLOR_FormatL24 = 37;
        public static final int COLOR_FormatL32 = 38;
        public static final int COLOR_FormatL4 = 34;
        public static final int COLOR_FormatL8 = 35;
        public static final int COLOR_FormatMonochrome = 1;
        public static final int COLOR_FormatRGBAFlexible = 2134288520;
        public static final int COLOR_FormatRGBFlexible = 2134292616;
        public static final int COLOR_FormatRawBayer10bit = 31;
        public static final int COLOR_FormatRawBayer8bit = 30;
        public static final int COLOR_FormatRawBayer8bitcompressed = 32;
        public static final int COLOR_FormatSurface = 2130708361;
        public static final int COLOR_FormatYCbYCr = 25;
        public static final int COLOR_FormatYCrYCb = 26;
        public static final int COLOR_FormatYUV411PackedPlanar = 18;
        public static final int COLOR_FormatYUV411Planar = 17;
        public static final int COLOR_FormatYUV420Flexible = 2135033992;
        public static final int COLOR_FormatYUV420PackedPlanar = 20;
        public static final int COLOR_FormatYUV420PackedSemiPlanar = 39;
        public static final int COLOR_FormatYUV420Planar = 19;
        public static final int COLOR_FormatYUV420SemiPlanar = 21;
        public static final int COLOR_FormatYUV422Flexible = 2135042184;
        public static final int COLOR_FormatYUV422PackedPlanar = 23;
        public static final int COLOR_FormatYUV422PackedSemiPlanar = 40;
        public static final int COLOR_FormatYUV422Planar = 22;
        public static final int COLOR_FormatYUV422SemiPlanar = 24;
        public static final int COLOR_FormatYUV444Flexible = 2135181448;
        public static final int COLOR_FormatYUV444Interleaved = 29;
        public static final int COLOR_QCOM_FormatYUV420SemiPlanar = 2141391872;
        public static final int COLOR_TI_FormatYUV420PackedSemiPlanar = 2130706688;
        private static final String TAG = "CodecCapabilities";
        public int[] colorFormats;
        private AudioCapabilities mAudioCaps;
        private MediaFormat mCapabilitiesInfo;
        private MediaFormat mDefaultFormat;
        private EncoderCapabilities mEncoderCaps;
        int mError;
        private int mFlagsRequired;
        private int mFlagsSupported;
        private int mFlagsVerified;
        private int mMaxSupportedInstances;
        private String mMime;
        private VideoCapabilities mVideoCaps;
        public CodecProfileLevel[] profileLevels;
        public static final String FEATURE_AdaptivePlayback = "adaptive-playback";
        public static final String FEATURE_SecurePlayback = "secure-playback";
        public static final String FEATURE_TunneledPlayback = "tunneled-playback";
        public static final String FEATURE_PartialFrame = "partial-frame";
        private static final Feature[] decoderFeatures = {new Feature(FEATURE_AdaptivePlayback, 1, true), new Feature(FEATURE_SecurePlayback, 2, false), new Feature(FEATURE_TunneledPlayback, 4, false), new Feature(FEATURE_PartialFrame, 8, false)};
        public static final String FEATURE_IntraRefresh = "intra-refresh";
        public static final String FEATURE_SecureEncode = "secure-encode";
        private static final Feature[] encoderFeatures = {new Feature(FEATURE_IntraRefresh, 1, false), new Feature(FEATURE_SecureEncode, 2, false)};

        public CodecCapabilities() {
        }

        public final boolean isFeatureSupported(String name) {
            return checkFeature(name, this.mFlagsSupported);
        }

        public final boolean isFeatureRequired(String name) {
            return checkFeature(name, this.mFlagsRequired);
        }

        public synchronized String[] validFeatures() {
            Feature[] features = getValidFeatures();
            String[] res = new String[features.length];
            for (int i = 0; i < res.length; i++) {
                res[i] = features[i].mName;
            }
            return res;
        }

        private synchronized Feature[] getValidFeatures() {
            if (!isEncoder()) {
                return decoderFeatures;
            }
            return encoderFeatures;
        }

        private synchronized boolean checkFeature(String name, int flags) {
            Feature[] validFeatures;
            for (Feature feat : getValidFeatures()) {
                if (feat.mName.equals(name)) {
                    return (feat.mValue & flags) != 0;
                }
            }
            return false;
        }

        public synchronized boolean isRegular() {
            Feature[] validFeatures;
            for (Feature feat : getValidFeatures()) {
                if (!feat.mDefault && isFeatureRequired(feat.mName)) {
                    return false;
                }
            }
            return true;
        }

        public final boolean isFormatSupported(MediaFormat format) {
            Feature[] validFeatures;
            CodecProfileLevel[] codecProfileLevelArr;
            Map<String, Object> map = format.getMap();
            String mime = (String) map.get(MediaFormat.KEY_MIME);
            if (mime != null && !this.mMime.equalsIgnoreCase(mime)) {
                return false;
            }
            for (Feature feat : getValidFeatures()) {
                Integer yesNo = (Integer) map.get(MediaFormat.KEY_FEATURE_ + feat.mName);
                if (yesNo != null && ((yesNo.intValue() == 1 && !isFeatureSupported(feat.mName)) || (yesNo.intValue() == 0 && isFeatureRequired(feat.mName)))) {
                    return false;
                }
            }
            Integer profile = (Integer) map.get(MediaFormat.KEY_PROFILE);
            Integer level = (Integer) map.get("level");
            if (profile != null) {
                if (!supportsProfileLevel(profile.intValue(), level)) {
                    return false;
                }
                int maxLevel = 0;
                for (CodecProfileLevel pl : this.profileLevels) {
                    if (pl.profile == profile.intValue() && pl.level > maxLevel) {
                        maxLevel = pl.level;
                    }
                }
                CodecCapabilities levelCaps = createFromProfileLevel(this.mMime, profile.intValue(), maxLevel);
                Map<String, Object> mapWithoutProfile = new HashMap<>(map);
                mapWithoutProfile.remove(MediaFormat.KEY_PROFILE);
                MediaFormat formatWithoutProfile = new MediaFormat(mapWithoutProfile);
                if (levelCaps != null && !levelCaps.isFormatSupported(formatWithoutProfile)) {
                    return false;
                }
            }
            if (this.mAudioCaps != null && !this.mAudioCaps.supportsFormat(format)) {
                return false;
            }
            if (this.mVideoCaps == null || this.mVideoCaps.supportsFormat(format)) {
                return this.mEncoderCaps == null || this.mEncoderCaps.supportsFormat(format);
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized boolean supportsBitrate(Range<Integer> bitrateRange, MediaFormat format) {
            Map<String, Object> map = format.getMap();
            Integer maxBitrate = (Integer) map.get(MediaFormat.KEY_MAX_BIT_RATE);
            Integer bitrate = (Integer) map.get(MediaFormat.KEY_BIT_RATE);
            if (bitrate == null) {
                bitrate = maxBitrate;
            } else if (maxBitrate != null) {
                bitrate = Integer.valueOf(Math.max(bitrate.intValue(), maxBitrate.intValue()));
            }
            if (bitrate != null && bitrate.intValue() > 0) {
                return bitrateRange.contains((Range<Integer>) bitrate);
            }
            return true;
        }

        private synchronized boolean supportsProfileLevel(int profile, Integer level) {
            CodecProfileLevel[] codecProfileLevelArr;
            for (CodecProfileLevel pl : this.profileLevels) {
                if (pl.profile == profile) {
                    if (level == null || this.mMime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_AAC)) {
                        return true;
                    }
                    if ((!this.mMime.equalsIgnoreCase(MediaFormat.MIMETYPE_VIDEO_H263) || pl.level == level.intValue() || pl.level != 16 || level.intValue() <= 1) && (!this.mMime.equalsIgnoreCase(MediaFormat.MIMETYPE_VIDEO_MPEG4) || pl.level == level.intValue() || pl.level != 4 || level.intValue() <= 1)) {
                        if (this.mMime.equalsIgnoreCase(MediaFormat.MIMETYPE_VIDEO_HEVC)) {
                            boolean supportsHighTier = (pl.level & 44739242) != 0;
                            boolean checkingHighTier = (44739242 & level.intValue()) != 0;
                            if (checkingHighTier && !supportsHighTier) {
                            }
                        }
                        if (pl.level >= level.intValue()) {
                            return createFromProfileLevel(this.mMime, profile, pl.level) == null || createFromProfileLevel(this.mMime, profile, level.intValue()) != null;
                        }
                    }
                }
            }
            return false;
        }

        public MediaFormat getDefaultFormat() {
            return this.mDefaultFormat;
        }

        public String getMimeType() {
            return this.mMime;
        }

        public int getMaxSupportedInstances() {
            return this.mMaxSupportedInstances;
        }

        private synchronized boolean isAudio() {
            return this.mAudioCaps != null;
        }

        public AudioCapabilities getAudioCapabilities() {
            return this.mAudioCaps;
        }

        private synchronized boolean isEncoder() {
            return this.mEncoderCaps != null;
        }

        public EncoderCapabilities getEncoderCapabilities() {
            return this.mEncoderCaps;
        }

        private synchronized boolean isVideo() {
            return this.mVideoCaps != null;
        }

        public VideoCapabilities getVideoCapabilities() {
            return this.mVideoCaps;
        }

        public synchronized CodecCapabilities dup() {
            CodecCapabilities caps = new CodecCapabilities();
            caps.profileLevels = (CodecProfileLevel[]) Arrays.copyOf(this.profileLevels, this.profileLevels.length);
            caps.colorFormats = Arrays.copyOf(this.colorFormats, this.colorFormats.length);
            caps.mMime = this.mMime;
            caps.mMaxSupportedInstances = this.mMaxSupportedInstances;
            caps.mFlagsRequired = this.mFlagsRequired;
            caps.mFlagsSupported = this.mFlagsSupported;
            caps.mFlagsVerified = this.mFlagsVerified;
            caps.mAudioCaps = this.mAudioCaps;
            caps.mVideoCaps = this.mVideoCaps;
            caps.mEncoderCaps = this.mEncoderCaps;
            caps.mDefaultFormat = this.mDefaultFormat;
            caps.mCapabilitiesInfo = this.mCapabilitiesInfo;
            return caps;
        }

        public static CodecCapabilities createFromProfileLevel(String mime, int profile, int level) {
            CodecProfileLevel pl = new CodecProfileLevel();
            pl.profile = profile;
            pl.level = level;
            MediaFormat defaultFormat = new MediaFormat();
            defaultFormat.setString(MediaFormat.KEY_MIME, mime);
            CodecCapabilities ret = new CodecCapabilities(new CodecProfileLevel[]{pl}, new int[0], true, 0, defaultFormat, new MediaFormat());
            if (ret.mError != 0) {
                return null;
            }
            return ret;
        }

        synchronized CodecCapabilities(CodecProfileLevel[] profLevs, int[] colFmts, boolean encoder, int flags, Map<String, Object> defaultFormatMap, Map<String, Object> capabilitiesMap) {
            this(profLevs, colFmts, encoder, flags, new MediaFormat(defaultFormatMap), new MediaFormat(capabilitiesMap));
        }

        synchronized CodecCapabilities(CodecProfileLevel[] profLevs, int[] colFmts, boolean encoder, int flags, MediaFormat defaultFormat, MediaFormat info) {
            Map<String, Object> map;
            boolean z;
            Map<String, Object> map2 = info.getMap();
            this.colorFormats = colFmts;
            this.mFlagsVerified = flags;
            this.mDefaultFormat = defaultFormat;
            this.mCapabilitiesInfo = info;
            this.mMime = this.mDefaultFormat.getString(MediaFormat.KEY_MIME);
            CodecProfileLevel[] profLevs2 = profLevs;
            int i = 0;
            if (profLevs2.length == 0 && this.mMime.equalsIgnoreCase(MediaFormat.MIMETYPE_VIDEO_VP9)) {
                CodecProfileLevel profLev = new CodecProfileLevel();
                profLev.profile = 1;
                profLev.level = VideoCapabilities.equivalentVP9Level(info);
                profLevs2 = new CodecProfileLevel[]{profLev};
            }
            this.profileLevels = profLevs2;
            if (this.mMime.toLowerCase().startsWith("audio/")) {
                this.mAudioCaps = AudioCapabilities.create(info, this);
                this.mAudioCaps.getDefaultFormat(this.mDefaultFormat);
            } else if (this.mMime.toLowerCase().startsWith("video/") || this.mMime.equalsIgnoreCase(MediaFormat.MIMETYPE_IMAGE_ANDROID_HEIC)) {
                this.mVideoCaps = VideoCapabilities.create(info, this);
            }
            if (encoder) {
                this.mEncoderCaps = EncoderCapabilities.create(info, this);
                this.mEncoderCaps.getDefaultFormat(this.mDefaultFormat);
            }
            Map<String, Object> global = MediaCodecList.getGlobalSettings();
            this.mMaxSupportedInstances = Utils.parseIntSafely(global.get("max-concurrent-instances"), 32);
            int maxInstances = Utils.parseIntSafely(map2.get("max-concurrent-instances"), this.mMaxSupportedInstances);
            this.mMaxSupportedInstances = ((Integer) Range.create(1, 256).clamp(Integer.valueOf(maxInstances))).intValue();
            Feature[] validFeatures = getValidFeatures();
            int length = validFeatures.length;
            while (i < length) {
                Feature feat = validFeatures[i];
                String key = MediaFormat.KEY_FEATURE_ + feat.mName;
                Integer yesNo = (Integer) map2.get(key);
                if (yesNo == null) {
                    map = map2;
                    z = true;
                } else {
                    if (yesNo.intValue() > 0) {
                        map = map2;
                        this.mFlagsRequired |= feat.mValue;
                    } else {
                        map = map2;
                    }
                    this.mFlagsSupported |= feat.mValue;
                    z = true;
                    this.mDefaultFormat.setInteger(key, 1);
                }
                i++;
                map2 = map;
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class AudioCapabilities {
        private static final int MAX_INPUT_CHANNEL_COUNT = 30;
        private static final String TAG = "AudioCapabilities";
        private Range<Integer> mBitrateRange;
        private int mMaxInputChannelCount;
        private CodecCapabilities mParent;
        private Range<Integer>[] mSampleRateRanges;
        private int[] mSampleRates;

        public Range<Integer> getBitrateRange() {
            return this.mBitrateRange;
        }

        public int[] getSupportedSampleRates() {
            return Arrays.copyOf(this.mSampleRates, this.mSampleRates.length);
        }

        public Range<Integer>[] getSupportedSampleRateRanges() {
            return (Range[]) Arrays.copyOf(this.mSampleRateRanges, this.mSampleRateRanges.length);
        }

        public int getMaxInputChannelCount() {
            return this.mMaxInputChannelCount;
        }

        private synchronized AudioCapabilities() {
        }

        public static synchronized AudioCapabilities create(MediaFormat info, CodecCapabilities parent) {
            AudioCapabilities caps = new AudioCapabilities();
            caps.init(info, parent);
            return caps;
        }

        private synchronized void init(MediaFormat info, CodecCapabilities parent) {
            this.mParent = parent;
            initWithPlatformLimits();
            applyLevelLimits();
            parseFromInfo(info);
        }

        private synchronized void initWithPlatformLimits() {
            this.mBitrateRange = Range.create(0, Integer.MAX_VALUE);
            this.mMaxInputChannelCount = 30;
            this.mSampleRateRanges = new Range[]{Range.create(8000, 96000)};
            this.mSampleRates = null;
        }

        private synchronized boolean supports(Integer sampleRate, Integer inputChannels) {
            if (inputChannels != null && (inputChannels.intValue() < 1 || inputChannels.intValue() > this.mMaxInputChannelCount)) {
                return false;
            }
            if (sampleRate != null) {
                int ix = Utils.binarySearchDistinctRanges(this.mSampleRateRanges, sampleRate);
                if (ix < 0) {
                    return false;
                }
            }
            return true;
        }

        public boolean isSampleRateSupported(int sampleRate) {
            return supports(Integer.valueOf(sampleRate), null);
        }

        private synchronized void limitSampleRates(int[] rates) {
            Arrays.sort(rates);
            ArrayList<Range<Integer>> ranges = new ArrayList<>();
            for (int rate : rates) {
                if (supports(Integer.valueOf(rate), null)) {
                    ranges.add(Range.create(Integer.valueOf(rate), Integer.valueOf(rate)));
                }
            }
            this.mSampleRateRanges = (Range[]) ranges.toArray(new Range[ranges.size()]);
            createDiscreteSampleRates();
        }

        private synchronized void createDiscreteSampleRates() {
            this.mSampleRates = new int[this.mSampleRateRanges.length];
            for (int i = 0; i < this.mSampleRateRanges.length; i++) {
                this.mSampleRates[i] = this.mSampleRateRanges[i].getLower().intValue();
            }
        }

        private synchronized void limitSampleRates(Range<Integer>[] rateRanges) {
            Range<Integer>[] rangeArr;
            Utils.sortDistinctRanges(rateRanges);
            this.mSampleRateRanges = Utils.intersectSortedDistinctRanges(this.mSampleRateRanges, rateRanges);
            for (Range<Integer> range : this.mSampleRateRanges) {
                if (!range.getLower().equals(range.getUpper())) {
                    this.mSampleRates = null;
                    return;
                }
            }
            createDiscreteSampleRates();
        }

        private synchronized void applyLevelLimits() {
            int[] sampleRates = null;
            Range<Integer> sampleRateRange = null;
            Range<Integer> bitRates = null;
            int maxChannels = 30;
            String mime = this.mParent.getMimeType();
            if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_MPEG)) {
                sampleRates = new int[]{8000, 11025, 12000, 16000, 22050, 24000, 32000, 44100, 48000};
                bitRates = Range.create(8000, 320000);
                maxChannels = 2;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_AMR_NB)) {
                sampleRates = new int[]{8000};
                bitRates = Range.create(4750, 12200);
                maxChannels = 1;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_AMR_WB)) {
                sampleRates = new int[]{16000};
                bitRates = Range.create(6600, 23850);
                maxChannels = 1;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_AAC)) {
                sampleRates = new int[]{7350, 8000, 11025, 12000, 16000, 22050, 24000, 32000, 44100, 48000, 64000, 88200, 96000};
                bitRates = Range.create(8000, 510000);
                maxChannels = 48;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_VORBIS)) {
                bitRates = Range.create(32000, 500000);
                sampleRateRange = Range.create(8000, Integer.valueOf((int) AudioFormat.SAMPLE_RATE_HZ_MAX));
                maxChannels = 255;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_OPUS)) {
                bitRates = Range.create(Integer.valueOf((int) BluetoothHealth.HEALTH_OPERATION_SUCCESS), 510000);
                sampleRates = new int[]{8000, 12000, 16000, 24000, 48000};
                maxChannels = 255;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_RAW)) {
                sampleRateRange = Range.create(1, 96000);
                bitRates = Range.create(1, 10000000);
                maxChannels = AudioTrack.CHANNEL_COUNT_MAX;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_FLAC)) {
                sampleRateRange = Range.create(1, 655350);
                maxChannels = 255;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_G711_ALAW) || mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_G711_MLAW)) {
                sampleRates = new int[]{8000};
                bitRates = Range.create(64000, 64000);
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_MSGSM)) {
                sampleRates = new int[]{8000};
                bitRates = Range.create(13000, 13000);
                maxChannels = 1;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_AC3)) {
                maxChannels = 6;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_EAC3)) {
                maxChannels = 16;
            } else {
                Log.w(TAG, "Unsupported mime " + mime);
                CodecCapabilities codecCapabilities = this.mParent;
                codecCapabilities.mError = codecCapabilities.mError | 2;
            }
            if (sampleRates != null) {
                limitSampleRates(sampleRates);
            } else if (sampleRateRange != null) {
                limitSampleRates(new Range[]{sampleRateRange});
            }
            applyLimits(maxChannels, bitRates);
        }

        private synchronized void applyLimits(int maxInputChannels, Range<Integer> bitRates) {
            this.mMaxInputChannelCount = ((Integer) Range.create(1, Integer.valueOf(this.mMaxInputChannelCount)).clamp(Integer.valueOf(maxInputChannels))).intValue();
            if (bitRates != null) {
                this.mBitrateRange = this.mBitrateRange.intersect(bitRates);
            }
        }

        private synchronized void parseFromInfo(MediaFormat info) {
            int maxInputChannels = 30;
            Range<Integer> bitRates = MediaCodecInfo.POSITIVE_INTEGERS;
            if (info.containsKey("sample-rate-ranges")) {
                String[] rateStrings = info.getString("sample-rate-ranges").split(",");
                Range<Integer>[] rateRanges = new Range[rateStrings.length];
                for (int i = 0; i < rateStrings.length; i++) {
                    rateRanges[i] = Utils.parseIntRange(rateStrings[i], null);
                }
                limitSampleRates(rateRanges);
            }
            if (info.containsKey("max-channel-count")) {
                maxInputChannels = Utils.parseIntSafely(info.getString("max-channel-count"), 30);
            } else if ((this.mParent.mError & 2) != 0) {
                maxInputChannels = 0;
            }
            if (info.containsKey("bitrate-range")) {
                bitRates = bitRates.intersect(Utils.parseIntRange(info.getString("bitrate-range"), bitRates));
            }
            applyLimits(maxInputChannels, bitRates);
        }

        public synchronized void getDefaultFormat(MediaFormat format) {
            if (this.mBitrateRange.getLower().equals(this.mBitrateRange.getUpper())) {
                format.setInteger(MediaFormat.KEY_BIT_RATE, this.mBitrateRange.getLower().intValue());
            }
            if (this.mMaxInputChannelCount == 1) {
                format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
            }
            if (this.mSampleRates != null && this.mSampleRates.length == 1) {
                format.setInteger(MediaFormat.KEY_SAMPLE_RATE, this.mSampleRates[0]);
            }
        }

        public synchronized boolean supportsFormat(MediaFormat format) {
            Map<String, Object> map = format.getMap();
            Integer sampleRate = (Integer) map.get(MediaFormat.KEY_SAMPLE_RATE);
            Integer channels = (Integer) map.get(MediaFormat.KEY_CHANNEL_COUNT);
            return supports(sampleRate, channels) && CodecCapabilities.supportsBitrate(this.mBitrateRange, format);
        }
    }

    /* loaded from: classes.dex */
    public static final class VideoCapabilities {
        private static final String TAG = "VideoCapabilities";
        private boolean mAllowMbOverride;
        private Range<Rational> mAspectRatioRange;
        private Range<Integer> mBitrateRange;
        private Range<Rational> mBlockAspectRatioRange;
        private Range<Integer> mBlockCountRange;
        private int mBlockHeight;
        private int mBlockWidth;
        private Range<Long> mBlocksPerSecondRange;
        private Range<Integer> mFrameRateRange;
        private int mHeightAlignment;
        private Range<Integer> mHeightRange;
        private Range<Integer> mHorizontalBlockRange;
        private Map<Size, Range<Long>> mMeasuredFrameRates;
        private CodecCapabilities mParent;
        private int mSmallerDimensionUpperLimit;
        private Range<Integer> mVerticalBlockRange;
        private int mWidthAlignment;
        private Range<Integer> mWidthRange;

        public Range<Integer> getBitrateRange() {
            return this.mBitrateRange;
        }

        public Range<Integer> getSupportedWidths() {
            return this.mWidthRange;
        }

        public Range<Integer> getSupportedHeights() {
            return this.mHeightRange;
        }

        public int getWidthAlignment() {
            return this.mWidthAlignment;
        }

        public int getHeightAlignment() {
            return this.mHeightAlignment;
        }

        public synchronized int getSmallerDimensionUpperLimit() {
            return this.mSmallerDimensionUpperLimit;
        }

        public Range<Integer> getSupportedFrameRates() {
            return this.mFrameRateRange;
        }

        public Range<Integer> getSupportedWidthsFor(int height) {
            try {
                Range<Integer> range = this.mWidthRange;
                if (!this.mHeightRange.contains((Range<Integer>) Integer.valueOf(height)) || height % this.mHeightAlignment != 0) {
                    throw new IllegalArgumentException("unsupported height");
                }
                int heightInBlocks = Utils.divUp(height, this.mBlockHeight);
                int minWidthInBlocks = Math.max(Utils.divUp(this.mBlockCountRange.getLower().intValue(), heightInBlocks), (int) Math.ceil(this.mBlockAspectRatioRange.getLower().doubleValue() * heightInBlocks));
                int maxWidthInBlocks = Math.min(this.mBlockCountRange.getUpper().intValue() / heightInBlocks, (int) (this.mBlockAspectRatioRange.getUpper().doubleValue() * heightInBlocks));
                Range<Integer> range2 = range.intersect(Integer.valueOf(((minWidthInBlocks - 1) * this.mBlockWidth) + this.mWidthAlignment), Integer.valueOf(this.mBlockWidth * maxWidthInBlocks));
                if (height > this.mSmallerDimensionUpperLimit) {
                    range2 = range2.intersect(1, Integer.valueOf(this.mSmallerDimensionUpperLimit));
                }
                return range2.intersect(Integer.valueOf((int) Math.ceil(this.mAspectRatioRange.getLower().doubleValue() * height)), Integer.valueOf((int) (this.mAspectRatioRange.getUpper().doubleValue() * height)));
            } catch (IllegalArgumentException e) {
                Log.v(TAG, "could not get supported widths for " + height);
                throw new IllegalArgumentException("unsupported height");
            }
        }

        public Range<Integer> getSupportedHeightsFor(int width) {
            try {
                Range<Integer> range = this.mHeightRange;
                if (!this.mWidthRange.contains((Range<Integer>) Integer.valueOf(width)) || width % this.mWidthAlignment != 0) {
                    throw new IllegalArgumentException("unsupported width");
                }
                int widthInBlocks = Utils.divUp(width, this.mBlockWidth);
                int minHeightInBlocks = Math.max(Utils.divUp(this.mBlockCountRange.getLower().intValue(), widthInBlocks), (int) Math.ceil(widthInBlocks / this.mBlockAspectRatioRange.getUpper().doubleValue()));
                int maxHeightInBlocks = Math.min(this.mBlockCountRange.getUpper().intValue() / widthInBlocks, (int) (widthInBlocks / this.mBlockAspectRatioRange.getLower().doubleValue()));
                Range<Integer> range2 = range.intersect(Integer.valueOf(((minHeightInBlocks - 1) * this.mBlockHeight) + this.mHeightAlignment), Integer.valueOf(this.mBlockHeight * maxHeightInBlocks));
                if (width > this.mSmallerDimensionUpperLimit) {
                    range2 = range2.intersect(1, Integer.valueOf(this.mSmallerDimensionUpperLimit));
                }
                return range2.intersect(Integer.valueOf((int) Math.ceil(width / this.mAspectRatioRange.getUpper().doubleValue())), Integer.valueOf((int) (width / this.mAspectRatioRange.getLower().doubleValue())));
            } catch (IllegalArgumentException e) {
                Log.v(TAG, "could not get supported heights for " + width);
                throw new IllegalArgumentException("unsupported width");
            }
        }

        public Range<Double> getSupportedFrameRatesFor(int width, int height) {
            Range<Integer> range = this.mHeightRange;
            if (!supports(Integer.valueOf(width), Integer.valueOf(height), null)) {
                throw new IllegalArgumentException("unsupported size");
            }
            int blockCount = Utils.divUp(width, this.mBlockWidth) * Utils.divUp(height, this.mBlockHeight);
            return Range.create(Double.valueOf(Math.max(this.mBlocksPerSecondRange.getLower().longValue() / blockCount, this.mFrameRateRange.getLower().intValue())), Double.valueOf(Math.min(this.mBlocksPerSecondRange.getUpper().longValue() / blockCount, this.mFrameRateRange.getUpper().intValue())));
        }

        private synchronized int getBlockCount(int width, int height) {
            return Utils.divUp(width, this.mBlockWidth) * Utils.divUp(height, this.mBlockHeight);
        }

        private synchronized Size findClosestSize(int width, int height) {
            int targetBlockCount = getBlockCount(width, height);
            Size closestSize = null;
            int minDiff = Integer.MAX_VALUE;
            for (Size size : this.mMeasuredFrameRates.keySet()) {
                int diff = Math.abs(targetBlockCount - getBlockCount(size.getWidth(), size.getHeight()));
                if (diff < minDiff) {
                    minDiff = diff;
                    closestSize = size;
                }
            }
            return closestSize;
        }

        private synchronized Range<Double> estimateFrameRatesFor(int width, int height) {
            Size size = findClosestSize(width, height);
            Range<Long> range = this.mMeasuredFrameRates.get(size);
            Double ratio = Double.valueOf(getBlockCount(size.getWidth(), size.getHeight()) / Math.max(getBlockCount(width, height), 1));
            return Range.create(Double.valueOf(range.getLower().longValue() * ratio.doubleValue()), Double.valueOf(range.getUpper().longValue() * ratio.doubleValue()));
        }

        public Range<Double> getAchievableFrameRatesFor(int width, int height) {
            if (!supports(Integer.valueOf(width), Integer.valueOf(height), null)) {
                throw new IllegalArgumentException("unsupported size");
            }
            if (this.mMeasuredFrameRates == null || this.mMeasuredFrameRates.size() <= 0) {
                Log.w(TAG, "Codec did not publish any measurement data.");
                return null;
            }
            return estimateFrameRatesFor(width, height);
        }

        public boolean areSizeAndRateSupported(int width, int height, double frameRate) {
            return supports(Integer.valueOf(width), Integer.valueOf(height), Double.valueOf(frameRate));
        }

        public boolean isSizeSupported(int width, int height) {
            return supports(Integer.valueOf(width), Integer.valueOf(height), null);
        }

        private synchronized boolean supports(Integer width, Integer height, Number rate) {
            boolean ok = true;
            boolean z = false;
            if (1 != 0 && width != null) {
                ok = this.mWidthRange.contains((Range<Integer>) width) && width.intValue() % this.mWidthAlignment == 0;
            }
            if (ok && height != null) {
                ok = this.mHeightRange.contains((Range<Integer>) height) && height.intValue() % this.mHeightAlignment == 0;
            }
            if (ok && rate != null) {
                ok = this.mFrameRateRange.contains(Utils.intRangeFor(rate.doubleValue()));
            }
            if (ok && height != null && width != null) {
                boolean ok2 = Math.min(height.intValue(), width.intValue()) <= this.mSmallerDimensionUpperLimit;
                int widthInBlocks = Utils.divUp(width.intValue(), this.mBlockWidth);
                int heightInBlocks = Utils.divUp(height.intValue(), this.mBlockHeight);
                int blockCount = widthInBlocks * heightInBlocks;
                if (ok2 && this.mBlockCountRange.contains((Range<Integer>) Integer.valueOf(blockCount)) && this.mBlockAspectRatioRange.contains((Range<Rational>) new Rational(widthInBlocks, heightInBlocks)) && this.mAspectRatioRange.contains((Range<Rational>) new Rational(width.intValue(), height.intValue()))) {
                    z = true;
                }
                boolean ok3 = z;
                if (ok3 && rate != null) {
                    double blocksPerSec = blockCount * rate.doubleValue();
                    return this.mBlocksPerSecondRange.contains(Utils.longRangeFor(blocksPerSec));
                }
                return ok3;
            }
            return ok;
        }

        public synchronized boolean supportsFormat(MediaFormat format) {
            Map<String, Object> map = format.getMap();
            Integer width = (Integer) map.get("width");
            Integer height = (Integer) map.get("height");
            Number rate = (Number) map.get(MediaFormat.KEY_FRAME_RATE);
            return supports(width, height, rate) && CodecCapabilities.supportsBitrate(this.mBitrateRange, format);
        }

        private synchronized VideoCapabilities() {
        }

        private protected static VideoCapabilities create(MediaFormat info, CodecCapabilities parent) {
            VideoCapabilities caps = new VideoCapabilities();
            caps.init(info, parent);
            return caps;
        }

        private synchronized void init(MediaFormat info, CodecCapabilities parent) {
            this.mParent = parent;
            initWithPlatformLimits();
            applyLevelLimits();
            parseFromInfo(info);
            updateLimits();
        }

        public synchronized Size getBlockSize() {
            return new Size(this.mBlockWidth, this.mBlockHeight);
        }

        public synchronized Range<Integer> getBlockCountRange() {
            return this.mBlockCountRange;
        }

        public synchronized Range<Long> getBlocksPerSecondRange() {
            return this.mBlocksPerSecondRange;
        }

        public synchronized Range<Rational> getAspectRatioRange(boolean blocks) {
            return blocks ? this.mBlockAspectRatioRange : this.mAspectRatioRange;
        }

        private synchronized void initWithPlatformLimits() {
            this.mBitrateRange = MediaCodecInfo.BITRATE_RANGE;
            this.mWidthRange = MediaCodecInfo.SIZE_RANGE;
            this.mHeightRange = MediaCodecInfo.SIZE_RANGE;
            this.mFrameRateRange = MediaCodecInfo.FRAME_RATE_RANGE;
            this.mHorizontalBlockRange = MediaCodecInfo.SIZE_RANGE;
            this.mVerticalBlockRange = MediaCodecInfo.SIZE_RANGE;
            this.mBlockCountRange = MediaCodecInfo.POSITIVE_INTEGERS;
            this.mBlocksPerSecondRange = MediaCodecInfo.POSITIVE_LONGS;
            this.mBlockAspectRatioRange = MediaCodecInfo.POSITIVE_RATIONALS;
            this.mAspectRatioRange = MediaCodecInfo.POSITIVE_RATIONALS;
            this.mWidthAlignment = 2;
            this.mHeightAlignment = 2;
            this.mBlockWidth = 2;
            this.mBlockHeight = 2;
            this.mSmallerDimensionUpperLimit = ((Integer) MediaCodecInfo.SIZE_RANGE.getUpper()).intValue();
        }

        private synchronized Map<Size, Range<Long>> getMeasuredFrameRates(Map<String, Object> map) {
            Range<Long> range;
            Map<Size, Range<Long>> ret = new HashMap<>();
            Set<String> keys = map.keySet();
            for (String key : keys) {
                if (key.startsWith("measured-frame-rate-")) {
                    key.substring("measured-frame-rate-".length());
                    String[] temp = key.split(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                    if (temp.length == 5) {
                        String sizeStr = temp[3];
                        Size size = Utils.parseSize(sizeStr, null);
                        if (size != null && size.getWidth() * size.getHeight() > 0 && (range = Utils.parseLongRange(map.get(key), null)) != null && range.getLower().longValue() >= 0 && range.getUpper().longValue() >= 0) {
                            ret.put(size, range);
                        }
                    }
                }
            }
            return ret;
        }

        private static synchronized Pair<Range<Integer>, Range<Integer>> parseWidthHeightRanges(Object o) {
            Pair<Size, Size> range = Utils.parseSizeRange(o);
            if (range != null) {
                try {
                    return Pair.create(Range.create(Integer.valueOf(range.first.getWidth()), Integer.valueOf(range.second.getWidth())), Range.create(Integer.valueOf(range.first.getHeight()), Integer.valueOf(range.second.getHeight())));
                } catch (IllegalArgumentException e) {
                    Log.w(TAG, "could not parse size range '" + o + "'");
                    return null;
                }
            }
            return null;
        }

        public static synchronized int equivalentVP9Level(MediaFormat info) {
            int D;
            Map<String, Object> map = info.getMap();
            Size blockSize = Utils.parseSize(map.get("block-size"), new Size(8, 8));
            int BS = blockSize.getWidth() * blockSize.getHeight();
            Range<Integer> counts = Utils.parseIntRange(map.get("block-count-range"), null);
            int FS = counts == null ? 0 : counts.getUpper().intValue() * BS;
            Range<Long> blockRates = Utils.parseLongRange(map.get("blocks-per-second-range"), null);
            long SR = blockRates == null ? 0L : BS * blockRates.getUpper().longValue();
            Pair<Range<Integer>, Range<Integer>> dimensionRanges = parseWidthHeightRanges(map.get("size-range"));
            if (dimensionRanges == null) {
                D = 0;
            } else {
                D = Math.max(dimensionRanges.first.getUpper().intValue(), dimensionRanges.second.getUpper().intValue());
            }
            Range<Integer> bitRates = Utils.parseIntRange(map.get("bitrate-range"), null);
            int BR = bitRates != null ? Utils.divUp(bitRates.getUpper().intValue(), 1000) : 0;
            if (SR <= 829440 && FS <= 36864 && BR <= 200 && D <= 512) {
                return 1;
            }
            if (SR <= 2764800 && FS <= 73728 && BR <= 800 && D <= 768) {
                return 2;
            }
            if (SR <= 4608000 && FS <= 122880 && BR <= 1800 && D <= 960) {
                return 4;
            }
            if (SR > 9216000 || FS > 245760 || BR > 3600 || D > 1344) {
                if (SR <= 20736000 && FS <= 552960 && BR <= 7200 && D <= 2048) {
                    return 16;
                }
                if (SR <= 36864000 && FS <= 983040 && BR <= 12000 && D <= 2752) {
                    return 32;
                }
                if (SR <= 83558400 && FS <= 2228224 && BR <= 18000 && D <= 4160) {
                    return 64;
                }
                if (SR <= 160432128 && FS <= 2228224 && BR <= 30000 && D <= 4160) {
                    return 128;
                }
                if (SR <= 311951360 && FS <= 8912896 && BR <= 60000 && D <= 8384) {
                    return 256;
                }
                if (SR > 588251136 || FS > 8912896 || BR > 120000 || D > 8384) {
                    if (SR <= 1176502272 && FS <= 8912896 && BR <= 180000 && D <= 8384) {
                        return 1024;
                    }
                    if (SR <= 1176502272 && FS <= 35651584 && BR <= 180000 && D <= 16832) {
                        return 2048;
                    }
                    if (SR > 2353004544L || FS > 35651584 || BR > 240000 || D > 16832) {
                        return (SR > 4706009088L || FS > 35651584 || BR > 480000 || D <= 16832) ? 8192 : 8192;
                    }
                    return 4096;
                }
                return 512;
            }
            return 8;
        }

        /* JADX WARN: Removed duplicated region for block: B:25:0x01c6  */
        /* JADX WARN: Removed duplicated region for block: B:49:0x026b  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x0277  */
        /* JADX WARN: Removed duplicated region for block: B:53:0x0283  */
        /* JADX WARN: Removed duplicated region for block: B:55:0x028f  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x02ae  */
        /* JADX WARN: Removed duplicated region for block: B:59:0x02ce  */
        /* JADX WARN: Removed duplicated region for block: B:61:0x02ec  */
        /* JADX WARN: Removed duplicated region for block: B:63:0x02f8  */
        /* JADX WARN: Removed duplicated region for block: B:65:0x0304  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private synchronized void parseFromInfo(android.media.MediaFormat r31) {
            /*
                Method dump skipped, instructions count: 803
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.MediaCodecInfo.VideoCapabilities.parseFromInfo(android.media.MediaFormat):void");
        }

        private synchronized void applyBlockLimits(int blockWidth, int blockHeight, Range<Integer> counts, Range<Long> rates, Range<Rational> ratios) {
            MediaCodecInfo.checkPowerOfTwo(blockWidth, "blockWidth must be a power of two");
            MediaCodecInfo.checkPowerOfTwo(blockHeight, "blockHeight must be a power of two");
            int newBlockWidth = Math.max(blockWidth, this.mBlockWidth);
            int newBlockHeight = Math.max(blockHeight, this.mBlockHeight);
            int factor = ((newBlockWidth * newBlockHeight) / this.mBlockWidth) / this.mBlockHeight;
            if (factor != 1) {
                this.mBlockCountRange = Utils.factorRange(this.mBlockCountRange, factor);
                this.mBlocksPerSecondRange = Utils.factorRange(this.mBlocksPerSecondRange, factor);
                this.mBlockAspectRatioRange = Utils.scaleRange(this.mBlockAspectRatioRange, newBlockHeight / this.mBlockHeight, newBlockWidth / this.mBlockWidth);
                this.mHorizontalBlockRange = Utils.factorRange(this.mHorizontalBlockRange, newBlockWidth / this.mBlockWidth);
                this.mVerticalBlockRange = Utils.factorRange(this.mVerticalBlockRange, newBlockHeight / this.mBlockHeight);
            }
            int factor2 = ((newBlockWidth * newBlockHeight) / blockWidth) / blockHeight;
            if (factor2 != 1) {
                counts = Utils.factorRange(counts, factor2);
                rates = Utils.factorRange(rates, factor2);
                ratios = Utils.scaleRange(ratios, newBlockHeight / blockHeight, newBlockWidth / blockWidth);
            }
            this.mBlockCountRange = this.mBlockCountRange.intersect(counts);
            this.mBlocksPerSecondRange = this.mBlocksPerSecondRange.intersect(rates);
            this.mBlockAspectRatioRange = this.mBlockAspectRatioRange.intersect(ratios);
            this.mBlockWidth = newBlockWidth;
            this.mBlockHeight = newBlockHeight;
        }

        private synchronized void applyAlignment(int widthAlignment, int heightAlignment) {
            MediaCodecInfo.checkPowerOfTwo(widthAlignment, "widthAlignment must be a power of two");
            MediaCodecInfo.checkPowerOfTwo(heightAlignment, "heightAlignment must be a power of two");
            if (widthAlignment > this.mBlockWidth || heightAlignment > this.mBlockHeight) {
                applyBlockLimits(Math.max(widthAlignment, this.mBlockWidth), Math.max(heightAlignment, this.mBlockHeight), MediaCodecInfo.POSITIVE_INTEGERS, MediaCodecInfo.POSITIVE_LONGS, MediaCodecInfo.POSITIVE_RATIONALS);
            }
            this.mWidthAlignment = Math.max(widthAlignment, this.mWidthAlignment);
            this.mHeightAlignment = Math.max(heightAlignment, this.mHeightAlignment);
            this.mWidthRange = Utils.alignRange(this.mWidthRange, this.mWidthAlignment);
            this.mHeightRange = Utils.alignRange(this.mHeightRange, this.mHeightAlignment);
        }

        private synchronized void updateLimits() {
            this.mHorizontalBlockRange = this.mHorizontalBlockRange.intersect(Utils.factorRange(this.mWidthRange, this.mBlockWidth));
            this.mHorizontalBlockRange = this.mHorizontalBlockRange.intersect(Range.create(Integer.valueOf(this.mBlockCountRange.getLower().intValue() / this.mVerticalBlockRange.getUpper().intValue()), Integer.valueOf(this.mBlockCountRange.getUpper().intValue() / this.mVerticalBlockRange.getLower().intValue())));
            this.mVerticalBlockRange = this.mVerticalBlockRange.intersect(Utils.factorRange(this.mHeightRange, this.mBlockHeight));
            this.mVerticalBlockRange = this.mVerticalBlockRange.intersect(Range.create(Integer.valueOf(this.mBlockCountRange.getLower().intValue() / this.mHorizontalBlockRange.getUpper().intValue()), Integer.valueOf(this.mBlockCountRange.getUpper().intValue() / this.mHorizontalBlockRange.getLower().intValue())));
            this.mBlockCountRange = this.mBlockCountRange.intersect(Range.create(Integer.valueOf(this.mHorizontalBlockRange.getLower().intValue() * this.mVerticalBlockRange.getLower().intValue()), Integer.valueOf(this.mHorizontalBlockRange.getUpper().intValue() * this.mVerticalBlockRange.getUpper().intValue())));
            this.mBlockAspectRatioRange = this.mBlockAspectRatioRange.intersect(new Rational(this.mHorizontalBlockRange.getLower().intValue(), this.mVerticalBlockRange.getUpper().intValue()), new Rational(this.mHorizontalBlockRange.getUpper().intValue(), this.mVerticalBlockRange.getLower().intValue()));
            this.mWidthRange = this.mWidthRange.intersect(Integer.valueOf(((this.mHorizontalBlockRange.getLower().intValue() - 1) * this.mBlockWidth) + this.mWidthAlignment), Integer.valueOf(this.mHorizontalBlockRange.getUpper().intValue() * this.mBlockWidth));
            this.mHeightRange = this.mHeightRange.intersect(Integer.valueOf(((this.mVerticalBlockRange.getLower().intValue() - 1) * this.mBlockHeight) + this.mHeightAlignment), Integer.valueOf(this.mVerticalBlockRange.getUpper().intValue() * this.mBlockHeight));
            this.mAspectRatioRange = this.mAspectRatioRange.intersect(new Rational(this.mWidthRange.getLower().intValue(), this.mHeightRange.getUpper().intValue()), new Rational(this.mWidthRange.getUpper().intValue(), this.mHeightRange.getLower().intValue()));
            this.mSmallerDimensionUpperLimit = Math.min(this.mSmallerDimensionUpperLimit, Math.min(this.mWidthRange.getUpper().intValue(), this.mHeightRange.getUpper().intValue()));
            this.mBlocksPerSecondRange = this.mBlocksPerSecondRange.intersect(Long.valueOf(this.mBlockCountRange.getLower().intValue() * this.mFrameRateRange.getLower().intValue()), Long.valueOf(this.mBlockCountRange.getUpper().intValue() * this.mFrameRateRange.getUpper().intValue()));
            this.mFrameRateRange = this.mFrameRateRange.intersect(Integer.valueOf((int) (this.mBlocksPerSecondRange.getLower().longValue() / this.mBlockCountRange.getUpper().intValue())), Integer.valueOf((int) (this.mBlocksPerSecondRange.getUpper().longValue() / this.mBlockCountRange.getLower().intValue())));
        }

        private synchronized void applyMacroBlockLimits(int maxHorizontalBlocks, int maxVerticalBlocks, int maxBlocks, long maxBlocksPerSecond, int blockWidth, int blockHeight, int widthAlignment, int heightAlignment) {
            applyMacroBlockLimits(1, 1, maxHorizontalBlocks, maxVerticalBlocks, maxBlocks, maxBlocksPerSecond, blockWidth, blockHeight, widthAlignment, heightAlignment);
        }

        private synchronized void applyMacroBlockLimits(int minHorizontalBlocks, int minVerticalBlocks, int maxHorizontalBlocks, int maxVerticalBlocks, int maxBlocks, long maxBlocksPerSecond, int blockWidth, int blockHeight, int widthAlignment, int heightAlignment) {
            applyAlignment(widthAlignment, heightAlignment);
            applyBlockLimits(blockWidth, blockHeight, Range.create(1, Integer.valueOf(maxBlocks)), Range.create(1L, Long.valueOf(maxBlocksPerSecond)), Range.create(new Rational(1, maxVerticalBlocks), new Rational(maxHorizontalBlocks, 1)));
            this.mHorizontalBlockRange = this.mHorizontalBlockRange.intersect(Integer.valueOf(Utils.divUp(minHorizontalBlocks, this.mBlockWidth / blockWidth)), Integer.valueOf(maxHorizontalBlocks / (this.mBlockWidth / blockWidth)));
            this.mVerticalBlockRange = this.mVerticalBlockRange.intersect(Integer.valueOf(Utils.divUp(minVerticalBlocks, this.mBlockHeight / blockHeight)), Integer.valueOf(maxVerticalBlocks / (this.mBlockHeight / blockHeight)));
        }

        /* JADX WARN: Removed duplicated region for block: B:134:0x05ed  */
        /* JADX WARN: Removed duplicated region for block: B:137:0x0602  */
        /* JADX WARN: Removed duplicated region for block: B:138:0x0610  */
        /* JADX WARN: Removed duplicated region for block: B:196:0x0805  */
        /* JADX WARN: Removed duplicated region for block: B:197:0x080f  */
        /* JADX WARN: Removed duplicated region for block: B:295:0x01a7 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:298:0x0368 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:51:0x01a3  */
        /* JADX WARN: Removed duplicated region for block: B:77:0x0364  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private synchronized void applyLevelLimits() {
            /*
                Method dump skipped, instructions count: 3568
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.MediaCodecInfo.VideoCapabilities.applyLevelLimits():void");
        }
    }

    /* loaded from: classes.dex */
    public static final class EncoderCapabilities {
        public static final int BITRATE_MODE_CBR = 2;
        public static final int BITRATE_MODE_CQ = 0;
        public static final int BITRATE_MODE_VBR = 1;
        private static final Feature[] bitrates = {new Feature("VBR", 1, true), new Feature("CBR", 2, false), new Feature("CQ", 0, false)};
        private int mBitControl;
        private Range<Integer> mComplexityRange;
        private Integer mDefaultComplexity;
        private Integer mDefaultQuality;
        private CodecCapabilities mParent;
        private Range<Integer> mQualityRange;
        private String mQualityScale;

        public Range<Integer> getQualityRange() {
            return this.mQualityRange;
        }

        public Range<Integer> getComplexityRange() {
            return this.mComplexityRange;
        }

        private static synchronized int parseBitrateMode(String mode) {
            Feature[] featureArr;
            for (Feature feat : bitrates) {
                if (feat.mName.equalsIgnoreCase(mode)) {
                    return feat.mValue;
                }
            }
            return 0;
        }

        public boolean isBitrateModeSupported(int mode) {
            Feature[] featureArr;
            for (Feature feat : bitrates) {
                if (mode == feat.mValue) {
                    return (this.mBitControl & (1 << mode)) != 0;
                }
            }
            return false;
        }

        private synchronized EncoderCapabilities() {
        }

        public static synchronized EncoderCapabilities create(MediaFormat info, CodecCapabilities parent) {
            EncoderCapabilities caps = new EncoderCapabilities();
            caps.init(info, parent);
            return caps;
        }

        private synchronized void init(MediaFormat info, CodecCapabilities parent) {
            this.mParent = parent;
            this.mComplexityRange = Range.create(0, 0);
            this.mQualityRange = Range.create(0, 0);
            this.mBitControl = 2;
            applyLevelLimits();
            parseFromInfo(info);
        }

        private synchronized void applyLevelLimits() {
            String mime = this.mParent.getMimeType();
            if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_FLAC)) {
                this.mComplexityRange = Range.create(0, 8);
                this.mBitControl = 1;
            } else if (mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_AMR_NB) || mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_AMR_WB) || mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_G711_ALAW) || mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_G711_MLAW) || mime.equalsIgnoreCase(MediaFormat.MIMETYPE_AUDIO_MSGSM)) {
                this.mBitControl = 4;
            }
        }

        private synchronized void parseFromInfo(MediaFormat info) {
            String[] split;
            Map<String, Object> map = info.getMap();
            if (info.containsKey("complexity-range")) {
                this.mComplexityRange = Utils.parseIntRange(info.getString("complexity-range"), this.mComplexityRange);
            }
            if (info.containsKey("quality-range")) {
                this.mQualityRange = Utils.parseIntRange(info.getString("quality-range"), this.mQualityRange);
            }
            if (info.containsKey("feature-bitrate-modes")) {
                for (String mode : info.getString("feature-bitrate-modes").split(",")) {
                    this.mBitControl |= 1 << parseBitrateMode(mode);
                }
            }
            try {
                this.mDefaultComplexity = Integer.valueOf(Integer.parseInt((String) map.get("complexity-default")));
            } catch (NumberFormatException e) {
            }
            try {
                this.mDefaultQuality = Integer.valueOf(Integer.parseInt((String) map.get("quality-default")));
            } catch (NumberFormatException e2) {
            }
            this.mQualityScale = (String) map.get("quality-scale");
        }

        private synchronized boolean supports(Integer complexity, Integer quality, Integer profile) {
            boolean ok = true;
            if (1 != 0 && complexity != null) {
                ok = this.mComplexityRange.contains((Range<Integer>) complexity);
            }
            if (ok && quality != null) {
                ok = this.mQualityRange.contains((Range<Integer>) quality);
            }
            if (ok && profile != null) {
                CodecProfileLevel[] codecProfileLevelArr = this.mParent.profileLevels;
                int length = codecProfileLevelArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    CodecProfileLevel pl = codecProfileLevelArr[i];
                    if (pl.profile != profile.intValue()) {
                        i++;
                    } else {
                        profile = null;
                        break;
                    }
                }
                return profile == null;
            }
            return ok;
        }

        public synchronized void getDefaultFormat(MediaFormat format) {
            Feature[] featureArr;
            if (!this.mQualityRange.getUpper().equals(this.mQualityRange.getLower()) && this.mDefaultQuality != null) {
                format.setInteger(MediaFormat.KEY_QUALITY, this.mDefaultQuality.intValue());
            }
            if (!this.mComplexityRange.getUpper().equals(this.mComplexityRange.getLower()) && this.mDefaultComplexity != null) {
                format.setInteger(MediaFormat.KEY_COMPLEXITY, this.mDefaultComplexity.intValue());
            }
            for (Feature feat : bitrates) {
                if ((this.mBitControl & (1 << feat.mValue)) != 0) {
                    format.setInteger(MediaFormat.KEY_BITRATE_MODE, feat.mValue);
                    return;
                }
            }
        }

        public synchronized boolean supportsFormat(MediaFormat format) {
            Map<String, Object> map = format.getMap();
            String mime = this.mParent.getMimeType();
            Integer mode = (Integer) map.get(MediaFormat.KEY_BITRATE_MODE);
            if (mode != null && !isBitrateModeSupported(mode.intValue())) {
                return false;
            }
            Integer complexity = (Integer) map.get(MediaFormat.KEY_COMPLEXITY);
            if (MediaFormat.MIMETYPE_AUDIO_FLAC.equalsIgnoreCase(mime)) {
                Integer flacComplexity = (Integer) map.get(MediaFormat.KEY_FLAC_COMPRESSION_LEVEL);
                if (complexity == null) {
                    complexity = flacComplexity;
                } else if (flacComplexity != null && !complexity.equals(flacComplexity)) {
                    throw new IllegalArgumentException("conflicting values for complexity and flac-compression-level");
                }
            }
            Integer profile = (Integer) map.get(MediaFormat.KEY_PROFILE);
            if (MediaFormat.MIMETYPE_AUDIO_AAC.equalsIgnoreCase(mime)) {
                Integer aacProfile = (Integer) map.get(MediaFormat.KEY_AAC_PROFILE);
                if (profile == null) {
                    profile = aacProfile;
                } else if (aacProfile != null && !aacProfile.equals(profile)) {
                    throw new IllegalArgumentException("conflicting values for profile and aac-profile");
                }
            }
            Integer quality = (Integer) map.get(MediaFormat.KEY_QUALITY);
            return supports(complexity, quality, profile);
        }
    }

    /* loaded from: classes.dex */
    public static final class CodecProfileLevel {
        public static final int AACObjectELD = 39;
        public static final int AACObjectERLC = 17;
        public static final int AACObjectERScalable = 20;
        public static final int AACObjectHE = 5;
        public static final int AACObjectHE_PS = 29;
        public static final int AACObjectLC = 2;
        public static final int AACObjectLD = 23;
        public static final int AACObjectLTP = 4;
        public static final int AACObjectMain = 1;
        public static final int AACObjectSSR = 3;
        public static final int AACObjectScalable = 6;
        public static final int AACObjectXHE = 42;
        public static final int AVCLevel1 = 1;
        public static final int AVCLevel11 = 4;
        public static final int AVCLevel12 = 8;
        public static final int AVCLevel13 = 16;
        public static final int AVCLevel1b = 2;
        public static final int AVCLevel2 = 32;
        public static final int AVCLevel21 = 64;
        public static final int AVCLevel22 = 128;
        public static final int AVCLevel3 = 256;
        public static final int AVCLevel31 = 512;
        public static final int AVCLevel32 = 1024;
        public static final int AVCLevel4 = 2048;
        public static final int AVCLevel41 = 4096;
        public static final int AVCLevel42 = 8192;
        public static final int AVCLevel5 = 16384;
        public static final int AVCLevel51 = 32768;
        public static final int AVCLevel52 = 65536;
        public static final int AVCProfileBaseline = 1;
        public static final int AVCProfileConstrainedBaseline = 65536;
        public static final int AVCProfileConstrainedHigh = 524288;
        public static final int AVCProfileExtended = 4;
        public static final int AVCProfileHigh = 8;
        public static final int AVCProfileHigh10 = 16;
        public static final int AVCProfileHigh422 = 32;
        public static final int AVCProfileHigh444 = 64;
        public static final int AVCProfileMain = 2;
        public static final int DolbyVisionLevelFhd24 = 4;
        public static final int DolbyVisionLevelFhd30 = 8;
        public static final int DolbyVisionLevelFhd60 = 16;
        public static final int DolbyVisionLevelHd24 = 1;
        public static final int DolbyVisionLevelHd30 = 2;
        public static final int DolbyVisionLevelUhd24 = 32;
        public static final int DolbyVisionLevelUhd30 = 64;
        public static final int DolbyVisionLevelUhd48 = 128;
        public static final int DolbyVisionLevelUhd60 = 256;
        public static final int DolbyVisionProfileDvavPen = 2;
        public static final int DolbyVisionProfileDvavPer = 1;
        public static final int DolbyVisionProfileDvavSe = 512;
        public static final int DolbyVisionProfileDvheDen = 8;
        public static final int DolbyVisionProfileDvheDer = 4;
        public static final int DolbyVisionProfileDvheDtb = 128;
        public static final int DolbyVisionProfileDvheDth = 64;
        public static final int DolbyVisionProfileDvheDtr = 16;
        public static final int DolbyVisionProfileDvheSt = 256;
        public static final int DolbyVisionProfileDvheStn = 32;
        public static final int H263Level10 = 1;
        public static final int H263Level20 = 2;
        public static final int H263Level30 = 4;
        public static final int H263Level40 = 8;
        public static final int H263Level45 = 16;
        public static final int H263Level50 = 32;
        public static final int H263Level60 = 64;
        public static final int H263Level70 = 128;
        public static final int H263ProfileBackwardCompatible = 4;
        public static final int H263ProfileBaseline = 1;
        public static final int H263ProfileH320Coding = 2;
        public static final int H263ProfileHighCompression = 32;
        public static final int H263ProfileHighLatency = 256;
        public static final int H263ProfileISWV2 = 8;
        public static final int H263ProfileISWV3 = 16;
        public static final int H263ProfileInterlace = 128;
        public static final int H263ProfileInternet = 64;
        public static final int HEVCHighTierLevel1 = 2;
        public static final int HEVCHighTierLevel2 = 8;
        public static final int HEVCHighTierLevel21 = 32;
        public static final int HEVCHighTierLevel3 = 128;
        public static final int HEVCHighTierLevel31 = 512;
        public static final int HEVCHighTierLevel4 = 2048;
        public static final int HEVCHighTierLevel41 = 8192;
        public static final int HEVCHighTierLevel5 = 32768;
        public static final int HEVCHighTierLevel51 = 131072;
        public static final int HEVCHighTierLevel52 = 524288;
        public static final int HEVCHighTierLevel6 = 2097152;
        public static final int HEVCHighTierLevel61 = 8388608;
        public static final int HEVCHighTierLevel62 = 33554432;
        private static final int HEVCHighTierLevels = 44739242;
        public static final int HEVCMainTierLevel1 = 1;
        public static final int HEVCMainTierLevel2 = 4;
        public static final int HEVCMainTierLevel21 = 16;
        public static final int HEVCMainTierLevel3 = 64;
        public static final int HEVCMainTierLevel31 = 256;
        public static final int HEVCMainTierLevel4 = 1024;
        public static final int HEVCMainTierLevel41 = 4096;
        public static final int HEVCMainTierLevel5 = 16384;
        public static final int HEVCMainTierLevel51 = 65536;
        public static final int HEVCMainTierLevel52 = 262144;
        public static final int HEVCMainTierLevel6 = 1048576;
        public static final int HEVCMainTierLevel61 = 4194304;
        public static final int HEVCMainTierLevel62 = 16777216;
        public static final int HEVCProfileMain = 1;
        public static final int HEVCProfileMain10 = 2;
        public static final int HEVCProfileMain10HDR10 = 4096;
        public static final int HEVCProfileMainStill = 4;
        public static final int MPEG2LevelH14 = 2;
        public static final int MPEG2LevelHL = 3;
        public static final int MPEG2LevelHP = 4;
        public static final int MPEG2LevelLL = 0;
        public static final int MPEG2LevelML = 1;
        public static final int MPEG2Profile422 = 2;
        public static final int MPEG2ProfileHigh = 5;
        public static final int MPEG2ProfileMain = 1;
        public static final int MPEG2ProfileSNR = 3;
        public static final int MPEG2ProfileSimple = 0;
        public static final int MPEG2ProfileSpatial = 4;
        public static final int MPEG4Level0 = 1;
        public static final int MPEG4Level0b = 2;
        public static final int MPEG4Level1 = 4;
        public static final int MPEG4Level2 = 8;
        public static final int MPEG4Level3 = 16;
        public static final int MPEG4Level3b = 24;
        public static final int MPEG4Level4 = 32;
        public static final int MPEG4Level4a = 64;
        public static final int MPEG4Level5 = 128;
        public static final int MPEG4Level6 = 256;
        public static final int MPEG4ProfileAdvancedCoding = 4096;
        public static final int MPEG4ProfileAdvancedCore = 8192;
        public static final int MPEG4ProfileAdvancedRealTime = 1024;
        public static final int MPEG4ProfileAdvancedScalable = 16384;
        public static final int MPEG4ProfileAdvancedSimple = 32768;
        public static final int MPEG4ProfileBasicAnimated = 256;
        public static final int MPEG4ProfileCore = 4;
        public static final int MPEG4ProfileCoreScalable = 2048;
        public static final int MPEG4ProfileHybrid = 512;
        public static final int MPEG4ProfileMain = 8;
        public static final int MPEG4ProfileNbit = 16;
        public static final int MPEG4ProfileScalableTexture = 32;
        public static final int MPEG4ProfileSimple = 1;
        public static final int MPEG4ProfileSimpleFBA = 128;
        public static final int MPEG4ProfileSimpleFace = 64;
        public static final int MPEG4ProfileSimpleScalable = 2;
        public static final int VP8Level_Version0 = 1;
        public static final int VP8Level_Version1 = 2;
        public static final int VP8Level_Version2 = 4;
        public static final int VP8Level_Version3 = 8;
        public static final int VP8ProfileMain = 1;
        public static final int VP9Level1 = 1;
        public static final int VP9Level11 = 2;
        public static final int VP9Level2 = 4;
        public static final int VP9Level21 = 8;
        public static final int VP9Level3 = 16;
        public static final int VP9Level31 = 32;
        public static final int VP9Level4 = 64;
        public static final int VP9Level41 = 128;
        public static final int VP9Level5 = 256;
        public static final int VP9Level51 = 512;
        public static final int VP9Level52 = 1024;
        public static final int VP9Level6 = 2048;
        public static final int VP9Level61 = 4096;
        public static final int VP9Level62 = 8192;
        public static final int VP9Profile0 = 1;
        public static final int VP9Profile1 = 2;
        public static final int VP9Profile2 = 4;
        public static final int VP9Profile2HDR = 4096;
        public static final int VP9Profile3 = 8;
        public static final int VP9Profile3HDR = 8192;
        public int level;
        public int profile;

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof CodecProfileLevel)) {
                return false;
            }
            CodecProfileLevel other = (CodecProfileLevel) obj;
            return other.profile == this.profile && other.level == this.level;
        }

        public int hashCode() {
            return Long.hashCode((this.profile << 32) | this.level);
        }
    }

    public final CodecCapabilities getCapabilitiesForType(String type) {
        CodecCapabilities caps = this.mCaps.get(type);
        if (caps == null) {
            throw new IllegalArgumentException("codec does not support type");
        }
        return caps.dup();
    }

    public synchronized MediaCodecInfo makeRegular() {
        ArrayList<CodecCapabilities> caps = new ArrayList<>();
        for (CodecCapabilities c : this.mCaps.values()) {
            if (c.isRegular()) {
                caps.add(c);
            }
        }
        if (caps.size() == 0) {
            return null;
        }
        if (caps.size() == this.mCaps.size()) {
            return this;
        }
        return new MediaCodecInfo(this.mName, this.mIsEncoder, (CodecCapabilities[]) caps.toArray(new CodecCapabilities[caps.size()]));
    }
}
