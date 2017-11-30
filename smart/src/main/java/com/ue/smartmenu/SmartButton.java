package com.ue.smartmenu;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jake.smart.R;

/**
 * Created by Jake on 2016/9/12.
 * Perhaps I would be single for the rest of my life
 */
class SmartButton extends View implements ValueAnimator.AnimatorUpdateListener {

    private float mPercent;
    /**
     * the max length of X
     */
    private int mDotDistance;
    private float mDotRadius = 6;
    private float mCenterX;
    private float mCenterY;

    private Paint mPaint;
    private Paint mBackgroundPaint;

    private RectF mLeftRectF = new RectF();
    private RectF mRightRectF = new RectF();

    private int mBackgroundColor;
    private int mShadowColor;
    private int mDotColor;

    public SmartButton(Context context) {
        this(context, null);
    }

    public SmartButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SmartButton);
        mDotRadius = ta.getDimensionPixelSize(R.styleable.SmartButton_dot_radius, dip2px(context, 1));
        mDotDistance = ta.getDimensionPixelSize(R.styleable.SmartButton_dot_distance, dip2px(context, 25));
        mDotColor = ta.getColor(R.styleable.SmartButton_dot_color, Color.WHITE);
        mShadowColor = ta.getColor(R.styleable.SmartButton_shadow_color, Color.WHITE);
        mBackgroundColor = ta.getColor(R.styleable.SmartButton_bg_color, Color.BLACK);
        ta.recycle();

        init();
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mDotColor);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setDither(true);
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setShadowLayer(15, 0, 0, mShadowColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawCenter(canvas);
        drawContent(canvas);
    }

    private void drawCenter(Canvas canvas) {
        canvas.drawCircle(mCenterX, mCenterY, mDotRadius, mPaint);
    }

    private void adjustCanvas(Canvas canvas, float angle) {
        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        canvas.rotate(angle);
        canvas.translate(-mCenterX, -mCenterY);
    }

    private void drawContent(Canvas canvas) {
        int length = (int) ((mDotDistance - 2 * mDotRadius) * mPercent);
        drawLeft(canvas, mDotRadius, length);
        drawRight(canvas, mDotRadius, length);
    }

    private void drawLeft(Canvas canvas, float radius, int length) {
        adjustCanvas(canvas, 45 * mPercent);
        float start = mCenterX - mDotDistance / 2;
        mLeftRectF.set(start, mCenterY - radius, start + length + 2 * mDotRadius, mCenterY + radius);
        canvas.drawRoundRect(mLeftRectF, radius, radius, mPaint);
        canvas.restore();

    }

    private void drawRight(Canvas canvas, float radius, int length) {
        adjustCanvas(canvas, -45 * mPercent);
        float start = mCenterX + mDotDistance / 2;
        mRightRectF.set(start - length - 2 * mDotRadius, mCenterY - radius, start, mCenterY + radius);
        canvas.drawRoundRect(mRightRectF, radius, radius, mPaint);
        canvas.restore();
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawCircle(mCenterX, mCenterY, getMeasuredWidth() / 2 - 10, mBackgroundPaint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(size, size);
        mCenterX = getMeasuredWidth() / 2.f;
        mCenterY = getMeasuredHeight() / 2.f;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mPercent = (float) valueAnimator.getAnimatedValue() / 100.f;
        invalidate();
    }

    void setRadius(float radius) {
        mDotRadius = radius;
    }

    void setLength(int length) {
        mDotDistance = length;
    }

    void setDotColor(int color) {
        this.mDotColor = color;
        mPaint.setColor(mDotColor);
        invalidate();
    }

    @Override
    public void setBackgroundColor(int color) {
        mBackgroundColor = color;
        mBackgroundPaint.setColor(color);
        invalidate();
    }

    public void setShadowColor(int color) {
        mShadowColor = color;
        mBackgroundPaint.setShadowLayer(15, 0, 0, color);
        invalidate();
    }
}
