package android.filterpacks.videoproc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GLFrame;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.opengl.GLES20;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.MediaStore;
import android.util.Log;
import com.xiaopeng.util.FeatureOption;
import java.nio.ByteBuffer;
import java.util.Arrays;
/* loaded from: classes.dex */
public class BackDropperFilter extends Filter {
    public protected static final float DEFAULT_ACCEPT_STDDEV = 0.85f;
    public protected static final float DEFAULT_ADAPT_RATE_BG = 0.0f;
    public protected static final float DEFAULT_ADAPT_RATE_FG = 0.0f;
    public protected static final String DEFAULT_AUTO_WB_SCALE = "0.25";
    public protected static final float DEFAULT_EXPOSURE_CHANGE = 1.0f;
    public protected static final int DEFAULT_HIER_LRG_EXPONENT = 3;
    public protected static final float DEFAULT_HIER_LRG_SCALE = 0.7f;
    public protected static final int DEFAULT_HIER_MID_EXPONENT = 2;
    public protected static final float DEFAULT_HIER_MID_SCALE = 0.6f;
    public protected static final int DEFAULT_HIER_SML_EXPONENT = 0;
    public protected static final float DEFAULT_HIER_SML_SCALE = 0.5f;
    public protected static final float DEFAULT_LEARNING_ADAPT_RATE = 0.2f;
    public protected static final int DEFAULT_LEARNING_DONE_THRESHOLD = 20;
    public protected static final int DEFAULT_LEARNING_DURATION = 40;
    public protected static final int DEFAULT_LEARNING_VERIFY_DURATION = 10;
    public protected static final float DEFAULT_MASK_BLEND_BG = 0.65f;
    public protected static final float DEFAULT_MASK_BLEND_FG = 0.95f;
    public protected static final int DEFAULT_MASK_HEIGHT_EXPONENT = 8;
    public protected static final float DEFAULT_MASK_VERIFY_RATE = 0.25f;
    public protected static final int DEFAULT_MASK_WIDTH_EXPONENT = 8;
    public protected static final float DEFAULT_UV_SCALE_FACTOR = 1.35f;
    public protected static final float DEFAULT_WHITE_BALANCE_BLUE_CHANGE = 0.0f;
    public protected static final float DEFAULT_WHITE_BALANCE_RED_CHANGE = 0.0f;
    public protected static final int DEFAULT_WHITE_BALANCE_TOGGLE = 0;
    public protected static final float DEFAULT_Y_SCALE_FACTOR = 0.4f;
    public protected static final String DISTANCE_STORAGE_SCALE = "0.6";
    public protected static final String MASK_SMOOTH_EXPONENT = "2.0";
    public protected static final String MIN_VARIANCE = "3.0";
    public protected static final String RGB_TO_YUV_MATRIX = "0.299, -0.168736,  0.5,      0.000, 0.587, -0.331264, -0.418688, 0.000, 0.114,  0.5,      -0.081312, 0.000, 0.000,  0.5,       0.5,      1.000 ";
    public protected static final String TAG = "BackDropperFilter";
    public protected static final String VARIANCE_STORAGE_SCALE = "5.0";
    public protected static final String mAutomaticWhiteBalance = "uniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform float pyramid_depth;\nuniform bool autowb_toggle;\nvarying vec2 v_texcoord;\nvoid main() {\n   vec4 mean_video = texture2D(tex_sampler_0, v_texcoord, pyramid_depth);\n   vec4 mean_bg = texture2D(tex_sampler_1, v_texcoord, pyramid_depth);\n   float green_normalizer = mean_video.g / mean_bg.g;\n   vec4 adjusted_value = vec4(mean_bg.r / mean_video.r * green_normalizer, 1., \n                         mean_bg.b / mean_video.b * green_normalizer, 1.) * auto_wb_scale; \n   gl_FragColor = autowb_toggle ? adjusted_value : vec4(auto_wb_scale);\n}\n";
    public protected static final String mBgDistanceShader = "uniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform float subsample_level;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 fg_rgb = texture2D(tex_sampler_0, v_texcoord, subsample_level);\n  vec4 fg = coeff_yuv * vec4(fg_rgb.rgb, 1.);\n  vec4 mean = texture2D(tex_sampler_1, v_texcoord);\n  vec4 variance = inv_var_scale * texture2D(tex_sampler_2, v_texcoord);\n\n  float dist_y = gauss_dist_y(fg.r, mean.r, variance.r);\n  float dist_uv = gauss_dist_uv(fg.gb, mean.gb, variance.gb);\n  gl_FragColor = vec4(0.5*fg.rg, dist_scale*dist_y, dist_scale*dist_uv);\n}\n";
    public protected static final String mBgMaskShader = "uniform sampler2D tex_sampler_0;\nuniform float accept_variance;\nuniform vec2 yuv_weights;\nuniform float scale_lrg;\nuniform float scale_mid;\nuniform float scale_sml;\nuniform float exp_lrg;\nuniform float exp_mid;\nuniform float exp_sml;\nvarying vec2 v_texcoord;\nbool is_fg(vec2 dist_yc, float accept_variance) {\n  return ( dot(yuv_weights, dist_yc) >= accept_variance );\n}\nvoid main() {\n  vec4 dist_lrg_sc = texture2D(tex_sampler_0, v_texcoord, exp_lrg);\n  vec4 dist_mid_sc = texture2D(tex_sampler_0, v_texcoord, exp_mid);\n  vec4 dist_sml_sc = texture2D(tex_sampler_0, v_texcoord, exp_sml);\n  vec2 dist_lrg = inv_dist_scale * dist_lrg_sc.ba;\n  vec2 dist_mid = inv_dist_scale * dist_mid_sc.ba;\n  vec2 dist_sml = inv_dist_scale * dist_sml_sc.ba;\n  vec2 norm_dist = 0.75 * dist_sml / accept_variance;\n  bool is_fg_lrg = is_fg(dist_lrg, accept_variance * scale_lrg);\n  bool is_fg_mid = is_fg_lrg || is_fg(dist_mid, accept_variance * scale_mid);\n  float is_fg_sml =\n      float(is_fg_mid || is_fg(dist_sml, accept_variance * scale_sml));\n  float alpha = 0.5 * is_fg_sml + 0.3 * float(is_fg_mid) + 0.2 * float(is_fg_lrg);\n  gl_FragColor = vec4(alpha, norm_dist, is_fg_sml);\n}\n";
    public protected static final String mBgSubtractForceShader = "  vec4 ghost_rgb = (fg_adjusted * 0.7 + vec4(0.3,0.3,0.4,0.))*0.65 + \n                   0.35*bg_rgb;\n  float glow_start = 0.75 * mask_blend_bg; \n  float glow_max   = mask_blend_bg; \n  gl_FragColor = mask.a < glow_start ? bg_rgb : \n                 mask.a < glow_max ? mix(bg_rgb, vec4(0.9,0.9,1.0,1.0), \n                                     (mask.a - glow_start) / (glow_max - glow_start) ) : \n                 mask.a < mask_blend_fg ? mix(vec4(0.9,0.9,1.0,1.0), ghost_rgb, \n                                    (mask.a - glow_max) / (mask_blend_fg - glow_max) ) : \n                 ghost_rgb;\n}\n";
    public protected static final String mBgSubtractShader = "uniform mat3 bg_fit_transform;\nuniform float mask_blend_bg;\nuniform float mask_blend_fg;\nuniform float exposure_change;\nuniform float whitebalancered_change;\nuniform float whitebalanceblue_change;\nuniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform sampler2D tex_sampler_3;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec2 bg_texcoord = (bg_fit_transform * vec3(v_texcoord, 1.)).xy;\n  vec4 bg_rgb = texture2D(tex_sampler_1, bg_texcoord);\n  vec4 wb_auto_scale = texture2D(tex_sampler_3, v_texcoord) * exposure_change / auto_wb_scale;\n  vec4 wb_manual_scale = vec4(1. + whitebalancered_change, 1., 1. + whitebalanceblue_change, 1.);\n  vec4 fg_rgb = texture2D(tex_sampler_0, v_texcoord);\n  vec4 fg_adjusted = fg_rgb * wb_manual_scale * wb_auto_scale;\n  vec4 mask = texture2D(tex_sampler_2, v_texcoord, \n                      2.0);\n  float alpha = smoothstep(mask_blend_bg, mask_blend_fg, mask.a);\n  gl_FragColor = mix(bg_rgb, fg_adjusted, alpha);\n";
    public protected static final String mMaskVerifyShader = "uniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform float verify_rate;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 lastmask = texture2D(tex_sampler_0, v_texcoord);\n  vec4 mask = texture2D(tex_sampler_1, v_texcoord);\n  float newmask = mix(lastmask.a, mask.a, verify_rate);\n  gl_FragColor = vec4(0., 0., 0., newmask);\n}\n";
    public protected static final String mUpdateBgModelMeanShader = "uniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform float subsample_level;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 fg_rgb = texture2D(tex_sampler_0, v_texcoord, subsample_level);\n  vec4 fg = coeff_yuv * vec4(fg_rgb.rgb, 1.);\n  vec4 mean = texture2D(tex_sampler_1, v_texcoord);\n  vec4 mask = texture2D(tex_sampler_2, v_texcoord, \n                      2.0);\n\n  float alpha = local_adapt_rate(mask.a);\n  vec4 new_mean = mix(mean, fg, alpha);\n  gl_FragColor = new_mean;\n}\n";
    public protected static final String mUpdateBgModelVarianceShader = "uniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform sampler2D tex_sampler_3;\nuniform float subsample_level;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 fg_rgb = texture2D(tex_sampler_0, v_texcoord, subsample_level);\n  vec4 fg = coeff_yuv * vec4(fg_rgb.rgb, 1.);\n  vec4 mean = texture2D(tex_sampler_1, v_texcoord);\n  vec4 variance = inv_var_scale * texture2D(tex_sampler_2, v_texcoord);\n  vec4 mask = texture2D(tex_sampler_3, v_texcoord, \n                      2.0);\n\n  float alpha = local_adapt_rate(mask.a);\n  vec4 cur_variance = (fg-mean)*(fg-mean);\n  vec4 new_variance = mix(variance, cur_variance, alpha);\n  new_variance = max(new_variance, vec4(min_variance));\n  gl_FragColor = var_scale * new_variance;\n}\n";
    public protected final int BACKGROUND_FILL_CROP;
    public protected final int BACKGROUND_FIT;
    public protected final int BACKGROUND_STRETCH;
    public protected ShaderProgram copyShaderProgram;
    public protected boolean isOpen;
    @GenerateFieldPort(hasDefault = true, name = "acceptStddev")
    public protected float mAcceptStddev;
    @GenerateFieldPort(hasDefault = true, name = "adaptRateBg")
    public protected float mAdaptRateBg;
    @GenerateFieldPort(hasDefault = true, name = "adaptRateFg")
    public protected float mAdaptRateFg;
    @GenerateFieldPort(hasDefault = true, name = "learningAdaptRate")
    public protected float mAdaptRateLearning;
    public protected GLFrame mAutoWB;
    @GenerateFieldPort(hasDefault = true, name = "autowbToggle")
    public protected int mAutoWBToggle;
    public protected ShaderProgram mAutomaticWhiteBalanceProgram;
    public protected MutableFrameFormat mAverageFormat;
    @GenerateFieldPort(hasDefault = true, name = "backgroundFitMode")
    public protected int mBackgroundFitMode;
    public protected boolean mBackgroundFitModeChanged;
    public protected ShaderProgram mBgDistProgram;
    public protected GLFrame mBgInput;
    public protected ShaderProgram mBgMaskProgram;
    public protected GLFrame[] mBgMean;
    public protected ShaderProgram mBgSubtractProgram;
    public protected ShaderProgram mBgUpdateMeanProgram;
    public protected ShaderProgram mBgUpdateVarianceProgram;
    public protected GLFrame[] mBgVariance;
    @GenerateFieldPort(hasDefault = true, name = "chromaScale")
    public protected float mChromaScale;
    public protected ShaderProgram mCopyOutProgram;
    public protected GLFrame mDistance;
    @GenerateFieldPort(hasDefault = true, name = "exposureChange")
    public protected float mExposureChange;
    public protected int mFrameCount;
    @GenerateFieldPort(hasDefault = true, name = "hierLrgExp")
    public protected int mHierarchyLrgExp;
    @GenerateFieldPort(hasDefault = true, name = "hierLrgScale")
    public protected float mHierarchyLrgScale;
    @GenerateFieldPort(hasDefault = true, name = "hierMidExp")
    public protected int mHierarchyMidExp;
    @GenerateFieldPort(hasDefault = true, name = "hierMidScale")
    public protected float mHierarchyMidScale;
    @GenerateFieldPort(hasDefault = true, name = "hierSmlExp")
    public protected int mHierarchySmlExp;
    @GenerateFieldPort(hasDefault = true, name = "hierSmlScale")
    public protected float mHierarchySmlScale;
    @GenerateFieldPort(hasDefault = true, name = "learningDoneListener")
    public protected LearningDoneListener mLearningDoneListener;
    @GenerateFieldPort(hasDefault = true, name = "learningDuration")
    public protected int mLearningDuration;
    @GenerateFieldPort(hasDefault = true, name = "learningVerifyDuration")
    public protected int mLearningVerifyDuration;
    public protected final boolean mLogVerbose;
    @GenerateFieldPort(hasDefault = true, name = "lumScale")
    public protected float mLumScale;
    public protected GLFrame mMask;
    public protected GLFrame mMaskAverage;
    @GenerateFieldPort(hasDefault = true, name = "maskBg")
    public protected float mMaskBg;
    @GenerateFieldPort(hasDefault = true, name = "maskFg")
    public protected float mMaskFg;
    public protected MutableFrameFormat mMaskFormat;
    @GenerateFieldPort(hasDefault = true, name = "maskHeightExp")
    public protected int mMaskHeightExp;
    public protected GLFrame[] mMaskVerify;
    public protected ShaderProgram mMaskVerifyProgram;
    @GenerateFieldPort(hasDefault = true, name = "maskWidthExp")
    public protected int mMaskWidthExp;
    public protected MutableFrameFormat mMemoryFormat;
    @GenerateFieldPort(hasDefault = true, name = "mirrorBg")
    public protected boolean mMirrorBg;
    @GenerateFieldPort(hasDefault = true, name = MediaStore.Images.ImageColumns.ORIENTATION)
    public protected int mOrientation;
    public protected FrameFormat mOutputFormat;
    public protected boolean mPingPong;
    @GenerateFinalPort(hasDefault = true, name = "provideDebugOutputs")
    public protected boolean mProvideDebugOutputs;
    public protected int mPyramidDepth;
    public protected float mRelativeAspect;
    public protected boolean mStartLearning;
    public protected int mSubsampleLevel;
    @GenerateFieldPort(hasDefault = true, name = "useTheForce")
    public protected boolean mUseTheForce;
    @GenerateFieldPort(hasDefault = true, name = "maskVerifyRate")
    public protected float mVerifyRate;
    public protected GLFrame mVideoInput;
    @GenerateFieldPort(hasDefault = true, name = "whitebalanceblueChange")
    public protected float mWhiteBalanceBlueChange;
    @GenerateFieldPort(hasDefault = true, name = "whitebalanceredChange")
    public protected float mWhiteBalanceRedChange;
    public protected long startTime;
    public protected static final float[] DEFAULT_BG_FIT_TRANSFORM = {1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};
    public protected static final String[] mInputNames = {"video", "background"};
    public protected static final String[] mOutputNames = {"video"};
    public protected static final String[] mDebugOutputNames = {"debug1", "debug2"};
    public protected static String mSharedUtilShader = "precision mediump float;\nuniform float fg_adapt_rate;\nuniform float bg_adapt_rate;\nconst mat4 coeff_yuv = mat4(0.299, -0.168736,  0.5,      0.000, 0.587, -0.331264, -0.418688, 0.000, 0.114,  0.5,      -0.081312, 0.000, 0.000,  0.5,       0.5,      1.000 );\nconst float dist_scale = 0.6;\nconst float inv_dist_scale = 1. / dist_scale;\nconst float var_scale=5.0;\nconst float inv_var_scale = 1. / var_scale;\nconst float min_variance = inv_var_scale *3.0/ 256.;\nconst float auto_wb_scale = 0.25;\n\nfloat gauss_dist_y(float y, float mean, float variance) {\n  float dist = (y - mean) * (y - mean) / variance;\n  return dist;\n}\nfloat gauss_dist_uv(vec2 uv, vec2 mean, vec2 variance) {\n  vec2 dist = (uv - mean) * (uv - mean) / variance;\n  return dist.r + dist.g;\n}\nfloat local_adapt_rate(float alpha) {\n  return mix(bg_adapt_rate, fg_adapt_rate, alpha);\n}\n\n";

