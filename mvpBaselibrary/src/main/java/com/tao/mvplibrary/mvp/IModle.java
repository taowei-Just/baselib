package com.tao.mvplibrary.mvp;


public interface IModle<P extends IPresenter> {
    P getP();
    void deattach();
}
