package com.tao.mvplibrary.mvp.base;

import com.tao.mvplibrary.mvp.IModle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public abstract class BaseModle<P extends BasePresenter> implements IModle<P> {
    P mPresenter;

    public BaseModle(P basePresenter) {
        this.mPresenter = basePresenter;
        EventBus.getDefault().register(this);
    }

    @Override
    public P getP() {
        return mPresenter;
    }

    @Subscribe
    public void sub(String[] args) {
    }

    @Override
    public void deattach() {
        EventBus.getDefault().unregister(this);
    }
}
