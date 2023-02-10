package com.candyenk.textediting.plugin;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import candyenk.android.tools.L;
import candyenk.android.utils.UFile;
import candyenk.android.utils.UMime;
import candyenk.api.textediting.Config;
import com.candyenk.textediting.APP;
import com.google.gson.Gson;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 插件管理器
 */
public class PM {
    private static final L.Loger l = APP.l;
    public static final String[] mime = UMime.ps("zip", "apk");//插件支持的后缀
    public static final List<String> sysList = new ArrayList<>();//系统插件列表
    public static final List<String> pluList = new ArrayList<>();//用户插件列表

    public static String cache;//插件缓存目录
    public static String sysPath;//系统插件目录
    public static String userPath;//用户插件目录
    private static final Map<String, Loader> pluginMap = new HashMap<>();//已加载的插件Map
    static Loader RUN;//当前正在运行的插件
    static final int APIV = 1;//当前API版本
    static final Gson gson = new Gson();//Gson
    static Application app;//宿主AppLication
    @SuppressLint("StaticFieldLeak")
    static Context context;//宿主Activity 或 Server
    @SuppressLint("StaticFieldLeak")
    static PEdit inEdit;
    @SuppressLint("StaticFieldLeak")
    static PEdit outEdit;

    /**
     * 初始化插件加载器
     */
    public static void initPlugin(Application app) {
        PM.app = app;
        PM.cache = app.getFilesDir().getAbsolutePath() + "/plugin";
        PM.sysPath = UFile.getFilesPath(app) + "/SystemPlugin";
        PM.userPath = UFile.getEFilesPath(app) + "/plugin";
        PM.userPath = APP.getSetting().getString("userPath", PM.userPath);
        //清空插件缓存区
        UFile.deleteFile(new File(cache), new File(sysPath, "oat"), new File(userPath, "oat"));
        copy(app);
        boolean b = list(sysPath, sysList);
        if (!b) l.e("系统插件加载失败");
        b = list(userPath, pluList);
        if (!b) l.e("用户插件加载失败");
        l.i("插件加载结束,共计加载插件数量:" + (sysList.size() + pluList.size()));
    }

    /**
     * 设置Activity 或 Service
     */
    public static void setContext(Context context) {
        PM.context = context;
    }

    /**
     * 设置插件操作文本框
     */
    public static void setEditText(EditText in, EditText out) {
        inEdit = PEdit.create(in);
        outEdit = PEdit.create(out);
    }

    /**
     * 清空插件列表
     */
    public static void clearAll(List<String> list) {
        for (String uuid : list) {
            Loader l = pluginMap.remove(uuid);
            if (l != null) l.release();
        }
        list.clear();
    }

    /**
     * 获取插件UUID列表
     */
    public static List<String> getUUIDList() {
        List<String> list = new ArrayList<>();
        list.addAll(sysList);
        list.addAll(pluList);
        return list;
    }

    /**
     * 加载插件
     */
    public static boolean load(String uuid) {
        Loader loader = getLoader(uuid);
        if (!loader.ready(context)) return false;
        return loader.load();
    }

    /**
     * 移除插件(User)
     *
     * @return 如果UUID不存在
     */
    public static boolean removePlugin(String uuid) {
        if (!pluList.remove(uuid)) return false;
        Loader l = pluginMap.remove(uuid);
        if (l != null) l.delete();
        return true;
    }

    /**
     * 获取插件加载器
     */
    public static Loader getLoader(String uuid) {
        Loader loader = pluginMap.get(uuid);
        if (loader == null) L.e("插件管理器", "UUID(" + uuid + ")不存在");
        return loader;
    }


    /**
     * 获取插件Config
     */
    public static Config getConfig(String uuid) {
        return getLoader(uuid).getConfig();
    }

    /**
     * 创建插件加载器
     */
    public static Loader createLoader(String name, InputStream in) {
        File file = new File(userPath, name);
        UFile.createFile(file);
        UFile.writeStream(file, in);
        return new PLoader(file);
    }

    /**
     * 检查插件
     * null:初始化失败
     * ==loader:通过
     * !=loader:重复
     */
    public static Loader checkLoader(Loader loader) {
        if (loader == null || !loader.isOK()) return null;
        Loader l = pluginMap.get(loader.getConfig().getUuid());
        return l == null ? loader : l;
    }

    /**
     * 检查插件后缀
     */
    public static boolean checkType(String name) {
        return name.endsWith(".apk") || name.endsWith(".zip");
    }

    /**
     * 添加插件到用户插件列表
     * 将覆盖相同UUID的插件
     * 返回true
     */
    public static void addPlugin(Loader loader) {
        addPlugin(loader, pluList);
    }

    /*** 复制系统插件 ***/
    private static void copy(Context context) {
        String[] list = {};
        try {
            list = context.getAssets().list("SystemPlugin");
        } catch (Exception e) {
            l.e(e, "读取Assets插件列表失败");
        }
        for (String s : list) {
            File file = new File(new File(context.getFilesDir(), "SystemPlugin"), s);
            UFile.deleteFile(file);
            UFile.createFile(file);
            try {
                InputStream is = context.getAssets().open("SystemPlugin/" + s);
                if (!UFile.writeStream(file, is)) l.e("插件包(" + s + ")复制失败");
            } catch (Exception e) {
                l.e(e, "插件包(" + s + ")复制失败");
            }
        }
    }

    /*** 初始化插件列表 ***/
    private static boolean list(String path, List<String> list) {
        if (TextUtils.isEmpty(path)) return false;
        File file = new File(path);
        UFile.createFolder(file);
        clearAll(list);
        File[] files = file.listFiles();
        if (files == null || files.length == 0) return true;
        for (File f : files) {
            if (!f.isFile()) continue;
            if (!f.getName().endsWith(".zip") && !f.getName().endsWith(".apk")) continue;
            PLoader loader = new PLoader(f);
            Loader l = checkLoader(loader);
            if (l == null) PM.l.e("插件包(" + f.getName() + ")初始化失败");
            else if (l == loader) addPlugin(loader, list);
            else {
                String t1 = loader.getConfig().getTitle();
                String t2 = l.getConfig().getTitle();
                PM.l.e("插件(" + t1 + ")与(" + t2 + ")初始化重复???");
            }
        }
        return true;
    }

    /*** 添加插件到指定列表,返回true ***/
    private static void addPlugin(Loader loader, List<String> list) {
        Config c = loader.getConfig();
        pluginMap.put(c.getUuid(), loader);
        if (!list.contains(loader.getConfig().getUuid())) list.add(c.getUuid());
    }
}
