package com.tao.mvpbaselibrary.app.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.tao.logger.log.Logger;
import com.tao.mvpbaselibrary.basic.manager.AppManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Context mContext;
    private Thread.UncaughtExceptionHandler mHandler;
    // 保存手机信息和异常信息
    private Map<String, String> mMessage = new HashMap<>();
    private boolean needCrashApp = true;
    private OnCrashListener crashListener;

    private CrashHandler() {
    }

    private static CrashHandler sUnCatchExceptionHandler = new CrashHandler();

    public static CrashHandler getExceptionHandler() {
        return sUnCatchExceptionHandler;
    }

    public void init(Context mContext) {
        initLooper();
        this.mContext = mContext.getApplicationContext();
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        Logger.init(mContext);
        Logger.setEnable(true);
        Logger.setPriority(Logger.MIN_LOG_PRIORITY);

    }

    public void setNeedCrashApp(boolean needCrashApp) {
        this.needCrashApp = needCrashApp;
    }

    public void setCrashListener(OnCrashListener crashListener) {
        this.crashListener = crashListener;
    }

    private void initLooper() {
        new Handler(Looper.myLooper()).post(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Looper.loop();
                    } catch (Throwable e) {
                        callOnMainCrash(handlerException(e), e);
                        if (needCrashApp) {
                            finshApp();
                        }
                    }
                }
            }
        });
    }

    private void finshApp() {
        AppManager.getAppManager().finishAllActivity();
//        try {
//            Process.killProcess(Process.myPid());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void callOnMainCrash(String crashPath, Throwable e) {
        Logger.i( "OnMainCrash_detail_from:"+crashPath);

        if (null == crashListener)
            return;
        crashListener.onMainCrash(crashPath, e);

    }

    private void callOnBackgroundCrash(String crashPath, Throwable e) {
        Logger.i( "OnBackgroundCrash_detail_from:"+crashPath);

        if (null == crashListener)
            return;
        crashListener.onBackgroundCrash(crashPath, e);
    }


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        handlerException(t, e, handlerException(e));
    }

    private void handlerException(Thread t, Throwable e, String strCrashLog) {
        if (t == Looper.getMainLooper().getThread()) {
            Logger.e(" err from main");
        } else {
            Logger.e(" err from background");
            callOnBackgroundCrash(strCrashLog, e);
        }
    }

    /**
     * 是否人为捕获异常
     *
     * @param e Throwable
     * @return true:已处理 false:未处理
     */
    private String handlerException(Throwable e) {
        if (e == null) {
            return "";
        }
        collectErrorMessages();
        String s = saveErrorMessages(e);
        return s;
    }

    /**
     * 1.收集错误信息
     */
    private void collectErrorMessages() {
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = TextUtils.isEmpty(pi.versionName) ? "null" : pi.versionName;
                String versionCode = "" + pi.versionCode;
                mMessage.put("versionName", versionName);
                mMessage.put("versionCode", versionCode);
            }
            // 通过反射拿到错误信息
            Field[] fields = Build.class.getFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        mMessage.put(field.getName(), field.get(null).toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 2.保存错误信息
     *
     * @param e Throwable
     */
    private String saveErrorMessages(Throwable e) {
        StringBuilder sb = new StringBuilder();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());

        sb.append("Thread:" + Thread.currentThread().toString() + "\n");
        sb.append("time:" + time + "\n");
        for (Map.Entry<String, String> entry : mMessage.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        e.printStackTrace(pw);
        Throwable cause = e.getCause();
        // 循环取出Cause
        if (cause != null)
            cause.printStackTrace(pw);

        pw.close();
        String result = writer.toString();
        sb.append(result);
        String fileName = "crash-" + time + "-" + System.currentTimeMillis() + ".log";
        // 有无SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            Calendar instance = Calendar.getInstance();
            int month = instance.get(Calendar.MONTH) + 1;
            int yera = instance.get(Calendar.YEAR);
            int day = instance.get(Calendar.DAY_OF_MONTH);

            String path = mContext.getExternalFilesDir(null) + "/log/crash/" + yera + "年/" + month + "月/" + day + "日/";
            File dir = new File(path);
            if (!dir.exists()) dir.mkdirs();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return path + fileName;
        }
        return "";
    }

}
