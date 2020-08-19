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
import com.tesladam.helper.Indicator.animation.ScaleAnimation;
import com.tesladam.helper.Indicator.animation.SlideAnimation;
import com.tesladam.helper.Indicator.painter.CirclePainter;

public final class CircleIndicator extends ViewPagerIndicator {
    private AnimationMode indicatorAnimationMode = AnimationMode.SLIDE;
    private CirclePainter painter;
    private int indicatorRadius;

    public CircleIndicator(Context context) {
        super(context);
        initialize(context, null);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                    R.styleable.CircleIndicator2, 0, 0);
            try {
                indicatorColor = typedArray.getColor(R.styleable.CircleIndicator2_indicatorColor2,
                        getResources().getColor(android.R.color.darker_gray));
                indicatorSelectedColor = typedArray.getColor(
                        R.styleable.CircleIndicator2_indicatorSelectedColor2, R.attr.colorAccent);
                indicatorGap = typedArray.getDimensionPixelSize(
                        R.styleable.CircleIndicator2_indicatorGap2, 0);
                indicatorRadius = typedArray.getDimensionPixelSize(
                        R.styleable.CircleIndicator2_indicatorRadius2, 0);
                indicatorItemCount = typedArray.getInt(
                        R.styleable.CircleIndicator2_indicatorItemCount2, 0);
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
                painter = new CirclePainter.SlideAnimationPainter(paint, indicatorColor,
                        indicatorSelectedColor, indicatorRadius, this);
                indicatorAnimation = new SlideAnimation(new IndicatorAnimation.AnimationUpdateListener() {
                    @Override
                    public void onAnimationUpdate(AnimationValue animationValue) {
                        ((CirclePainter.AnimationPainter) painter).setAnimationValue(animationValue);
                        invalidate();
                    }
                });

                break;
            case SCALE:
                painter = new CirclePainter.ScaleAnimationPainter(paint, indicatorColor,
                        indicatorSelectedColor, indicatorRadius, this);
                indicatorAnimation = new ScaleAnimation(indicatorSelectedColor, indicatorColor,
                        indicatorRadius, (int) (indicatorRadius * 0.8f),
                        new IndicatorAnimation.AnimationUpdateListener() {
                            @Override
                            public void onAnimationUpdate(AnimationValue animationValue) {
                                ((CirclePainter.AnimationPainter) painter).setAnimationValue(
                                        animationValue);
                                invalidate();
                            }
                        });
                break;
            case COLOR:
                painter = new CirclePainter.ColorAnimationPainter(paint, indicatorColor,
                        indicatorSelectedColor, indicatorRadius, this);
                indicatorAnimation = new ColorAnimation(indicatorSelectedColor, indicatorColor,
                        new IndicatorAnimation.AnimationUpdateListener() {
                            @Override
                            public void onAnimationUpdate(AnimationValue animationValue) {
                                ((CirclePainter.AnimationPainter) painter).setAnimationValue(
                                        animationValue);
                                invalidate();
                            }
                        });
                break;
            case NONE:
                indicatorAnimation = null;
                painter = new CirclePainter(paint, indicatorColor, indicatorSelectedColor,
                        indicatorRadius, this);
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

    @Override
    protected int measureWidth() {
        return indicatorRadius * 2 * getItemCount() + indicatorGap * (getItemCount() - 1);
    }

    @Override
    protected int measureHeight() {
        return indicatorRadius * 2;
    }

    @Override
    public int getCoordinateX(int position) {
        int coordinate = getPaddingLeft();
        for (int i = 0; i < getItemCount(); i++) {
            coordinate += indicatorRadius;
            if (i == position) {
                return coordinate;
            }
            coordinate += (indicatorRadius + indicatorGap);
        }
        return coordinate;
    }

    @Override
    public int getCoordinateY() {
        return indicatorRadius + getPaddingTop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < getItemCount(); i++) {
            painter.draw(canvas, i);
        }
    }

    public enum AnimationMode {
        SLIDE, SCALE, COLOR, NONE
    }
}

