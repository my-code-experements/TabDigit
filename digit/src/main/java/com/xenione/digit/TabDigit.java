package com.xenione.digit;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eugeni on 16/10/2016.
 */
public class TabDigit extends View implements Runnable {

    private static final String TAG = "TabDigit";
    /*
     * false: rotate upwards
     * true: rotate downwards
     */
    private boolean mReverseRotation = false;

    private Tab mTopTab;

    private Tab mBottomTab;

    private Tab mMiddleTab;

    private List<Tab> tabs = new ArrayList<>(3);

    private AbstractTabAnimation tabAnimation;

    private Matrix mProjectionMatrix = new Matrix();

    private int mCornerSize;

    private Paint mNumberPaint;

    private Paint mDividerPaint;

    private Paint mBackgroundPaint;
    private Paint mBackgroundPaint1;
    private Paint mBackgroundPaint2;
    private Paint mBackgroundPaint3;

    private Rect mTextMeasured = new Rect();

    private int mPadding = 0;

    private char[] mChars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public TabDigit(Context context) {
        this(context, null);
    }

    public TabDigit(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabDigit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TabDigit(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        Log.d(TAG, "init: ");
        initPaints();

        int padding = -1;
        int textSize = -1;
        int cornerSize = -1;
        int textColor = 1;
        int backgroundColor = 1;
        boolean reverseRotation = false;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabDigit, 0, 0);
        final int num = ta.getIndexCount();
        for (int i = 0; i < num; i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.TabDigit_textSize) {
                textSize = ta.getDimensionPixelSize(attr, -1);
                Log.d(TAG, "init: textSize " + textSize);
            } else if (attr == R.styleable.TabDigit_padding) {
                padding = ta.getDimensionPixelSize(attr, -1);
            } else if (attr == R.styleable.TabDigit_cornerSizeZ) {
                cornerSize = ta.getDimensionPixelSize(attr, -1);
            } else if (attr == R.styleable.TabDigit_textColor) {
                textColor = ta.getColor(attr, 1);
            } else if (attr == R.styleable.TabDigit_backgroundColor) {
                backgroundColor = ta.getColor(attr, 1);
            } else if (attr == R.styleable.TabDigit_reverseRotation) {
                reverseRotation = ta.getBoolean(attr, false);
            }
        }
        ta.recycle();

        if (padding > 0) {
            mPadding = padding;
        }

        if (textSize > 0) {
            mNumberPaint.setTextSize(textSize);
        }

        if (cornerSize > 0) {
            mCornerSize = cornerSize;
        }

        if (textColor < 1) {
            mNumberPaint.setColor(textColor);
        }

        if (backgroundColor < 1) {
            mBackgroundPaint.setColor(backgroundColor);
        }

        mReverseRotation = reverseRotation;
        mReverseRotation = true;

