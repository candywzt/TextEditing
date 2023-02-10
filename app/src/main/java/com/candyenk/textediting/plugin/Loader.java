package com.candyenk.textediting.plugin;

import android.content.Context;
import candyenk.api.textediting.API;
import candyenk.api.textediting.Plugin;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.ResourceFinder;

import java.io.File;
import java.util.zip.ZipFile;

/**
 * 插件加载器接口
 */
public interface Loader extends API {
    /**
     * 准备插件(宿主Activity 或 Server)
     */
    boolean ready(Context context);

    /**
     * 插件是否初始化完成
     */
    boolean isOK();

    /**
     * 插件是否已准备
     */
    boolean isReady();

    /**
     * 加载插件
     */
    boolean load();

    /**
     * 释放插件
     */
    void release();

    /**
     * 删除插件
     */
    void delete();

    /**
     * 删除插件包
     */
    void deleteFile();

    /**
     * 获取Lua全局变量
     */
    Globals getLua();

    /**
     * 获取资源查找器
     */
    ResourceFinder getRF();

    /**
     * 获取插件File实例
     */
    File getFile();

    /**
     * 获取插件ZIP实例
     */
    ZipFile getZIP();

    /**
     * 获取Plugin实例
     */
    Plugin getPlugin();

    /**
     * 获取AppContext(Activity)
     */
    Context getApp();

    /**
     * 获取插件Context
     */
    PContext getContext();

    /**
     * 增加一次启动次数
     */
    void addOpenCount();

    /**
     * 增加一次崩溃次数
     */
    void addErrorCount();

    /**
     * 修改安装时间
     */
    void setInstallTime(long time);

    /**
     * 修改更新时间
     */
    void setUpdateTime(long time);


}
