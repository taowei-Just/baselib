package com.tao.mvpbaselibrary.basic.widget.attrtab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;


import androidx.core.content.ContextCompat;

import com.tao.mvpbaselibrary.R;


/**
 * Project Name: test
 * Package Name: com.cg.test
 * Author: SmileChen
 * Created on: 2016/10/17
 * Description: 三角形
 *
 * @author chengang
 */
public class TriangleView extends View {
    private int color;
    private int direction;
    Paint paint;
    Path path;


   public static enum DIR{
        TOP(1),
        BOTTOM(3),
        LEFT(0),
        RIGHT(2),
        LEFT_TOP(7),
        LEFT_BOTTOM(6),
        RIGHT_TOP(5),
        RIGHT_BOTTOM(4);

        private int value = 0;

        private DIR(int value) {
            this.value = value;
        }
    }




    public TriangleView(Context context) {
        super(context);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TriangleView);
        color = a.getColor(R.styleable.TriangleView_draw_color, Color.WHITE);
        direction = a.getInt(R.styleable.TriangleView_direction, 0);
        a.recycle();
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDirection(DIR direction) {
        this.direction = direction.value;
        invalidate();
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    public void setColorRes(int colorRes) {
        this.color = ContextCompat.getColor(getContext(),colorRes);
        invalidate();
    }


    private void initPaint() {
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (path == null) {
            path = new Path();
        }
        path.reset();
        initPaint();
        paint.setColor(color);

        switch (direction) {
            default:
                break;
            //方向朝左
            case 0:
                // 此点为多边形的起点
                path.moveTo(0, getHeight() / 2);
                path.lineTo(getWidth(), 0);
                path.lineTo(getWidth(), getHeight());
                break;
            //方向朝上
            case 1:
                // 此点为多边形的起点
                path.moveTo(getWidth() / 2, 0);
                path.lineTo(0, getHeight());
                path.lineTo(getWidth(), getHeight());

                break;
            //方向朝右
            case 2:
                // 此点为多边形的起点
                path.moveTo(getWidth(), getHeight() / 2);
                path.lineTo(0, 0);
                path.lineTo(0, getHeight());
                break;
            //方向朝下
            case 3:
                // 此点为多边形的起点
                path.moveTo(getWidth() / 2, getHeight());
                path.lineTo(0, 0);
                path.lineTo(getWidth(), 0);
                break;
            case 4:
                // 右下
                path.moveTo(getWidth(), getHeight());
                path.lineTo(0, getHeight());
                path.lineTo(getWidth(), 0);
                break;

            case 5:
                // 右上
                path.moveTo(getWidth(), 0);
                path.lineTo(0, 0);
                path.lineTo(getWidth(), getHeight());
                break;
            case 6:
                // leftbottom  （0，h）
                path.moveTo(0, getHeight());
                path.lineTo(0, 0);
                path.lineTo(getWidth(), getHeight());
                break;
            case 7:
                // lefttop() (0,0)
                path.moveTo(0, 0);
                path.lineTo(0, getHeight());
                path.lineTo(getWidth(), 0);
                break;
        }


//
//           <enum name="rightbottom" value="4"/>
//            <enum name="righttop" value="5"/>
//            <enum name="leftbottom" value="6"/>
//            <enum name="lefttop" value="7"/>


        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, paint);
    }
}
