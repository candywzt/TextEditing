package com.candyenk.textediting.plugin;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import candyenk.android.tools.L;
import candyenk.api.textediting.Config;
import candyenk.java.io.IO;
import com.candyenk.textediting.APP;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 插件配置信息
 * API版本:001
 */

public class PConfig implements Config {
    private static final L.Loger l = APP.l;
    private Loader loader;
    /******/
    private String uuid = "";//插件唯一ID
    private String icon;//插件图标地址
    private String title = "未知脚本";//插件标题
    private String describe = "这个脚本话不多";//插件描述
    private int version;//插件版本
    private long createTime;//插件创建时间
    private long updateTime;//插件最近更新时间
    private String author = "神秘的作者";//作者
    private String authorUrl = "";//作者链接
    private String updateUrl = "";//更新链接
    private String mainClass = "";//Java插件入口
    private String mainLua = "";//Lua插件入口
    private int api;//支持的最低API版本


    /**
     * Json创建配置实例
     */
    static PConfig createJson(Loader loader) {
        ZipFile zip = loader.getZIP();
        ZipEntry ze = zip.getEntry("config.json");
        if (ze == null) {
            l.e("插件包(" + loader.getFile().getName() + ")Json配置文件不存在");
            return null;
        }
        try {
            String string = IO.readString(zip.getInputStream(ze));
            PConfig c = PM.gson.fromJson(string, PConfig.class);
            c.loader = loader;
            return c;
        } catch (Exception e) {
            l.e(e, "插件包(" + loader.getFile().getName() + ")Json配置文件读取异常");
            return null;
        }
    }

    @Override
    public String getUuid() {
        return uuid;
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public Drawable getIcon() {
        return new BitmapDrawable(null, loader.getRF().findResource(icon));
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescribe() {
        return describe;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public long getCreateTime() {
        return createTime;
    }

    @Override
    public long getUpdateTime() {
        return updateTime;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getAuthorUrl() {
        return authorUrl;
    }

    @Override
    public String getUpdateUrl() {
        return updateUrl;
    }

    @Override
    public String getMainClass() {
        return mainClass;
    }

    @Override
    public String getMainLua() {
        return mainLua;
    }

    @Override
    public int getAPI() {
        return api;
    }
}
