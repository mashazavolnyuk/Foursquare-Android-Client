package com.mashazavolnyuk.client;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class RatingView extends AppCompatTextView {



    public RatingView(Context context) {
        super(context);

    }

    public void setBackgroundShapeColor(int color) {
        Drawable background;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            background = getContext().getResources().getDrawable(R.drawable.ic_rating_style_bg, getContext().getTheme());
        } else {
            background = getContext().getResources().getDrawable(R.drawable.ic_rating_style_bg);
        }
        GradientDrawable gradientDrawable = (GradientDrawable) background;
        gradientDrawable.setColor(color);
        setBackground(gradientDrawable);
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
