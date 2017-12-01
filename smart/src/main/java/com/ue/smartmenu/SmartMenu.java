package com.ue.smartmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.jake.smart.R;

/**
 * 自定义布局容器(集成ViewGroup)，则一般需要实现/重载三个方法，
 * 一个是onMeasure()，也是用来测量尺寸；
 * 一个是onLayout()，用来布局子控件；
 * 还有一个是dispatchDraw()，用来绘制UI。
 */
public class SmartMenu extends ViewGroup {
    /**
     * measurement unit is dp
     */
    private static final int DEFAULT_PADDING = 10;
    /**
     * measurement unit is dp
     */
    private static final int DEFAULT_BTN_SIZE = 70;

    private int mOuterPadding;
    private int mInnerPadding;
    private int mMenuHeight;
    private int mVerticalPadding;
    private int mSwitchBtnSize;

    private boolean mOpen;

    private RectF mRect = new RectF();
    private ValueAnimator mSwitchAnimation;
    private ValueAnimator mScaleAnimator;

    private Paint mBackgroundPaint;

    private int mSwitchDuration = 300;
    private int mScaleDuration = 100;
    private int mCurrentTargetPosition;

    private int mBackgroundColor;
    private int mShadowColor;
    private int smartViewRes;

    private View smartView;

    public void setSmartListener(OnClickListener smartListener) {
        smartView.setOnClickListener(smartListener);
    }

    private void init() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setDither(true);
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setShadowLayer(15, 0, 0, mShadowColor);

        if (smartViewRes == 0) {
            smartViewRes = R.layout.layout_smart_button;
        }
        smartView = LayoutInflater.from(getContext()).inflate(smartViewRes, null);
        smartView.setOnClickListener(v -> toggle());

