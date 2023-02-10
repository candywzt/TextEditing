package com.candyenk.textediting.plugin;

import android.content.Context;
import candyenk.android.tools.L;
import candyenk.android.utils.UFile;
import candyenk.api.textediting.*;
import com.candyenk.textediting.APP;
import dalvik.system.DexClassLoader;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ResourceFinder;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 插件加载器
 */
public class PLoader extends TwoArgFunction implements API, Loader, ResourceFinder {
    /*************************************静态变量**************************************************/
    private static final L.Loger l = APP.l;
    /*************************************成员变量**************************************************/
    private final File pkgFile;//插件包文件
    private final ZipFile zip;//插件zip对象
    private final PConfig config;//插件配置
    private final PLog log;//插件日志
    private final PTool tool;//插件工具集
    private final PSetting setting;//插件持久存储
    private final PPanel panel;//插件操作面板
    private final Plugin plugin;//插件本体
    private boolean isOk;//插件初始化完成
    private boolean isReady;//插件准备完成
    private Globals lua;//lua插件全局变量
    private PContext context;//插件上下文

    /**********************************************************************************************/
    /*************************************构造方法**************************************************/
    /**********************************************************************************************/
    PLoader(File file) {
        this.pkgFile = file;
        this.zip = createZipFile();//解包
        this.config = createConfig();//解析配置
        this.log = createLog();//加载日志
        this.tool = createTool();//加载工具
        this.setting = createSetting();//加载持久存储
        this.panel = createPanel();//加载面板
        this.plugin = createPlugin();//加载插件
        if (this.plugin != null) this.isOk = true;
    }
    /**********************************************************************************************/
    /*************************************继承方法**************************************************/
    /**********************************************************************************************/
    @Override
    public int getAPIVersion() {
        return 1;
    }

    @Override
    public Edit getInput() {
        return PM.inEdit;
    }

