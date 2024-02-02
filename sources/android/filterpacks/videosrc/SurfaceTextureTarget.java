package android.filterpacks.videosrc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.GLEnvironment;
import android.filterfw.core.GLFrame;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.filterfw.geometry.Point;
import android.filterfw.geometry.Quad;
import android.graphics.SurfaceTexture;
import android.util.Log;
/* loaded from: classes.dex */
public class SurfaceTextureTarget extends Filter {
    public protected static final String TAG = "SurfaceTextureTarget";
    public protected final int RENDERMODE_CUSTOMIZE;
    public protected final int RENDERMODE_FILL_CROP;
    public protected final int RENDERMODE_FIT;
    public protected final int RENDERMODE_STRETCH;
    public protected float mAspectRatio;
    public protected boolean mLogVerbose;
    public protected ShaderProgram mProgram;
    public protected int mRenderMode;
    @GenerateFieldPort(hasDefault = true, name = "renderMode")
    public protected String mRenderModeString;
    public protected GLFrame mScreen;
    @GenerateFinalPort(name = "height")
    public protected int mScreenHeight;
    @GenerateFinalPort(name = "width")
    public protected int mScreenWidth;
    @GenerateFieldPort(hasDefault = true, name = "sourceQuad")
    public protected Quad mSourceQuad;
    public protected int mSurfaceId;
    @GenerateFinalPort(name = "surfaceTexture")
    public protected SurfaceTexture mSurfaceTexture;
    @GenerateFieldPort(hasDefault = true, name = "targetQuad")
    public protected Quad mTargetQuad;

    private protected synchronized SurfaceTextureTarget(String name) {
        super(name);
        this.RENDERMODE_STRETCH = 0;
        this.RENDERMODE_FIT = 1;
        this.RENDERMODE_FILL_CROP = 2;
        this.RENDERMODE_CUSTOMIZE = 3;
        this.mSourceQuad = new Quad(new Point(0.0f, 1.0f), new Point(1.0f, 1.0f), new Point(0.0f, 0.0f), new Point(1.0f, 0.0f));
        this.mTargetQuad = new Quad(new Point(0.0f, 0.0f), new Point(1.0f, 0.0f), new Point(0.0f, 1.0f), new Point(1.0f, 1.0f));
        this.mRenderMode = 1;
        this.mAspectRatio = 1.0f;
        this.mLogVerbose = Log.isLoggable(TAG, 2);
    }

    private protected synchronized void setupPorts() {
        if (this.mSurfaceTexture == null) {
            throw new RuntimeException("Null SurfaceTexture passed to SurfaceTextureTarget");
        }
        addMaskedInputPort("frame", ImageFormat.create(3));
    }

    private protected synchronized void updateRenderMode() {
        if (this.mLogVerbose) {
            Log.v(TAG, "updateRenderMode. Thread: " + Thread.currentThread());
        }
        if (this.mRenderModeString != null) {
            if (this.mRenderModeString.equals("stretch")) {
                this.mRenderMode = 0;
            } else if (this.mRenderModeString.equals("fit")) {
                this.mRenderMode = 1;
            } else if (this.mRenderModeString.equals("fill_crop")) {
                this.mRenderMode = 2;
            } else if (this.mRenderModeString.equals("customize")) {
                this.mRenderMode = 3;
            } else {
                throw new RuntimeException("Unknown render mode '" + this.mRenderModeString + "'!");
            }
        }
        updateTargetRect();
    }

    private protected synchronized void prepare(FilterContext context) {
        if (this.mLogVerbose) {
            Log.v(TAG, "Prepare. Thread: " + Thread.currentThread());
        }
        this.mProgram = ShaderProgram.createIdentity(context);
        this.mProgram.setSourceRect(0.0f, 1.0f, 1.0f, -1.0f);
        this.mProgram.setClearColor(0.0f, 0.0f, 0.0f);
        updateRenderMode();
        MutableFrameFormat screenFormat = new MutableFrameFormat(2, 3);
        screenFormat.setBytesPerSample(4);
        screenFormat.setDimensions(this.mScreenWidth, this.mScreenHeight);
        this.mScreen = (GLFrame) context.getFrameManager().newBoundFrame(screenFormat, 101, 0L);
    }

    private protected synchronized void open(FilterContext context) {
        if (this.mSurfaceTexture == null) {
            Log.e(TAG, "SurfaceTexture is null!!");
            throw new RuntimeException("Could not register SurfaceTexture: " + this.mSurfaceTexture);
        }
        this.mSurfaceId = context.getGLEnvironment().registerSurfaceTexture(this.mSurfaceTexture, this.mScreenWidth, this.mScreenHeight);
        if (this.mSurfaceId <= 0) {
            throw new RuntimeException("Could not register SurfaceTexture: " + this.mSurfaceTexture);
        }
    }

    private protected synchronized void close(FilterContext context) {
        if (this.mSurfaceId > 0) {
            context.getGLEnvironment().unregisterSurfaceId(this.mSurfaceId);
            this.mSurfaceId = -1;
        }
    }

    private protected synchronized void disconnect(FilterContext context) {
        if (this.mLogVerbose) {
            Log.v(TAG, "disconnect");
        }
        if (this.mSurfaceTexture == null) {
            Log.d(TAG, "SurfaceTexture is already null. Nothing to disconnect.");
            return;
        }
        this.mSurfaceTexture = null;
        if (this.mSurfaceId > 0) {
            context.getGLEnvironment().unregisterSurfaceId(this.mSurfaceId);
            this.mSurfaceId = -1;
        }
    }

