package com.android.internal.policy;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Size;
import android.view.Gravity;
import com.android.internal.R;
import com.xiaopeng.util.FeatureOption;
import java.io.PrintWriter;
import java.util.ArrayList;
/* loaded from: classes3.dex */
public class PipSnapAlgorithm {
    private static final float CORNER_MAGNET_THRESHOLD = 0.3f;
    private static final int SNAP_MODE_CORNERS_AND_SIDES = 1;
    private static final int SNAP_MODE_CORNERS_ONLY = 0;
    private static final int SNAP_MODE_EDGE = 2;
    private static final int SNAP_MODE_EDGE_MAGNET_CORNERS = 3;
    private static final int SNAP_MODE_LONG_EDGE_MAGNET_CORNERS = 4;
    private final Context mContext;
    private final float mDefaultSizePercent;
    private final int mFlingDeceleration;
    private boolean mIsMinimized;
    private final float mMaxAspectRatioForMinSize;
    private final float mMinAspectRatioForMinSize;
    private final int mMinimizedVisibleSize;
    private final ArrayList<Integer> mSnapGravities = new ArrayList<>();
    private final int mDefaultSnapMode = 3;
    private int mSnapMode = 3;
    private int mOrientation = 0;

    public PipSnapAlgorithm(Context context) {
        Resources res = context.getResources();
        this.mContext = context;
        this.mMinimizedVisibleSize = res.getDimensionPixelSize(R.dimen.pip_minimized_visible_size);
        this.mDefaultSizePercent = res.getFloat(R.dimen.config_pictureInPictureDefaultSizePercent);
        this.mMaxAspectRatioForMinSize = res.getFloat(R.dimen.config_pictureInPictureAspectRatioLimitForMinSize);
        this.mMinAspectRatioForMinSize = 1.0f / this.mMaxAspectRatioForMinSize;
        this.mFlingDeceleration = this.mContext.getResources().getDimensionPixelSize(R.dimen.pip_fling_deceleration);
        onConfigurationChanged();
    }

    public void onConfigurationChanged() {
        Resources res = this.mContext.getResources();
        this.mOrientation = res.getConfiguration().orientation;
        this.mSnapMode = res.getInteger(R.integer.config_pictureInPictureSnapMode);
        calculateSnapTargets();
    }

    public void setMinimized(boolean isMinimized) {
        this.mIsMinimized = isMinimized;
    }

    public Rect findClosestSnapBounds(Rect movementBounds, Rect stackBounds, float velocityX, float velocityY, Point dragStartPosition) {
        Rect intersectStackBounds = new Rect(stackBounds);
        Point intersect = getEdgeIntersect(stackBounds, movementBounds, velocityX, velocityY, dragStartPosition);
        intersectStackBounds.offsetTo(intersect.x, intersect.y);
        return findClosestSnapBounds(movementBounds, intersectStackBounds);
    }

    public Point getEdgeIntersect(Rect stackBounds, Rect movementBounds, float velX, float velY, Point dragStartPosition) {
        int maxDistance;
        int i;
        boolean isLandscape = this.mOrientation == 2;
        int x = stackBounds.left;
        int y = stackBounds.top;
        float slope = velY / velX;
        float yIntercept = y - (x * slope);
        Point vertPoint = new Point();
        Point horizPoint = new Point();
        vertPoint.x = velX > 0.0f ? movementBounds.right : movementBounds.left;
        vertPoint.y = findY(slope, yIntercept, vertPoint.x);
        horizPoint.y = velY > 0.0f ? movementBounds.bottom : movementBounds.top;
        horizPoint.x = findX(slope, yIntercept, horizPoint.y);
        if (isLandscape) {
            if (velX > 0.0f) {
                i = movementBounds.right - stackBounds.left;
            } else {
                i = stackBounds.left - movementBounds.left;
            }
            maxDistance = i;
        } else {
            int maxDistance2 = (velY > 0.0f ? 1 : (velY == 0.0f ? 0 : -1));
            if (maxDistance2 > 0) {
                maxDistance = movementBounds.bottom - stackBounds.top;
            } else {
                maxDistance = stackBounds.top - movementBounds.top;
            }
        }
        if (maxDistance > 0) {
            int startPoint = isLandscape ? dragStartPosition.y : dragStartPosition.x;
            int endPoint = isLandscape ? horizPoint.y : horizPoint.x;
            int center = movementBounds.centerX();
            if ((startPoint < center && endPoint < center) || (startPoint > center && endPoint > center)) {
                int distance = Math.min(((int) (FeatureOption.FO_BOOT_POLICY_CPU - Math.pow(isLandscape ? velX : velY, 2.0d))) / (this.mFlingDeceleration * 2), maxDistance);
                if (isLandscape) {
                    horizPoint.x = stackBounds.left + (velX > 0.0f ? distance : -distance);
                } else {
                    horizPoint.y = stackBounds.top + (velY > 0.0f ? distance : -distance);
                }
                return horizPoint;
            }
        }
        double distanceVert = Math.hypot(vertPoint.x - x, vertPoint.y - y);
        double distanceHoriz = Math.hypot(horizPoint.x - x, horizPoint.y - y);
        if (distanceVert == FeatureOption.FO_BOOT_POLICY_CPU) {
            return horizPoint;
        }
        if (distanceHoriz == FeatureOption.FO_BOOT_POLICY_CPU) {
            return vertPoint;
        }
        return Math.abs(distanceVert) > Math.abs(distanceHoriz) ? horizPoint : vertPoint;
    }

