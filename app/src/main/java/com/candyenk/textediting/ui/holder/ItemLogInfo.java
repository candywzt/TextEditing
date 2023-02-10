package com.candyenk.textediting.ui.holder;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.View;
import candyenk.android.tools.L;
import candyenk.android.tools.V;
import candyenk.android.tools.VB;
import com.candyenk.textediting.R;

/**
 * 日志信息弹窗(仅头部)
 */
public class ItemLogInfo extends VB {
    private final L.LogInfo info;
    private final Drawable d, bg;
    private final boolean ie;//是否有堆栈信息

    public ItemLogInfo(L.LogInfo info, Drawable d, Drawable bg, boolean ie) {
        this.info = info;
        this.d = d;
        this.bg = bg;
        this.ie = ie;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindContent(View view) {
        if (!ie) V.hide(id(R.id.log_stack_msg));
        id(R.id.icon, ICON).setImageDrawable(d);
        id(R.id.back).setBackground(bg);
        id(R.id.content, TEXT)
                .setText(string(R.string.log_source)
                        + info.getTag() + "\n"
                        + string(R.string.log_level)
                        + info.getLevelString() + "\n"
                        + string(R.string.log_time)
                        + info.getTimeString());
        id(R.id.msg, TEXT).setText(info.getMsg());
    }
}