    /* loaded from: classes.dex */
    public interface LearningDoneListener {
        private protected synchronized void onLearningDone(BackDropperFilter backDropperFilter);
    }

    private protected synchronized BackDropperFilter(String name) {
        super(name);
        this.BACKGROUND_STRETCH = 0;
        this.BACKGROUND_FIT = 1;
        this.BACKGROUND_FILL_CROP = 2;
        this.mBackgroundFitMode = 2;
        this.mLearningDuration = 40;
        this.mLearningVerifyDuration = 10;
        this.mAcceptStddev = 0.85f;
        this.mHierarchyLrgScale = 0.7f;
        this.mHierarchyMidScale = 0.6f;
        this.mHierarchySmlScale = 0.5f;
        this.mMaskWidthExp = 8;
        this.mMaskHeightExp = 8;
        this.mHierarchyLrgExp = 3;
        this.mHierarchyMidExp = 2;
        this.mHierarchySmlExp = 0;
        this.mLumScale = 0.4f;
        this.mChromaScale = 1.35f;
        this.mMaskBg = 0.65f;
        this.mMaskFg = 0.95f;
        this.mExposureChange = 1.0f;
        this.mWhiteBalanceRedChange = 0.0f;
        this.mWhiteBalanceBlueChange = 0.0f;
        this.mAutoWBToggle = 0;
        this.mAdaptRateLearning = 0.2f;
        this.mAdaptRateBg = 0.0f;
        this.mAdaptRateFg = 0.0f;
        this.mVerifyRate = 0.25f;
        this.mLearningDoneListener = null;
        this.mUseTheForce = false;
        this.mProvideDebugOutputs = false;
        this.mMirrorBg = false;
        this.mOrientation = 0;
        this.startTime = -1L;
        this.mLogVerbose = Log.isLoggable(TAG, 2);
        String adjStr = SystemProperties.get("ro.media.effect.bgdropper.adj");
        if (adjStr.length() > 0) {
            try {
                this.mAcceptStddev += Float.parseFloat(adjStr);
                if (this.mLogVerbose) {
                    Log.v(TAG, "Adjusting accept threshold by " + adjStr + ", now " + this.mAcceptStddev);
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, "Badly formatted property ro.media.effect.bgdropper.adj: " + adjStr);
            }
        }
    }

