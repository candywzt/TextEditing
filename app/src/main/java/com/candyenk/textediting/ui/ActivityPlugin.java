package com.candyenk.textediting.ui;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import candyenk.android.asbc.ActivityCDK;
import candyenk.android.asbc.AdapterRVCDK;
import candyenk.android.asbc.HolderCDK;
import candyenk.android.tools.L;
import candyenk.android.tools.RC;
import candyenk.android.tools.V;
import candyenk.android.utils.UFile;
import candyenk.android.utils.UShare;
import candyenk.android.view.LabelView;
import candyenk.android.widget.*;
import candyenk.java.utils.UData;
import candyenk.java.utils.UString;
import candyenk.java.utils.UTime;
import com.candyenk.textediting.APP;
import com.candyenk.textediting.R;
import com.candyenk.textediting.plugin.Loader;
import com.candyenk.textediting.plugin.LuaPlugin;
import com.candyenk.textediting.plugin.PConfig;
import com.candyenk.textediting.plugin.PM;
import com.candyenk.textediting.ui.holder.ItemPluginInfo;
import com.candyenk.textediting.ui.holder.ItemPluginUpdate;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.*;


public class ActivityPlugin extends ActivityCDK {
    private static final L.Loger log = APP.l;
    private RecyclerView listView;
    private PluginAdapter adp;
    private ItemBar menuView;
    private ItemBar.ItemPackage itemMenu, itemBack, itemChoose, itemReverse, itemImport, itemDelete;//菜单,返回,多选,反选,导入,删除

    @Override
    protected void intentInit() {

    }

