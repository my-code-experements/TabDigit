package com.xenione.digit;

import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class RectF {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0x00201, "-MainAbility-");

    public float left;
    public float top;
    public float right;
    public float bottom;
    //#region constructor

    public RectF() {

    }

    /**
     * Create a new rectangle with the specified coordinates. Note: no range
     * checking is performed, so the caller must ensure that left <= right and
     * top <= bottom.
     *
     * @param left   The X coordinate of the left side of the rectangle
     * @param top    The Y coordinate of the top of the rectangle
     * @param right  The X coordinate of the right side of the rectangle
     * @param bottom The Y coordinate of the bottom of the rectangle
     */
    public RectF(float left, float top, float right, float bottom) {
        HiLog.warn(LABEL_LOG, "RectF: RectF");
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /**
     * Create a new rectangle, initialized with the values in the specified
     * rectangle (which is left unmodified).
     *
     * @param r The rectangle whose coordinates are copied into the new
     *          rectangle.
     */
    public RectF(RectF r) {
        HiLog.warn(LABEL_LOG, "RectF: RectF" + r);
        if (r == null) {
            left = top = right = bottom = 0.0f;
        } else {
            left = r.left;
            top = r.top;
            right = r.right;
            bottom = r.bottom;
        }
    }

    public RectF(Rect r) {
        if (r == null) {
            left = top = right = bottom = 0.0f;
        } else {
            left = r.left;
            top = r.top;
            right = r.right;
            bottom = r.bottom;
        }
    }
//#endregion constructor

    /**
     * Set the rectangle's coordinates to the specified values. Note: no range
     * checking is performed, so it is up to the caller to ensure that
     * left <= right and top <= bottom.
     *
     * @param left   The X coordinate of the left side of the rectangle
     * @param top    The Y coordinate of the top of the rectangle
     * @param right  The X coordinate of the right side of the rectangle
     * @param bottom The Y coordinate of the bottom of the rectangle
     */
    public void set(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }


    /**
     * Copy the coordinates from src into this rectangle.
     *
     * @param src The rectangle whose coordinates are copied into this
     *            rectangle.
     */
    public void set(RectF src) {
        this.left = src.left;
        this.top = src.top;
        this.right = src.right;
        this.bottom = src.bottom;
    }

    /**
     * Copy the coordinates from src into this rectangle.
     *
     * @param src The rectangle whose coordinates are copied into this
     *            rectangle.
     */
    public void set(Rect src) {
        HiLog.warn(LABEL_LOG, "RectF: set" + src);
        this.left = src.left;
        this.top = src.top;
        this.right = src.right;
        this.bottom = src.bottom;
    }

    /**
     * Offset the rectangle by adding dx to its left and right coordinates, and
     * adding dy to its top and bottom coordinates.
     *
     * @param dx The amount to add to the rectangle's left and right coordinates
     * @param dy The amount to add to the rectangle's top and bottom coordinates
     */
    public void offset(float dx, float dy) {
        HiLog.warn(LABEL_LOG, String.format("RectF: offset(dx: %.2f, dy: %.2f)", dx, dy));
        left += dx;
        top += dy;
        right += dx;
        bottom += dy;
    }

    /**
     * @return the rectangle's height. This does not check for a valid rectangle
     * (i.e. top <= bottom) so the result may be negative.
     */
    public final float getHeight() {
        float height = bottom - top;
//            height = 200;
//            HiLog.warn(LABEL_LOG, "RectF: getHeight" + (height));
        return height;
    }

    public RectFloat toRectFloat() {
        int mul = 1;
        RectFloat r = new RectFloat(left * mul, top * mul, right * mul, bottom * mul);
//            HiLog.warn(LABEL_LOG, "RectF: toRectFloat -> " + r);
        return r;
//            return new RectFloat(-510, 0, 510, 1740);
    }


    @Override
    public String toString() {
        return String.format("RectF { top:%.2f, left:%.2f, right:%.2f,bottom: %.2f }", top, left, right, bottom);
    }

    /**
     * @return the rectangle's width. This does not check for a valid rectangle
     * (i.e. left <= right) so the result may be negative.
     */
    public final float getWidth() {
        float width = right - left;
        width = 100;
        HiLog.warn(LABEL_LOG, "RectF: getWidth" + (width));
        return width;
    }

}
