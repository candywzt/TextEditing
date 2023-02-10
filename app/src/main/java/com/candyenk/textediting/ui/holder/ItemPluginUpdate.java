package com.candyenk.textediting.ui.holder;

import android.view.View;
import android.widget.LinearLayout;
import candyenk.android.tools.V;
import candyenk.android.tools.VB;
import candyenk.api.textediting.Config;
import candyenk.java.utils.UData;
import candyenk.java.utils.UTime;
import com.candyenk.textediting.R;
import com.candyenk.textediting.plugin.Loader;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;

/**
 * 插件更新弹窗
 */
public class ItemPluginUpdate extends VB {
    private final Config c1, c2;
    private final File f1, f2;
    private LinearLayout parent;

    public ItemPluginUpdate(Loader l1, Loader l2) {
        this.c1 = l1.getConfig();
        this.c2 = l2.getConfig();
        this.f1 = l1.getFile();
        this.f2 = l2.getFile();
    }

    @Override
    public void bindContent(View view) {
        V.RL(view).size(-1, -2).refresh();
        this.parent = (LinearLayout) view;
        id(R.id.icon_old, ICON).setImageDrawable(c1.getIcon());
        id(R.id.icon_new, ICON).setImageDrawable(c2.getIcon());
        add(R.string.plugin_name, c1.getTitle(), c2.getTitle());
        add(R.string.plugin_version, c1.getVersion(), c2.getVersion());
        addTV(string(R.string.plugin_uuid) + c1.getUuid());
        add(R.string.plugin_author, c1.getAuthor(), c2.getAuthor());
        add(R.string.plugin_api, c1.getAPI(), c2.getAPI());
        add(R.string.plugin_size, UData.B2A(f1.length()), UData.B2A(f2.length()));
        add(R.string.plugin_describe, c1.getDescribe(), c2.getDescribe());
        add(R.string.plugin_create_time, UTime.D2S(c1.getCreateTime()), UTime.D2S(c2.getCreateTime()));
        add(R.string.plugin_update_time, UTime.D2S(c1.getUpdateTime()), UTime.D2S(c2.getUpdateTime()));
    }

    private void add(int id, Object o, Object n) {
        String str = string(id);
        if (o.equals(n)) addTV(str + o);
        else updateTV(str + o, str + n);
    }

    private void updateTV(String o, String n) {
        V.LL(new MaterialTextView(context)).size(-1, -2).textColorRes(R.color.md_red_500).text(o).parent(parent);
        V.LL(new MaterialTextView(context)).size(-1, -2).textColorRes(R.color.md_green_500).text(n).parent(parent);
    }

    private void addTV(String str) {
        V.LL(new MaterialTextView(context)).size(-1, -2).textColorRes(R.color.text_main).text(str).parent(parent);
    }
}
