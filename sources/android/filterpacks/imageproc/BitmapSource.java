package android.filterpacks.imageproc;

import android.app.slice.SliceItem;
import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.format.ImageFormat;
import android.graphics.Bitmap;
/* loaded from: classes.dex */
public class BitmapSource extends Filter {
    @GenerateFieldPort(name = "bitmap")
    public protected Bitmap mBitmap;
    public protected Frame mImageFrame;
    @GenerateFieldPort(hasDefault = true, name = "recycleBitmap")
    public protected boolean mRecycleBitmap;
    @GenerateFieldPort(hasDefault = true, name = "repeatFrame")
    public private protected boolean mRepeatFrame;
    public protected int mTarget;
    @GenerateFieldPort(name = "target")
    public private protected String mTargetString;

    private protected synchronized BitmapSource(String name) {
        super(name);
        this.mRecycleBitmap = true;
        this.mRepeatFrame = false;
    }

    private protected synchronized void setupPorts() {
        FrameFormat outputFormat = ImageFormat.create(3, 0);
        addOutputPort(SliceItem.FORMAT_IMAGE, outputFormat);
    }

    private protected synchronized void loadImage(FilterContext filterContext) {
        this.mTarget = FrameFormat.readTargetString(this.mTargetString);
        FrameFormat outputFormat = ImageFormat.create(this.mBitmap.getWidth(), this.mBitmap.getHeight(), 3, this.mTarget);
        this.mImageFrame = filterContext.getFrameManager().newFrame(outputFormat);
        this.mImageFrame.setBitmap(this.mBitmap);
        this.mImageFrame.setTimestamp(-1L);
        if (this.mRecycleBitmap) {
            this.mBitmap.recycle();
        }
        this.mBitmap = null;
    }

    private protected synchronized void fieldPortValueUpdated(String name, FilterContext context) {
        if ((name.equals("bitmap") || name.equals("target")) && this.mImageFrame != null) {
            this.mImageFrame.release();
            this.mImageFrame = null;
        }
    }

    private protected synchronized void process(FilterContext context) {
        if (this.mImageFrame == null) {
            loadImage(context);
        }
        pushOutput(SliceItem.FORMAT_IMAGE, this.mImageFrame);
        if (!this.mRepeatFrame) {
            closeOutputPort(SliceItem.FORMAT_IMAGE);
        }
    }

    private protected synchronized void tearDown(FilterContext env) {
        if (this.mImageFrame != null) {
            this.mImageFrame.release();
            this.mImageFrame = null;
        }
    }
}
