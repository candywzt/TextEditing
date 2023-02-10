package com.candyenk.textediting.plugin;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.LayoutInflater;
import candyenk.android.tools.L;
import com.candyenk.textediting.APP;
import com.candyenk.textediting.R;

import java.io.File;

/**
 * 插件所使用的的上下文
 * 以便插件可以使用R资源(划掉)
 */
public class PContext extends ContextWrapper {
    private static final L.Loger l = APP.l;
    private final File file;//插件包
    private Resources r;//插件资源
    private LayoutInflater li;

    public PContext(Context context, Loader loader) {
        super(context);
        this.file = loader.getFile();
        try {
            this.r = createResources();
            l.d("Java插件(" + file.getName() + ")资源功能载入成功");
        } catch (Exception e) {
            this.r = l.d("Java插件(" + file.getName() + ")未载入资源功能,将不能使用Content及Resources", null);
        }
    }

    @Override
    public Resources getResources() {return r == null ? super.getResources() : r;}

    @Override
    public AssetManager getAssets() {
        return r.getAssets();
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (li == null) li = LayoutInflater.from(getBaseContext()).cloneInContext(this);
            return li;
        }
        return getBaseContext().getSystemService(name);
    }

    /*** 创建插件Resources ***/
    private Resources createResources() throws Exception {
        PackageManager pm = getPackageManager();
        PackageInfo pi = pm.getPackageArchiveInfo(file.getAbsolutePath(),
                PackageManager.GET_ACTIVITIES
                        | PackageManager.GET_META_DATA
                        | PackageManager.GET_SERVICES
                        | PackageManager.GET_PROVIDERS);
        pi.applicationInfo.publicSourceDir = file.getAbsolutePath();
        pi.applicationInfo.sourceDir = file.getAbsolutePath();
        Resources r = pm.getResourcesForApplication(pi.applicationInfo);
        r.newTheme().applyStyle(R.style.Theme_CDK, true);
        return r;
    }

}
