package android.view.animation;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import com.android.internal.R;
import java.util.Random;

/* loaded from: classes3.dex */
public class GridLayoutAnimationController extends LayoutAnimationController {
    public static final int DIRECTION_BOTTOM_TO_TOP = 2;
    public static final int DIRECTION_HORIZONTAL_MASK = 1;
    public static final int DIRECTION_LEFT_TO_RIGHT = 0;
    public static final int DIRECTION_RIGHT_TO_LEFT = 1;
    public static final int DIRECTION_TOP_TO_BOTTOM = 0;
    public static final int DIRECTION_VERTICAL_MASK = 2;
    public static final int PRIORITY_COLUMN = 1;
    public static final int PRIORITY_NONE = 0;
    public static final int PRIORITY_ROW = 2;
    private float mColumnDelay;
    private int mDirection;
    private int mDirectionPriority;
    private float mRowDelay;

    /* loaded from: classes3.dex */
    public static class AnimationParameters extends LayoutAnimationController.AnimationParameters {
        public int column;
        public int columnsCount;
        public int row;
        public int rowsCount;
    }

    public GridLayoutAnimationController(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GridLayoutAnimation);
        Animation.Description d = Animation.Description.parseValue(a.peekValue(0));
        this.mColumnDelay = d.value;
        Animation.Description d2 = Animation.Description.parseValue(a.peekValue(1));
        this.mRowDelay = d2.value;
        this.mDirection = a.getInt(2, 0);
        this.mDirectionPriority = a.getInt(3, 0);
        a.recycle();
    }

    public GridLayoutAnimationController(Animation animation) {
        this(animation, 0.5f, 0.5f);
    }

    public GridLayoutAnimationController(Animation animation, float columnDelay, float rowDelay) {
        super(animation);
        this.mColumnDelay = columnDelay;
        this.mRowDelay = rowDelay;
    }

    public float getColumnDelay() {
        return this.mColumnDelay;
    }

    public void setColumnDelay(float columnDelay) {
        this.mColumnDelay = columnDelay;
    }

    public float getRowDelay() {
        return this.mRowDelay;
    }

    public void setRowDelay(float rowDelay) {
        this.mRowDelay = rowDelay;
    }

    public int getDirection() {
        return this.mDirection;
    }

    public void setDirection(int direction) {
        this.mDirection = direction;
    }

    public int getDirectionPriority() {
        return this.mDirectionPriority;
    }

    public void setDirectionPriority(int directionPriority) {
        this.mDirectionPriority = directionPriority;
    }

    @Override // android.view.animation.LayoutAnimationController
    public boolean willOverlap() {
        return this.mColumnDelay < 1.0f || this.mRowDelay < 1.0f;
    }

    @Override // android.view.animation.LayoutAnimationController
    protected long getDelayForView(View view) {
        long viewDelay;
        float totalDelay;
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        AnimationParameters params = (AnimationParameters) lp.layoutAnimationParameters;
        if (params == null) {
            return 0L;
        }
        int column = getTransformedColumnIndex(params);
        int row = getTransformedRowIndex(params);
        int rowsCount = params.rowsCount;
        int columnsCount = params.columnsCount;
        long duration = this.mAnimation.getDuration();
        float columnDelay = this.mColumnDelay * ((float) duration);
        float rowDelay = this.mRowDelay * ((float) duration);
        if (this.mInterpolator == null) {
            this.mInterpolator = new LinearInterpolator();
        }
        int i = this.mDirectionPriority;
        if (i == 1) {
            viewDelay = (row * rowDelay) + (column * rowsCount * rowDelay);
            totalDelay = (rowsCount * rowDelay) + (columnsCount * rowsCount * rowDelay);
        } else if (i == 2) {
            viewDelay = (column * columnDelay) + (row * columnsCount * columnDelay);
            totalDelay = (columnsCount * columnDelay) + (rowsCount * columnsCount * columnDelay);
        } else {
            viewDelay = (column * columnDelay) + (row * rowDelay);
            totalDelay = (columnsCount * columnDelay) + (rowsCount * rowDelay);
        }
        float normalizedDelay = ((float) viewDelay) / totalDelay;
        return this.mInterpolator.getInterpolation(normalizedDelay) * totalDelay;
    }

    private int getTransformedColumnIndex(AnimationParameters params) {
        int index;
        int order = getOrder();
        if (order == 1) {
            int index2 = params.columnsCount;
            index = (index2 - 1) - params.column;
        } else if (order == 2) {
            if (this.mRandomizer == null) {
                this.mRandomizer = new Random();
            }
            index = (int) (params.columnsCount * this.mRandomizer.nextFloat());
        } else {
            index = params.column;
        }
        int direction = this.mDirection & 1;
        if (direction == 1) {
            return (params.columnsCount - 1) - index;
        }
        return index;
    }

    private int getTransformedRowIndex(AnimationParameters params) {
        int index;
        int order = getOrder();
        if (order == 1) {
            int index2 = params.rowsCount;
            index = (index2 - 1) - params.row;
        } else if (order == 2) {
            if (this.mRandomizer == null) {
                this.mRandomizer = new Random();
            }
            index = (int) (params.rowsCount * this.mRandomizer.nextFloat());
        } else {
            index = params.row;
        }
        int direction = this.mDirection & 2;
        if (direction == 2) {
            return (params.rowsCount - 1) - index;
        }
        return index;
    }
}
