package com.candyenk.textediting.plugin;

import android.view.View;
import android.widget.TextView;
import candyenk.android.widget.*;
import candyenk.api.textediting.Log;
import candyenk.api.textediting.Panel;

import java.util.function.Consumer;

/**
 * 插件操作面板的实现
 */
public class PPanel implements Panel {
    private final Loader loader;
    private final Log l;
    private DialogBottom db;
    private DialogLoading dl;

    /**
     * 创建操作面板实例
     */
    static PPanel create(Loader loader) {
        return new PPanel(loader);
    }

    private PPanel(Loader loader) {
        this.loader = loader;
        this.l = loader.getLog();
    }

    @Override
    public void setTitle(CharSequence title) {
        if (this.db == null) l.e("尚未添加面板布局,请先SetContent!");
        else this.db.setTitle(title);
    }

    @Override
    public void setContent(View view) {
        if (this.db == null) {
            this.db = new DialogBottomView(loader.getApp());
            this.db.setTitle(loader.getConfig().getTitle());
        }
        this.db.setShowClose(true);
        this.db.setOnCancelListener(d -> loader.release());
        this.db.setOnDismissListener(d -> loader.release());
        if (this.db instanceof DialogBottomView) ((DialogBottomView) db).setContent(view);
    }


    @Override
    public void setContent(CharSequence... items) {
        if (this.db == null) {
            this.db = new DialogBottomItemText(loader.getApp());
            this.db.setTitle(loader.getConfig().getTitle());
            this.db.setShowClose(true);
            this.db.setOnCancelListener(d -> loader.release());
            this.db.setOnDismissListener(d -> loader.release());
        }
        if (this.db instanceof DialogBottomItemText) ((DialogBottomItemText) db).setContent(items);
    }

    @Override
    public void setItemCenter(boolean isCenter) {
        if (this.db == null) l.e("尚未添加面板布局,请先SetContent!");
        if (this.db instanceof DialogBottomItemText)
            ((DialogBottomItemText) db).setItemCenter(isCenter);
    }

    @Override
    public void setOnItemClickListener(ItemListener l) {
        if (this.db == null) this.l.e("尚未添加面板布局,请先SetContent!");
        if (this.db instanceof DialogBottomItemText)
            ((DialogBottomItemText) db).setOnItemClickListener((v, i) -> l.onClick(i, ((TextView) v).getText()));
    }

    @Override
    public void setOnItemLongClickListener(ItemListener l) {
        if (this.db == null) this.l.e("尚未添加面板布局,请先SetContent!");
        if (this.db instanceof DialogBottomItemText)
            ((DialogBottomItemText) db).setOnItemLongClickListener((v, i) -> l.onClick(i, ((TextView) v).getText()));
    }

    @Override
    public void setLeftButton(CharSequence text, Consumer<? extends DialogBottomRV> leftClick, Consumer<? extends DialogBottomRV> leftLong) {
        if (this.db == null) l.e("尚未添加面板布局,请先SetContent!");
        this.db.setLeftButton(text, leftClick, leftLong);
    }

    @Override
    public void setRightButton(CharSequence text, Consumer<? extends DialogBottomRV> rightClick, Consumer<? extends DialogBottomRV> rightLong) {
        if (this.db == null) this.l.e("尚未添加面板布局,请先SetContent!");
        this.db.setRightButton(text, rightClick, rightLong);
    }

    @Override
    public void show() {
        if (!this.loader.isReady()) return;
        if (this.db == null) l.e("尚未添加面板布局,请先SetContent!");
        if (!this.db.isShowing()) this.db.show();
    }

    @Override
    public void close() {
        if (this.db == null) l.e("尚未添加面板布局,请先SetContent!");
        else this.db.dismiss(dialogBottom -> loader.release());
    }

    @Override
    public void showLoading(CharSequence text, boolean touchOff, boolean backOff, CancelListener l) {
        if (this.dl == null) this.dl = new DialogLoading(loader.getContext());
        dl.setTitle(text);
        dl.setCanceledable(touchOff, backOff);
        dl.setOnCancelListener(d -> l.onCancel());
        dl.show();
    }

    @Override
    public void updateLoading(CharSequence text) {
        if (this.dl == null || !this.dl.isShowing()) return;
        this.dl.setTitle(text);
    }


    @Override
    public void closeLoading() {
        if (this.dl == null || !this.dl.isShowing()) return;
        this.dl.dismiss();
    }

    @Override
    public int getMainColor() {
        return loader.getApp().getColor(candyenk.android.R.color.main_01);
    }


}
