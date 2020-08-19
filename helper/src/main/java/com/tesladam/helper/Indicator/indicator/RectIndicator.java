package com.tesladam.helper.Indicator.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.tesladam.helper.R;
import com.tesladam.helper.Indicator.animation.AnimationValue;
import com.tesladam.helper.Indicator.animation.ColorAnimation;
import com.tesladam.helper.Indicator.animation.IndicatorAnimation;
import com.tesladam.helper.Indicator.animation.SlideAnimation;
import com.tesladam.helper.Indicator.painter.RectPainter;

public final class RectIndicator extends ViewPagerIndicator {
    private AnimationMode indicatorAnimationMode = AnimationMode.SLIDE;
    private RectPainter painter;
    private int indicatorWidth;
    private int indicatorHeight;
    protected float indicatorCornerRadius;

    public RectIndicator(Context context) {
        super(context);
        initialize(context, null);
    }

    public RectIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                    R.styleable.RectIndicator2, 0, 0);
            try {
                indicatorColor = typedArray.getColor(R.styleable.RectIndicator2_indicatorColor2,
                        getResources().getColor(android.R.color.darker_gray));
                indicatorSelectedColor = typedArray.getColor(
                        R.styleable.RectIndicator2_indicatorSelectedColor2, R.attr.colorAccent);
                indicatorGap = typedArray.getDimensionPixelSize(
                        R.styleable.RectIndicator2_indicatorGap2, 0);
                indicatorItemCount = typedArray.getInt(R.styleable.RectIndicator2_indicatorItemCount2, 0);
                indicatorWidth = typedArray.getDimensionPixelSize(
                        R.styleable.RectIndicator2_indicatorWidth2, 0);
                indicatorHeight = typedArray.getDimensionPixelSize(
                        R.styleable.RectIndicator2_indicatorHeight2, 0);
                indicatorCornerRadius = typedArray.getDimension(
                        R.styleable.RectIndicator2_indicatorCornerRadius2, 0);
            } finally {
                typedArray.recycle();
            }
        }

        setupAnimation();
    }

    private void setupAnimation() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        switch (indicatorAnimationMode) {
            case SLIDE:
                painter = new RectPainter.SlideAnimationPainter(paint, indicatorColor,
                        indicatorSelectedColor, indicatorWidth, indicatorHeight,
                        indicatorCornerRadius, this
                );
                indicatorAnimation = new SlideAnimation(new IndicatorAnimation.AnimationUpdateListener() {
                    @Override
                    public void onAnimationUpdate(AnimationValue animationValue) {
                        ((RectPainter.AnimationPainter) painter).setAnimationValue(animationValue);
                        invalidate();
                    }
                });
                break;
            case COLOR:
                painter = new RectPainter.ColorAnimationPainter(paint, indicatorColor,
                        indicatorSelectedColor, indicatorWidth, indicatorHeight,
                        indicatorCornerRadius, this
                );
                indicatorAnimation = new ColorAnimation(indicatorSelectedColor, indicatorColor,
                        new IndicatorAnimation.AnimationUpdateListener() {
                            @Override
                            public void onAnimationUpdate(AnimationValue animationValue) {
                                ((RectPainter.AnimationPainter) painter).setAnimationValue(
                                        animationValue);
                                invalidate();
                            }
                        });
                break;
            case NONE:
                indicatorAnimation = null;
                painter = new RectPainter(paint, indicatorColor, indicatorSelectedColor,
                        indicatorWidth, indicatorHeight, indicatorCornerRadius, this);
                break;
        }
    }

    public void setAnimationMode(AnimationMode animationMode) {
        setAnimationMode(animationMode, IndicatorAnimation.DEFAULT_DURATION);
    }

    public void setAnimationMode(AnimationMode animationMode, long duration) {
        indicatorAnimationMode = animationMode;
        setupAnimation();
        if (animationMode != AnimationMode.NONE) {
            indicatorAnimation.setDuration(duration);
        }
    }

    public int getIndicatorWidth() {
        return indicatorWidth;
    }

    public void setIndicatorWidth(int indicatorWidth) {
        this.indicatorWidth = indicatorWidth;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setIndicatorHeight(int indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
        invalidate();
    }

    public float getIndicatorCornerRadius() {
        return indicatorCornerRadius;
    }

    public void setIndicatorCornerRadius(float indicatorCornerRadius) {
        this.indicatorCornerRadius = indicatorCornerRadius;
        invalidate();
    }

    @Override
    protected int measureWidth() {
        return indicatorWidth * getItemCount() + indicatorGap * (getItemCount() - 1);
    }

    @Override
    protected int measureHeight() {
        return indicatorHeight;
    }

    @Override
    public int getCoordinateX(int position) {
        int coordinate = getPaddingLeft();
        for (int i = 0; i < getItemCount(); i++) {
            if (i == position) {
                return coordinate;
            }
            coordinate += indicatorWidth + indicatorGap;
        }
        return coordinate;
    }

    @Override
    public int getCoordinateY() {
        return getPaddingTop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < getItemCount(); i++) {
            painter.draw(canvas, i);
        }
    }

    public enum AnimationMode {
        SLIDE, COLOR, NONE
    }
}
