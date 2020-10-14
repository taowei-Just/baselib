package com.tao.mvpbaselibrary.basic.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author chengang
 * @date 2019-05-31
 * @email chenganghonor@gmail.com
 * @QQ 1410488687
 * @package_name com.nj.baijiayun.basic.ui
 * @describe
 */
public abstract class BaseFragment extends SupportFragment {
    public View mContextView = null;

    @Override
    public void onAttach(Context context) {
        initParms(getArguments());
        super.onAttach(context);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == mContextView) {
            View mView = bindView();
            if (null == mView) {
                mContextView = inflater.inflate(bindLayout(), container, false);
            } else {
                mContextView = mView;
            }
            initSomethingAfterBindView();
            initView(mContextView);
        }
        return mContextView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public View getConvertView() {
        return mContextView;
    }

    protected abstract void initSomethingAfterBindView();

    protected abstract int bindLayout();

    protected abstract void initView(View mContextView);


    public View bindView() {
        return null;
    }

    public abstract void registerListener();

    public abstract void processLogic();

    public void initParms(Bundle params) {
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mContextView != null && mContextView.getParent() != null) {
            ((ViewGroup) mContextView.getParent()).removeView(mContextView);
        }
        mContextView=null;

    }


}
