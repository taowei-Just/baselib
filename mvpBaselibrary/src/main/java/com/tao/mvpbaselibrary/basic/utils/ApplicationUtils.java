package com.tao.mvpbaselibrary.basic.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ApplicationUtils {

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager.getRunningAppProcesses() == null) {
            return null;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }


    public static long getFirstInstallTimeInSecond(Context context) {
        try {
            final PackageManager pm = context.getPackageManager();
            final PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.firstInstallTime >= 1000000000000L ? pi.firstInstallTime / 1000L : pi.firstInstallTime;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String encodeString(String content) {
        String result = null;

        try {
            result = URLEncoder.encode(content, "utf-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
        }

        return result;
    }

    public static long getLastUpdateTimeInSecond(Context context) {
        try {
            final PackageManager pm = context.getPackageManager();
            final PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.lastUpdateTime >= 1000000000000L ? pi.lastUpdateTime / 1000L : pi.lastUpdateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 包名
     *
     * @return
     */
    public String getPackageName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo != null) {
                return packageInfo.packageName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 版本名
     *
     * @return
     */
    public static String getVersionName(Context context) {

        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionName;
        }
        return "";
    }
 /**
     * 版本名
     *
     * @return
     */
    public static String getAppName(Context context) {

        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            int label= packageInfo.applicationInfo.labelRes;
            return context.getString(label);
        }
        return "";
    }

    /**
     * 版本号
     *
     * @return
     */
    public static long getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    return packageInfo.getLongVersionCode();
                } else {
                    return packageInfo.versionCode;
                }

            }
        } catch (Exception ignored) {
        }
        return 0;
    }

    /**
     * 获取安装时间
     */
    public long getFirstInstallTime(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.firstInstallTime;
        }
        return 0;
    }


    public static boolean isAppInstalled(Context ctx, String packageName) {
        try {
            return ctx.getPackageManager().getPackageInfo(packageName, 0) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNewUser(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return true;
        }
        return pi != null && pi.firstInstallTime == pi.lastUpdateTime;

    }

    public static String visitURL(String url, String format) {
        String urlDate = null;
        try {
            URL url1 = new URL(url);
            //生成连接对象
            URLConnection conn = url1.openConnection();
            //连接对象网页
            conn.connect();
            //获取对象网址时间
            Date date = new Date(conn.getDate());
            //设置日期格式
            SimpleDateFormat df = new SimpleDateFormat(format);
            urlDate = df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlDate;
    }


}
