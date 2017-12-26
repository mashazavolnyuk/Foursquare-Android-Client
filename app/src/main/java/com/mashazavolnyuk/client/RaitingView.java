package com.mashazavolnyuk.client;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

public class RaitingView extends TextView {

    Paint paint;
    Path path;

    public RaitingView(Context context) {
        super(context);

        init();
    }

    public RaitingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        if (!isInEditMode()) {
            //createTypeface(context, attrs); //whatever added functionality you are trying to add to Widget, call that inside this condition.
        }
    }

    public RaitingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {

        }
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        final RectF rect = new RectF();
        paint.setStyle(Paint.Style.STROKE);

        rect.set(50, 50, 150, 150);
        canvas.drawRoundRect(rect, 10, 10, paint);

        rect.set(200, 150, 450, 350);
        canvas.drawRoundRect(rect, 30, 30, paint);

        paint.setStyle(Paint.Style.FILL);
        rect.set(200, 400, 450, 600);
        canvas.drawRoundRect(rect, 50, 100, paint);

    }
}
