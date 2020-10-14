package com.tao.mvpbaselibrary.basic.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.tao.mvpbaselibrary.basic.manager.AppManager;
import com.tao.mvpbaselibrary.basic.utils.StatusBarUtil;
import androidx.core.content.ContextCompat;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * @author chengang
 * @date 2019-05-31
 * @email chenganghonor@gmail.com
 * @QQ 1410488687
 * @package_name com.nj.baijiayun.basic.ui
 * @describe
 */
public abstract class BaseActivity extends SupportActivity {


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        setScreenOrientation();
        super.onCreate(savedInstanceState);
        initParams();
        AppManager.getAppManager().addActivity(this);
        initAppStatusBar();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().popActivity(this);
    }


    public void setScreenOrientation() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected abstract int bindLayoutRes();

    protected void initParams() {
    }

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void registerListener();

    protected abstract void processLogic(Bundle savedInstanceState);

    public BaseActivity getActivity() {
        return this;
    }

    public void initAppStatusBar() {
        setStatusBar();
    }


    public void setStatusBar() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, android.R.color.white), 0);
    }

    public void setTranslucentBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
    }


}
