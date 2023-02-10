package candyenk.api.textediting;

import android.content.Context;

/**
 * 插件API操作接口
 */
public interface API {
    /**
     * 获取API版本
     * 版本:001
     */
    int getAPIVersion();

    /**
     * 获取输入框
     * 版本:001
     */
    Edit getInput();

    /**
     * 获取输出框
     * 版本:001
     */
    Edit getOutPut();

    /**
     * 获取插件配置
     * 版本:001
     */
    Config getConfig();

    /**
     * 获取日志工具
     * 版本:001
     */
    Log getLog();

    /**
     * 获取插件操作面板
     * 版本:001
     */
    Panel getPanel();

    /**
     * 获取插件持久存储
     * 版本:001
     */
    Setting getSetting();

    /**
     * 获取插件工具集
     * 版本:001
     */
    Tool getTool();

    /**
     * 获取Context
     * 可用来创建View获取R资源等
     * 版本:001
     */
    Context getContext();

    /**
     * 结束插件,释放资源
     * 无面板插件手动调用该方法释放资源
     * 特别是Lua插件,避免内存泄漏
     * 版本:001
     */
    void close();
}