    private protected synchronized void process(FilterContext context) {
        Frame gpuFrame;
        if (this.mSurfaceId <= 0) {
            return;
        }
        GLEnvironment glEnv = context.getGLEnvironment();
        Frame input = pullInput("frame");
        boolean createdFrame = false;
        float currentAspectRatio = input.getFormat().getWidth() / input.getFormat().getHeight();
        if (currentAspectRatio != this.mAspectRatio) {
            if (this.mLogVerbose) {
                Log.v(TAG, "Process. New aspect ratio: " + currentAspectRatio + ", previously: " + this.mAspectRatio + ". Thread: " + Thread.currentThread());
            }
            this.mAspectRatio = currentAspectRatio;
            updateTargetRect();
        }
        int target = input.getFormat().getTarget();
        if (target != 3) {
            gpuFrame = context.getFrameManager().duplicateFrameToTarget(input, 3);
            createdFrame = true;
        } else {
            gpuFrame = input;
        }
        glEnv.activateSurfaceWithId(this.mSurfaceId);
        this.mProgram.process(gpuFrame, this.mScreen);
        glEnv.setSurfaceTimestamp(input.getTimestamp());
        glEnv.swapBuffers();
        if (createdFrame) {
            gpuFrame.release();
        }
    }

    private protected synchronized void fieldPortValueUpdated(String name, FilterContext context) {
        if (this.mLogVerbose) {
            Log.v(TAG, "FPVU. Thread: " + Thread.currentThread());
        }
        updateRenderMode();
    }

    private protected synchronized void tearDown(FilterContext context) {
        if (this.mScreen != null) {
            this.mScreen.release();
        }
    }

    public protected synchronized void updateTargetRect() {
        if (this.mLogVerbose) {
            Log.v(TAG, "updateTargetRect. Thread: " + Thread.currentThread());
        }
        if (this.mScreenWidth > 0 && this.mScreenHeight > 0 && this.mProgram != null) {
            float screenAspectRatio = this.mScreenWidth / this.mScreenHeight;
            float relativeAspectRatio = screenAspectRatio / this.mAspectRatio;
            if (this.mLogVerbose) {
                StringBuilder sb = new StringBuilder();
                sb.append("UTR. screen w = ");
                sb.append(this.mScreenWidth);
                sb.append(" x screen h = ");
                sb.append(this.mScreenHeight);
                sb.append(" Screen AR: ");
                sb.append(screenAspectRatio);
                sb.append(", frame AR: ");
                sb.append(this.mAspectRatio);
                sb.append(", relative AR: ");
                sb.append(relativeAspectRatio);
                Log.v(TAG, sb.toString());
            }
            if (relativeAspectRatio == 1.0f && this.mRenderMode != 3) {
                this.mProgram.setTargetRect(0.0f, 0.0f, 1.0f, 1.0f);
                this.mProgram.setClearsOutput(false);
                return;
            }
            switch (this.mRenderMode) {
                case 0:
                    this.mTargetQuad.p0.set(0.0f, 0.0f);
                    this.mTargetQuad.p1.set(1.0f, 0.0f);
                    this.mTargetQuad.p2.set(0.0f, 1.0f);
                    this.mTargetQuad.p3.set(1.0f, 1.0f);
                    this.mProgram.setClearsOutput(false);
                    break;
                case 1:
                    if (relativeAspectRatio > 1.0f) {
                        this.mTargetQuad.p0.set(0.5f - (0.5f / relativeAspectRatio), 0.0f);
                        this.mTargetQuad.p1.set((0.5f / relativeAspectRatio) + 0.5f, 0.0f);
                        this.mTargetQuad.p2.set(0.5f - (0.5f / relativeAspectRatio), 1.0f);
                        this.mTargetQuad.p3.set(0.5f + (0.5f / relativeAspectRatio), 1.0f);
                    } else {
                        this.mTargetQuad.p0.set(0.0f, 0.5f - (0.5f * relativeAspectRatio));
                        this.mTargetQuad.p1.set(1.0f, 0.5f - (0.5f * relativeAspectRatio));
                        this.mTargetQuad.p2.set(0.0f, (0.5f * relativeAspectRatio) + 0.5f);
                        this.mTargetQuad.p3.set(1.0f, 0.5f + (0.5f * relativeAspectRatio));
                    }
                    this.mProgram.setClearsOutput(true);
                    break;
                case 2:
                    if (relativeAspectRatio > 1.0f) {
                        this.mTargetQuad.p0.set(0.0f, 0.5f - (0.5f * relativeAspectRatio));
                        this.mTargetQuad.p1.set(1.0f, 0.5f - (0.5f * relativeAspectRatio));
                        this.mTargetQuad.p2.set(0.0f, (0.5f * relativeAspectRatio) + 0.5f);
                        this.mTargetQuad.p3.set(1.0f, 0.5f + (0.5f * relativeAspectRatio));
                    } else {
                        this.mTargetQuad.p0.set(0.5f - (0.5f / relativeAspectRatio), 0.0f);
                        this.mTargetQuad.p1.set((0.5f / relativeAspectRatio) + 0.5f, 0.0f);
                        this.mTargetQuad.p2.set(0.5f - (0.5f / relativeAspectRatio), 1.0f);
                        this.mTargetQuad.p3.set(0.5f + (0.5f / relativeAspectRatio), 1.0f);
                    }
                    this.mProgram.setClearsOutput(true);
                    break;
                case 3:
                    this.mProgram.setSourceRegion(this.mSourceQuad);
                    break;
            }
            if (this.mLogVerbose) {
                Log.v(TAG, "UTR. quad: " + this.mTargetQuad);
            }
            this.mProgram.setTargetRegion(this.mTargetQuad);
        }
    }
}
