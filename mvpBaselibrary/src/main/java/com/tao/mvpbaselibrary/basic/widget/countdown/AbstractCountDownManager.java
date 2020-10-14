package com.tao.mvpbaselibrary.basic.widget.countdown;

import android.util.Log;

/**
 * 倒计时管理类，使用的时候new 这个类，start pause,stop
 */
public abstract class AbstractCountDownManager {
    private static final String TAG = "CountDownManager";

    /**
     * 普通的计时器
     */
    private AbstractNewCountDownTimer normalCountDownTimer;
    /**
     * 可暂停的计时器
     */
    private AbstractNewCountDownTimer canPauseCountDownTimer;
    /**
     * 第一次剩余时间
     */
    private long millisInFuture;

    /**
     * 倒计时剩余时间，用于恢复
     */
    private long lastMillisInFuture;
    /**
     * 间隔时间
     */
    private long countDownInterval;
    private boolean pause = true;
    private boolean isRunning = true;


    public boolean isPause() {
        return pause;
    }

    public void setMillisInFuture(long millisInFuture) {
        this.millisInFuture = millisInFuture;
        this.lastMillisInFuture = this.millisInFuture;
    }

    protected AbstractCountDownManager(long millisInFuture, long countDownInterval) {
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
        this.lastMillisInFuture = this.millisInFuture;
    }

    private void createPauseCountDown() {
        if (this.canPauseCountDownTimer != null) {
            this.canPauseCountDownTimer.cancel();
        }

        this.canPauseCountDownTimer = new AbstractNewCountDownTimer(this.lastMillisInFuture, this.countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                AbstractCountDownManager.this.lastMillisInFuture = millisUntilFinished;
                AbstractCountDownManager.this.onCountDownTick(millisUntilFinished);

            }

            @Override
            public void onFinish() {
                AbstractCountDownManager.this.onCountDownFinish();
                AbstractCountDownManager.this.stop();
            }
        };

    }

    private void createNormalCountDown() {
        if (this.normalCountDownTimer != null) {
            this.normalCountDownTimer.cancel();
        }
        this.normalCountDownTimer = new AbstractNewCountDownTimer(this.millisInFuture, this.countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                AbstractCountDownManager.this.lastMillisInFuture = millisUntilFinished;
                AbstractCountDownManager.this.onCountDownTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                AbstractCountDownManager.this.onCountDownFinish();
                AbstractCountDownManager.this.stop();
            }
        };

    }

    public AbstractCountDownManager start() {
        try {
            createNormalCountDown();
            this.normalCountDownTimer.start();
            this.isRunning = true;
            this.pause = false;
            return this;
        } catch (Exception ee) {
            return null;
        }
    }

    public AbstractCountDownManager resume() {
        Log.i(TAG, this + "CountDownManager resume" + pause);
        try {
            if (pause) {
                createPauseCountDown();
                this.canPauseCountDownTimer.start();
                this.pause = false;
                this.isRunning = true;

            }
            return this;

        } catch (Exception ee) {
            return null;
        }
    }

    public void stop() {
        if (!pause) {
            if (normalCountDownTimer != null) {
                normalCountDownTimer.cancel();
            }
            if (canPauseCountDownTimer != null) {
                canPauseCountDownTimer.cancel();
            }
            pause = true;
        }
        this.lastMillisInFuture = 0;
    }

    public void pause() {
        Log.i(TAG, this + "CountDownManager pause" + pause);

        if (!pause) {
            if (normalCountDownTimer != null) {
                normalCountDownTimer.cancel();
            }
            if (canPauseCountDownTimer != null) {
                canPauseCountDownTimer.cancel();
            }
            pause = true;
            this.isRunning = false;
        }


    }

    public abstract void onCountDownTick(long sec);

    public abstract void onCountDownFinish();


}
