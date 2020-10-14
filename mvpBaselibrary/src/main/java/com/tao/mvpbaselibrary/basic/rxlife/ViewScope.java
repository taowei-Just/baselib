package com.tao.mvpbaselibrary.basic.rxlife;

import android.os.Build;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;

import io.reactivex.disposables.Disposable;

/**
 * View的作用域
 * User: ljx
 * Date: 2019-05-26
 * Time: 18:17
 */
public final class ViewScope implements Scope, OnAttachStateChangeListener {

    private final View view;
    private Disposable disposable;

    private ViewScope(View view) {
        this.view = view;
    }

    static ViewScope from(View view) {
        return new ViewScope(view);
    }

    @Override
    public void onScopeStart(Disposable d) {
        disposable = d;
        final View view = this.view;
        if (view == null)
            throw new NullPointerException("view is null");
        boolean isAttached = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && view.isAttachedToWindow())
            || view.getWindowToken() != null;
        if (!isAttached)
            throw new OutsideScopeException("View is not attached!");
        view.addOnAttachStateChangeListener(this);
    }

    @Override
    public void onScopeEnd() {
        final View view = this.view;
        if (view == null) return;
        view.removeOnAttachStateChangeListener(this);
    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        disposable.dispose();
        v.removeOnAttachStateChangeListener(this);
    }
}