    private int findY(float slope, float yIntercept, float x) {
        return (int) ((slope * x) + yIntercept);
    }

    private int findX(float slope, float yIntercept, float y) {
        return (int) ((y - yIntercept) / slope);
    }

    public Rect findClosestSnapBounds(Rect movementBounds, Rect stackBounds) {
        Rect pipBounds = new Rect(movementBounds.left, movementBounds.top, movementBounds.right + stackBounds.width(), movementBounds.bottom + stackBounds.height());
        Rect newBounds = new Rect(stackBounds);
        int i = 0;
        if (this.mSnapMode == 4 || this.mSnapMode == 3) {
            Rect tmpBounds = new Rect();
            Point[] snapTargets = new Point[this.mSnapGravities.size()];
            while (i < this.mSnapGravities.size()) {
                Gravity.apply(this.mSnapGravities.get(i).intValue(), stackBounds.width(), stackBounds.height(), pipBounds, 0, 0, tmpBounds);
                snapTargets[i] = new Point(tmpBounds.left, tmpBounds.top);
                i++;
            }
            Point snapTarget = findClosestPoint(stackBounds.left, stackBounds.top, snapTargets);
            float distance = distanceToPoint(snapTarget, stackBounds.left, stackBounds.top);
            float thresh = Math.max(stackBounds.width(), stackBounds.height()) * CORNER_MAGNET_THRESHOLD;
            if (distance < thresh) {
                newBounds.offsetTo(snapTarget.x, snapTarget.y);
            } else {
                snapRectToClosestEdge(stackBounds, movementBounds, newBounds);
            }
        } else if (this.mSnapMode == 2) {
            snapRectToClosestEdge(stackBounds, movementBounds, newBounds);
        } else {
            Rect tmpBounds2 = new Rect();
            Point[] snapTargets2 = new Point[this.mSnapGravities.size()];
            while (true) {
                int i2 = i;
                if (i2 >= this.mSnapGravities.size()) {
                    break;
                }
                Gravity.apply(this.mSnapGravities.get(i2).intValue(), stackBounds.width(), stackBounds.height(), pipBounds, 0, 0, tmpBounds2);
                snapTargets2[i2] = new Point(tmpBounds2.left, tmpBounds2.top);
                i = i2 + 1;
            }
            Point snapTarget2 = findClosestPoint(stackBounds.left, stackBounds.top, snapTargets2);
            newBounds.offsetTo(snapTarget2.x, snapTarget2.y);
        }
        return newBounds;
    }

    public void applyMinimizedOffset(Rect stackBounds, Rect movementBounds, Point displaySize, Rect stableInsets) {
        if (stackBounds.left <= movementBounds.centerX()) {
            stackBounds.offsetTo((stableInsets.left + this.mMinimizedVisibleSize) - stackBounds.width(), stackBounds.top);
        } else {
            stackBounds.offsetTo((displaySize.x - stableInsets.right) - this.mMinimizedVisibleSize, stackBounds.top);
        }
    }

    public float getSnapFraction(Rect stackBounds, Rect movementBounds) {
        Rect tmpBounds = new Rect();
        snapRectToClosestEdge(stackBounds, movementBounds, tmpBounds);
        float widthFraction = (tmpBounds.left - movementBounds.left) / movementBounds.width();
        float heightFraction = (tmpBounds.top - movementBounds.top) / movementBounds.height();
        if (tmpBounds.top == movementBounds.top) {
            return widthFraction;
        }
        if (tmpBounds.left == movementBounds.right) {
            return 1.0f + heightFraction;
        }
        if (tmpBounds.top == movementBounds.bottom) {
            return 2.0f + (1.0f - widthFraction);
        }
        return 3.0f + (1.0f - heightFraction);
    }

    public void applySnapFraction(Rect stackBounds, Rect movementBounds, float snapFraction) {
        if (snapFraction < 1.0f) {
            int offset = movementBounds.left + ((int) (movementBounds.width() * snapFraction));
            stackBounds.offsetTo(offset, movementBounds.top);
        } else if (snapFraction < 2.0f) {
            int offset2 = movementBounds.top + ((int) (movementBounds.height() * (snapFraction - 1.0f)));
            stackBounds.offsetTo(movementBounds.right, offset2);
        } else if (snapFraction < 3.0f) {
            int offset3 = movementBounds.left + ((int) ((1.0f - (snapFraction - 2.0f)) * movementBounds.width()));
            stackBounds.offsetTo(offset3, movementBounds.bottom);
        } else {
            int offset4 = movementBounds.top + ((int) ((1.0f - (snapFraction - 3.0f)) * movementBounds.height()));
            stackBounds.offsetTo(movementBounds.left, offset4);
        }
    }

