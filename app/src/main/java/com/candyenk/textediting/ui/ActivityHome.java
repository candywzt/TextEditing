package com.candyenk.textediting.ui;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;
import candyenk.android.asbc.ActivityCDK;
import candyenk.android.asbc.AdapterRVCDK;
import candyenk.android.asbc.HolderCDK;
import candyenk.android.tools.L;
import candyenk.android.tools.V;
import candyenk.android.utils.ULay;
import candyenk.android.utils.UShare;
import candyenk.android.utils.USys;
import com.candyenk.textediting.APP;
import com.candyenk.textediting.R;
import com.candyenk.textediting.plugin.PM;
import org.jetbrains.annotations.NotNull;

/**
 * PageGrid列数(2-6)
 */
public class ActivityHome extends ActivityCDK {
    private static final L.Loger l = APP.l;
    private static final String TAG = ActivityHome.class.getSimpleName();
    private LinearLayout buttonGroup, indicatorGroup;
    private CardView inCard, outCard;
    private EditText inEdit, outEdit;
    private ViewPager2 pageView;
    private final int[] sign = {3, 1};//PageGrid列数,IO动画方向(正1反0)
    private ValueAnimator IOAnim;//动画

    @Override
    protected void intentInit() {
        sign[0] = APP.getSetting().getInt(PageSetting.ITEM_COUNT, sign[0]);
    }

    @Override
    protected void viewInit() {
        setContentView(R.layout.activity_home);
        setTitle(R.string.app_name);
        buttonGroup = findViewById(R.id.button_group);
        indicatorGroup = findViewById(R.id.indicator_group);
        inCard = findViewById(R.id.input_card);
        outCard = findViewById(R.id.output_card);
        inEdit = (EditText) inCard.getChildAt(0);
        outEdit = (EditText) outCard.getChildAt(0);
        pageView = findViewById(R.id.pager_view);
    }

    @Override
    protected void contentInit(Bundle save) {
        if (save != null) {
            inEdit.setText(save.getCharSequence("in"));
            outEdit.setText(save.getCharSequence("out"));
        }
        PM.setEditText(inEdit, outEdit);
        PM.setContext(this);
    }

