package com.candyenk.textediting;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import candyenk.android.asbc.ApplicationCDK;
import candyenk.android.tools.L;
import com.candyenk.textediting.plugin.PM;
import com.candyenk.textediting.ui.PageSetting;

import java.io.File;


public class APP extends ApplicationCDK {
    public static final String[] noCache = {"files", "shared_prefs"};//缓存清理白名单
    @SuppressLint("StaticFieldLeak")
    public static APP app;
    public static String TAG;
    public static L.Loger l;
    @SuppressLint("StaticFieldLeak")
    private static SharedPreferences setting;

    @Override
    public void onCreate() {
        super.onCreate();
        APP.app = this;
        APP.TAG = super.TAG;
        initLog();
        initPlugin();

    }

    public static SharedPreferences getSetting() {
        if (setting == null) setting = app.getSharedPreferences("setting", MODE_PRIVATE);
        return setting;
    }

    private void initLog() {
        L.initialize(new File(getFilesDir(), "Log"), 102400, 10);
        APP.l = L.getInstance().getLoger(TAG).setTargetLevel(getSetting().getInt(PageSetting.ITEM_LEVEL, L.INFO));
        l.i("应用已启动");
    }


    private void initPlugin() {
        PM.initPlugin(this);
    }
}
