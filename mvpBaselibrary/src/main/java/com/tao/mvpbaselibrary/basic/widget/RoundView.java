package com.tao.mvpbaselibrary.basic.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.tao.mvpbaselibrary.R;


/**
 *
 * @author chengang
 * @date 2017/2/27
 * 圆角的背景View
 */

public class RoundView extends View {
    private Paint paint;
    private int colorBg;
    private float radius;

    private float bottomLeftRadius;
    private float bottomRightRadius;
    private float topLeftRadius;
    private float topRightRadius;

    public RoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public RoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    public RoundView(Context context) {
        super(context);

    }


    private void init(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Basic_RoundView);
        colorBg = a.getColor(R.styleable.Basic_RoundView_basic_bgRound, 0xFF7ED321);
        radius = a.getDimensionPixelSize(R.styleable.Basic_RoundView_basic_radiusRound, 2);
        bottomLeftRadius = a.getDimension(R.styleable.Basic_RoundView_basic_bottomLeftRadius, radius);
        bottomRightRadius = a.getDimension(R.styleable.Basic_RoundView_basic_bottomRightRadius, radius);
        topLeftRadius = a.getDimension(R.styleable.Basic_RoundView_basic_topLeftRadius, radius);
        topRightRadius = a.getDimension(R.styleable.Basic_RoundView_basic_topRightRadius, radius);

       a.recycle();

        setBg();
    }

    private void setBg() {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape( GradientDrawable.RECTANGLE);
        if (radius != 0) {
            gd.setCornerRadius(radius);
        } else {
            //分别表示 左上 右上 右下 左下
            gd.setCornerRadii(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius, bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius});
        }
        gd.setColor(colorBg);
        setBackground(gd);
    }





}
