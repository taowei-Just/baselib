package com.tao.mvpbaselibrary.basic.widget.countdown;

/**
 * @author chengang
 * @date 2019-06-24
 * @email chenganghonor@gmail.com
 * @QQ 1410488687
 * @package_name com.nj.baijiayun.basic.widget.countdown
 * @describe
 */
public class CountDownHelper {

    private static final int MIN_IN_SEC = 60;
    private static final int HOUR_IN_SEC = MIN_IN_SEC * 60;

    public static String formatDuration(long ms) {
        int duration = (int) (ms / 1000);

        final int s = duration;
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
        StringBuilder stringBuilder = new StringBuilder("");
        if (h < 10) {
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
}
