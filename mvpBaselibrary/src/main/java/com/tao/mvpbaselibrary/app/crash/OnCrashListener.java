package com.tao.mvpbaselibrary.app.crash;

public interface OnCrashListener {
    void onMainCrash(String strException, Throwable e);

    void onBackgroundCrash(String strCrashLog, Throwable e);
}
