package com.tao.mvplibrary.mvp;
import androidx.lifecycle.LifecycleOwner;

/**
 * Created by Administrator on 2019-8-7.
 */

public interface IView<P extends IPresenter> extends LifecycleOwner {
    P getP() throws Exception;
    P getP(IView view) throws Exception;

}