    @Override
    protected void viewInit() {
        setTitle(R.string.act_plugin_manager);
        setContentView(createLayout());
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void contentInit(Bundle save) {
        this.adp = new PluginAdapter();
        this.listView.setLayoutManager(new LinearLayoutManager(this));
        this.listView.setAdapter(adp);
    }

    @Override
    protected void eventInit() {
        createEvent();
        menuView.showItem(itemMenu, 2);
    }

    @Override
    protected Bundle saveData(Bundle bundle) {
        return null;
    }

    /*** 创建布局 ***/
    private View createLayout() {
        //noinspection DuplicatedCode
        listView = new RecyclerView(this);
        V.FL(listView).size(-1, -1).refresh();
        menuView = new ItemBar(this);
        menuView.enableBigCenter(true);
        V.FL(menuView).size(-1, -2).ele(100).paddingDP(10, 10, 10, 40).lGravity(Gravity.BOTTOM).parent(container);
        return listView;
    }

    /*** 创建事件监听 ***/
    @SuppressLint("NotifyDataSetChanged")
    private void createEvent() {
        itemMenu = new ItemBar.ItemWrapper(this, 0, R.drawable.ic_ok, (v) -> {
            menuView.showItem(itemChoose, 1);
            menuView.showItem(itemBack, 2);
            menuView.showItem(itemImport, 3);

        }, (v) -> menuView.showTitle(R.string.app_menu, 2));
        itemBack = new ItemBar.ItemWrapper(this, 0, R.drawable.aaa, (v) -> {
            menuView.hideItem(1);
            menuView.showItem(itemMenu, 2);
            menuView.hideItem(3);
            adp.setChoose(false);
        }, (v) -> menuView.showTitle(R.string.app_back, 2));
        itemChoose = new ItemBar.ItemWrapper(this, 0, R.drawable.redo, (v) -> {
            menuView.showItem(itemReverse, 1);
            menuView.showItem(itemBack, 2);
            menuView.showItem(itemDelete, 3);
            adp.setChoose(true);
        }, (v) -> menuView.showTitle(R.string.app_multiplechoice, 1));
        itemReverse = new ItemBar.ItemWrapper(this, 0, R.drawable.delete, (v) -> {
            //TODO:反选震动
            adp.setReverse();
        }, (v) -> menuView.showTitle(R.string.app_inverseselection, 1));
        itemImport = new ItemBar.ItemWrapper(this, 0, R.drawable.filp, (v) -> {
            //TODO;导入震动
            UShare.getDocumentFile(this, uri -> {
                String[] meta = UFile.getMetaData(ActivityPlugin.this, uri);
                if (!PM.checkType(meta[0])) {
                    tips(v, R.string.plugin_import_fail_type, getString(R.string.plugin_import_file_name) + meta[0]);
                } else if (UString.toLong(meta[1]) > 10485760) {
                    tips(v, R.string.plugin_import_fail_size, getString(R.string.plugin_import_file_size) + UData.B2A(UString.toLong(meta[1])));
                } else {
                    DialogLoading dl = new DialogLoading(v);
                    dl.setTitle(R.string.plugin_importing);
                    dl.setThreadRun(new RC(rc -> {
                        InputStream in = UFile.readUri(ActivityPlugin.this, uri);
                        if (in == null) return null;//文件流为空
                        if (TextUtils.isEmpty(meta[0])) meta[0] = UUID.randomUUID().toString();//重置文件名
                        Loader loader = PM.createLoader(meta[0], in);
                        Loader l1 = PM.checkLoader(loader);
                        if (l1 == null) return log.e("插件包初始化失败,请检查插件包是否完整", null);
                        else if (l1 != loader) return new Loader[]{l1, loader};
                        log.i("导入插件:" + loader.getConfig().getTitle());
                        PM.addPlugin(loader);
                        adp.addItem(loader.getConfig().getUuid());
                        loader.setInstallTime(System.currentTimeMillis());
                        loader.setUpdateTime(System.currentTimeMillis());
                        return true;
                    }, (m) -> {
                        if (m == null)
                            tips(v, R.string.plugin_import_fail, getString(R.string.plugin_import_fail_msg));//失败
                        else if (m.equals(true)) successTips(v, false);//成功
                        else updateTips(v, ((Loader[]) m)[0], ((Loader[]) m)[1]);//更新
                    }));
                    dl.show();
                }
            }, PM.mime);
        }, (v) -> menuView.showTitle(R.string.app_import, 3));
        itemDelete = new ItemBar.ItemWrapper(this, 0, R.drawable.paste, (v) -> {
            List<String> dList = adp.getChecked();
            if (dList.size() == 0) return;
            StringBuilder sb = new StringBuilder(getString(R.string.plugin_delete_now)).append("\n");
            dList.forEach(s -> sb.append("[")
                    .append(getString(R.string.plugin_name))
                    .append(PM.getLoader(s).getConfig().getTitle())
                    .append("\n")
                    .append(getString(R.string.plugin_uuid))
                    .append(s)
                    .append("]\n"));
            DialogBottomConfirm db = new DialogBottomConfirm(v);
            db.setTitle(R.string.plugin_delete_confirm);
            db.setContent(sb.toString());
            db.setOnEventCallBack((DialogBottomConfirm.Yes) () -> {
                DialogLoading dl = new DialogLoading(v);
                dl.setTitle(R.string.plugin_deleting);
                dl.setThreadRun(new RC(rc -> {
                    dList.forEach(s -> {
                        PM.removePlugin(s);
                        adp.removeItem(s);
                    });
                    return null;
                }, (s, m) -> {
                    DialogBottomTips dbt = new DialogBottomTips(v);
                    dbt.setTitle(R.string.plugin_deleted);
                    dbt.show();
                    adp.notifyDataSetChanged();
                }));
                dl.show();
            });
            db.show();
            setResult(RESULT_OK);//在这个地方设置了返回值
        }, (v) -> menuView.showTitle(R.string.app_delete, 3));
    }

    /*** 提示 ***/
    private void tips(View v, int id, String content) {
        DialogBottomTips db = new DialogBottomTips(v);
        db.setTitle(id);
        if (content != null) db.setContent(content);
        db.show();
    }

    /*** 导入成功提示 ***/
    @SuppressLint("NotifyDataSetChanged")
    private void successTips(View v, boolean i) {
        tips(v, i ? R.string.plugin_import_ok : R.string.plugin_update_ok, null);
        setResult(RESULT_OK);//在这个地方设置了返回值
        adp.notifyDataSetChanged();
    }

    /*** 更新提示 ***/
    private void updateTips(View v, Loader l1, Loader l2) {
        DialogBottomView db = new DialogBottomView(v);
        db.setTitle(R.string.plugin_update);
        db.setContent(R.layout.item_plugin_update);
        db.bindContent(new ItemPluginUpdate(l1, l2));
        db.setLeftButton(null, d -> d.dismiss(dd -> {
            log.i("更新插件:" + l1.getConfig().getTitle() + "->" + l2.getConfig().getTitle());
            PM.addPlugin(l2);
            adp.addItem(l2.getConfig().getUuid());
            l2.setUpdateTime(System.currentTimeMillis());
            successTips(v, true);
        }), null);
        db.setRightButton(null, DialogBottom::dismiss, null);
        db.show();
    }

    /*** PluginList适配器 ***/
    private class PluginAdapter extends AdapterRVCDK<PluginHolder> {
        private final List<String> list;//所有用户插件UUID列表
        private final Map<String, Boolean> map;//UUID状态Map
        private boolean isChoose;//多选状态

        public PluginAdapter() {
            this.list = new ArrayList<>();
            this.list.addAll(PM.sysList);
            this.list.addAll(PM.pluList);
            this.map = new HashMap<>();
            PM.pluList.forEach(s -> map.put(s, false));
            this.isChoose = false;
            setOnClickListener(listView, this::onClick);
            setOnLongClickListener(listView, this::onLongClick);
        }

        @Override
        public PluginHolder onCreate(ViewGroup parent, int viewType) {
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(ActivityPlugin.this).inflate(R.layout.item_plugin, null);
            return new PluginHolder(view);
        }

        @Override
        public void onBind(@NotNull PluginHolder h, int p) {
            V.RV(h.itemView).size(-1, -2).marginDP(4, p == 0 ? 120 : 4, 4, p == getItemCount() - 1 ? 100 : 4).refresh();
            String uuid = list.get(p);
            Loader loader = PM.getLoader(uuid);
            PConfig c = (PConfig) loader.getConfig();
            h.setIcon(c.getIcon());
            h.setText(c.getTitle(), c.getVersion(), c.getUpdateTime());
            h.clearLabel();
            if (loader.getPlugin() instanceof LuaPlugin) h.addLabel(R.string.plugin_label_lua, 0);
            else h.addLabel(R.string.plugin_label_apk, 1);
            if (PM.sysList.contains(uuid)) h.addLabel(R.string.plugin_label_sys, 2);
            else h.addLabel(R.string.plugin_label_user, 3);
            if (PM.sysList.contains(uuid)) {
                h.setChoose(false);
                return;
            }
            h.setChoose(isChoose);
            Boolean b = map.get(uuid);
            h.setChecked(b != null && b);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        /*** 添加项目 ***/
        private void addItem(String uuid) {
            if (!list.contains(uuid)) list.add(uuid);
            map.put(uuid, false);
        }

        /*** 移除项目 ***/
        private void removeItem(String uuid) {
            list.remove(uuid);
            map.remove(uuid);
        }

        /*** 控件被单击 ***/
        private void onClick(PluginHolder h) {
            if (isChoose) toggleSelected(h);
            else openInfo(h);
        }

        /*** 控件被长按 ***/
        private void onLongClick(PluginHolder h) {
            if (isChoose) openInfo(h);
            else itemChoose.onClick(h.itemView);
        }

        /*** 切换选择状态 ***/
        private void toggleSelected(PluginHolder h) {
            h.setChecked(!h.isChecked());
            String s = list.get(h.getAdapterPosition());
            if (map.containsKey(s)) map.put(s, h.isChecked());
        }

        /*** 打开详情 ***/
        private void openInfo(PluginHolder h) {
            DialogBottomView db = new DialogBottomView(h.itemView);
            db.setTitle(R.string.plugin_info);
            db.setContent(R.layout.item_plugin_info);
            db.show();
            db.bindContent(new ItemPluginInfo(list.get(h.getItemPosition())));
        }

        /*** 设置多选状态 ***/
        @SuppressLint("NotifyDataSetChanged")
        private void setChoose(boolean b) {
            if (!b) map.forEach((s, a) -> map.put(s, false));
            if (isChoose != b) {
                isChoose = b;
                notifyDataSetChanged();
            }
        }

        /*** 设置反选 ***/
        @SuppressLint("NotifyDataSetChanged")
        private void setReverse() {
            map.forEach((s, b) -> map.put(s, !b));
            notifyDataSetChanged();
        }

        /*** 获取选中UUID ***/
        private List<String> getChecked() {
            List<String> l = new ArrayList<>();
            map.forEach((s, b) -> {if (b) l.add(s);});
            return l;
        }
    }

    /*** PluginList布局器 ***/
    private static class PluginHolder extends HolderCDK {
        private final ImageView icon;
        private final LinearLayout labList;
        private final TextView tv;
        private final CheckBox cb;

        public PluginHolder(View itemView) {
            super(itemView);
            this.icon = findViewById(R.id.icon);
            this.labList = findViewById(R.id.label_list);
            this.tv = findViewById(R.id.textview);
            this.cb = findViewById(R.id.checkbox);
        }

        /*** 设置Icon ***/
        private void setIcon(Drawable drawable) {
            this.icon.setImageDrawable(drawable);
        }

        /*** 设置文本 ***/
        @SuppressLint("StringFormatMatches")
        private void setText(String name, int version, long update) {
            String sb = context.getString(R.string.plugin_name) + name + "\n" +
                    context.getString(R.string.plugin_version) + version + "\n" +
                    context.getString(R.string.plugin_update_time) + UTime.D2S(update);
            this.tv.setText(sb);
        }

        /*** 开关选择状态 ***/
        private void setChoose(boolean b) {
            this.cb.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        /*** 设置开关状态 ***/
        private void setChecked(boolean b) {
            this.cb.setChecked(b);
        }

        /*** 获取开关状态 ***/
        private boolean isChecked() {
            return this.cb.isChecked();
        }

        /*** 清空Label ***/
        private void clearLabel() {
            this.labList.removeAllViews();
        }

        /*** 添加一个Label ***/
        private void addLabel(int id, int index) {
            LabelView l = new LabelView(context);
            V.LL(l).size(-2, -2).textSize(10).text(id).parent(labList);
            l.setColorIndex(index);
        }
    }
}
