package com.tao.mvpbaselibrary.basic.widget;

import android.text.format.DateUtils;


/**
 * @author 
 * @date 
 * @describe 可实现倒计时，计时就用系统默认的
 */
public class XChronometer {


    private static final int MIN_IN_SEC = 60;
    private static final int HOUR_IN_SEC = MIN_IN_SEC * 60;

    public static String formatDuration(long ms) {
        int duration = (int) (ms / DateUtils.SECOND_IN_MILLIS);
        if (duration < 0) {
            duration = -duration;
        }

        int h = 0;
        int m = 0;

        if (duration >= HOUR_IN_SEC) {
            h = duration / HOUR_IN_SEC;
            duration -= h * HOUR_IN_SEC;
        }
        if (duration >= MIN_IN_SEC) {
            m = duration / MIN_IN_SEC;
            duration -= m * MIN_IN_SEC;
        }
        final int s = duration;

        StringBuilder stringBuilder = new StringBuilder("");


        if (h <10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(h).append(":");

        if (m < 10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(m).append(":");

        if (s < 10) {
            stringBuilder.append("0");

        }
        stringBuilder.append(s);

        return stringBuilder.toString();

    }

    public static void main(String[] args) {
        System.err.println(formatDuration(System.currentTimeMillis()));
        
    }


}
