package com.candyenk.textediting.plugin;

import candyenk.android.tools.L;
import candyenk.api.textediting.Log;
import com.candyenk.textediting.APP;
import com.candyenk.textediting.ui.PageSetting;

import java.util.HashMap;
import java.util.Map;

/**
 * 我真聪明,hhh
 */
public class PLog extends L.Loger implements Log {
    private static final Map<String, PLog> map = new HashMap<>();//以防万一,弄个表

    static PLog create(Loader loader) {
        String uuid = loader.getConfig().getUuid();
        PLog pl = map.get(uuid);
        if (pl == null) pl = new PLog(L.getInstance(), uuid);
        return pl;
    }

    private PLog(L l, String tag) {
        super(l, tag);
        setTargetLevel(APP.getSetting().getInt(PageSetting.ITEM_LEVEL, L.INFO));
    }
}