    @Override
    public Edit getOutPut() {
        return PM.outEdit;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public Panel getPanel() {
        return panel;
    }

    @Override
    public Setting getSetting() {
        return setting;
    }

    @Override
    public Tool getTool() {
        return tool;
    }

    @Override
    public void close() {
        release();
    }

    @Override
    public boolean ready(Context context) {
        if (this.context == null) this.context = new PContext(context, this);
        return true;
    }

    @Override
    public boolean isOK() {
        return isOk;
    }

    @Override
    public boolean isReady() {
        return isReady;
    }

    @Override
    public boolean load() {
        if (PM.RUN != null) {
            getTool().toast("插件(" + PM.RUN.getConfig().getTitle() + ")正在运行!\n期间无法运行其他插件");
            return false;
        }
        l.i("插件(" + config.getTitle() + ")被运行");
        addOpenCount();
        try {
            PM.RUN = this;
            if (!isReady) {
                this.plugin.onReady(this);
                isReady = true;
            }
            this.plugin.onReset();
            this.plugin.onLoad();
            return true;
        } catch (Throwable e) {
            l.e(e, "插件(" + config.getTitle() + ")运行异常");
            addOpenCount();
            release();
            return false;
        }
    }


    @Override
    public void release() {
        if (PM.RUN == this) l.i("关闭插件(" + config.getTitle() + ")");
        PM.RUN = null;
    }

    @Override
    public void delete() {
        release();
        l.i("删除插件(" + config.getTitle() + ")");
        UFile.deleteFile(pkgFile, new File(tool.getFilesPath()));
    }

    @Override
    public void deleteFile() {
        UFile.deleteFile(pkgFile);
    }

    @Override
    public Globals getLua() {return lua == null ? initLuaEnv() : lua;}


    @Override
    public ResourceFinder getRF() {
        return this;
    }

    @Override
    public File getFile() {
        return pkgFile;
    }

    @Override
    public ZipFile getZIP() {
        return zip;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public Context getApp() {
        return PM.context == null ? PM.app : PM.context;
    }

    @Override
    public PContext getContext() {
        return context;
    }

    @Override
    public void addOpenCount() {
        setting.setInt(PSetting.OPENCOUNT, setting.getOpenCount() + 1);
    }

    @Override
    public void addErrorCount() {
        setting.setInt(PSetting.ERRORTIME, setting.getErrorCount() + 1);
    }

    @Override
    public void setInstallTime(long time) {
        setting.setLong(PSetting.INSTALLTIME, time);
    }

    @Override
    public void setUpdateTime(long time) {
        setting.setLong(PSetting.UPDATETIME, time);
    }

    @Override
    public InputStream findResource(String filename) {
        try {
            ZipEntry entry = zip.getEntry(filename);
            if (entry == null) {
                l.e("文件(" + filename + ")不在插件包(" + pkgFile.getName() + ")内");
                return null;
            } else {
                return zip.getInputStream(entry);
            }
        } catch (Exception e) {
            l.e(e, "文件(" + filename + ")未找到");
            return null;
        }
    }

    @Override
    public LuaValue call(LuaValue v, LuaValue env) {
        LuaValue lv = tableOf();
        lv.set("version", LuaValue.valueOf(getAPIVersion()));
        env.set("API", lv);
        env.get("package").get("loaded").set("API", this);
        return lv;
    }


    /**********************************************************************************************/
    /*************************************公共方法**************************************************/
    /**********************************************************************************************/


    /**********************************************************************************************/
    /*************************************私有方法**************************************************/
    /**********************************************************************************************/
    /*** 初始化Lua环境 ***/
    private Globals initLuaEnv() {
        this.lua = JsePlatform.standardGlobals();
        this.lua.finder = this;
        this.lua.load(this);
        return this.lua;
    }

    /*** 创建ZIPFile实例 ***/
    private ZipFile createZipFile() {
        if (UFile.isEmpty(pkgFile)) return null;
        try {
            return new ZipFile(pkgFile);
        } catch (Exception e) {
            l.e(e, "插件包(" + pkgFile.getName() + ")格式异常");
            return null;
        }
    }

    /*** 创建插件配置实例 ***/
    private PConfig createConfig() {
        if (zip == null) return null;
        PConfig config = PConfig.createJson(this);
        if (config == null) {
            l.e("插件包(" + pkgFile.getName() + ")读取失败,跳过");
        } else if (config.getAPI() > PM.APIV) {
            l.e("插件包(" + pkgFile.getName() + ")所需API版本(" + config.getAPI() + ")过高,跳过");
        } else if (config.getUuid().isEmpty()) {
            l.e("插件包(" + pkgFile.getName() + ")uuid不存在,跳过");
        } else if (config.getMainClass().isEmpty() && config.getMainLua().isEmpty()) {
            l.e("插件包(" + pkgFile.getName() + ")入口不存在,跳过");
        } else {
            return config;
        }
        return null;
    }

    /*** 创建日志实例 ***/
    private PLog createLog() {
        if (config == null) return null;
        return PLog.create(this);
    }

    /*** 创建插件实例 ***/
    private Plugin createPlugin() {
        if (config == null) return null;
        if (!config.getMainClass().isEmpty()) {
            try {
                DexClassLoader cl = new DexClassLoader(pkgFile.getAbsolutePath(), PM.cache, null, PM.app.getClassLoader());
                Class<?> c = cl.loadClass(config.getMainClass());
                return (Plugin) c.getConstructor().newInstance();
            } catch (Exception e) {
                l.e(e, "Java插件(" + config.getTitle() + ")载入失败");
                return null;
            }
        } else {
            if (lua == null) initLuaEnv();
            return LuaPlugin.create(this);
        }
    }

    /*** 创建面板实例 ***/
    private PPanel createPanel() {
        if (config == null) return null;
        return PPanel.create(this);
    }

    /*** 创建持久存储实例 ***/
    private PSetting createSetting() {
        if (config == null) return null;
        return PSetting.create(this);
    }

    /*** 创建插件工具集实例 ***/
    private PTool createTool() {
        if (config == null) return null;
        return PTool.create(this);
    }
}
