package com.tao.mvplibrary.mvp.base;

import android.app.ActionBar;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.tao.mvpbaselibrary.basic.manager.AppManager;
import com.tao.mvpbaselibrary.basic.utils.StatusBarUtil;
import com.tao.mvplibrary.mvp.IBaseView;
import com.tao.mvplibrary.mvp.IView;

import java.lang.reflect.ParameterizedType;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by Administrator on 2019-8-7.
 */

public abstract class BaseActivity<P extends BasePresenter> extends SupportActivity implements IBaseView<P> {

    private P mPresenter;
    private Unbinder bind;
    private IView attachView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            attachView = getAttachView();
            getP().attachView(attachView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        beforeCreate();
        super.onCreate(savedInstanceState);
        beforeSetContentView();
        setContentView(getLayoutId());
        bind = ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        initAppStatusBar();
        initView(getWindow().getDecorView());
        initData();
       
    }

    public void beforeCreate() {
        setScreenOrientation();
    }
    public void setScreenOrientation() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    public void initAppStatusBar() {
    
    }


    public void setStatusBar(int color ) {
        StatusBarUtil.setColor(this, color, 0);
    }

    public void setTranslucentBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
    }


    /**
     * 在用于设的contentview之前
     * 多用于窗口的设置
     */
    public void beforeSetContentView() { 
    }

    @Override
    public  abstract int getLayoutId();

    @Override
    public void initView(View mContextView) {

    }

    /**
     * 初始化数据
     */
    public void initData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mPresenter)
            mPresenter.dettachView();
        AppManager.getAppManager().popActivity(this);

    }

    @Override
    public P getP(IView v) throws Exception {
        if (v == null)
            return getP();
        P presenter = getP();
        if (presenter != null && !presenter.isAttachedV())
            presenter.attachView(v);
        return presenter;
    }


    @Override
    public P getP() throws Exception {
        if (mPresenter == null) {
            //实例化P层，类似于p = new P();
            ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
            Class<P> clazz = (Class<P>) parameterizedType.getActualTypeArguments()[0];
            mPresenter = clazz.newInstance();
        }
        if (mPresenter != null) {
            if (!mPresenter.isAttachedV() && !mPresenter.isDeattachV()) {
                mPresenter.attachView(this);
            }
        }
        return mPresenter;
    }

    protected IView getAttachView() {
        return this;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View ve = getCurrentFocus();
            if (isShouldHideKeyboard(ve, ev)) {
                boolean res = hideKeyboard(this, ve.getWindowToken());
            }
        }

        return super.dispatchTouchEvent(ev);
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            // 点击EditText的事件，忽略它。
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * 最好使用post隐藏
     *
     * @param token 焦点view的token
     */
    private static boolean hideKeyboard(Context context, IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            return im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return false;
    }

    public void noTitle(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    
    // 去掉默认的actionbar
    public void noActionBar() {

        ActionBar actionBar = getActionBar();
        if (actionBar != null)
            actionBar.hide();
        androidx.appcompat.app.ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.hide();
    }

    // 全屏
    public void fullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
 

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setSystemUiVisibility();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        
    }

    /**
     * 
     * 
     * 操作系统状态栏
     */
    
    public void setSystemUiVisibility() {
      
       
    }

    
}