        initScaleAnimator();
        initSwitchAnimator();
    }

    public SmartMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SmartMenu);
        mOuterPadding = ta.getDimensionPixelSize(R.styleable.SmartMenu_outer_padding, dip2px(context, DEFAULT_PADDING));
        mInnerPadding = ta.getDimensionPixelSize(R.styleable.SmartMenu_inner_padding, dip2px(context, DEFAULT_PADDING));
        mVerticalPadding = ta.getDimensionPixelSize(R.styleable.SmartMenu_vertical_padding, dip2px(context, DEFAULT_PADDING));
        mSwitchBtnSize = ta.getDimensionPixelSize(R.styleable.SmartMenu_smart_btn_size, dip2px(context, DEFAULT_BTN_SIZE));
        mShadowColor = ta.getColor(R.styleable.SmartMenu_shadow_color, Color.WHITE);
        mBackgroundColor = ta.getColor(R.styleable.SmartMenu_bg_color, Color.BLACK);
        mMenuHeight = mSwitchBtnSize - 2 * mVerticalPadding;
        smartViewRes = ta.getResourceId(R.styleable.SmartMenu_smart_view_res, 0);
        ta.recycle();
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //Math.ceil(x) 返回大于参数x的最小整数,即对浮点数向上取整.
        int layerCount = (int) Math.ceil((getChildCount() - 1) * 0.5);
        if (layerCount <= 0) {
            smartView.layout(
                    getMeasuredWidth() / 2 - mSwitchBtnSize / 2,
                    0,
                    getMeasuredWidth() / 2 + mSwitchBtnSize / 2,
                    getMeasuredHeight());
            return;
        }
        left = mOuterPadding;
        for (int i = 1; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.layout(left, mVerticalPadding, left + view.getMeasuredWidth(), getMeasuredHeight() - mVerticalPadding);

            left += view.getMeasuredWidth() + mInnerPadding;
            if (i == layerCount) {
                view = getChildAt(0);
                view.layout(left, 0, left + view.getMeasuredWidth(), getMeasuredHeight());
                left += view.getMeasuredWidth() + mInnerPadding;
            }
        }
    }

    private void setViewScale(View view, float scale) {
        if (scale == 0) {
            view.setVisibility(View.GONE);
        } else if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        //setPivotX,setPivotY设置锚点坐标值，默认是view的中心，不然缩放可能会改变缩放后的位置
        view.setPivotX(view.getMeasuredWidth() / 2);
        view.setPivotY(view.getMeasuredHeight() / 2);
        //setScaleX：宽度变为原来的scale倍，setScaleY：高度变为原来的scale倍
        view.setScaleX(scale);
        view.setScaleY(scale);
    }

    public void setAdapter(BaseAdapter adapter) {
        fillLayout(adapter);
    }

    public void setImages(int[] images, AdapterView.OnItemClickListener itemClickListener) {
        fillLayout(new MenuAdapter(images, itemClickListener));
    }

    public void setTexts(String[] texts, AdapterView.OnItemClickListener itemClickListener) {
        fillLayout(new MenuAdapter(texts, itemClickListener));
    }

    private void fillLayout(BaseAdapter mAdapter) {
        removeAllViews();
        addView(smartView, new LayoutParams(mSwitchBtnSize, mSwitchBtnSize));

        for (int i = 0, count = mAdapter.getCount(); i < count; i++) {
            View view = mAdapter.getView(i, null, this);
            view.setVisibility(View.GONE);
            addView(view, new LayoutParams(LayoutParams.WRAP_CONTENT, mMenuHeight));
        }
        int size = getChildCount() / 2;
        if (size > 0) {
            mScaleAnimator.setRepeatCount(size - 1);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mSwitchBtnSize;
        int childHeightSpec = MeasureSpec.makeMeasureSpec(mMenuHeight, MeasureSpec.AT_MOST);
        for (int i = 1; i < getChildCount(); i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, childHeightSpec);
            width += view.getMeasuredWidth();
            width += mInnerPadding;
        }
        smartView.measure(
                MeasureSpec.makeMeasureSpec(mSwitchBtnSize, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mSwitchBtnSize, MeasureSpec.EXACTLY));

        width += 2 * mOuterPadding;
        setMeasuredDimension(width, mSwitchBtnSize);
        if (mRect.left == 0)
            mRect.set(
                    getMeasuredWidth() / 2,
                    mVerticalPadding,
                    getMeasuredWidth() / 2,
                    getMeasuredHeight() - mVerticalPadding);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void toggle() {
        if (mSwitchAnimation.isRunning() || mScaleAnimator.isRunning()) {
            return;
        }
        if (mOpen) {
            mSwitchAnimation.setFloatValues(100, 0);
        } else {
            mSwitchAnimation.setFloatValues(0, 100);
        }
        mOpen = !mOpen;
        mSwitchAnimation.start();
    }

    private void initScaleAnimator() {
        if (mScaleAnimator != null) {
            return;
        }
        mScaleAnimator = ValueAnimator.ofFloat(0, 1f);
        mScaleAnimator.addUpdateListener(valueAnimator -> {
            int a = getChildCount() / 2 - mCurrentTargetPosition;
            int b = getChildCount() - a;

            float value = (float) valueAnimator.getAnimatedValue();
            if (a >= 0 && a < getChildCount()) {
                setViewScale(getChildAt(a), value);
                getChildAt(a).setAlpha(value);
            }
            if (a == b || b < 0 || b >= getChildCount()) {
                return;
            }
            setViewScale(getChildAt(b), value);
            getChildAt(b).setAlpha(value);
        });

        mScaleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                if (mOpen) {
                    //open
                    mCurrentTargetPosition++;
                    if (mCurrentTargetPosition >= (getChildCount() / 2)) {
                        mCurrentTargetPosition = getChildCount() / 2 - 1;
                    }
                    return;
                }
                //close
                mCurrentTargetPosition--;
                if (mCurrentTargetPosition < 0) {
                    mCurrentTargetPosition = 0;
                }
            }
        });
        mScaleAnimator.setDuration(mScaleDuration);
    }

    private void initSwitchAnimator() {
        if (mSwitchAnimation == null) {
            mSwitchAnimation = ValueAnimator.ofFloat(0, 100);
            mSwitchAnimation.setDuration(mSwitchDuration);
            mSwitchAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

            mSwitchAnimation.addUpdateListener(animation -> {
                updateAnimation(animation);
            });

            if (smartView instanceof ValueAnimator.AnimatorUpdateListener) {
                mSwitchAnimation.addUpdateListener((ValueAnimator.AnimatorUpdateListener) smartView);
            }

            mSwitchAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (!mOpen) {
                        toggleItems(mOpen);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (mOpen) {
                        toggleItems(mOpen);
                    }
                }
            });
        }
    }

    private void toggleItems(boolean isShow) {
        if (getChildCount() <= 1) {
            return;
        }
        if (isShow) {
            mScaleAnimator.setFloatValues(0, 1);
        } else {
            mScaleAnimator.setFloatValues(1, 0);
        }
        mScaleAnimator.start();
    }

    public void updateAnimation(ValueAnimator valueAnimator) {
        int centerX = getMeasuredWidth() / 2;
        float start = centerX;
        float end = centerX;
        float length = centerX - 10;
        float percent = (float) valueAnimator.getAnimatedValue() / 100.f;
        length *= percent;
        mRect.set(start - length, mVerticalPadding, end + length, getMeasuredHeight() - mVerticalPadding);
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mRect != null) {
            //rx：x方向上的圆角半径；ry：y方向上的圆角半径。
            canvas.drawRoundRect(mRect, mSwitchBtnSize / 3, mSwitchBtnSize / 3, mBackgroundPaint);
        }
        super.dispatchDraw(canvas);
    }

    public void setSwitchDuration(int duration) {
        mSwitchDuration = duration;
    }

    public void setScaleDuration(int duration) {
        mScaleDuration = duration;
    }
}
