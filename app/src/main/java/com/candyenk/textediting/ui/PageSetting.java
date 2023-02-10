package com.candyenk.textediting.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import candyenk.android.tools.L;
import candyenk.android.tools.RC;
import candyenk.android.utils.UFile;
import candyenk.android.utils.UShare;
import candyenk.android.widget.*;
import candyenk.java.utils.UArrays;
import candyenk.java.utils.UData;
import com.candyenk.textediting.APP;
import com.candyenk.textediting.R;
import com.candyenk.textediting.plugin.PM;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PageSetting {
    private static final L.Loger l = APP.l;
    public static final String ITEM_COUNT = "itemCount";//列数
    public static final String ITEM_LEVEL = "itemLevel";//日志级别
    private final ViewGroup parent;
    private final Context context;
    private final SharedPreferences sp;
    private ItemSeekBar countView;
    private Item clearView, pluginView, logView, levelView, aboutView, playView;

    /**
     * 创建系统脚本Page页面
     */
    static View createPage(LayoutInflater li, ViewGroup parent) {
        return new PageSetting(li, parent).build();
    }


    @SuppressLint("InflateParams")
    private PageSetting(LayoutInflater li, ViewGroup parent) {
        this.context = li.getContext();
        this.parent = (ViewGroup) li.inflate(R.layout.page_setting, parent, false);
        sp = APP.getSetting();
        initview();
        initContent();
        initEvent();
    }


    private void initview() {
        countView = findViewById(R.id.count_view);
        clearView = findViewById(R.id.clear_view);
        pluginView = findViewById(R.id.plugin_view);
        logView = findViewById(R.id.log_view);
        levelView = findViewById(R.id.loglevel_view);
        aboutView = findViewById(R.id.about_view);
        playView = findViewById(R.id.play_view);
    }

    private void initContent() {
        countView.setProgress(sp.getInt(ITEM_COUNT, 3));
        levelView.setSummaryText(L.getLevelString(sp.getInt(ITEM_LEVEL, L.INFO)));
    }

    private void initEvent() {
        //功能栏列数
        countView.setOnSeekBarChangeListener((p, per) -> {
            sp.edit().putInt(ITEM_COUNT, p).apply();
            PagePlugin.updateCount(p);
            countView.setProgressText(String.valueOf(p));
        });
        clearView.setOnClickListener(this::clearCache);
        pluginView.setOnClickListener(this::onClick);
        logView.setOnClickListener(this::onClick);
        levelView.setOnClickListener(this::logLevel);
        aboutView.setOnClickListener(this::onClick);
        playView.setOnClickListener(this::onClick);
    }

    /*** 清理缓存 ***/
    private void clearCache(View v) {
        DialogLoading dl = new DialogLoading(v);
        dl.setCanceledable(false, true);
        dl.setTitle(R.string.home_clear_up);
        dl.setOnCancelListener(d -> {
            Toast.makeText(context, context.getString(R.string.home_clear_cancel), Toast.LENGTH_SHORT).show();
        });
        dl.setThreadRun(new RC(rc -> {
            File data = context.getDataDir();
            List<File> list = new ArrayList<>();
            String[] nameList = data.list();
            nameList = nameList == null ? new String[0] : nameList;
            for (String s : nameList) if (!UArrays.isContain(APP.noCache, s)) list.add(new File(data, s));
            list.add(new File(PM.sysPath, "oat"));
            list.add(new File(PM.userPath, "oat"));
            File[] files = list.toArray(new File[0]);
            int n1 = UFile.numberFiles(files);
            int n2 = UFile.numberFolders(files);
            long z = UFile.sizeFile(files);
            UFile.deleteFile(files);
            return new Object[]{String.valueOf(n1), String.valueOf(n2), UData.B2A(z)};
        }, (s, m) -> {
            Object[] o = (Object[]) m;
            dl.setOnCancelListener(null);
            DialogBottomTips db = new DialogBottomTips(v);
            db.setTitle(R.string.home_clear_end);
            @SuppressLint("StringFormatMatches")
            String ss = String.format(context.getString(R.string.home_clear_end_msg), o);
            db.setContent(l.i(ss, ss));
            db.show();
        }));
        dl.show();
    }

    /*** 设置日志级别 ***/
    private void logLevel(View v) {
        DialogBottomItemText db = new DialogBottomItemText(v);
        db.setTitle(R.string.home_page_item_log_level);
        db.setContent(L.levelSA);
        db.setOnItemClickListener((vv, i) -> {
            int ol = sp.getInt(ITEM_LEVEL, L.INFO);
            int nl = L.levelIA[i];
            if (ol == nl) return;
            l.i("日志级别已更改(" + L.getLevelString(ol) + "->" + L.getLevelString(nl) + ")");
            levelView.setSummaryText(L.getLevelString(nl));
            sp.edit().putInt(ITEM_LEVEL, nl).apply();
            L.getInstance().getLogerMap().forEach((s, l) -> l.setTargetLevel(nl));
            db.dismiss();
        });
        db.show();
    }

    /*** FindView实现 ***/
    private <T extends View> T findViewById(int resId) {
        return parent.findViewById(resId);
    }

    /*** 返回父布局 ***/
    private View build() {
        return parent;
    }

    /*** 项目监听 ***/
    @SuppressLint("NonConstantResourceId")
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.plugin_view:
                UShare.startActivity(context, ActivityPlugin.class, d -> PagePlugin.updateList());
                break;
            case R.id.log_view:
                UShare.startActivity(context, ActivityLog.class);
                break;
            case R.id.about_view:
                UShare.startActivity(context, ActivityAbout.class);
                break;
            case R.id.play_view:
                UShare.startActivity(context, ActivityTest.class);
                break;
        }
    }
}


