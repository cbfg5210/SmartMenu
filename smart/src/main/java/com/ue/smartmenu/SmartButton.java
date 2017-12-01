package com.ue.smartmenu;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jake on 2016/9/12.
 * Perhaps I would be single for the rest of my life
 */
class SmartButton extends View implements ValueAnimator.AnimatorUpdateListener {

    private float mPercent;
    /**
     * the max length of X
     */
    private float mCenterX;
    private float mCenterY;

    private Paint mPaint;
    private Paint mBackgroundPaint;

    private int mBackgroundColor;
    private int mShadowColor;

    private RectF mLeftRectF;
    private RectF mRightRectF;
    //def content
    private int mDotColor;
    private int mDotDistance;
    private float mDotRadius;
    //text content
    private String mText;
    //image content
    private int imageSrc;
    private float imageRatio;

    private boolean hasInit;

    public SmartButton(Context context) {
        this(context, null);
    }

    public SmartButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
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
        //for text:
        mPaint.setStrokeWidth(3);
        mPaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!hasInit) {
            return;
        }
        drawBackground(canvas);

        if (imageSrc != 0) {
            drawImageContent(canvas);
            return;
        }
        if (!TextUtils.isEmpty(mText)) {
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
        mPaint.getTextBounds(mText, 0, mText.length(), bounds);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(mText, getMeasuredWidth() / 2 - bounds.width() / 2, baseline, mPaint);
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
        mCenterX = getMeasuredWidth() * 0.5f;
        mCenterY = getMeasuredHeight() * 0.5f;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        mPercent = (float) valueAnimator.getAnimatedValue() / 100.f;
        invalidate();
    }

    void initDefContent(float dotRadius, int dotDistance, int dotColor) {
        mDotRadius = dotRadius;
        mDotDistance = dotDistance;
        mDotColor = dotColor;

        mPaint.setColor(mDotColor);
        if (mLeftRectF == null) {
            mLeftRectF = new RectF();
            mRightRectF = new RectF();
        }

        hasInit = true;
        invalidate();
    }

    void initImageContent(@DrawableRes int imageSrc, float imageRatio) {
        this.imageSrc = imageSrc;
        this.imageRatio = imageRatio;

        hasInit = true;
        invalidate();
    }

    void initTextContent(int textSize, int textColor, String text) {
        mText = text;

        mPaint.setTextSize(textSize);
        mPaint.setColor(textColor);

        hasInit = true;
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