    private protected synchronized void setupPorts() {
        String[] strArr;
        String[] strArr2;
        String[] strArr3;
        FrameFormat imageFormat = ImageFormat.create(3, 0);
        for (String inputName : mInputNames) {
            addMaskedInputPort(inputName, imageFormat);
        }
        for (String outputName : mOutputNames) {
            addOutputBasedOnInput(outputName, "video");
        }
        if (this.mProvideDebugOutputs) {
            for (String outputName2 : mDebugOutputNames) {
                addOutputBasedOnInput(outputName2, "video");
            }
        }
    }

    private protected synchronized FrameFormat getOutputFormat(String portName, FrameFormat inputFormat) {
        MutableFrameFormat format = inputFormat.mutableCopy();
        if (!Arrays.asList(mOutputNames).contains(portName)) {
            format.setDimensions(0, 0);
        }
        return format;
    }

    public protected synchronized boolean createMemoryFormat(FrameFormat inputFormat) {
        if (this.mMemoryFormat != null) {
            return false;
        }
        if (inputFormat.getWidth() == 0 || inputFormat.getHeight() == 0) {
            throw new RuntimeException("Attempting to process input frame with unknown size");
        }
        this.mMaskFormat = inputFormat.mutableCopy();
        int maskWidth = (int) Math.pow(2.0d, this.mMaskWidthExp);
        int maskHeight = (int) Math.pow(2.0d, this.mMaskHeightExp);
        this.mMaskFormat.setDimensions(maskWidth, maskHeight);
        this.mPyramidDepth = Math.max(this.mMaskWidthExp, this.mMaskHeightExp);
        this.mMemoryFormat = this.mMaskFormat.mutableCopy();
        int widthExp = Math.max(this.mMaskWidthExp, pyramidLevel(inputFormat.getWidth()));
        int heightExp = Math.max(this.mMaskHeightExp, pyramidLevel(inputFormat.getHeight()));
        this.mPyramidDepth = Math.max(widthExp, heightExp);
        int memWidth = Math.max(maskWidth, (int) Math.pow(2.0d, widthExp));
        int memHeight = Math.max(maskHeight, (int) Math.pow(2.0d, heightExp));
        this.mMemoryFormat.setDimensions(memWidth, memHeight);
        this.mSubsampleLevel = this.mPyramidDepth - Math.max(this.mMaskWidthExp, this.mMaskHeightExp);
        if (this.mLogVerbose) {
            Log.v(TAG, "Mask frames size " + maskWidth + " x " + maskHeight);
            Log.v(TAG, "Pyramid levels " + widthExp + " x " + heightExp);
            Log.v(TAG, "Memory frames size " + memWidth + " x " + memHeight);
        }
        this.mAverageFormat = inputFormat.mutableCopy();
        this.mAverageFormat.setDimensions(1, 1);
        return true;
    }

