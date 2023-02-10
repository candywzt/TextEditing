package com.candyenk.textediting.ui;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import candyenk.android.asbc.ActivityCDK;
import candyenk.android.asbc.AdapterRVCDK;
import candyenk.android.asbc.HolderCDK;
import candyenk.android.tools.L;
import candyenk.android.tools.RC;
import candyenk.android.tools.V;
import candyenk.android.tools.VB;
import candyenk.android.utils.ULay;
import candyenk.android.view.LoadView;
import candyenk.android.widget.*;
import com.candyenk.textediting.APP;
import com.candyenk.textediting.R;
import com.candyenk.textediting.plugin.PM;
import com.candyenk.textediting.ui.holder.ItemLogInfo;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ActivityLog extends ActivityCDK {
    private static final L.Loger l = APP.l;
    private RecyclerView listView;
    private LoadView loadView;
    private TextView loadText;
    private LogAdapter adp;
    private ItemBar menuView;
    private ItemBar.ItemPackage itemMenu, itemBack, itemSift, itemSort, itemFlush, itemClear;//菜单,返回,筛选,排序,刷新,清空
    private List<L.LogInfo> finalList = new ArrayList<>();//参照日志列表
    private List<L.LogInfo> list = new ArrayList<>();//展示用日志列表
    private List<String> tagList;//日志TAG列表
    private Comparator<L.LogInfo> sort;//排序方法
    private Predicate<L.LogInfo> sift;//筛选方法
    private boolean[] siftA, siftB;//筛选数据组
    private boolean sign;//筛选修改标记

    @Override
    protected void intentInit() {

    }

    @Override
    protected void viewInit() {
        setTitle(R.string.act_log_manager);
        setContentView(createLayout());
    }

    @Override
    protected void contentInit(Bundle save) {
        this.adp = new LogAdapter();
        this.listView.setLayoutManager(new LinearLayoutManager(this));
        this.listView.setAdapter(adp);
        this.tagList = Arrays.asList(L.getInstance().getTagArray());
        this.siftA = new boolean[tagList.size()];
        this.siftB = new boolean[]{true, true, true};
        Arrays.fill(siftA, true);
        this.sort = Comparator.comparingLong(L.LogInfo::getTime);
        loadLog();
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
        listView = new RecyclerView(this);
        V.FL(listView).size(-1, -1).refresh();
        menuView = new ItemBar(this);
        menuView.enableBigCenter(true);
        V.FL(menuView).size(-1, -2).ele(100).paddingDP(10, 10, 10, 40).lGravity(Gravity.BOTTOM).parent(container);
        loadView = new LoadView(this);
        V.FL(loadView).sizeDP(180).lGravity(Gravity.CENTER).parent(container);
        loadText = new TextView(this);
        V.FL(loadText).sizeDP(180).gravity(Gravity.BOTTOM | Gravity.CENTER).lGravity(Gravity.CENTER).parent(container);
        return listView;
    }

    /*** 创建事件监听 ***/
    @SuppressLint("NotifyDataSetChanged")
    private void createEvent() {
        itemMenu = new ItemBar.ItemWrapper(this, 0, R.drawable.ic_ok, (v) -> {
            menuView.showItem(itemSift, 0);
            menuView.showItem(itemSort, 1);
            menuView.showItem(itemBack, 2);
            menuView.showItem(itemFlush, 3);
            menuView.showItem(itemClear, 4);
        }, (v) -> menuView.showTitle(R.string.app_menu, 2));
        itemBack = new ItemBar.ItemWrapper(this, 0, R.drawable.aaa, (v) -> {
            menuView.hideAll(() -> menuView.showItem(itemMenu, 2));
        }, (v) -> menuView.showTitle(R.string.app_back, 2));
        itemSift = new ItemBar.ItemWrapper(this, 0, R.drawable.aaa, (v) -> {
            displaySiftDialog();
        }, (v) -> menuView.showTitle(R.string.app_sift, 0));
        itemSort = new ItemBar.ItemWrapper(this, 0, R.drawable.aaa, (v) -> {
            DialogBottomItemText db = new DialogBottomItemText(this);
            db.setTitle(R.string.app_sort);
            db.setContent(getString(R.string.app_sort_time), getString(R.string.app_sort_time_r));
            db.setOnItemClickListener((view, i) -> {
                db.dismiss(d -> {
                    if (i == 0) sort = Comparator.comparingLong(L.LogInfo::getTime);
                    else sort = ((a, b) -> Long.compare(b.getTime(), a.getTime()));
                    siftList();
                    displayLogList(list.size());
                });
            });
            db.show();
        }, (v) -> menuView.showTitle(R.string.app_sort, 1));
        itemFlush = new ItemBar.ItemWrapper(this, 0, R.drawable.aaa, (v) -> {
            loadLog();
        }, (v) -> menuView.showTitle(R.string.app_flush, 3));
        itemClear = new ItemBar.ItemWrapper(this, 0, R.drawable.aaa, (v) -> {
            if (list.size() == 0) return;
            DialogBottomConfirm db = new DialogBottomConfirm(v);
            db.setTitle(R.string.log_clean_confirm);
            db.setContent(String.format(getString(R.string.log_clean_now), list.size()));
            db.setOnEventCallBack((DialogBottomConfirm.Yes) () -> {
                DialogLoading dl = new DialogLoading(v);
                dl.setTitle(R.string.log_clean_up);
                dl.setThreadRun(new RC(rc -> {
                    list = new ArrayList<>();
                    finalList = new ArrayList<>();
                    return L.getInstance().clear();
                }, (s, m) -> {
                    boolean b = (boolean) m;
                    if (b) displayLogList(0);
                    DialogBottomTips dbt = new DialogBottomTips(v);
                    dbt.setTitle(b ? R.string.log_clean_success : R.string.log_clean_fail);
                    if (!b) dbt.setContent(getString(R.string.log_clean_fail_msg));
                    dbt.show();
                }));
                dl.show();
            });
            db.show();
        }, (v) -> menuView.showTitle(R.string.app_clear, 4));
    }

    /*** 加载日志内容 ***/
    @SuppressLint("NotifyDataSetChanged")
    private void loadLog() {
        V.hide(listView);
        V.visible(loadView, loadText);
        loadView.start();
        loadText.setText(getString(R.string.log_read));
        new RC(rc -> {
            finalList.clear();
            finalList = L.getInstance().getLogList();
            siftList();
            return list.size();
        }, (s, m) -> {
            loadView.dismiss(v -> displayLogList((int) m));
        }).runThread();
    }

    /*** 展示日志列表 ***/
    @SuppressLint("NotifyDataSetChanged")
    private void displayLogList(int num) {
        if (num == 0) displayEmptyLog();
        else {
            V.hide(loadView, loadText);
            V.visible(listView);
            adp.notifyDataSetChanged();
            Toast.makeText(this, String.format(getString(R.string.log_show_now), num), Toast.LENGTH_SHORT).show();
        }
    }

    /*** 展示空日志 ***/
    private void displayEmptyLog() {
        V.hide(loadView, listView);
        V.visible(loadText);
        loadText.setText(R.string.log_empty);
    }


    /*** 展示筛选弹窗 ***/
    private void displaySiftDialog() {
        List<Integer> levelList = Arrays.asList(L.levelIA);
        VB vb = new VB() {
            @Override
            public void bindContent(View view) {
                ChipGroup cgp = id(R.id.log_sift_tag);
                ChipGroup cgl = id(R.id.log_sift_level);
                cgp.removeAllViews();
                if (tagList.contains(APP.TAG))
                    V.ML(createChip()).size(-2, -2).text(APP.TAG).check(siftA[0]).parent(cgp);
                for (int i = 1; i < siftA.length; i++)
                    V.ML(createChip()).size(-2, -2).text(getTitle(i)).check(siftA[i]).parent(cgp);
                for (int i = 0; i < siftB.length; i++)
                    V.getChild(cgl, i, Chip.class).setChecked(siftB[i]);
            }
        };
        DialogBottomView db = new DialogBottomView(this);
        db.setTitle(R.string.app_sift);
        db.setContent(R.layout.item_log_sift);
        db.bindContent(vb);
        db.setLeftButton(null, dd -> {
            dd.dismiss((Consumer<DialogBottomView>) d -> {
                ChipGroup cgp = d.getContent().findViewById(R.id.log_sift_tag);
                ChipGroup cgl = d.getContent().findViewById(R.id.log_sift_level);
                for (int i = 0; i < siftA.length; i++)
                    if (!(siftA[i] = V.getChild(cgp, i, Chip.class).isChecked())) sign = true;
                for (int i = 0; i < siftB.length; i++)
                    if (!(siftB[i] = V.getChild(cgl, i, Chip.class).isChecked())) sign = true;
                if (!sign) sift = null;//未做更改
                else sift = info -> {
                    int i1 = levelList.indexOf(info.getLevel());
                    if (i1 != -1 && !siftB[i1]) return false;
                    if (info.getTag().equals(APP.TAG) && !siftA[0]) return false;
                    int i2 = tagList.indexOf(info.getTag());
                    return i2 == -1 || siftA[i2];
                };
                siftList();
                displayLogList(list.size());
            });
        }, null);
        db.setRightButton(getString(R.string.app_reset), (Consumer<DialogBottomView>) d -> {
            vb.bindContent(d.getContent());
        }, null);
        db.show();
    }

    /*** 获取用来展示的Title ***/
    private String getTitle(int index) {
        String s = tagList.get(index);
        return APP.TAG.equals(s) ? APP.TAG : PM.getConfig(s).getTitle();
    }

    /*** 创建Chip ***/
    private Chip createChip() {
        return new Chip(this);
    }

    /*** 筛选并排序 ***/
    private void siftList() {
        if (sift == null) list = Arrays.asList(finalList.toArray(new L.LogInfo[0]));
        else list = finalList.stream().filter(sift).collect(Collectors.toList());
        list.sort(sort);
    }


    /*** Log条目适配器 ***/
    private class LogAdapter extends AdapterRVCDK<LogHolder> {
        private LogAdapter() {
            this.setOnClickListener(listView, this::openDetails);
            V.RV(setHeader(new LogHolder(new View(ActivityLog.this))).itemView).sizeDP(-1, 120).refresh();
            V.RV(setFooter(new LogHolder(new View(ActivityLog.this))).itemView).sizeDP(-1, 100).refresh();
        }

        @Override
        public LogHolder onCreate(ViewGroup parent, int viewType) {
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(ActivityLog.this).inflate(R.layout.item_log, null);
            V.RV(view).size(-1, -2).marginDP(4).refresh();
            return new LogHolder(view);
        }

        @Override
        public void onBind(@NotNull LogHolder h, int p) {
            h.setContent(list.get(p));
        }

        @Override
        public int getCount() {
            return list.size();
        }


        /*** 打开详情 ***/
        private void openDetails(LogHolder h) {
            List<String> sList = h.i.getStackList();
            DialogBottomItemHScroll db = new DialogBottomItemHScroll(h.itemView);
            db.setTitle(R.string.log_detail);
            db.setContent(R.layout.item_log_info_stack, sList.size());
            db.setOnBindViewHolder(hh -> V.RL(hh.itemView).text(sList.get(hh.getItemPosition())));
            ItemLogInfo ili = new ItemLogInfo(h.i, h.d(), h.bg(), sList.size() != 0);
            db.setHeader(ULay.parseXML(ActivityLog.this, R.layout.item_log_info, ili));
            db.show();
        }
    }

    /*** Log布局管理器 ***/
    private static class LogHolder extends HolderCDK {
        private final ImageView icon;
        private final TextView title, summary;
        private L.LogInfo i;

        public LogHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.icon = findViewById(R.id.icon);
            this.title = findViewById(R.id.title);
            this.summary = findViewById(R.id.summary);
        }

        @SuppressLint("SetTextI18n")
        private void setContent(L.LogInfo i) {
            this.i = i;
            if (i.getTag().equals(APP.TAG)) icon.setImageResource(R.mipmap.ic_launcher);
            else icon.setImageDrawable(PM.getLoader(i.getTag()).getConfig().getIcon());

            if (i.getLevel() == L.DEBUG) itemView.setBackgroundResource(R.drawable.bg_frame_yellow);
            else if (i.getLevel() == L.ERROR) itemView.setBackgroundResource(R.drawable.bg_frame_red);
            else itemView.setBackgroundResource(R.drawable.bg_frame);

            this.title.setText(i.getMsg());
            this.summary.setText(string(R.string.log_time) + i.getTimeString());
        }

        /*** 复制Icon ***/
        private Drawable d() {
            return icon.getDrawable().getConstantState().newDrawable();
        }

        /*** 复制Background ***/
        private Drawable bg() {
            return itemView.getBackground().getConstantState().newDrawable();
        }
    }
}