    public void getMovementBounds(Rect stackBounds, Rect insetBounds, Rect movementBoundsOut, int bottomOffset) {
        movementBoundsOut.set(insetBounds);
        movementBoundsOut.right = Math.max(insetBounds.left, insetBounds.right - stackBounds.width());
        movementBoundsOut.bottom = Math.max(insetBounds.top, insetBounds.bottom - stackBounds.height());
        movementBoundsOut.bottom -= bottomOffset;
    }

    public Size getSizeForAspectRatio(float aspectRatio, float minEdgeSize, int displayWidth, int displayHeight) {
        int height;
        int width;
        int smallestDisplaySize = Math.min(displayWidth, displayHeight);
        int minSize = (int) Math.max(minEdgeSize, smallestDisplaySize * this.mDefaultSizePercent);
        if (aspectRatio <= this.mMinAspectRatioForMinSize || aspectRatio > this.mMaxAspectRatioForMinSize) {
            if (aspectRatio <= 1.0f) {
                width = minSize;
                height = Math.round(width / aspectRatio);
            } else {
                height = minSize;
                width = Math.round(height * aspectRatio);
            }
        } else {
            float widthAtMaxAspectRatioForMinSize = this.mMaxAspectRatioForMinSize * minSize;
            float radius = PointF.length(widthAtMaxAspectRatioForMinSize, minSize);
            height = (int) Math.round(Math.sqrt((radius * radius) / ((aspectRatio * aspectRatio) + 1.0f)));
            width = Math.round(height * aspectRatio);
        }
        return new Size(width, height);
    }

    private Point findClosestPoint(int x, int y, Point[] points) {
        Point closestPoint = null;
        float minDistance = Float.MAX_VALUE;
        for (Point p : points) {
            float distance = distanceToPoint(p, x, y);
            if (distance < minDistance) {
                closestPoint = p;
                minDistance = distance;
            }
        }
        return closestPoint;
    }

    private void snapRectToClosestEdge(Rect stackBounds, Rect movementBounds, Rect boundsOut) {
        int shortest;
        int boundedLeft = Math.max(movementBounds.left, Math.min(movementBounds.right, stackBounds.left));
        int boundedTop = Math.max(movementBounds.top, Math.min(movementBounds.bottom, stackBounds.top));
        boundsOut.set(stackBounds);
        if (this.mIsMinimized) {
            boundsOut.offsetTo(boundedLeft, boundedTop);
            return;
        }
        int fromLeft = Math.abs(stackBounds.left - movementBounds.left);
        int fromTop = Math.abs(stackBounds.top - movementBounds.top);
        int fromRight = Math.abs(movementBounds.right - stackBounds.left);
        int fromBottom = Math.abs(movementBounds.bottom - stackBounds.top);
        if (this.mSnapMode == 4) {
            if (this.mOrientation == 2) {
                shortest = Math.min(fromTop, fromBottom);
            } else {
                shortest = Math.min(fromLeft, fromRight);
            }
        } else {
            int shortest2 = Math.min(fromLeft, fromRight);
            shortest = Math.min(shortest2, Math.min(fromTop, fromBottom));
        }
        if (shortest == fromLeft) {
            boundsOut.offsetTo(movementBounds.left, boundedTop);
        } else if (shortest == fromTop) {
            boundsOut.offsetTo(boundedLeft, movementBounds.top);
        } else if (shortest == fromRight) {
            boundsOut.offsetTo(movementBounds.right, boundedTop);
        } else {
            boundsOut.offsetTo(boundedLeft, movementBounds.bottom);
        }
    }

    private float distanceToPoint(Point p, int x, int y) {
        return PointF.length(p.x - x, p.y - y);
    }

    private void calculateSnapTargets() {
        this.mSnapGravities.clear();
        switch (this.mSnapMode) {
            case 0:
            case 3:
            case 4:
                break;
            case 2:
            default:
                return;
            case 1:
                if (this.mOrientation == 2) {
                    this.mSnapGravities.add(49);
                    this.mSnapGravities.add(81);
                    break;
                } else {
                    this.mSnapGravities.add(19);
                    this.mSnapGravities.add(21);
                    break;
                }
        }
        this.mSnapGravities.add(51);
        this.mSnapGravities.add(53);
        this.mSnapGravities.add(83);
        this.mSnapGravities.add(85);
    }

    public void dump(PrintWriter pw, String prefix) {
        String innerPrefix = prefix + "  ";
        pw.println(prefix + PipSnapAlgorithm.class.getSimpleName());
        pw.println(innerPrefix + "mSnapMode=" + this.mSnapMode);
        pw.println(innerPrefix + "mOrientation=" + this.mOrientation);
        pw.println(innerPrefix + "mMinimizedVisibleSize=" + this.mMinimizedVisibleSize);
        pw.println(innerPrefix + "mIsMinimized=" + this.mIsMinimized);
    }
}
