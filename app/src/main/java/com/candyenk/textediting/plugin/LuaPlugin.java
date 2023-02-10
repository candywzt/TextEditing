package com.candyenk.textediting.plugin;

import candyenk.api.textediting.API;
import candyenk.api.textediting.Log;
import candyenk.api.textediting.Plugin;
import org.luaj.vm2.Globals;

/**
 * Lua插件的Java包装
 */
public class LuaPlugin implements Plugin {
    private final Log l;
    private Loader loader;
    private Globals lua;

    /**
     * 创建Lua插件实例
     */
    static Plugin create(Loader loader) {
        return new LuaPlugin(loader);
    }

    private LuaPlugin(Loader loader) {
        this.loader = loader;
        this.l = loader.getLog();
        this.lua = loader.getLua();
    }


    @Override
    public void onReady(API api) {

    }

    @Override
    public void onLoad() throws Throwable {
        lua.loadfile(loader.getConfig().getMainLua()).call();
    }

    @Override
    public void onReset() {

    }
}