    private protected synchronized void prepare(FilterContext context) {
        if (this.mLogVerbose) {
            Log.v(TAG, "Preparing BackDropperFilter!");
        }
        this.mBgMean = new GLFrame[2];
        this.mBgVariance = new GLFrame[2];
        this.mMaskVerify = new GLFrame[2];
        this.copyShaderProgram = ShaderProgram.createIdentity(context);
    }

    public protected synchronized void allocateFrames(FrameFormat inputFormat, FilterContext context) {
        if (!createMemoryFormat(inputFormat)) {
            return;
        }
        if (this.mLogVerbose) {
            Log.v(TAG, "Allocating BackDropperFilter frames");
        }
        int numBytes = this.mMaskFormat.getSize();
        byte[] initialBgMean = new byte[numBytes];
        byte[] initialBgVariance = new byte[numBytes];
        byte[] initialMaskVerify = new byte[numBytes];
        for (int i = 0; i < numBytes; i++) {
            initialBgMean[i] = Byte.MIN_VALUE;
            initialBgVariance[i] = 10;
            initialMaskVerify[i] = 0;
        }
        for (int i2 = 0; i2 < 2; i2++) {
            this.mBgMean[i2] = (GLFrame) context.getFrameManager().newFrame(this.mMaskFormat);
            this.mBgMean[i2].setData(initialBgMean, 0, numBytes);
            this.mBgVariance[i2] = (GLFrame) context.getFrameManager().newFrame(this.mMaskFormat);
            this.mBgVariance[i2].setData(initialBgVariance, 0, numBytes);
            this.mMaskVerify[i2] = (GLFrame) context.getFrameManager().newFrame(this.mMaskFormat);
            this.mMaskVerify[i2].setData(initialMaskVerify, 0, numBytes);
        }
        if (this.mLogVerbose) {
            Log.v(TAG, "Done allocating texture for Mean and Variance objects!");
        }
        this.mDistance = (GLFrame) context.getFrameManager().newFrame(this.mMaskFormat);
        this.mMask = (GLFrame) context.getFrameManager().newFrame(this.mMaskFormat);
        this.mAutoWB = (GLFrame) context.getFrameManager().newFrame(this.mAverageFormat);
        this.mVideoInput = (GLFrame) context.getFrameManager().newFrame(this.mMemoryFormat);
        this.mBgInput = (GLFrame) context.getFrameManager().newFrame(this.mMemoryFormat);
        this.mMaskAverage = (GLFrame) context.getFrameManager().newFrame(this.mAverageFormat);
        this.mBgDistProgram = new ShaderProgram(context, mSharedUtilShader + mBgDistanceShader);
        this.mBgDistProgram.setHostValue("subsample_level", Float.valueOf((float) this.mSubsampleLevel));
        this.mBgMaskProgram = new ShaderProgram(context, mSharedUtilShader + mBgMaskShader);
        this.mBgMaskProgram.setHostValue("accept_variance", Float.valueOf(this.mAcceptStddev * this.mAcceptStddev));
        float[] yuvWeights = {this.mLumScale, this.mChromaScale};
        this.mBgMaskProgram.setHostValue("yuv_weights", yuvWeights);
        this.mBgMaskProgram.setHostValue("scale_lrg", Float.valueOf(this.mHierarchyLrgScale));
        this.mBgMaskProgram.setHostValue("scale_mid", Float.valueOf(this.mHierarchyMidScale));
        this.mBgMaskProgram.setHostValue("scale_sml", Float.valueOf(this.mHierarchySmlScale));
        this.mBgMaskProgram.setHostValue("exp_lrg", Float.valueOf(this.mSubsampleLevel + this.mHierarchyLrgExp));
        this.mBgMaskProgram.setHostValue("exp_mid", Float.valueOf(this.mSubsampleLevel + this.mHierarchyMidExp));
        this.mBgMaskProgram.setHostValue("exp_sml", Float.valueOf(this.mSubsampleLevel + this.mHierarchySmlExp));
        if (this.mUseTheForce) {
            this.mBgSubtractProgram = new ShaderProgram(context, mSharedUtilShader + mBgSubtractShader + mBgSubtractForceShader);
        } else {
            this.mBgSubtractProgram = new ShaderProgram(context, mSharedUtilShader + mBgSubtractShader + "}\n");
        }
        this.mBgSubtractProgram.setHostValue("bg_fit_transform", DEFAULT_BG_FIT_TRANSFORM);
        this.mBgSubtractProgram.setHostValue("mask_blend_bg", Float.valueOf(this.mMaskBg));
        this.mBgSubtractProgram.setHostValue("mask_blend_fg", Float.valueOf(this.mMaskFg));
        this.mBgSubtractProgram.setHostValue("exposure_change", Float.valueOf(this.mExposureChange));
        this.mBgSubtractProgram.setHostValue("whitebalanceblue_change", Float.valueOf(this.mWhiteBalanceBlueChange));
        this.mBgSubtractProgram.setHostValue("whitebalancered_change", Float.valueOf(this.mWhiteBalanceRedChange));
        this.mBgUpdateMeanProgram = new ShaderProgram(context, mSharedUtilShader + mUpdateBgModelMeanShader);
        this.mBgUpdateMeanProgram.setHostValue("subsample_level", Float.valueOf((float) this.mSubsampleLevel));
        this.mBgUpdateVarianceProgram = new ShaderProgram(context, mSharedUtilShader + mUpdateBgModelVarianceShader);
        this.mBgUpdateVarianceProgram.setHostValue("subsample_level", Float.valueOf((float) this.mSubsampleLevel));
        this.mCopyOutProgram = ShaderProgram.createIdentity(context);
        this.mAutomaticWhiteBalanceProgram = new ShaderProgram(context, mSharedUtilShader + mAutomaticWhiteBalance);
        this.mAutomaticWhiteBalanceProgram.setHostValue("pyramid_depth", Float.valueOf((float) this.mPyramidDepth));
        this.mAutomaticWhiteBalanceProgram.setHostValue("autowb_toggle", Integer.valueOf(this.mAutoWBToggle));
        this.mMaskVerifyProgram = new ShaderProgram(context, mSharedUtilShader + mMaskVerifyShader);
        this.mMaskVerifyProgram.setHostValue("verify_rate", Float.valueOf(this.mVerifyRate));
        if (this.mLogVerbose) {
            Log.v(TAG, "Shader width set to " + this.mMemoryFormat.getWidth());
        }
        this.mRelativeAspect = 1.0f;
        this.mFrameCount = 0;
        this.mStartLearning = true;
    }

