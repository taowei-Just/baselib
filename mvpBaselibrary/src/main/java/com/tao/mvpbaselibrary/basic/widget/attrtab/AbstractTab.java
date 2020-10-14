package com.tao.mvpbaselibrary.basic.widget.attrtab;

import android.content.Context;
import android.view.View;

/**
 * @author chengang
 * @date 2020-02-26
 * @email chenganghonor@gmail.com
 * @QQ 1410488687
 * @package_name com.nj.baijiayun.myapplication
 * @describe
 */
public abstract class AbstractTab implements ITab {
    TabOnIndicatorCloseCallBack tabCloseCallBack;
    protected TabSelectCallBack tabSelectCallBack;

    @Override
    public int weight() {
        return 1;
    }

    @Override
    public void setTabCloseCallBack(TabOnIndicatorCloseCallBack tabAttrCallBack) {
        this.tabCloseCallBack = tabAttrCallBack;
    }

    public TabOnIndicatorCloseCallBack getTabCloseCallBack() {
        return tabCloseCallBack;
    }

    @Override
    public View createContent(Context context) {
        return null;
    }

    @Override
    public void select(boolean isSelect) {
        if (getTabSelectCallBack() != null) {
            getTabSelectCallBack().call(isSelect);
        }
    }

    @Override
    public View getContentView() {
        return null;
    }

    public void setTabSelectCallBack(TabSelectCallBack tabSelectCallBack) {
        this.tabSelectCallBack = tabSelectCallBack;
    }

    public TabSelectCallBack getTabSelectCallBack() {
        return tabSelectCallBack;
    }
}