    @Override
    protected void eventInit() {
        bindingPage();
        inCard.setOnClickListener(v -> {
            inEdit.requestFocus();
            USys.showIM(this, inEdit);
        });
        outCard.setOnClickListener(v -> {
            outEdit.requestFocus();
            USys.showIM(this, outEdit);
        });
        inEdit.setOnClickListener(v -> USys.showIM(this, v));
        outEdit.setOnClickListener(v -> USys.showIM(this, v));
        inEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (IOAnim == null) IOAnim = createInOutAnim();
            if (hasFocus && !IOAnim.isRunning()) {sign[1] = 1; IOAnim.start();}

        });
        outEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (IOAnim == null) IOAnim = createInOutAnim();
            else if (hasFocus && !IOAnim.isRunning()) {sign[1] = 0; IOAnim.start();}
        });
        buttonListener();
    }

    @Override
    protected Bundle saveData(Bundle bundle) {
        bundle.putCharSequence("in", inEdit.getText());
        bundle.putCharSequence("out", outEdit.getText());
        return bundle;
    }

    /*****************************************************************************************************************/
    /**************************************************私有方法********************************************************/
    /*****************************************************************************************************************/
    /**
     * 添加脚本按钮事件
     */
    private void startAddScript() {
        UShare.startActivity(this, ActivityPlugin.class, d -> PagePlugin.updateList());
    }

    /**
     * Page页面进度条绑定
     */
    private void bindingPage() {
        for (int i = 0; i < 3; i++) {
            View view = V.getChild(indicatorGroup, i);
            final int ii = i;
            view.setOnClickListener(vv -> pageView.setCurrentItem(ii));
        }
        pageView.setAdapter(new PageAdapter());
        pageView.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            private final int max = (int) ULay.dp2px(pageView.getContext(), 12);
            private final int min = (int) ULay.dp2px(pageView.getContext(), 8);
            private final int value = max - min;

            @Override
            public void onPageScrolled(int p, float set, int px) {
                if (p == 2) {p = 1; set = 1;}
                int wh1 = (int) (max - set * value);
                int wh2 = (int) (min + set * value);
                V.LL(V.getChild(indicatorGroup, p)).size(wh1, wh1).refresh();
                V.LL(V.getChild(indicatorGroup, p + 1)).size(wh2, wh2).refresh();
            }
        });
    }

    /**
     * 操作按钮事件监听
     */
    private void buttonListener() {
        buttonGroup.getChildAt(0).setOnClickListener(v -> {
            inEdit.setText(null);
            outEdit.setText(null);
            try {
                throw new RuntimeException("哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒");
            } catch (Exception e) {
                l.e(e, "哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒哒");
            }
        });
        buttonGroup.getChildAt(1).setOnClickListener(v -> {
            CharSequence t1 = inEdit.getText();
            CharSequence t2 = outEdit.getText();
            if (TextUtils.isEmpty(t1) && TextUtils.isEmpty(t2)) {
                String error = getString(R.string.home_empty_content);
                inEdit.setError(error);
                outEdit.setError(error);
            } else {
                inEdit.setText(t2);
                outEdit.setText(t1);
            }
        });
        buttonGroup.getChildAt(2).setOnClickListener(v -> {});
        buttonGroup.getChildAt(3).setOnClickListener(v -> {});
        buttonGroup.getChildAt(4).setOnClickListener(v -> {
            if (TextUtils.isEmpty(outEdit.getText())) {
                outEdit.setError(getString(R.string.home_empty_content));
                outEdit.requestFocus();
                return;
            }
            UShare.writeClipboard(this, outEdit.getText());
            L.e(TAG, getString(R.string.home_copy) + outEdit.getText());
        });
        buttonGroup.getChildAt(5).setOnClickListener(v -> {
            if (TextUtils.isEmpty(UShare.readClipboard(this))) {
                inEdit.setError(getString(R.string.home_empty_clip));
                inEdit.requestFocus();
                return;
            }
            inEdit.setText(UShare.readClipboard(this));
            L.e(TAG, getString(R.string.home_paste) + inEdit.getText());
        });
        V.lClick(v -> toast(R.string.home_info_empty), buttonGroup.getChildAt(0));
        V.lClick(v -> toast(R.string.home_info_invert), buttonGroup.getChildAt(1));
        V.lClick(v -> {}, buttonGroup.getChildAt(2));
        V.lClick(v -> {}, buttonGroup.getChildAt(3));
        V.lClick(v -> toast(R.string.home_info_copy), buttonGroup.getChildAt(4));
        V.lClick(v -> toast(R.string.home_info_paste), buttonGroup.getChildAt(5));
    }


    /**
     * 创建输入输出框动画
     */
    private ValueAnimator createInOutAnim() {
        LinearLayout.LayoutParams lpIn = (LinearLayout.LayoutParams) inCard.getLayoutParams();
        LinearLayout.LayoutParams lpOut = (LinearLayout.LayoutParams) outCard.getLayoutParams();
        int in = inCard.getHeight();
        int out = outCard.getHeight();
        ValueAnimator va = ValueAnimator.ofInt(in, out);
        va.setDuration(300);
        va.setInterpolator(new AccelerateDecelerateInterpolator());
        va.addUpdateListener(anim -> {
            int value = (int) anim.getAnimatedValue();
            lpIn.height = sign[1] > 0 ? value : in + out - value;
            lpOut.height = sign[1] > 0 ? in + out - value : value;
            inCard.setLayoutParams(lpIn);
            outCard.setLayoutParams(lpOut);
        });
        return va;
    }


    /*****************************************************************************************************************/
    /***************************************************内部类*********************************************************/
    /*****************************************************************************************************************/
    /**
     * 功能栏适配器
     */
    class PageAdapter extends AdapterRVCDK<HolderCDK> {
        private final View[] vv = new View[3];

        private PageAdapter() {
            vv[0] = PagePlugin.createSystem(getLayoutInflater(), pageView, sign[0]);
            vv[1] = PagePlugin.createCusto(getLayoutInflater(), pageView, sign[0]);
            vv[2] = PageSetting.createPage(getLayoutInflater(), pageView);
        }

        @Override
        public HolderCDK onCreate(ViewGroup p, int t) {
            return new HolderCDK(vv[t]);
        }

        @Override
        public void onBind(@NotNull HolderCDK h, int p) {
            if (p == 2) return;
            int[] title = {R.string.home_page_system, R.string.home_page_user};
            View item = h.itemView;
            ((TextView) item.findViewById(R.id.title)).setText(title[p]);
            item.findViewById(R.id.add).setOnClickListener(v -> startAddScript());
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public int getType(int p) {
            return p;
        }
    }
}
