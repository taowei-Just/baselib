package com.tao.baselib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.tao.mvpbaselibrary.basic.utils.InstallUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    private String apkPath = "/storage/emulated/0/Android/data/com.tao.download/files/download/com.tencent.mtt_10.8.5.8430_10858430.apk";
    public void install(View v){
        InstallUtils.installApk(this ,apkPath);
//      InstallUtils.unInstallApp(this ,"com.tao.baselib");
    }
}