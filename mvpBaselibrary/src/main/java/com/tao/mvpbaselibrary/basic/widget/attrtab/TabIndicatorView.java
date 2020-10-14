package com.tao.mvpbaselibrary.basic.widget.attrtab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author chengang
 * @date 2020-02-26
 * @email chenganghonor@gmail.com
 * @QQ 1410488687
 * @package_name com.nj.baijiayun.myapplication
 * @describe
 */
public class TabIndicatorView extends LinearLayout {
    private ITab[] tabs;
    private int mLastSelectIndex = -1;
    LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);

    public TabIndicatorView(Context context) {
        this(context, null);
    }

    public TabIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabIndicatorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addTabs(ITab[] iTabs) {
        this.tabs = iTabs;
        for (int i = 0; i < this.tabs.length; i++) {
            View view = this.tabs[i].createTabView(this.getContext());
            params.weight = this.tabs[i].weight();
            view.setLayoutParams(params);
            addView(view);
            setTabListener(i, view);
        }
    }

    private void setTabListener(int i, View view) {
        view.setTag(i);
        tabs[i].setTabCloseCallBack(() -> closeNotRepeatTab());
        view.setOnClickListener(v -> select((int) v.getTag()));
    }

    private void closeNotRepeatTab() {
        if (getCurrentSelectTab() != null && !getCurrentSelectTab().isRepeat()) {
            getCurrentSelectTab().select(false);
            setAllUnSelect();
        }
    }


    private ITab getCurrentSelectTab() {
        if (isHasSelectedOne()) {
            return tabs[mLastSelectIndex];
        }
        return null;
    }

    public void select(int index) {
        if (tabs == null) {
            return;
        }

        if (index > -1) {
            if (tabs[index].getContentView() == null) {
                tabs[index].createContent(getContext());
                tabs[index].viewCreate();
            }
        }
        //再次选中自己
        if (index == mLastSelectIndex) {
            //自己是有再次可选中状态
            if (tabs[index].isRepeat()) {
                selectIndex(index);
            } else {
                //再次选自己就释放自己
                tabs[index].select(false);
                setAllUnSelect();
            }

        } else {
            //如果选中的上一个 不是可重复选的 需要设置为未选中
            if (isHasSelectedOne() && !tabs[mLastSelectIndex].isRepeat()) {
                tabs[mLastSelectIndex].select(false);
            }
            //选中当前的
            selectIndex(index);
        }

    }

    private boolean isHasSelectedOne() {
        return mLastSelectIndex > -1;
    }

    private void setAllUnSelect() {
        mLastSelectIndex = -1;
    }

    private void selectIndex(int index) {
        if (index < 0) {
            return;
        }
        tabs[index].select(true);
        mLastSelectIndex = index;

    }



}
