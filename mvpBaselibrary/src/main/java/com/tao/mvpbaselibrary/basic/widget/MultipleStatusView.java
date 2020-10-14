package com.tao.mvpbaselibrary.basic.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tao.mvpbaselibrary.R;


/**
 * 多状态的ViewGroup
 */

@SuppressWarnings("unused")
public class MultipleStatusView extends LinearLayout {
    private static final String TAG = "MultipleStatusView";

    private static final RelativeLayout.LayoutParams DEFAULT_LAYOUT_PARAMS =
            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);

    public static final int STATUS_CONTENT = 0x00;
    public static final int STATUS_LOADING = 0x01;
    public static final int STATUS_EMPTY = 0x02;
    public static final int STATUS_ERROR = 0x03;
    public static final int STATUS_NO_NETWORK = 0x04;
    public static final int STATUS_NO_LOGIN = 0x05;
    private static final int NULL_RESOURCE_ID = -1;

    private View mEmptyView;
    private View mErrorView;
    private View mLoadingView;
    private View mNoNetworkView;
    private View mContentView;
    private View mNoLoginView;
    private int mEmptyViewResId;
    private int mErrorViewResId;
    private int mLoadingViewResId;
    private int mNoNetworkViewResId;
    private int mContentViewResId;
    private int mContentViewId;
    private int mNoLoginViewResId;
    private int mViewStatus;
    private LayoutInflater mInflater;
    private OnClickListener mOnRetryClickListener;

    public MultipleStatusView(Context context) {
        this(context, null);
    }

    public MultipleStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Basic_MultipleStatusView, defStyleAttr, 0);
        mEmptyViewResId = a.getResourceId(R.styleable.Basic_MultipleStatusView_basic_emptyView, R.layout.basic_status_empty_view);
        mErrorViewResId = a.getResourceId(R.styleable.Basic_MultipleStatusView_basic_errorView, R.layout.basic_status_error_view);
        mLoadingViewResId = a.getResourceId(R.styleable.Basic_MultipleStatusView_basic_loadingView, R.layout.basic_status_loading_view);
        mNoNetworkViewResId = a.getResourceId(R.styleable.Basic_MultipleStatusView_basic_noNetworkView, R.layout.basic_status_no_network_view);
        mContentViewResId = a.getResourceId(R.styleable.Basic_MultipleStatusView_basic_contentView, NULL_RESOURCE_ID);
        mNoLoginViewResId = a.getResourceId(R.styleable.Basic_MultipleStatusView_basic_noLoginView, R.layout.basic_status_nologin_view);
        a.recycle();
        mInflater = LayoutInflater.from(getContext());
        if (null == mContentView && mContentViewResId != NULL_RESOURCE_ID) {
            mContentView = mInflater.inflate(mContentViewResId, null);
            addView(mContentView, 0, DEFAULT_LAYOUT_PARAMS);
            this.mContentViewId = mContentView.getId();
        }
    }

    public void setContentViewResId(int contentViewResId) {
        if (contentViewResId == mContentViewResId) {
            return;
        }
        if (contentViewResId != NULL_RESOURCE_ID) {
            if (null != mContentView) {
                final int childCount = getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View view = getChildAt(i);
                    if (view.getId() == contentViewResId) {
                        removeView(view);
                        break;
                    }
                }
            }
            mContentView = mInflater.inflate(contentViewResId, null);
            addView(mContentView, 0, DEFAULT_LAYOUT_PARAMS);
            this.mContentViewId = mContentView.getId();
            this.mContentViewResId = contentViewResId;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (getContext() instanceof Activity) {
            if (((Activity) getContext()).isFinishing()) {
                clear(mEmptyView, mLoadingView, mErrorView, mNoNetworkView, mNoLoginView);
                if (null != mOnRetryClickListener) {
                    mOnRetryClickListener = null;
                }
                mInflater = null;
            }
        }
    }

    /**
     * 获取当前状态
     */
    public int getViewStatus() {
        return mViewStatus;
    }

    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    public void setOnRetryClickListener(OnClickListener onRetryClickListener) {
        this.mOnRetryClickListener = onRetryClickListener;
    }

    /**
     * 显示空视图
     */
    public final void showEmpty() {
        showEmpty(mEmptyViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示未登录页面
     */
    public final void showNoLogin() {
        showNoLogin(mNoLoginViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示空视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showNoLogin(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showNoLogin(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示空视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    public final void showNoLogin(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "Empty view is null!");
        mViewStatus = STATUS_NO_LOGIN;
        if (null == mNoLoginView) {
            mNoLoginView = view;

            View emptyRetryView = mNoLoginView.findViewById(R.id.nologin);
            if (null != mOnRetryClickListener && null != emptyRetryView) {
                emptyRetryView.setOnClickListener(mOnRetryClickListener);
            }
            mNoLoginView.setId(STATUS_NO_LOGIN);
            addView(mNoLoginView, 0, layoutParams);
        }
        showViewById(mNoLoginView.getId());
    }

    /**
     * 显示空视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showEmpty(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showEmpty(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示空视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    public final void showEmpty(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "Empty com.baijiayun.zywx.module_public.view is null!");
        mViewStatus = STATUS_EMPTY;
        if (null == mEmptyView) {
            mEmptyView = view;
            View emptyRetryView = mEmptyView.findViewById(R.id.empty_retry_view);
            if (null != mOnRetryClickListener && null != emptyRetryView) {
                emptyRetryView.setOnClickListener(mOnRetryClickListener);
            }
            mEmptyView.setId(STATUS_EMPTY);
            addView(mEmptyView, 0, layoutParams);
        }
        showViewById(mEmptyView.getId());
    }

    /**
     * 显示错误视图
     */
    public final void showError() {
        showError(mErrorViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示错误视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showError(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showError(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示错误视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    public final void showError(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "Error com.baijiayun.zywx.module_public.view is null!");
        mViewStatus = STATUS_ERROR;
        if (null == mErrorView) {
            mErrorView = view;
            View errorRetryView = mErrorView.findViewById(R.id.error_retry_view);
            if (null != mOnRetryClickListener && null != errorRetryView) {
                errorRetryView.setOnClickListener(mOnRetryClickListener);
            }
            mErrorView.setId(STATUS_ERROR);
            addView(mErrorView, 0, layoutParams);
        }
        showViewById(mErrorView.getId());
    }

    /**
     * 显示加载中视图
     */
    public final void showLoading() {
        showLoading(mLoadingViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示加载中视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showLoading(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showLoading(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示加载中视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    public final void showLoading(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "Loading com.baijiayun.zywx.module_public.view is null!");
        mViewStatus = STATUS_LOADING;
        if (null == mLoadingView) {
            mLoadingView = view;
            addView(mLoadingView, 0, layoutParams);
            mLoadingView.setId(STATUS_LOADING);
        }
        showViewById(mLoadingView.getId());
    }

    /**
     * 显示无网络视图
     */
    public final void showNoNetwork() {
        showNoNetwork(mNoNetworkViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示无网络视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showNoNetwork(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showNoNetwork(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示无网络视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    public final void showNoNetwork(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "No network com.baijiayun.zywx.module_public.view is null!");
        mViewStatus = STATUS_NO_NETWORK;
        if (null == mNoNetworkView) {
            mNoNetworkView = view;
            View noNetworkRetryView = mNoNetworkView.findViewById(R.id.no_network_retry_view);
            if (null != mOnRetryClickListener && null != noNetworkRetryView) {
                noNetworkRetryView.setOnClickListener(mOnRetryClickListener);
            }
            mNoNetworkView.setId(STATUS_NO_NETWORK);
            addView(mNoNetworkView, 0, layoutParams);

        }
        showViewById(mNoNetworkView.getId());
    }

    /**
     * 显示内容视图
     */
    public final void showContent() {
        mViewStatus = STATUS_CONTENT;
        showViewById(mContentViewId);
    }

    private View inflateView(int layoutId) {
        return mInflater.inflate(layoutId, null);
    }

    private void showViewById(int viewId) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            view.setVisibility(view.getId() == viewId ? View.VISIBLE : View.GONE);
        }
    }

    private void checkNull(Object object, String hint) {
        if (null == object) {
            throw new NullPointerException(hint);
        }
    }

    private void clear(View... views) {
        if (null == views) {
            return;
        }
        try {
            for (View view : views) {
                if (null != view) {
                    removeView(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public View getNoLoginView() {
        return mNoLoginView;
    }

    public void setNoLoginViewResId(int mNoLoginViewResId) {
        this.mNoLoginViewResId = mNoLoginViewResId;
    }

    public void setEmptyViewResId(int emptyViewResId) {
        this.mEmptyViewResId = emptyViewResId;
    }

    public View getEmptyView() {
        return mEmptyView;
    }
}