        initTabs();
    }

    private void initPaints() {
        Log.d(TAG, "initPaints: ");
        mNumberPaint = new Paint();
        mNumberPaint.setAntiAlias(true);
        mNumberPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mNumberPaint.setColor(Color.WHITE);

        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mDividerPaint.setColor(Color.WHITE);
        mDividerPaint.setStrokeWidth(1);

        mBackgroundPaint = new Paint();
        mBackgroundPaint1 = new Paint();
        mBackgroundPaint2 = new Paint();
        mBackgroundPaint3 = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(Color.BLACK);
        mBackgroundPaint1.setColor(Color.BLUE);
        mBackgroundPaint2.setColor(Color.YELLOW);
        mBackgroundPaint3.setColor(Color.GRAY);

    }

    private void initTabs() {
        Log.d(TAG, "initTabs: ");
        // top Tab
        mTopTab = new Tab();
        mTopTab.rotate(180);
        tabs.add(mTopTab);

        // bottom Tab
        mBottomTab = new Tab();
        tabs.add(mBottomTab);

        // middle Tab
        mMiddleTab = new Tab();
        tabs.add(mMiddleTab);

        tabAnimation = mReverseRotation ? new TabAnimationDown(mTopTab, mBottomTab, mMiddleTab) : new TabAnimationUp(mTopTab, mBottomTab, mMiddleTab);

        tabAnimation.initMiddleTab();

        setInternalChar(0);
    }

    public void setChar(int index) {
        Log.d(TAG, "setChar: ");
        setInternalChar(index);
        invalidate();
    }

    private void setInternalChar(int index) {
        Log.d(TAG, "setInternalChar: ");
        for (Tab tab : tabs) {
            tab.setChar(index);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure: ");
        calculateTextSize(mTextMeasured);

        int childWidth = mTextMeasured.width() + mPadding;
        int childHeight = mTextMeasured.height() + mPadding;
        measureTabs(childWidth, childHeight);

        int maxChildWidth = mMiddleTab.maxWith();
        int maxChildHeight = 2 * mMiddleTab.maxHeight();
        int resolvedWidth = resolveSize(maxChildWidth, widthMeasureSpec);
        int resolvedHeight = resolveSize(maxChildHeight, heightMeasureSpec);

        Log.d(TAG, "TabDigit: onEstimateSize: mTopTab -> " + mTopTab.mStartBounds);
        Log.d(TAG, "TabDigit: onEstimateSize: mBottomTab -> " + mBottomTab.mStartBounds);
        Log.d(TAG, "TabDigit: onEstimateSize: mMiddleTab -> " + mMiddleTab.mStartBounds);

        setMeasuredDimension(resolvedWidth, resolvedHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged: ");
        if (w != oldw || h != oldh) {
            setupProjectionMatrix();
        }
    }

    private void setupProjectionMatrix() {
        mProjectionMatrix.reset();
        int centerY = getHeight() / 2;
        int centerX = getWidth() / 2;
        Log.d(TAG, "setupProjectionMatrix: centerY -> " + centerY);
        Log.d(TAG, "setupProjectionMatrix: centerX -> " + centerX);
        Log.d(TAG, "setupProjectionMatrix: mProjectionMatrix -> " + mProjectionMatrix);
        MatrixHelper.translate(mProjectionMatrix, centerX, -centerY, 0);
        Log.d(TAG, "setupProjectionMatrix: mProjectionMatrix -> " + mProjectionMatrix);
    }

    private void measureTabs(int width, int height) {
        Log.d(TAG, "measureTabs: ");
        for (Tab tab : tabs) {
            tab.measure(width, height);
        }
    }

    private void drawTabs(Canvas canvas) {
        Log.d(TAG, "drawTabs: ");
        int i = 0;
        for (Tab tab : tabs) {
            tab.draw(canvas, i);
            i++;
        }
    }

    private void drawDivider(Canvas canvas) {
        Log.d(TAG, "drawDivider: ");
        canvas.save();
        canvas.concat(mProjectionMatrix);
        canvas.drawLine(-canvas.getWidth() / 2, 0, canvas.getWidth() / 2, 0, mDividerPaint);
        canvas.restore();
    }

    private void calculateTextSize(Rect rect) {
        Log.d(TAG, "calculateTextSize: ");
        mNumberPaint.getTextBounds("8", 0, 1, rect);
    }

    public void setTextSize(int size) {
        Log.d(TAG, "setTextSize: ");
        mNumberPaint.setTextSize(size);
        requestLayout();
    }

    public int getTextSize() {
        Log.d(TAG, "getTextSize: ");
        return (int) mNumberPaint.getTextSize();
    }

    public void setPadding(int padding) {
        Log.d(TAG, "setPadding: ");
        mPadding = padding;
        requestLayout();
    }

    /**
     * Sets chars that are going to be displayed.
     * Note: <b>That only one digit is allow per character.</b>
     *
     * @param chars
     */
    public void setChars(char[] chars) {
        Log.d(TAG, "setChars: ");
        mChars = chars;
    }

    public char[] getChars() {
        Log.d(TAG, "getChars: ");
        return mChars;
    }


    public void setDividerColor(int color) {
        Log.d(TAG, "setDividerColor: ");
        mDividerPaint.setColor(color);
    }

    public int getPadding() {
        Log.d(TAG, "getPadding: ");
        return mPadding;
    }

    public void setTextColor(int color) {
        Log.d(TAG, "setTextColor: ");
        mNumberPaint.setColor(color);
    }

    public int getTextColor() {
        Log.d(TAG, "getTextColor: ");
        return mNumberPaint.getColor();
    }

    public void setCornerSize(int cornerSize) {
        Log.d(TAG, "setCornerSize: ");
        mCornerSize = cornerSize;
        invalidate();
    }

    public int getCornerSize() {
        Log.d(TAG, "getCornerSize: ");
        return mCornerSize;
    }

    public void setBackgroundColor(int color) {
        Log.d(TAG, "setBackgroundColor: ");
        mBackgroundPaint.setColor(color);
    }

    public int getBackgroundColor() {
        Log.d(TAG, "getBackgroundColor: ");
        return mBackgroundPaint.getColor();
    }

    public void start() {
        Log.d(TAG, "start: ");
        tabAnimation.start();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: ");
        drawTabs(canvas);
        drawDivider(canvas);
        ViewCompat.postOnAnimationDelayed(this, this, 40);
    }

    @Override
    public void run() {
        Log.d(TAG, "run: ");
        tabAnimation.run();
        invalidate();
    }

    public void sync() {
        Log.d(TAG, "sync: ");
        tabAnimation.sync();
        invalidate();
    }

    public class Tab {

        private final Matrix mModelViewMatrix = new Matrix();

        private final Matrix mModelViewProjectionMatrix = new Matrix();

        private final Matrix mRotationModelViewMatrix = new Matrix();

        private final RectF mStartBounds = new RectF();

        private final RectF mEndBounds = new RectF();

        private int mCurrIndex = 0;

        private int mAlpha;

        private Matrix mMeasuredMatrixHeight = new Matrix();

        private Matrix mMeasuredMatrixWidth = new Matrix();


        public void measure(int width, int height) {
            Rect area = new Rect(-width / 2, 0, width / 2, height / 2);
            Log.d(TAG, "measure: area -> " + area);
            mStartBounds.set(area);
            mEndBounds.set(area);
            mEndBounds.offset(0, -height / 2);
        }

        public int maxWith() {
            Log.d(TAG, "maxWith: ");
            RectF rect = new RectF(mStartBounds);
            Matrix projectionMatrix = new Matrix();
            MatrixHelper.translate(projectionMatrix, mStartBounds.left, -mStartBounds.top, 0);
            mMeasuredMatrixWidth.reset();
            mMeasuredMatrixWidth.setConcat(projectionMatrix, MatrixHelper.ROTATE_X_90);
            mMeasuredMatrixWidth.mapRect(rect);
            return (int) rect.width();
        }

        public int maxHeight() {
            Log.d(TAG, "maxHeight: ");
            RectF rect = new RectF(mStartBounds);
            Matrix projectionMatrix = new Matrix();
            mMeasuredMatrixHeight.reset();
            mMeasuredMatrixHeight.setConcat(projectionMatrix, MatrixHelper.ROTATE_X_0);
            mMeasuredMatrixHeight.mapRect(rect);
            return (int) rect.height();
        }

        public void setChar(int index) {
            Log.d(TAG, "setChar: ");
            mCurrIndex = index > mChars.length ? 0 : index;
        }

        public void next() {
            Log.d(TAG, "next: ");
            mCurrIndex++;
            if (mCurrIndex >= mChars.length) {
                mCurrIndex = 0;
            }
        }

        public void rotate(int alpha) {
            mAlpha = alpha;
            Log.d(TAG, "rotate: " + mRotationModelViewMatrix);
            MatrixHelper.rotateX(mRotationModelViewMatrix, alpha);
            Log.d(TAG, "rotate: " + mRotationModelViewMatrix);
        }

        public void draw(Canvas canvas, int i) {
            Log.d(TAG, "draw: "+i);
            drawBackground(canvas, i);
            drawText(canvas);
        }

        private void drawBackground(Canvas canvas, int i) {
            Paint p = mBackgroundPaint;
            switch (i) {
                case 0:
                    p = mBackgroundPaint1;
                    break;
                case 1:
                    p = mBackgroundPaint2;
                    break;
                case 3:
                    p = mBackgroundPaint3;
                    break;

            }
            Log.d(TAG, "drawBackground: " + mStartBounds);
            canvas.save();
            mModelViewMatrix.set(mRotationModelViewMatrix);
            applyTransformation(canvas, mModelViewMatrix);
            canvas.drawRoundRect(mStartBounds, mCornerSize, mCornerSize, p);
            canvas.restore();
        }

        private void drawText(Canvas canvas) {
            Log.d(TAG, "drawText: ");
            canvas.save();
            mModelViewMatrix.set(mRotationModelViewMatrix);
            RectF clip = mStartBounds;
            if (mAlpha > 90) {
                mModelViewMatrix.setConcat(mModelViewMatrix, MatrixHelper.MIRROR_X);
                clip = mEndBounds;
            }
            applyTransformation(canvas, mModelViewMatrix);
            canvas.clipRect(clip);
            Log.d(TAG, "drawText: clip -> " + clip);
            Log.d(TAG, "drawText: mModelViewMatrix -> " + mModelViewMatrix);
            Log.d(TAG, "drawText: mEndBounds -> " + mEndBounds);
            Log.d(TAG, "drawText: mChars[mCurrIndex] -> " + mChars[mCurrIndex]);
            canvas.drawText(Character.toString(mChars[mCurrIndex]), 0, 1, -mTextMeasured.centerX(), -mTextMeasured.centerY(), mNumberPaint);
            canvas.restore();
        }

        private void applyTransformation(Canvas canvas, Matrix matrix) {
            Log.d(TAG, "applyTransformation: ");
            mModelViewProjectionMatrix.reset();
            mModelViewProjectionMatrix.setConcat(mProjectionMatrix, matrix);
            canvas.concat(mModelViewProjectionMatrix);
        }
    }

}
