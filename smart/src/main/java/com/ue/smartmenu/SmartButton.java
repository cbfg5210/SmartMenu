package com.ue.smartmenu;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
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

    private String text;
    private int textSize;
    private float imageRatio;
    private int textColor;
    private int imageSrc;

    public SmartButton(Context context) {
        this(context, null);
    }

    public SmartButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SmartButton);
        mDotRadius = ta.getDimensionPixelSize(R.styleable.SmartButton_dotRadius, dip2px(context, 1));
        mDotDistance = ta.getDimensionPixelSize(R.styleable.SmartButton_dotDistance, dip2px(context, 25));
        mDotColor = ta.getColor(R.styleable.SmartButton_dotColor, Color.WHITE);
        mShadowColor = ta.getColor(R.styleable.SmartButton_shadowColor, Color.WHITE);
        mBackgroundColor = ta.getColor(R.styleable.SmartButton_bgColor, Color.BLACK);

        text = ta.getString(R.styleable.SmartButton_android_text);
        textSize = ta.getDimensionPixelSize(R.styleable.SmartButton_android_textSize, 15);
        imageRatio = ta.getFloat(R.styleable.SmartButton_imageRatio, 0.5f);
        textColor = ta.getColor(R.styleable.SmartButton_android_textColor, Color.WHITE);
        imageSrc = ta.getResourceId(R.styleable.SmartButton_android_src, 0);

        ta.recycle();

        init();
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setDither(true);
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setShadowLayer(15, 0, 0, mShadowColor);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        if (!TextUtils.isEmpty(text)) {
            mPaint.setStrokeWidth(3);
            mPaint.setTextSize(textSize);
            mPaint.setColor(textColor);
            mPaint.setTextAlign(Paint.Align.LEFT);
        } else {
            mPaint.setColor(mDotColor);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);

        if (imageSrc != 0) {
            drawImageContent(canvas);
            return;
        }
        if (!TextUtils.isEmpty(text)) {
            drawTextContent(canvas);
            return;
        }
        drawDefContent(canvas);
    }

    private void drawImageContent(Canvas canvas) {
        adjustCanvas(canvas, 360 * mPercent);

        int wh = (int) (getMeasuredWidth() * imageRatio);
        int offset = (getMeasuredWidth() - wh) / 2;

        Bitmap piece = BitmapFactory.decodeResource(getContext().getResources(), imageSrc);
        piece = Bitmap.createScaledBitmap(piece, wh, wh, false);
        canvas.drawBitmap(piece, offset, offset, mPaint);
    }

    private void drawTextContent(Canvas canvas) {
        adjustCanvas(canvas, 360 * mPercent);

        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(text, getMeasuredWidth() / 2 - bounds.width() / 2, baseline, mPaint);
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

    private void drawDefContent(Canvas canvas) {
        drawCenter(canvas);
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
