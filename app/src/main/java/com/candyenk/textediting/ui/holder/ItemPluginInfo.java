package com.candyenk.textediting.ui.holder;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import candyenk.android.tools.V;
import candyenk.android.tools.VB;
import candyenk.android.view.LabelView;
import candyenk.api.textediting.Config;
import candyenk.api.textediting.Setting;
import candyenk.api.textediting.Tool;
import candyenk.java.utils.UData;
import candyenk.java.utils.UTime;
import com.candyenk.textediting.R;
import com.candyenk.textediting.plugin.Loader;
import com.candyenk.textediting.plugin.PM;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;

/**
 * 插件信息弹窗
 */
public class ItemPluginInfo extends VB {
    private LinearLayout parent;
    private final Loader l;
    private final Config c;
    private final Tool t;
    private final Setting s;


    public ItemPluginInfo(String uuid) {
        this.l = PM.getLoader(uuid);
        c = l.getConfig();
        t = l.getTool();
        s = l.getSetting();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindContent(View view) {
        V.RL(view).size(-1, -1).refresh();
        this.parent = (LinearLayout) view;
        if (l == null) return;
        id(R.id.icon, ICON).setImageDrawable(c.getIcon());
        addLable(R.string.plugin_basic_info);
        addTV(R.string.plugin_name, c.getTitle());
        addTV(R.string.plugin_version, c.getVersion());
        addTV(R.string.plugin_author, c.getAuthor());
        addTV(R.string.plugin_uuid, c.getUuid());
        addTV(R.string.plugin_describe, c.getDescribe());
        addTV(R.string.plugin_create_time, UTime.D2S(c.getCreateTime()));
        addTV(R.string.plugin_update_time, UTime.D2S(c.getUpdateTime()));
        addTV(R.string.plugin_api, c.getAPI());
        addTV(R.string.plugin_size, UData.B2A(l.getFile().length()));
        addLable(R.string.plugin_run_info);
        addTV(R.string.plugin_cache_size, UData.B2A(new File(t.getCachePath()).length()));
        addTV(R.string.plugin_files_size, UData.B2A(new File(t.getFilesPath()).length()));
        addTV(R.string.plugin_firstinstall, UTime.D2S(s.getInstallTime()));
        addTV(R.string.plugin_lastupdate, UTime.D2S(s.getUpdateTime()));
        addTV(R.string.plugin_opencount, s.getOpenCount());
        addTV(R.string.plugin_errorcount, s.getErrorCount());
    }

    private void addLable(int id) {
        V.LL(new LabelView(context)).size(-1, -2).gravity(Gravity.CENTER_HORIZONTAL).text(id).parent(parent);
    }

    private void addTV(int id, Object o) {
        V.LL(new MaterialTextView(context)).size(-1, -2).textColorRes(R.color.text_main).text(string(id) + o).parent(parent);
    }
}
