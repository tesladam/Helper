package com.tesladam.helper;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jama.carouselview.CarouselView;

public class SliderView extends CarouselView {
    public SliderView(@NonNull Context context) {
        super(context);
    }

    public SliderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
