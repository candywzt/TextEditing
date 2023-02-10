package com.candyenk.textediting.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import candyenk.android.asbc.AdapterRVCDK;
import candyenk.android.asbc.HolderCDK;
import candyenk.android.tools.L;
import candyenk.android.tools.V;
import candyenk.android.utils.ULay;
import candyenk.android.widget.Popup;
import candyenk.api.textediting.Config;
import com.candyenk.textediting.APP;
import com.candyenk.textediting.R;
import com.candyenk.textediting.plugin.Loader;
import com.candyenk.textediting.plugin.PM;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class PagePlugin extends AdapterRVCDK<PagePlugin.PageHolder> {
    private static final L.Loger l = APP.l;
    @SuppressLint("StaticFieldLeak")
    private static PagePlugin pas, pac;
    private static GridLayoutManager gms, gmc;

    private final RecyclerView rv;
    private final Context context;
    private final int[] sign = {0, 0, 0, 0};//列数-增底索引-项目外边距-项目直径
    private final List<String> list;

    /**
     * 创建系统插件页面
     */
    static View createSystem(LayoutInflater li, ViewGroup parent, int count) {
        View view = li.inflate(R.layout.page_plugin, parent, false);
        RecyclerView rv = view.findViewById(R.id.list_view);
        pas = new PagePlugin(rv, count, PM.sysList);
        gms = new GridLayoutManager(li.getContext(), count);
        rv.setAdapter(pas);
        rv.setLayoutManager(gms);
        return view;
    }

    /**
     * 创建用户插件页面
     */
    static View createCusto(LayoutInflater li, ViewGroup parent, int count) {
        View view = li.inflate(R.layout.page_plugin, parent, false);
        RecyclerView rv = view.findViewById(R.id.list_view);
        pac = new PagePlugin(rv, count, PM.pluList);
        gmc = new GridLayoutManager(li.getContext(), count);
        rv.setAdapter(pac);
        rv.setLayoutManager(gmc);
        return view;
    }

    /**
     * 更新列数
     */
    @SuppressLint("NotifyDataSetChanged")
    static void updateCount(int count) {
        pas.sign[0] = count;
        pac.sign[0] = count;
        gms.setSpanCount(count);
        gmc.setSpanCount(count);
        updateList();
    }

    /**
     * 更新列表
     */
    @SuppressLint("NotifyDataSetChanged")
    static void updateList() {
        pas.updateData();
        pac.updateData();
    }

    private PagePlugin(RecyclerView rv, int count, List<String> list) {
        this.rv = rv;
        this.context = rv.getContext();
        this.sign[0] = count;
        this.list = list;
        this.setOnClickListener(rv, this::openPlugin);
        this.setOnLongClickListener(rv, this::openDescribe);
        updateData();
    }


    /**
     * 0:正常项目
     * -1:置顶项目
     * 1:置底项目
     */
    @Override
    public int getType(int p) {
        return p < sign[0] ? -1 : p < sign[1] ? 0 : 1;
    }

    @Override
    public PageHolder onCreate(ViewGroup p, int t) {
        @SuppressLint("InflateParams")
        View layout = LayoutInflater.from(context).inflate(R.layout.item_plugincard, null);
        return new PageHolder(layout, t, sign);
    }

    @Override
    public void onBind(@NotNull PageHolder h, int p) {
        Config config = PM.getLoader(list.get(p)).getConfig();
        h.setIcon(config.getIcon());
        h.setName(config.getTitle());
        h.update();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    /*** 更新卡片大小 ***/
    @SuppressLint("NotifyDataSetChanged")
    private void updateData() {
        int ic = getItemCount();
        this.sign[1] = sign[0] > ic ? 0 : ic % sign[0] == 0 ? (ic - sign[0]) : (ic - (ic % sign[0]));
        this.sign[2] = (int) (ULay.dp2px(context, 25 - 4 * sign[0]) + 5);
        int width = ULay.getWidth(context);
        this.sign[3] = width / sign[0] - 2 * sign[2];
        notifyDataSetChanged();
        V.getParent(V.getParent(rv).findViewById(R.id.add)).setVisibility(getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }

    /*** 启动插件 ***/
    private void openPlugin(PageHolder h) {
        if (h == null) return;
        PM.load(list.get(h.getAdapterPosition()));
    }

    /*** 打开描述 ***/
    private void openDescribe(PageHolder h) {
        if (h == null) return;
        String uuid = list.get(h.getAdapterPosition());
        Loader loader = PM.getLoader(uuid);
        loader.release();
        Popup p = new Popup(h.itemView);
        p.setContent(loader.getConfig().getDescribe());
        p.show();
    }

    /*** PluginList布局器 ***/
    static class PageHolder extends HolderCDK {
        private final int[] sign;
        private final ImageView icon;
        private final TextView name;
        private final int type;

        public PageHolder(View itemView, int type, int[] sign) {
            super(itemView);
            this.sign = sign;
            this.type = type;
            this.icon = findViewById(R.id.icon);
            this.name = findViewById(R.id.name);
            V.RL(itemView).size(sign[3]).nimble().refresh();
        }

        /*** 更新 ***/
        private void update() {
            if (sign[0] > 3) name.setVisibility(View.GONE);
            else name.setVisibility(View.VISIBLE);
            V.RV(itemView).margin(sign[2], type == -1 ? 120 : sign[2], sign[2], type == 1 ? 120 : sign[2]).radius(sign[2]).refresh();
            V.FL(icon).margin(sign[2] - 5).refresh();
            V.RV(itemView).size(sign[3]).refresh();
        }

        /*** 设置Name ***/
        private void setName(CharSequence text) {
            this.name.setText(text);
        }

        /*** 设置Icon ***/
        private void setIcon(Drawable icon) {
            this.icon.setImageDrawable(icon);
        }

    }
}


