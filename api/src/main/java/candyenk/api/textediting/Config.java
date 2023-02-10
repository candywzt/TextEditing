package candyenk.api.textediting;

import android.graphics.drawable.Drawable;

/**
 * 插件的信息接口
 */
public interface Config {
    /**
     * 插件的唯一UUID
     * 十六位字符串(位数随便其实)
     * 版本:001
     */
    String getUuid();

    /**
     * 插件的图标
     * 版本:001
     */
    Drawable getIcon();

    /**
     * 插件的标题
     * 字符串
     * 版本:001
     */
    String getTitle();

    /**
     * 拆建的描述
     * 字符串
     * 版本:001
     */
    String getDescribe();

    /**
     * 插件的版本
     * int数值
     * 版本:001
     */
    int getVersion();

    /**
     * 插件的创建时间
     * long毫秒时间戳
     * 版本:001
     */
    long getCreateTime();

    /**
     * 插件的最后更新时间
     * long毫秒时间戳
     * 版本:001
     */
    long getUpdateTime();

    /**
     * 插件的作者
     * 字符串
     * 版本:001
     */
    String getAuthor();

    /**
     * 插件的作者提供的URL
     * URL字符串
     * 版本:001
     */
    String getAuthorUrl();

    /**
     * 插件的更新URL
     * URL字符串
     * 版本:001
     */
    String getUpdateUrl();

    /**
     * Java插件的入口类
     * 类名字符串
     * 版本:001
     */
    String getMainClass();

    /**
     * Lua插件的入口文件路径
     * 相对文件路径字符串
     * 版本:001
     */
    String getMainLua();

    /**
     * 插件支持的API版本
     * 版本:001
     */
    int getAPI();
}
