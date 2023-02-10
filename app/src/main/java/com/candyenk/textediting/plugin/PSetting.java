package com.candyenk.textediting.plugin;

import android.content.Context;
import android.content.SharedPreferences;
import candyenk.api.textediting.Log;
import candyenk.api.textediting.Setting;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.Set;

/**
 * 插件持久存储的实现
 */
public class PSetting implements Setting {
    public static final String OPENCOUNT = "openCount";//打开次数
    public static final String ERRORTIME = "errorTime";//崩溃次数
    public static final String UPDATETIME = "updateTime";//更新时间
    public static final String INSTALLTIME = "installTime";//安装时间
    private final Log l;
    SharedPreferences sp;

    /**
     * 创建持久存储实例
     */
    static PSetting create(Loader loader) {
        return new PSetting(loader);
    }

    private PSetting(Loader loader) {
        this.l = loader.getLog();
        this.sp = loader.getApp().getSharedPreferences(loader.getConfig().getUuid(), Context.MODE_PRIVATE);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return Double.parseDouble(sp.getString(key, String.valueOf(defaultValue)));
    }

    @Override
    public String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    @Override
    public void setInt(String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    @Override
    public void setLong(String key, long value) {
        sp.edit().putLong(key, value).apply();
    }

    @Override
    public void setFloat(String key, float value) {
        sp.edit().putFloat(key, value).apply();
    }

    @Override
    public void setDouble(String key, double value) {
        sp.edit().putString(key, String.valueOf(value)).apply();
    }

    @Override
    public void setString(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    @Override
    public void setBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    @Override
    public int getOpenCount() {
        return getInt(OPENCOUNT, 0);
    }

    @Override
    public int getErrorCount() {
        return getInt(ERRORTIME, 0);
    }

    @Override
    public long getUpdateTime() {
        return getLong(UPDATETIME, 0);
    }

    @Override
    public long getInstallTime() {
        return getLong(INSTALLTIME, 0);
    }

    @Override
    public Set<String> keySet() {
        return sp.getAll().keySet();
    }

    @Override
    public void clear() {
        sp.edit().clear()
                .putInt(OPENCOUNT, getOpenCount())
                .putInt(ERRORTIME, getErrorCount())
                .putLong(UPDATETIME, getUpdateTime())
                .putLong(INSTALLTIME, getInstallTime()).apply();
    }


    private LuaValue keyValue() {
        LuaTable t = new LuaTable();
        for (String s : keySet()) {
            t.set(s, getString(s, ""));
        }
        return t;
    }
}