    private protected synchronized void process(FilterContext context) {
        boolean z;
        int i;
        Frame video = pullInput("video");
        Frame background = pullInput("background");
        allocateFrames(video.getFormat(), context);
        if (this.mStartLearning) {
            if (this.mLogVerbose) {
                Log.v(TAG, "Starting learning");
            }
            this.mBgUpdateMeanProgram.setHostValue("bg_adapt_rate", Float.valueOf(this.mAdaptRateLearning));
            this.mBgUpdateMeanProgram.setHostValue("fg_adapt_rate", Float.valueOf(this.mAdaptRateLearning));
            this.mBgUpdateVarianceProgram.setHostValue("bg_adapt_rate", Float.valueOf(this.mAdaptRateLearning));
            this.mBgUpdateVarianceProgram.setHostValue("fg_adapt_rate", Float.valueOf(this.mAdaptRateLearning));
            this.mFrameCount = 0;
        }
        int inputIndex = !this.mPingPong ? 1 : 0;
        boolean z2 = this.mPingPong;
        this.mPingPong = !this.mPingPong;
        updateBgScaling(video, background, this.mBackgroundFitModeChanged);
        this.mBackgroundFitModeChanged = false;
        this.copyShaderProgram.process(video, this.mVideoInput);
        this.copyShaderProgram.process(background, this.mBgInput);
        this.mVideoInput.generateMipMap();
        this.mVideoInput.setTextureParameter(10241, 9985);
        this.mBgInput.generateMipMap();
        this.mBgInput.setTextureParameter(10241, 9985);
        if (this.mStartLearning) {
            this.copyShaderProgram.process(this.mVideoInput, this.mBgMean[inputIndex]);
            this.mStartLearning = false;
        }
        Frame[] distInputs = {this.mVideoInput, this.mBgMean[inputIndex], this.mBgVariance[inputIndex]};
        this.mBgDistProgram.process(distInputs, this.mDistance);
        this.mDistance.generateMipMap();
        this.mDistance.setTextureParameter(10241, 9985);
        this.mBgMaskProgram.process(this.mDistance, this.mMask);
        this.mMask.generateMipMap();
        this.mMask.setTextureParameter(10241, 9985);
        Frame[] autoWBInputs = {this.mVideoInput, this.mBgInput};
        this.mAutomaticWhiteBalanceProgram.process(autoWBInputs, this.mAutoWB);
        if (this.mFrameCount <= this.mLearningDuration) {
            pushOutput("video", video);
            if (this.mFrameCount == this.mLearningDuration - this.mLearningVerifyDuration) {
                ShaderProgram shaderProgram = this.copyShaderProgram;
                GLFrame gLFrame = this.mMask;
                GLFrame[] gLFrameArr = this.mMaskVerify;
                int outputIndex = z2 ? 1 : 0;
                shaderProgram.process(gLFrame, gLFrameArr[outputIndex]);
                this.mBgUpdateMeanProgram.setHostValue("bg_adapt_rate", Float.valueOf(this.mAdaptRateBg));
                this.mBgUpdateMeanProgram.setHostValue("fg_adapt_rate", Float.valueOf(this.mAdaptRateFg));
                this.mBgUpdateVarianceProgram.setHostValue("bg_adapt_rate", Float.valueOf(this.mAdaptRateBg));
                this.mBgUpdateVarianceProgram.setHostValue("fg_adapt_rate", Float.valueOf(this.mAdaptRateFg));
            } else if (this.mFrameCount > this.mLearningDuration - this.mLearningVerifyDuration) {
                Frame[] maskVerifyInputs = {this.mMaskVerify[inputIndex], this.mMask};
                ShaderProgram shaderProgram2 = this.mMaskVerifyProgram;
                GLFrame[] gLFrameArr2 = this.mMaskVerify;
                int outputIndex2 = z2 ? 1 : 0;
                shaderProgram2.process(maskVerifyInputs, gLFrameArr2[outputIndex2]);
                GLFrame[] gLFrameArr3 = this.mMaskVerify;
                int outputIndex3 = z2 ? 1 : 0;
                gLFrameArr3[outputIndex3].generateMipMap();
                GLFrame[] gLFrameArr4 = this.mMaskVerify;
                int outputIndex4 = z2 ? 1 : 0;
                gLFrameArr4[outputIndex4].setTextureParameter(10241, 9985);
            }
            if (this.mFrameCount == this.mLearningDuration) {
                ShaderProgram shaderProgram3 = this.copyShaderProgram;
                GLFrame[] gLFrameArr5 = this.mMaskVerify;
                int outputIndex5 = z2 ? 1 : 0;
                shaderProgram3.process(gLFrameArr5[outputIndex5], this.mMaskAverage);
                ByteBuffer mMaskAverageByteBuffer = this.mMaskAverage.getData();
                byte[] mask_average = mMaskAverageByteBuffer.array();
                int bi = mask_average[3] & 255;
                if (this.mLogVerbose) {
                    i = 20;
                    z = true;
                    Log.v(TAG, String.format("Mask_average is %d, threshold is %d", Integer.valueOf(bi), 20));
                } else {
                    z = true;
                    i = 20;
                }
                if (bi >= i) {
                    this.mStartLearning = z;
                } else {
                    if (this.mLogVerbose) {
                        Log.v(TAG, "Learning done");
                    }
                    if (this.mLearningDoneListener != null) {
                        this.mLearningDoneListener.onLearningDone(this);
                    }
                }
            }
        } else {
            Frame output = context.getFrameManager().newFrame(video.getFormat());
            Frame[] subtractInputs = {video, background, this.mMask, this.mAutoWB};
            this.mBgSubtractProgram.process(subtractInputs, output);
            pushOutput("video", output);
            output.release();
        }
        if (this.mFrameCount < this.mLearningDuration - this.mLearningVerifyDuration || this.mAdaptRateBg > FeatureOption.FO_BOOT_POLICY_CPU || this.mAdaptRateFg > FeatureOption.FO_BOOT_POLICY_CPU) {
            Frame[] meanUpdateInputs = {this.mVideoInput, this.mBgMean[inputIndex], this.mMask};
            ShaderProgram shaderProgram4 = this.mBgUpdateMeanProgram;
            GLFrame[] gLFrameArr6 = this.mBgMean;
            int outputIndex6 = z2 ? 1 : 0;
            shaderProgram4.process(meanUpdateInputs, gLFrameArr6[outputIndex6]);
            GLFrame[] gLFrameArr7 = this.mBgMean;
            int outputIndex7 = z2 ? 1 : 0;
            gLFrameArr7[outputIndex7].generateMipMap();
            GLFrame[] gLFrameArr8 = this.mBgMean;
            int outputIndex8 = z2 ? 1 : 0;
            gLFrameArr8[outputIndex8].setTextureParameter(10241, 9985);
            Frame[] varianceUpdateInputs = {this.mVideoInput, this.mBgMean[inputIndex], this.mBgVariance[inputIndex], this.mMask};
            ShaderProgram shaderProgram5 = this.mBgUpdateVarianceProgram;
            GLFrame[] gLFrameArr9 = this.mBgVariance;
            int outputIndex9 = z2 ? 1 : 0;
            shaderProgram5.process(varianceUpdateInputs, gLFrameArr9[outputIndex9]);
            GLFrame[] gLFrameArr10 = this.mBgVariance;
            int outputIndex10 = z2 ? 1 : 0;
            gLFrameArr10[outputIndex10].generateMipMap();
            GLFrame[] gLFrameArr11 = this.mBgVariance;
            int outputIndex11 = z2 ? 1 : 0;
            gLFrameArr11[outputIndex11].setTextureParameter(10241, 9985);
        }
        if (this.mProvideDebugOutputs) {
            Frame dbg1 = context.getFrameManager().newFrame(video.getFormat());
            this.mCopyOutProgram.process(video, dbg1);
            pushOutput("debug1", dbg1);
            dbg1.release();
            Frame dbg2 = context.getFrameManager().newFrame(this.mMemoryFormat);
            this.mCopyOutProgram.process(this.mMask, dbg2);
            pushOutput("debug2", dbg2);
            dbg2.release();
        }
        this.mFrameCount++;
        if (this.mLogVerbose && this.mFrameCount % 30 == 0) {
            if (this.startTime == -1) {
                context.getGLEnvironment().activate();
                GLES20.glFinish();
                this.startTime = SystemClock.elapsedRealtime();
                return;
            }
            context.getGLEnvironment().activate();
            GLES20.glFinish();
            long endTime = SystemClock.elapsedRealtime();
            Log.v(TAG, "Avg. frame duration: " + String.format("%.2f", Double.valueOf((endTime - this.startTime) / 30.0d)) + " ms. Avg. fps: " + String.format("%.2f", Double.valueOf(1000.0d / ((endTime - this.startTime) / 30.0d))));
            this.startTime = endTime;
        }
    }

