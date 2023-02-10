package com.candyenk.textediting.plugin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import candyenk.android.utils.UFile;
import candyenk.android.utils.UHttp;
import candyenk.api.textediting.Log;
import candyenk.api.textediting.Tool;
import candyenk.java.io.IO;
import dalvik.system.DexClassLoader;
import org.luaj.vm2.LuaValue;

import java.io.File;
import java.io.InputStream;

public class PTool implements Tool {
    private final Loader loader;
    private final Log l;
    private final File cache, files;

    /**
     * 创建工具集实例
     */
    static PTool create(Loader loader) {
        return new PTool(loader);
    }

    private PTool(Loader loader) {
        this.loader = loader;
        String uuid = loader.getConfig().getUuid();
        String cache = UFile.getCachePath(loader.getApp());
        String files = UFile.getFilesPath(loader.getApp());
        this.cache = new File(cache + "/plugin/" + uuid);
        this.files = new File(files + "/plugin/" + uuid);
        this.l = loader.getLog();
    }

    @Override
    public void toast(CharSequence text) {
        Toast.makeText(loader.getApp(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public InputStream getResource(String fileName) {
        return loader.getRF().findResource(fileName);
    }

    @Override
    public String getText(String path) {
        return IO.readString(getResource(path));
    }

    @Override
    public Drawable getDrawable(String path) {
        if (TextUtils.isEmpty(path)) {
            l.e("图片路径为空");
            return null;
        }
        InputStream is = getResource(path);
        if (is == null) {
            l.e("插件(" + loader.getConfig().getTitle() + ")图片资源获取失败:" + path);
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return new BitmapDrawable(loader.getApp().getResources(), bitmap);
    }

    @Override
    public String getCachePath() {
        if (!cache.exists()) cache.mkdirs();
        return cache.getAbsolutePath();
    }

    @Override
    public String getFilesPath() {
        if (!files.exists()) files.mkdirs();
        return files.getAbsolutePath();
    }

    @Override
    public File getCacheFile(String path) {
        return new File(cache, path);
    }

    @Override
    public File getFilesFile(String path) {
        return new File(files, path);
    }

    @Override
    public <T extends View> T getLayout(int id) {
        return (T) LayoutInflater.from(loader.getContext()).inflate(id, null);
    }

    @Override
    public LuaValue getLua(String script) {
        return loader.getLua().load(script);
    }

    @Override
    public LuaValue getLua(File file) {
        return loader.getLua().loadfile(file.getPath());
    }

    @Override
    public ClassLoader getClassLoader(String dexpath) {
        try {
            File dexFile = getCacheFile(dexpath);
            dexFile.delete();
            UFile.createFile(dexFile);
            UFile.writeStream(dexFile, getResource(dexpath));
            return new DexClassLoader(dexFile.getAbsolutePath(), getCachePath(), null, loader.getApp().getClassLoader());
        } catch (Exception e) {
            l.e(e, "dex文件加载异常");
            return null;
        }
    }

    @Override
    public void getHttp(String url, HttpCallBack callBack) {
        UHttp.get(url, (candyenk.java.utils.UHttp.StringCallBack) callBack::callback);
    }

    @Override
    public void postHttp(String url, String params, HttpCallBack callBack) {
        UHttp.post(url, params, (candyenk.java.utils.UHttp.StringCallBack) callBack::callback);
    }
}
