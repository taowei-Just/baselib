package com.tao.mvplibrary.mvp;


import android.view.View;

/**
 * Created by Administrator on 2019-8-7.
 */

public interface IBaseView< P extends IPresenter> extends IView<P> {

    int getLayoutId();
    void initView(View mContextView);
    void initData();

}
