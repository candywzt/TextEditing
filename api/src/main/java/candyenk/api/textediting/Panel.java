package candyenk.api.textediting;

import android.view.View;
import candyenk.android.widget.DialogBottomRV;

import java.util.function.Consumer;

/**
 * 插件面板操作接口
 * 父布局是RecycleView
 * 布局控件使用原生TextView,Button,EditText
 * 不用特意设置样式,有统一风格样式
 * 也不用特意套一层滑动组件,自带的
 */
public interface Panel {

    /**
     * 设置弹窗标题
     * 版本:001
     */
    void setTitle(CharSequence title);

    /**
     * 设置自定义弹窗布局
     * 版本:001
     */
    void setContent(View view);

    /**
     * 设置字符串列表弹窗布局
     * 版本:001
     */
    void setContent(CharSequence... items);

    /**
     * 设置字符串列表文本居中
     * 默认true
     * 版本:001
     */
    void setItemCenter(boolean isCenter);

    /**
     * 设置字符串列表下项目点击事件
     * 设置字符串列表布局才有用
     * 版本:001
     */
    void setOnItemClickListener(ItemListener l);

    /**
     * 设置字符串列表下项目长按事件
     * 设置字符串列表布局才有用
     * 版本:001
     */
    void setOnItemLongClickListener(ItemListener l);

    /**
     * 设置底栏按钮
     * 最多两个
     * 不设置事件是不起作用的
     * 版本:001
     */
    void setLeftButton(CharSequence text, Consumer<? extends DialogBottomRV> leftClick, Consumer<? extends DialogBottomRV> leftLong);

    /**
     * 设置底栏按钮点击事件
     * 版本:001
     */
    void setRightButton(CharSequence text, Consumer<? extends DialogBottomRV> rightClick, Consumer<? extends DialogBottomRV> rightLong);

    /**
     * 拉起弹窗
     * 版本:001
     */
    void show();

    /**
     * 关闭弹窗
     * 版本:001
     */
    void close();

    /**
     * 显示加载框
     * 显示文本内容,是否外触关闭,是否返回键关闭,加载框强制关闭回调
     * 版本:001
     */
    void showLoading(CharSequence text, boolean touchOff, boolean backOff, CancelListener l);

    /**
     * 更新加载框文本
     * 加载框未显示不起作用
     * 版本:001
     */
    void updateLoading(CharSequence text);

    /**
     * 结束加载框
     * 版本:001
     */
    void closeLoading();

    /**
     * 获取主色调
     * 颜色会更搭配
     * ColorInt颜色值
     * 版本:001
     */
    int getMainColor();

    /**
     * 项目点击(长按)回调接口
     * 版本:001
     */
    interface ItemListener {
        void onClick(int position, CharSequence string);
    }

    /**
     * 加载框被关闭回调接口
     * 版本:001
     */
    interface CancelListener {
        void onCancel();
    }
}
