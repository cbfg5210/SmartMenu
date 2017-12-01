package com.ue.smartmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.jake.smart.R;

/**
 * Created by Jake on 2016/9/12.
 * Perhaps I would be single for the rest of my life
 */
class ContentButton extends View {
    /**
     * the max length of X
     */
    private float mCenterX;
    private float mCenterY;

    private Paint mBackgroundPaint;

    private int mBackgroundColor;
    private int mShadowColor;
    private String text;
    private int textSize;
    private float imageRatio;
    private int textColor;
    private int imageSrc;

    public ContentButton(Context context) {
        this(context, null);
    }

    public ContentButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContentButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ContentButton);
        mShadowColor = ta.getColor(R.styleable.ContentButton_shadow_color, Color.WHITE);
        mBackgroundColor = ta.getColor(R.styleable.ContentButton_bg_color, Color.BLACK);
        text = ta.getString(R.styleable.ContentButton_android_text);
        textSize = ta.getDimensionPixelSize(R.styleable.ContentButton_android_textSize, 15);
        imageRatio = ta.getFloat(R.styleable.ContentButton_image_ratio, 0.5f);
        textColor = ta.getColor(R.styleable.ContentButton_android_textColor, Color.WHITE);
        imageSrc = ta.getResourceId(R.styleable.ContentButton_android_src, 0);
        ta.recycle();

        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setDither(true);
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setShadowLayer(15, 0, 0, mShadowColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawImage(canvas);
//        drawText(canvas);
    }

    private void drawImage(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);//抖动处理，平滑处理

        int wh = (int) (getMeasuredWidth() * imageRatio);
        int offset = (getMeasuredWidth() - wh) / 2;

        Bitmap piece = BitmapFactory.decodeResource(getContext().getResources(), imageSrc);
        piece = Bitmap.createScaledBitmap(piece, wh, wh, false);
        canvas.drawBitmap(piece, offset, offset, mPaint);
    }

    private void drawText(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize(40);
        mPaint.setColor(textColor);
        mPaint.setTextAlign(Paint.Align.LEFT);
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(text, getMeasuredWidth() / 2 - bounds.width() / 2, baseline, mPaint);
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