    private protected synchronized void close(FilterContext context) {
        if (this.mMemoryFormat == null) {
            return;
        }
        if (this.mLogVerbose) {
            Log.v(TAG, "Filter Closing!");
        }
        for (int i = 0; i < 2; i++) {
            this.mBgMean[i].release();
            this.mBgVariance[i].release();
            this.mMaskVerify[i].release();
        }
        this.mDistance.release();
        this.mMask.release();
        this.mAutoWB.release();
        this.mVideoInput.release();
        this.mBgInput.release();
        this.mMaskAverage.release();
        this.mMemoryFormat = null;
    }

    private protected synchronized void relearn() {
        this.mStartLearning = true;
    }

    private protected synchronized void fieldPortValueUpdated(String name, FilterContext context) {
        if (name.equals("backgroundFitMode")) {
            this.mBackgroundFitModeChanged = true;
        } else if (name.equals("acceptStddev")) {
            this.mBgMaskProgram.setHostValue("accept_variance", Float.valueOf(this.mAcceptStddev * this.mAcceptStddev));
        } else if (name.equals("hierLrgScale")) {
            this.mBgMaskProgram.setHostValue("scale_lrg", Float.valueOf(this.mHierarchyLrgScale));
        } else if (name.equals("hierMidScale")) {
            this.mBgMaskProgram.setHostValue("scale_mid", Float.valueOf(this.mHierarchyMidScale));
        } else if (name.equals("hierSmlScale")) {
            this.mBgMaskProgram.setHostValue("scale_sml", Float.valueOf(this.mHierarchySmlScale));
        } else if (name.equals("hierLrgExp")) {
            this.mBgMaskProgram.setHostValue("exp_lrg", Float.valueOf(this.mSubsampleLevel + this.mHierarchyLrgExp));
        } else if (name.equals("hierMidExp")) {
            this.mBgMaskProgram.setHostValue("exp_mid", Float.valueOf(this.mSubsampleLevel + this.mHierarchyMidExp));
        } else if (name.equals("hierSmlExp")) {
            this.mBgMaskProgram.setHostValue("exp_sml", Float.valueOf(this.mSubsampleLevel + this.mHierarchySmlExp));
        } else if (name.equals("lumScale") || name.equals("chromaScale")) {
            float[] yuvWeights = {this.mLumScale, this.mChromaScale};
            this.mBgMaskProgram.setHostValue("yuv_weights", yuvWeights);
        } else if (name.equals("maskBg")) {
            this.mBgSubtractProgram.setHostValue("mask_blend_bg", Float.valueOf(this.mMaskBg));
        } else if (name.equals("maskFg")) {
            this.mBgSubtractProgram.setHostValue("mask_blend_fg", Float.valueOf(this.mMaskFg));
        } else if (name.equals("exposureChange")) {
            this.mBgSubtractProgram.setHostValue("exposure_change", Float.valueOf(this.mExposureChange));
        } else if (name.equals("whitebalanceredChange")) {
            this.mBgSubtractProgram.setHostValue("whitebalancered_change", Float.valueOf(this.mWhiteBalanceRedChange));
        } else if (name.equals("whitebalanceblueChange")) {
            this.mBgSubtractProgram.setHostValue("whitebalanceblue_change", Float.valueOf(this.mWhiteBalanceBlueChange));
        } else if (name.equals("autowbToggle")) {
            this.mAutomaticWhiteBalanceProgram.setHostValue("autowb_toggle", Integer.valueOf(this.mAutoWBToggle));
        }
    }

