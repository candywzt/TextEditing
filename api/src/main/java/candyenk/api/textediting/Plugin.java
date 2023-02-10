package candyenk.api.textediting;

/**
 * Java脚本接口
 * 方法调用顺序(onReady->onReset->onLoad)
 * View创建使用纯Java实现(无法使用xml)
 * 颜色图片请使用ColorInt和Drawable实现(无法使用R资源)
 */
public interface Plugin {
    /**
     * 插件即将准备
     * 初始化一些可复用内容
     * 请勿执行耗时操作和UI操作!!!
     * 只会调用一次(APP打开时调用)
     * 版本:001
     */
    void onReady(API api);

    /**
     * 插件将被加载
     * 用户点一次运行一次
     * 版本:001
     */
    void onLoad() throws Throwable;

    /**
     * 插件状态将被重置
     * 将插件恢复到初始化状态
     * onLoad前调用以重置状态
     * 版本:001
     */
    void onReset();
}
