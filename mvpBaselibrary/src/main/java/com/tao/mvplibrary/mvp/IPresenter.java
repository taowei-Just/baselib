package com.tao.mvplibrary.mvp;


/**
 * Created by Administrator on 2019-8-7.
 */

public interface IPresenter<V extends IView>  {
    V getV() throws Exception;

}
