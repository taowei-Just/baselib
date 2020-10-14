package com.tao.mvplibrary.mvp.base;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.tao.mvplibrary.mvp.IBaseView;
import com.tao.mvplibrary.mvp.IView;

import java.lang.reflect.ParameterizedType;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2019-8-7.
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements LifecycleOwner, IBaseView<P> {
    P mPresenter;
    public View mView;
    public Context mcContext;
    private Unbinder bind;
    private IView attachView;
    private LifecycleRegistry         lifecycleRegistry = new LifecycleRegistry(this);
    

    public static <T extends BaseFragment> T getInstance(Class<T> tClass) throws Exception {
        return tClass.newInstance();
    }

    public View mContextView = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        try {
            attachView = getAttachView();
            getP().attachView(attachView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (null == mContextView) {
            View mView = bindView();
            if (null == mView) {
                mContextView = inflater.inflate(getLayoutId(), container, false);
            } else {
                mContextView = mView;
            }
            initSomethingAfterBindView();
            initView(mContextView);
        }
        return mContextView;
    }

    private void initSomethingAfterBindView() {
        
    }

    protected View bindView() {
        return null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = ButterKnife.bind(this, view);
        mView = view;
        mcContext = view.getContext();
        initView(mContextView);
    }

    @Override
    public abstract int getLayoutId();

    @Override
    public void initView(View mContextView) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void initData() {

    }

    public IView getAttachView() {
        return this;
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

    @Override
    public void onPause() {
        super.onPause();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        if (null != mPresenter)
            mPresenter.dettachView();
    }


    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }
}