    public protected synchronized void updateBgScaling(Frame video, Frame background, boolean fitModeChanged) {
        float foregroundAspect = video.getFormat().getWidth() / video.getFormat().getHeight();
        float backgroundAspect = background.getFormat().getWidth() / background.getFormat().getHeight();
        float currentRelativeAspect = foregroundAspect / backgroundAspect;
        if (currentRelativeAspect != this.mRelativeAspect || fitModeChanged) {
            this.mRelativeAspect = currentRelativeAspect;
            float xMin = 0.0f;
            float xWidth = 1.0f;
            float yMin = 0.0f;
            float yWidth = 1.0f;
            switch (this.mBackgroundFitMode) {
                case 1:
                    if (this.mRelativeAspect > 1.0f) {
                        xMin = 0.5f - (this.mRelativeAspect * 0.5f);
                        xWidth = 1.0f * this.mRelativeAspect;
                        break;
                    } else {
                        yMin = 0.5f - (0.5f / this.mRelativeAspect);
                        yWidth = 1.0f / this.mRelativeAspect;
                        break;
                    }
                case 2:
                    if (this.mRelativeAspect > 1.0f) {
                        yMin = 0.5f - (0.5f / this.mRelativeAspect);
                        yWidth = 1.0f / this.mRelativeAspect;
                        break;
                    } else {
                        xMin = 0.5f - (this.mRelativeAspect * 0.5f);
                        xWidth = this.mRelativeAspect;
                        break;
                    }
            }
            if (this.mMirrorBg) {
                if (this.mLogVerbose) {
                    Log.v(TAG, "Mirroring the background!");
                }
                if (this.mOrientation == 0 || this.mOrientation == 180) {
                    xWidth = -xWidth;
                    xMin = 1.0f - xMin;
                } else {
                    yWidth = -yWidth;
                    yMin = 1.0f - yMin;
                }
            }
            if (this.mLogVerbose) {
                Log.v(TAG, "bgTransform: xMin, yMin, xWidth, yWidth : " + xMin + ", " + yMin + ", " + xWidth + ", " + yWidth + ", mRelAspRatio = " + this.mRelativeAspect);
            }
            float[] bgTransform = {xWidth, 0.0f, 0.0f, 0.0f, yWidth, 0.0f, xMin, yMin, 1.0f};
            this.mBgSubtractProgram.setHostValue("bg_fit_transform", bgTransform);
        }
    }

    public protected synchronized int pyramidLevel(int size) {
        return ((int) Math.floor(Math.log10(size) / Math.log10(2.0d))) - 1;
    }
}
