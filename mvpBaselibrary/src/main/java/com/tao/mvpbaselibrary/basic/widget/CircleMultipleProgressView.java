package com.tao.mvpbaselibrary.basic.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.tao.mvpbaselibrary.R;


/**
 * @author chengang
 * @date 2019/4/25
 * @describe 可以绘制多个进度的圆形进度条，所有属性用,隔开
 */
public class CircleMultipleProgressView extends View {

    private int[] progressArray;
    private int[] startDegreeArray;
    private int[] directionArray;
    private int[] roundIsNeedArray;
    private float[] strokeWithArray;
    private int[] colorArray;
    private int[] fillArray;
    private RectF[] rectFS;
    private Paint paint;

    //最大进度
    private int max = 100;
    private int maxStrokeWidth = 1;
    //默认方向
    private static final int DIRECTION_DEFAULT = 1;
    //默认宽度
    private static final int STROKE_WITH_DEFAULT = dp2px(1);
    //默认开始角度
    private static final float START_DEGREE_DEFAULT = -90;
    //默认颜色
    private static final int COLOR_DEFAULT = Color.parseColor("#ffffff");
    //圆角默认为1，不为1 就不是圆角

    public CircleMultipleProgressView(Context context) {
        super(context);
    }

    public CircleMultipleProgressView(Context context, @Nullable AttributeSet attrs) throws Exception {
        this(context, attrs, 0);
    }

    public CircleMultipleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) throws Exception {
        super(context, attrs, defStyleAttr);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Basic_CircleMultipleProgressView, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();
    }

    private void initByAttributes(TypedArray attributes) throws Exception {
        max = attributes.getInteger(R.styleable.Basic_CircleMultipleProgressView_basic_circle_multiple_max, 100);
        String progressStr = attributes.getString(R.styleable.Basic_CircleMultipleProgressView_basic_circle_multiple_progress);
        String startDegreeStr = attributes.getString(R.styleable.Basic_CircleMultipleProgressView_basic_circle_multiple_starting_degree);
        String directionStr = attributes.getString(R.styleable.Basic_CircleMultipleProgressView_basic_circle_multiple_direction);
        String roundIsNeedStr = attributes.getString(R.styleable.Basic_CircleMultipleProgressView_basic_circle_multiple_round_is_need);
        String strokeWidthStr = attributes.getString(R.styleable.Basic_CircleMultipleProgressView_basic_circle_multiple_stroke_width);
        String colorStr = attributes.getString(R.styleable.Basic_CircleMultipleProgressView_basic_circle_multiple_color);
        String fillStr = attributes.getString(R.styleable.Basic_CircleMultipleProgressView_basic_circle_multiple_is_fill);
        progressArray = strToIntArray(progressStr);
        startDegreeArray = strToIntArray(startDegreeStr);
        directionArray = strToIntArray(directionStr);
        roundIsNeedArray = strToIntArray(roundIsNeedStr);
        strokeWithArray = strToFloatArray(strokeWidthStr);
        colorArray = strToIntArray(colorStr);
        fillArray = strToIntArray(fillStr);

        if (progressArray == null) {
            throw new Exception("You Need Set Progress");
        }
        check();
        initRectF();
        changeStrokeWidth();


    }

    private void initRectF() {
        rectFS = new RectF[progressArray.length];
        for (int i = 0; i < rectFS.length; i++) {
            rectFS[i] = new RectF();
        }
    }

    private void changeStrokeWidth() {
        if (strokeWithArray != null) {
            float maxWith = 1;
            for (int i = 0; i < strokeWithArray.length; i++) {
                strokeWithArray[i] = dp2px(strokeWithArray[i]);
                if (strokeWithArray[i] > maxWith) {
                    maxWith = strokeWithArray[i];
                }
            }

            maxStrokeWidth = dp2px(maxWith);
        }
    }

    private void check() throws Exception {
        if (startDegreeArray != null && startDegreeArray.length != progressArray.length) {
            throw new Exception("startDegreeArray length is must equal progressArray");
        }
        if (directionArray != null && directionArray.length != progressArray.length) {
            throw new Exception("directionArray length is must equal progressArray");
        }
        if (roundIsNeedArray != null && roundIsNeedArray.length != progressArray.length) {
            throw new Exception("roundIsNeedArray length is must equal progressArray");
        }
        if (strokeWithArray != null && strokeWithArray.length != progressArray.length) {
            throw new Exception("strokeWithArray length is must equal progressArray");
        }
        if (colorArray != null && colorArray.length != progressArray.length) {
            throw new Exception("colorArray length is must equal progressArray");
        }
    }


    private int[] strToIntArray(String str) {
        if (str == null) {
            return null;
        }
        String[] array = str.split(",");
        int[] ints = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i].startsWith("#")) {
                ints[i] = Color.parseColor(array[i]);
            } else {
                ints[i] = Integer.parseInt(array[i]);
            }

        }
        return ints;

    }

    private float[] strToFloatArray(String str) {
        if (str == null) {
            return null;
        }
        String[] array = str.split(",");
        float[] ints = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            ints[i] = Float.parseFloat(array[i]);

        }
        return ints;

    }

    public void setMax(int max) {
        this.max = max;
        invalidate();
    }

    public void setProgressArray(int... progress) {
        if (progress == null) {
            return;
        }
        if (progress.length != this.progressArray.length) {
            Log.e("TAG", "You set progress length must equals last");
            return;
        }
        this.progressArray = progress;
        invalidate();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint == null) {
            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);

        }


        for (int i = 0; i < progressArray.length; i++) {
            if (colorArray != null) {
                paint.setColor(colorArray[i]);
            } else {
                paint.setColor(COLOR_DEFAULT);
            }
            if (fillArray != null) {
                paint.setStyle(fillArray[i] == 1 ? Paint.Style.FILL : Paint.Style.STROKE);
            }

            if (strokeWithArray != null) {
                paint.setStrokeWidth(strokeWithArray[i]);
            } else {
                paint.setStrokeWidth(STROKE_WITH_DEFAULT);
            }
            if (roundIsNeedArray != null) {
                if (roundIsNeedArray[i] == 1) {
                    paint.setStrokeCap(Paint.Cap.ROUND);
                } else {
                    paint.setStrokeCap(Paint.Cap.SQUARE);
                }
            }
            int dir = DIRECTION_DEFAULT;
            if (directionArray != null) {
                dir = directionArray[i];
            }

            float startAngle = START_DEGREE_DEFAULT;
            if (startDegreeArray != null) {
                startAngle = (float) startDegreeArray[i];
            }
            rectFS[i].set((float) maxStrokeWidth / 2, (float) maxStrokeWidth / 2,
                    getWidth() - (float) (maxStrokeWidth / 2), getHeight() - (float) (maxStrokeWidth / 2));

            canvas.drawArc(rectFS[i], startAngle, (float) (dir * 360 * progressArray[i] / max), fillArray[i] == 1, paint);

        }

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue 虚拟像素
     * @return 像素
     */
    public static int dp2px(float dpValue) {
        return (int) (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, Resources.getSystem().getDisplayMetrics());
    }


}
