package com.tao.mvpbaselibrary.basic.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.tao.mvpbaselibrary.R;


/**
 * @project zywx_android
 * @class name：com.baijiayun.baselib.widget.dialog
 * @describe
 * @anthor houyi QQ:1007362137
 * @time 18/12/4 下午2:36
 * @change
 * @time
 * @describe
 */
public class CommonDialog extends Dialog {

    private float widthScale = 0.8f;
    private float heightScale = 0.5f;

    public CommonDialog(@NonNull Context context) {
        this(context, R.style.BasicCommonDialog);
    }

    public CommonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public CommonDialog setCustomView(@LayoutRes int layoutIds) {
        return setCustomView(View.inflate(getContext(), layoutIds, null));
    }

    public CommonDialog setCustomView(View view) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().addContentView(view, params);
        return this;
    }

    public CommonDialog setWidthScale(float withScale) {
        this.widthScale = withScale;
        return this;
    }

    public CommonDialog setMaxHeightScale(float heightScale) {
        this.heightScale = heightScale;
        return this;
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (display.getWidth() * widthScale);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int height = display.getHeight();
        if (needHeightWrapContent) {
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {

            if (metrics.heightPixels < getMaxHeight(height)) {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                params.height = getMaxHeight(height);

            }
        }
        window.setAttributes(params);
    }

    private boolean needHeightWrapContent = false;

    public CommonDialog setHeightWrapContent() {
        needHeightWrapContent = true;
        return this;
    }

    private int getMaxHeight(int fullHeight) {
        return (int) (fullHeight * heightScale);
    }

}