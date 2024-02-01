package android.filterpacks.videosrc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.GLFrame;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.graphics.SurfaceTexture;
import android.opengl.Matrix;
import android.os.ConditionVariable;
import android.util.Log;
/* loaded from: classes.dex */
public class SurfaceTextureSource extends Filter {
    @GenerateFieldPort(hasDefault = true, name = "closeOnTimeout")
    public protected boolean mCloseOnTimeout;
    public protected boolean mFirstFrame;
    public protected ShaderProgram mFrameExtractor;
    public protected float[] mFrameTransform;
    @GenerateFieldPort(name = "height")
    public protected int mHeight;
    public protected float[] mMappedCoords;
    public protected GLFrame mMediaFrame;
    public protected ConditionVariable mNewFrameAvailable;
    public protected MutableFrameFormat mOutputFormat;
    public protected final String mRenderShader;
    @GenerateFinalPort(name = "sourceListener")
    public protected SurfaceTextureSourceListener mSourceListener;
    public protected SurfaceTexture mSurfaceTexture;
    @GenerateFieldPort(hasDefault = true, name = "waitForNewFrame")
    public protected boolean mWaitForNewFrame;
    @GenerateFieldPort(hasDefault = true, name = "waitTimeout")
    public protected int mWaitTimeout;
    @GenerateFieldPort(name = "width")
    public protected int mWidth;
    public protected SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener;
    public protected static final float[] mSourceCoords = {0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f};
    public protected static final String TAG = "SurfaceTextureSource";
    public protected static final boolean mLogVerbose = Log.isLoggable(TAG, 2);

    /* loaded from: classes.dex */
    public interface SurfaceTextureSourceListener {
        private protected synchronized void onSurfaceTextureSourceReady(SurfaceTexture surfaceTexture);
    }

    private protected synchronized SurfaceTextureSource(String name) {
        super(name);
        this.mWaitForNewFrame = true;
        this.mWaitTimeout = 1000;
        this.mCloseOnTimeout = false;
        this.mRenderShader = CameraSource.mFrameShader;
        this.onFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener() { // from class: android.filterpacks.videosrc.SurfaceTextureSource.1
            @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                if (SurfaceTextureSource.mLogVerbose) {
                    Log.v(SurfaceTextureSource.TAG, "New frame from SurfaceTexture");
                }
                SurfaceTextureSource.this.mNewFrameAvailable.open();
            }
        };
        this.mNewFrameAvailable = new ConditionVariable();
        this.mFrameTransform = new float[16];
        this.mMappedCoords = new float[16];
    }

    private protected synchronized void setupPorts() {
        addOutputPort("video", ImageFormat.create(3, 3));
    }

    public protected synchronized void createFormats() {
        this.mOutputFormat = ImageFormat.create(this.mWidth, this.mHeight, 3, 3);
    }

    public private synchronized void prepare(FilterContext context) {
        if (mLogVerbose) {
            Log.v(TAG, "Preparing SurfaceTextureSource");
        }
        createFormats();
        this.mMediaFrame = (GLFrame) context.getFrameManager().newBoundFrame(this.mOutputFormat, 104, 0L);
        this.mFrameExtractor = new ShaderProgram(context, CameraSource.mFrameShader);
    }

    private protected synchronized void open(FilterContext context) {
        if (mLogVerbose) {
            Log.v(TAG, "Opening SurfaceTextureSource");
        }
        this.mSurfaceTexture = new SurfaceTexture(this.mMediaFrame.getTextureId());
        this.mSurfaceTexture.setOnFrameAvailableListener(this.onFrameAvailableListener);
        this.mSourceListener.onSurfaceTextureSourceReady(this.mSurfaceTexture);
        this.mFirstFrame = true;
    }

    private protected synchronized void process(FilterContext context) {
        if (mLogVerbose) {
            Log.v(TAG, "Processing new frame");
        }
        if (this.mWaitForNewFrame || this.mFirstFrame) {
            if (this.mWaitTimeout != 0) {
                boolean gotNewFrame = this.mNewFrameAvailable.block(this.mWaitTimeout);
                if (!gotNewFrame) {
                    if (!this.mCloseOnTimeout) {
                        throw new RuntimeException("Timeout waiting for new frame");
                    }
                    if (mLogVerbose) {
                        Log.v(TAG, "Timeout waiting for a new frame. Closing.");
                    }
                    closeOutputPort("video");
                    return;
                }
            } else {
                this.mNewFrameAvailable.block();
            }
            this.mNewFrameAvailable.close();
            this.mFirstFrame = false;
        }
        this.mSurfaceTexture.updateTexImage();
        this.mSurfaceTexture.getTransformMatrix(this.mFrameTransform);
        Matrix.multiplyMM(this.mMappedCoords, 0, this.mFrameTransform, 0, mSourceCoords, 0);
        this.mFrameExtractor.setSourceRegion(this.mMappedCoords[0], this.mMappedCoords[1], this.mMappedCoords[4], this.mMappedCoords[5], this.mMappedCoords[8], this.mMappedCoords[9], this.mMappedCoords[12], this.mMappedCoords[13]);
        Frame output = context.getFrameManager().newFrame(this.mOutputFormat);
        this.mFrameExtractor.process(this.mMediaFrame, output);
        output.setTimestamp(this.mSurfaceTexture.getTimestamp());
        pushOutput("video", output);
        output.release();
    }

    private protected synchronized void close(FilterContext context) {
        if (mLogVerbose) {
            Log.v(TAG, "SurfaceTextureSource closed");
        }
        this.mSourceListener.onSurfaceTextureSourceReady(null);
        this.mSurfaceTexture.release();
        this.mSurfaceTexture = null;
    }

    private protected synchronized void tearDown(FilterContext context) {
        if (this.mMediaFrame != null) {
            this.mMediaFrame.release();
        }
    }

    private protected synchronized void fieldPortValueUpdated(String name, FilterContext context) {
        if (name.equals("width") || name.equals("height")) {
            this.mOutputFormat.setDimensions(this.mWidth, this.mHeight);
        }
    }
}
