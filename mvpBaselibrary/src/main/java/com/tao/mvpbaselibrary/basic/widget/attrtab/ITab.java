package com.tao.mvpbaselibrary.basic.widget.attrtab;

import android.content.Context;
import android.view.View;

/**
 * @author chengang
 * @date 2020-02-26
 * @email chenganghonor@gmail.com
 * @QQ 1410488687
 * @package_name com.nj.baijiayun.myapplication.tab
 * @describe
 */
public interface ITab {
    //显示选中
    void select(boolean isSelect);

    //tab的ui
    View createTabView(Context context);

    //tab 是否可以重复选
    boolean isRepeat();

    View createContent(Context context);

    void viewCreate();

    View getContentView();

    //仅仅给内部调用的
     void setTabCloseCallBack(TabOnIndicatorCloseCallBack tabAttrCallBack);

    int weight();

    View getTabView();

    void setGravity(int gravity);



}
